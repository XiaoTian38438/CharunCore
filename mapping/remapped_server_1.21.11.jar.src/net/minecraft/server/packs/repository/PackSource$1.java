/*    */ package net.minecraft.server.packs.repository;
/*    */ 
/*    */ import java.util.function.UnaryOperator;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements PackSource
/*    */ {
/*    */   public Component decorate(Component paramComponent) {
/* 28 */     return decorator.apply(paramComponent);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldAddAutomatically() {
/* 33 */     return addAutomatically;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\repository\PackSource$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */