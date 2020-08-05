package com.doudou.db;

/**
 *
 */
public class Mysql {
    //存储引擎

    /**
     * MySQL8.0中InnoDB是默认的存储引擎。
     * 支持事务模型，报错事务的提交，回滚，故障恢复；
     * 支持行级锁，oracle风格的一致性读操作，提高用户的并发和性能；
     * 支持基于主键的的优化查询，称之为聚集索引；
     * 存储限制：64TB
     */
    interface InnoDB {
        
    }
    
    //数据库索引，索引的数据结构，存储结构，如何和物理磁盘对应
    interface Index{
        
    }
    
    //数据库事务，事务特性，隔离级别，脏读，幻读，不可重复读，如何解决？
    
    //MVCC
    /**
     *
     * Acronym for “multiversion concurrency control”. 
     * This technique lets InnoDB transactions with 
     * certain isolation levels perform consistent read 
     * operations; that is, to query rows that are being 
     * updated by other transactions, and see the values 
     * from before those updates occurred. This is a powerful 
     * technique to increase concurrency, by allowing queries 
     * to proceed without waiting due to locks held by the 
     * other transactions.

     * This technique is not universal in the database world. 
     * Some other database products, and some other MySQL storage 
     * engines, do not support it.
     */
    
    //分库分表，分布式事务的解决方案，主备容灾
    
    //SQL语法，查看执行计划，SQL的调优。
    
    //数据库锁，乐观锁和悲观锁，以及实际使用的案例。
}
