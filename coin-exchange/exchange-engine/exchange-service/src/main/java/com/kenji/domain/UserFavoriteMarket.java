package com.kenji.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author  Kenji
 * @Date  2021/8/25 14:34
 * @Description 
 */
/**
    * 用户收藏交易市场
    */
@ApiModel(value="com-kenji-domain-UserFavoriteMarket")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_favorite_market")
public class UserFavoriteMarket {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 交易对类型：1-币币交易；2-创新交易；
     */
    @TableField(value = "type")
    @ApiModelProperty(value="交易对类型：1-币币交易；2-创新交易；")
    private Integer type;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    @ApiModelProperty(value="用户ID")
    private Long userId;

    /**
     * 交易对ID
     */
    @TableField(value = "market_id")
    @ApiModelProperty(value="交易对ID")
    private Long marketId;

    /**
     * 交易对
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "交易对")
    private String symbol;

    public static final String COL_ID = "id";

    public static final String COL_TYPE = "type";

    public static final String COL_USER_ID = "user_id";

    public static final String COL_MARKET_ID = "market_id";
}