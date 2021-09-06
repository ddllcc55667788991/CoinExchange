package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.Config;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author Kenji
 * @Date 2021/8/17 1:46
 * @Description
 */
public interface ConfigService extends IService<Config> {


    /**
     * 根据条件分页查询
     *
     * @param page 分页模型
     * @param type 配置规则类型
     * @param code 配置规则代码
     * @param name 配置规则名称
     * @return
     */
    Page<Config> findConfigByPage(Page<Config> page, String type, String code, String name);

    /**
     * 根据配置规则代码查询config
     * @param code
     * @return
     */
    Config getConfigByCode(String code);


}
