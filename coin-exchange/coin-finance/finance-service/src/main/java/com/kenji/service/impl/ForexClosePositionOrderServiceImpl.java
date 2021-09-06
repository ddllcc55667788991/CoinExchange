package com.kenji.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.ForexClosePositionOrderMapper;
import com.kenji.domain.ForexClosePositionOrder;
import com.kenji.service.ForexClosePositionOrderService;
/**
 * @Author  Kenji
 * @Date  2021/8/23 0:19
 * @Description 
 */
@Service
public class ForexClosePositionOrderServiceImpl extends ServiceImpl<ForexClosePositionOrderMapper, ForexClosePositionOrder> implements ForexClosePositionOrderService{

}
