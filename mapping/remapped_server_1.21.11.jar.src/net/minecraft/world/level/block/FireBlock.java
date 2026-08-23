/*     */ package net.minecraft.world.level.block;
/*     */ 
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
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
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FireBlock extends BaseFireBlock {
/*  32 */   public static final MapCodec<FireBlock> CODEC = simpleCodec(FireBlock::new);
/*     */   public static final int MAX_AGE = 15;
/*     */   
/*     */   public MapCodec<FireBlock> codec() {
/*  36 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*  40 */   public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
/*     */   
/*  42 */   public static final BooleanProperty NORTH = PipeBlock.NORTH;
/*  43 */   public static final BooleanProperty EAST = PipeBlock.EAST;
/*  44 */   public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
/*  45 */   public static final BooleanProperty WEST = PipeBlock.WEST;
/*  46 */   public static final BooleanProperty UP = PipeBlock.UP; public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION; private final Function<BlockState, VoxelShape> shapes; private static final int IGNITE_INSTANT = 60; private static final int IGNITE_EASY = 30;
/*     */   private static final int IGNITE_MEDIUM = 15;
/*     */   
/*     */   static {
/*  50 */     PROPERTY_BY_DIRECTION = (Map<Direction, BooleanProperty>)PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(paramEntry -> (paramEntry.getKey() != Direction.DOWN)).collect(Util.toMap());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int IGNITE_HARD = 5;
/*     */ 
/*     */   
/*     */   private static final int BURN_INSTANT = 100;
/*     */ 
/*     */   
/*     */   private static final int BURN_EASY = 60;
/*     */   
/*     */   private static final int BURN_MEDIUM = 20;
/*     */   
/*     */   private static final int BURN_HARD = 5;
/*     */   
/*  67 */   private final Object2IntMap<Block> igniteOdds = (Object2IntMap<Block>)new Object2IntOpenHashMap();
/*  68 */   private final Object2IntMap<Block> burnOdds = (Object2IntMap<Block>)new Object2IntOpenHashMap();
/*     */   
/*     */   public FireBlock(BlockBehaviour.Properties paramProperties) {
/*  71 */     super(paramProperties, 1.0F);
/*  72 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)AGE, Integer.valueOf(0))).setValue((Property)NORTH, Boolean.valueOf(false))).setValue((Property)EAST, Boolean.valueOf(false))).setValue((Property)SOUTH, Boolean.valueOf(false))).setValue((Property)WEST, Boolean.valueOf(false))).setValue((Property)UP, Boolean.valueOf(false)));
/*     */     
/*  74 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  78 */     Map map = Shapes.rotateAll(Block.boxZ(16.0D, 0.0D, 1.0D));
/*     */     
/*  80 */     return getShapeForEachState(paramBlockState -> { VoxelShape voxelShape = Shapes.empty(); for (Map.Entry<Direction, BooleanProperty> entry : PROPERTY_BY_DIRECTION.entrySet()) { if (((Boolean)paramBlockState.getValue((Property)entry.getValue())).booleanValue()) voxelShape = Shapes.or(voxelShape, (VoxelShape)paramMap.get(entry.getKey()));  }  return voxelShape.isEmpty() ? SHAPE : voxelShape; }(Property<?>[])new Property[] { (Property)AGE });
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
/*     */   protected BlockState updateShape(BlockState paramBlockState1, LevelReader paramLevelReader, ScheduledTickAccess paramScheduledTickAccess, BlockPos paramBlockPos1, Direction paramDirection, BlockPos paramBlockPos2, BlockState paramBlockState2, RandomSource paramRandomSource) {
/*  95 */     if (canSurvive(paramBlockState1, paramLevelReader, paramBlockPos1)) {
/*  96 */       return getStateWithAge(paramLevelReader, paramBlockPos1, ((Integer)paramBlockState1.getValue((Property)AGE)).intValue());
/*     */     }
/*     */     
/*  99 */     return Blocks.AIR.defaultBlockState();
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState paramBlockState, BlockGetter paramBlockGetter, BlockPos paramBlockPos, CollisionContext paramCollisionContext) {
/* 104 */     return this.shapes.apply(paramBlockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext paramBlockPlaceContext) {
/* 109 */     return getStateForPlacement((BlockGetter)paramBlockPlaceContext.getLevel(), paramBlockPlaceContext.getClickedPos());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BlockState getStateForPlacement(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 115 */     BlockPos blockPos = paramBlockPos.below();
/* 116 */     BlockState blockState1 = paramBlockGetter.getBlockState(blockPos);
/* 117 */     if (canBurn(blockState1) || blockState1.isFaceSturdy(paramBlockGetter, blockPos, Direction.UP)) {
/* 118 */       return defaultBlockState();
/*     */     }
/*     */     
/* 121 */     BlockState blockState2 = defaultBlockState();
/* 122 */     for (Direction direction : Direction.values()) {
/* 123 */       BooleanProperty booleanProperty = PROPERTY_BY_DIRECTION.get(direction);
/* 124 */       if (booleanProperty != null) {
/* 125 */         blockState2 = (BlockState)blockState2.setValue((Property)booleanProperty, Boolean.valueOf(canBurn(paramBlockGetter.getBlockState(paramBlockPos.relative(direction)))));
/*     */       }
/*     */     } 
/*     */     
/* 129 */     return blockState2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState paramBlockState, LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 134 */     BlockPos blockPos = paramBlockPos.below();
/* 135 */     return (paramLevelReader.getBlockState(blockPos).isFaceSturdy((BlockGetter)paramLevelReader, blockPos, Direction.UP) || isValidFireLocation((BlockGetter)paramLevelReader, paramBlockPos));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void tick(BlockState paramBlockState, ServerLevel paramServerLevel, BlockPos paramBlockPos, RandomSource paramRandomSource) {
/* 141 */     paramServerLevel.scheduleTick(paramBlockPos, this, getFireTickDelay(paramServerLevel.random));
/*     */     
/* 143 */     if (!paramServerLevel.canSpreadFireAround(paramBlockPos)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 148 */     if (!paramBlockState.canSurvive((LevelReader)paramServerLevel, paramBlockPos)) {
/* 149 */       paramServerLevel.removeBlock(paramBlockPos, false);
/*     */     }
/*     */     
/* 152 */     BlockState blockState = paramServerLevel.getBlockState(paramBlockPos.below());
/* 153 */     boolean bool1 = blockState.is(paramServerLevel.dimensionType().infiniburn());
/*     */     
/* 155 */     int i = ((Integer)paramBlockState.getValue((Property)AGE)).intValue();
/* 156 */     if (!bool1 && paramServerLevel.isRaining() && isNearRain((Level)paramServerLevel, paramBlockPos) && paramRandomSource.nextFloat() < 0.2F + i * 0.03F) {
/* 157 */       paramServerLevel.removeBlock(paramBlockPos, false);
/*     */       
/*     */       return;
/*     */     } 
/* 161 */     int j = Math.min(15, i + paramRandomSource.nextInt(3) / 2);
/* 162 */     if (i != j) {
/* 163 */       paramBlockState = (BlockState)paramBlockState.setValue((Property)AGE, Integer.valueOf(j));
/* 164 */       paramServerLevel.setBlock(paramBlockPos, paramBlockState, 260);
/*     */     } 
/*     */     
/* 167 */     if (!bool1) {
/* 168 */       if (!isValidFireLocation((BlockGetter)paramServerLevel, paramBlockPos)) {
/* 169 */         BlockPos blockPos = paramBlockPos.below();
/* 170 */         if (!paramServerLevel.getBlockState(blockPos).isFaceSturdy((BlockGetter)paramServerLevel, blockPos, Direction.UP) || i > 3) {
/* 171 */           paramServerLevel.removeBlock(paramBlockPos, false);
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 177 */       if (i == 15 && paramRandomSource.nextInt(4) == 0 && !canBurn(paramServerLevel.getBlockState(paramBlockPos.below()))) {
/* 178 */         paramServerLevel.removeBlock(paramBlockPos, false);
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 183 */     boolean bool2 = ((Boolean)paramServerLevel.environmentAttributes().getValue(EnvironmentAttributes.INCREASED_FIRE_BURNOUT, paramBlockPos)).booleanValue();
/* 184 */     byte b = bool2 ? -50 : 0;
/*     */     
/* 186 */     checkBurnOut((Level)paramServerLevel, paramBlockPos.east(), 300 + b, paramRandomSource, i);
/* 187 */     checkBurnOut((Level)paramServerLevel, paramBlockPos.west(), 300 + b, paramRandomSource, i);
/* 188 */     checkBurnOut((Level)paramServerLevel, paramBlockPos.below(), 250 + b, paramRandomSource, i);
/* 189 */     checkBurnOut((Level)paramServerLevel, paramBlockPos.above(), 250 + b, paramRandomSource, i);
/* 190 */     checkBurnOut((Level)paramServerLevel, paramBlockPos.north(), 300 + b, paramRandomSource, i);
/* 191 */     checkBurnOut((Level)paramServerLevel, paramBlockPos.south(), 300 + b, paramRandomSource, i);
/*     */     
/* 193 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 194 */     for (byte b1 = -1; b1 <= 1; b1++) {
/* 195 */       for (byte b2 = -1; b2 <= 1; b2++) {
/* 196 */         for (byte b3 = -1; b3 <= 4; b3++) {
/* 197 */           if (b1 != 0 || b3 != 0 || b2 != 0) {
/*     */ 
/*     */ 
/*     */             
/* 201 */             int k = 100;
/* 202 */             if (b3 > 1) {
/* 203 */               k += (b3 - 1) * 100;
/*     */             }
/*     */             
/* 206 */             mutableBlockPos.setWithOffset((Vec3i)paramBlockPos, b1, b3, b2);
/* 207 */             int m = getIgniteOdds((LevelReader)paramServerLevel, (BlockPos)mutableBlockPos);
/* 208 */             if (m > 0) {
/*     */ 
/*     */ 
/*     */               
/* 212 */               int n = (m + 40 + paramServerLevel.getDifficulty().getId() * 7) / (i + 30);
/* 213 */               if (bool2) {
/* 214 */                 n /= 2;
/*     */               }
/* 216 */               if (n > 0 && paramRandomSource.nextInt(k) <= n && (
/* 217 */                 !paramServerLevel.isRaining() || !isNearRain((Level)paramServerLevel, (BlockPos)mutableBlockPos))) {
/*     */ 
/*     */ 
/*     */                 
/* 221 */                 int i1 = Math.min(15, i + paramRandomSource.nextInt(5) / 4);
/* 222 */                 paramServerLevel.setBlock((BlockPos)mutableBlockPos, getStateWithAge((LevelReader)paramServerLevel, (BlockPos)mutableBlockPos, i1), 3);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   } protected boolean isNearRain(Level paramLevel, BlockPos paramBlockPos) {
/* 230 */     return (paramLevel.isRainingAt(paramBlockPos) || paramLevel.isRainingAt(paramBlockPos.west()) || paramLevel.isRainingAt(paramBlockPos.east()) || paramLevel.isRainingAt(paramBlockPos.north()) || paramLevel.isRainingAt(paramBlockPos.south()));
/*     */   }
/*     */   
/*     */   private int getBurnOdds(BlockState paramBlockState) {
/* 234 */     if (paramBlockState.hasProperty((Property)BlockStateProperties.WATERLOGGED) && ((Boolean)paramBlockState.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue()) {
/* 235 */       return 0;
/*     */     }
/* 237 */     return this.burnOdds.getInt(paramBlockState.getBlock());
/*     */   }
/*     */   
/*     */   private int getIgniteOdds(BlockState paramBlockState) {
/* 241 */     if (paramBlockState.hasProperty((Property)BlockStateProperties.WATERLOGGED) && ((Boolean)paramBlockState.getValue((Property)BlockStateProperties.WATERLOGGED)).booleanValue()) {
/* 242 */       return 0;
/*     */     }
/* 244 */     return this.igniteOdds.getInt(paramBlockState.getBlock());
/*     */   }
/*     */   
/*     */   private void checkBurnOut(Level paramLevel, BlockPos paramBlockPos, int paramInt1, RandomSource paramRandomSource, int paramInt2) {
/* 248 */     int i = getBurnOdds(paramLevel.getBlockState(paramBlockPos));
/* 249 */     if (paramRandomSource.nextInt(paramInt1) < i) {
/* 250 */       BlockState blockState = paramLevel.getBlockState(paramBlockPos);
/*     */       
/* 252 */       if (paramRandomSource.nextInt(paramInt2 + 10) < 5 && !paramLevel.isRainingAt(paramBlockPos)) {
/* 253 */         int j = Math.min(paramInt2 + paramRandomSource.nextInt(5) / 4, 15);
/* 254 */         paramLevel.setBlock(paramBlockPos, getStateWithAge((LevelReader)paramLevel, paramBlockPos, j), 3);
/*     */       } else {
/* 256 */         paramLevel.removeBlock(paramBlockPos, false);
/*     */       } 
/*     */       
/* 259 */       Block block = blockState.getBlock();
/* 260 */       if (block instanceof TntBlock) {
/* 261 */         TntBlock.prime(paramLevel, paramBlockPos);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private BlockState getStateWithAge(LevelReader paramLevelReader, BlockPos paramBlockPos, int paramInt) {
/* 267 */     BlockState blockState = getState((BlockGetter)paramLevelReader, paramBlockPos);
/* 268 */     if (blockState.is(Blocks.FIRE)) {
/* 269 */       return (BlockState)blockState.setValue((Property)AGE, Integer.valueOf(paramInt));
/*     */     }
/*     */     
/* 272 */     return blockState;
/*     */   }
/*     */   
/*     */   private boolean isValidFireLocation(BlockGetter paramBlockGetter, BlockPos paramBlockPos) {
/* 276 */     for (Direction direction : Direction.values()) {
/* 277 */       if (canBurn(paramBlockGetter.getBlockState(paramBlockPos.relative(direction)))) {
/* 278 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 282 */     return false;
/*     */   }
/*     */   
/*     */   private int getIgniteOdds(LevelReader paramLevelReader, BlockPos paramBlockPos) {
/* 286 */     if (!paramLevelReader.isEmptyBlock(paramBlockPos)) {
/* 287 */       return 0;
/*     */     }
/*     */     
/* 290 */     int i = 0;
/* 291 */     for (Direction direction : Direction.values()) {
/* 292 */       BlockState blockState = paramLevelReader.getBlockState(paramBlockPos.relative(direction));
/* 293 */       i = Math.max(getIgniteOdds(blockState), i);
/*     */     } 
/*     */     
/* 296 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canBurn(BlockState paramBlockState) {
/* 301 */     return (getIgniteOdds(paramBlockState) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onPlace(BlockState paramBlockState1, Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState2, boolean paramBoolean) {
/* 306 */     super.onPlace(paramBlockState1, paramLevel, paramBlockPos, paramBlockState2, paramBoolean);
/*     */     
/* 308 */     paramLevel.scheduleTick(paramBlockPos, this, getFireTickDelay(paramLevel.random));
/*     */   }
/*     */   
/*     */   private static int getFireTickDelay(RandomSource paramRandomSource) {
/* 312 */     return 30 + paramRandomSource.nextInt(10);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> paramBuilder) {
/* 317 */     paramBuilder.add(new Property[] { (Property)AGE, (Property)NORTH, (Property)EAST, (Property)SOUTH, (Property)WEST, (Property)UP });
/*     */   }
/*     */   
/*     */   public void setFlammable(Block paramBlock, int paramInt1, int paramInt2) {
/* 321 */     this.igniteOdds.put(paramBlock, paramInt1);
/* 322 */     this.burnOdds.put(paramBlock, paramInt2);
/*     */   }
/*     */   
/*     */   public static void bootStrap() {
/* 326 */     FireBlock fireBlock = (FireBlock)Blocks.FIRE;
/* 327 */     fireBlock.setFlammable(Blocks.OAK_PLANKS, 5, 20);
/* 328 */     fireBlock.setFlammable(Blocks.SPRUCE_PLANKS, 5, 20);
/* 329 */     fireBlock.setFlammable(Blocks.BIRCH_PLANKS, 5, 20);
/* 330 */     fireBlock.setFlammable(Blocks.JUNGLE_PLANKS, 5, 20);
/* 331 */     fireBlock.setFlammable(Blocks.ACACIA_PLANKS, 5, 20);
/* 332 */     fireBlock.setFlammable(Blocks.CHERRY_PLANKS, 5, 20);
/* 333 */     fireBlock.setFlammable(Blocks.DARK_OAK_PLANKS, 5, 20);
/* 334 */     fireBlock.setFlammable(Blocks.PALE_OAK_PLANKS, 5, 20);
/* 335 */     fireBlock.setFlammable(Blocks.MANGROVE_PLANKS, 5, 20);
/* 336 */     fireBlock.setFlammable(Blocks.BAMBOO_PLANKS, 5, 20);
/* 337 */     fireBlock.setFlammable(Blocks.BAMBOO_MOSAIC, 5, 20);
/* 338 */     fireBlock.setFlammable(Blocks.OAK_SLAB, 5, 20);
/* 339 */     fireBlock.setFlammable(Blocks.SPRUCE_SLAB, 5, 20);
/* 340 */     fireBlock.setFlammable(Blocks.BIRCH_SLAB, 5, 20);
/* 341 */     fireBlock.setFlammable(Blocks.JUNGLE_SLAB, 5, 20);
/* 342 */     fireBlock.setFlammable(Blocks.ACACIA_SLAB, 5, 20);
/* 343 */     fireBlock.setFlammable(Blocks.CHERRY_SLAB, 5, 20);
/* 344 */     fireBlock.setFlammable(Blocks.DARK_OAK_SLAB, 5, 20);
/* 345 */     fireBlock.setFlammable(Blocks.PALE_OAK_SLAB, 5, 20);
/* 346 */     fireBlock.setFlammable(Blocks.MANGROVE_SLAB, 5, 20);
/* 347 */     fireBlock.setFlammable(Blocks.BAMBOO_SLAB, 5, 20);
/* 348 */     fireBlock.setFlammable(Blocks.BAMBOO_MOSAIC_SLAB, 5, 20);
/* 349 */     fireBlock.setFlammable(Blocks.OAK_FENCE_GATE, 5, 20);
/* 350 */     fireBlock.setFlammable(Blocks.SPRUCE_FENCE_GATE, 5, 20);
/* 351 */     fireBlock.setFlammable(Blocks.BIRCH_FENCE_GATE, 5, 20);
/* 352 */     fireBlock.setFlammable(Blocks.JUNGLE_FENCE_GATE, 5, 20);
/* 353 */     fireBlock.setFlammable(Blocks.ACACIA_FENCE_GATE, 5, 20);
/* 354 */     fireBlock.setFlammable(Blocks.CHERRY_FENCE_GATE, 5, 20);
/* 355 */     fireBlock.setFlammable(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
/* 356 */     fireBlock.setFlammable(Blocks.PALE_OAK_FENCE_GATE, 5, 20);
/* 357 */     fireBlock.setFlammable(Blocks.MANGROVE_FENCE_GATE, 5, 20);
/* 358 */     fireBlock.setFlammable(Blocks.BAMBOO_FENCE_GATE, 5, 20);
/* 359 */     fireBlock.setFlammable(Blocks.OAK_FENCE, 5, 20);
/* 360 */     fireBlock.setFlammable(Blocks.SPRUCE_FENCE, 5, 20);
/* 361 */     fireBlock.setFlammable(Blocks.BIRCH_FENCE, 5, 20);
/* 362 */     fireBlock.setFlammable(Blocks.JUNGLE_FENCE, 5, 20);
/* 363 */     fireBlock.setFlammable(Blocks.ACACIA_FENCE, 5, 20);
/* 364 */     fireBlock.setFlammable(Blocks.CHERRY_FENCE, 5, 20);
/* 365 */     fireBlock.setFlammable(Blocks.DARK_OAK_FENCE, 5, 20);
/* 366 */     fireBlock.setFlammable(Blocks.PALE_OAK_FENCE, 5, 20);
/* 367 */     fireBlock.setFlammable(Blocks.MANGROVE_FENCE, 5, 20);
/* 368 */     fireBlock.setFlammable(Blocks.BAMBOO_FENCE, 5, 20);
/* 369 */     fireBlock.setFlammable(Blocks.OAK_STAIRS, 5, 20);
/* 370 */     fireBlock.setFlammable(Blocks.BIRCH_STAIRS, 5, 20);
/* 371 */     fireBlock.setFlammable(Blocks.SPRUCE_STAIRS, 5, 20);
/* 372 */     fireBlock.setFlammable(Blocks.JUNGLE_STAIRS, 5, 20);
/* 373 */     fireBlock.setFlammable(Blocks.ACACIA_STAIRS, 5, 20);
/* 374 */     fireBlock.setFlammable(Blocks.CHERRY_STAIRS, 5, 20);
/* 375 */     fireBlock.setFlammable(Blocks.DARK_OAK_STAIRS, 5, 20);
/* 376 */     fireBlock.setFlammable(Blocks.PALE_OAK_STAIRS, 5, 20);
/* 377 */     fireBlock.setFlammable(Blocks.MANGROVE_STAIRS, 5, 20);
/* 378 */     fireBlock.setFlammable(Blocks.BAMBOO_STAIRS, 5, 20);
/* 379 */     fireBlock.setFlammable(Blocks.BAMBOO_MOSAIC_STAIRS, 5, 20);
/* 380 */     fireBlock.setFlammable(Blocks.OAK_LOG, 5, 5);
/* 381 */     fireBlock.setFlammable(Blocks.SPRUCE_LOG, 5, 5);
/* 382 */     fireBlock.setFlammable(Blocks.BIRCH_LOG, 5, 5);
/* 383 */     fireBlock.setFlammable(Blocks.JUNGLE_LOG, 5, 5);
/* 384 */     fireBlock.setFlammable(Blocks.ACACIA_LOG, 5, 5);
/* 385 */     fireBlock.setFlammable(Blocks.CHERRY_LOG, 5, 5);
/* 386 */     fireBlock.setFlammable(Blocks.PALE_OAK_LOG, 5, 5);
/* 387 */     fireBlock.setFlammable(Blocks.DARK_OAK_LOG, 5, 5);
/* 388 */     fireBlock.setFlammable(Blocks.MANGROVE_LOG, 5, 5);
/* 389 */     fireBlock.setFlammable(Blocks.BAMBOO_BLOCK, 5, 5);
/* 390 */     fireBlock.setFlammable(Blocks.STRIPPED_OAK_LOG, 5, 5);
/* 391 */     fireBlock.setFlammable(Blocks.STRIPPED_SPRUCE_LOG, 5, 5);
/* 392 */     fireBlock.setFlammable(Blocks.STRIPPED_BIRCH_LOG, 5, 5);
/* 393 */     fireBlock.setFlammable(Blocks.STRIPPED_JUNGLE_LOG, 5, 5);
/* 394 */     fireBlock.setFlammable(Blocks.STRIPPED_ACACIA_LOG, 5, 5);
/* 395 */     fireBlock.setFlammable(Blocks.STRIPPED_CHERRY_LOG, 5, 5);
/* 396 */     fireBlock.setFlammable(Blocks.STRIPPED_DARK_OAK_LOG, 5, 5);
/* 397 */     fireBlock.setFlammable(Blocks.STRIPPED_PALE_OAK_LOG, 5, 5);
/* 398 */     fireBlock.setFlammable(Blocks.STRIPPED_MANGROVE_LOG, 5, 5);
/* 399 */     fireBlock.setFlammable(Blocks.STRIPPED_BAMBOO_BLOCK, 5, 5);
/* 400 */     fireBlock.setFlammable(Blocks.STRIPPED_OAK_WOOD, 5, 5);
/* 401 */     fireBlock.setFlammable(Blocks.STRIPPED_SPRUCE_WOOD, 5, 5);
/* 402 */     fireBlock.setFlammable(Blocks.STRIPPED_BIRCH_WOOD, 5, 5);
/* 403 */     fireBlock.setFlammable(Blocks.STRIPPED_JUNGLE_WOOD, 5, 5);
/* 404 */     fireBlock.setFlammable(Blocks.STRIPPED_ACACIA_WOOD, 5, 5);
/* 405 */     fireBlock.setFlammable(Blocks.STRIPPED_CHERRY_WOOD, 5, 5);
/* 406 */     fireBlock.setFlammable(Blocks.STRIPPED_DARK_OAK_WOOD, 5, 5);
/* 407 */     fireBlock.setFlammable(Blocks.STRIPPED_PALE_OAK_WOOD, 5, 5);
/* 408 */     fireBlock.setFlammable(Blocks.STRIPPED_MANGROVE_WOOD, 5, 5);
/* 409 */     fireBlock.setFlammable(Blocks.OAK_WOOD, 5, 5);
/* 410 */     fireBlock.setFlammable(Blocks.SPRUCE_WOOD, 5, 5);
/* 411 */     fireBlock.setFlammable(Blocks.BIRCH_WOOD, 5, 5);
/* 412 */     fireBlock.setFlammable(Blocks.JUNGLE_WOOD, 5, 5);
/* 413 */     fireBlock.setFlammable(Blocks.ACACIA_WOOD, 5, 5);
/* 414 */     fireBlock.setFlammable(Blocks.CHERRY_WOOD, 5, 5);
/* 415 */     fireBlock.setFlammable(Blocks.PALE_OAK_WOOD, 5, 5);
/* 416 */     fireBlock.setFlammable(Blocks.DARK_OAK_WOOD, 5, 5);
/* 417 */     fireBlock.setFlammable(Blocks.MANGROVE_WOOD, 5, 5);
/* 418 */     fireBlock.setFlammable(Blocks.MANGROVE_ROOTS, 5, 20);
/* 419 */     fireBlock.setFlammable(Blocks.OAK_LEAVES, 30, 60);
/* 420 */     fireBlock.setFlammable(Blocks.SPRUCE_LEAVES, 30, 60);
/* 421 */     fireBlock.setFlammable(Blocks.BIRCH_LEAVES, 30, 60);
/* 422 */     fireBlock.setFlammable(Blocks.JUNGLE_LEAVES, 30, 60);
/* 423 */     fireBlock.setFlammable(Blocks.ACACIA_LEAVES, 30, 60);
/* 424 */     fireBlock.setFlammable(Blocks.CHERRY_LEAVES, 30, 60);
/* 425 */     fireBlock.setFlammable(Blocks.DARK_OAK_LEAVES, 30, 60);
/* 426 */     fireBlock.setFlammable(Blocks.PALE_OAK_LEAVES, 30, 60);
/* 427 */     fireBlock.setFlammable(Blocks.MANGROVE_LEAVES, 30, 60);
/* 428 */     fireBlock.setFlammable(Blocks.BOOKSHELF, 30, 20);
/* 429 */     fireBlock.setFlammable(Blocks.TNT, 15, 100);
/* 430 */     fireBlock.setFlammable(Blocks.SHORT_GRASS, 60, 100);
/* 431 */     fireBlock.setFlammable(Blocks.FERN, 60, 100);
/* 432 */     fireBlock.setFlammable(Blocks.DEAD_BUSH, 60, 100);
/* 433 */     fireBlock.setFlammable(Blocks.SHORT_DRY_GRASS, 60, 100);
/* 434 */     fireBlock.setFlammable(Blocks.TALL_DRY_GRASS, 60, 100);
/* 435 */     fireBlock.setFlammable(Blocks.SUNFLOWER, 60, 100);
/* 436 */     fireBlock.setFlammable(Blocks.LILAC, 60, 100);
/* 437 */     fireBlock.setFlammable(Blocks.ROSE_BUSH, 60, 100);
/* 438 */     fireBlock.setFlammable(Blocks.PEONY, 60, 100);
/* 439 */     fireBlock.setFlammable(Blocks.TALL_GRASS, 60, 100);
/* 440 */     fireBlock.setFlammable(Blocks.LARGE_FERN, 60, 100);
/* 441 */     fireBlock.setFlammable(Blocks.DANDELION, 60, 100);
/* 442 */     fireBlock.setFlammable(Blocks.POPPY, 60, 100);
/* 443 */     fireBlock.setFlammable(Blocks.OPEN_EYEBLOSSOM, 60, 100);
/* 444 */     fireBlock.setFlammable(Blocks.CLOSED_EYEBLOSSOM, 60, 100);
/* 445 */     fireBlock.setFlammable(Blocks.BLUE_ORCHID, 60, 100);
/* 446 */     fireBlock.setFlammable(Blocks.ALLIUM, 60, 100);
/* 447 */     fireBlock.setFlammable(Blocks.AZURE_BLUET, 60, 100);
/* 448 */     fireBlock.setFlammable(Blocks.RED_TULIP, 60, 100);
/* 449 */     fireBlock.setFlammable(Blocks.ORANGE_TULIP, 60, 100);
/* 450 */     fireBlock.setFlammable(Blocks.WHITE_TULIP, 60, 100);
/* 451 */     fireBlock.setFlammable(Blocks.PINK_TULIP, 60, 100);
/* 452 */     fireBlock.setFlammable(Blocks.OXEYE_DAISY, 60, 100);
/* 453 */     fireBlock.setFlammable(Blocks.CORNFLOWER, 60, 100);
/* 454 */     fireBlock.setFlammable(Blocks.LILY_OF_THE_VALLEY, 60, 100);
/* 455 */     fireBlock.setFlammable(Blocks.TORCHFLOWER, 60, 100);
/* 456 */     fireBlock.setFlammable(Blocks.PITCHER_PLANT, 60, 100);
/* 457 */     fireBlock.setFlammable(Blocks.WITHER_ROSE, 60, 100);
/* 458 */     fireBlock.setFlammable(Blocks.PINK_PETALS, 60, 100);
/* 459 */     fireBlock.setFlammable(Blocks.WILDFLOWERS, 60, 100);
/* 460 */     fireBlock.setFlammable(Blocks.LEAF_LITTER, 60, 100);
/* 461 */     fireBlock.setFlammable(Blocks.CACTUS_FLOWER, 60, 100);
/* 462 */     fireBlock.setFlammable(Blocks.WHITE_WOOL, 30, 60);
/* 463 */     fireBlock.setFlammable(Blocks.ORANGE_WOOL, 30, 60);
/* 464 */     fireBlock.setFlammable(Blocks.MAGENTA_WOOL, 30, 60);
/* 465 */     fireBlock.setFlammable(Blocks.LIGHT_BLUE_WOOL, 30, 60);
/* 466 */     fireBlock.setFlammable(Blocks.YELLOW_WOOL, 30, 60);
/* 467 */     fireBlock.setFlammable(Blocks.LIME_WOOL, 30, 60);
/* 468 */     fireBlock.setFlammable(Blocks.PINK_WOOL, 30, 60);
/* 469 */     fireBlock.setFlammable(Blocks.GRAY_WOOL, 30, 60);
/* 470 */     fireBlock.setFlammable(Blocks.LIGHT_GRAY_WOOL, 30, 60);
/* 471 */     fireBlock.setFlammable(Blocks.CYAN_WOOL, 30, 60);
/* 472 */     fireBlock.setFlammable(Blocks.PURPLE_WOOL, 30, 60);
/* 473 */     fireBlock.setFlammable(Blocks.BLUE_WOOL, 30, 60);
/* 474 */     fireBlock.setFlammable(Blocks.BROWN_WOOL, 30, 60);
/* 475 */     fireBlock.setFlammable(Blocks.GREEN_WOOL, 30, 60);
/* 476 */     fireBlock.setFlammable(Blocks.RED_WOOL, 30, 60);
/* 477 */     fireBlock.setFlammable(Blocks.BLACK_WOOL, 30, 60);
/* 478 */     fireBlock.setFlammable(Blocks.VINE, 15, 100);
/* 479 */     fireBlock.setFlammable(Blocks.COAL_BLOCK, 5, 5);
/* 480 */     fireBlock.setFlammable(Blocks.HAY_BLOCK, 60, 20);
/* 481 */     fireBlock.setFlammable(Blocks.TARGET, 15, 20);
/* 482 */     fireBlock.setFlammable(Blocks.WHITE_CARPET, 60, 20);
/* 483 */     fireBlock.setFlammable(Blocks.ORANGE_CARPET, 60, 20);
/* 484 */     fireBlock.setFlammable(Blocks.MAGENTA_CARPET, 60, 20);
/* 485 */     fireBlock.setFlammable(Blocks.LIGHT_BLUE_CARPET, 60, 20);
/* 486 */     fireBlock.setFlammable(Blocks.YELLOW_CARPET, 60, 20);
/* 487 */     fireBlock.setFlammable(Blocks.LIME_CARPET, 60, 20);
/* 488 */     fireBlock.setFlammable(Blocks.PINK_CARPET, 60, 20);
/* 489 */     fireBlock.setFlammable(Blocks.GRAY_CARPET, 60, 20);
/* 490 */     fireBlock.setFlammable(Blocks.LIGHT_GRAY_CARPET, 60, 20);
/* 491 */     fireBlock.setFlammable(Blocks.CYAN_CARPET, 60, 20);
/* 492 */     fireBlock.setFlammable(Blocks.PURPLE_CARPET, 60, 20);
/* 493 */     fireBlock.setFlammable(Blocks.BLUE_CARPET, 60, 20);
/* 494 */     fireBlock.setFlammable(Blocks.BROWN_CARPET, 60, 20);
/* 495 */     fireBlock.setFlammable(Blocks.GREEN_CARPET, 60, 20);
/* 496 */     fireBlock.setFlammable(Blocks.RED_CARPET, 60, 20);
/* 497 */     fireBlock.setFlammable(Blocks.BLACK_CARPET, 60, 20);
/* 498 */     fireBlock.setFlammable(Blocks.PALE_MOSS_BLOCK, 5, 100);
/* 499 */     fireBlock.setFlammable(Blocks.PALE_MOSS_CARPET, 5, 100);
/* 500 */     fireBlock.setFlammable(Blocks.PALE_HANGING_MOSS, 5, 100);
/* 501 */     fireBlock.setFlammable(Blocks.DRIED_KELP_BLOCK, 30, 60);
/* 502 */     fireBlock.setFlammable(Blocks.BAMBOO, 60, 60);
/* 503 */     fireBlock.setFlammable(Blocks.SCAFFOLDING, 60, 60);
/* 504 */     fireBlock.setFlammable(Blocks.LECTERN, 30, 20);
/* 505 */     fireBlock.setFlammable(Blocks.COMPOSTER, 5, 20);
/* 506 */     fireBlock.setFlammable(Blocks.SWEET_BERRY_BUSH, 60, 100);
/* 507 */     fireBlock.setFlammable(Blocks.BEEHIVE, 5, 20);
/* 508 */     fireBlock.setFlammable(Blocks.BEE_NEST, 30, 20);
/* 509 */     fireBlock.setFlammable(Blocks.AZALEA_LEAVES, 30, 60);
/* 510 */     fireBlock.setFlammable(Blocks.FLOWERING_AZALEA_LEAVES, 30, 60);
/* 511 */     fireBlock.setFlammable(Blocks.CAVE_VINES, 15, 60);
/* 512 */     fireBlock.setFlammable(Blocks.CAVE_VINES_PLANT, 15, 60);
/* 513 */     fireBlock.setFlammable(Blocks.SPORE_BLOSSOM, 60, 100);
/* 514 */     fireBlock.setFlammable(Blocks.AZALEA, 30, 60);
/* 515 */     fireBlock.setFlammable(Blocks.FLOWERING_AZALEA, 30, 60);
/* 516 */     fireBlock.setFlammable(Blocks.BIG_DRIPLEAF, 60, 100);
/* 517 */     fireBlock.setFlammable(Blocks.BIG_DRIPLEAF_STEM, 60, 100);
/* 518 */     fireBlock.setFlammable(Blocks.SMALL_DRIPLEAF, 60, 100);
/* 519 */     fireBlock.setFlammable(Blocks.HANGING_ROOTS, 30, 60);
/* 520 */     fireBlock.setFlammable(Blocks.GLOW_LICHEN, 15, 100);
/* 521 */     fireBlock.setFlammable(Blocks.FIREFLY_BUSH, 60, 100);
/* 522 */     fireBlock.setFlammable(Blocks.BUSH, 60, 100);
/* 523 */     fireBlock.setFlammable(Blocks.ACACIA_SHELF, 30, 20);
/* 524 */     fireBlock.setFlammable(Blocks.BAMBOO_SHELF, 30, 20);
/* 525 */     fireBlock.setFlammable(Blocks.BIRCH_SHELF, 30, 20);
/* 526 */     fireBlock.setFlammable(Blocks.CHERRY_SHELF, 30, 20);
/* 527 */     fireBlock.setFlammable(Blocks.DARK_OAK_SHELF, 30, 20);
/* 528 */     fireBlock.setFlammable(Blocks.JUNGLE_SHELF, 30, 20);
/* 529 */     fireBlock.setFlammable(Blocks.MANGROVE_SHELF, 30, 20);
/* 530 */     fireBlock.setFlammable(Blocks.OAK_SHELF, 30, 20);
/* 531 */     fireBlock.setFlammable(Blocks.PALE_OAK_SHELF, 30, 20);
/* 532 */     fireBlock.setFlammable(Blocks.SPRUCE_SHELF, 30, 20);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\block\FireBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */