/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
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
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.Fluid;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultifaceBlock
/*     */   extends Block
/*     */   implements SimpleWaterloggedBlock
/*     */ {
/*  39 */   public static final MapCodec<MultifaceBlock> CODEC = simpleCodec(MultifaceBlock::new);
/*     */ 
/*     */   
/*     */   protected MapCodec<? extends MultifaceBlock> codec() {
/*  43 */     return CODEC;
/*     */   }
/*     */   
/*  46 */   public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*     */   
/*  48 */   private static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION;
/*     */   
/*  50 */   protected static final Direction[] DIRECTIONS = Direction.values();
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   private final boolean canRotate;
/*     */   private final boolean canMirrorX;
/*     */   private final boolean canMirrorZ;
/*     */   
/*     */   public MultifaceBlock(BlockBehaviour.Properties paramProperties) {
/*  59 */     super(paramProperties);
/*  60 */     registerDefaultState(getDefaultMultifaceState(this.stateDefinition));
/*  61 */     this.shapes = makeShapes();
/*     */     
/*  63 */     this.canRotate = Direction.Plane.HORIZONTAL.stream().allMatch(this::isFaceSupported);
/*  64 */     this.canMirrorX = (Direction.Plane.HORIZONTAL.stream().filter((Predicate)Direction.Axis.X).filter(this::isFaceSupported).count() % 2L == 0L);
/*  65 */     this.canMirrorZ = (Direction.Plane.HORIZONTAL.stream().filter((Predicate)Direction.Axis.Z).filter(this::isFaceSupported).count() % 2L == 0L);
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  69 */     Map map = Shapes.rotateAll(Block.boxZ(16.0D, 0.0D, 1.0D));
/*     */     
/*  71 */     return getShapeForEachState(paramBlockState -> { VoxelShape voxelShape = Shapes.empty(); for (Direction direction : DIRECTIONS) { if (hasFace(paramBlockState, direction)) voxelShape = Shapes.or(voxelShape, (VoxelShape)paramMap.get(direction));  }  return voxelShape.isEmpty() ? Shapes.block() : voxelShape; }(Property<?>[])new Property[] { (Property)WATERLOGGED });
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
/*     */   public static Set<Direction> availableFaces(BlockState paramBlockState) {
/*  83 */     if (!(paramBlockState.getBlock() instanceof MultifaceBlock)) {
/*  84 */       return Set.of();
/*     */     }
/*  86 */     EnumSet<Direction> enumSet = EnumSet.noneOf(Direction.class);
/*  87 */     for (Direction direction : Direction.values()) {
/*  88 */       if (hasFace(paramBlockState, direction)) {
/*  89 */         enumSet.add(direction);
/*     */       }
/*     */     } 
/*  92 */     return enumSet;
/*     */   }
/*     */   
/*     */   public static Set<Direction> unpack(byte paramByte) {
/*  96 */     EnumSet<Direction> enumSet = EnumSet.noneOf(Direction.class);
/*  97 */     for (Direction direction : Direction.values()) {
/*  98 */       if ((paramByte & (byte)(1 << direction.ordinal())) > 0) {
/*  99 */         enumSet.add(direction);
/*     */       }
/*     */     } 
/* 102 */     return enumSet;
/*     */   }
/*     */   
/*     */   public static byte pack(Collection<Direction> paramCollection) {
/* 106 */     byte b = 0;
/* 107 */     for (Direction direction : paramCollection) {
/* 108 */       b = (byte)(b | 1 << direction.ordinal());
/*     */     }
/* 110 */     return b;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isFaceSupported(Direction paramDirection) {
/* 115 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 120 */     for (Direction direction : DIRECTIONS) {
/* 121 */       if (isFaceSupported(direction)) {
/* 122 */         paramBuilder.add(new Property[] { (Property)getFaceProperty(direction) });
/*     */       }
/*     */     } 
/* 125 */     paramBuilder.add(new Property[] { (Property)WATERLOGGED });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/* 133 */     if (((Boolean)paramBlockState1.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 134 */       paramScheduledTickAccess.scheduleTick(paramBlockPos1, (Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(paramLevelReader));
/*     */     }
/*     */     
/* 137 */     if (!hasAnyFace(paramBlockState1)) {
/* 138 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 141 */     if (!hasFace(paramBlockState1, paramDirection) || canAttachTo((BlockGetter)paramLevelReader, paramDirection, paramBlockPos2, paramBlockState2)) {
/* 142 */       return paramBlockState1;
/*     */     }
/* 144 */     return removeFace(paramBlockState1, getFaceProperty(paramDirection));
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState paramBlockState) {
/* 149 */     if (((Boolean)paramBlockState.getValue((Property)WATERLOGGED)).booleanValue()) {
/* 150 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 152 */     return super.getFluidState(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 157 */     return this.shapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 162 */     boolean bool = false;
/* 163 */     for (Direction direction : DIRECTIONS) {
/* 164 */       if (hasFace(paramBlockState, direction)) {
/*     */ 
/*     */         
/* 167 */         if (!canAttachTo((BlockGetter)paramLevelReader, paramBlockPos, direction)) {
/* 168 */           return false;
/*     */         }
/* 170 */         bool = true;
/*     */       } 
/* 172 */     }  return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBeReplaced(BlockState paramBlockState, BlockPlaceContext paramBlockPlaceContext) {
/* 177 */     return (!paramBlockPlaceContext.getItemInHand().is(asItem()) || hasAnyVacantFace(paramBlockState));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 182 */     Level level = paramBlockPlaceContext.getLevel();
/* 183 */     BlockPos blockPos = paramBlockPlaceContext.getClickedPos();
/* 184 */     BlockState blockState = level.getBlockState(blockPos);
/* 185 */     return Arrays.<Direction>stream(paramBlockPlaceContext.getNearestLookingDirections())
/* 186 */       .map(paramDirection -> getStateForPlacement(paramBlockState, (BlockGetter)paramLevel, paramBlockPos, paramDirection))
/* 187 */       .filter(Objects::nonNull)
/* 188 */       .findFirst()
/* 189 */       .orElse(null);
/*     */   }
/*     */   
/*     */   public boolean isValidStateForPlacement(BlockGetter paramBlockGetter, BlockState paramBlockState, BlockPos paramBlockPos, Direction paramDirection) {
/* 193 */     if (!isFaceSupported(paramDirection) || (paramBlockState.is(this) && hasFace(paramBlockState, paramDirection))) {
/* 194 */       return false;
/*     */     }
/* 196 */     BlockPos blockPos = paramBlockPos.relative(paramDirection);
/* 197 */     return canAttachTo(paramBlockGetter, paramDirection, blockPos, paramBlockGetter.getBlockState(blockPos));
/*     */   }
/*     */   public BlockState getStateForPlacement(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/*     */     BlockState blockState;
/* 201 */     if (!isValidStateForPlacement(paramBlockGetter, paramBlockState, paramBlockPos, paramDirection)) {
/* 202 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 206 */     if (paramBlockState.is(this)) {
/*     */       
/* 208 */       blockState = paramBlockState;
/* 209 */     } else if (paramBlockState.getFluidState().isSourceOfType((Fluid)Fluids.WATER)) {
/* 210 */       blockState = (BlockState)defaultBlockState().setValue((Property)BlockStateProperties.WATERLOGGED, Boolean.valueOf(true));
/*     */     } else {
/* 212 */       blockState = defaultBlockState();
/*     */     } 
/*     */     
/* 215 */     return (BlockState)blockState.setValue((Property)getFaceProperty(paramDirection), Boolean.valueOf(true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState paramBlockState, Rotation paramRotation) {
/* 220 */     if (!this.canRotate) {
/* 221 */       return paramBlockState;
/*     */     }
/*     */     
/* 224 */     Objects.requireNonNull(paramRotation); return mapDirections(paramBlockState, paramRotation::rotate);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState paramBlockState, Mirror paramMirror) {
/* 229 */     if (paramMirror == Mirror.FRONT_BACK && !this.canMirrorX) {
/* 230 */       return paramBlockState;
/*     */     }
/* 232 */     if (paramMirror == Mirror.LEFT_RIGHT && !this.canMirrorZ) {
/* 233 */       return paramBlockState;
/*     */     }
/*     */     
/* 236 */     Objects.requireNonNull(paramMirror); return mapDirections(paramBlockState, paramMirror::mirror);
/*     */   }
/*     */   
/*     */   private BlockState mapDirections(BlockState paramBlockState, Function<Direction, Direction> paramFunction) {
/* 240 */     BlockState blockState = paramBlockState;
/* 241 */     for (Direction direction : DIRECTIONS) {
/* 242 */       if (isFaceSupported(direction)) {
/* 243 */         blockState = (BlockState)blockState.setValue((Property)getFaceProperty(paramFunction.apply(direction)), paramBlockState.getValue((Property)getFaceProperty(direction)));
/*     */       }
/*     */     } 
/* 246 */     return blockState;
/*     */   }
/*     */   
/*     */   public static boolean hasFace(BlockState paramBlockState, Direction paramDirection) {
/* 250 */     BooleanProperty booleanProperty = getFaceProperty(paramDirection);
/* 251 */     return ((Boolean)paramBlockState.getValueOrElse((Property)booleanProperty, Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */   
/*     */   public static boolean canAttachTo(BlockGetter paramBlockGetter, BlockPos paramBlockPos, Direction paramDirection) {
/* 255 */     BlockPos blockPos = paramBlockPos.relative(paramDirection);
/* 256 */     BlockState blockState = paramBlockGetter.getBlockState(blockPos);
/* 257 */     return canAttachTo(paramBlockGetter, paramDirection, blockPos, blockState);
/*     */   }
/*     */   
/*     */   public static boolean canAttachTo(BlockGetter paramBlockGetter, Direction paramDirection, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 261 */     return (Block.isFaceFull(paramBlockState.getBlockSupportShape(paramBlockGetter, paramBlockPos), paramDirection.getOpposite()) || 
/* 262 */       Block.isFaceFull(paramBlockState.getCollisionShape(paramBlockGetter, paramBlockPos), paramDirection.getOpposite()));
/*     */   }
/*     */   
/*     */   private static BlockState removeFace(BlockState paramBlockState, BooleanProperty paramBooleanProperty) {
/* 266 */     BlockState blockState = (BlockState)paramBlockState.setValue((Property)paramBooleanProperty, Boolean.valueOf(false));
/* 267 */     if (hasAnyFace(blockState)) {
/* 268 */       return blockState;
/*     */     }
/*     */     
/* 271 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */   
/*     */   public static BooleanProperty getFaceProperty(Direction paramDirection) {
/* 275 */     return PROPERTY_BY_DIRECTION.get(paramDirection);
/*     */   }
/*     */   
/*     */   private static BlockState getDefaultMultifaceState(StateDefinition<Block, BlockState> paramStateDefinition) {
/* 279 */     BlockState blockState = (BlockState)((BlockState)paramStateDefinition.any()).setValue((Property)WATERLOGGED, Boolean.valueOf(false));
/* 280 */     for (BooleanProperty booleanProperty : PROPERTY_BY_DIRECTION.values()) {
/* 281 */       blockState = (BlockState)blockState.trySetValue((Property)booleanProperty, Boolean.valueOf(false));
/*     */     }
/* 283 */     return blockState;
/*     */   }
/*     */   
/*     */   protected static boolean hasAnyFace(BlockState paramBlockState) {
/* 287 */     for (Direction direction : DIRECTIONS) {
/* 288 */       if (hasFace(paramBlockState, direction)) {
/* 289 */         return true;
/*     */       }
/*     */     } 
/* 292 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean hasAnyVacantFace(BlockState paramBlockState) {
/* 296 */     for (Direction direction : DIRECTIONS) {
/* 297 */       if (!hasFace(paramBlockState, direction)) {
/* 298 */         return true;
/*     */       }
/*     */     } 
/* 301 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\MultifaceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */