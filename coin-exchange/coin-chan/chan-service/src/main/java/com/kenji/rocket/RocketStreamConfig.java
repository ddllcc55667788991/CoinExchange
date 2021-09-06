package com.kenji.rocket;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Kenji
 * @Date 2021/8/27 22:28
 * @Description
 */
@Configuration
@EnableBinding(Sink.class)
public class RocketStreamConfig {
}
