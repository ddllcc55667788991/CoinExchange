package com.kenji.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.SysUserRole;
import com.kenji.service.SysUserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.SysUser;
import com.kenji.mapper.SysUserMapper;
import com.kenji.service.SysUserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @Author Kenji
 * @Date 2021/8/17 1:46
 * @Description
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 根据fullname和mobile模糊分页查询
     *
     * @param page
     * @param fullName
     * @param mobile
     * @return
     */
    @Override
    public Page<SysUser> findUserByMobileAndFullName(Page<SysUser> page, String fullName, String mobile) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<SysUser> pageData = page(page, new LambdaQueryWrapper<SysUser>()
                .like(!StringUtils.isEmpty(mobile), SysUser::getMobile, mobile)
                .like(!StringUtils.isEmpty(fullName), SysUser::getFullname, fullName));
        List<SysUser> records = pageData.getRecords();
        if (!CollectionUtil.isEmpty(records)){
            for (SysUser record : records) {
                List<SysUserRole> userRoles = sysUserRoleService.list(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, record.getId()));
                if (!CollectionUtils.isEmpty(userRoles)){
                    record.setRole_strings(
                    userRoles.stream().map(sysUserRole -> sysUserRole.getRoleId().toString()).collect(Collectors.joining(",")));
                }
            }
        }
        return pageData;
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @Transactional
    @Override
    public Boolean insertUser(SysUser user) {
        String originPassword = user.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String newpwd = encoder.encode(originPassword);
        user.setPassword(newpwd);
        String roleStrings = user.getRole_strings();
        String[] roleIds = roleStrings.split(",");
        boolean save = super.save(user);
        if (save) {
            List<SysUserRole> userRoleList = new ArrayList<>();
            for (String roleid : roleIds) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(Long.valueOf(roleid));
                sysUserRole.setUserId(user.getId());
                userRoleList.add(sysUserRole);
            }
            boolean index2 = sysUserRoleService.saveBatch(userRoleList);
            return index2;
        }
        return false;
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @Override
    public Boolean updateUser(SysUser user) {
        String originPassword = user.getPassword();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String newpwd = encoder.encode(originPassword);
        user.setPassword(newpwd);
        String roleStrings = user.getRole_strings();
        String[] roleIds = roleStrings.split(",");
        boolean update = super.updateById(user);
        if (update) {
            sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId,user.getId()));
            List<SysUserRole> userRoleList = new ArrayList<>();
            for (String roleid : roleIds) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(Long.valueOf(roleid));
                sysUserRole.setUserId(user.getId());
                userRoleList.add(sysUserRole);
            }
            boolean index2 = sysUserRoleService.saveBatch(userRoleList);
            return index2;
        }
        return false;
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @Override
    public Boolean deleteUser(String[] ids) {
        List<String> userIds = Arrays.asList(ids);
        boolean index1 = super.removeByIds(userIds);
        if (index1){
            boolean index2 = sysUserRoleService.remove(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, userIds));
            return true;
        }
        return false;
    }
}
