package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.UpdateLoginForm;
import com.kenji.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kenji.dto.*;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

/**
 * @Author Kenji
 * @Date 2021/8/19 10:36
 * @Description
 */
public interface UserService extends IService<User> {


    /**
     * 根据条件分页查询
     *
     * @param page     分页模型
     * @param mobile   手机
     * @param userId   用户ID
     * @param userName 用户名
     * @param realName 姓名
     * @param status   状态
     * @param reviewsStatus 审核状态
     * @return
     */
    Page<User> findUserByPage(Page<User> page, String mobile, Long userId, String userName, String realName, Byte status, Integer reviewsStatus);

    /**
     * 根据条件分页查询
     * @param page     分页模型
     * @param userId   用户ID
     * @return
     */
    Page<User> findDirectInviteByUserId(Page<User> page, Long userId);


    /**
     * 审核用户
     * @param id
     * @param authStatus
     * @param remark
     * @param authCode
     * @return
     */
    void updateAuthStatus(Long id, Byte authStatus, String remark, Long authCode);


    /**
     * 根据手机号码或邮箱查询会员姓名
     * @param username
     * @return
     */
    String findUserByMobileOrEmail(String username);

    /**
     * 认证用户
     * @param userid 用户ID
     * @param userAuthForm 前端的认证数据
     * @return
     */
    boolean authAccount(Long userid, UserAuthForm userAuthForm);

    /**
     * 高级实名认证审核
     * @param page  分页数据
     * @param realName  姓名
     * @param userId    用户ID
     * @param userName  用户名
     * @param mobile    手机号
     * @param reviewsStatus 审核状态
     * @return
     */
    Page<User> findUserAuthByPage(Page<User> page, String mobile, Long userId, String userName, String realName, Integer reviewsStatus);

    /**
     * 检查更新电话并发送验证码
     * @param newMobile    新电话号码
     * @param countryCode   国家编码
     * @return
     */
    Boolean checkTel(String newMobile, String countryCode);


    /**
     * 更新手机号码
     * @param countryCode   国家地区编码
     * @param newMobilePhone    新电话号码
     * @param oldValidateCode   旧验证码
     * @param validateCode  新验证码
     * @return
     */
    Boolean updateMobile(String userid, String countryCode, String newMobilePhone, Integer oldValidateCode, Integer validateCode);

    /**
     * 更新登录密码
     * @param userid 用户id
     * @param updateLoginForm   前端修改登录密码的json数据
     * @return
     */
    Boolean updateLoginPassword(Long userid, UpdateLoginForm updateLoginForm);

    /**
     * 更新交易密码
     * @param updateLoginForm
     * @return
     */
    Boolean updatePayPassword(Long userid, UpdateLoginForm updateLoginForm);

    /**
     * 忘记密码，设置密码
     * @param userid
     * @param payPassword 交易密码
     * @param validateCode  手机验证码
     * @return
     */
    Boolean setPayPassword(String userid,String payPassword, String validateCode);

    /**
     * 邀请用户列表
     * @return
     */
    List<User> invites(String userid);

    /**
     * 通过用户的id 批量查询用户的基础信息
     * @param ids
     * @return
     */
    Map<Long,UserDto> getBasicUsers(List<Long> ids,String userName,String mobile);

    /**
     * 注册会员
     * @param registerForm  前端注册用户传来的json数据
     * @return
     */
    Boolean register(RegisterForm registerForm);

    /**
     * 重置登录密码
     * @param unsetpwdForm  前端重置登录密码的数据
     * @return
     */
    Boolean unsetLoginPwd(UnsetpwdForm unsetpwdForm);

    /**
     * 上传高级认证身份证
     *
     * @param images
     * @return
     */
    void authUser(String userid, String[] images);
}
