package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.CoinType;
import com.kenji.model.R;
import com.kenji.service.CoinTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/23 1:04
 * @Description
 */
@RestController
@RequestMapping("/coinTypes")
@Api(tags = "币种类型配置")
public class CoinTypesController {

    @Autowired
    private CoinTypeService coinTypeService;

    /**
     * 条件分页查询币种类型
     *
     * @param page 分页数据
     * @param code 币种类型
     * @return
     */
    @GetMapping
    @ApiOperation(value = "条件分页查询币种类型")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页码"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "code", value = "币种类型"),
    })
    @PreAuthorize("hasAuthority('trade_coin_type_query')")
    public R<Page<CoinType>> showCoinTypeByPage(@ApiIgnore Page<CoinType> page, String code) {
        Page<CoinType> coinTypePage = coinTypeService.showCoinTypeByPage(page, code);
        return R.ok(coinTypePage);
    }

    /**
     * 新增币种类型
     * @param coinType
     * @return
     */
    @PostMapping
    @ApiOperation(value = "增加币种类型")
    @ApiImplicitParam(name = "coinType",value = "币种类型")
    @PreAuthorize("hasAuthority('trade_coin_type_create')")
    public R addCoinType(@RequestBody @Validated CoinType coinType) {
        boolean save = coinTypeService.save(coinType);
        if (save){
            return R.ok();
        }
        return R.fail("新增币种失败");
    }

    /**
     * 编辑币种类型
     * @param coinType
     * @return
     */
    @PatchMapping
    @ApiOperation(value = "编辑币种类型")
    @ApiImplicitParam(name = "coinType",value = "币种类型")
    @PreAuthorize("hasAuthority('trade_coin_type_update')")
    public R updateCoinType(@RequestBody @Validated CoinType coinType) {
        boolean update = coinTypeService.updateById(coinType);
        if (update){
            return R.ok();
        }
        return R.fail("编辑币种失败");
    }

    /**
     * 编辑币种类型状态
     * @param coinType
     * @return
     */
    @PostMapping("/setStatus")
    @ApiOperation(value = "编辑币种类型状态")
    @ApiImplicitParam(name = "coinType",value = "币种类型")
    @PreAuthorize("hasAuthority('trade_coin_type_update')")
    public R updateCoinTypeStatus(@RequestBody CoinType coinType) {
        boolean update = coinTypeService.updateById(coinType);
        if (update){
            return R.ok();
        }
        return R.fail("修改币种状态失败");
    }

    /**
     * 查询币种类型
     * @param status    币种状态
     * @return
     */
    @GetMapping("/all")
    @ApiOperation(value = "查询币种类型")
    @ApiImplicitParam(value = "status",name = "币种状态")
    @PreAuthorize("hasAuthority('trade_coin_type_query')")
    public R<List<CoinType>> listAll(Byte status){
        List<CoinType> coinTypeList = coinTypeService.listAll(status);
        return R.ok(coinTypeList);
    }
}
