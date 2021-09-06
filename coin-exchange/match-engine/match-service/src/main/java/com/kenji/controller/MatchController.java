package com.kenji.controller;

import com.kenji.enums.OrderDirection;
import com.kenji.disruptor.OrderEvent;
import com.kenji.disruptor.OrderMatchDisruptorHandler;
import com.kenji.domain.DepthItemVo;
import com.kenji.feign.OrderBookFeignClient;
import com.kenji.model.MergeOrder;
import com.kenji.model.OrderBooks;
import com.kenji.model.TradePlate;
import com.lmax.disruptor.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author Kenji
 * @Date 2021/8/26 18:58
 * @Description
 */
@RestController
public class MatchController implements OrderBookFeignClient {

    @Autowired
    private EventHandler<OrderEvent>[] eventHandler;

    @GetMapping("/match/order")
    public TreeMap<BigDecimal, MergeOrder> getOrderData(@RequestParam(required = true) String symbol,@RequestParam(required = true)Integer orderDirection){
        for (EventHandler<OrderEvent> eventHandler : eventHandler) {
            OrderMatchDisruptorHandler orderEventHandler = (OrderMatchDisruptorHandler)eventHandler;
            if (orderEventHandler.getSymbol().equals(symbol)){
                OrderBooks orderBooks = orderEventHandler.getOrderBooks();
                return orderBooks.getCurrentLimitPrices(OrderDirection.getOrderDirection(orderDirection));
            }
        }
        return null;
    }

    /**
     * 远程调用深度数据
     *
     * @param symbol
     * @return
     */
    @Override
    public Map<String, List<DepthItemVo>> getDepth(String symbol) {
        Map<String,List<DepthItemVo>> depths = new HashMap<>();
        for (EventHandler<OrderEvent> eventHandler : eventHandler) {
            OrderMatchDisruptorHandler orderEventHandler = (OrderMatchDisruptorHandler)eventHandler;
            //找到对应的深度数据
            if (orderEventHandler.getSymbol().equals(symbol)){
                OrderBooks orderBooks = orderEventHandler.getOrderBooks();
                TradePlate buyTradePlate = orderBooks.getBuyTradePlate();
                TradePlate sellTradePlate = orderBooks.getSellTradePlate();
                depths.put("bids",buyTradePlate.getItems());
                depths.put("asks",sellTradePlate.getItems());
                return depths;
            }
        }
        return null;
    }
}
