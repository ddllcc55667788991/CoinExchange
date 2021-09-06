package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.UserFavoriteMarketMapper;
import com.kenji.domain.UserFavoriteMarket;
import com.kenji.service.UserFavoriteMarketService;

/**
 * @Author Kenji
 * @Date 2021/8/25 14:34
 * @Description
 */
@Service
public class UserFavoriteMarketServiceImpl extends ServiceImpl<UserFavoriteMarketMapper, UserFavoriteMarket> implements UserFavoriteMarketService {

    /**
     * 取消收藏
     *
     * @param userId
     * @param marketId
     * @return
     */
    @Override
    public Boolean deleteFavorite(Long userId, Long marketId) {
        return remove(new LambdaQueryWrapper<UserFavoriteMarket>()
                .eq(UserFavoriteMarket::getUserId, userId)
                .eq(UserFavoriteMarket::getMarketId,marketId));
    }
}
