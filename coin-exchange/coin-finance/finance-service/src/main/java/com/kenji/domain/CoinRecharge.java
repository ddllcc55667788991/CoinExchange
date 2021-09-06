package com.kenji.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author  Kenji
 * @Date  2021/8/23 0:19
 * @Description 
 */
/**
    * 数字货币充值记录
    */
@ApiModel(value="com-kenji-domain-CoinRecharge")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "coin_recharge")
public class CoinRecharge {
    /**
     * 自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="自增id")
    private Long id;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="用户id")
    private Long userId;

    /**
     * 币种id
     */
    @TableField(value = "coin_id")
    @ApiModelProperty(value="币种id")
    private Long coinId;

    /**
     * 币种名称
     */
    @TableField(value = "coin_name")
    @ApiModelProperty(value="币种名称")
    private String coinName;

    /**
     * 币种类型
     */
    @TableField(value = "coin_type")
    @ApiModelProperty(value="币种类型")
    private String coinType;

    /**
     * 钱包地址
     */
    @TableField(value = "address")
    @ApiModelProperty(value="钱包地址")
    private String address;

    /**
     * 充值确认数
     */
    @TableField(value = "confirm")
    @ApiModelProperty(value="充值确认数")
    private Integer confirm;

    /**
     * 状态：0-待入帐；1-充值失败，2到账失败，3到账成功；
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态：0-待入帐；1-充值失败，2到账失败，3到账成功；")
    private Integer status;

    /**
     * 交易id
     */
    @TableField(value = "txid")
    @ApiModelProperty(value="交易id")
    private String txid;

    @TableField(value = "amount")
    @ApiModelProperty(value="")
    private BigDecimal amount;

    /**
     * 修改时间
     */
    @TableField(value = "last_update_time")
    @ApiModelProperty(value="修改时间")
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    @TableField(value = "created")
    @ApiModelProperty(value="创建时间")
    private Date created;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户名")
    private String username;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户姓名")
    private String realName;

    public static final String COL_ID = "id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_COIN_ID = "coin_id";

    public static final String COL_COIN_NAME = "coin_name";

    public static final String COL_COIN_TYPE = "coin_type";

    public static final String COL_ADDRESS = "address";

    public static final String COL_CONFIRM = "confirm";

    public static final String COL_STATUS = "status";

    public static final String COL_TXID = "txid";

    public static final String COL_AMOUNT = "amount";

    public static final String COL_LAST_UPDATE_TIME = "last_update_time";

    public static final String COL_CREATED = "created";
}