package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kenji.domain.*;
import com.kenji.dto.MarketDto;
import com.kenji.feign.MarketServiceFeign;
import com.kenji.mappers.AccountVoMappers;
import com.kenji.service.AccountDetailService;
import com.kenji.service.CoinService;
import com.kenji.service.ConfigService;
import com.kenji.vo.AccountVo;
import com.kenji.vo.SymbolAssetVo;
import com.kenji.vo.UserTotalAccountVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.mapper.AccountMapper;
import com.kenji.service.AccountService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * @Author Kenji
 * @Date 2021/8/23 0:19
 * @Description
 */
@Service
@Slf4j
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {



    @Autowired
    private AccountDetailService accountDetailService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private MarketServiceFeign marketServiceFeign;

    /**
     * 用户账户充值
     *
     * @param userId
     * @param cashRecharge
     * @param remark
     * @param businessType
     * @param direction
     * @return
     */
    @Override
    @Transactional
    public Boolean transferAccountAmount(Long userId, CashRecharge cashRecharge, Long orderId, String remark, String businessType, Byte direction) {
        Account account = getCoinAccount(cashRecharge.getCoinId(), cashRecharge.getUserId());
//        if (account == null) {
//            throw new IllegalArgumentException("用户当前的该币种的余额不存在");
//        }
        //增加一条流水记录
        AccountDetail accountDetail = new AccountDetail();
        accountDetail.setCoinId(cashRecharge.getCoinId());
        accountDetail.setUserId(cashRecharge.getUserId());
        accountDetail.setAmount(cashRecharge.getNum());
        accountDetail.setFee(cashRecharge.getFee());
        accountDetail.setOrderId(orderId);
        accountDetail.setAccountId(cashRecharge.getUserId());
        accountDetail.setRefAccountId(userId);
        accountDetail.setRemark(remark);
        accountDetail.setBusinessType(businessType);
        accountDetail.setDirection(direction);
        accountDetail.setCreated(new Date());
        boolean save = accountDetailService.save(accountDetail);
        if (save) {
            //用户余额的增加
            if (account == null) {
                account = new Account();
            }
            account.setBalanceAmount(account == null ? cashRecharge.getNum() : account.getBalanceAmount().add(cashRecharge.getNum()));
            account.setRechargeAmount(account == null ? cashRecharge.getNum() : account.getRechargeAmount().add(cashRecharge.getNum()));
            boolean update = saveOrUpdate(account);
            return update;
        }
        return save;
    }


    private Account getCoinAccount(Long coinId, Long userId) {
        return getOne(new LambdaQueryWrapper<Account>()
                .eq(Account::getCoinId, coinId)
                .eq(Account::getUserId, userId)
                .eq(Account::getStatus, 1));
    }


    /**
     * 获取当前用户的货币的资产情况
     *
     * @param userId
     * @param coinName
     * @return
     */
    @Override
    public Account findUserAndCoin(Long userId, String coinName) {
        Coin coin = coinService.getByName(coinName);
        if (coin == null) {
            throw new IllegalArgumentException("货币名称不正确");
        }
        Account account = getOne(new LambdaQueryWrapper<Account>()
                .eq(userId != null, Account::getUserId, userId)
                .eq(coin.getId() != null, Account::getCoinId, coin.getId()));
        if (account == null) {
            throw new IllegalArgumentException("该资产不存在");
        }

        //卖出费率
        Config sellRateConfig = configService.getConfigByCode("USDT2CNY");
        account.setSellRate(new BigDecimal(sellRateConfig.getValue()));

        //买入费率
        Config buyRateConfig = configService.getConfigByCode("CNY2USDT");
        account.setBuyRate(new BigDecimal(buyRateConfig.getValue()));
        return account;
    }

    /**
     * 暂时锁定用户资产
     *
     * @param userId  用户ID
     * @param coinId  币种ID
     * @param mum     锁定的金额
     * @param type    业务类型
     * @param orderId 订单ID
     * @param fee     本次操作的手续费
     */
    @Override
    @Transactional
    public void lockUserAmount(Long userId, Long coinId, BigDecimal mum, String type, Long orderId, BigDecimal fee) {
        Account account = getOne(new LambdaQueryWrapper<Account>()
                .eq(Account::getCoinId, coinId)
                .eq(Account::getUserId, userId));
        if (account==null){
            throw new IllegalArgumentException("您输入的资产类型不存在");
        }
        BigDecimal balanceAmount = account.getBalanceAmount();
        if(balanceAmount.compareTo(mum)<0){
            throw new IllegalArgumentException("账号的资金不足");
        }
        account.setBalanceAmount(balanceAmount.subtract(mum));
        account.setFreezeAmount(account.getFreezeAmount().add(mum));
        boolean update = updateById(account);
        if (update){
            AccountDetail accountDetail = new AccountDetail(
                    null,
                    userId,
                    coinId,
                    account.getId(),
                    account.getId(),
                    orderId,
                    (byte)2,
                    type,
                    mum,
                    fee,
                    "用户提现",
                    null,
                    null,
                    null
            );
            accountDetailService.save(accountDetail);
        }
    }

    /**
     * 用户用户ID获取用户总资产
     *
     * @param userId
     * @return
     */
    @Override
    public UserTotalAccountVo getUserTotalAccount(Long userId) {
        //计算总资产
        UserTotalAccountVo userTotalAccountVo = new UserTotalAccountVo();
        BigDecimal basicCoin2CnyRate = BigDecimal.ONE;  //汇率
        BigDecimal basicCoin = BigDecimal.ZERO; //平台计算币的基币
        List<AccountVo> assertList = new ArrayList<>();
        //用户的总资产位于Account里面
        List<Account> accounts = list(new LambdaQueryWrapper<Account>().eq(Account::getUserId, userId));
        if (CollectionUtils.isEmpty(accounts)){
            userTotalAccountVo.setAssertList(assertList);
            userTotalAccountVo.setAmountUs(BigDecimal.ZERO);
            userTotalAccountVo.setAmount(BigDecimal.ZERO);
            return userTotalAccountVo;
        }
        AccountVoMappers mappers = AccountVoMappers.mappers;
        //获取所有币种
        for (Account account : accounts) {
            AccountVo accountVo = mappers.toConvertVo(account);
            Long coinId = account.getCoinId();
            Coin coin = coinService.getById(coinId);
            if (coin == null || coin.getStatus() != (byte)1){
                continue;
            }
            //设置币的信息
            accountVo.setCoinName(coin.getName());
            accountVo.setCoinImgUrl(coin.getImg());
            accountVo.setCoinType(coin.getType());
            accountVo.setWithdrawFlag(coin.getWithdrawFlag());
            accountVo.setRechargeFlag(coin.getRechargeFlag());
            accountVo.setFeeRate(BigDecimal.valueOf(coin.getRate()));
            accountVo.setMinFeeNum(coin.getMinFeeNum());
            assertList.add(accountVo);

            //计算总的账面余额
            BigDecimal volume = accountVo.getBalanceAmount().add(accountVo.getFreezeAmount());
            accountVo.setCarryingAmount(volume); //总的账面余额
            //将该币和我们系统统计币使用的基币转化
            BigDecimal currentPrice = getCurrentCoinPrice(coinId);
            BigDecimal total = volume.multiply(currentPrice);
            basicCoin = basicCoin.add(total);   //将该资产添加到总资产里面
        }
        userTotalAccountVo.setAmount(basicCoin.multiply(basicCoin2CnyRate).setScale(8, RoundingMode.HALF_UP)); //用户的总金额CNY(人民币)
        userTotalAccountVo.setAmountUs(basicCoin);   //用户的总金额(USDT平台币)
        userTotalAccountVo.setAssertList(assertList); //资产列表
        userTotalAccountVo.setAmountUsUnit("GCN"); //账户的金额单位(USDT平台币)
        return userTotalAccountVo;
    }


    /**
     * 获取当前币的价格
     * 使用我们的基币兑换该币的价格
     *
     * @param coinId
     * @return
     */
    private BigDecimal getCurrentCoinPrice(Long coinId) {
        //查询基础币是什么
        Config configBasicCoin = configService.getConfigByCode("PLATFORM_COIN_ID");
        if (configBasicCoin == null){
            throw new IllegalArgumentException("请配置基础币后使用");
        }
        Long basicCoinId = Long.valueOf(configBasicCoin.getValue());
        if (coinId.equals(basicCoinId)){ //该币就是基础币
            return BigDecimal.ONE;
        }
        //不等于，需要查询交易市场，使用基础币作为报价货币，使用报价货币的金额，来计算我们的当前币的价格
        MarketDto marketDto = marketServiceFeign.findCoinById(basicCoinId, coinId);
        if (marketDto!=null){ //存在交易对
            return marketDto.getOpenPrice();
        }else {
            //该交易对不存在
            return BigDecimal.ZERO; //TODO
        }
    }


    /**
     * 获取交易对资产
     *
     * @param userId
     * @param symbol
     * @return
     */
    @Override
    public SymbolAssetVo getSymbolAssert(Long userId, String symbol) {
        MarketDto marketDto = marketServiceFeign.findBySymbol(symbol);
        SymbolAssetVo symbolAssetVo = new SymbolAssetVo();
        //查询报价货币
        Long buyCoinId = marketDto.getBuyCoinId(); //报价货币id
        Account buyCoinAccount = getCoinAccount(buyCoinId, userId);
        symbolAssetVo.setBuyAmount(buyCoinAccount.getBalanceAmount());
        symbolAssetVo.setBuyLockAmount(buyCoinAccount.getFreezeAmount());
        //市场里面配置的值
        symbolAssetVo.setBuyFeeRate(marketDto.getFeeBuy());
        Coin buyCoin = coinService.getById(buyCoinId);
        symbolAssetVo.setBuyUnit(buyCoin.getName());
        //查询基础汇报
        Long sellCoinId = marketDto.getSellCoinId();
        Account sellCoinAccount = getCoinAccount(sellCoinId, userId);
        symbolAssetVo.setSellAmount(sellCoinAccount.getBalanceAmount());
        symbolAssetVo.setSellLockAmount(sellCoinAccount.getFreezeAmount());
        //市场里面配置的值
        symbolAssetVo.setSellFeeRate(marketDto.getFeeSell());
        Coin sellCoin = coinService.getById(sellCoinId);
        symbolAssetVo.setSellUnit(sellCoin.getName());
        return symbolAssetVo;
    }

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
    @Override
    public void transferBuyAmount(Long fromUserId, Long toUserId, Long coinId, BigDecimal amount, String businessType, Long orderId) {
        Account fromAccount = getCoinAccount(coinId, fromUserId);
        if (fromAccount == null){
            log.error("资金划转-资金账户异常，userId:{},coinId:{}",fromUserId,coinId);
            throw new IllegalArgumentException("资金账户异常");
        }else {
            Account toAccount = getCoinAccount(coinId,toUserId);
            if (toAccount == null){
                throw new IllegalArgumentException("资金账户异常");
            }else {
                boolean count1 = decreaseAmount(fromAccount,amount);
                boolean count2 = increaseAmount(toAccount,amount);
                if (count1 && count2){
                    List<AccountDetail> accountDetails = new ArrayList<>(2);
                    AccountDetail fromAccountDetail = new AccountDetail(fromUserId, coinId, fromAccount.getId(), toAccount.getId(), orderId, 2, businessType, amount, BigDecimal.ZERO, businessType);
                    AccountDetail toAccountDetail = new AccountDetail(toUserId, coinId, toAccount.getId(), fromAccount.getId(), orderId, 1, businessType, amount, BigDecimal.ZERO, businessType);
                    accountDetails.add(fromAccountDetail);
                    accountDetails.add(toAccountDetail);
                }else {
                    throw new RuntimeException("资金划转失败");
                }
            }
        }
    }

    private boolean increaseAmount(Account account, BigDecimal amount) {
        account.setBalanceAmount(account.getBalanceAmount().add(amount));
        return updateById(account);
    }

    private boolean decreaseAmount(Account account, BigDecimal amount) {
        account.setBalanceAmount(account.getBalanceAmount().add(amount));
        return updateById(account);
    }



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
    @Override
    public void transferSellAmount(Long fromUserId, Long toUserId, Long coinId, BigDecimal amount, String businessType, Long orderId) {
        Account fromAccount = getCoinAccount(coinId, fromUserId);
        if (fromAccount == null){
            log.error("资金划转-资金账户异常，userId:{},coinId:{}",fromUserId,coinId);
            throw new IllegalArgumentException("资金账户异常");
        }else {
            Account toAccount = getCoinAccount(coinId,toUserId);
            if (toAccount == null){
                throw new IllegalArgumentException("资金账户异常");
            }else {
                boolean count1 = decreaseAmount(toAccount,amount);
                boolean count2 = increaseAmount(fromAccount,amount);
                if (count1 && count2){
                    List<AccountDetail> accountDetails = new ArrayList<>(2);
                    AccountDetail fromAccountDetail = new AccountDetail(fromUserId, coinId, fromAccount.getId(), toAccount.getId(), orderId, 2, businessType, amount, BigDecimal.ZERO, businessType);
                    AccountDetail toAccountDetail = new AccountDetail(toUserId, coinId, toAccount.getId(), fromAccount.getId(), orderId, 1, businessType, amount, BigDecimal.ZERO, businessType);
                    accountDetails.add(fromAccountDetail);
                    accountDetails.add(toAccountDetail);
                }else {
                    throw new RuntimeException("资金划转失败");
                }
            }
        }
    }
}
