package com.kenji.geetest;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author Kenji
 * @Date 2021/8/20 10:06
 * @Description
 */
@Data
@ConfigurationProperties(prefix = "geetest")
public class GeetestProperties {

    /**
     * 极验的ID
     */
    private String geetestId ;

    /**
     * 极验的key
     */
    private String geetestKey;
}
