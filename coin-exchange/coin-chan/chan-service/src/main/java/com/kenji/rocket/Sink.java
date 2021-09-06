package com.kenji.rocket;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @Author Kenji
 * @Date 2021/8/27 22:22
 * @Description
 */
public interface Sink {

    @Input("tio_group")
   MessageChannel messageGroupChannel();
}
