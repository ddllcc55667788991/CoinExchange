package com.kenji.service;

import com.kenji.dto.LoginForm;
import com.kenji.dto.LoginUser;

/**
 * @Author Kenji
 * @Date 2021/8/20 11:33
 * @Description
 */
public interface UserLoginService {
    /**
     * 用户登录的接口
     * @param loginForm 前端登录传递的数据
     * @return
     */
    LoginUser login(LoginForm loginForm);

}
