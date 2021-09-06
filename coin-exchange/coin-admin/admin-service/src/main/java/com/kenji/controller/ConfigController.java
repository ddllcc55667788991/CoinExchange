package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.Config;
import com.kenji.model.R;
import com.kenji.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author Kenji
 * @Date 2021/8/19 2:33
 * @Description
 */
@Api(tags = "参数配置管理")
@RequestMapping("/configs")
@RestController
public class ConfigController {

    @Autowired
    private ConfigService configService;


    /**
     * 根据条件分页查询
     * @param page  分页模型
     * @param type  配置规则类型
     * @param code  配置规则代码
     * @param name  配置规则名称
     * @return
     */
    @ApiOperation(value = "根据条件分页查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current",value = "当前页码"),
            @ApiImplicitParam(name = "size",value = "每页大小"),
            @ApiImplicitParam(name = "type",value = "配置规则类型"),
            @ApiImplicitParam(name = "code",value = "配置规则代码"),
            @ApiImplicitParam(name = "name",value = "配置规则名称"),
    })
    @GetMapping
    @PreAuthorize("hasAuthority('config_query')")
    public R<Page<Config>> showConfigByPage(@ApiIgnore Page<Config> page,String type,String code,String name){
        Page<Config> configPage = configService.findConfigByPage(page,type,code,name);
        return R.ok(configPage);
    }

    /**
     * 新增配置
     * @param config  新增的配置
     * @return
     */
    @ApiOperation(value = "新增配置")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "config",value = "新增的配置"),
    })
    @PostMapping
    @PreAuthorize("hasAuthority('config_create')")
    public R<Page<Config>> createConfig(@RequestBody @Validated Config config){
        boolean save = configService.save(config);
        if (save){
            return R.ok();
        }
        return R.fail("新增配置失败");
    }

    /**
     * 修改配置
     * @param config  修改的配置
     * @return
     */
    @ApiOperation(value = "修改配置")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "config",value = "修改的配置"),
    })
    @PatchMapping
    @PreAuthorize("hasAuthority('config_update')")
    public R<Page<Config>> updateConfig(@RequestBody @Validated Config config){
        boolean update = configService.updateById(config);
        if (update){
            return R.ok();
        }
        return R.fail("新增配置失败");
    }
}
