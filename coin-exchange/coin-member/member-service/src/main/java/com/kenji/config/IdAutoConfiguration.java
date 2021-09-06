package com.kenji.config;

import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

/**
 * @Author Kenji
 * @Date 2021/8/20 19:15
 * @Description
 */
@Configuration
@EnableConfigurationProperties(IdProprties.class)
public class IdAutoConfiguration {

    private static IdProprties idProprties;

    /**
     * 发送请求工具
     */
    private static RestTemplate restTemplate = new RestTemplate();

    public IdAutoConfiguration(IdProprties idProprties){
        this.idProprties =idProprties;
    }

    /**
     * 实名认证
     * @param realName  用户真实名字
     * @param idCard    用户身份证号码
     */
    public static boolean check(String realName,String idCard){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization","APPCODE "+idProprties.getAppCode());
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                String.format(idProprties.getUrl(), idCard, realName),
                HttpMethod.GET,
                new HttpEntity<>(null, httpHeaders),
                String.class
        );
        if (responseEntity.getStatusCode()== HttpStatus.OK){
            String body = responseEntity.getBody();
            JSONObject jsonObject = JSON.parseObject(body);
            String status = jsonObject.getString("status");
            if (status.equals("01")){   //验证成功
                return true;
            }
        }
        return false;
    }
}
