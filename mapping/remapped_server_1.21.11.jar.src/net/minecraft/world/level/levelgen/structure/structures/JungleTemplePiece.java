/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.LeverBlock;
/*     */ import net.minecraft.world.level.block.RedStoneWireBlock;
/*     */ import net.minecraft.world.level.block.RepeaterBlock;
/*     */ import net.minecraft.world.level.block.StairBlock;
/*     */ import net.minecraft.world.level.block.TripWireBlock;
/*     */ import net.minecraft.world.level.block.TripWireHookBlock;
/*     */ import net.minecraft.world.level.block.VineBlock;
/*     */ import net.minecraft.world.level.block.piston.PistonBaseBlock;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.block.state.properties.RedstoneSide;
/*     */ import net.minecraft.world.level.chunk.ChunkGenerator;
/*     */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*     */ import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*     */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*     */ import net.minecraft.world.level.storage.loot.BuiltInLootTables;
/*     */ 
/*     */ public class JungleTemplePiece extends ScatteredFeaturePiece {
/*     */   public static final int WIDTH = 12;
/*     */   public static final int DEPTH = 15;
/*     */   private boolean placedMainChest;
/*     */   
/*     */   public JungleTemplePiece(RandomSource paramRandomSource, int paramInt1, int paramInt2) {
/*  38 */     super(StructurePieceType.JUNGLE_PYRAMID_PIECE, paramInt1, 64, paramInt2, 12, 10, 15, getRandomHorizontalDirection(paramRandomSource));
/*     */   }
/*     */   private boolean placedHiddenChest; private boolean placedTrap1; private boolean placedTrap2;
/*     */   public JungleTemplePiece(CompoundTag paramCompoundTag) {
/*  42 */     super(StructurePieceType.JUNGLE_PYRAMID_PIECE, paramCompoundTag);
/*  43 */     this.placedMainChest = paramCompoundTag.getBooleanOr("placedMainChest", false);
/*  44 */     this.placedHiddenChest = paramCompoundTag.getBooleanOr("placedHiddenChest", false);
/*  45 */     this.placedTrap1 = paramCompoundTag.getBooleanOr("placedTrap1", false);
/*  46 */     this.placedTrap2 = paramCompoundTag.getBooleanOr("placedTrap2", false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/*  51 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/*  52 */     paramCompoundTag.putBoolean("placedMainChest", this.placedMainChest);
/*  53 */     paramCompoundTag.putBoolean("placedHiddenChest", this.placedHiddenChest);
/*  54 */     paramCompoundTag.putBoolean("placedTrap1", this.placedTrap1);
/*  55 */     paramCompoundTag.putBoolean("placedTrap2", this.placedTrap2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/*  60 */     if (!updateAverageGroundHeight((LevelAccessor)paramWorldGenLevel, paramBoundingBox, 0)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  65 */     generateBox(paramWorldGenLevel, paramBoundingBox, 0, -4, 0, this.width - 1, 0, this.depth - 1, false, paramRandomSource, STONE_SELECTOR);
/*     */ 
/*     */     
/*  68 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 1, 2, 9, 2, 2, false, paramRandomSource, STONE_SELECTOR);
/*  69 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 1, 12, 9, 2, 12, false, paramRandomSource, STONE_SELECTOR);
/*  70 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 1, 3, 2, 2, 11, false, paramRandomSource, STONE_SELECTOR);
/*  71 */     generateBox(paramWorldGenLevel, paramBoundingBox, 9, 1, 3, 9, 2, 11, false, paramRandomSource, STONE_SELECTOR);
/*     */ 
/*     */     
/*  74 */     generateBox(paramWorldGenLevel, paramBoundingBox, 1, 3, 1, 10, 6, 1, false, paramRandomSource, STONE_SELECTOR);
/*  75 */     generateBox(paramWorldGenLevel, paramBoundingBox, 1, 3, 13, 10, 6, 13, false, paramRandomSource, STONE_SELECTOR);
/*  76 */     generateBox(paramWorldGenLevel, paramBoundingBox, 1, 3, 2, 1, 6, 12, false, paramRandomSource, STONE_SELECTOR);
/*  77 */     generateBox(paramWorldGenLevel, paramBoundingBox, 10, 3, 2, 10, 6, 12, false, paramRandomSource, STONE_SELECTOR);
/*     */ 
/*     */     
/*  80 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 3, 2, 9, 3, 12, false, paramRandomSource, STONE_SELECTOR);
/*  81 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 6, 2, 9, 6, 12, false, paramRandomSource, STONE_SELECTOR);
/*  82 */     generateBox(paramWorldGenLevel, paramBoundingBox, 3, 7, 3, 8, 7, 11, false, paramRandomSource, STONE_SELECTOR);
/*  83 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 8, 4, 7, 8, 10, false, paramRandomSource, STONE_SELECTOR);
/*     */ 
/*     */     
/*  86 */     generateAirBox(paramWorldGenLevel, paramBoundingBox, 3, 1, 3, 8, 2, 11);
/*  87 */     generateAirBox(paramWorldGenLevel, paramBoundingBox, 4, 3, 6, 7, 3, 9);
/*  88 */     generateAirBox(paramWorldGenLevel, paramBoundingBox, 2, 4, 2, 9, 5, 12);
/*  89 */     generateAirBox(paramWorldGenLevel, paramBoundingBox, 4, 6, 5, 7, 6, 9);
/*  90 */     generateAirBox(paramWorldGenLevel, paramBoundingBox, 5, 7, 6, 6, 7, 8);
/*     */ 
/*     */     
/*  93 */     generateAirBox(paramWorldGenLevel, paramBoundingBox, 5, 1, 2, 6, 2, 2);
/*  94 */     generateAirBox(paramWorldGenLevel, paramBoundingBox, 5, 2, 12, 6, 2, 12);
/*  95 */     generateAirBox(paramWorldGenLevel, paramBoundingBox, 5, 5, 1, 6, 5, 1);
/*  96 */     generateAirBox(paramWorldGenLevel, paramBoundingBox, 5, 5, 13, 6, 5, 13);
/*  97 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 1, 5, 5, paramBoundingBox);
/*  98 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 10, 5, 5, paramBoundingBox);
/*  99 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 1, 5, 9, paramBoundingBox);
/* 100 */     placeBlock(paramWorldGenLevel, Blocks.AIR.defaultBlockState(), 10, 5, 9, paramBoundingBox);
/*     */     
/*     */     byte b1;
/* 103 */     for (b1 = 0; b1 <= 14; b1 += 14) {
/* 104 */       generateBox(paramWorldGenLevel, paramBoundingBox, 2, 4, b1, 2, 5, b1, false, paramRandomSource, STONE_SELECTOR);
/* 105 */       generateBox(paramWorldGenLevel, paramBoundingBox, 4, 4, b1, 4, 5, b1, false, paramRandomSource, STONE_SELECTOR);
/* 106 */       generateBox(paramWorldGenLevel, paramBoundingBox, 7, 4, b1, 7, 5, b1, false, paramRandomSource, STONE_SELECTOR);
/* 107 */       generateBox(paramWorldGenLevel, paramBoundingBox, 9, 4, b1, 9, 5, b1, false, paramRandomSource, STONE_SELECTOR);
/*     */     } 
/* 109 */     generateBox(paramWorldGenLevel, paramBoundingBox, 5, 6, 0, 6, 6, 0, false, paramRandomSource, STONE_SELECTOR);
/* 110 */     for (b1 = 0; b1 <= 11; b1 += 11) {
/* 111 */       for (byte b = 2; b <= 12; b += 2) {
/* 112 */         generateBox(paramWorldGenLevel, paramBoundingBox, b1, 4, b, b1, 5, b, false, paramRandomSource, STONE_SELECTOR);
/*     */       }
/* 114 */       generateBox(paramWorldGenLevel, paramBoundingBox, b1, 6, 5, b1, 6, 5, false, paramRandomSource, STONE_SELECTOR);
/* 115 */       generateBox(paramWorldGenLevel, paramBoundingBox, b1, 6, 9, b1, 6, 9, false, paramRandomSource, STONE_SELECTOR);
/*     */     } 
/* 117 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 7, 2, 2, 9, 2, false, paramRandomSource, STONE_SELECTOR);
/* 118 */     generateBox(paramWorldGenLevel, paramBoundingBox, 9, 7, 2, 9, 9, 2, false, paramRandomSource, STONE_SELECTOR);
/* 119 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, 7, 12, 2, 9, 12, false, paramRandomSource, STONE_SELECTOR);
/* 120 */     generateBox(paramWorldGenLevel, paramBoundingBox, 9, 7, 12, 9, 9, 12, false, paramRandomSource, STONE_SELECTOR);
/* 121 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 9, 4, 4, 9, 4, false, paramRandomSource, STONE_SELECTOR);
/* 122 */     generateBox(paramWorldGenLevel, paramBoundingBox, 7, 9, 4, 7, 9, 4, false, paramRandomSource, STONE_SELECTOR);
/* 123 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 9, 10, 4, 9, 10, false, paramRandomSource, STONE_SELECTOR);
/* 124 */     generateBox(paramWorldGenLevel, paramBoundingBox, 7, 9, 10, 7, 9, 10, false, paramRandomSource, STONE_SELECTOR);
/* 125 */     generateBox(paramWorldGenLevel, paramBoundingBox, 5, 9, 7, 6, 9, 7, false, paramRandomSource, STONE_SELECTOR);
/*     */     
/* 127 */     BlockState blockState1 = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.EAST);
/* 128 */     BlockState blockState2 = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.WEST);
/* 129 */     BlockState blockState3 = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.SOUTH);
/* 130 */     BlockState blockState4 = (BlockState)Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue((Property)StairBlock.FACING, (Comparable)Direction.NORTH);
/*     */     
/* 132 */     placeBlock(paramWorldGenLevel, blockState4, 5, 9, 6, paramBoundingBox);
/* 133 */     placeBlock(paramWorldGenLevel, blockState4, 6, 9, 6, paramBoundingBox);
/* 134 */     placeBlock(paramWorldGenLevel, blockState3, 5, 9, 8, paramBoundingBox);
/* 135 */     placeBlock(paramWorldGenLevel, blockState3, 6, 9, 8, paramBoundingBox);
/*     */ 
/*     */     
/* 138 */     placeBlock(paramWorldGenLevel, blockState4, 4, 0, 0, paramBoundingBox);
/* 139 */     placeBlock(paramWorldGenLevel, blockState4, 5, 0, 0, paramBoundingBox);
/* 140 */     placeBlock(paramWorldGenLevel, blockState4, 6, 0, 0, paramBoundingBox);
/* 141 */     placeBlock(paramWorldGenLevel, blockState4, 7, 0, 0, paramBoundingBox);
/*     */ 
/*     */     
/* 144 */     placeBlock(paramWorldGenLevel, blockState4, 4, 1, 8, paramBoundingBox);
/* 145 */     placeBlock(paramWorldGenLevel, blockState4, 4, 2, 9, paramBoundingBox);
/* 146 */     placeBlock(paramWorldGenLevel, blockState4, 4, 3, 10, paramBoundingBox);
/* 147 */     placeBlock(paramWorldGenLevel, blockState4, 7, 1, 8, paramBoundingBox);
/* 148 */     placeBlock(paramWorldGenLevel, blockState4, 7, 2, 9, paramBoundingBox);
/* 149 */     placeBlock(paramWorldGenLevel, blockState4, 7, 3, 10, paramBoundingBox);
/* 150 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 1, 9, 4, 1, 9, false, paramRandomSource, STONE_SELECTOR);
/* 151 */     generateBox(paramWorldGenLevel, paramBoundingBox, 7, 1, 9, 7, 1, 9, false, paramRandomSource, STONE_SELECTOR);
/* 152 */     generateBox(paramWorldGenLevel, paramBoundingBox, 4, 1, 10, 7, 2, 10, false, paramRandomSource, STONE_SELECTOR);
/*     */ 
/*     */     
/* 155 */     generateBox(paramWorldGenLevel, paramBoundingBox, 5, 4, 5, 6, 4, 5, false, paramRandomSource, STONE_SELECTOR);
/* 156 */     placeBlock(paramWorldGenLevel, blockState1, 4, 4, 5, paramBoundingBox);
/* 157 */     placeBlock(paramWorldGenLevel, blockState2, 7, 4, 5, paramBoundingBox);
/*     */     
/*     */     byte b2;
/* 160 */     for (b2 = 0; b2 < 4; b2++) {
/* 161 */       placeBlock(paramWorldGenLevel, blockState3, 5, 0 - b2, 6 + b2, paramBoundingBox);
/* 162 */       placeBlock(paramWorldGenLevel, blockState3, 6, 0 - b2, 6 + b2, paramBoundingBox);
/* 163 */       generateAirBox(paramWorldGenLevel, paramBoundingBox, 5, 0 - b2, 7 + b2, 6, 0 - b2, 9 + b2);
/*     */     } 
/*     */ 
/*     */     
/* 167 */     generateAirBox(paramWorldGenLevel, paramBoundingBox, 1, -3, 12, 10, -1, 13);
/* 168 */     generateAirBox(paramWorldGenLevel, paramBoundingBox, 1, -3, 1, 3, -1, 13);
/* 169 */     generateAirBox(paramWorldGenLevel, paramBoundingBox, 1, -3, 1, 9, -1, 5);
/* 170 */     for (b2 = 1; b2 <= 13; b2 += 2) {
/* 171 */       generateBox(paramWorldGenLevel, paramBoundingBox, 1, -3, b2, 1, -2, b2, false, paramRandomSource, STONE_SELECTOR);
/*     */     }
/* 173 */     for (b2 = 2; b2 <= 12; b2 += 2) {
/* 174 */       generateBox(paramWorldGenLevel, paramBoundingBox, 1, -1, b2, 3, -1, b2, false, paramRandomSource, STONE_SELECTOR);
/*     */     }
/* 176 */     generateBox(paramWorldGenLevel, paramBoundingBox, 2, -2, 1, 5, -2, 1, false, paramRandomSource, STONE_SELECTOR);
/* 177 */     generateBox(paramWorldGenLevel, paramBoundingBox, 7, -2, 1, 9, -2, 1, false, paramRandomSource, STONE_SELECTOR);
/* 178 */     generateBox(paramWorldGenLevel, paramBoundingBox, 6, -3, 1, 6, -3, 1, false, paramRandomSource, STONE_SELECTOR);
/* 179 */     generateBox(paramWorldGenLevel, paramBoundingBox, 6, -1, 1, 6, -1, 1, false, paramRandomSource, STONE_SELECTOR);
/*     */ 
/*     */     
/* 182 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.TRIPWIRE_HOOK.defaultBlockState().setValue((Property)TripWireHookBlock.FACING, (Comparable)Direction.EAST)).setValue((Property)TripWireHookBlock.ATTACHED, Boolean.valueOf(true)), 1, -3, 8, paramBoundingBox);
/* 183 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.TRIPWIRE_HOOK.defaultBlockState().setValue((Property)TripWireHookBlock.FACING, (Comparable)Direction.WEST)).setValue((Property)TripWireHookBlock.ATTACHED, Boolean.valueOf(true)), 4, -3, 8, paramBoundingBox);
/* 184 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue((Property)TripWireBlock.EAST, Boolean.valueOf(true))).setValue((Property)TripWireBlock.WEST, Boolean.valueOf(true))).setValue((Property)TripWireBlock.ATTACHED, Boolean.valueOf(true)), 2, -3, 8, paramBoundingBox);
/* 185 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue((Property)TripWireBlock.EAST, Boolean.valueOf(true))).setValue((Property)TripWireBlock.WEST, Boolean.valueOf(true))).setValue((Property)TripWireBlock.ATTACHED, Boolean.valueOf(true)), 3, -3, 8, paramBoundingBox);
/* 186 */     BlockState blockState5 = (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue((Property)RedStoneWireBlock.NORTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.SOUTH, (Comparable)RedstoneSide.SIDE);
/* 187 */     placeBlock(paramWorldGenLevel, blockState5, 5, -3, 7, paramBoundingBox);
/* 188 */     placeBlock(paramWorldGenLevel, blockState5, 5, -3, 6, paramBoundingBox);
/* 189 */     placeBlock(paramWorldGenLevel, blockState5, 5, -3, 5, paramBoundingBox);
/* 190 */     placeBlock(paramWorldGenLevel, blockState5, 5, -3, 4, paramBoundingBox);
/* 191 */     placeBlock(paramWorldGenLevel, blockState5, 5, -3, 3, paramBoundingBox);
/* 192 */     placeBlock(paramWorldGenLevel, blockState5, 5, -3, 2, paramBoundingBox);
/* 193 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue((Property)RedStoneWireBlock.NORTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.WEST, (Comparable)RedstoneSide.SIDE), 5, -3, 1, paramBoundingBox);
/* 194 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue((Property)RedStoneWireBlock.EAST, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.WEST, (Comparable)RedstoneSide.SIDE), 4, -3, 1, paramBoundingBox);
/* 195 */     placeBlock(paramWorldGenLevel, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3, -3, 1, paramBoundingBox);
/* 196 */     if (!this.placedTrap1) {
/* 197 */       this.placedTrap1 = createDispenser(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 3, -2, 1, Direction.NORTH, BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER);
/*     */     }
/* 199 */     placeBlock(paramWorldGenLevel, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)VineBlock.SOUTH, Boolean.valueOf(true)), 3, -2, 2, paramBoundingBox);
/*     */ 
/*     */     
/* 202 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.TRIPWIRE_HOOK.defaultBlockState().setValue((Property)TripWireHookBlock.FACING, (Comparable)Direction.NORTH)).setValue((Property)TripWireHookBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 1, paramBoundingBox);
/* 203 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.TRIPWIRE_HOOK.defaultBlockState().setValue((Property)TripWireHookBlock.FACING, (Comparable)Direction.SOUTH)).setValue((Property)TripWireHookBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 5, paramBoundingBox);
/* 204 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue((Property)TripWireBlock.NORTH, Boolean.valueOf(true))).setValue((Property)TripWireBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)TripWireBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 2, paramBoundingBox);
/* 205 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue((Property)TripWireBlock.NORTH, Boolean.valueOf(true))).setValue((Property)TripWireBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)TripWireBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 3, paramBoundingBox);
/* 206 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)((BlockState)Blocks.TRIPWIRE.defaultBlockState().setValue((Property)TripWireBlock.NORTH, Boolean.valueOf(true))).setValue((Property)TripWireBlock.SOUTH, Boolean.valueOf(true))).setValue((Property)TripWireBlock.ATTACHED, Boolean.valueOf(true)), 7, -3, 4, paramBoundingBox);
/* 207 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue((Property)RedStoneWireBlock.EAST, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.WEST, (Comparable)RedstoneSide.SIDE), 8, -3, 6, paramBoundingBox);
/* 208 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue((Property)RedStoneWireBlock.WEST, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.SOUTH, (Comparable)RedstoneSide.SIDE), 9, -3, 6, paramBoundingBox);
/* 209 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue((Property)RedStoneWireBlock.NORTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.SOUTH, (Comparable)RedstoneSide.UP), 9, -3, 5, paramBoundingBox);
/* 210 */     placeBlock(paramWorldGenLevel, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 9, -3, 4, paramBoundingBox);
/* 211 */     placeBlock(paramWorldGenLevel, blockState5, 9, -2, 4, paramBoundingBox);
/* 212 */     if (!this.placedTrap2) {
/* 213 */       this.placedTrap2 = createDispenser(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 9, -2, 3, Direction.WEST, BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER);
/*     */     }
/* 215 */     placeBlock(paramWorldGenLevel, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)VineBlock.EAST, Boolean.valueOf(true)), 8, -1, 3, paramBoundingBox);
/* 216 */     placeBlock(paramWorldGenLevel, (BlockState)Blocks.VINE.defaultBlockState().setValue((Property)VineBlock.EAST, Boolean.valueOf(true)), 8, -2, 3, paramBoundingBox);
/* 217 */     if (!this.placedMainChest) {
/* 218 */       this.placedMainChest = createChest(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 8, -3, 3, BuiltInLootTables.JUNGLE_TEMPLE);
/*     */     }
/* 220 */     placeBlock(paramWorldGenLevel, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 9, -3, 2, paramBoundingBox);
/* 221 */     placeBlock(paramWorldGenLevel, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 8, -3, 1, paramBoundingBox);
/* 222 */     placeBlock(paramWorldGenLevel, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 4, -3, 5, paramBoundingBox);
/* 223 */     placeBlock(paramWorldGenLevel, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 5, -2, 5, paramBoundingBox);
/* 224 */     placeBlock(paramWorldGenLevel, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 5, -1, 5, paramBoundingBox);
/* 225 */     placeBlock(paramWorldGenLevel, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 6, -3, 5, paramBoundingBox);
/* 226 */     placeBlock(paramWorldGenLevel, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 7, -2, 5, paramBoundingBox);
/* 227 */     placeBlock(paramWorldGenLevel, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 7, -1, 5, paramBoundingBox);
/* 228 */     placeBlock(paramWorldGenLevel, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 8, -3, 5, paramBoundingBox);
/* 229 */     generateBox(paramWorldGenLevel, paramBoundingBox, 9, -1, 1, 9, -1, 5, false, paramRandomSource, STONE_SELECTOR);
/*     */ 
/*     */     
/* 232 */     generateAirBox(paramWorldGenLevel, paramBoundingBox, 8, -3, 8, 10, -1, 10);
/* 233 */     placeBlock(paramWorldGenLevel, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 8, -2, 11, paramBoundingBox);
/* 234 */     placeBlock(paramWorldGenLevel, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 9, -2, 11, paramBoundingBox);
/* 235 */     placeBlock(paramWorldGenLevel, Blocks.CHISELED_STONE_BRICKS.defaultBlockState(), 10, -2, 11, paramBoundingBox);
/* 236 */     BlockState blockState6 = (BlockState)((BlockState)Blocks.LEVER.defaultBlockState().setValue((Property)LeverBlock.FACING, (Comparable)Direction.NORTH)).setValue((Property)LeverBlock.FACE, (Comparable)AttachFace.WALL);
/* 237 */     placeBlock(paramWorldGenLevel, blockState6, 8, -2, 12, paramBoundingBox);
/* 238 */     placeBlock(paramWorldGenLevel, blockState6, 9, -2, 12, paramBoundingBox);
/* 239 */     placeBlock(paramWorldGenLevel, blockState6, 10, -2, 12, paramBoundingBox);
/* 240 */     generateBox(paramWorldGenLevel, paramBoundingBox, 8, -3, 8, 8, -3, 10, false, paramRandomSource, STONE_SELECTOR);
/* 241 */     generateBox(paramWorldGenLevel, paramBoundingBox, 10, -3, 8, 10, -3, 10, false, paramRandomSource, STONE_SELECTOR);
/* 242 */     placeBlock(paramWorldGenLevel, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 10, -2, 9, paramBoundingBox);
/* 243 */     placeBlock(paramWorldGenLevel, blockState5, 8, -2, 9, paramBoundingBox);
/* 244 */     placeBlock(paramWorldGenLevel, blockState5, 8, -2, 10, paramBoundingBox);
/* 245 */     placeBlock(paramWorldGenLevel, (BlockState)((BlockState)((BlockState)((BlockState)Blocks.REDSTONE_WIRE.defaultBlockState().setValue((Property)RedStoneWireBlock.NORTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.SOUTH, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.EAST, (Comparable)RedstoneSide.SIDE)).setValue((Property)RedStoneWireBlock.WEST, (Comparable)RedstoneSide.SIDE), 10, -1, 9, paramBoundingBox);
/* 246 */     placeBlock(paramWorldGenLevel, (BlockState)Blocks.STICKY_PISTON.defaultBlockState().setValue((Property)PistonBaseBlock.FACING, (Comparable)Direction.UP), 9, -2, 8, paramBoundingBox);
/* 247 */     placeBlock(paramWorldGenLevel, (BlockState)Blocks.STICKY_PISTON.defaultBlockState().setValue((Property)PistonBaseBlock.FACING, (Comparable)Direction.WEST), 10, -2, 8, paramBoundingBox);
/* 248 */     placeBlock(paramWorldGenLevel, (BlockState)Blocks.STICKY_PISTON.defaultBlockState().setValue((Property)PistonBaseBlock.FACING, (Comparable)Direction.WEST), 10, -1, 8, paramBoundingBox);
/* 249 */     placeBlock(paramWorldGenLevel, (BlockState)Blocks.REPEATER.defaultBlockState().setValue((Property)RepeaterBlock.FACING, (Comparable)Direction.NORTH), 10, -2, 10, paramBoundingBox);
/* 250 */     if (!this.placedHiddenChest)
/* 251 */       this.placedHiddenChest = createChest(paramWorldGenLevel, paramBoundingBox, paramRandomSource, 9, -3, 10, BuiltInLootTables.JUNGLE_TEMPLE); 
/*     */   }
/*     */   
/*     */   private static class MossStoneSelector
/*     */     extends StructurePiece.BlockSelector
/*     */   {
/*     */     public void next(RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, boolean param1Boolean) {
/* 258 */       if (param1RandomSource.nextFloat() < 0.4F) {
/* 259 */         this.next = Blocks.COBBLESTONE.defaultBlockState();
/*     */       } else {
/* 261 */         this.next = Blocks.MOSSY_COBBLESTONE.defaultBlockState();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/* 266 */   private static final MossStoneSelector STONE_SELECTOR = new MossStoneSelector();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\JungleTemplePiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */