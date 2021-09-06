package com.kenji.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author  Kenji
 * @Date  2021/8/19 10:36
 * @Description 
 */
/**
    * 用户表
    */
@ApiModel(value="com-kenji-domain-User")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "`user`")
public class User {
    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="自增id")
    @NotNull
    private Long id;

    /**
     * 用户类型：1-普通用户；2-代理人
     */
    @TableField(value = "type")
    @ApiModelProperty(value="用户类型：1-普通用户；2-代理人")
    private Byte type;

    /**
     * 用户名
     */
    @TableField(value = "username")
    @ApiModelProperty(value="用户名")
    private String username;

    /**
     * 国际电话区号
     */
    @TableField(value = "country_code")
    @ApiModelProperty(value="国际电话区号")
    @NotBlank
    private String countryCode;

    /**
     * 手机号
     */
    @TableField(value = "mobile")
    @ApiModelProperty(value="手机号")
    @NotBlank
    private String mobile;

    /**
     * 密码
     */
    @TableField(value = "password")
    @ApiModelProperty(value="密码")
    private String password;

    /**
     * 交易密码
     */
    @TableField(value = "paypassword")
    @ApiModelProperty(value="交易密码")
    private String paypassword;

    /**
     * 交易密码设置状态
     */
    @TableField(value = "paypass_setting")
    @ApiModelProperty(value="交易密码设置状态")
    private Boolean paypassSetting;

    /**
     * 邮箱
     */
    @TableField(value = "email")
    @ApiModelProperty(value="邮箱")
    @NotBlank
    private String email;

    /**
     * 真实姓名
     */
    @TableField(value = "real_name")
    @ApiModelProperty(value="真实姓名")
    private String realName;

    /**
     * 证件类型:1，身份证；2，军官证；3，护照；4，台湾居民通行证；5，港澳居民通行证；9，其他；
     */
    @TableField(value = "id_card_type")
    @ApiModelProperty(value="证件类型:1，身份证；2，军官证；3，护照；4，台湾居民通行证；5，港澳居民通行证；9，其他；")
    private Integer idCardType;

    /**
     * 认证状态：0-未认证；1-初级实名认证；2-高级实名认证
     */
    @TableField(value = "auth_status")
    @ApiModelProperty(value="认证状态：0-未认证；1-初级实名认证；2-高级实名认证")
    private Byte authStatus;

    /**
     * Google令牌秘钥
     */
    @TableField(value = "ga_secret")
    @ApiModelProperty(value="Google令牌秘钥")
    private String gaSecret;

    /**
     * Google认证开启状态,0,未启用，1启用
     */
    @TableField(value = "ga_status")
    @ApiModelProperty(value="Google认证开启状态,0,未启用，1启用")
    private Boolean gaStatus;

    /**
     * 身份证号
     */
    @TableField(value = "id_card")
    @ApiModelProperty(value="身份证号")
    @NotBlank
    private String idCard;

    /**
     * 代理商级别
     */
    @TableField(value = "level")
    @ApiModelProperty(value="代理商级别")
    private Integer level;

    /**
     * 认证时间
     */
    @TableField(value = "authtime")
    @ApiModelProperty(value="认证时间")
    private Date authtime;

    /**
     * 登录数
     */
    @TableField(value = "logins")
    @ApiModelProperty(value="登录数")
    private Integer logins;

    /**
     * 状态：0，禁用；1，启用；
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态：0，禁用；1，启用；")
    private Byte status;

    /**
     * 邀请码
     */
    @TableField(value = "invite_code")
    @ApiModelProperty(value="邀请码")
    private String inviteCode;

    /**
     * 邀请关系
     */
    @TableField(value = "invite_relation")
    @ApiModelProperty(value="邀请关系")
    private String inviteRelation;

    /**
     * 直接邀请人ID
     */
    @TableField(value = "direct_inviteid")
    @ApiModelProperty(value="直接邀请人ID")
    private String directInviteid;

    /**
     * 0 否 1是  是否开启平台币抵扣手续费
     */
    @TableField(value = "is_deductible")
    @ApiModelProperty(value="0 否 1是  是否开启平台币抵扣手续费")
    private Integer isDeductible;

    /**
     * 审核状态,1通过,2拒绝,0,待审核
     */
    @TableField(value = "reviews_status")
    @ApiModelProperty(value="审核状态,1通过,2拒绝,0,待审核")
    private Integer reviewsStatus;

    /**
     * 代理商拒绝原因
     */
    @TableField(value = "agent_note")
    @ApiModelProperty(value="代理商拒绝原因")
    private String agentNote;

    /**
     * API的KEY
     */
    @TableField(value = "access_key_id")
    @ApiModelProperty(value="API的KEY")
    private String accessKeyId;

    /**
     * API的密钥
     */
    @TableField(value = "access_key_secret")
    @ApiModelProperty(value="API的密钥")
    private String accessKeySecret;

    /**
     * 引用认证状态id
     */
    @TableField(value = "refe_auth_id")
    @ApiModelProperty(value="引用认证状态id")
    private Long refeAuthId;

    /**
     * 修改时间
     */
    @TableField(value = "last_update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="修改时间")
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    @TableField(value = "created",fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private Date created;


    /**
     * 高级认证状态
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "高级认证状态")
    private  Byte seniorAuthStatus;

    /**
     * 高级认证描述
     */
    @ApiModelProperty(value = "高级认证描述")
    @TableField(exist = false)
    private String seniorAuthDesc;

    public static final String COL_ID = "id";

    public static final String COL_TYPE = "type";

    public static final String COL_USERNAME = "username";

    public static final String COL_COUNTRY_CODE = "country_code";

    public static final String COL_MOBILE = "mobile";

    public static final String COL_PASSWORD = "password";

    public static final String COL_PAYPASSWORD = "paypassword";

    public static final String COL_PAYPASS_SETTING = "paypass_setting";

    public static final String COL_EMAIL = "email";

    public static final String COL_REAL_NAME = "real_name";

    public static final String COL_ID_CARD_TYPE = "id_card_type";

    public static final String COL_AUTH_STATUS = "auth_status";

    public static final String COL_GA_SECRET = "ga_secret";

    public static final String COL_GA_STATUS = "ga_status";

    public static final String COL_ID_CARD = "id_card";

    public static final String COL_LEVEL = "level";

    public static final String COL_AUTHTIME = "authtime";

    public static final String COL_LOGINS = "logins";

    public static final String COL_STATUS = "status";

    public static final String COL_INVITE_CODE = "invite_code";

    public static final String COL_INVITE_RELATION = "invite_relation";

    public static final String COL_DIRECT_INVITEID = "direct_inviteid";

    public static final String COL_IS_DEDUCTIBLE = "is_deductible";

    public static final String COL_REVIEWS_STATUS = "reviews_status";

    public static final String COL_AGENT_NOTE = "agent_note";

    public static final String COL_ACCESS_KEY_ID = "access_key_id";

    public static final String COL_ACCESS_KEY_SECRET = "access_key_secret";

    public static final String COL_REFE_AUTH_ID = "refe_auth_id";

    public static final String COL_LAST_UPDATE_TIME = "last_update_time";

    public static final String COL_CREATED = "created";
}