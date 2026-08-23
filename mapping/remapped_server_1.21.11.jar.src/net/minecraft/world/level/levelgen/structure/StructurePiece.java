/*     */ package net.minecraft.world.level.levelgen.structure;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DispenserBlock;
/*     */ import net.minecraft.world.level.block.HorizontalDirectionalBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.ChestBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.DispenserBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.storage.loot.LootTable;
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
/*     */ public abstract class StructurePiece
/*     */ {
/*  66 */   protected static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
/*     */   protected BoundingBox boundingBox;
/*     */   private Direction orientation;
/*     */   private Mirror mirror;
/*     */   private Rotation rotation;
/*     */   protected int genDepth;
/*     */   private final StructurePieceType type;
/*     */   
/*     */   protected StructurePiece(StructurePieceType paramStructurePieceType, int paramInt, BoundingBox paramBoundingBox) {
/*  75 */     this.type = paramStructurePieceType;
/*  76 */     this.genDepth = paramInt;
/*  77 */     this.boundingBox = paramBoundingBox;
/*     */   }
/*     */   
/*     */   public StructurePiece(StructurePieceType paramStructurePieceType, CompoundTag paramCompoundTag) {
/*  81 */     this(paramStructurePieceType, paramCompoundTag
/*     */         
/*  83 */         .getIntOr("GD", 0), paramCompoundTag
/*  84 */         .read("BB", BoundingBox.CODEC).orElseThrow());
/*     */     
/*  86 */     int i = paramCompoundTag.getIntOr("O", 0);
/*  87 */     setOrientation((i == -1) ? null : Direction.from2DDataValue(i));
/*     */   }
/*     */   
/*     */   protected static BoundingBox makeBoundingBox(int paramInt1, int paramInt2, int paramInt3, Direction paramDirection, int paramInt4, int paramInt5, int paramInt6) {
/*  91 */     if (paramDirection.getAxis() == Direction.Axis.Z) {
/*  92 */       return new BoundingBox(paramInt1, paramInt2, paramInt3, paramInt1 + paramInt4 - 1, paramInt2 + paramInt5 - 1, paramInt3 + paramInt6 - 1);
/*     */     }
/*  94 */     return new BoundingBox(paramInt1, paramInt2, paramInt3, paramInt1 + paramInt6 - 1, paramInt2 + paramInt5 - 1, paramInt3 + paramInt4 - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static Direction getRandomHorizontalDirection(RandomSource paramRandomSource) {
/*  99 */     return Direction.Plane.HORIZONTAL.getRandomDirection(paramRandomSource);
/*     */   }
/*     */   
/*     */   public final CompoundTag createTag(StructurePieceSerializationContext paramStructurePieceSerializationContext) {
/* 103 */     CompoundTag compoundTag = new CompoundTag();
/*     */     
/* 105 */     compoundTag.putString("id", BuiltInRegistries.STRUCTURE_PIECE.getKey(getType()).toString());
/* 106 */     compoundTag.store("BB", BoundingBox.CODEC, this.boundingBox);
/* 107 */     Direction direction = getOrientation();
/* 108 */     compoundTag.putInt("O", (direction == null) ? -1 : direction.get2DDataValue());
/* 109 */     compoundTag.putInt("GD", this.genDepth);
/*     */     
/* 111 */     addAdditionalSaveData(paramStructurePieceSerializationContext, compoundTag);
/*     */     
/* 113 */     return compoundTag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addChildren(StructurePiece paramStructurePiece, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public BoundingBox getBoundingBox() {
/* 124 */     return this.boundingBox;
/*     */   }
/*     */   
/*     */   public int getGenDepth() {
/* 128 */     return this.genDepth;
/*     */   }
/*     */   
/*     */   public void setGenDepth(int paramInt) {
/* 132 */     this.genDepth = paramInt;
/*     */   }
/*     */   
/*     */   public boolean isCloseToChunk(ChunkPos paramChunkPos, int paramInt) {
/* 136 */     int i = paramChunkPos.getMinBlockX();
/* 137 */     int j = paramChunkPos.getMinBlockZ();
/*     */     
/* 139 */     return this.boundingBox.intersects(i - paramInt, j - paramInt, i + 15 + paramInt, j + 15 + paramInt);
/*     */   }
/*     */   
/*     */   public BlockPos getLocatorPosition() {
/* 143 */     return new BlockPos((Vec3i)this.boundingBox.getCenter());
/*     */   }
/*     */   
/*     */   protected BlockPos.MutableBlockPos getWorldPos(int paramInt1, int paramInt2, int paramInt3) {
/* 147 */     return new BlockPos.MutableBlockPos(getWorldX(paramInt1, paramInt3), getWorldY(paramInt2), getWorldZ(paramInt1, paramInt3));
/*     */   }
/*     */   
/*     */   protected int getWorldX(int paramInt1, int paramInt2) {
/* 151 */     Direction direction = getOrientation();
/* 152 */     if (direction == null) {
/* 153 */       return paramInt1;
/*     */     }
/*     */     
/* 156 */     switch (direction) {
/*     */       case NORTH:
/*     */       case SOUTH:
/* 159 */         return this.boundingBox.minX() + paramInt1;
/*     */       case WEST:
/* 161 */         return this.boundingBox.maxX() - paramInt2;
/*     */       case EAST:
/* 163 */         return this.boundingBox.minX() + paramInt2;
/*     */     } 
/* 165 */     return paramInt1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getWorldY(int paramInt) {
/* 170 */     if (getOrientation() == null) {
/* 171 */       return paramInt;
/*     */     }
/* 173 */     return paramInt + this.boundingBox.minY();
/*     */   }
/*     */   
/*     */   protected int getWorldZ(int paramInt1, int paramInt2) {
/* 177 */     Direction direction = getOrientation();
/* 178 */     if (direction == null) {
/* 179 */       return paramInt2;
/*     */     }
/*     */     
/* 182 */     switch (direction) {
/*     */       case NORTH:
/* 184 */         return this.boundingBox.maxZ() - paramInt2;
/*     */       case SOUTH:
/* 186 */         return this.boundingBox.minZ() + paramInt2;
/*     */       case WEST:
/*     */       case EAST:
/* 189 */         return this.boundingBox.minZ() + paramInt1;
/*     */     } 
/* 191 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   
/* 195 */   private static final Set<Block> SHAPE_CHECK_BLOCKS = (Set<Block>)ImmutableSet.builder()
/*     */     
/* 197 */     .add(Blocks.NETHER_BRICK_FENCE)
/* 198 */     .add(Blocks.TORCH)
/* 199 */     .add(Blocks.WALL_TORCH)
/* 200 */     .add(Blocks.OAK_FENCE)
/* 201 */     .add(Blocks.SPRUCE_FENCE)
/* 202 */     .add(Blocks.DARK_OAK_FENCE)
/* 203 */     .add(Blocks.PALE_OAK_FENCE)
/* 204 */     .add(Blocks.ACACIA_FENCE)
/* 205 */     .add(Blocks.BIRCH_FENCE)
/* 206 */     .add(Blocks.JUNGLE_FENCE)
/* 207 */     .add(Blocks.LADDER)
/* 208 */     .add(Blocks.IRON_BARS)
/* 209 */     .build();
/*     */   
/*     */   protected void placeBlock(WorldGenLevel paramWorldGenLevel, BlockState paramBlockState, int paramInt1, int paramInt2, int paramInt3, BoundingBox paramBoundingBox) {
/* 212 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(paramInt1, paramInt2, paramInt3);
/*     */     
/* 214 */     if (!paramBoundingBox.isInside((Vec3i)mutableBlockPos)) {
/*     */       return;
/*     */     }
/*     */     
/* 218 */     if (!canBeReplaced((LevelReader)paramWorldGenLevel, paramInt1, paramInt2, paramInt3, paramBoundingBox)) {
/*     */       return;
/*     */     }
/*     */     
/* 222 */     if (this.mirror != Mirror.NONE) {
/* 223 */       paramBlockState = paramBlockState.mirror(this.mirror);
/*     */     }
/* 225 */     if (this.rotation != Rotation.NONE) {
/* 226 */       paramBlockState = paramBlockState.rotate(this.rotation);
/*     */     }
/*     */     
/* 229 */     paramWorldGenLevel.setBlock((BlockPos)mutableBlockPos, paramBlockState, 2);
/* 230 */     FluidState fluidState = paramWorldGenLevel.getFluidState((BlockPos)mutableBlockPos);
/* 231 */     if (!fluidState.isEmpty()) {
/* 232 */       paramWorldGenLevel.scheduleTick((BlockPos)mutableBlockPos, fluidState.getType(), 0);
/*     */     }
/* 234 */     if (SHAPE_CHECK_BLOCKS.contains(paramBlockState.getBlock())) {
/* 235 */       paramWorldGenLevel.getChunk((BlockPos)mutableBlockPos).markPosForPostprocessing((BlockPos)mutableBlockPos);
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean canBeReplaced(LevelReader paramLevelReader, int paramInt1, int paramInt2, int paramInt3, BoundingBox paramBoundingBox) {
/* 240 */     return true;
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
/*     */   protected BlockState getBlock(BlockGetter paramBlockGetter, int paramInt1, int paramInt2, int paramInt3, BoundingBox paramBoundingBox) {
/* 256 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(paramInt1, paramInt2, paramInt3);
/* 257 */     if (!paramBoundingBox.isInside((Vec3i)mutableBlockPos)) {
/* 258 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 261 */     return paramBlockGetter.getBlockState((BlockPos)mutableBlockPos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isInterior(LevelReader paramLevelReader, int paramInt1, int paramInt2, int paramInt3, BoundingBox paramBoundingBox) {
/* 266 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(paramInt1, paramInt2 + 1, paramInt3);
/*     */     
/* 268 */     if (!paramBoundingBox.isInside((Vec3i)mutableBlockPos)) {
/* 269 */       return false;
/*     */     }
/*     */     
/* 272 */     return (mutableBlockPos.getY() < paramLevelReader.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, mutableBlockPos.getX(), mutableBlockPos.getZ()));
/*     */   }
/*     */   
/*     */   protected void generateAirBox(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
/* 276 */     for (int i = paramInt2; i <= paramInt5; i++) {
/* 277 */       for (int j = paramInt1; j <= paramInt4; j++) {
/* 278 */         for (int k = paramInt3; k <= paramInt6; k++) {
/* 279 */           placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), j, i, k, paramBoundingBox);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateBox(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, BlockState paramBlockState1, BlockState paramBlockState2, boolean paramBoolean) {
/* 286 */     for (int i = paramInt2; i <= paramInt5; i++) {
/* 287 */       for (int j = paramInt1; j <= paramInt4; j++) {
/* 288 */         for (int k = paramInt3; k <= paramInt6; k++) {
/* 289 */           if (!paramBoolean || !getBlock((BlockGetter)paramWorldGenLevel, j, i, k, paramBoundingBox).isAir())
/*     */           {
/*     */             
/* 292 */             if (i == paramInt2 || i == paramInt5 || j == paramInt1 || j == paramInt4 || k == paramInt3 || k == paramInt6) {
/* 293 */               placeBlock(paramWorldGenLevel, paramBlockState1, j, i, k, paramBoundingBox);
/*     */             } else {
/* 295 */               placeBlock(paramWorldGenLevel, paramBlockState2, j, i, k, paramBoundingBox);
/*     */             }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void generateBox(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox1, BoundingBox paramBoundingBox2, BlockState paramBlockState1, BlockState paramBlockState2, boolean paramBoolean) {
/* 303 */     generateBox(paramWorldGenLevel, paramBoundingBox1, paramBoundingBox2.minX(), paramBoundingBox2.minY(), paramBoundingBox2.minZ(), paramBoundingBox2.maxX(), paramBoundingBox2.maxY(), paramBoundingBox2.maxZ(), paramBlockState1, paramBlockState2, paramBoolean);
/*     */   }
/*     */   
/*     */   protected void generateBox(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, boolean paramBoolean, RandomSource paramRandomSource, BlockSelector paramBlockSelector) {
/* 307 */     for (int i = paramInt2; i <= paramInt5; i++) {
/* 308 */       for (int j = paramInt1; j <= paramInt4; j++) {
/* 309 */         for (int k = paramInt3; k <= paramInt6; k++) {
/* 310 */           if (!paramBoolean || !getBlock((BlockGetter)paramWorldGenLevel, j, i, k, paramBoundingBox).isAir()) {
/*     */ 
/*     */             
/* 313 */             paramBlockSelector.next(paramRandomSource, j, i, k, (i == paramInt2 || i == paramInt5 || j == paramInt1 || j == paramInt4 || k == paramInt3 || k == paramInt6));
/* 314 */             placeBlock(paramWorldGenLevel, paramBlockSelector.getNext(), j, i, k, paramBoundingBox);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   protected void generateBox(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox1, BoundingBox paramBoundingBox2, boolean paramBoolean, RandomSource paramRandomSource, BlockSelector paramBlockSelector) {
/* 321 */     generateBox(paramWorldGenLevel, paramBoundingBox1, paramBoundingBox2.minX(), paramBoundingBox2.minY(), paramBoundingBox2.minZ(), paramBoundingBox2.maxX(), paramBoundingBox2.maxY(), paramBoundingBox2.maxZ(), paramBoolean, paramRandomSource, paramBlockSelector);
/*     */   }
/*     */   
/*     */   protected void generateMaybeBox(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, RandomSource paramRandomSource, float paramFloat, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, BlockState paramBlockState1, BlockState paramBlockState2, boolean paramBoolean1, boolean paramBoolean2) {
/* 325 */     for (int i = paramInt2; i <= paramInt5; i++) {
/* 326 */       for (int j = paramInt1; j <= paramInt4; j++) {
/* 327 */         for (int k = paramInt3; k <= paramInt6; k++) {
/* 328 */           if (paramRandomSource.nextFloat() <= paramFloat)
/*     */           {
/*     */             
/* 331 */             if (!paramBoolean1 || !getBlock((BlockGetter)paramWorldGenLevel, j, i, k, paramBoundingBox).isAir())
/*     */             {
/*     */               
/* 334 */               if (!paramBoolean2 || isInterior((LevelReader)paramWorldGenLevel, j, i, k, paramBoundingBox))
/*     */               {
/*     */                 
/* 337 */                 if (i == paramInt2 || i == paramInt5 || j == paramInt1 || j == paramInt4 || k == paramInt3 || k == paramInt6) {
/* 338 */                   placeBlock(paramWorldGenLevel, paramBlockState1, j, i, k, paramBoundingBox);
/*     */                 } else {
/* 340 */                   placeBlock(paramWorldGenLevel, paramBlockState2, j, i, k, paramBoundingBox);
/*     */                 }  }  }  } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void maybeGenerateBlock(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, RandomSource paramRandomSource, float paramFloat, int paramInt1, int paramInt2, int paramInt3, BlockState paramBlockState) {
/* 348 */     if (paramRandomSource.nextFloat() < paramFloat) {
/* 349 */       placeBlock(paramWorldGenLevel, paramBlockState, paramInt1, paramInt2, paramInt3, paramBoundingBox);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void generateUpperHalfSphere(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, BlockState paramBlockState, boolean paramBoolean) {
/* 354 */     float f1 = (paramInt4 - paramInt1 + 1);
/* 355 */     float f2 = (paramInt5 - paramInt2 + 1);
/* 356 */     float f3 = (paramInt6 - paramInt3 + 1);
/*     */     
/* 358 */     float f4 = paramInt1 + f1 / 2.0F;
/* 359 */     float f5 = paramInt3 + f3 / 2.0F;
/*     */     
/* 361 */     for (int i = paramInt2; i <= paramInt5; i++) {
/* 362 */       float f = (i - paramInt2) / f2;
/*     */       
/* 364 */       for (int j = paramInt1; j <= paramInt4; j++) {
/* 365 */         float f6 = (j - f4) / f1 * 0.5F;
/*     */         
/* 367 */         for (int k = paramInt3; k <= paramInt6; k++) {
/* 368 */           float f7 = (k - f5) / f3 * 0.5F;
/*     */           
/* 370 */           if (!paramBoolean || !getBlock((BlockGetter)paramWorldGenLevel, j, i, k, paramBoundingBox).isAir()) {
/*     */ 
/*     */ 
/*     */             
/* 374 */             float f8 = f6 * f6 + f * f + f7 * f7;
/*     */             
/* 376 */             if (f8 <= 1.05F)
/* 377 */               placeBlock(paramWorldGenLevel, paramBlockState, j, i, k, paramBoundingBox); 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void fillColumnDown(WorldGenLevel paramWorldGenLevel, BlockState paramBlockState, int paramInt1, int paramInt2, int paramInt3, BoundingBox paramBoundingBox) {
/* 385 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(paramInt1, paramInt2, paramInt3);
/* 386 */     if (!paramBoundingBox.isInside((Vec3i)mutableBlockPos)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 391 */     while (isReplaceableByStructures(paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos)) && mutableBlockPos.getY() > paramWorldGenLevel.getMinY() + 1) {
/* 392 */       paramWorldGenLevel.setBlock((BlockPos)mutableBlockPos, paramBlockState, 2);
/* 393 */       mutableBlockPos.move(Direction.DOWN);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean isReplaceableByStructures(BlockState paramBlockState) {
/* 398 */     return (paramBlockState.isAir() || paramBlockState.liquid() || paramBlockState.is(Blocks.GLOW_LICHEN) || paramBlockState.is(Blocks.SEAGRASS) || paramBlockState.is(Blocks.TALL_SEAGRASS));
/*     */   }
/*     */   
/*     */   protected boolean createChest(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, ResourceKey<LootTable> paramResourceKey) {
/* 402 */     return createChest((ServerLevelAccessor)paramWorldGenLevel, paramBoundingBox, paramRandomSource, (BlockPos)getWorldPos(paramInt1, paramInt2, paramInt3), paramResourceKey, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlockState reorient(BlockGetter paramBlockGetter, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 407 */     Direction direction1 = null;
/* 408 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 409 */       BlockPos blockPos1 = paramBlockPos.relative(direction);
/* 410 */       BlockState blockState = paramBlockGetter.getBlockState(blockPos1);
/* 411 */       if (blockState.is(Blocks.CHEST)) {
/* 412 */         return paramBlockState;
/*     */       }
/* 414 */       if (blockState.isSolidRender()) {
/* 415 */         if (direction1 == null) {
/* 416 */           direction1 = direction; continue;
/*     */         } 
/* 418 */         direction1 = null;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 423 */     if (direction1 != null) {
/* 424 */       return (BlockState)paramBlockState.setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)direction1.getOpposite());
/*     */     }
/*     */ 
/*     */     
/* 428 */     Direction direction2 = (Direction)paramBlockState.getValue((Property)HorizontalDirectionalBlock.FACING);
/* 429 */     BlockPos blockPos = paramBlockPos.relative(direction2);
/* 430 */     if (paramBlockGetter.getBlockState(blockPos).isSolidRender()) {
/* 431 */       direction2 = direction2.getOpposite();
/* 432 */       blockPos = paramBlockPos.relative(direction2);
/*     */     } 
/* 434 */     if (paramBlockGetter.getBlockState(blockPos).isSolidRender()) {
/* 435 */       direction2 = direction2.getClockWise();
/* 436 */       blockPos = paramBlockPos.relative(direction2);
/*     */     } 
/* 438 */     if (paramBlockGetter.getBlockState(blockPos).isSolidRender()) {
/* 439 */       direction2 = direction2.getOpposite();
/* 440 */       blockPos = paramBlockPos.relative(direction2);
/*     */     } 
/*     */     
/* 443 */     return (BlockState)paramBlockState.setValue((Property)HorizontalDirectionalBlock.FACING, (Comparable)direction2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean createChest(ServerLevelAccessor paramServerLevelAccessor, BoundingBox paramBoundingBox, RandomSource paramRandomSource, BlockPos paramBlockPos, ResourceKey<LootTable> paramResourceKey, BlockState paramBlockState) {
/* 450 */     if (!paramBoundingBox.isInside((Vec3i)paramBlockPos) || paramServerLevelAccessor.getBlockState(paramBlockPos).is(Blocks.CHEST)) {
/* 451 */       return false;
/*     */     }
/*     */     
/* 454 */     if (paramBlockState == null) {
/* 455 */       paramBlockState = reorient((BlockGetter)paramServerLevelAccessor, paramBlockPos, Blocks.CHEST.defaultBlockState());
/*     */     }
/* 457 */     paramServerLevelAccessor.setBlock(paramBlockPos, paramBlockState, 2);
/*     */     
/* 459 */     BlockEntity blockEntity = paramServerLevelAccessor.getBlockEntity(paramBlockPos);
/* 460 */     if (blockEntity instanceof ChestBlockEntity) {
/* 461 */       ((ChestBlockEntity)blockEntity).setLootTable(paramResourceKey, paramRandomSource.nextLong());
/*     */     }
/* 463 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean createDispenser(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection, ResourceKey<LootTable> paramResourceKey) {
/* 467 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(paramInt1, paramInt2, paramInt3);
/*     */     
/* 469 */     if (paramBoundingBox.isInside((Vec3i)mutableBlockPos) && 
/* 470 */       !paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos).is(Blocks.DISPENSER)) {
/* 471 */       placeBlock(paramWorldGenLevel, (BlockState)Blocks.DISPENSER.defaultBlockState().setValue((Property)DispenserBlock.FACING, (Comparable)paramDirection), paramInt1, paramInt2, paramInt3, paramBoundingBox);
/*     */       
/* 473 */       BlockEntity blockEntity = paramWorldGenLevel.getBlockEntity((BlockPos)mutableBlockPos);
/* 474 */       if (blockEntity instanceof DispenserBlockEntity) {
/* 475 */         ((DispenserBlockEntity)blockEntity).setLootTable(paramResourceKey, paramRandomSource.nextLong());
/*     */       }
/* 477 */       return true;
/*     */     } 
/*     */     
/* 480 */     return false;
/*     */   }
/*     */   
/*     */   public void move(int paramInt1, int paramInt2, int paramInt3) {
/* 484 */     this.boundingBox.move(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */   
/*     */   public static BoundingBox createBoundingBox(Stream<StructurePiece> paramStream) {
/* 488 */     Objects.requireNonNull(paramStream.map(StructurePiece::getBoundingBox)); return BoundingBox.encapsulatingBoxes(paramStream.map(StructurePiece::getBoundingBox)::iterator).<Throwable>orElseThrow(() -> new IllegalStateException("Unable to calculate boundingbox without pieces"));
/*     */   }
/*     */   
/*     */   public static StructurePiece findCollisionPiece(List<StructurePiece> paramList, BoundingBox paramBoundingBox) {
/* 492 */     for (StructurePiece structurePiece : paramList) {
/* 493 */       if (structurePiece.getBoundingBox().intersects(paramBoundingBox)) {
/* 494 */         return structurePiece;
/*     */       }
/*     */     } 
/* 497 */     return null;
/*     */   }
/*     */   
/*     */   public Direction getOrientation() {
/* 501 */     return this.orientation;
/*     */   }
/*     */   
/*     */   public void setOrientation(Direction paramDirection) {
/* 505 */     this.orientation = paramDirection;
/* 506 */     if (paramDirection == null) {
/* 507 */       this.rotation = Rotation.NONE;
/* 508 */       this.mirror = Mirror.NONE;
/*     */     } else {
/* 510 */       switch (paramDirection) {
/*     */         case SOUTH:
/* 512 */           this.mirror = Mirror.LEFT_RIGHT;
/* 513 */           this.rotation = Rotation.NONE;
/*     */           return;
/*     */         case WEST:
/* 516 */           this.mirror = Mirror.LEFT_RIGHT;
/* 517 */           this.rotation = Rotation.CLOCKWISE_90;
/*     */           return;
/*     */         case EAST:
/* 520 */           this.mirror = Mirror.NONE;
/* 521 */           this.rotation = Rotation.CLOCKWISE_90;
/*     */           return;
/*     */       } 
/* 524 */       this.mirror = Mirror.NONE;
/* 525 */       this.rotation = Rotation.NONE;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Rotation getRotation() {
/* 532 */     return this.rotation;
/*     */   }
/*     */   
/*     */   public Mirror getMirror() {
/* 536 */     return this.mirror;
/*     */   }
/*     */   protected abstract void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag);
/*     */   public StructurePieceType getType() {
/* 540 */     return this.type;
/*     */   }
/*     */   public abstract void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos);
/*     */   
/* 544 */   public static abstract class BlockSelector { protected BlockState next = Blocks.AIR.defaultBlockState();
/*     */     
/*     */     public abstract void next(RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean);
/*     */     
/*     */     public BlockState getNext() {
/* 549 */       return this.next;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\StructurePiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */