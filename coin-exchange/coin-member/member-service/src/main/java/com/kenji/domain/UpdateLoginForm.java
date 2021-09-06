package com.kenji.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @Author Kenji
 * @Date 2021/8/21 23:50
 * @Description
 */
@ApiModel(value = "修改登录密码的数据模型")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateLoginForm {
    @ApiModelProperty(value = "新密码")
    @NotBlank
    private String newpassword;
    @ApiModelProperty(value = "旧密码")
    @NotBlank
    private String oldpassword;
    @ApiModelProperty(value = "验证码")
    @NotBlank
    private Integer validateCode;
}
