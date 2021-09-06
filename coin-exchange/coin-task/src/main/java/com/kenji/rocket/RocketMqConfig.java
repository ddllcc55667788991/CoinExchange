package com.kenji.rocket;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Kenji
 * @Date 2021/8/28 0:16
 * @Description 开启rocketmq
 */
@EnableBinding(value = {Source.class,Sink.class})
@Configuration
public class RocketMqConfig {
}
