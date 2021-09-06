package com.kenji.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Author Kenji
 * @Date 2021/8/24 17:21
 * @Description
 */
@ApiModel("用户提现数据")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CashSellParam {
    @ApiModelProperty("币种id")
    @NotNull
    private Long coinId;

    @ApiModelProperty("提现总额")
    @NotNull
    private BigDecimal mum;

    @ApiModelProperty("提现货币的数量")
    @NotNull
    private BigDecimal num;

    @ApiModelProperty("支付密码")
    @NotBlank
    private String payPassword;

    @ApiModelProperty("手机验证码")
    @NotBlank
    private String validateCode;
}
