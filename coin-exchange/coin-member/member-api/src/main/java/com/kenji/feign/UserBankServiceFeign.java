package com.kenji.feign;

import com.kenji.config.feign.OAuth2FeignConfig;
import com.kenji.dto.UserBankDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author Kenji
 * @Date 2021/8/24 19:30
 * @Description
 */
@FeignClient(name = "member-service",path = "/userBanks",configuration = OAuth2FeignConfig.class,contextId = "userBankServiceFeign")
public interface UserBankServiceFeign {

    /**
     * 根据userid查询userBank信息
     * @param userid
     * @return
     */
    @GetMapping("/{userid}/info")
    UserBankDto getUserBankInfo(@PathVariable Long userid);
}
