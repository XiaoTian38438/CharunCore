/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.tags.FluidTags;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.SlabType;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class SlabBlock extends Block implements SimpleWaterloggedBlock {
/*  31 */   public static final MapCodec<SlabBlock> CODEC = simpleCodec(SlabBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<? extends SlabBlock> codec() {
/*  35 */     return CODEC;
/*     */   }
/*     */   
/*  38 */   public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;
/*  39 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  41 */   private static final VoxelShape SHAPE_BOTTOM = Block.column(16.0D, 0.0D, 8.0D);
/*  42 */   private static final VoxelShape SHAPE_TOP = Block.column(16.0D, 8.0D, 16.0D);
/*     */   
/*     */   public SlabBlock(BlockBehaviour.Properties paramProperties) {
/*  45 */     super(paramProperties);
/*     */     
/*  47 */     registerDefaultState((BlockState)((BlockState)defaultBlockState().setValue((Property)TYPE, (Comparable)SlabType.BOTTOM)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState paramBlockState) {
/*  52 */     return (paramBlockState.getValue((Property)TYPE) != SlabType.DOUBLE);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  57 */     paramBuilder.add(new Property[] { (Property)TYPE, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  62 */     switch ((SlabType)paramBlockState.getValue((Property)TYPE)) { default: throw new MatchException(null, null);case LAND: case WATER: case AIR: break; }  return 
/*     */ 
/*     */       
/*  65 */       Shapes.block();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  71 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*  72 */     BlockState blockState1 = paramBlockPlaceContext.getLevel().getBlockState(blockPos);
/*  73 */     if (blockState1.is(this)) {
/*  74 */       return (BlockState)((BlockState)blockState1.setValue((Property)TYPE, (Comparable)SlabType.DOUBLE)).setValue((Property)WATERLOGGED, Boolean.valueOf(false));
/*     */     }
/*     */     
/*  77 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(blockPos);
/*  78 */     BlockState blockState2 = (BlockState)((BlockState)defaultBlockState().setValue((Property)TYPE, (Comparable)SlabType.BOTTOM)).setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/*     */     
/*  80 */     Direction direction = paramBlockPlaceContext.getClickedFace();
/*  81 */     if (direction == Direction.DOWN || (direction != Direction.UP && (paramBlockPlaceContext.getClickLocation()).y - blockPos.getY() > 0.5D)) {
/*  82 */       return (BlockState)blockState2.setValue((Property)TYPE, (Comparable)SlabType.TOP);
/*     */     }
/*  84 */     return blockState2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState paramBlockState, BlockPlaceContext paramBlockPlaceContext) {
/*  89 */     ItemStack itemStack = paramBlockPlaceContext.getItemInHand();
/*     */     
/*  91 */     SlabType slabType = (SlabType)paramBlockState.getValue((Property)TYPE);
/*  92 */     if (slabType == SlabType.DOUBLE || !itemStack.is(asItem())) {
/*  93 */       return false;
/*     */     }
/*     */     
/*  96 */     if (paramBlockPlaceContext.replacingClickedOnBlock()) {
/*  97 */       boolean bool = ((paramBlockPlaceContext.getClickLocation()).y - paramBlockPlaceContext.getClickedPos().getY() > 0.5D) ? true : false;
/*  98 */       Direction direction = paramBlockPlaceContext.getClickedFace();
/*  99 */       if (slabType == SlabType.BOTTOM) {
/* 100 */         return (direction == Direction.UP || (bool && direction.getAxis().isHorizontal()));
/*     */       }
/* 102 */       return (direction == Direction.DOWN || (!bool && direction.getAxis().isHorizontal()));
/*     */     } 
/*     */     
/* 105 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 110 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 111 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 113 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean placeLiquid(LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, BlockState paramBlockState, FluidState paramFluidState) {
/* 118 */     if (paramBlockState.getValue((Property)TYPE) != SlabType.DOUBLE) {
/* 119 */       return super.placeLiquid(paramLevelAccessor, paramBlockPos, paramBlockState, paramFluidState);
/*     */     }
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canPlaceLiquid(LivingEntity paramLivingEntity, BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState, Fluid paramFluid) {
/* 126 */     if (paramBlockState.getValue((Property)TYPE) != SlabType.DOUBLE) {
/* 127 */       return super.canPlaceLiquid(paramLivingEntity, paramBlockGetter, paramBlockPos, paramBlockState, paramFluid);
/*     */     }
/* 129 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 134 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 135 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/* 137 */     return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/* 142 */     switch (paramPathComputationType) {
/*     */       case LAND:
/* 144 */         return false;
/*     */       case WATER:
/* 146 */         return paramBlockState.getFluidState().is(FluidTags.WATER);
/*     */       case AIR:
/* 148 */         return false;
/*     */     } 
/* 150 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\SlabBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */