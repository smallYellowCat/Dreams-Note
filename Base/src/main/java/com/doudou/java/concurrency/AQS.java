package com.doudou.java.concurrency;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.*;

//AQS，显式锁的实现原理

/**
 * 通过类的继承结构可以一窥AQS的奥秘，首先AQS本身向上继承了AbstractOwnableSynchronizer，
 * 这个父类没有什么特殊的地方，就一个属性，用来标识当前独占锁的线程是哪一个线程。
 * 向下看就比较多了，首先是Sync，在JDK的并发包底下有四个Sync，其次是ThreadPoolExecutor
 * 中的Worker。
 *
 * CountDownLatch就是借助AQS的state和xxx来实现的，首先将state设置为构造参数的值，每次执行
 * countDown都对对这个状态做减一操作。
 *
 * 排他锁：排他锁是独占锁，state是用来实现可重入的功能的，检测到请求锁的线程当前正持有该锁，
 * 那么对state做+1操作，释放锁的时候做减一操作，state为0的时候代表锁没有被占用，如果请求锁
 * 的线程发现有别的线程持有该锁那么会进行双向链表的插入操作，然后通过LockSupport的park来中断
 * 当前线程，让当前线程在该lock上等待。排他锁有两种实现一种是公平锁，一种是非公平锁，默认无参的
 * 构造函数创建是非公平锁，非公平的意思假设当前线程释放了锁，但是还没有unpark双向队列上的头结点
 * 对应的线程，那么此时如果有新的线程来请求锁就会获取到锁，这就是非公平的原因。
 *
 * 中断之后下次从哪里开始执行。
 *
 * 共享锁：读读共享，读写互斥，写写互斥；
 * 读写锁是通过AQS的state来实现的，高16位用来实现读锁，低16位实现写锁，如果当前持有锁的线程是读锁的时候每次有
 * 请求读锁的线程，那么state加65536，当读锁的线程数到达一定数量的时候也是需要条件队列的，新的线程
 * 以共享NODE的形势行程双端队列。如果持有锁的线程的为读锁，那么请求写 的锁的线程也会被阻塞。
 * 如果持有锁的线程是写锁，那么后续的读锁和写锁请求都会被阻塞。因为写锁是独占锁。
 *
 * 信号量Semaphore
 */
public class AQS {
    //底层的原理决定了上层的高度
    //学习就应该学习这种真理，三生
    //万物，万物是学不完的，要学会这个三

    public static void main(String[] args) throws InterruptedException {
        AbstractQueuedSynchronizer aqs;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        Lock lock = new ReentrantLock();
        //lock.lock();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                lock.lock();
                long i = 0;
                while (true) {
                    System.out.println("a");
                    if (i == Long.MAX_VALUE)
                        break;
                    i++;
                }
                lock.unlock();
            }
        };


        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                lock.lock();
                long i = 0;
                while (true) {
                    System.out.println("b");
                    if (i == Long.MAX_VALUE)
                        break;
                    i++;
                }
                lock.unlock();
            }
        };

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                lock.lock();
                long i = 0;
                while (true) {
                    System.out.println("b");
                    if (i == Long.MAX_VALUE)
                        break;
                    i++;
                }
                lock.unlock();
            }
        };


        Runnable runnable3 = new Runnable() {
            @Override
            public void run() {
                lock.lock();
            }
        };

        Thread t = new Thread(runnable);
        Thread t1 = new Thread(runnable1);
        Thread t2 = new Thread(runnable2);
        Thread t3 = new Thread(runnable3);
        t.setName("DD-1");
        t1.setName("DD-2");
        t2.setName("DD-3");
        t3.setName("DD-4");
        t.start();
        Thread.sleep(1000);
        t1.start();
        Thread.sleep(1000);
        t2.start();
        Thread.sleep(1000);
        //t3.start();




        //lock.unlock();



        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readWriteLock.readLock().lock();
        readWriteLock.readLock().unlock();
        readWriteLock.writeLock().lock();
        readWriteLock.writeLock().unlock();
    }

}
