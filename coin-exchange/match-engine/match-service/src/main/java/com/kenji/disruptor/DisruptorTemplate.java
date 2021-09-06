package com.kenji.disruptor;

import com.kenji.model.Order;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import org.springframework.stereotype.Component;

/**
 * @Author Kenji
 * @Date 2021/8/26 11:19
 * @Description 在 boot 里面使用它发送消息
 */
public class DisruptorTemplate {
    private static final EventTranslatorOneArg<OrderEvent, Order> TRANSLATOR = new EventTranslatorOneArg<OrderEvent, Order>() {

        public void translateTo(OrderEvent event, long sequence, Order input) {
            event.setSource(input);
        }

    };

    private final RingBuffer<OrderEvent> ringBuffer;

    public DisruptorTemplate(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(Order input) {
        ringBuffer.publishEvent(TRANSLATOR, input);
    }
}
