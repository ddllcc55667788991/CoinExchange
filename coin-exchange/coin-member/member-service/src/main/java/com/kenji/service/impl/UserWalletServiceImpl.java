package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.User;
import com.kenji.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.UserWallet;
import com.kenji.mapper.UserWalletMapper;
import com.kenji.service.UserWalletService;

/**
 * @Author Kenji
 * @Date 2021/8/19 10:36
 * @Description
 */
@Service
public class UserWalletServiceImpl extends ServiceImpl<UserWalletMapper, UserWallet> implements UserWalletService {

    @Autowired
    private UserService userService;

    /**
     * 分页查询
     *
     * @param page   分页数据
     * @param userId 用户ID
     * @return
     */
    @Override
    public Page<UserWallet> findUserWalletByPage(Page<UserWallet> page, Long userId) {
        return super.page(page, new LambdaQueryWrapper<UserWallet>()
                .eq(UserWallet::getUserId, userId));
    }

    /**
     * 查询提币地址
     *
     * @param userid 用户ID
     * @param coinId 币种ID
     * @return
     */
    @Override
    public List<UserWallet> getCoinAddress(Long userid, Long coinId) {
        return list(new LambdaQueryWrapper<UserWallet>()
                .eq(UserWallet::getUserId, userid)
                .eq(UserWallet::getCoinId,coinId));
    }

    /**
     * 插入一条记录（选择字段，策略插入）
     *
     * @param entity 实体对象
     */
    @Override
    public boolean save(Long userid,UserWallet entity) {
        User user = userService.getById(userid);
        if (!StringUtils.isEmpty(entity.getPayPassword())){
            if (new BCryptPasswordEncoder().matches(entity.getPayPassword(),user.getPaypassword())){
                entity.setUserId(userid);
                return super.save(entity);
            }
        }
        return false;
    }

    /**
     * 删除提币地址
     *
     * @param addressId   钱包地址ID
     * @param payPassword 交易密码
     * @return
     */
    @Override
    public Boolean deleteAddress(Long addressId, String payPassword) {
        UserWallet userWallet = super.getById(addressId);
        if (userWallet!=null){
            Long userId = userWallet.getUserId();
            User user = userService.getById(userId);
            if (new BCryptPasswordEncoder().matches(payPassword,user.getPaypassword())){
                return super.removeById(addressId);
            }
        }
        return false;
    }
}
