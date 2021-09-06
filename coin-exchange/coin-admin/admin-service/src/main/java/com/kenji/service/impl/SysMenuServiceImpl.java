package com.kenji.service.impl;

import com.kenji.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.SysMenu;
import com.kenji.mapper.SysMenuMapper;
import com.kenji.service.SysMenuService;
/**
 * @Author  Kenji
 * @Date  2021/8/17 1:46
 * @Description 
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService{

    @Autowired
    private SysMenuMapper sysMenuMapper;
    /**
     * 根据用户id查询菜单
     *
     * @param userId
     * @return
     */
    @Override
    public List<SysMenu> selectMenuById(Long userId) {
        return sysMenuMapper.selectMenusById(userId);
    }
}
