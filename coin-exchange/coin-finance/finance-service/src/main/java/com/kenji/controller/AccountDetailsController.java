package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.AccountDetail;
import com.kenji.domain.CoinWithdraw;
import com.kenji.model.R;
import com.kenji.service.AccountDetailService;
import com.kenji.service.CoinWithdrawService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author Kenji
 * @Date 2021/8/23 21:05
 * @Description
 */
@RequestMapping("/accountDetails")
@RestController
@Api(tags = "资金流水")
public class AccountDetailsController {
    @Autowired
    private AccountDetailService  accountDetailService;

    /**
     * 条件分页查询流水记录
     *
     * @param page      分页数据
     * @param coinId    币种coinId
     * @param userId    用户ID
     * @param userName  用户名
     * @param mobile    手机号码
     * @param amountStart    充值最小金额
     * @param amountEnd    充值最大金额
     * @param accountId    账户ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @GetMapping("/records")
    @ApiOperation("条件分页查询流水记录")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页码"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "coinId", value = "币种coinId"),
            @ApiImplicitParam(name = "userId", value = "用户ID"),
            @ApiImplicitParam(name = "userName", value = "用户名"),
            @ApiImplicitParam(name = "mobile", value = "手机号码"),
            @ApiImplicitParam(name = "amountStart", value = "账户开始金额"),
            @ApiImplicitParam(name = "amountEnd", value = "账户结束金额"),
            @ApiImplicitParam(name = "accountId", value = "账户ID"),
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间"),
    })
    public R<Page<AccountDetail>> findByPage(
            @ApiIgnore
                    Page<AccountDetail> page,
            Long coinId,
            Long userId,
            String userName,
            String mobile,
            Byte status,
            String amountStart,
            String amountEnd,
            Long  accountId,
            String startTime,
            String endTime) {
        Page<AccountDetail> accountDetailPage = accountDetailService.findByPage(page, coinId, userId, userName, mobile, status,accountId,amountStart, amountEnd, startTime, endTime);
        return R.ok(accountDetailPage);
    }
}
