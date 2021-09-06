package com.kenji.match;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Kenji
 * @Date 2021/8/26 16:20
 * @Description
 */
public class MatchStrategyFactory {

    private static Map<MatchStrategy, MatchService> matchServiceMap = new HashMap<>();

    /**
     * 给策略工程里面添加一个交易的实现类型
     * @param matchStrategy
     * @param matchService
     */
    public static void addMatchStrategy(MatchStrategy matchStrategy,MatchService matchService){
        matchServiceMap.put(matchStrategy,matchService);
    }

    /**
     * 使用策略的名称获取具体的实现类
     * @param matchStrategy
     * @return
     */
    public static MatchService getMatchService(MatchStrategy matchStrategy){
        return matchServiceMap.get(matchStrategy);
    }
}
