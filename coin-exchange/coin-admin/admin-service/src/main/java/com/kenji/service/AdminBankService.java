package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.AdminBank;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kenji.dto.AdminBankDto;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/19 1:38
 * @Description
 */
public interface AdminBankService extends IService<AdminBank> {


    /**
     * 根据条件分页查询
     *
     * @param page     分页数据
     * @param bankCard 银行卡号
     * @return
     */
    Page<AdminBank> findAdminBankByPage(Page<AdminBank> page, String bankCard);

    /**
     * 获取所有银行卡数据
     * @return
     */
    List<AdminBankDto> findAdminBank();

}
