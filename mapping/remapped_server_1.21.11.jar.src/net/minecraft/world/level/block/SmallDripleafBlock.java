/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SmallDripleafBlock extends DoublePlantBlock implements BonemealableBlock, SimpleWaterloggedBlock {
/*  29 */   public static final MapCodec<SmallDripleafBlock> CODEC = simpleCodec(SmallDripleafBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<SmallDripleafBlock> codec() {
/*  33 */     return CODEC;
/*     */   }
/*     */   
/*  36 */   private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  37 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*     */   
/*  39 */   private static final VoxelShape SHAPE = Block.column(12.0D, 0.0D, 13.0D);
/*     */   
/*     */   public SmallDripleafBlock(BlockBehaviour.Properties paramProperties) {
/*  42 */     super(paramProperties);
/*     */     
/*  44 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HALF, (Comparable)DoubleBlockHalf.LOWER)).setValue((Property)WATERLOGGED, Boolean.valueOf(false))).setValue((Property)FACING, (Comparable)Direction.NORTH));
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  49 */     return SHAPE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean mayPlaceOn(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  54 */     return (paramBlockState.is(BlockTags.SMALL_DRIPLEAF_PLACEABLE) || (paramBlockGetter.getFluidState(paramBlockPos.above()).isSourceOfType((Fluid)Fluids.WATER) && super.mayPlaceOn(paramBlockState, paramBlockGetter, paramBlockPos)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  59 */     BlockState blockState = super.getStateForPlacement(paramBlockPlaceContext);
/*  60 */     if (blockState != null) {
/*  61 */       return copyWaterloggedFrom((LevelReader)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos(), (BlockState)blockState.setValue((Property)FACING, (Comparable)paramBlockPlaceContext.getHorizontalDirection().getOpposite()));
/*     */     }
/*  63 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, LivingEntity paramLivingEntity, ItemStack paramItemStack) {
/*  68 */     if (!paramLevel.isClientSide()) {
/*  69 */       BlockPos blockPos = paramBlockPos.above();
/*  70 */       BlockState blockState = DoublePlantBlock.copyWaterloggedFrom((LevelReader)paramLevel, blockPos, (BlockState)((BlockState)defaultBlockState().setValue((Property)HALF, (Comparable)DoubleBlockHalf.UPPER)).setValue((Property)FACING, paramBlockState.getValue((Property)FACING)));
/*  71 */       paramLevel.setBlock(blockPos, blockState, 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/*  77 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  78 */       return Fluids.WATER.getSource(false);
/*     */     }
/*  80 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  85 */     if (paramBlockState.getValue((Property)HALF) == DoubleBlockHalf.UPPER) {
/*  86 */       return super.canSurvive(paramBlockState, paramLevelReader, paramBlockPos);
/*     */     }
/*     */     
/*  89 */     BlockPos blockPos = paramBlockPos.below();
/*  90 */     BlockState blockState = paramLevelReader.getBlockState(blockPos);
/*  91 */     return mayPlaceOn(blockState, (BlockGetter)paramLevelReader, blockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  96 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/*  97 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*  99 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 104 */     paramBuilder.add(new Property[] { (Property)HALF, (Property)WATERLOGGED, (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 109 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 114 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel paramServerLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 119 */     if (paramBlockState.getValue((Property)DoublePlantBlock.HALF) == DoubleBlockHalf.LOWER) {
/*     */       
/* 121 */       BlockPos blockPos = paramBlockPos.above();
/* 122 */       paramServerLevel.setBlock(blockPos, paramServerLevel.getFluidState(blockPos).createLegacyBlock(), 18);
/* 123 */       BigDripleafBlock.placeWithRandomHeight((LevelAccessor)paramServerLevel, paramRandomSource, paramBlockPos, (Direction)paramBlockState.getValue((Property)FACING));
/*     */     } else {
/* 125 */       BlockPos blockPos = paramBlockPos.below();
/* 126 */       performBonemeal(paramServerLevel, paramRandomSource, blockPos, paramServerLevel.getBlockState(blockPos));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 132 */     return (BlockState)paramBlockState.setValue((Property)FACING, (Comparable)paramRotation.rotate((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 137 */     return paramBlockState.rotate(paramMirror.getRotation((Direction)paramBlockState.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getMaxVerticalOffset() {
/* 142 */     return 0.1F;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SmallDripleafBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */