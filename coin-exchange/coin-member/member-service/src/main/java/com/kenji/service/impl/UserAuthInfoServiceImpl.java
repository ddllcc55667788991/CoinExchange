package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.UserAuthInfo;
import com.kenji.mapper.UserAuthInfoMapper;
import com.kenji.service.UserAuthInfoService;

/**
 * @Author Kenji
 * @Date 2021/8/19 10:36
 * @Description
 */
@Service
public class UserAuthInfoServiceImpl extends ServiceImpl<UserAuthInfoMapper, UserAuthInfo> implements UserAuthInfoService {

    /**
     * 根据用户id查找用户认证详情
     *
     * @param id
     * @return
     */
    @Override
    public List<UserAuthInfo> getByUserId(Long id) {
        return super.list(new LambdaQueryWrapper<UserAuthInfo>()
                .eq(id != null, UserAuthInfo::getUserId, id)
                .orderByDesc(UserAuthInfo::getLastUpdateTime));

    }
}
