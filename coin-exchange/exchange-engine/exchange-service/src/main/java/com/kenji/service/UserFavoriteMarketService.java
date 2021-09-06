package com.kenji.service;

import com.kenji.domain.UserFavoriteMarket;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author Kenji
 * @Date 2021/8/25 14:34
 * @Description
 */
public interface UserFavoriteMarketService extends IService<UserFavoriteMarket> {


    /**
     * 取消收藏
     * @param marketId
     * @param userId
     * @return
     */
    Boolean deleteFavorite(Long userId, Long marketId);
}
