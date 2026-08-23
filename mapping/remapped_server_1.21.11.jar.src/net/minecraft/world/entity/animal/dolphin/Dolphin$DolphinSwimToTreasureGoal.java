/*     */ package net.minecraft.world.entity.animal.dolphin;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.StructureTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DolphinSwimToTreasureGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Dolphin dolphin;
/*     */   private boolean stuck;
/*     */   
/*     */   DolphinSwimToTreasureGoal(Dolphin paramDolphin) {
/* 503 */     this.dolphin = paramDolphin;
/* 504 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterruptable() {
/* 509 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 514 */     return (this.dolphin.gotFish() && this.dolphin.getAirSupply() >= 100);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/* 519 */     BlockPos blockPos = this.dolphin.treasurePos;
/* 520 */     if (blockPos == null) {
/* 521 */       return false;
/*     */     }
/* 523 */     return (!BlockPos.containing(blockPos.getX(), this.dolphin.getY(), blockPos.getZ()).closerToCenterThan((Position)this.dolphin.position(), 4.0D) && !this.stuck && this.dolphin.getAirSupply() >= 100);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 528 */     if (!(this.dolphin.level() instanceof ServerLevel)) {
/*     */       return;
/*     */     }
/* 531 */     ServerLevel serverLevel = (ServerLevel)this.dolphin.level();
/* 532 */     this.stuck = false;
/* 533 */     this.dolphin.getNavigation().stop();
/*     */     
/* 535 */     BlockPos blockPos1 = this.dolphin.blockPosition();
/*     */     
/* 537 */     BlockPos blockPos2 = serverLevel.findNearestMapStructure(StructureTags.DOLPHIN_LOCATED, blockPos1, 50, false);
/* 538 */     if (blockPos2 != null) {
/* 539 */       this.dolphin.treasurePos = blockPos2;
/*     */     } else {
/*     */       
/* 542 */       this.stuck = true;
/*     */       
/*     */       return;
/*     */     } 
/* 546 */     serverLevel.broadcastEntityEvent((Entity)this.dolphin, (byte)38);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 551 */     BlockPos blockPos = this.dolphin.treasurePos;
/* 552 */     if (blockPos == null || BlockPos.containing(blockPos.getX(), this.dolphin.getY(), blockPos.getZ()).closerToCenterThan((Position)this.dolphin.position(), 4.0D) || this.stuck) {
/* 553 */       this.dolphin.setGotFish(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 559 */     if (this.dolphin.treasurePos == null) {
/*     */       return;
/*     */     }
/*     */     
/* 563 */     Level level = this.dolphin.level();
/*     */     
/* 565 */     if (this.dolphin.closeToNextPos() || this.dolphin.getNavigation().isDone()) {
/* 566 */       Vec3 vec31 = Vec3.atCenterOf((Vec3i)this.dolphin.treasurePos);
/* 567 */       Vec3 vec32 = DefaultRandomPos.getPosTowards((PathfinderMob)this.dolphin, 16, 1, vec31, 0.39269909262657166D);
/* 568 */       if (vec32 == null) {
/* 569 */         vec32 = DefaultRandomPos.getPosTowards((PathfinderMob)this.dolphin, 8, 4, vec31, 1.5707963705062866D);
/*     */       }
/*     */       
/* 572 */       if (vec32 != null) {
/* 573 */         BlockPos blockPos = BlockPos.containing((Position)vec32);
/* 574 */         if (!level.getFluidState(blockPos).is(FluidTags.WATER) || !level.getBlockState(blockPos).isPathfindable(PathComputationType.WATER)) {
/* 575 */           vec32 = DefaultRandomPos.getPosTowards((PathfinderMob)this.dolphin, 8, 5, vec31, 1.5707963705062866D);
/*     */         }
/*     */       } 
/*     */       
/* 579 */       if (vec32 == null) {
/* 580 */         this.stuck = true;
/*     */         
/*     */         return;
/*     */       } 
/* 584 */       this.dolphin.getLookControl().setLookAt(vec32.x, vec32.y, vec32.z, (this.dolphin.getMaxHeadYRot() + 20), this.dolphin.getMaxHeadXRot());
/* 585 */       this.dolphin.getNavigation().moveTo(vec32.x, vec32.y, vec32.z, 1.3D);
/*     */       
/* 587 */       if (level.random.nextInt(adjustedTickDelay(80)) == 0)
/* 588 */         level.broadcastEntityEvent((Entity)this.dolphin, (byte)38); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\dolphin\Dolphin$DolphinSwimToTreasureGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */