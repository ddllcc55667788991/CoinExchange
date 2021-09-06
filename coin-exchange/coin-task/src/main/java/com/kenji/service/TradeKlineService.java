package com.kenji.service;

import com.kenji.constant.Constants;
import com.kenji.dto.CreateKLineDto;
import com.kenji.enums.KlineType;
import com.kenji.model.Line;
import com.kenji.util.KlineTimeUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @Author Kenji
 * @Date 2021/8/28 10:02
 * @Description K线的生成
 */
@Component
public class TradeKlineService implements CommandLineRunner {

    //当交易完成（撮合）后，就触发k线的生成
    public static BlockingQueue<CreateKLineDto> queue =new LinkedBlockingDeque<>();

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        while (true){
            CreateKLineDto createKLineDto = queue.poll();   //从里面获取一个数据,没有数据会阻塞
            if (createKLineDto!=null){
                for (KlineType klineType : KlineType.values()) {
                    this.generateKLine(createKLineDto,klineType);
                }
            }
        }
    }

    /**
     * 为当前交易数据生成K线
     * @param klineData
     * @param klineType
     */
    private void generateKLine(CreateKLineDto klineData, KlineType klineType) {
        // 1 获取之前该K线的数据 keyPrefix:etcgcn:1min
        String redisKey = new StringBuffer().append(Constants.REDIS_KEY_TRADE_KLINE)
                .append(klineData.getSymbol())
                .append(":")
                .append(klineType.getValue().toLowerCase()).toString();
        Long size = redisTemplate.opsForList().size(redisKey);
        DateTime dateTime = KlineTimeUtil.getKLineTime(klineType);
        // 2 之前没有该k线的数据
        if (size == 0){
            Line line = new Line(dateTime, klineData.getPrice(), klineData.getVolume());
            //放在Redis里面
            redisTemplate.opsForList().leftPush(redisKey,line.toKline());
            return;
        }
        // 3 之前有数据，获取最近的一个数据
        String historyKlineData = redisTemplate.opsForList().range(redisKey, 0, 1).get(0);
        Line historyKline = new Line(historyKlineData);
        // 4 若当前的时间：是否还在上一个时间的区间内
        if (dateTime.compareTo(historyKline.getTime()) ==1){

            //redis的容量是否超
            if (size >Constants.REDIS_MAX_CACHE_KLINE_SIZE){
                redisTemplate.opsForList().rightPop(redisKey);
            }
            Line line = new Line();
            line.setTime(dateTime);
            //如果我们当前的交易量为0
            if (klineData.getVolume().compareTo(BigDecimal.ZERO)==0){
                line.setHigh(historyKline.getClose());
                line.setLow(historyKline.getClose());
                line.setClose(historyKline.getClose());
                line.setOpen(historyKline.getClose());
                line.setVolume(BigDecimal.ZERO);
                //放在redis里面
                redisTemplate.opsForList().leftPush(redisKey,line.toKline());
                return;
            }
            line.setOpen(klineData.getPrice());
            line.setClose(klineData.getPrice());
            //最高价，最低价
            if (klineData.getPrice().compareTo(historyKline.getHigh())==1){
                line.setHigh(klineData.getPrice());
                line.setLow(historyKline.getClose());
            }
            if (klineData.getPrice().compareTo(historyKline.getLow())==1){
                line.setLow(klineData.getPrice());
                line.setHigh(historyKline.getClose());
            }
            historyKline.setClose(klineData.getPrice());
            redisTemplate.opsForList().set(redisKey,0,historyKlineData.toLowerCase());
            //放最新的进入redis里面
            redisTemplate.opsForList().leftPush(redisKey,line.toKline());
            return;
        }
        if (klineData.getVolume().compareTo(BigDecimal.ZERO)==0){
            return;
        }
        historyKline.setClose(klineData.getPrice());
        if (klineData.getPrice().compareTo(historyKline.getHigh())==1){
            historyKline.setHigh(klineData.getPrice());
        }
        if (klineData.getPrice().compareTo(historyKline.getLow())==1){
            historyKline.setLow(klineData.getPrice());
        }
        historyKline.setVolume(historyKline.getVolume().add(klineData.getVolume()));
        redisTemplate.opsForList().set(redisKey,0,historyKline.toKline());
    }
}
