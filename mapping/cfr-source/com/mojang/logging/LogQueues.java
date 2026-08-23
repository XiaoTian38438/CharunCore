/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package com.mojang.logging;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;

public class LogQueues {
    private static final Map<String, BlockingQueue<String>> QUEUES = new HashMap<String, BlockingQueue<String>>();
    private static final ReentrantReadWriteLock QUEUE_LOCK = new ReentrantReadWriteLock();

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static BlockingQueue<String> getOrCreateQueue(String string2) {
        BlockingQueue blockingQueue;
        try {
            QUEUE_LOCK.readLock().lock();
            blockingQueue = QUEUES.get(string2);
            if (blockingQueue != null) {
                BlockingQueue blockingQueue2 = blockingQueue;
                return blockingQueue2;
            }
        }
        finally {
            QUEUE_LOCK.readLock().unlock();
        }
        try {
            QUEUE_LOCK.writeLock().lock();
            blockingQueue = QUEUES.computeIfAbsent(string2, string -> new LinkedBlockingQueue());
            return blockingQueue;
        }
        finally {
            QUEUE_LOCK.writeLock().unlock();
        }
    }

    @Nullable
    public static String getNextLogEvent(String string) {
        QUEUE_LOCK.readLock().lock();
        BlockingQueue<String> blockingQueue = QUEUES.get(string);
        QUEUE_LOCK.readLock().unlock();
        if (blockingQueue != null) {
            try {
                return blockingQueue.take();
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        }
        return null;
    }
}

