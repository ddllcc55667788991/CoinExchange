package com.kenji.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Kenji
 * @Date 2021/8/20 11:25
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "登录成功的用户")
public class LoginUser {

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "token过期时间")
    private Long expire;

    @ApiModelProperty(value = "access_token")
    private String access_token;

    @ApiModelProperty(value = "refresh_token")
    private String refresh_token;

}
