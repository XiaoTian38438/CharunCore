/*     */ package net.minecraft.world.entity.monster.piglin;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.CrossbowAttackMob;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.npc.InventoryCarrier;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.CrossbowItem;
/*     */ import net.minecraft.world.item.Item;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ 
/*     */ public class Piglin
/*     */   extends AbstractPiglin
/*     */   implements CrossbowAttackMob, InventoryCarrier
/*     */ {
/*  69 */   private static final EntityDataAccessor<Boolean> DATA_BABY_ID = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
/*  70 */   private static final EntityDataAccessor<Boolean> DATA_IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
/*  71 */   private static final EntityDataAccessor<Boolean> DATA_IS_DANCING = SynchedEntityData.defineId(Piglin.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  73 */   private static final Identifier SPEED_MODIFIER_BABY_ID = Identifier.withDefaultNamespace("baby");
/*  74 */   private static final AttributeModifier SPEED_MODIFIER_BABY = new AttributeModifier(SPEED_MODIFIER_BABY_ID, 0.20000000298023224D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
/*     */   
/*     */   private static final int MAX_HEALTH = 16;
/*     */   private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.35F;
/*     */   private static final int ATTACK_DAMAGE = 5;
/*     */   private static final float CHANCE_OF_WEARING_EACH_ARMOUR_ITEM = 0.1F;
/*     */   private static final int MAX_PASSENGERS_ON_ONE_HOGLIN = 3;
/*     */   private static final float PROBABILITY_OF_SPAWNING_AS_BABY = 0.2F;
/*  82 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.PIGLIN.getDimensions().scale(0.5F).withEyeHeight(0.97F);
/*     */   
/*     */   private static final double PROBABILITY_OF_SPAWNING_WITH_CROSSBOW_INSTEAD_OF_SWORD = 0.5D;
/*     */   private static final boolean DEFAULT_IS_BABY = false;
/*     */   private static final boolean DEFAULT_CANNOT_HUNT = false;
/*  87 */   private final SimpleContainer inventory = new SimpleContainer(8);
/*     */   
/*     */   private boolean cannotHunt = false;
/*  90 */   protected static final ImmutableList<SensorType<? extends Sensor<? super Piglin>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_SPECIFIC_SENSOR);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  98 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.DOORS_TO_CLOSE, MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS, MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY, (Object[])new MemoryModuleType[] { MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryModuleType.INTERACTION_TARGET, MemoryModuleType.PATH, MemoryModuleType.ANGRY_AT, MemoryModuleType.UNIVERSAL_ANGER, MemoryModuleType.AVOID_TARGET, MemoryModuleType.ADMIRING_ITEM, MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM, MemoryModuleType.ADMIRING_DISABLED, MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM, MemoryModuleType.CELEBRATE_LOCATION, MemoryModuleType.DANCING, MemoryModuleType.HUNTED_RECENTLY, MemoryModuleType.NEAREST_VISIBLE_BABY_HOGLIN, MemoryModuleType.NEAREST_VISIBLE_NEMESIS, MemoryModuleType.NEAREST_VISIBLE_ZOMBIFIED, MemoryModuleType.RIDE_TARGET, MemoryModuleType.VISIBLE_ADULT_PIGLIN_COUNT, MemoryModuleType.VISIBLE_ADULT_HOGLIN_COUNT, MemoryModuleType.NEAREST_VISIBLE_HUNTABLE_HOGLIN, MemoryModuleType.NEAREST_TARGETABLE_PLAYER_NOT_WEARING_GOLD, MemoryModuleType.NEAREST_PLAYER_HOLDING_WANTED_ITEM, MemoryModuleType.ATE_RECENTLY, MemoryModuleType.NEAREST_REPELLENT, MemoryModuleType.SPEAR_FLEEING_TIME, MemoryModuleType.SPEAR_FLEEING_POSITION, MemoryModuleType.SPEAR_CHARGE_POSITION, MemoryModuleType.SPEAR_ENGAGE_TIME, MemoryModuleType.SPEAR_STATUS });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Piglin(EntityType<? extends AbstractPiglin> paramEntityType, Level paramLevel) {
/* 146 */     super(paramEntityType, paramLevel);
/* 147 */     this.xpReward = 5;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 152 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/* 154 */     paramValueOutput.putBoolean("IsBaby", isBaby());
/* 155 */     paramValueOutput.putBoolean("CannotHunt", this.cannotHunt);
/* 156 */     writeInventoryToTag(paramValueOutput);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 161 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/* 163 */     setBaby(paramValueInput.getBooleanOr("IsBaby", false));
/* 164 */     setCannotHunt(paramValueInput.getBooleanOr("CannotHunt", false));
/* 165 */     readInventoryFromTag(paramValueInput);
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*     */   public SimpleContainer getInventory() {
/* 171 */     return this.inventory;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropCustomDeathLoot(ServerLevel paramServerLevel, DamageSource paramDamageSource, boolean paramBoolean) {
/* 176 */     super.dropCustomDeathLoot(paramServerLevel, paramDamageSource, paramBoolean);
/* 177 */     this.inventory.removeAllItems().forEach(paramItemStack -> spawnAtLocation(paramServerLevel, paramItemStack));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ItemStack addToInventory(ItemStack paramItemStack) {
/* 184 */     return this.inventory.addItem(paramItemStack);
/*     */   }
/*     */   
/*     */   protected boolean canAddToInventory(ItemStack paramItemStack) {
/* 188 */     return this.inventory.canAddItem(paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 193 */     super.defineSynchedData(paramBuilder);
/* 194 */     paramBuilder.define(DATA_BABY_ID, Boolean.valueOf(false));
/* 195 */     paramBuilder.define(DATA_IS_CHARGING_CROSSBOW, Boolean.valueOf(false));
/* 196 */     paramBuilder.define(DATA_IS_DANCING, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/* 201 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/* 202 */     if (DATA_BABY_ID.equals(paramEntityDataAccessor)) {
/* 203 */       refreshDimensions();
/*     */     }
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 208 */     return Monster.createMonsterAttributes()
/* 209 */       .add(Attributes.MAX_HEALTH, 16.0D)
/* 210 */       .add(Attributes.MOVEMENT_SPEED, 0.3499999940395355D)
/* 211 */       .add(Attributes.ATTACK_DAMAGE, 5.0D);
/*     */   }
/*     */   
/*     */   public static boolean checkPiglinSpawnRules(EntityType<Piglin> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 215 */     return !paramLevelAccessor.getBlockState(paramBlockPos.below()).is(Blocks.NETHER_WART_BLOCK);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 220 */     RandomSource randomSource = paramServerLevelAccessor.getRandom();
/* 221 */     if (paramEntitySpawnReason != EntitySpawnReason.STRUCTURE) {
/* 222 */       if (randomSource.nextFloat() < 0.2F) {
/* 223 */         setBaby(true);
/* 224 */       } else if (isAdult()) {
/* 225 */         setItemSlot(EquipmentSlot.MAINHAND, createSpawnWeapon());
/*     */       } 
/*     */     }
/* 228 */     PiglinAi.initMemories(this, paramServerLevelAccessor.getRandom());
/* 229 */     populateDefaultEquipmentSlots(randomSource, paramDifficultyInstance);
/* 230 */     populateDefaultEquipmentEnchantments(paramServerLevelAccessor, randomSource, paramDifficultyInstance);
/* 231 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double paramDouble) {
/* 236 */     return !isPersistenceRequired();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void populateDefaultEquipmentSlots(RandomSource paramRandomSource, DifficultyInstance paramDifficultyInstance) {
/* 241 */     if (isAdult()) {
/* 242 */       maybeWearArmor(EquipmentSlot.HEAD, new ItemStack((ItemLike)Items.GOLDEN_HELMET), paramRandomSource);
/* 243 */       maybeWearArmor(EquipmentSlot.CHEST, new ItemStack((ItemLike)Items.GOLDEN_CHESTPLATE), paramRandomSource);
/* 244 */       maybeWearArmor(EquipmentSlot.LEGS, new ItemStack((ItemLike)Items.GOLDEN_LEGGINGS), paramRandomSource);
/* 245 */       maybeWearArmor(EquipmentSlot.FEET, new ItemStack((ItemLike)Items.GOLDEN_BOOTS), paramRandomSource);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void maybeWearArmor(EquipmentSlot paramEquipmentSlot, ItemStack paramItemStack, RandomSource paramRandomSource) {
/* 250 */     if (paramRandomSource.nextFloat() < 0.1F) {
/* 251 */       setItemSlot(paramEquipmentSlot, paramItemStack);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<Piglin> brainProvider() {
/* 257 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> paramDynamic) {
/* 262 */     return PiglinAi.makeBrain(this, brainProvider().makeBrain(paramDynamic));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<Piglin> getBrain() {
/* 268 */     return super.getBrain();
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 273 */     InteractionResult interactionResult = super.mobInteract(paramPlayer, paramInteractionHand);
/* 274 */     if (interactionResult.consumesAction()) {
/* 275 */       return interactionResult;
/*     */     }
/* 277 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 278 */       return PiglinAi.mobInteract(serverLevel, this, paramPlayer, paramInteractionHand); }
/*     */     
/* 280 */     boolean bool = (PiglinAi.canAdmire(this, paramPlayer.getItemInHand(paramInteractionHand)) && getArmPose() != PiglinArmPose.ADMIRING_ITEM) ? true : false;
/* 281 */     return bool ? (InteractionResult)InteractionResult.SUCCESS : (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose paramPose) {
/* 286 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(paramPose);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaby(boolean paramBoolean) {
/* 291 */     getEntityData().set(DATA_BABY_ID, Boolean.valueOf(paramBoolean));
/*     */     
/* 293 */     if (!level().isClientSide()) {
/* 294 */       AttributeInstance attributeInstance = getAttribute(Attributes.MOVEMENT_SPEED);
/* 295 */       attributeInstance.removeModifier(SPEED_MODIFIER_BABY.id());
/* 296 */       if (paramBoolean) {
/* 297 */         attributeInstance.addTransientModifier(SPEED_MODIFIER_BABY);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBaby() {
/* 304 */     return ((Boolean)getEntityData().get(DATA_BABY_ID)).booleanValue();
/*     */   }
/*     */   
/*     */   private void setCannotHunt(boolean paramBoolean) {
/* 308 */     this.cannotHunt = paramBoolean;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canHunt() {
/* 313 */     return !this.cannotHunt;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/* 318 */     ProfilerFiller profilerFiller = Profiler.get();
/* 319 */     profilerFiller.push("piglinBrain");
/* 320 */     getBrain().tick(paramServerLevel, (LivingEntity)this);
/* 321 */     profilerFiller.pop();
/*     */     
/* 323 */     PiglinAi.updateActivity(this);
/*     */     
/* 325 */     super.customServerAiStep(paramServerLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getBaseExperienceReward(ServerLevel paramServerLevel) {
/* 330 */     return this.xpReward;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void finishConversion(ServerLevel paramServerLevel) {
/* 335 */     PiglinAi.cancelAdmiring(paramServerLevel, this);
/* 336 */     this.inventory.removeAllItems().forEach(paramItemStack -> spawnAtLocation(paramServerLevel, paramItemStack));
/* 337 */     super.finishConversion(paramServerLevel);
/*     */   }
/*     */   
/*     */   private ItemStack createSpawnWeapon() {
/* 341 */     if (this.random.nextFloat() < 0.5D) {
/* 342 */       return new ItemStack((ItemLike)Items.CROSSBOW);
/*     */     }
/* 344 */     return new ItemStack((this.random.nextInt(10) == 0) ? (ItemLike)Items.GOLDEN_SPEAR : (ItemLike)Items.GOLDEN_SWORD);
/*     */   }
/*     */ 
/*     */   
/*     */   public TagKey<Item> getPreferredWeaponType() {
/* 349 */     if (isBaby()) {
/* 350 */       return null;
/*     */     }
/* 352 */     return ItemTags.PIGLIN_PREFERRED_WEAPONS;
/*     */   }
/*     */   
/*     */   private boolean isChargingCrossbow() {
/* 356 */     return ((Boolean)this.entityData.get(DATA_IS_CHARGING_CROSSBOW)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChargingCrossbow(boolean paramBoolean) {
/* 361 */     this.entityData.set(DATA_IS_CHARGING_CROSSBOW, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCrossbowAttackPerformed() {
/* 366 */     this.noActionTime = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PiglinArmPose getArmPose() {
/* 372 */     if (isDancing())
/* 373 */       return PiglinArmPose.DANCING; 
/* 374 */     if (PiglinAi.isLovedItem(getOffhandItem()))
/* 375 */       return PiglinArmPose.ADMIRING_ITEM; 
/* 376 */     if (isAggressive() && isHoldingMeleeWeapon())
/* 377 */       return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON; 
/* 378 */     if (isChargingCrossbow())
/* 379 */       return PiglinArmPose.CROSSBOW_CHARGE; 
/* 380 */     if (isHolding(Items.CROSSBOW) && CrossbowItem.isCharged(getWeaponItem())) {
/* 381 */       return PiglinArmPose.CROSSBOW_HOLD;
/*     */     }
/* 383 */     return PiglinArmPose.DEFAULT;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDancing() {
/* 388 */     return ((Boolean)this.entityData.get(DATA_IS_DANCING)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setDancing(boolean paramBoolean) {
/* 392 */     this.entityData.set(DATA_IS_DANCING, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 397 */     boolean bool = super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/* 398 */     if (bool) { Entity entity = paramDamageSource.getEntity(); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 399 */         PiglinAi.wasHurtBy(paramServerLevel, this, livingEntity); }
/*     */        }
/* 401 */      return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performRangedAttack(LivingEntity paramLivingEntity, float paramFloat) {
/* 406 */     performCrossbowAttack((LivingEntity)this, 1.6F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseNonMeleeWeapon(ItemStack paramItemStack) {
/* 411 */     return (paramItemStack.getItem() == Items.CROSSBOW || paramItemStack.has(DataComponents.KINETIC_WEAPON));
/*     */   }
/*     */   
/*     */   protected void holdInMainHand(ItemStack paramItemStack) {
/* 415 */     setItemSlotAndDropWhenKilled(EquipmentSlot.MAINHAND, paramItemStack);
/*     */   }
/*     */   
/*     */   protected void holdInOffHand(ItemStack paramItemStack) {
/* 419 */     if (paramItemStack.is(PiglinAi.BARTERING_ITEM)) {
/*     */       
/* 421 */       setItemSlot(EquipmentSlot.OFFHAND, paramItemStack);
/* 422 */       setGuaranteedDrop(EquipmentSlot.OFFHAND);
/*     */     } else {
/* 424 */       setItemSlotAndDropWhenKilled(EquipmentSlot.OFFHAND, paramItemStack);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wantsToPickUp(ServerLevel paramServerLevel, ItemStack paramItemStack) {
/* 430 */     return (((Boolean)paramServerLevel.getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue() && canPickUpLoot() && PiglinAi.wantsToPickup(this, paramItemStack));
/*     */   }
/*     */   
/*     */   protected boolean canReplaceCurrentItem(ItemStack paramItemStack) {
/* 434 */     EquipmentSlot equipmentSlot = getEquipmentSlotForItem(paramItemStack);
/* 435 */     ItemStack itemStack = getItemBySlot(equipmentSlot);
/* 436 */     return canReplaceCurrentItem(paramItemStack, itemStack, equipmentSlot);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canReplaceCurrentItem(ItemStack paramItemStack1, ItemStack paramItemStack2, EquipmentSlot paramEquipmentSlot) {
/* 441 */     if (EnchantmentHelper.has(paramItemStack2, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) {
/* 442 */       return false;
/*     */     }
/*     */     
/* 445 */     TagKey<Item> tagKey = getPreferredWeaponType();
/*     */ 
/*     */ 
/*     */     
/* 449 */     boolean bool1 = (PiglinAi.isLovedItem(paramItemStack1) || (tagKey != null && paramItemStack1.is(tagKey))) ? true : false;
/* 450 */     boolean bool2 = (PiglinAi.isLovedItem(paramItemStack2) || (tagKey != null && paramItemStack2.is(tagKey))) ? true : false;
/*     */ 
/*     */ 
/*     */     
/* 454 */     if (bool1 && !bool2) {
/* 455 */       return true;
/*     */     }
/* 457 */     if (!bool1 && bool2) {
/* 458 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 462 */     return super.canReplaceCurrentItem(paramItemStack1, paramItemStack2, paramEquipmentSlot);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pickUpItem(ServerLevel paramServerLevel, ItemEntity paramItemEntity) {
/* 467 */     onItemPickup(paramItemEntity);
/* 468 */     PiglinAi.pickUpItem(paramServerLevel, this, paramItemEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean startRiding(Entity paramEntity, boolean paramBoolean1, boolean paramBoolean2) {
/* 473 */     if (isBaby() && paramEntity.getType() == EntityType.HOGLIN) {
/* 474 */       paramEntity = getTopPassenger(paramEntity, 3);
/*     */     }
/* 476 */     return super.startRiding(paramEntity, paramBoolean1, paramBoolean2);
/*     */   }
/*     */   
/*     */   private Entity getTopPassenger(Entity paramEntity, int paramInt) {
/* 480 */     List<Entity> list = paramEntity.getPassengers();
/* 481 */     if (paramInt == 1 || list.isEmpty()) {
/* 482 */       return paramEntity;
/*     */     }
/* 484 */     return getTopPassenger(list.getFirst(), paramInt - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 490 */     if (level().isClientSide()) {
/* 491 */       return null;
/*     */     }
/* 493 */     return PiglinAi.getSoundForCurrentActivity(this).orElse(null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 498 */     return SoundEvents.PIGLIN_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 503 */     return SoundEvents.PIGLIN_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 508 */     playSound(SoundEvents.PIGLIN_STEP, 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playConvertedSound() {
/* 513 */     makeSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\piglin\Piglin.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */