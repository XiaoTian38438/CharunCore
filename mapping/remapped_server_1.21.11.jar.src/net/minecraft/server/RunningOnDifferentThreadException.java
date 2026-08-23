/*    */ package net.minecraft.server;
/*    */ 
/*    */ public final class RunningOnDifferentThreadException extends RuntimeException {
/*  4 */   public static final RunningOnDifferentThreadException RUNNING_ON_DIFFERENT_THREAD = new RunningOnDifferentThreadException();
/*    */   
/*    */   private RunningOnDifferentThreadException() {
/*  7 */     setStackTrace(new StackTraceElement[0]);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized Throwable fillInStackTrace() {
/* 12 */     setStackTrace(new StackTraceElement[0]);
/* 13 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\RunningOnDifferentThreadException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */