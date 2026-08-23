/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.InsideBlockEffectApplier;
/*     */ import net.minecraft.world.entity.animal.frog.Tadpole;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FrogspawnBlock extends Block {
/*  29 */   public static final MapCodec<FrogspawnBlock> CODEC = simpleCodec(FrogspawnBlock::new); private static final int MIN_TADPOLES_SPAWN = 2;
/*     */   private static final int MAX_TADPOLES_SPAWN = 5;
/*     */   
/*     */   public MapCodec<FrogspawnBlock> codec() {
/*  33 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_MIN_HATCH_TICK_DELAY = 3600;
/*     */   
/*     */   private static final int DEFAULT_MAX_HATCH_TICK_DELAY = 12000;
/*     */   
/*  41 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 1.5D);
/*     */   
/*  43 */   private static int minHatchTickDelay = 3600;
/*  44 */   private static int maxHatchTickDelay = 12000;
/*     */   
/*     */   public FrogspawnBlock(BlockBehaviour.Properties paramProperties) {
/*  47 */     super(paramProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  52 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  57 */     return mayPlaceOn((BlockGetter)paramLevelReader, paramBlockPos.below());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/*  62 */     paramLevel.scheduleTick(paramBlockPos, this, getFrogspawnHatchDelay(paramLevel.getRandom()));
/*     */   }
/*     */   
/*     */   private static int getFrogspawnHatchDelay(RandomSource paramRandomSource) {
/*  66 */     return paramRandomSource.nextInt(minHatchTickDelay, maxHatchTickDelay);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  71 */     if (!canSurvive(paramBlockState1, paramLevelReader, paramBlockPos1)) {
/*  72 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*  74 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  79 */     if (!canSurvive(paramBlockState, (LevelReader)paramServerLevel, paramBlockPos)) {
/*  80 */       destroyBlock((Level)paramServerLevel, paramBlockPos);
/*     */       return;
/*     */     } 
/*  83 */     hatchFrogspawn(paramServerLevel, paramBlockPos, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInside(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Entity paramEntity, InsideBlockEffectApplier paramInsideBlockEffectApplier, boolean paramBoolean) {
/*  88 */     if (paramEntity.getType().equals(EntityType.FALLING_BLOCK)) {
/*  89 */       destroyBlock(paramLevel, paramBlockPos);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean mayPlaceOn(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  94 */     FluidState fluidState1 = paramBlockGetter.getFluidState(paramBlockPos);
/*  95 */     FluidState fluidState2 = paramBlockGetter.getFluidState(paramBlockPos.above());
/*  96 */     return (fluidState1.getType() == Fluids.WATER && fluidState2.getType() == Fluids.EMPTY);
/*     */   }
/*     */   
/*     */   private void hatchFrogspawn(ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 100 */     destroyBlock((Level)paramServerLevel, paramBlockPos);
/* 101 */     paramServerLevel.playSound(null, paramBlockPos, SoundEvents.FROGSPAWN_HATCH, SoundSource.BLOCKS, 1.0F, 1.0F);
/* 102 */     spawnTadpoles(paramServerLevel, paramBlockPos, paramRandomSource);
/*     */   }
/*     */   
/*     */   private void destroyBlock(Level paramLevel, BlockPos paramBlockPos) {
/* 106 */     paramLevel.destroyBlock(paramBlockPos, false);
/*     */   }
/*     */   
/*     */   private void spawnTadpoles(ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 110 */     int i = paramRandomSource.nextInt(2, 6);
/* 111 */     for (byte b = 1; b <= i; b++) {
/* 112 */       Tadpole tadpole = (Tadpole)EntityType.TADPOLE.create((Level)paramServerLevel, EntitySpawnReason.BREEDING);
/* 113 */       if (tadpole != null) {
/* 114 */         double d1 = paramBlockPos.getX() + getRandomTadpolePositionOffset(paramRandomSource);
/* 115 */         double d2 = paramBlockPos.getZ() + getRandomTadpolePositionOffset(paramRandomSource);
/* 116 */         int j = paramRandomSource.nextInt(1, 361);
/* 117 */         tadpole.snapTo(d1, paramBlockPos.getY() - 0.5D, d2, j, 0.0F);
/* 118 */         tadpole.setPersistenceRequired();
/* 119 */         paramServerLevel.addFreshEntity((Entity)tadpole);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private double getRandomTadpolePositionOffset(RandomSource paramRandomSource) {
/* 125 */     double d = 0.20000000298023224D;
/* 126 */     return Mth.clamp(paramRandomSource.nextDouble(), 0.20000000298023224D, 0.7999999970197678D);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static void setHatchDelay(int paramInt1, int paramInt2) {
/* 131 */     minHatchTickDelay = paramInt1;
/* 132 */     maxHatchTickDelay = paramInt2;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public static void setDefaultHatchDelay() {
/* 137 */     minHatchTickDelay = 3600;
/* 138 */     maxHatchTickDelay = 12000;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\FrogspawnBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */