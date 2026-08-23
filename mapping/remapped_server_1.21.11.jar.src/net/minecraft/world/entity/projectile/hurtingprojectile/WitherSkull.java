/*     */ package net.minecraft.world.entity.projectile.hurtingprojectile;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.syncher.EntityDataAccessor;
/*     */ import net.minecraft.network.syncher.EntityDataSerializers;
/*     */ import net.minecraft.network.syncher.SynchedEntityData;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ import net.minecraft.world.effect.MobEffects;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.boss.wither.WitherBoss;
/*     */ import net.minecraft.world.item.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Explosion;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.EntityHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class WitherSkull
/*     */   extends AbstractHurtingProjectile {
/*  30 */   private static final EntityDataAccessor<Boolean> DATA_DANGEROUS = SynchedEntityData.defineId(WitherSkull.class, EntityDataSerializers.BOOLEAN);
/*     */   private static final boolean DEFAULT_DANGEROUS = false;
/*     */   
/*     */   public WitherSkull(EntityType<? extends WitherSkull> paramEntityType, Level paramLevel) {
/*  34 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   public WitherSkull(Level paramLevel, LivingEntity paramLivingEntity, Vec3 paramVec3) {
/*  38 */     super(EntityType.WITHER_SKULL, paramLivingEntity, paramVec3, paramLevel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getInertia() {
/*  43 */     return isDangerous() ? 0.73F : super.getInertia();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnFire() {
/*  48 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBlockExplosionResistance(Explosion paramExplosion, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState, float paramFloat) {
/*  53 */     if (isDangerous() && WitherBoss.canDestroy(paramBlockState)) {
/*  54 */       return Math.min(0.8F, paramFloat);
/*     */     }
/*     */     
/*  57 */     return paramFloat;
/*     */   }
/*     */   protected void onHitEntity(EntityHitResult paramEntityHitResult) {
/*     */     ServerLevel serverLevel;
/*     */     boolean bool;
/*  62 */     super.onHitEntity(paramEntityHitResult);
/*     */     
/*  64 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */     else
/*     */     { return; }
/*  67 */      Entity entity1 = paramEntityHitResult.getEntity();
/*  68 */     Entity entity2 = getOwner();
/*     */     
/*  70 */     if (entity2 instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity2;
/*  71 */       DamageSource damageSource = damageSources().witherSkull(this, (Entity)livingEntity);
/*  72 */       bool = entity1.hurtServer(serverLevel, damageSource, 8.0F);
/*  73 */       if (bool) {
/*  74 */         if (entity1.isAlive()) {
/*  75 */           EnchantmentHelper.doPostAttackEffects(serverLevel, entity1, damageSource);
/*     */         } else {
/*  77 */           livingEntity.heal(5.0F);
/*     */         } 
/*     */       } }
/*     */     else
/*  81 */     { bool = entity1.hurtServer(serverLevel, damageSources().magic(), 5.0F); }
/*     */     
/*  83 */     if (bool && entity1 instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity1;
/*  84 */       byte b = 0;
/*  85 */       if (level().getDifficulty() == Difficulty.NORMAL) {
/*  86 */         b = 10;
/*  87 */       } else if (level().getDifficulty() == Difficulty.HARD) {
/*  88 */         b = 40;
/*     */       } 
/*  90 */       if (b > 0) {
/*  91 */         livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * b, 1), getEffectSource());
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHit(HitResult paramHitResult) {
/*  98 */     super.onHit(paramHitResult);
/*  99 */     if (!level().isClientSide()) {
/* 100 */       level().explode((Entity)this, getX(), getY(), getZ(), 1.0F, false, Level.ExplosionInteraction.MOB);
/* 101 */       discard();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void defineSynchedData(SynchedEntityData.Builder paramBuilder) {
/* 107 */     paramBuilder.define(DATA_DANGEROUS, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean isDangerous() {
/* 111 */     return ((Boolean)this.entityData.get(DATA_DANGEROUS)).booleanValue();
/*     */   }
/*     */   
/*     */   public void setDangerous(boolean paramBoolean) {
/* 115 */     this.entityData.set(DATA_DANGEROUS, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldBurn() {
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput paramValueOutput) {
/* 125 */     super.addAdditionalSaveData(paramValueOutput);
/* 126 */     paramValueOutput.putBoolean("dangerous", isDangerous());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput paramValueInput) {
/* 131 */     super.readAdditionalSaveData(paramValueInput);
/* 132 */     setDangerous(paramValueInput.getBooleanOr("dangerous", false));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\hurtingprojectile\WitherSkull.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */