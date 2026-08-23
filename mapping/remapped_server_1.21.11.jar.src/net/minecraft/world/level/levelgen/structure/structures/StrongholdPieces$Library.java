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
/*      */ import net.minecraft.world.level.block.LadderBlock;
/*      */ import net.minecraft.world.level.block.WallTorchBlock;
/*      */ import net.minecraft.world.level.block.state.BlockState;
/*      */ import net.minecraft.world.level.block.state.properties.Property;
/*      */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*      */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*      */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*      */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*      */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Library
/*      */   extends StrongholdPieces.StrongholdPiece
/*      */ {
/*      */   protected static final int WIDTH = 14;
/*      */   protected static final int HEIGHT = 6;
/*      */   protected static final int TALL_HEIGHT = 11;
/*      */   protected static final int DEPTH = 15;
/*      */   private final boolean isTall;
/*      */   
/*      */   public Library(int paramInt, RandomSource paramRandomSource, BoundingBox paramBoundingBox, Direction paramDirection) {
/* 1069 */     super(StructurePieceType.STRONGHOLD_LIBRARY, paramInt, paramBoundingBox);
/*      */     
/* 1071 */     setOrientation(paramDirection);
/* 1072 */     this.entryDoor = randomSmallDoor(paramRandomSource);
/* 1073 */     this.isTall = (paramBoundingBox.getYSpan() > 6);
/*      */   }
/*      */   
/*      */   public Library(CompoundTag paramCompoundTag) {
/* 1077 */     super(StructurePieceType.STRONGHOLD_LIBRARY, paramCompoundTag);
/* 1078 */     this.isTall = paramCompoundTag.getBooleanOr("Tall", false);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/* 1083 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/* 1084 */     paramCompoundTag.putBoolean("Tall", this.isTall);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Library createPiece(StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection, int paramInt4) {
/* 1089 */     BoundingBox boundingBox = BoundingBox.orientBox(paramInt1, paramInt2, paramInt3, -4, -1, 0, 14, 11, 15, paramDirection);
/*      */     
/* 1091 */     if (!isOkBox(boundingBox) || paramStructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/*      */       
/* 1093 */       boundingBox = BoundingBox.orientBox(paramInt1, paramInt2, paramInt3, -4, -1, 0, 14, 6, 15, paramDirection);
/*      */       
/* 1095 */       if (!isOkBox(boundingBox) || paramStructurePieceAccessor.findCollisionPiece(boundingBox) != null) {
/* 1096 */         return null;
/*      */       }
/*      */     } 
/*      */     
/* 1100 */     return new Library(paramInt4, paramRandomSource, boundingBox, paramDirection);
/*      */   }
/*      */ 
/*      */   
/*      */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/* 1105 */     byte b1 = 11;
/* 1106 */     if (!this.isTall) {
/* 1107 */       b1 = 6;
/*      */     }
/*      */ 
/*      */     
/* 1111 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 0, 0, 13, b1 - 1, 14, true, paramRandomSource, StrongholdPieces.SMOOTH_STONE_SELECTOR);
/*      */     
/* 1113 */     generateSmallDoor(paramWorldGenLevel, paramRandomSource, paramBoundingBox, this.entryDoor, 4, 1, 0);
/*      */ 
/*      */     
/* 1116 */     generateMaybeBox(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 0.07F, 2, 1, 1, 11, 4, 13, Blocks.COBWEB.defaultBlockState(), Blocks.COBWEB.defaultBlockState(), false, false);
/*      */     
/* 1118 */     boolean bool = true;
/* 1119 */     byte b2 = 12;
/*      */     
/*      */     byte b3;
/* 1122 */     for (b3 = 1; b3 <= 13; b3++) {
/* 1123 */       if ((b3 - 1) % 4 == 0) {
/* 1124 */         generateBox(paramWorldGenLevel, paramBoundingBox, 1, 1, b3, 1, 4, b3, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1125 */         generateBox(paramWorldGenLevel, paramBoundingBox, 12, 1, b3, 12, 4, b3, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/*      */         
/* 1127 */         placeBlock(paramWorldGenLevel, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.EAST), 2, 3, b3, paramBoundingBox);
/* 1128 */         placeBlock(paramWorldGenLevel, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.WEST), 11, 3, b3, paramBoundingBox);
/*      */         
/* 1130 */         if (this.isTall) {
/* 1131 */           generateBox(paramWorldGenLevel, paramBoundingBox, 1, 6, b3, 1, 9, b3, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1132 */           generateBox(paramWorldGenLevel, paramBoundingBox, 12, 6, b3, 12, 9, b3, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/*      */         } 
/*      */       } else {
/* 1135 */         generateBox(paramWorldGenLevel, paramBoundingBox, 1, 1, b3, 1, 4, b3, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1136 */         generateBox(paramWorldGenLevel, paramBoundingBox, 12, 1, b3, 12, 4, b3, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */         
/* 1138 */         if (this.isTall) {
/* 1139 */           generateBox(paramWorldGenLevel, paramBoundingBox, 1, 6, b3, 1, 9, b3, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1140 */           generateBox(paramWorldGenLevel, paramBoundingBox, 12, 6, b3, 12, 9, b3, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1146 */     for (b3 = 3; b3 < 12; b3 += 2) {
/* 1147 */       generateBox(paramWorldGenLevel, paramBoundingBox, 3, 1, b3, 4, 3, b3, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1148 */       generateBox(paramWorldGenLevel, paramBoundingBox, 6, 1, b3, 7, 3, b3, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/* 1149 */       generateBox(paramWorldGenLevel, paramBoundingBox, 9, 1, b3, 10, 3, b3, Blocks.BOOKSHELF.defaultBlockState(), Blocks.BOOKSHELF.defaultBlockState(), false);
/*      */     } 
/*      */     
/* 1152 */     if (this.isTall) {
/*      */       
/* 1154 */       generateBox(paramWorldGenLevel, paramBoundingBox, 1, 5, 1, 3, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1155 */       generateBox(paramWorldGenLevel, paramBoundingBox, 10, 5, 1, 12, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1156 */       generateBox(paramWorldGenLevel, paramBoundingBox, 4, 5, 1, 9, 5, 2, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/* 1157 */       generateBox(paramWorldGenLevel, paramBoundingBox, 4, 5, 12, 9, 5, 13, Blocks.OAK_PLANKS.defaultBlockState(), Blocks.OAK_PLANKS.defaultBlockState(), false);
/*      */       
/* 1159 */       placeBlock(paramWorldGenLevel, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 11, paramBoundingBox);
/* 1160 */       placeBlock(paramWorldGenLevel, Blocks.OAK_PLANKS.defaultBlockState(), 8, 5, 11, paramBoundingBox);
/* 1161 */       placeBlock(paramWorldGenLevel, Blocks.OAK_PLANKS.defaultBlockState(), 9, 5, 10, paramBoundingBox);
/*      */       
/* 1163 */       BlockState blockState1 = (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1164 */       BlockState blockState2 = (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true));
/*      */ 
/*      */       
/* 1167 */       generateBox(paramWorldGenLevel, paramBoundingBox, 3, 6, 3, 3, 6, 11, blockState2, blockState2, false);
/* 1168 */       generateBox(paramWorldGenLevel, paramBoundingBox, 10, 6, 3, 10, 6, 9, blockState2, blockState2, false);
/* 1169 */       generateBox(paramWorldGenLevel, paramBoundingBox, 4, 6, 2, 9, 6, 2, blockState1, blockState1, false);
/* 1170 */       generateBox(paramWorldGenLevel, paramBoundingBox, 4, 6, 12, 7, 6, 12, blockState1, blockState1, false);
/*      */       
/* 1172 */       placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 3, 6, 2, paramBoundingBox);
/* 1173 */       placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 3, 6, 12, paramBoundingBox);
/* 1174 */       placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 10, 6, 2, paramBoundingBox);
/*      */       
/* 1176 */       for (byte b4 = 0; b4 <= 2; b4++) {
/* 1177 */         placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), 8 + b4, 6, 12 - b4, paramBoundingBox);
/* 1178 */         if (b4 != 2) {
/* 1179 */           placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), 8 + b4, 6, 11 - b4, paramBoundingBox);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1184 */       BlockState blockState3 = (BlockState)Blocks.LADDER.defaultBlockState().setValue((Property)LadderBlock.FACING, (Comparable)Direction.SOUTH);
/* 1185 */       placeBlock(paramWorldGenLevel, blockState3, 10, 1, 13, paramBoundingBox);
/* 1186 */       placeBlock(paramWorldGenLevel, blockState3, 10, 2, 13, paramBoundingBox);
/* 1187 */       placeBlock(paramWorldGenLevel, blockState3, 10, 3, 13, paramBoundingBox);
/* 1188 */       placeBlock(paramWorldGenLevel, blockState3, 10, 4, 13, paramBoundingBox);
/* 1189 */       placeBlock(paramWorldGenLevel, blockState3, 10, 5, 13, paramBoundingBox);
/* 1190 */       placeBlock(paramWorldGenLevel, blockState3, 10, 6, 13, paramBoundingBox);
/* 1191 */       placeBlock(paramWorldGenLevel, blockState3, 10, 7, 13, paramBoundingBox);
/*      */ 
/*      */       
/* 1194 */       byte b5 = 7;
/* 1195 */       byte b6 = 7;
/* 1196 */       BlockState blockState4 = (BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/* 1197 */       placeBlock(paramWorldGenLevel, blockState4, 6, 9, 7, paramBoundingBox);
/* 1198 */       BlockState blockState5 = (BlockState)Blocks.OAK_FENCE.defaultBlockState().setValue((Property)FenceBlock.WEST, Boolean.valueOf(true));
/* 1199 */       placeBlock(paramWorldGenLevel, blockState5, 7, 9, 7, paramBoundingBox);
/*      */       
/* 1201 */       placeBlock(paramWorldGenLevel, blockState4, 6, 8, 7, paramBoundingBox);
/* 1202 */       placeBlock(paramWorldGenLevel, blockState5, 7, 8, 7, paramBoundingBox);
/*      */       
/* 1204 */       BlockState blockState6 = (BlockState)((BlockState)blockState2.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true))).setValue((Property)FenceBlock.EAST, Boolean.valueOf(true));
/*      */       
/* 1206 */       placeBlock(paramWorldGenLevel, blockState6, 6, 7, 7, paramBoundingBox);
/* 1207 */       placeBlock(paramWorldGenLevel, blockState6, 7, 7, 7, paramBoundingBox);
/*      */       
/* 1209 */       placeBlock(paramWorldGenLevel, blockState4, 5, 7, 7, paramBoundingBox);
/*      */       
/* 1211 */       placeBlock(paramWorldGenLevel, blockState5, 8, 7, 7, paramBoundingBox);
/*      */       
/* 1213 */       placeBlock(paramWorldGenLevel, (BlockState)blockState4.setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 6, 7, 6, paramBoundingBox);
/* 1214 */       placeBlock(paramWorldGenLevel, (BlockState)blockState4.setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 6, 7, 8, paramBoundingBox);
/*      */       
/* 1216 */       placeBlock(paramWorldGenLevel, (BlockState)blockState5.setValue((Property)FenceBlock.NORTH, Boolean.valueOf(true)), 7, 7, 6, paramBoundingBox);
/* 1217 */       placeBlock(paramWorldGenLevel, (BlockState)blockState5.setValue((Property)FenceBlock.SOUTH, Boolean.valueOf(true)), 7, 7, 8, paramBoundingBox);
/*      */       
/* 1219 */       BlockState blockState7 = Blocks.TORCH.defaultBlockState();
/* 1220 */       placeBlock(paramWorldGenLevel, blockState7, 5, 8, 7, paramBoundingBox);
/* 1221 */       placeBlock(paramWorldGenLevel, blockState7, 8, 8, 7, paramBoundingBox);
/* 1222 */       placeBlock(paramWorldGenLevel, blockState7, 6, 8, 6, paramBoundingBox);
/* 1223 */       placeBlock(paramWorldGenLevel, blockState7, 6, 8, 8, paramBoundingBox);
/* 1224 */       placeBlock(paramWorldGenLevel, blockState7, 7, 8, 6, paramBoundingBox);
/* 1225 */       placeBlock(paramWorldGenLevel, blockState7, 7, 8, 8, paramBoundingBox);
/*      */     } 
/*      */ 
/*      */     
/* 1229 */     createChest(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 3, 3, 5, BuiltInLootTables.STRONGHOLD_LIBRARY);
/* 1230 */     if (this.isTall) {
/* 1231 */       placeBlock(paramWorldGenLevel, CAVE_AIR, 12, 9, 1, paramBoundingBox);
/* 1232 */       createChest(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 12, 8, 1, BuiltInLootTables.STRONGHOLD_LIBRARY);
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$Library.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */