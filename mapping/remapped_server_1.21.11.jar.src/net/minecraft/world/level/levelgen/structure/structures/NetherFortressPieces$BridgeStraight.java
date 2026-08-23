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
/*     */ import net.minecraft.world.level.block.FenceBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
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
/*     */ public class BridgeStraight
/*     */   extends NetherFortressPieces.NetherBridgePiece
/*     */ {
/*     */   private static final int WIDTH = 5;
/*     */   private static final int HEIGHT = 10;
/*     */   private static final int DEPTH = 19;
/*     */   
/*     */   public BridgeStraight(int paramInt, RandomSource paramRandomSource, BoundingBox paramBoundingBox, Direction paramDirection) {
/* 280 */     super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, paramInt, paramBoundingBox);
/*     */     
/* 282 */     setOrientation(paramDirection);
/*     */   }
/*     */   
/*     */   public BridgeStraight(CompoundTag paramCompoundTag) {
/* 286 */     super(StructurePieceType.NETHER_FORTRESS_BRIDGE_STRAIGHT, paramCompoundTag);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChildren(StructurePiece paramStructurePiece, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource) {
/* 291 */     generateChildForward((NetherFortressPieces.StartPiece)paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, 1, 3, false);
/*     */   }
/*     */   
/*     */   public static BridgeStraight createPiece(StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection, int paramInt4) {
/* 295 */     BoundingBox boundingBox = BoundingBox.orientBox(paramInt1, paramInt2, paramInt3, -1, -3, 0, 5, 10, 19, paramDirection);
/*     */     
/* 297 */     if (!isOkBox(boundingBox) || paramStructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/* 298 */       return null;
/*     */     }
/*     */     
/* 301 */     return new BridgeStraight(paramInt4, paramRandomSource, boundingBox, paramDirection);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/* 307 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 3, 0, 4, 4, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*     */     
/* 309 */     generateBox(paramWorldGenLevel, paramBoundingBox, 1, 5, 0, 3, 7, 18, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*     */ 
/*     */     
/* 312 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 5, 0, 0, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 313 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 5, 0, 4, 5, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*     */ 
/*     */     
/* 316 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 2, 0, 4, 2, 5, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 317 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 2, 13, 4, 2, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 318 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 0, 0, 4, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 319 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 0, 15, 4, 1, 18, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*     */     
/* 321 */     for (byte b = 0; b <= 4; b++) {
/* 322 */       for (byte b1 = 0; b1 <= 2; b1++) {
/* 323 */         fillColumnDown(paramWorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, b1, paramBoundingBox);
/* 324 */         fillColumnDown(paramWorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b, -1, 18 - b1, paramBoundingBox);
/*     */       } 
/*     */     } 
/*     */     
/* 328 */     BlockState blockState1 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/* 329 */     BlockState blockState2 = (BlockState)blockState1.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 330 */     BlockState blockState3 = (BlockState)blockState1.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true));
/* 331 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 1, 0, 4, 1, blockState2, blockState2, false);
/* 332 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 3, 4, 0, 4, 4, blockState2, blockState2, false);
/* 333 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 3, 14, 0, 4, 14, blockState2, blockState2, false);
/* 334 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 1, 17, 0, 4, 17, blockState2, blockState2, false);
/* 335 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 1, 1, 4, 4, 1, blockState3, blockState3, false);
/* 336 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 3, 4, 4, 4, 4, blockState3, blockState3, false);
/* 337 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 3, 14, 4, 4, 14, blockState3, blockState3, false);
/* 338 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 1, 17, 4, 4, 17, blockState3, blockState3, false);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$BridgeStraight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */