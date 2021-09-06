package com.kenji.task;

import com.kenji.event.TradeKLineEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @Author Kenji
 * @Date 2021/8/28 0:12
 * @Description  关于交易事件的触发
 */
//@Component
@Slf4j
public class TradeKLineTask {
    @Autowired
    private TradeKLineEvent tradeKLineEvent;

    /**
     * 币币交易生成一次 K 线
     */
    @Scheduled(fixedRate = 25000)
    public void generateKLine() {
        tradeKLineEvent.handle();
    }
}
