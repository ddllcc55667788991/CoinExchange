package com.kenji.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author Kenji
 * @Date 2021/8/28 2:00
 * @Description
 */
public enum DepthMergeType {
    DEFAULT("step0", 0),
    LOW("step1", 1),
    HIGH("step2", 2);
    /**
     * 代码
     */
    private String code;
    /**
     * 值
     */
    private int value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    DepthMergeType(String code, int value) {
        this.code = code;
        this.value = value;
    }

    public static DepthMergeType getByCode(String code) {
        if (StringUtils.isEmpty(code)) {
            return null;
        }
        for (DepthMergeType depthMergeType : DepthMergeType.values()) {
            if (depthMergeType.getCode().equals(code)) {
                return depthMergeType;
            }
        }
        return null;
    }

    public static DepthMergeType getByValue(int value) {
        for (DepthMergeType depthMergeType : DepthMergeType.values()) {
            if (depthMergeType.getValue() == value) {
                return depthMergeType;
            }
        }
        return null;
    }
}
