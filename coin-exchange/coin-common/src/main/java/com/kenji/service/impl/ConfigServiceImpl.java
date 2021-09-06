package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.ConfigMapper;
import com.kenji.domain.Config;
import com.kenji.service.ConfigService;

import javax.xml.transform.Templates;

/**
 * @Author  Kenji
 * @Date  2021/8/17 1:46
 * @Description 
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService{

    /**
     * 根据条件分页查询
     *
     * @param page 分页模型
     * @param type 配置规则类型
     * @param code 配置规则代码
     * @param name 配置规则名称
     * @return
     */
    @Override
    public Page<Config> findConfigByPage(Page<Config> page, String type, String code, String name) {
        page.addOrder(OrderItem.desc("created"));
        Page<Config> configPage = page(page, new LambdaQueryWrapper<Config>()
                .like(!StringUtils.isEmpty(type), Config::getType, type)
                .like(!StringUtils.isEmpty(code), Config::getCode, code)
                .like(!StringUtils.isEmpty(name), Config::getName, name));
        return configPage;
    }

    /**
     * 根据配置规则代码查询config
     * @param code
     * @return
     */
    @Override
    public Config getConfigByCode(String code) {
        return super.getOne(new LambdaQueryWrapper<Config>().eq(Config::getCode,code));
    }


}
