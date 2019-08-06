package com.ludm.util.concurrency.locks;

import com.ludm.Util;
import sun.misc.Unsafe;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.concurrent.locks.LockSupport;

public abstract class AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer implements Serializable {

    private volatile int state;
    private transient volatile Node head;
    private transient volatile Node tail;

    private static final Unsafe unsafe = AbstractQueuedSynchronizer.getUnsafe();
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;

    private static final Unsafe getUnsafe() {
        Util.printLog();
        Field f = null;
        try {
            f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    static {
        try {
            Util.printLog();
            stateOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("next"));

        } catch (Exception ex) { throw new Error(ex); }
    }

    protected final int getState() {
        Util.printLog();
        return state;
    }

    protected final void setState(int state) {
        Util.printLog();
        this.state = state;
    }

    protected AbstractQueuedSynchronizer() {
        Util.printLog();
    }

    public final void acquire(int arg) {
        Util.printLog();
        if (!tryAcquire(arg) &&
                acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }

    static void selfInterrupt() {
        Util.printLog();
        Thread.currentThread().interrupt();
    }

    public final boolean hasQueuedPredecessors() {
        Util.printLog();
        Node tail = this.tail;
        Node head = this.head;
        Node node;
        return tail != head && ((node = head.next) != null || node.thread != Thread.currentThread());
    }

    protected boolean tryAcquire(int arg) {
        Util.printLog();
        throw new UnsupportedOperationException();
    }

    private Node addWaiter(Node mode) {
        Util.printLog();
        Node node = new Node(Thread.currentThread(), mode);
        // Try the fast path of enq; backup to full enq on failure
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        enq(node);
        return node;
    }

    private Node enq(final Node node) {
        Util.printLog();
        for (;;) {
            Node t = tail;
            if (t == null) { // Must initialize
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else {
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }

    final boolean acquireQueued(final Node node, int arg) {
        Util.printLog();
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
//            if (failed)
//                cancelAcquire(node);
        }
    }

    private final boolean parkAndCheckInterrupt() {
        Util.printLog();
        LockSupport.park(this);
        return Thread.interrupted();
    }

    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        Util.printLog();
        int waitStatus = pred.waitStatus;
        if(waitStatus == Node.SIGNAL) {

            return true;
        } else if (waitStatus > 0 ) {

            do {
                node.prev = pred = pred.prev;
            } while(pred.waitStatus > 0);
            pred.next = node;
        } else {

            compareAndSetWaitStatus(pred, waitStatus, Node.SIGNAL);
        }
        return false;
    }

    private final boolean compareAndSetHead(Node update) {
        Util.printLog();
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    private final boolean compareAndSetTail(Node expect, Node update) {
        Util.printLog();
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    private static final boolean compareAndSetWaitStatus(Node node, int expect, int update) {
        Util.printLog();
        return unsafe.compareAndSwapInt(node, waitStatusOffset, expect, update);
    }

    protected final boolean compareAndSetState(int expect, int update) {
        Util.printLog();
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    private void setHead(Node node) {
        Util.printLog();
        head = node;
        node.thread = null;
        node.prev = null;
    }

    /**
     *
     *
     *
     *
     *
     */
    static final class Node {

        static final Node EXCLUSIVE = null;

        static final int CANCELLED = 1;
        static final int SIGNAL = -1;

        volatile int waitStatus;
        volatile Node prev;
        volatile Node next;
        volatile Thread thread;
        Node nextWaiter;

        Node() {
            Util.printLog();
        }

        Node(Thread thread, Node mode) {
            Util.printLog();
            this.nextWaiter = mode;
            this.thread = thread;
        }

        /**
         *  返回上一个节点。头节点会抛异常
         * @return
         * @throws NullPointerException
         */
        final Node predecessor() throws NullPointerException {
            Util.printLog();
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }
    }
}
