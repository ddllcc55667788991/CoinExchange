package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.EntrustOrder;
import com.kenji.model.R;
import com.kenji.param.OrderParam;
import com.kenji.service.EntrustOrderService;
import com.kenji.vo.TradeEntrustOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author Kenji
 * @Date 2021/8/25 15:35
 * @Description
 */
@RequestMapping("/entrustOrders")
@RestController
@Api(tags = "委托记录api")
public class EntrustOrdersController {

    @Autowired
    private EntrustOrderService entrustOrderService;

    /**
     * 分页查询委托记录
     *
     * @param page   分页数据
     * @param symbol 交易对
     * @param type   买卖类型
     * @return
     */
    @GetMapping
    @ApiOperation(value = "分页查询委托记录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页码"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "symbol", value = "交易对"),
            @ApiImplicitParam(name = "type", value = "买卖类型"),
    })
    public R<Page<EntrustOrder>> findByPage(@ApiIgnore Page<EntrustOrder> page, String symbol, Byte type) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<EntrustOrder> entrustOrderPage = entrustOrderService.findByPage(page, userId, type, symbol);
        return R.ok(entrustOrderPage);
    }

    /**
     * 查询历史委托单记录
     *
     * @param page
     * @param symbol
     * @return
     */
    @GetMapping("/history/{symbol}")
    @ApiOperation(value = "查询历史委托单记录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页码"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
    })
    public R<Page<TradeEntrustOrderVo>> historyEntrustOrder(@ApiIgnore Page<EntrustOrder> page, @PathVariable("symbol") String symbol) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<TradeEntrustOrderVo> tradeEntrustOrderVoPage = entrustOrderService.getHistoryEntrustOrder(page, userId, symbol);
        return R.ok(tradeEntrustOrderVoPage);
    }

    /**
     * 查询未委托单记录
     *
     * @param page
     * @param symbol
     * @return
     */
    @GetMapping("/{symbol}")
    @ApiOperation(value = "查询未委托单记录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页码"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
    })
    public R<Page<TradeEntrustOrderVo>> EntrustOrder(@ApiIgnore Page<EntrustOrder> page, @PathVariable("symbol") String symbol) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<TradeEntrustOrderVo> tradeEntrustOrderVoPage = entrustOrderService.getEntrustOrder(page, userId, symbol);
        return R.ok(tradeEntrustOrderVoPage);
    }


    /**
     * 委托单的下单操作
     *
     * @param orderParam
     * @return
     */
    @PostMapping
    @ApiOperation(value = "委托单的下单操作")
    @ApiImplicitParam(name = "orderParam", value = "orderParam 的json数据")
    public R createEntrustOrder(@RequestBody OrderParam orderParam) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Boolean isOk = entrustOrderService.createEntrustOrder(userId, orderParam);
        return isOk ? R.ok() : R.fail("创建失败");
    }


    /**
     * 取消委托单
     *
     * @param entrustOrderId
     * @return
     */
    @DeleteMapping("/{entrustOrderId}")
    @ApiOperation(value = "取消委托单")
    @ApiImplicitParam(name = "entrustOrderId", value = "取消委托单的id")
    public R cancelEntrustOrder(@PathVariable("entrustOrderId") Long entrustOrderId) {
        entrustOrderService.cancelEntrustOrder(entrustOrderId);
        return R.ok();

    }
}
