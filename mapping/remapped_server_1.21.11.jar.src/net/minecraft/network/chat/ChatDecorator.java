/*   */ package net.minecraft.network.chat;
/*   */ 
/*   */ import net.minecraft.server.level.ServerPlayer;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface ChatDecorator {
/*   */   static {
/* 8 */     PLAIN = ((paramServerPlayer, paramComponent) -> paramComponent);
/*   */   }
/*   */   
/*   */   public static final ChatDecorator PLAIN;
/*   */   
/*   */   Component decorate(ServerPlayer paramServerPlayer, Component paramComponent);
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\network\chat\ChatDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */