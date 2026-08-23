/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class FunctionInstantiationException extends Exception {
/*    */   private final Component messageComponent;
/*    */   
/*    */   public FunctionInstantiationException(Component paramComponent) {
/*  9 */     super(paramComponent.getString());
/* 10 */     this.messageComponent = paramComponent;
/*    */   }
/*    */   
/*    */   public Component messageComponent() {
/* 14 */     return this.messageComponent;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\FunctionInstantiationException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */