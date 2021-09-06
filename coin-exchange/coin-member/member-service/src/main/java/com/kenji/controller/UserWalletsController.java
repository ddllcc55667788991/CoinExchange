package com.kenji.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.UserWallet;
import com.kenji.model.R;
import com.kenji.service.UserWalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/19 15:17
 * @Description 用户中心的钱包地址管理
 */
@RestController
@RequestMapping("/userWallets")
@Api(tags = "用户中心的提币地址管理")
public class UserWalletsController {

    @Autowired
    private UserWalletService userWalletService;

    /**
     * 分页查询
     *
     * @param page   分页数据
     * @param userId 用户ID
     * @return
     */
    @ApiOperation(value = "根据用户id分页查询")
    @GetMapping
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "userId", value = "用户ID"),
    })
    @PreAuthorize("hasAuthority('user_wallet_query')")
    public R<Page<UserWallet>> showUserWalletByPage(@ApiIgnore Page<UserWallet> page, Long userId) {
        page.addOrder(OrderItem.desc("last_update_time"));
        Page<UserWallet> userWalletPage = userWalletService.findUserWalletByPage(page, userId);
        return R.ok(userWalletPage);
    }

    /**
     * 查询提币地址
     *
     * @param coinId
     * @return
     */
    @GetMapping("/getCoinAddress/{coinId}")
    @ApiOperation("查询提币地址")
    @ApiImplicitParam(name = "coinId", value = "币种ID")
    public R<List<UserWallet>> getCoinAddress(@PathVariable Long coinId) {
        Long userid = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        List<UserWallet> userWallet = userWalletService.getCoinAddress(userid, coinId);
        return R.ok(userWallet);
    }

    /**
     * 添加提币地址
     *
     * @param userWallet
     * @return
     */
    @PostMapping
    @ApiOperation(value = "添加提币地址")
    @ApiImplicitParam(name = "userWallet", value = "前端发送的钱包地址json数据")
    public R addUserWallet(@RequestBody UserWallet userWallet) {
        Long userid = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Boolean save = userWalletService.save(userid, userWallet);
        if (save) {
            return R.ok();
        }
        return R.fail("添加提币地址失败");
    }

    /**
     * 删除提币地址
     * @param addressId 钱包地址ID
     * @param payPassword 交易密码
     * @return
     */
    @PostMapping("/deleteAddress")
    @ApiOperation("删除提币地址")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "addressId", value = "钱包地址ID"),
            @ApiImplicitParam(name = "payPassword", value = "交易密码"),
    })
    public R deleteAddress(@RequestParam(required = true) Long addressId, @RequestParam(required = true) String payPassword) {
            Boolean delete = userWalletService.deleteAddress(addressId,payPassword);
            if (delete){
                return R.ok();
            }
            return R.fail("删除失败");
    }
}
