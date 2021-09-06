package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.SysUser;
import com.kenji.model.R;
import com.kenji.service.SysUserService;
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
 * @Date 2021/8/18 12:44
 * @Description 员工管理
 */
@RequestMapping("/users")
@RestController
@Api(tags = "员工管理")
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 按条件分页查询员工信息
     *
     * @param page
     * @param fullname
     * @param mobile
     * @return
     */
    @GetMapping
    @ApiOperation(value = "按条件分页查询员工信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页码"),
            @ApiImplicitParam(name = "size", value = "每页显示条数"),
            @ApiImplicitParam(name = "fullname", value = "员工姓名"),
            @ApiImplicitParam(name = "mobile", value = "手机号码")
    })
    @PreAuthorize("hasAuthority('sys_user_query')")
    public R<Page<SysUser>> showUserByPage(@ApiIgnore Page<SysUser> page, String fullname, String mobile) {
        Page<SysUser> sysUserPage = sysUserService.findUserByMobileAndFullName(page, fullname, mobile);
        return R.ok(sysUserPage);
    }


    /**
     * 添加用户
     *
     * @param sysUser
     * @return
     */
    @ApiOperation(value = "添加用户")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "sysUser", value = "前端传送sysUser的json数据")
    })
    @PreAuthorize("hasAuthority('sys_user_create')")
    @PostMapping
    public R addUser(@Validated @RequestBody SysUser sysUser) {
        Boolean index = sysUserService.insertUser(sysUser);
        if (index) {
            return R.ok();
        }
        return R.fail("新增用户失败");
    }


    /**
     * 修改用户
     *
     * @param sysUser
     * @return
     */
    @ApiOperation(value = "修改用户")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "sysUser", value = "前端传送sysUser的json数据")
    })
    @PreAuthorize("hasAuthority('sys_user_update')")
    @PatchMapping
    public R updateUser(@Validated @RequestBody SysUser sysUser) {
        Boolean index = sysUserService.updateUser(sysUser);
        if (index) {
            return R.ok();
        }
        return R.fail("修改用户失败");
    }

    /**
     * 删除用户
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除用户")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ids", value = "要删除的userIds")
    })
    @PreAuthorize("hasAuthority('sys_user_delete')")
    @PostMapping("/delete")
    public R deleteUser(@RequestBody String[] ids) {
        if (ids.length != 0 && ids != null) {
            Boolean index = sysUserService.deleteUser(ids);
            if (index) {
                return R.ok();
            }
        }
        return R.fail("删除用户失败");
    }
}
