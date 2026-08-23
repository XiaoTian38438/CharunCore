/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import java.util.Queue;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class QueueStrictQueue
/*    */   implements StrictQueue<Runnable>
/*    */ {
/*    */   private final Queue<Runnable> queue;
/*    */   
/*    */   public QueueStrictQueue(Queue<Runnable> paramQueue) {
/* 23 */     this.queue = paramQueue;
/*    */   }
/*    */ 
/*    */   
/*    */   public Runnable pop() {
/* 28 */     return this.queue.poll();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean push(Runnable paramRunnable) {
/* 33 */     return this.queue.add(paramRunnable);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 38 */     return this.queue.isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 43 */     return this.queue.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\thread\StrictQueue$QueueStrictQueue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */