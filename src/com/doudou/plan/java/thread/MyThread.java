package com.doudou.plan.java.thread;

import com.sun.istack.internal.NotNull;
import sun.nio.ch.Interruptible;

import java.security.AccessControlContext;

public class MyThread implements Runnable{

    //确保registerNatives是做的第一件事
    //private static native void registerNatives();
    static {
        //registerNatives();
    }

    private volatile String name;
    private int priority;
    private Thread threadQ;
    private long eetop;

    /**是否单步执行此线程*/
    private boolean single_step;

    /**此线程是否为守护线程 daemon thread*/
    private boolean daemon = false;

    /**JVM state*/
    private boolean stillborn = false;

    /**What will be run*/
    private Runnable target;

    /**线程组*/
    private ThreadGroup group;

    /**此线程的上下文类加载器*/
    private ClassLoader contextClassLoader;

    /**继承了此线程的AccessControlContext*/
    private AccessControlContext inheritedAccessControlContext;

    /**用于自动编号匿名线程*/
    private static int threadInitNumber;
    private static synchronized int nextThreadNum(){return threadInitNumber++;}

    MyThreadLocal.ThreadLocalMap threadLocals = null;

    MyThreadLocal.ThreadLocalMap inheritableThreadLocals = null;

    private long statckSize;

    private long nativeParkEventPointer;

    private long tid;

    private static long threadSeqNumber;

    private volatile int threadStatus = 0;

    private static synchronized long nextThreadID(){return ++threadSeqNumber;}

    volatile Object parkBlocker;

    private volatile Interruptible blocker;
    private final Object blockerLock = new Object();

    void blockedOn(Interruptible b){
        synchronized (blockerLock){
            blocker = b;
        }
    }

    public final static int MIN_PRIORITY = 1;

    public final static int NORM_PRIORITY = 5;

    public final static int MAX_PRIORITY = 10;

    public static native MyThread currentThread();




    @Override
    public void run() {

    }


    /**
     *入口函数，自动生成
     * @author 豆豆
     * @date 2019/11/29 15:53a
    */
    public static void main(String[] args) throws InterruptedException {

        //join方法测试， join阻塞的是当前调用join方法的线程，当前被join阻塞的线程如果
        //被任何一个线程中断都会清除中断标志
        Thread1 t1 = new Thread1();
        Thread2 t2 = new Thread2(t1);
        Thread3 t3 = new Thread3(t2);
        t1.start();
        t2.start();
        t3.start();
        //t1.join(100);
    }


    /**
     *
     */
    static class Thread1 extends Thread{

        public Thread1(){
        }

        @Override
        public void run() {
            super.run();
            int i = 0;
            do {
                i++;
                if (Thread.interrupted()){
                    break;
                }
                System.out.println(i);

            }while (i < 1000);
        }
    }


    /**
     *
     */
    static class Thread2 extends Thread{

        private Thread t;

        public Thread2(Thread t){
            this.t = t;
        }

        @Override
        public void run() {
            super.run();
            try {
                System.out.println("===============开始join===============");
                t.join(100);
                System.out.println("==============结束join====================");
            } catch (InterruptedException e) {
                e.printStackTrace();
                boolean flag = t.isInterrupted();
                System.out.println("");
            }
            t.interrupt();
        }
    }


    static class Thread3 extends Thread{

        private Thread t;

        public Thread3(Thread t){
            this.t = t;
        }

        @Override
        public void run() {
            super.run();
            try {
                Thread.sleep(1);
                System.out.println("==========开始执行中断===================");
                t.interrupt();
                System.out.println("==========结束执行中断===================");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            t.interrupt();
        }
    }
}
