package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.User;
import com.kenji.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.UserBank;
import com.kenji.mapper.UserBankMapper;
import com.kenji.service.UserBankService;

/**
 * @Author Kenji
 * @Date 2021/8/19 10:36
 * @Description
 */
@Service
public class UserBankServiceImpl extends ServiceImpl<UserBankMapper, UserBank> implements UserBankService {


    @Autowired
    private UserService userService;

    /**
     * 分页查询
     *
     * @param page  分页数据
     * @param usrId 用户ID
     * @return
     */
    @Override
    public Page<UserBank> findUserByPage(Page<UserBank> page, Long usrId) {
        page.addOrder(OrderItem.desc("last_update_time"));
        return super.page(page, new LambdaQueryWrapper<UserBank>()
                .eq(usrId != null, UserBank::getUserId, usrId));

    }

    /**
     * 绑定银行卡
     *
     * @param userid   用户id
     * @param userBank 银行卡
     * @return
     */
    @Override
    public Boolean bind(Long userid, UserBank userBank) {
        //验证交易密码
        User user = userService.getById(userid);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (encoder.matches(userBank.getPayPassword(), user.getPaypassword())) {
            //id=null，添加银行卡,不是null,修改银行卡
            if (userBank.getId() == 0) {
                userBank.setStatus(Byte.valueOf("1"));
                userBank.setUserId(userid);
                return super.save(userBank);
            } else {
                //修改银行卡
                UserBank bank = getById(userBank.getId());
                if (bank == null) {
                    throw new IllegalArgumentException("银行卡号错误");
                }
                return updateById(userBank);
            }
        }
        return false;
    }

    /**
     * 查询用户银行卡
     *
     * @param userid
     * @return
     */
    @Override
    public UserBank currentUserBank(Long userid) {
        UserBank userBank = super.getOne(new LambdaQueryWrapper<UserBank>()
                .eq(UserBank::getUserId, userid)
                .eq(UserBank::getStatus, 1));
        return userBank;
    }


}
