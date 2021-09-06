package com.kenji.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author  Kenji
 * @Date  2021/8/23 0:19
 * @Description 
 */
/**
    * 币种配置信息
    */
@ApiModel(value="com-kenji-domain-Coin")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "coin")
public class Coin {
    /**
     * 币种ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="币种ID")
    private Long id;

    /**
     * 币种名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value="币种名称")
    @NotBlank
    private String name;

    /**
     * 币种标题
     */
    @TableField(value = "title")
    @ApiModelProperty(value="币种标题")
    @NotBlank
    private String title;

    /**
     * 币种logo
     */
    @TableField(value = "img")
    @ApiModelProperty(value="币种logo")
    @NotBlank
    private String img;

    /**
     * xnb：人民币 default：比特币系列 ETH：以太坊 ethToken：以太坊代币
     */
    @TableField(value = "type")
    @ApiModelProperty(value="xnb：人民币,default：比特币系列,ETH：以太坊,ethToken：以太坊代币,,")
    private String type;

    /**
     * rgb：认购币 qbb：钱包币
     */
    @TableField(value = "wallet")
    @ApiModelProperty(value="rgb：认购币,qbb：钱包币,")
    @NotBlank
    private String wallet;

    /**
     * 小数位数
     */
    @TableField(value = "round")
    @ApiModelProperty(value="小数位数")
    private Byte round;

    /**
     * 最小提现单位
     */
    @TableField(value = "base_amount")
    @ApiModelProperty(value="最小提现单位")
    @NotNull
    private BigDecimal baseAmount;

    /**
     * 单笔最小提现数量
     */
    @TableField(value = "min_amount")
    @ApiModelProperty(value="单笔最小提现数量")
    @NotNull
    private BigDecimal minAmount;

    /**
     * 单笔最大提现数量
     */
    @TableField(value = "max_amount")
    @ApiModelProperty(value="单笔最大提现数量")
    @NotNull
    private BigDecimal maxAmount;

    /**
     * 当日最大提现数量
     */
    @TableField(value = "day_max_amount")
    @ApiModelProperty(value="当日最大提现数量")
    @NotNull
    private BigDecimal dayMaxAmount;

    /**
     * status=1：启用 0：禁用
     */
    @TableField(value = "status")
    @ApiModelProperty(value="status=1：启用,0：禁用")
    private byte status;

    /**
     * 自动转出数量
     */
    @TableField(value = "auto_out")
    @ApiModelProperty(value="自动转出数量")
    private Double autoOut;

    /**
     * 手续费率
     */
    @TableField(value = "rate")
    @ApiModelProperty(value="手续费率")
    @NotNull
    private Double rate;

    /**
     * 最低收取手续费个数
     */
    @TableField(value = "min_fee_num")
    @ApiModelProperty(value="最低收取手续费个数")
    @NotNull
    private BigDecimal minFeeNum;

    /**
     * 提现开关
     */
    @TableField(value = "withdraw_flag")
    @ApiModelProperty(value="提现开关")
    private Byte withdrawFlag;

    /**
     * 充值开关
     */
    @TableField(value = "recharge_flag")
    @ApiModelProperty(value="充值开关")
    private Byte rechargeFlag;

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

    public static final String COL_ID = "id";

    public static final String COL_NAME = "name";

    public static final String COL_TITLE = "title";

    public static final String COL_IMG = "img";

    public static final String COL_TYPE = "type";

    public static final String COL_WALLET = "wallet";

    public static final String COL_ROUND = "round";

    public static final String COL_BASE_AMOUNT = "base_amount";

    public static final String COL_MIN_AMOUNT = "min_amount";

    public static final String COL_MAX_AMOUNT = "max_amount";

    public static final String COL_DAY_MAX_AMOUNT = "day_max_amount";

    public static final String COL_STATUS = "status";

    public static final String COL_AUTO_OUT = "auto_out";

    public static final String COL_RATE = "rate";

    public static final String COL_MIN_FEE_NUM = "min_fee_num";

    public static final String COL_WITHDRAW_FLAG = "withdraw_flag";

    public static final String COL_RECHARGE_FLAG = "recharge_flag";

    public static final String COL_LAST_UPDATE_TIME = "last_update_time";

    public static final String COL_CREATED = "created";
}