package com.kenji.geetest;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author Kenji
 * @Date 2021/8/20 10:12
 * @Description
 */
@Configuration
@EnableConfigurationProperties(GeetestProperties.class)
public class GeetestAutoConfiguration {

    private GeetestProperties geetestProperties;

    public GeetestAutoConfiguration(GeetestProperties geetestProperties){
        this.geetestProperties=geetestProperties;
    }

    @Bean
    public GeetestLib geetestLib(){
        return new GeetestLib(geetestProperties.getGeetestId(),geetestProperties.getGeetestKey());
    }
}
