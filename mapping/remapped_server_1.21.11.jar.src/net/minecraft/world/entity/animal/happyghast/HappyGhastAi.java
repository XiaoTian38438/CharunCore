/*     */ package net.minecraft.world.entity.animal.happyghast;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ public class HappyGhastAi {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
/*  30 */   private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(3, 16);
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT = 1.1F;
/*  32 */   private static final ImmutableList<SensorType<? extends Sensor<? super HappyGhast>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.HURT_BY, SensorType.FOOD_TEMPTATIONS, SensorType.NEAREST_ADULT_ANY_TYPE, SensorType.NEAREST_PLAYERS);
/*     */ 
/*     */ 
/*     */   
/*     */   private static final double BABY_GHAST_CLOSE_ENOUGH_DIST = 3.0D;
/*     */ 
/*     */ 
/*     */   
/*  40 */   private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.WALK_TARGET, MemoryModuleType.LOOK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.BREED_TARGET, MemoryModuleType.IS_PANICKING, MemoryModuleType.HURT_BY, MemoryModuleType.NEAREST_VISIBLE_ADULT, (Object[])new MemoryModuleType[] { MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS });
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
/*     */   public static Brain.Provider<HappyGhast> brainProvider() {
/*  62 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<HappyGhast> paramBrain) {
/*  66 */     initCoreActivity(paramBrain);
/*  67 */     initIdleActivity(paramBrain);
/*  68 */     initPanicActivity(paramBrain);
/*     */     
/*  70 */     paramBrain.setCoreActivities(Set.of(Activity.CORE));
/*  71 */     paramBrain.setDefaultActivity(Activity.IDLE);
/*  72 */     paramBrain.useDefaultActivity();
/*  73 */     return paramBrain;
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(Brain<HappyGhast> paramBrain) {
/*  77 */     paramBrain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new AnimalPanic(2.0F, 0), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<HappyGhast> paramBrain) {
/*  87 */     paramBrain.addActivity(Activity.IDLE, ImmutableList.of(
/*  88 */           Pair.of(Integer.valueOf(1), new FollowTemptation(paramLivingEntity -> Float.valueOf(1.25F), paramLivingEntity -> Double.valueOf(3.0D), true)), 
/*  89 */           Pair.of(Integer.valueOf(2), BabyFollowAdult.create(ADULT_FOLLOW_RANGE, paramLivingEntity -> Float.valueOf(1.1F), MemoryModuleType.NEAREST_VISIBLE_PLAYER, true)), 
/*  90 */           Pair.of(Integer.valueOf(3), BabyFollowAdult.create(ADULT_FOLLOW_RANGE, paramLivingEntity -> Float.valueOf(1.1F), MemoryModuleType.NEAREST_VISIBLE_ADULT, true)), 
/*  91 */           Pair.of(Integer.valueOf(4), new RunOne(
/*  92 */               (List)ImmutableList.of(
/*  93 */                 Pair.of(RandomStroll.fly(1.0F), Integer.valueOf(1)), 
/*  94 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(1)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initPanicActivity(Brain<HappyGhast> paramBrain) {
/* 102 */     paramBrain.addActivityWithConditions(Activity.PANIC, ImmutableList.of(), 
/* 103 */         Set.of(
/* 104 */           Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_PRESENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void updateActivity(HappyGhast paramHappyGhast) {
/* 109 */     paramHappyGhast.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.PANIC, Activity.IDLE));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\happyghast\HappyGhastAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */