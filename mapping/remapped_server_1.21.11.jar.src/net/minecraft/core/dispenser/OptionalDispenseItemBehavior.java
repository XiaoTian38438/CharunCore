/*    */ package net.minecraft.core.dispenser;
/*    */ 
/*    */ public abstract class OptionalDispenseItemBehavior
/*    */   extends DefaultDispenseItemBehavior
/*    */ {
/*    */   private boolean success = true;
/*    */   
/*    */   public boolean isSuccess() {
/*  9 */     return this.success;
/*    */   }
/*    */   
/*    */   public void setSuccess(boolean paramBoolean) {
/* 13 */     this.success = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playSound(BlockSource paramBlockSource) {
/* 18 */     paramBlockSource.level().levelEvent(isSuccess() ? 1000 : 1001, paramBlockSource.pos(), 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\dispenser\OptionalDispenseItemBehavior.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */