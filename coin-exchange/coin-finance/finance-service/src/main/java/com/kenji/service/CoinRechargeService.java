package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.CoinRecharge;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:19
 * @Description
 */
public interface CoinRechargeService extends IService<CoinRecharge> {

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
    Page<CoinRecharge> findByPage(Page<CoinRecharge> page, Long coinId, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime);

    /**
     * 充币记录分页查询
     * @param page  分页数据
     * @param coinId 币种id
     * @return
     */
    Page<CoinRecharge> findRecordByPage(Page<CoinRecharge> page, Long userId, Long coinId);

}
