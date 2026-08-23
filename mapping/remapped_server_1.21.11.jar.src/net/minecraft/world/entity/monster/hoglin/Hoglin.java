/*     */ package net.minecraft.world.entity.monster.hoglin;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.monster.Enemy;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.Zoglin;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ public class Hoglin
/*     */   extends Animal
/*     */   implements Enemy, HoglinBase
/*     */ {
/*  60 */   private static final EntityDataAccessor<Boolean> DATA_IMMUNE_TO_ZOMBIFICATION = SynchedEntityData.defineId(Hoglin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int MAX_HEALTH = 40;
/*     */   
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.3F;
/*     */   private static final int ATTACK_KNOCKBACK = 1;
/*     */   private static final float KNOCKBACK_RESISTANCE = 0.6F;
/*     */   private static final int ATTACK_DAMAGE = 6;
/*     */   private static final float BABY_ATTACK_DAMAGE = 0.5F;
/*     */   private static final boolean DEFAULT_IMMUNE_TO_ZOMBIFICATION = false;
/*     */   private static final int DEFAULT_TIME_IN_OVERWORLD = 0;
/*     */   private static final boolean DEFAULT_CANNOT_BE_HUNTED = false;
/*     */   public static final int CONVERSION_TIME = 300;
/*     */   private int attackAnimationRemainingTicks;
/*  74 */   private int timeInOverworld = 0;
/*     */   
/*     */   private boolean cannotBeHunted = false;
/*  77 */   protected static final ImmutableList<? extends SensorType<? extends Sensor<? super Hoglin>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ADULT, SensorType.HOGLIN_SPECIFIC_SENSOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.BREED_TARGET, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, (Object[])new MemoryModuleType[] { MemoryModuleType.AVOID_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.PACIFIED, MemoryModuleType.IS_PANICKING });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Hoglin(EntityType<? extends Hoglin> paramEntityType, Level paramLevel) {
/* 107 */     super(paramEntityType, paramLevel);
/* 108 */     this.xpReward = 5;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public void setTimeInOverworld(int paramInt) {
/* 113 */     this.timeInOverworld = paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeLeashed() {
/* 118 */     return true;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 122 */     return Monster.createMonsterAttributes()
/* 123 */       .add(Attributes.MAX_HEALTH, 40.0D)
/* 124 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 125 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.6000000238418579D)
/* 126 */       .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
/* 127 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*     */   }
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel paramServerLevel, Entity paramEntity) {
/*     */     LivingEntity livingEntity;
/* 132 */     if (paramEntity instanceof LivingEntity) { livingEntity = (LivingEntity)paramEntity; }
/* 133 */     else { return false; }
/*     */     
/* 135 */     this.attackAnimationRemainingTicks = 10;
/* 136 */     level().broadcastEntityEvent((Entity)this, (byte)4);
/*     */     
/* 138 */     makeSound(SoundEvents.HOGLIN_ATTACK);
/* 139 */     HoglinAi.onHitTarget(this, livingEntity);
/* 140 */     return HoglinBase.hurtAndThrowTarget(paramServerLevel, (LivingEntity)this, livingEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void blockedByItem(LivingEntity paramLivingEntity) {
/* 145 */     if (isAdult()) {
/* 146 */       HoglinBase.throwTarget((LivingEntity)this, paramLivingEntity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 152 */     boolean bool = super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/* 153 */     if (bool) { Entity entity = paramDamageSource.getEntity(); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 154 */         HoglinAi.wasHurtBy(paramServerLevel, this, livingEntity); }
/*     */        }
/* 156 */      return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<Hoglin> brainProvider() {
/* 161 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> paramDynamic) {
/* 166 */     return HoglinAi.makeBrain(brainProvider().makeBrain(paramDynamic));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<Hoglin> getBrain() {
/* 172 */     return super.getBrain();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/* 177 */     ProfilerFiller profilerFiller = Profiler.get();
/* 178 */     profilerFiller.push("hoglinBrain");
/* 179 */     getBrain().tick(paramServerLevel, (LivingEntity)this);
/* 180 */     profilerFiller.pop();
/*     */     
/* 182 */     HoglinAi.updateActivity(this);
/*     */     
/* 184 */     if (isConverting()) {
/* 185 */       this.timeInOverworld++;
/* 186 */       if (this.timeInOverworld > 300) {
/* 187 */         makeSound(SoundEvents.HOGLIN_CONVERTED_TO_ZOMBIFIED);
/* 188 */         finishConversion();
/*     */       } 
/*     */     } else {
/* 191 */       this.timeInOverworld = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 198 */     if (this.attackAnimationRemainingTicks > 0) {
/* 199 */       this.attackAnimationRemainingTicks--;
/*     */     }
/* 201 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void ageBoundaryReached() {
/* 206 */     if (isBaby()) {
/* 207 */       this.xpReward = 3;
/* 208 */       getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5D);
/*     */     } else {
/* 210 */       this.xpReward = 5;
/* 211 */       getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(6.0D);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean checkHoglinSpawnRules(EntityType<Hoglin> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 216 */     return !paramLevelAccessor.getBlockState(paramBlockPos.below()).is(Blocks.NETHER_WART_BLOCK);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 221 */     if (paramServerLevelAccessor.getRandom().nextFloat() < 0.2F) {
/* 222 */       setBaby(true);
/*     */     }
/*     */     
/* 225 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double paramDouble) {
/* 230 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos paramBlockPos, LevelReader paramLevelReader) {
/* 235 */     if (HoglinAi.isPosNearNearestRepellent(this, paramBlockPos)) {
/* 236 */       return -1.0F;
/*     */     }
/* 238 */     if (paramLevelReader.getBlockState(paramBlockPos.below()).is(Blocks.CRIMSON_NYLIUM))
/*     */     {
/* 240 */       return 10.0F;
/*     */     }
/* 242 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 247 */     InteractionResult interactionResult = super.mobInteract(paramPlayer, paramInteractionHand);
/* 248 */     if (interactionResult.consumesAction()) {
/* 249 */       setPersistenceRequired();
/*     */     }
/* 251 */     return interactionResult;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte paramByte) {
/* 257 */     if (paramByte == 4) {
/*     */       
/* 259 */       this.attackAnimationRemainingTicks = 10;
/* 260 */       makeSound(SoundEvents.HOGLIN_ATTACK);
/*     */     } else {
/* 262 */       super.handleEntityEvent(paramByte);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAttackAnimationRemainingTicks() {
/* 268 */     return this.attackAnimationRemainingTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldDropExperience() {
/* 273 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getBaseExperienceReward(ServerLevel paramServerLevel) {
/* 278 */     return this.xpReward;
/*     */   }
/*     */   
/*     */   private void finishConversion() {
/* 282 */     convertTo(EntityType.ZOGLIN, ConversionParams.single((Mob)this, true, false), paramZoglin -> paramZoglin.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 0)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack paramItemStack) {
/* 289 */     return paramItemStack.is(ItemTags.HOGLIN_FOOD);
/*     */   }
/*     */   
/*     */   public boolean isAdult() {
/* 293 */     return !isBaby();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 298 */     super.defineSynchedData(paramBuilder);
/* 299 */     paramBuilder.define(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 304 */     super.addAdditionalSaveData(paramValueOutput);
/* 305 */     paramValueOutput.putBoolean("IsImmuneToZombification", isImmuneToZombification());
/* 306 */     paramValueOutput.putInt("TimeInOverworld", this.timeInOverworld);
/* 307 */     paramValueOutput.putBoolean("CannotBeHunted", this.cannotBeHunted);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 312 */     super.readAdditionalSaveData(paramValueInput);
/* 313 */     setImmuneToZombification(paramValueInput.getBooleanOr("IsImmuneToZombification", false));
/* 314 */     this.timeInOverworld = paramValueInput.getIntOr("TimeInOverworld", 0);
/* 315 */     setCannotBeHunted(paramValueInput.getBooleanOr("CannotBeHunted", false));
/*     */   }
/*     */   
/*     */   public void setImmuneToZombification(boolean paramBoolean) {
/* 319 */     getEntityData().set(DATA_IMMUNE_TO_ZOMBIFICATION, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */   
/*     */   private boolean isImmuneToZombification() {
/* 323 */     return ((Boolean)getEntityData().get(DATA_IMMUNE_TO_ZOMBIFICATION)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isConverting() {
/* 327 */     return (!isImmuneToZombification() && !isNoAi() && ((Boolean)level().environmentAttributes().getValue(EnvironmentAttributes.PIGLINS_ZOMBIFY, position())).booleanValue());
/*     */   }
/*     */   
/*     */   private void setCannotBeHunted(boolean paramBoolean) {
/* 331 */     this.cannotBeHunted = paramBoolean;
/*     */   }
/*     */   
/*     */   public boolean canBeHunted() {
/* 335 */     return (isAdult() && !this.cannotBeHunted);
/*     */   }
/*     */ 
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel paramServerLevel, AgeableMob paramAgeableMob) {
/* 340 */     Hoglin hoglin = (Hoglin)EntityType.HOGLIN.create((Level)paramServerLevel, EntitySpawnReason.BREEDING);
/* 341 */     if (hoglin != null) {
/* 342 */       hoglin.setPersistenceRequired();
/*     */     }
/* 344 */     return (AgeableMob)hoglin;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canFallInLove() {
/* 349 */     return (!HoglinAi.isPacified(this) && super.canFallInLove());
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSoundSource() {
/* 354 */     return SoundSource.HOSTILE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 359 */     if (level().isClientSide()) {
/* 360 */       return null;
/*     */     }
/* 362 */     return HoglinAi.getSoundForCurrentActivity(this).orElse(null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 367 */     return SoundEvents.HOGLIN_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 372 */     return SoundEvents.HOGLIN_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/* 377 */     return SoundEvents.HOSTILE_SWIM;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSplashSound() {
/* 382 */     return SoundEvents.HOSTILE_SPLASH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 387 */     playSound(SoundEvents.HOGLIN_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public LivingEntity getTarget() {
/* 392 */     return getTargetFromBrain();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\hoglin\Hoglin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */