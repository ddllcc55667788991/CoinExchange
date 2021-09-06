package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.CashRecharge;
import com.kenji.dto.UserDto;
import com.kenji.feign.UserServiceFeign;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.CoinRecharge;
import com.kenji.mapper.CoinRechargeMapper;
import com.kenji.service.CoinRechargeService;
import org.springframework.util.CollectionUtils;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:19
 * @Description
 */
@Service
public class CoinRechargeServiceImpl extends ServiceImpl<CoinRechargeMapper, CoinRecharge> implements CoinRechargeService {

    @Autowired
    private UserServiceFeign userServiceFeign;

    /**
     * 条件分页查询充币记录
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
    public Page<CoinRecharge> findByPage(Page<CoinRecharge> page, Long coinId, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime) {
        LambdaQueryWrapper<CoinRecharge> queryWrapper = new LambdaQueryWrapper<>();
        Map<Long, UserDto> userDtoMap = null;
        //有用用户信息查询
        if (userId != null || userName != null || mobile != null) {
            //远程调用查询用户信息
            userDtoMap = userServiceFeign.getBasicUsers(userId == null ? null : Arrays.asList(userId), userName, mobile);
            if (CollectionUtils.isEmpty(userDtoMap)) {
                return page;
            }
            Set<Long> ids = userDtoMap.keySet();
            queryWrapper.in(!CollectionUtils.isEmpty(ids), CoinRecharge::getUserId, ids);
        }
        //没有用用户信息查询
        queryWrapper
                .eq(coinId != null, CoinRecharge::getCoinId, coinId)
                .eq(status != null, CoinRecharge::getStatus, status)
                .between(!(StringUtils.isEmpty(numMin) || StringUtils.isEmpty(numMax)), CoinRecharge::getAmount,
                        new BigDecimal(StringUtils.isEmpty(numMin) ? "0" : numMin), new BigDecimal(StringUtils.isEmpty(numMax) ? "0" : numMax))
                .between(!(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)), CoinRecharge::getCreated, startTime, endTime + " 23:59:59");
        Page<CoinRecharge> coinRechargePage = super.page(page, queryWrapper);
        List<CoinRecharge> records = coinRechargePage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<Long> userIds = records.stream().map(CoinRecharge::getUserId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userDtoMap)) { //没有用用户信息查询
                userDtoMap = userServiceFeign.getBasicUsers(userIds, null, null);
            }
            Map<Long, UserDto> finalBasicUsers = userDtoMap;
            records.forEach(coinRecharge -> {
                        UserDto userDto = finalBasicUsers.get(coinRecharge.getUserId());
                        if (userDto != null) {
                            coinRecharge.setRealName(userDto.getRealName());
                            coinRecharge.setUsername(userDto.getUsername());
                        }
                    }
            );
        }
        return coinRechargePage;
    }

    /**
     * 充币记录分页查询
     *
     * @param page   分页数据
     * @param userId
     * @param coinId 币种id
     * @return
     */
    @Override
    public Page<CoinRecharge> findRecordByPage(Page<CoinRecharge> page, Long userId, Long coinId) {
        return page(page, new LambdaQueryWrapper<CoinRecharge>()
                .eq(CoinRecharge::getUserId, userId)
                .eq(coinId!=null,CoinRecharge::getCoinId,coinId));
    }
}
