package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.dto.CoinDto;
import com.kenji.dto.MarketDto;
import com.kenji.feign.CoinServiceFeign;
import com.kenji.mappers.MarketDtoMappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.Market;
import com.kenji.mapper.MarketMapper;
import com.kenji.service.MarketService;
import org.springframework.util.CollectionUtils;

/**
 * @Author Kenji
 * @Date 2021/8/24 22:23
 * @Description
 */
@Service
public class MarketServiceImpl extends ServiceImpl<MarketMapper, Market> implements MarketService {

    @Autowired
    private CoinServiceFeign coinServiceFeign;

    /**
     * 条件分页查询
     *
     * @param page        分页数据
     * @param tradeAreaId 交易区域id
     * @param coinId      币种id
     * @param status      状态
     * @return
     */
    @Override
    public Page<Market> findByPage(Page<Market> page, Long tradeAreaId, Long coinId, Byte status) {
        return page(page, new LambdaQueryWrapper<Market>()
                .eq(tradeAreaId != null, Market::getTradeAreaId, tradeAreaId)
                .eq(status != null, Market::getStatus, status));
    }

    /**
     * 根据交易区域id查询交易市场
     *
     * @param id
     * @return
     */
    @Override
    public List<Market> getMarketByTradeAreaId(Long id) {
        return list(new LambdaQueryWrapper<Market>().eq(Market::getTradeAreaId, id).eq(Market::getStatus, 1).orderByAsc(Market::getSort));
    }

    /**
     * 根据symbol查询市场
     *
     * @param symbol
     * @return
     */
    @Override
    public Market findBySymbol(String symbol) {
        return getOne(new LambdaQueryWrapper<Market>().eq(Market::getSymbol, symbol).eq(Market::getStatus,1));
    }


    /**
     * 新增交易市场
     * `symbol` '交易对标识',
     * *   `name` '名称',
     * *   `title` '标题'
     * `img`  '市场logo'
     *
     * @param entity
     * @return
     */
    @Override
    public boolean save(Market entity) {
        Long buyCoinId = entity.getBuyCoinId(); //基础货币
        Long sellCoinId = entity.getSellCoinId(); //报价货币
        List<CoinDto> coinDtoList = coinServiceFeign.getCoinList(Arrays.asList(sellCoinId, buyCoinId));
        if (CollectionUtils.isEmpty(coinDtoList)) {
            throw new IllegalArgumentException("货币输入失败");
        }
        CoinDto coinDto = coinDtoList.get(0);
        CoinDto buyCoin = null;
        CoinDto sellCoin = null;
        if (buyCoinId.equals(sellCoinId)) {
            sellCoin = coinDto;
            buyCoin = coinDto;
        } else {
            if (coinDto.getId().equals(sellCoinId)) {
                sellCoin = coinDto;
                buyCoin = coinDtoList.get(1);
            } else {
                buyCoin = coinDto;
                sellCoin = coinDtoList.get(1);
            }
        }
        entity.setSymbol(buyCoin.getName() + sellCoin.getName()); // 交易市场的标识 报价货币基础货币
        entity.setName(buyCoin.getName() + "/" + sellCoin.getName());   // 交易市场的名称  报价货币/基础货币
        entity.setImg(buyCoin.getImg());    // 交易市场的图标
        entity.setTitle(buyCoin.getTitle() + "/" + sellCoin.getTitle());  // 交易市场的标题 报价货币/基础货币
        return super.save(entity);
    }

    /**
     * 使用报价货币及出售货币获取market数据
     *
     * @param buyCoinId
     * @param sellCoinId
     * @return
     */
    @Override
    public MarketDto findCoinById(Long buyCoinId, Long sellCoinId) {
        Market market = getOne(new LambdaQueryWrapper<Market>()
                .eq(Market::getBuyCoinId, buyCoinId)
                .eq(Market::getSellCoinId, sellCoinId)
                .eq(Market::getStatus,1));
        if (market == null) {
            return null;
        }
        MarketDto marketDto = MarketDtoMappers.INSTANCE.toConvertDto(market);
        return marketDto;
    }

    /**
     * 查询全市场数据
     *
     * @return
     */
    @Override
    public List<MarketDto> queryAllMarkets() {
        List<Market> list = list(new LambdaQueryWrapper<Market>()
                .eq(Market::getStatus, 1));
        return MarketDtoMappers.INSTANCE.toConvertDto(list);
    }
}
