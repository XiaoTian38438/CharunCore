/*     */ package net.minecraft.world.entity.animal.squid;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class GlowSquid
/*     */   extends Squid {
/*  26 */   private static final EntityDataAccessor<Integer> DATA_DARK_TICKS_REMAINING = SynchedEntityData.defineId(GlowSquid.class, EntityDataSerializers.INT);
/*     */   private static final int DEFAULT_DARK_TICKS_REMAINING = 0;
/*     */   
/*     */   public GlowSquid(EntityType<? extends GlowSquid> paramEntityType, Level paramLevel) {
/*  30 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ParticleOptions getInkParticle() {
/*  35 */     return (ParticleOptions)ParticleTypes.GLOW_SQUID_INK;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/*  40 */     super.defineSynchedData(paramBuilder);
/*  41 */     paramBuilder.define(DATA_DARK_TICKS_REMAINING, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public AgeableMob getBreedOffspring(ServerLevel paramServerLevel, AgeableMob paramAgeableMob) {
/*  46 */     return (AgeableMob)EntityType.GLOW_SQUID.create((Level)paramServerLevel, EntitySpawnReason.BREEDING);
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getSquirtSound() {
/*  51 */     return SoundEvents.GLOW_SQUID_SQUIRT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getAmbientSound() {
/*  56 */     return SoundEvents.GLOW_SQUID_AMBIENT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getHurtSound(DamageSource paramDamageSource) {
/*  61 */     return SoundEvents.GLOW_SQUID_HURT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected SoundEvent getDeathSound() {
/*  66 */     return SoundEvents.GLOW_SQUID_DEATH;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/*  71 */     super.addAdditionalSaveData(paramValueOutput);
/*  72 */     paramValueOutput.putInt("DarkTicksRemaining", getDarkTicksRemaining());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/*  77 */     super.readAdditionalSaveData(paramValueInput);
/*  78 */     setDarkTicks(paramValueInput.getIntOr("DarkTicksRemaining", 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  83 */     super.aiStep();
/*     */     
/*  85 */     int i = getDarkTicksRemaining();
/*  86 */     if (i > 0) {
/*  87 */       setDarkTicks(i - 1);
/*     */     }
/*     */     
/*  90 */     level().addParticle((ParticleOptions)ParticleTypes.GLOW, getRandomX(0.6D), getRandomY(), getRandomZ(0.6D), 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hurtServer(ServerLevel paramServerLevel, DamageSource paramDamageSource, float paramFloat) {
/*  95 */     boolean bool = super.hurtServer(paramServerLevel, paramDamageSource, paramFloat);
/*  96 */     if (bool) {
/*  97 */       setDarkTicks(100);
/*     */     }
/*     */     
/* 100 */     return bool;
/*     */   }
/*     */   
/*     */   private void setDarkTicks(int paramInt) {
/* 104 */     this.entityData.set(DATA_DARK_TICKS_REMAINING, Integer.valueOf(paramInt));
/*     */   }
/*     */   
/*     */   public int getDarkTicksRemaining() {
/* 108 */     return ((Integer)this.entityData.get(DATA_DARK_TICKS_REMAINING)).intValue();
/*     */   }
/*     */   
/*     */   public static boolean checkGlowSquidSpawnRules(EntityType<? extends LivingEntity> paramEntityType, ServerLevelAccessor paramServerLevelAccessor, EntitySpawnReason paramEntitySpawnReason, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 112 */     return (paramBlockPos.getY() <= paramServerLevelAccessor.getSeaLevel() - 33 && paramServerLevelAccessor.getRawBrightness(paramBlockPos, 0) == 0 && paramServerLevelAccessor.getBlockState(paramBlockPos).is(Blocks.WATER));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\animal\squid\GlowSquid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */