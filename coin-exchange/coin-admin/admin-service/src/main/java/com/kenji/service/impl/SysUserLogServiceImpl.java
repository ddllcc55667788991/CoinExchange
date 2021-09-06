package com.kenji.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.SysUserLogMapper;
import com.kenji.domain.SysUserLog;
import com.kenji.service.SysUserLogService;
/**
 * @Author  Kenji
 * @Date  2021/8/17 1:46
 * @Description 
 */
@Service
public class SysUserLogServiceImpl extends ServiceImpl<SysUserLogMapper, SysUserLog> implements SysUserLogService{

}
