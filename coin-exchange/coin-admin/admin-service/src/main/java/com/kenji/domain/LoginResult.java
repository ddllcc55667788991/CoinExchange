package com.kenji.domain;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/17 14:49
 * @Description
 */
@ApiModel(value = "登录的结果")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResult {

    /**
     * 登录系统的token
     */
    @ApiModelProperty(value = "登录系统的token")
    private String token;

    /**
     * 前端显示的菜单
     */
    @ApiModelProperty(value = "前端显示的菜单")
    private List<SysMenu> menus;

    /**
     * 该用户的权限
     */
    @ApiModelProperty(value = "该用户的权限")
    private List<SimpleGrantedAuthority> authorities;
}
