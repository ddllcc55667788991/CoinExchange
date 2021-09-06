package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.EntrustOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kenji.domain.ExchangeTrade;
import com.kenji.param.OrderParam;
import com.kenji.vo.TradeEntrustOrderVo;

/**
 * @Author Kenji
 * @Date 2021/8/24 22:23
 * @Description
 */
public interface EntrustOrderService extends IService<EntrustOrder> {


    /**
     * 分页查询委托记录
     * @param page  分页数据
     * @param symbol 交易对
     * @param type 买卖类型
     * @return
     */
    Page<EntrustOrder> findByPage(Page<EntrustOrder> page, Long userId, Byte type, String symbol);

    /**
     * 查询历史委托单记录
     * @param page
     * @param symbol
     * @return
     */
    Page<TradeEntrustOrderVo> getHistoryEntrustOrder(Page<EntrustOrder> page, Long userId, String symbol);

    /**
     * 查询未委托单记录
     * @param page
     * @param symbol
     * @return
     */
    Page<TradeEntrustOrderVo> getEntrustOrder(Page<EntrustOrder> page, Long userId, String symbol);

    /**
     * 委托单的下单操作
     * @param orderParam
     * @return
     */
    Boolean createEntrustOrder(Long userId, OrderParam orderParam);

    /**
     * 更新委托单的数据
     * @param exchangeTrade
     */
    void doMatch(ExchangeTrade exchangeTrade);

    /**
     * 取消委托单
     * @param entrustOrderId
     * @return
     */
    void cancelEntrustOrder(Long entrustOrderId);


    /**
     * 取消订单
     * @param orderId
     */
    void cancelEntrustOrderDb(String orderId);
}
