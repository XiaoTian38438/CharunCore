/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ public abstract class ReentrantBlockableEventLoop<R extends Runnable> extends BlockableEventLoop<R> {
/*    */   private int reentrantCount;
/*    */   
/*    */   public ReentrantBlockableEventLoop(String paramString) {
/*  7 */     super(paramString);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean scheduleExecutables() {
/* 12 */     return (runningTask() || super.scheduleExecutables());
/*    */   }
/*    */   
/*    */   protected boolean runningTask() {
/* 16 */     return (this.reentrantCount != 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void doRunTask(R paramR) {
/* 21 */     this.reentrantCount++;
/*    */     try {
/* 23 */       super.doRunTask(paramR);
/*    */     } finally {
/* 25 */       this.reentrantCount--;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\thread\ReentrantBlockableEventLoop.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */