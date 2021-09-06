package com.kenji.feign;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Author Kenji
 * @Date 2021/8/17 15:28
 * @Description
 */
@Data
public class JWTResult {

    /**
     * accessToken
     */
    @JsonProperty(value = "access_token")
    private String accessToken;

    /**
     * token类型
     */
    @JsonProperty(value = "token_type")
    private String tokenType;

    /**
     * refreshToken
     */
    @JsonProperty(value = "refresh_token")
    private String refreshToken;

    /**
     * token过期时间
     */
    @JsonProperty(value = "expires_in")
    private Long expiresIn;

    /**
     * token范围
     */
    @JsonProperty(value = "scope")
    private String scope;

    /**
     * 颁发的凭证
     */
    @JsonProperty(value = "jti")
    private String jti;
}
