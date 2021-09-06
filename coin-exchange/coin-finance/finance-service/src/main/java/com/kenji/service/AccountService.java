package com.kenji.service;

import com.kenji.domain.Account;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kenji.domain.CashRecharge;
import com.kenji.vo.SymbolAssetVo;
import com.kenji.vo.UserTotalAccountVo;

import java.math.BigDecimal;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:19
 * @Description
 */
public interface AccountService extends IService<Account> {

    /**
     * 用户账户充值
     * @param userId
     * @param cashRecharge
     * @return
     */
    Boolean transferAccountAmount(Long userId, CashRecharge cashRecharge,Long orderId,String remark,String businessType,Byte direction);

    /**
     * 获取当前用户的货币的资产情况
     * @param userId
     * @param coinName
     * @return
     */
    Account findUserAndCoin(Long userId, String coinName);

    /**
     * 暂时锁定用户资产
     * @param userId    用户ID
     * @param coinId    币种ID
     * @param mum   锁定的金额
     * @param type  业务类型
     * @param orderId   订单ID
     * @param fee   本次操作的手续费
     */
    void lockUserAmount(Long userId, Long coinId, BigDecimal mum, String type, Long orderId, BigDecimal fee);

    /**
     * 用户用户ID获取用户总资产
     * @param userId
     * @return
     */
    UserTotalAccountVo getUserTotalAccount(Long userId);

    /**
     * 获取交易对资产
     * @param userId
     * @param symbol
     * @return
     */
    SymbolAssetVo getSymbolAssert(Long userId, String symbol);

    /**
     * 划转买入的账户余额
     *
     * @param fromUserId
     * @param toUserId
     * @param coinId
     * @param amount
     * @param businessType
     * @param orderId
     */
    void transferBuyAmount(Long fromUserId, Long toUserId, Long coinId, BigDecimal amount, String businessType, Long orderId);

    /**
     * 划转出售成功的账户余额
     *
     * @param fromUserId
     * @param toUserId
     * @param coinId
     * @param amount
     * @param businessType
     * @param orderId
     */
    void transferSellAmount(Long fromUserId, Long toUserId, Long coinId, BigDecimal amount, String businessType, Long orderId);
}
