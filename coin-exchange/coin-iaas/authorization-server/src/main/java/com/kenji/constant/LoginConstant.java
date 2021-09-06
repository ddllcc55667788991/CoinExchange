package com.kenji.constant;

/**
 * @Author Kenji
 * @Date 2021/8/15 22:43
 * @Description
 */
public class LoginConstant {
    /**
     * 管理员(包含超级管理员和后台其他成员)登录
     */
    public static final String ADMIN_TYPE="admin_type";

    /**
     * 会员登录
     */
    public static final String MEMBER_TYPE="member_type";

    /**
     * 管理员代码
     */
    public static final String ADMIN_CODE="ROLE_ADMIN";

    /**
     * 会员代码
     */
    public static final String MEMBER_CODE="ROLE_USER";

    /**
     * refreshToken
     */
    public static final String REFRESH_TOKEN = "refresh_token";

    /**
     * 根据用户名查询用户
     */
    public static final String   QUERY_ADMIN_SQL="select id,'username',password,status  from sys_user where username= ?";

    /**
     * 判断用户是否是管理员
     */
    public static final String  QUERY_ROLE_CODE_SQL = "select `code` from sys_role , sys_user_role where sys_role.id =sys_user_role.role_id and user_id = ? ";

    /**
     * 查询所有权限（管理员）
     */
    public static final String  QUERY_ALL_PERMISSIONS = "select `name` from sys_privilege";

    /**
     * 根据用户的角色查询用户的权限
     */
    public static final String  QUERY_PERMISSION_SQL = " select `name` from sys_privilege ,sys_role_privilege where sys_privilege.id = sys_role_privilege.privilege_id and sys_role_privilege.role_id=(select sys_role.id from sys_role , sys_user_role where sys_role.id =sys_user_role.role_id and user_id = ?)";

    /**
     * 会员登录的接入
     */
    public static final String  QUERY_MEMBER_SQL = "select id,`password`,status from user where mobile =? or email = ?";

    /**
     * 根据管理员id查询用户名
     */
    public static final String  QUERY_ADMIN_USER_WITH_ID = "select `username` from sys_user where id = ?";

    /**
     * 根据会员id查询电话号码
     */
    public static final String  QUERY_MEMBER_USER_WITH_ID ="select mobile from user where id =?";
}
