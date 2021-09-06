package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.UserBank;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author Kenji
 * @Date 2021/8/19 10:36
 * @Description
 */
public interface UserBankService extends IService<UserBank> {


    /**
     * 分页查询
     * @param page  分页数据
     * @param userId    用户ID
     * @return
     */
    Page<UserBank> findUserByPage(Page<UserBank> page, Long userId);

    /**
     * 绑定银行卡
     * @param userBank 银行卡
     * @param userid 用户id
     * @return
     */
    Boolean bind(Long userid, UserBank userBank);

    /**
     * 查询用户银行卡
     * @return
     */
    UserBank currentUserBank(Long userid);

}
