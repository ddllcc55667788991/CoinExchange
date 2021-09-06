package com.kenji.feign;

import com.kenji.config.feign.OAuth2FeignConfig;
import com.kenji.domain.DepthItemVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author Kenji
 * @Date 2021/8/27 11:21
 * @Description
 */
@FeignClient(name = "match-service",configuration = OAuth2FeignConfig.class)
public interface OrderBookFeignClient {

    /**
     * 远程调用深度数据
     * @param symbol
     * @return
     */
    @GetMapping("/match/depth")
    Map<String, List<DepthItemVo>> getDepth(@RequestParam(required = true) String symbol);
}
