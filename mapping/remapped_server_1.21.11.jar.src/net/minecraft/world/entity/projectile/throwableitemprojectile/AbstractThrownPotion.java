/*     */ package net.minecraft.world.entity.projectile.throwableitemprojectile;
/*     */ import it.unimi.dsi.fastutil.doubles.DoubleDoubleImmutablePair;
/*     */ import java.util.List;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.alchemy.PotionContents;
/*     */ import net.minecraft.world.item.alchemy.Potions;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.AbstractCandleBlock;
/*     */ import net.minecraft.world.level.block.CampfireBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ 
/*     */ public abstract class AbstractThrownPotion extends ThrowableItemProjectile {
/*     */   public static final double SPLASH_RANGE = 4.0D;
/*     */   
/*     */   static {
/*  31 */     WATER_SENSITIVE_OR_ON_FIRE = (paramLivingEntity -> 
/*  32 */       (paramLivingEntity.isSensitiveToWater() || paramLivingEntity.isOnFire()));
/*     */   } protected static final double SPLASH_RANGE_SQ = 16.0D; public static final Predicate<LivingEntity> WATER_SENSITIVE_OR_ON_FIRE;
/*     */   public AbstractThrownPotion(EntityType<? extends AbstractThrownPotion> paramEntityType, Level paramLevel) {
/*  35 */     super((EntityType)paramEntityType, paramLevel);
/*     */   }
/*     */   
/*     */   public AbstractThrownPotion(EntityType<? extends AbstractThrownPotion> paramEntityType, Level paramLevel, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/*  39 */     super((EntityType)paramEntityType, paramLivingEntity, paramLevel, paramItemStack);
/*     */   }
/*     */   
/*     */   public AbstractThrownPotion(EntityType<? extends AbstractThrownPotion> paramEntityType, Level paramLevel, double paramDouble1, double paramDouble2, double paramDouble3, ItemStack paramItemStack) {
/*  43 */     super((EntityType)paramEntityType, paramDouble1, paramDouble2, paramDouble3, paramLevel, paramItemStack);
/*     */   }
/*     */ 
/*     */   
/*     */   protected double getDefaultGravity() {
/*  48 */     return 0.05D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onHitBlock(BlockHitResult paramBlockHitResult) {
/*  53 */     super.onHitBlock(paramBlockHitResult);
/*  54 */     if (level().isClientSide()) {
/*     */       return;
/*     */     }
/*  57 */     ItemStack itemStack = getItem();
/*  58 */     Direction direction = paramBlockHitResult.getDirection();
/*  59 */     BlockPos blockPos1 = paramBlockHitResult.getBlockPos();
/*  60 */     BlockPos blockPos2 = blockPos1.relative(direction);
/*     */     
/*  62 */     PotionContents potionContents = (PotionContents)itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/*  63 */     if (potionContents.is(Potions.WATER)) {
/*  64 */       dowseFire(blockPos2);
/*  65 */       dowseFire(blockPos2.relative(direction.getOpposite()));
/*  66 */       for (Direction direction1 : Direction.Plane.HORIZONTAL) {
/*  67 */         dowseFire(blockPos2.relative(direction1));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void onHit(HitResult paramHitResult) {
/*     */     ServerLevel serverLevel;
/*  74 */     super.onHit(paramHitResult);
/*  75 */     Level level = level(); if (level instanceof ServerLevel) { serverLevel = (ServerLevel)level; }
/*     */     else
/*     */     { return; }
/*  78 */      ItemStack itemStack = getItem();
/*     */     
/*  80 */     PotionContents potionContents = (PotionContents)itemStack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
/*     */     
/*  82 */     if (potionContents.is(Potions.WATER)) {
/*  83 */       onHitAsWater(serverLevel);
/*  84 */     } else if (potionContents.hasEffects()) {
/*  85 */       onHitAsPotion(serverLevel, itemStack, paramHitResult);
/*     */     } 
/*  87 */     char c = (potionContents.potion().isPresent() && ((Potion)((Holder)potionContents.potion().get()).value()).hasInstantEffects()) ? 'ߗ' : 'ߒ';
/*  88 */     serverLevel.levelEvent(c, blockPosition(), potionContents.getColor());
/*     */     
/*  90 */     discard();
/*     */   }
/*     */   
/*     */   private void onHitAsWater(ServerLevel paramServerLevel) {
/*  94 */     AABB aABB = getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
/*  95 */     List list1 = level().getEntitiesOfClass(LivingEntity.class, aABB, WATER_SENSITIVE_OR_ON_FIRE);
/*  96 */     for (LivingEntity livingEntity : list1) {
/*  97 */       double d = distanceToSqr((Entity)livingEntity);
/*  98 */       if (d < 16.0D) {
/*  99 */         if (livingEntity.isSensitiveToWater()) {
/* 100 */           livingEntity.hurtServer(paramServerLevel, damageSources().indirectMagic((Entity)this, getOwner()), 1.0F);
/*     */         }
/* 102 */         if (livingEntity.isOnFire() && livingEntity.isAlive()) {
/* 103 */           livingEntity.extinguishFire();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 108 */     List list2 = level().getEntitiesOfClass(Axolotl.class, aABB);
/* 109 */     for (Axolotl axolotl : list2) {
/* 110 */       axolotl.rehydrate();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void dowseFire(BlockPos paramBlockPos) {
/* 117 */     BlockState blockState = level().getBlockState(paramBlockPos);
/* 118 */     if (blockState.is(BlockTags.FIRE)) {
/* 119 */       level().destroyBlock(paramBlockPos, false, (Entity)this);
/* 120 */     } else if (AbstractCandleBlock.isLit(blockState)) {
/* 121 */       AbstractCandleBlock.extinguish(null, blockState, (LevelAccessor)level(), paramBlockPos);
/* 122 */     } else if (CampfireBlock.isLitCampfire(blockState)) {
/* 123 */       level().levelEvent(null, 1009, paramBlockPos, 0);
/* 124 */       CampfireBlock.dowse(getOwner(), (LevelAccessor)level(), paramBlockPos, blockState);
/* 125 */       level().setBlockAndUpdate(paramBlockPos, (BlockState)blockState.setValue((Property)CampfireBlock.LIT, Boolean.valueOf(false)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public DoubleDoubleImmutablePair calculateHorizontalHurtKnockbackDirection(LivingEntity paramLivingEntity, DamageSource paramDamageSource) {
/* 131 */     double d1 = (paramLivingEntity.position()).x - (position()).x;
/* 132 */     double d2 = (paramLivingEntity.position()).z - (position()).z;
/* 133 */     return DoubleDoubleImmutablePair.of(d1, d2);
/*     */   }
/*     */   
/*     */   protected abstract void onHitAsPotion(ServerLevel paramServerLevel, ItemStack paramItemStack, HitResult paramHitResult);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\entity\projectile\throwableitemprojectile\AbstractThrownPotion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */