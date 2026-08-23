/*     */ package net.minecraft.world.level.block.entity;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
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
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.block.SculkCatalystBlock;
/*     */ import net.minecraft.world.level.block.SculkSpreader;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gameevent.BlockPositionSource;
/*     */ import net.minecraft.world.level.gameevent.GameEvent;
/*     */ import net.minecraft.world.level.gameevent.GameEventListener;
/*     */ import net.minecraft.world.level.gameevent.PositionSource;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class SculkCatalystBlockEntity extends BlockEntity implements GameEventListener.Provider<SculkCatalystBlockEntity.CatalystListener> {
/*     */   public SculkCatalystBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState) {
/*  34 */     super(BlockEntityType.SCULK_CATALYST, paramBlockPos, paramBlockState);
/*  35 */     this.catalystListener = new CatalystListener(paramBlockState, (PositionSource)new BlockPositionSource(paramBlockPos));
/*     */   }
/*     */   private final CatalystListener catalystListener;
/*     */   public static void serverTick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, SculkCatalystBlockEntity paramSculkCatalystBlockEntity) {
/*  39 */     paramSculkCatalystBlockEntity.catalystListener.getSculkSpreader().updateCursors((LevelAccessor)paramLevel, paramBlockPos, paramLevel.getRandom(), true);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void loadAdditional(ValueInput paramValueInput) {
/*  44 */     super.loadAdditional(paramValueInput);
/*     */     
/*  46 */     this.catalystListener.sculkSpreader.load(paramValueInput);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveAdditional(ValueOutput paramValueOutput) {
/*  51 */     this.catalystListener.sculkSpreader.save(paramValueOutput);
/*  52 */     super.saveAdditional(paramValueOutput);
/*     */   }
/*     */ 
/*     */   
/*     */   public CatalystListener getListener() {
/*  57 */     return this.catalystListener;
/*     */   }
/*     */   
/*     */   public static class CatalystListener implements GameEventListener {
/*     */     public static final int PULSE_TICKS = 8;
/*     */     final SculkSpreader sculkSpreader;
/*     */     private final BlockState blockState;
/*     */     private final PositionSource positionSource;
/*     */     
/*     */     public CatalystListener(BlockState param1BlockState, PositionSource param1PositionSource) {
/*  67 */       this.blockState = param1BlockState;
/*  68 */       this.positionSource = param1PositionSource;
/*  69 */       this.sculkSpreader = SculkSpreader.createLevelSpreader();
/*     */     }
/*     */ 
/*     */     
/*     */     public PositionSource getListenerSource() {
/*  74 */       return this.positionSource;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getListenerRadius() {
/*  79 */       return 8;
/*     */     }
/*     */ 
/*     */     
/*     */     public GameEventListener.DeliveryMode getDeliveryMode() {
/*  84 */       return GameEventListener.DeliveryMode.BY_DISTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean handleGameEvent(ServerLevel param1ServerLevel, Holder<GameEvent> param1Holder, GameEvent.Context param1Context, Vec3 param1Vec3) {
/*  89 */       if (param1Holder.is((Holder)GameEvent.ENTITY_DIE)) { Entity entity = param1Context.sourceEntity(); if (entity instanceof LivingEntity) { LivingEntity livingEntity = (LivingEntity)entity;
/*  90 */           if (!livingEntity.wasExperienceConsumed()) {
/*  91 */             DamageSource damageSource = livingEntity.getLastDamageSource();
/*  92 */             int i = livingEntity.getExperienceReward(param1ServerLevel, (Entity)Optionull.map(damageSource, DamageSource::getEntity));
/*  93 */             if (livingEntity.shouldDropExperience() && i > 0) {
/*  94 */               this.sculkSpreader.addCursors(BlockPos.containing((Position)param1Vec3.relative(Direction.UP, 0.5D)), i);
/*  95 */               tryAwardItSpreadsAdvancement((Level)param1ServerLevel, livingEntity);
/*     */             } 
/*  97 */             livingEntity.skipDropExperience();
/*  98 */             this.positionSource.getPosition((Level)param1ServerLevel).ifPresent(param1Vec3 -> bloom(param1ServerLevel, BlockPos.containing((Position)param1Vec3), this.blockState, param1ServerLevel.getRandom()));
/*     */           } 
/* 100 */           return true; }
/*     */          }
/*     */       
/* 103 */       return false;
/*     */     }
/*     */     
/*     */     @VisibleForTesting
/*     */     public SculkSpreader getSculkSpreader() {
/* 108 */       return this.sculkSpreader;
/*     */     }
/*     */     
/*     */     private void bloom(ServerLevel param1ServerLevel, BlockPos param1BlockPos, BlockState param1BlockState, RandomSource param1RandomSource) {
/* 112 */       param1ServerLevel.setBlock(param1BlockPos, (BlockState)param1BlockState.setValue((Property)SculkCatalystBlock.PULSE, Boolean.valueOf(true)), 3);
/* 113 */       param1ServerLevel.scheduleTick(param1BlockPos, param1BlockState.getBlock(), 8);
/*     */       
/* 115 */       param1ServerLevel.sendParticles((ParticleOptions)ParticleTypes.SCULK_SOUL, param1BlockPos.getX() + 0.5D, param1BlockPos.getY() + 1.15D, param1BlockPos.getZ() + 0.5D, 2, 0.2D, 0.0D, 0.2D, 0.0D);
/*     */       
/* 117 */       param1ServerLevel.playSound(null, param1BlockPos, SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.BLOCKS, 2.0F, 0.6F + param1RandomSource.nextFloat() * 0.4F);
/*     */     }
/*     */     
/*     */     private void tryAwardItSpreadsAdvancement(Level param1Level, LivingEntity param1LivingEntity) {
/* 121 */       LivingEntity livingEntity = param1LivingEntity.getLastHurtByMob();
/* 122 */       if (livingEntity instanceof ServerPlayer) { ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
/* 123 */         DamageSource damageSource = (param1LivingEntity.getLastDamageSource() == null) ? param1Level.damageSources().playerAttack((Player)serverPlayer) : param1LivingEntity.getLastDamageSource();
/* 124 */         CriteriaTriggers.KILL_MOB_NEAR_SCULK_CATALYST.trigger(serverPlayer, (Entity)param1LivingEntity, damageSource); }
/*     */     
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\entity\SculkCatalystBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */