package com.kenji.contollrr;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @Author Kenji
 * @Date 2021/8/14 21:55
 * @Description
 */
@RestController
public class GatewayController {

    @GetMapping("/gateway")
    public Set<GatewayFlowRule> getGatewayFlowRules() {
        return GatewayRuleManager.getRules();
    }

    @GetMapping("/api")
    public Set<ApiDefinition> getApiGroupRules(){
        System.out.println("api");
        return GatewayApiDefinitionManager.getApiDefinitions();
    }
}
