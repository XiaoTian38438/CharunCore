/*    */ package net.minecraft.world.entity.ai.targeting;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Difficulty;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class TargetingConditions {
/* 10 */   public static final TargetingConditions DEFAULT = forCombat();
/*    */   
/*    */   private static final double MIN_VISIBILITY_DISTANCE_FOR_INVISIBLE_TARGET = 2.0D;
/*    */   private final boolean isCombat;
/* 14 */   private double range = -1.0D;
/*    */   private boolean checkLineOfSight = true;
/*    */   private boolean testInvisible = true;
/*    */   private Selector selector;
/*    */   
/*    */   private TargetingConditions(boolean paramBoolean) {
/* 20 */     this.isCombat = paramBoolean;
/*    */   }
/*    */   
/*    */   public static TargetingConditions forCombat() {
/* 24 */     return new TargetingConditions(true);
/*    */   }
/*    */   
/*    */   public static TargetingConditions forNonCombat() {
/* 28 */     return new TargetingConditions(false);
/*    */   }
/*    */   
/*    */   public TargetingConditions copy() {
/* 32 */     TargetingConditions targetingConditions = this.isCombat ? forCombat() : forNonCombat();
/* 33 */     targetingConditions.range = this.range;
/* 34 */     targetingConditions.checkLineOfSight = this.checkLineOfSight;
/* 35 */     targetingConditions.testInvisible = this.testInvisible;
/* 36 */     targetingConditions.selector = this.selector;
/* 37 */     return targetingConditions;
/*    */   }
/*    */   
/*    */   public TargetingConditions range(double paramDouble) {
/* 41 */     this.range = paramDouble;
/* 42 */     return this;
/*    */   }
/*    */   
/*    */   public TargetingConditions ignoreLineOfSight() {
/* 46 */     this.checkLineOfSight = false;
/* 47 */     return this;
/*    */   }
/*    */   
/*    */   public TargetingConditions ignoreInvisibilityTesting() {
/* 51 */     this.testInvisible = false;
/* 52 */     return this;
/*    */   }
/*    */   
/*    */   public TargetingConditions selector(Selector paramSelector) {
/* 56 */     this.selector = paramSelector;
/* 57 */     return this;
/*    */   }
/*    */   
/*    */   public boolean test(ServerLevel paramServerLevel, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/* 61 */     if (paramLivingEntity1 == paramLivingEntity2) {
/* 62 */       return false;
/*    */     }
/* 64 */     if (!paramLivingEntity2.canBeSeenByAnyone()) {
/* 65 */       return false;
/*    */     }
/* 67 */     if (this.selector != null && !this.selector.test(paramLivingEntity2, paramServerLevel)) {
/* 68 */       return false;
/*    */     }
/* 70 */     if (paramLivingEntity1 == null) {
/* 71 */       if (this.isCombat && (!paramLivingEntity2.canBeSeenAsEnemy() || paramServerLevel.getDifficulty() == Difficulty.PEACEFUL)) {
/* 72 */         return false;
/*    */       }
/*    */     } else {
/* 75 */       if (this.isCombat && (!paramLivingEntity1.canAttack(paramLivingEntity2) || !paramLivingEntity1.canAttackType(paramLivingEntity2.getType()) || paramLivingEntity1.isAlliedTo((Entity)paramLivingEntity2))) {
/* 76 */         return false;
/*    */       }
/*    */       
/* 79 */       if (this.range > 0.0D) {
/* 80 */         double d1 = this.testInvisible ? paramLivingEntity2.getVisibilityPercent((Entity)paramLivingEntity1) : 1.0D;
/* 81 */         double d2 = Math.max(this.range * d1, 2.0D);
/* 82 */         double d3 = paramLivingEntity1.distanceToSqr(paramLivingEntity2.getX(), paramLivingEntity2.getY(), paramLivingEntity2.getZ());
/* 83 */         if (d3 > d2 * d2) {
/* 84 */           return false;
/*    */         }
/*    */       } 
/*    */ 
/*    */       
/* 89 */       if (this.checkLineOfSight && paramLivingEntity1 instanceof Mob) { Mob mob = (Mob)paramLivingEntity1; if (!mob.getSensing().hasLineOfSight((Entity)paramLivingEntity2))
/* 90 */           return false;  }
/*    */     
/*    */     } 
/* 93 */     return true;
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Selector {
/*    */     boolean test(LivingEntity param1LivingEntity, ServerLevel param1ServerLevel);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\targeting\TargetingConditions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */