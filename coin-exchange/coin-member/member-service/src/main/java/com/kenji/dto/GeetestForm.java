package com.kenji.dto;

import com.alibaba.fastjson.JSON;
import com.kenji.geetest.GeetestLib;
import com.kenji.geetest.GeetestLibResult;
import com.kenji.util.IpUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Kenji
 * @Date 2021/8/20 20:13
 * @Description
 */
@Data
@Slf4j
public class GeetestForm {

    /**
     * 极验数据
     */
    private String geetest_challenge;

    private String geetest_seccode;

    private String geetest_validate;

    private String uuid;


    public  void checkFormData( GeetestLib geetestLib, RedisTemplate<String,Object> redisTemplate) {
        String geetestChallenge = this.getGeetest_challenge();
        String geetestValidate = this.getGeetest_validate();
        String geetestSeccode = this.getGeetest_seccode();
        int status = 0;
        String userId = "";
        // session必须取出值，若取不出值，直接当做异常退出
        status = Integer.valueOf(redisTemplate.opsForValue().get(GeetestLib.GEETEST_SERVER_STATUS_SESSION_KEY).toString()).intValue();
        userId = (String) redisTemplate.opsForValue().get(GeetestLib.GEETEST_SERVER_USER_KEY+":"+this.getUuid());
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
            log.error("验证异常", JSON.toJSONString(result,true));
            throw new IllegalArgumentException("验证码验证异常");
        }
    }
}
