/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.parrot.ShoulderRidingEntity;
/*    */ 
/*    */ public class LandOnOwnersShoulderGoal extends Goal {
/*    */   private final ShoulderRidingEntity entity;
/*    */   private boolean isSittingOnShoulder;
/*    */   
/*    */   public LandOnOwnersShoulderGoal(ShoulderRidingEntity paramShoulderRidingEntity) {
/* 12 */     this.entity = paramShoulderRidingEntity;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 17 */     LivingEntity livingEntity = this.entity.getOwner(); if (livingEntity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
/* 18 */       boolean bool = (!serverPlayer.isSpectator() && !(serverPlayer.getAbilities()).flying && !serverPlayer.isInWater() && !serverPlayer.isInPowderSnow) ? true : false;
/* 19 */       return (!this.entity.isOrderedToSit() && bool && this.entity.canSitOnShoulder()); }
/*    */     
/* 21 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInterruptable() {
/* 26 */     return !this.isSittingOnShoulder;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 31 */     this.isSittingOnShoulder = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 36 */     if (this.isSittingOnShoulder || this.entity.isInSittingPose() || this.entity.isLeashed()) {
/*    */       return;
/*    */     }
/*    */     
/* 40 */     LivingEntity livingEntity = this.entity.getOwner(); if (livingEntity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
/* 41 */       if (this.entity.getBoundingBox().intersects(serverPlayer.getBoundingBox()))
/* 42 */         this.isSittingOnShoulder = this.entity.setEntityOnShoulder(serverPlayer);  }
/*    */   
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\LandOnOwnersShoulderGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */