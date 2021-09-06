package com.kenji.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.UserFavoriteMarketMapper;
import com.kenji.domain.UserFavoriteMarket;
import com.kenji.service.UserFavoriteMarketService;
/**
 * @Author  Kenji
 * @Date  2021/8/19 10:36
 * @Description 
 */
@Service
public class UserFavoriteMarketServiceImpl extends ServiceImpl<UserFavoriteMarketMapper, UserFavoriteMarket> implements UserFavoriteMarketService{

}
