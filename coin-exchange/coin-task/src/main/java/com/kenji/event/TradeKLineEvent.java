package com.kenji.event;

import com.kenji.dto.CreateKLineDto;
import com.kenji.dto.MarketDto;
import com.kenji.feign.MarketServiceFeign;
import com.kenji.model.MessagePayload;
import com.kenji.rocket.Source;
import com.kenji.service.TradeKlineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeTypeUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/28 0:07
 * @Description  行情数据的 K 线
 */
@Component
@Slf4j
public class TradeKLineEvent implements Event {

    @Autowired
    private Source source;

    @Autowired
    private MarketServiceFeign marketServiceFeign;

    @Override
    public void handle() {
        List<MarketDto> marketDtos = marketServiceFeign.tradeMarkets();
        if (CollectionUtils.isEmpty(marketDtos)){
            return;
        }
        for (MarketDto marketDto : marketDtos) {
            CreateKLineDto createKLineDto = new CreateKLineDto();
            createKLineDto.setVolume(BigDecimal.ZERO);
            createKLineDto.setPrice(marketDto.getOpenPrice());
            createKLineDto.setSymbol(marketDto.getSymbol().toLowerCase());
            TradeKlineService.queue.offer(createKLineDto);
        }
    }
}
