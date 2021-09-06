package com.kenji.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author Kenji
 * @Date 2021/8/20 20:19
 * @Description
 */
@Data
@ApiModel
public class UserAuthForm extends GeetestForm {

    @NotBlank
    @ApiModelProperty("身份证号码")
    private String idCard;

    @NotBlank
    @ApiModelProperty("证件类型")
    private String idCardType;

    @NotBlank
    @ApiModelProperty("证件号码")
    private String realName;
}
