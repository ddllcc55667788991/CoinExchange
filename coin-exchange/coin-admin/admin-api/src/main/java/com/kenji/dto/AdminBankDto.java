package com.kenji.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Kenji
 * @Date 2021/8/24 14:57
 * @Description
 */
@ApiModel(value = "银行卡参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminBankDto {

    @ApiModelProperty(value ="开户人姓名" )
    private String name;

    @ApiModelProperty(value = "开户行名称")
    private String bankName;

    @ApiModelProperty(value = "开户卡号")
    private String bankCard;
}
