package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.UserAddressMapper;
import com.kenji.domain.UserAddress;
import com.kenji.service.UserAddressService;

/**
 * @Author Kenji
 * @Date 2021/8/19 10:36
 * @Description
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    /**
     * 分页查询
     *
     * @param page   分页数据
     * @param userId 用户ID
     * @return
     */
    @Override
    public Page<UserAddress> findUserAddressByPage(Page<UserAddress> page, Long userId) {
        page.addOrder(OrderItem.desc("last_update_time"));
        return super.page(page, new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userId));
    }

    /**
     * 充币获取钱包地址
     *
     * @param userId 用户id
     * @param coinId 币种id
     * @return
     */
    @Override
    public UserAddress getCoinAddress(String userId, Long coinId) {
        return getOne(new LambdaQueryWrapper<UserAddress>()
                .eq(UserAddress::getUserId, userId)
                .eq(UserAddress::getCoinId,coinId));
    }
}
