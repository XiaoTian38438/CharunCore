/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SeaPickleBlock extends VegetationBlock implements BonemealableBlock, SimpleWaterloggedBlock {
/*  27 */   public static final MapCodec<SeaPickleBlock> CODEC = simpleCodec(SeaPickleBlock::new);
/*     */   public static final int MAX_PICKLES = 4;
/*     */   
/*     */   public MapCodec<SeaPickleBlock> codec() {
/*  31 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  35 */   public static final IntegerProperty PICKLES = BlockStateProperties.PICKLES;
/*  36 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  38 */   private static final VoxelShape SHAPE_ONE = Block.column(4.0D, 0.0D, 6.0D);
/*  39 */   private static final VoxelShape SHAPE_TWO = Block.column(10.0D, 0.0D, 6.0D);
/*  40 */   private static final VoxelShape SHAPE_THREE = Block.column(12.0D, 0.0D, 6.0D);
/*  41 */   private static final VoxelShape SHAPE_FOUR = Block.column(12.0D, 0.0D, 7.0D);
/*     */   
/*     */   protected SeaPickleBlock(BlockBehaviour.Properties paramProperties) {
/*  44 */     super(paramProperties);
/*  45 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)PICKLES, Integer.valueOf(1))).setValue((Property)WATERLOGGED, Boolean.valueOf(true)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  50 */     BlockState blockState = paramBlockPlaceContext.getLevel().getBlockState(paramBlockPlaceContext.getClickedPos());
/*  51 */     if (blockState.is(this)) {
/*  52 */       return (BlockState)blockState.setValue((Property)PICKLES, Integer.valueOf(Math.min(4, ((Integer)blockState.getValue((Property)PICKLES)).intValue() + 1)));
/*     */     }
/*     */     
/*  55 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*  56 */     boolean bool = (fluidState.getType() == Fluids.WATER) ? true : false;
/*  57 */     return (BlockState)super.getStateForPlacement(paramBlockPlaceContext).setValue((Property)WATERLOGGED, Boolean.valueOf(bool));
/*     */   }
/*     */   
/*     */   public static boolean isDead(BlockState paramBlockState) {
/*  61 */     return !((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  66 */     return (!paramBlockState.getCollisionShape(paramBlockGetter, paramBlockPos).getFaceShape(Direction.UP).isEmpty() || paramBlockState.isFaceSturdy(paramBlockGetter, paramBlockPos, Direction.UP));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  71 */     BlockPos blockPos = paramBlockPos.below();
/*  72 */     return mayPlaceOn(paramLevelReader.getBlockState(blockPos), (BlockGetter)paramLevelReader, blockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  77 */     if (!paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  78 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  81 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  82 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/*  85 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState paramBlockState, BlockPlaceContext paramBlockPlaceContext) {
/*  90 */     if (!paramBlockPlaceContext.isSecondaryUseActive() && paramBlockPlaceContext.getItemInHand().is(asItem()) && ((Integer)paramBlockState.getValue((Property)PICKLES)).intValue() < 4) {
/*  91 */       return true;
/*     */     }
/*  93 */     return super.canBeReplaced(paramBlockState, paramBlockPlaceContext);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  98 */     switch (((Integer)paramBlockState.getValue((Property)PICKLES)).intValue()) { default: case 2: case 3: case 4: break; }  return 
/*     */ 
/*     */ 
/*     */       
/* 102 */       SHAPE_FOUR;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 108 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 109 */       return Fluids.WATER.getSource(false);
/*     */     }
/*     */     
/* 112 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 117 */     paramBuilder.add(new Property[] { (Property)PICKLES, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 122 */     return (!isDead(paramBlockState) && paramLevelReader.getBlockState(paramBlockPos.below()).is(BlockTags.CORAL_BLOCKS));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 127 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 132 */     byte b1 = 5;
/* 133 */     byte b2 = 1;
/* 134 */     byte b3 = 2;
/* 135 */     byte b4 = 0;
/*     */     
/* 137 */     int i = paramBlockPos.getX() - 2;
/* 138 */     byte b5 = 0;
/*     */     
/* 140 */     for (byte b6 = 0; b6 < 5; b6++) {
/* 141 */       for (byte b = 0; b < b2; b++) {
/* 142 */         int j = 2 + paramBlockPos.getY() - 1;
/* 143 */         for (int k = j - 2; k < j; k++) {
/* 144 */           BlockPos blockPos = new BlockPos(i + b6, k, paramBlockPos.getZ() - b5 + b);
/* 145 */           if (!blockPos.equals(paramBlockPos))
/*     */           {
/*     */ 
/*     */             
/* 149 */             if (paramRandomSource.nextInt(6) == 0 && paramServerLevel.getBlockState(blockPos).is(Blocks.WATER)) {
/* 150 */               BlockState blockState = paramServerLevel.getBlockState(blockPos.below());
/* 151 */               if (blockState.is(BlockTags.CORAL_BLOCKS)) {
/* 152 */                 paramServerLevel.setBlock(blockPos, (BlockState)Blocks.SEA_PICKLE.defaultBlockState().setValue((Property)PICKLES, Integer.valueOf(paramRandomSource.nextInt(4) + 1)), 3);
/*     */               }
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 158 */       if (b4 < 2) {
/* 159 */         b2 += 2;
/* 160 */         b5++;
/*     */       } else {
/* 162 */         b2 -= 2;
/* 163 */         b5--;
/*     */       } 
/* 165 */       b4++;
/*     */     } 
/*     */     
/* 168 */     paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)PICKLES, Integer.valueOf(4)), 2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 173 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SeaPickleBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */