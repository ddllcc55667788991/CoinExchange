package com.kenji.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kenji.domain.ExchangeTrade;
import com.kenji.domain.Market;
import com.kenji.domain.TurnoverOrder;
import com.kenji.feign.AccountServiceFeign;
import com.kenji.param.OrderParam;
import com.kenji.rocket.Source;
import com.kenji.service.MarketService;
import com.kenji.service.TurnoverOrderService;
import com.kenji.vo.TradeEntrustOrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kenji.domain.EntrustOrder;
import com.kenji.mapper.EntrustOrderMapper;
import com.kenji.service.EntrustOrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeTypeUtils;

/**
 * @Author Kenji
 * @Date 2021/8/24 22:23
 * @Description
 */
@Service
public class EntrustOrderServiceImpl extends ServiceImpl<EntrustOrderMapper, EntrustOrder> implements EntrustOrderService {


    @Autowired
    private TurnoverOrderService turnoverOrderService;

    @Autowired
    private MarketService marketService;

    @Autowired
    private AccountServiceFeign accountServiceFeign;

    @Autowired
    private Source source;

    /**
     * 分页查询委托记录
     *
     * @param page   分页数据
     * @param userId
     * @param type   买卖类型
     * @param symbol 交易对
     * @return
     */
    @Override
    public Page<EntrustOrder> findByPage(Page<EntrustOrder> page, Long userId, Byte type, String symbol) {
        return page(page, new LambdaQueryWrapper<EntrustOrder>()
                .eq(userId != null, EntrustOrder::getUserId, userId)
                .eq(type != null && type != 0, EntrustOrder::getType, type)
                .eq(!StringUtils.isEmpty(symbol), EntrustOrder::getSymbol, symbol)
                .orderByDesc(EntrustOrder::getCreated)
        );
    }

    /**
     * 查询历史委托单记录
     *
     * @param page
     * @param userId
     * @param symbol
     * @return
     */
    @Override
    public Page<TradeEntrustOrderVo> getHistoryEntrustOrder(Page<EntrustOrder> page, Long userId, String symbol) {
        Page<EntrustOrder> entrustOrderPage = super.page(page, new LambdaQueryWrapper<EntrustOrder>()
                .eq(EntrustOrder::getSymbol, symbol)
                .eq(EntrustOrder::getUserId, userId)
        );
        Page<TradeEntrustOrderVo> tradeEntrustOrderVoPage = new Page<>(page.getCurrent(), page.getSize());
        List<EntrustOrder> entrustOrders = entrustOrderPage.getRecords();
        if (CollectionUtils.isEmpty(entrustOrders)) {
            tradeEntrustOrderVoPage.setRecords(Collections.EMPTY_LIST);
        } else {
            List<TradeEntrustOrderVo> tradeEntrustOrderVos = entrustOrders2tradeEntrustOrderVos(entrustOrders);
            tradeEntrustOrderVoPage.setRecords(tradeEntrustOrderVos);
        }
        return tradeEntrustOrderVoPage;
    }

    /**
     * 查询未委托单记录
     *
     * @param page
     * @param userId
     * @param symbol
     * @return
     */
    @Override
    public Page<TradeEntrustOrderVo> getEntrustOrder(Page<EntrustOrder> page, Long userId, String symbol) {
        Page<EntrustOrder> entrustOrderPage = super.page(page, new LambdaQueryWrapper<EntrustOrder>()
                .eq(EntrustOrder::getSymbol, symbol)
                .eq(EntrustOrder::getUserId, userId)
                .eq(EntrustOrder::getStatus, 0)
        );
        Page<TradeEntrustOrderVo> tradeEntrustOrderVoPage = new Page<>(page.getCurrent(), page.getSize());
        List<EntrustOrder> entrustOrders = entrustOrderPage.getRecords();
        if (CollectionUtils.isEmpty(entrustOrders)) {
            tradeEntrustOrderVoPage.setRecords(Collections.EMPTY_LIST);
        } else {
            List<TradeEntrustOrderVo> tradeEntrustOrderVos = entrustOrders2tradeEntrustOrderVos(entrustOrders);
            tradeEntrustOrderVoPage.setRecords(tradeEntrustOrderVos);
        }
        return tradeEntrustOrderVoPage;
    }


    /**
     * 将委托单转化为TradeEntrustOrderVo
     *
     * @param entrustOrders
     * @return
     */
    private List<TradeEntrustOrderVo> entrustOrders2tradeEntrustOrderVos(List<EntrustOrder> entrustOrders) {
        List<TradeEntrustOrderVo> tradeEntrustOrderVos = new ArrayList<>(entrustOrders.size());
        for (EntrustOrder entrustOrder : entrustOrders) {
            tradeEntrustOrderVos.add(entrustOrder2tradeEntrustOrderVo(entrustOrder));
        }
        return tradeEntrustOrderVos;
    }

    private TradeEntrustOrderVo entrustOrder2tradeEntrustOrderVo(EntrustOrder entrustOrder) {
        TradeEntrustOrderVo tradeEntrustOrderVo = new TradeEntrustOrderVo();
        tradeEntrustOrderVo.setOrderId(entrustOrder.getId());
        tradeEntrustOrderVo.setCreated(entrustOrder.getCreated());
        tradeEntrustOrderVo.setStatus(entrustOrder.getStatus().intValue());
        tradeEntrustOrderVo.setAmount(entrustOrder.getAmount());
        tradeEntrustOrderVo.setDealVolume(entrustOrder.getDeal());
        tradeEntrustOrderVo.setPrice(entrustOrder.getPrice());
        tradeEntrustOrderVo.setVolume(entrustOrder.getVolume());

        tradeEntrustOrderVo.setType(entrustOrder.getType().intValue()); //1-买入 2-卖出
        //查询已经成交的额度
        BigDecimal dealAmount = BigDecimal.ZERO;
        BigDecimal dealVolume = BigDecimal.ZERO;
        if (tradeEntrustOrderVo.getType() == 1) {
            List<TurnoverOrder> buyTurnoverOrder = turnoverOrderService.getBuyTurnoverOrder(entrustOrder.getId(), entrustOrder.getUserId());
            if (CollectionUtils.isEmpty(buyTurnoverOrder)) {
                for (TurnoverOrder turnoverOrder : buyTurnoverOrder) {
                    BigDecimal amount = turnoverOrder.getAmount();
                    dealAmount = dealAmount.add(amount);
                }
            }
        }
        if (tradeEntrustOrderVo.getType() == 2) {
            List<TurnoverOrder> sellTurnoverOrder = turnoverOrderService.getSellTurnoverOrder(entrustOrder.getId(), entrustOrder.getUserId());
            if (CollectionUtils.isEmpty(sellTurnoverOrder)) {
                for (TurnoverOrder turnoverOrder : sellTurnoverOrder) {
                    BigDecimal amount = turnoverOrder.getAmount();
                    dealAmount = dealAmount.add(amount);
                }
            }
        }
        //算买卖的额度
        tradeEntrustOrderVo.setDealAmount(dealAmount); //已经成交的总额（钱）
        tradeEntrustOrderVo.setDealVolume(entrustOrder.getDeal()); //成交的数量
        BigDecimal dealAvgPrice = BigDecimal.ZERO;
        if (dealAmount.compareTo(BigDecimal.ZERO) > 0) {
            dealAvgPrice = dealAmount.divide(entrustOrder.getDeal(), 8, RoundingMode.HALF_UP);
        }
        tradeEntrustOrderVo.setDealAvgPrice(dealAvgPrice); //成交的评价价格
        return tradeEntrustOrderVo;
    }

    /**
     * 委托单的下单操作
     *
     * @param userId
     * @param orderParam
     * @return
     */
    @Override
    @Transactional
    public Boolean createEntrustOrder(Long userId, OrderParam orderParam) {
        @NotBlank String symbol = orderParam.getSymbol();
        Market market = marketService.findBySymbol(symbol);
        if (market == null) {
            throw new IllegalArgumentException("您购买的交易对不存在");
        }
        BigDecimal price = orderParam.getPrice().setScale(market.getPriceScale(), RoundingMode.HALF_UP); //买入价
        BigDecimal volume = orderParam.getVolume().setScale(market.getNumScale(), RoundingMode.HALF_UP); //买入量

        //计算总成交额
        BigDecimal mum = price.multiply(volume);  //买入价 x 买入量=总成交额

        //委托量的校验
        @NotNull BigDecimal numMax = market.getNumMax();    //最大委托量
        @NotNull BigDecimal numMin = market.getNumMin();    //最小委托量
        if (volume.compareTo(numMax) > 0 || volume.compareTo(numMin) < 0) {
            throw new IllegalArgumentException("交易的数量不在范围");
        }
        //交易额的校验
        @NotNull BigDecimal tradeMin = market.getTradeMin();
        @NotNull BigDecimal tradeMax = market.getTradeMax();
        if (price.compareTo(tradeMax) > 0 || price.compareTo(tradeMin) < 0) {
            throw new IllegalArgumentException("交易额不在范围内");
        }

        //计算手续费
        BigDecimal fee = BigDecimal.ZERO;   //手续费
        BigDecimal feeRate = BigDecimal.ZERO;   //手续费率
        @NotNull Integer type = orderParam.getType();
        if (type == 1) {     //买入buy
            feeRate = market.getFeeBuy();   //买入手续费率
            fee = mum.multiply(market.getFeeBuy());  //买入手续费
        } else {     //卖出sell
            feeRate = market.getFeeSell();  //卖出手续费率
            fee = mum.multiply(market.getFeeSell());  //卖出手续费
        }
        EntrustOrder entrustOrder = new EntrustOrder();
        entrustOrder.setUserId(userId);
        entrustOrder.setAmount(mum);
        entrustOrder.setType(orderParam.getType().byteValue());
        entrustOrder.setPrice(price);
        entrustOrder.setVolume(volume);
        entrustOrder.setFee(fee);
        entrustOrder.setCreated(new Date());
        entrustOrder.setStatus((byte) 0);
        entrustOrder.setMarketId(market.getId());
        entrustOrder.setMarketName(market.getName());
        entrustOrder.setMarketType(market.getType());
        entrustOrder.setSymbol(market.getSymbol());
        entrustOrder.setFeeRate(feeRate);
        entrustOrder.setDeal(BigDecimal.ZERO);
        entrustOrder.setFreeze(entrustOrder.getAmount().add(entrustOrder.getFee())); //冻结金额 = 总交易额 x 手续费
        boolean save = save(entrustOrder);
        if (save) {
            @NotNull Long coinId = null;
            if (type == 1) { //购买操作
                coinId = market.getBuyCoinId();
            } else {
                coinId = market.getSellCoinId();
            }
            accountServiceFeign.lockUserAmount(userId, coinId, entrustOrder.getFreeze(), "type_create", entrustOrder.getId(), fee);
            MessageBuilder<EntrustOrder> entrustOrderMessageBuilder = MessageBuilder.withPayload(entrustOrder).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
            source.outputMessage().send(entrustOrderMessageBuilder.build());
        }
        return save;
    }

    /**
     * 更新委托单的数据
     *
     * @param exchangeTrade
     */
    @Override
    @Transactional
    public void doMatch(ExchangeTrade exchangeTrade) {
        String sellOrderId = exchangeTrade.getSellOrderId();
        String buyOrderId = exchangeTrade.getBuyOrderId();
        EntrustOrder sellOrder = getById(sellOrderId);
        EntrustOrder buyOrder = getById(buyOrderId);
        Long marketId = sellOrder.getMarketId();
        Market market = marketService.getById(marketId);

        // 1 新增成交记录
        addTurnoverOrderRecord(sellOrder, buyOrder, market, exchangeTrade);
        // 2 更新委托单
        updateEntrustOrder(sellOrder, buyOrder, exchangeTrade);
        // 3 余额的返回
        rollbackAccount(sellOrder, buyOrder, market, exchangeTrade);
    }


    /**
     * 添加成交记录
     *
     * @param sellOrder
     * @param buyOrder
     * @param market
     * @param exchangeTrade
     */
    private void addTurnoverOrderRecord(EntrustOrder sellOrder, EntrustOrder buyOrder, Market market, ExchangeTrade exchangeTrade) {
        Date date = new Date();
        //出售订单的成交记录
        TurnoverOrder sellTurnoverOrder = new TurnoverOrder();
        sellTurnoverOrder.setSellOrderId(sellOrder.getId());
        sellTurnoverOrder.setBuyCoinId(buyOrder.getId());
        sellTurnoverOrder.setBuyVolume(exchangeTrade.getAmount());
        sellTurnoverOrder.setAmount(exchangeTrade.getSellTurnover());
        sellTurnoverOrder.setBuyCoinId(market.getBuyCoinId());
        sellTurnoverOrder.setSellCoinId(market.getSellCoinId());
        sellTurnoverOrder.setCreated(date);
        sellTurnoverOrder.setBuyUserId(buyOrder.getUserId());
        sellTurnoverOrder.setSellUserId(sellOrder.getUserId());
        sellTurnoverOrder.setPrice(exchangeTrade.getPrice());
        sellTurnoverOrder.setBuyPrice(buyOrder.getPrice());
        sellTurnoverOrder.setTradeType((byte) 2);
        turnoverOrderService.save(sellTurnoverOrder);

        //买方订单的成交记录
        TurnoverOrder buyTurnoverOrder = new TurnoverOrder();
        buyTurnoverOrder.setSellOrderId(sellOrder.getId());
        buyTurnoverOrder.setBuyCoinId(buyOrder.getId());
        buyTurnoverOrder.setBuyVolume(exchangeTrade.getAmount());
        buyTurnoverOrder.setAmount(exchangeTrade.getBuyTurnover());
        buyTurnoverOrder.setBuyCoinId(market.getBuyCoinId());
        buyTurnoverOrder.setSellCoinId(market.getSellCoinId());
        buyTurnoverOrder.setCreated(date);
        buyTurnoverOrder.setBuyUserId(buyOrder.getUserId());
        buyTurnoverOrder.setSellUserId(sellOrder.getUserId());
        buyTurnoverOrder.setPrice(exchangeTrade.getPrice());
        buyTurnoverOrder.setBuyPrice(buyOrder.getPrice());
        buyTurnoverOrder.setTradeType((byte) 1);
        turnoverOrderService.save(buyTurnoverOrder);
    }

    /**
     * 更新委托单
     *
     * @param sellOrder
     * @param buyOrder
     * @param exchangeTrade
     */
    private void updateEntrustOrder(EntrustOrder sellOrder, EntrustOrder buyOrder, ExchangeTrade exchangeTrade) {
        sellOrder.setDeal(exchangeTrade.getAmount());
        buyOrder.setDeal(exchangeTrade.getAmount());
        BigDecimal volume = sellOrder.getVolume();  //总的数量
        BigDecimal amount = exchangeTrade.getAmount();  //本次成交的数量
        if (volume.compareTo(amount) == 0) {     //交易完成
            sellOrder.setStatus((byte) 1);
        }
        BigDecimal buyOrderVolume = buyOrder.getVolume();
        if (buyOrderVolume.compareTo(volume) == 0) {     //交易完成
            buyOrder.setStatus((byte) 1);
        }

        //更新委托单
        updateById(sellOrder);
        updateById(buyOrder);
    }

    /**
     * 余额的返回
     *
     * @param sellOrder
     * @param buyOrder
     * @param market
     * @param exchangeTrade
     */
    private void rollbackAccount(EntrustOrder sellOrder, EntrustOrder buyOrder, Market market, ExchangeTrade exchangeTrade) {
        //买单需要返回用户的余额，之前扣减的余额
        accountServiceFeign.transferBuyAmount(
                buyOrder.getUserId(),   //买单用户id
                sellOrder.getUserId(),  //卖单用户id
                market.getBuyCoinId(),  //买单支付的币种
                exchangeTrade.getBuyTurnover(),     //买单成交的金额
                "币币交易",
                Long.valueOf(exchangeTrade.getBuyOrderId())
        );

        //出售单需要
        accountServiceFeign.transferSellAmount(
                sellOrder.getUserId(),   //卖单用户id
                buyOrder.getUserId(),  //买单用户id
                market.getSellCoinId(), //卖单支付的币种
                exchangeTrade.getSellTurnover(),     //卖单成交的金额
                "币币交易",
                Long.valueOf(exchangeTrade.getSellOrderId())
        );
    }


    /**
     * 取消委托单
     *
     * @param entrustOrderId
     * @return
     */
    @Override
    public void cancelEntrustOrder(Long entrustOrderId) {
        //1 将该委托单从撮合引擎里面的委托账本里面移除
        EntrustOrder entrustOrder = new EntrustOrder();
        entrustOrder.setStatus((byte) 2);
        entrustOrder.setId(entrustOrderId);
        Message<EntrustOrder> message = MessageBuilder.withPayload(entrustOrder).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build();
        source.outputMessage().send(message);

    }

    /**
     * 取消订单
     *
     * @param orderId
     */
    @Override
    public void cancelEntrustOrderDb(String orderId) {
        //2 数据库操作
        if (!StringUtils.isEmpty(orderId)){
            EntrustOrder entrustOrder = getById(orderId);
            entrustOrder.setStatus((byte)2);
            updateById(entrustOrder);
        }
    }


}
