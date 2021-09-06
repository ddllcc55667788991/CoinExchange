package com.kenji.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author Kenji
 * @Date 2021/8/24 14:35
 * @Description
 */
@Data
@ApiModel(value = "GCN充值参数")
public class CashParam {

    @ApiModelProperty(value = "币种ID")
    @NotNull
    private Long coinId;

    @ApiModelProperty(value = "实际金额")
    @NotNull
    private BigDecimal mum;

    @ApiModelProperty(value = "购买数量")
    @NotNull
    private BigDecimal num;
}
