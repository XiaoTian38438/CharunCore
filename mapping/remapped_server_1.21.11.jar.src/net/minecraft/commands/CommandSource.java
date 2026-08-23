/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public interface CommandSource {
/*  6 */   public static final CommandSource NULL = new CommandSource()
/*    */     {
/*    */       public void sendSystemMessage(Component param1Component) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public boolean acceptsSuccess() {
/* 13 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean acceptsFailure() {
/* 18 */         return false;
/*    */       }
/*    */ 
/*    */       
/*    */       public boolean shouldInformAdmins() {
/* 23 */         return false;
/*    */       }
/*    */     };
/*    */   
/*    */   void sendSystemMessage(Component paramComponent);
/*    */   
/*    */   boolean acceptsSuccess();
/*    */   
/*    */   boolean acceptsFailure();
/*    */   
/*    */   boolean shouldInformAdmins();
/*    */   
/*    */   default boolean alwaysAccepts() {
/* 36 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\CommandSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */