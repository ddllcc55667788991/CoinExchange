package com.kenji.feign;

import com.kenji.config.feign.OAuth2FeignConfig;
import com.kenji.dto.AdminBankDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/24 14:53
 * @Description
 */
@FeignClient(path = "/adminBanks",value = "admin-service",configuration = OAuth2FeignConfig.class)
public interface AdminServiceFeign {

    @GetMapping("/list")
    List<AdminBankDto> findAdminBank();
}
