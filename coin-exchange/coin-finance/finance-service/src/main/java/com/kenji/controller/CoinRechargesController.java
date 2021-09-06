package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.CashRecharge;
import com.kenji.domain.CoinRecharge;
import com.kenji.model.R;
import com.kenji.service.CoinRechargeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author Kenji
 * @Date 2021/8/23 20:34
 * @Description
 */
@RequestMapping("/coinRecharges")
@RestController
@Api(tags = "充币记录接口")
public class CoinRechargesController {

    @Autowired
    private CoinRechargeService coinRechargeService;

    /**
     * 条件分页查询充币记录
     *
     * @param page      分页数据
     * @param coinId    币种coinId
     * @param userId    用户ID
     * @param userName  用户名
     * @param mobile    手机号码
     * @param status    状态
     * @param numMin    充值最小金额
     * @param numMax    充值最大金额
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @GetMapping("/records")
    @ApiOperation("条件分页查询充币记录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页码"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "coinId", value = "币种coinId"),
            @ApiImplicitParam(name = "userId", value = "用户ID"),
            @ApiImplicitParam(name = "userName", value = "用户名"),
            @ApiImplicitParam(name = "mobile", value = "手机号码"),
            @ApiImplicitParam(name = "status", value = "状态"),
            @ApiImplicitParam(name = "numMin", value = "充值最小金额"),
            @ApiImplicitParam(name = "numMax", value = "充值最大金额"),
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
    })
    public R<Page<CoinRecharge>> findByPage(
            @ApiIgnore
                    Page<CoinRecharge> page,
            Long coinId,
            Long userId,
            String userName,
            String mobile,
            Byte status,
            String numMin,
            String numMax,
            String startTime,
            String endTime) {
        Page<CoinRecharge> coinRechargePage = coinRechargeService.findByPage(page, coinId, userId, userName, mobile, status, numMin, numMax, startTime, endTime);
        return R.ok(coinRechargePage);
    }

    /**
     * 充币记录分页查询
     * @param page  分页数据
     * @param coinId 币种id
     * @return
     */
    @GetMapping("/user/record")
    @ApiOperation("充币记录分页查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current",value = "当前页码"),
            @ApiImplicitParam(name = "size",value = "每页大小"),
            @ApiImplicitParam(name = "coinId",value = "币种id"),
    })
    public R<Page<CoinRecharge>> findByPage(@ApiIgnore Page<CoinRecharge> page,Long coinId){
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<CoinRecharge> coinRechargePage = coinRechargeService.findRecordByPage(page,userId,coinId);
        return R.ok(coinRechargePage);
    }
}
