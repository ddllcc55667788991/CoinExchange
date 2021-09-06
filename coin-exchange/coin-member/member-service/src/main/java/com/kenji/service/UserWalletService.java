package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.UserWallet;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/19 10:36
 * @Description
 */
public interface UserWalletService extends IService<UserWallet> {


    /**
     * 分页查询
     * @param page  分页数据
     * @param userId    用户ID
     * @return
     */
    Page<UserWallet> findUserWalletByPage(Page<UserWallet> page, Long userId);

    /**
     * 查询提币地址
     * @param coinId 币种ID
     * @param userid 用户ID
     * @return
     */
    List<UserWallet> getCoinAddress(Long userid, Long coinId);

    /**
     * 添加提币地址
     * @param userid
     * @param entity
     * @return
     */
    boolean save(Long userid,UserWallet entity);

    /**
     * 删除提币地址
     * @param addressId 钱包地址ID
     * @param payPassword 交易密码
     * @return
     */
    Boolean deleteAddress(Long addressId, String payPassword);
}
