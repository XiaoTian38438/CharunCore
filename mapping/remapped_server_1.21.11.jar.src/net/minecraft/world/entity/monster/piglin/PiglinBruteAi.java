/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.core.GlobalPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWith;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.StopBeingAngryIfTargetDead;
/*     */ import net.minecraft.world.entity.ai.behavior.StrollAroundPoi;
/*     */ import net.minecraft.world.entity.ai.behavior.StrollToPoi;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PiglinBruteAi
/*     */ {
/*     */   private static final int ANGER_DURATION = 600;
/*     */   private static final int MELEE_ATTACK_COOLDOWN = 20;
/*     */   private static final double ACTIVITY_SOUND_LIKELIHOOD_PER_TICK = 0.0125D;
/*     */   private static final int MAX_LOOK_DIST = 8;
/*     */   private static final int INTERACTION_RANGE = 8;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.6F;
/*     */   private static final int HOME_CLOSE_ENOUGH_DISTANCE = 2;
/*     */   private static final int HOME_TOO_FAR_DISTANCE = 100;
/*     */   private static final int HOME_STROLL_AROUND_DISTANCE = 5;
/*     */   
/*     */   protected static Brain<?> makeBrain(PiglinBrute paramPiglinBrute, Brain<PiglinBrute> paramBrain) {
/*  54 */     initCoreActivity(paramPiglinBrute, paramBrain);
/*     */     
/*  56 */     initIdleActivity(paramPiglinBrute, paramBrain);
/*  57 */     initFightActivity(paramPiglinBrute, paramBrain);
/*     */     
/*  59 */     paramBrain.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/*  60 */     paramBrain.setDefaultActivity(Activity.IDLE);
/*  61 */     paramBrain.useDefaultActivity();
/*     */     
/*  63 */     return paramBrain;
/*     */   }
/*     */   
/*     */   protected static void initMemories(PiglinBrute paramPiglinBrute) {
/*  67 */     GlobalPos globalPos = GlobalPos.of(paramPiglinBrute.level().dimension(), paramPiglinBrute.blockPosition());
/*  68 */     paramPiglinBrute.getBrain().setMemory(MemoryModuleType.HOME, globalPos);
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(PiglinBrute paramPiglinBrute, Brain<PiglinBrute> paramBrain) {
/*  72 */     paramBrain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), 
/*     */ 
/*     */           
/*  75 */           InteractWithDoor.create(), 
/*  76 */           StopBeingAngryIfTargetDead.create()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(PiglinBrute paramPiglinBrute, Brain<PiglinBrute> paramBrain) {
/*  81 */     paramBrain.addActivity(Activity.IDLE, 10, ImmutableList.of(
/*  82 */           StartAttacking.create(PiglinBruteAi::findNearestValidAttackTarget), 
/*  83 */           createIdleLookBehaviors(), 
/*  84 */           createIdleMovementBehaviors(), 
/*  85 */           SetLookAndInteract.create(EntityType.PLAYER, 4)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initFightActivity(PiglinBrute paramPiglinBrute, Brain<PiglinBrute> paramBrain) {
/*  90 */     paramBrain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(
/*  91 */           StopAttackingIfTargetInvalid.create((paramServerLevel, paramLivingEntity) -> !isNearestValidAttackTarget(paramServerLevel, paramPiglinBrute, paramLivingEntity)), 
/*  92 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), 
/*  93 */           MeleeAttack.create(20)), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */ 
/*     */   
/*     */   private static RunOne<PiglinBrute> createIdleLookBehaviors() {
/*  98 */     return new RunOne((List)ImmutableList.of(
/*  99 */           Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), Integer.valueOf(1)), 
/* 100 */           Pair.of(SetEntityLookTarget.create(EntityType.PIGLIN, 8.0F), Integer.valueOf(1)), 
/* 101 */           Pair.of(SetEntityLookTarget.create(EntityType.PIGLIN_BRUTE, 8.0F), Integer.valueOf(1)), 
/* 102 */           Pair.of(SetEntityLookTarget.create(8.0F), Integer.valueOf(1)), 
/* 103 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static RunOne<PiglinBrute> createIdleMovementBehaviors() {
/* 108 */     return new RunOne((List)ImmutableList.of(
/* 109 */           Pair.of(RandomStroll.stroll(0.6F), Integer.valueOf(2)), 
/* 110 */           Pair.of(InteractWith.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), Integer.valueOf(2)), 
/* 111 */           Pair.of(InteractWith.of(EntityType.PIGLIN_BRUTE, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), Integer.valueOf(2)), 
/* 112 */           Pair.of(StrollToPoi.create(MemoryModuleType.HOME, 0.6F, 2, 100), Integer.valueOf(2)), 
/* 113 */           Pair.of(StrollAroundPoi.create(MemoryModuleType.HOME, 0.6F, 5), Integer.valueOf(2)), 
/* 114 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void updateActivity(PiglinBrute paramPiglinBrute) {
/* 119 */     Brain<PiglinBrute> brain = paramPiglinBrute.getBrain();
/*     */ 
/*     */ 
/*     */     
/* 123 */     Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);
/*     */ 
/*     */ 
/*     */     
/* 127 */     brain.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.FIGHT, Activity.IDLE));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 132 */     Activity activity2 = brain.getActiveNonCoreActivity().orElse(null);
/* 133 */     if (activity1 != activity2)
/*     */     {
/* 135 */       playActivitySound(paramPiglinBrute);
/*     */     }
/*     */ 
/*     */     
/* 139 */     paramPiglinBrute.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */   }
/*     */   
/*     */   private static boolean isNearestValidAttackTarget(ServerLevel paramServerLevel, AbstractPiglin paramAbstractPiglin, LivingEntity paramLivingEntity) {
/* 143 */     return findNearestValidAttackTarget(paramServerLevel, paramAbstractPiglin)
/* 144 */       .filter(paramLivingEntity2 -> (paramLivingEntity2 == paramLivingEntity1))
/* 145 */       .isPresent();
/*     */   }
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel paramServerLevel, AbstractPiglin paramAbstractPiglin) {
/* 149 */     Optional<LivingEntity> optional = BehaviorUtils.getLivingEntityFromUUIDMemory((LivingEntity)paramAbstractPiglin, MemoryModuleType.ANGRY_AT);
/* 150 */     if (optional.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(paramServerLevel, (LivingEntity)paramAbstractPiglin, optional.get())) {
/* 151 */       return optional;
/*     */     }
/*     */     
/* 154 */     Optional<? extends LivingEntity> optional1 = paramAbstractPiglin.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
/* 155 */     if (optional1.isPresent()) {
/* 156 */       return optional1;
/*     */     }
/*     */     
/* 159 */     return paramAbstractPiglin.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void wasHurtBy(ServerLevel paramServerLevel, PiglinBrute paramPiglinBrute, LivingEntity paramLivingEntity) {
/* 164 */     if (paramLivingEntity instanceof AbstractPiglin) {
/*     */       return;
/*     */     }
/*     */     
/* 168 */     PiglinAi.maybeRetaliate(paramServerLevel, paramPiglinBrute, paramLivingEntity);
/*     */   }
/*     */   
/*     */   protected static void setAngerTarget(PiglinBrute paramPiglinBrute, LivingEntity paramLivingEntity) {
/* 172 */     paramPiglinBrute.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 173 */     paramPiglinBrute.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, paramLivingEntity.getUUID(), 600L);
/*     */   }
/*     */   
/*     */   protected static void maybePlayActivitySound(PiglinBrute paramPiglinBrute) {
/* 177 */     if ((paramPiglinBrute.level()).random.nextFloat() < 0.0125D) {
/* 178 */       playActivitySound(paramPiglinBrute);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static void playActivitySound(PiglinBrute paramPiglinBrute) {
/* 184 */     paramPiglinBrute.getBrain().getActiveNonCoreActivity().ifPresent(paramActivity -> {
/*     */           if (paramActivity == Activity.FIGHT)
/*     */             paramPiglinBrute.playAngrySound(); 
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\piglin\PiglinBruteAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */