package com.kenji.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.SysPrivilege;
import com.kenji.model.R;
import com.kenji.service.SysPrivilegeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;

/**
 * @Author Kenji
 * @Date 2021/8/17 19:06
 * @Description 权限管理
 * 查询权限  sys_privilege_query
 * 新增权限  sys_privilege_create
 * 修改权限  sys_privilege_update
 * 删除权限  sys_privilege_delete
 *
 */
@RestController
@RequestMapping("/privileges")
@Api(tags = "权限管理")
public class PrivilegeController {

    @Autowired
    private SysPrivilegeService sysPrivilegeService;

    /**
     * 分页查询权限
     */
    @PreAuthorize("hasAuthority('sys_privilege_query')")
    @GetMapping
    @ApiOperation("分页查询权限数据")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current",value = "当前页码"),
            @ApiImplicitParam(name = "size",value = "每页大小")
    })
    public R<Page<SysPrivilege>> privilegeQuery(@ApiIgnore Page<SysPrivilege> page){
        //按修改的时间倒序排序
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<SysPrivilege> privilegePage = sysPrivilegeService.page(page);
        return R.ok(privilegePage);
    }

    /**
     * 新增权限
     */
    @PostMapping
    @ApiOperation(value = "新增权限")
    @ApiImplicitParams(
            value = {@ApiImplicitParam(name = "sysPrivilege",value = "前端传来的sysPrivilege的json数据")} )
    @PreAuthorize("hasAuthority('sys_privilege_create')")
    public R privilegeCreate(@Validated @RequestBody SysPrivilege sysPrivilege){
//        Long userid = Long.parseLong(SecurityContextHolder.getContext().getAuthentication().getPrincipal()+"") ;
//        Date date = new Date();
//        sysPrivilege.setCreateBy(userid);
//        sysPrivilege.setCreated(date);
//        sysPrivilege.setLastUpdateTime(date);
        boolean save = sysPrivilegeService.save(sysPrivilege);
        if (!save){
            return R.fail("新增失败");
        }
        return R.ok("新增成功");
    }


    /**
     * 编辑权限
     */
    @PatchMapping
    @ApiOperation(value = "编辑权限")
    @ApiImplicitParams(
            value = {@ApiImplicitParam(name = "sysPrivilege",value = "前端传来的sysPrivilege的json数据")} )
    @PreAuthorize("hasAuthority('sys_privilege_update')")
    public R sys_privilegeUpdate(@Validated @RequestBody SysPrivilege sysPrivilege){
//        Date date = new Date();
//        sysPrivilege.setLastUpdateTime(date);
        boolean update = sysPrivilegeService.updateById(sysPrivilege);
        if (!update){
            return R.fail("修改失败");
        }
        return R.ok("修改成功");
    }
}
