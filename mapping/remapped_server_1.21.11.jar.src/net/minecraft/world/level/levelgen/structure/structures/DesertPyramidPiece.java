/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.StairBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class DesertPyramidPiece extends ScatteredFeaturePiece {
/*     */   public static final int WIDTH = 21;
/*  27 */   private final boolean[] hasPlacedChest = new boolean[4]; public static final int DEPTH = 21;
/*  28 */   private final List<BlockPos> potentialSuspiciousSandWorldPositions = new ArrayList<>();
/*  29 */   private BlockPos randomCollapsedRoofPos = BlockPos.ZERO;
/*     */   
/*     */   public DesertPyramidPiece(RandomSource paramRandomSource, int paramInt1, int paramInt2) {
/*  32 */     super(StructurePieceType.DESERT_PYRAMID_PIECE, paramInt1, 64, paramInt2, 21, 15, 21, getRandomHorizontalDirection(paramRandomSource));
/*     */   }
/*     */   
/*     */   public DesertPyramidPiece(CompoundTag paramCompoundTag) {
/*  36 */     super(StructurePieceType.DESERT_PYRAMID_PIECE, paramCompoundTag);
/*  37 */     this.hasPlacedChest[0] = paramCompoundTag.getBooleanOr("hasPlacedChest0", false);
/*  38 */     this.hasPlacedChest[1] = paramCompoundTag.getBooleanOr("hasPlacedChest1", false);
/*  39 */     this.hasPlacedChest[2] = paramCompoundTag.getBooleanOr("hasPlacedChest2", false);
/*  40 */     this.hasPlacedChest[3] = paramCompoundTag.getBooleanOr("hasPlacedChest3", false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/*  45 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/*  46 */     paramCompoundTag.putBoolean("hasPlacedChest0", this.hasPlacedChest[0]);
/*  47 */     paramCompoundTag.putBoolean("hasPlacedChest1", this.hasPlacedChest[1]);
/*  48 */     paramCompoundTag.putBoolean("hasPlacedChest2", this.hasPlacedChest[2]);
/*  49 */     paramCompoundTag.putBoolean("hasPlacedChest3", this.hasPlacedChest[3]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/*  54 */     if (!updateHeightPositionToLowestGroundHeight((LevelAccessor)paramWorldGenLevel, -paramRandomSource.nextInt(3))) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  59 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, -4, 0, this.width - 1, 0, this.depth - 1, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false); byte b;
/*  60 */     for (b = 1; b <= 9; b++) {
/*  61 */       generateBox(paramWorldGenLevel, paramBoundingBox, b, b, b, this.width - 1 - b, b, this.depth - 1 - b, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/*  62 */       generateBox(paramWorldGenLevel, paramBoundingBox, b + 1, b, b + 1, this.width - 2 - b, b, this.depth - 2 - b, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*     */     } 
/*  64 */     for (b = 0; b < this.width; b++) {
/*  65 */       for (byte b1 = 0; b1 < this.depth; b1++) {
/*  66 */         byte b2 = -5;
/*  67 */         fillColumnDown(paramWorldGenLevel, Blocks.SANDSTONE.defaultBlockState(), b, -5, b1, paramBoundingBox);
/*     */       } 
/*     */     } 
/*     */     
/*  71 */     BlockState blockState1 = (BlockState)Blocks.SANDSTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.NORTH);
/*  72 */     BlockState blockState2 = (BlockState)Blocks.SANDSTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.SOUTH);
/*  73 */     BlockState blockState3 = (BlockState)Blocks.SANDSTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.EAST);
/*  74 */     BlockState blockState4 = (BlockState)Blocks.SANDSTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.WEST);
/*     */ 
/*     */     
/*  77 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, 0, 0, 4, 9, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  78 */     generateBox(paramWorldGenLevel, paramBoundingBox, 1, 10, 1, 3, 10, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/*  79 */     placeBlock(paramWorldGenLevel, blockState1, 2, 10, 0, paramBoundingBox);
/*  80 */     placeBlock(paramWorldGenLevel, blockState2, 2, 10, 4, paramBoundingBox);
/*  81 */     placeBlock(paramWorldGenLevel, blockState3, 0, 10, 2, paramBoundingBox);
/*  82 */     placeBlock(paramWorldGenLevel, blockState4, 4, 10, 2, paramBoundingBox);
/*  83 */     generateBox(paramWorldGenLevel, paramBoundingBox, this.width - 5, 0, 0, this.width - 1, 9, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  84 */     generateBox(paramWorldGenLevel, paramBoundingBox, this.width - 4, 10, 1, this.width - 2, 10, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/*  85 */     placeBlock(paramWorldGenLevel, blockState1, this.width - 3, 10, 0, paramBoundingBox);
/*  86 */     placeBlock(paramWorldGenLevel, blockState2, this.width - 3, 10, 4, paramBoundingBox);
/*  87 */     placeBlock(paramWorldGenLevel, blockState3, this.width - 5, 10, 2, paramBoundingBox);
/*  88 */     placeBlock(paramWorldGenLevel, blockState4, this.width - 1, 10, 2, paramBoundingBox);
/*     */ 
/*     */     
/*  91 */     generateBox(paramWorldGenLevel, paramBoundingBox, 8, 0, 0, 12, 4, 4, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  92 */     generateBox(paramWorldGenLevel, paramBoundingBox, 9, 1, 0, 11, 3, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*  93 */     placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 1, 1, paramBoundingBox);
/*  94 */     placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 2, 1, paramBoundingBox);
/*  95 */     placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), 9, 3, 1, paramBoundingBox);
/*  96 */     placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, 3, 1, paramBoundingBox);
/*  97 */     placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 3, 1, paramBoundingBox);
/*  98 */     placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 2, 1, paramBoundingBox);
/*  99 */     placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), 11, 1, 1, paramBoundingBox);
/*     */ 
/*     */     
/* 102 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 1, 1, 8, 3, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 103 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 1, 2, 8, 2, 2, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 104 */     generateBox(paramWorldGenLevel, paramBoundingBox, 12, 1, 1, 16, 3, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 105 */     generateBox(paramWorldGenLevel, paramBoundingBox, 12, 1, 2, 16, 2, 2, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/*     */ 
/*     */     
/* 108 */     generateBox(paramWorldGenLevel, paramBoundingBox, 5, 4, 5, this.width - 6, 4, this.depth - 6, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 109 */     generateBox(paramWorldGenLevel, paramBoundingBox, 9, 4, 9, 11, 4, 11, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 110 */     generateBox(paramWorldGenLevel, paramBoundingBox, 8, 1, 8, 8, 3, 8, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 111 */     generateBox(paramWorldGenLevel, paramBoundingBox, 12, 1, 8, 12, 3, 8, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 112 */     generateBox(paramWorldGenLevel, paramBoundingBox, 8, 1, 12, 8, 3, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 113 */     generateBox(paramWorldGenLevel, paramBoundingBox, 12, 1, 12, 12, 3, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/*     */ 
/*     */     
/* 116 */     generateBox(paramWorldGenLevel, paramBoundingBox, 1, 1, 5, 4, 4, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 117 */     generateBox(paramWorldGenLevel, paramBoundingBox, this.width - 5, 1, 5, this.width - 2, 4, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 118 */     generateBox(paramWorldGenLevel, paramBoundingBox, 6, 7, 9, 6, 7, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 119 */     generateBox(paramWorldGenLevel, paramBoundingBox, this.width - 7, 7, 9, this.width - 7, 7, 11, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 120 */     generateBox(paramWorldGenLevel, paramBoundingBox, 5, 5, 9, 5, 7, 11, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 121 */     generateBox(paramWorldGenLevel, paramBoundingBox, this.width - 6, 5, 9, this.width - 6, 7, 11, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 122 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 5, 5, 10, paramBoundingBox);
/* 123 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 5, 6, 10, paramBoundingBox);
/* 124 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 6, 6, 10, paramBoundingBox);
/* 125 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), this.width - 6, 5, 10, paramBoundingBox);
/* 126 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), this.width - 6, 6, 10, paramBoundingBox);
/* 127 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), this.width - 7, 6, 10, paramBoundingBox);
/*     */ 
/*     */     
/* 130 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 4, 4, 2, 6, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 131 */     generateBox(paramWorldGenLevel, paramBoundingBox, this.width - 3, 4, 4, this.width - 3, 6, 4, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 132 */     placeBlock(paramWorldGenLevel, blockState1, 2, 4, 5, paramBoundingBox);
/* 133 */     placeBlock(paramWorldGenLevel, blockState1, 2, 3, 4, paramBoundingBox);
/* 134 */     placeBlock(paramWorldGenLevel, blockState1, this.width - 3, 4, 5, paramBoundingBox);
/* 135 */     placeBlock(paramWorldGenLevel, blockState1, this.width - 3, 3, 4, paramBoundingBox);
/* 136 */     generateBox(paramWorldGenLevel, paramBoundingBox, 1, 1, 3, 2, 2, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 137 */     generateBox(paramWorldGenLevel, paramBoundingBox, this.width - 3, 1, 3, this.width - 2, 2, 3, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 138 */     placeBlock(paramWorldGenLevel, Blocks.SANDSTONE.defaultBlockState(), 1, 1, 2, paramBoundingBox);
/* 139 */     placeBlock(paramWorldGenLevel, Blocks.SANDSTONE.defaultBlockState(), this.width - 2, 1, 2, paramBoundingBox);
/* 140 */     placeBlock(paramWorldGenLevel, Blocks.SANDSTONE_SLAB.defaultBlockState(), 1, 2, 2, paramBoundingBox);
/* 141 */     placeBlock(paramWorldGenLevel, Blocks.SANDSTONE_SLAB.defaultBlockState(), this.width - 2, 2, 2, paramBoundingBox);
/* 142 */     placeBlock(paramWorldGenLevel, blockState4, 2, 1, 2, paramBoundingBox);
/* 143 */     placeBlock(paramWorldGenLevel, blockState3, this.width - 3, 1, 2, paramBoundingBox);
/*     */ 
/*     */     
/* 146 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 3, 5, 4, 3, 17, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 147 */     generateBox(paramWorldGenLevel, paramBoundingBox, this.width - 5, 3, 5, this.width - 5, 3, 17, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 148 */     generateBox(paramWorldGenLevel, paramBoundingBox, 3, 1, 5, 4, 2, 16, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 149 */     generateBox(paramWorldGenLevel, paramBoundingBox, this.width - 6, 1, 5, this.width - 5, 2, 16, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false); int i;
/* 150 */     for (i = 5; i <= 17; i += 2) {
/* 151 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), 4, 1, i, paramBoundingBox);
/* 152 */       placeBlock(paramWorldGenLevel, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 4, 2, i, paramBoundingBox);
/* 153 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), this.width - 5, 1, i, paramBoundingBox);
/* 154 */       placeBlock(paramWorldGenLevel, Blocks.CHISELED_SANDSTONE.defaultBlockState(), this.width - 5, 2, i, paramBoundingBox);
/*     */     } 
/* 156 */     placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 7, paramBoundingBox);
/* 157 */     placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 8, paramBoundingBox);
/* 158 */     placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 0, 9, paramBoundingBox);
/* 159 */     placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 0, 9, paramBoundingBox);
/* 160 */     placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 8, 0, 10, paramBoundingBox);
/* 161 */     placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 12, 0, 10, paramBoundingBox);
/* 162 */     placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 7, 0, 10, paramBoundingBox);
/* 163 */     placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 13, 0, 10, paramBoundingBox);
/* 164 */     placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 0, 11, paramBoundingBox);
/* 165 */     placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 0, 11, paramBoundingBox);
/* 166 */     placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 12, paramBoundingBox);
/* 167 */     placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 10, 0, 13, paramBoundingBox);
/* 168 */     placeBlock(paramWorldGenLevel, Blocks.BLUE_TERRACOTTA.defaultBlockState(), 10, 0, 10, paramBoundingBox);
/*     */ 
/*     */     
/* 171 */     for (i = 0; i <= this.width - 1; i += this.width - 1) {
/* 172 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i, 2, 1, paramBoundingBox);
/* 173 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i, 2, 2, paramBoundingBox);
/* 174 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i, 2, 3, paramBoundingBox);
/* 175 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i, 3, 1, paramBoundingBox);
/* 176 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i, 3, 2, paramBoundingBox);
/* 177 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i, 3, 3, paramBoundingBox);
/* 178 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i, 4, 1, paramBoundingBox);
/* 179 */       placeBlock(paramWorldGenLevel, Blocks.CHISELED_SANDSTONE.defaultBlockState(), i, 4, 2, paramBoundingBox);
/* 180 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i, 4, 3, paramBoundingBox);
/* 181 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i, 5, 1, paramBoundingBox);
/* 182 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i, 5, 2, paramBoundingBox);
/* 183 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i, 5, 3, paramBoundingBox);
/* 184 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i, 6, 1, paramBoundingBox);
/* 185 */       placeBlock(paramWorldGenLevel, Blocks.CHISELED_SANDSTONE.defaultBlockState(), i, 6, 2, paramBoundingBox);
/* 186 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i, 6, 3, paramBoundingBox);
/* 187 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i, 7, 1, paramBoundingBox);
/* 188 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i, 7, 2, paramBoundingBox);
/* 189 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i, 7, 3, paramBoundingBox);
/* 190 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i, 8, 1, paramBoundingBox);
/* 191 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i, 8, 2, paramBoundingBox);
/* 192 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i, 8, 3, paramBoundingBox);
/*     */     } 
/* 194 */     for (i = 2; i <= this.width - 3; i += this.width - 3 - 2) {
/* 195 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i - 1, 2, 0, paramBoundingBox);
/* 196 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i, 2, 0, paramBoundingBox);
/* 197 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i + 1, 2, 0, paramBoundingBox);
/* 198 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i - 1, 3, 0, paramBoundingBox);
/* 199 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i, 3, 0, paramBoundingBox);
/* 200 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i + 1, 3, 0, paramBoundingBox);
/* 201 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i - 1, 4, 0, paramBoundingBox);
/* 202 */       placeBlock(paramWorldGenLevel, Blocks.CHISELED_SANDSTONE.defaultBlockState(), i, 4, 0, paramBoundingBox);
/* 203 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i + 1, 4, 0, paramBoundingBox);
/* 204 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i - 1, 5, 0, paramBoundingBox);
/* 205 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i, 5, 0, paramBoundingBox);
/* 206 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i + 1, 5, 0, paramBoundingBox);
/* 207 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i - 1, 6, 0, paramBoundingBox);
/* 208 */       placeBlock(paramWorldGenLevel, Blocks.CHISELED_SANDSTONE.defaultBlockState(), i, 6, 0, paramBoundingBox);
/* 209 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i + 1, 6, 0, paramBoundingBox);
/* 210 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i - 1, 7, 0, paramBoundingBox);
/* 211 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i, 7, 0, paramBoundingBox);
/* 212 */       placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), i + 1, 7, 0, paramBoundingBox);
/* 213 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i - 1, 8, 0, paramBoundingBox);
/* 214 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i, 8, 0, paramBoundingBox);
/* 215 */       placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), i + 1, 8, 0, paramBoundingBox);
/*     */     } 
/* 217 */     generateBox(paramWorldGenLevel, paramBoundingBox, 8, 4, 0, 12, 6, 0, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 218 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 8, 6, 0, paramBoundingBox);
/* 219 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 12, 6, 0, paramBoundingBox);
/* 220 */     placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 9, 5, 0, paramBoundingBox);
/* 221 */     placeBlock(paramWorldGenLevel, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, 5, 0, paramBoundingBox);
/* 222 */     placeBlock(paramWorldGenLevel, Blocks.ORANGE_TERRACOTTA.defaultBlockState(), 11, 5, 0, paramBoundingBox);
/*     */ 
/*     */     
/* 225 */     generateBox(paramWorldGenLevel, paramBoundingBox, 8, -14, 8, 12, -11, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 226 */     generateBox(paramWorldGenLevel, paramBoundingBox, 8, -10, 8, 12, -10, 12, Blocks.CHISELED_SANDSTONE.defaultBlockState(), Blocks.CHISELED_SANDSTONE.defaultBlockState(), false);
/* 227 */     generateBox(paramWorldGenLevel, paramBoundingBox, 8, -9, 8, 12, -9, 12, Blocks.CUT_SANDSTONE.defaultBlockState(), Blocks.CUT_SANDSTONE.defaultBlockState(), false);
/* 228 */     generateBox(paramWorldGenLevel, paramBoundingBox, 8, -8, 8, 12, -1, 12, Blocks.SANDSTONE.defaultBlockState(), Blocks.SANDSTONE.defaultBlockState(), false);
/* 229 */     generateBox(paramWorldGenLevel, paramBoundingBox, 9, -11, 9, 11, -1, 11, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 230 */     placeBlock(paramWorldGenLevel, Blocks.STONE_PRESSURE_PLATE.defaultBlockState(), 10, -11, 10, paramBoundingBox);
/* 231 */     generateBox(paramWorldGenLevel, paramBoundingBox, 9, -13, 9, 11, -13, 11, Blocks.TNT.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
/* 232 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 8, -11, 10, paramBoundingBox);
/* 233 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 8, -10, 10, paramBoundingBox);
/* 234 */     placeBlock(paramWorldGenLevel, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 7, -10, 10, paramBoundingBox);
/* 235 */     placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), 7, -11, 10, paramBoundingBox);
/* 236 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 12, -11, 10, paramBoundingBox);
/* 237 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 12, -10, 10, paramBoundingBox);
/* 238 */     placeBlock(paramWorldGenLevel, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 13, -10, 10, paramBoundingBox);
/* 239 */     placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), 13, -11, 10, paramBoundingBox);
/* 240 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 10, -11, 8, paramBoundingBox);
/* 241 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 10, -10, 8, paramBoundingBox);
/* 242 */     placeBlock(paramWorldGenLevel, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, -10, 7, paramBoundingBox);
/* 243 */     placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, -11, 7, paramBoundingBox);
/* 244 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 10, -11, 12, paramBoundingBox);
/* 245 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 10, -10, 12, paramBoundingBox);
/* 246 */     placeBlock(paramWorldGenLevel, Blocks.CHISELED_SANDSTONE.defaultBlockState(), 10, -10, 13, paramBoundingBox);
/* 247 */     placeBlock(paramWorldGenLevel, Blocks.CUT_SANDSTONE.defaultBlockState(), 10, -11, 13, paramBoundingBox);
/*     */ 
/*     */     
/* 250 */     for (Direction direction : Direction.Plane.HORIZONTAL) {
/* 251 */       if (!this.hasPlacedChest[direction.get2DDataValue()]) {
/* 252 */         int j = direction.getStepX() * 2;
/* 253 */         int k = direction.getStepZ() * 2;
/* 254 */         this.hasPlacedChest[direction.get2DDataValue()] = createChest(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 10 + j, -11, 10 + k, BuiltInLootTables.DESERT_PYRAMID);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 259 */     addCellar(paramWorldGenLevel, paramBoundingBox);
/*     */   }
/*     */   
/*     */   private void addCellar(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox) {
/* 263 */     BlockPos blockPos = new BlockPos(16, -4, 13);
/*     */     
/* 265 */     addCellarStairs(blockPos, paramWorldGenLevel, paramBoundingBox);
/* 266 */     addCellarRoom(blockPos, paramWorldGenLevel, paramBoundingBox);
/*     */   }
/*     */   
/*     */   private void addCellarStairs(BlockPos paramBlockPos, WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox) {
/* 270 */     int i = paramBlockPos.getX();
/* 271 */     int j = paramBlockPos.getY();
/* 272 */     int k = paramBlockPos.getZ();
/*     */ 
/*     */     
/* 275 */     BlockState blockState1 = Blocks.SANDSTONE_STAIRS.defaultBlockState();
/* 276 */     placeBlock(paramWorldGenLevel, blockState1.rotate(Rotation.COUNTERCLOCKWISE_90), 13, -1, 17, paramBoundingBox);
/* 277 */     placeBlock(paramWorldGenLevel, blockState1.rotate(Rotation.COUNTERCLOCKWISE_90), 14, -2, 17, paramBoundingBox);
/* 278 */     placeBlock(paramWorldGenLevel, blockState1.rotate(Rotation.COUNTERCLOCKWISE_90), 15, -3, 17, paramBoundingBox);
/*     */     
/* 280 */     BlockState blockState2 = Blocks.SAND.defaultBlockState();
/* 281 */     BlockState blockState3 = Blocks.SANDSTONE.defaultBlockState();
/* 282 */     boolean bool = paramWorldGenLevel.getRandom().nextBoolean();
/* 283 */     placeBlock(paramWorldGenLevel, blockState2, i - 4, j + 4, k + 4, paramBoundingBox);
/* 284 */     placeBlock(paramWorldGenLevel, blockState2, i - 3, j + 4, k + 4, paramBoundingBox);
/* 285 */     placeBlock(paramWorldGenLevel, blockState2, i - 2, j + 4, k + 4, paramBoundingBox);
/* 286 */     placeBlock(paramWorldGenLevel, blockState2, i - 1, j + 4, k + 4, paramBoundingBox);
/* 287 */     placeBlock(paramWorldGenLevel, blockState2, i, j + 4, k + 4, paramBoundingBox);
/*     */ 
/*     */     
/* 290 */     placeBlock(paramWorldGenLevel, blockState2, i - 2, j + 3, k + 4, paramBoundingBox);
/* 291 */     placeBlock(paramWorldGenLevel, bool ? blockState2 : blockState3, i - 1, j + 3, k + 4, paramBoundingBox);
/* 292 */     placeBlock(paramWorldGenLevel, !bool ? blockState2 : blockState3, i, j + 3, k + 4, paramBoundingBox);
/*     */     
/* 294 */     placeBlock(paramWorldGenLevel, blockState2, i - 1, j + 2, k + 4, paramBoundingBox);
/* 295 */     placeBlock(paramWorldGenLevel, blockState3, i, j + 2, k + 4, paramBoundingBox);
/* 296 */     placeBlock(paramWorldGenLevel, blockState2, i, j + 1, k + 4, paramBoundingBox);
/*     */   }
/*     */   
/*     */   private void addCellarRoom(BlockPos paramBlockPos, WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox) {
/* 300 */     int i = paramBlockPos.getX();
/* 301 */     int j = paramBlockPos.getY();
/* 302 */     int k = paramBlockPos.getZ();
/*     */ 
/*     */     
/* 305 */     BlockState blockState1 = Blocks.CUT_SANDSTONE.defaultBlockState();
/* 306 */     BlockState blockState2 = Blocks.CHISELED_SANDSTONE.defaultBlockState();
/* 307 */     generateBox(paramWorldGenLevel, paramBoundingBox, i - 3, j + 1, k - 3, i - 3, j + 1, k + 2, blockState1, blockState1, true);
/* 308 */     generateBox(paramWorldGenLevel, paramBoundingBox, i + 3, j + 1, k - 3, i + 3, j + 1, k + 2, blockState1, blockState1, true);
/* 309 */     generateBox(paramWorldGenLevel, paramBoundingBox, i - 3, j + 1, k - 3, i + 3, j + 1, k - 2, blockState1, blockState1, true);
/* 310 */     generateBox(paramWorldGenLevel, paramBoundingBox, i - 3, j + 1, k + 3, i + 3, j + 1, k + 3, blockState1, blockState1, true);
/*     */     
/* 312 */     generateBox(paramWorldGenLevel, paramBoundingBox, i - 3, j + 2, k - 3, i - 3, j + 2, k + 2, blockState2, blockState2, true);
/* 313 */     generateBox(paramWorldGenLevel, paramBoundingBox, i + 3, j + 2, k - 3, i + 3, j + 2, k + 2, blockState2, blockState2, true);
/* 314 */     generateBox(paramWorldGenLevel, paramBoundingBox, i - 3, j + 2, k - 3, i + 3, j + 2, k - 2, blockState2, blockState2, true);
/* 315 */     generateBox(paramWorldGenLevel, paramBoundingBox, i - 3, j + 2, k + 3, i + 3, j + 2, k + 3, blockState2, blockState2, true);
/*     */     
/* 317 */     generateBox(paramWorldGenLevel, paramBoundingBox, i - 3, -1, k - 3, i - 3, -1, k + 2, blockState1, blockState1, true);
/* 318 */     generateBox(paramWorldGenLevel, paramBoundingBox, i + 3, -1, k - 3, i + 3, -1, k + 2, blockState1, blockState1, true);
/* 319 */     generateBox(paramWorldGenLevel, paramBoundingBox, i - 3, -1, k - 3, i + 3, -1, k - 2, blockState1, blockState1, true);
/* 320 */     generateBox(paramWorldGenLevel, paramBoundingBox, i - 3, -1, k + 3, i + 3, -1, k + 3, blockState1, blockState1, true);
/*     */     
/* 322 */     placeSandBox(i - 2, j + 1, k - 2, i + 2, j + 3, k + 2);
/* 323 */     placeCollapsedRoof(paramWorldGenLevel, paramBoundingBox, i - 2, j + 4, k - 2, i + 2, k + 2);
/*     */     
/* 325 */     BlockState blockState3 = Blocks.ORANGE_TERRACOTTA.defaultBlockState();
/* 326 */     BlockState blockState4 = Blocks.BLUE_TERRACOTTA.defaultBlockState();
/*     */ 
/*     */     
/* 329 */     placeBlock(paramWorldGenLevel, blockState4, i, j, k, paramBoundingBox);
/*     */     
/* 331 */     placeBlock(paramWorldGenLevel, blockState3, i + 1, j, k - 1, paramBoundingBox);
/* 332 */     placeBlock(paramWorldGenLevel, blockState3, i + 1, j, k + 1, paramBoundingBox);
/* 333 */     placeBlock(paramWorldGenLevel, blockState3, i - 1, j, k - 1, paramBoundingBox);
/* 334 */     placeBlock(paramWorldGenLevel, blockState3, i - 1, j, k + 1, paramBoundingBox);
/*     */     
/* 336 */     placeBlock(paramWorldGenLevel, blockState3, i + 2, j, k, paramBoundingBox);
/* 337 */     placeBlock(paramWorldGenLevel, blockState3, i - 2, j, k, paramBoundingBox);
/* 338 */     placeBlock(paramWorldGenLevel, blockState3, i, j, k + 2, paramBoundingBox);
/* 339 */     placeBlock(paramWorldGenLevel, blockState3, i, j, k - 2, paramBoundingBox);
/*     */ 
/*     */     
/* 342 */     placeBlock(paramWorldGenLevel, blockState3, i + 3, j, k, paramBoundingBox);
/* 343 */     placeSand(i + 3, j + 1, k);
/* 344 */     placeSand(i + 3, j + 2, k);
/* 345 */     placeBlock(paramWorldGenLevel, blockState1, i + 4, j + 1, k, paramBoundingBox);
/* 346 */     placeBlock(paramWorldGenLevel, blockState2, i + 4, j + 2, k, paramBoundingBox);
/*     */     
/* 348 */     placeBlock(paramWorldGenLevel, blockState3, i - 3, j, k, paramBoundingBox);
/* 349 */     placeSand(i - 3, j + 1, k);
/* 350 */     placeSand(i - 3, j + 2, k);
/* 351 */     placeBlock(paramWorldGenLevel, blockState1, i - 4, j + 1, k, paramBoundingBox);
/* 352 */     placeBlock(paramWorldGenLevel, blockState2, i - 4, j + 2, k, paramBoundingBox);
/*     */     
/* 354 */     placeBlock(paramWorldGenLevel, blockState3, i, j, k + 3, paramBoundingBox);
/* 355 */     placeSand(i, j + 1, k + 3);
/* 356 */     placeSand(i, j + 2, k + 3);
/*     */     
/* 358 */     placeBlock(paramWorldGenLevel, blockState3, i, j, k - 3, paramBoundingBox);
/* 359 */     placeSand(i, j + 1, k - 3);
/* 360 */     placeSand(i, j + 2, k - 3);
/* 361 */     placeBlock(paramWorldGenLevel, blockState1, i, j + 1, k - 4, paramBoundingBox);
/* 362 */     placeBlock(paramWorldGenLevel, blockState2, i, -2, k - 4, paramBoundingBox);
/*     */   }
/*     */   
/*     */   private void placeSand(int paramInt1, int paramInt2, int paramInt3) {
/* 366 */     BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(paramInt1, paramInt2, paramInt3);
/* 367 */     this.potentialSuspiciousSandWorldPositions.add(mutableBlockPos);
/*     */   }
/*     */   
/*     */   private void placeSandBox(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
/* 371 */     for (int i = paramInt2; i <= paramInt5; i++) {
/* 372 */       for (int j = paramInt1; j <= paramInt4; j++) {
/* 373 */         for (int k = paramInt3; k <= paramInt6; k++) {
/* 374 */           placeSand(j, i, k);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void placeCollapsedRoofPiece(WorldGenLevel paramWorldGenLevel, int paramInt1, int paramInt2, int paramInt3, BoundingBox paramBoundingBox) {
/* 382 */     if (paramWorldGenLevel.getRandom().nextFloat() < 0.33F) {
/* 383 */       BlockState blockState = Blocks.SANDSTONE.defaultBlockState();
/* 384 */       placeBlock(paramWorldGenLevel, blockState, paramInt1, paramInt2, paramInt3, paramBoundingBox);
/*     */     } else {
/* 386 */       BlockState blockState = Blocks.SAND.defaultBlockState();
/* 387 */       placeBlock(paramWorldGenLevel, blockState, paramInt1, paramInt2, paramInt3, paramBoundingBox);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void placeCollapsedRoof(WorldGenLevel paramWorldGenLevel, BoundingBox paramBoundingBox, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
/* 392 */     for (int i = paramInt1; i <= paramInt4; i++) {
/* 393 */       for (int m = paramInt3; m <= paramInt5; m++) {
/* 394 */         placeCollapsedRoofPiece(paramWorldGenLevel, i, paramInt2, m, paramBoundingBox);
/*     */       }
/*     */     } 
/*     */     
/* 398 */     RandomSource randomSource = RandomSource.create(paramWorldGenLevel.getSeed()).forkPositional().at((BlockPos)getWorldPos(paramInt1, paramInt2, paramInt3));
/* 399 */     int j = randomSource.nextIntBetweenInclusive(paramInt1, paramInt4);
/* 400 */     int k = randomSource.nextIntBetweenInclusive(paramInt3, paramInt5);
/* 401 */     this.randomCollapsedRoofPos = new BlockPos(getWorldX(j, k), getWorldY(paramInt2), getWorldZ(j, k));
/*     */   }
/*     */   
/*     */   public List<BlockPos> getPotentialSuspiciousSandWorldPositions() {
/* 405 */     return this.potentialSuspiciousSandWorldPositions;
/*     */   }
/*     */   
/*     */   public BlockPos getRandomCollapsedRoofPos() {
/* 409 */     return this.randomCollapsedRoofPos;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\DesertPyramidPiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */