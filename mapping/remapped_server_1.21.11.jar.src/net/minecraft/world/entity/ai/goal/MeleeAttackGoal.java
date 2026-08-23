/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySelector;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.pathfinder.Path;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MeleeAttackGoal
/*     */   extends Goal
/*     */ {
/*     */   protected final PathfinderMob mob;
/*     */   private final double speedModifier;
/*     */   private final boolean followingTargetEvenIfNotSeen;
/*     */   private Path path;
/*     */   private double pathedTargetX;
/*     */   private double pathedTargetY;
/*     */   private double pathedTargetZ;
/*     */   private int ticksUntilNextPathRecalculation;
/*     */   private int ticksUntilNextAttack;
/*  26 */   private final int attackInterval = 20;
/*     */   
/*     */   private long lastCanUseCheck;
/*     */   private static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;
/*     */   
/*     */   public MeleeAttackGoal(PathfinderMob paramPathfinderMob, double paramDouble, boolean paramBoolean) {
/*  32 */     this.mob = paramPathfinderMob;
/*  33 */     this.speedModifier = paramDouble;
/*  34 */     this.followingTargetEvenIfNotSeen = paramBoolean;
/*  35 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  40 */     long l = this.mob.level().getGameTime();
/*  41 */     if (l - this.lastCanUseCheck < 20L) {
/*  42 */       return false;
/*     */     }
/*     */     
/*  45 */     this.lastCanUseCheck = l;
/*     */     
/*  47 */     LivingEntity livingEntity = this.mob.getTarget();
/*  48 */     if (livingEntity == null) {
/*  49 */       return false;
/*     */     }
/*  51 */     if (!livingEntity.isAlive()) {
/*  52 */       return false;
/*     */     }
/*  54 */     this.path = this.mob.getNavigation().createPath((Entity)livingEntity, 0);
/*  55 */     if (this.path != null) {
/*  56 */       return true;
/*     */     }
/*  58 */     if (this.mob.isWithinMeleeAttackRange(livingEntity)) {
/*  59 */       return true;
/*     */     }
/*  61 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  66 */     LivingEntity livingEntity = this.mob.getTarget();
/*  67 */     if (livingEntity == null) {
/*  68 */       return false;
/*     */     }
/*  70 */     if (!livingEntity.isAlive()) {
/*  71 */       return false;
/*     */     }
/*  73 */     if (!this.followingTargetEvenIfNotSeen) {
/*  74 */       return !this.mob.getNavigation().isDone();
/*     */     }
/*  76 */     if (!this.mob.isWithinHome(livingEntity.blockPosition())) {
/*  77 */       return false;
/*     */     }
/*     */     
/*  80 */     if (livingEntity instanceof Player) { Player player = (Player)livingEntity; if (player.isSpectator() || player.isCreative()) {
/*  81 */         return false;
/*     */       } }
/*     */     
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  89 */     this.mob.getNavigation().moveTo(this.path, this.speedModifier);
/*  90 */     this.mob.setAggressive(true);
/*  91 */     this.ticksUntilNextPathRecalculation = 0;
/*  92 */     this.ticksUntilNextAttack = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  97 */     LivingEntity livingEntity = this.mob.getTarget();
/*  98 */     if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
/*  99 */       this.mob.setTarget(null);
/*     */     }
/* 101 */     this.mob.setAggressive(false);
/* 102 */     this.mob.getNavigation().stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/* 107 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 112 */     LivingEntity livingEntity = this.mob.getTarget();
/* 113 */     if (livingEntity == null) {
/*     */       return;
/*     */     }
/* 116 */     this.mob.getLookControl().setLookAt((Entity)livingEntity, 30.0F, 30.0F);
/* 117 */     this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
/*     */     
/* 119 */     if ((this.followingTargetEvenIfNotSeen || this.mob.getSensing().hasLineOfSight((Entity)livingEntity)) && 
/* 120 */       this.ticksUntilNextPathRecalculation <= 0 && ((
/* 121 */       this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D) || livingEntity.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0D || this.mob.getRandom().nextFloat() < 0.05F)) {
/* 122 */       this.pathedTargetX = livingEntity.getX();
/* 123 */       this.pathedTargetY = livingEntity.getY();
/* 124 */       this.pathedTargetZ = livingEntity.getZ();
/* 125 */       this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
/*     */       
/* 127 */       double d = this.mob.distanceToSqr((Entity)livingEntity);
/* 128 */       if (d > 1024.0D) {
/* 129 */         this.ticksUntilNextPathRecalculation += 10;
/* 130 */       } else if (d > 256.0D) {
/* 131 */         this.ticksUntilNextPathRecalculation += 5;
/*     */       } 
/*     */       
/* 134 */       if (!this.mob.getNavigation().moveTo((Entity)livingEntity, this.speedModifier)) {
/* 135 */         this.ticksUntilNextPathRecalculation += 15;
/*     */       }
/*     */       
/* 138 */       this.ticksUntilNextPathRecalculation = adjustedTickDelay(this.ticksUntilNextPathRecalculation);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 143 */     this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
/* 144 */     checkAndPerformAttack(livingEntity);
/*     */   }
/*     */   
/*     */   protected void checkAndPerformAttack(LivingEntity paramLivingEntity) {
/* 148 */     if (canPerformAttack(paramLivingEntity)) {
/* 149 */       resetAttackCooldown();
/* 150 */       this.mob.swing(InteractionHand.MAIN_HAND);
/* 151 */       this.mob.doHurtTarget(getServerLevel((Entity)this.mob), (Entity)paramLivingEntity);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void resetAttackCooldown() {
/* 156 */     this.ticksUntilNextAttack = adjustedTickDelay(20);
/*     */   }
/*     */   
/*     */   protected boolean isTimeToAttack() {
/* 160 */     return (this.ticksUntilNextAttack <= 0);
/*     */   }
/*     */   
/*     */   protected boolean canPerformAttack(LivingEntity paramLivingEntity) {
/* 164 */     return (isTimeToAttack() && this.mob.isWithinMeleeAttackRange(paramLivingEntity) && this.mob.getSensing().hasLineOfSight((Entity)paramLivingEntity));
/*     */   }
/*     */   
/*     */   protected int getTicksUntilNextAttack() {
/* 168 */     return this.ticksUntilNextAttack;
/*     */   }
/*     */   
/*     */   protected int getAttackInterval() {
/* 172 */     return adjustedTickDelay(20);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\ai\goal\MeleeAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */