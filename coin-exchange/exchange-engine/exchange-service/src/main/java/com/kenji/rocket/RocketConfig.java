package com.kenji.rocket;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Kenji
 * @Date 2021/8/26 19:35
 * @Description
 */
@Configuration
@EnableBinding(value = {Source.class,Sink.class})
public class RocketConfig {
}
