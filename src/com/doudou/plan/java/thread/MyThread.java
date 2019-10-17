package com.doudou.plan.java.thread;

import com.sun.istack.internal.NotNull;
import sun.nio.ch.Interruptible;

import java.security.AccessControlContext;

public class MyThread implements Runnable{

    //确保registerNatives是做的第一件事
    private static native void registerNatives();
    static {
        registerNatives();
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
}
