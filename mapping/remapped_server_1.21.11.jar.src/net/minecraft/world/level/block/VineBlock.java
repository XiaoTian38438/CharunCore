/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class VineBlock extends Block {
/*  26 */   public static final MapCodec<VineBlock> CODEC = simpleCodec(VineBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<VineBlock> codec() {
/*  30 */     return CODEC;
/*     */   }
/*     */   
/*  33 */   public static final BooleanProperty UP = PipeBlock.UP;
/*  34 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/*  35 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/*  36 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/*  37 */   public static final BooleanProperty WEST = PipeBlock.WEST; public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION;
/*     */   static {
/*  39 */     PROPERTY_BY_DIRECTION = (Map<Direction, BooleanProperty>)PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(paramEntry -> (paramEntry.getKey() != Direction.DOWN)).collect(Util.toMap());
/*     */   }
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   public VineBlock(BlockBehaviour.Properties paramProperties) {
/*  44 */     super(paramProperties);
/*  45 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)UP, Boolean.valueOf(false))).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false)));
/*     */     
/*  47 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  51 */     Map map = Shapes.rotateAll(Block.boxZ(16.0D, 0.0D, 1.0D));
/*     */     
/*  53 */     return getShapeForEachState(paramBlockState -> {
/*     */           VoxelShape voxelShape = Shapes.empty();
/*     */           for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) {
/*     */             if (((Boolean)paramBlockState.getValue((Property)entry.getValue())).booleanValue()) {
/*     */               voxelShape = Shapes.or(voxelShape, (VoxelShape)paramMap.get(entry.getKey()));
/*     */             }
/*     */           } 
/*     */           return voxelShape.isEmpty() ? Shapes.block() : voxelShape;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/*  68 */     return this.shapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState paramBlockState) {
/*  73 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/*  78 */     return hasFaces(getUpdatedState(paramBlockState, (BlockGetter)paramLevelReader, paramBlockPos));
/*     */   }
/*     */   
/*     */   private boolean hasFaces(BlockState paramBlockState) {
/*  82 */     return (countFaces(paramBlockState) > 0);
/*     */   }
/*     */   
/*     */   private int countFaces(BlockState paramBlockState) {
/*  86 */     byte b = 0;
/*  87 */     for (BooleanProperty booleanProperty : PROPERTY_BY_DIRECTION.values()) {
/*  88 */       if (((Boolean)paramBlockState.getValue((Property)booleanProperty)).booleanValue()) {
/*  89 */         b++;
/*     */       }
/*     */     } 
/*     */     
/*  93 */     return b;
/*     */   }
/*     */   
/*     */   private boolean canSupportAtFace(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*  97 */     if (paramDirection == Direction.DOWN) {
/*  98 */       return false;
/*     */     }
/*     */     
/* 101 */     BlockPos blockPos = paramBlockPos.relative(paramDirection);
/* 102 */     if (isAcceptableNeighbour(paramBlockGetter, blockPos, paramDirection)) {
/* 103 */       return true;
/*     */     }
/*     */     
/* 106 */     if (paramDirection.getAxis() != Direction.Axis.Y) {
/*     */       
/* 108 */       BooleanProperty booleanProperty = PROPERTY_BY_DIRECTION.get(paramDirection);
/* 109 */       BlockState blockState = paramBlockGetter.getBlockState(paramBlockPos.above());
/* 110 */       return (blockState.is(this) && ((Boolean)blockState.getValue((Property)booleanProperty)).booleanValue());
/*     */     } 
/* 112 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isAcceptableNeighbour(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 116 */     return MultifaceBlock.canAttachTo(paramBlockGetter, paramDirection, paramBlockPos, paramBlockGetter.getBlockState(paramBlockPos));
/*     */   }
/*     */   
/*     */   private BlockState getUpdatedState(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 120 */     BlockPos blockPos = paramBlockPos.above();
/* 121 */     if (((Boolean)paramBlockState.getValue((Property)UP)).booleanValue()) {
/* 122 */       paramBlockState = (BlockState)paramBlockState.setValue((Property)UP, Boolean.valueOf(isAcceptableNeighbour(paramBlockGetter, blockPos, Direction.DOWN)));
/*     */     }
/*     */ 
/*     */     
/* 126 */     BlockState blockState = null;
/* 127 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 128 */       BooleanProperty booleanProperty = getPropertyForFace(direction);
/*     */       
/* 130 */       if (((Boolean)paramBlockState.getValue((Property)booleanProperty)).booleanValue()) {
/* 131 */         boolean bool = canSupportAtFace(paramBlockGetter, paramBlockPos, direction);
/* 132 */         if (!bool) {
/* 133 */           if (blockState == null) {
/* 134 */             blockState = paramBlockGetter.getBlockState(blockPos);
/*     */           }
/* 136 */           bool = (blockState.is(this) && ((Boolean)blockState.getValue((Property)booleanProperty)).booleanValue());
/*     */         } 
/* 138 */         paramBlockState = (BlockState)paramBlockState.setValue((Property)booleanProperty, Boolean.valueOf(bool));
/*     */       } 
/*     */     } 
/* 141 */     return paramBlockState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 146 */     if (paramDirection == Direction.DOWN) {
/* 147 */       return super.updateShape(paramBlockState1, paramLevelReader, paramScheduledTickAccess, paramBlockPos1, paramDirection, paramBlockPos2, paramBlockState2, paramRandomSource);
/*     */     }
/*     */     
/* 150 */     BlockState blockState = getUpdatedState(paramBlockState1, (BlockGetter)paramLevelReader, paramBlockPos1);
/*     */     
/* 152 */     if (!hasFaces(blockState)) {
/* 153 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 156 */     return blockState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void randomTick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 161 */     if (!((Boolean)paramServerLevel.getGameRules().get(GameRules.SPREAD_VINES)).booleanValue()) {
/*     */       return;
/*     */     }
/* 164 */     if (paramRandomSource.nextInt(4) != 0) {
/*     */       return;
/*     */     }
/*     */     
/* 168 */     Direction direction = Direction.getRandom(paramRandomSource);
/*     */     
/* 170 */     BlockPos blockPos = paramBlockPos.above();
/* 171 */     if (direction.getAxis().isHorizontal() && !((Boolean)paramBlockState.getValue((Property)getPropertyForFace(direction))).booleanValue()) {
/* 172 */       if (!canSpread((BlockGetter)paramServerLevel, paramBlockPos)) {
/*     */         return;
/*     */       }
/*     */       
/* 176 */       BlockPos blockPos1 = paramBlockPos.relative(direction);
/*     */       
/* 178 */       BlockState blockState = paramServerLevel.getBlockState(blockPos1);
/* 179 */       if (blockState.isAir()) {
/*     */         
/* 181 */         Direction direction1 = direction.getClockWise();
/* 182 */         Direction direction2 = direction.getCounterClockWise();
/*     */ 
/*     */         
/* 185 */         boolean bool1 = ((Boolean)paramBlockState.getValue((Property)getPropertyForFace(direction1))).booleanValue();
/* 186 */         boolean bool2 = ((Boolean)paramBlockState.getValue((Property)getPropertyForFace(direction2))).booleanValue();
/*     */         
/* 188 */         BlockPos blockPos2 = blockPos1.relative(direction1);
/* 189 */         BlockPos blockPos3 = blockPos1.relative(direction2);
/*     */         
/* 191 */         if (bool1 && isAcceptableNeighbour((BlockGetter)paramServerLevel, blockPos2, direction1)) {
/* 192 */           paramServerLevel.setBlock(blockPos1, (BlockState)defaultBlockState().setValue((Property)getPropertyForFace(direction1), Boolean.valueOf(true)), 2);
/* 193 */         } else if (bool2 && isAcceptableNeighbour((BlockGetter)paramServerLevel, blockPos3, direction2)) {
/* 194 */           paramServerLevel.setBlock(blockPos1, (BlockState)defaultBlockState().setValue((Property)getPropertyForFace(direction2), Boolean.valueOf(true)), 2);
/*     */         } else {
/*     */           
/* 197 */           Direction direction3 = direction.getOpposite();
/* 198 */           if (bool1 && paramServerLevel.isEmptyBlock(blockPos2) && isAcceptableNeighbour((BlockGetter)paramServerLevel, paramBlockPos.relative(direction1), direction3)) {
/* 199 */             paramServerLevel.setBlock(blockPos2, (BlockState)defaultBlockState().setValue((Property)getPropertyForFace(direction3), Boolean.valueOf(true)), 2);
/* 200 */           } else if (bool2 && paramServerLevel.isEmptyBlock(blockPos3) && isAcceptableNeighbour((BlockGetter)paramServerLevel, paramBlockPos.relative(direction2), direction3)) {
/* 201 */             paramServerLevel.setBlock(blockPos3, (BlockState)defaultBlockState().setValue((Property)getPropertyForFace(direction3), Boolean.valueOf(true)), 2);
/*     */           
/*     */           }
/* 204 */           else if (paramRandomSource.nextFloat() < 0.05D && isAcceptableNeighbour((BlockGetter)paramServerLevel, blockPos1.above(), Direction.UP)) {
/* 205 */             paramServerLevel.setBlock(blockPos1, (BlockState)defaultBlockState().setValue((Property)UP, Boolean.valueOf(true)), 2);
/*     */           }
/*     */         
/*     */         } 
/* 209 */       } else if (isAcceptableNeighbour((BlockGetter)paramServerLevel, blockPos1, direction)) {
/*     */         
/* 211 */         paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)getPropertyForFace(direction), Boolean.valueOf(true)), 2);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 216 */     if (direction == Direction.UP && paramBlockPos.getY() < paramServerLevel.getMaxY()) {
/* 217 */       if (canSupportAtFace((BlockGetter)paramServerLevel, paramBlockPos, direction)) {
/* 218 */         paramServerLevel.setBlock(paramBlockPos, (BlockState)paramBlockState.setValue((Property)UP, Boolean.valueOf(true)), 2);
/*     */         return;
/*     */       } 
/* 221 */       if (paramServerLevel.isEmptyBlock(blockPos)) {
/* 222 */         if (!canSpread((BlockGetter)paramServerLevel, paramBlockPos)) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 227 */         BlockState blockState = paramBlockState;
/* 228 */         for (Direction direction1 : Direction.Plane.HORIZONTAL) {
/* 229 */           if (paramRandomSource.nextBoolean() || !isAcceptableNeighbour((BlockGetter)paramServerLevel, blockPos.relative(direction1), direction1)) {
/* 230 */             blockState = (BlockState)blockState.setValue((Property)getPropertyForFace(direction1), Boolean.valueOf(false));
/*     */           }
/*     */         } 
/* 233 */         if (hasHorizontalConnection(blockState)) {
/* 234 */           paramServerLevel.setBlock(blockPos, blockState, 2);
/*     */         }
/*     */         return;
/*     */       } 
/*     */     } 
/* 239 */     if (paramBlockPos.getY() > paramServerLevel.getMinY()) {
/*     */       
/* 241 */       BlockPos blockPos1 = paramBlockPos.below();
/* 242 */       BlockState blockState = paramServerLevel.getBlockState(blockPos1);
/*     */       
/* 244 */       if (blockState.isAir() || blockState.is(this)) {
/* 245 */         BlockState blockState1 = blockState.isAir() ? defaultBlockState() : blockState;
/* 246 */         BlockState blockState2 = copyRandomFaces(paramBlockState, blockState1, paramRandomSource);
/* 247 */         if (blockState1 != blockState2 && hasHorizontalConnection(blockState2)) {
/* 248 */           paramServerLevel.setBlock(blockPos1, blockState2, 2);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private BlockState copyRandomFaces(BlockState paramBlockState1, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 255 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 256 */       if (paramRandomSource.nextBoolean()) {
/* 257 */         BooleanProperty booleanProperty = getPropertyForFace(direction);
/* 258 */         if (((Boolean)paramBlockState1.getValue((Property)booleanProperty)).booleanValue()) {
/* 259 */           paramBlockState2 = (BlockState)paramBlockState2.setValue((Property)booleanProperty, Boolean.valueOf(true));
/*     */         }
/*     */       } 
/*     */     } 
/* 263 */     return paramBlockState2;
/*     */   }
/*     */   
/*     */   private boolean hasHorizontalConnection(BlockState paramBlockState) {
/* 267 */     return (((Boolean)paramBlockState.getValue((Property)NORTH)).booleanValue() || ((Boolean)paramBlockState.getValue((Property)EAST)).booleanValue() || ((Boolean)paramBlockState.getValue((Property)SOUTH)).booleanValue() || ((Boolean)paramBlockState.getValue((Property)WEST)).booleanValue());
/*     */   }
/*     */   
/*     */   private boolean canSpread(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 271 */     byte b1 = 4;
/*     */     
/* 273 */     Iterable iterable = BlockPos.betweenClosed(paramBlockPos
/* 274 */         .getX() - 4, paramBlockPos.getY() - 1, paramBlockPos.getZ() - 4, paramBlockPos
/* 275 */         .getX() + 4, paramBlockPos.getY() + 1, paramBlockPos.getZ() + 4);
/*     */ 
/*     */     
/* 278 */     byte b2 = 5;
/* 279 */     for (BlockPos blockPos : iterable) {
/* 280 */       if (paramBlockGetter.getBlockState(blockPos).is(this) && 
/* 281 */         --b2 <= 0) {
/* 282 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 286 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState paramBlockState, BlockPlaceContext paramBlockPlaceContext) {
/* 291 */     BlockState blockState = paramBlockPlaceContext.getLevel().getBlockState(paramBlockPlaceContext.getClickedPos());
/* 292 */     if (blockState.is(this)) {
/* 293 */       return (countFaces(blockState) < PROPERTY_BY_DIRECTION.size());
/*     */     }
/*     */     
/* 296 */     return super.canBeReplaced(paramBlockState, paramBlockPlaceContext);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 301 */     BlockState blockState1 = paramBlockPlaceContext.getLevel().getBlockState(paramBlockPlaceContext.getClickedPos());
/* 302 */     boolean bool = blockState1.is(this);
/* 303 */     BlockState blockState2 = bool ? blockState1 : defaultBlockState();
/*     */     
/* 305 */     for (Direction direction : paramBlockPlaceContext.getNearestLookingDirections()) {
/* 306 */       if (direction != Direction.DOWN) {
/* 307 */         BooleanProperty booleanProperty = getPropertyForFace(direction);
/* 308 */         boolean bool1 = (bool && ((Boolean)blockState1.getValue((Property)booleanProperty)).booleanValue()) ? true : false;
/* 309 */         if (!bool1 && canSupportAtFace((BlockGetter)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos(), direction)) {
/* 310 */           return (BlockState)blockState2.setValue((Property)booleanProperty, Boolean.valueOf(true));
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 315 */     return bool ? blockState2 : null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 320 */     paramBuilder.add(new Property[] { (Property)UP, (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST });
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 325 */     switch (paramRotation) {
/*     */       case LEFT_RIGHT:
/* 327 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)SOUTH))).setValue((Property)EAST, paramBlockState.getValue((Property)WEST))).setValue((Property)SOUTH, paramBlockState.getValue((Property)NORTH))).setValue((Property)WEST, paramBlockState.getValue((Property)EAST));
/*     */       case FRONT_BACK:
/* 329 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)EAST))).setValue((Property)EAST, paramBlockState.getValue((Property)SOUTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)WEST))).setValue((Property)WEST, paramBlockState.getValue((Property)NORTH));
/*     */       case null:
/* 331 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)WEST))).setValue((Property)EAST, paramBlockState.getValue((Property)NORTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)EAST))).setValue((Property)WEST, paramBlockState.getValue((Property)SOUTH));
/*     */     } 
/* 333 */     return paramBlockState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 339 */     switch (paramMirror) {
/*     */       case LEFT_RIGHT:
/* 341 */         return (BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)SOUTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)NORTH));
/*     */       case FRONT_BACK:
/* 343 */         return (BlockState)((BlockState)paramBlockState.setValue((Property)EAST, paramBlockState.getValue((Property)WEST))).setValue((Property)WEST, paramBlockState.getValue((Property)EAST));
/*     */     } 
/*     */ 
/*     */     
/* 347 */     return super.mirror(paramBlockState, paramMirror);
/*     */   }
/*     */   
/*     */   public static BooleanProperty getPropertyForFace(Direction paramDirection) {
/* 351 */     return PROPERTY_BY_DIRECTION.get(paramDirection);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\VineBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */