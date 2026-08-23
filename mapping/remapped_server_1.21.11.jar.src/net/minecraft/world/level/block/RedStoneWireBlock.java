/*     */ package net.minecraft.world.level.block;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.particles.DustParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.flag.FeatureFlags;
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
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RedstoneSide;
/*     */ import net.minecraft.world.level.redstone.DefaultRedstoneWireEvaluator;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneWireEvaluator;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.level.redstone.RedstoneWireEvaluator;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class RedStoneWireBlock extends Block {
/*  45 */   public static final MapCodec<RedStoneWireBlock> CODEC = simpleCodec(RedStoneWireBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<RedStoneWireBlock> codec() {
/*  49 */     return CODEC;
/*     */   }
/*     */   
/*  52 */   public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.NORTH_REDSTONE;
/*  53 */   public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.EAST_REDSTONE;
/*  54 */   public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.SOUTH_REDSTONE;
/*  55 */   public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.WEST_REDSTONE;
/*  56 */   public static final IntegerProperty POWER = BlockStateProperties.POWER;
/*     */   
/*  58 */   public static final Map<Direction, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION = (Map<Direction, EnumProperty<RedstoneSide>>)ImmutableMap.copyOf(Maps.newEnumMap(Map.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST)));
/*     */   private static final int[] COLORS;
/*     */   private static final float PARTICLE_DENSITY = 0.2F;
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   private final BlockState crossState;
/*     */   
/*     */   static {
/*  65 */     COLORS = (int[])Util.make(new int[16], paramArrayOfint -> {
/*     */           for (byte b = 0; b <= 15; b++) {
/*     */             float f1 = b / 15.0F;
/*     */             float f2 = f1 * 0.6F + ((f1 > 0.0F) ? 0.4F : 0.3F);
/*     */             float f3 = Mth.clamp(f1 * f1 * 0.7F - 0.5F, 0.0F, 1.0F);
/*     */             float f4 = Mth.clamp(f1 * f1 * 0.6F - 0.7F, 0.0F, 1.0F);
/*     */             paramArrayOfint[b] = ARGB.colorFromFloat(1.0F, f2, f3, f4);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  80 */   private final RedstoneWireEvaluator evaluator = (RedstoneWireEvaluator)new DefaultRedstoneWireEvaluator(this);
/*     */   private boolean shouldSignal = true;
/*     */   
/*     */   public RedStoneWireBlock(BlockBehaviour.Properties paramProperties) {
/*  84 */     super(paramProperties);
/*  85 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)NORTH, (Comparable)RedstoneSide.NONE)).setValue((Property)EAST, (Comparable)RedstoneSide.NONE)).setValue((Property)SOUTH, (Comparable)RedstoneSide.NONE)).setValue((Property)WEST, (Comparable)RedstoneSide.NONE)).setValue((Property)POWER, Integer.valueOf(0)));
/*     */     
/*  87 */     this.shapes = makeShapes();
/*     */     
/*  89 */     this.crossState = (BlockState)((BlockState)((BlockState)((BlockState)defaultBlockState().setValue((Property)NORTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)EAST, (Comparable)RedstoneSide.SIDE)).setValue((Property)SOUTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)WEST, (Comparable)RedstoneSide.SIDE);
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  93 */     boolean bool = true;
/*  94 */     byte b = 10;
/*     */     
/*  96 */     VoxelShape voxelShape = Block.column(10.0D, 0.0D, 1.0D);
/*  97 */     Map map1 = Shapes.rotateHorizontal(Block.boxZ(10.0D, 0.0D, 1.0D, 0.0D, 8.0D));
/*  98 */     Map map2 = Shapes.rotateHorizontal(Block.boxZ(10.0D, 16.0D, 0.0D, 1.0D));
/*     */     
/* 100 */     return getShapeForEachState(paramBlockState -> { VoxelShape voxelShape = paramVoxelShape; for (Map.Entry<Direction, EnumProperty<RedstoneSide>> entry : PROPERTY_BY_DIRECTION.entrySet()) { switch ((RedstoneSide)paramBlockState.getValue((Property)entry.getValue())) { default: throw new MatchException(null, null);case LEFT_RIGHT: case FRONT_BACK: case null: break; }  voxelShape = voxelShape; }  return voxelShape; }(Property<?>[])new Property[] { (Property)POWER });
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
/* 116 */     return this.shapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 121 */     return getConnectionState((BlockGetter)paramBlockPlaceContext.getLevel(), this.crossState, paramBlockPlaceContext.getClickedPos());
/*     */   }
/*     */   
/*     */   private BlockState getConnectionState(BlockGetter paramBlockGetter, BlockState paramBlockState, BlockPos paramBlockPos) {
/* 125 */     boolean bool1 = isDot(paramBlockState);
/* 126 */     paramBlockState = getMissingConnections(paramBlockGetter, (BlockState)defaultBlockState().setValue((Property)POWER, paramBlockState.getValue((Property)POWER)), paramBlockPos);
/*     */ 
/*     */     
/* 129 */     if (bool1 && isDot(paramBlockState)) {
/* 130 */       return paramBlockState;
/*     */     }
/*     */     
/* 133 */     boolean bool2 = ((RedstoneSide)paramBlockState.getValue((Property)NORTH)).isConnected();
/* 134 */     boolean bool3 = ((RedstoneSide)paramBlockState.getValue((Property)SOUTH)).isConnected();
/* 135 */     boolean bool4 = ((RedstoneSide)paramBlockState.getValue((Property)EAST)).isConnected();
/* 136 */     boolean bool5 = ((RedstoneSide)paramBlockState.getValue((Property)WEST)).isConnected();
/* 137 */     boolean bool6 = (!bool2 && !bool3) ? true : false;
/* 138 */     boolean bool7 = (!bool4 && !bool5) ? true : false;
/*     */     
/* 140 */     if (!bool5 && bool6) {
/* 141 */       paramBlockState = (BlockState)paramBlockState.setValue((Property)WEST, (Comparable)RedstoneSide.SIDE);
/*     */     }
/* 143 */     if (!bool4 && bool6) {
/* 144 */       paramBlockState = (BlockState)paramBlockState.setValue((Property)EAST, (Comparable)RedstoneSide.SIDE);
/*     */     }
/* 146 */     if (!bool2 && bool7) {
/* 147 */       paramBlockState = (BlockState)paramBlockState.setValue((Property)NORTH, (Comparable)RedstoneSide.SIDE);
/*     */     }
/* 149 */     if (!bool3 && bool7) {
/* 150 */       paramBlockState = (BlockState)paramBlockState.setValue((Property)SOUTH, (Comparable)RedstoneSide.SIDE);
/*     */     }
/* 152 */     return paramBlockState;
/*     */   }
/*     */   
/*     */   private BlockState getMissingConnections(BlockGetter paramBlockGetter, BlockState paramBlockState, BlockPos paramBlockPos) {
/* 156 */     boolean bool = !paramBlockGetter.getBlockState(paramBlockPos.above()).isRedstoneConductor(paramBlockGetter, paramBlockPos) ? true : false;
/* 157 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 158 */       if (!((RedstoneSide)paramBlockState.getValue((Property)PROPERTY_BY_DIRECTION.get(direction))).isConnected()) {
/* 159 */         RedstoneSide redstoneSide = getConnectingSide(paramBlockGetter, paramBlockPos, direction, bool);
/* 160 */         paramBlockState = (BlockState)paramBlockState.setValue((Property)PROPERTY_BY_DIRECTION.get(direction), (Comparable)redstoneSide);
/*     */       } 
/*     */     } 
/* 163 */     return paramBlockState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 168 */     if (paramDirection == Direction.DOWN) {
/* 169 */       if (!canSurviveOn((BlockGetter)paramLevelReader, paramBlockPos2, paramBlockState2)) {
/* 170 */         return Blocks.AIR.defaultBlockState();
/*     */       }
/* 172 */       return paramBlockState1;
/*     */     } 
/* 174 */     if (paramDirection == Direction.UP) {
/* 175 */       return getConnectionState((BlockGetter)paramLevelReader, paramBlockState1, paramBlockPos1);
/*     */     }
/*     */     
/* 178 */     RedstoneSide redstoneSide = getConnectingSide((BlockGetter)paramLevelReader, paramBlockPos1, paramDirection);
/* 179 */     if (redstoneSide.isConnected() == ((RedstoneSide)paramBlockState1.getValue((Property)PROPERTY_BY_DIRECTION.get(paramDirection))).isConnected() && !isCross(paramBlockState1)) {
/* 180 */       return (BlockState)paramBlockState1.setValue((Property)PROPERTY_BY_DIRECTION.get(paramDirection), (Comparable)redstoneSide);
/*     */     }
/* 182 */     return getConnectionState((BlockGetter)paramLevelReader, (BlockState)((BlockState)this.crossState.setValue((Property)POWER, paramBlockState1.getValue((Property)POWER))).setValue((Property)PROPERTY_BY_DIRECTION.get(paramDirection), (Comparable)redstoneSide), paramBlockPos1);
/*     */   }
/*     */   
/*     */   private static boolean isCross(BlockState paramBlockState) {
/* 186 */     return (((RedstoneSide)paramBlockState.getValue((Property)NORTH)).isConnected() && ((RedstoneSide)paramBlockState.getValue((Property)SOUTH)).isConnected() && ((RedstoneSide)paramBlockState.getValue((Property)EAST)).isConnected() && ((RedstoneSide)paramBlockState.getValue((Property)WEST)).isConnected());
/*     */   }
/*     */   
/*     */   private static boolean isDot(BlockState paramBlockState) {
/* 190 */     return (!((RedstoneSide)paramBlockState.getValue((Property)NORTH)).isConnected() && !((RedstoneSide)paramBlockState.getValue((Property)SOUTH)).isConnected() && !((RedstoneSide)paramBlockState.getValue((Property)EAST)).isConnected() && !((RedstoneSide)paramBlockState.getValue((Property)WEST)).isConnected());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateIndirectNeighbourShapes(BlockState paramBlockState, LevelAccessor paramLevelAccessor, BlockPos paramBlockPos, @UpdateFlags int paramInt1, int paramInt2) {
/* 195 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 196 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 197 */       RedstoneSide redstoneSide = (RedstoneSide)paramBlockState.getValue((Property)PROPERTY_BY_DIRECTION.get(direction));
/* 198 */       if (redstoneSide != RedstoneSide.NONE && !paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, direction)).is(this)) {
/* 199 */         mutableBlockPos.move(Direction.DOWN);
/* 200 */         BlockState blockState1 = paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos);
/* 201 */         if (blockState1.is(this)) {
/* 202 */           BlockPos blockPos = mutableBlockPos.relative(direction.getOpposite());
/* 203 */           paramLevelAccessor.neighborShapeChanged(direction.getOpposite(), (BlockPos)mutableBlockPos, blockPos, paramLevelAccessor.getBlockState(blockPos), paramInt1, paramInt2);
/*     */         } 
/*     */         
/* 206 */         mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, direction).move(Direction.UP);
/* 207 */         BlockState blockState2 = paramLevelAccessor.getBlockState((BlockPos)mutableBlockPos);
/* 208 */         if (blockState2.is(this)) {
/* 209 */           BlockPos blockPos = mutableBlockPos.relative(direction.getOpposite());
/* 210 */           paramLevelAccessor.neighborShapeChanged(direction.getOpposite(), (BlockPos)mutableBlockPos, blockPos, paramLevelAccessor.getBlockState(blockPos), paramInt1, paramInt2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private RedstoneSide getConnectingSide(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 217 */     return getConnectingSide(paramBlockGetter, paramBlockPos, paramDirection, !paramBlockGetter.getBlockState(paramBlockPos.above()).isRedstoneConductor(paramBlockGetter, paramBlockPos));
/*     */   }
/*     */   
/*     */   private RedstoneSide getConnectingSide(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection, boolean paramBoolean) {
/* 221 */     BlockPos blockPos = paramBlockPos.relative(paramDirection);
/* 222 */     BlockState blockState = paramBlockGetter.getBlockState(blockPos);
/* 223 */     if (paramBoolean) {
/*     */       
/* 225 */       boolean bool = (blockState.getBlock() instanceof TrapDoorBlock || canSurviveOn(paramBlockGetter, blockPos, blockState)) ? true : false;
/* 226 */       if (bool && shouldConnectTo(paramBlockGetter.getBlockState(blockPos.above()))) {
/*     */ 
/*     */         
/* 229 */         if (blockState.isFaceSturdy(paramBlockGetter, blockPos, paramDirection.getOpposite())) {
/* 230 */           return RedstoneSide.UP;
/*     */         }
/* 232 */         return RedstoneSide.SIDE;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 237 */     if (shouldConnectTo(blockState, paramDirection) || (!blockState.isRedstoneConductor(paramBlockGetter, blockPos) && shouldConnectTo(paramBlockGetter.getBlockState(blockPos.below())))) {
/* 238 */       return RedstoneSide.SIDE;
/*     */     }
/* 240 */     return RedstoneSide.NONE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 245 */     BlockPos blockPos = paramBlockPos.below();
/* 246 */     BlockState blockState = paramLevelReader.getBlockState(blockPos);
/* 247 */     return canSurviveOn((BlockGetter)paramLevelReader, blockPos, blockState);
/*     */   }
/*     */   
/*     */   private boolean canSurviveOn(BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 251 */     return (paramBlockState.isFaceSturdy(paramBlockGetter, paramBlockPos, Direction.UP) || paramBlockState.is(Blocks.HOPPER));
/*     */   }
/*     */   
/*     */   private void updatePowerStrength(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, Orientation paramOrientation, boolean paramBoolean) {
/* 255 */     if (useExperimentalEvaluator(paramLevel)) {
/* 256 */       (new ExperimentalRedstoneWireEvaluator(this)).updatePowerStrength(paramLevel, paramBlockPos, paramBlockState, paramOrientation, paramBoolean);
/*     */     } else {
/* 258 */       this.evaluator.updatePowerStrength(paramLevel, paramBlockPos, paramBlockState, paramOrientation, paramBoolean);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBlockSignal(Level paramLevel, BlockPos paramBlockPos) {
/* 266 */     this.shouldSignal = false;
/* 267 */     int i = paramLevel.getBestNeighborSignal(paramBlockPos);
/* 268 */     this.shouldSignal = true;
/* 269 */     return i;
/*     */   }
/*     */   
/*     */   private void checkCornerChangeAt(Level paramLevel, BlockPos paramBlockPos) {
/* 273 */     if (!paramLevel.getBlockState(paramBlockPos).is(this)) {
/*     */       return;
/*     */     }
/*     */     
/* 277 */     paramLevel.updateNeighborsAt(paramBlockPos, this);
/* 278 */     for (Direction direction : Direction.values()) {
/* 279 */       paramLevel.updateNeighborsAt(paramBlockPos.relative(direction), this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 285 */     if (paramBlockState2.is(paramBlockState1.getBlock()) || paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*     */     
/* 289 */     updatePowerStrength(paramLevel, paramBlockPos, paramBlockState1, (Orientation)null, true);
/*     */     
/* 291 */     for (Direction direction : Direction.Plane.VERTICAL) {
/* 292 */       paramLevel.updateNeighborsAt(paramBlockPos.relative(direction), this);
/*     */     }
/*     */     
/* 295 */     updateNeighborsOfNeighboringWires(paramLevel, paramBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, boolean paramBoolean) {
/* 300 */     if (paramBoolean) {
/*     */       return;
/*     */     }
/* 303 */     for (Direction direction : Direction.values()) {
/* 304 */       paramServerLevel.updateNeighborsAt(paramBlockPos.relative(direction), this);
/*     */     }
/* 306 */     updatePowerStrength((Level)paramServerLevel, paramBlockPos, paramBlockState, (Orientation)null, false);
/*     */     
/* 308 */     updateNeighborsOfNeighboringWires((Level)paramServerLevel, paramBlockPos);
/*     */   }
/*     */   
/*     */   private void updateNeighborsOfNeighboringWires(Level paramLevel, BlockPos paramBlockPos) {
/* 312 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 313 */       checkCornerChangeAt(paramLevel, paramBlockPos.relative(direction));
/*     */     }
/*     */     
/* 316 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 317 */       BlockPos blockPos = paramBlockPos.relative(direction);
/*     */       
/* 319 */       if (paramLevel.getBlockState(blockPos).isRedstoneConductor((BlockGetter)paramLevel, blockPos)) {
/* 320 */         checkCornerChangeAt(paramLevel, blockPos.above()); continue;
/*     */       } 
/* 322 */       checkCornerChangeAt(paramLevel, blockPos.below());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Block paramBlock, Orientation paramOrientation, boolean paramBoolean) {
/* 329 */     if (paramLevel.isClientSide()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 334 */     if (paramBlock == this && useExperimentalEvaluator(paramLevel)) {
/*     */       return;
/*     */     }
/*     */     
/* 338 */     if (paramBlockState.canSurvive((LevelReader)paramLevel, paramBlockPos)) {
/* 339 */       updatePowerStrength(paramLevel, paramBlockPos, paramBlockState, paramOrientation, false);
/*     */     } else {
/* 341 */       dropResources(paramBlockState, paramLevel, paramBlockPos);
/* 342 */       paramLevel.removeBlock(paramBlockPos, false);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean useExperimentalEvaluator(Level paramLevel) {
/* 347 */     return paramLevel.enabledFeatures().contains(FeatureFlags.REDSTONE_EXPERIMENTS);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getDirectSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 352 */     if (!this.shouldSignal) {
/* 353 */       return 0;
/*     */     }
/* 355 */     return paramBlockState.getSignal(paramBlockGetter, paramBlockPos, paramDirection);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getSignal(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 360 */     if (!this.shouldSignal || paramDirection == Direction.DOWN) {
/* 361 */       return 0;
/*     */     }
/* 363 */     int i = ((Integer)paramBlockState.getValue((Property)POWER)).intValue();
/* 364 */     if (i == 0) {
/* 365 */       return 0;
/*     */     }
/*     */     
/* 368 */     if (paramDirection == Direction.UP || ((RedstoneSide)getConnectionState(paramBlockGetter, paramBlockState, paramBlockPos).getValue((Property)PROPERTY_BY_DIRECTION.get(paramDirection.getOpposite()))).isConnected()) {
/* 369 */       return i;
/*     */     }
/* 371 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean shouldConnectTo(BlockState paramBlockState) {
/* 376 */     return shouldConnectTo(paramBlockState, (Direction)null);
/*     */   }
/*     */   
/*     */   protected static boolean shouldConnectTo(BlockState paramBlockState, Direction paramDirection) {
/* 380 */     if (paramBlockState.is(Blocks.REDSTONE_WIRE)) {
/* 381 */       return true;
/*     */     }
/*     */     
/* 384 */     if (paramBlockState.is(Blocks.REPEATER)) {
/* 385 */       Direction direction = (Direction)paramBlockState.getValue((Property)RepeaterBlock.FACING);
/* 386 */       return (direction == paramDirection || direction.getOpposite() == paramDirection);
/*     */     } 
/*     */     
/* 389 */     if (paramBlockState.is(Blocks.OBSERVER)) {
/* 390 */       return (paramDirection == paramBlockState.getValue((Property)ObserverBlock.FACING));
/*     */     }
/*     */     
/* 393 */     return (paramBlockState.isSignalSource() && paramDirection != null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isSignalSource(BlockState paramBlockState) {
/* 398 */     return this.shouldSignal;
/*     */   }
/*     */   
/*     */   public static int getColorForPower(int paramInt) {
/* 402 */     return COLORS[paramInt];
/*     */   }
/*     */   
/*     */   private static void spawnParticlesAlongLine(Level paramLevel, RandomSource paramRandomSource, BlockPos paramBlockPos, int paramInt, Direction paramDirection1, Direction paramDirection2, float paramFloat1, float paramFloat2) {
/* 406 */     float f1 = paramFloat2 - paramFloat1;
/* 407 */     if (paramRandomSource.nextFloat() >= 0.2F * f1) {
/*     */       return;
/*     */     }
/* 410 */     float f2 = 0.4375F;
/* 411 */     float f3 = paramFloat1 + f1 * paramRandomSource.nextFloat();
/* 412 */     double d1 = 0.5D + (0.4375F * paramDirection1.getStepX()) + (f3 * paramDirection2.getStepX());
/* 413 */     double d2 = 0.5D + (0.4375F * paramDirection1.getStepY()) + (f3 * paramDirection2.getStepY());
/* 414 */     double d3 = 0.5D + (0.4375F * paramDirection1.getStepZ()) + (f3 * paramDirection2.getStepZ());
/* 415 */     paramLevel.addParticle((ParticleOptions)new DustParticleOptions(paramInt, 1.0F), paramBlockPos.getX() + d1, paramBlockPos.getY() + d2, paramBlockPos.getZ() + d3, 0.0D, 0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void animateTick(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 420 */     int i = ((Integer)paramBlockState.getValue((Property)POWER)).intValue();
/* 421 */     if (i == 0) {
/*     */       return;
/*     */     }
/* 424 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 425 */       RedstoneSide redstoneSide = (RedstoneSide)paramBlockState.getValue((Property)PROPERTY_BY_DIRECTION.get(direction));
/* 426 */       switch (redstoneSide) {
/*     */         case LEFT_RIGHT:
/* 428 */           spawnParticlesAlongLine(paramLevel, paramRandomSource, paramBlockPos, COLORS[i], direction, Direction.UP, -0.5F, 0.5F);
/*     */         
/*     */         case FRONT_BACK:
/* 431 */           spawnParticlesAlongLine(paramLevel, paramRandomSource, paramBlockPos, COLORS[i], Direction.DOWN, direction, 0.0F, 0.5F);
/*     */           continue;
/*     */       } 
/*     */       
/* 435 */       spawnParticlesAlongLine(paramLevel, paramRandomSource, paramBlockPos, COLORS[i], Direction.DOWN, direction, 0.0F, 0.3F);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 442 */     switch (paramRotation) {
/*     */       case LEFT_RIGHT:
/* 444 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)SOUTH))).setValue((Property)EAST, paramBlockState.getValue((Property)WEST))).setValue((Property)SOUTH, paramBlockState.getValue((Property)NORTH))).setValue((Property)WEST, paramBlockState.getValue((Property)EAST));
/*     */       case FRONT_BACK:
/* 446 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)EAST))).setValue((Property)EAST, paramBlockState.getValue((Property)SOUTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)WEST))).setValue((Property)WEST, paramBlockState.getValue((Property)NORTH));
/*     */       case null:
/* 448 */         return (BlockState)((BlockState)((BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)WEST))).setValue((Property)EAST, paramBlockState.getValue((Property)NORTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)EAST))).setValue((Property)WEST, paramBlockState.getValue((Property)SOUTH));
/*     */     } 
/* 450 */     return paramBlockState;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 456 */     switch (paramMirror) {
/*     */       case LEFT_RIGHT:
/* 458 */         return (BlockState)((BlockState)paramBlockState.setValue((Property)NORTH, paramBlockState.getValue((Property)SOUTH))).setValue((Property)SOUTH, paramBlockState.getValue((Property)NORTH));
/*     */       case FRONT_BACK:
/* 460 */         return (BlockState)((BlockState)paramBlockState.setValue((Property)EAST, paramBlockState.getValue((Property)WEST))).setValue((Property)WEST, paramBlockState.getValue((Property)EAST));
/*     */     } 
/*     */ 
/*     */     
/* 464 */     return super.mirror(paramBlockState, paramMirror);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 469 */     paramBuilder.add(new Property[] { (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST, (Property)POWER });
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState paramBlockState, Level paramLevel, BlockPos paramBlockPos, Player paramPlayer, BlockHitResult paramBlockHitResult) {
/* 474 */     if (!(paramPlayer.getAbilities()).mayBuild) {
/* 475 */       return (InteractionResult)InteractionResult.PASS;
/*     */     }
/*     */     
/* 478 */     if (isCross(paramBlockState) || isDot(paramBlockState)) {
/* 479 */       BlockState blockState = isCross(paramBlockState) ? defaultBlockState() : this.crossState;
/* 480 */       blockState = (BlockState)blockState.setValue((Property)POWER, paramBlockState.getValue((Property)POWER));
/* 481 */       blockState = getConnectionState((BlockGetter)paramLevel, blockState, paramBlockPos);
/* 482 */       if (blockState != paramBlockState) {
/* 483 */         paramLevel.setBlock(paramBlockPos, blockState, 3);
/*     */         
/* 485 */         updatesOnShapeChange(paramLevel, paramBlockPos, paramBlockState, blockState);
/* 486 */         return (InteractionResult)InteractionResult.SUCCESS;
/*     */       } 
/*     */     } 
/* 489 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */   
/*     */   private void updatesOnShapeChange(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState1, BlockState paramBlockState2) {
/* 493 */     Orientation orientation = ExperimentalRedstoneUtils.initialOrientation(paramLevel, null, Direction.UP);
/* 494 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 495 */       BlockPos blockPos = paramBlockPos.relative(direction);
/* 496 */       if (((RedstoneSide)paramBlockState1.getValue((Property)PROPERTY_BY_DIRECTION.get(direction))).isConnected() != ((RedstoneSide)paramBlockState2.getValue((Property)PROPERTY_BY_DIRECTION.get(direction))).isConnected() && paramLevel.getBlockState(blockPos).isRedstoneConductor((BlockGetter)paramLevel, blockPos))
/* 497 */         paramLevel.updateNeighborsAtExceptFromFacing(blockPos, paramBlockState2.getBlock(), direction.getOpposite(), ExperimentalRedstoneUtils.withFront(orientation, direction)); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\RedStoneWireBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */