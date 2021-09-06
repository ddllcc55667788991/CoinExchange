package com.kenji.filter;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

/**
 * @Author Kenji
 * @Date 2021/8/15 17:04
 * @Description
 */
@Component
public class TokenCheckFilter implements GlobalFilter, Ordered {

        @Autowired
        private StringRedisTemplate redisTemplate;

        @Value("${no.require.urls:/admin/login,/user/gt/register," +
                "/user/login,/user/users/register,/user/sms/sendTo," +
                "/admin/webConfigs/banners,/admin/notices/simple,/admin/notices/simple/{id}}")
        private Set<String> noTokenAccessUrls;

    /**
     * 实现判断用户是否携带token，或者token错误的功能
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //不需要token就能访问
        if(!requireToken(exchange)){
            return chain.filter(exchange);
        }
        //获取用户token
        String token = getToken(exchange);
        //如果没token，不能访问
        if (StringUtils.isEmpty(token)){
            return buildNoAuthorizatiedResult(exchange);
        }
        Boolean hasKey = redisTemplate.hasKey(token);
        //token有效，放行
        if (hasKey != null&& hasKey){
            return chain.filter(exchange);
        }
        return buildNoAuthorizatiedResult(exchange);
    }

    /**
     * 给用户响应一个没有token的错误
     * @param exchange
     * @return
     */
    private Mono<Void> buildNoAuthorizatiedResult(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().set("Content-Type","application/json;charset=UTF-8");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("error","NoAuthorization");
        jsonObject.put("errorMsg","Token is Null or Error");
        DataBuffer dataBuffer = response.bufferFactory().wrap(jsonObject.toJSONString().getBytes());
        return response.writeWith(Flux.just(dataBuffer));
    }

    /**
     * 获取从请求头中获取用户的token
     * @param exchange
     * @return
     */
    private String getToken(ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return token ==null ? null : token.replace("bearer ","") ;
    }

    /**
     * 判断该接口是否需要token
     * @param exchange
     * @return
     */
    private boolean requireToken(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        if (noTokenAccessUrls.contains(path)){
            return false;
        }
        if (path.contains("/kline/")){
            return false;
        }
        return true;

    }


    /**
     * 拦截器的顺序
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
