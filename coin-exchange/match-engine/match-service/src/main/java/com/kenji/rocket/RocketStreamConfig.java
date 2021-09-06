package com.kenji.rocket;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Kenji
 * @Date 2021/8/26 13:01
 * @Description 开启stream开发
 */
@Configuration
@EnableBinding({Sink.class,Source.class})
public class RocketStreamConfig {
}
