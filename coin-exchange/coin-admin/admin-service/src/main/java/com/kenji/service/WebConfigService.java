package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.WebConfig;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/17 1:46
 * @Description
 */
public interface WebConfigService extends IService<WebConfig> {


    /**
     * 根据条件分页查询
     *
     * @param page 分页数据
     * @param type 资源类型
     * @param name 资源名称
     * @return
     */
    Page<WebConfig> queryByPage(Page<WebConfig> page, String type, String name);

    /**
     * 查询所有banner图
     * @return
     */
    List<WebConfig> getBanner();


}
