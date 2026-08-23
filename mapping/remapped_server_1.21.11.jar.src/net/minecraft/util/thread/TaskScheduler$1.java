/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import java.util.concurrent.Executor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements TaskScheduler<Runnable>
/*    */ {
/*    */   public String name() {
/* 29 */     return name;
/*    */   }
/*    */ 
/*    */   
/*    */   public void schedule(Runnable paramRunnable) {
/* 34 */     executor.execute(paramRunnable);
/*    */   }
/*    */ 
/*    */   
/*    */   public Runnable wrapRunnable(Runnable paramRunnable) {
/* 39 */     return paramRunnable;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     return name;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\thread\TaskScheduler$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */