package com.kenji.vo;

/**
 * @Author Kenji
 * @Date 2021/8/24 14:40
 * @Description
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

import java.math.BigDecimal;

/**
 * 收款方户名 吴志锋
 * 收款方开户行 北京银行
 * 收款方账号 6214680131522508
 * 转账金额 ￥100000
 * 参考号 515685(汇款务必备注)
 * 状态 待审核
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "现金交易结果")
public class CashTradeVo {

    @ApiModelProperty(value ="收款方户名" )
    private String name;

    @ApiModelProperty(value = "收款方开户行")
    private String bankName;

    @ApiModelProperty(value = "收款方账号")
    private String bankCard;

    @ApiModelProperty(value = "转账金额")
    private BigDecimal amount;

    @ApiModelProperty(value = "参考号")
    private String remark;

    @ApiModelProperty(value = "状态")
    private Byte status;

}
