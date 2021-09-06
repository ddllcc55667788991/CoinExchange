package com.kenji.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.constant.Constants;
import com.kenji.domain.DepthItemVo;
import com.kenji.domain.Market;
import com.kenji.domain.TurnoverOrder;
import com.kenji.dto.MarketDto;
import com.kenji.dto.TradeMarketDto;
import com.kenji.feign.MarketServiceFeign;
import com.kenji.feign.OrderBookFeignClient;
import com.kenji.mappers.MarketDtoMappers;
import com.kenji.model.R;
import com.kenji.service.MarketService;
import com.kenji.service.TurnoverOrderService;
import com.kenji.vo.DepthsVo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author Kenji
 * @Date 2021/8/24 23:23
 * @Description
 */
@RequestMapping("/markets")
@RestController
@Api(tags = "交易市场接口")
public class MarketsController implements MarketServiceFeign {

    @Autowired
    private MarketService marketService;

    @Autowired
    private TurnoverOrderService turnoverOrderService;

    @Autowired
    private OrderBookFeignClient orderBookFeignClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 条件分页查询
     *
     * @param page        分页数据
     * @param tradeAreaId 交易区域id
     * @param coinId      币种id
     * @param status      状态
     * @return
     */
    @GetMapping
    @ApiOperation("条件分页查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页码"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "status", value = "状态"),
            @ApiImplicitParam(name = "coinId", value = "币种id"),
            @ApiImplicitParam(name = "tradeAreaId", value = "交易区域id"),
    })
    @PreAuthorize("hasAuthority('trade_market_query')")
    public R<Page<Market>> findByPage(@ApiIgnore Page<Market> page, Long tradeAreaId, Long coinId, Byte status) {
        Page<Market> marketPage = marketService.findByPage(page, tradeAreaId, coinId, status);
        return R.ok(marketPage);
    }

    /**
     * 修改交易市场状态
     *
     * @param market
     * @return
     */
    @PostMapping("/setStatus")
    @ApiOperation("修改交易市场状态")
    @ApiImplicitParam(name = "market", value = "交易市场json数据")
    @PreAuthorize("hasAuthority('trade_market_update')")
    public R setStatus(@RequestBody Market market) {
        boolean update = marketService.updateById(market);
        if (update) {
            return R.ok();
        }
        return R.fail("修改状态失败");
    }

    /**
     * 新增交易市场
     * `symbol` '交易对标识',
     * `name` '名称',
     * `title` '标题'
     *
     * @param market
     * @return
     */
    @PostMapping
    @ApiOperation("新增交易市场")
    @ApiImplicitParam(name = "market", value = "交易市场json数据")
    @PreAuthorize("hasAuthority('trade_market_create')")
    public R addMarket(@RequestBody Market market) {
        boolean save = marketService.save(market);
        if (save) {
            return R.ok();
        }
        return R.fail("新增交易市场失败");
    }


    /**
     * 修改交易市场
     *
     * @param market
     * @return
     */
    @PatchMapping
    @ApiOperation("修改交易市场")
    @ApiImplicitParam(name = "market", value = "交易市场json数据")
    @PreAuthorize("hasAuthority('trade_market_update')")
    public R updateMarket(@RequestBody Market market) {
        boolean update = marketService.updateById(market);
        if (update) {
            return R.ok();
        }
        return R.fail("修改交易市场失败");
    }

    /**
     * 查询全市场
     *
     * @return
     */
    @GetMapping("/all")
    @ApiOperation("查询全市场")
    @PreAuthorize("hasAuthority('trade_market_query')")
    public R<List<Market>> getList() {
        return R.ok(marketService.list());
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
        MarketDto marketDto = marketService.findCoinById(buyCoinId, sellCoinId);
        return marketDto;
    }

    /**
     * 通过交易对获取市场信息
     *
     * @param symbol
     * @return
     */
    @Override
    public MarketDto findBySymbol(String symbol) {
        Market market = marketService.findBySymbol(symbol);
        MarketDto marketDto = MarketDtoMappers.INSTANCE.toConvertDto(market);
        return marketDto;
    }

    /**
     * 查询所有的交易市场
     *
     * @return
     */
    @Override
    public List<MarketDto> tradeMarkets() {
        return marketService.queryAllMarkets();
    }

    /**
     * 查询该交易对下的盘口数据
     *
     * @param symbol
     * @param value
     * @return
     */
    @Override
    public String depthData(String symbol, int value) {
        R<DepthsVo> depthVosBySymbol = findDepthVosBySymbol(symbol, value + "");
        DepthsVo data = depthVosBySymbol.getData();
        return JSON.toJSONString(data);
    }

    /**
     * 使用市场ids,查询市场的交易趋势
     *
     * @param marketIds
     * @return
     */
    @Override
    public List<TradeMarketDto> queryMarketsByIds(String marketIds) {
        return null;
    }

    /**
     * 通过交易对查询所有的交易数据
     *
     * @param symbol
     * @return
     */
    @Override
    public String trades(String symbol) {
        return null;
    }

    /**
     * 刷新24小时成交数据
     *
     * @param symbol 交易对
     */
    @Override
    public void refresh24hour(String symbol) {

    }


    @GetMapping("/depth/{symbol}/{dept}")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "symbol", value = "交易对"),
            @ApiImplicitParam(name = "dept", value = "深度类型"),
    })
    public R<DepthsVo> findDepthVosBySymbol(@PathVariable("symbol") String symbol, String dept) {
        //交易市场
        Market market = marketService.findBySymbol(symbol);
        DepthsVo depthsVo = new DepthsVo();
        depthsVo.setCnyPrice(market.getOpenPrice()); //CNY的价格
        depthsVo.setPrice(market.getOpenPrice());   //GCN的价格
        Map<String, List<DepthItemVo>> depthMap = orderBookFeignClient.getDepth(symbol);
        if (!CollectionUtils.isEmpty(depthMap)){
            depthsVo.setAsks(depthMap.get("asks"));
            depthsVo.setBids(depthMap.get("bids"));
        }
        return R.ok(depthsVo);
    }

    /**
     * 成交数据的查询
     * @param symbol
     * @return
     */
    @GetMapping("/trades/{symbol}")
    @ApiOperation("成交数据的查询")
    @ApiImplicitParam(name = "symbol",value = "交易对")
    public R<List<TurnoverOrder>> gerTurnoverOrderBySymbol(@PathVariable("symbol") String symbol){
        List<TurnoverOrder> turnoverOrderList = turnoverOrderService.gerTurnoverOrderBySymbol(symbol);
        return R.ok(turnoverOrderList);
    }

    /**
     * k线查询
     * @param symbol
     * @param type
     * @return
     */
    @GetMapping("/kline/{symbol}/{type}")
    public R<List<JSONArray>> queryKLine(@PathVariable("symbol")String symbol,@PathVariable("type")String type){
        String redisKey = new StringBuffer(Constants.REDIS_KEY_TRADE_KLINE).append(symbol.toLowerCase()).append(":").append(type).toString();
        List<String> klines = redisTemplate.opsForList().range(redisKey, 0, Constants.REDIS_MAX_CACHE_KLINE_SIZE - 1);
        List<JSONArray> result = new ArrayList<>(klines.size());
        if (!CollectionUtils.isEmpty(result)){
            for (String kline : klines) {
                JSONArray objects = JSON.parseArray(kline);
                result.add(objects);
            }
            return R.ok(result);
        }
        return null;
    }
}
