package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.CoinRecharge;
import com.kenji.domain.CoinWithdraw;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:19
 * @Description
 */
public interface CoinWithdrawService extends IService<CoinWithdraw> {

    /**
     * 条件分页查询提币记录
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
    Page<CoinWithdraw> findByPage(Page<CoinWithdraw> page, Long coinId, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime);


    /**
     * 提币记录分页查询
     * @param page 分页数据
     * @param coinId 币种Id
     * @param userId 用户Id
     * @return
     */
    Page<CoinWithdraw> findRecordByPage(Page<CoinWithdraw> page, Long userId, Long coinId);
}
