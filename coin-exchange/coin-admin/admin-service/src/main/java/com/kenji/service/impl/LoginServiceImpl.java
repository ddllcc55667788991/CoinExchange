package com.kenji.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.enums.ApiErrorCode;
import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.kenji.feign.JWTResult;
import com.kenji.domain.LoginResult;
import com.kenji.domain.SysMenu;
import com.kenji.feign.OAuth2FeignClient;
import com.kenji.service.LoginService;
import com.kenji.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author Kenji
 * @Date 2021/8/17 15:13
 * @Description
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Autowired
    private OAuth2FeignClient auth2FeignClient;

    @Value("${basic.token:Basic Y29pbi1hcGk6Y29pbi1zZWNyZXQ=}")
    private String basicToken;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 后台用户登录
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public LoginResult login(String username, String password) {
        log.info("用户{}开始登录",username);
        //获取token,需要远程调用authorization-server
        ResponseEntity<JWTResult> tokenEntity = auth2FeignClient.getToken("password", username, password, "admin_type", basicToken);
        if (!tokenEntity.getStatusCode().equals(HttpStatus.OK)){
            throw new ApiException(ApiErrorCode.FAILED);
        }
        JWTResult jwtResult = tokenEntity.getBody();
        log.info("远程调用授权服务器成功，获取的token={}", JSON.toJSONString(jwtResult,true));
        String token = jwtResult.getAccessToken();
        //查询菜单
        Jwt jwt = JwtHelper.decode(token);
        String claims = jwt.getClaims();
        JSONObject  jsonObject = (JSONObject) JSON.parse(claims);
        Long  userId = Long.valueOf(jsonObject.getString("user_name"));
        List<SysMenu> menus = sysMenuService.selectMenuById(userId);
        //获取权限
        JSONArray authoritiesArray = jsonObject.getJSONArray("authorities");
        List<SimpleGrantedAuthority> authorities = authoritiesArray.stream().map(authorityJson -> new SimpleGrantedAuthority(authorityJson.toString())).collect(Collectors.toList());
        //登出问题，将token存在redis里面，网关做jwt验证
        redisTemplate.opsForValue().set(token,"",jwtResult.getExpiresIn(), TimeUnit.SECONDS);
        return new LoginResult(jwtResult.getTokenType()+" "+token,menus,authorities);
    }
}
