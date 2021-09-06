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
    * 数字货币提现记录
    */
@ApiModel(value="com-kenji-domain-CoinWithdraw")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "coin_withdraw")
public class CoinWithdraw {
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
     * 交易id
     */
    @TableField(value = "txid")
    @ApiModelProperty(value="交易id")
    private String txid;

    /**
     * 提现量
     */
    @TableField(value = "num")
    @ApiModelProperty(value="提现量")
    private BigDecimal num;

    /**
     * 手续费()
     */
    @TableField(value = "fee")
    @ApiModelProperty(value="手续费()")
    private BigDecimal fee;

    /**
     * 实际提现
     */
    @TableField(value = "mum")
    @ApiModelProperty(value="实际提现")
    private BigDecimal mum;

    /**
     * 0站内1其他2手工提币
     */
    @TableField(value = "type")
    @ApiModelProperty(value="0站内1其他2手工提币")
    private Boolean type;

    /**
     * 链上手续费花费
     */
    @TableField(value = "chain_fee")
    @ApiModelProperty(value="链上手续费花费")
    private BigDecimal chainFee;

    /**
     * 区块高度
     */
    @TableField(value = "block_num")
    @ApiModelProperty(value="区块高度")
    private Integer blockNum;

    /**
     * 后台审核人员提币备注备注
     */
    @TableField(value = "remark")
    @ApiModelProperty(value="后台审核人员提币备注备注")
    private String remark;

    /**
     * 钱包提币备注备注
     */
    @TableField(value = "wallet_mark")
    @ApiModelProperty(value="钱包提币备注备注")
    private String walletMark;

    /**
     * 当前审核级数
     */
    @TableField(value = "step")
    @ApiModelProperty(value="当前审核级数")
    private Byte step;

    /**
     * 状态：0-审核中；1-成功；2-拒绝；3-撤销；4-审核通过；5-打币中；
     */
    @TableField(value = "status")
    @ApiModelProperty(value="状态：0-审核中；1-成功；2-拒绝；3-撤销；4-审核通过；5-打币中；")
    private Boolean status;

    /**
     * 审核时间
     */
    @TableField(value = "audit_time")
    @ApiModelProperty(value="审核时间")
    private Date auditTime;

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

    public static final String COL_TXID = "txid";

    public static final String COL_NUM = "num";

    public static final String COL_FEE = "fee";

    public static final String COL_MUM = "mum";

    public static final String COL_TYPE = "type";

    public static final String COL_CHAIN_FEE = "chain_fee";

    public static final String COL_BLOCK_NUM = "block_num";

    public static final String COL_REMARK = "remark";

    public static final String COL_WALLET_MARK = "wallet_mark";

    public static final String COL_STEP = "step";

    public static final String COL_STATUS = "status";

    public static final String COL_AUDIT_TIME = "audit_time";

    public static final String COL_LAST_UPDATE_TIME = "last_update_time";

    public static final String COL_CREATED = "created";
}