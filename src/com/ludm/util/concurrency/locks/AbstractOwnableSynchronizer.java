package com.ludm.util.concurrency.locks;

import com.ludm.Util;

import java.io.Serializable;

public abstract class AbstractOwnableSynchronizer implements Serializable {

    protected AbstractOwnableSynchronizer() {
        Util.printLog();
    }

    private transient Thread exclusiveOwnerThread;

    protected final void setExclusiveOwnerThread(Thread exclusiveOwnerThread) {
        Util.printLog();
        this.exclusiveOwnerThread = exclusiveOwnerThread;
    }

    protected final Thread getExclusiveOwnerThread() {
        Util.printLog();
        return exclusiveOwnerThread;
    }
}
