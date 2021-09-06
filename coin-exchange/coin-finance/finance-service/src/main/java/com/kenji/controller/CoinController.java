package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.Coin;
import com.kenji.domain.CoinServer;
import com.kenji.dto.CoinDto;
import com.kenji.feign.CoinServiceFeign;
import com.kenji.model.R;
import com.kenji.service.CoinService;
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
 * @Date 2021/8/23 8:41
 * @Description
 */
@Api(tags = "币种配置接口")
@RestController
@RequestMapping("/coins")
public class CoinController implements CoinServiceFeign {

    @Autowired
    private CoinService coinService;

    /**
     * 根据条件分页查询币种配置
     *
     * @param page       分页数据
     * @param name       币种名称
     * @param type       币种类型
     * @param status     状态
     * @param title      标题
     * @param walletType 钱包类型
     * @return
     */
    @GetMapping
    @ApiOperation(value = "根据条件分页查询币种配置")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页码"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "name", value = "币种名称"),
            @ApiImplicitParam(name = "type", value = "币种类型"),
            @ApiImplicitParam(name = "status", value = "状态"),
            @ApiImplicitParam(name = "title", value = "标题"),
            @ApiImplicitParam(name = "walletType", value = "钱包类型"),
    })
    @PreAuthorize("hasAuthority('trade_coin_query')")
    public R<Page<Coin>> findCoinByPage(@ApiIgnore Page<Coin> page, String name, String type, Byte status, String title, @RequestParam(value = "wallet_type", required = false) String walletType) {
        Page<Coin> coinPage = coinService.findCoinByPage(page, name, type, status, title, walletType);
        return R.ok(coinPage);
    }

    /**
     * 通过状态查询所有币种信息
     *
     * @param status 币种状态
     * @return
     */
    @GetMapping("/all")
    @ApiOperation(value = "通过状态查询所有币种信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "status", value = "币种状态")
    })
    public R<List<Coin>> findCoinAll(Byte status) {
        List<Coin> coinList = coinService.findCoinAll(status);
        return R.ok(coinList);
    }

    /**
     * 根据币种查询币种信息
     *
     * @param id 币种ID
     * @return
     */
    @GetMapping("/info/{id}")
    @ApiOperation(value = "根据币种查询币种信息")
    @ApiImplicitParam(name = "id", value = "币种ID")
    @PreAuthorize("hasAuthority('trade_coin_query')")
    public R<Coin> findById(@PathVariable Long id) {
        Coin coin = coinService.getById(id);
        return R.ok(coin);
    }

    /**
     * 编辑coin币种信息
     *
     * @param coin 前端传递的coin币种信息json数据
     * @return
     */
    @PatchMapping
    @ApiOperation(value = "编辑coin币种信息")
    @ApiImplicitParam(name = "coin", value = "前端传递的coin币种信息json数据")
    @PreAuthorize("hasAuthority('trade_coin_update')")
    public R updateCoin(@RequestBody @Validated Coin coin) {
        boolean update = coinService.updateById(coin);
        if (update) {
            return R.ok();
        }
        return R.fail("编辑币种信息失败");
    }


    /**
     * 新增coin币种信息
     *
     * @param coin 前端传递的coin币种信息json数据
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增coin币种信息")
    @ApiImplicitParam(name = "coin", value = "前端传递的coin币种信息json数据")
    public R<Coin> addCoin(@RequestBody @Validated Coin coin) {
        coin.setStatus((byte) 1);
        boolean save = coinService.save(coin);
        //mybatis-plus，新增成功后，会执行一个sql语句查询，查询的结果是id，之后把id设置给coin
        if (save) {
            return R.ok(coin);
        }
        return R.fail("新增币种信息失败");
    }

    /**
     * 启动或禁用coin状态
     *
     * @param coin 前端传来coin的json数据
     * @return
     */
    @PostMapping("/setStatus")
    @ApiOperation(value = "启动或禁用coin状态")
    @ApiImplicitParam(name = "coin", value = "前端传来coin的json数据")
    public R updateCoinStatus(@RequestBody Coin coin) {
        boolean update = coinService.updateById(coin);
        if (update) {
            return R.ok();
        }
        return R.fail("改变币种状态失败");
    }

    @Override
    public List<CoinDto> getCoinList(List<Long> coinIds) {
        List<CoinDto> coinDtoList = coinService.getCoinList(coinIds);
        return coinDtoList;
    }
}
