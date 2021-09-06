package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.UserBank;
import com.kenji.dto.UserBankDto;
import com.kenji.dto.UserDto;
import com.kenji.feign.UserBankServiceFeign;
import com.kenji.feign.UserServiceFeign;
import com.kenji.mappers.UserBankDtoMappper;
import com.kenji.model.R;
import com.kenji.service.UserBankService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Author Kenji
 * @Date 2021/8/19 12:55
 * @Description 会员银行卡管理
 */
@RequestMapping("/userBanks")
@Api(tags = "会员银行卡管理")
@RestController
public class UserBanksController implements UserBankServiceFeign {

    @Autowired
    private UserBankService userBankService;

    /**
     * 分页查询
     * @param page  分页数据
     * @param usrId    用户ID
     * @return
     */
    @ApiOperation(value = "分页查询" )
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "usrId", value = "用户ID"),
    })
    @PreAuthorize("hasAuthority('user_bank_query')")
    @GetMapping
    public R<Page<UserBank>> findUserByPage(Page<UserBank> page,Long usrId){
        Page<UserBank> userBankPage = userBankService.findUserByPage(page,usrId);
        return R.ok(userBankPage);
    }

    /**
     * 修改会员信银行卡信息
     * @param userBank    修改后的银行卡信息json数据
     * @return
     */
    @ApiOperation(value = "修改会员银行卡信息" )
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "UserBank", value = "修改后的银行卡信息json数据"),
    })
    @PatchMapping
    public R<Page<UserBank>> updateUser(@RequestBody @Validated UserBank userBank){
        boolean update = userBankService.updateById(userBank);
        if (update){
            return R.ok();
        }
        return R.fail("修改失败");
    }

    /**
     * 修改会员信银行卡状态
     * @param id    银行卡ID
     * @param status    银行卡状态
     * @return
     */
    @ApiOperation(value = "修改会员信银行卡状态" )
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "银行卡ID"),
            @ApiImplicitParam(name = "status", value = "银行卡状态"),
    })
    @PostMapping("/status")
    public R<Page<UserBank>> updateUserStatus(Long id,Byte status){
        UserBank userBank = new UserBank();
        userBank.setId(id);
        userBank.setStatus(status);
        boolean update = userBankService.updateById(userBank);
        if (update){
            return R.ok();
        }
        return R.fail("修改失败");
    }

    /**
     * 查询用户银行卡
     * @return
     */
    @GetMapping("/current")
    @ApiOperation(("查询用户银行卡"))
    public R<UserBank> currentUserBank(){
        Long userid = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        UserBank userBank = userBankService.currentUserBank(userid);
        return R.ok(userBank);
    }

    /**
     * 绑定银行卡
     * @param userBank 银行卡
     * @return
     */
    @PostMapping("/bind")
    @ApiOperation("绑定银行卡")
    @ApiImplicitParam(value = "银行卡json数据",name = "userBank")
    public R bind(@RequestBody UserBank userBank){
        Long userid = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Boolean isOk = userBankService.bind(userid,userBank);
        return isOk?R.ok():R.fail("绑定失败");
    }


    /**
     * 根据userid查询userBank信息
     *
     * @param userid
     * @return
     */
    @Override
    public UserBankDto getUserBankInfo(Long userid) {
        UserBank userBank = userBankService.currentUserBank(userid);
        UserBankDto userBankDto = UserBankDtoMappper.INSTANCE.convert2Dto(userBank);
        return userBankDto;
    }
}
