/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.item.component.KineticWeapon;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class SpearUseGoal<T extends Monster>
/*     */   extends Goal
/*     */ {
/*     */   static final int MIN_REPOSITION_DISTANCE = 6;
/*     */   static final int MAX_REPOSITION_DISTANCE = 7;
/*     */   static final int MIN_COOLDOWN_DISTANCE = 9;
/*     */   static final int MAX_COOLDOWN_DISTANCE = 11;
/*  24 */   static final double MAX_FLEEING_TIME = reducedTickDelay(100);
/*     */   
/*     */   private final T mob;
/*     */   
/*     */   private SpearUseState state;
/*     */   double speedModifierWhenCharging;
/*     */   double speedModifierWhenRepositioning;
/*     */   float approachDistanceSq;
/*     */   float targetInRangeRadiusSq;
/*     */   
/*     */   public SpearUseGoal(T paramT, double paramDouble1, double paramDouble2, float paramFloat1, float paramFloat2) {
/*  35 */     this.mob = paramT;
/*  36 */     this.speedModifierWhenCharging = paramDouble1;
/*  37 */     this.speedModifierWhenRepositioning = paramDouble2;
/*  38 */     this.approachDistanceSq = paramFloat1 * paramFloat1;
/*  39 */     this.targetInRangeRadiusSq = paramFloat2 * paramFloat2;
/*  40 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  45 */     return (ableToAttack() && !this.mob.isUsingItem());
/*     */   }
/*     */   
/*     */   private boolean ableToAttack() {
/*  49 */     return (this.mob.getTarget() != null && this.mob.getMainHandItem().has(DataComponents.KINETIC_WEAPON));
/*     */   }
/*     */   
/*     */   private int getKineticWeaponUseDuration() {
/*  53 */     int i = ((Integer)Optional.<KineticWeapon>ofNullable((KineticWeapon)this.mob.getMainHandItem().get(DataComponents.KINETIC_WEAPON)).map(KineticWeapon::computeDamageUseDuration).orElse(Integer.valueOf(0))).intValue();
/*  54 */     return reducedTickDelay(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  59 */     return (this.state != null && !this.state.done && ableToAttack());
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  64 */     super.start();
/*  65 */     this.mob.setAggressive(true);
/*  66 */     this.state = new SpearUseState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  71 */     super.stop();
/*  72 */     this.mob.getNavigation().stop();
/*  73 */     this.mob.setAggressive(false);
/*  74 */     this.state = null;
/*  75 */     this.mob.stopUsingItem();
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  80 */     if (this.state == null) {
/*     */       return;
/*     */     }
/*     */     
/*  84 */     LivingEntity livingEntity = this.mob.getTarget();
/*  85 */     double d = this.mob.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
/*  86 */     Entity entity = this.mob.getRootVehicle();
/*  87 */     float f = 1.0F;
/*  88 */     if (entity instanceof Mob) { Mob mob = (Mob)entity;
/*  89 */       f = mob.chargeSpeedModifier(); }
/*     */     
/*  91 */     byte b = this.mob.isPassenger() ? 2 : 0;
/*     */     
/*  93 */     this.mob.lookAt((Entity)livingEntity, 30.0F, 30.0F);
/*  94 */     this.mob.getLookControl().setLookAt((Entity)livingEntity, 30.0F, 30.0F);
/*     */     
/*  96 */     if (this.state.notEngagedYet()) {
/*  97 */       if (d > this.approachDistanceSq) {
/*  98 */         this.mob.getNavigation().moveTo((Entity)livingEntity, f * this.speedModifierWhenRepositioning);
/*     */         return;
/*     */       } 
/* 101 */       this.state.startEngagement(getKineticWeaponUseDuration());
/* 102 */       this.mob.startUsingItem(InteractionHand.MAIN_HAND);
/*     */     } 
/*     */     
/* 105 */     if (this.state.tickAndCheckEngagement()) {
/* 106 */       this.mob.stopUsingItem();
/* 107 */       double d1 = Math.sqrt(d);
/* 108 */       this.state.awayPos = LandRandomPos.getPosAway((PathfinderMob)this.mob, Math.max(0.0D, (9 + b) - d1), Math.max(1.0D, (11 + b) - d1), 7, livingEntity.position());
/* 109 */       this.state.fleeingTime = 1;
/*     */     } 
/*     */     
/* 112 */     if (this.state.tickAndCheckFleeing()) {
/*     */       return;
/*     */     }
/*     */     
/* 116 */     if (this.state.awayPos != null) {
/* 117 */       this.mob.getNavigation().moveTo(this.state.awayPos.x, this.state.awayPos.y, this.state.awayPos.z, f * this.speedModifierWhenRepositioning);
/* 118 */       if (this.mob.getNavigation().isDone()) {
/* 119 */         if (this.state.fleeingTime > 0) {
/* 120 */           this.state.done = true;
/*     */           return;
/*     */         } 
/* 123 */         this.state.awayPos = null;
/*     */       } 
/*     */     } else {
/* 126 */       this.mob.getNavigation().moveTo((Entity)livingEntity, f * this.speedModifierWhenCharging);
/*     */       
/* 128 */       if (d < this.targetInRangeRadiusSq || this.mob.getNavigation().isDone()) {
/* 129 */         double d1 = Math.sqrt(d);
/* 130 */         this.state.awayPos = LandRandomPos.getPosAway((PathfinderMob)this.mob, (6 + b) - d1, (7 + b) - d1, 7, livingEntity.position());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SpearUseState
/*     */   {
/* 142 */     private int engageTime = -1;
/* 143 */     int fleeingTime = -1;
/*     */     Vec3 awayPos;
/*     */     boolean done = false;
/*     */     
/*     */     public boolean notEngagedYet() {
/* 148 */       return (this.engageTime < 0);
/*     */     }
/*     */     
/*     */     public void startEngagement(int param1Int) {
/* 152 */       this.engageTime = param1Int;
/*     */     }
/*     */     
/*     */     public boolean tickAndCheckEngagement() {
/* 156 */       if (this.engageTime > 0) {
/* 157 */         this.engageTime--;
/* 158 */         if (this.engageTime == 0) {
/* 159 */           return true;
/*     */         }
/*     */       } 
/* 162 */       return false;
/*     */     }
/*     */     
/*     */     public boolean tickAndCheckFleeing() {
/* 166 */       if (this.fleeingTime > 0) {
/* 167 */         this.fleeingTime++;
/* 168 */         if (this.fleeingTime > SpearUseGoal.MAX_FLEEING_TIME) {
/* 169 */           this.done = true;
/* 170 */           return true;
/*     */         } 
/*     */       } 
/* 173 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\SpearUseGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */