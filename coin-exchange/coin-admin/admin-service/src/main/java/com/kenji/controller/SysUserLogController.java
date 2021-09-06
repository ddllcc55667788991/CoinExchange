package com.kenji.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.SysUserLog;
import com.kenji.model.R;
import com.kenji.service.SysUserLogService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author Kenji
 * @Date 2021/8/18 19:19
 * @Description 系统日志管理
 */
@RestController
@RequestMapping("/sysUserLog")
@Api(tags = "系统日志管理")
public class SysUserLogController {

    @Autowired
    private SysUserLogService sysUserLogService;

    /**
     * 分页查询系统日志
     * @param page
     * @return
     */
    @GetMapping
    @ApiOperation(value = "分页查询系统日志")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current",value = "当前页"),
            @ApiImplicitParam(name = "size",value = "每页显示条数")
    })
    @PreAuthorize("hasAuthority('sys_user_log_query')")
    public R<Page<SysUserLog>> showUserLog(@ApiIgnore Page<SysUserLog> page){
        page.addOrder(OrderItem.desc("created"));
        return R.ok(sysUserLogService.page(page));
    }
}
