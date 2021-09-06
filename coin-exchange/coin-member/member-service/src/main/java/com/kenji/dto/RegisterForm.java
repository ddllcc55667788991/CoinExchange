package com.kenji.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @Author Kenji
 * @Date 2021/8/22 17:19
 * @Description 用户注册表单数据
 */
@ApiModel(value = "用户注册表单数据")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterForm extends GeetestForm {

    @ApiModelProperty("国家地区编码")
    @NotBlank
    private String countryCode;

    @ApiModelProperty("电子邮件")
    private String email;

    @ApiModelProperty("邀请码")
    private String invitionCode;

    @ApiModelProperty("密码")
    @NotBlank
    private String password;

    @ApiModelProperty("手机号码")
    private String mobile;

    @ApiModelProperty("验证码")
    private String validateCode;

}
