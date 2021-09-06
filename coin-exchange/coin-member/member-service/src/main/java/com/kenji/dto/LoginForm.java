package com.kenji.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Kenji
 * @Date 2021/8/20 11:07
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "前端登录传递的数据")
public class LoginForm {

    @ApiModelProperty(value = "地区码")
    private String countryCode;

    @ApiModelProperty(value = "极验的geetest_challenge")
    private String geetest_challenge;

    @ApiModelProperty(value = "极验的geetest_seccode")
    private String geetest_seccode;

    @ApiModelProperty(value = "极验的geetest_validate")
    private String geetest_validate;

    @ApiModelProperty(value = "用户的密码")
    private String password;

    @ApiModelProperty(value = "用户的账户")
    private String username;

    @ApiModelProperty(value = "uuid")
    private String uuid;
}
