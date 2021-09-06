package com.kenji.task;

import com.kenji.constant.Constants;
import com.kenji.dto.MarketDto;
import com.kenji.event.KlineEvent;
import com.kenji.feign.MarketServiceFeign;
import com.kenji.rocket.Source;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author Kenji
 * @Date 2021/8/28 0:10
 * @Description  K 线数据的推送
 */
//@Component
@Slf4j
public class TopicKLineTask {

    @Autowired
    private MarketServiceFeign marketServiceFeign;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private Source source;

    private ExecutorService executor = null;
    {
        executor = new ThreadPoolExecutor(
                5,
                10,
                100L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(30),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
    /**
     * 每 3 秒推送 K 线数据
     * 推送时：是由executor实际上做数据的推送
     */
    @Scheduled(fixedRate = 3000)
    public void pushKline() {
        List<MarketDto> marketDtos = marketServiceFeign.tradeMarkets();
        if (!CollectionUtils.isEmpty(marketDtos)){
            for (MarketDto marketDto : marketDtos) {
                KlineEvent klineEvent = new KlineEvent(marketDto.getSymbol().toLowerCase(), "market.%s.line.%s", Constants.REDIS_KEY_TRADE_KLINE);
                klineEvent.setRedisTemplate(redisTemplate);
                klineEvent.setSource(source);
                executor.submit(klineEvent);
            }
        }
        executor.submit(new KlineEvent());
    }
}
