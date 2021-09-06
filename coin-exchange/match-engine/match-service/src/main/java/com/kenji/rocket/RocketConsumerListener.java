package com.kenji.rocket;

import com.kenji.disruptor.DisruptorTemplate;
import com.kenji.domain.EntrustOrder;
import com.kenji.model.Order;
import com.kenji.utils.BeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

/**
 * @Author Kenji
 * @Date 2021/8/26 12:54
 * @Description
 */
@Service
@Slf4j
public class RocketConsumerListener {

    @Autowired
    private DisruptorTemplate disruptorTemplate;

    @StreamListener("order_in")
    public void handlerMessage(EntrustOrder entrustOrder){
        Order order = null;
//        if (entrustOrder.getStatus() ==2){  //取消该单
//            order = new Order();
//            order.setOrderId(entrustOrder.getId().toString());
//            order.setCancelOrder(true);
//        }else {
            order = BeanUtils.entrustOrder2Order(entrustOrder);
//        }
        log.info("接受到委托订单：{}",entrustOrder);
        disruptorTemplate.onData(order);
    }
}
