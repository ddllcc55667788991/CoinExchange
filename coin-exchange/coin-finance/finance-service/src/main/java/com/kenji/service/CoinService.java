package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.Coin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kenji.dto.CoinDto;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:19
 * @Description
 */
public interface CoinService extends IService<Coin> {

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
    Page<Coin> findCoinByPage(Page<Coin> page, String name, String type, Byte status, String title, String walletType);

    /**
     * 通过状态查询所有币种信息
     * @param status    币种状态
     * @return
     */
    List<Coin> findCoinAll(Byte status);

    /**
     * 通过货币名称获取货币
     * @param coinName
     * @return
     */
    Coin getByName(String coinName);


    List<CoinDto> getCoinList(List<Long> coinIds);
}
