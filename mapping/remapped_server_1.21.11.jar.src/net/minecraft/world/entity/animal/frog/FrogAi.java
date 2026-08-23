/*     */ package net.minecraft.world.entity.animal.frog;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.Croak;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.GateBehavior;
/*     */ import net.minecraft.world.entity.ai.behavior.LongJumpMidJump;
/*     */ import net.minecraft.world.entity.ai.behavior.LongJumpToPreferredBlock;
/*     */ import net.minecraft.world.entity.ai.behavior.LongJumpToRandomPos;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.TryFindLand;
/*     */ import net.minecraft.world.entity.ai.behavior.TryFindLandNearWater;
/*     */ import net.minecraft.world.entity.ai.behavior.TryLaySpawnOnWaterNearLand;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.pathfinder.PathfindingContext;
/*     */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*     */ 
/*     */ public class FrogAi {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
/*  58 */   private static final UniformInt TIME_BETWEEN_LONG_JUMPS = UniformInt.of(100, 140); private static final float SPEED_MULTIPLIER_ON_LAND = 1.0F; private static final float SPEED_MULTIPLIER_IN_WATER = 0.75F;
/*     */   private static final int MAX_LONG_JUMP_HEIGHT = 2;
/*     */   private static final int MAX_LONG_JUMP_WIDTH = 4;
/*     */   private static final float MAX_JUMP_VELOCITY_MULTIPLIER = 3.5714288F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
/*     */   
/*     */   protected static void initMemories(Frog paramFrog, RandomSource paramRandomSource) {
/*  65 */     paramFrog.getBrain().setMemory(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, Integer.valueOf(TIME_BETWEEN_LONG_JUMPS.sample(paramRandomSource)));
/*     */   }
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<Frog> paramBrain) {
/*  69 */     initCoreActivity(paramBrain);
/*  70 */     initIdleActivity(paramBrain);
/*  71 */     initSwimActivity(paramBrain);
/*  72 */     initLaySpawnActivity(paramBrain);
/*  73 */     initTongueActivity(paramBrain);
/*  74 */     initJumpActivity(paramBrain);
/*     */     
/*  76 */     paramBrain.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/*  77 */     paramBrain.setDefaultActivity(Activity.IDLE);
/*  78 */     paramBrain.useDefaultActivity();
/*  79 */     return paramBrain;
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(Brain<Frog> paramBrain) {
/*  83 */     paramBrain.addActivity(Activity.CORE, 0, ImmutableList.of(new AnimalPanic(2.0F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Frog> paramBrain) {
/*  93 */     paramBrain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(
/*  94 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/*  95 */           Pair.of(Integer.valueOf(0), new AnimalMakeLove(EntityType.FROG)), 
/*  96 */           Pair.of(Integer.valueOf(1), new FollowTemptation(paramLivingEntity -> Float.valueOf(1.25F))), 
/*  97 */           Pair.of(Integer.valueOf(2), StartAttacking.create((paramServerLevel, paramFrog) -> canAttack(paramFrog), (paramServerLevel, paramFrog) -> paramFrog.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE))), 
/*  98 */           Pair.of(Integer.valueOf(3), TryFindLand.create(6, 1.0F)), 
/*  99 */           Pair.of(Integer.valueOf(4), new RunOne(
/* 100 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */ 
/*     */               
/* 103 */               (List)ImmutableList.of(
/* 104 */                 Pair.of(RandomStroll.stroll(1.0F), Integer.valueOf(1)), 
/* 105 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(1)), 
/* 106 */                 Pair.of(new Croak(), Integer.valueOf(3)), 
/* 107 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), Integer.valueOf(2)))))), 
/*     */ 
/*     */         
/* 110 */         (Set)ImmutableSet.of(
/* 111 */           Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT), 
/* 112 */           Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initSwimActivity(Brain<Frog> paramBrain) {
/* 117 */     paramBrain.addActivityWithConditions(Activity.SWIM, ImmutableList.of(
/* 118 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/* 119 */           Pair.of(Integer.valueOf(1), new FollowTemptation(paramLivingEntity -> Float.valueOf(1.25F))), 
/* 120 */           Pair.of(Integer.valueOf(2), StartAttacking.create((paramServerLevel, paramFrog) -> canAttack(paramFrog), (paramServerLevel, paramFrog) -> paramFrog.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE))), 
/* 121 */           Pair.of(Integer.valueOf(3), TryFindLand.create(8, 1.5F)), 
/* 122 */           Pair.of(Integer.valueOf(5), new GateBehavior(
/* 123 */               (Map)ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/*     */ 
/*     */               
/* 126 */               (Set)ImmutableSet.of(), GateBehavior.OrderPolicy.ORDERED, GateBehavior.RunningPolicy.TRY_ALL, 
/*     */ 
/*     */               
/* 129 */               (List)ImmutableList.of(
/* 130 */                 Pair.of(RandomStroll.swim(0.75F), Integer.valueOf(1)), 
/* 131 */                 Pair.of(RandomStroll.stroll(1.0F, true), Integer.valueOf(1)), 
/* 132 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(1)), 
/* 133 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::isInWater), Integer.valueOf(5)))))), 
/*     */ 
/*     */         
/* 136 */         (Set)ImmutableSet.of(
/* 137 */           Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT), 
/* 138 */           Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_PRESENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initLaySpawnActivity(Brain<Frog> paramBrain) {
/* 143 */     paramBrain.addActivityWithConditions(Activity.LAY_SPAWN, ImmutableList.of(
/* 144 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/* 145 */           Pair.of(Integer.valueOf(1), StartAttacking.create((paramServerLevel, paramFrog) -> canAttack(paramFrog), (paramServerLevel, paramFrog) -> paramFrog.getBrain().getMemory(MemoryModuleType.NEAREST_ATTACKABLE))), 
/* 146 */           Pair.of(Integer.valueOf(2), TryFindLandNearWater.create(8, 1.0F)), 
/* 147 */           Pair.of(Integer.valueOf(3), TryLaySpawnOnWaterNearLand.create(Blocks.FROGSPAWN)), 
/* 148 */           Pair.of(Integer.valueOf(4), new RunOne(
/* 149 */               (List)ImmutableList.of(
/* 150 */                 Pair.of(RandomStroll.stroll(1.0F), Integer.valueOf(2)), 
/* 151 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(1)), 
/* 152 */                 Pair.of(new Croak(), Integer.valueOf(2)), 
/* 153 */                 Pair.of(BehaviorBuilder.triggerIf(Entity::onGround), Integer.valueOf(1)))))), 
/*     */ 
/*     */         
/* 156 */         (Set)ImmutableSet.of(
/* 157 */           Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT), 
/* 158 */           Pair.of(MemoryModuleType.IS_PREGNANT, MemoryStatus.VALUE_PRESENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initJumpActivity(Brain<Frog> paramBrain) {
/* 163 */     paramBrain.addActivityWithConditions(Activity.LONG_JUMP, ImmutableList.of(
/* 164 */           Pair.of(Integer.valueOf(0), new LongJumpMidJump(TIME_BETWEEN_LONG_JUMPS, SoundEvents.FROG_STEP)), 
/* 165 */           Pair.of(Integer.valueOf(1), new LongJumpToPreferredBlock(TIME_BETWEEN_LONG_JUMPS, 2, 4, 3.5714288F, paramFrog -> SoundEvents.FROG_LONG_JUMP, BlockTags.FROG_PREFER_JUMP_TO, 0.5F, FrogAi::isAcceptableLandingSpot))), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 175 */         (Set)ImmutableSet.of(
/* 176 */           Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT), 
/* 177 */           Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 178 */           Pair.of(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT), 
/* 179 */           Pair.of(MemoryModuleType.IS_IN_WATER, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initTongueActivity(Brain<Frog> paramBrain) {
/* 184 */     paramBrain.addActivityAndRemoveMemoryWhenStopped(Activity.TONGUE, 0, ImmutableList.of(
/* 185 */           StopAttackingIfTargetInvalid.create(), new ShootTongue(SoundEvents.FROG_TONGUE, SoundEvents.FROG_EAT)), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static <E extends Mob> boolean isAcceptableLandingSpot(E paramE, BlockPos paramBlockPos) {
/* 191 */     Level level = paramE.level();
/* 192 */     BlockPos blockPos = paramBlockPos.below();
/* 193 */     if (!level.getFluidState(paramBlockPos).isEmpty() || 
/* 194 */       !level.getFluidState(blockPos).isEmpty() || 
/* 195 */       !level.getFluidState(paramBlockPos.above()).isEmpty()) {
/* 196 */       return false;
/*     */     }
/* 198 */     BlockState blockState1 = level.getBlockState(paramBlockPos);
/* 199 */     BlockState blockState2 = level.getBlockState(blockPos);
/* 200 */     if (blockState1.is(BlockTags.FROG_PREFER_JUMP_TO) || blockState2.is(BlockTags.FROG_PREFER_JUMP_TO)) {
/* 201 */       return true;
/*     */     }
/* 203 */     PathfindingContext pathfindingContext = new PathfindingContext((CollisionGetter)paramE.level(), (Mob)paramE);
/* 204 */     PathType pathType1 = WalkNodeEvaluator.getPathTypeStatic(pathfindingContext, paramBlockPos.mutable());
/* 205 */     PathType pathType2 = WalkNodeEvaluator.getPathTypeStatic(pathfindingContext, blockPos.mutable());
/* 206 */     if (pathType1 == PathType.TRAPDOOR || (blockState1.isAir() && pathType2 == PathType.TRAPDOOR)) {
/* 207 */       return true;
/*     */     }
/* 209 */     return LongJumpToRandomPos.defaultAcceptableLandingSpot((Mob)paramE, paramBlockPos);
/*     */   }
/*     */   
/*     */   private static boolean canAttack(Frog paramFrog) {
/* 213 */     return !BehaviorUtils.isBreeding((LivingEntity)paramFrog);
/*     */   }
/*     */   
/*     */   public static void updateActivity(Frog paramFrog) {
/* 217 */     paramFrog.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.TONGUE, Activity.LAY_SPAWN, Activity.LONG_JUMP, Activity.SWIM, Activity.IDLE));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Predicate<ItemStack> getTemptations() {
/* 227 */     return paramItemStack -> paramItemStack.is(ItemTags.FROG_FOOD);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\frog\FrogAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */