package com.kenji.controller;

import com.kenji.domain.User;
import com.kenji.dto.LoginForm;
import com.kenji.dto.LoginUser;
import com.kenji.model.R;
import com.kenji.service.UserLoginService;
import com.kenji.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Kenji
 * @Date 2021/8/20 11:14
 * @Description
 */

@Api(tags = "登录接口")
@RestController
public class LoginController {

    @Autowired
    private UserLoginService userLoginService;

    /**
     * 用户登录的接口
     * @param loginForm 前端登录传递的数据
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "用户登录的接口")
    @ApiImplicitParam(name = "loginForm",value = "前端登录传递的数据")
    public R<LoginUser> login(@RequestBody LoginForm loginForm){
        LoginUser loginUser = userLoginService.login(loginForm);
        System.out.println(loginUser);
        return R.ok(loginUser);
    }
}
