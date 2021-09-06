package com.kenji.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.AdminBank;
import com.kenji.dto.AdminBankDto;
import com.kenji.feign.AdminServiceFeign;
import com.kenji.model.R;
import com.kenji.service.AdminBankService;
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
 * @Date 2021/8/19 1:39
 * @Description 银行卡管理
 */
@RestController
@Api(tags = "银行卡管理")
@RequestMapping("/adminBanks")
public class AdminBankController implements AdminServiceFeign {

    @Autowired
    private AdminBankService adminBankService;

    /**
     * 根据条件分页查询
     * @param page  分页数据
     * @param bankCard  银行卡号
     * @return
     */
    @ApiOperation(value = "根据条件分页查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current",value = "当前页码"),
            @ApiImplicitParam(name = "size",value = "每页大小"),
            @ApiImplicitParam(name = "bankCard",value = "银行卡号"),
    })
    @GetMapping
    @PreAuthorize("hasAuthority('admin_bank_query')")
    public R<Page<AdminBank>> showAdminBankByPage(@ApiIgnore Page<AdminBank> page, String bankCard){
        return R.ok(adminBankService.findAdminBankByPage(page,bankCard));
    }


    /**
     * 新增信用卡
     * @param adminBank  新增的银行卡
     * @return
     */
    @ApiOperation(value = "新增信用卡")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "AdminBank",value = "新增的银行卡json数据"),
    })
    @PostMapping
    @PreAuthorize("hasAuthority('admin_bank_create')")
    public R<Page<AdminBank>> addAdminBank(@RequestBody @Validated AdminBank adminBank){
        boolean save = adminBankService.save(adminBank);
        if (save){
            return R.ok();
        }else {
            return R.fail("新增信用卡失败");
        }
    }

    /**
     * 修改信用卡
     * @param adminBank  修改的银行卡
     * @return
     */
    @ApiOperation(value = "修改信用卡")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "AdminBank",value = "修改的银行卡json数据"),
    })
    @PatchMapping
    @PreAuthorize("hasAuthority('admin_bank_update')")
    public R<Page<AdminBank>> updateAdminBank(@RequestBody @Validated AdminBank adminBank){
        boolean update = adminBankService.updateById(adminBank);
        if (update){
            return R.ok();
        }else {
            return R.fail("修改信用卡失败");
        }
    }


    /**
     * 修改信用卡状态
     * @param bankId  银行卡id
     * @param status  银行卡状态
     * @return
     */
    @ApiOperation(value = "修改信用卡")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "bankId",value = "银行卡id"),
            @ApiImplicitParam(name = "status",value = "银行卡状态"),
    })
    @PostMapping("/adminUpdateBankStatus")
    @PreAuthorize("hasAuthority('admin_bank_update')")
    public R<Page<AdminBank>> updateAdminBankStatus(String bankId,byte status){
        AdminBank adminBank = new AdminBank();
        adminBank.setId(Long.valueOf(bankId));
        adminBank.setStatus(status);
        boolean update = adminBankService.updateById(adminBank);
        if (update){
            return R.ok();
        }else {
            return R.fail("修改信用卡状态失败");
        }
    }

    /**
     * 获取所有银行卡数据
     * @return
     */
    @Override
    public List<AdminBankDto> findAdminBank() {
        List<AdminBankDto> list = adminBankService.findAdminBank();
        return list;
    }
}
