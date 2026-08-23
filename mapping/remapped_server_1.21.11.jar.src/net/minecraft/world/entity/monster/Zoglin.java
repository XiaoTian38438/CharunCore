/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.monster.hoglin.HoglinBase;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Zoglin
/*     */   extends Monster
/*     */   implements HoglinBase
/*     */ {
/*  62 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(Zoglin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int MAX_HEALTH = 40;
/*     */   
/*     */   private static final int ATTACK_KNOCKBACK = 1;
/*     */   
/*     */   private static final float KNOCKBACK_RESISTANCE = 0.6F;
/*     */   
/*     */   private static final int ATTACK_DAMAGE = 6;
/*     */   
/*     */   private static final float BABY_ATTACK_DAMAGE = 0.5F;
/*     */   private static final int ATTACK_INTERVAL = 40;
/*     */   private static final int BABY_ATTACK_INTERVAL = 15;
/*     */   private static final int ATTACK_DURATION = 200;
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.3F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.4F;
/*     */   private static final boolean DEFAULT_BABY = false;
/*     */   private int attackAnimationRemainingTicks;
/*  80 */   protected static final ImmutableList<? extends SensorType<? extends Sensor<? super Zoglin>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS);
/*     */ 
/*     */ 
/*     */   
/*  84 */   protected static final ImmutableList<? extends MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.LOOK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN);
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
/*     */   public Zoglin(EntityType<? extends Zoglin> paramEntityType, Level paramLevel) {
/*  98 */     super((EntityType)paramEntityType, paramLevel);
/*  99 */     this.xpReward = 5;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<Zoglin> brainProvider() {
/* 104 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> paramDynamic) {
/* 109 */     Brain<Zoglin> brain = brainProvider().makeBrain(paramDynamic);
/* 110 */     initCoreActivity(brain);
/* 111 */     initIdleActivity(brain);
/* 112 */     initFightActivity(brain);
/*     */     
/* 114 */     brain.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/* 115 */     brain.setDefaultActivity(Activity.IDLE);
/* 116 */     brain.useDefaultActivity();
/* 117 */     return brain;
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(Brain<Zoglin> paramBrain) {
/* 121 */     paramBrain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Zoglin> paramBrain) {
/* 128 */     paramBrain.addActivity(Activity.IDLE, 10, ImmutableList.of(
/* 129 */           StartAttacking.create((paramServerLevel, paramZoglin) -> paramZoglin.findNearestValidAttackTarget(paramServerLevel)), 
/* 130 */           SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), new RunOne(
/* 131 */             (List)ImmutableList.of(
/* 132 */               Pair.of(RandomStroll.stroll(0.4F), Integer.valueOf(2)), 
/* 133 */               Pair.of(SetWalkTargetFromLookTarget.create(0.4F, 3), Integer.valueOf(2)), 
/* 134 */               Pair.of(new DoNothing(30, 60), Integer.valueOf(1))))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initFightActivity(Brain<Zoglin> paramBrain) {
/* 140 */     paramBrain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(
/* 141 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), 
/* 142 */           BehaviorBuilder.triggerIf(Zoglin::isAdult, MeleeAttack.create(40)), 
/* 143 */           BehaviorBuilder.triggerIf(Zoglin::isBaby, MeleeAttack.create(15)), 
/* 144 */           StopAttackingIfTargetInvalid.create()), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */ 
/*     */   
/*     */   private Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel paramServerLevel) {
/* 149 */     return ((NearestVisibleLivingEntities)getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty())).findClosest(paramLivingEntity -> isTargetable(paramServerLevel, paramLivingEntity));
/*     */   }
/*     */   
/*     */   private boolean isTargetable(ServerLevel paramServerLevel, LivingEntity paramLivingEntity) {
/* 153 */     EntityType entityType = paramLivingEntity.getType();
/* 154 */     return (entityType != EntityType.ZOGLIN && entityType != EntityType.CREEPER && Sensor.isEntityAttackable(paramServerLevel, (LivingEntity)this, paramLivingEntity));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 159 */     super.defineSynchedData(paramBuilder);
/* 160 */     paramBuilder.define(DATA_BABY_ID, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/* 165 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/* 166 */     if (DATA_BABY_ID.equals(paramEntityDataAccessor)) {
/* 167 */       refreshDimensions();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 173 */     if (paramServerLevelAccessor.getRandom().nextFloat() < 0.2F) {
/* 174 */       setBaby(true);
/*     */     }
/*     */     
/* 177 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 181 */     return Monster.createMonsterAttributes()
/* 182 */       .add(Attributes.MAX_HEALTH, 40.0D)
/* 183 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 184 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.6000000238418579D)
/* 185 */       .add(Attributes.ATTACK_KNOCKBACK, 1.0D)
/* 186 */       .add(Attributes.ATTACK_DAMAGE, 6.0D);
/*     */   }
/*     */   
/*     */   public boolean isAdult() {
/* 190 */     return !isBaby();
/*     */   }
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel paramServerLevel, Entity paramEntity) {
/*     */     LivingEntity livingEntity;
/* 195 */     if (paramEntity instanceof LivingEntity) { livingEntity = (LivingEntity)paramEntity; }
/* 196 */     else { return false; }
/*     */     
/* 198 */     this.attackAnimationRemainingTicks = 10;
/* 199 */     paramServerLevel.broadcastEntityEvent((Entity)this, (byte)4);
/*     */     
/* 201 */     makeSound(SoundEvents.ZOGLIN_ATTACK);
/* 202 */     return HoglinBase.hurtAndThrowTarget(paramServerLevel, (LivingEntity)this, livingEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeLeashed() {
/* 207 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void blockedByItem(LivingEntity paramLivingEntity) {
/* 212 */     if (!isBaby()) {
/* 213 */       HoglinBase.throwTarget((LivingEntity)this, paramLivingEntity);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/*     */     LivingEntity livingEntity;
/* 219 */     boolean bool = super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/* 220 */     if (bool) { Entity entity = paramDamageSource.getEntity(); if (entity instanceof LivingEntity) { livingEntity = (LivingEntity)entity; }
/* 221 */       else { return bool; }  } else { return bool; }
/*     */     
/* 223 */     if (canAttack(livingEntity) && !BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget((LivingEntity)this, livingEntity, 4.0D)) {
/* 224 */       setAttackTarget(livingEntity);
/*     */     }
/* 226 */     return true;
/*     */   }
/*     */   
/*     */   private void setAttackTarget(LivingEntity paramLivingEntity) {
/* 230 */     this.brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 231 */     this.brain.setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, paramLivingEntity, 200L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<Zoglin> getBrain() {
/* 237 */     return super.getBrain();
/*     */   }
/*     */   
/*     */   protected void updateActivity() {
/* 241 */     Activity activity1 = this.brain.getActiveNonCoreActivity().orElse(null);
/*     */ 
/*     */     
/* 244 */     this.brain.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/*     */     
/* 246 */     Activity activity2 = this.brain.getActiveNonCoreActivity().orElse(null);
/* 247 */     if (activity2 == Activity.FIGHT && activity1 != Activity.FIGHT)
/*     */     {
/* 249 */       playAngrySound();
/*     */     }
/*     */ 
/*     */     
/* 253 */     setAggressive(this.brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/* 258 */     ProfilerFiller profilerFiller = Profiler.get();
/* 259 */     profilerFiller.push("zoglinBrain");
/* 260 */     getBrain().tick(paramServerLevel, (LivingEntity)this);
/* 261 */     profilerFiller.pop();
/*     */     
/* 263 */     updateActivity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaby(boolean paramBoolean) {
/* 268 */     getEntityData().set(DATA_BABY_ID, Boolean.valueOf(paramBoolean));
/* 269 */     if (!level().isClientSide() && paramBoolean) {
/* 270 */       getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(0.5D);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBaby() {
/* 276 */     return ((Boolean)getEntityData().get(DATA_BABY_ID)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 282 */     if (this.attackAnimationRemainingTicks > 0) {
/* 283 */       this.attackAnimationRemainingTicks--;
/*     */     }
/* 285 */     super.aiStep();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte paramByte) {
/* 291 */     if (paramByte == 4) {
/*     */       
/* 293 */       this.attackAnimationRemainingTicks = 10;
/* 294 */       makeSound(SoundEvents.ZOGLIN_ATTACK);
/*     */     } else {
/* 296 */       super.handleEntityEvent(paramByte);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAttackAnimationRemainingTicks() {
/* 302 */     return this.attackAnimationRemainingTicks;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 307 */     if (level().isClientSide()) {
/* 308 */       return null;
/*     */     }
/* 310 */     if (this.brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
/* 311 */       return SoundEvents.ZOGLIN_ANGRY;
/*     */     }
/* 313 */     return SoundEvents.ZOGLIN_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 318 */     return SoundEvents.ZOGLIN_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 323 */     return SoundEvents.ZOGLIN_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 328 */     playSound(SoundEvents.ZOGLIN_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void playAngrySound() {
/* 332 */     makeSound(SoundEvents.ZOGLIN_ANGRY);
/*     */   }
/*     */ 
/*     */   
/*     */   public LivingEntity getTarget() {
/* 337 */     return getTargetFromBrain();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 342 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/* 344 */     paramValueOutput.putBoolean("IsBaby", isBaby());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 349 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/* 351 */     setBaby(paramValueInput.getBooleanOr("IsBaby", false));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\Zoglin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */