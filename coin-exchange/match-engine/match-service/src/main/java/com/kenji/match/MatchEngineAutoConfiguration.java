package com.kenji.match;

import com.kenji.disruptor.OrderEvent;
import com.kenji.disruptor.OrderMatchDisruptorHandler;
import com.kenji.model.OrderBooks;
import com.lmax.disruptor.EventHandler;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @Author Kenji
 * @Date 2021/8/26 15:38
 * @Description
 */
@Configuration
@EnableConfigurationProperties(value = MatchEngineProperties.class)
public class MatchEngineAutoConfiguration {

    private MatchEngineProperties matchEngineProperties;

    public MatchEngineAutoConfiguration(MatchEngineProperties matchEngineProperties) {
        this.matchEngineProperties = matchEngineProperties;
    }

    @Bean("eventHandlers")
    public EventHandler<OrderEvent>[] eventHandlers() {
        Map<String, MatchEngineProperties.CoinScale> symbols = matchEngineProperties.getSymbols();
        EventHandler[] eventHandlers = new EventHandler[symbols.size()];
        int i = 0;
        for (Map.Entry<String, MatchEngineProperties.CoinScale> entry : symbols.entrySet()) {
            String symbol = entry.getKey();
            MatchEngineProperties.CoinScale coinScale = entry.getValue();
            OrderBooks orderBooks = null;
            if (coinScale != null) {
                orderBooks = new OrderBooks(symbol, coinScale.getCoinScale(), coinScale.getBaseCoinScale());
            }else {
                orderBooks =new OrderBooks(symbol);
            }
            eventHandlers[i++] = new OrderMatchDisruptorHandler(orderBooks);
        }
        return eventHandlers;
    }
}
