package com.doudou.distribute;

/**
 * 分布式事务
 */
public class DistributeTransaction {
    //在说分布式事务之前首先得说下普通得事务，即本地事务

    /**
     * 本地事务
     */
    interface Transaction {
        //首先四个特性描绘了事务得架构, ACID模型
        /*
        * 1. atomicity，原子性
        * 2. consistency， 一致性
        * 3. isolation， 隔离性
        * 4. durability， 持久性
        * */

    }


    //讲完了本地事务就讲下分布式事务

    /**
     * 分布式事务
     */
    interface IDistributeTransaction {

    }
}
