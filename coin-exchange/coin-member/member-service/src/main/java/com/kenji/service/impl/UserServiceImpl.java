package com.kenji.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.config.IdAutoConfiguration;
import com.kenji.domain.*;
import com.kenji.dto.*;
import com.kenji.geetest.GeetestLib;
import com.kenji.mappers.UserDtoMappper;
import com.kenji.service.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.UserMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * @Author Kenji
 * @Date 2021/8/19 10:36
 * @Description
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserAuthAuditRecordService userAuthAuditRecordService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private GeetestLib geetestLib;

    @Autowired
    private SmsService smsService;

    @Autowired
    private UserAuthInfoService userAuthInfoService;

    @Autowired
    private Snowflake snowflake;


    /**
     * 会员登录的接入
     */
    public static final String QUERY_MEMBER_SQL = "select username from user where mobile =? or email = ?";

    /**
     * 根据条件分页查询
     *
     * @param page          分页模型
     * @param mobile        手机
     * @param userId        用户ID
     * @param userName      用户名
     * @param realName      姓名
     * @param status        状态
     * @param reviewsStatus 审核状态
     * @return
     */
    @Override
    public Page<User> findUserByPage(Page<User> page, String mobile, Long userId, String userName, String realName, Byte status, Integer reviewsStatus) {
        page.addOrder(OrderItem.asc("created"));
        Page<User> userPage = super.page(page, new LambdaQueryWrapper<User>()
                .like(!StringUtils.isEmpty(mobile), User::getMobile, mobile)
                .eq(userId != null, User::getId, userId)
                .like(!StringUtils.isEmpty(userName), User::getUsername, userName)
                .like(!StringUtils.isEmpty(realName), User::getRealName, realName)
                .eq(status != null, User::getAuthStatus, status));
        return userPage;
    }

    /**
     * 根据条件分页查询
     *
     * @param page   分页模型
     * @param userId 用户ID
     * @return
     */
    @Override
    public Page<User> findDirectInviteByUserId(Page<User> page, Long userId) {
        return super.page(page, new LambdaQueryWrapper<User>()
                .eq(User::getDirectInviteid, userId));
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
    @Override
    @Transactional
    public void updateAuthStatus(Long id, Byte authStatus, String remark, Long authCode) {
        User user = getById(id);
        if (user != null) {
            user.setReviewsStatus(authStatus.intValue());   //审核的状态
            super.updateById(user); //修改用户的状态
        }
        UserAuthAuditRecord userAuthAuditRecord = new UserAuthAuditRecord();
        userAuthAuditRecord.setAuditUserName("-------");
        userAuthAuditRecord.setAuthCode(authCode);
        userAuthAuditRecord.setRemark(remark);
        userAuthAuditRecord.setUserId(id);
        userAuthAuditRecord.setStatus(authStatus);
        userAuthAuditRecordService.save(userAuthAuditRecord);
    }

    /**
     * 根据手机号码或邮箱查询会员姓名
     *
     * @param username
     * @return
     */
    @Override
    public String findUserByMobileOrEmail(String username) {
        return jdbcTemplate.queryForObject(QUERY_MEMBER_SQL, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                String name = resultSet.getString("username");
                return name;
            }
        }, username, username);

    }

    /**
     * 认证用户
     *
     * @param userid       用户ID
     * @param userAuthForm 前端的认证数据
     * @return
     */
    @Override
    public boolean authAccount(Long userid, UserAuthForm userAuthForm) {
        User user = getById(userid);
        Assert.notNull(user, "用户不存在");
        Byte authStatus = user.getAuthStatus();
        if (!authStatus.equals((byte) 0)) {
            throw new IllegalArgumentException("该用户已经认证成功");
        }
        //执行极验
        checkForm(userAuthForm);
        boolean check = IdAutoConfiguration.check(userAuthForm.getRealName(), userAuthForm.getIdCard());
        if (!check) {
            throw new IllegalArgumentException("认证不成功！");
        }
        user.setAuthStatus((byte) 1);
        user.setRealName(userAuthForm.getRealName());
        user.setIdCardType(Integer.valueOf(userAuthForm.getIdCardType()).intValue());
        user.setIdCard(userAuthForm.getIdCard());
        Date date = new Date();
        user.setAuthtime(date);
        user.setLastUpdateTime(date);
        return updateById(user);
    }

    /**
     * 高级实名认证审核
     *
     * @param page          分页数据
     * @param mobile        手机号
     * @param userId        用户ID
     * @param userName      用户名
     * @param realName      姓名
     * @param reviewsStatus 审核状态
     * @return
     */
    @Override
    public Page<User> findUserAuthByPage(Page<User> page, String mobile, Long userId, String userName, String realName, Integer reviewsStatus) {
        return page(page,
                new LambdaQueryWrapper<User>()
                        .like(!StringUtils.isEmpty(mobile), User::getMobile, mobile)
                        .like(!StringUtils.isEmpty(userName), User::getUsername, userName)
                        .like(!StringUtils.isEmpty(realName), User::getRealName, realName)
                        .eq(userId != null, User::getId, userId)
                        .eq(reviewsStatus != null, User::getReviewsStatus, reviewsStatus)
        );
    }

    /**
     * 检查更新电话并发送验证码
     *
     * @param newMobile   新电话号码
     * @param countryCode 国家编码
     * @return
     */
    @Override
    public Boolean checkTel(String newMobile, String countryCode) {
        //检查新电话号码是否重复
        int count = count(new LambdaQueryWrapper<User>()
                .eq(User::getMobile, newMobile)
                .eq(User::getCountryCode, countryCode));
        if (count > 0) {
            return false;
        }
        //发送验证码
        Sms sms = new Sms();
        sms.setCountryCode(countryCode);
        sms.setMobile(newMobile);
        sms.setTemplateCode("CHANGE_PHONE_VERIFY");
        return smsService.sendMsg(sms);
    }

    /**
     * 更新手机号码
     *
     * @param userid
     * @param countryCode     国家地区编码
     * @param newMobilePhone  新电话号码
     * @param oldValidateCode 旧验证码
     * @param validateCode    新验证码
     * @return
     */
    @Override
    public Boolean updateMobile(String userid, String countryCode, String newMobilePhone, Integer oldValidateCode, Integer validateCode) {
        User user = getById(userid);
        String oldMobile = user.getMobile();
        //验证旧验证码
        String oldSendValidateCode = redisTemplate.opsForValue().get("SMS:VERIFY_OLD_PHONE:" + oldMobile).toString();
        if (oldValidateCode != Integer.valueOf(oldSendValidateCode)) {
            throw new IllegalArgumentException("原手机验证码错误！");
        }
        //验证新验证码
        String newSendValidateCode = redisTemplate.opsForValue().get("SMS:CHANGE_PHONE_VERIFY:" + newMobilePhone).toString();
        if (validateCode != Integer.valueOf(newSendValidateCode)) {
            throw new IllegalArgumentException("新手机验证码错误！");
        }
        //更新号码
        user.setMobile(newMobilePhone);
        return super.updateById(user);
    }

    /**
     * 更新登录密码
     *
     * @param userid          用户id
     * @param updateLoginForm 前端修改登录密码的json数据
     * @return
     */
    @Override
    public Boolean updateLoginPassword(Long userid, UpdateLoginForm updateLoginForm) {
        //验证旧密码是否正确
        User user = super.getById(userid);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(updateLoginForm.getOldpassword(), user.getPassword());
        if (!matches) {
            throw new IllegalArgumentException("旧密码输入错误！");
        }
        //校验手机验证码
        String code = redisTemplate.opsForValue().get("SMS:CHANGE_LOGIN_PWD_VERIFY:" + updateLoginForm.getNewpassword()).toString();
        if (updateLoginForm.equals(code)) {
            //修改密码
            user.setPassword(encoder.encode(updateLoginForm.getNewpassword()));
            if (super.updateById(user)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 更新交易密码
     *
     * @param userid
     * @param updateLoginForm
     * @return
     */
    @Override
    public Boolean updatePayPassword(Long userid, UpdateLoginForm updateLoginForm) {
        //验证旧密码是否正确
        User user = super.getById(userid);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        boolean matches = encoder.matches(updateLoginForm.getOldpassword(), user.getPaypassword());
        if (!matches) {
            throw new IllegalArgumentException("旧密码输入错误！");
        }
        //校验手机验证码
        String code = redisTemplate.opsForValue().get("SMS:CHANGE_PAY_PWD_VERIFY:" + updateLoginForm.getNewpassword()).toString();
        if (updateLoginForm.equals(code)) {
            //修改密码
            user.setPaypassword(encoder.encode(updateLoginForm.getNewpassword()));
            if (super.updateById(user)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 忘记密码，设置密码
     *
     * @param userid
     * @param payPassword  交易密码
     * @param validateCode 手机验证码
     * @return
     */
    @Override
    public Boolean setPayPassword(String userid, String payPassword, String validateCode) {
        User user = getById(userid);
        String code = redisTemplate.opsForValue().get("SMS:FORGOT_PAY_PWD_VERIFY:" + user.getMobile()).toString();
        if (code.equals(validateCode)) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPaypassword(encoder.encode(payPassword));
            return super.updateById(user);
        }
        return false;
    }

    /**
     * 邀请用户列表
     *
     * @param userid
     * @return
     */
    @Override
    public List<User> invites(String userid) {
        List<User> list = super.list(new LambdaQueryWrapper<User>().eq(User::getDirectInviteid, userid));
        if (CollectionUtils.isEmpty(list)) {
            return Collections.EMPTY_LIST;
        }
        list.forEach(user -> {
            user.setPaypassword("******");
            user.setPassword("******");
            user.setAccessKeySecret("******");
            user.setAccessKeyId("******");
        });
        return list;
    }

    /**
     * 通过用户的id 批量查询用户的基础信息
     *
     * @param ids 用户的id
     * @return
     */
    @Override
    public Map<Long, UserDto> getBasicUsers(List<Long> ids, String userName, String mobile) {
//        if (CollectionUtils.isEmpty(ids)) {
//            return Collections.emptyList();
//        }
//        List<User> list = list(new LambdaQueryWrapper<User>().in(User::getId, ids));
//        //对象的转化
//        List<UserDto> userDtoList = UserDtoMappper.INSTANCE.convert2Dto(list);
//        return userDtoList;
        if (CollectionUtils.isEmpty(ids)&&StringUtils.isEmpty(userName)&&StringUtils.isEmpty(mobile)){
            return Collections.emptyMap();
        }
        List<User> userList = super.list(new LambdaQueryWrapper<User>()
                .in(!CollectionUtils.isEmpty(ids), User::getId, ids)
                .eq(!StringUtils.isEmpty(userName), User::getUsername, userName)
                .eq(!StringUtils.isEmpty(mobile), User::getMobile, mobile));
        if (CollectionUtils.isEmpty(userList)){
            return Collections.emptyMap();
        }
        List<UserDto> userDtoList = UserDtoMappper.INSTANCE.convert2Dto(userList);
        Map<Long, UserDto> userDtoMap = userDtoList.stream().collect(Collectors.toMap(UserDto::getId, userDto -> userDto));
        return userDtoMap;
    }

    /**
     * 注册会员
     *
     * @param registerForm 前端注册用户传来的json数据
     * @return
     */
    @Override
    public Boolean register(RegisterForm registerForm) {
        //判断手机号码注册和邮箱注册
        if (StringUtils.isEmpty(registerForm.getEmail()) && StringUtils.isEmpty(registerForm.getMobile())) {
            throw new IllegalArgumentException("请用手机号码或邮箱注册");
        }
        int count = super.count(new LambdaQueryWrapper<User>()
                .eq(!StringUtils.isEmpty(registerForm.getMobile()), User::getMobile, registerForm.getMobile())
                .eq(!StringUtils.isEmpty(registerForm.getEmail()), User::getEmail, registerForm.getEmail()));
        if (count == 0) {
            registerForm.checkFormData(geetestLib, redisTemplate);
            User user = getUser(registerForm);
            return super.save(user);
        }

        return false;
    }

    /**
     * 重置登录密码
     *
     * @param unsetpwdForm 前端重置登录密码的数据
     * @return
     */
    @Override
    public Boolean unsetLoginPwd(UnsetpwdForm unsetpwdForm) {
        //极速验证
        unsetpwdForm.checkFormData(geetestLib, redisTemplate);

        //判断手机验证码
        String code = redisTemplate.opsForValue().get("SMS:FORGOT_VERIFY:" + unsetpwdForm.getMobile()).toString();
        if (!code.equals(unsetpwdForm.getValidateCode())) {
            throw new IllegalArgumentException("手机验证码错误");
        }
        //判断手机号是否存在
        User user = super.getOne(new LambdaQueryWrapper<User>()
                .eq(!StringUtils.isEmpty(unsetpwdForm.getMobile()), User::getMobile, unsetpwdForm));
        if (user == null) {
            throw new IllegalArgumentException("手机号码错误");
        }
        //修改登录密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(unsetpwdForm.getPassword()));
        return super.updateById(user);
    }

    /**
     * 上传高级认证身份证
     *
     * @param userid
     * @param images
     * @return
     */
    @Override
    @Transactional
    public void authUser(String userid, String[] images) {
        List<String> imageList = Arrays.stream(images).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(imageList)) {
            throw new IllegalArgumentException("用户图片不能为空");
        }
        User user = super.getById(userid);
        if (user == null) {
            throw new IllegalArgumentException("用户id不存在");
        }
        List<UserAuthInfo> userAuthInfoList = new ArrayList<>(imageList.size());
        long authCode = snowflake.nextId();
        for (int i = 0; i < imageList.size(); i++) {
            UserAuthInfo userAuthInfo = new UserAuthInfo();
            userAuthInfo.setUserId(Long.valueOf(userid));
            userAuthInfo.setImageUrl(imageList.get(i));
            userAuthInfo.setSerialno(i + 1);
            userAuthInfo.setAuthCode(authCode);
            userAuthInfoList.add(userAuthInfo);
        }
        userAuthInfoService.saveBatch(userAuthInfoList);
        user.setReviewsStatus(0);
        super.updateById(user);
    }

    private User getUser(RegisterForm registerForm) {
        User user = new User();
        user.setType((byte) 1);
        user.setCountryCode(registerForm.getCountryCode());
        user.setMobile(registerForm.getMobile());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(registerForm.getPassword()));
        user.setEmail(registerForm.getEmail());
        user.setAuthStatus((byte) 1);
        user.setStatus((byte) 1);
        if (!StringUtils.isEmpty(registerForm.getMobile())) {
            user.setUsername(registerForm.getMobile());
        } else {
            user.setUsername(registerForm.getEmail());
        }
        user.setInviteCode(RandomUtil.randomString(6));
        if (!StringUtils.isEmpty(registerForm.getInvitionCode())) {
            User invitedUser = getOne(new LambdaQueryWrapper<User>().eq(User::getInviteCode, registerForm.getInvitionCode()));
            user.setInviteRelation(invitedUser.getId().toString());   //邀请关系，设置userid
            user.setDirectInviteid(invitedUser.getId().toString());   //直接邀请人
        }
        return user;
    }


    /**
     * 执行身份认证的极验
     *
     * @param userAuthForm
     */
    private void checkForm(UserAuthForm userAuthForm) {
        userAuthForm.checkFormData(geetestLib, redisTemplate);

    }

    /**
     * 重写getid,获取高级认证信息
     *
     * @param id
     * @return
     */
    @Override
    public User getById(Serializable id) {
        User user = super.getById(id);
        Assert.notNull(user, "请输入用户正确的ID");
        Integer reviewsStatus = user.getReviewsStatus();
        //reviews_status 审核状态,1通过,2拒绝,0,待审核
        //seniorAuthStatus 0审核中 1通过 2拒绝 3未填写
        if (reviewsStatus == null) {    //未审核,高级认证状态为3未填写
            user.setSeniorAuthStatus((byte) 3);
            user.setSeniorAuthDesc("资料未填写");
        } else {
            //1-初级实名认证 ，高级认证状态为
            switch (reviewsStatus) {
                case 1: //1通过
                    user.setSeniorAuthStatus((byte) 1);
                    user.setSeniorAuthDesc("审核通过");
                    break;
                case 2: //2拒绝
                    user.setSeniorAuthStatus((byte) 2);
                    UserAuthAuditRecord userAuthAuditRecord = userAuthAuditRecordService.getOne(new LambdaQueryWrapper<UserAuthAuditRecord>().eq(UserAuthAuditRecord::getUserId, id));
                    String remark = userAuthAuditRecord.getRemark();
                    if (!StringUtils.isEmpty(remark)) {
                        user.setSeniorAuthDesc(remark);
                    } else {
                        user.setSeniorAuthDesc("原因未知");
                    }
                    break;
                case 0:
                    user.setSeniorAuthStatus((byte) 0);
                    user.setSeniorAuthDesc("审核中");
                    break;
            }
        }
        return user;
    }
}
