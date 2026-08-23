/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ 
/*    */ class ExhaustedAttemptsException
/*    */   extends Throwable
/*    */ {
/*    */   public ExhaustedAttemptsException(int paramInt1, int paramInt2, GameTestInfo paramGameTestInfo) {
/*  8 */     super("Not enough successes: " + paramInt2 + " out of " + paramInt1 + " attempts. Required successes: " + paramGameTestInfo
/*    */         
/* 10 */         .requiredSuccesses() + ". max attempts: " + paramGameTestInfo.maxAttempts() + ".", paramGameTestInfo.getError());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\ExhaustedAttemptsException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */