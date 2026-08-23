/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ 
/*     */ 
/*     */ public class LookAtPlayerGoal
/*     */   extends Goal
/*     */ {
/*     */   public static final float DEFAULT_PROBABILITY = 0.02F;
/*     */   protected final Mob mob;
/*     */   protected Entity lookAt;
/*     */   protected final float lookDistance;
/*     */   private int lookTime;
/*     */   protected final float probability;
/*     */   private final boolean onlyHorizontal;
/*     */   protected final Class<? extends LivingEntity> lookAtType;
/*     */   protected final TargetingConditions lookAtContext;
/*     */   
/*     */   public LookAtPlayerGoal(Mob paramMob, Class<? extends LivingEntity> paramClass, float paramFloat) {
/*  28 */     this(paramMob, paramClass, paramFloat, 0.02F);
/*     */   }
/*     */   
/*     */   public LookAtPlayerGoal(Mob paramMob, Class<? extends LivingEntity> paramClass, float paramFloat1, float paramFloat2) {
/*  32 */     this(paramMob, paramClass, paramFloat1, paramFloat2, false);
/*     */   }
/*     */   
/*     */   public LookAtPlayerGoal(Mob paramMob, Class<? extends LivingEntity> paramClass, float paramFloat1, float paramFloat2, boolean paramBoolean) {
/*  36 */     this.mob = paramMob;
/*  37 */     this.lookAtType = paramClass;
/*  38 */     this.lookDistance = paramFloat1;
/*  39 */     this.probability = paramFloat2;
/*  40 */     this.onlyHorizontal = paramBoolean;
/*  41 */     setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */     
/*  43 */     if (paramClass == Player.class) {
/*  44 */       Predicate predicate = EntitySelector.notRiding((Entity)paramMob);
/*  45 */       this.lookAtContext = TargetingConditions.forNonCombat().range(paramFloat1).selector((paramLivingEntity, paramServerLevel) -> paramPredicate.test(paramLivingEntity));
/*     */     } else {
/*  47 */       this.lookAtContext = TargetingConditions.forNonCombat().range(paramFloat1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  53 */     if (this.mob.getRandom().nextFloat() >= this.probability) {
/*  54 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  58 */     if (this.mob.getTarget() != null) {
/*  59 */       this.lookAt = (Entity)this.mob.getTarget();
/*     */     }
/*     */     
/*  62 */     ServerLevel serverLevel = getServerLevel((Entity)this.mob);
/*  63 */     if (this.lookAtType == Player.class) {
/*  64 */       this.lookAt = (Entity)serverLevel.getNearestPlayer(this.lookAtContext, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*     */     } else {
/*  66 */       this.lookAt = (Entity)serverLevel.getNearestEntity(this.mob.level().getEntitiesOfClass(this.lookAtType, this.mob.getBoundingBox().inflate(this.lookDistance, 3.0D, this.lookDistance), paramLivingEntity -> true), this.lookAtContext, (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*     */     } 
/*     */     
/*  69 */     return (this.lookAt != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  74 */     if (!this.lookAt.isAlive()) {
/*  75 */       return false;
/*     */     }
/*  77 */     if (this.mob.distanceToSqr(this.lookAt) > (this.lookDistance * this.lookDistance)) {
/*  78 */       return false;
/*     */     }
/*  80 */     return (this.lookTime > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  85 */     this.lookTime = adjustedTickDelay(40 + this.mob.getRandom().nextInt(40));
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  90 */     this.lookAt = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  95 */     if (!this.lookAt.isAlive()) {
/*     */       return;
/*     */     }
/*  98 */     double d = this.onlyHorizontal ? this.mob.getEyeY() : this.lookAt.getEyeY();
/*  99 */     this.mob.getLookControl().setLookAt(this.lookAt.getX(), d, this.lookAt.getZ());
/* 100 */     this.lookTime--;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\LookAtPlayerGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */