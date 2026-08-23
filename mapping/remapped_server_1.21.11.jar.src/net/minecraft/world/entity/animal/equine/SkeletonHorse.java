/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntityAttachment;
/*     */ import net.minecraft.world.entity.EntityAttachments;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ import net.minecraft.world.entity.Pose;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.entity.animal.Animal;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.BlockAndTintGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class SkeletonHorse extends AbstractHorse {
/*  32 */   private final SkeletonTrapGoal skeletonTrapGoal = new SkeletonTrapGoal(this);
/*     */   
/*     */   private static final int TRAP_MAX_LIFE = 18000;
/*     */   private static final boolean DEFAULT_IS_TRAP = false;
/*     */   private static final int DEFAULT_TRAP_TIME = 0;
/*  37 */   private static final EntityDimensions BABY_DIMENSIONS = EntityType.SKELETON_HORSE.getDimensions()
/*  38 */     .withAttachments(EntityAttachments.builder()
/*  39 */       .attach(EntityAttachment.PASSENGER, 0.0F, EntityType.SKELETON_HORSE.getHeight() - 0.03125F, 0.0F))
/*     */     
/*  41 */     .scale(0.5F);
/*     */   
/*     */   private boolean isTrap = false;
/*  44 */   private int trapTime = 0;
/*     */   
/*     */   public SkeletonHorse(EntityType<? extends SkeletonHorse> paramEntityType, Level paramLevel) {
/*  47 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  51 */     return createBaseHorseAttributes()
/*  52 */       .add(Attributes.MAX_HEALTH, 15.0D)
/*  53 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */   
/*     */   public static boolean checkSkeletonHorseSpawnRules(EntityType<? extends Animal> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  57 */     if (EntitySpawnReason.isSpawner(paramEntitySpawnReason)) {
/*  58 */       return (EntitySpawnReason.ignoresLightRequirements(paramEntitySpawnReason) || isBrightEnoughToSpawn((BlockAndTintGetter)paramLevelAccessor, paramBlockPos));
/*     */     }
/*  60 */     return Animal.checkAnimalSpawnRules(paramEntityType, paramLevelAccessor, paramEntitySpawnReason, paramBlockPos, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomizeAttributes(RandomSource paramRandomSource) {
/*  65 */     Objects.requireNonNull(paramRandomSource); getAttribute(Attributes.JUMP_STRENGTH).setBaseValue(generateJumpStrength(paramRandomSource::nextDouble));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addBehaviourGoals() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  75 */     if (isEyeInFluid(FluidTags.WATER)) {
/*  76 */       return SoundEvents.SKELETON_HORSE_AMBIENT_WATER;
/*     */     }
/*  78 */     return SoundEvents.SKELETON_HORSE_AMBIENT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  84 */     return SoundEvents.SKELETON_HORSE_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/*  89 */     return SoundEvents.SKELETON_HORSE_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSwimSound() {
/*  94 */     if (onGround()) {
/*  95 */       if (isVehicle()) {
/*  96 */         this.gallopSoundCounter++;
/*  97 */         if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0)
/*  98 */           return SoundEvents.SKELETON_HORSE_GALLOP_WATER; 
/*  99 */         if (this.gallopSoundCounter <= 5) {
/* 100 */           return SoundEvents.SKELETON_HORSE_STEP_WATER;
/*     */         }
/*     */       } else {
/* 103 */         return SoundEvents.SKELETON_HORSE_STEP_WATER;
/*     */       } 
/*     */     }
/* 106 */     return SoundEvents.SKELETON_HORSE_SWIM;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playSwimSound(float paramFloat) {
/* 111 */     if (onGround()) {
/* 112 */       super.playSwimSound(0.3F);
/*     */     } else {
/* 114 */       super.playSwimSound(Math.min(0.1F, paramFloat * 25.0F));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playJumpSound() {
/* 120 */     if (isInWater()) {
/* 121 */       playSound(SoundEvents.SKELETON_HORSE_JUMP_WATER, 0.4F, 1.0F);
/*     */     } else {
/* 123 */       super.playJumpSound();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityDimensions getDefaultDimensions(Pose paramPose) {
/* 129 */     return isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(paramPose);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/* 134 */     super.aiStep();
/*     */     
/* 136 */     if (isTrap() && this.trapTime++ >= 18000) {
/* 137 */       discard();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 143 */     super.addAdditionalSaveData(paramValueOutput);
/*     */     
/* 145 */     paramValueOutput.putBoolean("SkeletonTrap", isTrap());
/* 146 */     paramValueOutput.putInt("SkeletonTrapTime", this.trapTime);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 151 */     super.readAdditionalSaveData(paramValueInput);
/*     */     
/* 153 */     setTrap(paramValueInput.getBooleanOr("SkeletonTrap", false));
/* 154 */     this.trapTime = paramValueInput.getIntOr("SkeletonTrapTime", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getWaterSlowDown() {
/* 159 */     return 0.96F;
/*     */   }
/*     */   
/*     */   public boolean isTrap() {
/* 163 */     return this.isTrap;
/*     */   }
/*     */   
/*     */   public void setTrap(boolean paramBoolean) {
/* 167 */     if (paramBoolean == this.isTrap) {
/*     */       return;
/*     */     }
/*     */     
/* 171 */     this.isTrap = paramBoolean;
/* 172 */     if (paramBoolean) {
/* 173 */       this.goalSelector.addGoal(1, this.skeletonTrapGoal);
/*     */     } else {
/* 175 */       this.goalSelector.removeGoal(this.skeletonTrapGoal);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel paramServerLevel, AgeableMob paramAgeableMob) {
/* 181 */     return (AgeableMob)EntityType.SKELETON_HORSE.create((Level)paramServerLevel, EntitySpawnReason.BREEDING);
/*     */   }
/*     */ 
/*     */   
/*     */   public InteractionResult mobInteract(Player paramPlayer, InteractionHand paramInteractionHand) {
/* 186 */     if (!isTamed()) {
/* 187 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/* 189 */     return super.mobInteract(paramPlayer, paramInteractionHand);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUseSlot(EquipmentSlot paramEquipmentSlot) {
/* 194 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\equine\SkeletonHorse.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */