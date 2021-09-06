package com.kenji.controller;

import com.kenji.domain.Coin;
import com.kenji.domain.CoinConfig;
import com.kenji.model.R;
import com.kenji.service.CoinConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Kenji
 * @Date 2021/8/23 11:20
 * @Description
 */
@RequestMapping("/coinConfigs")
@Api(tags = "币种配置接口")
@RestController
public class CoinConfigController {

    @Autowired
    private CoinConfigService coinConfigService;

    /**
     * 根据id查询coin配置信息
     * @param id    币种ID
     * @return
     */
    @GetMapping("/info/{id}")
    @ApiOperation("根据id查询coin配置信息")
    @ApiImplicitParam(name = "id",value = "币种ID")
    public R<CoinConfig> findCoinConfigById(@PathVariable Long id){
        CoinConfig coinConfig = coinConfigService.getById(id);
        return R.ok(coinConfig);
    }


    /**
     * 编辑/新增coin币种配置信息
     * @param coinConfig  前端传递的coin币种配置信息json数据
     * @return
     */
    @PatchMapping
    @ApiOperation(value = "编辑coin币种配置信息")
    @ApiImplicitParam(name = "coin",value = "前端传递的coin币种配置信息json数据")
    public R updateCoin(@RequestBody @Validated CoinConfig coinConfig){
        boolean update = coinConfigService.updateOrSave(coinConfig);
        if (update){
            return R.ok();
        }
        return R.fail("编辑coin币种配置信息失败");
    }


}
