package com.kenji.task;

import com.kenji.event.DepthEvent;
import com.kenji.event.MarketEvent;
import com.kenji.event.TradeEvent;
import com.kenji.feign.MarketServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author Kenji
 * @Date 2021/8/28 0:09
 * @Description  行情的任务触发
 */
@Component
public class MarketTickerTask {


    @Autowired
    private MarketEvent marketEvent;

    @Autowired
    private TradeEvent tradeEvent;

    @Autowired
    private DepthEvent depthEvent;

    @Autowired
    private MarketServiceFeign marketServiceFeign;

    /**
     * 推送交易对信息
     */
//    @Scheduled(fixedRate = 1000)
    public void pushMarkets() {
        marketEvent.handle();
    }

    /**
     * 推送市场深度
     */
    @Scheduled(fixedRate = 500)
    public void pushDepths() {
        depthEvent.handle();
    }

    /**
     * 推送实时成交订单数据
     */
//    @Scheduled(fixedRate = 500)
    public void pushTrades() {
        tradeEvent.handle();
    }

    /**
     * 刷新 24 小时成交数据
     */
//    @Scheduled(fixedRate = 1000)
    public void refresh24HDeal() {
        marketServiceFeign.tradeMarkets().forEach(market -> {
            if (market.getStatus() == 1) {
                marketServiceFeign.refresh24hour(market.getSymbol().toLowerCase());
            }
        });
    }
}