package com.kenji.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.SmsMapper;
import com.kenji.domain.Sms;
import com.kenji.service.SmsService;
/**
 * @Author  Kenji
 * @Date  2021/8/17 1:46
 * @Description 
 */
@Service
public class SmsServiceImpl extends ServiceImpl<SmsMapper, Sms> implements SmsService{

}
