package com.kenji.controller;

import com.alibaba.alicloud.sms.SmsServiceImpl;
import com.kenji.domain.Sms;
import com.kenji.model.R;
import com.kenji.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Kenji
 * @Date 2021/8/21 15:15
 * @Description
 */
@RestController
@Api(tags = "短信发送")
@RequestMapping("/sms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    /**
     * 发送验证码
     * @param sms
     * @return
     */
    @PostMapping("/sendTo")
    @ApiOperation("发送验证码")
    @ApiImplicitParam(name = "sms",value = "前端发送的短信数据")
    public R sendSms(@RequestBody @Validated Sms sms){
        Boolean send = smsService.sendMsg(sms);
        if (send){
            return R.ok();
        }
        return R.fail("发送失败");
    }
}
