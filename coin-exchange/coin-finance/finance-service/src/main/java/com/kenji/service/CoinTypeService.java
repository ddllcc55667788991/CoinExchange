package com.kenji.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.CoinType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:19
 * @Description
 */
public interface CoinTypeService extends IService<CoinType> {

    /**
     * 条件分页查询币种类型
     * @param page  分页数据
     * @param code  币种类型
     * @return
     */
    Page<CoinType> showCoinTypeByPage(Page<CoinType> page, String code);

    /**
     * 查询币种类型
     * @param status    币种状态
     * @return
     */
    List<CoinType> listAll(Byte status);

}
