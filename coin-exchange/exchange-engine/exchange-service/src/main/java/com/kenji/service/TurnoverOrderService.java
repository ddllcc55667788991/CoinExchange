package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.TurnoverOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/24 22:23
 * @Description
 */
public interface TurnoverOrderService extends IService<TurnoverOrder> {

    /**
     * 分页查询成交记录
     * @param page  分页数据
     * @param symbol  交易对
     * @param type 交易类型
     * @param userId 用户id
     * @return
     */
    Page<TurnoverOrder> findByPage(Long userId,Page<TurnoverOrder> page, String symbol, Byte type);

    /**
     * 获取买入的订单交易记录
     * @param id
     * @param userId
     * @return
     */
    List<TurnoverOrder> getBuyTurnoverOrder(Long id, Long userId);

    /**
     * 获取卖出的订单交易记录
     * @param id
     * @param userId
     * @return
     */
    List<TurnoverOrder> getSellTurnoverOrder(Long id, Long userId);

    /**
     * 成交数据的查询
     * @param symbol
     * @return
     */
    List<TurnoverOrder> gerTurnoverOrderBySymbol(String symbol);
}
