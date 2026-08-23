/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.vehicle.minecart.MinecartChest;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.FenceBlock;
/*     */ import net.minecraft.world.level.block.RailBlock;
/*     */ import net.minecraft.world.level.block.WallTorchBlock;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RailShape;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MineShaftCorridor
/*     */   extends MineshaftPieces.MineShaftPiece
/*     */ {
/*     */   private final boolean hasRails;
/*     */   private final boolean spiderCorridor;
/*     */   private boolean hasPlacedSpider;
/*     */   private final int numSections;
/*     */   
/*     */   public MineShaftCorridor(CompoundTag paramCompoundTag) {
/* 310 */     super(StructurePieceType.MINE_SHAFT_CORRIDOR, paramCompoundTag);
/*     */     
/* 312 */     this.hasRails = paramCompoundTag.getBooleanOr("hr", false);
/* 313 */     this.spiderCorridor = paramCompoundTag.getBooleanOr("sc", false);
/* 314 */     this.hasPlacedSpider = paramCompoundTag.getBooleanOr("hps", false);
/* 315 */     this.numSections = paramCompoundTag.getIntOr("Num", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/* 320 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/* 321 */     paramCompoundTag.putBoolean("hr", this.hasRails);
/* 322 */     paramCompoundTag.putBoolean("sc", this.spiderCorridor);
/* 323 */     paramCompoundTag.putBoolean("hps", this.hasPlacedSpider);
/* 324 */     paramCompoundTag.putInt("Num", this.numSections);
/*     */   }
/*     */   
/*     */   public MineShaftCorridor(int paramInt, RandomSource paramRandomSource, BoundingBox paramBoundingBox, Direction paramDirection, MineshaftStructure.Type paramType) {
/* 328 */     super(StructurePieceType.MINE_SHAFT_CORRIDOR, paramInt, paramType, paramBoundingBox);
/* 329 */     setOrientation(paramDirection);
/* 330 */     this.hasRails = (paramRandomSource.nextInt(3) == 0);
/* 331 */     this.spiderCorridor = (!this.hasRails && paramRandomSource.nextInt(23) == 0);
/*     */     
/* 333 */     if (getOrientation().getAxis() == Direction.Axis.Z) {
/* 334 */       this.numSections = paramBoundingBox.getZSpan() / 5;
/*     */     } else {
/* 336 */       this.numSections = paramBoundingBox.getXSpan() / 5;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static BoundingBox findCorridorSize(StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection) {
/* 341 */     int i = paramRandomSource.nextInt(3) + 2;
/* 342 */     while (i > 0) {
/*     */       BoundingBox boundingBox;
/* 344 */       int j = i * 5;
/*     */       
/* 346 */       switch (MineshaftPieces.null.$SwitchMap$net$minecraft$core$Direction[paramDirection.ordinal()]) {
/*     */         
/*     */         default:
/* 349 */           boundingBox = new BoundingBox(0, 0, -(j - 1), 2, 2, 0);
/*     */           break;
/*     */         case 2:
/* 352 */           boundingBox = new BoundingBox(0, 0, 0, 2, 2, j - 1);
/*     */           break;
/*     */         case 3:
/* 355 */           boundingBox = new BoundingBox(-(j - 1), 0, 0, 0, 2, 2);
/*     */           break;
/*     */         case 4:
/* 358 */           boundingBox = new BoundingBox(0, 0, 0, j - 1, 2, 2);
/*     */           break;
/*     */       } 
/*     */       
/* 362 */       boundingBox.move(paramInt1, paramInt2, paramInt3);
/*     */       
/* 364 */       if (paramStructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/* 365 */         i--; continue;
/*     */       } 
/* 367 */       return boundingBox;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 372 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChildren(StructurePiece paramStructurePiece, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource) {
/* 377 */     int i = getGenDepth();
/* 378 */     int j = paramRandomSource.nextInt(4);
/* 379 */     Direction direction = getOrientation();
/* 380 */     if (direction != null) {
/* 381 */       switch (MineshaftPieces.null.$SwitchMap$net$minecraft$core$Direction[direction.ordinal()]) {
/*     */         
/*     */         default:
/* 384 */           if (j <= 1) {
/* 385 */             MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX(), this.boundingBox.minY() - 1 + paramRandomSource.nextInt(3), this.boundingBox.minZ() - 1, direction, i); break;
/* 386 */           }  if (j == 2) {
/* 387 */             MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + paramRandomSource.nextInt(3), this.boundingBox.minZ(), Direction.WEST, i); break;
/*     */           } 
/* 389 */           MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + paramRandomSource.nextInt(3), this.boundingBox.minZ(), Direction.EAST, i);
/*     */           break;
/*     */         
/*     */         case 2:
/* 393 */           if (j <= 1) {
/* 394 */             MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX(), this.boundingBox.minY() - 1 + paramRandomSource.nextInt(3), this.boundingBox.maxZ() + 1, direction, i); break;
/* 395 */           }  if (j == 2) {
/* 396 */             MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + paramRandomSource.nextInt(3), this.boundingBox.maxZ() - 3, Direction.WEST, i); break;
/*     */           } 
/* 398 */           MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + paramRandomSource.nextInt(3), this.boundingBox.maxZ() - 3, Direction.EAST, i);
/*     */           break;
/*     */         
/*     */         case 3:
/* 402 */           if (j <= 1) {
/* 403 */             MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() - 1 + paramRandomSource.nextInt(3), this.boundingBox.minZ(), direction, i); break;
/* 404 */           }  if (j == 2) {
/* 405 */             MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX(), this.boundingBox.minY() - 1 + paramRandomSource.nextInt(3), this.boundingBox.minZ() - 1, Direction.NORTH, i); break;
/*     */           } 
/* 407 */           MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX(), this.boundingBox.minY() - 1 + paramRandomSource.nextInt(3), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
/*     */           break;
/*     */         
/*     */         case 4:
/* 411 */           if (j <= 1) {
/* 412 */             MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() - 1 + paramRandomSource.nextInt(3), this.boundingBox.minZ(), direction, i); break;
/* 413 */           }  if (j == 2) {
/* 414 */             MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.maxX() - 3, this.boundingBox.minY() - 1 + paramRandomSource.nextInt(3), this.boundingBox.minZ() - 1, Direction.NORTH, i); break;
/*     */           } 
/* 416 */           MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.maxX() - 3, this.boundingBox.minY() - 1 + paramRandomSource.nextInt(3), this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 423 */     if (i < 8) {
/* 424 */       if (direction == Direction.NORTH || direction == Direction.SOUTH) {
/* 425 */         for (int k = this.boundingBox.minZ() + 3; k + 3 <= this.boundingBox.maxZ(); k += 5) {
/* 426 */           int m = paramRandomSource.nextInt(5);
/* 427 */           if (m == 0) {
/* 428 */             MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY(), k, Direction.WEST, i + 1);
/* 429 */           } else if (m == 1) {
/* 430 */             MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY(), k, Direction.EAST, i + 1);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 434 */         for (int k = this.boundingBox.minX() + 3; k + 3 <= this.boundingBox.maxX(); k += 5) {
/* 435 */           int m = paramRandomSource.nextInt(5);
/* 436 */           if (m == 0) {
/* 437 */             MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, k, this.boundingBox.minY(), this.boundingBox.minZ() - 1, Direction.NORTH, i + 1);
/* 438 */           } else if (m == 1) {
/* 439 */             MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, k, this.boundingBox.minY(), this.boundingBox.maxZ() + 1, Direction.SOUTH, i + 1);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean createChest(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, ResourceKey<LootTable> paramResourceKey) {
/* 448 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(paramInt1, paramInt2, paramInt3);
/* 449 */     if (paramBoundingBox.isInside((Vec3i)mutableBlockPos) && 
/* 450 */       paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos).isAir() && !paramWorldGenLevel.getBlockState(mutableBlockPos.below()).isAir()) {
/* 451 */       BlockState blockState = (BlockState)Blocks.RAIL.defaultBlockState().setValue((Property)RailBlock.SHAPE, paramRandomSource.nextBoolean() ? (Comparable)RailShape.NORTH_SOUTH : (Comparable)RailShape.EAST_WEST);
/* 452 */       placeBlock(paramWorldGenLevel, blockState, paramInt1, paramInt2, paramInt3, paramBoundingBox);
/* 453 */       MinecartChest minecartChest = (MinecartChest)EntityType.CHEST_MINECART.create((Level)paramWorldGenLevel.getLevel(), EntitySpawnReason.CHUNK_GENERATION);
/* 454 */       if (minecartChest != null) {
/* 455 */         minecartChest.setInitialPos(mutableBlockPos.getX() + 0.5D, mutableBlockPos.getY() + 0.5D, mutableBlockPos.getZ() + 0.5D);
/* 456 */         minecartChest.setLootTable(paramResourceKey, paramRandomSource.nextLong());
/* 457 */         paramWorldGenLevel.addFreshEntity((Entity)minecartChest);
/*     */       } 
/* 459 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 463 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/* 468 */     if (isInInvalidLocation((LevelAccessor)paramWorldGenLevel, paramBoundingBox)) {
/*     */       return;
/*     */     }
/*     */     
/* 472 */     boolean bool1 = false;
/* 473 */     byte b1 = 2;
/* 474 */     boolean bool2 = false;
/* 475 */     byte b2 = 2;
/* 476 */     int i = this.numSections * 5 - 1;
/*     */     
/* 478 */     BlockState blockState = this.type.getPlanksState();
/*     */ 
/*     */     
/* 481 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 0, 0, 2, 1, i, CAVE_AIR, CAVE_AIR, false);
/* 482 */     generateMaybeBox(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 0.8F, 0, 2, 0, 2, 2, i, CAVE_AIR, CAVE_AIR, false, false);
/*     */     
/* 484 */     if (this.spiderCorridor) {
/* 485 */       generateMaybeBox(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 0.6F, 0, 0, 0, 2, 1, i, Blocks.COBWEB.defaultBlockState(), CAVE_AIR, false, true);
/*     */     }
/*     */     
/*     */     byte b3;
/* 489 */     for (b3 = 0; b3 < this.numSections; b3++) {
/* 490 */       int j = 2 + b3 * 5;
/*     */       
/* 492 */       placeSupport(paramWorldGenLevel, paramBoundingBox, 0, 0, j, 2, 2, paramRandomSource);
/*     */       
/* 494 */       maybePlaceCobWeb(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 0.1F, 0, 2, j - 1);
/* 495 */       maybePlaceCobWeb(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 0.1F, 2, 2, j - 1);
/* 496 */       maybePlaceCobWeb(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 0.1F, 0, 2, j + 1);
/* 497 */       maybePlaceCobWeb(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 0.1F, 2, 2, j + 1);
/* 498 */       maybePlaceCobWeb(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 0.05F, 0, 2, j - 2);
/* 499 */       maybePlaceCobWeb(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 0.05F, 2, 2, j - 2);
/* 500 */       maybePlaceCobWeb(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 0.05F, 0, 2, j + 2);
/* 501 */       maybePlaceCobWeb(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 0.05F, 2, 2, j + 2);
/*     */       
/* 503 */       if (paramRandomSource.nextInt(100) == 0) {
/* 504 */         createChest(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 2, 0, j - 1, BuiltInLootTables.ABANDONED_MINESHAFT);
/*     */       }
/* 506 */       if (paramRandomSource.nextInt(100) == 0) {
/* 507 */         createChest(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 0, 0, j + 1, BuiltInLootTables.ABANDONED_MINESHAFT);
/*     */       }
/* 509 */       if (this.spiderCorridor && !this.hasPlacedSpider) {
/* 510 */         boolean bool = true;
/* 511 */         int k = j - 1 + paramRandomSource.nextInt(3);
/* 512 */         BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(1, 0, k);
/*     */         
/* 514 */         if (paramBoundingBox.isInside((Vec3i)mutableBlockPos) && isInterior((LevelReader)paramWorldGenLevel, 1, 0, k, paramBoundingBox)) {
/* 515 */           this.hasPlacedSpider = true;
/* 516 */           paramWorldGenLevel.setBlock((BlockPos)mutableBlockPos, Blocks.SPAWNER.defaultBlockState(), 2);
/*     */           
/* 518 */           BlockEntity blockEntity = paramWorldGenLevel.getBlockEntity((BlockPos)mutableBlockPos);
/* 519 */           if (blockEntity instanceof SpawnerBlockEntity) { SpawnerBlockEntity spawnerBlockEntity = (SpawnerBlockEntity)blockEntity;
/* 520 */             spawnerBlockEntity.setEntityId(EntityType.CAVE_SPIDER, paramRandomSource); }
/*     */         
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 527 */     for (b3 = 0; b3 <= 2; b3++) {
/* 528 */       for (byte b = 0; b <= i; b++) {
/* 529 */         setPlanksBlock(paramWorldGenLevel, paramBoundingBox, blockState, b3, -1, b);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 534 */     b3 = 2;
/* 535 */     placeDoubleLowerOrUpperSupport(paramWorldGenLevel, paramBoundingBox, 0, -1, 2);
/* 536 */     if (this.numSections > 1) {
/* 537 */       int j = i - 2;
/* 538 */       placeDoubleLowerOrUpperSupport(paramWorldGenLevel, paramBoundingBox, 0, -1, j);
/*     */     } 
/*     */     
/* 541 */     if (this.hasRails) {
/* 542 */       BlockState blockState1 = (BlockState)Blocks.RAIL.defaultBlockState().setValue((Property)RailBlock.SHAPE, (Comparable)RailShape.NORTH_SOUTH);
/* 543 */       for (byte b = 0; b <= i; b++) {
/* 544 */         BlockState blockState2 = getBlock((BlockGetter)paramWorldGenLevel, 1, -1, b, paramBoundingBox);
/* 545 */         if (!blockState2.isAir() && blockState2.isSolidRender()) {
/* 546 */           float f = isInterior((LevelReader)paramWorldGenLevel, 1, 0, b, paramBoundingBox) ? 0.7F : 0.9F;
/* 547 */           maybeGenerateBlock(paramWorldGenLevel, paramBoundingBox, paramRandomSource, f, 1, 0, b, blockState1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeDoubleLowerOrUpperSupport(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, int paramInt1, int paramInt2, int paramInt3) {
/* 554 */     BlockState blockState1 = this.type.getWoodState();
/* 555 */     BlockState blockState2 = this.type.getPlanksState();
/* 556 */     if (getBlock((BlockGetter)paramWorldGenLevel, paramInt1, paramInt2, paramInt3, paramBoundingBox).is(blockState2.getBlock())) {
/* 557 */       fillPillarDownOrChainUp(paramWorldGenLevel, blockState1, paramInt1, paramInt2, paramInt3, paramBoundingBox);
/*     */     }
/* 559 */     if (getBlock((BlockGetter)paramWorldGenLevel, paramInt1 + 2, paramInt2, paramInt3, paramBoundingBox).is(blockState2.getBlock())) {
/* 560 */       fillPillarDownOrChainUp(paramWorldGenLevel, blockState1, paramInt1 + 2, paramInt2, paramInt3, paramBoundingBox);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void fillColumnDown(WorldGenLevel paramWorldGenLevel, BlockState paramBlockState, int paramInt1, int paramInt2, int paramInt3, BoundingBox paramBoundingBox) {
/* 566 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(paramInt1, paramInt2, paramInt3);
/* 567 */     if (!paramBoundingBox.isInside((Vec3i)mutableBlockPos)) {
/*     */       return;
/*     */     }
/*     */     
/* 571 */     int i = mutableBlockPos.getY();
/*     */ 
/*     */     
/* 574 */     while (isReplaceableByStructures(paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos)) && mutableBlockPos.getY() > paramWorldGenLevel.getMinY() + 1) {
/* 575 */       mutableBlockPos.move(Direction.DOWN);
/*     */     }
/* 577 */     if (!canPlaceColumnOnTopOf((LevelReader)paramWorldGenLevel, (BlockPos)mutableBlockPos, paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos))) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 582 */     while (mutableBlockPos.getY() < i) {
/* 583 */       mutableBlockPos.move(Direction.UP);
/* 584 */       paramWorldGenLevel.setBlock((BlockPos)mutableBlockPos, paramBlockState, 2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void fillPillarDownOrChainUp(WorldGenLevel paramWorldGenLevel, BlockState paramBlockState, int paramInt1, int paramInt2, int paramInt3, BoundingBox paramBoundingBox) {
/* 590 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(paramInt1, paramInt2, paramInt3);
/* 591 */     if (!paramBoundingBox.isInside((Vec3i)mutableBlockPos)) {
/*     */       return;
/*     */     }
/*     */     
/* 595 */     int i = mutableBlockPos.getY();
/*     */ 
/*     */     
/* 598 */     byte b = 1;
/*     */     
/* 600 */     boolean bool1 = true;
/* 601 */     boolean bool2 = true;
/* 602 */     while (bool1 || bool2) {
/* 603 */       if (bool1) {
/* 604 */         mutableBlockPos.setY(i - b);
/* 605 */         BlockState blockState = paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos);
/* 606 */         boolean bool = (isReplaceableByStructures(blockState) && !blockState.is(Blocks.LAVA)) ? true : false;
/* 607 */         if (!bool && canPlaceColumnOnTopOf((LevelReader)paramWorldGenLevel, (BlockPos)mutableBlockPos, blockState)) {
/* 608 */           fillColumnBetween(paramWorldGenLevel, paramBlockState, mutableBlockPos, i - b + 1, i);
/*     */           return;
/*     */         } 
/* 611 */         bool1 = (b <= 20 && bool && mutableBlockPos.getY() > paramWorldGenLevel.getMinY() + 1) ? true : false;
/*     */       } 
/*     */       
/* 614 */       if (bool2) {
/* 615 */         mutableBlockPos.setY(i + b);
/* 616 */         BlockState blockState = paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos);
/* 617 */         boolean bool = isReplaceableByStructures(blockState);
/* 618 */         if (!bool && canHangChainBelow((LevelReader)paramWorldGenLevel, (BlockPos)mutableBlockPos, blockState)) {
/*     */           
/* 620 */           paramWorldGenLevel.setBlock((BlockPos)mutableBlockPos.setY(i + 1), this.type.getFenceState(), 2);
/* 621 */           fillColumnBetween(paramWorldGenLevel, Blocks.IRON_CHAIN.defaultBlockState(), mutableBlockPos, i + 2, i + b);
/*     */           return;
/*     */         } 
/* 624 */         bool2 = (b <= 50 && bool && mutableBlockPos.getY() < paramWorldGenLevel.getMaxY()) ? true : false;
/*     */       } 
/*     */       
/* 627 */       b++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void fillColumnBetween(WorldGenLevel paramWorldGenLevel, BlockState paramBlockState, BlockPos.MutableBlockPos paramMutableBlockPos, int paramInt1, int paramInt2) {
/* 632 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 633 */       paramWorldGenLevel.setBlock((BlockPos)paramMutableBlockPos.setY(i), paramBlockState, 2);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean canPlaceColumnOnTopOf(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 638 */     return paramBlockState.isFaceSturdy((BlockGetter)paramLevelReader, paramBlockPos, Direction.UP);
/*     */   }
/*     */   
/*     */   private boolean canHangChainBelow(LevelReader paramLevelReader, BlockPos paramBlockPos, BlockState paramBlockState) {
/* 642 */     return (Block.canSupportCenter(paramLevelReader, paramBlockPos, Direction.DOWN) && !(paramBlockState.getBlock() instanceof net.minecraft.world.level.block.FallingBlock));
/*     */   }
/*     */ 
/*     */   
/*     */   private void placeSupport(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, RandomSource paramRandomSource) {
/* 647 */     if (!isSupportingBox((BlockGetter)paramWorldGenLevel, paramBoundingBox, paramInt1, paramInt5, paramInt4, paramInt3)) {
/*     */       return;
/*     */     }
/*     */     
/* 651 */     BlockState blockState1 = this.type.getPlanksState();
/* 652 */     BlockState blockState2 = this.type.getFenceState();
/*     */     
/* 654 */     generateBox(paramWorldGenLevel, paramBoundingBox, paramInt1, paramInt2, paramInt3, paramInt1, paramInt4 - 1, paramInt3, (BlockState)blockState2.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), CAVE_AIR, false);
/* 655 */     generateBox(paramWorldGenLevel, paramBoundingBox, paramInt5, paramInt2, paramInt3, paramInt5, paramInt4 - 1, paramInt3, (BlockState)blockState2.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), CAVE_AIR, false);
/* 656 */     if (paramRandomSource.nextInt(4) == 0) {
/* 657 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt1, paramInt4, paramInt3, paramInt1, paramInt4, paramInt3, blockState1, CAVE_AIR, false);
/* 658 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt5, paramInt4, paramInt3, paramInt5, paramInt4, paramInt3, blockState1, CAVE_AIR, false);
/*     */     } else {
/* 660 */       generateBox(paramWorldGenLevel, paramBoundingBox, paramInt1, paramInt4, paramInt3, paramInt5, paramInt4, paramInt3, blockState1, CAVE_AIR, false);
/* 661 */       maybeGenerateBlock(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 0.05F, paramInt1 + 1, paramInt4, paramInt3 - 1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.SOUTH));
/* 662 */       maybeGenerateBlock(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 0.05F, paramInt1 + 1, paramInt4, paramInt3 + 1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.NORTH));
/*     */     } 
/*     */   }
/*     */   
/*     */   private void maybePlaceCobWeb(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, RandomSource paramRandomSource, float paramFloat, int paramInt1, int paramInt2, int paramInt3) {
/* 667 */     if (isInterior((LevelReader)paramWorldGenLevel, paramInt1, paramInt2, paramInt3, paramBoundingBox) && paramRandomSource.nextFloat() < paramFloat && hasSturdyNeighbours(paramWorldGenLevel, paramBoundingBox, paramInt1, paramInt2, paramInt3, 2)) {
/* 668 */       placeBlock(paramWorldGenLevel, Blocks.COBWEB.defaultBlockState(), paramInt1, paramInt2, paramInt3, paramBoundingBox);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasSturdyNeighbours(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 673 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(paramInt1, paramInt2, paramInt3);
/* 674 */     byte b = 0;
/* 675 */     for (Direction direction : Direction.values()) {
/* 676 */       mutableBlockPos.move(direction);
/*     */       
/* 678 */       b++;
/* 679 */       if (paramBoundingBox.isInside((Vec3i)mutableBlockPos) && paramWorldGenLevel.getBlockState((BlockPos)mutableBlockPos).isFaceSturdy((BlockGetter)paramWorldGenLevel, (BlockPos)mutableBlockPos, direction.getOpposite()) && b >= paramInt4) {
/* 680 */         return true;
/*     */       }
/*     */       
/* 683 */       mutableBlockPos.move(direction.getOpposite());
/*     */     } 
/* 685 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\MineshaftPieces$MineShaftCorridor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */