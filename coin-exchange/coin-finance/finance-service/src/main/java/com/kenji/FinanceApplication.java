package com.kenji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:23
 * @Description
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class FinanceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinanceApplication.class,args);
    }
}
