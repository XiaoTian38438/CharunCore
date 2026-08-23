/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ public interface PlayerRideableJumping extends PlayerRideable {
/*    */   void onPlayerJump(int paramInt);
/*    */   
/*    */   boolean canJump();
/*    */   
/*    */   void handleStartJump(int paramInt);
/*    */   
/*    */   void handleStopJump();
/*    */   
/*    */   default int getJumpCooldown() {
/* 13 */     return 0;
/*    */   }
/*    */   
/*    */   default float getPlayerJumpPendingScale(int paramInt) {
/* 17 */     return (paramInt >= 90) ? 1.0F : (0.4F + 0.4F * paramInt / 90.0F);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\PlayerRideableJumping.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */