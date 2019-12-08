package com.doudou.plan.java.thread;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *<em>总结下：</em>
 * <p>
*   <ul>线程池的参数：</ul>
 *  <li>corePoolSize ： 核心线程数, 线程池刚创建的时候线程数量为0，
 *  当有任务提交时，如果无可用线程则创建新线程</li>
 *  <li>maximumPoolSize ： 最大线程数, 当队列满了之后允许创建的最大线程数</li>
 *  <li>keepAliveTime ：存活时间</li>
 *  <li>unit ： 时间单位</li>
 *  <li>workQueue ：工作队列，有三种可选值：
 *  <ol>
 *   <li>1. SynchronousQueue:不会保存提交的任务， 直接提交给线程池，newCachedThreadPool使用的就是
 *   此工作队列，最大线程设置的是Integer.MAX_VALUE，因此当有任务提交且没有空闲线程时会创建新线程。</li>
 *   <li>2. LinkedBlockingQueue:无参的构造方法<code>LinkedBlockingQueue()</code>会创建一个对长
 *   为Integer.MAX_VALUE的无界队列，此时maximumPoolSize不会产生效果，因为队列不会满，如果任务的生产
 *   速度远大于消费的速度那么队列里阻塞的任务会越来越多最终OOM.此时可能需要考虑增加大核心线程数，检查任务
 *   代码。</li>
 *   <li>3. ArrayBlockingQueue:有界队列，使用有界队列和有限的最大线程数可以防止资源耗尽。队列大小和最大池
 *   大小可以相互交换：大队列小池可以最小化cpu使用， 操作系统资源，和上下文切换的开销，但是吞吐量比较低。小队列
 *   大池子可以提高cpu的使用率，使得cpu尽可能忙碌，但是可能会遇到无法忍受的调度开销从而导致低的吞吐量。</li>
 *  </ol>
 *  </li>
 *  <li>threadFactory ： 线程工厂， 可选参数，默认是DefaultThreadFactory，
 *  线程工厂用于生产线程，默认情况下生产的线程都是在同一线程组，具有NORM_PRIORITY
 *  优先级， 非守护线程。生产的线程名都是：pool-poolNumber-thread-threadNumber,
 *  线程栈的大小为0. 线程生产失败或者返回null后Executor可以继续创建，但是无法提交任务。</li>
 *  <li>handler ： 拒绝策略。 当Executor关闭的时候，提交新任务来执行会被拒绝，当线程达到最大数，
 *  且工作队列已满时也会拒绝新任务提交。在上述两种情况下execute方法就会调用{@link
 *  RejectedExecutionHandler#rejectedExecution(Runnable, ThreadPoolExecutor)}
 *  方法（线程池设置的拒绝策略），框架提供了四个可选的拒绝策略：</li>
 *  <ol>
 *  <li>{@link ThreadPoolExecutor.AbortPolicy}, 此策略为默认拒绝策略，会抛弃任务，在拒绝时抛出一个
 *  {@link RejectedExecutionException}异常，此异常是一个未受检查的异常，调用者可以捕获此异常
 *  来自己编写处理代码。</li>
 *  <li>{@link ThreadPoolExecutor.CallerRunsPolicy}， 此策略是让调用execute的线程本身来
 *  执行这个任务，这是一个简单的反馈控制机制，能够降低提交任务的速率。如果线程池关闭了，
 *  任务会被丢弃</li>
 *  <li>{@link ThreadPoolExecutor.DiscardPolicy}，任务不能被执行时直接丢弃</li>
 *  <li>{@link ThreadPoolExecutor.DiscardOldestPolicy}，丢弃最老的任务即工作队列队头元素，
 *  并尝试再一次执行此任务，可能会再次失败也可能导致重复执行。</li>
 *
 *  </ol>
 *
 *  <ul>任务和任务提交：</ul>
 *  </p>
 *
 *
 *
 * @author doudou
 * @since 2019/12/6
 *
 *
 */
public class MyThreadPool extends ThreadPoolExecutor{

    private ThreadFactory namedThreadFactory = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r);
        }
    };

    private static ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("demo-pool-%d").build();


    ThreadPoolExecutor executor = new ThreadPoolExecutor(
            1,
            20,
            1L,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10),
            threadFactory,
            (r, executor) -> {
                System.err.println("拒绝策略执行");
        });




     ExecutorService executor2 = new ThreadPoolExecutor(
            10,
            20,
            1L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(10),
            namedThreadFactory,
             (r, executor) -> {
                 System.err.println("拒绝策略执行");
             }
         );

    public MyThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public MyThreadPool() {
        super(0, 0, 0, TimeUnit.DAYS, null);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
    }

    /**
     * 入口函数，自动生成
     */
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MyThreadPool myThreadPool = new MyThreadPool();
        myThreadPool.executor.allowCoreThreadTimeOut(true);
        for (int  i = 0; i < 20; i++){
            MyTask task = new MyTask(i);
            //executor2.submit(task);

            /*FutureTask<Object> futureTask = (FutureTask<Object>)
                    myThreadPool.executor.submit(task);*/
            myThreadPool.executor2.submit(task);

            //futureTask.get();
        }


    }


    static class MyTask implements Callable<Object> {

        private int i;

        public MyTask(int i) {
            this.i = i;
        }


        /**
         * Computes a result, or throws an exception if unable to do so.
         *
         * @return computed result
         *
         * @throws Exception if unable to compute a result
         */
        @Override
        public Object call() throws Exception {
            System.out.println("执行任务：" + i);
            Thread.sleep(300);
            return i;
        }
    }



    static class PausableThreadPoolExecutor extends ThreadPoolExecutor {
        private boolean isPaused;
        private ReentrantLock pauseLock = new ReentrantLock();
        private Condition unpaused = pauseLock.newCondition();


        public PausableThreadPoolExecutor(
                int corePoolSize,
                int maximumPoolSize,
                long keepAliveTime,
                TimeUnit unit,
                BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize,
                keepAliveTime, unit, workQueue);
        }


        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            pauseLock.lock();
            try {
                while (isPaused) {
                    unpaused.await();
                }
            } catch (InterruptedException ie) {
                t.interrupt();
            } finally {
                pauseLock.unlock();
            }
        }

        public void pause() {
            pauseLock.lock();
            try {
                isPaused = true;
            } finally {
                pauseLock.unlock();
            }
        }

        public void resume() {
            pauseLock.lock();
            try {
                isPaused = false;
                unpaused.signalAll();
            } finally {
                pauseLock.unlock();
            }
        }
    }
}



