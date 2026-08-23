/*     */ package net.minecraft.world.entity.monster.hoglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
/*     */ import net.minecraft.world.entity.ai.behavior.BecomePassiveIfMemoryPresent;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.EraseMemoryIf;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HoglinAi
/*     */ {
/*     */   public static final int REPELLENT_DETECTION_RANGE_HORIZONTAL = 8;
/*     */   public static final int REPELLENT_DETECTION_RANGE_VERTICAL = 4;
/*  50 */   private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
/*     */   private static final int ATTACK_DURATION = 200;
/*     */   private static final int DESIRED_DISTANCE_FROM_PIGLIN_WHEN_IDLING = 8;
/*     */   private static final int DESIRED_DISTANCE_FROM_PIGLIN_WHEN_RETREATING = 15;
/*     */   private static final int ATTACK_INTERVAL = 40;
/*     */   private static final int BABY_ATTACK_INTERVAL = 15;
/*     */   private static final int REPELLENT_PACIFY_TIME = 200;
/*  57 */   private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_AVOIDING_REPELLENT = 1.0F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_RETREATING = 1.3F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 0.6F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.4F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT = 0.6F;
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<Hoglin> paramBrain) {
/*  67 */     initCoreActivity(paramBrain);
/*     */     
/*  69 */     initIdleActivity(paramBrain);
/*  70 */     initFightActivity(paramBrain);
/*  71 */     initRetreatActivity(paramBrain);
/*     */     
/*  73 */     paramBrain.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/*  74 */     paramBrain.setDefaultActivity(Activity.IDLE);
/*  75 */     paramBrain.useDefaultActivity();
/*  76 */     return paramBrain;
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(Brain<Hoglin> paramBrain) {
/*  80 */     paramBrain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Hoglin> paramBrain) {
/*  87 */     paramBrain.addActivity(Activity.IDLE, 10, ImmutableList.of(
/*  88 */           BecomePassiveIfMemoryPresent.create(MemoryModuleType.NEAREST_REPELLENT, 200), new AnimalMakeLove(EntityType.HOGLIN, 0.6F, 2), 
/*     */           
/*  90 */           SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, true), 
/*  91 */           StartAttacking.create(HoglinAi::findNearestValidAttackTarget), 
/*  92 */           BehaviorBuilder.triggerIf(Hoglin::isAdult, SetWalkTargetAwayFrom.entity(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLIN, 0.4F, 8, false)), 
/*  93 */           SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), 
/*  94 */           BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 0.6F), 
/*  95 */           createIdleMovementBehaviors()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initFightActivity(Brain<Hoglin> paramBrain) {
/* 100 */     paramBrain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(
/* 101 */           BecomePassiveIfMemoryPresent.create(MemoryModuleType.NEAREST_REPELLENT, 200), new AnimalMakeLove(EntityType.HOGLIN, 0.6F, 2), 
/*     */           
/* 103 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), 
/* 104 */           BehaviorBuilder.triggerIf(Hoglin::isAdult, MeleeAttack.create(40)), 
/* 105 */           BehaviorBuilder.triggerIf(AgeableMob::isBaby, MeleeAttack.create(15)), 
/* 106 */           StopAttackingIfTargetInvalid.create(), 
/* 107 */           EraseMemoryIf.create(HoglinAi::isBreeding, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initRetreatActivity(Brain<Hoglin> paramBrain) {
/* 112 */     paramBrain.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10, ImmutableList.of(
/* 113 */           SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.3F, 15, false), 
/* 114 */           createIdleMovementBehaviors(), 
/* 115 */           SetEntityLookTargetSometimes.create(8.0F, UniformInt.of(30, 60)), 
/* 116 */           EraseMemoryIf.create(HoglinAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET);
/*     */   }
/*     */ 
/*     */   
/*     */   private static RunOne<Hoglin> createIdleMovementBehaviors() {
/* 121 */     return new RunOne((List)ImmutableList.of(
/* 122 */           Pair.of(RandomStroll.stroll(0.4F), Integer.valueOf(2)), 
/* 123 */           Pair.of(SetWalkTargetFromLookTarget.create(0.4F, 3), Integer.valueOf(2)), 
/* 124 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void updateActivity(Hoglin paramHoglin) {
/* 129 */     Brain<Hoglin> brain = paramHoglin.getBrain();
/*     */     
/* 131 */     Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);
/*     */ 
/*     */     
/* 134 */     brain.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.AVOID, Activity.IDLE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     Activity activity2 = brain.getActiveNonCoreActivity().orElse(null);
/* 141 */     if (activity1 != activity2) {
/*     */       
/* 143 */       Objects.requireNonNull(paramHoglin); getSoundForCurrentActivity(paramHoglin).ifPresent(paramHoglin::makeSound);
/*     */     } 
/*     */ 
/*     */     
/* 147 */     paramHoglin.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */   }
/*     */   
/*     */   protected static void onHitTarget(Hoglin paramHoglin, LivingEntity paramLivingEntity) {
/* 151 */     if (paramHoglin.isBaby()) {
/*     */       return;
/*     */     }
/*     */     
/* 155 */     if (paramLivingEntity.getType() == EntityType.PIGLIN && piglinsOutnumberHoglins(paramHoglin)) {
/*     */       
/* 157 */       setAvoidTarget(paramHoglin, paramLivingEntity);
/* 158 */       broadcastRetreat(paramHoglin, paramLivingEntity);
/*     */       return;
/*     */     } 
/* 161 */     broadcastAttackTarget(paramHoglin, paramLivingEntity);
/*     */   }
/*     */   
/*     */   private static void broadcastRetreat(Hoglin paramHoglin, LivingEntity paramLivingEntity) {
/* 165 */     getVisibleAdultHoglins(paramHoglin).forEach(paramHoglin -> retreatFromNearestTarget(paramHoglin, paramLivingEntity));
/*     */   }
/*     */   
/*     */   private static void retreatFromNearestTarget(Hoglin paramHoglin, LivingEntity paramLivingEntity) {
/* 169 */     LivingEntity livingEntity = paramLivingEntity;
/*     */     
/* 171 */     Brain<Hoglin> brain = paramHoglin.getBrain();
/* 172 */     livingEntity = BehaviorUtils.getNearestTarget((LivingEntity)paramHoglin, brain.getMemory(MemoryModuleType.AVOID_TARGET), livingEntity);
/* 173 */     livingEntity = BehaviorUtils.getNearestTarget((LivingEntity)paramHoglin, brain.getMemory(MemoryModuleType.ATTACK_TARGET), livingEntity);
/*     */     
/* 175 */     setAvoidTarget(paramHoglin, livingEntity);
/*     */   }
/*     */   
/*     */   private static void setAvoidTarget(Hoglin paramHoglin, LivingEntity paramLivingEntity) {
/* 179 */     paramHoglin.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/* 180 */     paramHoglin.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 181 */     paramHoglin.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, paramLivingEntity, RETREAT_DURATION.sample((paramHoglin.level()).random));
/*     */   }
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel paramServerLevel, Hoglin paramHoglin) {
/* 185 */     if (isPacified(paramHoglin) || isBreeding(paramHoglin))
/*     */     {
/* 187 */       return Optional.empty();
/*     */     }
/*     */ 
/*     */     
/* 191 */     return paramHoglin.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
/*     */   }
/*     */   
/*     */   static boolean isPosNearNearestRepellent(Hoglin paramHoglin, BlockPos paramBlockPos) {
/* 195 */     Optional<BlockPos> optional = paramHoglin.getBrain().getMemory(MemoryModuleType.NEAREST_REPELLENT);
/* 196 */     return (optional.isPresent() && ((BlockPos)optional.get()).closerThan((Vec3i)paramBlockPos, 8.0D));
/*     */   }
/*     */   
/*     */   private static boolean wantsToStopFleeing(Hoglin paramHoglin) {
/* 200 */     return (paramHoglin.isAdult() && !piglinsOutnumberHoglins(paramHoglin));
/*     */   }
/*     */   
/*     */   private static boolean piglinsOutnumberHoglins(Hoglin paramHoglin) {
/* 204 */     if (paramHoglin.isBaby()) {
/* 205 */       return false;
/*     */     }
/*     */     
/* 208 */     int i = ((Integer)paramHoglin.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(Integer.valueOf(0))).intValue();
/* 209 */     int j = ((Integer)paramHoglin.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(Integer.valueOf(0))).intValue() + 1;
/* 210 */     return (i > j);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void wasHurtBy(ServerLevel paramServerLevel, Hoglin paramHoglin, LivingEntity paramLivingEntity) {
/* 215 */     Brain<Hoglin> brain = paramHoglin.getBrain();
/* 216 */     brain.eraseMemory(MemoryModuleType.PACIFIED);
/* 217 */     brain.eraseMemory(MemoryModuleType.BREED_TARGET);
/*     */     
/* 219 */     if (paramHoglin.isBaby()) {
/*     */       
/* 221 */       retreatFromNearestTarget(paramHoglin, paramLivingEntity);
/*     */       
/*     */       return;
/*     */     } 
/* 225 */     maybeRetaliate(paramServerLevel, paramHoglin, paramLivingEntity);
/*     */   }
/*     */   
/*     */   private static void maybeRetaliate(ServerLevel paramServerLevel, Hoglin paramHoglin, LivingEntity paramLivingEntity) {
/* 229 */     if (paramHoglin.getBrain().isActive(Activity.AVOID) && paramLivingEntity.getType() == EntityType.PIGLIN) {
/*     */       return;
/*     */     }
/* 232 */     if (paramLivingEntity.getType() == EntityType.HOGLIN) {
/*     */       return;
/*     */     }
/* 235 */     if (BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget((LivingEntity)paramHoglin, paramLivingEntity, 4.0D)) {
/*     */       return;
/*     */     }
/*     */     
/* 239 */     if (!Sensor.isEntityAttackable(paramServerLevel, (LivingEntity)paramHoglin, paramLivingEntity)) {
/*     */       return;
/*     */     }
/*     */     
/* 243 */     setAttackTarget(paramHoglin, paramLivingEntity);
/* 244 */     broadcastAttackTarget(paramHoglin, paramLivingEntity);
/*     */   }
/*     */   
/*     */   private static void setAttackTarget(Hoglin paramHoglin, LivingEntity paramLivingEntity) {
/* 248 */     Brain<Hoglin> brain = paramHoglin.getBrain();
/* 249 */     brain.eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 250 */     brain.eraseMemory(MemoryModuleType.BREED_TARGET);
/* 251 */     brain.setMemoryWithExpiry(MemoryModuleType.ATTACK_TARGET, paramLivingEntity, 200L);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void broadcastAttackTarget(Hoglin paramHoglin, LivingEntity paramLivingEntity) {
/* 256 */     getVisibleAdultHoglins(paramHoglin).forEach(paramHoglin -> setAttackTargetIfCloserThanCurrent(paramHoglin, paramLivingEntity));
/*     */   }
/*     */   
/*     */   private static void setAttackTargetIfCloserThanCurrent(Hoglin paramHoglin, LivingEntity paramLivingEntity) {
/* 260 */     if (isPacified(paramHoglin)) {
/*     */       return;
/*     */     }
/*     */     
/* 264 */     Optional optional = paramHoglin.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET);
/* 265 */     LivingEntity livingEntity = BehaviorUtils.getNearestTarget((LivingEntity)paramHoglin, optional, paramLivingEntity);
/* 266 */     setAttackTarget(paramHoglin, livingEntity);
/*     */   }
/*     */   
/*     */   public static Optional<SoundEvent> getSoundForCurrentActivity(Hoglin paramHoglin) {
/* 270 */     return paramHoglin.getBrain().getActiveNonCoreActivity().map(paramActivity -> getSoundForActivity(paramHoglin, paramActivity));
/*     */   }
/*     */   
/*     */   private static SoundEvent getSoundForActivity(Hoglin paramHoglin, Activity paramActivity) {
/* 274 */     if (paramActivity == Activity.AVOID || paramHoglin.isConverting())
/* 275 */       return SoundEvents.HOGLIN_RETREAT; 
/* 276 */     if (paramActivity == Activity.FIGHT)
/* 277 */       return SoundEvents.HOGLIN_ANGRY; 
/* 278 */     if (isNearRepellent(paramHoglin)) {
/* 279 */       return SoundEvents.HOGLIN_RETREAT;
/*     */     }
/* 281 */     return SoundEvents.HOGLIN_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Hoglin> getVisibleAdultHoglins(Hoglin paramHoglin) {
/* 286 */     return (List<Hoglin>)paramHoglin.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_HOGLINS).orElse(ImmutableList.of());
/*     */   }
/*     */   
/*     */   private static boolean isNearRepellent(Hoglin paramHoglin) {
/* 290 */     return paramHoglin.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
/*     */   }
/*     */   
/*     */   private static boolean isBreeding(Hoglin paramHoglin) {
/* 294 */     return paramHoglin.getBrain().hasMemoryValue(MemoryModuleType.BREED_TARGET);
/*     */   }
/*     */   
/*     */   protected static boolean isPacified(Hoglin paramHoglin) {
/* 298 */     return paramHoglin.getBrain().hasMemoryValue(MemoryModuleType.PACIFIED);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\hoglin\HoglinAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */