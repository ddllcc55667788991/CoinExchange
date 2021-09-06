package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.CashRecharge;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kenji.domain.CoinRecharge;
import com.kenji.model.CashParam;
import com.kenji.vo.CashTradeVo;

import java.math.BigDecimal;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:19
 * @Description
 */
public interface CashRechargeService extends IService<CashRecharge> {


    /**
     * 条件分页查询GCN充值记录
     * @param page 分页数据
     * @param coinId 币种coinId
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
    Page<CashRecharge> findRecordsByPage(Page<CashRecharge> page, Long coinId, Long userId, String userName, String mobile, Byte status, String numMin, String numMax, String startTime, String endTime);


    /**
     * 场外交易充值审核
     * @param id 审核记录ID
     * @param remark 审核备注
     * @param status 状态
     * @param userId 用户ID
     * @return
     */
    Boolean updateStatus(Long userId,Long id, String remark, Byte status);

    /**
     * 查询买入记录
     * @param useid 用户ID
     * @param page 分页数据
     * @param status 状态
     * @return
     */
    Page<CashRecharge> findByPage(Long useid, Page<CashRecharge> page,Byte status);

    /**
     * 买入GCN
     * @param cashParam GCN充值参数
     * @param userId 用户ID
     * @return
     */
    CashTradeVo buy(Long userId, CashParam cashParam);
}
