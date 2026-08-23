/*     */ package net.minecraft.world.entity.animal.allay;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.AnimalPanic;
/*     */ import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.CountDownCooldownTicks;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.EntityTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.GoAndGiveItemsToTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.GoToWantedItem;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.PositionTracker;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.StayCloseToTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.Swim;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ 
/*     */ public class AllayAi {
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_FOLLOWING_DEPOSIT_TARGET = 2.25F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_RETRIEVING_ITEM = 1.75F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_PANICKING = 2.5F;
/*     */   private static final int CLOSE_ENOUGH_TO_TARGET = 4;
/*     */   private static final int TOO_FAR_FROM_TARGET = 16;
/*     */   private static final int MAX_LOOK_DISTANCE = 6;
/*     */   private static final int MIN_WAIT_DURATION = 30;
/*     */   private static final int MAX_WAIT_DURATION = 60;
/*     */   private static final int TIME_TO_FORGET_NOTEBLOCK = 600;
/*     */   private static final int DISTANCE_TO_WANTED_ITEM = 32;
/*     */   private static final int GIVE_ITEM_TIMEOUT_DURATION = 20;
/*     */   
/*     */   protected static Brain<?> makeBrain(Brain<Allay> paramBrain) {
/*  55 */     initCoreActivity(paramBrain);
/*  56 */     initIdleActivity(paramBrain);
/*     */     
/*  58 */     paramBrain.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/*  59 */     paramBrain.setDefaultActivity(Activity.IDLE);
/*  60 */     paramBrain.useDefaultActivity();
/*  61 */     return paramBrain;
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(Brain<Allay> paramBrain) {
/*  65 */     paramBrain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8F), new AnimalPanic(2.5F), new LookAtTargetSink(45, 90), new MoveToTargetSink(), new CountDownCooldownTicks(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS), new CountDownCooldownTicks(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Allay> paramBrain) {
/*  76 */     paramBrain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(
/*  77 */           Pair.of(Integer.valueOf(0), GoToWantedItem.create(paramAllay -> true, 1.75F, true, 32)), 
/*  78 */           Pair.of(Integer.valueOf(1), new GoAndGiveItemsToTarget(AllayAi::getItemDepositPosition, 2.25F, 20)), 
/*  79 */           Pair.of(Integer.valueOf(2), StayCloseToTarget.create(AllayAi::getItemDepositPosition, Predicate.not(AllayAi::hasWantedItem), 4, 16, 2.25F)), 
/*  80 */           Pair.of(Integer.valueOf(3), SetEntityLookTargetSometimes.create(6.0F, UniformInt.of(30, 60))), 
/*  81 */           Pair.of(Integer.valueOf(4), new RunOne((List)ImmutableList.of(
/*  82 */                 Pair.of(RandomStroll.fly(1.0F), Integer.valueOf(2)), 
/*  83 */                 Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), Integer.valueOf(2)), 
/*  84 */                 Pair.of(new DoNothing(30, 60), Integer.valueOf(1)))))), 
/*     */         
/*  86 */         (Set)ImmutableSet.of());
/*     */   }
/*     */ 
/*     */   
/*     */   public static void updateActivity(Allay paramAllay) {
/*  91 */     paramAllay.getBrain().setActiveActivityToFirstValid((List)ImmutableList.of(Activity.IDLE));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void hearNoteblock(LivingEntity paramLivingEntity, BlockPos paramBlockPos) {
/*  97 */     Brain brain = paramLivingEntity.getBrain();
/*  98 */     GlobalPos globalPos = GlobalPos.of(paramLivingEntity.level().dimension(), paramBlockPos);
/*  99 */     Optional<GlobalPos> optional = brain.getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
/* 100 */     if (optional.isEmpty()) {
/* 101 */       brain.setMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION, globalPos);
/* 102 */       brain.setMemory(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, Integer.valueOf(600));
/* 103 */     } else if (((GlobalPos)optional.get()).equals(globalPos)) {
/* 104 */       brain.setMemory(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, Integer.valueOf(600));
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Optional<PositionTracker> getItemDepositPosition(LivingEntity paramLivingEntity) {
/* 109 */     Brain<?> brain = paramLivingEntity.getBrain();
/*     */     
/* 111 */     Optional<GlobalPos> optional = brain.getMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
/* 112 */     if (optional.isPresent()) {
/* 113 */       GlobalPos globalPos = optional.get();
/* 114 */       if (shouldDepositItemsAtLikedNoteblock(paramLivingEntity, brain, globalPos)) {
/* 115 */         return (Optional)Optional.of(new BlockPosTracker(globalPos.pos().above()));
/*     */       }
/* 117 */       brain.eraseMemory(MemoryModuleType.LIKED_NOTEBLOCK_POSITION);
/*     */     } 
/*     */     
/* 120 */     return getLikedPlayerPositionTracker(paramLivingEntity);
/*     */   }
/*     */   
/*     */   private static boolean hasWantedItem(LivingEntity paramLivingEntity) {
/* 124 */     Brain brain = paramLivingEntity.getBrain();
/* 125 */     return brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM);
/*     */   }
/*     */   
/*     */   private static boolean shouldDepositItemsAtLikedNoteblock(LivingEntity paramLivingEntity, Brain<?> paramBrain, GlobalPos paramGlobalPos) {
/* 129 */     Optional optional = paramBrain.getMemory(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS);
/* 130 */     Level level = paramLivingEntity.level();
/* 131 */     return (paramGlobalPos.isCloseEnough(level.dimension(), paramLivingEntity.blockPosition(), 1024) && level
/* 132 */       .getBlockState(paramGlobalPos.pos()).is(Blocks.NOTE_BLOCK) && optional
/* 133 */       .isPresent());
/*     */   }
/*     */   
/*     */   private static Optional<PositionTracker> getLikedPlayerPositionTracker(LivingEntity paramLivingEntity) {
/* 137 */     return getLikedPlayer(paramLivingEntity).map(paramServerPlayer -> new EntityTracker((Entity)paramServerPlayer, true));
/*     */   }
/*     */   
/*     */   public static Optional<ServerPlayer> getLikedPlayer(LivingEntity paramLivingEntity) {
/* 141 */     Level level = paramLivingEntity.level();
/* 142 */     if (!level.isClientSide() && level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 143 */       Optional<UUID> optional = paramLivingEntity.getBrain().getMemory(MemoryModuleType.LIKED_PLAYER);
/* 144 */       if (optional.isPresent()) {
/* 145 */         Entity entity = serverLevel.getEntity(optional.get());
/*     */         
/* 147 */         if (entity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)entity; if ((serverPlayer.gameMode
/* 148 */             .isSurvival() || serverPlayer.gameMode.isCreative()) && serverPlayer
/* 149 */             .closerThan((Entity)paramLivingEntity, 64.0D))
/*     */           {
/* 151 */             return Optional.of(serverPlayer); }  }
/*     */         
/* 153 */         return Optional.empty();
/*     */       }  }
/*     */ 
/*     */     
/* 157 */     return Optional.empty();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\allay\AllayAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */