package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.TradeArea;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kenji.vo.TradeAreaMarketVo;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/24 22:23
 * @Description
 */
public interface TradeAreaService extends IService<TradeArea> {

    /**
     * 条件分页查询交易区域
     *
     * @param page   分页数据
     * @param name   名称
     * @param status 状态
     * @return
     */
    Page<TradeArea> findByPage(Page<TradeArea> page, String name, Byte status);

    /**
     * 获取所有已启用的交易区域
     *
     * @param status 状态
     * @return
     */
    List<TradeArea> getAll(Byte status);

    /**
     * 查询交易区域，以及区域下的市场
     * @return
     */
    List<TradeAreaMarketVo> getTradeAreaMarkets();


    /**
     * 查询用户自选交易市场
     * @return
     */
    List<TradeAreaMarketVo> getUserFavorite(Long userid);
}
