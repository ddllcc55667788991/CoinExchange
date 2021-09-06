package com.kenji.disruptor;

import com.alibaba.fastjson.JSON;
import com.kenji.match.MatchStrategy;
import com.kenji.match.MatchStrategyFactory;
import com.kenji.model.Order;
import com.kenji.model.OrderBooks;
import com.lmax.disruptor.EventHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author Kenji
 * @Date 2021/8/26 12:21
 * @Description
 */
@Slf4j
@Data
public class OrderMatchDisruptorHandler implements EventHandler<OrderEvent> {

    private OrderBooks orderBooks;

    private String symbol;

    public OrderMatchDisruptorHandler(OrderBooks orderBooks) {
        this.orderBooks = orderBooks;
        this.symbol=orderBooks.getSymbol();
    }

    /**
     * 交给引擎处理事件
     * @param orderEvent
     * @param sequence
     * @param endOfBatch
     * @throws Exception
     */
    @Override
    public void onEvent(OrderEvent orderEvent, long sequence, boolean endOfBatch) throws Exception {
        //从RingBuffer里面接收某个数据
        Order order = (Order) orderEvent.getSource();
        if (!order.getSymbol().equals(symbol)){     //接收到一个不应该处理的数据，就不处理
            return;
        }
        log.info("=====> 开始接受委托单{} <======", JSON.toJSONString(orderEvent));
        try {
            MatchStrategyFactory.getMatchService(MatchStrategy.LIMIT_PRICE).match(orderBooks,order);
        }catch (Exception e){
            log.error(String.format("处理委托单: %s 发生了错误",JSON.toJSON(orderEvent),e));
        }finally {

        }
    }
}
