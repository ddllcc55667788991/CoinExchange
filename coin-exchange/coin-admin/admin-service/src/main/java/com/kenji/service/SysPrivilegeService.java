package com.kenji.service;

import com.kenji.domain.SysPrivilege;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author  Kenji
 * @Date  2021/8/17 1:46
 * @Description 
 */
public interface SysPrivilegeService extends IService<SysPrivilege>{


    /**
     *  获取该菜单下面所有的权限
     * @param roleId
     *          roleId 代表当前的查询的角色的ID
     * @param menuId  菜单的ID
     *
     * @return
     */
    List<SysPrivilege> getAllSysPrivilege(Long menuId ,Long roleId);
    }
