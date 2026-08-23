/*     */ package net.minecraft.world.entity.monster;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
/*     */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class MagmaCube extends Slime {
/*     */   public MagmaCube(EntityType<? extends MagmaCube> paramEntityType, Level paramLevel) {
/*  24 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   public static AttributeSupplier.Builder createAttributes() {
/*  28 */     return Monster.createMonsterAttributes()
/*  29 */       .add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
/*     */   }
/*     */   
/*     */   public static boolean checkMagmaCubeSpawnRules(EntityType<MagmaCube> paramEntityType, LevelAccessor paramLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  33 */     return (paramLevelAccessor.getDifficulty() != Difficulty.PEACEFUL);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSize(int paramInt, boolean paramBoolean) {
/*  38 */     super.setSize(paramInt, paramBoolean);
/*  39 */     getAttribute(Attributes.ARMOR).setBaseValue((paramInt * 3));
/*     */   }
/*     */ 
/*     */   
/*     */   public float getLightLevelDependentMagicValue() {
/*  44 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ParticleOptions getParticleType() {
/*  49 */     return (ParticleOptions)ParticleTypes.FLAME;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnFire() {
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getJumpDelay() {
/*  59 */     return super.getJumpDelay() * 4;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void decreaseSquish() {
/*  64 */     this.targetSquish *= 0.9F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void jumpFromGround() {
/*  69 */     Vec3 vec3 = getDeltaMovement();
/*  70 */     float f = getSize() * 0.1F;
/*  71 */     setDeltaMovement(vec3.x, (getJumpPower() + f), vec3.z);
/*  72 */     this.needsSync = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void jumpInLiquid(TagKey<Fluid> paramTagKey) {
/*  77 */     if (paramTagKey == FluidTags.LAVA) {
/*  78 */       Vec3 vec3 = getDeltaMovement();
/*  79 */       setDeltaMovement(vec3.x, (0.22F + getSize() * 0.05F), vec3.z);
/*  80 */       this.needsSync = true;
/*     */     } else {
/*  82 */       super.jumpInLiquid(paramTagKey);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isDealsDamage() {
/*  88 */     return isEffectiveAi();
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getAttackDamage() {
/*  93 */     return super.getAttackDamage() + 2.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/*  98 */     if (isTiny()) {
/*  99 */       return SoundEvents.MAGMA_CUBE_HURT_SMALL;
/*     */     }
/* 101 */     return SoundEvents.MAGMA_CUBE_HURT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/* 107 */     if (isTiny()) {
/* 108 */       return SoundEvents.MAGMA_CUBE_DEATH_SMALL;
/*     */     }
/* 110 */     return SoundEvents.MAGMA_CUBE_DEATH;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getSquishSound() {
/* 116 */     if (isTiny()) {
/* 117 */       return SoundEvents.MAGMA_CUBE_SQUISH_SMALL;
/*     */     }
/* 119 */     return SoundEvents.MAGMA_CUBE_SQUISH;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected SoundEvent getJumpSound() {
/* 125 */     return SoundEvents.MAGMA_CUBE_JUMP;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\monster\MagmaCube.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */