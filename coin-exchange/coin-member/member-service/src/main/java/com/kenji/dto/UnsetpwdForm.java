package com.kenji.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @Author Kenji
 * @Date 2021/8/22 19:21
 * @Description
 */
@ApiModel(value = "前端传送重置密码的数据")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnsetpwdForm extends GeetestForm {

    @ApiModelProperty(value = "国家地区编码")
    @NotBlank
    private String countryCode;

    @ApiModelProperty(value = "手机号码")
    @NotBlank
    private String mobile;

    @ApiModelProperty(value = "密码")
    @NotBlank
    private String password;

    @ApiModelProperty(value = "手机验证码")
    @NotBlank
    private String validateCode;
}
