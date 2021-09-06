package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.Coin;
import com.kenji.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.AdminAddress;
import com.kenji.mapper.AdminAddressMapper;
import com.kenji.service.AdminAddressService;

/**
 * @Author Kenji
 * @Date 2021/8/23 11:30
 * @Description
 */
@Service
public class AdminAddressServiceImpl extends ServiceImpl<AdminAddressMapper, AdminAddress> implements AdminAddressService {

    @Autowired
    private CoinService coinService;

    /**
     * 分页查询钱包归集地址
     *
     * @param page   分页大小
     * @param coinId 币种ID
     * @return
     */
    @Override
    public Page<AdminAddress> findAdminAddressByPage(Page<AdminAddress> page, Long coinId) {
        return super.page(page, new LambdaQueryWrapper<AdminAddress>()
                .eq(coinId != null,AdminAddress::getCoinId,coinId));
    }

    /**
     * 新增钱包归集地址
     *
     * @param adminAddress 前端传来的钱包归集地址
     * @return
     */
    @Override
    public Boolean addAdminAddress(AdminAddress adminAddress) {
         Long coinId = adminAddress.getCoinId();
         if (coinId!=0){
             Coin coin = coinService.getById(coinId);
             adminAddress.setCoinType(coin.getType());
         }
        return super.save(adminAddress);
    }
}
