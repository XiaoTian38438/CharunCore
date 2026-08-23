/*     */ package net.minecraft.world.entity.animal.turtle;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ExperienceOrb;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.goal.BreedGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.TemptGoal;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.util.DefaultRandomPos;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.TurtleEggBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class Turtle extends Animal {
/*  68 */   private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
/*  69 */   private static final EntityDataAccessor<Boolean> LAYING_EGG = SynchedEntityData.defineId(Turtle.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final float BABY_SCALE = 0.3F;
/*  72 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.TURTLE.getDimensions()
/*  73 */     .withAttachments(EntityAttachments.builder()
/*  74 */       .attach(EntityAttachment.PASSENGER, 0.0F, EntityType.TURTLE.getHeight(), -0.25F))
/*     */     
/*  76 */     .scale(0.3F);
/*     */   private static final boolean DEFAULT_HAS_EGG = false;
/*     */   int layEggCounter;
/*     */   public static final TargetingConditions.Selector BABY_ON_LAND_SELECTOR;
/*     */   
/*     */   static {
/*  82 */     BABY_ON_LAND_SELECTOR = ((paramLivingEntity, paramServerLevel) -> (paramLivingEntity.isBaby() && !paramLivingEntity.isInWater()));
/*     */   }
/*  84 */   BlockPos homePos = BlockPos.ZERO;
/*     */   BlockPos travelPos;
/*     */   boolean goingHome;
/*     */   
/*     */   public Turtle(EntityType<? extends Turtle> paramEntityType, Level paramLevel) {
/*  89 */     super(paramEntityType, paramLevel);
/*     */     
/*  91 */     setPathfindingMalus(PathType.WATER, 0.0F);
/*  92 */     setPathfindingMalus(PathType.DOOR_IRON_CLOSED, -1.0F);
/*  93 */     setPathfindingMalus(PathType.DOOR_WOOD_CLOSED, -1.0F);
/*  94 */     setPathfindingMalus(PathType.DOOR_OPEN, -1.0F);
/*  95 */     this.moveControl = new TurtleMoveControl(this);
/*     */   }
/*     */   
/*     */   public void setHomePos(BlockPos paramBlockPos) {
/*  99 */     this.homePos = paramBlockPos;
/*     */   }
/*     */   
/*     */   public boolean hasEgg() {
/* 103 */     return ((Boolean)this.entityData.get(HAS_EGG)).booleanValue();
/*     */   }
/*     */   
/*     */   void setHasEgg(boolean paramBoolean) {
/* 107 */     this.entityData.set(HAS_EGG, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */   
/*     */   public boolean isLayingEgg() {
/* 111 */     return ((Boolean)this.entityData.get(LAYING_EGG)).booleanValue();
/*     */   }
/*     */   
/*     */   void setLayingEgg(boolean paramBoolean) {
/* 115 */     this.layEggCounter = paramBoolean ? 1 : 0;
/* 116 */     this.entityData.set(LAYING_EGG, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 121 */     super.defineSynchedData(paramBuilder);
/* 122 */     paramBuilder.define(HAS_EGG, Boolean.valueOf(false));
/* 123 */     paramBuilder.define(LAYING_EGG, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 128 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/* 130 */     paramValueOutput.store("home_pos", BlockPos.CODEC, this.homePos);
/*     */     
/* 132 */     paramValueOutput.putBoolean("has_egg", hasEgg());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 137 */     setHomePos(paramValueInput.read("home_pos", BlockPos.CODEC).orElse(blockPosition()));
/*     */     
/* 139 */     super.readAdditionalSaveData(paramValueInput);
/* 140 */     setHasEgg(paramValueInput.getBooleanOr("has_egg", false));
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 145 */     setHomePos(blockPosition());
/* 146 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */   }
/*     */   
/*     */   public static boolean checkTurtleSpawnRules(EntityType<Turtle> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 150 */     return (paramBlockPos.getY() < paramLevelAccessor.getSeaLevel() + 4 && 
/* 151 */       TurtleEggBlock.onSand((BlockGetter)paramLevelAccessor, paramBlockPos) && 
/* 152 */       isBrightEnoughToSpawn((BlockAndTintGetter)paramLevelAccessor, paramBlockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/* 157 */     this.goalSelector.addGoal(0, (Goal)new TurtlePanicGoal(this, 1.2D));
/* 158 */     this.goalSelector.addGoal(1, (Goal)new TurtleBreedGoal(this, 1.0D));
/* 159 */     this.goalSelector.addGoal(1, (Goal)new TurtleLayEggGoal(this, 1.0D));
/* 160 */     this.goalSelector.addGoal(2, (Goal)new TemptGoal((PathfinderMob)this, 1.1D, paramItemStack -> paramItemStack.is(ItemTags.TURTLE_FOOD), false));
/* 161 */     this.goalSelector.addGoal(3, (Goal)new TurtleGoToWaterGoal(this, 1.0D));
/* 162 */     this.goalSelector.addGoal(4, new TurtleGoHomeGoal(this, 1.0D));
/* 163 */     this.goalSelector.addGoal(7, new TurtleTravelGoal(this, 1.0D));
/* 164 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 165 */     this.goalSelector.addGoal(9, (Goal)new TurtleRandomStrollGoal(this, 1.0D, 100));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 169 */     return Animal.createAnimalAttributes()
/* 170 */       .add(Attributes.MAX_HEALTH, 30.0D)
/* 171 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/* 172 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPushedByFluid() {
/* 177 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAmbientSoundInterval() {
/* 182 */     return 200;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 187 */     if (!isInWater() && onGround() && !isBaby()) {
/* 188 */       return SoundEvents.TURTLE_AMBIENT_LAND;
/*     */     }
/*     */     
/* 191 */     return super.getAmbientSound();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playSwimSound(float paramFloat) {
/* 196 */     super.playSwimSound(paramFloat * 1.5F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 201 */     return SoundEvents.TURTLE_SWIM;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 206 */     if (isBaby()) {
/* 207 */       return SoundEvents.TURTLE_HURT_BABY;
/*     */     }
/* 209 */     return SoundEvents.TURTLE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 214 */     if (isBaby()) {
/* 215 */       return SoundEvents.TURTLE_DEATH_BABY;
/*     */     }
/* 217 */     return SoundEvents.TURTLE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 222 */     SoundEvent soundEvent = isBaby() ? SoundEvents.TURTLE_SHAMBLE_BABY : SoundEvents.TURTLE_SHAMBLE;
/*     */     
/* 224 */     playSound(soundEvent, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canFallInLove() {
/* 229 */     return (super.canFallInLove() && !hasEgg());
/*     */   }
/*     */ 
/*     */   
/*     */   protected float nextStep() {
/* 234 */     return this.moveDist + 0.15F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAgeScale() {
/* 239 */     return isBaby() ? 0.3F : 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level paramLevel) {
/* 244 */     return (PathNavigation)new TurtlePathNavigation(this, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel paramServerLevel, AgeableMob paramAgeableMob) {
/* 249 */     return (AgeableMob)EntityType.TURTLE.create((Level)paramServerLevel, EntitySpawnReason.BREEDING);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack paramItemStack) {
/* 254 */     return paramItemStack.is(ItemTags.TURTLE_FOOD);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos paramBlockPos, LevelReader paramLevelReader) {
/* 259 */     if (!this.goingHome && paramLevelReader.getFluidState(paramBlockPos).is(FluidTags.WATER)) {
/* 260 */       return 10.0F;
/*     */     }
/*     */     
/* 263 */     if (TurtleEggBlock.onSand((BlockGetter)paramLevelReader, paramBlockPos)) {
/* 264 */       return 10.0F;
/*     */     }
/*     */     
/* 267 */     return paramLevelReader.getPathfindingCostFromLightLevels(paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 272 */     super.aiStep();
/*     */     
/* 274 */     if (isAlive() && isLayingEgg() && this.layEggCounter >= 1 && this.layEggCounter % 5 == 0) {
/* 275 */       BlockPos blockPos = blockPosition();
/* 276 */       if (TurtleEggBlock.onSand((BlockGetter)level(), blockPos)) {
/* 277 */         level().levelEvent(2001, blockPos, Block.getId(level().getBlockState(blockPos.below())));
/* 278 */         gameEvent((Holder)GameEvent.ENTITY_ACTION);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 285 */     super.ageBoundaryReached();
/*     */     
/* 287 */     if (!isBaby()) { Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; if (((Boolean)serverLevel.getGameRules().get(GameRules.MOB_DROPS)).booleanValue())
/* 288 */           dropFromGiftLootTable(serverLevel, BuiltInLootTables.TURTLE_GROW, this::spawnAtLocation);  }
/*     */        }
/*     */   
/*     */   }
/*     */   
/*     */   protected void travelInWater(Vec3 paramVec3, double paramDouble1, boolean paramBoolean, double paramDouble2) {
/* 294 */     moveRelative(0.1F, paramVec3);
/* 295 */     move(MoverType.SELF, getDeltaMovement());
/*     */     
/* 297 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/* 298 */     if (getTarget() == null && (!this.goingHome || !this.homePos.closerToCenterThan((Position)position(), 20.0D))) {
/* 299 */       setDeltaMovement(getDeltaMovement().add(0.0D, -0.005D, 0.0D));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeLeashed() {
/* 305 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void thunderHit(ServerLevel paramServerLevel, LightningBolt paramLightningBolt) {
/* 310 */     hurtServer(paramServerLevel, damageSources().lightningBolt(), Float.MAX_VALUE);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose paramPose) {
/* 315 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(paramPose);
/*     */   }
/*     */   
/*     */   private static class TurtlePanicGoal extends PanicGoal {
/*     */     TurtlePanicGoal(Turtle param1Turtle, double param1Double) {
/* 320 */       super((PathfinderMob)param1Turtle, param1Double);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 325 */       if (!shouldPanic()) {
/* 326 */         return false;
/*     */       }
/*     */       
/* 329 */       BlockPos blockPos = lookForWater((BlockGetter)this.mob.level(), (Entity)this.mob, 7);
/* 330 */       if (blockPos != null) {
/* 331 */         this.posX = blockPos.getX();
/* 332 */         this.posY = blockPos.getY();
/* 333 */         this.posZ = blockPos.getZ();
/*     */         
/* 335 */         return true;
/*     */       } 
/*     */       
/* 338 */       return findRandomPosition();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleTravelGoal extends Goal {
/*     */     private final Turtle turtle;
/*     */     private final double speedModifier;
/*     */     private boolean stuck;
/*     */     
/*     */     TurtleTravelGoal(Turtle param1Turtle, double param1Double) {
/* 348 */       this.turtle = param1Turtle;
/* 349 */       this.speedModifier = param1Double;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 354 */       return (!this.turtle.goingHome && !this.turtle.hasEgg() && this.turtle.isInWater());
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 359 */       char c = 'Ȁ';
/* 360 */       byte b = 4;
/* 361 */       RandomSource randomSource = this.turtle.random;
/* 362 */       int i = randomSource.nextInt(1025) - 512;
/* 363 */       int j = randomSource.nextInt(9) - 4;
/* 364 */       int k = randomSource.nextInt(1025) - 512;
/*     */       
/* 366 */       if (j + this.turtle.getY() > (this.turtle.level().getSeaLevel() - 1)) {
/* 367 */         j = 0;
/*     */       }
/* 369 */       this.turtle.travelPos = BlockPos.containing(i + this.turtle.getX(), j + this.turtle.getY(), k + this.turtle.getZ());
/* 370 */       this.stuck = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 375 */       if (this.turtle.travelPos == null) {
/* 376 */         this.stuck = true;
/*     */         
/*     */         return;
/*     */       } 
/* 380 */       if (this.turtle.getNavigation().isDone()) {
/* 381 */         Vec3 vec31 = Vec3.atBottomCenterOf((Vec3i)this.turtle.travelPos);
/* 382 */         Vec3 vec32 = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 3, vec31, 0.3141592741012573D);
/* 383 */         if (vec32 == null) {
/* 384 */           vec32 = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 8, 7, vec31, 1.5707963705062866D);
/*     */         }
/*     */ 
/*     */         
/* 388 */         if (vec32 != null) {
/* 389 */           int i = Mth.floor(vec32.x);
/* 390 */           int j = Mth.floor(vec32.z);
/* 391 */           byte b = 34;
/* 392 */           if (!this.turtle.level().hasChunksAt(i - 34, j - 34, i + 34, j + 34)) {
/* 393 */             vec32 = null;
/*     */           }
/*     */         } 
/*     */         
/* 397 */         if (vec32 == null) {
/* 398 */           this.stuck = true;
/*     */           
/*     */           return;
/*     */         } 
/* 402 */         this.turtle.getNavigation().moveTo(vec32.x, vec32.y, vec32.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 408 */       return (!this.turtle.getNavigation().isDone() && !this.stuck && !this.turtle.goingHome && !this.turtle.isInLove() && !this.turtle.hasEgg());
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 413 */       this.turtle.travelPos = null;
/* 414 */       super.stop();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleGoHomeGoal extends Goal {
/*     */     private final Turtle turtle;
/*     */     private final double speedModifier;
/*     */     private boolean stuck;
/*     */     private int closeToHomeTryTicks;
/*     */     private static final int GIVE_UP_TICKS = 600;
/*     */     
/*     */     TurtleGoHomeGoal(Turtle param1Turtle, double param1Double) {
/* 426 */       this.turtle = param1Turtle;
/* 427 */       this.speedModifier = param1Double;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 432 */       if (this.turtle.isBaby()) {
/* 433 */         return false;
/*     */       }
/*     */       
/* 436 */       if (this.turtle.hasEgg()) {
/* 437 */         return true;
/*     */       }
/*     */       
/* 440 */       if (this.turtle.getRandom().nextInt(reducedTickDelay(700)) != 0) {
/* 441 */         return false;
/*     */       }
/*     */       
/* 444 */       return !this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 64.0D);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 449 */       this.turtle.goingHome = true;
/* 450 */       this.stuck = false;
/* 451 */       this.closeToHomeTryTicks = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void stop() {
/* 456 */       this.turtle.goingHome = false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 461 */       return (!this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 7.0D) && !this.stuck && this.closeToHomeTryTicks <= adjustedTickDelay(600));
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 466 */       BlockPos blockPos = this.turtle.homePos;
/* 467 */       boolean bool = blockPos.closerToCenterThan((Position)this.turtle.position(), 16.0D);
/* 468 */       if (bool) {
/* 469 */         this.closeToHomeTryTicks++;
/*     */       }
/*     */       
/* 472 */       if (this.turtle.getNavigation().isDone()) {
/* 473 */         Vec3 vec31 = Vec3.atBottomCenterOf((Vec3i)blockPos);
/* 474 */         Vec3 vec32 = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 3, vec31, 0.3141592741012573D);
/* 475 */         if (vec32 == null) {
/* 476 */           vec32 = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 8, 7, vec31, 1.5707963705062866D);
/*     */         }
/*     */         
/* 479 */         if (vec32 != null && !bool && !this.turtle.level().getBlockState(BlockPos.containing((Position)vec32)).is(Blocks.WATER))
/*     */         {
/* 481 */           vec32 = DefaultRandomPos.getPosTowards((PathfinderMob)this.turtle, 16, 5, vec31, 1.5707963705062866D);
/*     */         }
/*     */         
/* 484 */         if (vec32 == null) {
/* 485 */           this.stuck = true;
/*     */           
/*     */           return;
/*     */         } 
/* 489 */         this.turtle.getNavigation().moveTo(vec32.x, vec32.y, vec32.z, this.speedModifier);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleBreedGoal extends BreedGoal {
/*     */     private final Turtle turtle;
/*     */     
/*     */     TurtleBreedGoal(Turtle param1Turtle, double param1Double) {
/* 498 */       super(param1Turtle, param1Double);
/* 499 */       this.turtle = param1Turtle;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 504 */       return (super.canUse() && !this.turtle.hasEgg());
/*     */     }
/*     */ 
/*     */     
/*     */     protected void breed() {
/* 509 */       ServerPlayer serverPlayer = this.animal.getLoveCause();
/* 510 */       if (serverPlayer == null && this.partner.getLoveCause() != null) {
/* 511 */         serverPlayer = this.partner.getLoveCause();
/*     */       }
/*     */       
/* 514 */       if (serverPlayer != null) {
/* 515 */         serverPlayer.awardStat(Stats.ANIMALS_BRED);
/* 516 */         CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayer, this.animal, this.partner, null);
/*     */       } 
/*     */       
/* 519 */       this.turtle.setHasEgg(true);
/* 520 */       this.animal.setAge(6000);
/* 521 */       this.partner.setAge(6000);
/* 522 */       this.animal.resetLove();
/* 523 */       this.partner.resetLove();
/*     */       
/* 525 */       RandomSource randomSource = this.animal.getRandom();
/* 526 */       if (((Boolean)getServerLevel((Level)this.level).getGameRules().get(GameRules.MOB_DROPS)).booleanValue())
/* 527 */         this.level.addFreshEntity((Entity)new ExperienceOrb((Level)this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), randomSource.nextInt(7) + 1)); 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleLayEggGoal
/*     */     extends MoveToBlockGoal {
/*     */     private final Turtle turtle;
/*     */     
/*     */     TurtleLayEggGoal(Turtle param1Turtle, double param1Double) {
/* 536 */       super((PathfinderMob)param1Turtle, param1Double, 16);
/* 537 */       this.turtle = param1Turtle;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 542 */       if (this.turtle.hasEgg() && this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 9.0D)) {
/* 543 */         return super.canUse();
/*     */       }
/* 545 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 550 */       return (super.canContinueToUse() && this.turtle.hasEgg() && this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 9.0D));
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 555 */       super.tick();
/*     */       
/* 557 */       BlockPos blockPos = this.turtle.blockPosition();
/* 558 */       if (!this.turtle.isInWater() && isReachedTarget()) {
/* 559 */         if (this.turtle.layEggCounter < 1) {
/* 560 */           this.turtle.setLayingEgg(true);
/* 561 */         } else if (this.turtle.layEggCounter > adjustedTickDelay(200)) {
/* 562 */           Level level = this.turtle.level();
/* 563 */           level.playSound(null, blockPos, SoundEvents.TURTLE_LAY_EGG, SoundSource.BLOCKS, 0.3F, 0.9F + level.random.nextFloat() * 0.2F);
/* 564 */           BlockPos blockPos1 = this.blockPos.above();
/* 565 */           BlockState blockState = (BlockState)Blocks.TURTLE_EGG.defaultBlockState().setValue((Property)TurtleEggBlock.EGGS, Integer.valueOf(this.turtle.random.nextInt(4) + 1));
/* 566 */           level.setBlock(blockPos1, blockState, 3);
/* 567 */           level.gameEvent((Holder)GameEvent.BLOCK_PLACE, blockPos1, GameEvent.Context.of((Entity)this.turtle, blockState));
/* 568 */           this.turtle.setHasEgg(false);
/* 569 */           this.turtle.setLayingEgg(false);
/* 570 */           this.turtle.setInLoveTime(600);
/*     */         } 
/* 572 */         if (this.turtle.isLayingEgg()) {
/* 573 */           this.turtle.layEggCounter++;
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isValidTarget(LevelReader param1LevelReader, BlockPos param1BlockPos) {
/* 580 */       if (!param1LevelReader.isEmptyBlock(param1BlockPos.above())) {
/* 581 */         return false;
/*     */       }
/*     */       
/* 584 */       return TurtleEggBlock.isSand((BlockGetter)param1LevelReader, param1BlockPos);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleRandomStrollGoal extends RandomStrollGoal {
/*     */     private final Turtle turtle;
/*     */     
/*     */     TurtleRandomStrollGoal(Turtle param1Turtle, double param1Double, int param1Int) {
/* 592 */       super((PathfinderMob)param1Turtle, param1Double, param1Int);
/* 593 */       this.turtle = param1Turtle;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 598 */       if (!this.mob.isInWater() && !this.turtle.goingHome && !this.turtle.hasEgg()) {
/* 599 */         return super.canUse();
/*     */       }
/*     */       
/* 602 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleGoToWaterGoal
/*     */     extends MoveToBlockGoal {
/*     */     private static final int GIVE_UP_TICKS = 1200;
/*     */     private final Turtle turtle;
/*     */     
/*     */     TurtleGoToWaterGoal(Turtle param1Turtle, double param1Double) {
/* 612 */       super((PathfinderMob)param1Turtle, param1Turtle.isBaby() ? 2.0D : param1Double, 24);
/* 613 */       this.turtle = param1Turtle;
/* 614 */       this.verticalSearchStart = -1;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 619 */       return (!this.turtle.isInWater() && this.tryTicks <= 1200 && isValidTarget((LevelReader)this.turtle.level(), this.blockPos));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 624 */       if (this.turtle.isBaby() && !this.turtle.isInWater()) {
/* 625 */         return super.canUse();
/*     */       }
/*     */       
/* 628 */       if (!this.turtle.goingHome && !this.turtle.isInWater() && !this.turtle.hasEgg()) {
/* 629 */         return super.canUse();
/*     */       }
/*     */       
/* 632 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldRecalculatePath() {
/* 637 */       return (this.tryTicks % 160 == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean isValidTarget(LevelReader param1LevelReader, BlockPos param1BlockPos) {
/* 642 */       return param1LevelReader.getBlockState(param1BlockPos).is(Blocks.WATER);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtleMoveControl extends MoveControl {
/*     */     private final Turtle turtle;
/*     */     
/*     */     TurtleMoveControl(Turtle param1Turtle) {
/* 650 */       super((Mob)param1Turtle);
/* 651 */       this.turtle = param1Turtle;
/*     */     }
/*     */     
/*     */     private void updateSpeed() {
/* 655 */       if (this.turtle.isInWater()) {
/*     */         
/* 657 */         this.turtle.setDeltaMovement(this.turtle.getDeltaMovement().add(0.0D, 0.005D, 0.0D));
/*     */         
/* 659 */         if (!this.turtle.homePos.closerToCenterThan((Position)this.turtle.position(), 16.0D)) {
/* 660 */           this.turtle.setSpeed(Math.max(this.turtle.getSpeed() / 2.0F, 0.08F));
/*     */         }
/*     */         
/* 663 */         if (this.turtle.isBaby()) {
/* 664 */           this.turtle.setSpeed(Math.max(this.turtle.getSpeed() / 3.0F, 0.06F));
/*     */         }
/* 666 */       } else if (this.turtle.onGround()) {
/* 667 */         this.turtle.setSpeed(Math.max(this.turtle.getSpeed() / 2.0F, 0.06F));
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 673 */       updateSpeed();
/*     */       
/* 675 */       if (this.operation != MoveControl.Operation.MOVE_TO || this.turtle.getNavigation().isDone()) {
/* 676 */         this.turtle.setSpeed(0.0F);
/*     */         
/*     */         return;
/*     */       } 
/* 680 */       double d1 = this.wantedX - this.turtle.getX();
/* 681 */       double d2 = this.wantedY - this.turtle.getY();
/* 682 */       double d3 = this.wantedZ - this.turtle.getZ();
/* 683 */       double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
/* 684 */       if (d4 < 9.999999747378752E-6D) {
/* 685 */         this.mob.setSpeed(0.0F);
/*     */         
/*     */         return;
/*     */       } 
/* 689 */       d2 /= d4;
/*     */       
/* 691 */       float f1 = (float)(Mth.atan2(d3, d1) * 57.2957763671875D) - 90.0F;
/* 692 */       this.turtle.setYRot(rotlerp(this.turtle.getYRot(), f1, 90.0F));
/* 693 */       this.turtle.yBodyRot = this.turtle.getYRot();
/*     */       
/* 695 */       float f2 = (float)(this.speedModifier * this.turtle.getAttributeValue(Attributes.MOVEMENT_SPEED));
/* 696 */       this.turtle.setSpeed(Mth.lerp(0.125F, this.turtle.getSpeed(), f2));
/*     */       
/* 698 */       this.turtle.setDeltaMovement(this.turtle.getDeltaMovement().add(0.0D, this.turtle.getSpeed() * d2 * 0.1D, 0.0D));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TurtlePathNavigation extends AmphibiousPathNavigation {
/*     */     TurtlePathNavigation(Turtle param1Turtle, Level param1Level) {
/* 704 */       super((Mob)param1Turtle, param1Level);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isStableDestination(BlockPos param1BlockPos) {
/* 709 */       Mob mob = this.mob; if (mob instanceof Turtle) { Turtle turtle = (Turtle)mob;
/* 710 */         if (turtle.travelPos != null) {
/* 711 */           return this.level.getBlockState(param1BlockPos).is(Blocks.WATER);
/*     */         } }
/*     */ 
/*     */       
/* 715 */       return !this.level.getBlockState(param1BlockPos.below()).isAir();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\turtle\Turtle.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */