package com.kenji.service.impl;

import com.kenji.domain.Coin;
import com.kenji.service.CoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.CoinConfigMapper;
import com.kenji.domain.CoinConfig;
import com.kenji.service.CoinConfigService;
/**
 * @Author  Kenji
 * @Date  2021/8/23 0:19
 * @Description 
 */
@Service
public class CoinConfigServiceImpl extends ServiceImpl<CoinConfigMapper, CoinConfig> implements CoinConfigService{

    @Autowired
    private CoinService coinService;

    /**
     * 编辑/新增coin币种配置信息
     *
     * @param coinConfig 前端传递的coin币种配置信息json数据
     * @return
     */
    @Override
    public boolean updateOrSave(CoinConfig coinConfig) {
        //判断新增还是编辑
        CoinConfig config = super.getById(coinConfig.getId());
        if (config==null){
            //新增
            Coin coin = coinService.getById(coinConfig.getId());
            if (coin==null){
                throw new IllegalArgumentException("没有该币种");
            }
            coinConfig.setName(coin.getName());
            coinConfig.setCoinType(coin.getType());
            return super.save(coinConfig);
        }else {
            //修改
           return super.updateById(coinConfig);
        }
    }
}
