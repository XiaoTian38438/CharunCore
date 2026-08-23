/*     */ package net.minecraft.world.entity.animal.nautilus;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.SimpleContainer;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.HasCustomInventoryScreen;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.MoverType;
/*     */ import net.minecraft.world.entity.PlayerRideableJumping;
/*     */ import net.minecraft.world.entity.SlotAccess;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.TamableAnimal;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.MoveControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.food.FoodProperties;
/*     */ import net.minecraft.world.inventory.AbstractMountInventoryMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.ItemUtils;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.equipment.Equippable;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.phys.Vec2;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public abstract class AbstractNautilus
/*     */   extends TamableAnimal implements HasCustomInventoryScreen, PlayerRideableJumping {
/*     */   public static final int INVENTORY_SLOT_OFFSET = 500;
/*     */   public static final int INVENTORY_ROWS = 3;
/*     */   public static final int SMALL_RESTRICTION_RADIUS = 16;
/*     */   public static final int LARGE_RESTRICTION_RADIUS = 32;
/*     */   public static final int RESTRICTION_RADIUS_BUFFER = 8;
/*     */   private static final int EFFECT_DURATION = 60;
/*     */   private static final int EFFECT_REFRESH_RATE = 40;
/*     */   private static final double NAUTILUS_WATER_RESISTANCE = 0.9D;
/*     */   private static final float IN_WATER_SPEED_MODIFIER = 0.011F;
/*     */   private static final float RIDDEN_SPEED_MODIFIER_IN_WATER = 0.0325F;
/*     */   private static final float RIDDEN_SPEED_MODIFIER_ON_LAND = 0.02F;
/*  81 */   private static final EntityDataAccessor<Boolean> DASH = SynchedEntityData.defineId(AbstractNautilus.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*     */   private static final int DASH_COOLDOWN_TICKS = 40;
/*     */   private static final int DASH_MINIMUM_DURATION_TICKS = 5;
/*     */   private static final float DASH_MOMENTUM_IN_WATER = 1.2F;
/*     */   private static final float DASH_MOMENTUM_ON_LAND = 0.5F;
/*  87 */   private int dashCooldown = 0;
/*     */   
/*     */   protected float playerJumpPendingScale;
/*     */   protected SimpleContainer inventory;
/*     */   private static final double BUBBLE_SPREAD_FACTOR = 0.8D;
/*     */   private static final double BUBBLE_DIRECTION_SCALE = 1.1D;
/*     */   private static final double BUBBLE_Y_OFFSET = 0.25D;
/*     */   private static final double BUBBLE_PROBABILITY_MULTIPLIER = 2.0D;
/*     */   private static final float BUBBLE_PROBABILITY_MIN = 0.15F;
/*     */   private static final float BUBBLE_PROBABILITY_MAX = 1.0F;
/*     */   
/*     */   protected AbstractNautilus(EntityType<? extends AbstractNautilus> paramEntityType, Level paramLevel) {
/*  99 */     super(paramEntityType, paramLevel);
/*     */     
/* 101 */     this.moveControl = (MoveControl)new SmoothSwimmingMoveControl((Mob)this, 85, 10, 0.011F, 0.0F, true);
/* 102 */     this.lookControl = (LookControl)new SmoothSwimmingLookControl((Mob)this, 10);
/*     */     
/* 104 */     setPathfindingMalus(PathType.WATER, 0.0F);
/* 105 */     createInventory();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFood(ItemStack paramItemStack) {
/* 110 */     return (isTame() || isBaby()) ? paramItemStack.is(ItemTags.NAUTILUS_FOOD) : paramItemStack.is(ItemTags.NAUTILUS_TAMING_ITEMS);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void usePlayerItem(Player paramPlayer, InteractionHand paramInteractionHand, ItemStack paramItemStack) {
/* 116 */     if (paramItemStack.is(ItemTags.NAUTILUS_BUCKET_FOOD)) {
/* 117 */       paramPlayer.setItemInHand(paramInteractionHand, ItemUtils.createFilledResult(paramItemStack, paramPlayer, new ItemStack((ItemLike)Items.WATER_BUCKET)));
/*     */     } else {
/* 119 */       super.usePlayerItem(paramPlayer, paramInteractionHand, paramItemStack);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 124 */     return Animal.createAnimalAttributes()
/* 125 */       .add(Attributes.MAX_HEALTH, 15.0D)
/* 126 */       .add(Attributes.MOVEMENT_SPEED, 1.0D)
/* 127 */       .add(Attributes.ATTACK_DAMAGE, 3.0D)
/* 128 */       .add(Attributes.KNOCKBACK_RESISTANCE, 0.30000001192092896D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPushedByFluid() {
/* 134 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level paramLevel) {
/* 139 */     return (PathNavigation)new WaterBoundPathNavigation((Mob)this, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos paramBlockPos, LevelReader paramLevelReader) {
/* 144 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public static boolean checkNautilusSpawnRules(EntityType<? extends AbstractNautilus> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 148 */     int i = paramLevelAccessor.getSeaLevel();
/* 149 */     int j = i - 25;
/* 150 */     return (paramBlockPos.getY() >= j && paramBlockPos
/* 151 */       .getY() <= i - 5 && paramLevelAccessor
/* 152 */       .getFluidState(paramBlockPos.below()).is(FluidTags.WATER) && paramLevelAccessor
/* 153 */       .getBlockState(paramBlockPos.above()).is(Blocks.WATER));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader paramLevelReader) {
/* 158 */     return paramLevelReader.isUnobstructed((Entity)this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot paramEquipmentSlot) {
/* 163 */     if (paramEquipmentSlot == EquipmentSlot.SADDLE || paramEquipmentSlot == EquipmentSlot.BODY) {
/* 164 */       return (isAlive() && !isBaby() && isTame());
/*     */     }
/* 166 */     return super.canUseSlot(paramEquipmentSlot);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canDispenserEquipIntoSlot(EquipmentSlot paramEquipmentSlot) {
/* 171 */     return (paramEquipmentSlot == EquipmentSlot.BODY || paramEquipmentSlot == EquipmentSlot.SADDLE || super.canDispenserEquipIntoSlot(paramEquipmentSlot));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean canAddPassenger(Entity paramEntity) {
/* 178 */     return !isVehicle();
/*     */   }
/*     */ 
/*     */   
/*     */   public LivingEntity getControllingPassenger() {
/* 183 */     Entity entity = getFirstPassenger();
/* 184 */     if (isSaddled() && entity instanceof Player) return (LivingEntity)entity;
/*     */ 
/*     */     
/* 187 */     return super.getControllingPassenger();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Vec3 getRiddenInput(Player paramPlayer, Vec3 paramVec3) {
/* 193 */     float f1 = paramPlayer.xxa;
/* 194 */     float f2 = 0.0F;
/* 195 */     float f3 = 0.0F;
/*     */     
/* 197 */     if (paramPlayer.zza != 0.0F) {
/*     */       
/* 199 */       float f4 = Mth.cos((paramPlayer.getXRot() * 0.017453292F));
/* 200 */       float f5 = -Mth.sin((paramPlayer.getXRot() * 0.017453292F));
/* 201 */       if (paramPlayer.zza < 0.0F) {
/*     */         
/* 203 */         f4 *= -0.5F;
/* 204 */         f5 *= -0.5F;
/*     */       } 
/* 206 */       f3 = f5;
/* 207 */       f2 = f4;
/*     */     } 
/* 209 */     return new Vec3(f1, f3, f2);
/*     */   }
/*     */   
/*     */   protected Vec2 getRiddenRotation(LivingEntity paramLivingEntity) {
/* 213 */     return new Vec2(paramLivingEntity.getXRot() * 0.5F, paramLivingEntity.getYRot());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tickRidden(Player paramPlayer, Vec3 paramVec3) {
/* 218 */     super.tickRidden(paramPlayer, paramVec3);
/* 219 */     Vec2 vec2 = getRiddenRotation((LivingEntity)paramPlayer);
/* 220 */     float f1 = getYRot();
/* 221 */     float f2 = Mth.wrapDegrees(vec2.y - f1);
/* 222 */     float f3 = 0.5F;
/* 223 */     f1 += f2 * 0.5F;
/* 224 */     setRot(f1, vec2.x);
/* 225 */     this.yRotO = this.yBodyRot = this.yHeadRot = f1;
/* 226 */     if (isLocalInstanceAuthoritative()) {
/*     */       
/* 228 */       if (this.playerJumpPendingScale > 0.0F && !isJumping()) {
/* 229 */         executeRidersJump(this.playerJumpPendingScale, paramPlayer);
/*     */       }
/* 231 */       this.playerJumpPendingScale = 0.0F;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void travelInWater(Vec3 paramVec3, double paramDouble1, boolean paramBoolean, double paramDouble2) {
/* 237 */     float f = getSpeed();
/* 238 */     moveRelative(f, paramVec3);
/* 239 */     move(MoverType.SELF, getDeltaMovement());
/* 240 */     setDeltaMovement(getDeltaMovement().scale(0.9D));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getRiddenSpeed(Player paramPlayer) {
/* 245 */     return isInWater() ? (0.0325F * (float)getAttributeValue(Attributes.MOVEMENT_SPEED)) : (0.02F * (float)getAttributeValue(Attributes.MOVEMENT_SPEED));
/*     */   }
/*     */   
/*     */   protected void doPlayerRide(Player paramPlayer) {
/* 249 */     if (!level().isClientSide()) {
/* 250 */       paramPlayer.startRiding((Entity)this);
/* 251 */       if (!isVehicle()) {
/* 252 */         clearHome();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int getNautilusRestrictionRadius() {
/* 260 */     if (!isBaby() && getItemBySlot(EquipmentSlot.SADDLE).isEmpty()) {
/* 261 */       return 32;
/*     */     }
/* 263 */     return 16;
/*     */   }
/*     */   
/*     */   protected void checkRestriction() {
/* 267 */     if (isLeashed() || isVehicle() || !isTame()) {
/*     */       return;
/*     */     }
/* 270 */     int i = getNautilusRestrictionRadius();
/* 271 */     if (hasHome() && getHomePosition().closerThan((Vec3i)blockPosition(), (i + 8)) && i == getHomeRadius()) {
/*     */       return;
/*     */     }
/* 274 */     setHomeTo(blockPosition(), i);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/* 279 */     checkRestriction();
/* 280 */     super.customServerAiStep(paramServerLevel);
/*     */   }
/*     */   
/*     */   private void applyEffects(Level paramLevel) {
/* 284 */     Entity entity = getFirstPassenger();
/*     */     
/* 286 */     if (entity instanceof Player) { Player player = (Player)entity;
/* 287 */       boolean bool = player.hasEffect(MobEffects.BREATH_OF_THE_NAUTILUS);
/* 288 */       boolean bool1 = (paramLevel.getGameTime() % 40L == 0L) ? true : false;
/* 289 */       if (!bool || bool1) {
/* 290 */         player.addEffect(new MobEffectInstance(MobEffects.BREATH_OF_THE_NAUTILUS, 60, 0, true, true, true));
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private void spawnBubbles() {
/* 297 */     double d1 = getDeltaMovement().length();
/* 298 */     double d2 = Mth.clamp(d1 * 2.0D, 0.15000000596046448D, 1.0D);
/* 299 */     if (this.random.nextFloat() < d2) {
/*     */       
/* 301 */       float f1 = getYRot();
/* 302 */       float f2 = Mth.clamp(getXRot(), -10.0F, 10.0F);
/* 303 */       Vec3 vec3 = calculateViewVector(f2, f1);
/* 304 */       double d3 = this.random.nextDouble() * 0.8D * (1.0D + d1);
/* 305 */       double d4 = (this.random.nextFloat() - 0.5D) * d3;
/* 306 */       double d5 = (this.random.nextFloat() - 0.5D) * d3;
/* 307 */       double d6 = (this.random.nextFloat() - 0.5D) * d3;
/* 308 */       level().addParticle((ParticleOptions)ParticleTypes.BUBBLE, getX() - vec3.x * 1.1D, getY() - vec3.y + 0.25D, getZ() - vec3.z * 1.1D, d4, d5, d6);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 314 */     super.tick();
/* 315 */     if (!level().isClientSide()) {
/* 316 */       applyEffects(level());
/*     */     }
/*     */     
/* 319 */     if (isDashing() && this.dashCooldown < 35) {
/* 320 */       setDashing(false);
/*     */     }
/* 322 */     if (this.dashCooldown > 0) {
/* 323 */       this.dashCooldown--;
/* 324 */       if (this.dashCooldown == 0) {
/* 325 */         makeSound(getDashReadySound());
/*     */       }
/*     */     } 
/*     */     
/* 329 */     if (isInWater()) {
/* 330 */       spawnBubbles();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canJump() {
/* 338 */     return isSaddled();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPlayerJump(int paramInt) {
/* 343 */     if (!isSaddled() || this.dashCooldown > 0) {
/*     */       return;
/*     */     }
/* 346 */     this.playerJumpPendingScale = getPlayerJumpPendingScale(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 351 */     super.defineSynchedData(paramBuilder);
/* 352 */     paramBuilder.define(DASH, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean isDashing() {
/* 356 */     return ((Boolean)this.entityData.get(DASH)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setDashing(boolean paramBoolean) {
/* 360 */     this.entityData.set(DASH, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */   
/*     */   protected void executeRidersJump(float paramFloat, Player paramPlayer) {
/* 364 */     addDeltaMovement(paramPlayer.getLookAngle()
/* 365 */         .scale(((isInWater() ? 1.2F : 0.5F) * paramFloat) * getAttributeValue(Attributes.MOVEMENT_SPEED) * getBlockSpeedFactor()));
/*     */ 
/*     */     
/* 368 */     this.dashCooldown = 40;
/* 369 */     setDashing(true);
/* 370 */     this.needsSync = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStartJump(int paramInt) {
/* 375 */     makeSound(getDashSound());
/* 376 */     gameEvent((Holder)GameEvent.ENTITY_ACTION);
/* 377 */     setDashing(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getJumpCooldown() {
/* 382 */     return this.dashCooldown;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/* 387 */     if (!this.firstTick && 
/* 388 */       DASH.equals(paramEntityDataAccessor)) {
/* 389 */       this.dashCooldown = (this.dashCooldown == 0) ? 40 : this.dashCooldown;
/*     */     }
/*     */     
/* 392 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleStopJump() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos paramBlockPos, BlockState paramBlockState) {}
/*     */ 
/*     */   
/*     */   protected SoundEvent getDashSound() {
/* 405 */     return null;
/*     */   }
/*     */   
/*     */   protected SoundEvent getDashReadySound() {
/* 409 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult interact(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 414 */     setPersistenceRequired();
/* 415 */     return super.interact(paramPlayer, paramInteractionHand);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 420 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/*     */     
/* 422 */     if (isBaby()) {
/* 423 */       return super.mobInteract(paramPlayer, paramInteractionHand);
/*     */     }
/*     */     
/* 426 */     if (isTame() && paramPlayer.isSecondaryUseActive()) {
/* 427 */       openCustomInventoryScreen(paramPlayer);
/* 428 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 431 */     if (!itemStack.isEmpty()) {
/*     */       
/* 433 */       if (!level().isClientSide() && !isTame() && isFood(itemStack)) {
/* 434 */         usePlayerItem(paramPlayer, paramInteractionHand, itemStack);
/* 435 */         tryToTame(paramPlayer);
/* 436 */         return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/* 437 */       }  if (isFood(itemStack) && getHealth() < getMaxHealth()) {
/* 438 */         FoodProperties foodProperties = (FoodProperties)itemStack.get(DataComponents.FOOD);
/* 439 */         heal((foodProperties != null) ? (2 * foodProperties.nutrition()) : 1.0F);
/* 440 */         usePlayerItem(paramPlayer, paramInteractionHand, itemStack);
/* 441 */         playEatingSound();
/* 442 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/*     */       
/* 445 */       InteractionResult interactionResult = itemStack.interactLivingEntity(paramPlayer, (LivingEntity)this, paramInteractionHand);
/* 446 */       if (interactionResult.consumesAction()) {
/* 447 */         return interactionResult;
/*     */       }
/*     */     } 
/*     */     
/* 451 */     if (isTame() && !paramPlayer.isSecondaryUseActive() && !isFood(itemStack)) {
/* 452 */       doPlayerRide(paramPlayer);
/* 453 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 456 */     return super.mobInteract(paramPlayer, paramInteractionHand);
/*     */   }
/*     */   
/*     */   private void tryToTame(Player paramPlayer) {
/* 460 */     if (this.random.nextInt(3) == 0) {
/* 461 */       tame(paramPlayer);
/* 462 */       this.navigation.stop();
/* 463 */       level().broadcastEntityEvent((Entity)this, (byte)7);
/*     */     } else {
/* 465 */       level().broadcastEntityEvent((Entity)this, (byte)6);
/*     */     } 
/* 467 */     playEatingSound();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeWhenFarAway(double paramDouble) {
/* 472 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 477 */     boolean bool = super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/* 478 */     if (bool) { Entity entity = paramDamageSource.getEntity(); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/* 479 */         NautilusAi.setAngerTarget(paramServerLevel, this, livingEntity); }
/*     */        }
/* 481 */      return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canBeAffected(MobEffectInstance paramMobEffectInstance) {
/* 487 */     if (paramMobEffectInstance.getEffect() == MobEffects.POISON) {
/* 488 */       return false;
/*     */     }
/* 490 */     return super.canBeAffected(paramMobEffectInstance);
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 495 */     RandomSource randomSource = paramServerLevelAccessor.getRandom();
/* 496 */     NautilusAi.initMemories(this, randomSource);
/* 497 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */   }
/*     */   
/*     */   protected Holder<SoundEvent> getEquipSound(EquipmentSlot paramEquipmentSlot, ItemStack paramItemStack, Equippable paramEquippable) {
/* 501 */     if (paramEquipmentSlot == EquipmentSlot.SADDLE && isUnderWater()) {
/* 502 */       return (Holder<SoundEvent>)SoundEvents.NAUTILUS_SADDLE_UNDERWATER_EQUIP;
/*     */     }
/* 504 */     if (paramEquipmentSlot == EquipmentSlot.SADDLE) {
/* 505 */       return (Holder<SoundEvent>)SoundEvents.NAUTILUS_SADDLE_EQUIP;
/*     */     }
/* 507 */     return super.getEquipSound(paramEquipmentSlot, paramItemStack, paramEquippable);
/*     */   }
/*     */   
/*     */   public final int getInventorySize() {
/* 511 */     return AbstractMountInventoryMenu.getInventorySize(getInventoryColumns());
/*     */   }
/*     */   
/*     */   protected void createInventory() {
/* 515 */     SimpleContainer simpleContainer = this.inventory;
/* 516 */     this.inventory = new SimpleContainer(getInventorySize());
/* 517 */     if (simpleContainer != null) {
/* 518 */       int i = Math.min(simpleContainer.getContainerSize(), this.inventory.getContainerSize());
/* 519 */       for (byte b = 0; b < i; b++) {
/* 520 */         ItemStack itemStack = simpleContainer.getItem(b);
/* 521 */         if (!itemStack.isEmpty()) {
/* 522 */           this.inventory.setItem(b, itemStack.copy());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void openCustomInventoryScreen(Player paramPlayer) {
/* 530 */     if (!level().isClientSide() && (!isVehicle() || hasPassenger((Entity)paramPlayer)) && isTame()) {
/* 531 */       paramPlayer.openNautilusInventory(this, (Container)this.inventory);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SlotAccess getSlot(int paramInt) {
/* 537 */     int i = paramInt - 500;
/* 538 */     if (i >= 0 && i < this.inventory.getContainerSize()) {
/* 539 */       return this.inventory.getSlot(i);
/*     */     }
/* 541 */     return super.getSlot(paramInt);
/*     */   }
/*     */   
/*     */   public boolean hasInventoryChanged(Container paramContainer) {
/* 545 */     return (this.inventory != paramContainer);
/*     */   }
/*     */   
/*     */   public int getInventoryColumns() {
/* 549 */     return 0;
/*     */   }
/*     */   
/*     */   protected boolean isMobControlled() {
/* 553 */     return getFirstPassenger() instanceof Mob;
/*     */   }
/*     */   
/*     */   protected boolean isAggravated() {
/* 557 */     return (getBrain().hasMemoryValue(MemoryModuleType.ANGRY_AT) || getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\nautilus\AbstractNautilus.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */