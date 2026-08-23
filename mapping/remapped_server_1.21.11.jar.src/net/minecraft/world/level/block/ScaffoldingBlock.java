/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.item.FallingBlockEntity;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ScaffoldingBlock extends Block implements SimpleWaterloggedBlock {
/*  26 */   public static final MapCodec<ScaffoldingBlock> CODEC = simpleCodec(ScaffoldingBlock::new);
/*     */   private static final int TICK_DELAY = 1;
/*     */   
/*     */   public MapCodec<ScaffoldingBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  35 */   private static final VoxelShape SHAPE_STABLE = Shapes.or(
/*  36 */       Block.column(16.0D, 14.0D, 16.0D), 
/*  37 */       Shapes.rotateHorizontal(Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 2.0D)).values().stream().reduce(Shapes.empty(), Shapes::or));
/*     */   
/*  39 */   private static final VoxelShape SHAPE_UNSTABLE_BOTTOM = Block.column(16.0D, 0.0D, 2.0D);
/*  40 */   private static final VoxelShape SHAPE_UNSTABLE = Shapes.or(SHAPE_STABLE, new VoxelShape[] { SHAPE_UNSTABLE_BOTTOM, 
/*     */ 
/*     */         
/*  43 */         Shapes.rotateHorizontal(Block.boxZ(16.0D, 0.0D, 2.0D, 0.0D, 2.0D)).values().stream().reduce(Shapes.empty(), Shapes::or) });
/*     */ 
/*     */   
/*  46 */   private static final VoxelShape SHAPE_BELOW_BLOCK = Shapes.block().move(0.0D, -1.0D, 0.0D).optimize();
/*     */   
/*     */   public static final int STABILITY_MAX_DISTANCE = 7;
/*  49 */   public static final IntegerProperty DISTANCE = BlockStateProperties.STABILITY_DISTANCE;
/*  50 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  51 */   public static final BooleanProperty BOTTOM = BlockStateProperties.BOTTOM;
/*     */   
/*     */   protected ScaffoldingBlock(BlockBehaviour.Properties paramProperties) {
/*  54 */     super(paramProperties);
/*  55 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)DISTANCE, Integer.valueOf(7))).setValue((Property)WATERLOGGED, Boolean.valueOf(false))).setValue((Property)BOTTOM, Boolean.valueOf(false)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/*  60 */     paramBuilder.add(new Property[] { (Property)DISTANCE, (Property)WATERLOGGED, (Property)BOTTOM });
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  65 */     if (!paramCollisionContext.isHoldingItem(paramBlockState.getBlock().asItem())) {
/*  66 */       return ((Boolean)paramBlockState.getValue((Property)BOTTOM)).booleanValue() ? SHAPE_UNSTABLE : SHAPE_STABLE;
/*     */     }
/*  68 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getInteractionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/*  73 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState paramBlockState, BlockPlaceContext paramBlockPlaceContext) {
/*  78 */     return paramBlockPlaceContext.getItemInHand().is(asItem());
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/*  83 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/*  84 */     Level level = paramBlockPlaceContext.getLevel();
/*     */     
/*  86 */     int i = getDistance((BlockGetter)level, blockPos);
/*  87 */     return (BlockState)((BlockState)((BlockState)defaultBlockState()
/*  88 */       .setValue((Property)WATERLOGGED, Boolean.valueOf((level.getFluidState(blockPos).getType() == Fluids.WATER))))
/*  89 */       .setValue((Property)DISTANCE, Integer.valueOf(i)))
/*  90 */       .setValue((Property)BOTTOM, Boolean.valueOf(isBottom((BlockGetter)level, blockPos, i)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/*  95 */     if (!paramLevel.isClientSide()) {
/*  96 */       paramLevel.scheduleTick(paramBlockPos, this, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 102 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 103 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/* 106 */     if (!paramLevelReader.isClientSide()) {
/* 107 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, this, 1);
/*     */     }
/*     */     
/* 110 */     return paramBlockState1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 115 */     int i = getDistance((BlockGetter)paramServerLevel, paramBlockPos);
/*     */ 
/*     */     
/* 118 */     BlockState blockState = (BlockState)((BlockState)paramBlockState.setValue((Property)DISTANCE, Integer.valueOf(i))).setValue((Property)BOTTOM, Boolean.valueOf(isBottom((BlockGetter)paramServerLevel, paramBlockPos, i)));
/*     */     
/* 120 */     if (((Integer)blockState.getValue((Property)DISTANCE)).intValue() == 7) {
/* 121 */       if (((Integer)paramBlockState.getValue((Property)DISTANCE)).intValue() == 7) {
/*     */         
/* 123 */         FallingBlockEntity.fall((Level)paramServerLevel, paramBlockPos, blockState);
/*     */       } else {
/*     */         
/* 126 */         paramServerLevel.destroyBlock(paramBlockPos, true);
/*     */       } 
/* 128 */     } else if (paramBlockState != blockState) {
/* 129 */       paramServerLevel.setBlock(paramBlockPos, blockState, 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 135 */     return (getDistance((BlockGetter)paramLevelReader, paramBlockPos) < 7);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 140 */     if (paramCollisionContext.isPlacement()) {
/* 141 */       return Shapes.empty();
/*     */     }
/*     */     
/* 144 */     if (!paramCollisionContext.isAbove(Shapes.block(), paramBlockPos, true) || paramCollisionContext.isDescending()) {
/* 145 */       if (((Integer)paramBlockState.getValue((Property)DISTANCE)).intValue() != 0 && ((Boolean)paramBlockState.getValue((Property)BOTTOM)).booleanValue() && paramCollisionContext.isAbove(SHAPE_BELOW_BLOCK, paramBlockPos, true)) {
/* 146 */         return SHAPE_UNSTABLE_BOTTOM;
/*     */       }
/* 148 */       return Shapes.empty();
/*     */     } 
/* 150 */     return SHAPE_STABLE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 155 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 156 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 158 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */   
/*     */   private boolean isBottom(BlockGetter paramBlockGetter, BlockPos paramBlockPos, int paramInt) {
/* 162 */     return (paramInt > 0 && !paramBlockGetter.getBlockState(paramBlockPos.below()).is(this));
/*     */   }
/*     */   
/*     */   public static int getDistance(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 166 */     BlockPos.MutableBlockPos mutableBlockPos = paramBlockPos.mutable().move(Direction.DOWN);
/* 167 */     BlockState blockState = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos);
/*     */     
/* 169 */     int i = 7;
/* 170 */     if (blockState.is(Blocks.SCAFFOLDING)) {
/* 171 */       i = ((Integer)blockState.getValue((Property)DISTANCE)).intValue();
/*     */     }
/* 173 */     else if (blockState.isFaceSturdy(paramBlockGetter, (BlockPos)mutableBlockPos, Direction.UP)) {
/* 174 */       return 0;
/*     */     } 
/*     */     
/* 177 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 178 */       BlockState blockState1 = paramBlockGetter.getBlockState((BlockPos)mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, direction));
/* 179 */       if (!blockState1.is(Blocks.SCAFFOLDING)) {
/*     */         continue;
/*     */       }
/*     */       
/* 183 */       i = Math.min(i, ((Integer)blockState1.getValue((Property)DISTANCE)).intValue() + 1);
/*     */       
/* 185 */       if (i == 1) {
/*     */         break;
/*     */       }
/*     */     } 
/* 189 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\ScaffoldingBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */