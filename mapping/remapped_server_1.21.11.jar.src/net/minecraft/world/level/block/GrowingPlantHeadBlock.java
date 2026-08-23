/*     */ package net.minecraft.world.level.block;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public abstract class GrowingPlantHeadBlock extends GrowingPlantBlock implements BonemealableBlock {
/*  19 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_25;
/*     */   
/*     */   public static final int MAX_AGE = 25;
/*     */   private final double growPerTickProbability;
/*     */   
/*     */   protected GrowingPlantHeadBlock(BlockBehaviour.Properties paramProperties, Direction paramDirection, VoxelShape paramVoxelShape, boolean paramBoolean, double paramDouble) {
/*  25 */     super(paramProperties, paramDirection, paramVoxelShape, paramBoolean);
/*  26 */     this.growPerTickProbability = paramDouble;
/*  27 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract MapCodec<? extends GrowingPlantHeadBlock> codec();
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(RandomSource paramRandomSource) {
/*  35 */     return (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(paramRandomSource.nextInt(25)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/*  40 */     return (((Integer)paramBlockState.getValue((Property)AGE)).intValue() < 25);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  45 */     if (((Integer)paramBlockState.getValue((Property)AGE)).intValue() < 25 && paramRandomSource.nextDouble() < this.growPerTickProbability) {
/*  46 */       BlockPos blockPos = paramBlockPos.relative(this.growthDirection);
/*  47 */       if (canGrowInto(paramServerLevel.getBlockState(blockPos))) {
/*  48 */         paramServerLevel.setBlockAndUpdate(blockPos, getGrowIntoState(paramBlockState, paramServerLevel.random));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected BlockState getGrowIntoState(BlockState paramBlockState, RandomSource paramRandomSource) {
/*  54 */     return (BlockState)paramBlockState.cycle((Property)AGE);
/*     */   }
/*     */   
/*     */   public BlockState getMaxAgeState(BlockState paramBlockState) {
/*  58 */     return (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(25));
/*     */   }
/*     */   
/*     */   public boolean isMaxAge(BlockState paramBlockState) {
/*  62 */     return (((Integer)paramBlockState.getValue((Property)AGE)).intValue() == 25);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateBodyAfterConvertedFromHead(BlockState paramBlockState1, BlockState paramBlockState2) {
/*  69 */     return paramBlockState2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  74 */     if (paramDirection == this.growthDirection.getOpposite()) {
/*  75 */       if (!paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  76 */         paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 1);
/*     */       } else {
/*     */         
/*  79 */         BlockState blockState = paramLevelReader.getBlockState(paramBlockPos1.relative(this.growthDirection));
/*  80 */         if (blockState.is(this) || blockState.is(getBodyBlock())) {
/*  81 */           return updateBodyAfterConvertedFromHead(paramBlockState1, getBodyBlock().defaultBlockState());
/*     */         }
/*     */       } 
/*     */     }
/*  85 */     if (paramDirection == this.growthDirection && (paramBlockState2.is(this) || paramBlockState2.is(getBodyBlock())))
/*     */     {
/*  87 */       return updateBodyAfterConvertedFromHead(paramBlockState1, getBodyBlock().defaultBlockState());
/*     */     }
/*  89 */     if (this.scheduleFluidTicks) {
/*  90 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/*  93 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  98 */     paramBuilder.add(new Property[] { (Property)AGE });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 103 */     return canGrowInto(paramLevelReader.getBlockState(paramBlockPos.relative(this.growthDirection)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 108 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 113 */     BlockPos blockPos = paramBlockPos.relative(this.growthDirection);
/* 114 */     int i = Math.min(((Integer)paramBlockState.getValue((Property)AGE)).intValue() + 1, 25);
/*     */     
/* 116 */     int j = getBlocksToGrowWhenBonemealed(paramRandomSource);
/* 117 */     for (byte b = 0; b < j && 
/* 118 */       canGrowInto(paramServerLevel.getBlockState(blockPos)); b++) {
/*     */ 
/*     */       
/* 121 */       paramServerLevel.setBlockAndUpdate(blockPos, (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(i)));
/*     */       
/* 123 */       blockPos = blockPos.relative(this.growthDirection);
/* 124 */       i = Math.min(i + 1, 25);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract int getBlocksToGrowWhenBonemealed(RandomSource paramRandomSource);
/*     */   
/*     */   protected abstract boolean canGrowInto(BlockState paramBlockState);
/*     */   
/*     */   protected GrowingPlantHeadBlock getHeadBlock() {
/* 134 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\GrowingPlantHeadBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */