package com.kenji.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Author Kenji
 * @Date 2021/8/26 1:11
 * @Description
 */
@Data
@ApiModel("明细")
@AllArgsConstructor
@NoArgsConstructor
public class DepthItemVo {

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private BigDecimal price = BigDecimal.ZERO;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private BigDecimal volume = BigDecimal.ZERO;
}
