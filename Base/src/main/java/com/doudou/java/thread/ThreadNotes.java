package com.doudou.java.thread;

import java.util.Random;

public class ThreadNotes {
    public static void main(String[] args) throws InterruptedException {
        MThread mThread = new MThread();
        mThread.start();
        //测试join
        System.out.println("计算资源紧张，只能给你三秒，10ms如果完成不了就不管了");
        //mThread.wait();
        //主线程等待子线程死亡
        mThread.join(10L);
        System.out.println(mThread.getState());
        //Thread.yield();
        //mThread.interrupt();
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
