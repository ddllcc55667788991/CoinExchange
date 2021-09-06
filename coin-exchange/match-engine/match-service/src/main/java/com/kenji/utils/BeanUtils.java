package com.kenji.utils;

import com.kenji.enums.OrderDirection;
import com.kenji.domain.EntrustOrder;
import com.kenji.model.Order;

/**
 * @Author Kenji
 * @Date 2021/8/26 19:42
 * @Description
 */
public class BeanUtils {
    public static Order entrustOrder2Order(EntrustOrder entrustOrder) {
        Order order = new Order();
        order.setSymbol(entrustOrder.getSymbol());  //设置交易对
        order.setAmount(entrustOrder.getVolume().add(entrustOrder.getDeal().negate()));     //设置交易额
        order.setPrice(entrustOrder.getPrice());    //设置交易价格
        order.setTime(entrustOrder.getCreated().getTime()); //设置交易时间
        order.setOrderId(entrustOrder.getId().toString());  //设置交易的id
        order.setOrderDirection(OrderDirection.getOrderDirection(entrustOrder.getType()));     //设置交易方向
        return order;
    }
}
