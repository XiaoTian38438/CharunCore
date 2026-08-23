/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class GameTestAssertException extends GameTestException {
/*    */   protected final Component message;
/*    */   protected final int tick;
/*    */   
/*    */   public GameTestAssertException(Component paramComponent, int paramInt) {
/* 10 */     super(paramComponent.getString());
/* 11 */     this.message = paramComponent;
/* 12 */     this.tick = paramInt;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDescription() {
/* 17 */     return (Component)Component.translatable("test.error.tick", new Object[] { this.message, Integer.valueOf(this.tick) });
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 22 */     return getDescription().getString();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GameTestAssertException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */