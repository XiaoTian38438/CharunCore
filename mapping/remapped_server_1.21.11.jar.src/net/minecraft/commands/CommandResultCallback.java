/*    */ package net.minecraft.commands;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface CommandResultCallback {
/*  5 */   public static final CommandResultCallback EMPTY = new CommandResultCallback()
/*    */     {
/*    */       public void onResult(boolean param1Boolean, int param1Int) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public String toString() {
/* 12 */         return "<empty>";
/*    */       }
/*    */     };
/*    */   
/*    */   void onResult(boolean paramBoolean, int paramInt);
/*    */   
/*    */   default void onSuccess(int paramInt) {
/* 19 */     onResult(true, paramInt);
/*    */   }
/*    */   
/*    */   default void onFailure() {
/* 23 */     onResult(false, 0);
/*    */   }
/*    */   
/*    */   static CommandResultCallback chain(CommandResultCallback paramCommandResultCallback1, CommandResultCallback paramCommandResultCallback2) {
/* 27 */     if (paramCommandResultCallback1 == EMPTY) {
/* 28 */       return paramCommandResultCallback2;
/*    */     }
/*    */     
/* 31 */     if (paramCommandResultCallback2 == EMPTY) {
/* 32 */       return paramCommandResultCallback1;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 37 */     return (paramBoolean, paramInt) -> {
/*    */         paramCommandResultCallback1.onResult(paramBoolean, paramInt);
/*    */         paramCommandResultCallback2.onResult(paramBoolean, paramInt);
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\CommandResultCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */