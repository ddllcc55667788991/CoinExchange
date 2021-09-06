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
 * @Date  2021/8/23 0:19
 * @Description 
 */
@ApiModel(value="com-kenji-domain-UserCoinFreeze")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "user_coin_freeze")
public class UserCoinFreeze {
    @TableId(value = "user_id", type = IdType.INPUT)
    @ApiModelProperty(value="")
    private Long userId;

    @TableField(value = "coin_id")
    @ApiModelProperty(value="")
    private Long coinId;

    @TableField(value = "freeze")
    @ApiModelProperty(value="")
    private Long freeze;

    public static final String COL_USER_ID = "user_id";

    public static final String COL_COIN_ID = "coin_id";

    public static final String COL_FREEZE = "freeze";
}