package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.WebConfig;
import com.kenji.model.R;
import com.kenji.service.WebConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/19 0:04
 * @Description
 */
@Api(tags = "资源配置")
@RestController
@RequestMapping("/webConfigs")
public class WebConfigController {

    @Autowired
    private WebConfigService webConfigService;

    /**
     * 根据条件分页查询
     * @param page  分页数据
     * @param type  资源类型
     * @param name  资源名称
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAuthority('web_config_query')")
    @ApiOperation(value = "根据条件分页查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current",value = "当前页码"),
            @ApiImplicitParam(name = "size",value = "每页大小"),
            @ApiImplicitParam(name = "type",value = "资源类型"),
            @ApiImplicitParam(name = "name",value = "资源名称"),
    })
    public R<Page<WebConfig>> showWebConfigByPage(@ApiIgnore Page<WebConfig> page, String type, String name){
        Page<WebConfig> webConfigPage = webConfigService.queryByPage(page, type, name);
        return R.fail(webConfigPage);
    }

    /**
     * 新增资源配置
     * @param webConfig  新增的资源
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAuthority('web_config_create')")
    @ApiOperation(value = "新增资源配置")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "webConfig",value = "新增资源的json数据"),
    })
    public R<Page<WebConfig>> createWebConfigByPage(@Validated @RequestBody WebConfig webConfig){
        boolean save = webConfigService.save(webConfig);
        if (save){
            return R.ok();
        }
        return R.fail("新增失败");
    }


    /**
     * 修改资源配置
     * @param webConfig  修改的资源
     * @return
     */
    @PatchMapping
    @PreAuthorize("hasAuthority('web_config_update')")
    @ApiOperation(value = "修改资源配置")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "webConfig",value = "修改资源的json数据"),
    })
    public R<Page<WebConfig>> updateWebConfigByPage(@Validated @RequestBody WebConfig webConfig){
        boolean update = webConfigService.updateById(webConfig);
        if (update){
            return R.ok();
        }
        return R.fail("修改失败");
    }

    /**
     * 删除资源配置
     * @param ids  删除的资源
     * @return
     */
    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('web_config_delete')")
    @ApiOperation(value = "删除资源配置")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "webConfig",value = "删除资源的json数据"),
    })
    public R<Page<WebConfig>> deleteWebConfigByPage(@RequestBody String[] ids){
        boolean delete = webConfigService.removeByIds(Arrays.asList(ids));
        if (delete){
            return R.ok();
        }
        return R.fail("删除失败");
    }

    /**
     * 查询所有banner图
     * @return
     */
    @GetMapping("/banners")
    @ApiOperation("查询所有banner图")
    public R<List<WebConfig>> getBanners(){
        List<WebConfig> bannerList = webConfigService.getBanner();
        return R.ok(bannerList);
    }
}
