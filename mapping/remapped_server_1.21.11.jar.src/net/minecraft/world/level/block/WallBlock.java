/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
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
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.WallSide;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.phys.shapes.BooleanOp;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class WallBlock extends Block implements SimpleWaterloggedBlock {
/*  33 */   public static final MapCodec<WallBlock> CODEC = simpleCodec(WallBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<WallBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   public static final BooleanProperty UP = BlockStateProperties.UP;
/*  41 */   public static final EnumProperty<WallSide> EAST = BlockStateProperties.EAST_WALL;
/*  42 */   public static final EnumProperty<WallSide> NORTH = BlockStateProperties.NORTH_WALL;
/*  43 */   public static final EnumProperty<WallSide> SOUTH = BlockStateProperties.SOUTH_WALL;
/*  44 */   public static final EnumProperty<WallSide> WEST = BlockStateProperties.WEST_WALL;
/*  45 */   public static final Map<Direction, EnumProperty<WallSide>> PROPERTY_BY_DIRECTION = (Map<Direction, EnumProperty<WallSide>>)ImmutableMap.copyOf(Maps.newEnumMap(Map.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> collisionShapes;
/*  56 */   private static final VoxelShape TEST_SHAPE_POST = Block.column(2.0D, 0.0D, 16.0D);
/*  57 */   private static final Map<Direction, VoxelShape> TEST_SHAPES_WALL = Shapes.rotateHorizontal(Block.boxZ(2.0D, 16.0D, 0.0D, 9.0D));
/*     */   
/*     */   public WallBlock(BlockBehaviour.Properties paramProperties) {
/*  60 */     super(paramProperties);
/*  61 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)UP, Boolean.valueOf(true))).setValue((Property)NORTH, (Comparable)WallSide.NONE)).setValue((Property)EAST, (Comparable)WallSide.NONE)).setValue((Property)SOUTH, (Comparable)WallSide.NONE)).setValue((Property)WEST, (Comparable)WallSide.NONE)).setValue((Property)WATERLOGGED, Boolean.valueOf(false)));
/*     */     
/*  63 */     this.shapes = makeShapes(16.0F, 14.0F);
/*  64 */     this.collisionShapes = makeShapes(24.0F, 24.0F);
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes(float paramFloat1, float paramFloat2) {
/*  68 */     VoxelShape voxelShape = Block.column(8.0D, 0.0D, paramFloat1);
/*  69 */     byte b = 6;
/*  70 */     Map map1 = Shapes.rotateHorizontal(Block.boxZ(6.0D, 0.0D, paramFloat2, 0.0D, 11.0D));
/*  71 */     Map map2 = Shapes.rotateHorizontal(Block.boxZ(6.0D, 0.0D, paramFloat1, 0.0D, 11.0D));
/*     */     
/*  73 */     return getShapeForEachState(paramBlockState -> { VoxelShape voxelShape = ((Boolean)paramBlockState.getValue((Property)UP)).booleanValue() ? paramVoxelShape : Shapes.empty(); for (Map.Entry<Direction, EnumProperty<WallSide>> entry : PROPERTY_BY_DIRECTION.entrySet()) { switch ((WallSide)paramBlockState.getValue((Property)entry.getValue())) { default: throw new MatchException(null, null);case NONE: case LOW: case TALL: break; }  voxelShape = Shapes.or(voxelShape, (VoxelShape)paramMap2.get(entry.getKey())); }  return voxelShape; }(Property<?>[])new Property[] { (Property)WATERLOGGED });
/*     */   }
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
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  89 */     return this.shapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  94 */     return this.collisionShapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState paramBlockState, PathComputationType paramPathComputationType) {
/*  99 */     return false;
/*     */   }
/*     */   
/*     */   private boolean connectsTo(BlockState paramBlockState, boolean paramBoolean, Direction paramDirection) {
/* 103 */     Block block = paramBlockState.getBlock();
/*     */     
/* 105 */     boolean bool = (block instanceof FenceGateBlock && FenceGateBlock.connectsToDirection(paramBlockState, paramDirection)) ? true : false;
/* 106 */     return (paramBlockState.is(BlockTags.WALLS) || (!isExceptionForConnection(paramBlockState) && paramBoolean) || block instanceof IronBarsBlock || bool);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 111 */     Level level = paramBlockPlaceContext.getLevel();
/* 112 */     BlockPos blockPos1 = paramBlockPlaceContext.getClickedPos();
/* 113 */     FluidState fluidState = paramBlockPlaceContext.getLevel().getFluidState(paramBlockPlaceContext.getClickedPos());
/*     */     
/* 115 */     BlockPos blockPos2 = blockPos1.north();
/* 116 */     BlockPos blockPos3 = blockPos1.east();
/* 117 */     BlockPos blockPos4 = blockPos1.south();
/* 118 */     BlockPos blockPos5 = blockPos1.west();
/* 119 */     BlockPos blockPos6 = blockPos1.above();
/*     */     
/* 121 */     BlockState blockState1 = level.getBlockState(blockPos2);
/* 122 */     BlockState blockState2 = level.getBlockState(blockPos3);
/* 123 */     BlockState blockState3 = level.getBlockState(blockPos4);
/* 124 */     BlockState blockState4 = level.getBlockState(blockPos5);
/* 125 */     BlockState blockState5 = level.getBlockState(blockPos6);
/*     */     
/* 127 */     boolean bool1 = connectsTo(blockState1, blockState1.isFaceSturdy((BlockGetter)level, blockPos2, Direction.SOUTH), Direction.SOUTH);
/* 128 */     boolean bool2 = connectsTo(blockState2, blockState2.isFaceSturdy((BlockGetter)level, blockPos3, Direction.WEST), Direction.WEST);
/* 129 */     boolean bool3 = connectsTo(blockState3, blockState3.isFaceSturdy((BlockGetter)level, blockPos4, Direction.NORTH), Direction.NORTH);
/* 130 */     boolean bool4 = connectsTo(blockState4, blockState4.isFaceSturdy((BlockGetter)level, blockPos5, Direction.EAST), Direction.EAST);
/*     */     
/* 132 */     BlockState blockState6 = (BlockState)defaultBlockState().setValue((Property)WATERLOGGED, Boolean.valueOf((fluidState.getType() == Fluids.WATER)));
/* 133 */     return updateShape((LevelReader)level, blockState6, blockPos6, blockState5, bool1, bool2, bool3, bool4);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 138 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 139 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/* 142 */     if (paramDirection == Direction.DOWN) {
/* 143 */       return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */     }
/*     */     
/* 146 */     if (paramDirection == Direction.UP) {
/* 147 */       return topUpdate(paramLevelReader, paramBlockState1, paramBlockPos2, paramBlockState2);
/*     */     }
/*     */     
/* 150 */     return sideUpdate(paramLevelReader, paramBlockPos1, paramBlockState1, paramBlockPos2, paramBlockState2, paramDirection);
/*     */   }
/*     */   
/*     */   private static boolean isConnected(BlockState paramBlockState, Property<WallSide> paramProperty) {
/* 154 */     return (paramBlockState.getValue(paramProperty) != WallSide.NONE);
/*     */   }
/*     */   
/*     */   private static boolean isCovered(VoxelShape paramVoxelShape1, VoxelShape paramVoxelShape2) {
/* 158 */     return !Shapes.joinIsNotEmpty(paramVoxelShape2, paramVoxelShape1, BooleanOp.ONLY_FIRST);
/*     */   }
/*     */   
/*     */   private BlockState topUpdate(LevelReader paramLevelReader, BlockState paramBlockState1, BlockPos paramBlockPos, BlockState paramBlockState2) {
/* 162 */     boolean bool1 = isConnected(paramBlockState1, (Property<WallSide>)NORTH);
/* 163 */     boolean bool2 = isConnected(paramBlockState1, (Property<WallSide>)EAST);
/* 164 */     boolean bool3 = isConnected(paramBlockState1, (Property<WallSide>)SOUTH);
/* 165 */     boolean bool4 = isConnected(paramBlockState1, (Property<WallSide>)WEST);
/*     */     
/* 167 */     return updateShape(paramLevelReader, paramBlockState1, paramBlockPos, paramBlockState2, bool1, bool2, bool3, bool4);
/*     */   }
/*     */   
/*     */   private BlockState sideUpdate(LevelReader paramLevelReader, BlockPos paramBlockPos1, BlockState paramBlockState1, BlockPos paramBlockPos2, BlockState paramBlockState2, Direction paramDirection) {
/* 171 */     Direction direction = paramDirection.getOpposite();
/* 172 */     boolean bool1 = (paramDirection == Direction.NORTH) ? connectsTo(paramBlockState2, paramBlockState2.isFaceSturdy((BlockGetter)paramLevelReader, paramBlockPos2, direction), direction) : isConnected(paramBlockState1, (Property<WallSide>)NORTH);
/* 173 */     boolean bool2 = (paramDirection == Direction.EAST) ? connectsTo(paramBlockState2, paramBlockState2.isFaceSturdy((BlockGetter)paramLevelReader, paramBlockPos2, direction), direction) : isConnected(paramBlockState1, (Property<WallSide>)EAST);
/* 174 */     boolean bool3 = (paramDirection == Direction.SOUTH) ? connectsTo(paramBlockState2, paramBlockState2.isFaceSturdy((BlockGetter)paramLevelReader, paramBlockPos2, direction), direction) : isConnected(paramBlockState1, (Property<WallSide>)SOUTH);
/* 175 */     boolean bool4 = (paramDirection == Direction.WEST) ? connectsTo(paramBlockState2, paramBlockState2.isFaceSturdy((BlockGetter)paramLevelReader, paramBlockPos2, direction), direction) : isConnected(paramBlockState1, (Property<WallSide>)WEST);
/*     */     
/* 177 */     BlockPos blockPos = paramBlockPos1.above();
/* 178 */     BlockState blockState = paramLevelReader.getBlockState(blockPos);
/* 179 */     return updateShape(paramLevelReader, paramBlockState1, blockPos, blockState, bool1, bool2, bool3, bool4);
/*     */   }
/*     */   
/*     */   private BlockState updateShape(LevelReader paramLevelReader, BlockState paramBlockState1, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4) {
/* 183 */     VoxelShape voxelShape = paramBlockState2.getCollisionShape((BlockGetter)paramLevelReader, paramBlockPos).getFaceShape(Direction.DOWN);
/* 184 */     BlockState blockState = updateSides(paramBlockState1, paramBoolean1, paramBoolean2, paramBoolean3, paramBoolean4, voxelShape);
/*     */     
/* 186 */     return (BlockState)blockState.setValue((Property)UP, Boolean.valueOf(shouldRaisePost(blockState, paramBlockState2, voxelShape)));
/*     */   }
/*     */   
/*     */   private boolean shouldRaisePost(BlockState paramBlockState1, BlockState paramBlockState2, VoxelShape paramVoxelShape) {
/* 190 */     boolean bool1 = (paramBlockState2.getBlock() instanceof WallBlock && ((Boolean)paramBlockState2.getValue((Property)UP)).booleanValue()) ? true : false;
/* 191 */     if (bool1) {
/* 192 */       return true;
/*     */     }
/*     */     
/* 195 */     WallSide wallSide1 = (WallSide)paramBlockState1.getValue((Property)NORTH);
/* 196 */     WallSide wallSide2 = (WallSide)paramBlockState1.getValue((Property)SOUTH);
/* 197 */     WallSide wallSide3 = (WallSide)paramBlockState1.getValue((Property)EAST);
/* 198 */     WallSide wallSide4 = (WallSide)paramBlockState1.getValue((Property)WEST);
/*     */     
/* 200 */     boolean bool2 = (wallSide2 == WallSide.NONE) ? true : false;
/* 201 */     boolean bool3 = (wallSide4 == WallSide.NONE) ? true : false;
/* 202 */     boolean bool4 = (wallSide3 == WallSide.NONE) ? true : false;
/* 203 */     boolean bool5 = (wallSide1 == WallSide.NONE) ? true : false;
/*     */     
/* 205 */     boolean bool6 = ((bool5 && bool2 && bool3 && bool4) || bool5 != bool2 || bool3 != bool4) ? true : false;
/*     */ 
/*     */     
/* 208 */     if (bool6) {
/* 209 */       return true;
/*     */     }
/*     */     
/* 212 */     boolean bool7 = ((wallSide1 == WallSide.TALL && wallSide2 == WallSide.TALL) || (wallSide3 == WallSide.TALL && wallSide4 == WallSide.TALL)) ? true : false;
/*     */     
/* 214 */     if (bool7) {
/* 215 */       return false;
/*     */     }
/*     */     
/* 218 */     return (paramBlockState2.is(BlockTags.WALL_POST_OVERRIDE) || isCovered(paramVoxelShape, TEST_SHAPE_POST));
/*     */   }
/*     */   
/*     */   private BlockState updateSides(BlockState paramBlockState, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, boolean paramBoolean4, VoxelShape paramVoxelShape) {
/* 222 */     return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState
/* 223 */       .setValue((Property)NORTH, (Comparable)makeWallState(paramBoolean1, paramVoxelShape, TEST_SHAPES_WALL.get(Direction.NORTH))))
/* 224 */       .setValue((Property)EAST, (Comparable)makeWallState(paramBoolean2, paramVoxelShape, TEST_SHAPES_WALL.get(Direction.EAST))))
/* 225 */       .setValue((Property)SOUTH, (Comparable)makeWallState(paramBoolean3, paramVoxelShape, TEST_SHAPES_WALL.get(Direction.SOUTH))))
/* 226 */       .setValue((Property)WEST, (Comparable)makeWallState(paramBoolean4, paramVoxelShape, TEST_SHAPES_WALL.get(Direction.WEST)));
/*     */   }
/*     */   
/*     */   private WallSide makeWallState(boolean paramBoolean, VoxelShape paramVoxelShape1, VoxelShape paramVoxelShape2) {
/* 230 */     if (paramBoolean) {
/* 231 */       if (isCovered(paramVoxelShape1, paramVoxelShape2)) {
/* 232 */         return WallSide.TALL;
/*     */       }
/* 234 */       return WallSide.LOW;
/*     */     } 
/*     */     
/* 237 */     return WallSide.NONE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 243 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 244 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 246 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/* 251 */     return !((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 256 */     paramBuilder.add(new Property[] { (Property)UP, (Property)NORTH, (Property)EAST, (Property)WEST, (Property)SOUTH, (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 261 */     switch (paramRotation) {
/*     */       case NONE:
/* 263 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)SOUTH))).setValue((Property)EAST, paramBlockState.getValue((Property)WEST))).setValue((Property)SOUTH, paramBlockState.getValue((Property)NORTH))).setValue((Property)WEST, paramBlockState.getValue((Property)EAST));
/*     */       case LOW:
/* 265 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)EAST))).setValue((Property)EAST, paramBlockState.getValue((Property)SOUTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)WEST))).setValue((Property)WEST, paramBlockState.getValue((Property)NORTH));
/*     */       case TALL:
/* 267 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)WEST))).setValue((Property)EAST, paramBlockState.getValue((Property)NORTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)EAST))).setValue((Property)WEST, paramBlockState.getValue((Property)SOUTH));
/*     */     } 
/* 269 */     return paramBlockState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 275 */     switch (paramMirror) {
/*     */       case NONE:
/* 277 */         return (BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)SOUTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)NORTH));
/*     */       case LOW:
/* 279 */         return (BlockState)((BlockState)paramBlockState.setValue((Property)EAST, paramBlockState.getValue((Property)WEST))).setValue((Property)WEST, paramBlockState.getValue((Property)EAST));
/*     */     } 
/*     */ 
/*     */     
/* 283 */     return super.mirror(paramBlockState, paramMirror);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\WallBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */