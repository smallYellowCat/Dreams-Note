package com.doudou.java.concurrency;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * wait是Object级别的方法，调用wait之前必须获取到该对象的监视器资源，
 * 也就是说需要synchronized（obj）获取到锁。
 *
 * Condition是与lock对应的广义上的条件队列。
 *
 * 条件队列：使得一组线程能够通过某种方式来等待特定的条件变为正（比如文件是否存在）
 *
 * 条件队列中的元素是一个个正在等待相关条件的线程。
 *
 *
 * @author doudou
 */
public class LockNote {

    private final String s = "a";
    private final Integer i = 1;
    private final Integer n = 129;
    private final Object o = new Object();

    //测试锁对象

    //测试String不能作为锁对象
    public void syncStr() {
        synchronized (s) {
            try {
                System.out.println(System.currentTimeMillis());
                Thread.sleep(10 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //测试integer也不能作为锁对象, 当integer在-128~127之外时，
    // 会创建新对象而不是缓存在常量池中的值，此时可以作为锁对象
    //当然这个缓存的范围是可以通过虚拟机的参数来控制的。
    public void syncInte() {
        synchronized (n) {
            try {
                System.out.println(System.currentTimeMillis());
                Thread.sleep(10 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void syncObj() {
        synchronized (o) {
            try {
                System.out.println(System.currentTimeMillis());
                Thread.sleep(10 * 1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized void syncTask1() {
        int n = 0;
        System.out.println("当前持有锁的方法是：syncTask1");
        do {
            for (long i = 0; i < 1000 * 100; ) {
                i++;
            }
            n++;
        } while (n <= 1000*100);
        System.out.println("重入锁");
        syncTask3();
        //System.out.println("syncTask1 执行结束，没有通知wait状态的线程来获取锁！！");
        System.out.println("syncTask1 执行结束，通知wait状态的线程来获取锁！！");
        notify();
    }




    public synchronized void syncTask3() {
        System.out.println("syncTask3！！！");
    }


    public static void main(String[] args) throws InterruptedException {
        LockNote lockNote = new LockNote();
        Task2 task2 = new Task2();

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                task2.read();
            }
        });
        t2.start();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                task2.write();
            }
        });
        t1.start();
    }

}


//基于显示锁和condition条件队列实现的文件交替读写的方法
//当写方法持有锁时，会检查文件是否存在，如果存在就会调用
// await阻塞当前线程，释放锁，此时读方法获取到锁，读出文
// 件内容，调用signal唤醒阻塞的写线程。如果读线程先获取到
//锁，会检查文件是否存在，如果不存在，阻塞当前线程，等待写
// 线程写入文件并调用signal唤醒读线程。
class Task2 {

    private Lock lock = new ReentrantLock();

    Condition read = lock.newCondition();
    Condition write = lock.newCondition();

    public void write() {
        lock.lock();
        BufferedWriter writer = null;
        try {
            Thread.sleep(10L);
            Path path = Paths.get("D:\\dddd.txt");
            while (Files.isReadable(path)) {
                //如果文件存在，线程阻塞等待读线程删掉文件
                System.out.println("文件存在等待删除");
                write.await();
            }
            writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE);
            writer.write("hello！");
            writer.flush();
            System.out.println("创建文件成功");
            read.signal();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
        }
    }

    public void read() {
        lock.lock();
        Path path = null;
        BufferedReader reader = null;
        try {
            path = Paths.get("D:\\dddd.txt");
            while (!Files.isReadable(path) ) {
                System.out.println("文件不存在，调用await释放锁");
                read.await();
            }
            reader = Files.newBufferedReader(path);
            String s =reader.readLine();
            System.out.println(s);
            Files.delete(path);
            write.signal();
            //condition2.signal();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            lock.unlock();
        }
        System.out.println("执行结束");
    }

}


//AQS，显示锁的实现原理， RetreenLock和ReadLock， WriteLock

//synchronized，膨胀锁，锁升级， JMM
