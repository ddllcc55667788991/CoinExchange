package com.kenji.disruptor;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author Kenji
 * @Date 2021/8/26 11:03
 * @Description
 */
@Data
@ConfigurationProperties(prefix = "spring.disrupter")
public class DisruptorProperties {

    //RingBuffer 缓冲区大小，默认1024*1024
    private Integer ringBufferSize = 1024 * 1024;

    /**
     * 是否为多生产者，如果是则通过RingBuffer.createMultiProducer创建一个多生产者的RingBuffer,
     * 否则通过RingBuffer.createSingleProducer创建一个单生产者的RingBuffer
     */
    private Boolean multiProducer = false;
}
