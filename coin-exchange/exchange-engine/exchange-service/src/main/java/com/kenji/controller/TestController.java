package com.kenji.controller;

import com.kenji.model.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Kenji
 * @Date 2021/8/24 22:28
 * @Description
 */
@Api(tags = "测试接口")
@RestController
public class TestController {

    @ApiOperation("交易系统测试接口")
    @GetMapping("/test")
    public R<String> test(){
        return R.ok("交易系统测试成功");
    }
}
