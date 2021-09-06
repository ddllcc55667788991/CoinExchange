package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author Kenji
 * @Date 2021/8/17 1:46
 * @Description
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据fullname和mobile模糊分页查询
     *
     * @param page
     * @param fullName
     * @param mobile
     * @return
     */
    Page<SysUser> findUserByMobileAndFullName(Page<SysUser> page, String fullName, String mobile);


    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    Boolean insertUser(SysUser user);

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    Boolean updateUser(SysUser user);


    /**
     * 删除用户
     * @param ids
     * @return
     */
    Boolean deleteUser(String[] ids);
}
