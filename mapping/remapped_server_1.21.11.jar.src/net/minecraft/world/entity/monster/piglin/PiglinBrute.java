/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ 
/*     */ public class PiglinBrute
/*     */   extends AbstractPiglin
/*     */ {
/*     */   private static final int MAX_HEALTH = 50;
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.35F;
/*     */   private static final int ATTACK_DAMAGE = 7;
/*     */   private static final double TARGETING_RANGE = 12.0D;
/*  43 */   protected static final ImmutableList<SensorType<? extends Sensor<? super PiglinBrute>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_BRUTE_SPECIFIC_SENSOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, (Object[])new MemoryModuleType[] { MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.HOME });
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
/*     */   public PiglinBrute(EntityType<? extends PiglinBrute> paramEntityType, Level paramLevel) {
/*  74 */     super((EntityType)paramEntityType, paramLevel);
/*  75 */     this.xpReward = 20;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  79 */     return Monster.createMonsterAttributes()
/*  80 */       .add(Attributes.MAX_HEALTH, 50.0D)
/*  81 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/*  82 */       .add(Attributes.ATTACK_DAMAGE, 7.0D)
/*  83 */       .add(Attributes.FOLLOW_RANGE, 12.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/*  88 */     PiglinBruteAi.initMemories(this);
/*  89 */     populateDefaultEquipmentSlots(paramServerLevelAccessor.getRandom(), paramDifficultyInstance);
/*  90 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource paramRandomSource, DifficultyInstance paramDifficultyInstance) {
/*  95 */     setItemSlot(EquipmentSlot.MAINHAND, new ItemStack((ItemLike)Items.GOLDEN_AXE));
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<PiglinBrute> brainProvider() {
/* 100 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> paramDynamic) {
/* 105 */     return PiglinBruteAi.makeBrain(this, brainProvider().makeBrain(paramDynamic));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<PiglinBrute> getBrain() {
/* 111 */     return super.getBrain();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canHunt() {
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel paramServerLevel, ItemStack paramItemStack) {
/* 121 */     if (paramItemStack.is(Items.GOLDEN_AXE)) {
/* 122 */       return super.wantsToPickUp(paramServerLevel, paramItemStack);
/*     */     }
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/* 129 */     ProfilerFiller profilerFiller = Profiler.get();
/* 130 */     profilerFiller.push("piglinBruteBrain");
/* 131 */     getBrain().tick(paramServerLevel, (LivingEntity)this);
/* 132 */     profilerFiller.pop();
/*     */     
/* 134 */     PiglinBruteAi.updateActivity(this);
/* 135 */     PiglinBruteAi.maybePlayActivitySound(this);
/*     */     
/* 137 */     super.customServerAiStep(paramServerLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public PiglinArmPose getArmPose() {
/* 142 */     if (isAggressive() && isHoldingMeleeWeapon()) {
/* 143 */       return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON;
/*     */     }
/* 145 */     return PiglinArmPose.DEFAULT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 151 */     boolean bool = super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/* 152 */     if (bool) { Entity entity = paramDamageSource.getEntity(); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 153 */         PiglinBruteAi.wasHurtBy(paramServerLevel, this, livingEntity); }
/*     */        }
/* 155 */      return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 160 */     return SoundEvents.PIGLIN_BRUTE_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 165 */     return SoundEvents.PIGLIN_BRUTE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 170 */     return SoundEvents.PIGLIN_BRUTE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 175 */     playSound(SoundEvents.PIGLIN_BRUTE_STEP, 0.15F, 1.0F);
/*     */   }
/*     */   
/*     */   protected void playAngrySound() {
/* 179 */     makeSound(SoundEvents.PIGLIN_BRUTE_ANGRY);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playConvertedSound() {
/* 184 */     makeSound(SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\piglin\PiglinBrute.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */