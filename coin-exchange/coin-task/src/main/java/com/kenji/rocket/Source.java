package com.kenji.rocket;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @Author Kenji
 * @Date 2021/8/28 0:13
 * @Description
 */
public interface Source {

    /**
     * 向指定的output里输出数据
     * @return
     */
    @Output("subscribe_event_out")
    MessageChannel subscribeEventOutput();
}
