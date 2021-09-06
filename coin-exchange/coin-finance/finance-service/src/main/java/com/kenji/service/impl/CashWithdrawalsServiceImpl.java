package com.kenji.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.Account;
import com.kenji.domain.CashRecharge;
import com.kenji.domain.Config;
import com.kenji.dto.UserBankDto;
import com.kenji.dto.UserDto;
import com.kenji.feign.UserBankServiceFeign;
import com.kenji.feign.UserServiceFeign;
import com.kenji.model.CashSellParam;
import com.kenji.service.AccountService;
import com.kenji.service.ConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.CashWithdrawalsMapper;
import com.kenji.domain.CashWithdrawals;
import com.kenji.service.CashWithdrawalsService;
import org.springframework.util.CollectionUtils;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:19
 * @Description
 */
@Service
public class CashWithdrawalsServiceImpl extends ServiceImpl<CashWithdrawalsMapper, CashWithdrawals> implements CashWithdrawalsService {

    @Autowired
    private UserServiceFeign userServiceFeign;

    @Autowired
    private ConfigService configService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserBankServiceFeign userBankServiceFeign;

    /**
     * 条件分页查询GCN提现记录
     *
     * @param page      分页数据
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
    public Page<CashWithdrawals> findByPage(Page<CashWithdrawals> page, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime) {
        LambdaQueryWrapper<CashWithdrawals> queryWrapper = new LambdaQueryWrapper<>();
        Map<Long, UserDto> userDtoMap = null;
        //有用用户信息查询
        if (userId != null || userName != null || mobile != null) {
            //远程调用查询用户信息
            userDtoMap = userServiceFeign.getBasicUsers(userId == null ? null : Arrays.asList(userId), userName, mobile);
            if (CollectionUtils.isEmpty(userDtoMap)) {
                return page;
            }
            Set<Long> ids = userDtoMap.keySet();
            queryWrapper.in(!CollectionUtils.isEmpty(ids), CashWithdrawals::getUserId, ids);
        }
        //没有用用户信息查询
        queryWrapper
                .eq(status != null, CashWithdrawals::getStatus, status)
                .between(!(StringUtils.isEmpty(numMin) || StringUtils.isEmpty(numMax)), CashWithdrawals::getMum,
                        new BigDecimal(StringUtils.isEmpty(numMin) ? "0" : numMin), new BigDecimal(StringUtils.isEmpty(numMax) ? "0" : numMax))
                .between(!(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)), CashWithdrawals::getCreated, startTime, endTime + " 23:59:59");
        Page<CashWithdrawals> cashWithdrawalsPage = super.page(page, queryWrapper);
        List<CashWithdrawals> records = cashWithdrawalsPage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<Long> userIds = records.stream().map(CashWithdrawals::getUserId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userDtoMap)) { //没有用用户信息查询
                userDtoMap = userServiceFeign.getBasicUsers(userIds, null, null);
            }
            Map<Long, UserDto> finalBasicUsers = userDtoMap;
            records.forEach(cashWithdrawals -> {
                        UserDto userDto = finalBasicUsers.get(cashWithdrawals.getUserId());
                        if (userDto != null) {
                            cashWithdrawals.setRealName(userDto.getRealName());
                            cashWithdrawals.setUsername(userDto.getUsername());
                        }
                    }
            );
        }
        return cashWithdrawalsPage;

    }

    /**
     * 查询卖出记录
     *
     * @param useid
     * @param page   分页数据
     * @param status 状态
     * @return
     */
    @Override
    public Page<CashWithdrawals> findRecordsByPage(Long useid, Page<CashWithdrawals> page, Byte status) {
        return page(page, new LambdaQueryWrapper<CashWithdrawals>()
                .eq(CashWithdrawals::getUserId, useid)
                .eq(status != null, CashWithdrawals::getStatus, status));
    }

    /**
     * 用户提现
     *
     * @param userId
     * @param cashSellParam 提现参数的json数据
     * @return
     */
    @Override
    public Boolean sell(Long userId, CashSellParam cashSellParam) {
        Map<Long, UserDto> basicUsers = userServiceFeign.getBasicUsers(Arrays.asList(userId), null, null);
        if (CollectionUtils.isEmpty(basicUsers)) {
            throw new IllegalArgumentException("用户id不正确");
        }
        UserDto userDto = basicUsers.get(userId);
        //提现数据校验
        checkSellParam(cashSellParam);
        //手机验证码校验
//        checkValidateCode(cashSellParam, userDto);
        //支付密码校验
        checkPayPassword(userDto.getPaypassword(), cashSellParam.getPayPassword());
        //提现操作->增加流水，减少资产
        //获取账户ID
        Account account = accountService.findUserAndCoin(userId, "GCN");
        //计算本次交易的总金额
        BigDecimal amount = getCashWithdrawsAmount(cashSellParam);
        //计算本次的手续费
        BigDecimal fee = getCashWithdrawsFee(amount);
        //根据userid获取userbank信息
        UserBankDto userBankInfo = userBankServiceFeign.getUserBankInfo(userId);
        if (userBankInfo == null) {
            throw new IllegalArgumentException("用户无银行卡信息");
        }
        //创建订单
        CashWithdrawals cashWithdrawals = new CashWithdrawals();
        cashWithdrawals.setUserId(userId);
        cashWithdrawals.setCoinId(cashSellParam.getCoinId());
        cashWithdrawals.setAccountId(account.getId());
        cashWithdrawals.setNum(cashSellParam.getNum());
        cashWithdrawals.setFee(fee);
        cashWithdrawals.setMum(amount);
        cashWithdrawals.setBank(userBankInfo.getBank());
        cashWithdrawals.setBankProv(userBankInfo.getBankProv());
        cashWithdrawals.setBankCity(userBankInfo.getBankCity());
        cashWithdrawals.setBankAddr(userBankInfo.getBankAddr());
        cashWithdrawals.setBankCard(userBankInfo.getBankCard());
        String remark = RandomUtil.randomNumbers(6);
        cashWithdrawals.setRemark(remark);
        cashWithdrawals.setStep((byte) 1);
        cashWithdrawals.setStatus((byte) 0);
        cashWithdrawals.setTruename(userBankInfo.getRealName());
        boolean save = save(cashWithdrawals);
        if (save) {
            //扣减资产 account -> accountDetail
            accountService.lockUserAmount(userId, cashWithdrawals.getCoinId(), cashWithdrawals.getMum(),
                    "withdrawals_out", cashWithdrawals.getId(), cashWithdrawals.getFee());
        }
        return save;
    }

    /**
     * 计算本次交易的总金额
     *
     * @param cashSellParam
     * @return
     */
    private BigDecimal getCashWithdrawsAmount(CashSellParam cashSellParam) {
        @NotNull BigDecimal num = cashSellParam.getNum();
        Config config = configService.getConfigByCode("USDT2CNY");
        return new BigDecimal(config.getValue()).multiply(num).setScale(2, RoundingMode.HALF_UP);

    }

    /**
     * 计算本次的手续费
     *
     * @param amount
     * @return
     */
    private BigDecimal getCashWithdrawsFee(BigDecimal amount) {
        //手续费率
        Config withdrawPoundageRate = configService.getConfigByCode("WITHDRAW_POUNDAGE_RATE");
        BigDecimal withdrawPoundage = amount.multiply(new BigDecimal(withdrawPoundageRate.getValue())).setScale(2, RoundingMode.HALF_UP);
        Config withdrawMinPoundage = configService.getConfigByCode("WITHDRAW_MIN_POUNDAGE");
        BigDecimal minPoundage = new BigDecimal(withdrawMinPoundage.getValue());
        if (minPoundage.compareTo(withdrawPoundage) < 0) {
            return withdrawPoundage;
        }
        return minPoundage;
    }


    /**
     * 支付密码校验
     *
     * @param paypasswordDB
     * @param payPassword
     */
    private void checkPayPassword(String paypasswordDB, String payPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(payPassword, paypasswordDB)) {
            throw new IllegalArgumentException("支付密码不正确");
        }
    }

    /**
     * 手机验证码校验
     *
     * @param cashSellParam
     */
    private void checkValidateCode(CashSellParam cashSellParam, UserDto userDto) {
        String validateword = stringRedisTemplate.opsForValue().get("SMS:CASH_WITHDRAWS:" + userDto.getMobile());
        if (!validateword.equals(cashSellParam.getValidateCode())) {
            throw new IllegalArgumentException("手机验证码不正确");
        }
    }

    /**
     * 提现数据校验
     *
     * @param cashSellParam
     */
    private void checkSellParam(CashSellParam cashSellParam) {
        Config withdrawStatus = configService.getConfigByCode("WITHDRAW_STATUS");
        if (Integer.valueOf(withdrawStatus.getValue()) == 0) {
            throw new IllegalArgumentException("用户暂未开启提现功能");
        }
        //小于最大提取额度 WITHDRAW_MAX_AMOUNT  大于最小提取额度 WITHDRAW_MIN_AMOUNT
        Config withdrawMaxAmount = configService.getConfigByCode("WITHDRAW_MAX_AMOUNT");
        @NotNull BigDecimal num = cashSellParam.getNum();
        if (num.compareTo(new BigDecimal(withdrawMaxAmount.getValue())) > 0) {
            throw new IllegalArgumentException("不能大于最大提取额度");
        }
        //大于最小提取额度 WITHDRAW_MIN_AMOUNT
        Config withdrawMinAmount = configService.getConfigByCode("WITHDRAW_MIN_AMOUNT");
        if (num.compareTo(new BigDecimal(withdrawMinAmount.getValue())) < 0) {
            throw new IllegalArgumentException("不能小于最小提取额度");
        }
    }
}
