package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.UserAddress;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author Kenji
 * @Date 2021/8/19 10:36
 * @Description
 */
public interface UserAddressService extends IService<UserAddress> {

    /**
     * 分页查询
     * @param page  分页数据
     * @param userId    用户ID
     * @return
     */
    Page<UserAddress> findUserAddressByPage(Page<UserAddress> page, Long userId);

    /**
     * 充币获取钱包地址
     * @param coinId 币种id
     * @param userId 用户id
     * @return
     */
    UserAddress getCoinAddress(String userId, Long coinId);

}
