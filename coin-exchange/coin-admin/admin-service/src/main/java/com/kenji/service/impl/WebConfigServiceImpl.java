package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.WebConfig;
import com.kenji.mapper.WebConfigMapper;
import com.kenji.service.WebConfigService;

/**
 * @Author Kenji
 * @Date 2021/8/17 1:46
 * @Description
 */
@Service
public class WebConfigServiceImpl extends ServiceImpl<WebConfigMapper, WebConfig> implements WebConfigService {

    /**
     * 根据条件分页查询
     *
     * @param page 分页数据
     * @param type 资源类型
     * @param name 资源名称
     * @return
     */
    @Override
    public Page<WebConfig> queryByPage(Page<WebConfig> page, String type, String name) {
        page.addOrder(OrderItem.desc("created"));
        return super.page(page, new LambdaQueryWrapper<WebConfig>()
                .like(!StringUtils.isEmpty(type), WebConfig::getType, type)
                .like(!StringUtils.isEmpty(name), WebConfig::getName, name)
        );

    }

    /**
     * 查询所有banner图
     *
     * @return
     */
    @Override
    public List<WebConfig> getBanner() {
        return super.list(new LambdaQueryWrapper<WebConfig>()
                .orderByAsc(WebConfig::getSort)
                .eq(WebConfig::getStatus, 1));
    }
}
