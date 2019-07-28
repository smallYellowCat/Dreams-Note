package com.doudou.queue;
/**
*队列
*作者：豆豆
*时间:2017/10/13
*/
public class Queue {
    private Object[] data = null;
    private int maxSize; //队列的最大容量
    private int front; //队列头
    private int end; //队列尾

    public Queue(int maxSize){
        this.maxSize = maxSize + 1;
        this.data = new Object[maxSize];
        front = end = 0;
    }

    //入队
    public boolean insert(Object obj){
        if (end == maxSize){
            System.out.println("队满");
            return false;
        }
        data[end] = obj;
        end++;
        return true;
    }

    //出队
    public Object deQueue(){
        Object object = null;

        if (front == end){
            System.out.println("队空！");
        }else {
        object = data[front];
        front++;
        }

        return object;
    }
}
