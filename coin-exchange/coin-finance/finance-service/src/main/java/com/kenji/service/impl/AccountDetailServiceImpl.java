package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.CoinWithdraw;
import com.kenji.dto.UserDto;
import com.kenji.feign.UserServiceFeign;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
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
import com.kenji.domain.AccountDetail;
import com.kenji.mapper.AccountDetailMapper;
import com.kenji.service.AccountDetailService;
import org.springframework.util.CollectionUtils;

/**
 * @Author  Kenji
 * @Date  2021/8/23 0:19
 * @Description 
 */
@Service
public class AccountDetailServiceImpl extends ServiceImpl<AccountDetailMapper, AccountDetail> implements AccountDetailService{

    @Autowired
    private UserServiceFeign userServiceFeign;

    /**
     * 条件分页查询流水记录
     *
     * @param page        分页数据
     * @param coinId      币种coinId
     * @param userId      用户ID
     * @param userName    用户名
     * @param mobile      手机号码
     * @param accountId   账户ID
     * @param amountStart 充值最小金额
     * @param amountEnd   充值最大金额
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return
     */
    @Override
    public Page<AccountDetail> findByPage(Page<AccountDetail> page, Long coinId, Long userId, String userName, String mobile, Byte status, Long accountId, String amountStart, String amountEnd, String startTime, String endTime) {
        LambdaQueryWrapper<AccountDetail> queryWrapper = new LambdaQueryWrapper<>();
        Map<Long, UserDto> userDtoMap = null;
        //有用用户信息查询
        if (userId != null || userName != null || mobile != null) {
            //远程调用查询用户信息
            userDtoMap = userServiceFeign.getBasicUsers(userId == null ? null : Arrays.asList(userId), userName, mobile);
            if (CollectionUtils.isEmpty(userDtoMap)) {
                return page;
            }
            Set<Long> ids = userDtoMap.keySet();
            queryWrapper.in(!CollectionUtils.isEmpty(ids), AccountDetail::getUserId, ids);
        }
        //没有用用户信息查询
        queryWrapper
                .eq(coinId != null, AccountDetail::getCoinId, coinId)
                .eq(accountId != null, AccountDetail::getAccountId, accountId)
                .between(!(StringUtils.isEmpty(amountStart) || StringUtils.isEmpty(amountEnd)), AccountDetail::getAmount,
                        new BigDecimal(StringUtils.isEmpty(amountStart) ? "0" : amountEnd), new BigDecimal(StringUtils.isEmpty(amountEnd) ? "0" : amountEnd))
                .between(!(StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)), AccountDetail::getCreated, startTime, endTime + " 23:59:59");
        Page<AccountDetail> accountDetailPage = super.page(page, queryWrapper);
        List<AccountDetail> records = accountDetailPage.getRecords();
        if (!CollectionUtils.isEmpty(records)) {
            List<Long> userIds = records.stream().map(AccountDetail::getUserId).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(userDtoMap)) { //没有用用户信息查询
                userDtoMap = userServiceFeign.getBasicUsers(userIds, null, null);
            }
            Map<Long, UserDto> finalBasicUsers = userDtoMap;
            records.forEach(accountDetail -> {
                        UserDto userDto = finalBasicUsers.get(accountDetail.getUserId());
                        if (userDto!=null){
                            accountDetail.setRealName(userDto.getRealName());
                            accountDetail.setUsername(userDto.getUsername());
                        }
                    }
            );
        }
        return accountDetailPage;
    }
}
