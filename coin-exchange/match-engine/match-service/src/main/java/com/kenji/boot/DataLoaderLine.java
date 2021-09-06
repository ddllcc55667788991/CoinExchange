package com.kenji.boot;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kenji.disruptor.DisruptorTemplate;
import com.kenji.domain.EntrustOrder;
import com.kenji.mapper.EntrustOrderMapper;
import com.kenji.model.Order;
import com.kenji.utils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/26 17:11
 * @Description
 */
@Component
public class DataLoaderLine implements CommandLineRunner {

    @Autowired
    private EntrustOrderMapper entrustOrderMapper;

    @Autowired
    private DisruptorTemplate disruptorTemplate;

    @Override
    public void run(String... args) throws Exception {
        List<EntrustOrder> entrustOrders = entrustOrderMapper.selectList(new LambdaQueryWrapper<EntrustOrder>()
                .eq(EntrustOrder::getStatus, 0)
                .orderByAsc(EntrustOrder::getCreated));
        if (!CollectionUtils.isEmpty(entrustOrders)){
            for (EntrustOrder entrustOrder : entrustOrders) {
                disruptorTemplate.onData(BeanUtils.entrustOrder2Order(entrustOrder));
            }
        }
    }


}
