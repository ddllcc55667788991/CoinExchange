package com.kenji.dto;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sun.istack.internal.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Kenji
 * @Date 2021/8/25 19:05
 * @Description
 */
@Data
@ApiModel(value = "Mark RPC的调用数据")
@AllArgsConstructor
@NoArgsConstructor
public class MarketDto {
    /**
     * 市场ID
     */
    @ApiModelProperty(value = "市场ID")
    private Long id;

    /**
     * 类型：1-数字货币；2：创新交易
     */
    @ApiModelProperty(value = "类型：1-数字货币；2：创新交易")
    @NotNull
    private Byte type;

    /**
     * 交易区域ID
     */
    @ApiModelProperty(value = "交易区域ID")
    @NotNull
    private Long tradeAreaId;

    /**
     * 卖方市场ID
     */
    @ApiModelProperty(value = "卖方市场ID")
    @NotNull
    private Long sellCoinId;

    /**
     * 买方币种ID
     */
    @ApiModelProperty(value = "买方币种ID")
    @NotNull
    private Long buyCoinId;

    /**
     * 交易对标识
     */
    @ApiModelProperty(value = "交易对标识")
    private String symbol;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String name;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 市场logo
     */
    @ApiModelProperty(value = "市场logo")
    private String img;

    /**
     * 开盘价格
     */
    @ApiModelProperty(value = "开盘价格")
    @NotNull
    private BigDecimal openPrice;

    /**
     * 买入手续费率
     */
    @ApiModelProperty(value = "买入手续费率")
    @NotNull
    private BigDecimal feeBuy;

    /**
     * 卖出手续费率
     */
    @ApiModelProperty(value = "卖出手续费率")
    @NotNull
    private BigDecimal feeSell;

    /**
     * 保证金占用比例
     */
    @ApiModelProperty(value = "保证金占用比例")
    @NotNull
    private BigDecimal marginRate;

    /**
     * 单笔最小委托量
     */
    @ApiModelProperty(value = "单笔最小委托量")
    @NotNull
    private BigDecimal numMin;

    /**
     * 单笔最大委托量
     */
    @ApiModelProperty(value = "单笔最大委托量")
    @NotNull
    private BigDecimal numMax;

    /**
     * 单笔最小成交额
     */
    @ApiModelProperty(value = "单笔最小成交额")
    @NotNull
    private BigDecimal tradeMin;

    /**
     * 单笔最大成交额
     */
    @ApiModelProperty(value = "单笔最大成交额")
    @NotNull
    private BigDecimal tradeMax;

    /**
     * 价格小数位
     */
    @ApiModelProperty(value = "价格小数位")
    @NotNull
    private Byte priceScale;

    /**
     * 数量小数位
     */
    @ApiModelProperty(value = "数量小数位")
    @NotNull
    private Byte numScale;

    /**
     * 合约单位
     */
    @ApiModelProperty(value = "合约单位")
    @NotNull
    private Integer contractUnit;

    /**
     * 点
     */
    @ApiModelProperty(value = "点")
    @NotNull
    private BigDecimal pointValue;

    /**
     * 合并深度（格式：4,3,2）4:表示为0.0001；3：表示为0.001
     */
    @ApiModelProperty(value = "合并深度（格式：4,3,2）4:表示为0.0001；3：表示为0.001")
    @NotBlank
    private String mergeDepth;

    /**
     * 交易时间
     */
    @ApiModelProperty(value = "交易时间")
    @NotBlank
    private String tradeTime;

    /**
     * 交易周期
     */
    @ApiModelProperty(value = "交易周期")

    private String tradeWeek;

    /**
     * 排序列
     */
    @ApiModelProperty(value = "排序列")
    @NotNull
    private Integer sort;

    /**
     * 状态
     0禁用
     1启用
     */
    @ApiModelProperty(value = "状态,0禁用,1启用")
    @NotNull
    private Byte status;


}
