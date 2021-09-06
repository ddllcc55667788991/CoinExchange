package com.kenji.service.impl;

import com.alibaba.fastjson.JSON;
import com.kenji.dto.LoginForm;
import com.kenji.dto.LoginUser;
import com.kenji.feign.JWTResult;
import com.kenji.feign.OAuth2FeignClient;
import com.kenji.geetest.GeetestLib;
import com.kenji.geetest.GeetestLibResult;
import com.kenji.service.UserLoginService;
import com.kenji.service.UserService;
import com.kenji.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author Kenji
 * @Date 2021/8/20 11:34
 * @Description
 */
@Service
@Slf4j
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private UserService userService;

    @Autowired
    private OAuth2FeignClient oAuth2FeignClient;

    @Value("${basic.token:Basic Y29pbi1hcGk6Y29pbi1zZWNyZXQ=}")
    private String basicToken;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private GeetestLib geetestLib;

    /**
     * 用户登录的接口
     *
     * @param loginForm 前端登录传递的数据
     * @return
     */
    @Override
    public LoginUser login(LoginForm loginForm) {
        log.info("会员{}开始登录", loginForm.getUsername());
        checkFormData(loginForm);
        ResponseEntity<JWTResult> tokenResponseEntity = oAuth2FeignClient.getToken("password", loginForm.getUsername(), loginForm.getPassword(), "member_type", basicToken);
        LoginUser loginUser = null;
        if (tokenResponseEntity.getStatusCode() == HttpStatus.OK) {
            JWTResult jwtResult = tokenResponseEntity.getBody();
            log.info("远程调用成功，结果为", JSON.toJSONString(jwtResult,true));
            String username = userService.findUserByMobileOrEmail(loginForm.getUsername());
            loginUser = new LoginUser(username,jwtResult.getExpiresIn(),jwtResult.getTokenType()+" "+jwtResult.getAccessToken(),jwtResult.getTokenType()+" "+jwtResult.getRefreshToken());
            //使用网关解决登出问题
            redisTemplate.opsForValue().set(jwtResult.getAccessToken(),"",jwtResult.getExpiresIn(), TimeUnit.SECONDS);
        }
        return loginUser;
    }

    /**
     * 极验校验数据
     * @param loginForm
     */
    private void checkFormData(LoginForm loginForm) {
        String geetestChallenge = loginForm.getGeetest_challenge();
        String geetestValidate = loginForm.getGeetest_validate();
        String geetestSeccode = loginForm.getGeetest_seccode();
        int status = 0;
        String userId = "";
            // session必须取出值，若取不出值，直接当做异常退出
            status = Integer.valueOf(redisTemplate.opsForValue().get(GeetestLib.GEETEST_SERVER_STATUS_SESSION_KEY).toString()).intValue();
            userId = (String) redisTemplate.opsForValue().get(GeetestLib.GEETEST_SERVER_USER_KEY+":"+loginForm.getUuid());

        GeetestLibResult result = null;
        if (status == 1) {
            /*
            自定义参数,可选择添加
                user_id 客户端用户的唯一标识，确定用户的唯一性；作用于提供进阶数据分析服务，可在register和validate接口传入，不传入也不影响验证服务的使用；若担心用户信息风险，可作预处理(如哈希处理)再提供到极验
                client_type 客户端类型，web：电脑上的浏览器；h5：手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生sdk植入app应用的方式；unknown：未知
                ip_address 客户端请求sdk服务器的ip地址
            */
            Map<String, String> paramMap = new HashMap<String, String>();
            paramMap.put("user_id", userId);
            paramMap.put("client_type", "web");
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            paramMap.put("ip_address", IpUtil.getIpAddr(requestAttributes.getRequest()));
            result = geetestLib.successValidate(geetestChallenge, geetestValidate, geetestSeccode, paramMap);
        } else {
            result = geetestLib.failValidate(geetestChallenge, geetestValidate, geetestSeccode);
        }
        // 注意，不要更改返回的结构和值类型
        if (result.getStatus() != 1) {
            log.error("验证异常",JSON.toJSONString(result,true));
            throw new IllegalArgumentException("验证码验证异常");
        }
    }


}
