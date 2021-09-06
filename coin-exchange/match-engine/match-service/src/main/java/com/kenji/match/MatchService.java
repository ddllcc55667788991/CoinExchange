package com.kenji.match;

import com.kenji.model.Order;
import com.kenji.model.OrderBooks;

/**
 * @Author Kenji
 * @Date 2021/8/26 16:17
 * @Description
 */
public interface MatchService {

    /**
     * 执行撮合交易
     * @param order
     */
    void match(OrderBooks orderBooks,Order order);
}
