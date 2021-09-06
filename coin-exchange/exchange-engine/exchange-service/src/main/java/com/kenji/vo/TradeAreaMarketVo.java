package com.kenji.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/25 10:59
 * @Description
 */
@Data
@ApiModel(value = "交易区域和交易市场")
public class TradeAreaMarketVo {
    /**
     * 交易区域的名称
     */
    @ApiModelProperty(value = "交易区域的名称")
    private String areaName ;
    /**
     * 交易区域包含的市场
     */
    @ApiModelProperty(value = "交易区域下包含的市场")
    private List<TradeMarketVo> markets ;
}

