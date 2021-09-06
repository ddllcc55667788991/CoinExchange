package com.kenji.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.RandomUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.*;
import com.kenji.dto.AdminBankDto;
import com.kenji.dto.UserDto;
import com.kenji.feign.AdminServiceFeign;
import com.kenji.feign.UserServiceFeign;
import com.kenji.model.CashParam;
import com.kenji.service.*;
import com.kenji.vo.CashTradeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.CashRechargeMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotNull;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:19
 * @Description
 */
@Service
public class CashRechargeServiceImpl extends ServiceImpl<CashRechargeMapper, CashRecharge> implements CashRechargeService {

    @Autowired
    private UserServiceFeign userServiceFeign;

    @Autowired
    private CashRechargeAuditRecordService cashRechargeAuditRecordService;

    @Autowired
    private AccountService accountService;


    @CreateCache(name = "CASH_RECHARGE_LOCK", expire = 100, timeUnit = TimeUnit.SECONDS, cacheType = CacheType.BOTH)
    private Cache<String, String> cache;

    @Autowired
    private AdminServiceFeign adminServiceFeign;

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private CoinService coinService;

    @Autowired
    private ConfigService configService;

    /**
     * 条件分页查询GCN充值记录
     *
     * @param page      分页数据
     * @param coinId    币种coinId
     * @param userId    用户ID
     * @param userName  用户名
     * @param mobile    手机号码
     * @param status    状态
     * @param numMin    充值最小金额
     * @param numMax    充值最大金额
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @Override
    public Page<CashRecharge> findRecordsByPage(Page<CashRecharge> page, Long coinId, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime) {
        LambdaQueryWrapper<CashRecharge> queryWrapper = new LambdaQueryWrapper<>();
        Map<Long, UserDto> userDtoMap = null;
        //有用用户信息查询
        if (userId != null || userName != null || mobile != null) {
            //远程调用查询用户信息
            userDtoMap = userServiceFeign.getBasicUsers(userId == null ? null : Arrays.asList(userId), userName, mobile);
            if (CollectionUtils.isEmpty(userDtoMap)) {
                return page;
            }
            Set<Long> ids = userDtoMap.keySet();
            queryWrapper.in(!CollectionUtils.isEmpty(ids), CashRecharge::getUserId, ids);
        }
        //没有用用户信息查询
        queryWrapper
                .eq(coinId != null, CashRecharge::getCoinId, coinId)
                .eq(status != null, CashRecharge::getStatus, status)
                .between(!(StringUtils.isEmpty(numMin) || StringUtils.isEmpty(numMax)), CashRecharge::getMum,
                        new BigDecimal(StringUtils.isEmpty(numMin) ? "0" : numMin), new BigDecimal(StringUtils.isEmpty(numMax) ? "0" : numMax))
                .between(!(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)), CashRecharge::getCreated, startTime, endTime + " 23:59:59");
        Page<CashRecharge> cashRechargePage = super.page(page, queryWrapper);
        List<CashRecharge> records = cashRechargePage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<Long> userIds = records.stream().map(CashRecharge::getUserId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userDtoMap)) { //没有用用户信息查询
                userDtoMap = userServiceFeign.getBasicUsers(userIds, null, null);
            }
            Map<Long, UserDto> finalBasicUsers = userDtoMap;
            records.forEach(cashRecharge -> {
                        UserDto userDto = finalBasicUsers.get(cashRecharge.getUserId());
                        if (userDto != null) {
                            cashRecharge.setRealName(userDto.getRealName());
                            cashRecharge.setUsername(userDto.getUsername());
                        }
                    }
            );
        }
        return cashRechargePage;
    }

    /**
     * 场外交易充值审核
     *
     * @param userId 用户ID
     * @param id     审核记录ID
     * @param remark 审核备注
     * @param status 状态
     * @return
     */
    @Override
    @Transactional
    public Boolean updateStatus(Long userId, Long id, String remark, Byte status) {
        //一个员工审核时，另一个员工不能审核
        boolean tryLockAndRun = cache.tryLockAndRun("" + id, 300, TimeUnit.SECONDS, () -> {
            CashRecharge cashRecharge = getById(id);
            if (cashRecharge == null) {
                throw new IllegalArgumentException("充值记录不存在");
            }
            if (cashRecharge.getStatus() == 1) {
                throw new IllegalArgumentException("充值记录审核已通过");
            }
            CashRechargeAuditRecord cashRechargeAuditRecord = new CashRechargeAuditRecord();
            cashRechargeAuditRecord.setAuditUserId(userId);
            cashRechargeAuditRecord.setStatus(status);
            cashRechargeAuditRecord.setRemark(remark);
            Integer step = cashRecharge.getStep() + 1;
            cashRechargeAuditRecord.setStep(step.byteValue());
            cashRechargeAuditRecord.setCreated(new Date());
            cashRechargeAuditRecord.setOrderId(id);
            cashRechargeAuditRecordService.save(cashRechargeAuditRecord);
            cashRecharge.setAuditRemark(remark);
            cashRecharge.setStatus(status);
            cashRecharge.setStep(step.byteValue());
            if (status == 2) {  //审核拒绝
                super.updateById(cashRecharge);
            } else { //审核通过，给用户账户充值
                Boolean isOk = accountService.transferAccountAmount(userId, cashRecharge, id, "充值", "recharge_into", (byte) 1);
                if (isOk) {
                    cashRecharge.setLastTime(new Date());
                    updateById(cashRecharge);
                }
            }
        });
        return tryLockAndRun;
    }

    /**
     * 查询买入记录
     *
     * @param useid  用户ID
     * @param page   分页数据
     * @param status 状态
     * @return
     */
    @Override
    public Page<CashRecharge> findByPage(Long useid, Page<CashRecharge> page, Byte status) {
        return page(page, new LambdaQueryWrapper<CashRecharge>()
                .eq(CashRecharge::getUserId, useid)
                .eq(status!=null,CashRecharge::getStatus,status));
    }

    /**
     * 买入GCN
     *
     * @param userId    用户ID
     * @param cashParam GCN充值参数
     * @return
     */
    @Override
    @Transactional
    public CashTradeVo buy(Long userId, CashParam cashParam) {
        //买入数据检查
        Boolean isOk = checkCashParam(cashParam);
        if (isOk){
            //查询银行卡,loadbalance一张银行卡
            List<AdminBankDto> adminBankDtoList = adminServiceFeign.findAdminBank();
            AdminBankDto adminBankDto =loadBanlance(adminBankDtoList);
            //生成订单号
            long orderNo = snowflake.nextId();
            String remark = RandomUtil.randomNumbers(6);
            //获取购买比率
            Config config = configService.getConfigByCode("CNY2USDT");
            BigDecimal realMum = cashParam.getNum().multiply(new BigDecimal(config.getValue())).setScale(2, RoundingMode.HALF_UP);
            //插入充值数据
            Coin coin = coinService.getById(cashParam.getCoinId());
            if (coin==null){
                throw new IllegalArgumentException("coinId不存在");
            }
            CashRecharge cashRecharge = new CashRecharge();
            cashRecharge.setUserId(userId);
            cashRecharge.setCoinId(cashParam.getCoinId());
            cashRecharge.setCoinName(coin.getName());
            cashRecharge.setNum(cashParam.getNum());
            cashRecharge.setFee(BigDecimal.ZERO);
            cashRecharge.setMum(realMum);
            cashRecharge.setType("linepay");
            cashRecharge.setTradeno(String.valueOf(orderNo));
            cashRecharge.setStep((byte)1);
            cashRecharge.setStatus((byte)0);
            cashRecharge.setRemark(remark);
            //银行卡信息
            cashRecharge.setName(adminBankDto.getName());
            cashRecharge.setBankName(adminBankDto.getBankName());
            cashRecharge.setBankCard(adminBankDto.getBankCard());
            boolean save = save(cashRecharge);
            if (save){
                //返回值
                CashTradeVo cashTradeVo = new CashTradeVo();
                cashTradeVo.setAmount(realMum);
                cashTradeVo.setBankCard(adminBankDto.getBankCard());
                cashTradeVo.setBankName(adminBankDto.getBankName());
                cashTradeVo.setName(adminBankDto.getName());
                cashTradeVo.setRemark(remark);
                cashTradeVo.setStatus((byte)0);
                return cashTradeVo;
            }
        }
        return null;
    }

    /**
     * 只用一张银行卡
     * @param adminBankDtoList
     * @return
     */
    private AdminBankDto loadBanlance(List<AdminBankDto> adminBankDtoList) {
        if (adminBankDtoList.size()==0){
            throw new IllegalArgumentException("没有可用信用卡");
        }
        if (adminBankDtoList.size()==1){
            return adminBankDtoList.get(0);
        }
        Random random = new Random();
        int randomSize = random.nextInt(adminBankDtoList.size());
        return adminBankDtoList.get(randomSize);

    }

    /**
     * 检查购买参数合法性
     * @param cashParam
     * @return
     */
    private Boolean checkCashParam(CashParam cashParam) {
        @NotNull BigDecimal num = cashParam.getNum();
        Config withdrawMinAmount = configService.getConfigByCode("WITHDRAW_MIN_AMOUNT");
        BigDecimal minNum = new BigDecimal(withdrawMinAmount.getValue());
        if (num.compareTo(minNum)<0){
            return false;
        }
        return true;
    }


}
