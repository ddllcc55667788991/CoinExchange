package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.Market;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kenji.dto.MarketDto;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/24 22:23
 * @Description
 */
public interface MarketService extends IService<Market> {

    /**
     * 条件分页查询
     * @param page  分页数据
     * @param tradeAreaId 交易区域id
     * @param coinId     币种id
     * @param status 状态
     * @return
     */
    Page<Market> findByPage(Page<Market> page, Long tradeAreaId, Long coinId, Byte status);

    /**
     * 根据交易区域id查询交易市场
     * @param id
     * @return
     */
    List<Market> getMarketByTradeAreaId(Long id);

    /**
     * 根据symbol查询市场
     * @param symbol
     * @return
     */
    Market findBySymbol(String symbol);

    /**
     * 使用报价货币及出售货币获取market数据
     *
     * @param buyCoinId
     * @param sellCoinId
     * @return
     */
    MarketDto findCoinById(Long buyCoinId, Long sellCoinId);

    /**
     * 查询全市场数据
     * @return
     */
    List<MarketDto> queryAllMarkets();


}
