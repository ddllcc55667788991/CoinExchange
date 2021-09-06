package com.kenji.controller;

import com.kenji.domain.SysUser;
import com.kenji.model.R;
import com.kenji.service.SysUserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Kenji
 * @Date 2021/8/17 2:04
 * @Description
 */
@Api(tags = "admin-service-test接口")
@RestController
public class AdminController {
    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "使用用户id查询用户",authorizations = {@Authorization("Authorization")})
    @ApiImplicitParams(@ApiImplicitParam(name = "id",value = "用户ID"))
    @GetMapping("/test/selectUserById/{id}")
    public R selectUserById(@PathVariable Long id){
        SysUser user = sysUserService.getById(id);
        return R.ok(user);
    }
}
