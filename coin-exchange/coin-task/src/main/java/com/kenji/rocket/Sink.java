package com.kenji.rocket;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

/**
 * @Author Kenji
 * @Date 2021/8/28 10:51
 * @Description
 */
public interface Sink {

    /**
     * 接收撮合引擎里的交易记录
     * @return
     */
    @Input("exchange_trades_in")
    MessageChannel exchangeTradesIn();
}
