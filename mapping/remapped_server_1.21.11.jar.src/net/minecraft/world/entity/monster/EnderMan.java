/*     */ package net.minecraft.world.entity.monster;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.tags.DamageTypeTags;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeInstance;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.FloatGoal;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.entity.projectile.throwableitemprojectile.AbstractThrownPotion;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.item.enchantment.providers.VanillaEnchantmentProviders;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.pathfinder.PathType;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class EnderMan extends Monster implements NeutralMob {
/*  73 */   private static final Identifier SPEED_MODIFIER_ATTACKING_ID = Identifier.withDefaultNamespace("attacking");
/*  74 */   private static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(SPEED_MODIFIER_ATTACKING_ID, 0.15000000596046448D, AttributeModifier.Operation.ADD_VALUE);
/*     */   
/*     */   private static final int DELAY_BETWEEN_CREEPY_STARE_SOUND = 400;
/*     */   private static final int MIN_DEAGGRESSION_TIME = 600;
/*  78 */   private static final EntityDataAccessor<Optional<BlockState>> DATA_CARRY_STATE = SynchedEntityData.defineId(EnderMan.class, EntityDataSerializers.OPTIONAL_BLOCK_STATE);
/*  79 */   private static final EntityDataAccessor<Boolean> DATA_CREEPY = SynchedEntityData.defineId(EnderMan.class, EntityDataSerializers.BOOLEAN);
/*  80 */   private static final EntityDataAccessor<Boolean> DATA_STARED_AT = SynchedEntityData.defineId(EnderMan.class, EntityDataSerializers.BOOLEAN);
/*     */   
/*  82 */   private int lastStareSound = Integer.MIN_VALUE;
/*     */   
/*     */   private int targetChangeTime;
/*  85 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   private long persistentAngerEndTime;
/*     */   private EntityReference<LivingEntity> persistentAngerTarget;
/*     */   
/*     */   public EnderMan(EntityType<? extends EnderMan> paramEntityType, Level paramLevel) {
/*  90 */     super((EntityType)paramEntityType, paramLevel);
/*     */     
/*  92 */     setPathfindingMalus(PathType.WATER, -1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  97 */     this.goalSelector.addGoal(0, (Goal)new FloatGoal((Mob)this));
/*  98 */     this.goalSelector.addGoal(1, new EndermanFreezeWhenLookedAt(this));
/*  99 */     this.goalSelector.addGoal(2, (Goal)new MeleeAttackGoal(this, 1.0D, false));
/* 100 */     this.goalSelector.addGoal(7, (Goal)new WaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
/* 101 */     this.goalSelector.addGoal(8, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 8.0F));
/* 102 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/* 104 */     this.goalSelector.addGoal(10, new EndermanLeaveBlockGoal(this));
/* 105 */     this.goalSelector.addGoal(11, new EndermanTakeBlockGoal(this));
/*     */     
/* 107 */     this.targetSelector.addGoal(1, (Goal)new EndermanLookForPlayerGoal(this, this::isAngryAt));
/* 108 */     this.targetSelector.addGoal(2, (Goal)new HurtByTargetGoal(this, new Class[0]));
/* 109 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Endermite.class, true, false));
/* 110 */     this.targetSelector.addGoal(4, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, false));
/*     */   }
/*     */ 
/*     */   
/*     */   public float getWalkTargetValue(BlockPos paramBlockPos, LevelReader paramLevelReader) {
/* 115 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 119 */     return Monster.createMonsterAttributes()
/* 120 */       .add(Attributes.MAX_HEALTH, 40.0D)
/* 121 */       .add(Attributes.MOVEMENT_SPEED, 0.30000001192092896D)
/* 122 */       .add(Attributes.ATTACK_DAMAGE, 7.0D)
/* 123 */       .add(Attributes.FOLLOW_RANGE, 64.0D)
/* 124 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTarget(LivingEntity paramLivingEntity) {
/* 129 */     super.setTarget(paramLivingEntity);
/*     */     
/* 131 */     AttributeInstance attributeInstance = getAttribute(Attributes.MOVEMENT_SPEED);
/*     */     
/* 133 */     if (paramLivingEntity == null) {
/* 134 */       this.targetChangeTime = 0;
/* 135 */       this.entityData.set(DATA_CREEPY, Boolean.valueOf(false));
/* 136 */       this.entityData.set(DATA_STARED_AT, Boolean.valueOf(false));
/*     */       
/* 138 */       attributeInstance.removeModifier(SPEED_MODIFIER_ATTACKING_ID);
/*     */     } else {
/* 140 */       this.targetChangeTime = this.tickCount;
/* 141 */       this.entityData.set(DATA_CREEPY, Boolean.valueOf(true));
/*     */       
/* 143 */       if (!attributeInstance.hasModifier(SPEED_MODIFIER_ATTACKING_ID)) {
/* 144 */         attributeInstance.addTransientModifier(SPEED_MODIFIER_ATTACKING);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 151 */     super.defineSynchedData(paramBuilder);
/*     */     
/* 153 */     paramBuilder.define(DATA_CARRY_STATE, Optional.empty());
/* 154 */     paramBuilder.define(DATA_CREEPY, Boolean.valueOf(false));
/* 155 */     paramBuilder.define(DATA_STARED_AT, Boolean.valueOf(false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPersistentAngerTimer() {
/* 160 */     setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistentAngerEndTime(long paramLong) {
/* 165 */     this.persistentAngerEndTime = paramLong;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPersistentAngerEndTime() {
/* 170 */     return this.persistentAngerEndTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistentAngerTarget(EntityReference<LivingEntity> paramEntityReference) {
/* 175 */     this.persistentAngerTarget = paramEntityReference;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityReference<LivingEntity> getPersistentAngerTarget() {
/* 180 */     return this.persistentAngerTarget;
/*     */   }
/*     */   
/*     */   public void playStareSound() {
/* 184 */     if (this.tickCount >= this.lastStareSound + 400) {
/* 185 */       this.lastStareSound = this.tickCount;
/* 186 */       if (!isSilent()) {
/* 187 */         level().playLocalSound(getX(), getEyeY(), getZ(), SoundEvents.ENDERMAN_STARE, getSoundSource(), 2.5F, 1.0F, false);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSyncedDataUpdated(EntityDataAccessor<?> paramEntityDataAccessor) {
/* 194 */     if (DATA_CREEPY.equals(paramEntityDataAccessor) && 
/* 195 */       hasBeenStaredAt() && level().isClientSide()) {
/* 196 */       playStareSound();
/*     */     }
/*     */     
/* 199 */     super.onSyncedDataUpdated(paramEntityDataAccessor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 204 */     super.addAdditionalSaveData(paramValueOutput);
/* 205 */     BlockState blockState = getCarriedBlock();
/* 206 */     if (blockState != null) {
/* 207 */       paramValueOutput.store("carriedBlockState", BlockState.CODEC, blockState);
/*     */     }
/* 209 */     addPersistentAngerSaveData(paramValueOutput);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 214 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/* 216 */     setCarriedBlock(paramValueInput.read("carriedBlockState", BlockState.CODEC)
/* 217 */         .filter(paramBlockState -> !paramBlockState.isAir())
/* 218 */         .orElse(null));
/*     */     
/* 220 */     readPersistentAngerSaveData(level(), paramValueInput);
/*     */   }
/*     */   
/*     */   boolean isBeingStaredBy(Player paramPlayer) {
/* 224 */     if (!LivingEntity.PLAYER_NOT_WEARING_DISGUISE_ITEM.test(paramPlayer)) {
/* 225 */       return false;
/*     */     }
/* 227 */     return isLookingAtMe((LivingEntity)paramPlayer, 0.025D, true, false, new double[] { getEyeY() });
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 232 */     if (level().isClientSide()) {
/* 233 */       for (byte b = 0; b < 2; b++) {
/* 234 */         level().addParticle((ParticleOptions)ParticleTypes.PORTAL, getRandomX(0.5D), getRandomY() - 0.25D, getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(), (this.random.nextDouble() - 0.5D) * 2.0D);
/*     */       }
/*     */     }
/*     */     
/* 238 */     this.jumping = false;
/*     */     
/* 240 */     if (!level().isClientSide()) {
/* 241 */       updatePersistentAnger((ServerLevel)level(), true);
/*     */     }
/* 243 */     super.aiStep();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSensitiveToWater() {
/* 248 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/* 253 */     if (paramServerLevel.isBrightOutside() && this.tickCount >= this.targetChangeTime + 600) {
/* 254 */       float f = getLightLevelDependentMagicValue();
/* 255 */       if (f > 0.5F && 
/* 256 */         paramServerLevel.canSeeSky(blockPosition()) && this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
/* 257 */         setTarget((LivingEntity)null);
/* 258 */         teleport();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 263 */     super.customServerAiStep(paramServerLevel);
/*     */   }
/*     */   
/*     */   protected boolean teleport() {
/* 267 */     if (level().isClientSide() || !isAlive()) {
/* 268 */       return false;
/*     */     }
/*     */     
/* 271 */     double d1 = getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
/* 272 */     double d2 = getY() + (this.random.nextInt(64) - 32);
/* 273 */     double d3 = getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
/* 274 */     return teleport(d1, d2, d3);
/*     */   }
/*     */   
/*     */   boolean teleportTowards(Entity paramEntity) {
/* 278 */     Vec3 vec3 = new Vec3(getX() - paramEntity.getX(), getY(0.5D) - paramEntity.getEyeY(), getZ() - paramEntity.getZ());
/* 279 */     vec3 = vec3.normalize();
/* 280 */     double d1 = 16.0D;
/* 281 */     double d2 = getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vec3.x * 16.0D;
/* 282 */     double d3 = getY() + (this.random.nextInt(16) - 8) - vec3.y * 16.0D;
/* 283 */     double d4 = getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vec3.z * 16.0D;
/* 284 */     return teleport(d2, d3, d4);
/*     */   }
/*     */   
/*     */   private boolean teleport(double paramDouble1, double paramDouble2, double paramDouble3) {
/* 288 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(paramDouble1, paramDouble2, paramDouble3);
/* 289 */     while (mutableBlockPos.getY() > level().getMinY() && !level().getBlockState((BlockPos)mutableBlockPos).blocksMotion()) {
/* 290 */       mutableBlockPos.move(Direction.DOWN);
/*     */     }
/* 292 */     BlockState blockState = level().getBlockState((BlockPos)mutableBlockPos);
/* 293 */     boolean bool1 = blockState.blocksMotion();
/* 294 */     boolean bool2 = blockState.getFluidState().is(FluidTags.WATER);
/* 295 */     if (!bool1 || bool2) {
/* 296 */       return false;
/*     */     }
/*     */     
/* 299 */     Vec3 vec3 = position();
/* 300 */     boolean bool3 = randomTeleport(paramDouble1, paramDouble2, paramDouble3, true);
/* 301 */     if (bool3) {
/* 302 */       level().gameEvent((Holder)GameEvent.TELEPORT, vec3, GameEvent.Context.of((Entity)this));
/*     */       
/* 304 */       if (!isSilent()) {
/* 305 */         level().playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, getSoundSource(), 1.0F, 1.0F);
/* 306 */         playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
/*     */       } 
/*     */     } 
/*     */     
/* 310 */     return bool3;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 315 */     return isCreepy() ? SoundEvents.ENDERMAN_SCREAM : SoundEvents.ENDERMAN_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 320 */     return SoundEvents.ENDERMAN_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 325 */     return SoundEvents.ENDERMAN_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropCustomDeathLoot(ServerLevel paramServerLevel, DamageSource paramDamageSource, boolean paramBoolean) {
/* 330 */     super.dropCustomDeathLoot(paramServerLevel, paramDamageSource, paramBoolean);
/* 331 */     BlockState blockState = getCarriedBlock();
/* 332 */     if (blockState != null) {
/*     */       
/* 334 */       ItemStack itemStack = new ItemStack((ItemLike)Items.DIAMOND_AXE);
/* 335 */       EnchantmentHelper.enchantItemFromProvider(itemStack, paramServerLevel.registryAccess(), VanillaEnchantmentProviders.ENDERMAN_LOOT_DROP, paramServerLevel.getCurrentDifficultyAt(blockPosition()), getRandom());
/*     */ 
/*     */ 
/*     */       
/* 339 */       LootParams.Builder builder = (new LootParams.Builder((ServerLevel)level())).withParameter(LootContextParams.ORIGIN, position()).withParameter(LootContextParams.TOOL, itemStack).withOptionalParameter(LootContextParams.THIS_ENTITY, this);
/* 340 */       List list = blockState.getDrops(builder);
/* 341 */       for (ItemStack itemStack1 : list) {
/* 342 */         spawnAtLocation(paramServerLevel, itemStack1);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setCarriedBlock(BlockState paramBlockState) {
/* 348 */     this.entityData.set(DATA_CARRY_STATE, Optional.ofNullable(paramBlockState));
/*     */   }
/*     */   
/*     */   public BlockState getCarriedBlock() {
/* 352 */     return ((Optional<BlockState>)this.entityData.get(DATA_CARRY_STATE)).orElse(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 357 */     if (isInvulnerableTo(paramServerLevel, paramDamageSource)) {
/* 358 */       return false;
/*     */     }
/*     */     
/* 361 */     Entity entity = paramDamageSource.getDirectEntity(); AbstractThrownPotion abstractThrownPotion2 = (AbstractThrownPotion)entity, abstractThrownPotion1 = (entity instanceof AbstractThrownPotion) ? abstractThrownPotion2 : null;
/* 362 */     if (paramDamageSource.is(DamageTypeTags.IS_PROJECTILE) || abstractThrownPotion1 != null) {
/* 363 */       boolean bool1 = (abstractThrownPotion1 != null && hurtWithCleanWater(paramServerLevel, paramDamageSource, abstractThrownPotion1, paramFloat)) ? true : false;
/* 364 */       for (byte b = 0; b < 64; b++) {
/* 365 */         if (teleport()) {
/* 366 */           return true;
/*     */         }
/*     */       } 
/* 369 */       return bool1;
/*     */     } 
/*     */     
/* 372 */     boolean bool = super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/* 373 */     if (!(paramDamageSource.getEntity() instanceof LivingEntity) && this.random.nextInt(10) != 0) {
/* 374 */       teleport();
/*     */     }
/*     */     
/* 377 */     return bool;
/*     */   }
/*     */   
/*     */   private boolean hurtWithCleanWater(ServerLevel paramServerLevel, DamageSource paramDamageSource, AbstractThrownPotion paramAbstractThrownPotion, float paramFloat) {
/* 381 */     ItemStack itemStack = paramAbstractThrownPotion.getItem();
/*     */     
/* 383 */     PotionContents potionContents = (PotionContents)itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/* 384 */     if (potionContents.is(Potions.WATER)) {
/* 385 */       return super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/*     */     }
/*     */     
/* 388 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isCreepy() {
/* 392 */     return ((Boolean)this.entityData.get(DATA_CREEPY)).booleanValue();
/*     */   }
/*     */   
/*     */   public boolean hasBeenStaredAt() {
/* 396 */     return ((Boolean)this.entityData.get(DATA_STARED_AT)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setBeingStaredAt() {
/* 400 */     this.entityData.set(DATA_STARED_AT, Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresCustomPersistence() {
/* 405 */     return (super.requiresCustomPersistence() || getCarriedBlock() != null);
/*     */   }
/*     */   
/*     */   private static class EndermanLookForPlayerGoal
/*     */     extends NearestAttackableTargetGoal<Player> {
/*     */     private final EnderMan enderman;
/*     */     private Player pendingTarget;
/*     */     private int aggroTime;
/*     */     private int teleportTime;
/*     */     private final TargetingConditions startAggroTargetConditions;
/* 415 */     private final TargetingConditions continueAggroTargetConditions = TargetingConditions.forCombat().ignoreLineOfSight();
/*     */     private final TargetingConditions.Selector isAngerInducing;
/*     */     
/*     */     public EndermanLookForPlayerGoal(EnderMan param1EnderMan, TargetingConditions.Selector param1Selector) {
/* 419 */       super((Mob)param1EnderMan, Player.class, 10, false, false, param1Selector);
/* 420 */       this.enderman = param1EnderMan;
/* 421 */       this.isAngerInducing = ((param1LivingEntity, param1ServerLevel) -> ((param1EnderMan.isBeingStaredBy((Player)param1LivingEntity) || param1EnderMan.isAngryAt(param1LivingEntity, param1ServerLevel)) && !param1EnderMan.hasIndirectPassenger((Entity)param1LivingEntity)));
/*     */       
/* 423 */       this.startAggroTargetConditions = TargetingConditions.forCombat().range(getFollowDistance()).selector(this.isAngerInducing);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 428 */       this.pendingTarget = getServerLevel((Entity)this.enderman).getNearestPlayer(this.startAggroTargetConditions.range(getFollowDistance()), (LivingEntity)this.enderman);
/* 429 */       return (this.pendingTarget != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 434 */       this.aggroTime = adjustedTickDelay(5);
/* 435 */       this.teleportTime = 0;
/* 436 */       this.enderman.setBeingStaredAt();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void stop() {
/* 442 */       this.pendingTarget = null;
/*     */       
/* 444 */       super.stop();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canContinueToUse() {
/* 449 */       if (this.pendingTarget != null) {
/* 450 */         if (!this.isAngerInducing.test((LivingEntity)this.pendingTarget, getServerLevel((Entity)this.enderman))) {
/* 451 */           return false;
/*     */         }
/* 453 */         this.enderman.lookAt((Entity)this.pendingTarget, 10.0F, 10.0F);
/* 454 */         return true;
/* 455 */       }  if (this.target != null) {
/* 456 */         if (this.enderman.hasIndirectPassenger((Entity)this.target))
/* 457 */           return false; 
/* 458 */         if (this.continueAggroTargetConditions.test(getServerLevel((Entity)this.enderman), (LivingEntity)this.enderman, this.target)) {
/* 459 */           return true;
/*     */         }
/*     */       } 
/* 462 */       return super.canContinueToUse();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 467 */       if (this.enderman.getTarget() == null) {
/* 468 */         setTarget(null);
/*     */       }
/*     */       
/* 471 */       if (this.pendingTarget != null) {
/* 472 */         if (--this.aggroTime <= 0) {
/* 473 */           this.target = (LivingEntity)this.pendingTarget;
/* 474 */           this.pendingTarget = null;
/* 475 */           super.start();
/*     */         } 
/*     */       } else {
/* 478 */         if (this.target != null && !this.enderman.isPassenger()) {
/* 479 */           if (this.enderman.isBeingStaredBy((Player)this.target)) {
/* 480 */             if (this.target.distanceToSqr((Entity)this.enderman) < 16.0D) {
/* 481 */               this.enderman.teleport();
/*     */             }
/* 483 */             this.teleportTime = 0;
/* 484 */           } else if (this.target.distanceToSqr((Entity)this.enderman) > 256.0D && 
/* 485 */             this.teleportTime++ >= adjustedTickDelay(30) && 
/* 486 */             this.enderman.teleportTowards((Entity)this.target)) {
/* 487 */             this.teleportTime = 0;
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 493 */         super.tick();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EndermanFreezeWhenLookedAt extends Goal {
/*     */     private final EnderMan enderman;
/*     */     private LivingEntity target;
/*     */     
/*     */     public EndermanFreezeWhenLookedAt(EnderMan param1EnderMan) {
/* 503 */       this.enderman = param1EnderMan;
/* 504 */       setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*     */     }
/*     */     
/*     */     public boolean canUse() {
/*     */       Player player;
/* 509 */       this.target = this.enderman.getTarget();
/* 510 */       LivingEntity livingEntity = this.target; if (livingEntity instanceof Player) { player = (Player)livingEntity; }
/* 511 */       else { return false; }
/*     */       
/* 513 */       double d = this.target.distanceToSqr((Entity)this.enderman);
/* 514 */       if (d > 256.0D) {
/* 515 */         return false;
/*     */       }
/* 517 */       return this.enderman.isBeingStaredBy(player);
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 522 */       this.enderman.getNavigation().stop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 527 */       this.enderman.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EndermanLeaveBlockGoal extends Goal {
/*     */     private final EnderMan enderman;
/*     */     
/*     */     public EndermanLeaveBlockGoal(EnderMan param1EnderMan) {
/* 535 */       this.enderman = param1EnderMan;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 540 */       if (this.enderman.getCarriedBlock() == null) {
/* 541 */         return false;
/*     */       }
/* 543 */       if (!((Boolean)getServerLevel((Entity)this.enderman).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 544 */         return false;
/*     */       }
/* 546 */       return (this.enderman.getRandom().nextInt(reducedTickDelay(2000)) == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 551 */       RandomSource randomSource = this.enderman.getRandom();
/* 552 */       Level level = this.enderman.level();
/*     */       
/* 554 */       int i = Mth.floor(this.enderman.getX() - 1.0D + randomSource.nextDouble() * 2.0D);
/* 555 */       int j = Mth.floor(this.enderman.getY() + randomSource.nextDouble() * 2.0D);
/* 556 */       int k = Mth.floor(this.enderman.getZ() - 1.0D + randomSource.nextDouble() * 2.0D);
/* 557 */       BlockPos blockPos1 = new BlockPos(i, j, k);
/* 558 */       BlockState blockState1 = level.getBlockState(blockPos1);
/* 559 */       BlockPos blockPos2 = blockPos1.below();
/* 560 */       BlockState blockState2 = level.getBlockState(blockPos2);
/*     */       
/* 562 */       BlockState blockState3 = this.enderman.getCarriedBlock();
/* 563 */       if (blockState3 == null) {
/*     */         return;
/*     */       }
/*     */       
/* 567 */       blockState3 = Block.updateFromNeighbourShapes(blockState3, (LevelAccessor)this.enderman.level(), blockPos1);
/* 568 */       if (canPlaceBlock(level, blockPos1, blockState3, blockState1, blockState2, blockPos2)) {
/* 569 */         level.setBlock(blockPos1, blockState3, 3);
/* 570 */         level.gameEvent((Holder)GameEvent.BLOCK_PLACE, blockPos1, GameEvent.Context.of((Entity)this.enderman, blockState3));
/* 571 */         this.enderman.setCarriedBlock((BlockState)null);
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean canPlaceBlock(Level param1Level, BlockPos param1BlockPos1, BlockState param1BlockState1, BlockState param1BlockState2, BlockState param1BlockState3, BlockPos param1BlockPos2) {
/* 576 */       return (param1BlockState2.isAir() && !param1BlockState3.isAir() && !param1BlockState3.is(Blocks.BEDROCK) && param1BlockState3.isCollisionShapeFullBlock((BlockGetter)param1Level, param1BlockPos2) && param1BlockState1.canSurvive((LevelReader)param1Level, param1BlockPos1) && param1Level
/* 577 */         .getEntities((Entity)this.enderman, AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf((Vec3i)param1BlockPos1))).isEmpty());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EndermanTakeBlockGoal extends Goal {
/*     */     private final EnderMan enderman;
/*     */     
/*     */     public EndermanTakeBlockGoal(EnderMan param1EnderMan) {
/* 585 */       this.enderman = param1EnderMan;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 590 */       if (this.enderman.getCarriedBlock() != null) {
/* 591 */         return false;
/*     */       }
/* 593 */       if (!((Boolean)getServerLevel((Entity)this.enderman).getGameRules().get(GameRules.MOB_GRIEFING)).booleanValue()) {
/* 594 */         return false;
/*     */       }
/* 596 */       return (this.enderman.getRandom().nextInt(reducedTickDelay(20)) == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 601 */       RandomSource randomSource = this.enderman.getRandom();
/* 602 */       Level level = this.enderman.level();
/*     */       
/* 604 */       int i = Mth.floor(this.enderman.getX() - 2.0D + randomSource.nextDouble() * 4.0D);
/* 605 */       int j = Mth.floor(this.enderman.getY() + randomSource.nextDouble() * 3.0D);
/* 606 */       int k = Mth.floor(this.enderman.getZ() - 2.0D + randomSource.nextDouble() * 4.0D);
/* 607 */       BlockPos blockPos = new BlockPos(i, j, k);
/* 608 */       BlockState blockState = level.getBlockState(blockPos);
/*     */       
/* 610 */       Vec3 vec31 = new Vec3(this.enderman.getBlockX() + 0.5D, j + 0.5D, this.enderman.getBlockZ() + 0.5D);
/* 611 */       Vec3 vec32 = new Vec3(i + 0.5D, j + 0.5D, k + 0.5D);
/* 612 */       BlockHitResult blockHitResult = level.clip(new ClipContext(vec31, vec32, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, (Entity)this.enderman));
/* 613 */       boolean bool = blockHitResult.getBlockPos().equals(blockPos);
/*     */       
/* 615 */       if (blockState.is(BlockTags.ENDERMAN_HOLDABLE) && bool) {
/* 616 */         level.removeBlock(blockPos, false);
/* 617 */         level.gameEvent((Holder)GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Context.of((Entity)this.enderman, blockState));
/* 618 */         this.enderman.setCarriedBlock(blockState.getBlock().defaultBlockState());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\EnderMan.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */