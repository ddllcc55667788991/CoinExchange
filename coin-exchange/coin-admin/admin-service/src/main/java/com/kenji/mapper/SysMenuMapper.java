package com.kenji.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kenji.domain.SysMenu;

import java.util.List;

/**
 * @Author  Kenji
 * @Date  2021/8/17 1:46
 * @Description 
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {
    List<SysMenu> selectMenusById(Long userId);
}