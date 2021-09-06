package com.kenji.feign;

import com.kenji.config.feign.OAuth2FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @Author Kenji
 * @Date 2021/8/26 9:31
 * @Description
 */
@FeignClient(name = "finance-service", path = "/account", configuration = OAuth2FeignConfig.class, contextId = "AccountServiceFeign")
public interface AccountServiceFeign {


    /**
     * 锁定用户的余额
     * @param userId    用户id
     * @param coinId    币种id
     * @param mum   锁定数量
     * @param type  业务类型
     * @param orderId   订单编号
     * @param fee   手续费
     */
    @PostMapping("/lockUserAmount")
    void lockUserAmount(@RequestParam("userId") Long userId, @RequestParam("coinId") Long coinId,
                        @RequestParam("mum") BigDecimal mum, @RequestParam("type") String type,
                        @RequestParam("orderId") Long orderId, @RequestParam("fee") BigDecimal fee);

    /**
     * 划转买入的账户余额
     * @param fromUserId
     * @param toUserId
     * @param coinId
     * @param amount
     * @param businessType
     * @param orderId
     */
    @PostMapping("/transferBuyAmount")
    void transferBuyAmount(@RequestParam("fromUserId") Long fromUserId, @RequestParam("toUserId") Long toUserId,
                           @RequestParam("coinId") Long coinId, @RequestParam("amount") BigDecimal amount,
                           @RequestParam("businessType") String businessType, @RequestParam("orderId") Long orderId);



    /**
     * 划转出售成功的账户余额
     * @param fromUserId
     * @param toUserId
     * @param coinId
     * @param amount
     * @param businessType
     * @param orderId
     */
    @PostMapping("/transferSellAmount")
    void transferSellAmount(@RequestParam("fromUserId") Long fromUserId, @RequestParam("toUserId") Long toUserId,
                            @RequestParam("coinId") Long coinId, @RequestParam("amount") BigDecimal amount,
                            @RequestParam("businessType") String businessType, @RequestParam("orderId") Long orderId);




}
