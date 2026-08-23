/*     */ package net.minecraft.world.entity.animal.nautilus;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
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
/*     */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public class NautilusAi {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.3F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 0.4F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 1.6F;
/*  49 */   private static final UniformInt TIME_BETWEEN_NON_PLAYER_ATTACKS = UniformInt.of(2400, 3600);
/*     */   
/*     */   private static final float SPEED_WHEN_ATTACKING = 0.6F;
/*     */   
/*     */   private static final float ATTACK_KNOCKBACK_FORCE = 2.0F;
/*     */   private static final int ANGER_DURATION = 400;
/*     */   
/*     */   static {
/*  57 */     ATTACK_TARGET_CONDITIONS = TargetingConditions.forCombat().selector((paramLivingEntity, paramServerLevel) -> 
/*  58 */         ((((Boolean)paramServerLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() || !paramLivingEntity.getType().equals(EntityType.ARMOR_STAND)) && paramServerLevel.getWorldBorder().isWithinBounds(paramLivingEntity.getBoundingBox())));
/*     */   }
/*     */   private static final int TIME_BETWEEN_ATTACKS = 80; private static final double MAX_CHARGE_DISTANCE = 12.0D; private static final double MAX_TARGET_DETECTION_DISTANCE = 11.0D; protected static final TargetingConditions ATTACK_TARGET_CONDITIONS;
/*  61 */   protected static final ImmutableList<SensorType<? extends Sensor<? super Nautilus>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_ADULT, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.NAUTILUS_TEMPTATIONS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.BREED_TARGET, MemoryModuleType.IS_PANICKING, MemoryModuleType.ATTACK_TARGET, (Object[])new MemoryModuleType[] { MemoryModuleType.CHARGE_COOLDOWN_TICKS, MemoryModuleType.HURT_BY, MemoryModuleType.ANGRY_AT, MemoryModuleType.ATTACK_TARGET_COOLDOWN });
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
/*     */   protected static void initMemories(AbstractNautilus paramAbstractNautilus, RandomSource paramRandomSource) {
/*  89 */     paramAbstractNautilus.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET_COOLDOWN, Integer.valueOf(TIME_BETWEEN_NON_PLAYER_ATTACKS.sample(paramRandomSource)));
/*     */   }
/*     */   
/*     */   protected static Brain.Provider<Nautilus> brainProvider() {
/*  93 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<Nautilus> paramBrain) {
/*  97 */     initCoreActivity(paramBrain);
/*  98 */     initIdleActivity(paramBrain);
/*  99 */     initFightActivity(paramBrain);
/*     */     
/* 101 */     paramBrain.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/* 102 */     paramBrain.setDefaultActivity(Activity.IDLE);
/* 103 */     paramBrain.useDefaultActivity();
/* 104 */     return paramBrain;
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(Brain<Nautilus> paramBrain) {
/* 108 */     paramBrain.addActivity(Activity.CORE, 0, ImmutableList.of(new AnimalPanic(1.6F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.CHARGE_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.ATTACK_TARGET_COOLDOWN)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Nautilus> paramBrain) {
/* 119 */     paramBrain.addActivity(Activity.IDLE, ImmutableList.of(
/* 120 */           Pair.of(Integer.valueOf(1), new AnimalMakeLove(EntityType.NAUTILUS, 0.4F, 2)), 
/* 121 */           Pair.of(Integer.valueOf(2), new FollowTemptation(paramLivingEntity -> Float.valueOf(1.3F), paramLivingEntity -> Double.valueOf(paramLivingEntity.isBaby() ? 2.5D : 3.5D))), 
/* 122 */           Pair.of(Integer.valueOf(3), StartAttacking.create(NautilusAi::findNearestValidAttackTarget)), 
/* 123 */           Pair.of(Integer.valueOf(4), new GateBehavior(
/* 124 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */ 
/*     */               
/* 127 */               (Set)ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, 
/*     */ 
/*     */               
/* 130 */               (List)ImmutableList.of(
/* 131 */                 Pair.of(RandomStroll.swim(1.0F), Integer.valueOf(2)), 
/* 132 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(3)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initFightActivity(Brain<Nautilus> paramBrain) {
/* 140 */     paramBrain.addActivityWithConditions(Activity.FIGHT, ImmutableList.of(
/* 141 */           Pair.of(Integer.valueOf(0), new ChargeAttack(80, ATTACK_TARGET_CONDITIONS, 0.6F, 2.0F, 12.0D, 11.0D, SoundEvents.NAUTILUS_DASH))), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 149 */         (Set)ImmutableSet.of(
/* 150 */           Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT), 
/* 151 */           Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT), 
/* 152 */           Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 153 */           Pair.of(MemoryModuleType.CHARGE_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel paramServerLevel, AbstractNautilus paramAbstractNautilus) {
/* 158 */     if (BehaviorUtils.isBreeding((LivingEntity)paramAbstractNautilus) || !paramAbstractNautilus.isInWater() || paramAbstractNautilus.isBaby() || paramAbstractNautilus.isTame()) {
/* 159 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 163 */     Optional<? extends LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory((LivingEntity)paramAbstractNautilus, MemoryModuleType.ANGRY_AT).filter(paramLivingEntity -> (paramLivingEntity.isInWater() && Sensor.isEntityAttackableIgnoringLineOfSight(paramServerLevel, (LivingEntity)paramAbstractNautilus, paramLivingEntity)));
/* 164 */     if (optional.isPresent()) {
/* 165 */       return optional;
/*     */     }
/*     */     
/* 168 */     if (paramAbstractNautilus.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET_COOLDOWN)) {
/* 169 */       return Optional.empty();
/*     */     }
/*     */     
/* 172 */     paramAbstractNautilus.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET_COOLDOWN, Integer.valueOf(TIME_BETWEEN_NON_PLAYER_ATTACKS.sample(paramServerLevel.random)));
/*     */     
/* 174 */     if (paramServerLevel.random.nextFloat() < 0.5F) {
/* 175 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 180 */     return ((NearestVisibleLivingEntities)paramAbstractNautilus.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty())).findClosest(NautilusAi::isHostileTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void setAngerTarget(ServerLevel paramServerLevel, AbstractNautilus paramAbstractNautilus, LivingEntity paramLivingEntity) {
/* 186 */     if (Sensor.isEntityAttackableIgnoringLineOfSight(paramServerLevel, (LivingEntity)paramAbstractNautilus, paramLivingEntity)) {
/* 187 */       paramAbstractNautilus.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 188 */       paramAbstractNautilus.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, paramLivingEntity.getUUID(), 400L);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isHostileTarget(LivingEntity paramLivingEntity) {
/* 193 */     return (paramLivingEntity.isInWater() && paramLivingEntity.getType().is(EntityTypeTags.NAUTILUS_HOSTILES));
/*     */   }
/*     */   
/*     */   public static void updateActivity(Nautilus paramNautilus) {
/* 197 */     paramNautilus.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/*     */   }
/*     */   
/*     */   public static Predicate<ItemStack> getTemptations() {
/* 201 */     return paramItemStack -> paramItemStack.is(ItemTags.NAUTILUS_FOOD);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\nautilus\NautilusAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */