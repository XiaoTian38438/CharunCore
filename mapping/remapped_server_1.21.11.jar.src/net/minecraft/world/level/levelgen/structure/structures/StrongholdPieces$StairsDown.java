/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StairsDown
/*     */   extends StrongholdPieces.StrongholdPiece
/*     */ {
/*     */   private static final int WIDTH = 5;
/*     */   private static final int HEIGHT = 11;
/*     */   private static final int DEPTH = 5;
/*     */   private final boolean isSource;
/*     */   
/*     */   public StairsDown(StructurePieceType paramStructurePieceType, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection) {
/* 443 */     super(paramStructurePieceType, paramInt1, makeBoundingBox(paramInt2, 64, paramInt3, paramDirection, 5, 11, 5));
/*     */     
/* 445 */     this.isSource = true;
/* 446 */     setOrientation(paramDirection);
/* 447 */     this.entryDoor = StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING;
/*     */   }
/*     */   
/*     */   public StairsDown(int paramInt, RandomSource paramRandomSource, BoundingBox paramBoundingBox, Direction paramDirection) {
/* 451 */     super(StructurePieceType.STRONGHOLD_STAIRS_DOWN, paramInt, paramBoundingBox);
/*     */     
/* 453 */     this.isSource = false;
/* 454 */     setOrientation(paramDirection);
/* 455 */     this.entryDoor = randomSmallDoor(paramRandomSource);
/*     */   }
/*     */   
/*     */   public StairsDown(StructurePieceType paramStructurePieceType, CompoundTag paramCompoundTag) {
/* 459 */     super(paramStructurePieceType, paramCompoundTag);
/* 460 */     this.isSource = paramCompoundTag.getBooleanOr("Source", false);
/*     */   }
/*     */   
/*     */   public StairsDown(CompoundTag paramCompoundTag) {
/* 464 */     this(StructurePieceType.STRONGHOLD_STAIRS_DOWN, paramCompoundTag);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/* 469 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/* 470 */     paramCompoundTag.putBoolean("Source", this.isSource);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChildren(StructurePiece paramStructurePiece, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource) {
/* 475 */     if (this.isSource)
/*     */     {
/* 477 */       StrongholdPieces.imposedPiece = (Class)StrongholdPieces.FiveCrossing.class;
/*     */     }
/* 479 */     generateSmallDoorChildForward((StrongholdPieces.StartPiece)paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, 1, 1);
/*     */   }
/*     */   
/*     */   public static StairsDown createPiece(StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection, int paramInt4) {
/* 483 */     BoundingBox boundingBox = BoundingBox.orientBox(paramInt1, paramInt2, paramInt3, -1, -7, 0, 5, 11, 5, paramDirection);
/*     */     
/* 485 */     if (!isOkBox(boundingBox) || paramStructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/* 486 */       return null;
/*     */     }
/*     */     
/* 489 */     return new StairsDown(paramInt4, paramRandomSource, boundingBox, paramDirection);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/* 495 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 0, 0, 4, 10, 4, true, paramRandomSource, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*     */     
/* 497 */     generateSmallDoor(paramWorldGenLevel, paramRandomSource, paramBoundingBox, this.entryDoor, 1, 7, 0);
/*     */     
/* 499 */     generateSmallDoor(paramWorldGenLevel, paramRandomSource, paramBoundingBox, StrongholdPieces.StrongholdPiece.SmallDoorType.OPENING, 1, 1, 4);
/*     */ 
/*     */     
/* 502 */     placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 2, 6, 1, paramBoundingBox);
/* 503 */     placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 1, paramBoundingBox);
/* 504 */     placeBlock(paramWorldGenLevel, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 6, 1, paramBoundingBox);
/* 505 */     placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 1, 5, 2, paramBoundingBox);
/* 506 */     placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, 3, paramBoundingBox);
/* 507 */     placeBlock(paramWorldGenLevel, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 5, 3, paramBoundingBox);
/* 508 */     placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, 3, paramBoundingBox);
/* 509 */     placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 3, paramBoundingBox);
/* 510 */     placeBlock(paramWorldGenLevel, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 4, 3, paramBoundingBox);
/* 511 */     placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 3, 3, 2, paramBoundingBox);
/* 512 */     placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 3, 2, 1, paramBoundingBox);
/* 513 */     placeBlock(paramWorldGenLevel, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 3, 3, 1, paramBoundingBox);
/* 514 */     placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 2, 2, 1, paramBoundingBox);
/* 515 */     placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 1, paramBoundingBox);
/* 516 */     placeBlock(paramWorldGenLevel, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 2, 1, paramBoundingBox);
/* 517 */     placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 1, 1, 2, paramBoundingBox);
/* 518 */     placeBlock(paramWorldGenLevel, Blocks.SMOOTH_STONE_SLAB.defaultBlockState(), 1, 1, 3, paramBoundingBox);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$StairsDown.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */