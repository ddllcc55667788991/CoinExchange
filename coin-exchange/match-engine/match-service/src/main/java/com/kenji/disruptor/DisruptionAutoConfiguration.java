package com.kenji.disruptor;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import net.openhft.affinity.AffinityThreadFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadFactory;

/**
 * @Author Kenji
 * @Date 2021/8/26 11:29
 * @Description
 */
@Configuration
@EnableConfigurationProperties(value = DisruptorProperties.class)
public class DisruptionAutoConfiguration {

    public DisruptorProperties disruptorProperties;

    public DisruptionAutoConfiguration(DisruptorProperties disruptorProperties) {
        this.disruptorProperties = disruptorProperties;
    }

    @Bean
    public EventFactory<OrderEvent> eventEventFactory() {
        EventFactory<OrderEvent> orderEventEventFactory = new EventFactory<OrderEvent>() {
            @Override
            public OrderEvent newInstance() {
                return new OrderEvent();
            }
        };
        return orderEventEventFactory;
    }

    @Bean
    public ThreadFactory threadFactory() {
        return new AffinityThreadFactory("Match-Handler:");
    }

    /**
     * 无锁搞笑等待策略
     *
     * @return
     */
    @Bean
    public WaitStrategy waitStrategy() {
        return new YieldingWaitStrategy();
    }


    /**
     * 创建一个RingBuffer
     *
     * @param eventFactory  事件工厂
     * @param threadFactory 执行者（消费者）的线程创建
     * @param waitStrategy  等待策略： 当RingBuffer没有数据时，如何等待
     * @param eventHandlers 事件处理
     * @return
     */
    @Bean
    public RingBuffer<OrderEvent> ringBuffer(
            EventFactory<OrderEvent> eventFactory,
            ThreadFactory threadFactory,
            WaitStrategy waitStrategy,
            EventHandler<OrderEvent>[] eventHandlers
    ) {
        /**
         * 构建disruptor
         */
        Disruptor<OrderEvent> disruptor = null;

        ProducerType producerType = ProducerType.SINGLE;

        if (disruptorProperties.getMultiProducer()) {
            producerType = ProducerType.MULTI;
        }

        disruptor = new Disruptor<OrderEvent>(
                eventFactory, disruptorProperties.getRingBufferSize(),
                threadFactory, producerType, waitStrategy);

        disruptor.setDefaultExceptionHandler(new DisruptorHandlerException());  //设置异常处理器

        //设置消费者，每个消费者代表一个交易对，有多少个交易对，就有多少eventHandlers,事件来了后，多个eventHandlers是并发执行的
        disruptor.handleEventsWith(eventHandlers);
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        disruptor.start();  //开始监听
        final Disruptor<OrderEvent> disruptorShutdown = disruptor;

        //使用优雅停机
        Runtime.getRuntime().addShutdownHook(new Thread(
                ()->{
                    disruptorShutdown.shutdown();
                },"DisruptorShutdownThread"
        ));
        return ringBuffer;
    }

    /**
     * 创建DisruptorTemplate
     * @param ringBuffer
     * @return
     */
    @Bean
    public DisruptorTemplate disruptorTemplate(RingBuffer<OrderEvent> ringBuffer){
        return new DisruptorTemplate(ringBuffer);
    }
}
