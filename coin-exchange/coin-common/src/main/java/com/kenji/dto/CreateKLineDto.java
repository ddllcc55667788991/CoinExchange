package com.kenji.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * @Author Kenji
 * @Date 2021/8/28 1:58
 * @Description
 */
@Data
@EqualsAndHashCode
public class CreateKLineDto {
    /**
     * 交易对名称
     */
    private String symbol;
    /**
     * 交易的价格
     */
    private BigDecimal price;
    /**
     * 交易的数量
     */
    private BigDecimal volume;
}
