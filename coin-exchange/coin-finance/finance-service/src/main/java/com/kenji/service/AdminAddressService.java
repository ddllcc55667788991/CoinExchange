package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.AdminAddress;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author Kenji
 * @Date 2021/8/23 11:30
 * @Description
 */
public interface AdminAddressService extends IService<AdminAddress> {


    /**
     * 分页查询钱包归集地址
     * @param page  分页大小
     * @param coinId    币种ID
     * @return
     */
    Page<AdminAddress> findAdminAddressByPage(Page<AdminAddress> page, Long coinId);

    /**
     * 新增钱包归集地址
     * @param adminAddress 前端传来的钱包归集地址
     * @return
     */
    Boolean addAdminAddress(AdminAddress adminAddress);

}
