package com.kenji.rocket;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

/**
 * @Author Kenji
 * @Date 2021/8/27 14:02
 * @Description 数据接收
 */
public interface Sink {

    @Input("exchange_trades_out")
    MessageChannel exchangeTradeIn();


    /**
     * 取消订单
     * @return
     */
//    @Input("cancel_order_in")
//    MessageChannel cancelOrder();
}
