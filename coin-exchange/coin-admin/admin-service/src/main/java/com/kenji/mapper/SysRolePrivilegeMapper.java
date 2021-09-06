package com.kenji.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kenji.domain.SysRolePrivilege;

import java.util.List;
import java.util.Set;

/**
 * @Author  Kenji
 * @Date  2021/8/17 1:46
 * @Description 
 */
public interface SysRolePrivilegeMapper extends BaseMapper<SysRolePrivilege> {
    /**
     * 根据角色id查询权限
     * @param roleId
     * @return
     */
    Set<Long> getPrivilegeByRoleId(Long roleId);
}