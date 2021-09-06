package com.kenji.rocket;

import com.kenji.domain.ExchangeTrade;
import com.kenji.service.EntrustOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/27 14:14
 * @Description
 */
@Component
@Slf4j
public class ExchangeTradeListener {

    @Autowired
    private EntrustOrderService entrustOrderService;

    /**
     * 交易数据的监听
     * @param exchangeTrades
     */
    @Transactional
    @StreamListener("exchange_trades_out")
    public void receiveExchangeTrade(List<ExchangeTrade> exchangeTrades){
        if (!CollectionUtils.isEmpty(exchangeTrades)){
            return;
        }
        for (ExchangeTrade exchangeTrade : exchangeTrades) {
            if (exchangeTrade !=null){
                //交易完成后，去更新数据库
                entrustOrderService.doMatch(exchangeTrade);
            }
        }
    }

    /**
     * 取消订单
     * @param orderId
     */
//    @Transactional
//    @StreamListener("cancel_order_in")
//    public void receiveCancelOrder(String orderId){
//        entrustOrderService.cancelEntrustOrderDb(orderId);
//    }
}
