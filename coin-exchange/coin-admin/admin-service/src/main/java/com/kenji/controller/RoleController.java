package com.kenji.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.SysRole;
import com.kenji.model.R;
import com.kenji.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;

/**
 * @Author Kenji
 * @Date 2021/8/17 23:01
 * @Description 角色管理
 * sys_role_query
 * sys_role_create
 * sys_role_update
 * sys_role_delete
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 根据用户名分页模糊查询
     *
     * @param page
     * @param name
     * @return
     */
    @GetMapping
    @ApiOperation(value = "根据用户名分页模糊查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页码"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "name", value = "用户名")
    })
    @PreAuthorize("hasAuthority('sys_role_query')")
    public R<Page<SysRole>> roleQuery(@ApiIgnore Page<SysRole> page, String name) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<SysRole> rolePage = sysRoleService.pageByName(page, name);
        return R.ok(rolePage);
    }

    /**
     * 新增角色
     *
     * @param sysRole
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增角色")
    @ApiImplicitParams(value =
            {@ApiImplicitParam(name = "sysRole", value = "新增的角色")}
    )
    @PreAuthorize("hasAuthority('sys_role_create')")
    public R roleCreate(@RequestBody SysRole sysRole) {
        boolean save = sysRoleService.save(sysRole);
        if (save) {
            return R.ok();
        }
        return R.fail("新增失败");
    }

    /**
     * 删除角色
     * @param ids
     * @return
     */
    @PostMapping("/delete")
    @ApiOperation(value = "删除角色")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ids",value = "要删除的主键")
    })
    @PreAuthorize("hasAuthority('sys_role_delete')")
    public R role_delete(@RequestBody String[] ids){
        if (ids.length==0 || ids==null){
            return R.fail("要删除的数据不能为空");
        }
        boolean isDelete = sysRoleService.removeByIds(Arrays.asList(ids));
        if (isDelete){
            return R.ok();
        }
        return R.fail("删除失败");
    }
}
