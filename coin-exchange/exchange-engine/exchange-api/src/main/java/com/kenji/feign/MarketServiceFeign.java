package com.kenji.feign;

import com.kenji.config.feign.OAuth2FeignConfig;
import com.kenji.dto.MarketDto;
import com.kenji.dto.TradeAreaDto;
import com.kenji.dto.TradeMarketDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/25 19:10
 * @Description
 */
@FeignClient(path = "/markets",name = "exchange-service",configuration = OAuth2FeignConfig.class,contextId = "marketServiceFeign")
public interface MarketServiceFeign {

    /**
     * 使用报价货币及出售货币获取market数据
     * @param buyCoinId
     * @param sellCoinId
     * @return
     */
    @GetMapping("/getMarket")
    MarketDto findCoinById(@RequestParam("buyCoinId") Long buyCoinId,@RequestParam("sellCoinId")Long sellCoinId);

    /**
     * 通过交易对获取市场信息
     * @param symbol
     * @return
     */
    @GetMapping("/findBySymbol")
    MarketDto findBySymbol(@RequestParam("symbol") String symbol);


    /**
     * 查询所有的交易市场
     * @return
     */
    @GetMapping("/tradeMarkets")
    List<MarketDto> tradeMarkets();


    /**
     * 查询该交易对下的盘口数据
     * @param symbol
     * @param value
     * @return
     */
    @GetMapping("/depthData")
    String depthData(@RequestParam("sysmbol") String symbol, @RequestParam("type") int value);

    /**
     * 使用市场ids,查询市场的交易趋势
     * @param marketIds
     * @return
     */
    @GetMapping("/queryMarketsByIds")
    List<TradeMarketDto> queryMarketsByIds(@RequestParam("marketIds") String marketIds);

    /**
     * 通过交易对查询所有的交易数据
     * @param symbol
     * @return
     */
    @GetMapping("/trades")
    String trades(String symbol);

    /**
     * 刷新24小时成交数据
     * @param symbol    交易对
     */
    @GetMapping(value = "/refresh_24hour")
    void refresh24hour(@RequestParam("symbol") String symbol);
}
