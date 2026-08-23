/*     */ package net.minecraft.world.entity.animal.golem;
/*     */ 
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import net.minecraft.world.entity.ContainerUser;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.LightningBolt;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.Shearable;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.ChestBlock;
/*     */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*     */ import net.minecraft.world.level.block.WeatheringCopper;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
/*     */ import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.ChestType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class CopperGolem
/*     */   extends AbstractGolem implements ContainerUser, Shearable {
/*     */   private static final long IGNORE_WEATHERING_TICK = -2L;
/*     */   private static final long UNSET_WEATHERING_TICK = -1L;
/*     */   private static final int WEATHERING_TICK_FROM = 504000;
/*     */   private static final int WEATHERING_TICK_TO = 552000;
/*     */   private static final int SPIN_ANIMATION_MIN_COOLDOWN = 200;
/*     */   private static final int SPIN_ANIMATION_MAX_COOLDOWN = 240;
/*     */   private static final float SPIN_SOUND_TIME_INTERVAL_OFFSET = 10.0F;
/*     */   private static final float TURN_TO_STATUE_CHANCE = 0.0058F;
/*     */   private static final int SPAWN_COOLDOWN_MIN = 60;
/*     */   private static final int SPAWN_COOLDOWN_MAX = 100;
/*  73 */   private static final EntityDataAccessor<WeatheringCopper.WeatherState> DATA_WEATHER_STATE = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.WEATHERING_COPPER_STATE);
/*  74 */   private static final EntityDataAccessor<CopperGolemState> COPPER_GOLEM_STATE = SynchedEntityData.defineId(CopperGolem.class, EntityDataSerializers.COPPER_GOLEM_STATE);
/*     */   
/*     */   private BlockPos openedChestPos;
/*     */   private UUID lastLightningBoltUUID;
/*  78 */   private long nextWeatheringTick = -1L;
/*  79 */   private int idleAnimationStartTick = 0;
/*     */   
/*  81 */   private final AnimationState idleAnimationState = new AnimationState();
/*  82 */   private final AnimationState interactionGetItemAnimationState = new AnimationState();
/*  83 */   private final AnimationState interactionGetNoItemAnimationState = new AnimationState();
/*  84 */   private final AnimationState interactionDropItemAnimationState = new AnimationState();
/*  85 */   private final AnimationState interactionDropNoItemAnimationState = new AnimationState();
/*     */   
/*  87 */   public static final EquipmentSlot EQUIPMENT_SLOT_ANTENNA = EquipmentSlot.SADDLE;
/*     */   
/*     */   public CopperGolem(EntityType<? extends AbstractGolem> paramEntityType, Level paramLevel) {
/*  90 */     super(paramEntityType, paramLevel);
/*  91 */     getNavigation().setRequiredPathLength(48.0F);
/*  92 */     getNavigation().setCanOpenDoors(true);
/*  93 */     setPersistenceRequired();
/*  94 */     setState(CopperGolemState.IDLE);
/*  95 */     setPathfindingMalus(PathType.DANGER_FIRE, 16.0F);
/*  96 */     setPathfindingMalus(PathType.DANGER_OTHER, 16.0F);
/*  97 */     setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
/*  98 */     getBrain().setMemory(MemoryModuleType.TRANSPORT_ITEMS_COOLDOWN_TICKS, Integer.valueOf(getRandom().nextInt(60, 100)));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 102 */     return Mob.createMobAttributes()
/* 103 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D)
/* 104 */       .add(Attributes.STEP_HEIGHT, 1.0D)
/* 105 */       .add(Attributes.MAX_HEALTH, 12.0D);
/*     */   }
/*     */   
/*     */   public CopperGolemState getState() {
/* 109 */     return (CopperGolemState)this.entityData.get(COPPER_GOLEM_STATE);
/*     */   }
/*     */   
/*     */   public void setState(CopperGolemState paramCopperGolemState) {
/* 113 */     this.entityData.set(COPPER_GOLEM_STATE, paramCopperGolemState);
/*     */   }
/*     */   
/*     */   public WeatheringCopper.WeatherState getWeatherState() {
/* 117 */     return (WeatheringCopper.WeatherState)this.entityData.get(DATA_WEATHER_STATE);
/*     */   }
/*     */   
/*     */   public void setWeatherState(WeatheringCopper.WeatherState paramWeatherState) {
/* 121 */     this.entityData.set(DATA_WEATHER_STATE, paramWeatherState);
/*     */   }
/*     */   
/*     */   public void setOpenedChestPos(BlockPos paramBlockPos) {
/* 125 */     this.openedChestPos = paramBlockPos;
/*     */   }
/*     */   
/*     */   public void clearOpenedChestPos() {
/* 129 */     this.openedChestPos = null;
/*     */   }
/*     */   
/*     */   public AnimationState getIdleAnimationState() {
/* 133 */     return this.idleAnimationState;
/*     */   }
/*     */   
/*     */   public AnimationState getInteractionGetItemAnimationState() {
/* 137 */     return this.interactionGetItemAnimationState;
/*     */   }
/*     */   
/*     */   public AnimationState getInteractionGetNoItemAnimationState() {
/* 141 */     return this.interactionGetNoItemAnimationState;
/*     */   }
/*     */   
/*     */   public AnimationState getInteractionDropItemAnimationState() {
/* 145 */     return this.interactionDropItemAnimationState;
/*     */   }
/*     */   
/*     */   public AnimationState getInteractionDropNoItemAnimationState() {
/* 149 */     return this.interactionDropNoItemAnimationState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<CopperGolem> brainProvider() {
/* 154 */     return CopperGolemAi.brainProvider();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> paramDynamic) {
/* 159 */     return CopperGolemAi.makeBrain(brainProvider().makeBrain(paramDynamic));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<CopperGolem> getBrain() {
/* 165 */     return super.getBrain();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 170 */     super.defineSynchedData(paramBuilder);
/* 171 */     paramBuilder.define(DATA_WEATHER_STATE, WeatheringCopper.WeatherState.UNAFFECTED);
/* 172 */     paramBuilder.define(COPPER_GOLEM_STATE, CopperGolemState.IDLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 177 */     super.addAdditionalSaveData(paramValueOutput);
/* 178 */     paramValueOutput.putLong("next_weather_age", this.nextWeatheringTick);
/* 179 */     paramValueOutput.store("weather_state", WeatheringCopper.WeatherState.CODEC, getWeatherState());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readAdditionalSaveData(ValueInput paramValueInput) {
/* 184 */     super.readAdditionalSaveData(paramValueInput);
/* 185 */     this.nextWeatheringTick = paramValueInput.getLongOr("next_weather_age", -1L);
/* 186 */     setWeatherState(paramValueInput.read("weather_state", WeatheringCopper.WeatherState.CODEC).orElse(WeatheringCopper.WeatherState.UNAFFECTED));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/* 191 */     ProfilerFiller profilerFiller = Profiler.get();
/* 192 */     profilerFiller.push("copperGolemBrain");
/* 193 */     getBrain().tick(paramServerLevel, (LivingEntity)this);
/* 194 */     profilerFiller.pop();
/*     */     
/* 196 */     profilerFiller.push("copperGolemActivityUpdate");
/* 197 */     CopperGolemAi.updateActivity(this);
/* 198 */     profilerFiller.pop();
/*     */     
/* 200 */     super.customServerAiStep(paramServerLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 205 */     super.tick();
/* 206 */     if (level().isClientSide()) {
/* 207 */       if (!isNoAi()) {
/* 208 */         setupAnimationStates();
/*     */       }
/*     */     } else {
/* 211 */       updateWeathering((ServerLevel)level(), level().getRandom(), level().getGameTime());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 217 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 218 */     if (itemStack.isEmpty()) {
/* 219 */       ItemStack itemStack1 = getMainHandItem();
/* 220 */       if (!itemStack1.isEmpty()) {
/* 221 */         BehaviorUtils.throwItem((LivingEntity)this, itemStack1, paramPlayer.position());
/* 222 */         setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
/* 223 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/*     */     
/* 227 */     Level level = level();
/* 228 */     if (itemStack.is(Items.SHEARS) && readyForShearing()) {
/* 229 */       if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 230 */         shear(serverLevel, SoundSource.PLAYERS, itemStack);
/* 231 */         gameEvent((Holder)GameEvent.SHEAR, (Entity)paramPlayer);
/* 232 */         itemStack.hurtAndBreak(1, (LivingEntity)paramPlayer, paramInteractionHand); }
/*     */       
/* 234 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/*     */     
/* 237 */     if (level.isClientSide()) {
/* 238 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 241 */     if (itemStack.is(Items.HONEYCOMB) && 
/* 242 */       this.nextWeatheringTick != -2L) {
/* 243 */       level.levelEvent((Entity)this, 3003, blockPosition(), 0);
/*     */       
/* 245 */       this.nextWeatheringTick = -2L;
/* 246 */       usePlayerItem(paramPlayer, paramInteractionHand, itemStack);
/* 247 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/*     */ 
/*     */     
/* 251 */     if (itemStack.is(ItemTags.AXES) && 
/* 252 */       this.nextWeatheringTick == -2L) {
/* 253 */       level.playSound(null, (Entity)this, SoundEvents.AXE_SCRAPE, getSoundSource(), 1.0F, 1.0F);
/* 254 */       level.levelEvent((Entity)this, 3004, blockPosition(), 0);
/*     */       
/* 256 */       this.nextWeatheringTick = -1L;
/* 257 */       itemStack.hurtAndBreak(1, (LivingEntity)paramPlayer, paramInteractionHand.asEquipmentSlot());
/* 258 */       return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */     } 
/*     */ 
/*     */     
/* 262 */     if (itemStack.is(ItemTags.AXES)) {
/* 263 */       WeatheringCopper.WeatherState weatherState = getWeatherState();
/* 264 */       if (weatherState != WeatheringCopper.WeatherState.UNAFFECTED) {
/* 265 */         level.playSound(null, (Entity)this, SoundEvents.AXE_SCRAPE, getSoundSource(), 1.0F, 1.0F);
/* 266 */         level.levelEvent((Entity)this, 3005, blockPosition(), 0);
/*     */         
/* 268 */         this.nextWeatheringTick = -1L;
/* 269 */         this.entityData.set(DATA_WEATHER_STATE, weatherState.previous(), true);
/* 270 */         itemStack.hurtAndBreak(1, (LivingEntity)paramPlayer, paramInteractionHand.asEquipmentSlot());
/* 271 */         return (InteractionResult)InteractionResult.SUCCESS_SERVER;
/*     */       } 
/*     */     } 
/*     */     
/* 275 */     return super.mobInteract(paramPlayer, paramInteractionHand);
/*     */   }
/*     */   
/*     */   private void updateWeathering(ServerLevel paramServerLevel, RandomSource paramRandomSource, long paramLong) {
/* 279 */     if (this.nextWeatheringTick == -2L) {
/*     */       return;
/*     */     }
/* 282 */     if (this.nextWeatheringTick == -1L) {
/* 283 */       this.nextWeatheringTick = paramLong + paramRandomSource.nextIntBetweenInclusive(504000, 552000);
/*     */       return;
/*     */     } 
/* 286 */     WeatheringCopper.WeatherState weatherState = (WeatheringCopper.WeatherState)this.entityData.get(DATA_WEATHER_STATE);
/* 287 */     boolean bool = weatherState.equals(WeatheringCopper.WeatherState.OXIDIZED);
/* 288 */     if (paramLong >= this.nextWeatheringTick && !bool) {
/* 289 */       WeatheringCopper.WeatherState weatherState1 = weatherState.next();
/* 290 */       boolean bool1 = weatherState1.equals(WeatheringCopper.WeatherState.OXIDIZED);
/* 291 */       setWeatherState(weatherState1);
/* 292 */       this.nextWeatheringTick = bool1 ? 0L : (this.nextWeatheringTick + paramRandomSource.nextIntBetweenInclusive(504000, 552000));
/*     */     } 
/*     */     
/* 295 */     if (bool && 
/* 296 */       canTurnToStatue((Level)paramServerLevel)) {
/* 297 */       turnToStatue(paramServerLevel);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canTurnToStatue(Level paramLevel) {
/* 303 */     return (paramLevel.getBlockState(blockPosition()).isAir() && paramLevel.random.nextFloat() <= 0.0058F);
/*     */   }
/*     */   
/*     */   private void turnToStatue(ServerLevel paramServerLevel) {
/* 307 */     BlockPos blockPos = blockPosition();
/* 308 */     paramServerLevel.setBlock(blockPos, (BlockState)((BlockState)Blocks.OXIDIZED_COPPER_GOLEM_STATUE.defaultBlockState()
/* 309 */         .setValue((Property)CopperGolemStatueBlock.POSE, (Comparable)CopperGolemStatueBlock.Pose.values()[this.random.nextInt(0, (CopperGolemStatueBlock.Pose.values()).length)]))
/* 310 */         .setValue((Property)CopperGolemStatueBlock.FACING, (Comparable)Direction.fromYRot(getYRot())), 3);
/* 311 */     BlockEntity blockEntity = paramServerLevel.getBlockEntity(blockPos); if (blockEntity instanceof CopperGolemStatueBlockEntity) { CopperGolemStatueBlockEntity copperGolemStatueBlockEntity = (CopperGolemStatueBlockEntity)blockEntity;
/* 312 */       copperGolemStatueBlockEntity.createStatue(this);
/* 313 */       dropPreservedEquipment(paramServerLevel);
/* 314 */       discard();
/* 315 */       playSound(SoundEvents.COPPER_GOLEM_BECOME_STATUE);
/*     */       
/* 317 */       if (isLeashed()) {
/* 318 */         if (((Boolean)paramServerLevel.getGameRules().get(GameRules.ENTITY_DROPS)).booleanValue()) {
/* 319 */           dropLeash();
/*     */         } else {
/* 321 */           removeLeash();
/*     */         } 
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   private void setupAnimationStates() {
/* 328 */     switch (getState()) {
/*     */       case IDLE:
/* 330 */         this.interactionGetNoItemAnimationState.stop();
/* 331 */         this.interactionGetItemAnimationState.stop();
/* 332 */         this.interactionDropItemAnimationState.stop();
/* 333 */         this.interactionDropNoItemAnimationState.stop();
/* 334 */         if (this.idleAnimationStartTick == this.tickCount) {
/* 335 */           this.idleAnimationState.start(this.tickCount);
/* 336 */         } else if (this.idleAnimationStartTick == 0) {
/* 337 */           this.idleAnimationStartTick = this.tickCount + this.random.nextInt(200, 240);
/*     */         } 
/*     */         
/* 340 */         if (this.tickCount == this.idleAnimationStartTick + 10.0F) {
/* 341 */           playHeadSpinSound();
/* 342 */           this.idleAnimationStartTick = 0;
/*     */         } 
/*     */         break;
/*     */       case GETTING_ITEM:
/* 346 */         this.idleAnimationState.stop();
/* 347 */         this.idleAnimationStartTick = 0;
/* 348 */         this.interactionGetNoItemAnimationState.stop();
/* 349 */         this.interactionDropItemAnimationState.stop();
/* 350 */         this.interactionDropNoItemAnimationState.stop();
/* 351 */         this.interactionGetItemAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */       case GETTING_NO_ITEM:
/* 354 */         this.idleAnimationState.stop();
/* 355 */         this.idleAnimationStartTick = 0;
/* 356 */         this.interactionGetItemAnimationState.stop();
/* 357 */         this.interactionDropNoItemAnimationState.stop();
/* 358 */         this.interactionDropItemAnimationState.stop();
/* 359 */         this.interactionGetNoItemAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */       case DROPPING_ITEM:
/* 362 */         this.idleAnimationState.stop();
/* 363 */         this.idleAnimationStartTick = 0;
/* 364 */         this.interactionGetItemAnimationState.stop();
/* 365 */         this.interactionGetNoItemAnimationState.stop();
/* 366 */         this.interactionDropNoItemAnimationState.stop();
/* 367 */         this.interactionDropItemAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */       case DROPPING_NO_ITEM:
/* 370 */         this.idleAnimationState.stop();
/* 371 */         this.idleAnimationStartTick = 0;
/* 372 */         this.interactionGetItemAnimationState.stop();
/* 373 */         this.interactionGetNoItemAnimationState.stop();
/* 374 */         this.interactionDropItemAnimationState.stop();
/* 375 */         this.interactionDropNoItemAnimationState.startIfStopped(this.tickCount);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void spawn(WeatheringCopper.WeatherState paramWeatherState) {
/* 381 */     setWeatherState(paramWeatherState);
/* 382 */     playSpawnSound();
/*     */   }
/*     */ 
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor paramServerLevelAccessor, DifficultyInstance paramDifficultyInstance, EntitySpawnReason paramEntitySpawnReason, SpawnGroupData paramSpawnGroupData) {
/* 387 */     playSpawnSound();
/* 388 */     return super.finalizeSpawn(paramServerLevelAccessor, paramDifficultyInstance, paramEntitySpawnReason, paramSpawnGroupData);
/*     */   }
/*     */   
/*     */   public void playSpawnSound() {
/* 392 */     playSound(SoundEvents.COPPER_GOLEM_SPAWN);
/*     */   }
/*     */   
/*     */   private void playHeadSpinSound() {
/* 396 */     if (!isSilent()) {
/* 397 */       level().playLocalSound(getX(), getY(), getZ(), getSpinHeadSound(), getSoundSource(), 1.0F, 1.0F, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 403 */     return CopperGolemOxidationLevels.getOxidationLevel(getWeatherState()).hurtSound();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 408 */     return CopperGolemOxidationLevels.getOxidationLevel(getWeatherState()).deathSound();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 413 */     playSound(CopperGolemOxidationLevels.getOxidationLevel(getWeatherState()).stepSound(), 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   private SoundEvent getSpinHeadSound() {
/* 417 */     return CopperGolemOxidationLevels.getOxidationLevel(getWeatherState()).spinHeadSound();
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getLeashOffset() {
/* 422 */     return new Vec3(0.0D, (0.75F * getEyeHeight()), 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasContainerOpen(ContainerOpenersCounter paramContainerOpenersCounter, BlockPos paramBlockPos) {
/* 427 */     if (this.openedChestPos == null) {
/* 428 */       return false;
/*     */     }
/* 430 */     BlockState blockState = level().getBlockState(this.openedChestPos);
/* 431 */     return (this.openedChestPos.equals(paramBlockPos) || (blockState.getBlock() instanceof ChestBlock && blockState
/* 432 */       .getValue((Property)ChestBlock.TYPE) != ChestType.SINGLE && 
/* 433 */       ChestBlock.getConnectedBlockPos(this.openedChestPos, blockState).equals(paramBlockPos)));
/*     */   }
/*     */ 
/*     */   
/*     */   public double getContainerInteractionRange() {
/* 438 */     return 3.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void shear(ServerLevel paramServerLevel, SoundSource paramSoundSource, ItemStack paramItemStack) {
/* 443 */     paramServerLevel.playSound(null, (Entity)this, SoundEvents.COPPER_GOLEM_SHEAR, paramSoundSource, 1.0F, 1.0F);
/* 444 */     ItemStack itemStack = getItemBySlot(EQUIPMENT_SLOT_ANTENNA);
/* 445 */     setItemSlot(EQUIPMENT_SLOT_ANTENNA, ItemStack.EMPTY);
/* 446 */     spawnAtLocation(paramServerLevel, itemStack, 1.5F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean readyForShearing() {
/* 451 */     return (isAlive() && getItemBySlot(EQUIPMENT_SLOT_ANTENNA).is(ItemTags.SHEARABLE_FROM_COPPER_GOLEM));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropEquipment(ServerLevel paramServerLevel) {
/* 456 */     super.dropEquipment(paramServerLevel);
/* 457 */     dropPreservedEquipment(paramServerLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actuallyHurt(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 462 */     super.actuallyHurt(paramServerLevel, paramDamageSource, paramFloat);
/* 463 */     setState(CopperGolemState.IDLE);
/*     */   }
/*     */ 
/*     */   
/*     */   public void thunderHit(ServerLevel paramServerLevel, LightningBolt paramLightningBolt) {
/* 468 */     super.thunderHit(paramServerLevel, paramLightningBolt);
/*     */     
/* 470 */     UUID uUID = paramLightningBolt.getUUID();
/* 471 */     if (!uUID.equals(this.lastLightningBoltUUID)) {
/* 472 */       this.lastLightningBoltUUID = uUID;
/* 473 */       WeatheringCopper.WeatherState weatherState = getWeatherState();
/* 474 */       if (weatherState != WeatheringCopper.WeatherState.UNAFFECTED) {
/* 475 */         this.nextWeatheringTick = -1L;
/* 476 */         this.entityData.set(DATA_WEATHER_STATE, weatherState.previous(), true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\golem\CopperGolem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */