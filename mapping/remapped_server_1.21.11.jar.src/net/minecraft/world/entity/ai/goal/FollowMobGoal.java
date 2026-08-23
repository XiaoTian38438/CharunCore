/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ 
/*     */ 
/*     */ public class FollowMobGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Mob mob;
/*     */   private final Predicate<Mob> followPredicate;
/*     */   private Mob followingMob;
/*     */   private final double speedModifier;
/*     */   private final PathNavigation navigation;
/*     */   private int timeToRecalcPath;
/*     */   private final float stopDistance;
/*     */   private float oldWaterCost;
/*     */   private final float areaSize;
/*     */   
/*     */   public FollowMobGoal(Mob paramMob, double paramDouble, float paramFloat1, float paramFloat2) {
/*  27 */     this.mob = paramMob;
/*  28 */     this.followPredicate = (paramMob2 -> (paramMob1.getClass() != paramMob2.getClass()));
/*  29 */     this.speedModifier = paramDouble;
/*  30 */     this.navigation = paramMob.getNavigation();
/*  31 */     this.stopDistance = paramFloat1;
/*  32 */     this.areaSize = paramFloat2;
/*     */     
/*  34 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     
/*  36 */     if (!(paramMob.getNavigation() instanceof net.minecraft.world.entity.ai.navigation.GroundPathNavigation) && !(paramMob.getNavigation() instanceof net.minecraft.world.entity.ai.navigation.FlyingPathNavigation)) {
/*  37 */       throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  43 */     List list = this.mob.level().getEntitiesOfClass(Mob.class, this.mob.getBoundingBox().inflate(this.areaSize), this.followPredicate);
/*  44 */     if (!list.isEmpty()) {
/*  45 */       for (Mob mob : list) {
/*  46 */         if (mob.isInvisible()) {
/*     */           continue;
/*     */         }
/*     */         
/*  50 */         this.followingMob = mob;
/*  51 */         return true;
/*     */       } 
/*     */     }
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  59 */     return (this.followingMob != null && !this.navigation.isDone() && this.mob.distanceToSqr((Entity)this.followingMob) > (this.stopDistance * this.stopDistance));
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  64 */     this.timeToRecalcPath = 0;
/*  65 */     this.oldWaterCost = this.mob.getPathfindingMalus(PathType.WATER);
/*  66 */     this.mob.setPathfindingMalus(PathType.WATER, 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  71 */     this.followingMob = null;
/*  72 */     this.navigation.stop();
/*  73 */     this.mob.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  78 */     if (this.followingMob == null || this.mob.isLeashed()) {
/*     */       return;
/*     */     }
/*     */     
/*  82 */     this.mob.getLookControl().setLookAt((Entity)this.followingMob, 10.0F, this.mob.getMaxHeadXRot());
/*     */     
/*  84 */     if (--this.timeToRecalcPath > 0) {
/*     */       return;
/*     */     }
/*  87 */     this.timeToRecalcPath = adjustedTickDelay(10);
/*     */     
/*  89 */     double d1 = this.mob.getX() - this.followingMob.getX();
/*  90 */     double d2 = this.mob.getY() - this.followingMob.getY();
/*  91 */     double d3 = this.mob.getZ() - this.followingMob.getZ();
/*     */     
/*  93 */     double d4 = d1 * d1 + d2 * d2 + d3 * d3;
/*  94 */     if (d4 <= (this.stopDistance * this.stopDistance)) {
/*  95 */       this.navigation.stop();
/*     */       
/*  97 */       LookControl lookControl = this.followingMob.getLookControl();
/*  98 */       if (d4 <= this.stopDistance || (lookControl.getWantedX() == this.mob.getX() && lookControl.getWantedY() == this.mob.getY() && lookControl.getWantedZ() == this.mob.getZ())) {
/*  99 */         double d5 = this.followingMob.getX() - this.mob.getX();
/* 100 */         double d6 = this.followingMob.getZ() - this.mob.getZ();
/* 101 */         this.navigation.moveTo(this.mob.getX() - d5, this.mob.getY(), this.mob.getZ() - d6, this.speedModifier);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 106 */     this.navigation.moveTo((Entity)this.followingMob, this.speedModifier);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\FollowMobGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */