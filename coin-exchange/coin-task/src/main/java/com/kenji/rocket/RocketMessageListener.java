package com.kenji.rocket;

import com.kenji.domain.ExchangeTrade;
import com.kenji.dto.CreateKLineDto;
import com.kenji.service.TradeKlineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/28 10:55
 * @Description
 */
@Component
@Slf4j
public class RocketMessageListener {

    @StreamListener("exchange_trades_in")
    public void handlerExchangeTrades(List<ExchangeTrade> exchangeTrades){
        log.info("接收到撮合引擎的数据====>{}",exchangeTrades);
        if (!CollectionUtils.isEmpty(exchangeTrades)){
            for (ExchangeTrade exchangeTrade : exchangeTrades) {
                if (exchangeTrade ==null){
                    return;
                }
                CreateKLineDto createKLineDto = exchangeTrade2CreateKLineDto(exchangeTrade);
                TradeKlineService.queue.offer(createKLineDto);
            }
        }
    }

    private CreateKLineDto exchangeTrade2CreateKLineDto(ExchangeTrade exchangeTrade) {
        CreateKLineDto createKLineDto = new CreateKLineDto();
        createKLineDto.setPrice(exchangeTrade.getPrice());
        createKLineDto.setSymbol(exchangeTrade.getSymbol());
        createKLineDto.setVolume(exchangeTrade.getAmount());
        return null;
    }
}
