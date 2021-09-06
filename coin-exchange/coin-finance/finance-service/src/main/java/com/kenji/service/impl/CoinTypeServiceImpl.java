package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.CoinType;
import com.kenji.mapper.CoinTypeMapper;
import com.kenji.service.CoinTypeService;
/**
 * @Author  Kenji
 * @Date  2021/8/23 0:19
 * @Description 
 */
@Service
public class CoinTypeServiceImpl extends ServiceImpl<CoinTypeMapper, CoinType> implements CoinTypeService{

    /**
     * 条件分页查询币种类型
     *
     * @param page 分页数据
     * @param code 币种类型
     * @return
     */
    @Override
    public Page<CoinType> showCoinTypeByPage(Page<CoinType> page, String code) {
        page.addOrder(OrderItem.desc("last_update_time"));
        return super.page(page,new LambdaQueryWrapper<CoinType>()
                .eq(!StringUtils.isEmpty(code),CoinType::getCode,code));
    }

    /**
     * 查询币种类型
     *
     * @param status 币种状态
     * @return
     */
    @Override
    public List<CoinType> listAll(Byte status) {
        return list(new LambdaQueryWrapper<CoinType>().eq(status!=null,CoinType::getStatus,status));
    }


}
