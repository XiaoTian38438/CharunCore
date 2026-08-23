/*      */ package net.minecraft.world.level.levelgen.structure.structures;
/*      */ 
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.StructureManager;
/*      */ import net.minecraft.world.level.WorldGenLevel;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.block.FenceBlock;
/*      */ import net.minecraft.world.level.block.StairBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CastleStalkRoom
/*      */   extends NetherFortressPieces.NetherBridgePiece
/*      */ {
/*      */   private static final int WIDTH = 13;
/*      */   private static final int HEIGHT = 14;
/*      */   private static final int DEPTH = 13;
/*      */   
/*      */   public CastleStalkRoom(int paramInt, BoundingBox paramBoundingBox, Direction paramDirection) {
/*  872 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, paramInt, paramBoundingBox);
/*      */     
/*  874 */     setOrientation(paramDirection);
/*      */   }
/*      */   
/*      */   public CastleStalkRoom(CompoundTag paramCompoundTag) {
/*  878 */     super(StructurePieceType.NETHER_FORTRESS_CASTLE_STALK_ROOM, paramCompoundTag);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addChildren(StructurePiece paramStructurePiece, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource) {
/*  883 */     generateChildForward((NetherFortressPieces.StartPiece)paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, 5, 3, true);
/*  884 */     generateChildForward((NetherFortressPieces.StartPiece)paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, 5, 11, true);
/*      */   }
/*      */   
/*      */   public static CastleStalkRoom createPiece(StructurePieceAccessor paramStructurePieceAccessor, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection, int paramInt4) {
/*  888 */     BoundingBox boundingBox = BoundingBox.orientBox(paramInt1, paramInt2, paramInt3, -5, -3, 0, 13, 14, 13, paramDirection);
/*      */     
/*  890 */     if (!isOkBox(boundingBox) || paramStructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/*  891 */       return null;
/*      */     }
/*      */     
/*  894 */     return new CastleStalkRoom(paramInt4, boundingBox, paramDirection);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/*  900 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 3, 0, 12, 4, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */     
/*  902 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 5, 0, 12, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */     
/*  905 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 5, 0, 1, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  906 */     generateBox(paramWorldGenLevel, paramBoundingBox, 11, 5, 0, 12, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  907 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 5, 11, 4, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  908 */     generateBox(paramWorldGenLevel, paramBoundingBox, 8, 5, 11, 10, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  909 */     generateBox(paramWorldGenLevel, paramBoundingBox, 5, 9, 11, 7, 12, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  910 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 5, 0, 4, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  911 */     generateBox(paramWorldGenLevel, paramBoundingBox, 8, 5, 0, 10, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  912 */     generateBox(paramWorldGenLevel, paramBoundingBox, 5, 9, 0, 7, 12, 1, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */ 
/*      */     
/*  915 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 11, 2, 10, 12, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */     
/*  917 */     BlockState blockState1 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*  918 */     BlockState blockState2 = (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*  919 */     BlockState blockState3 = (BlockState)blockState2.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true));
/*  920 */     BlockState blockState4 = (BlockState)blockState2.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*      */     
/*      */     byte b1;
/*  923 */     for (b1 = 1; b1 <= 11; b1 += 2) {
/*  924 */       generateBox(paramWorldGenLevel, paramBoundingBox, b1, 10, 0, b1, 11, 0, blockState1, blockState1, false);
/*  925 */       generateBox(paramWorldGenLevel, paramBoundingBox, b1, 10, 12, b1, 11, 12, blockState1, blockState1, false);
/*  926 */       generateBox(paramWorldGenLevel, paramBoundingBox, 0, 10, b1, 0, 11, b1, blockState2, blockState2, false);
/*  927 */       generateBox(paramWorldGenLevel, paramBoundingBox, 12, 10, b1, 12, 11, b1, blockState2, blockState2, false);
/*  928 */       placeBlock(paramWorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b1, 13, 0, paramBoundingBox);
/*  929 */       placeBlock(paramWorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b1, 13, 12, paramBoundingBox);
/*  930 */       placeBlock(paramWorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), 0, 13, b1, paramBoundingBox);
/*  931 */       placeBlock(paramWorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), 12, 13, b1, paramBoundingBox);
/*  932 */       if (b1 != 11) {
/*  933 */         placeBlock(paramWorldGenLevel, blockState1, b1 + 1, 13, 0, paramBoundingBox);
/*  934 */         placeBlock(paramWorldGenLevel, blockState1, b1 + 1, 13, 12, paramBoundingBox);
/*  935 */         placeBlock(paramWorldGenLevel, blockState2, 0, 13, b1 + 1, paramBoundingBox);
/*  936 */         placeBlock(paramWorldGenLevel, blockState2, 12, 13, b1 + 1, paramBoundingBox);
/*      */       } 
/*      */     } 
/*  939 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 0, paramBoundingBox);
/*  940 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 0, 13, 12, paramBoundingBox);
/*  941 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 12, paramBoundingBox);
/*  942 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.NETHER_BRICK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 12, 13, 0, paramBoundingBox);
/*      */ 
/*      */     
/*  945 */     for (b1 = 3; b1 <= 9; b1 += 2) {
/*  946 */       generateBox(paramWorldGenLevel, paramBoundingBox, 1, 7, b1, 1, 8, b1, blockState3, blockState3, false);
/*  947 */       generateBox(paramWorldGenLevel, paramBoundingBox, 11, 7, b1, 11, 8, b1, blockState4, blockState4, false);
/*      */     } 
/*      */ 
/*      */     
/*  951 */     BlockState blockState5 = (BlockState)Blocks.NETHER_BRICK_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.NORTH); byte b2;
/*  952 */     for (b2 = 0; b2 <= 6; b2++) {
/*  953 */       int i = b2 + 4;
/*  954 */       for (byte b = 5; b <= 7; b++) {
/*  955 */         placeBlock(paramWorldGenLevel, blockState5, b, 5 + b2, i, paramBoundingBox);
/*      */       }
/*  957 */       if (i >= 5 && i <= 8) {
/*  958 */         generateBox(paramWorldGenLevel, paramBoundingBox, 5, 5, i, 7, b2 + 4, i, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  959 */       } else if (i >= 9 && i <= 10) {
/*  960 */         generateBox(paramWorldGenLevel, paramBoundingBox, 5, 8, i, 7, b2 + 4, i, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */       } 
/*  962 */       if (b2 >= 1) {
/*  963 */         generateBox(paramWorldGenLevel, paramBoundingBox, 5, 6 + b2, i, 7, 9 + b2, i, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */       }
/*      */     } 
/*  966 */     for (b2 = 5; b2 <= 7; b2++) {
/*  967 */       placeBlock(paramWorldGenLevel, blockState5, b2, 12, 11, paramBoundingBox);
/*      */     }
/*  969 */     generateBox(paramWorldGenLevel, paramBoundingBox, 5, 6, 7, 5, 7, 7, blockState4, blockState4, false);
/*  970 */     generateBox(paramWorldGenLevel, paramBoundingBox, 7, 6, 7, 7, 7, 7, blockState3, blockState3, false);
/*  971 */     generateBox(paramWorldGenLevel, paramBoundingBox, 5, 13, 12, 7, 13, 12, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*      */ 
/*      */     
/*  974 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 5, 2, 3, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  975 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 5, 9, 3, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  976 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 5, 4, 2, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  977 */     generateBox(paramWorldGenLevel, paramBoundingBox, 9, 5, 2, 10, 5, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  978 */     generateBox(paramWorldGenLevel, paramBoundingBox, 9, 5, 9, 10, 5, 10, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  979 */     generateBox(paramWorldGenLevel, paramBoundingBox, 10, 5, 4, 10, 5, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  980 */     BlockState blockState6 = (BlockState)blockState5.setValue((Property)StairBlock.FACING, (Comparable)Direction.EAST);
/*  981 */     BlockState blockState7 = (BlockState)blockState5.setValue((Property)StairBlock.FACING, (Comparable)Direction.WEST);
/*  982 */     placeBlock(paramWorldGenLevel, blockState7, 4, 5, 2, paramBoundingBox);
/*  983 */     placeBlock(paramWorldGenLevel, blockState7, 4, 5, 3, paramBoundingBox);
/*  984 */     placeBlock(paramWorldGenLevel, blockState7, 4, 5, 9, paramBoundingBox);
/*  985 */     placeBlock(paramWorldGenLevel, blockState7, 4, 5, 10, paramBoundingBox);
/*  986 */     placeBlock(paramWorldGenLevel, blockState6, 8, 5, 2, paramBoundingBox);
/*  987 */     placeBlock(paramWorldGenLevel, blockState6, 8, 5, 3, paramBoundingBox);
/*  988 */     placeBlock(paramWorldGenLevel, blockState6, 8, 5, 9, paramBoundingBox);
/*  989 */     placeBlock(paramWorldGenLevel, blockState6, 8, 5, 10, paramBoundingBox);
/*      */ 
/*      */     
/*  992 */     generateBox(paramWorldGenLevel, paramBoundingBox, 3, 4, 4, 4, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
/*  993 */     generateBox(paramWorldGenLevel, paramBoundingBox, 8, 4, 4, 9, 4, 8, Blocks.SOUL_SAND.defaultBlockState(), Blocks.SOUL_SAND.defaultBlockState(), false);
/*  994 */     generateBox(paramWorldGenLevel, paramBoundingBox, 3, 5, 4, 4, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
/*  995 */     generateBox(paramWorldGenLevel, paramBoundingBox, 8, 5, 4, 9, 5, 8, Blocks.NETHER_WART.defaultBlockState(), Blocks.NETHER_WART.defaultBlockState(), false);
/*      */ 
/*      */     
/*  998 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 2, 0, 8, 2, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*  999 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 2, 4, 12, 2, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */     
/* 1001 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 0, 0, 8, 1, 3, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1002 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 0, 9, 8, 1, 12, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1003 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 0, 4, 3, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/* 1004 */     generateBox(paramWorldGenLevel, paramBoundingBox, 9, 0, 4, 12, 1, 8, Blocks.NETHER_BRICKS.defaultBlockState(), Blocks.NETHER_BRICKS.defaultBlockState(), false);
/*      */     byte b3;
/* 1006 */     for (b3 = 4; b3 <= 8; b3++) {
/* 1007 */       for (byte b = 0; b <= 2; b++) {
/* 1008 */         fillColumnDown(paramWorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b3, -1, b, paramBoundingBox);
/* 1009 */         fillColumnDown(paramWorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b3, -1, 12 - b, paramBoundingBox);
/*      */       } 
/*      */     } 
/* 1012 */     for (b3 = 0; b3 <= 2; b3++) {
/* 1013 */       for (byte b = 4; b <= 8; b++) {
/* 1014 */         fillColumnDown(paramWorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), b3, -1, b, paramBoundingBox);
/* 1015 */         fillColumnDown(paramWorldGenLevel, Blocks.NETHER_BRICKS.defaultBlockState(), 12 - b3, -1, b, paramBoundingBox);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$CastleStalkRoom.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */