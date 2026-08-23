/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.BlockUtil;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
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
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class BigDripleafStemBlock extends HorizontalDirectionalBlock implements BonemealableBlock, SimpleWaterloggedBlock {
/*  30 */   public static final MapCodec<BigDripleafStemBlock> CODEC = simpleCodec(BigDripleafStemBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<BigDripleafStemBlock> codec() {
/*  34 */     return CODEC;
/*     */   }
/*     */   
/*  37 */   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  39 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.column(6.0D, 0.0D, 16.0D).move(0.0D, 0.0D, 0.25D).optimize());
/*     */   
/*     */   protected BigDripleafStemBlock(BlockBehaviour.Properties paramProperties) {
/*  42 */     super(paramProperties);
/*  43 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)WATERLOGGED, Boolean.valueOf(false))).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  48 */     return SHAPES.get(paramBlockState.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  53 */     paramBuilder.add(new Property[] { (Property)WATERLOGGED, (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/*  58 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  59 */       return Fluids.WATER.getSource(false);
/*     */     }
/*     */     
/*  62 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  67 */     BlockPos blockPos = paramBlockPos.below();
/*  68 */     BlockState blockState1 = paramLevelReader.getBlockState(blockPos);
/*  69 */     BlockState blockState2 = paramLevelReader.getBlockState(paramBlockPos.above());
/*  70 */     return ((blockState1.is(this) || blockState1.is(BlockTags.BIG_DRIPLEAF_PLACEABLE)) && (blockState2
/*  71 */       .is(this) || blockState2.is(Blocks.BIG_DRIPLEAF)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static boolean place(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, FluidState paramFluidState, Direction paramDirection) {
/*  77 */     BlockState blockState = (BlockState)((BlockState)Blocks.BIG_DRIPLEAF_STEM.defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf(paramFluidState.isSourceOfType((Fluid)Fluids.WATER)))).setValue((Property)FACING, (Comparable)paramDirection);
/*  78 */     return paramLevelAccessor.setBlock(paramBlockPos, blockState, 3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  83 */     if ((paramDirection == Direction.DOWN || paramDirection == Direction.UP) && !paramBlockState1.canSurvive(paramLevelReader, paramBlockPos1)) {
/*  84 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 1);
/*     */     }
/*  86 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  87 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*  89 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/*  94 */     if (!paramBlockState.canSurvive((LevelReader)paramServerLevel, paramBlockPos)) {
/*  95 */       paramServerLevel.destroyBlock(paramBlockPos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 101 */     Optional<BlockPos> optional = BlockUtil.getTopConnectedBlock((BlockGetter)paramLevelReader, paramBlockPos, paramBlockState.getBlock(), Direction.UP, Blocks.BIG_DRIPLEAF);
/* 102 */     if (optional.isEmpty()) {
/* 103 */       return false;
/*     */     }
/* 105 */     BlockPos blockPos = ((BlockPos)optional.get()).above();
/* 106 */     BlockState blockState = paramLevelReader.getBlockState(blockPos);
/* 107 */     return BigDripleafBlock.canPlaceAt((LevelHeightAccessor)paramLevelReader, blockPos, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 112 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 117 */     Optional<BlockPos> optional = BlockUtil.getTopConnectedBlock((BlockGetter)paramServerLevel, paramBlockPos, paramBlockState.getBlock(), Direction.UP, Blocks.BIG_DRIPLEAF);
/* 118 */     if (optional.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 122 */     BlockPos blockPos1 = optional.get();
/* 123 */     BlockPos blockPos2 = blockPos1.above();
/* 124 */     Direction direction = (Direction)paramBlockState.getValue((Property)FACING);
/*     */     
/* 126 */     place((LevelAccessor)paramServerLevel, blockPos1, paramServerLevel.getFluidState(blockPos1), direction);
/* 127 */     BigDripleafBlock.place((LevelAccessor)paramServerLevel, blockPos2, paramServerLevel.getFluidState(blockPos2), direction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState, boolean paramBoolean) {
/* 132 */     return new ItemStack(Blocks.BIG_DRIPLEAF);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\BigDripleafStemBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */