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
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BambooLeaves;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BambooStalkBlock extends Block implements BonemealableBlock {
/*  27 */   public static final MapCodec<BambooStalkBlock> CODEC = simpleCodec(BambooStalkBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<BambooStalkBlock> codec() {
/*  31 */     return CODEC;
/*     */   }
/*     */   
/*  34 */   private static final VoxelShape SHAPE_SMALL = Block.column(6.0D, 0.0D, 16.0D);
/*  35 */   private static final VoxelShape SHAPE_LARGE = Block.column(10.0D, 0.0D, 16.0D);
/*  36 */   private static final VoxelShape SHAPE_COLLISION = Block.column(3.0D, 0.0D, 16.0D);
/*     */   
/*  38 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
/*  39 */   public static final EnumProperty<BambooLeaves> LEAVES = BlockStateProperties.BAMBOO_LEAVES;
/*  40 */   public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
/*     */   
/*     */   public static final int MAX_HEIGHT = 16;
/*     */   public static final int STAGE_GROWING = 0;
/*     */   public static final int STAGE_DONE_GROWING = 1;
/*     */   public static final int AGE_THIN_BAMBOO = 0;
/*     */   public static final int AGE_THICK_BAMBOO = 1;
/*     */   
/*     */   public BambooStalkBlock(BlockBehaviour.Properties paramProperties) {
/*  49 */     super(paramProperties);
/*  50 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0))).setValue((Property)LEAVES, (Comparable)BambooLeaves.NONE)).setValue((Property)STAGE, Integer.valueOf(0)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  55 */     paramBuilder.add(new Property[] { (Property)AGE, (Property)LEAVES, (Property)STAGE });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/*  60 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  65 */     VoxelShape voxelShape = (paramBlockState.getValue((Property)LEAVES) == BambooLeaves.LARGE) ? SHAPE_LARGE : SHAPE_SMALL;
/*  66 */     return voxelShape.move(paramBlockState.getOffset(paramBlockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  76 */     return SHAPE_COLLISION.move(paramBlockState.getOffset(paramBlockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isCollisionShapeFullBlock(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  86 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*  87 */     if (!fluidState.isEmpty()) {
/*  88 */       return null;
/*     */     }
/*     */     
/*  91 */     BlockState blockState = paramBlockPlaceContext.getLevel().getBlockState(paramBlockPlaceContext.getClickedPos().below());
/*  92 */     if (blockState.is(BlockTags.BAMBOO_PLANTABLE_ON)) {
/*  93 */       if (blockState.is(Blocks.BAMBOO_SAPLING))
/*  94 */         return (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(0)); 
/*  95 */       if (blockState.is(Blocks.BAMBOO)) {
/*  96 */         boolean bool = (((Integer)blockState.getValue((Property)AGE)).intValue() > 0) ? true : false;
/*  97 */         return (BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(bool));
/*     */       } 
/*  99 */       BlockState blockState1 = paramBlockPlaceContext.getLevel().getBlockState(paramBlockPlaceContext.getClickedPos().above());
/* 100 */       if (blockState1.is(Blocks.BAMBOO)) {
/* 101 */         return (BlockState)defaultBlockState().setValue((Property)AGE, blockState1.getValue((Property)AGE));
/*     */       }
/* 103 */       return Blocks.BAMBOO_SAPLING.defaultBlockState();
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 113 */     if (!paramBlockState.canSurvive((LevelReader)paramServerLevel, paramBlockPos)) {
/* 114 */       paramServerLevel.destroyBlock(paramBlockPos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isRandomlyTicking(BlockState paramBlockState) {
/* 120 */     return (((Integer)paramBlockState.getValue((Property)STAGE)).intValue() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 125 */     if (((Integer)paramBlockState.getValue((Property)STAGE)).intValue() != 0) {
/*     */       return;
/*     */     }
/*     */     
/* 129 */     if (paramRandomSource.nextInt(3) == 0 && paramServerLevel.isEmptyBlock(paramBlockPos.above()) && paramServerLevel.getRawBrightness(paramBlockPos.above(), 0) >= 9) {
/* 130 */       int i = getHeightBelowUpToMax((BlockGetter)paramServerLevel, paramBlockPos) + 1;
/* 131 */       if (i < 16) {
/* 132 */         growBamboo(paramBlockState, (Level)paramServerLevel, paramBlockPos, paramRandomSource, i);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 139 */     return paramLevelReader.getBlockState(paramBlockPos.below()).is(BlockTags.BAMBOO_PLANTABLE_ON);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 144 */     if (!paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/* 145 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 1);
/*     */     }
/*     */     
/* 148 */     if (paramDirection == Direction.UP && 
/* 149 */       paramBlockState2.is(Blocks.BAMBOO) && ((Integer)paramBlockState2.getValue((Property)AGE)).intValue() > ((Integer)paramBlockState1.getValue((Property)AGE)).intValue()) {
/* 150 */       return (BlockState)paramBlockState1.cycle((Property)AGE);
/*     */     }
/*     */ 
/*     */     
/* 154 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 159 */     int i = getHeightAboveUpToMax((BlockGetter)paramLevelReader, paramBlockPos);
/* 160 */     int j = getHeightBelowUpToMax((BlockGetter)paramLevelReader, paramBlockPos);
/* 161 */     return (i + j + 1 < 16 && ((Integer)paramLevelReader.getBlockState(paramBlockPos.above(i)).getValue((Property)STAGE)).intValue() != 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 166 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 171 */     int i = getHeightAboveUpToMax((BlockGetter)paramServerLevel, paramBlockPos);
/* 172 */     int j = getHeightBelowUpToMax((BlockGetter)paramServerLevel, paramBlockPos);
/* 173 */     int k = i + j + 1;
/*     */     
/* 175 */     int m = 1 + paramRandomSource.nextInt(2);
/* 176 */     for (byte b = 0; b < m; b++) {
/* 177 */       BlockPos blockPos = paramBlockPos.above(i);
/* 178 */       BlockState blockState = paramServerLevel.getBlockState(blockPos);
/* 179 */       if (k >= 16 || ((Integer)blockState.getValue((Property)STAGE)).intValue() == 1 || !paramServerLevel.isEmptyBlock(blockPos.above())) {
/*     */         return;
/*     */       }
/*     */       
/* 183 */       growBamboo(blockState, (Level)paramServerLevel, blockPos, paramRandomSource, k);
/*     */       
/* 185 */       i++;
/* 186 */       k++;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void growBamboo(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource, int paramInt) {
/* 191 */     BlockState blockState1 = paramLevel.getBlockState(paramBlockPos.below());
/* 192 */     BlockPos blockPos = paramBlockPos.below(2);
/* 193 */     BlockState blockState2 = paramLevel.getBlockState(blockPos);
/*     */     
/* 195 */     BambooLeaves bambooLeaves = BambooLeaves.NONE;
/* 196 */     if (paramInt >= 1) {
/* 197 */       if (!blockState1.is(Blocks.BAMBOO) || blockState1.getValue((Property)LEAVES) == BambooLeaves.NONE) {
/* 198 */         bambooLeaves = BambooLeaves.SMALL;
/* 199 */       } else if (blockState1.is(Blocks.BAMBOO) && blockState1.getValue((Property)LEAVES) != BambooLeaves.NONE) {
/* 200 */         bambooLeaves = BambooLeaves.LARGE;
/*     */         
/* 202 */         if (blockState2.is(Blocks.BAMBOO)) {
/* 203 */           paramLevel.setBlock(paramBlockPos.below(), (BlockState)blockState1.setValue((Property)LEAVES, (Comparable)BambooLeaves.SMALL), 3);
/* 204 */           paramLevel.setBlock(blockPos, (BlockState)blockState2.setValue((Property)LEAVES, (Comparable)BambooLeaves.NONE), 3);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 209 */     boolean bool1 = (((Integer)paramBlockState.getValue((Property)AGE)).intValue() == 1 || blockState2.is(Blocks.BAMBOO)) ? true : false;
/* 210 */     boolean bool2 = ((paramInt >= 11 && paramRandomSource.nextFloat() < 0.25F) || paramInt == 15) ? true : false;
/* 211 */     paramLevel.setBlock(paramBlockPos.above(), (BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)AGE, Integer.valueOf(bool1))).setValue((Property)LEAVES, (Comparable)bambooLeaves)).setValue((Property)STAGE, Integer.valueOf(bool2)), 3);
/*     */   }
/*     */   
/*     */   protected int getHeightAboveUpToMax(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 215 */     byte b = 0;
/* 216 */     while (b < 16 && paramBlockGetter.getBlockState(paramBlockPos.above(b + 1)).is(Blocks.BAMBOO)) {
/* 217 */       b++;
/*     */     }
/* 219 */     return b;
/*     */   }
/*     */   
/*     */   protected int getHeightBelowUpToMax(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 223 */     byte b = 0;
/* 224 */     while (b < 16 && paramBlockGetter.getBlockState(paramBlockPos.below(b + 1)).is(Blocks.BAMBOO)) {
/* 225 */       b++;
/*     */     }
/* 227 */     return b;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BambooStalkBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */