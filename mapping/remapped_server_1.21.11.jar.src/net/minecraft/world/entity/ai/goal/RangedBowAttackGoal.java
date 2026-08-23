/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.BowItem;
/*     */ import net.minecraft.world.item.Items;
/*     */ 
/*     */ public class RangedBowAttackGoal<T extends Monster & RangedAttackMob>
/*     */   extends Goal {
/*     */   private final T mob;
/*     */   private final double speedModifier;
/*     */   private int attackIntervalMin;
/*     */   private final float attackRadiusSqr;
/*  19 */   private int attackTime = -1;
/*     */   private int seeTime;
/*     */   private boolean strafingClockwise;
/*     */   private boolean strafingBackwards;
/*  23 */   private int strafingTime = -1;
/*     */   
/*     */   public RangedBowAttackGoal(T paramT, double paramDouble, int paramInt, float paramFloat) {
/*  26 */     this.mob = paramT;
/*  27 */     this.speedModifier = paramDouble;
/*  28 */     this.attackIntervalMin = paramInt;
/*  29 */     this.attackRadiusSqr = paramFloat * paramFloat;
/*  30 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */   
/*     */   public void setMinAttackInterval(int paramInt) {
/*  34 */     this.attackIntervalMin = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  39 */     if (this.mob.getTarget() == null) {
/*  40 */       return false;
/*     */     }
/*  42 */     return isHoldingBow();
/*     */   }
/*     */   
/*     */   protected boolean isHoldingBow() {
/*  46 */     return this.mob.isHolding(Items.BOW);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  51 */     return ((canUse() || !this.mob.getNavigation().isDone()) && isHoldingBow());
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  56 */     super.start();
/*     */     
/*  58 */     this.mob.setAggressive(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  63 */     super.stop();
/*     */     
/*  65 */     this.mob.setAggressive(false);
/*  66 */     this.seeTime = 0;
/*  67 */     this.attackTime = -1;
/*  68 */     this.mob.stopUsingItem();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/*  73 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  78 */     LivingEntity livingEntity = this.mob.getTarget();
/*  79 */     if (livingEntity == null) {
/*     */       return;
/*     */     }
/*  82 */     double d = this.mob.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
/*  83 */     boolean bool1 = this.mob.getSensing().hasLineOfSight((Entity)livingEntity);
/*  84 */     boolean bool2 = (this.seeTime > 0);
/*     */     
/*  86 */     if (bool1 != bool2) {
/*  87 */       this.seeTime = 0;
/*     */     }
/*     */     
/*  90 */     if (bool1) {
/*  91 */       this.seeTime++;
/*     */     } else {
/*  93 */       this.seeTime--;
/*     */     } 
/*     */     
/*  96 */     if (d > this.attackRadiusSqr || this.seeTime < 20) {
/*  97 */       this.mob.getNavigation().moveTo((Entity)livingEntity, this.speedModifier);
/*  98 */       this.strafingTime = -1;
/*     */     } else {
/* 100 */       this.mob.getNavigation().stop();
/* 101 */       this.strafingTime++;
/*     */     } 
/*     */     
/* 104 */     if (this.strafingTime >= 20) {
/* 105 */       if (this.mob.getRandom().nextFloat() < 0.3D) {
/* 106 */         this.strafingClockwise = !this.strafingClockwise;
/*     */       }
/* 108 */       if (this.mob.getRandom().nextFloat() < 0.3D) {
/* 109 */         this.strafingBackwards = !this.strafingBackwards;
/*     */       }
/* 111 */       this.strafingTime = 0;
/*     */     } 
/*     */     
/* 114 */     if (this.strafingTime > -1) {
/* 115 */       if (d > (this.attackRadiusSqr * 0.75F)) {
/* 116 */         this.strafingBackwards = false;
/* 117 */       } else if (d < (this.attackRadiusSqr * 0.25F)) {
/* 118 */         this.strafingBackwards = true;
/*     */       } 
/* 120 */       this.mob.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
/* 121 */       Entity entity = this.mob.getControlledVehicle(); if (entity instanceof Mob) { Mob mob = (Mob)entity;
/* 122 */         mob.lookAt((Entity)livingEntity, 30.0F, 30.0F); }
/*     */       
/* 124 */       this.mob.lookAt((Entity)livingEntity, 30.0F, 30.0F);
/*     */     } else {
/* 126 */       this.mob.getLookControl().setLookAt((Entity)livingEntity, 30.0F, 30.0F);
/*     */     } 
/*     */     
/* 129 */     if (this.mob.isUsingItem()) {
/* 130 */       if (!bool1 && this.seeTime < -60) {
/* 131 */         this.mob.stopUsingItem();
/* 132 */       } else if (bool1) {
/* 133 */         int i = this.mob.getTicksUsingItem();
/*     */         
/* 135 */         if (i >= 20) {
/* 136 */           this.mob.stopUsingItem();
/* 137 */           ((RangedAttackMob)this.mob).performRangedAttack(livingEntity, BowItem.getPowerForTime(i));
/* 138 */           this.attackTime = this.attackIntervalMin;
/*     */         } 
/*     */       } 
/* 141 */     } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
/* 142 */       this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand((LivingEntity)this.mob, Items.BOW));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\RangedBowAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */