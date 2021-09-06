package com.kenji.service;

import com.kenji.domain.UserAuthInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/19 10:36
 * @Description
 */
public interface UserAuthInfoService extends IService<UserAuthInfo> {


    /**
     * 根据用户id查找用户认证详情
     * @param id
     * @return
     */
    List<UserAuthInfo> getByUserId(Long id);

}
