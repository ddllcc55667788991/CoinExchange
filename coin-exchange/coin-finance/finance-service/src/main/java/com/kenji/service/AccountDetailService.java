package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.AccountDetail;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:19
 * @Description
 */
public interface AccountDetailService extends IService<AccountDetail> {

    /**
     * 条件分页查询流水记录
     *
     * @param page      分页数据
     * @param coinId    币种coinId
     * @param userId    用户ID
     * @param userName  用户名
     * @param mobile    手机号码
     * @param status    状态
     * @param amountStart    充值最小金额
     * @param amountEnd    充值最大金额
     * @param accountId    账户ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    Page<AccountDetail> findByPage(Page<AccountDetail> page, Long coinId, Long userId, String userName, String mobile, Byte status, Long accountId, String amountStart, String amountEnd, String startTime, String endTime);

}
