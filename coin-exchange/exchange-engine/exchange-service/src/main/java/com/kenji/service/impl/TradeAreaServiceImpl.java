package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.Market;
import com.kenji.domain.UserFavoriteMarket;
import com.kenji.dto.CoinDto;
import com.kenji.feign.CoinServiceFeign;
import com.kenji.service.MarketService;
import com.kenji.service.UserFavoriteMarketService;
import com.kenji.vo.MergeDeptVo;
import com.kenji.vo.TradeAreaMarketVo;
import com.kenji.vo.TradeMarketVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.TradeAreaMapper;
import com.kenji.domain.TradeArea;
import com.kenji.service.TradeAreaService;
import org.springframework.util.CollectionUtils;

/**
 * @Author Kenji
 * @Date 2021/8/24 22:23
 * @Description
 */
@Service
public class TradeAreaServiceImpl extends ServiceImpl<TradeAreaMapper, TradeArea> implements TradeAreaService {


    @Autowired
    private TradeAreaService tradeAreaService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private CoinServiceFeign coinServiceFeign;

    @Autowired
    private UserFavoriteMarketService userFavoriteMarketService;
    /**
     * 条件分页查询交易区域
     *
     * @param page   分页数据
     * @param name   名称
     * @param status 状态
     * @return
     */
    @Override
    public Page<TradeArea> findByPage(Page<TradeArea> page, String name, Byte status) {
        return page(page, new LambdaQueryWrapper<TradeArea>()
                .eq(!StringUtils.isEmpty(name), TradeArea::getName, name)
                .eq(status!=null,TradeArea::getStatus,status));
    }

    /**
     * 获取所有已启用的交易区域
     *
     * @param status 状态
     * @return
     */
    @Override
    public List<TradeArea> getAll(Byte status) {
        return list(new LambdaQueryWrapper<TradeArea>()
                .eq(status!=null,TradeArea::getStatus,status));
    }

    /**
     * 查询交易区域，以及区域下的市场
     *
     * @return
     */
    @Override
    public List<TradeAreaMarketVo> getTradeAreaMarkets() {
        //查询所有交易区域
        List<TradeArea> tradeAreaList = tradeAreaService.list(new LambdaQueryWrapper<TradeArea>().eq(TradeArea::getStatus, 1).orderByAsc(TradeArea::getSort));
        if (CollectionUtils.isEmpty(tradeAreaList)){
            return Collections.EMPTY_LIST;
        }
        List<TradeAreaMarketVo> tradeAreaMarketVos = new ArrayList<>();
        for (TradeArea tradeArea : tradeAreaList) {
            //查询交易区域下的市场
            List<Market> marketList =marketService.getMarketByTradeAreaId(tradeArea.getId());
            if (!CollectionUtils.isEmpty(marketList)){
                TradeAreaMarketVo tradeAreaMarketVo = new TradeAreaMarketVo();
                tradeAreaMarketVo.setAreaName(tradeArea.getName());
                tradeAreaMarketVo.setMarkets(market2MarketVo(marketList));
                tradeAreaMarketVos.add(tradeAreaMarketVo);
            }
        }
        return tradeAreaMarketVos;
    }



    /**
     * 将market转化为TradeMarketVo
     * @param marketList
     * @return
     */
    private List<TradeMarketVo> market2MarketVo(List<Market> marketList) {
        return marketList.stream().map(market -> toConvertVo(market)).collect(Collectors.toList());

    }

    /**
     * 将market转化为TradeMarketVo
     * @param market
     * @return
     */
    private TradeMarketVo toConvertVo(Market market) {
        TradeMarketVo tradeMarketVo = new TradeMarketVo();
        tradeMarketVo.setImage(market.getImg());
        tradeMarketVo.setName(market.getName());
        tradeMarketVo.setSymbol(market.getSymbol());

        //TODO
        //价格设置
        tradeMarketVo.setHigh(market.getOpenPrice());   //OpenPrice开盘价格
        tradeMarketVo.setLow(market.getOpenPrice());    //获取K线的数据
        tradeMarketVo.setPrice(market.getOpenPrice());  //获取K线的数据
        tradeMarketVo.setCnyPrice(market.getOpenPrice());   //计算获得
        tradeMarketVo.setPriceScale(market.getPriceScale());    //价格保存的小数点

        //获取报价货币
        @NotNull Long buyCoinId = market.getBuyCoinId();
        List<CoinDto> coinList = coinServiceFeign.getCoinList(Arrays.asList(buyCoinId));
        if (CollectionUtils.isEmpty(coinList)||coinList.size() >1){
            throw new IllegalArgumentException("报价货币错误");
        }
        CoinDto coinDto = coinList.get(0);
        tradeMarketVo.setPriceUnit(coinDto.getName());  //报价货币的名称

        //交易的额度
        tradeMarketVo.setTradeMax(market.getTradeMax());
        tradeMarketVo.setTradeMin(market.getTradeMin());

        //下单的数据限制
        tradeMarketVo.setNumMin(market.getNumMin());
        tradeMarketVo.setNumMax(market.getNumMax());

        //手续费的设置
        tradeMarketVo.setSellFeeRate(market.getFeeSell());
        tradeMarketVo.setBuyFeeRate(market.getFeeBuy());

        //价格的小数的位数
        tradeMarketVo.setNumScale(market.getSort());

        //排序
        tradeMarketVo.setSort(market.getSort());

        //设置交易量
        tradeMarketVo.setVolume(BigDecimal.ZERO);   //日的总交易量
        tradeMarketVo.setAmount(BigDecimal.ZERO);   //日的总交易额

        //设置涨幅
        tradeMarketVo.setChange(0.00);

        //设置合并的深度
        tradeMarketVo.setMergeDepth(getMergeDeths(market.getMergeDepth()));
        return tradeMarketVo;
    }

    /**
     * 获取合并的深度
     * @param mergeDepth
     * @return
     */
    private List<MergeDeptVo> getMergeDeths(String mergeDepth) {
        String[] split = mergeDepth.split(",");
        if (split.length!=3){
            throw new IllegalArgumentException("合并深度不合法");
        }
        //6(1/1000000),5(10000),4(10000)
        //最小深度
        MergeDeptVo minMergeDeptVo = new MergeDeptVo();
        minMergeDeptVo.setMergeType("MIN");
        minMergeDeptVo.setValue(getDeptValue(Integer.valueOf(split[0])));

        MergeDeptVo defaultMergeDeptVo = new MergeDeptVo();
        defaultMergeDeptVo.setMergeType("DEFAULT");
        defaultMergeDeptVo.setValue(getDeptValue(Integer.valueOf(split[1])));

        MergeDeptVo maxMergeDeptVo = new MergeDeptVo();
        maxMergeDeptVo.setMergeType("MAX");
        maxMergeDeptVo.setValue(getDeptValue(Integer.valueOf(split[2])));

        List<MergeDeptVo> mergeDeptVo = new ArrayList<>();
        mergeDeptVo.add(minMergeDeptVo);
        mergeDeptVo.add(maxMergeDeptVo);
        mergeDeptVo.add(defaultMergeDeptVo);
        return mergeDeptVo;
    }

    //6(1/1000000)
    private BigDecimal getDeptValue(Integer scale) {
        BigDecimal bigDecimal = new BigDecimal(Math.pow(10, scale));    //Math.pow(10,scale) 指数操作
        return BigDecimal.ONE.divide(bigDecimal).setScale(scale, RoundingMode.HALF_UP); //1/10^n
    }

    /**
     * 查询用户自选交易市场
     *
     * @param userid
     * @return
     */
    @Override
    public List<TradeAreaMarketVo> getUserFavorite(Long userid) {
        List<UserFavoriteMarket> userFavoriteMarkets = userFavoriteMarketService.list(new LambdaQueryWrapper<UserFavoriteMarket>()
                .eq(UserFavoriteMarket::getUserId, userid));
        if (CollectionUtils.isEmpty(userFavoriteMarkets)){
            throw new IllegalArgumentException("用户ID不正确");
        }
        List<Long> marketId = userFavoriteMarkets.stream().map(UserFavoriteMarket::getMarketId).collect(Collectors.toList());
        List<Market> marketList = marketService.listByIds(marketId);
        TradeAreaMarketVo tradeAreaMarketVo = new TradeAreaMarketVo();
        tradeAreaMarketVo.setAreaName("自选");
        List<TradeMarketVo> tradeMarketVos = market2MarketVo(marketList);
        tradeAreaMarketVo.setMarkets(tradeMarketVos);
        return Arrays.asList(tradeAreaMarketVo);
    }
}
