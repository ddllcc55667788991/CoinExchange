package com.kenji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author Kenji
 * @Date 2021/8/24 22:24
 * @Description
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class ExchangeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ExchangeApplication.class,args);
    }
}
