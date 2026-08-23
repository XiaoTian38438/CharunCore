/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.damagesource.DamageSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.SculkCatalystBlock;
/*     */ import net.minecraft.world.level.block.SculkSpreader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.phys.Vec3;
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
/*     */ 
/*     */ 
/*     */ public class CatalystListener
/*     */   implements GameEventListener
/*     */ {
/*     */   public static final int PULSE_TICKS = 8;
/*     */   final SculkSpreader sculkSpreader;
/*     */   private final BlockState blockState;
/*     */   private final PositionSource positionSource;
/*     */   
/*     */   public CatalystListener(BlockState paramBlockState, PositionSource paramPositionSource) {
/*  67 */     this.blockState = paramBlockState;
/*  68 */     this.positionSource = paramPositionSource;
/*  69 */     this.sculkSpreader = SculkSpreader.createLevelSpreader();
/*     */   }
/*     */ 
/*     */   
/*     */   public PositionSource getListenerSource() {
/*  74 */     return this.positionSource;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getListenerRadius() {
/*  79 */     return 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public GameEventListener.DeliveryMode getDeliveryMode() {
/*  84 */     return GameEventListener.DeliveryMode.BY_DISTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleGameEvent(ServerLevel paramServerLevel, Holder<GameEvent> paramHolder, GameEvent.Context paramContext, Vec3 paramVec3) {
/*  89 */     if (paramHolder.is((Holder)GameEvent.ENTITY_DIE)) { Entity entity = paramContext.sourceEntity(); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/*  90 */         if (!livingEntity.wasExperienceConsumed()) {
/*  91 */           DamageSource damageSource = livingEntity.getLastDamageSource();
/*  92 */           int i = livingEntity.getExperienceReward(paramServerLevel, (Entity)Optionull.map(damageSource, DamageSource::getEntity));
/*  93 */           if (livingEntity.shouldDropExperience() && i > 0) {
/*  94 */             this.sculkSpreader.addCursors(BlockPos.containing((Position)paramVec3.relative(Direction.UP, 0.5D)), i);
/*  95 */             tryAwardItSpreadsAdvancement((Level)paramServerLevel, livingEntity);
/*     */           } 
/*  97 */           livingEntity.skipDropExperience();
/*  98 */           this.positionSource.getPosition((Level)paramServerLevel).ifPresent(paramVec3 -> bloom(paramServerLevel, BlockPos.containing((Position)paramVec3), this.blockState, paramServerLevel.getRandom()));
/*     */         } 
/* 100 */         return true; }
/*     */        }
/*     */     
/* 103 */     return false;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public SculkSpreader getSculkSpreader() {
/* 108 */     return this.sculkSpreader;
/*     */   }
/*     */   
/*     */   private void bloom(ServerLevel paramServerLevel, BlockPos paramBlockPos, BlockState paramBlockState, RandomSource paramRandomSource) {
/* 112 */     paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)SculkCatalystBlock.PULSE, Boolean.valueOf(true)), 3);
/* 113 */     paramServerLevel.scheduleTick(paramBlockPos, paramBlockState.getBlock(), 8);
/*     */     
/* 115 */     paramServerLevel.sendParticles((ParticleOptions)ParticleTypes.SCULK_SOUL, paramBlockPos.getX() + 0.5D, paramBlockPos.getY() + 1.15D, paramBlockPos.getZ() + 0.5D, 2, 0.2D, 0.0D, 0.2D, 0.0D);
/*     */     
/* 117 */     paramServerLevel.playSound(null, paramBlockPos, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 2.0F, 0.6F + paramRandomSource.nextFloat() * 0.4F);
/*     */   }
/*     */   
/*     */   private void tryAwardItSpreadsAdvancement(Level paramLevel, LivingEntity paramLivingEntity) {
/* 121 */     LivingEntity livingEntity = paramLivingEntity.getLastHurtByMob();
/* 122 */     if (livingEntity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
/* 123 */       DamageSource damageSource = (paramLivingEntity.getLastDamageSource() == null) ? paramLevel.damageSources().playerAttack((Player)serverPlayer) : paramLivingEntity.getLastDamageSource();
/* 124 */       CriteriaTriggers.KILL_MOB_NEAR_SCULK_CATALYST.trigger(serverPlayer, (Entity)paramLivingEntity, damageSource); }
/*     */   
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\SculkCatalystBlockEntity$CatalystListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */