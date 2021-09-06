package com.kenji.rocket;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;

/**
 * @Author Kenji
 * @Date 2021/8/26 12:53
 * @Description
 */
public interface Sink {

    @Input("order_in")
    public MessageChannel messageChannel();


}
