/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.EquipmentSlotGroup;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.behavior.BackUpIfTooClose;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.behavior.CopyMemoryWithExpiry;
/*     */ import net.minecraft.world.entity.ai.behavior.CrossbowAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.DismountOrSkipMounting;
/*     */ import net.minecraft.world.entity.ai.behavior.DoNothing;
/*     */ import net.minecraft.world.entity.ai.behavior.EraseMemoryIf;
/*     */ import net.minecraft.world.entity.ai.behavior.GoToTargetLocation;
/*     */ import net.minecraft.world.entity.ai.behavior.GoToWantedItem;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWith;
/*     */ import net.minecraft.world.entity.ai.behavior.InteractWithDoor;
/*     */ import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.MeleeAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.Mount;
/*     */ import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
/*     */ import net.minecraft.world.entity.ai.behavior.OneShot;
/*     */ import net.minecraft.world.entity.ai.behavior.RandomStroll;
/*     */ import net.minecraft.world.entity.ai.behavior.RunOne;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SetEntityLookTargetSometimes;
/*     */ import net.minecraft.world.entity.ai.behavior.SetLookAndInteract;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetAwayFrom;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromAttackTargetIfTargetOutOfReach;
/*     */ import net.minecraft.world.entity.ai.behavior.SetWalkTargetFromLookTarget;
/*     */ import net.minecraft.world.entity.ai.behavior.SpearApproach;
/*     */ import net.minecraft.world.entity.ai.behavior.SpearAttack;
/*     */ import net.minecraft.world.entity.ai.behavior.SpearRetreat;
/*     */ import net.minecraft.world.entity.ai.behavior.StartAttacking;
/*     */ import net.minecraft.world.entity.ai.behavior.StartCelebratingIfTargetDead;
/*     */ import net.minecraft.world.entity.ai.behavior.StopAttackingIfTargetInvalid;
/*     */ import net.minecraft.world.entity.ai.behavior.StopBeingAngryIfTargetDead;
/*     */ import net.minecraft.world.entity.ai.behavior.TriggerGate;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*     */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.schedule.Activity;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PiglinAi
/*     */ {
/*     */   public static final int REPELLENT_DETECTION_RANGE_HORIZONTAL = 8;
/*     */   public static final int REPELLENT_DETECTION_RANGE_VERTICAL = 4;
/*  88 */   public static final Item BARTERING_ITEM = Items.GOLD_INGOT;
/*     */   
/*     */   private static final int PLAYER_ANGER_RANGE = 16;
/*     */   private static final int ANGER_DURATION = 600;
/*     */   private static final int ADMIRE_DURATION = 119;
/*     */   private static final int MAX_DISTANCE_TO_WALK_TO_ITEM = 9;
/*     */   private static final int MAX_TIME_TO_WALK_TO_ITEM = 200;
/*     */   private static final int HOW_LONG_TIME_TO_DISABLE_ADMIRE_WALKING_IF_CANT_REACH_ITEM = 200;
/*     */   private static final int CELEBRATION_TIME = 300;
/*  97 */   protected static final UniformInt TIME_BETWEEN_HUNTS = TimeUtil.rangeOfSeconds(30, 120);
/*     */   private static final int BABY_FLEE_DURATION_AFTER_GETTING_HIT = 100;
/*     */   private static final int HIT_BY_PLAYER_MEMORY_TIMEOUT = 400;
/*     */   private static final int MAX_WALK_DISTANCE_TO_START_RIDING = 8;
/* 101 */   private static final UniformInt RIDE_START_INTERVAL = TimeUtil.rangeOfSeconds(10, 40);
/* 102 */   private static final UniformInt RIDE_DURATION = TimeUtil.rangeOfSeconds(10, 30);
/* 103 */   private static final UniformInt RETREAT_DURATION = TimeUtil.rangeOfSeconds(5, 20);
/*     */   private static final int MELEE_ATTACK_COOLDOWN = 20;
/*     */   private static final int EAT_COOLDOWN = 200;
/*     */   private static final int DESIRED_DISTANCE_FROM_ENTITY_WHEN_AVOIDING = 12;
/*     */   private static final int MAX_LOOK_DIST = 8;
/*     */   private static final int MAX_LOOK_DIST_FOR_PLAYER_HOLDING_LOVED_ITEM = 14;
/*     */   private static final int INTERACTION_RANGE = 8;
/*     */   private static final int MIN_DESIRED_DIST_FROM_TARGET_WHEN_HOLDING_CROSSBOW = 5;
/*     */   private static final float SPEED_WHEN_STRAFING_BACK_FROM_TARGET = 0.75F;
/*     */   private static final int DESIRED_DISTANCE_FROM_ZOMBIFIED = 6;
/* 113 */   private static final UniformInt AVOID_ZOMBIFIED_DURATION = TimeUtil.rangeOfSeconds(5, 7);
/* 114 */   private static final UniformInt BABY_AVOID_NEMESIS_DURATION = TimeUtil.rangeOfSeconds(5, 7);
/*     */   
/*     */   private static final float PROBABILITY_OF_CELEBRATION_DANCE = 0.1F;
/*     */   
/*     */   private static final float SPEED_MULTIPLIER_WHEN_AVOIDING = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_RETREATING = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_MOUNTING = 0.8F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_GOING_TO_WANTED_ITEM = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_GOING_TO_CELEBRATE_LOCATION = 1.0F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_DANCING = 0.6F;
/*     */   private static final float SPEED_MULTIPLIER_WHEN_IDLING = 0.6F;
/*     */   
/*     */   protected static Brain<?> makeBrain(Piglin paramPiglin, Brain<Piglin> paramBrain) {
/* 127 */     initCoreActivity(paramBrain);
/*     */     
/* 129 */     initIdleActivity(paramBrain);
/*     */     
/* 131 */     initAdmireItemActivity(paramBrain);
/*     */     
/* 133 */     initFightActivity(paramPiglin, paramBrain);
/* 134 */     initCelebrateActivity(paramBrain);
/*     */     
/* 136 */     initRetreatActivity(paramBrain);
/* 137 */     initRideHoglinActivity(paramBrain);
/*     */     
/* 139 */     paramBrain.setCoreActivities((Set)ImmutableSet.of(Activity.CORE));
/* 140 */     paramBrain.setDefaultActivity(Activity.IDLE);
/* 141 */     paramBrain.useDefaultActivity();
/*     */     
/* 143 */     return paramBrain;
/*     */   }
/*     */   
/*     */   protected static void initMemories(Piglin paramPiglin, RandomSource paramRandomSource) {
/* 147 */     int i = TIME_BETWEEN_HUNTS.sample(paramRandomSource);
/* 148 */     paramPiglin.getBrain().setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, Boolean.valueOf(true), i);
/*     */   }
/*     */   
/*     */   private static void initCoreActivity(Brain<Piglin> paramBrain) {
/* 152 */     paramBrain.addActivity(Activity.CORE, 0, ImmutableList.of(new LookAtTargetSink(45, 90), new MoveToTargetSink(), 
/*     */ 
/*     */           
/* 155 */           InteractWithDoor.create(), 
/* 156 */           babyAvoidNemesis(), 
/* 157 */           avoidZombified(), 
/* 158 */           StopHoldingItemIfNoLongerAdmiring.create(), 
/* 159 */           StartAdmiringItemIfSeen.create(119), 
/* 160 */           StartCelebratingIfTargetDead.create(300, PiglinAi::wantsToDance), 
/* 161 */           StopBeingAngryIfTargetDead.create()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initIdleActivity(Brain<Piglin> paramBrain) {
/* 166 */     paramBrain.addActivity(Activity.IDLE, 10, ImmutableList.of(
/* 167 */           SetEntityLookTarget.create(PiglinAi::isPlayerHoldingLovedItem, 14.0F), 
/* 168 */           StartAttacking.create((paramServerLevel, paramPiglin) -> paramPiglin.isAdult(), PiglinAi::findNearestValidAttackTarget), 
/* 169 */           BehaviorBuilder.triggerIf(Piglin::canHunt, StartHuntingHoglin.create()), 
/* 170 */           avoidRepellent(), 
/* 171 */           babySometimesRideBabyHoglin(), 
/* 172 */           createIdleLookBehaviors(), 
/* 173 */           createIdleMovementBehaviors(), 
/* 174 */           SetLookAndInteract.create(EntityType.PLAYER, 4)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initFightActivity(Piglin paramPiglin, Brain<Piglin> paramBrain) {
/* 179 */     paramBrain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(
/* 180 */           StopAttackingIfTargetInvalid.create((paramServerLevel, paramLivingEntity) -> !isNearestValidAttackTarget(paramServerLevel, paramPiglin, paramLivingEntity)), 
/* 181 */           BehaviorBuilder.triggerIf(PiglinAi::hasCrossbow, BackUpIfTooClose.create(5, 0.75F)), 
/* 182 */           SetWalkTargetFromAttackTargetIfTargetOutOfReach.create(1.0F), new SpearApproach(1.0D, 10.0F), new SpearAttack(1.0D, 1.0D, 10.0F, 2.0F), new SpearRetreat(1.0D), 
/*     */ 
/*     */ 
/*     */           
/* 186 */           MeleeAttack.create(20), new CrossbowAttack(), 
/*     */           
/* 188 */           RememberIfHoglinWasKilled.create(), 
/* 189 */           EraseMemoryIf.create(PiglinAi::isNearZombified, MemoryModuleType.ATTACK_TARGET)), MemoryModuleType.ATTACK_TARGET);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initCelebrateActivity(Brain<Piglin> paramBrain) {
/* 194 */     paramBrain.addActivityAndRemoveMemoryWhenStopped(Activity.CELEBRATE, 10, ImmutableList.of(
/* 195 */           avoidRepellent(), 
/* 196 */           SetEntityLookTarget.create(PiglinAi::isPlayerHoldingLovedItem, 14.0F), 
/* 197 */           StartAttacking.create((paramServerLevel, paramPiglin) -> paramPiglin.isAdult(), PiglinAi::findNearestValidAttackTarget), 
/* 198 */           BehaviorBuilder.triggerIf(paramPiglin -> !paramPiglin.isDancing(), GoToTargetLocation.create(MemoryModuleType.CELEBRATE_LOCATION, 2, 1.0F)), 
/* 199 */           BehaviorBuilder.triggerIf(Piglin::isDancing, GoToTargetLocation.create(MemoryModuleType.CELEBRATE_LOCATION, 4, 0.6F)), new RunOne(
/* 200 */             (List)ImmutableList.of(
/* 201 */               Pair.of(SetEntityLookTarget.create(EntityType.PIGLIN, 8.0F), Integer.valueOf(1)), 
/* 202 */               Pair.of(RandomStroll.stroll(0.6F, 2, 1), Integer.valueOf(1)), 
/* 203 */               Pair.of(new DoNothing(10, 20), Integer.valueOf(1))))), MemoryModuleType.CELEBRATE_LOCATION);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initAdmireItemActivity(Brain<Piglin> paramBrain) {
/* 209 */     paramBrain.addActivityAndRemoveMemoryWhenStopped(Activity.ADMIRE_ITEM, 10, ImmutableList.of(
/* 210 */           GoToWantedItem.create(PiglinAi::isNotHoldingLovedItemInOffHand, 1.0F, true, 9), 
/* 211 */           StopAdmiringIfItemTooFarAway.create(9), 
/* 212 */           StopAdmiringIfTiredOfTryingToReachItem.create(200, 200)), MemoryModuleType.ADMIRING_ITEM);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initRetreatActivity(Brain<Piglin> paramBrain) {
/* 217 */     paramBrain.addActivityAndRemoveMemoryWhenStopped(Activity.AVOID, 10, ImmutableList.of(
/* 218 */           SetWalkTargetAwayFrom.entity(MemoryModuleType.AVOID_TARGET, 1.0F, 12, true), 
/* 219 */           createIdleLookBehaviors(), 
/* 220 */           createIdleMovementBehaviors(), 
/* 221 */           EraseMemoryIf.create(PiglinAi::wantsToStopFleeing, MemoryModuleType.AVOID_TARGET)), MemoryModuleType.AVOID_TARGET);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void initRideHoglinActivity(Brain<Piglin> paramBrain) {
/* 226 */     paramBrain.addActivityAndRemoveMemoryWhenStopped(Activity.RIDE, 10, ImmutableList.of(
/* 227 */           Mount.create(0.8F), 
/* 228 */           SetEntityLookTarget.create(PiglinAi::isPlayerHoldingLovedItem, 8.0F), 
/* 229 */           BehaviorBuilder.sequence(
/* 230 */             (Trigger)BehaviorBuilder.triggerIf(Entity::isPassenger), 
/* 231 */             (Trigger)TriggerGate.triggerOneShuffled(
/* 232 */               (List)ImmutableList.builder()
/* 233 */               .addAll((Iterable)createLookBehaviors())
/* 234 */               .add(Pair.of(BehaviorBuilder.triggerIf(paramPiglin -> true), Integer.valueOf(1)))
/* 235 */               .build())), 
/*     */ 
/*     */           
/* 238 */           DismountOrSkipMounting.create(8, PiglinAi::wantsToStopRiding)), MemoryModuleType.RIDE_TARGET);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ImmutableList<Pair<OneShot<LivingEntity>, Integer>> createLookBehaviors() {
/* 244 */     return ImmutableList.of(
/* 245 */         Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 8.0F), Integer.valueOf(1)), 
/* 246 */         Pair.of(SetEntityLookTarget.create(EntityType.PIGLIN, 8.0F), Integer.valueOf(1)), 
/* 247 */         Pair.of(SetEntityLookTarget.create(8.0F), Integer.valueOf(1)));
/*     */   }
/*     */ 
/*     */   
/*     */   private static RunOne<LivingEntity> createIdleLookBehaviors() {
/* 252 */     return new RunOne(
/* 253 */         (List)ImmutableList.builder()
/* 254 */         .addAll((Iterable)createLookBehaviors())
/* 255 */         .add(Pair.of(new DoNothing(30, 60), Integer.valueOf(1)))
/* 256 */         .build());
/*     */   }
/*     */ 
/*     */   
/*     */   private static RunOne<Piglin> createIdleMovementBehaviors() {
/* 261 */     return new RunOne((List)ImmutableList.of(
/* 262 */           Pair.of(RandomStroll.stroll(0.6F), Integer.valueOf(2)), 
/*     */           
/* 264 */           Pair.of(InteractWith.of(EntityType.PIGLIN, 8, MemoryModuleType.INTERACTION_TARGET, 0.6F, 2), Integer.valueOf(2)), 
/* 265 */           Pair.of(BehaviorBuilder.triggerIf(PiglinAi::doesntSeeAnyPlayerHoldingLovedItem, SetWalkTargetFromLookTarget.create(0.6F, 3)), Integer.valueOf(2)), 
/* 266 */           Pair.of(new DoNothing(30, 60), Integer.valueOf(1))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static BehaviorControl<PathfinderMob> avoidRepellent() {
/* 271 */     return SetWalkTargetAwayFrom.pos(MemoryModuleType.NEAREST_REPELLENT, 1.0F, 8, false);
/*     */   }
/*     */   
/*     */   private static BehaviorControl<Piglin> babyAvoidNemesis() {
/* 275 */     return CopyMemoryWithExpiry.create(Piglin::isBaby, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.AVOID_TARGET, BABY_AVOID_NEMESIS_DURATION);
/*     */   }
/*     */   
/*     */   private static BehaviorControl<Piglin> avoidZombified() {
/* 279 */     return CopyMemoryWithExpiry.create(PiglinAi::isNearZombified, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.AVOID_TARGET, AVOID_ZOMBIFIED_DURATION);
/*     */   }
/*     */   
/*     */   protected static void updateActivity(Piglin paramPiglin) {
/* 283 */     Brain<Piglin> brain = paramPiglin.getBrain();
/*     */     
/* 285 */     Activity activity1 = brain.getActiveNonCoreActivity().orElse(null);
/*     */ 
/*     */ 
/*     */     
/* 289 */     brain.setActiveActivityToFirstValid((List)ImmutableList.of(Activity.ADMIRE_ITEM, Activity.FIGHT, Activity.AVOID, Activity.CELEBRATE, Activity.RIDE, Activity.IDLE));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 298 */     Activity activity2 = brain.getActiveNonCoreActivity().orElse(null);
/* 299 */     if (activity1 != activity2) {
/*     */       
/* 301 */       Objects.requireNonNull(paramPiglin); getSoundForCurrentActivity(paramPiglin).ifPresent(paramPiglin::makeSound);
/*     */     } 
/*     */ 
/*     */     
/* 305 */     paramPiglin.setAggressive(brain.hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */     
/* 307 */     if (!brain.hasMemoryValue(MemoryModuleType.RIDE_TARGET) && isBabyRidingBaby(paramPiglin))
/*     */     {
/*     */ 
/*     */       
/* 311 */       paramPiglin.stopRiding();
/*     */     }
/*     */     
/* 314 */     if (!brain.hasMemoryValue(MemoryModuleType.CELEBRATE_LOCATION))
/*     */     {
/*     */       
/* 317 */       brain.eraseMemory(MemoryModuleType.DANCING);
/*     */     }
/* 319 */     paramPiglin.setDancing(brain.hasMemoryValue(MemoryModuleType.DANCING));
/*     */   }
/*     */   
/*     */   private static boolean isBabyRidingBaby(Piglin paramPiglin) {
/* 323 */     if (!paramPiglin.isBaby()) {
/* 324 */       return false;
/*     */     }
/* 326 */     Entity entity = paramPiglin.getVehicle();
/* 327 */     return ((entity instanceof Piglin && ((Piglin)entity).isBaby()) || (entity instanceof Hoglin && ((Hoglin)entity)
/* 328 */       .isBaby()));
/*     */   }
/*     */   protected static void pickUpItem(ServerLevel paramServerLevel, Piglin paramPiglin, ItemEntity paramItemEntity) {
/*     */     ItemStack itemStack;
/* 332 */     stopWalking(paramPiglin);
/*     */ 
/*     */ 
/*     */     
/* 336 */     if (paramItemEntity.getItem().is(Items.GOLD_NUGGET)) {
/*     */ 
/*     */       
/* 339 */       paramPiglin.take((Entity)paramItemEntity, paramItemEntity.getItem().getCount());
/* 340 */       itemStack = paramItemEntity.getItem();
/* 341 */       paramItemEntity.discard();
/*     */     } else {
/* 343 */       paramPiglin.take((Entity)paramItemEntity, 1);
/* 344 */       itemStack = removeOneItemFromItemEntity(paramItemEntity);
/*     */     } 
/*     */     
/* 347 */     if (isLovedItem(itemStack)) {
/* 348 */       paramPiglin.getBrain().eraseMemory(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM);
/* 349 */       holdInOffhand(paramServerLevel, paramPiglin, itemStack);
/* 350 */       admireGoldItem((LivingEntity)paramPiglin);
/*     */       
/*     */       return;
/*     */     } 
/* 354 */     if (isFood(itemStack) && !hasEatenRecently(paramPiglin)) {
/* 355 */       eat(paramPiglin);
/*     */       
/*     */       return;
/*     */     } 
/* 359 */     boolean bool = !paramPiglin.equipItemIfPossible(paramServerLevel, itemStack).equals(ItemStack.EMPTY) ? true : false;
/* 360 */     if (bool) {
/*     */       return;
/*     */     }
/*     */     
/* 364 */     putInInventory(paramPiglin, itemStack);
/*     */   }
/*     */   
/*     */   private static void holdInOffhand(ServerLevel paramServerLevel, Piglin paramPiglin, ItemStack paramItemStack) {
/* 368 */     if (isHoldingItemInOffHand(paramPiglin)) {
/* 369 */       paramPiglin.spawnAtLocation(paramServerLevel, paramPiglin.getItemInHand(InteractionHand.OFF_HAND));
/*     */     }
/* 371 */     paramPiglin.holdInOffHand(paramItemStack);
/*     */   }
/*     */   
/*     */   private static ItemStack removeOneItemFromItemEntity(ItemEntity paramItemEntity) {
/* 375 */     ItemStack itemStack1 = paramItemEntity.getItem();
/* 376 */     ItemStack itemStack2 = itemStack1.split(1);
/* 377 */     if (itemStack1.isEmpty()) {
/* 378 */       paramItemEntity.discard();
/*     */     } else {
/* 380 */       paramItemEntity.setItem(itemStack1);
/*     */     } 
/* 382 */     return itemStack2;
/*     */   }
/*     */   
/*     */   protected static void stopHoldingOffHandItem(ServerLevel paramServerLevel, Piglin paramPiglin, boolean paramBoolean) {
/* 386 */     ItemStack itemStack = paramPiglin.getItemInHand(InteractionHand.OFF_HAND);
/* 387 */     paramPiglin.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
/*     */     
/* 389 */     if (paramPiglin.isAdult()) {
/* 390 */       boolean bool = isBarterCurrency(itemStack);
/* 391 */       if (paramBoolean && bool) {
/* 392 */         throwItems(paramPiglin, getBarterResponseItems(paramPiglin));
/* 393 */       } else if (!bool) {
/* 394 */         boolean bool1 = !paramPiglin.equipItemIfPossible(paramServerLevel, itemStack).isEmpty() ? true : false;
/* 395 */         if (!bool1) {
/* 396 */           putInInventory(paramPiglin, itemStack);
/*     */         }
/*     */       } 
/*     */     } else {
/* 400 */       boolean bool = !paramPiglin.equipItemIfPossible(paramServerLevel, itemStack).isEmpty() ? true : false;
/* 401 */       if (!bool) {
/*     */ 
/*     */ 
/*     */         
/* 405 */         ItemStack itemStack1 = paramPiglin.getMainHandItem();
/* 406 */         if (isLovedItem(itemStack1)) {
/* 407 */           putInInventory(paramPiglin, itemStack1);
/*     */         } else {
/* 409 */           throwItems(paramPiglin, Collections.singletonList(itemStack1));
/*     */         } 
/* 411 */         paramPiglin.holdInMainHand(itemStack);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static void cancelAdmiring(ServerLevel paramServerLevel, Piglin paramPiglin) {
/* 417 */     if (isAdmiringItem(paramPiglin) && !paramPiglin.getOffhandItem().isEmpty()) {
/* 418 */       paramPiglin.spawnAtLocation(paramServerLevel, paramPiglin.getOffhandItem());
/* 419 */       paramPiglin.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void putInInventory(Piglin paramPiglin, ItemStack paramItemStack) {
/* 424 */     ItemStack itemStack = paramPiglin.addToInventory(paramItemStack);
/* 425 */     throwItemsTowardRandomPos(paramPiglin, Collections.singletonList(itemStack));
/*     */   }
/*     */   
/*     */   private static void throwItems(Piglin paramPiglin, List<ItemStack> paramList) {
/* 429 */     Optional<Player> optional = paramPiglin.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
/* 430 */     if (optional.isPresent()) {
/* 431 */       throwItemsTowardPlayer(paramPiglin, optional.get(), paramList);
/*     */     } else {
/* 433 */       throwItemsTowardRandomPos(paramPiglin, paramList);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void throwItemsTowardRandomPos(Piglin paramPiglin, List<ItemStack> paramList) {
/* 438 */     throwItemsTowardPos(paramPiglin, paramList, getRandomNearbyPos(paramPiglin));
/*     */   }
/*     */   
/*     */   private static void throwItemsTowardPlayer(Piglin paramPiglin, Player paramPlayer, List<ItemStack> paramList) {
/* 442 */     throwItemsTowardPos(paramPiglin, paramList, paramPlayer.position());
/*     */   }
/*     */   
/*     */   private static void throwItemsTowardPos(Piglin paramPiglin, List<ItemStack> paramList, Vec3 paramVec3) {
/* 446 */     if (!paramList.isEmpty()) {
/* 447 */       paramPiglin.swing(InteractionHand.OFF_HAND);
/* 448 */       for (ItemStack itemStack : paramList) {
/* 449 */         BehaviorUtils.throwItem((LivingEntity)paramPiglin, itemStack, paramVec3.add(0.0D, 1.0D, 0.0D));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private static List<ItemStack> getBarterResponseItems(Piglin paramPiglin) {
/* 455 */     LootTable lootTable = paramPiglin.level().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.PIGLIN_BARTERING);
/* 456 */     return (List<ItemStack>)lootTable.getRandomItems((new LootParams.Builder((ServerLevel)paramPiglin.level()))
/* 457 */         .withParameter(LootContextParams.THIS_ENTITY, paramPiglin)
/* 458 */         .create(LootContextParamSets.PIGLIN_BARTER));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean wantsToDance(LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2) {
/* 463 */     if (paramLivingEntity2.getType() != EntityType.HOGLIN) {
/* 464 */       return false;
/*     */     }
/*     */     
/* 467 */     return (RandomSource.create(paramLivingEntity1.level().getGameTime()).nextFloat() < 0.1F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean wantsToPickup(Piglin paramPiglin, ItemStack paramItemStack) {
/* 476 */     if (paramPiglin.isBaby() && paramItemStack.is(ItemTags.IGNORED_BY_PIGLIN_BABIES)) {
/* 477 */       return false;
/*     */     }
/*     */     
/* 480 */     if (paramItemStack.is(ItemTags.PIGLIN_REPELLENTS)) {
/* 481 */       return false;
/*     */     }
/* 483 */     if (isAdmiringDisabled(paramPiglin) && paramPiglin.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET)) {
/* 484 */       return false;
/*     */     }
/* 486 */     if (isBarterCurrency(paramItemStack)) {
/* 487 */       return isNotHoldingLovedItemInOffHand(paramPiglin);
/*     */     }
/*     */     
/* 490 */     boolean bool = paramPiglin.canAddToInventory(paramItemStack);
/* 491 */     if (paramItemStack.is(Items.GOLD_NUGGET)) {
/* 492 */       return bool;
/*     */     }
/* 494 */     if (isFood(paramItemStack)) {
/* 495 */       return (!hasEatenRecently(paramPiglin) && bool);
/*     */     }
/* 497 */     if (isLovedItem(paramItemStack)) {
/* 498 */       return (isNotHoldingLovedItemInOffHand(paramPiglin) && bool);
/*     */     }
/* 500 */     return paramPiglin.canReplaceCurrentItem(paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean isLovedItem(ItemStack paramItemStack) {
/* 505 */     return paramItemStack.is(ItemTags.PIGLIN_LOVED);
/*     */   }
/*     */   
/*     */   private static boolean wantsToStopRiding(Piglin paramPiglin, Entity paramEntity) {
/* 509 */     if (paramEntity instanceof Mob) { Mob mob = (Mob)paramEntity;
/* 510 */       return (!mob.isBaby() || 
/* 511 */         !mob.isAlive() || 
/* 512 */         wasHurtRecently((LivingEntity)paramPiglin) || 
/* 513 */         wasHurtRecently((LivingEntity)mob) || (mob instanceof Piglin && mob
/* 514 */         .getVehicle() == null)); }
/*     */     
/* 516 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isNearestValidAttackTarget(ServerLevel paramServerLevel, Piglin paramPiglin, LivingEntity paramLivingEntity) {
/* 520 */     return findNearestValidAttackTarget(paramServerLevel, paramPiglin)
/* 521 */       .filter(paramLivingEntity2 -> (paramLivingEntity2 == paramLivingEntity1))
/* 522 */       .isPresent();
/*     */   }
/*     */   
/*     */   private static boolean isNearZombified(Piglin paramPiglin) {
/* 526 */     Brain<Piglin> brain = paramPiglin.getBrain();
/* 527 */     if (brain.hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED)) {
/* 528 */       LivingEntity livingEntity = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED).get();
/* 529 */       return paramPiglin.closerThan((Entity)livingEntity, 6.0D);
/*     */     } 
/* 531 */     return false;
/*     */   }
/*     */   
/*     */   private static Optional<? extends LivingEntity> findNearestValidAttackTarget(ServerLevel paramServerLevel, Piglin paramPiglin) {
/* 535 */     Brain<Piglin> brain = paramPiglin.getBrain();
/*     */     
/* 537 */     if (isNearZombified(paramPiglin)) {
/* 538 */       return Optional.empty();
/*     */     }
/*     */     
/* 541 */     Optional<LivingEntity> optional1 = BehaviorUtils.getLivingEntityFromUUIDMemory((LivingEntity)paramPiglin, MemoryModuleType.ANGRY_AT);
/* 542 */     if (optional1.isPresent() && Sensor.isEntityAttackableIgnoringLineOfSight(paramServerLevel, (LivingEntity)paramPiglin, optional1.get())) {
/* 543 */       return optional1;
/*     */     }
/*     */     
/* 546 */     if (brain.hasMemoryValue(MemoryModuleType.UNIVERSAL_ANGER)) {
/* 547 */       Optional<? extends LivingEntity> optional3 = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
/* 548 */       if (optional3.isPresent()) {
/* 549 */         return optional3;
/*     */       }
/*     */     } 
/*     */     
/* 553 */     Optional<? extends LivingEntity> optional = brain.getMemory(MemoryModuleType.NEAREST_VISIBLE_NEMESIS);
/* 554 */     if (optional.isPresent()) {
/* 555 */       return optional;
/*     */     }
/*     */     
/* 558 */     Optional<LivingEntity> optional2 = brain.getMemory(MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD);
/* 559 */     if (optional2.isPresent() && Sensor.isEntityAttackable(paramServerLevel, (LivingEntity)paramPiglin, optional2.get())) {
/* 560 */       return optional2;
/*     */     }
/*     */     
/* 563 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static void angerNearbyPiglins(ServerLevel paramServerLevel, Player paramPlayer, boolean paramBoolean) {
/* 567 */     List list = paramPlayer.level().getEntitiesOfClass(Piglin.class, paramPlayer.getBoundingBox().inflate(16.0D));
/* 568 */     list.stream()
/* 569 */       .filter(PiglinAi::isIdle)
/* 570 */       .filter(paramPiglin -> (!paramBoolean || BehaviorUtils.canSee((LivingEntity)paramPiglin, (LivingEntity)paramPlayer)))
/* 571 */       .forEach(paramPiglin -> {
/*     */           if (((Boolean)paramServerLevel.getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue()) {
/*     */             setAngerTargetToNearestTargetablePlayerIfFound(paramServerLevel, paramPiglin, (LivingEntity)paramPlayer);
/*     */           } else {
/*     */             setAngerTarget(paramServerLevel, paramPiglin, (LivingEntity)paramPlayer);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public static InteractionResult mobInteract(ServerLevel paramServerLevel, Piglin paramPiglin, Player paramPlayer, InteractionHand paramInteractionHand) {
/* 581 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 582 */     if (canAdmire(paramPiglin, itemStack)) {
/* 583 */       ItemStack itemStack1 = itemStack.consumeAndReturn(1, (LivingEntity)paramPlayer);
/* 584 */       holdInOffhand(paramServerLevel, paramPiglin, itemStack1);
/* 585 */       admireGoldItem((LivingEntity)paramPiglin);
/* 586 */       stopWalking(paramPiglin);
/* 587 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 589 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   protected static boolean canAdmire(Piglin paramPiglin, ItemStack paramItemStack) {
/* 593 */     return (!isAdmiringDisabled(paramPiglin) && !isAdmiringItem(paramPiglin) && paramPiglin.isAdult() && isBarterCurrency(paramItemStack));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static void wasHurtBy(ServerLevel paramServerLevel, Piglin paramPiglin, LivingEntity paramLivingEntity) {
/* 598 */     if (paramLivingEntity instanceof Piglin) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 603 */     if (isHoldingItemInOffHand(paramPiglin)) {
/* 604 */       stopHoldingOffHandItem(paramServerLevel, paramPiglin, false);
/*     */     }
/* 606 */     Brain<Piglin> brain = paramPiglin.getBrain();
/* 607 */     brain.eraseMemory(MemoryModuleType.CELEBRATE_LOCATION);
/* 608 */     brain.eraseMemory(MemoryModuleType.DANCING);
/* 609 */     brain.eraseMemory(MemoryModuleType.ADMIRING_ITEM);
/*     */     
/* 611 */     if (paramLivingEntity instanceof Player)
/*     */     {
/* 613 */       brain.setMemoryWithExpiry(MemoryModuleType.ADMIRING_DISABLED, Boolean.valueOf(true), 400L);
/*     */     }
/*     */     
/* 616 */     getAvoidTarget(paramPiglin).ifPresent(paramLivingEntity2 -> {
/*     */           if (paramLivingEntity2.getType() != paramLivingEntity1.getType()) {
/*     */             paramBrain.eraseMemory(MemoryModuleType.AVOID_TARGET);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 623 */     if (paramPiglin.isBaby()) {
/*     */       
/* 625 */       brain.setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, paramLivingEntity, 100L);
/* 626 */       if (Sensor.isEntityAttackableIgnoringLineOfSight(paramServerLevel, (LivingEntity)paramPiglin, paramLivingEntity)) {
/* 627 */         broadcastAngerTarget(paramServerLevel, paramPiglin, paramLivingEntity);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 632 */     if (paramLivingEntity.getType() == EntityType.HOGLIN && hoglinsOutnumberPiglins(paramPiglin)) {
/*     */       
/* 634 */       setAvoidTargetAndDontHuntForAWhile(paramPiglin, paramLivingEntity);
/* 635 */       broadcastRetreat(paramPiglin, paramLivingEntity);
/*     */       
/*     */       return;
/*     */     } 
/* 639 */     maybeRetaliate(paramServerLevel, paramPiglin, paramLivingEntity);
/*     */   }
/*     */   
/*     */   protected static void maybeRetaliate(ServerLevel paramServerLevel, AbstractPiglin paramAbstractPiglin, LivingEntity paramLivingEntity) {
/* 643 */     if (paramAbstractPiglin.getBrain().isActive(Activity.AVOID)) {
/*     */       return;
/*     */     }
/* 646 */     if (!Sensor.isEntityAttackableIgnoringLineOfSight(paramServerLevel, (LivingEntity)paramAbstractPiglin, paramLivingEntity)) {
/*     */       return;
/*     */     }
/* 649 */     if (BehaviorUtils.isOtherTargetMuchFurtherAwayThanCurrentAttackTarget((LivingEntity)paramAbstractPiglin, paramLivingEntity, 4.0D)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 655 */     if (paramLivingEntity.getType() == EntityType.PLAYER && ((Boolean)paramServerLevel.getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue()) {
/*     */ 
/*     */       
/* 658 */       setAngerTargetToNearestTargetablePlayerIfFound(paramServerLevel, paramAbstractPiglin, paramLivingEntity);
/* 659 */       broadcastUniversalAnger(paramServerLevel, paramAbstractPiglin);
/*     */     } else {
/* 661 */       setAngerTarget(paramServerLevel, paramAbstractPiglin, paramLivingEntity);
/* 662 */       broadcastAngerTarget(paramServerLevel, paramAbstractPiglin, paramLivingEntity);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Optional<SoundEvent> getSoundForCurrentActivity(Piglin paramPiglin) {
/* 667 */     return paramPiglin.getBrain().getActiveNonCoreActivity().map(paramActivity -> getSoundForActivity(paramPiglin, paramActivity));
/*     */   }
/*     */   
/*     */   private static SoundEvent getSoundForActivity(Piglin paramPiglin, Activity paramActivity) {
/* 671 */     if (paramActivity == Activity.FIGHT)
/* 672 */       return SoundEvents.PIGLIN_ANGRY; 
/* 673 */     if (paramPiglin.isConverting())
/* 674 */       return SoundEvents.PIGLIN_RETREAT; 
/* 675 */     if (paramActivity == Activity.AVOID && isNearAvoidTarget(paramPiglin))
/* 676 */       return SoundEvents.PIGLIN_RETREAT; 
/* 677 */     if (paramActivity == Activity.ADMIRE_ITEM)
/* 678 */       return SoundEvents.PIGLIN_ADMIRING_ITEM; 
/* 679 */     if (paramActivity == Activity.CELEBRATE)
/* 680 */       return SoundEvents.PIGLIN_CELEBRATE; 
/* 681 */     if (seesPlayerHoldingLovedItem((LivingEntity)paramPiglin))
/* 682 */       return SoundEvents.PIGLIN_JEALOUS; 
/* 683 */     if (isNearRepellent(paramPiglin)) {
/* 684 */       return SoundEvents.PIGLIN_RETREAT;
/*     */     }
/* 686 */     return SoundEvents.PIGLIN_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isNearAvoidTarget(Piglin paramPiglin) {
/* 691 */     Brain<Piglin> brain = paramPiglin.getBrain();
/* 692 */     if (!brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
/* 693 */       return false;
/*     */     }
/* 695 */     return ((LivingEntity)brain.getMemory(MemoryModuleType.AVOID_TARGET).get()).closerThan((Entity)paramPiglin, 12.0D);
/*     */   }
/*     */   
/*     */   protected static List<AbstractPiglin> getVisibleAdultPiglins(Piglin paramPiglin) {
/* 699 */     return (List<AbstractPiglin>)paramPiglin.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS).orElse(ImmutableList.of());
/*     */   }
/*     */   
/*     */   private static List<AbstractPiglin> getAdultPiglins(AbstractPiglin paramAbstractPiglin) {
/* 703 */     return (List<AbstractPiglin>)paramAbstractPiglin.getBrain().getMemory(MemoryModuleType.NEARBY_ADULT_PIGLINS).orElse(ImmutableList.of());
/*     */   }
/*     */   
/*     */   public static boolean isWearingSafeArmor(LivingEntity paramLivingEntity) {
/* 707 */     for (EquipmentSlot equipmentSlot : EquipmentSlotGroup.ARMOR) {
/* 708 */       if (paramLivingEntity.getItemBySlot(equipmentSlot).is(ItemTags.PIGLIN_SAFE_ARMOR)) {
/* 709 */         return true;
/*     */       }
/*     */     } 
/* 712 */     return false;
/*     */   }
/*     */   
/*     */   private static void stopWalking(Piglin paramPiglin) {
/* 716 */     paramPiglin.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 717 */     paramPiglin.getNavigation().stop();
/*     */   }
/*     */   
/*     */   private static BehaviorControl<LivingEntity> babySometimesRideBabyHoglin() {
/* 721 */     SetEntityLookTargetSometimes.Ticker ticker = new SetEntityLookTargetSometimes.Ticker(RIDE_START_INTERVAL);
/* 722 */     return CopyMemoryWithExpiry.create(paramLivingEntity -> (paramLivingEntity.isBaby() && paramTicker.tickDownAndCheck((paramLivingEntity.level()).random)), MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.RIDE_TARGET, RIDE_DURATION);
/*     */   }
/*     */   
/*     */   protected static void broadcastAngerTarget(ServerLevel paramServerLevel, AbstractPiglin paramAbstractPiglin, LivingEntity paramLivingEntity) {
/* 726 */     getAdultPiglins(paramAbstractPiglin).forEach(paramAbstractPiglin -> {
/*     */           if (paramLivingEntity.getType() == EntityType.HOGLIN && (!paramAbstractPiglin.canHunt() || !((Hoglin)paramLivingEntity).canBeHunted())) {
/*     */             return;
/*     */           }
/*     */           setAngerTargetIfCloserThanCurrent(paramServerLevel, paramAbstractPiglin, paramLivingEntity);
/*     */         });
/*     */   }
/*     */   
/*     */   protected static void broadcastUniversalAnger(ServerLevel paramServerLevel, AbstractPiglin paramAbstractPiglin) {
/* 735 */     getAdultPiglins(paramAbstractPiglin).forEach(paramAbstractPiglin -> getNearestVisibleTargetablePlayer(paramAbstractPiglin).ifPresent(()));
/*     */   }
/*     */   
/*     */   protected static void setAngerTarget(ServerLevel paramServerLevel, AbstractPiglin paramAbstractPiglin, LivingEntity paramLivingEntity) {
/* 739 */     if (!Sensor.isEntityAttackableIgnoringLineOfSight(paramServerLevel, (LivingEntity)paramAbstractPiglin, paramLivingEntity)) {
/*     */       return;
/*     */     }
/*     */     
/* 743 */     paramAbstractPiglin.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
/* 744 */     paramAbstractPiglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, paramLivingEntity.getUUID(), 600L);
/* 745 */     if (paramLivingEntity.getType() == EntityType.HOGLIN && paramAbstractPiglin.canHunt()) {
/* 746 */       dontKillAnyMoreHoglinsForAWhile(paramAbstractPiglin);
/*     */     }
/* 748 */     if (paramLivingEntity.getType() == EntityType.PLAYER && ((Boolean)paramServerLevel.getGameRules().get(GameRules.UNIVERSAL_ANGER)).booleanValue()) {
/* 749 */       paramAbstractPiglin.getBrain().setMemoryWithExpiry(MemoryModuleType.UNIVERSAL_ANGER, Boolean.valueOf(true), 600L);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void setAngerTargetToNearestTargetablePlayerIfFound(ServerLevel paramServerLevel, AbstractPiglin paramAbstractPiglin, LivingEntity paramLivingEntity) {
/* 754 */     Optional<Player> optional = getNearestVisibleTargetablePlayer(paramAbstractPiglin);
/* 755 */     if (optional.isPresent()) {
/* 756 */       setAngerTarget(paramServerLevel, paramAbstractPiglin, (LivingEntity)optional.get());
/*     */     } else {
/* 758 */       setAngerTarget(paramServerLevel, paramAbstractPiglin, paramLivingEntity);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void setAngerTargetIfCloserThanCurrent(ServerLevel paramServerLevel, AbstractPiglin paramAbstractPiglin, LivingEntity paramLivingEntity) {
/* 763 */     Optional<LivingEntity> optional = getAngerTarget(paramAbstractPiglin);
/* 764 */     LivingEntity livingEntity = BehaviorUtils.getNearestTarget((LivingEntity)paramAbstractPiglin, optional, paramLivingEntity);
/* 765 */     if (optional.isPresent() && optional.get() == livingEntity) {
/*     */       return;
/*     */     }
/* 768 */     setAngerTarget(paramServerLevel, paramAbstractPiglin, livingEntity);
/*     */   }
/*     */   
/*     */   private static Optional<LivingEntity> getAngerTarget(AbstractPiglin paramAbstractPiglin) {
/* 772 */     return BehaviorUtils.getLivingEntityFromUUIDMemory((LivingEntity)paramAbstractPiglin, MemoryModuleType.ANGRY_AT);
/*     */   }
/*     */   
/*     */   public static Optional<LivingEntity> getAvoidTarget(Piglin paramPiglin) {
/* 776 */     if (paramPiglin.getBrain().hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
/* 777 */       return paramPiglin.getBrain().getMemory(MemoryModuleType.AVOID_TARGET);
/*     */     }
/* 779 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static Optional<Player> getNearestVisibleTargetablePlayer(AbstractPiglin paramAbstractPiglin) {
/* 783 */     if (paramAbstractPiglin.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER)) {
/* 784 */       return paramAbstractPiglin.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER);
/*     */     }
/* 786 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private static void broadcastRetreat(Piglin paramPiglin, LivingEntity paramLivingEntity) {
/* 790 */     getVisibleAdultPiglins(paramPiglin).stream()
/* 791 */       .filter(paramAbstractPiglin -> paramAbstractPiglin instanceof Piglin)
/* 792 */       .forEach(paramAbstractPiglin -> retreatFromNearestTarget((Piglin)paramAbstractPiglin, paramLivingEntity));
/*     */   }
/*     */   
/*     */   private static void retreatFromNearestTarget(Piglin paramPiglin, LivingEntity paramLivingEntity) {
/* 796 */     Brain<Piglin> brain = paramPiglin.getBrain();
/* 797 */     LivingEntity livingEntity = paramLivingEntity;
/* 798 */     livingEntity = BehaviorUtils.getNearestTarget((LivingEntity)paramPiglin, brain.getMemory(MemoryModuleType.AVOID_TARGET), livingEntity);
/* 799 */     livingEntity = BehaviorUtils.getNearestTarget((LivingEntity)paramPiglin, brain.getMemory(MemoryModuleType.ATTACK_TARGET), livingEntity);
/* 800 */     setAvoidTargetAndDontHuntForAWhile(paramPiglin, livingEntity);
/*     */   }
/*     */   
/*     */   private static boolean wantsToStopFleeing(Piglin paramPiglin) {
/* 804 */     Brain<Piglin> brain = paramPiglin.getBrain();
/* 805 */     if (!brain.hasMemoryValue(MemoryModuleType.AVOID_TARGET)) {
/* 806 */       return true;
/*     */     }
/* 808 */     LivingEntity livingEntity = brain.getMemory(MemoryModuleType.AVOID_TARGET).get();
/* 809 */     EntityType<?> entityType = livingEntity.getType();
/*     */     
/* 811 */     if (entityType == EntityType.HOGLIN) {
/* 812 */       return piglinsEqualOrOutnumberHoglins(paramPiglin);
/*     */     }
/* 814 */     if (isZombified(entityType)) {
/* 815 */       return !brain.isMemoryValue(MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, livingEntity);
/*     */     }
/* 817 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean piglinsEqualOrOutnumberHoglins(Piglin paramPiglin) {
/* 821 */     return !hoglinsOutnumberPiglins(paramPiglin);
/*     */   }
/*     */   
/*     */   private static boolean hoglinsOutnumberPiglins(Piglin paramPiglin) {
/* 825 */     int i = ((Integer)paramPiglin.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT).orElse(Integer.valueOf(0))).intValue() + 1;
/* 826 */     int j = ((Integer)paramPiglin.getBrain().getMemory(MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT).orElse(Integer.valueOf(0))).intValue();
/* 827 */     return (j > i);
/*     */   }
/*     */   
/*     */   private static void setAvoidTargetAndDontHuntForAWhile(Piglin paramPiglin, LivingEntity paramLivingEntity) {
/* 831 */     paramPiglin.getBrain().eraseMemory(MemoryModuleType.ANGRY_AT);
/* 832 */     paramPiglin.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
/* 833 */     paramPiglin.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
/* 834 */     paramPiglin.getBrain().setMemoryWithExpiry(MemoryModuleType.AVOID_TARGET, paramLivingEntity, RETREAT_DURATION.sample((paramPiglin.level()).random));
/* 835 */     dontKillAnyMoreHoglinsForAWhile(paramPiglin);
/*     */   }
/*     */   
/*     */   protected static void dontKillAnyMoreHoglinsForAWhile(AbstractPiglin paramAbstractPiglin) {
/* 839 */     paramAbstractPiglin.getBrain().setMemoryWithExpiry(MemoryModuleType.HUNTED_RECENTLY, Boolean.valueOf(true), TIME_BETWEEN_HUNTS.sample((paramAbstractPiglin.level()).random));
/*     */   }
/*     */   
/*     */   private static void eat(Piglin paramPiglin) {
/* 843 */     paramPiglin.getBrain().setMemoryWithExpiry(MemoryModuleType.ATE_RECENTLY, Boolean.valueOf(true), 200L);
/*     */   }
/*     */   
/*     */   private static Vec3 getRandomNearbyPos(Piglin paramPiglin) {
/* 847 */     Vec3 vec3 = LandRandomPos.getPos((PathfinderMob)paramPiglin, 4, 2);
/* 848 */     return (vec3 == null) ? paramPiglin.position() : vec3;
/*     */   }
/*     */   
/*     */   private static boolean hasEatenRecently(Piglin paramPiglin) {
/* 852 */     return paramPiglin.getBrain().hasMemoryValue(MemoryModuleType.ATE_RECENTLY);
/*     */   }
/*     */   
/*     */   protected static boolean isIdle(AbstractPiglin paramAbstractPiglin) {
/* 856 */     return paramAbstractPiglin.getBrain().isActive(Activity.IDLE);
/*     */   }
/*     */   
/*     */   private static boolean hasCrossbow(LivingEntity paramLivingEntity) {
/* 860 */     return paramLivingEntity.isHolding(Items.CROSSBOW);
/*     */   }
/*     */   
/*     */   private static void admireGoldItem(LivingEntity paramLivingEntity) {
/* 864 */     paramLivingEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.ADMIRING_ITEM, Boolean.valueOf(true), 119L);
/*     */   }
/*     */   
/*     */   private static boolean isAdmiringItem(Piglin paramPiglin) {
/* 868 */     return paramPiglin.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_ITEM);
/*     */   }
/*     */   
/*     */   private static boolean isBarterCurrency(ItemStack paramItemStack) {
/* 872 */     return paramItemStack.is(BARTERING_ITEM);
/*     */   }
/*     */   
/*     */   private static boolean isFood(ItemStack paramItemStack) {
/* 876 */     return paramItemStack.is(ItemTags.PIGLIN_FOOD);
/*     */   }
/*     */   
/*     */   private static boolean isNearRepellent(Piglin paramPiglin) {
/* 880 */     return paramPiglin.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_REPELLENT);
/*     */   }
/*     */   
/*     */   private static boolean seesPlayerHoldingLovedItem(LivingEntity paramLivingEntity) {
/* 884 */     return paramLivingEntity.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM);
/*     */   }
/*     */   
/*     */   private static boolean doesntSeeAnyPlayerHoldingLovedItem(LivingEntity paramLivingEntity) {
/* 888 */     return !seesPlayerHoldingLovedItem(paramLivingEntity);
/*     */   }
/*     */   
/*     */   public static boolean isPlayerHoldingLovedItem(LivingEntity paramLivingEntity) {
/* 892 */     return (paramLivingEntity.getType() == EntityType.PLAYER && paramLivingEntity.isHolding(PiglinAi::isLovedItem));
/*     */   }
/*     */   
/*     */   private static boolean isAdmiringDisabled(Piglin paramPiglin) {
/* 896 */     return paramPiglin.getBrain().hasMemoryValue(MemoryModuleType.ADMIRING_DISABLED);
/*     */   }
/*     */   
/*     */   private static boolean wasHurtRecently(LivingEntity paramLivingEntity) {
/* 900 */     return paramLivingEntity.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
/*     */   }
/*     */   
/*     */   private static boolean isHoldingItemInOffHand(Piglin paramPiglin) {
/* 904 */     return !paramPiglin.getOffhandItem().isEmpty();
/*     */   }
/*     */   
/*     */   private static boolean isNotHoldingLovedItemInOffHand(Piglin paramPiglin) {
/* 908 */     return (paramPiglin.getOffhandItem().isEmpty() || !isLovedItem(paramPiglin.getOffhandItem()));
/*     */   }
/*     */   
/*     */   public static boolean isZombified(EntityType<?> paramEntityType) {
/* 912 */     return (paramEntityType == EntityType.ZOMBIFIED_PIGLIN || paramEntityType == EntityType.ZOGLIN);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\piglin\PiglinAi.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */