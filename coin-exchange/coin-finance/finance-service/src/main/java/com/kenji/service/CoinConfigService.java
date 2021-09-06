package com.kenji.service;

import com.kenji.domain.CoinConfig;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:19
 * @Description
 */
public interface CoinConfigService extends IService<CoinConfig> {

    /**
     * 编辑/新增coin币种配置信息
     * @param coinConfig  前端传递的coin币种配置信息json数据
     * @return
     */
    boolean updateOrSave(CoinConfig coinConfig);
}
