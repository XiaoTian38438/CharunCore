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
/*     */ public class FillerCorridor
/*     */   extends StrongholdPieces.StrongholdPiece
/*     */ {
/*     */   private final int steps;
/*     */   
/*     */   public FillerCorridor(int paramInt, BoundingBox paramBoundingBox, Direction paramDirection) {
/* 364 */     super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, paramInt, paramBoundingBox);
/*     */     
/* 366 */     setOrientation(paramDirection);
/* 367 */     this.steps = (paramDirection == Direction.NORTH || paramDirection == Direction.SOUTH) ? paramBoundingBox.getZSpan() : paramBoundingBox.getXSpan();
/*     */   }
/*     */   
/*     */   public FillerCorridor(CompoundTag paramCompoundTag) {
/* 371 */     super(StructurePieceType.STRONGHOLD_FILLER_CORRIDOR, paramCompoundTag);
/* 372 */     this.steps = paramCompoundTag.getIntOr("Steps", 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/* 377 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/* 378 */     paramCompoundTag.putInt("Steps", this.steps);
/*     */   }
/*     */   
/*     */   public static BoundingBox findPieceBox(StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection) {
/* 382 */     byte b = 3;
/*     */     
/* 384 */     BoundingBox boundingBox = BoundingBox.orientBox(paramInt1, paramInt2, paramInt3, -1, -1, 0, 5, 5, 4, paramDirection);
/*     */     
/* 386 */     StructurePiece structurePiece = paramStructurePieceAccessor.findCollisionPiece(boundingBox);
/* 387 */     if (structurePiece == null)
/*     */     {
/* 389 */       return null;
/*     */     }
/*     */     
/* 392 */     if (structurePiece.getBoundingBox().minY() == boundingBox.minY())
/*     */     {
/* 394 */       for (byte b1 = 2; b1 >= 1; b1--) {
/* 395 */         boundingBox = BoundingBox.orientBox(paramInt1, paramInt2, paramInt3, -1, -1, 0, 5, 5, b1, paramDirection);
/* 396 */         if (!structurePiece.getBoundingBox().intersects(boundingBox))
/*     */         {
/*     */           
/* 399 */           return BoundingBox.orientBox(paramInt1, paramInt2, paramInt3, -1, -1, 0, 5, 5, b1 + 1, paramDirection);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 404 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/* 410 */     for (byte b = 0; b < this.steps; b++) {
/*     */       
/* 412 */       placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 0, 0, b, paramBoundingBox);
/* 413 */       placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 1, 0, b, paramBoundingBox);
/* 414 */       placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 2, 0, b, paramBoundingBox);
/* 415 */       placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 3, 0, b, paramBoundingBox);
/* 416 */       placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 4, 0, b, paramBoundingBox);
/*     */       
/* 418 */       for (byte b1 = 1; b1 <= 3; b1++) {
/* 419 */         placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 0, b1, b, paramBoundingBox);
/* 420 */         placeBlock(paramWorldGenLevel, Blocks.CAVE_AIR.defaultBlockState(), 1, b1, b, paramBoundingBox);
/* 421 */         placeBlock(paramWorldGenLevel, Blocks.CAVE_AIR.defaultBlockState(), 2, b1, b, paramBoundingBox);
/* 422 */         placeBlock(paramWorldGenLevel, Blocks.CAVE_AIR.defaultBlockState(), 3, b1, b, paramBoundingBox);
/* 423 */         placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 4, b1, b, paramBoundingBox);
/*     */       } 
/*     */       
/* 426 */       placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 0, 4, b, paramBoundingBox);
/* 427 */       placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 1, 4, b, paramBoundingBox);
/* 428 */       placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 2, 4, b, paramBoundingBox);
/* 429 */       placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 3, 4, b, paramBoundingBox);
/* 430 */       placeBlock(paramWorldGenLevel, Blocks.STONE_BRICKS.defaultBlockState(), 4, 4, b, paramBoundingBox);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$FillerCorridor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */