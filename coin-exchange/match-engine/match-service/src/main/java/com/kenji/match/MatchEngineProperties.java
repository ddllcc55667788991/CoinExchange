package com.kenji.match;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @Author Kenji
 * @Date 2021/8/26 15:36
 * @Description
 */
@Data
@ConfigurationProperties(prefix = "spring.match")
public class MatchEngineProperties {

    private Map<String,CoinScale> symbols;

    @Data
    public static class CoinScale{
        /**
         * 交易币种的精度
         */
        private int coinScale;

        /**
         * 基币的精度
         */
        private int baseCoinScale;
    }
}
