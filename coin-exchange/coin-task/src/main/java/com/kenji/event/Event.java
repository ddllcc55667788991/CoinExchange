package com.kenji.event;

/**
 * @Author Kenji
 * @Date 2021/8/28 0:04
 * @Description 事件的接口
 */
public interface Event {
    /**
     * 事件触发处理机制
     *
     */
    void handle();
}
