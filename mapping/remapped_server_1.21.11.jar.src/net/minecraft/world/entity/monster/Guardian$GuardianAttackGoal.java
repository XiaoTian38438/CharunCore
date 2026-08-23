/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class GuardianAttackGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Guardian guardian;
/*     */   private int attackTime;
/*     */   private final boolean elder;
/*     */   
/*     */   public GuardianAttackGoal(Guardian paramGuardian) {
/* 371 */     this.guardian = paramGuardian;
/*     */ 
/*     */     
/* 374 */     this.elder = paramGuardian instanceof ElderGuardian;
/*     */     
/* 376 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/* 381 */     LivingEntity livingEntity = this.guardian.getTarget();
/* 382 */     return (livingEntity != null && livingEntity.isAlive());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/* 387 */     return (super.canContinueToUse() && (this.elder || (this.guardian.getTarget() != null && this.guardian.distanceToSqr((Entity)this.guardian.getTarget()) > 9.0D)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 392 */     this.attackTime = -10;
/* 393 */     this.guardian.getNavigation().stop();
/* 394 */     LivingEntity livingEntity = this.guardian.getTarget();
/* 395 */     if (livingEntity != null) {
/* 396 */       this.guardian.getLookControl().setLookAt((Entity)livingEntity, 90.0F, 90.0F);
/*     */     }
/*     */ 
/*     */     
/* 400 */     this.guardian.needsSync = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/* 405 */     this.guardian.setActiveAttackTarget(0);
/* 406 */     this.guardian.setTarget(null);
/*     */     
/* 408 */     this.guardian.randomStrollGoal.trigger();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/* 413 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 418 */     LivingEntity livingEntity = this.guardian.getTarget();
/* 419 */     if (livingEntity == null) {
/*     */       return;
/*     */     }
/*     */     
/* 423 */     this.guardian.getNavigation().stop();
/* 424 */     this.guardian.getLookControl().setLookAt((Entity)livingEntity, 90.0F, 90.0F);
/*     */     
/* 426 */     if (!this.guardian.hasLineOfSight((Entity)livingEntity)) {
/* 427 */       this.guardian.setTarget(null);
/*     */       
/*     */       return;
/*     */     } 
/* 431 */     this.attackTime++;
/* 432 */     if (this.attackTime == 0) {
/*     */       
/* 434 */       this.guardian.setActiveAttackTarget(livingEntity.getId());
/* 435 */       if (!this.guardian.isSilent()) {
/* 436 */         this.guardian.level().broadcastEntityEvent((Entity)this.guardian, (byte)21);
/*     */       }
/* 438 */     } else if (this.attackTime >= this.guardian.getAttackDuration()) {
/* 439 */       float f = 1.0F;
/* 440 */       if (this.guardian.level().getDifficulty() == Difficulty.HARD) {
/* 441 */         f += 2.0F;
/*     */       }
/* 443 */       if (this.elder) {
/* 444 */         f += 2.0F;
/*     */       }
/* 446 */       ServerLevel serverLevel = getServerLevel((Entity)this.guardian);
/* 447 */       livingEntity.hurtServer(serverLevel, this.guardian.damageSources().indirectMagic((Entity)this.guardian, (Entity)this.guardian), f);
/* 448 */       this.guardian.doHurtTarget(serverLevel, (Entity)livingEntity);
/* 449 */       this.guardian.setTarget(null);
/*     */     } 
/*     */     
/* 452 */     super.tick();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Guardian$GuardianAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */