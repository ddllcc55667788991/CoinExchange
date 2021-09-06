package com.kenji.feign;

import com.kenji.config.feign.OAuth2FeignConfig;
import com.kenji.dto.CoinDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/25 0:32
 * @Description
 */
@FeignClient(name = "finance-service",path = "/coins",configuration = OAuth2FeignConfig.class,contextId = "CoinServiceFeign")
public interface CoinServiceFeign {

    @GetMapping("/list")
    List<CoinDto> getCoinList(@RequestParam List<Long> coinIds);
}
