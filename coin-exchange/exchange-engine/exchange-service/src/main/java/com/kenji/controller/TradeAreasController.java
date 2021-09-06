package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.TradeArea;
import com.kenji.model.R;
import com.kenji.service.TradeAreaService;
import com.kenji.vo.TradeAreaMarketVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/24 22:46
 * @Description
 */
@RequestMapping("/tradeAreas")
@RestController
@Api("交易区域的数据接口")
public class TradeAreasController {

    @Autowired
    private TradeAreaService tradeAreaService;

    /**
     * 条件分页查询交易区域
     * @param page  分页数据
     * @param name  名称
     * @param status    状态
     * @return
     */
    @GetMapping
    @ApiOperation("条件分页查询交易区域")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current",value = "当前页码"),
            @ApiImplicitParam(name = "size",value = "每页大小"),
            @ApiImplicitParam(name = "name",value = "名称"),
            @ApiImplicitParam(name = "status",value = "状态"),
    })
    @PreAuthorize("hasAuthority('trade_area_query')")
    public R<Page<TradeArea>> findByPage(@ApiIgnore Page<TradeArea> page,String name,Byte status){
        Page<TradeArea> tradeAreaPage = tradeAreaService.findByPage(page,name,status);
        return R.ok(tradeAreaPage);
    }

    /**
     * 新增交易区域
     * @param tradeArea tradeArea的json数据
     * @return
     */
    @PostMapping
    @ApiOperation("新增交易区域")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "tradeArea",value = "tradeArea的json数据")
    })
    @PreAuthorize("hasAuthority('trade_area_create')")
    public R addTradeArea(@RequestBody @Validated TradeArea tradeArea){
        boolean save = tradeAreaService.save(tradeArea);
        if (save){
            return R.ok();
        }
        return R.fail("新增失败");
    }

    /**
     * 修改交易区域状态
     * @param tradeArea
     * @return
     */
    @PostMapping("/status")
    @ApiOperation("修改交易区域状态")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "tradeArea",value = "tradeArea的json数据")
    })
    @PreAuthorize("hasAuthority('trade_area_update')")
    public R updateTradeAreaStatus(@RequestBody  TradeArea tradeArea){
        boolean update = tradeAreaService.updateById(tradeArea);
        if (update){
            return R.ok();
        }
        return R.fail("修改失败");
    }

    /**
     * 修改交易区域
     * @param tradeArea
     * @return
     */
    @PatchMapping
    @ApiOperation("修改交易区域")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "tradeArea",value = "tradeArea的json数据")
    })
    @PreAuthorize("hasAuthority('trade_area_update')")
    public R updateTradeArea(@RequestBody @Validated  TradeArea tradeArea){
        boolean update = tradeAreaService.updateById(tradeArea);
        if (update){
            return R.ok();
        }
        return R.fail("修改失败");
    }

    /**
     * 获取所有已启用的交易区域
     * @param status 状态
     * @return
     */
    @GetMapping("/all")
    @ApiOperation("获取所有已启用的交易区域")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "status",value = "状态")
    })
    @PreAuthorize("hasAuthority('trade_area_query')")
    public R<List<TradeArea>> getAll(Byte status){
        List<TradeArea> tradeAreaList = tradeAreaService.getAll(status);
        return R.ok(tradeAreaList);
    }

    /**
     * 查询交易区域，以及区域下的市场
     * @return
     */
    @GetMapping("/markets")
    @ApiOperation("查询交易区域，以及区域下的市场")
    public R<List<TradeAreaMarketVo>> getTradeAreaMarkets(){
        List<TradeAreaMarketVo> tradeAreaMarketVoList = tradeAreaService.getTradeAreaMarkets();
        return R.ok(tradeAreaMarketVoList);
    }

    /**
     * 查询用户自选交易市场
     * @return
     */
    @GetMapping("/market/favorite")
    public R<List<TradeAreaMarketVo>> getUserFavorite(){
        Long userid = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        List<TradeAreaMarketVo> tradeAreaMarketVoList = tradeAreaService.getUserFavorite(userid);
        return R.ok(tradeAreaMarketVoList);
    }
}
