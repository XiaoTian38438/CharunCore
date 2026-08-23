/*     */ package net.minecraft.world.entity.animal.nautilus;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.ChargeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.GateBehavior;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ public class ZombieNautilusAi
/*     */ {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 0.9F;
/*     */   private static final float SPEED_WHEN_ATTACKING = 0.5F;
/*  34 */   protected static final ImmutableList<SensorType<? extends Sensor<? super ZombieNautilus>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_ADULT, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NAUTILUS_TEMPTATIONS);
/*     */   
/*     */   private static final float ATTACK_KNOCKBACK_FORCE = 2.0F;
/*     */   
/*     */   private static final int TIME_BETWEEN_ATTACKS = 80;
/*     */   
/*     */   private static final double MAX_CHARGE_DISTANCE = 12.0D;
/*     */   private static final double MAX_TARGET_DETECTION_DISTANCE = 11.0D;
/*  42 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.BREED_TARGET, MemoryModuleType.IS_PANICKING, MemoryModuleType.ATTACK_TARGET, (Object[])new MemoryModuleType[] { MemoryModuleType.CHARGE_COOLDOWN_TICKS, MemoryModuleType.HURT_BY, MemoryModuleType.ANGRY_AT, MemoryModuleType.ATTACK_TARGET_COOLDOWN });
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
/*     */   protected static Brain.Provider<ZombieNautilus> brainProvider() {
/*  62 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<ZombieNautilus> paramBrain) {
/*  66 */     initCoreActivity(paramBrain);
/*  67 */     initIdleActivity(paramBrain);
/*  68 */     initFightActivity(paramBrain);
/*     */     
/*  70 */     paramBrain.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/*  71 */     paramBrain.setDefaultActivity(Activity.IDLE);
/*  72 */     paramBrain.useDefaultActivity();
/*  73 */     return paramBrain;
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(Brain<ZombieNautilus> paramBrain) {
/*  77 */     paramBrain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.CHARGE_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.ATTACK_TARGET_COOLDOWN)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<ZombieNautilus> paramBrain) {
/*  87 */     paramBrain.addActivity(Activity.IDLE, ImmutableList.of(
/*  88 */           Pair.of(Integer.valueOf(1), new FollowTemptation(paramLivingEntity -> Float.valueOf(0.9F), paramLivingEntity -> Double.valueOf(paramLivingEntity.isBaby() ? 2.5D : 3.5D))), 
/*  89 */           Pair.of(Integer.valueOf(2), StartAttacking.create(NautilusAi::findNearestValidAttackTarget)), 
/*  90 */           Pair.of(Integer.valueOf(3), new GateBehavior(
/*  91 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */ 
/*     */               
/*  94 */               (Set)ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, 
/*     */ 
/*     */               
/*  97 */               (List)ImmutableList.of(
/*  98 */                 Pair.of(RandomStroll.swim(1.0F), Integer.valueOf(2)), 
/*  99 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(3)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initFightActivity(Brain<ZombieNautilus> paramBrain) {
/* 107 */     paramBrain.addActivityWithConditions(Activity.FIGHT, ImmutableList.of(
/* 108 */           Pair.of(Integer.valueOf(0), new ChargeAttack(80, NautilusAi.ATTACK_TARGET_CONDITIONS, 0.5F, 2.0F, 12.0D, 11.0D, SoundEvents.ZOMBIE_NAUTILUS_DASH))), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 116 */         (Set)ImmutableSet.of(
/* 117 */           Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 
/* 118 */           Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT), 
/* 119 */           Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 120 */           Pair.of(MemoryModuleType.CHARGE_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void updateActivity(ZombieNautilus paramZombieNautilus) {
/* 125 */     paramZombieNautilus.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\nautilus\ZombieNautilusAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */