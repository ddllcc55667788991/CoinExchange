package com.kenji.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @Author Kenji
 * @Date 2021/8/15 12:37
 * @Description
 */
@RestController
public class UserInfoController {

    /**
     * 通过token获取用户对象
     * @param principal OAuth2.0自动注入
     * @return
     */
    @GetMapping("/user/info")
    public Principal userInfo(Principal principal){
        //利用context,将授权放在线程里，利用threadLocal来获取当前用户对象
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return principal;
    }
}
