/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentLinkedQueue;
/*    */ import java.util.concurrent.Executor;
/*    */ 
/*    */ public class ConsecutiveExecutor
/*    */   extends AbstractConsecutiveExecutor<Runnable> {
/*    */   public ConsecutiveExecutor(Executor paramExecutor, String paramString) {
/*  9 */     super(new StrictQueue.QueueStrictQueue(new ConcurrentLinkedQueue<>()), paramExecutor, paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public Runnable wrapRunnable(Runnable paramRunnable) {
/* 14 */     return paramRunnable;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\thread\ConsecutiveExecutor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */