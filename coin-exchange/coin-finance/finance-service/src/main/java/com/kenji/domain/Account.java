package com.kenji.domain;

import com.baomidou.mybatisplus.annotation.*;
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
    * 用户财产记录
    */
@ApiModel(value="com-kenji-domain-Account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "account")
public class Account {
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
     * 账号状态：1，正常；2，冻结；
     */
    @TableField(value = "status")
    @ApiModelProperty(value="账号状态：1，正常；2，冻结；")
    private Boolean status;

    /**
     * 币种可用金额
     */
    @TableField(value = "balance_amount")
    @ApiModelProperty(value="币种可用金额")
    private BigDecimal balanceAmount;

    /**
     * 币种冻结金额
     */
    @TableField(value = "freeze_amount")
    @ApiModelProperty(value="币种冻结金额")
    private BigDecimal freezeAmount;

    /**
     * 累计充值金额
     */
    @TableField(value = "recharge_amount")
    @ApiModelProperty(value="累计充值金额")
    private BigDecimal rechargeAmount;

    /**
     * 累计提现金额
     */
    @TableField(value = "withdrawals_amount")
    @ApiModelProperty(value="累计提现金额")
    private BigDecimal withdrawalsAmount;

    /**
     * 净值
     */
    @TableField(value = "net_value")
    @ApiModelProperty(value="净值")
    private BigDecimal netValue;

    /**
     * 占用保证金
     */
    @TableField(value = "lock_margin")
    @ApiModelProperty(value="占用保证金")
    private BigDecimal lockMargin;

    /**
     * 持仓盈亏/浮动盈亏
     */
    @TableField(value = "float_profit")
    @ApiModelProperty(value="持仓盈亏/浮动盈亏")
    private BigDecimal floatProfit;

    /**
     * 总盈亏
     */
    @TableField(value = "total_profit")
    @ApiModelProperty(value="总盈亏")
    private BigDecimal totalProfit;

    /**
     * 充值地址
     */
    @TableField(value = "rec_addr")
    @ApiModelProperty(value="充值地址")
    private String recAddr;

    /**
     * 版本号
     */
    @TableField(value = "version")
    @ApiModelProperty(value="版本号")
    private Long version;

    /**
     * 更新时间
     */
    @TableField(value = "last_update_time",fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value="更新时间")
    private Date lastUpdateTime;

    /**
     * 创建时间
     */
    @TableField(value = "created",fill = FieldFill.INSERT)
    @ApiModelProperty(value="创建时间")
    private Date created;

    /**
     * 出售费率
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "出售费率")
    private BigDecimal sellRate;

    /**
     * 买入费率
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "买入费率")
    private BigDecimal buyRate;

    public static final String COL_ID = "id";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_COIN_ID = "coin_id";

    public static final String COL_STATUS = "status";

    public static final String COL_BALANCE_AMOUNT = "balance_amount";

    public static final String COL_FREEZE_AMOUNT = "freeze_amount";

    public static final String COL_RECHARGE_AMOUNT = "recharge_amount";

    public static final String COL_WITHDRAWALS_AMOUNT = "withdrawals_amount";

    public static final String COL_NET_VALUE = "net_value";

    public static final String COL_LOCK_MARGIN = "lock_margin";

    public static final String COL_FLOAT_PROFIT = "float_profit";

    public static final String COL_TOTAL_PROFIT = "total_profit";

    public static final String COL_REC_ADDR = "rec_addr";

    public static final String COL_VERSION = "version";

    public static final String COL_LAST_UPDATE_TIME = "last_update_time";

    public static final String COL_CREATED = "created";
}