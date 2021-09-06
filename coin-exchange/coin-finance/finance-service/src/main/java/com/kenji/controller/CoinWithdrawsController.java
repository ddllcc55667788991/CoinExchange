package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.CoinRecharge;
import com.kenji.domain.CoinWithdraw;
import com.kenji.model.R;
import com.kenji.service.CoinRechargeService;
import com.kenji.service.CoinWithdrawService;
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
 * @Date 2021/8/23 20:43
 * @Description
 */
@Api(tags = "提币接口")
@RestController
@RequestMapping("/coinWithdraws")
public class CoinWithdrawsController {
    @Autowired
    private CoinWithdrawService coinWithdrawService;

    /**
     * 条件分页查询提币记录
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
    @ApiOperation("条件分页查询提币记录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页码"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "coinId", value = "币种coinId"),
            @ApiImplicitParam(name = "userId", value = "用户ID"),
            @ApiImplicitParam(name = "userName", value = "用户名"),
            @ApiImplicitParam(name = "mobile", value = "手机号码"),
            @ApiImplicitParam(name = "status", value = "状态"),
            @ApiImplicitParam(name = "numMin", value = "提币最小金额"),
            @ApiImplicitParam(name = "numMax", value = "提币最大金额"),
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
    })
    public R<Page<CoinWithdraw>> findByPage(
            @ApiIgnore
                    Page<CoinWithdraw> page,
            Long coinId,
            Long userId,
            String userName,
            String mobile,
            Byte status,
            String numMin,
            String numMax,
            String startTime,
            String endTime) {
        Page<CoinWithdraw> coinWithdrawPage = coinWithdrawService.findByPage(page, coinId, userId, userName, mobile, status, numMin, numMax, startTime, endTime);
        return R.ok(coinWithdrawPage);
    }

    /**
     * 提币记录分页查询
     * @param page 分页数据
     * @param coinId 币种Id
     * @return
     */
    @GetMapping("/user/record")
    @ApiOperation("提币记录分页查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current",value = "当前页码"),
            @ApiImplicitParam(name = "size",value = "每页大小"),
            @ApiImplicitParam(name = "coinId",value = "币种Id"),
    })
    public R<Page<CoinWithdraw>> findRecordByPage(@ApiIgnore Page<CoinWithdraw> page,Long coinId){
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Page<CoinWithdraw> coinWithdrawPage = coinWithdrawService.findRecordByPage(page,userId,coinId);
        return R.ok(coinWithdrawPage);
    }
}
