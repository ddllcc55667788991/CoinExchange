package com.kenji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author Kenji
 * @Date 2021/8/26 10:16
 * @Description
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class MatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatchApplication.class,args);
    }
}
