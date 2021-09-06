package com.kenji.controller;

import com.kenji.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Kenji
 * @Date 2021/8/19 10:47
 * @Description 测试环境
 */
@RestController
@Api
public class TestController {

    @GetMapping("/member/test")
    @ApiOperation(value = "测试环境接口",authorizations = {@Authorization("Authorization")})
    public R test(String str){
        return R.ok("启动成功");
    }
}
