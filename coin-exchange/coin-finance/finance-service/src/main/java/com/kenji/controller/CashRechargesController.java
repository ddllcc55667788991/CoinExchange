package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.CashRecharge;
import com.kenji.domain.CoinRecharge;
import com.kenji.model.CashParam;
import com.kenji.model.R;
import com.kenji.service.CashRechargeService;
import com.kenji.util.ReportCsvUtils;
import com.kenji.vo.CashTradeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.math.BigDecimal;

/**
 * @Author Kenji
 * @Date 2021/8/23 16:35
 * @Description
 */
@Api(tags = "GCN充值控制器")
@RequestMapping("/cashRecharges")
@RestController
public class CashRechargesController {

    @Autowired
    private CashRechargeService cashRechargeService;

    /**
     * 条件分页查询GCN充值记录
     * @param page 分页数据
     * @param coinId 币种coinId
     * @param userId 用户ID
     * @param userName 用户名
     * @param mobile 手机号码
     * @param status 状态
     * @param numMin 充值最小金额
     * @param numMax 充值最大金额
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    @GetMapping("/records")
    @ApiOperation("条件分页查询GCN充值记录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current",value = "当前页码"),
            @ApiImplicitParam(name = "size",value = "每页大小"),
            @ApiImplicitParam(name = "coinId",value = "币种coinId"),
            @ApiImplicitParam(name = "userId",value = "用户ID"),
            @ApiImplicitParam(name = "userName",value = "用户名"),
            @ApiImplicitParam(name = "mobile",value = "手机号码"),
            @ApiImplicitParam(name = "status",value = "状态"),
            @ApiImplicitParam(name = "numMin",value = "充值最小金额"),
            @ApiImplicitParam(name = "numMax",value = "充值最大金额"),
            @ApiImplicitParam(name = "startTime",value = "开始时间"),
            @ApiImplicitParam(name = "endTime",value = "结束时间"),
    })
    public R<Page<CashRecharge>> findRecordsByPage(
            @ApiIgnore
            Page<CashRecharge> page,
            Long coinId,
            Long userId,
            String userName,
            String mobile,
            Byte status,
            String numMin,
            String numMax,
            String startTime,
            String endTime) {
        Page<CashRecharge> cashRechargePage = cashRechargeService.findRecordsByPage(page,coinId,userId,userName,mobile,status,numMin,numMax,startTime,endTime);
        return R.ok(cashRechargePage);
    }

    /**
     * 查询买入记录
     * @param page 分页数据
     * @param status 状态
     * @return
     */
    @GetMapping("/user/records")
    @ApiOperation("查询买入记录")
    @ApiImplicitParams(value = {
           @ApiImplicitParam( name= "current",value = "当前页码"),
           @ApiImplicitParam( name= "size",value = "每页大小"),
           @ApiImplicitParam( name= "status",value = "状态"),
    } )
    public R<Page<CashRecharge>> findByPage(Page<CashRecharge> page,Byte status){
        Long useid = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<CashRecharge> cashRechargePage = cashRechargeService.findByPage(useid,page,status);
        return R.ok(cashRechargePage);
    }

    /**
     * 买入GCN
     * @param cashParam GCN充值参数
     * @return
     */
    @PostMapping("/buy")
    @ApiOperation("买入GCN")
    @ApiImplicitParam(name = "cashParam",value = "GCN充值参数")
    public R<CashTradeVo> buy(@RequestBody @Validated CashParam cashParam){
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        CashTradeVo cashTradeVo = cashRechargeService.buy(userId,cashParam);
        return R.ok(cashTradeVo);
    }
}
