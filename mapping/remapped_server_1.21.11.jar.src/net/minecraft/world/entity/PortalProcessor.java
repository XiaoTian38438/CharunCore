/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.block.Portal;
/*    */ import net.minecraft.world.level.portal.TeleportTransition;
/*    */ 
/*    */ public class PortalProcessor
/*    */ {
/*    */   private final Portal portal;
/*    */   private BlockPos entryPosition;
/*    */   private int portalTime;
/*    */   private boolean insidePortalThisTick;
/*    */   
/*    */   public PortalProcessor(Portal paramPortal, BlockPos paramBlockPos) {
/* 16 */     this.portal = paramPortal;
/* 17 */     this.entryPosition = paramBlockPos;
/* 18 */     this.insidePortalThisTick = true;
/*    */   }
/*    */   
/*    */   public boolean processPortalTeleportation(ServerLevel paramServerLevel, Entity paramEntity, boolean paramBoolean) {
/* 22 */     if (this.insidePortalThisTick) {
/* 23 */       this.insidePortalThisTick = false;
/* 24 */       return (paramBoolean && this.portalTime++ >= this.portal.getPortalTransitionTime(paramServerLevel, paramEntity));
/*    */     } 
/* 26 */     decayTick();
/* 27 */     return false;
/*    */   }
/*    */   
/*    */   public TeleportTransition getPortalDestination(ServerLevel paramServerLevel, Entity paramEntity) {
/* 31 */     return this.portal.getPortalDestination(paramServerLevel, paramEntity, this.entryPosition);
/*    */   }
/*    */   
/*    */   public Portal.Transition getPortalLocalTransition() {
/* 35 */     return this.portal.getLocalTransition();
/*    */   }
/*    */   
/*    */   private void decayTick() {
/* 39 */     this.portalTime = Math.max(this.portalTime - 4, 0);
/*    */   }
/*    */   
/*    */   public boolean hasExpired() {
/* 43 */     return (this.portalTime <= 0);
/*    */   }
/*    */   
/*    */   public BlockPos getEntryPosition() {
/* 47 */     return this.entryPosition;
/*    */   }
/*    */   
/*    */   public void updateEntryPosition(BlockPos paramBlockPos) {
/* 51 */     this.entryPosition = paramBlockPos;
/*    */   }
/*    */   
/*    */   public int getPortalTime() {
/* 55 */     return this.portalTime;
/*    */   }
/*    */   
/*    */   public boolean isInsidePortalThisTick() {
/* 59 */     return this.insidePortalThisTick;
/*    */   }
/*    */   
/*    */   public void setAsInsidePortalThisTick(boolean paramBoolean) {
/* 63 */     this.insidePortalThisTick = paramBoolean;
/*    */   }
/*    */   
/*    */   public boolean isSamePortal(Portal paramPortal) {
/* 67 */     return (this.portal == paramPortal);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\PortalProcessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */