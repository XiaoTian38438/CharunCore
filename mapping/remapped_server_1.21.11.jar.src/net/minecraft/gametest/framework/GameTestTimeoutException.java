/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class GameTestTimeoutException extends GameTestException {
/*    */   protected final Component message;
/*    */   
/*    */   public GameTestTimeoutException(Component paramComponent) {
/*  9 */     super(paramComponent.getString());
/* 10 */     this.message = paramComponent;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDescription() {
/* 15 */     return this.message;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GameTestTimeoutException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */