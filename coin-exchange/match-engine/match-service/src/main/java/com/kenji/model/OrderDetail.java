package com.kenji.model;

/**
 * @Author Kenji
 * @Date 2021/8/26 11:18
 * @Description
 */

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetail {

    /**
     * 订单 Id
     */
    private String orderId;

    /**
     * 成交价格
     */
    private BigDecimal price;

    /**
     * 成交数量
     */
    private BigDecimal amount;

    /**
     * 成交额
     */
    private BigDecimal turnover;

    /**
     * 费率
     */
    private BigDecimal fee;

    /**
     * 成功时间
     */
    private Long dealTime;
}
