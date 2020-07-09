package com.doudou.plan.java.concurrent;

import java.util.concurrent.Executor;

public class A {
    private Executor executor;

    /**
     *入口函数，自动生成
     * @author 豆豆
     * @date 2019/12/3 11:54a
    */
    public static void main(String[] args){
        try {
            throw new OutOfMemoryError("内存溢出！");
        }catch (OutOfMemoryError error){
            System.out.println("....");
        }
    }
}
