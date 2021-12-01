package com.doudou.ds;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class APIGateWay {
    private static Map<String, Integer> statistic = new ConcurrentHashMap<>();

    public static void main(String[] args) {

    }

    private static void  invokeCountAdd(String apiName) {

    }

    private static void initApiCount(String apiName, Integer num){
        statistic.put(apiName, num);
    }

    private static boolean judgeCanAccess(String apiName){
        return false;
    }
}
