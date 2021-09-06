package com.kenji.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author Kenji
 * @Date 2021/8/20 19:05
 * @Description
 */
@ConfigurationProperties(prefix = "identify")
@Data
public class IdProprties {
    /**
     * Aliyun_appKey
     */
    private String appKey;

    /**
     * Aliyun_appSecret
     */
    private String appSecret;

    /**
     * Aliyun_appSecret
     */
    private String appCode;

    /**
     * Aliyun_url
     */
    private String url;

}
