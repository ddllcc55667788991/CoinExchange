package com.kenji.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author  Kenji
 * @Date  2021/8/19 10:36
 * @Description 
 */
/**
    * 用户登录日志
    */
@ApiModel(value="com-kenji-domain-UserLoginLog")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_login_log")
public class UserLoginLog {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="用户ID")
    private Long userId;

    /**
     * 客户端类型
            1-PC
            2-IOS
            3-Android
     */
    @TableField(value = "client_type")
    @ApiModelProperty(value="客户端类型,            1-PC,            2-IOS,            3-Android")
    private Byte clientType;

    /**
     * 登录IP
     */
    @TableField(value = "login_ip")
    @ApiModelProperty(value="登录IP")
    private String loginIp;

    /**
     * 登录地址
     */
    @TableField(value = "login_address")
    @ApiModelProperty(value="登录地址")
    private String loginAddress;

    /**
     * 登录时间
     */
    @TableField(value = "login_time")
    @ApiModelProperty(value="登录时间")
    private Date loginTime;

    public static final String COL_ID = "id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_CLIENT_TYPE = "client_type";

    public static final String COL_LOGIN_IP = "login_ip";

    public static final String COL_LOGIN_ADDRESS = "login_address";

    public static final String COL_LOGIN_TIME = "login_time";
}