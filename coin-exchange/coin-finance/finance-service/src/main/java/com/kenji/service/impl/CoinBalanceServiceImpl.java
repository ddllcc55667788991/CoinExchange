package com.kenji.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.CoinBalanceMapper;
import com.kenji.domain.CoinBalance;
import com.kenji.service.CoinBalanceService;
/**
 * @Author  Kenji
 * @Date  2021/8/23 0:19
 * @Description 
 */
@Service
public class CoinBalanceServiceImpl extends ServiceImpl<CoinBalanceMapper, CoinBalance> implements CoinBalanceService{

}
