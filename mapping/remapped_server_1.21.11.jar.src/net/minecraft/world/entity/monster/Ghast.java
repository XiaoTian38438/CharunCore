/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Ghast extends Mob implements Enemy {
/*  47 */   private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING = SynchedEntityData.defineId(Ghast.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final byte DEFAULT_EXPLOSION_POWER = 1;
/*     */   
/*  51 */   private int explosionPower = 1;
/*     */   
/*     */   public Ghast(EntityType<? extends Ghast> paramEntityType, Level paramLevel) {
/*  54 */     super(paramEntityType, paramLevel);
/*     */     
/*  56 */     this.xpReward = 5;
/*     */     
/*  58 */     this.moveControl = new GhastMoveControl(this, false, () -> false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  63 */     this.goalSelector.addGoal(5, new RandomFloatAroundGoal(this));
/*     */     
/*  65 */     this.goalSelector.addGoal(7, new GhastLookGoal(this));
/*  66 */     this.goalSelector.addGoal(7, new GhastShootFireballGoal(this));
/*     */ 
/*     */     
/*  69 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal(this, Player.class, 10, true, false, (paramLivingEntity, paramServerLevel) -> (Math.abs(paramLivingEntity.getY() - getY()) <= 4.0D)));
/*     */   }
/*     */   
/*     */   public boolean isCharging() {
/*  73 */     return ((Boolean)this.entityData.get(DATA_IS_CHARGING)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setCharging(boolean paramBoolean) {
/*  77 */     this.entityData.set(DATA_IS_CHARGING, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */   
/*     */   public int getExplosionPower() {
/*  81 */     return this.explosionPower;
/*     */   }
/*     */   
/*     */   private static boolean isReflectedFireball(DamageSource paramDamageSource) {
/*  85 */     return (paramDamageSource.getDirectEntity() instanceof LargeFireball && paramDamageSource.getEntity() instanceof Player);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInvulnerableTo(ServerLevel paramServerLevel, DamageSource paramDamageSource) {
/*  90 */     return ((isInvulnerable() && !paramDamageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) || (
/*  91 */       !isReflectedFireball(paramDamageSource) && super.isInvulnerableTo(paramServerLevel, paramDamageSource)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkFallDamage(double paramDouble, boolean paramBoolean, BlockState paramBlockState, BlockPos paramBlockPos) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onClimbable() {
/* 102 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void travel(Vec3 paramVec3) {
/* 107 */     travelFlying(paramVec3, 0.02F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 112 */     if (isReflectedFireball(paramDamageSource)) {
/*     */       
/* 114 */       super.hurtServer(paramServerLevel, paramDamageSource, 1000.0F);
/* 115 */       return true;
/*     */     } 
/*     */     
/* 118 */     if (isInvulnerableTo(paramServerLevel, paramDamageSource)) {
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     return super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 127 */     super.defineSynchedData(paramBuilder);
/*     */     
/* 129 */     paramBuilder.define(DATA_IS_CHARGING, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 133 */     return Mob.createMobAttributes()
/* 134 */       .add(Attributes.MAX_HEALTH, 10.0D)
/* 135 */       .add(Attributes.FOLLOW_RANGE, 100.0D)
/* 136 */       .add(Attributes.CAMERA_DISTANCE, 8.0D)
/* 137 */       .add(Attributes.FLYING_SPEED, 0.06D);
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 142 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 147 */     return SoundEvents.GHAST_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 152 */     return SoundEvents.GHAST_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 157 */     return SoundEvents.GHAST_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 162 */     return 5.0F;
/*     */   }
/*     */   
/*     */   public static boolean checkGhastSpawnRules(EntityType<Ghast> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 166 */     return (paramLevelAccessor.getDifficulty() != Difficulty.PEACEFUL && paramRandomSource
/* 167 */       .nextInt(20) == 0 && 
/* 168 */       checkMobSpawnRules(paramEntityType, paramLevelAccessor, paramEntitySpawnReason, paramBlockPos, paramRandomSource));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSpawnClusterSize() {
/* 173 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 178 */     super.addAdditionalSaveData(paramValueOutput);
/* 179 */     paramValueOutput.putByte("ExplosionPower", (byte)this.explosionPower);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 184 */     super.readAdditionalSaveData(paramValueInput);
/* 185 */     this.explosionPower = paramValueInput.getByteOr("ExplosionPower", (byte)1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean supportQuadLeashAsHolder() {
/* 190 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public double leashElasticDistance() {
/* 195 */     return 10.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double leashSnapDistance() {
/* 200 */     return 16.0D;
/*     */   }
/*     */   
/*     */   public static class GhastMoveControl extends MoveControl {
/*     */     private final Mob ghast;
/*     */     private int floatDuration;
/*     */     private final boolean careful;
/*     */     private final BooleanSupplier shouldBeStopped;
/*     */     
/*     */     public GhastMoveControl(Mob param1Mob, boolean param1Boolean, BooleanSupplier param1BooleanSupplier) {
/* 210 */       super(param1Mob);
/* 211 */       this.ghast = param1Mob;
/* 212 */       this.careful = param1Boolean;
/* 213 */       this.shouldBeStopped = param1BooleanSupplier;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 218 */       if (this.shouldBeStopped.getAsBoolean()) {
/* 219 */         this.operation = MoveControl.Operation.WAIT;
/* 220 */         this.ghast.stopInPlace();
/*     */       } 
/*     */       
/* 223 */       if (this.operation != MoveControl.Operation.MOVE_TO) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 228 */       if (this.floatDuration-- <= 0) {
/* 229 */         this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 234 */         Vec3 vec3 = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
/*     */ 
/*     */         
/* 237 */         if (canReach(vec3)) {
/* 238 */           this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(vec3.normalize().scale(this.ghast.getAttributeValue(Attributes.FLYING_SPEED) * 5.0D / 3.0D)));
/*     */         } else {
/* 240 */           this.operation = MoveControl.Operation.WAIT;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean canReach(Vec3 param1Vec3) {
/* 246 */       AABB aABB1 = this.ghast.getBoundingBox();
/* 247 */       AABB aABB2 = aABB1.move(param1Vec3);
/* 248 */       if (this.careful) {
/* 249 */         for (BlockPos blockPos : BlockPos.betweenClosed(aABB2.inflate(1.0D))) {
/* 250 */           if (!blockTraversalPossible((BlockGetter)this.ghast.level(), (Vec3)null, (Vec3)null, blockPos, false, false)) {
/* 251 */             return false;
/*     */           }
/*     */         } 
/*     */       }
/* 255 */       boolean bool1 = this.ghast.isInWater();
/* 256 */       boolean bool2 = this.ghast.isInLava();
/* 257 */       Vec3 vec31 = this.ghast.position();
/* 258 */       Vec3 vec32 = vec31.add(param1Vec3);
/*     */       
/* 260 */       return BlockGetter.forEachBlockIntersectedBetween(vec31, vec32, aABB2, (param1BlockPos, param1Int) -> param1AABB.intersects(param1BlockPos) ? true : blockTraversalPossible((BlockGetter)this.ghast.level(), param1Vec31, param1Vec32, param1BlockPos, param1Boolean1, param1Boolean2));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean blockTraversalPossible(BlockGetter param1BlockGetter, Vec3 param1Vec31, Vec3 param1Vec32, BlockPos param1BlockPos, boolean param1Boolean1, boolean param1Boolean2) {
/* 269 */       BlockState blockState = param1BlockGetter.getBlockState(param1BlockPos);
/* 270 */       if (blockState.isAir()) {
/* 271 */         return true;
/*     */       }
/* 273 */       boolean bool1 = (param1Vec31 != null && param1Vec32 != null) ? true : false;
/* 274 */       boolean bool2 = bool1 ? (!this.ghast.collidedWithShapeMovingFrom(param1Vec31, param1Vec32, blockState.getCollisionShape(param1BlockGetter, param1BlockPos).move(new Vec3((Vec3i)param1BlockPos)).toAabbs()) ? true : false) : blockState.getCollisionShape(param1BlockGetter, param1BlockPos).isEmpty();
/* 275 */       if (!this.careful) {
/* 276 */         return bool2;
/*     */       }
/* 278 */       if (blockState.is(BlockTags.HAPPY_GHAST_AVOIDS)) {
/* 279 */         return false;
/*     */       }
/* 281 */       FluidState fluidState = param1BlockGetter.getFluidState(param1BlockPos);
/* 282 */       if (!fluidState.isEmpty() && (!bool1 || this.ghast.collidedWithFluid(fluidState, param1BlockPos, param1Vec31, param1Vec32))) {
/* 283 */         if (fluidState.is(FluidTags.WATER)) {
/* 284 */           return param1Boolean1;
/*     */         }
/* 286 */         if (fluidState.is(FluidTags.LAVA)) {
/* 287 */           return param1Boolean2;
/*     */         }
/*     */       } 
/* 290 */       return bool2;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RandomFloatAroundGoal extends Goal {
/*     */     private static final int MAX_ATTEMPTS = 64;
/*     */     private final Mob ghast;
/*     */     private final int distanceToBlocks;
/*     */     
/*     */     public RandomFloatAroundGoal(Mob param1Mob) {
/* 300 */       this(param1Mob, 0);
/*     */     }
/*     */     
/*     */     public RandomFloatAroundGoal(Mob param1Mob, int param1Int) {
/* 304 */       this.ghast = param1Mob;
/* 305 */       this.distanceToBlocks = param1Int;
/* 306 */       setFlags(EnumSet.of(Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 311 */       MoveControl moveControl = this.ghast.getMoveControl();
/* 312 */       if (!moveControl.hasWanted()) {
/* 313 */         return true;
/*     */       }
/*     */       
/* 316 */       double d1 = moveControl.getWantedX() - this.ghast.getX();
/* 317 */       double d2 = moveControl.getWantedY() - this.ghast.getY();
/* 318 */       double d3 = moveControl.getWantedZ() - this.ghast.getZ();
/*     */       
/* 320 */       double d4 = d1 * d1 + d2 * d2 + d3 * d3;
/*     */       
/* 322 */       return (d4 < 1.0D || d4 > 3600.0D);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 327 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 332 */       Vec3 vec3 = getSuitableFlyToPosition(this.ghast, this.distanceToBlocks);
/* 333 */       this.ghast.getMoveControl().setWantedPosition(vec3.x(), vec3.y(), vec3.z(), 1.0D);
/*     */     }
/*     */     
/*     */     public static Vec3 getSuitableFlyToPosition(Mob param1Mob, int param1Int) {
/* 337 */       Level level = param1Mob.level();
/* 338 */       RandomSource randomSource = param1Mob.getRandom();
/* 339 */       Vec3 vec31 = param1Mob.position();
/* 340 */       Vec3 vec32 = null;
/* 341 */       for (byte b = 0; b < 64; b++) {
/* 342 */         vec32 = chooseRandomPositionWithRestriction(param1Mob, vec31, randomSource);
/* 343 */         if (vec32 != null && isGoodTarget(level, vec32, param1Int)) {
/* 344 */           return vec32;
/*     */         }
/*     */       } 
/* 347 */       if (vec32 == null) {
/* 348 */         vec32 = chooseRandomPosition(vec31, randomSource);
/*     */       }
/*     */ 
/*     */       
/* 352 */       BlockPos blockPos = BlockPos.containing((Position)vec32);
/* 353 */       int i = level.getHeight(Heightmap.Types.MOTION_BLOCKING, blockPos.getX(), blockPos.getZ());
/* 354 */       if (i < blockPos.getY() && i > level.getMinY()) {
/* 355 */         vec32 = new Vec3(vec32.x(), param1Mob.getY() - Math.abs(param1Mob.getY() - vec32.y()), vec32.z());
/*     */       }
/* 357 */       return vec32;
/*     */     }
/*     */     
/*     */     private static boolean isGoodTarget(Level param1Level, Vec3 param1Vec3, int param1Int) {
/* 361 */       if (param1Int <= 0) {
/* 362 */         return true;
/*     */       }
/* 364 */       BlockPos blockPos = BlockPos.containing((Position)param1Vec3);
/* 365 */       if (!param1Level.getBlockState(blockPos).isAir()) {
/* 366 */         return false;
/*     */       }
/*     */       
/* 369 */       for (Direction direction : Direction.values()) {
/* 370 */         for (byte b = 1; b < param1Int; b++) {
/* 371 */           BlockPos blockPos1 = blockPos.relative(direction, b);
/* 372 */           if (!param1Level.getBlockState(blockPos1).isAir()) {
/* 373 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 377 */       return false;
/*     */     }
/*     */     
/*     */     private static Vec3 chooseRandomPosition(Vec3 param1Vec3, RandomSource param1RandomSource) {
/* 381 */       double d1 = param1Vec3.x() + ((param1RandomSource.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 382 */       double d2 = param1Vec3.y() + ((param1RandomSource.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 383 */       double d3 = param1Vec3.z() + ((param1RandomSource.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 384 */       return new Vec3(d1, d2, d3);
/*     */     }
/*     */     
/*     */     private static Vec3 chooseRandomPositionWithRestriction(Mob param1Mob, Vec3 param1Vec3, RandomSource param1RandomSource) {
/* 388 */       Vec3 vec3 = chooseRandomPosition(param1Vec3, param1RandomSource);
/* 389 */       if (param1Mob.hasHome() && !param1Mob.isWithinHome(vec3)) {
/* 390 */         return null;
/*     */       }
/* 392 */       return vec3;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GhastLookGoal extends Goal {
/*     */     private final Mob ghast;
/*     */     
/*     */     public GhastLookGoal(Mob param1Mob) {
/* 400 */       this.ghast = param1Mob;
/*     */       
/* 402 */       setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 407 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 412 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 417 */       Ghast.faceMovementDirection(this.ghast);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void faceMovementDirection(Mob paramMob) {
/* 422 */     if (paramMob.getTarget() == null) {
/* 423 */       Vec3 vec3 = paramMob.getDeltaMovement();
/* 424 */       paramMob.setYRot(-((float)Mth.atan2(vec3.x, vec3.z)) * 57.295776F);
/* 425 */       paramMob.yBodyRot = paramMob.getYRot();
/*     */     } else {
/* 427 */       LivingEntity livingEntity = paramMob.getTarget();
/*     */       
/* 429 */       double d = 64.0D;
/* 430 */       if (livingEntity.distanceToSqr((Entity)paramMob) < 4096.0D) {
/* 431 */         double d1 = livingEntity.getX() - paramMob.getX();
/* 432 */         double d2 = livingEntity.getZ() - paramMob.getZ();
/* 433 */         paramMob.setYRot(-((float)Mth.atan2(d1, d2)) * 57.295776F);
/* 434 */         paramMob.yBodyRot = paramMob.getYRot();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static class GhastShootFireballGoal extends Goal {
/*     */     private final Ghast ghast;
/*     */     public int chargeTime;
/*     */     
/*     */     public GhastShootFireballGoal(Ghast param1Ghast) {
/* 444 */       this.ghast = param1Ghast;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 449 */       return (this.ghast.getTarget() != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 454 */       this.chargeTime = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 459 */       this.ghast.setCharging(false);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 464 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 469 */       LivingEntity livingEntity = this.ghast.getTarget();
/* 470 */       if (livingEntity == null) {
/*     */         return;
/*     */       }
/*     */       
/* 474 */       double d = 64.0D;
/* 475 */       if (livingEntity.distanceToSqr((Entity)this.ghast) < 4096.0D && this.ghast.hasLineOfSight((Entity)livingEntity)) {
/* 476 */         Level level = this.ghast.level();
/*     */         
/* 478 */         this.chargeTime++;
/* 479 */         if (this.chargeTime == 10 && !this.ghast.isSilent()) {
/* 480 */           level.levelEvent(null, 1015, this.ghast.blockPosition(), 0);
/*     */         }
/* 482 */         if (this.chargeTime == 20) {
/* 483 */           double d1 = 4.0D;
/* 484 */           Vec3 vec31 = this.ghast.getViewVector(1.0F);
/*     */           
/* 486 */           double d2 = livingEntity.getX() - this.ghast.getX() + vec31.x * 4.0D;
/* 487 */           double d3 = livingEntity.getY(0.5D) - 0.5D + this.ghast.getY(0.5D);
/* 488 */           double d4 = livingEntity.getZ() - this.ghast.getZ() + vec31.z * 4.0D;
/* 489 */           Vec3 vec32 = new Vec3(d2, d3, d4);
/*     */           
/* 491 */           if (!this.ghast.isSilent()) {
/* 492 */             level.levelEvent(null, 1016, this.ghast.blockPosition(), 0);
/*     */           }
/* 494 */           LargeFireball largeFireball = new LargeFireball(level, (LivingEntity)this.ghast, vec32.normalize(), this.ghast.getExplosionPower());
/* 495 */           largeFireball.setPos(this.ghast.getX() + vec31.x * 4.0D, this.ghast.getY(0.5D) + 0.5D, largeFireball.getZ() + vec31.z * 4.0D);
/* 496 */           level.addFreshEntity((Entity)largeFireball);
/* 497 */           this.chargeTime = -40;
/*     */         } 
/* 499 */       } else if (this.chargeTime > 0) {
/* 500 */         this.chargeTime--;
/*     */       } 
/* 502 */       this.ghast.setCharging((this.chargeTime > 10));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Ghast.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */