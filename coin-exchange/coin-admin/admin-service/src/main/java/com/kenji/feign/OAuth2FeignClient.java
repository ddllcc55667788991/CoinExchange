package com.kenji.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Kenji
 * @Date 2021/8/17 15:37
 * @Description
 */
@FeignClient(value = "authorization-server")
public interface OAuth2FeignClient {

    /**
     * 调用authorziation-server获取token
     * @param grantType
     * @param username
     * @param password
     * @param loginType
     * @param authorization
     * @return
     */
    @PostMapping("/oauth/token")
    ResponseEntity<JWTResult> getToken(
            @RequestParam("grant_type") String grantType,   //授权方式
            @RequestParam("username")String username,   //用户名
            @RequestParam("password")String password,   //密码
            @RequestParam("login_type")String loginType,     //登录方式
            @RequestHeader("Authorization")String authorization
    );
}
