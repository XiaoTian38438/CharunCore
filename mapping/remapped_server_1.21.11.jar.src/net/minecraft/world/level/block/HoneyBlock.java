/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.particles.BlockParticleOption;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
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
/*     */ public class HoneyBlock
/*     */   extends HalfTransparentBlock
/*     */ {
/*  38 */   public static final MapCodec<HoneyBlock> CODEC = simpleCodec(HoneyBlock::new);
/*     */   private static final double SLIDE_STARTS_WHEN_VERTICAL_SPEED_IS_AT_LEAST = 0.13D;
/*     */   
/*     */   public MapCodec<HoneyBlock> codec() {
/*  42 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final double MIN_FALL_SPEED_TO_BE_CONSIDERED_SLIDING = 0.08D;
/*     */ 
/*     */   
/*     */   private static final double THROTTLE_SLIDE_SPEED_TO = 0.05D;
/*     */   
/*     */   private static final int SLIDE_ADVANCEMENT_CHECK_INTERVAL = 20;
/*     */   
/*  54 */   private static final VoxelShape SHAPE = Block.column(14.0D, 0.0D, 15.0D);
/*     */   
/*     */   public HoneyBlock(BlockBehaviour.Properties paramProperties) {
/*  57 */     super(paramProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean doesEntityDoHoneyBlockSlideEffects(Entity paramEntity) {
/*  62 */     return (paramEntity instanceof net.minecraft.world.entity.LivingEntity || paramEntity instanceof net.minecraft.world.entity.vehicle.minecart.AbstractMinecart || paramEntity instanceof net.minecraft.world.entity.item.PrimedTnt || paramEntity instanceof net.minecraft.world.entity.vehicle.boat.AbstractBoat);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  70 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fallOn(Level paramLevel, BlockState paramBlockState, BlockPos paramBlockPos, Entity paramEntity, double paramDouble) {
/*  75 */     paramEntity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
/*     */     
/*  77 */     if (!paramLevel.isClientSide())
/*     */     {
/*     */       
/*  80 */       paramLevel.broadcastEntityEvent(paramEntity, (byte)54);
/*     */     }
/*     */     
/*  83 */     if (paramEntity.causeFallDamage(paramDouble, 0.2F, paramLevel.damageSources().fall())) {
/*  84 */       paramEntity.playSound(this.soundType.getFallSound(), this.soundType.getVolume() * 0.5F, this.soundType.getPitch() * 0.75F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/*  90 */     if (isSlidingDown(paramBlockPos, paramEntity)) {
/*  91 */       maybeDoSlideAchievement(paramEntity, paramBlockPos);
/*  92 */       doSlideMovement(paramEntity);
/*  93 */       maybeDoSlideEffects(paramLevel, paramEntity);
/*     */     } 
/*  95 */     super.entityInside(paramBlockState, paramLevel, paramBlockPos, paramEntity, paramInsideBlockEffectApplier, paramBoolean);
/*     */   }
/*     */ 
/*     */   
/*     */   private static double getOldDeltaY(double paramDouble) {
/* 100 */     return paramDouble / 0.9800000190734863D + 0.08D;
/*     */   }
/*     */   
/*     */   private static double getNewDeltaY(double paramDouble) {
/* 104 */     return (paramDouble - 0.08D) * 0.9800000190734863D;
/*     */   }
/*     */   
/*     */   private boolean isSlidingDown(BlockPos paramBlockPos, Entity paramEntity) {
/* 108 */     if (paramEntity.onGround()) {
/* 109 */       return false;
/*     */     }
/* 111 */     if (paramEntity.getY() > paramBlockPos.getY() + 0.9375D - 1.0E-7D)
/*     */     {
/* 113 */       return false;
/*     */     }
/* 115 */     if (getOldDeltaY((paramEntity.getDeltaMovement()).y) >= -0.08D) {
/* 116 */       return false;
/*     */     }
/*     */     
/* 119 */     double d1 = Math.abs(paramBlockPos.getX() + 0.5D - paramEntity.getX());
/* 120 */     double d2 = Math.abs(paramBlockPos.getZ() + 0.5D - paramEntity.getZ());
/*     */     
/* 122 */     double d3 = 0.4375D + (paramEntity.getBbWidth() / 2.0F);
/*     */     
/* 124 */     return (d1 + 1.0E-7D > d3 || d2 + 1.0E-7D > d3);
/*     */   }
/*     */   
/*     */   private void maybeDoSlideAchievement(Entity paramEntity, BlockPos paramBlockPos) {
/* 128 */     if (paramEntity instanceof ServerPlayer && paramEntity.level().getGameTime() % 20L == 0L)
/*     */     {
/* 130 */       CriteriaTriggers.HONEY_BLOCK_SLIDE.trigger((ServerPlayer)paramEntity, paramEntity.level().getBlockState(paramBlockPos));
/*     */     }
/*     */   }
/*     */   
/*     */   private void doSlideMovement(Entity paramEntity) {
/* 135 */     Vec3 vec3 = paramEntity.getDeltaMovement();
/* 136 */     if (getOldDeltaY((paramEntity.getDeltaMovement()).y) < -0.13D) {
/*     */       
/* 138 */       double d = -0.05D / getOldDeltaY((paramEntity.getDeltaMovement()).y);
/* 139 */       paramEntity.setDeltaMovement(new Vec3(vec3.x * d, getNewDeltaY(-0.05D), vec3.z * d));
/*     */     } else {
/*     */       
/* 142 */       paramEntity.setDeltaMovement(new Vec3(vec3.x, getNewDeltaY(-0.05D), vec3.z));
/*     */     } 
/* 144 */     paramEntity.resetFallDistance();
/*     */   }
/*     */   
/*     */   private void maybeDoSlideEffects(Level paramLevel, Entity paramEntity) {
/* 148 */     if (doesEntityDoHoneyBlockSlideEffects(paramEntity)) {
/* 149 */       if (paramLevel.random.nextInt(5) == 0) {
/* 150 */         paramEntity.playSound(SoundEvents.HONEY_BLOCK_SLIDE, 1.0F, 1.0F);
/*     */       }
/*     */       
/* 153 */       if (!paramLevel.isClientSide() && paramLevel.random.nextInt(5) == 0) {
/* 154 */         paramLevel.broadcastEntityEvent(paramEntity, (byte)53);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void showSlideParticles(Entity paramEntity) {
/* 160 */     showParticles(paramEntity, 5);
/*     */   }
/*     */   
/*     */   public static void showJumpParticles(Entity paramEntity) {
/* 164 */     showParticles(paramEntity, 10);
/*     */   }
/*     */   
/*     */   private static void showParticles(Entity paramEntity, int paramInt) {
/* 168 */     if (!paramEntity.level().isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 172 */     BlockState blockState = Blocks.HONEY_BLOCK.defaultBlockState();
/* 173 */     for (byte b = 0; b < paramInt; b++)
/*     */     {
/* 175 */       paramEntity.level().addParticle((ParticleOptions)new BlockParticleOption(ParticleTypes.BLOCK, blockState), paramEntity.getX(), paramEntity.getY(), paramEntity.getZ(), 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\HoneyBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */