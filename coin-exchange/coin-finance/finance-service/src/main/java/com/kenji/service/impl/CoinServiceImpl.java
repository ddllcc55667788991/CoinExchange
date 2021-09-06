package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.dto.CoinDto;
import com.kenji.mappers.CoinDtoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.CoinMapper;
import com.kenji.domain.Coin;
import com.kenji.service.CoinService;
import org.springframework.util.CollectionUtils;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:19
 * @Description
 */
@Service
public class CoinServiceImpl extends ServiceImpl<CoinMapper, Coin> implements CoinService {

    /**
     * 根据条件分页查询币种配置
     *
     * @param page       分页数据
     * @param name       币种名称
     * @param type       币种类型
     * @param status     状态
     * @param title      标题
     * @param walletType 钱包类型
     * @return
     */
    @Override
    public Page<Coin> findCoinByPage(Page<Coin> page, String name, String type, Byte status, String title, String walletType) {
        page.addOrder(OrderItem.desc("last_update_time"));
        return super.page(page, new LambdaQueryWrapper<Coin>()
                .like(!StringUtils.isEmpty(name), Coin::getName, name)
                .like(!StringUtils.isEmpty(title), Coin::getTitle, title)
                .eq(!StringUtils.isEmpty(type), Coin::getType, type)
                .eq(!StringUtils.isEmpty(walletType), Coin::getWallet, walletType)
                .eq(status!=null, Coin::getStatus, status));
    }

    /**
     * 通过状态查询所有币种信息
     *
     * @param status 币种状态
     * @return
     */
    @Override
    public List<Coin> findCoinAll(Byte status) {
        return list(new LambdaQueryWrapper<Coin>().eq(status != null,Coin::getStatus,status));
    }

    /**
     * 通过货币名称获取货币
     *
     * @param coinName
     * @return
     */
    @Override
    public Coin getByName(String coinName) {
        return getOne(new LambdaQueryWrapper<Coin>().eq(!StringUtils.isEmpty(coinName),Coin::getName,coinName));
    }

    @Override
    public List<CoinDto> getCoinList(List<Long> coinIds) {
        if (CollectionUtils.isEmpty(coinIds)){
            return Collections.emptyList();
        }
        List<Coin> coinList = super.listByIds(coinIds);
        if (CollectionUtils.isEmpty(coinList)){
            return Collections.emptyList();
        }
        List<CoinDto> coinDtoList = CoinDtoMapper.INSTANCE.toConvertDto(coinList);
        return coinDtoList;
    }


}
