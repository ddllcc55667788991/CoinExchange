package com.kenji.controller;

import cn.hutool.core.lang.Snowflake;
import com.kenji.domain.Market;
import com.kenji.domain.UserFavoriteMarket;
import com.kenji.model.R;
import com.kenji.service.MarketService;
import com.kenji.service.UserFavoriteMarketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Kenji
 * @Date 2021/8/25 14:54
 * @Description
 */
@RestController
@RequestMapping("/userFavorites")
@Api(tags = "用户交易市场收藏")
public class UserFavoritesController {

    @Autowired
    private Snowflake snowflake;

    @Autowired
    private MarketService marketService;

    @Autowired
    private UserFavoriteMarketService userFavoriteMarketService;

    /**
     * 增加收藏
     * @param userFavoriteMarket
     * @return
     */
    @PostMapping("/addFavorite")
    @ApiOperation(value = "增加收藏")
    @ApiImplicitParam(name = "userFavoriteMarket",value = "用户交易市场收藏json数据")
    public R addFavorite(@RequestBody UserFavoriteMarket userFavoriteMarket){
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Market market = marketService.findBySymbol(userFavoriteMarket.getSymbol());
        userFavoriteMarket.setId(snowflake.nextId());
        userFavoriteMarket.setMarketId(market.getId());
        userFavoriteMarket.setUserId(userId);
        userFavoriteMarket.setSymbol(userFavoriteMarket.getSymbol());
        boolean save = userFavoriteMarketService.save(userFavoriteMarket);
        if (save){
            return R.ok();
        }
        return R.fail("增加收藏失败");
    }

    /**
     * 取消收藏
     * @param symbol
     * @return
     */
    @DeleteMapping("{symbol}")
    @ApiOperation(value = "取消收藏")
    @ApiImplicitParam(name = "symbol",value = "要取消的交易市场交易对")
    public R deleteFavorite(@PathVariable String symbol){
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Market market = marketService.findBySymbol(symbol);
        Long marketId = market.getId();
        Boolean delete = userFavoriteMarketService.deleteFavorite(userId,marketId);
        if (delete){
            return R.ok();
        }
        return R.fail("取消收藏失败");
    }
}
