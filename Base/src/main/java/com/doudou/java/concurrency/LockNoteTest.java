package com.doudou.java.concurrency;

public class LockNoteTest {
    public static void main(String[] args) {
        LockNote lockNote1 = new LockNote();
        LockNote lockNote2 = new LockNote();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                //lockNote1.syncStr();
                lockNote1.syncInte();
                //lockNote1.syncObj();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //lockNote2.syncStr();
                lockNote2.syncInte();
                //lockNote2.syncObj();
            }
        });

        t1.start();
        t2.start();
    }
}
