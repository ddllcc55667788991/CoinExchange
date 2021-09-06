package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
    /**
 * @Author  Kenji
 * @Date  2021/8/17 1:46
 * @Description 
 */
public interface SysRoleService extends IService<SysRole>{

        /**
         * 根据用户名模糊分页查询
         * @param page
         * @param name
         * @return
         */
        Page<SysRole> pageByName(Page<SysRole> page, String name);
    }
