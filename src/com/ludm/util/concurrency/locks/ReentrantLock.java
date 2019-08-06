package com.ludm.util.concurrency.locks;

import com.ludm.Util;

import java.io.Serializable;

public class ReentrantLock implements Lock, Serializable {

    private final Sync sync;

    public ReentrantLock() {
        Util.printLog();
        this.sync = new FairSync();
    }

    @Override
    public void lock() {
        Util.printLog();
        sync.lock();
    }

    @Override
    public void unlock() {

    }

    abstract static class Sync extends AbstractQueuedSynchronizer {

        abstract void lock();
    }

    static final class FairSync extends Sync {

        @Override
        void lock() {
            Util.printLog();
            acquire(1);
        }

        protected final boolean tryAcquire(int acquires) {
            Util.printLog();
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (!hasQueuedPredecessors() &&
                        compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    }
}
