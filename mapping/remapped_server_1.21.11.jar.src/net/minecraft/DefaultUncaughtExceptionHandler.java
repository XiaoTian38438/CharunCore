/*    */ package net.minecraft;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class DefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
/*    */   private final Logger logger;
/*    */   
/*    */   public DefaultUncaughtExceptionHandler(Logger paramLogger) {
/*  9 */     this.logger = paramLogger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
/* 14 */     this.logger.error("Caught previously unhandled exception :", paramThrowable);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\DefaultUncaughtExceptionHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */