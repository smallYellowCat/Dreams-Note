package com.doudou.java.thread;

import java.util.Random;

public class ThreadNotes {
    public static void main(String[] args) throws InterruptedException {
        MThread mThread = new MThread();
        mThread.start();
        //测试join
        System.out.println("计算资源紧张，只能给你三秒，10ms如果完成不了就不管了");
        //通知当前线程等待，知道调用notify被唤醒或者调用interrupt被中断
        mThread.wait();
        //主线程等待子线程死亡，这种情况适用于某个线程里面启用了一个子线程做一个其他的操作，
        //但是主线程又希望等待这个子线程完成之后在继续执行下面的步骤，那么就可以采用join的方式
        //来等待特定的时间， join（）默认时join（0），一直等待子线程死亡。
        mThread.join(10L);
        System.out.println(mThread.getState());

        //告诉操作系统的线程调度器，当前线程让出cpu的时间片，但是调度器也可能会忽略这个提示。
        Thread.yield();

        //中断线程，当线程由于调用wait或者join，或sleep处于阻塞状态时，中断标志位会被清除，并且抛出
        //一个中断异常，
        mThread.interrupt();
    }
}

class MThread extends Thread {
    public long sum = 0;
    @Override
    public void run() {
        System.out.println("这是一个耗时的计算任务，需要等待");

        long num = 0;
        int ran = new Random(100).nextInt();
        for (int i = 0; i < 100000;) {
            i++;
            for (int j = 0; j < 10000;) {
                j++;
                ran = new Random(10).nextInt();
                //System.out.println();
                if (j == 99999 || ran == 47)
                    num = i + j;
            }
        }
        System.out.println("计算任务完成！！！！");
        sum = num;
    }
}
