/*     */ package net.minecraft.world.entity.animal.axolotl;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.EraseMemoryIf;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.GateBehavior;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.TryFindWater;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public class AxolotlAi {
/*  43 */   private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
/*     */   private static final float SPEED_MULTIPLIER_WHEN_MAKING_LOVE = 0.2F;
/*     */   private static final float SPEED_MULTIPLIER_ON_LAND = 0.15F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING_IN_WATER = 0.5F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_CHASING_IN_WATER = 0.6F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT_IN_WATER = 0.6F;
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<Axolotl> paramBrain) {
/*  51 */     initCoreActivity(paramBrain);
/*  52 */     initIdleActivity(paramBrain);
/*  53 */     initFightActivity(paramBrain);
/*  54 */     initPlayDeadActivity(paramBrain);
/*     */     
/*  56 */     paramBrain.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/*  57 */     paramBrain.setDefaultActivity(Activity.IDLE);
/*  58 */     paramBrain.useDefaultActivity();
/*  59 */     return paramBrain;
/*     */   }
/*     */   
/*     */   private static void initPlayDeadActivity(Brain<Axolotl> paramBrain) {
/*  63 */     paramBrain.addActivityAndRemoveMemoriesWhenStopped(Activity.PLAY_DEAD, 
/*  64 */         ImmutableList.of(
/*  65 */           Pair.of(Integer.valueOf(0), new PlayDead()), 
/*  66 */           Pair.of(Integer.valueOf(1), EraseMemoryIf.create(BehaviorUtils::isBreeding, MemoryModuleType.PLAY_DEAD_TICKS))), 
/*     */         
/*  68 */         (Set)ImmutableSet.of(Pair.of(MemoryModuleType.PLAY_DEAD_TICKS, MemoryStatus.VALUE_PRESENT)), 
/*  69 */         (Set)ImmutableSet.of(MemoryModuleType.PLAY_DEAD_TICKS));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initFightActivity(Brain<Axolotl> paramBrain) {
/*  74 */     paramBrain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 0, ImmutableList.of(
/*  75 */           StopAttackingIfTargetInvalid.create(Axolotl::onStopAttacking), 
/*  76 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(AxolotlAi::getSpeedModifierChasing), 
/*  77 */           MeleeAttack.create(20), 
/*  78 */           EraseMemoryIf.create(BehaviorUtils::isBreeding, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initCoreActivity(Brain<Axolotl> paramBrain) {
/*  83 */     paramBrain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), 
/*     */ 
/*     */           
/*  86 */           ValidatePlayDead.create(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Axolotl> paramBrain) {
/*  92 */     paramBrain.addActivity(Activity.IDLE, ImmutableList.of(
/*  93 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/*  94 */           Pair.of(Integer.valueOf(1), new AnimalMakeLove(EntityType.AXOLOTL, 0.2F, 2)), 
/*  95 */           Pair.of(Integer.valueOf(2), new RunOne((List)ImmutableList.of(
/*  96 */                 Pair.of(new FollowTemptation(AxolotlAi::getSpeedModifier), Integer.valueOf(1)), 
/*  97 */                 Pair.of(BabyFollowAdult.create(ADULT_FOLLOW_RANGE, AxolotlAi::getSpeedModifierFollowingAdult, MemoryModuleType.NEAREST_VISIBLE_ADULT, false), Integer.valueOf(1))))), 
/*     */           
/*  99 */           Pair.of(Integer.valueOf(3), StartAttacking.create(AxolotlAi::findNearestValidAttackTarget)), 
/* 100 */           Pair.of(Integer.valueOf(3), TryFindWater.create(6, 0.15F)), 
/* 101 */           Pair.of(Integer.valueOf(4), new GateBehavior(
/* 102 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */ 
/*     */               
/* 105 */               (Set)ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, 
/*     */ 
/*     */               
/* 108 */               (List)ImmutableList.of(
/* 109 */                 Pair.of(RandomStroll.swim(0.5F), Integer.valueOf(2)), 
/* 110 */                 Pair.of(RandomStroll.stroll(0.15F, false), Integer.valueOf(2)), 
/* 111 */                 Pair.of(SetWalkTargetFromLookTarget.create(AxolotlAi::canSetWalkTargetFromLookTarget, AxolotlAi::getSpeedModifier, 3), Integer.valueOf(3)), 
/* 112 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::isInWater), Integer.valueOf(5)), 
/* 113 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), Integer.valueOf(5)))))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean canSetWalkTargetFromLookTarget(LivingEntity paramLivingEntity) {
/* 120 */     Level level = paramLivingEntity.level();
/* 121 */     Optional<PositionTracker> optional = paramLivingEntity.getBrain().getMemory(MemoryModuleType.LOOK_TARGET);
/*     */     
/* 123 */     if (optional.isPresent()) {
/* 124 */       BlockPos blockPos = ((PositionTracker)optional.get()).currentBlockPosition();
/* 125 */       return (level.isWaterAt(blockPos) == paramLivingEntity.isInWater());
/*     */     } 
/*     */     
/* 128 */     return false;
/*     */   }
/*     */   
/*     */   public static void updateActivity(Axolotl paramAxolotl) {
/* 132 */     Brain<Axolotl> brain = paramAxolotl.getBrain();
/*     */     
/* 134 */     Activity activity = brain.getActiveNonCoreActivity().orElse(null);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 139 */     if (activity != Activity.PLAY_DEAD) {
/* 140 */       brain.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.PLAY_DEAD, Activity.FIGHT, Activity.IDLE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 147 */       if (activity == Activity.FIGHT && brain.getActiveNonCoreActivity().orElse(null) != Activity.FIGHT) {
/* 148 */         brain.setMemoryWithExpiry(MemoryModuleType.HAS_HUNTING_COOLDOWN, Boolean.valueOf(true), 2400L);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static float getSpeedModifierChasing(LivingEntity paramLivingEntity) {
/* 154 */     return paramLivingEntity.isInWater() ? 0.6F : 0.15F;
/*     */   }
/*     */   
/*     */   private static float getSpeedModifierFollowingAdult(LivingEntity paramLivingEntity) {
/* 158 */     return paramLivingEntity.isInWater() ? 0.6F : 0.15F;
/*     */   }
/*     */   
/*     */   private static float getSpeedModifier(LivingEntity paramLivingEntity) {
/* 162 */     return paramLivingEntity.isInWater() ? 0.5F : 0.15F;
/*     */   }
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel paramServerLevel, Axolotl paramAxolotl) {
/* 166 */     if (BehaviorUtils.isBreeding((LivingEntity)paramAxolotl)) {
/* 167 */       return Optional.empty();
/*     */     }
/*     */     
/* 170 */     return paramAxolotl.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\axolotl\AxolotlAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */