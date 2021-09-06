package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.SysRole;
import com.kenji.mapper.SysRoleMapper;
import com.kenji.service.SysRoleService;
/**
 * @Author  Kenji
 * @Date  2021/8/17 1:46
 * @Description 
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService{

    /**
     * 根据用户名模糊分页查询
     *
     * @param page
     * @param name
     * @return
     */
    @Override
    public Page<SysRole> pageByName(Page<SysRole> page, String name) {
       return page(page,new LambdaQueryWrapper<SysRole>().like(
                ! StringUtils.isEmpty(name),
                SysRole::getName,
                name

        ));

    }
}
