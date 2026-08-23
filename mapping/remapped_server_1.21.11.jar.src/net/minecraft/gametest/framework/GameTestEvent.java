/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ 
/*    */ class GameTestEvent
/*    */ {
/*    */   public final Long expectedDelay;
/*    */   public final Runnable assertion;
/*    */   
/*    */   private GameTestEvent(Long paramLong, Runnable paramRunnable) {
/* 10 */     this.expectedDelay = paramLong;
/* 11 */     this.assertion = paramRunnable;
/*    */   }
/*    */   
/*    */   static GameTestEvent create(Runnable paramRunnable) {
/* 15 */     return new GameTestEvent(null, paramRunnable);
/*    */   }
/*    */   
/*    */   static GameTestEvent create(long paramLong, Runnable paramRunnable) {
/* 19 */     return new GameTestEvent(Long.valueOf(paramLong), paramRunnable);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GameTestEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */