/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.damagesource.DamageTypes;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Guardian
/*     */   extends Monster
/*     */ {
/*     */   protected static final int ATTACK_TIME = 80;
/*  53 */   private static final EntityDataAccessor<Boolean> DATA_ID_MOVING = SynchedEntityData.defineId(Guardian.class, EntityDataSerializers.BOOLEAN);
/*  54 */   private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(Guardian.class, EntityDataSerializers.INT);
/*     */   
/*     */   private float clientSideTailAnimation;
/*     */   private float clientSideTailAnimationO;
/*     */   private float clientSideTailAnimationSpeed;
/*     */   private float clientSideSpikesAnimation;
/*     */   private float clientSideSpikesAnimationO;
/*     */   private LivingEntity clientSideCachedAttackTarget;
/*     */   private int clientSideAttackTime;
/*     */   private boolean clientSideTouchedGround;
/*     */   protected RandomStrollGoal randomStrollGoal;
/*     */   
/*     */   public Guardian(EntityType<? extends Guardian> paramEntityType, Level paramLevel) {
/*  67 */     super((EntityType)paramEntityType, paramLevel);
/*     */     
/*  69 */     this.xpReward = 10;
/*     */     
/*  71 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*  72 */     this.moveControl = new GuardianMoveControl(this);
/*     */     
/*  74 */     this.clientSideTailAnimation = this.random.nextFloat();
/*  75 */     this.clientSideTailAnimationO = this.clientSideTailAnimation;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  80 */     MoveTowardsRestrictionGoal moveTowardsRestrictionGoal = new MoveTowardsRestrictionGoal(this, 1.0D);
/*  81 */     this.randomStrollGoal = new RandomStrollGoal(this, 1.0D, 80);
/*     */     
/*  83 */     this.goalSelector.addGoal(4, new GuardianAttackGoal(this));
/*  84 */     this.goalSelector.addGoal(5, (Goal)moveTowardsRestrictionGoal);
/*  85 */     this.goalSelector.addGoal(7, (Goal)this.randomStrollGoal);
/*  86 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/*     */     
/*  88 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Guardian.class, 12.0F, 0.01F));
/*  89 */     this.goalSelector.addGoal(9, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */ 
/*     */     
/*  92 */     this.randomStrollGoal.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*  93 */     moveTowardsRestrictionGoal.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     
/*  95 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal((Mob)this, LivingEntity.class, 10, true, false, new GuardianAttackSelector(this)));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  99 */     return Monster.createMonsterAttributes()
/* 100 */       .add(Attributes.ATTACK_DAMAGE, 6.0D)
/* 101 */       .add(Attributes.MOVEMENT_SPEED, 0.5D)
/* 102 */       .add(Attributes.MAX_HEALTH, 30.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level paramLevel) {
/* 107 */     return (PathNavigation)new WaterBoundPathNavigation((Mob)this, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 112 */     super.defineSynchedData(paramBuilder);
/*     */     
/* 114 */     paramBuilder.define(DATA_ID_MOVING, Boolean.valueOf(false));
/* 115 */     paramBuilder.define(DATA_ID_ATTACK_TARGET, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */   public boolean isMoving() {
/* 119 */     return ((Boolean)this.entityData.get(DATA_ID_MOVING)).booleanValue();
/*     */   }
/*     */   
/*     */   void setMoving(boolean paramBoolean) {
/* 123 */     this.entityData.set(DATA_ID_MOVING, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */   
/*     */   public int getAttackDuration() {
/* 127 */     return 80;
/*     */   }
/*     */   
/*     */   void setActiveAttackTarget(int paramInt) {
/* 131 */     this.entityData.set(DATA_ID_ATTACK_TARGET, Integer.valueOf(paramInt));
/*     */   }
/*     */   
/*     */   public boolean hasActiveAttackTarget() {
/* 135 */     return (((Integer)this.entityData.get(DATA_ID_ATTACK_TARGET)).intValue() != 0);
/*     */   }
/*     */   
/*     */   public LivingEntity getActiveAttackTarget() {
/* 139 */     if (!hasActiveAttackTarget()) {
/* 140 */       return null;
/*     */     }
/* 142 */     if (level().isClientSide()) {
/* 143 */       if (this.clientSideCachedAttackTarget != null) {
/* 144 */         return this.clientSideCachedAttackTarget;
/*     */       }
/* 146 */       Entity entity = level().getEntity(((Integer)this.entityData.get(DATA_ID_ATTACK_TARGET)).intValue());
/* 147 */       if (entity instanceof LivingEntity) {
/* 148 */         this.clientSideCachedAttackTarget = (LivingEntity)entity;
/* 149 */         return this.clientSideCachedAttackTarget;
/*     */       } 
/* 151 */       return null;
/*     */     } 
/* 153 */     return getTarget();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/* 158 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/*     */     
/* 160 */     if (DATA_ID_ATTACK_TARGET.equals(paramEntityDataAccessor)) {
/* 161 */       this.clientSideAttackTime = 0;
/* 162 */       this.clientSideCachedAttackTarget = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAmbientSoundInterval() {
/* 168 */     return 160;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 173 */     return isInWater() ? SoundEvents.GUARDIAN_AMBIENT : SoundEvents.GUARDIAN_AMBIENT_LAND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 178 */     return isInWater() ? SoundEvents.GUARDIAN_HURT : SoundEvents.GUARDIAN_HURT_LAND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 183 */     return isInWater() ? SoundEvents.GUARDIAN_DEATH : SoundEvents.GUARDIAN_DEATH_LAND;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Entity.MovementEmission getMovementEmission() {
/* 188 */     return Entity.MovementEmission.EVENTS;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos paramBlockPos, LevelReader paramLevelReader) {
/* 193 */     if (paramLevelReader.getFluidState(paramBlockPos).is(FluidTags.WATER)) {
/* 194 */       return 10.0F + paramLevelReader.getPathfindingCostFromLightLevels(paramBlockPos);
/*     */     }
/* 196 */     return super.getWalkTargetValue(paramBlockPos, paramLevelReader);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 201 */     if (isAlive()) {
/* 202 */       if (level().isClientSide()) {
/*     */         
/* 204 */         this.clientSideTailAnimationO = this.clientSideTailAnimation;
/* 205 */         if (!isInWater()) {
/* 206 */           this.clientSideTailAnimationSpeed = 2.0F;
/* 207 */           Vec3 vec3 = getDeltaMovement();
/* 208 */           if (vec3.y > 0.0D && this.clientSideTouchedGround && !isSilent()) {
/* 209 */             level().playLocalSound(getX(), getY(), getZ(), getFlopSound(), getSoundSource(), 1.0F, 1.0F, false);
/*     */           }
/* 211 */           this.clientSideTouchedGround = (vec3.y < 0.0D && level().loadedAndEntityCanStandOn(blockPosition().below(), (Entity)this));
/* 212 */         } else if (isMoving()) {
/* 213 */           if (this.clientSideTailAnimationSpeed < 0.5F) {
/* 214 */             this.clientSideTailAnimationSpeed = 4.0F;
/*     */           } else {
/* 216 */             this.clientSideTailAnimationSpeed += (0.5F - this.clientSideTailAnimationSpeed) * 0.1F;
/*     */           } 
/*     */         } else {
/* 219 */           this.clientSideTailAnimationSpeed += (0.125F - this.clientSideTailAnimationSpeed) * 0.2F;
/*     */         } 
/* 221 */         this.clientSideTailAnimation += this.clientSideTailAnimationSpeed;
/*     */ 
/*     */         
/* 224 */         this.clientSideSpikesAnimationO = this.clientSideSpikesAnimation;
/* 225 */         if (!isInWater()) {
/* 226 */           this.clientSideSpikesAnimation = this.random.nextFloat();
/* 227 */         } else if (isMoving()) {
/* 228 */           this.clientSideSpikesAnimation += (0.0F - this.clientSideSpikesAnimation) * 0.25F;
/*     */         } else {
/* 230 */           this.clientSideSpikesAnimation += (1.0F - this.clientSideSpikesAnimation) * 0.06F;
/*     */         } 
/*     */         
/* 233 */         if (isMoving() && isInWater()) {
/* 234 */           Vec3 vec3 = getViewVector(0.0F);
/* 235 */           for (byte b = 0; b < 2; b++) {
/* 236 */             level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, getRandomX(0.5D) - vec3.x * 1.5D, getRandomY() - vec3.y * 1.5D, getRandomZ(0.5D) - vec3.z * 1.5D, 0.0D, 0.0D, 0.0D);
/*     */           }
/*     */         } 
/*     */         
/* 240 */         if (hasActiveAttackTarget()) {
/* 241 */           if (this.clientSideAttackTime < getAttackDuration()) {
/* 242 */             this.clientSideAttackTime++;
/*     */           }
/* 244 */           LivingEntity livingEntity = getActiveAttackTarget();
/* 245 */           if (livingEntity != null) {
/* 246 */             getLookControl().setLookAt((Entity)livingEntity, 90.0F, 90.0F);
/* 247 */             getLookControl().tick();
/*     */             
/* 249 */             double d1 = getAttackAnimationScale(0.0F);
/* 250 */             double d2 = livingEntity.getX() - getX();
/* 251 */             double d3 = livingEntity.getY(0.5D) - getEyeY();
/* 252 */             double d4 = livingEntity.getZ() - getZ();
/* 253 */             double d5 = Math.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
/* 254 */             d2 /= d5;
/* 255 */             d3 /= d5;
/* 256 */             d4 /= d5;
/* 257 */             double d6 = this.random.nextDouble();
/* 258 */             while (d6 < d5) {
/* 259 */               d6 += 1.8D - d1 + this.random.nextDouble() * (1.7D - d1);
/* 260 */               level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, getX() + d2 * d6, getEyeY() + d3 * d6, getZ() + d4 * d6, 0.0D, 0.0D, 0.0D);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 266 */       if (isInWater()) {
/* 267 */         setAirSupply(300);
/*     */       }
/* 269 */       else if (onGround()) {
/* 270 */         setDeltaMovement(getDeltaMovement().add(((this.random
/* 271 */               .nextFloat() * 2.0F - 1.0F) * 0.4F), 0.5D, ((this.random
/*     */               
/* 273 */               .nextFloat() * 2.0F - 1.0F) * 0.4F)));
/*     */         
/* 275 */         setYRot(this.random.nextFloat() * 360.0F);
/* 276 */         setOnGround(false);
/* 277 */         this.needsSync = true;
/*     */       } 
/*     */ 
/*     */       
/* 281 */       if (hasActiveAttackTarget()) {
/* 282 */         setYRot(this.yHeadRot);
/*     */       }
/*     */     } 
/*     */     
/* 286 */     super.aiStep();
/*     */   }
/*     */   
/*     */   protected SoundEvent getFlopSound() {
/* 290 */     return SoundEvents.GUARDIAN_FLOP;
/*     */   }
/*     */   
/*     */   public float getTailAnimation(float paramFloat) {
/* 294 */     return Mth.lerp(paramFloat, this.clientSideTailAnimationO, this.clientSideTailAnimation);
/*     */   }
/*     */   
/*     */   public float getSpikesAnimation(float paramFloat) {
/* 298 */     return Mth.lerp(paramFloat, this.clientSideSpikesAnimationO, this.clientSideSpikesAnimation);
/*     */   }
/*     */   
/*     */   public float getAttackAnimationScale(float paramFloat) {
/* 302 */     return (this.clientSideAttackTime + paramFloat) / getAttackDuration();
/*     */   }
/*     */   
/*     */   public float getClientSideAttackTime() {
/* 306 */     return this.clientSideAttackTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader paramLevelReader) {
/* 311 */     return paramLevelReader.isUnobstructed((Entity)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean checkGuardianSpawnRules(EntityType<? extends Guardian> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 316 */     return ((paramRandomSource.nextInt(20) == 0 || !paramLevelAccessor.canSeeSkyFromBelowWater(paramBlockPos)) && paramLevelAccessor
/* 317 */       .getDifficulty() != Difficulty.PEACEFUL && (
/* 318 */       EntitySpawnReason.isSpawner(paramEntitySpawnReason) || paramLevelAccessor.getFluidState(paramBlockPos).is(FluidTags.WATER)) && paramLevelAccessor
/* 319 */       .getFluidState(paramBlockPos.below()).is(FluidTags.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 324 */     if (!isMoving() && !paramDamageSource.is(DamageTypeTags.AVOIDS_GUARDIAN_THORNS) && !paramDamageSource.is(DamageTypes.THORNS)) { Entity entity = paramDamageSource.getDirectEntity(); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 325 */         livingEntity.hurtServer(paramServerLevel, damageSources().thorns((Entity)this), 2.0F); }
/*     */        }
/*     */     
/* 328 */     if (this.randomStrollGoal != null) {
/* 329 */       this.randomStrollGoal.trigger();
/*     */     }
/*     */     
/* 332 */     return super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadXRot() {
/* 337 */     return 180;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void travelInWater(Vec3 paramVec3, double paramDouble1, boolean paramBoolean, double paramDouble2) {
/* 342 */     moveRelative(0.1F, paramVec3);
/* 343 */     move(MoverType.SELF, getDeltaMovement());
/*     */     
/* 345 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */     
/* 347 */     if (!isMoving() && getTarget() == null)
/* 348 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.005D, 0.0D)); 
/*     */   }
/*     */   
/*     */   private static class GuardianAttackSelector
/*     */     implements TargetingConditions.Selector {
/*     */     private final Guardian guardian;
/*     */     
/*     */     public GuardianAttackSelector(Guardian param1Guardian) {
/* 356 */       this.guardian = param1Guardian;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean test(LivingEntity param1LivingEntity, ServerLevel param1ServerLevel) {
/* 361 */       return ((param1LivingEntity instanceof Player || param1LivingEntity instanceof net.minecraft.world.entity.animal.squid.Squid || param1LivingEntity instanceof net.minecraft.world.entity.animal.axolotl.Axolotl) && param1LivingEntity.distanceToSqr((Entity)this.guardian) > 9.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class GuardianAttackGoal extends Goal {
/*     */     private final Guardian guardian;
/*     */     private int attackTime;
/*     */     private final boolean elder;
/*     */     
/*     */     public GuardianAttackGoal(Guardian param1Guardian) {
/* 371 */       this.guardian = param1Guardian;
/*     */ 
/*     */       
/* 374 */       this.elder = param1Guardian instanceof ElderGuardian;
/*     */       
/* 376 */       setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 381 */       LivingEntity livingEntity = this.guardian.getTarget();
/* 382 */       return (livingEntity != null && livingEntity.isAlive());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 387 */       return (super.canContinueToUse() && (this.elder || (this.guardian.getTarget() != null && this.guardian.distanceToSqr((Entity)this.guardian.getTarget()) > 9.0D)));
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 392 */       this.attackTime = -10;
/* 393 */       this.guardian.getNavigation().stop();
/* 394 */       LivingEntity livingEntity = this.guardian.getTarget();
/* 395 */       if (livingEntity != null) {
/* 396 */         this.guardian.getLookControl().setLookAt((Entity)livingEntity, 90.0F, 90.0F);
/*     */       }
/*     */ 
/*     */       
/* 400 */       this.guardian.needsSync = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 405 */       this.guardian.setActiveAttackTarget(0);
/* 406 */       this.guardian.setTarget(null);
/*     */       
/* 408 */       this.guardian.randomStrollGoal.trigger();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 413 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 418 */       LivingEntity livingEntity = this.guardian.getTarget();
/* 419 */       if (livingEntity == null) {
/*     */         return;
/*     */       }
/*     */       
/* 423 */       this.guardian.getNavigation().stop();
/* 424 */       this.guardian.getLookControl().setLookAt((Entity)livingEntity, 90.0F, 90.0F);
/*     */       
/* 426 */       if (!this.guardian.hasLineOfSight((Entity)livingEntity)) {
/* 427 */         this.guardian.setTarget(null);
/*     */         
/*     */         return;
/*     */       } 
/* 431 */       this.attackTime++;
/* 432 */       if (this.attackTime == 0) {
/*     */         
/* 434 */         this.guardian.setActiveAttackTarget(livingEntity.getId());
/* 435 */         if (!this.guardian.isSilent()) {
/* 436 */           this.guardian.level().broadcastEntityEvent((Entity)this.guardian, (byte)21);
/*     */         }
/* 438 */       } else if (this.attackTime >= this.guardian.getAttackDuration()) {
/* 439 */         float f = 1.0F;
/* 440 */         if (this.guardian.level().getDifficulty() == Difficulty.HARD) {
/* 441 */           f += 2.0F;
/*     */         }
/* 443 */         if (this.elder) {
/* 444 */           f += 2.0F;
/*     */         }
/* 446 */         ServerLevel serverLevel = getServerLevel((Entity)this.guardian);
/* 447 */         livingEntity.hurtServer(serverLevel, this.guardian.damageSources().indirectMagic((Entity)this.guardian, (Entity)this.guardian), f);
/* 448 */         this.guardian.doHurtTarget(serverLevel, (Entity)livingEntity);
/* 449 */         this.guardian.setTarget(null);
/*     */       } 
/*     */       
/* 452 */       super.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class GuardianMoveControl extends MoveControl {
/*     */     private final Guardian guardian;
/*     */     
/*     */     public GuardianMoveControl(Guardian param1Guardian) {
/* 460 */       super((Mob)param1Guardian);
/* 461 */       this.guardian = param1Guardian;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 466 */       if (this.operation != MoveControl.Operation.MOVE_TO || this.guardian.getNavigation().isDone()) {
/*     */         
/* 468 */         this.guardian.setSpeed(0.0F);
/* 469 */         this.guardian.setMoving(false);
/*     */ 
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 477 */       Vec3 vec3 = new Vec3(this.wantedX - this.guardian.getX(), this.wantedY - this.guardian.getY(), this.wantedZ - this.guardian.getZ());
/*     */       
/* 479 */       double d1 = vec3.length();
/*     */       
/* 481 */       double d2 = vec3.x / d1;
/* 482 */       double d3 = vec3.y / d1;
/* 483 */       double d4 = vec3.z / d1;
/*     */       
/* 485 */       float f1 = (float)(Mth.atan2(vec3.z, vec3.x) * 57.2957763671875D) - 90.0F;
/*     */       
/* 487 */       this.guardian.setYRot(rotlerp(this.guardian.getYRot(), f1, 90.0F));
/* 488 */       this.guardian.yBodyRot = this.guardian.getYRot();
/*     */       
/* 490 */       float f2 = (float)(this.speedModifier * this.guardian.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 491 */       float f3 = Mth.lerp(0.125F, this.guardian.getSpeed(), f2);
/* 492 */       this.guardian.setSpeed(f3);
/* 493 */       double d5 = Math.sin((this.guardian.tickCount + this.guardian.getId()) * 0.5D) * 0.05D;
/* 494 */       double d6 = Math.cos((this.guardian.getYRot() * 0.017453292F));
/* 495 */       double d7 = Math.sin((this.guardian.getYRot() * 0.017453292F));
/* 496 */       double d8 = Math.sin((this.guardian.tickCount + this.guardian.getId()) * 0.75D) * 0.05D;
/*     */       
/* 498 */       this.guardian.setDeltaMovement(this.guardian.getDeltaMovement().add(d5 * d6, d8 * (d7 + d6) * 0.25D + f3 * d3 * 0.1D, d5 * d7));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 504 */       LookControl lookControl = this.guardian.getLookControl();
/* 505 */       double d9 = this.guardian.getX() + d2 * 2.0D;
/* 506 */       double d10 = this.guardian.getEyeY() + d3 / d1;
/* 507 */       double d11 = this.guardian.getZ() + d4 * 2.0D;
/* 508 */       double d12 = lookControl.getWantedX();
/* 509 */       double d13 = lookControl.getWantedY();
/* 510 */       double d14 = lookControl.getWantedZ();
/* 511 */       if (!lookControl.isLookingAtTarget()) {
/* 512 */         d12 = d9;
/* 513 */         d13 = d10;
/* 514 */         d14 = d11;
/*     */       } 
/* 516 */       this.guardian.getLookControl().setLookAt(Mth.lerp(0.125D, d12, d9), Mth.lerp(0.125D, d13, d10), Mth.lerp(0.125D, d14, d11), 10.0F, 40.0F);
/* 517 */       this.guardian.setMoving(true);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Guardian.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */