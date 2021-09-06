package com.kenji.disruptor;

import java.io.Serializable;

/**
 * @Author Kenji
 * @Date 2021/8/26 11:21
 * @Description
 */
public class OrderEvent implements Serializable {
    private final static Long serialVersionUID = 5516075349620653480L;
    /**
     * 时间戳
     */
    private final long timestamp;

    /**
     * 事件携带的对象
     */
    protected transient Object source;

    public OrderEvent(Object source) {
        timestamp = System.currentTimeMillis();
        this.source = source;
    }

    public OrderEvent() {
        timestamp = System.currentTimeMillis();
    }

    /**
     * clearing Objects From the Ring Buffer
     */
    public void clear() {
        this.source = null;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }
}
