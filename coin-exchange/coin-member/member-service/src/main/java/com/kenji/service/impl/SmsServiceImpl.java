package com.kenji.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.alicloud.sms.ISmsService;
import com.alibaba.fastjson.JSON;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.kenji.domain.Config;
import com.kenji.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.SmsMapper;
import com.kenji.domain.Sms;
import com.kenji.service.SmsService;
import org.springframework.util.Assert;

/**
 * @Author  Kenji
 * @Date  2021/8/21 15:31
 * @Description 
 */
@Service
@Slf4j
public class SmsServiceImpl extends ServiceImpl<SmsMapper, Sms> implements SmsService{

    @Autowired
    private ISmsService smsService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 发送验证码
     *
     * @param sms
     * @return
     */
    @Override
    public Boolean sendMsg(Sms sms) {
        log.info("开始发送短信{}", JSON.toJSONString(sms));
        try {
            SendSmsRequest sendSmsRequest = buidSmsRequest(sms);
            SendSmsResponse sendSmsResponse = smsService.sendSmsRequest(sendSmsRequest);
            log.info("发送的结果为 ",JSON.toJSONString(sendSmsResponse,true));
            if (sendSmsResponse.getCode().equals("OK")){
                sms.setStatus(1);
                return save(sms);
            }
            return false;
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 发送短信
     * @param sms
     * @return
     */
    private SendSmsRequest buidSmsRequest(Sms sms) {
        SendSmsRequest sendSmsRequest =new SendSmsRequest();
        sendSmsRequest.setPhoneNumbers(sms.getMobile());    //设置手机号码
        Config signConfig = configService.getConfigByCode("SIGN");
        sendSmsRequest.setSignName(signConfig.getValue());    //设置签名
        Config templateconfig = configService.getConfigByCode(sms.getTemplateCode());
        Assert.notNull(templateconfig,"您输入的模板不存在");
        sendSmsRequest.setTemplateCode(templateconfig.getValue());   //设置模板code
        String code = RandomUtil.randomNumbers(6);
        //把code存在Redis里
        //key:SMS:VERIFY_OLD_PHONE:18826230223 value:code
        redisTemplate.opsForValue().set("SMS:"+sms.getTemplateCode()+":"+sms.getMobile(),code,5, TimeUnit.MINUTES);
        // Required:The param of sms template.For exmaple, if the template is "Hello,your verification code is ${code}". The param should be like following value
        sendSmsRequest.setTemplateParam("{\"code\":\"" + code + "\"}");
        String desc = templateconfig.getDesc(); //sign:您的验证码${code}，该验证码5分钟内有效，请勿泄漏于他人！
        String content = templateconfig.getValue()+":"+desc.replaceAll("\\$\\{code\\}",code);
        sms.setContent(content);
        return sendSmsRequest ;
    }
}
