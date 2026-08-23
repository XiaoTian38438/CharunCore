/*    */ package com.mojang.logging;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.BlockingQueue;
/*    */ import java.util.concurrent.LinkedBlockingQueue;
/*    */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ public class LogQueues {
/* 11 */   private static final Map<String, BlockingQueue<String>> QUEUES = new HashMap<>();
/* 12 */   private static final ReentrantReadWriteLock QUEUE_LOCK = new ReentrantReadWriteLock();
/*    */   
/*    */   public static BlockingQueue<String> getOrCreateQueue(String paramString) {
/*    */     try {
/* 16 */       QUEUE_LOCK.readLock().lock();
/* 17 */       BlockingQueue<String> blockingQueue = QUEUES.get(paramString);
/* 18 */       if (blockingQueue != null) {
/* 19 */         return blockingQueue;
/*    */       }
/*    */     } finally {
/* 22 */       QUEUE_LOCK.readLock().unlock();
/*    */     } 
/*    */     
/*    */     try {
/* 26 */       QUEUE_LOCK.writeLock().lock();
/* 27 */       return QUEUES.computeIfAbsent(paramString, paramString -> new LinkedBlockingQueue());
/*    */     } finally {
/* 29 */       QUEUE_LOCK.writeLock().unlock();
/*    */     } 
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public static String getNextLogEvent(String paramString) {
/* 35 */     QUEUE_LOCK.readLock().lock();
/* 36 */     BlockingQueue<String> blockingQueue = QUEUES.get(paramString);
/* 37 */     QUEUE_LOCK.readLock().unlock();
/*    */     
/* 39 */     if (blockingQueue != null) {
/*    */       try {
/* 41 */         return blockingQueue.take();
/* 42 */       } catch (InterruptedException interruptedException) {}
/*    */     }
/*    */ 
/*    */     
/* 46 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\logging\LogQueues.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */