/*   */ package net.minecraft.gametest.framework;
/*   */ 
/*   */ import net.minecraft.network.chat.Component;
/*   */ 
/*   */ public abstract class GameTestException extends RuntimeException {
/*   */   public GameTestException(String paramString) {
/* 7 */     super(paramString);
/*   */   }
/*   */   
/*   */   public abstract Component getDescription();
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\gametest\framework\GameTestException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */