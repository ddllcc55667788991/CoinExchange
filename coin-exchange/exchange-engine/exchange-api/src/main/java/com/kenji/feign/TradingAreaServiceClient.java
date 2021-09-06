package com.kenji.feign;

import com.kenji.config.feign.OAuth2FeignConfig;
import com.kenji.dto.TradeAreaDto;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/28 11:12
 * @Description
 */
@FeignClient(path = "/tradeAreas",name = "exchange-service",configuration = OAuth2FeignConfig.class,contextId = "TradingAreaServiceClient")
public interface TradingAreaServiceClient {

    /**
     * 查询所有交易区域
     * @return
     */
    @GetMapping("/all")
    List<TradeAreaDto> tradeAreaList();
}
