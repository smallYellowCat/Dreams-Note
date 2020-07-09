package com.doudou.java.base;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * -----------扩容机制----------
 * 1. 何时扩容？
 * 不同于HashMap的扩容条件（插入后的容量大于下一次扩容的容量乘以负载因子的值会引发hashMap的扩容），
 * ConcurrentHashMap是在链表节点树化对时候进行扩容的，而且扩容的过程是多线程扩容。当链表的长度
 * 达到8的时候就开始将链表转为红黑树。
 *
 */
public class MyCurrentHashMap {

    /**
     *入口函数，自动生成
     * @author 豆豆
     * @date 2019/12/5 14:02a
    */
    public static void main(String[] args){
        ConcurrentMap<String, String> cmap = new ConcurrentHashMap<>();
    }


}
