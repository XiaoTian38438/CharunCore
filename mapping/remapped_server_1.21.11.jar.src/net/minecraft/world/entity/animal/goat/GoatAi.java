/*     */ package net.minecraft.world.entity.animal.goat;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalMakeLove;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.BabyFollowAdult;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.FollowTemptation;
/*     */ import net.minecraft.world.entity.ai.behavior.LongJumpMidJump;
/*     */ import net.minecraft.world.entity.ai.behavior.LongJumpToRandomPos;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.PrepareRamNearestTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ 
/*     */ public class GoatAi {
/*     */   public static final int RAM_PREPARE_TIME = 20;
/*  38 */   private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16); public static final int RAM_MAX_DISTANCE = 7;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_ADULT = 1.25F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_TEMPTED = 1.25F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PREPARING_TO_RAM = 1.25F;
/*  44 */   private static final UniformInt TIME_BETWEEN_LONG_JUMPS = UniformInt.of(600, 1200);
/*     */   public static final int MAX_LONG_JUMP_HEIGHT = 5;
/*     */   public static final int MAX_LONG_JUMP_WIDTH = 5;
/*     */   public static final float MAX_JUMP_VELOCITY_MULTIPLIER = 3.5714288F;
/*  48 */   private static final UniformInt TIME_BETWEEN_RAMS = UniformInt.of(600, 6000);
/*  49 */   private static final UniformInt TIME_BETWEEN_RAMS_SCREAMER = UniformInt.of(100, 300); private static final TargetingConditions RAM_TARGET_CONDITIONS; private static final float SPEED_MULTIPLIER_WHEN_RAMMING = 3.0F; static {
/*  50 */     RAM_TARGET_CONDITIONS = TargetingConditions.forCombat().selector((paramLivingEntity, paramServerLevel) -> 
/*  51 */         (!paramLivingEntity.getType().equals(EntityType.GOAT) && (((Boolean)paramServerLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() || !paramLivingEntity.getType().equals(EntityType.ARMOR_STAND)) && paramServerLevel.getWorldBorder().isWithinBounds(paramLivingEntity.getBoundingBox())));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int RAM_MIN_DISTANCE = 4;
/*     */   
/*     */   public static final float ADULT_RAM_KNOCKBACK_FORCE = 2.5F;
/*     */   public static final float BABY_RAM_KNOCKBACK_FORCE = 1.0F;
/*     */   
/*     */   protected static void initMemories(Goat paramGoat, RandomSource paramRandomSource) {
/*  61 */     paramGoat.getBrain().setMemory(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, Integer.valueOf(TIME_BETWEEN_LONG_JUMPS.sample(paramRandomSource)));
/*  62 */     paramGoat.getBrain().setMemory(MemoryModuleType.RAM_COOLDOWN_TICKS, Integer.valueOf(TIME_BETWEEN_RAMS.sample(paramRandomSource)));
/*     */   }
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<Goat> paramBrain) {
/*  66 */     initCoreActivity(paramBrain);
/*  67 */     initIdleActivity(paramBrain);
/*  68 */     initLongJumpActivity(paramBrain);
/*  69 */     initRamActivity(paramBrain);
/*     */     
/*  71 */     paramBrain.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/*  72 */     paramBrain.setDefaultActivity(Activity.IDLE);
/*  73 */     paramBrain.useDefaultActivity();
/*  74 */     return paramBrain;
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(Brain<Goat> paramBrain) {
/*  78 */     paramBrain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new AnimalPanic(2.0F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.RAM_COOLDOWN_TICKS)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Goat> paramBrain) {
/*  90 */     paramBrain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(
/*  91 */           Pair.of(Integer.valueOf(0), SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))), 
/*  92 */           Pair.of(Integer.valueOf(0), new AnimalMakeLove(EntityType.GOAT)), 
/*  93 */           Pair.of(Integer.valueOf(1), new FollowTemptation(paramLivingEntity -> Float.valueOf(1.25F))), 
/*  94 */           Pair.of(Integer.valueOf(2), BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1.25F)), 
/*  95 */           Pair.of(Integer.valueOf(3), new RunOne((List)ImmutableList.of(
/*  96 */                 Pair.of(RandomStroll.stroll(1.0F), Integer.valueOf(2)), 
/*  97 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(2)), 
/*  98 */                 Pair.of(new DoNothing(30, 60), Integer.valueOf(1)))))), 
/*     */         
/* 100 */         (Set)ImmutableSet.of(
/* 101 */           Pair.of(MemoryModuleType.RAM_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 102 */           Pair.of(MemoryModuleType.LONG_JUMP_MID_JUMP, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initLongJumpActivity(Brain<Goat> paramBrain) {
/* 107 */     paramBrain.addActivityWithConditions(Activity.LONG_JUMP, ImmutableList.of(
/* 108 */           Pair.of(Integer.valueOf(0), new LongJumpMidJump(TIME_BETWEEN_LONG_JUMPS, SoundEvents.GOAT_STEP)), 
/* 109 */           Pair.of(Integer.valueOf(1), new LongJumpToRandomPos(TIME_BETWEEN_LONG_JUMPS, 5, 5, 3.5714288F, paramGoat -> paramGoat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_LONG_JUMP : SoundEvents.GOAT_LONG_JUMP))), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 116 */         (Set)ImmutableSet.of(
/* 117 */           Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT), 
/* 118 */           Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 119 */           Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 120 */           Pair.of(MemoryModuleType.LONG_JUMP_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initRamActivity(Brain<Goat> paramBrain) {
/* 125 */     paramBrain.addActivityWithConditions(Activity.RAM, ImmutableList.of(
/* 126 */           Pair.of(Integer.valueOf(0), new RamTarget(paramGoat -> paramGoat.isScreamingGoat() ? TIME_BETWEEN_RAMS_SCREAMER : TIME_BETWEEN_RAMS, RAM_TARGET_CONDITIONS, 3.0F, paramGoat -> paramGoat.isBaby() ? 1.0D : 2.5D, paramGoat -> paramGoat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_RAM_IMPACT : SoundEvents.GOAT_RAM_IMPACT, paramGoat -> SoundEvents.GOAT_HORN_BREAK)), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 133 */           Pair.of(Integer.valueOf(1), new PrepareRamNearestTarget(paramGoat -> paramGoat.isScreamingGoat() ? TIME_BETWEEN_RAMS_SCREAMER.getMinValue() : TIME_BETWEEN_RAMS.getMinValue(), 4, 7, 1.25F, RAM_TARGET_CONDITIONS, 20, paramGoat -> paramGoat.isScreamingGoat() ? SoundEvents.GOAT_SCREAMING_PREPARE_RAM : SoundEvents.GOAT_PREPARE_RAM))), 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 141 */         (Set)ImmutableSet.of(
/* 142 */           Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT), 
/* 143 */           Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT), 
/* 144 */           Pair.of(MemoryModuleType.RAM_COOLDOWN_TICKS, MemoryStatus.VALUE_ABSENT)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void updateActivity(Goat paramGoat) {
/* 149 */     paramGoat.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.RAM, Activity.LONG_JUMP, Activity.IDLE));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\goat\GoatAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */