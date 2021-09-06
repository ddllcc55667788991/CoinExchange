package com.kenji.rocket;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @Author Kenji
 * @Date 2021/8/26 19:34
 * @Description
 */
public interface Source {

    @Output("order_out")
    MessageChannel outputMessage();

}
