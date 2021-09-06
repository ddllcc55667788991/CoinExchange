package com.kenji.config;

import cn.hutool.core.lang.Snowflake;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Kenji
 * @Date 2021/8/23 10:38
 * @Description
 */
@Configuration
public class IDGenConfig {

    @Value("${id.machine:0}")
    private int machineCode;

    @Value("${id.app:0}")
    private int appCode;

    //雪花算法
    @Bean
    public Snowflake snowflake(){
        Snowflake snowflake = new Snowflake(appCode, machineCode);
        return snowflake;
    }
}
