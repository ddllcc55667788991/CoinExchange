package com.kenji.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.UpdateLoginForm;
import com.kenji.domain.User;
import com.kenji.domain.UserAuthAuditRecord;
import com.kenji.domain.UserAuthInfo;
import com.kenji.dto.RegisterForm;
import com.kenji.dto.UnsetpwdForm;
import com.kenji.dto.UserAuthForm;
import com.kenji.dto.UserDto;
import com.kenji.feign.UserServiceFeign;
import com.kenji.model.R;
import com.kenji.service.UserAuthAuditRecordService;
import com.kenji.service.UserAuthInfoService;
import com.kenji.service.UserService;
import com.kenji.vo.UserAuthInfoVo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @Author Kenji
 * @Date 2021/8/19 11:06
 * @Description
 */
@Api(tags = "用户列表接口")
@RestController
@RequestMapping("/users")
public class UserController implements UserServiceFeign {


    @Autowired
    private UserService userService;

    @Autowired
    private UserAuthInfoService userAuthInfoService;

    @Autowired
    private UserAuthAuditRecordService userAuthAuditRecordService;

    /**
     * 根据条件分页查询
     *
     * @param page     分页模型
     * @param mobile   手机
     * @param userId   用户ID
     * @param userName 用户名
     * @param realName 姓名
     * @param status   状态
     * @return
     */
    @ApiOperation("根据条件分页查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "mobile", value = "手机"),
            @ApiImplicitParam(name = "userId", value = "用户ID"),
            @ApiImplicitParam(name = "userName", value = "用户名"),
            @ApiImplicitParam(name = "realName", value = "姓名"),
            @ApiImplicitParam(name = "status", value = "状态"),
    })
    @PreAuthorize("hasAuthority('user_query')")
    @GetMapping
    public R<Page<User>> findUserByPage(@ApiIgnore Page<User> page, String mobile, Long userId, String userName, String realName, Byte status) {
        Page<User> userPage = userService.findUserByPage(page, mobile, userId, userName, realName, status, null);
        return R.ok(userPage);
    }


    /**
     * 修改用户状态
     *
     * @param id     用户ID
     * @param status 状态
     * @return
     */
    @ApiOperation("修改用户状态")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "用户ID"),
            @ApiImplicitParam(name = "status", value = "状态"),
    })
    @PreAuthorize("hasAuthority('user_update')")
    @PostMapping("/status")
    public R updateUserStatus(@RequestParam Long id, @RequestParam Byte status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        boolean update = userService.updateById(user);
        if (update) {
            return R.ok();
        } else {
            return R.fail("修改状态失败");
        }
    }

    /**
     * 修改用户信息
     *
     * @param user 用户数据
     * @return
     */
    @ApiOperation("修改用户信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "user", value = "用户数据")
    })
    @PreAuthorize("hasAuthority('user_update')")
    @PatchMapping
    public R updateUser(@RequestBody User user) {
        boolean update = userService.updateById(user);
        if (update) {
            return R.ok();
        } else {
            return R.fail("修改失败");
        }
    }


    /**
     * 根据id查询用户信息
     *
     * @param id 用户id
     * @return
     */
    @ApiOperation("根据id查询用户信息")
    @GetMapping("/info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "用户id")
    })
    public R findUserById(String id) {
        User user = userService.getById(id);
        return R.ok(user);
    }


    /**
     * 根据条件分页查询
     *
     * @param page   分页模型
     * @param userId 用户ID
     * @return
     */
    @ApiOperation("根据条件分页查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "userId", value = "用户ID"),
    })
    @PreAuthorize("hasAuthority('user_query')")
    @GetMapping("/directInvites")
    public R<Page<User>> findUserByPage(@ApiIgnore Page<User> page, Long userId) {
        Page<User> userPage = userService.findDirectInviteByUserId(page, userId);
        return R.ok(userPage);
    }

    /**
     * 高级实名认证审核
     *
     * @param page          分页数据
     * @param realName      姓名
     * @param userId        用户ID
     * @param userName      用户名
     * @param mobile        手机号
     * @param reviewsStatus 审核状态
     * @return
     */
    @ApiOperation("高级实名认证审核")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "current", value = "当前页"),
            @ApiImplicitParam(name = "size", value = "每页大小"),
            @ApiImplicitParam(name = "realName", value = "姓名"),
            @ApiImplicitParam(name = "userId", value = "用户ID"),
            @ApiImplicitParam(name = "userName", value = "用户名"),
            @ApiImplicitParam(name = "mobile", value = "手机号"),
            @ApiImplicitParam(name = "reviewsStatus", value = "审核状态"),
    })
    @GetMapping("/auths")
    public R<Page<User>> UserAuthPage(@ApiIgnore Page<User> page, String realName, Long userId, String userName, String mobile, Integer reviewsStatus) {
        Page<User> userPage = userService.findUserAuthByPage(page, mobile, userId, userName, realName, reviewsStatus);
        return R.ok(userPage);
    }


    /**
     * 根据用户id查询用户认证审核详情
     *
     * @param id
     * @return
     */
    @ApiOperation("查看用户认证审核详情")
    @GetMapping("/auth/info")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "用户id")
    })
    public R<UserAuthInfoVo> showUserAuthInfo(Long id) {
        User user = userService.getById(id);
        List<UserAuthInfo> userAuthInfoList = null;
        List<UserAuthAuditRecord> userAuthAuditRecordList = null;
        Integer reviewsStatus = user.getReviewsStatus();
        if (reviewsStatus == 0 || reviewsStatus == null) {
            userAuthInfoList = userAuthInfoService.getByUserId(user.getId());
        } else {
            userAuthInfoList = userAuthInfoService.getByUserId(user.getId());
            userAuthAuditRecordList = userAuthAuditRecordService.getAuthAuditRecord(userAuthInfoList.get(0).getAuthCode());
        }
        return R.ok(new UserAuthInfoVo(user, userAuthAuditRecordList, userAuthInfoList));
    }


    /**
     * 审核用户
     *
     * @param id
     * @param authStatus
     * @param remark
     * @param authCode
     * @return
     */
    @ApiOperation("审核用户")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "用户ID"),
            @ApiImplicitParam(name = "authStatus", value = "审核状态"),
            @ApiImplicitParam(name = "remark", value = "拒绝原因"),
            @ApiImplicitParam(name = "authCode", value = "审核码"),
    })
    @PostMapping("/auths/status")
    public R updateAuthStatus(Long id, Byte authStatus, String remark, Long authCode) {
        //审核：在user表改变authStatus，在UserAuthAuditRecord增加一条记录
        userService.updateAuthStatus(id, authStatus, remark, authCode);
        return R.ok();
    }


    /**
     * 个人中心显示
     */
    @GetMapping("/current/info")
    @ApiOperation(value = "个人中心显示")
    public R<User> showInfo() {
        String userid = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getById(userid);
        user.setPassword(" ");
        user.setPaypassword(" ");
        user.setGaSecret(" ");
        user.setAccessKeySecret(" ");
        return R.ok(user);
    }

    /**
     * 认证身份
     *
     * @param userAuthForm
     * @return
     */
    @ApiOperation(value = "认证身份")
    @ApiImplicitParam(name = "userAuthForm", value = "前端传递的json数据")
    @PostMapping("/authAccount")
    public R authAccount(@RequestBody UserAuthForm userAuthForm) {
        String userid = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean index = userService.authAccount(Long.valueOf(userid), userAuthForm);
        if (index) {
            return R.ok();
        }
        return R.fail("认证失败");
    }

    /**
     * 检查更新电话并发送验证码
     *
     * @param mobile      新电话号码
     * @param countryCode 国家编码
     * @return
     */
    @GetMapping("/checkTel")
    @ApiOperation(value = "检查更新电话并发送验证码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "mobile", value = "新电话号码"),
            @ApiImplicitParam(name = "countryCode", value = "国家编码"),
    })
    public R checkTel(@RequestParam(required = true) String mobile, @RequestParam(required = true) String countryCode) {
        Boolean isOk = userService.checkTel(mobile, countryCode);
        if (isOk) {
            return R.ok();
        }
        return R.fail("发送验证码失败");
    }

    /**
     * 更新手机号码
     *
     * @param countryCode     国家地区编码
     * @param newMobilePhone  新电话号码
     * @param oldValidateCode 旧验证码
     * @param validateCode    新验证码
     * @return
     */
    @PostMapping("/updatePhone")
    @ApiOperation(value = "更新电话号码")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "countryCode", value = "国家地区编码"),
            @ApiImplicitParam(name = "newMobilePhone", value = "新电话号码"),
            @ApiImplicitParam(name = "oldValidateCode", value = "旧验证码"),
            @ApiImplicitParam(name = "validateCode", value = "新验证码"),
    })
    public R updateMobile(@RequestBody @RequestParam(required = true) String countryCode, @RequestBody @RequestParam(required = true) String newMobilePhone, @RequestBody @RequestParam(required = true) Integer oldValidateCode, @RequestBody @RequestParam(required = true) Integer validateCode) {
        String userid = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Boolean isOk = userService.updateMobile(userid, countryCode, newMobilePhone, oldValidateCode, validateCode);
        if (isOk) {
            return R.ok();
        }
        return R.fail();
    }

    /**
     * 更新登录密码
     *
     * @param updateLoginForm 前端修改登录密码的json数据
     * @return
     */
    @PostMapping("/updateLoginPassword")
    @ApiOperation(value = "更新登录密码")
    @ApiImplicitParam(name = "updateLoginForm", value = "前端修改登录密码的json数据")
    public R updateLoginPassword(@RequestBody @Validated UpdateLoginForm updateLoginForm) {
        Long userid = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Boolean isOk = userService.updateLoginPassword(userid, updateLoginForm);
        if (isOk) {
            return R.ok();
        }
        return R.fail("修改密码失败");
    }

    /**
     * 更新交易密码
     *
     * @param updateLoginForm
     * @return
     */
    @PostMapping("/updatePayPassword")
    @ApiOperation(value = "更新交易密码")
    @ApiImplicitParam(name = "updateLoginForm", value = "前端修改交易密码的json数据")
    public R updatePayPassword(@RequestBody @Validated UpdateLoginForm updateLoginForm) {
        Long userid = Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        Boolean isOk = userService.updatePayPassword(userid, updateLoginForm);
        if (isOk) {
            return R.ok();
        }
        return R.fail("修改密码失败");
    }

    /**
     * 忘记密码，设置密码
     *
     * @param payPassword  交易密码
     * @param validateCode 手机验证码
     * @return
     */
    @ApiOperation("/setPayPassword")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "payPassword", value = "交易密码"),
            @ApiImplicitParam(name = "validateCode", value = "手机验证码")

    })
    public R setPayPassword(@RequestBody @NotBlank String payPassword, @RequestBody @NotBlank String validateCode) {
        String userid = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Boolean isOk = userService.setPayPassword(userid, payPassword, validateCode);
        return isOk ? R.ok() : R.fail("设置密码失败");
    }

    /**
     * 邀请用户列表
     *
     * @return
     */
    @GetMapping("/invites")
    @ApiOperation(("邀请用户列表"))
    public R<List<User>> invites() {
        String userid = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        List<User> list = userService.invites(userid);
        return R.ok(list);
    }

    /**
     * 用于admin-service远程调用member-service
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long,UserDto> getBasicUsers(List<Long> ids, String userName, String mobile) {
        Map<Long,UserDto> map = userService.getBasicUsers(ids,userName,mobile);
        return map;
    }

    /**
     * 注册会员
     *
     * @param registerForm 前端注册用户传来的json数据
     * @return
     */
    @PostMapping("/register")
    @ApiOperation("注册会员")
    @ApiImplicitParam(name = "registerForm", value = "前端注册用户传来的json数据")
    public R register(@RequestBody @Validated RegisterForm registerForm) {
        Boolean isOk = userService.register(registerForm);
        if (isOk) {
            return R.ok();
        }
        return R.fail("注册失败");
    }

    /**
     * 重置登录密码
     *
     * @param unsetpwdForm 前端重置登录密码的数据
     * @return
     */
    @PostMapping("/setPassword")
    @ApiOperation(value = "重置登录密码")
    @ApiImplicitParam(name = "unsetpwdForm", value = "前端重置登录密码的数据")
    public R unsetPassword(@RequestBody UnsetpwdForm unsetpwdForm) {
        Boolean isOk = userService.unsetLoginPwd(unsetpwdForm);
        if (isOk) {
            return R.ok();
        }
        return R.fail("重置登录密码失败");
    }

    /**
     * 上传高级认证身份证
     *
     * @param images
     * @return
     */
    @PostMapping("/authUser")
    @ApiOperation("上传高级认证身份证")
    @ApiImplicitParam(name = "images", value = "用户的图片地址")
    public R authUser(@RequestBody String[] images) {
        String userid = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        userService.authUser(userid, images);
        return R.ok();
    }
}
