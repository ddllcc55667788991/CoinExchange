package com.kenji.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @Author Kenji
 * @Date 2021/8/28 1:59
 * @Description
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
public class MergeDepthDTO {
    /**
     * 合并类型
     */
    private String mergeType;
    /**
     * 合并精度
     */
    private BigDecimal value;
}
