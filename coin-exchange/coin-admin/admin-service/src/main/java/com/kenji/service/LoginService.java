package com.kenji.service;

import com.kenji.domain.LoginResult;
import org.springframework.stereotype.Service;

/**
 * @Author Kenji
 * @Date 2021/8/17 15:12
 * @Description
 */
public interface LoginService {

    /**
     * 后台用户登录
     * @param username
     * @param password
     * @return
     */
    LoginResult login(String username,String password);
}
