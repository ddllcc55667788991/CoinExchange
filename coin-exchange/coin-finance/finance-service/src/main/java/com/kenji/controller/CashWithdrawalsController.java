package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.CashRecharge;
import com.kenji.domain.CashWithdrawals;
import com.kenji.model.CashSellParam;
import com.kenji.model.R;
import com.kenji.service.CashRechargeService;
import com.kenji.service.CashWithdrawalsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author Kenji
 * @Date 2021/8/23 20:15
 * @Description
 */
@RestController
@Api(tags = "GCN提现")
@RequestMapping("/cashWithdrawals")
public class CashWithdrawalsController {

    @Autowired
    private CashWithdrawalsService cashWithdrawalsService;

    /**
     * 条件分页查询GCN提现记录
     * @param page 分页数据
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
    @ApiOperation("条件分页查询GCN提现记录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current",value = "当前页码"),
            @ApiImplicitParam(name = "size",value = "每页大小"),
            @ApiImplicitParam(name = "userId",value = "用户ID"),
            @ApiImplicitParam(name = "userName",value = "用户名"),
            @ApiImplicitParam(name = "mobile",value = "手机号码"),
            @ApiImplicitParam(name = "status",value = "状态"),
            @ApiImplicitParam(name = "numMin",value = "提现最小金额"),
            @ApiImplicitParam(name = "numMax",value = "提现最大金额"),
            @ApiImplicitParam(name = "startTime",value = "开始时间"),
            @ApiImplicitParam(name = "endTime",value = "结束时间"),
    })
    public R<Page<CashWithdrawals>> findByPage(
            @ApiIgnore
                    Page<CashWithdrawals> page,
            Long userId,
            String userName,
            String mobile,
            Byte status,
            String numMin,
            String numMax,
            String startTime,
            String endTime) {
        Page<CashWithdrawals> cashWithdrawalsPage = cashWithdrawalsService.findByPage(page,userId,userName,mobile,status,numMin,numMax,startTime,endTime);
        return R.ok(cashWithdrawalsPage);
    }

    /**
     * 查询卖出记录
     * @param page 分页数据
     * @param status 状态
     * @return
     */
    @GetMapping("/user/records")
    @ApiOperation("查询卖出记录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam( name= "current",value = "当前页码"),
            @ApiImplicitParam( name= "size",value = "每页大小"),
            @ApiImplicitParam( name= "status",value = "状态"),
    } )
    public R<Page<CashWithdrawals>> findByPage(Page<CashWithdrawals> page,Byte status){
        Long useid = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<CashWithdrawals> cashWithdrawalsPage = cashWithdrawalsService.findRecordsByPage(useid,page,status);
        return R.ok(cashWithdrawalsPage);
    }

    /**
     * 用户提现
     * @param cashSellParam 提现参数的json数据
     * @return
     */
    @PostMapping("/sell")
    @ApiOperation("提现api")
    @ApiImplicitParam(name = "cashSellParam",value = "提现参数的json数据")
    public R sell(@RequestBody @Validated CashSellParam cashSellParam){
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Boolean isOk = cashWithdrawalsService.sell(userId,cashSellParam);
        if (isOk){
            return R.ok();
        }
        return R.fail("提现失败");
    }
}
