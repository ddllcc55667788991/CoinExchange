package com.kenji.controller;

import com.kenji.domain.Account;
import com.kenji.feign.AccountServiceFeign;
import com.kenji.model.R;
import com.kenji.service.AccountService;
import com.kenji.vo.SymbolAssetVo;
import com.kenji.vo.UserTotalAccountVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @Author Kenji
 * @Date 2021/8/24 11:24
 * @Description
 */
@RestController
@RequestMapping("/account")
@Api(tags = "账户控制器")
public class AccountController implements AccountServiceFeign {

    @Autowired
    private AccountService accountService;

    /**
     * 获取当前用户的货币的资产情况
     *
     * @param coinName
     * @return
     */
    @GetMapping("/{coinName}")
    @ApiOperation(value = "获取当前用户的货币的资产情况")
    @ApiImplicitParam(name = "coinName", value = "货币名称")
    public R<Account> getUserAccount(@PathVariable("coinName") String coinName) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Account account = accountService.findUserAndCoin(userId, coinName);
        return R.ok(account);
    }

    /**
     * 计算用户的总资产
     *
     * @return
     */
    @GetMapping("/total")
    @ApiOperation(value = "计算用户的总资产")
    public R<UserTotalAccountVo> total() {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        UserTotalAccountVo userTotalAccountVo = accountService.getUserTotalAccount(userId);
        return R.ok(userTotalAccountVo);
    }

    /**
     * 获取交易对资产
     * @param symbol
     * @return
     */
    @ApiOperation("获取交易对资产")
    @GetMapping("/asset/{symbol}")
    @ApiImplicitParam(name = "symbol",value = "交易对")
    public R<SymbolAssetVo> getSymbolAssert(@PathVariable("symbol") String symbol) {
        Long userId = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        SymbolAssetVo symbolAssert = accountService.getSymbolAssert(userId, symbol);
        return R.ok(symbolAssert);
    }

    /**
     * 锁定用户的余额
     *
     * @param userId  用户id
     * @param coinId  币种id
     * @param mum     锁定数量
     * @param type    业务类型
     * @param orderId 订单编号
     * @param fee     手续费
     */
    @Override
    public void lockUserAmount(Long userId, Long coinId, BigDecimal mum, String type, Long orderId, BigDecimal fee) {
        accountService.lockUserAmount(userId,coinId,mum,type,orderId,fee);
    }

    /**
     * 划转买入的账户余额
     *
     * @param fromUserId
     * @param toUserId
     * @param coinId
     * @param amount
     * @param businessType
     * @param orderId
     */
    @Override
    public void transferBuyAmount(Long fromUserId, Long toUserId, Long coinId, BigDecimal amount, String businessType, Long orderId) {
        accountService.transferBuyAmount(fromUserId, toUserId,coinId, amount,  businessType,  orderId);
    }

    /**
     * 划转出售成功的账户余额
     *
     * @param fromUserId
     * @param toUserId
     * @param coinId
     * @param amount
     * @param businessType
     * @param orderId
     */
    @Override
    public void transferSellAmount(Long fromUserId, Long toUserId, Long coinId, BigDecimal amount, String businessType, Long orderId) {
        accountService.transferSellAmount(fromUserId, toUserId,coinId, amount,  businessType,  orderId);
    }
}
