package com.kenji.service;

import com.kenji.domain.Sms;
import com.baomidou.mybatisplus.extension.service.IService;
    /**
 * @Author  Kenji
 * @Date  2021/8/21 15:31
 * @Description 
 */
public interface SmsService extends IService<Sms>{

        /**
         * 发送验证码
         * @param sms
         * @return
         */
        Boolean sendMsg(Sms sms);
    }
