package com.ludm;

import com.ludm.util.concurrency.locks.ReentrantLock;

import java.util.concurrent.TimeUnit;

public class Test {

    static ReentrantLock reentrantLock = new ReentrantLock();
    public static void main(String[] args) throws InterruptedException {

        Util.printLog();
        Thread t1 = new Thread() {

            @Override
            public void run() {

                reentrantLock.lock();
                System.out.println(Thread.currentThread().getId() + " lock");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reentrantLock.unlock();
                System.out.println(Thread.currentThread().getId() + " unlock");
            }
        };

        Thread t2 = new Thread() {

            @Override
            public void run() {

                reentrantLock.lock();
                System.out.println(Thread.currentThread().getId() + " lock");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reentrantLock.unlock();
                System.out.println(Thread.currentThread().getId() + " unlock");
            }
        };

        Thread t3 = new Thread() {

            @Override
            public void run() {

                reentrantLock.lock();
                System.out.println(Thread.currentThread().getId() + " lock");
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                reentrantLock.unlock();
                System.out.println(Thread.currentThread().getId() + " unlock");
            }
        };
        t1.start();
        TimeUnit.SECONDS.sleep(1);
        t2.start();
        TimeUnit.SECONDS.sleep(1);
        t3.start();

    }
}
