package com.kenji.controller;

import com.kenji.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:24
 * @Description
 */
@Api(tags = "财务系统的测试")
@RestController
public class TestController {

    @GetMapping("/test")
    @ApiOperation(value = "测试接口")
    @ApiImplicitParam(name = "str",value = "str")
    public R test(String str){
        return R.ok(str);
    }
}
