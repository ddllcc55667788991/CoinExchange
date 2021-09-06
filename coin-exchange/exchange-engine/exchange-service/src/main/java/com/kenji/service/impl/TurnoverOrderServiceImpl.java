package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.TurnoverOrder;
import com.kenji.mapper.TurnoverOrderMapper;
import com.kenji.service.TurnoverOrderService;

/**
 * @Author Kenji
 * @Date 2021/8/24 22:23
 * @Description
 */
@Service
public class TurnoverOrderServiceImpl extends ServiceImpl<TurnoverOrderMapper, TurnoverOrder> implements TurnoverOrderService {

    /**
     * 分页查询成交记录
     *
     * @param page   分页数据
     * @param symbol 交易对
     * @param type   交易类型
     * @param userId 用户id
     * @return
     */
    @Override
    public Page<TurnoverOrder> findByPage(Long userId, Page<TurnoverOrder> page, String symbol, Byte type) {
        return null;
    }

    /**
     * 获取买入的订单交易记录
     *
     * @param id
     * @param userId
     * @return
     */
    @Override
    public List<TurnoverOrder> getBuyTurnoverOrder(Long id, Long userId) {
        return list(new LambdaQueryWrapper<TurnoverOrder>()
                .eq(TurnoverOrder::getOrderId, id)
                .eq(TurnoverOrder::getBuyUserId, userId));
    }

    /**
     * 获取卖出的订单交易记录
     *
     * @param id
     * @param userId
     * @return
     */
    @Override
    public List<TurnoverOrder> getSellTurnoverOrder(Long id, Long userId) {
        return list(new LambdaQueryWrapper<TurnoverOrder>()
                .eq(TurnoverOrder::getOrderId, id)
                .eq(TurnoverOrder::getSellUserId, userId));
    }

    /**
     * 成交数据的查询
     *
     * @param symbol
     * @return
     */
    @Override
    public List<TurnoverOrder> gerTurnoverOrderBySymbol(String symbol) {
        return list(new LambdaQueryWrapper<TurnoverOrder>()
                .eq(!StringUtils.isEmpty(symbol), TurnoverOrder::getSymbol, symbol)
                .orderByDesc(TurnoverOrder::getCreated)
                .eq(TurnoverOrder::getStatus, 1)
                .last("limit 60"));
    }
}
