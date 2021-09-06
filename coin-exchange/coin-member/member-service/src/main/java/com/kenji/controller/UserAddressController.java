package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.UserAddress;
import com.kenji.model.R;
import com.kenji.service.UserAddressService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Kenji
 * @Date 2021/8/19 15:55
 * @Description 用户中心钱包地址管理
 */
@Api(tags = "用户中心钱包地址管理")
@RequestMapping("/userAddress")
@RestController
public class UserAddressController {

    @Autowired
    private UserAddressService userAddressService;

    /**
     * 分页查询
     * @param page  分页数据
     * @param userId    用户ID
     * @return
     */
    @ApiOperation(value = "根据用户id分页查询")
    @GetMapping
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "userId", value = "用户ID"),
    })
    @PreAuthorize("hasAuthority('user_wallet_address_query')")
    public R<Page<UserAddress>> findUserAddressByPage(Page<UserAddress> page,Long userId){
        Page<UserAddress> userAddressPage = userAddressService.findUserAddressByPage( page,userId);
        return R.ok(userAddressPage);
    }

    /**
     * 充币获取钱包地址
     * @param coinId 币种id
     * @return
     */
    @GetMapping("/getCoinAddress/{coinId}")
    @ApiOperation("充币获取钱包地址")
    @ApiImplicitParam(name = "coinId",value = "币种id")
    public R<String> getCoinAddress(@PathVariable Long coinId){
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        UserAddress userAddress = userAddressService.getCoinAddress(userId,coinId);
        return R.ok(userAddress.getAddress());
    }
}
