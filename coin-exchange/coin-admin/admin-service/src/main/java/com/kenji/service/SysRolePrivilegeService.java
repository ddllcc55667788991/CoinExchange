package com.kenji.service;

import com.kenji.domain.RolePrivilegesParam;
import com.kenji.domain.SysMenu;
import com.kenji.domain.SysRolePrivilege;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author  Kenji
 * @Date  2021/8/17 1:46
 * @Description 
 */
public interface SysRolePrivilegeService extends IService<SysRolePrivilege>{

    /**
     * 查询角色的权限
     * @param roleId
     * @return
     */
    List<SysMenu> findSysMenuAndPrivileges(Long roleId);


    /**
     * 给角色授权权限
     * @param rolePrivilegesParam
     * @return
     */
    boolean grantPrivileges(RolePrivilegesParam rolePrivilegesParam);
}
