/*    */ package net.minecraft.server;
/*    */ 
/*    */ public class TickTask implements Runnable {
/*    */   private final int tick;
/*    */   private final Runnable runnable;
/*    */   
/*    */   public TickTask(int paramInt, Runnable paramRunnable) {
/*  8 */     this.tick = paramInt;
/*  9 */     this.runnable = paramRunnable;
/*    */   }
/*    */   
/*    */   public int getTick() {
/* 13 */     return this.tick;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 18 */     this.runnable.run();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\TickTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */