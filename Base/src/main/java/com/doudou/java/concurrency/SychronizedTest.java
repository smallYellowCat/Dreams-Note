package com.doudou.java.concurrency;

import java.util.concurrent.CountDownLatch;

public class SychronizedTest {
    public static void main(String[] args) throws InterruptedException {
        Sychronized syncAndCondition = new Sychronized();

        //使用闭锁来实现并发， countdownlatch用来等待一组线程到某个特定的点一起执行
        //这个门打开后就是一直开着的
        CountDownLatch countDownLatch = new CountDownLatch(2);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                    System.out.println(System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //while (true)
                //syncAndCondition.sync1();
                syncAndCondition.testSpinLock();
            }
        });


        Thread t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    countDownLatch.await();
                    System.out.println(System.currentTimeMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //while (true)
                //syncAndCondition.sync2();
                syncAndCondition.testSpinLock();
            }
        });

        t1.setName("T-1");
        t2.setName("T-2");

        t1.setPriority(1);
        t2.setPriority(2);

        t1.start();
        countDownLatch.countDown();
        t2.start();
        countDownLatch.countDown();

        do {
            System.out.println("t2 = " + t2.getState());
            System.out.println("t1 = " + t1.getState());
            Thread.sleep(100);
        } while (!t2.getState().equals(Thread.State.TERMINATED));
    }
}
