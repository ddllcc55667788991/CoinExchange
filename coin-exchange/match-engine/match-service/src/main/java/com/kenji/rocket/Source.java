package com.kenji.rocket;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;

/**
 * @Author Kenji
 * @Date 2021/8/27 9:23
 * @Description
 */
public interface Source {

    /**
     * 发送盘口数据，供以后前端的数据更新
     *
     */
    @Output("trade_plate_out")
    MessageChannel plateOut();

    /**
     * 订单的完成
     *
     */
    @Output("complete_order_out")
    MessageChannel completedOrdersOut();

    /**
     * 处理订单的记录
     *
     */
    @Output("exchange_trades_out")
    MessageChannel exchangeTradesOut();

    /**
     * 取消订单
     * @return
     */
//    @Output("cancel_order_out")
//    MessageChannel cancelOrderOut();
}
