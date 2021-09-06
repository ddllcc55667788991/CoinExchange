package com.kenji.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.UserLoginLog;
import com.kenji.mapper.UserLoginLogMapper;
import com.kenji.service.UserLoginLogService;
/**
 * @Author  Kenji
 * @Date  2021/8/19 10:36
 * @Description 
 */
@Service
public class UserLoginLogServiceImpl extends ServiceImpl<UserLoginLogMapper, UserLoginLog> implements UserLoginLogService{

}
