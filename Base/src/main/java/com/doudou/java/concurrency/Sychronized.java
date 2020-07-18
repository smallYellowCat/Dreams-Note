package com.doudou.java.concurrency;
//synchronized内置锁和object级别的内置条件队列


// synchronized是非公平锁，如果在锁释放的时候有
//新的线程来获取锁，那么锁会给新的线程，而不是去唤
//醒条件队列中的线程。 总结一句synchronized是虚拟机
//支持的可重入的非公平内置锁。

//锁膨胀和优化

//https://www.cnblogs.com/aspirant/p/11705068.html

//https://juejin.im/post/5c936018f265da60ec281bcb

//锁的信息记录在对象头的Mark Word中
//重量级锁是基于操作系统的mutex互斥原语实现的
//1.6之前内置锁是重量级锁，当一个线程持有锁时，其他
//想获取锁的线程都会被阻塞挂起，阻塞挂起会引起内外存
//的交换以及操作系统的用户态和内核态的切换。


//1.6的时候对锁做了优化，引入了偏向锁。轻量级锁和重量级锁
//刚开始线程处于无锁状态， 此时访问需要锁的方法或者代码时
//如果获取锁失败，首先会自旋等待（自适应自旋，根据线程以往
// 获取锁时的自旋次数来决定本次的自旋次数，这是一个不断反馈
// 的过程），自旋一段时间仍然无法获取锁时线程阻塞挂起，等到锁
//释放之后会重新请求锁。 假设刚开始没有竞争，锁对象为无锁状态，
// 采用CAS机制修改状态，标记为偏向锁，此时获取到的是偏向锁，偏向锁不会
// 主动释放，如果此时有另外的一个线程请求锁，首先看下线程ID是不是
//自己，如果是直接获的锁，这就是锁重入，如果不是，执行CAS操作来
//替换线程id，如果成功，则获取偏向锁，如果失败，检查当前持有
//偏向锁的线程是否存活，否，直接将对象头置为无锁状态，是，处于
//同步代码块之中，先挂起持有偏向锁的线程，升级为轻量级锁状态，
//再唤醒线程， 当持有

//锁消除

//锁粗化
public class Sychronized {

    private final Object obj = new Object();
    private int i = 0;

    //测试自适应的自旋锁的首次自旋次数，可能能测出可能不能
    public long testSpinLock() {
        long num = 0;
        synchronized (obj) {
            for (int i = 0; i < 100000; i++) {
                for (int j = 0; j < 100000;) {
                    if (j == 99999) {
                        num = i * j;
                        break;
                    }
                    j++;
                }
            }
        }
        return num;
    }


    public void sync1() {
        synchronized (obj) {
            while (i <= 0) {
                try {
                    System.out.println("i 小于 100 释放锁， 等待 i 变大, i = " + i);
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i--;
            obj.notify();
        }
    }


    public void sync2() {
        synchronized (obj) {
            while (i > 100) {
                try {
                    System.out.println("i 大于 100 释放锁， 等待 i 减小, i = " + i);
                    obj.wait();
                    System.out.println("挂起后的点");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i++;
            System.out.println(" i = " + i);
            obj.notify();
        }
    }

}