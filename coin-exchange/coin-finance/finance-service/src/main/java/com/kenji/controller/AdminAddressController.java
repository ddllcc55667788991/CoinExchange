package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.AdminAddress;
import com.kenji.model.R;
import com.kenji.service.AdminAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author Kenji
 * @Date 2021/8/23 11:28
 * @Description
 */
@RestController
@RequestMapping("/adminAddress")
@Api(tags = "钱包归集接口")
public class AdminAddressController {

    @Autowired
    private AdminAddressService adminAddressService;

    /**
     * 分页查询钱包归集地址
     * @param page  分页大小
     * @param coinId    币种ID
     * @return
     */
    @GetMapping
    @ApiOperation("分页查询钱包归集地址")
    @ApiImplicitParams(value = {
            @ApiImplicitParam( name = "current",value = "当前页码"),
            @ApiImplicitParam( name = "size",value = "每页大小"),
            @ApiImplicitParam( name = "coinId",value = "币种ID"),
    })
    public R<Page<AdminAddress>> findAdminAddressByPage(@ApiIgnore Page<AdminAddress> page,Long coinId){
        Page<AdminAddress> adminAddressPage = adminAddressService.findAdminAddressByPage(page,coinId);
        return R.ok(adminAddressPage);
    }

    /**
     * 新增钱包归集地址
     * @param adminAddress 前端传来的钱包归集地址
     * @return
     */
    @PostMapping
    @ApiOperation("新增钱包归集地址")
    @ApiImplicitParam(name = "adminAddress",value = "前端传来的钱包归集地址")
    public R addAdminAddress(@RequestBody @Validated AdminAddress adminAddress){
            Boolean isOk = adminAddressService.addAdminAddress(adminAddress);
            if (isOk){
                return R.ok();
            }
            return R.fail("新增地址归集地址失败");
    }

    /**
     * 修改钱包归集地址
     * @param adminAddress 前端传来的钱包归集地址
     * @return
     */
    @PatchMapping
    @ApiOperation("修改钱包归集地址")
    @ApiImplicitParam(name = "adminAddress",value = "前端传来的钱包归集地址")
    public R updateAdminAddress(@RequestBody @Validated AdminAddress adminAddress){
        Boolean isOk = adminAddressService.updateById(adminAddress);
        if (isOk){
            return R.ok();
        }
        return R.fail("修改地址归集地址失败");
    }
}
