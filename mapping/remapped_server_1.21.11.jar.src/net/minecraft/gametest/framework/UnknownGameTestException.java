/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class UnknownGameTestException extends GameTestException {
/*    */   private final Throwable reason;
/*    */   
/*    */   public UnknownGameTestException(Throwable paramThrowable) {
/*  9 */     super(paramThrowable.getMessage());
/* 10 */     this.reason = paramThrowable;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDescription() {
/* 15 */     return (Component)Component.translatable("test.error.unknown", new Object[] { this.reason.getMessage() });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\UnknownGameTestException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */