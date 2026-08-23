/*     */ package net.minecraft.world.entity.animal.golem;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Crackiness;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityReference;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.NeutralMob;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.GolemRandomStrollInVillageGoal;
/*     */ import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
/*     */ import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.OfferFlowerGoal;
/*     */ import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.DefendVillageTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class IronGolem
/*     */   extends AbstractGolem
/*     */   implements NeutralMob
/*     */ {
/*  54 */   protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(IronGolem.class, EntityDataSerializers.BYTE);
/*     */   
/*     */   private static final int IRON_INGOT_HEAL_AMOUNT = 25;
/*     */   
/*     */   private static final boolean DEFAULT_PLAYER_CREATED = false;
/*     */   private int attackAnimationTick;
/*     */   private int offerFlowerTick;
/*  61 */   private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
/*     */   private long persistentAngerEndTime;
/*     */   private EntityReference<LivingEntity> persistentAngerTarget;
/*     */   
/*     */   public IronGolem(EntityType<? extends IronGolem> paramEntityType, Level paramLevel) {
/*  66 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  71 */     this.goalSelector.addGoal(1, (Goal)new MeleeAttackGoal(this, 1.0D, true));
/*  72 */     this.goalSelector.addGoal(2, (Goal)new MoveTowardsTargetGoal(this, 0.9D, 32.0F));
/*  73 */     this.goalSelector.addGoal(2, (Goal)new MoveBackToVillageGoal(this, 0.6D, false));
/*  74 */     this.goalSelector.addGoal(4, (Goal)new GolemRandomStrollInVillageGoal(this, 0.6D));
/*  75 */     this.goalSelector.addGoal(5, (Goal)new OfferFlowerGoal(this));
/*  76 */     this.goalSelector.addGoal(7, (Goal)new LookAtPlayerGoal((Mob)this, Player.class, 6.0F));
/*  77 */     this.goalSelector.addGoal(8, (Goal)new RandomLookAroundGoal((Mob)this));
/*     */     
/*  79 */     this.targetSelector.addGoal(1, (Goal)new DefendVillageTargetGoal(this));
/*  80 */     this.targetSelector.addGoal(2, (Goal)new HurtByTargetGoal(this, new Class[0]));
/*  81 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Player.class, 10, true, false, this::isAngryAt));
/*  82 */     this.targetSelector.addGoal(3, (Goal)new NearestAttackableTargetGoal((Mob)this, Mob.class, 5, false, false, (paramLivingEntity, paramServerLevel) -> (paramLivingEntity instanceof net.minecraft.world.entity.monster.Enemy && !(paramLivingEntity instanceof net.minecraft.world.entity.monster.Creeper))));
/*  83 */     this.targetSelector.addGoal(4, (Goal)new ResetUniversalAngerTargetGoal((Mob)this, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  88 */     super.defineSynchedData(paramBuilder);
/*  89 */     paramBuilder.define(DATA_FLAGS_ID, Byte.valueOf((byte)0));
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  93 */     return Mob.createMobAttributes()
/*  94 */       .add(Attributes.MAX_HEALTH, 100.0D)
/*  95 */       .add(Attributes.MOVEMENT_SPEED, 0.25D)
/*  96 */       .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
/*  97 */       .add(Attributes.ATTACK_DAMAGE, 15.0D)
/*  98 */       .add(Attributes.STEP_HEIGHT, 1.0D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int decreaseAirSupply(int paramInt) {
/* 104 */     return paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doPush(Entity paramEntity) {
/* 109 */     if (paramEntity instanceof net.minecraft.world.entity.monster.Enemy && !(paramEntity instanceof net.minecraft.world.entity.monster.Creeper) && 
/* 110 */       getRandom().nextInt(20) == 0) {
/* 111 */       setTarget((LivingEntity)paramEntity);
/*     */     }
/*     */     
/* 114 */     super.doPush(paramEntity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 119 */     super.aiStep();
/*     */     
/* 121 */     if (this.attackAnimationTick > 0) {
/* 122 */       this.attackAnimationTick--;
/*     */     }
/* 124 */     if (this.offerFlowerTick > 0) {
/* 125 */       this.offerFlowerTick--;
/*     */     }
/*     */     
/* 128 */     if (!level().isClientSide()) {
/* 129 */       updatePersistentAnger((ServerLevel)level(), true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canSpawnSprintParticle() {
/* 135 */     return (getDeltaMovement().horizontalDistanceSqr() > 2.500000277905201E-7D && this.random.nextInt(5) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttackType(EntityType<?> paramEntityType) {
/* 140 */     if (isPlayerCreated() && paramEntityType == EntityType.PLAYER) {
/* 141 */       return false;
/*     */     }
/* 143 */     if (paramEntityType == EntityType.CREEPER) {
/* 144 */       return false;
/*     */     }
/* 146 */     return super.canAttackType(paramEntityType);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 151 */     super.addAdditionalSaveData(paramValueOutput);
/* 152 */     paramValueOutput.putBoolean("PlayerCreated", isPlayerCreated());
/* 153 */     addPersistentAngerSaveData(paramValueOutput);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 158 */     super.readAdditionalSaveData(paramValueInput);
/* 159 */     setPlayerCreated(paramValueInput.getBooleanOr("PlayerCreated", false));
/* 160 */     readPersistentAngerSaveData(level(), paramValueInput);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startPersistentAngerTimer() {
/* 165 */     setTimeToRemainAngry(PERSISTENT_ANGER_TIME.sample(this.random));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistentAngerEndTime(long paramLong) {
/* 170 */     this.persistentAngerEndTime = paramLong;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPersistentAngerEndTime() {
/* 175 */     return this.persistentAngerEndTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPersistentAngerTarget(EntityReference<LivingEntity> paramEntityReference) {
/* 180 */     this.persistentAngerTarget = paramEntityReference;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityReference<LivingEntity> getPersistentAngerTarget() {
/* 185 */     return this.persistentAngerTarget;
/*     */   }
/*     */   
/*     */   private float getAttackDamage() {
/* 189 */     return (float)getAttributeValue(Attributes.ATTACK_DAMAGE);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean doHurtTarget(ServerLevel paramServerLevel, Entity paramEntity) {
/* 194 */     this.attackAnimationTick = 10;
/* 195 */     paramServerLevel.broadcastEntityEvent((Entity)this, (byte)4);
/* 196 */     float f1 = getAttackDamage();
/* 197 */     float f2 = ((int)f1 > 0) ? (f1 / 2.0F + this.random.nextInt((int)f1)) : f1;
/* 198 */     DamageSource damageSource = damageSources().mobAttack((LivingEntity)this);
/* 199 */     boolean bool = paramEntity.hurtServer(paramServerLevel, damageSource, f2);
/* 200 */     if (bool) {
/* 201 */       LivingEntity livingEntity = (LivingEntity)paramEntity; double d1 = (paramEntity instanceof LivingEntity) ? livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) : 0.0D;
/* 202 */       double d2 = Math.max(0.0D, 1.0D - d1);
/*     */       
/* 204 */       paramEntity.setDeltaMovement(paramEntity.getDeltaMovement().add(0.0D, 0.4000000059604645D * d2, 0.0D));
/* 205 */       EnchantmentHelper.doPostAttackEffects(paramServerLevel, paramEntity, damageSource);
/*     */     } 
/* 207 */     playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
/* 208 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/* 213 */     Crackiness.Level level = getCrackiness();
/* 214 */     boolean bool = super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/* 215 */     if (bool && getCrackiness() != level) {
/* 216 */       playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
/*     */     }
/* 218 */     return bool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Crackiness.Level getCrackiness() {
/* 225 */     return Crackiness.GOLEM.byFraction(getHealth() / getMaxHealth());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEntityEvent(byte paramByte) {
/* 230 */     if (paramByte == 4) {
/* 231 */       this.attackAnimationTick = 10;
/* 232 */       playSound(SoundEvents.IRON_GOLEM_ATTACK, 1.0F, 1.0F);
/* 233 */     } else if (paramByte == 11) {
/* 234 */       this.offerFlowerTick = 400;
/* 235 */     } else if (paramByte == 34) {
/* 236 */       this.offerFlowerTick = 0;
/*     */     } else {
/* 238 */       super.handleEntityEvent(paramByte);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getAttackAnimationTick() {
/* 243 */     return this.attackAnimationTick;
/*     */   }
/*     */   
/*     */   public void offerFlower(boolean paramBoolean) {
/* 247 */     if (paramBoolean) {
/* 248 */       this.offerFlowerTick = 400;
/* 249 */       level().broadcastEntityEvent((Entity)this, (byte)11);
/*     */     } else {
/* 251 */       this.offerFlowerTick = 0;
/* 252 */       level().broadcastEntityEvent((Entity)this, (byte)34);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 258 */     return SoundEvents.IRON_GOLEM_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 263 */     return SoundEvents.IRON_GOLEM_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 268 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 269 */     if (!itemStack.is(Items.IRON_INGOT)) {
/* 270 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 273 */     float f1 = getHealth();
/* 274 */     heal(25.0F);
/* 275 */     if (getHealth() == f1) {
/* 276 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 279 */     float f2 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
/* 280 */     playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, f2);
/*     */     
/* 282 */     itemStack.consume(1, (LivingEntity)paramPlayer);
/* 283 */     return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos paramBlockPos, BlockState paramBlockState) {
/* 288 */     playSound(SoundEvents.IRON_GOLEM_STEP, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public int getOfferFlowerTick() {
/* 292 */     return this.offerFlowerTick;
/*     */   }
/*     */   
/*     */   public boolean isPlayerCreated() {
/* 296 */     return ((((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue() & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public void setPlayerCreated(boolean paramBoolean) {
/* 300 */     byte b = ((Byte)this.entityData.get(DATA_FLAGS_ID)).byteValue();
/* 301 */     if (paramBoolean) {
/* 302 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(b | 0x1)));
/*     */     } else {
/* 304 */       this.entityData.set(DATA_FLAGS_ID, Byte.valueOf((byte)(b & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void die(DamageSource paramDamageSource) {
/* 311 */     super.die(paramDamageSource);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkSpawnObstruction(LevelReader paramLevelReader) {
/* 317 */     BlockPos blockPos1 = blockPosition();
/* 318 */     BlockPos blockPos2 = blockPos1.below();
/* 319 */     BlockState blockState = paramLevelReader.getBlockState(blockPos2);
/* 320 */     if (blockState.entityCanStandOn((BlockGetter)paramLevelReader, blockPos2, (Entity)this)) {
/* 321 */       for (byte b = 1; b < 3; b++) {
/* 322 */         BlockPos blockPos = blockPos1.above(b);
/* 323 */         BlockState blockState1 = paramLevelReader.getBlockState(blockPos);
/* 324 */         if (!NaturalSpawner.isValidEmptySpawnBlock((BlockGetter)paramLevelReader, blockPos, blockState1, blockState1.getFluidState(), EntityType.IRON_GOLEM)) {
/* 325 */           return false;
/*     */         }
/*     */       } 
/* 328 */       return (NaturalSpawner.isValidEmptySpawnBlock((BlockGetter)paramLevelReader, blockPos1, paramLevelReader.getBlockState(blockPos1), Fluids.EMPTY.defaultFluidState(), EntityType.IRON_GOLEM) && paramLevelReader
/* 329 */         .isUnobstructed((Entity)this));
/*     */     } 
/* 331 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getLeashOffset() {
/* 336 */     return new Vec3(0.0D, (0.875F * getEyeHeight()), (getBbWidth() * 0.4F));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\golem\IronGolem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */