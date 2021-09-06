package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.CashWithdrawals;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kenji.model.CashSellParam;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:19
 * @Description
 */
public interface CashWithdrawalsService extends IService<CashWithdrawals> {

    /**
     * 条件分页查询GCN提现记录
     * @param page 分页数据
     * @param userId 用户ID
     * @param userName 用户名
     * @param mobile 手机号码
     * @param status 状态
     * @param numMin 充值最小金额
     * @param numMax 充值最大金额
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    Page<CashWithdrawals> findByPage(Page<CashWithdrawals> page, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime);

    /**
     * 查询卖出记录
     * @param page 分页数据
     * @param status 状态
     * @return
     */
    Page<CashWithdrawals> findRecordsByPage(Long useid, Page<CashWithdrawals> page, Byte status);

    /**
     * 用户提现
     * @param cashSellParam 提现参数的json数据
     * @return
     */
    Boolean sell(Long userId, CashSellParam cashSellParam);

}
