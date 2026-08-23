/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*    */ 
/*    */ public class RangedAttackGoal
/*    */   extends Goal {
/*    */   private final Mob mob;
/*    */   private final RangedAttackMob rangedAttackMob;
/*    */   private LivingEntity target;
/* 15 */   private int attackTime = -1;
/*    */   private final double speedModifier;
/*    */   private int seeTime;
/*    */   private final int attackIntervalMin;
/*    */   private final int attackIntervalMax;
/*    */   private final float attackRadius;
/*    */   private final float attackRadiusSqr;
/*    */   
/*    */   public RangedAttackGoal(RangedAttackMob paramRangedAttackMob, double paramDouble, int paramInt, float paramFloat) {
/* 24 */     this(paramRangedAttackMob, paramDouble, paramInt, paramInt, paramFloat);
/*    */   }
/*    */   
/*    */   public RangedAttackGoal(RangedAttackMob paramRangedAttackMob, double paramDouble, int paramInt1, int paramInt2, float paramFloat) {
/* 28 */     if (!(paramRangedAttackMob instanceof LivingEntity)) {
/* 29 */       throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
/*    */     }
/* 31 */     this.rangedAttackMob = paramRangedAttackMob;
/* 32 */     this.mob = (Mob)paramRangedAttackMob;
/* 33 */     this.speedModifier = paramDouble;
/* 34 */     this.attackIntervalMin = paramInt1;
/* 35 */     this.attackIntervalMax = paramInt2;
/* 36 */     this.attackRadius = paramFloat;
/* 37 */     this.attackRadiusSqr = paramFloat * paramFloat;
/* 38 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 43 */     LivingEntity livingEntity = this.mob.getTarget();
/* 44 */     if (livingEntity == null || !livingEntity.isAlive()) {
/* 45 */       return false;
/*    */     }
/* 47 */     this.target = livingEntity;
/* 48 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 53 */     return (canUse() || (this.target.isAlive() && !this.mob.getNavigation().isDone()));
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 58 */     this.target = null;
/* 59 */     this.seeTime = 0;
/* 60 */     this.attackTime = -1;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean requiresUpdateEveryTick() {
/* 65 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 70 */     double d = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
/* 71 */     boolean bool = this.mob.getSensing().hasLineOfSight((Entity)this.target);
/*    */     
/* 73 */     if (bool) {
/* 74 */       this.seeTime++;
/*    */     } else {
/* 76 */       this.seeTime = 0;
/*    */     } 
/*    */     
/* 79 */     if (d > this.attackRadiusSqr || this.seeTime < 5) {
/* 80 */       this.mob.getNavigation().moveTo((Entity)this.target, this.speedModifier);
/*    */     } else {
/* 82 */       this.mob.getNavigation().stop();
/*    */     } 
/*    */     
/* 85 */     this.mob.getLookControl().setLookAt((Entity)this.target, 30.0F, 30.0F);
/*    */     
/* 87 */     if (--this.attackTime == 0) {
/* 88 */       if (!bool) {
/*    */         return;
/*    */       }
/*    */       
/* 92 */       float f1 = (float)Math.sqrt(d) / this.attackRadius;
/* 93 */       float f2 = Mth.clamp(f1, 0.1F, 1.0F);
/*    */       
/* 95 */       this.rangedAttackMob.performRangedAttack(this.target, f2);
/* 96 */       this.attackTime = Mth.floor(f1 * (this.attackIntervalMax - this.attackIntervalMin) + this.attackIntervalMin);
/* 97 */     } else if (this.attackTime < 0) {
/* 98 */       this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d) / this.attackRadius, this.attackIntervalMin, this.attackIntervalMax));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\RangedAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */