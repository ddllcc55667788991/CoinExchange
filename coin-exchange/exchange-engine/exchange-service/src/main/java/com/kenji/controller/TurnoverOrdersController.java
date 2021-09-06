package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.TurnoverOrder;
import com.kenji.model.R;
import com.kenji.service.TurnoverOrderService;
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
 * @Date 2021/8/25 16:02
 * @Description
 */
@RestController
@RequestMapping("/turnoverOrders")
@Api(tags = "成交记录api")
public class TurnoverOrdersController {

    @Autowired
    private TurnoverOrderService turnoverOrderService;

    /**
     * 分页查询成交记录
     * @param page  分页数据
     * @param symbol  交易对
     * @param type 交易类型
     * @return
     */
    @PostMapping
    @ApiOperation("分页查询成交记录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current",value = "当前页码"),
            @ApiImplicitParam(name = "size",value = "每页大小"),
            @ApiImplicitParam(name = "symbol",value = "交易对"),
            @ApiImplicitParam(name = "type",value = "交易类型"),
    })
    public R<Page<TurnoverOrder>> fingByPage(@RequestParam @ApiIgnore Page<TurnoverOrder> page,@RequestParam String symbol,@RequestParam Byte type){
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<TurnoverOrder> turnoverOrderPage = turnoverOrderService.findByPage(userId,page,symbol,type);
        return R.ok(turnoverOrderPage);
    }
}
