package com.kenji.controller;

import com.kenji.domain.LoginResult;
import com.kenji.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Kenji
 * @Date 2021/8/17 15:01
 * @Description 登录的控制器
 */
@RestController
@Api(tags = "登录的控制器")
public class LoginController {

    @Autowired
    private LoginService loginService;

    /**
     * 用户登录控制器
     * @param username
     * @param password
     * @return
     */
    @ApiOperation(value = "后台用户登录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "username",value = "用户名"),
            @ApiImplicitParam(name = "password",value = "密码")})
    @PostMapping("/login")
    public LoginResult login(@RequestParam(required = true) String username, @RequestParam(required = true) String password){
    return loginService.login(username, password);
    }

}
