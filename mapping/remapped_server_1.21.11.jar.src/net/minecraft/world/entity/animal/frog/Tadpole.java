/*     */ package net.minecraft.world.entity.animal.frog;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.Collection;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.ItemTags;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.ConversionParams;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.Brain;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.ai.control.LookControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
/*     */ import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*     */ import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
/*     */ import net.minecraft.world.entity.ai.sensing.Sensor;
/*     */ import net.minecraft.world.entity.ai.sensing.SensorType;
/*     */ import net.minecraft.world.entity.animal.Bucketable;
/*     */ import net.minecraft.world.entity.animal.fish.AbstractFish;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.CustomData;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class Tadpole extends AbstractFish {
/*     */   @VisibleForTesting
/*  49 */   public static int ticksToBeFrog = Math.abs(-24000); private static final int DEFAULT_AGE = 0;
/*     */   public static final float HITBOX_WIDTH = 0.4F;
/*     */   public static final float HITBOX_HEIGHT = 0.3F;
/*  52 */   private int age = 0;
/*     */   
/*  54 */   protected static final ImmutableList<SensorType<? extends Sensor<? super Tadpole>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.HURT_BY, SensorType.FROG_TEMPTATIONS);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.PATH, MemoryModuleType.NEAREST_VISIBLE_ADULT, MemoryModuleType.TEMPTATION_COOLDOWN_TICKS, MemoryModuleType.IS_TEMPTED, MemoryModuleType.TEMPTING_PLAYER, MemoryModuleType.BREED_TARGET, MemoryModuleType.IS_PANICKING);
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
/*     */   public Tadpole(EntityType<? extends AbstractFish> paramEntityType, Level paramLevel) {
/*  76 */     super(paramEntityType, paramLevel);
/*     */     
/*  78 */     this.moveControl = (MoveControl)new SmoothSwimmingMoveControl((Mob)this, 85, 10, 0.02F, 0.1F, true);
/*  79 */     this.lookControl = (LookControl)new SmoothSwimmingLookControl((Mob)this, 10);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigation createNavigation(Level paramLevel) {
/*  84 */     return (PathNavigation)new WaterBoundPathNavigation((Mob)this, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain.Provider<Tadpole> brainProvider() {
/*  89 */     return Brain.provider((Collection)MEMORY_TYPES, (Collection)SENSOR_TYPES);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Brain<?> makeBrain(Dynamic<?> paramDynamic) {
/*  94 */     return TadpoleAi.makeBrain(brainProvider().makeBrain(paramDynamic));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Brain<Tadpole> getBrain() {
/* 100 */     return super.getBrain();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getFlopSound() {
/* 105 */     return SoundEvents.TADPOLE_FLOP;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void customServerAiStep(ServerLevel paramServerLevel) {
/* 110 */     ProfilerFiller profilerFiller = Profiler.get();
/* 111 */     profilerFiller.push("tadpoleBrain");
/* 112 */     getBrain().tick(paramServerLevel, (LivingEntity)this);
/* 113 */     profilerFiller.pop();
/*     */     
/* 115 */     profilerFiller.push("tadpoleActivityUpdate");
/* 116 */     TadpoleAi.updateActivity(this);
/* 117 */     profilerFiller.pop();
/*     */     
/* 119 */     super.customServerAiStep(paramServerLevel);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/* 123 */     return Animal.createAnimalAttributes()
/* 124 */       .add(Attributes.MOVEMENT_SPEED, 1.0D)
/* 125 */       .add(Attributes.MAX_HEALTH, 6.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 130 */     super.aiStep();
/*     */     
/* 132 */     if (!level().isClientSide()) {
/* 133 */       setAge(this.age + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 139 */     super.addAdditionalSaveData(paramValueOutput);
/* 140 */     paramValueOutput.putInt("Age", this.age);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 145 */     super.readAdditionalSaveData(paramValueInput);
/* 146 */     setAge(paramValueInput.getIntOr("Age", 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/* 151 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/* 156 */     return SoundEvents.TADPOLE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 161 */     return SoundEvents.TADPOLE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 166 */     ItemStack itemStack = paramPlayer.getItemInHand(paramInteractionHand);
/* 167 */     if (isFood(itemStack)) {
/* 168 */       feed(paramPlayer, itemStack);
/* 169 */       return (InteractionResult)InteractionResult.SUCCESS;
/*     */     } 
/* 171 */     return Bucketable.bucketMobPickup(paramPlayer, paramInteractionHand, (LivingEntity)this).orElse(super.mobInteract(paramPlayer, paramInteractionHand));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean fromBucket() {
/* 177 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFromBucket(boolean paramBoolean) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveToBucketTag(ItemStack paramItemStack) {
/* 187 */     Bucketable.saveDefaultDataToBucketTag((Mob)this, paramItemStack);
/*     */     
/* 189 */     CustomData.update(DataComponents.BUCKET_ENTITY_DATA, paramItemStack, paramCompoundTag -> paramCompoundTag.putInt("Age", getAge()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadFromBucketTag(CompoundTag paramCompoundTag) {
/* 194 */     Bucketable.loadDefaultDataFromBucketTag((Mob)this, paramCompoundTag);
/* 195 */     paramCompoundTag.getInt("Age").ifPresent(this::setAge);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getBucketItemStack() {
/* 200 */     return new ItemStack((ItemLike)Items.TADPOLE_BUCKET);
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundEvent getPickupSound() {
/* 205 */     return SoundEvents.BUCKET_FILL_TADPOLE;
/*     */   }
/*     */   
/*     */   private boolean isFood(ItemStack paramItemStack) {
/* 209 */     return paramItemStack.is(ItemTags.FROG_FOOD);
/*     */   }
/*     */   
/*     */   private void feed(Player paramPlayer, ItemStack paramItemStack) {
/* 213 */     usePlayerItem(paramPlayer, paramItemStack);
/* 214 */     ageUp(AgeableMob.getSpeedUpSecondsWhenFeeding(getTicksLeftUntilAdult()));
/* 215 */     level().addParticle((ParticleOptions)ParticleTypes.HAPPY_VILLAGER, getRandomX(1.0D), getRandomY() + 0.5D, getRandomZ(1.0D), 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */   
/*     */   private void usePlayerItem(Player paramPlayer, ItemStack paramItemStack) {
/* 219 */     paramItemStack.consume(1, (LivingEntity)paramPlayer);
/*     */   }
/*     */   
/*     */   private int getAge() {
/* 223 */     return this.age;
/*     */   }
/*     */   
/*     */   private void ageUp(int paramInt) {
/* 227 */     setAge(this.age + paramInt * 20);
/*     */   }
/*     */   
/*     */   private void setAge(int paramInt) {
/* 231 */     this.age = paramInt;
/*     */     
/* 233 */     if (this.age >= ticksToBeFrog) {
/* 234 */       ageUp();
/*     */     }
/*     */   }
/*     */   
/*     */   private void ageUp() {
/* 239 */     Level level = level(); if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 240 */       convertTo(EntityType.FROG, ConversionParams.single((Mob)this, false, false), paramFrog -> {
/*     */             paramFrog.finalizeSpawn((ServerLevelAccessor)paramServerLevel, paramServerLevel.getCurrentDifficultyAt(paramFrog.blockPosition()), EntitySpawnReason.CONVERSION, (SpawnGroupData)null);
/*     */             paramFrog.setPersistenceRequired();
/*     */             paramFrog.fudgePositionAfterSizeChange(getDimensions(getPose()));
/*     */             playSound(SoundEvents.TADPOLE_GROW_UP, 0.15F, 1.0F);
/*     */           }); }
/*     */   
/*     */   }
/*     */   
/*     */   private int getTicksLeftUntilAdult() {
/* 250 */     return Math.max(0, ticksToBeFrog - this.age);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldDropExperience() {
/* 255 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\frog\Tadpole.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */