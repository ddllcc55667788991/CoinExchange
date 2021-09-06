package com.kenji.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author Kenji
 * @Date 2021/8/26 11:08
 * @Description
 */

/**
 * 异常处理
 */
@Slf4j
public class DisruptorHandlerException implements ExceptionHandler {


    @Override
    public void handleEventException(Throwable throwable, long sequence, Object event) {
        log.error("process data error sequence ===>{} event===>{} ex===>{}",sequence,event,throwable.getMessage());
    }

    @Override
    public void handleOnStartException(Throwable throwable) {
        log.error("start disruptor error ===>{}",throwable.getMessage());
    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {
        log.error("shutdown disruptor error ===>{}",throwable.getMessage());
    }
}
