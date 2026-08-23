/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
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
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.BiomeTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.ConversionType;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.levelgen.WorldgenRandom;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ 
/*     */ public class Slime
/*     */   extends Mob
/*     */   implements Enemy
/*     */ {
/*  55 */   private static final EntityDataAccessor<Integer> ID_SIZE = SynchedEntityData.defineId(Slime.class, EntityDataSerializers.INT);
/*     */   
/*     */   public static final int MIN_SIZE = 1;
/*     */   public static final int MAX_SIZE = 127;
/*     */   public static final int MAX_NATURAL_SIZE = 4;
/*     */   private static final boolean DEFAULT_WAS_ON_GROUND = false;
/*     */   public float targetSquish;
/*     */   public float squish;
/*     */   public float oSquish;
/*     */   private boolean wasOnGround = false;
/*     */   
/*     */   public Slime(EntityType<? extends Slime> paramEntityType, Level paramLevel) {
/*  67 */     super(paramEntityType, paramLevel);
/*     */ 
/*     */     
/*  70 */     fixupDimensions();
/*  71 */     this.moveControl = new SlimeMoveControl(this);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  76 */     this.goalSelector.addGoal(1, new SlimeFloatGoal(this));
/*     */     
/*  78 */     this.goalSelector.addGoal(2, new SlimeAttackGoal(this));
/*  79 */     this.goalSelector.addGoal(3, new SlimeRandomDirectionGoal(this));
/*     */     
/*  81 */     this.goalSelector.addGoal(5, new SlimeKeepOnJumpingGoal(this));
/*     */ 
/*     */     
/*  84 */     this.targetSelector.addGoal(1, (Goal)new NearestAttackableTargetGoal(this, Player.class, 10, true, false, (paramLivingEntity, paramServerLevel) -> (Math.abs(paramLivingEntity.getY() - getY()) <= 4.0D)));
/*  85 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal(this, IronGolem.class, true));
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/*  90 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  95 */     super.defineSynchedData(paramBuilder);
/*     */     
/*  97 */     paramBuilder.define(ID_SIZE, Integer.valueOf(1));
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setSize(int paramInt, boolean paramBoolean) {
/* 102 */     int i = Mth.clamp(paramInt, 1, 127);
/* 103 */     this.entityData.set(ID_SIZE, Integer.valueOf(i));
/* 104 */     reapplyPosition();
/*     */     
/* 106 */     refreshDimensions();
/*     */     
/* 108 */     getAttribute(Attributes.MAX_HEALTH).setBaseValue((i * i));
/* 109 */     getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue((0.2F + 0.1F * i));
/* 110 */     getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(i);
/* 111 */     if (paramBoolean) {
/* 112 */       setHealth(getMaxHealth());
/*     */     }
/* 114 */     this.xpReward = i;
/*     */   }
/*     */   
/*     */   public int getSize() {
/* 118 */     return ((Integer)this.entityData.get(ID_SIZE)).intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 123 */     super.addAdditionalSaveData(paramValueOutput);
/* 124 */     paramValueOutput.putInt("Size", getSize() - 1);
/* 125 */     paramValueOutput.putBoolean("wasOnGround", this.wasOnGround);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 130 */     setSize(paramValueInput.getIntOr("Size", 0) + 1, false);
/* 131 */     super.readAdditionalSaveData(paramValueInput);
/* 132 */     this.wasOnGround = paramValueInput.getBooleanOr("wasOnGround", false);
/*     */   }
/*     */   
/*     */   public boolean isTiny() {
/* 136 */     return (getSize() <= 1);
/*     */   }
/*     */   
/*     */   protected ParticleOptions getParticleType() {
/* 140 */     return (ParticleOptions)ParticleTypes.ITEM_SLIME;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 145 */     this.oSquish = this.squish;
/* 146 */     this.squish += (this.targetSquish - this.squish) * 0.5F;
/* 147 */     super.tick();
/*     */     
/* 149 */     if (onGround() && !this.wasOnGround) {
/* 150 */       float f1 = getDimensions(getPose()).width() * 2.0F;
/* 151 */       float f2 = f1 / 2.0F;
/* 152 */       for (byte b = 0; b < f1 * 16.0F; b++) {
/* 153 */         float f3 = this.random.nextFloat() * 6.2831855F;
/* 154 */         float f4 = this.random.nextFloat() * 0.5F + 0.5F;
/* 155 */         float f5 = Mth.sin(f3) * f2 * f4;
/* 156 */         float f6 = Mth.cos(f3) * f2 * f4;
/* 157 */         level().addParticle(getParticleType(), getX() + f5, getY(), getZ() + f6, 0.0D, 0.0D, 0.0D);
/*     */       } 
/*     */       
/* 160 */       playSound(getSquishSound(), getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) / 0.8F);
/* 161 */       this.targetSquish = -0.5F;
/* 162 */     } else if (!onGround() && this.wasOnGround) {
/* 163 */       this.targetSquish = 1.0F;
/*     */     } 
/* 165 */     this.wasOnGround = onGround();
/* 166 */     decreaseSquish();
/*     */   }
/*     */   
/*     */   protected void decreaseSquish() {
/* 170 */     this.targetSquish *= 0.6F;
/*     */   }
/*     */   
/*     */   protected int getJumpDelay() {
/* 174 */     return this.random.nextInt(20) + 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public void refreshDimensions() {
/* 179 */     double d1 = getX();
/* 180 */     double d2 = getY();
/* 181 */     double d3 = getZ();
/* 182 */     super.refreshDimensions();
/* 183 */     setPos(d1, d2, d3);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/* 188 */     if (ID_SIZE.equals(paramEntityDataAccessor)) {
/* 189 */       refreshDimensions();
/* 190 */       setYRot(this.yHeadRot);
/* 191 */       this.yBodyRot = this.yHeadRot;
/*     */       
/* 193 */       if (isInWater() && 
/* 194 */         this.random.nextInt(20) == 0) {
/* 195 */         doWaterSplashEffect();
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 200 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityType<? extends Slime> getType() {
/* 206 */     return super.getType();
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(Entity.RemovalReason paramRemovalReason) {
/* 211 */     int i = getSize();
/* 212 */     if (!level().isClientSide() && i > 1 && isDeadOrDying()) {
/* 213 */       float f1 = getDimensions(getPose()).width();
/* 214 */       float f2 = f1 / 2.0F;
/* 215 */       int j = i / 2;
/*     */       
/* 217 */       int k = 2 + this.random.nextInt(3);
/* 218 */       PlayerTeam playerTeam = getTeam();
/* 219 */       for (byte b = 0; b < k; b++) {
/* 220 */         float f3 = ((b % 2) - 0.5F) * f2;
/* 221 */         float f4 = ((b / 2) - 0.5F) * f2;
/* 222 */         convertTo(getType(), new ConversionParams(ConversionType.SPLIT_ON_DEATH, false, false, playerTeam), EntitySpawnReason.TRIGGERED, paramSlime -> {
/*     */               paramSlime.setSize(paramInt, true);
/*     */               paramSlime.snapTo(getX() + paramFloat1, getY() + 0.5D, getZ() + paramFloat2, this.random.nextFloat() * 360.0F, 0.0F);
/*     */             });
/*     */       } 
/*     */     } 
/* 228 */     super.remove(paramRemovalReason);
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(Entity paramEntity) {
/* 233 */     super.push(paramEntity);
/* 234 */     if (paramEntity instanceof IronGolem && isDealsDamage()) {
/* 235 */       dealDamage((LivingEntity)paramEntity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void playerTouch(Player paramPlayer) {
/* 241 */     if (isDealsDamage()) {
/* 242 */       dealDamage((LivingEntity)paramPlayer);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void dealDamage(LivingEntity paramLivingEntity) {
/* 247 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 248 */       if (isAlive() && 
/* 249 */         isWithinMeleeAttackRange(paramLivingEntity) && hasLineOfSight((Entity)paramLivingEntity)) {
/* 250 */         DamageSource damageSource = damageSources().mobAttack((LivingEntity)this);
/* 251 */         if (paramLivingEntity.hurtServer(serverLevel, damageSource, getAttackDamage())) {
/* 252 */           playSound(SoundEvents.SLIME_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
/* 253 */           EnchantmentHelper.doPostAttackEffects(serverLevel, (Entity)paramLivingEntity, damageSource);
/*     */         } 
/*     */       }  }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Vec3 getPassengerAttachmentPoint(Entity paramEntity, EntityDimensions paramEntityDimensions, float paramFloat) {
/* 262 */     return new Vec3(0.0D, paramEntityDimensions.height() - 0.015625D * getSize() * paramFloat, 0.0D);
/*     */   }
/*     */   
/*     */   protected boolean isDealsDamage() {
/* 266 */     return (!isTiny() && isEffectiveAi());
/*     */   }
/*     */   
/*     */   protected float getAttackDamage() {
/* 270 */     return (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 275 */     if (isTiny()) {
/* 276 */       return SoundEvents.SLIME_HURT_SMALL;
/*     */     }
/* 278 */     return SoundEvents.SLIME_HURT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 284 */     if (isTiny()) {
/* 285 */       return SoundEvents.SLIME_DEATH_SMALL;
/*     */     }
/* 287 */     return SoundEvents.SLIME_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSquishSound() {
/* 292 */     if (isTiny()) {
/* 293 */       return SoundEvents.SLIME_SQUISH_SMALL;
/*     */     }
/* 295 */     return SoundEvents.SLIME_SQUISH;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean checkSlimeSpawnRules(EntityType<Slime> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 300 */     if (paramLevelAccessor.getDifficulty() != Difficulty.PEACEFUL) {
/* 301 */       if (EntitySpawnReason.isSpawner(paramEntitySpawnReason)) {
/* 302 */         return checkMobSpawnRules(paramEntityType, paramLevelAccessor, paramEntitySpawnReason, paramBlockPos, paramRandomSource);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 307 */       if (paramLevelAccessor.getBiome(paramBlockPos).is(BiomeTags.ALLOWS_SURFACE_SLIME_SPAWNS) && paramBlockPos.getY() > 50 && paramBlockPos.getY() < 70) {
/* 308 */         float f = ((Float)paramLevelAccessor.environmentAttributes().getValue(EnvironmentAttributes.SURFACE_SLIME_SPAWN_CHANCE, paramBlockPos)).floatValue();
/* 309 */         if (paramRandomSource.nextFloat() < f && paramLevelAccessor.getMaxLocalRawBrightness(paramBlockPos) <= paramRandomSource.nextInt(8)) {
/* 310 */           return checkMobSpawnRules(paramEntityType, paramLevelAccessor, paramEntitySpawnReason, paramBlockPos, paramRandomSource);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 315 */       if (!(paramLevelAccessor instanceof WorldGenLevel)) {
/* 316 */         return false;
/*     */       }
/* 318 */       ChunkPos chunkPos = new ChunkPos(paramBlockPos);
/* 319 */       boolean bool = (WorldgenRandom.seedSlimeChunk(chunkPos.x, chunkPos.z, ((WorldGenLevel)paramLevelAccessor).getSeed(), 987234911L).nextInt(10) == 0) ? true : false;
/* 320 */       if (paramRandomSource.nextInt(10) == 0 && bool && paramBlockPos.getY() < 40) {
/* 321 */         return checkMobSpawnRules(paramEntityType, paramLevelAccessor, paramEntitySpawnReason, paramBlockPos, paramRandomSource);
/*     */       }
/*     */     } 
/* 324 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 329 */     return 0.4F * getSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxHeadXRot() {
/* 334 */     return 0;
/*     */   }
/*     */   
/*     */   protected boolean doPlayJumpSound() {
/* 338 */     return (getSize() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void jumpFromGround() {
/* 343 */     Vec3 vec3 = getDeltaMovement();
/* 344 */     setDeltaMovement(vec3.x, getJumpPower(), vec3.z);
/* 345 */     this.needsSync = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 350 */     RandomSource randomSource = paramServerLevelAccessor.getRandom();
/* 351 */     int i = randomSource.nextInt(3);
/* 352 */     if (i < 2 && randomSource.nextFloat() < 0.5F * paramDifficultyInstance.getSpecialMultiplier()) {
/* 353 */       i++;
/*     */     }
/* 355 */     int j = 1 << i;
/* 356 */     setSize(j, true);
/*     */     
/* 358 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */   }
/*     */   
/*     */   private static class SlimeMoveControl extends MoveControl {
/*     */     private float yRot;
/*     */     private int jumpDelay;
/*     */     private final Slime slime;
/*     */     private boolean isAggressive;
/*     */     
/*     */     public SlimeMoveControl(Slime param1Slime) {
/* 368 */       super(param1Slime);
/* 369 */       this.slime = param1Slime;
/* 370 */       this.yRot = 180.0F * param1Slime.getYRot() / 3.1415927F;
/*     */     }
/*     */     
/*     */     public void setDirection(float param1Float, boolean param1Boolean) {
/* 374 */       this.yRot = param1Float;
/* 375 */       this.isAggressive = param1Boolean;
/*     */     }
/*     */     
/*     */     public void setWantedMovement(double param1Double) {
/* 379 */       this.speedModifier = param1Double;
/* 380 */       this.operation = MoveControl.Operation.MOVE_TO;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 385 */       this.mob.setYRot(rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
/* 386 */       this.mob.yHeadRot = this.mob.getYRot();
/* 387 */       this.mob.yBodyRot = this.mob.getYRot();
/*     */       
/* 389 */       if (this.operation != MoveControl.Operation.MOVE_TO) {
/* 390 */         this.mob.setZza(0.0F);
/*     */         return;
/*     */       } 
/* 393 */       this.operation = MoveControl.Operation.WAIT;
/*     */       
/* 395 */       if (this.mob.onGround()) {
/* 396 */         this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
/* 397 */         if (this.jumpDelay-- <= 0) {
/* 398 */           this.jumpDelay = this.slime.getJumpDelay();
/* 399 */           if (this.isAggressive) {
/* 400 */             this.jumpDelay /= 3;
/*     */           }
/* 402 */           this.slime.getJumpControl().jump();
/* 403 */           if (this.slime.doPlayJumpSound()) {
/* 404 */             this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getSoundPitch());
/*     */           }
/*     */         } else {
/* 407 */           this.slime.xxa = 0.0F;
/* 408 */           this.slime.zza = 0.0F;
/* 409 */           this.mob.setSpeed(0.0F);
/*     */         } 
/*     */       } else {
/* 412 */         this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   float getSoundPitch() {
/* 418 */     float f = isTiny() ? 1.4F : 0.8F;
/* 419 */     return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * f;
/*     */   }
/*     */   
/*     */   protected SoundEvent getJumpSound() {
/* 423 */     return isTiny() ? SoundEvents.SLIME_JUMP_SMALL : SoundEvents.SLIME_JUMP;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose paramPose) {
/* 428 */     return super.getDefaultDimensions(paramPose).scale(getSize());
/*     */   }
/*     */   
/*     */   private static class SlimeAttackGoal extends Goal {
/*     */     private final Slime slime;
/*     */     private int growTiredTimer;
/*     */     
/*     */     public SlimeAttackGoal(Slime param1Slime) {
/* 436 */       this.slime = param1Slime;
/* 437 */       setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 442 */       LivingEntity livingEntity = this.slime.getTarget();
/*     */       
/* 444 */       if (livingEntity == null) {
/* 445 */         return false;
/*     */       }
/*     */       
/* 448 */       if (!this.slime.canAttack(livingEntity)) {
/* 449 */         return false;
/*     */       }
/*     */       
/* 452 */       return this.slime.getMoveControl() instanceof Slime.SlimeMoveControl;
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 457 */       this.growTiredTimer = reducedTickDelay(300);
/* 458 */       super.start();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 463 */       LivingEntity livingEntity = this.slime.getTarget();
/*     */       
/* 465 */       if (livingEntity == null) {
/* 466 */         return false;
/*     */       }
/*     */       
/* 469 */       if (!this.slime.canAttack(livingEntity)) {
/* 470 */         return false;
/*     */       }
/*     */       
/* 473 */       if (--this.growTiredTimer <= 0) {
/* 474 */         return false;
/*     */       }
/*     */       
/* 477 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 482 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 487 */       LivingEntity livingEntity = this.slime.getTarget();
/* 488 */       if (livingEntity != null) {
/* 489 */         this.slime.lookAt((Entity)livingEntity, 10.0F, 10.0F);
/*     */       }
/* 491 */       MoveControl moveControl = this.slime.getMoveControl(); if (moveControl instanceof Slime.SlimeMoveControl) { Slime.SlimeMoveControl slimeMoveControl = (Slime.SlimeMoveControl)moveControl;
/* 492 */         slimeMoveControl.setDirection(this.slime.getYRot(), this.slime.isDealsDamage()); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SlimeRandomDirectionGoal
/*     */     extends Goal {
/*     */     private final Slime slime;
/*     */     private float chosenDegrees;
/*     */     private int nextRandomizeTime;
/*     */     
/*     */     public SlimeRandomDirectionGoal(Slime param1Slime) {
/* 504 */       this.slime = param1Slime;
/* 505 */       setFlags(EnumSet.of(Goal.Flag.LOOK));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 510 */       return (this.slime.getTarget() == null && (this.slime.onGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION)) && this.slime.getMoveControl() instanceof Slime.SlimeMoveControl);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 515 */       if (--this.nextRandomizeTime <= 0) {
/* 516 */         this.nextRandomizeTime = adjustedTickDelay(40 + this.slime.getRandom().nextInt(60));
/* 517 */         this.chosenDegrees = this.slime.getRandom().nextInt(360);
/*     */       } 
/* 519 */       MoveControl moveControl = this.slime.getMoveControl(); if (moveControl instanceof Slime.SlimeMoveControl) { Slime.SlimeMoveControl slimeMoveControl = (Slime.SlimeMoveControl)moveControl;
/* 520 */         slimeMoveControl.setDirection(this.chosenDegrees, false); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SlimeFloatGoal extends Goal {
/*     */     private final Slime slime;
/*     */     
/*     */     public SlimeFloatGoal(Slime param1Slime) {
/* 529 */       this.slime = param1Slime;
/* 530 */       setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/* 531 */       param1Slime.getNavigation().setCanFloat(true);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 536 */       return ((this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof Slime.SlimeMoveControl);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean requiresUpdateEveryTick() {
/* 541 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 546 */       if (this.slime.getRandom().nextFloat() < 0.8F) {
/* 547 */         this.slime.getJumpControl().jump();
/*     */       }
/* 549 */       MoveControl moveControl = this.slime.getMoveControl(); if (moveControl instanceof Slime.SlimeMoveControl) { Slime.SlimeMoveControl slimeMoveControl = (Slime.SlimeMoveControl)moveControl;
/* 550 */         slimeMoveControl.setWantedMovement(1.2D); }
/*     */     
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SlimeKeepOnJumpingGoal extends Goal {
/*     */     private final Slime slime;
/*     */     
/*     */     public SlimeKeepOnJumpingGoal(Slime param1Slime) {
/* 559 */       this.slime = param1Slime;
/* 560 */       setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 565 */       return !this.slime.isPassenger();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 570 */       MoveControl moveControl = this.slime.getMoveControl(); if (moveControl instanceof Slime.SlimeMoveControl) { Slime.SlimeMoveControl slimeMoveControl = (Slime.SlimeMoveControl)moveControl;
/* 571 */         slimeMoveControl.setWantedMovement(1.0D); }
/*     */     
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Slime.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */