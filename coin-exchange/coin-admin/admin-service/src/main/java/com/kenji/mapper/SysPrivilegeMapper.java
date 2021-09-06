package com.kenji.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kenji.domain.SysPrivilege;

import java.util.Set;

/**
 * @Author  Kenji
 * @Date  2021/8/17 1:46
 * @Description 
 */
public interface SysPrivilegeMapper extends BaseMapper<SysPrivilege> {
    Set<Long> getPrivilegesByRoleId(Long roleId);
}