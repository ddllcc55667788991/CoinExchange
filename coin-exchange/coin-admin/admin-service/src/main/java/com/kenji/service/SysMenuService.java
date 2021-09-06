package com.kenji.service;

import com.kenji.domain.SysMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/17 1:46
 * @Description
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 根据用户id查询菜单
     *
     * @param userId
     * @return
     */
    List<SysMenu> selectMenuById(Long userId);
}
