/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.BiomeTags;
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
/*     */ public class MineshaftPieces
/*     */ {
/*     */   private static final int DEFAULT_SHAFT_WIDTH = 3;
/*     */   private static final int DEFAULT_SHAFT_HEIGHT = 3;
/*     */   private static final int DEFAULT_SHAFT_LENGTH = 5;
/*     */   private static final int MAX_PILLAR_HEIGHT = 20;
/*     */   private static final int MAX_CHAIN_HEIGHT = 50;
/*     */   private static final int MAX_DEPTH = 8;
/*     */   public static final int MAGIC_START_Y = 50;
/*     */   
/*     */   private static abstract class MineShaftPiece
/*     */     extends StructurePiece {
/*     */     protected MineshaftStructure.Type type;
/*     */     
/*     */     public MineShaftPiece(StructurePieceType param1StructurePieceType, int param1Int, MineshaftStructure.Type param1Type, BoundingBox param1BoundingBox) {
/*  57 */       super(param1StructurePieceType, param1Int, param1BoundingBox);
/*  58 */       this.type = param1Type;
/*     */     }
/*     */     
/*     */     public MineShaftPiece(StructurePieceType param1StructurePieceType, CompoundTag param1CompoundTag) {
/*  62 */       super(param1StructurePieceType, param1CompoundTag);
/*  63 */       this.type = MineshaftStructure.Type.byId(param1CompoundTag.getIntOr("MST", 0));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean canBeReplaced(LevelReader param1LevelReader, int param1Int1, int param1Int2, int param1Int3, BoundingBox param1BoundingBox) {
/*  69 */       BlockState blockState = getBlock((BlockGetter)param1LevelReader, param1Int1, param1Int2, param1Int3, param1BoundingBox);
/*  70 */       return (!blockState.is(this.type.getPlanksState().getBlock()) && 
/*  71 */         !blockState.is(this.type.getWoodState().getBlock()) && 
/*  72 */         !blockState.is(this.type.getFenceState().getBlock()) && 
/*  73 */         !blockState.is(Blocks.IRON_CHAIN));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/*  78 */       param1CompoundTag.putInt("MST", this.type.ordinal());
/*     */     }
/*     */     
/*     */     protected boolean isSupportingBox(BlockGetter param1BlockGetter, BoundingBox param1BoundingBox, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
/*  82 */       for (int i = param1Int1; i <= param1Int2; i++) {
/*  83 */         if (getBlock(param1BlockGetter, i, param1Int3 + 1, param1Int4, param1BoundingBox).isAir()) {
/*  84 */           return false;
/*     */         }
/*     */       } 
/*  87 */       return true;
/*     */     }
/*     */     
/*     */     protected boolean isInInvalidLocation(LevelAccessor param1LevelAccessor, BoundingBox param1BoundingBox) {
/*  91 */       int i = Math.max(this.boundingBox.minX() - 1, param1BoundingBox.minX());
/*  92 */       int j = Math.max(this.boundingBox.minY() - 1, param1BoundingBox.minY());
/*  93 */       int k = Math.max(this.boundingBox.minZ() - 1, param1BoundingBox.minZ());
/*  94 */       int m = Math.min(this.boundingBox.maxX() + 1, param1BoundingBox.maxX());
/*  95 */       int n = Math.min(this.boundingBox.maxY() + 1, param1BoundingBox.maxY());
/*  96 */       int i1 = Math.min(this.boundingBox.maxZ() + 1, param1BoundingBox.maxZ());
/*     */       
/*  98 */       BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos((i + m) / 2, (j + n) / 2, (k + i1) / 2);
/*     */       
/* 100 */       if (param1LevelAccessor.getBiome((BlockPos)mutableBlockPos).is(BiomeTags.MINESHAFT_BLOCKING)) {
/* 101 */         return true;
/*     */       }
/*     */       
/*     */       int i2;
/* 105 */       for (i2 = i; i2 <= m; i2++) {
/* 106 */         for (int i3 = k; i3 <= i1; i3++) {
/* 107 */           if (param1LevelAccessor.getBlockState((BlockPos)mutableBlockPos.set(i2, j, i3)).liquid()) {
/* 108 */             return true;
/*     */           }
/* 110 */           if (param1LevelAccessor.getBlockState((BlockPos)mutableBlockPos.set(i2, n, i3)).liquid()) {
/* 111 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 116 */       for (i2 = i; i2 <= m; i2++) {
/* 117 */         for (int i3 = j; i3 <= n; i3++) {
/* 118 */           if (param1LevelAccessor.getBlockState((BlockPos)mutableBlockPos.set(i2, i3, k)).liquid()) {
/* 119 */             return true;
/*     */           }
/* 121 */           if (param1LevelAccessor.getBlockState((BlockPos)mutableBlockPos.set(i2, i3, i1)).liquid()) {
/* 122 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 127 */       for (i2 = k; i2 <= i1; i2++) {
/* 128 */         for (int i3 = j; i3 <= n; i3++) {
/* 129 */           if (param1LevelAccessor.getBlockState((BlockPos)mutableBlockPos.set(i, i3, i2)).liquid()) {
/* 130 */             return true;
/*     */           }
/* 132 */           if (param1LevelAccessor.getBlockState((BlockPos)mutableBlockPos.set(m, i3, i2)).liquid()) {
/* 133 */             return true;
/*     */           }
/*     */         } 
/*     */       } 
/* 137 */       return false;
/*     */     }
/*     */     
/*     */     protected void setPlanksBlock(WorldGenLevel param1WorldGenLevel, BoundingBox param1BoundingBox, BlockState param1BlockState, int param1Int1, int param1Int2, int param1Int3) {
/* 141 */       if (!isInterior((LevelReader)param1WorldGenLevel, param1Int1, param1Int2, param1Int3, param1BoundingBox)) {
/*     */         return;
/*     */       }
/* 144 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(param1Int1, param1Int2, param1Int3);
/* 145 */       BlockState blockState = param1WorldGenLevel.getBlockState((BlockPos)mutableBlockPos);
/* 146 */       if (!blockState.isFaceSturdy((BlockGetter)param1WorldGenLevel, (BlockPos)mutableBlockPos, Direction.UP))
/*     */       {
/* 148 */         param1WorldGenLevel.setBlock((BlockPos)mutableBlockPos, param1BlockState, 2);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static MineShaftPiece createRandomShaftPiece(StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection, int paramInt4, MineshaftStructure.Type paramType) {
/* 154 */     int i = paramRandomSource.nextInt(100);
/* 155 */     if (i >= 80) {
/* 156 */       BoundingBox boundingBox = MineShaftCrossing.findCrossing(paramStructurePieceAccessor, paramRandomSource, paramInt1, paramInt2, paramInt3, paramDirection);
/* 157 */       if (boundingBox != null) {
/* 158 */         return new MineShaftCrossing(paramInt4, boundingBox, paramDirection, paramType);
/*     */       }
/* 160 */     } else if (i >= 70) {
/* 161 */       BoundingBox boundingBox = MineShaftStairs.findStairs(paramStructurePieceAccessor, paramRandomSource, paramInt1, paramInt2, paramInt3, paramDirection);
/* 162 */       if (boundingBox != null) {
/* 163 */         return new MineShaftStairs(paramInt4, boundingBox, paramDirection, paramType);
/*     */       }
/*     */     } else {
/* 166 */       BoundingBox boundingBox = MineShaftCorridor.findCorridorSize(paramStructurePieceAccessor, paramRandomSource, paramInt1, paramInt2, paramInt3, paramDirection);
/* 167 */       if (boundingBox != null) {
/* 168 */         return new MineShaftCorridor(paramInt4, paramRandomSource, boundingBox, paramDirection, paramType);
/*     */       }
/*     */     } 
/*     */     
/* 172 */     return null;
/*     */   }
/*     */   
/*     */   static MineShaftPiece generateAndAddPiece(StructurePiece paramStructurePiece, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection, int paramInt4) {
/* 176 */     if (paramInt4 > 8) {
/* 177 */       return null;
/*     */     }
/* 179 */     if (Math.abs(paramInt1 - paramStructurePiece.getBoundingBox().minX()) > 80 || Math.abs(paramInt3 - paramStructurePiece.getBoundingBox().minZ()) > 80) {
/* 180 */       return null;
/*     */     }
/*     */     
/* 183 */     MineshaftStructure.Type type = ((MineShaftPiece)paramStructurePiece).type;
/* 184 */     MineShaftPiece mineShaftPiece = createRandomShaftPiece(paramStructurePieceAccessor, paramRandomSource, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4 + 1, type);
/* 185 */     if (mineShaftPiece != null) {
/* 186 */       paramStructurePieceAccessor.addPiece(mineShaftPiece);
/* 187 */       mineShaftPiece.addChildren(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource);
/*     */     } 
/* 189 */     return mineShaftPiece;
/*     */   }
/*     */   
/*     */   public static class MineShaftRoom extends MineShaftPiece {
/* 193 */     private final List<BoundingBox> childEntranceBoxes = Lists.newLinkedList();
/*     */     
/*     */     public MineShaftRoom(int param1Int1, RandomSource param1RandomSource, int param1Int2, int param1Int3, MineshaftStructure.Type param1Type) {
/* 196 */       super(StructurePieceType.MINE_SHAFT_ROOM, param1Int1, param1Type, new BoundingBox(param1Int2, 50, param1Int3, param1Int2 + 7 + param1RandomSource.nextInt(6), 54 + param1RandomSource.nextInt(6), param1Int3 + 7 + param1RandomSource.nextInt(6)));
/* 197 */       this.type = param1Type;
/*     */     }
/*     */     
/*     */     public MineShaftRoom(CompoundTag param1CompoundTag) {
/* 201 */       super(StructurePieceType.MINE_SHAFT_ROOM, param1CompoundTag);
/* 202 */       this.childEntranceBoxes.addAll(param1CompoundTag.read("Entrances", BoundingBox.CODEC.listOf()).orElse(List.of()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/* 207 */       int i = getGenDepth();
/*     */ 
/*     */ 
/*     */       
/* 211 */       int k = this.boundingBox.getYSpan() - 3 - 1;
/* 212 */       if (k <= 0) {
/* 213 */         k = 1;
/*     */       }
/*     */ 
/*     */       
/* 217 */       int j = 0;
/* 218 */       while (j < this.boundingBox.getXSpan()) {
/* 219 */         j += param1RandomSource.nextInt(this.boundingBox.getXSpan());
/* 220 */         if (j + 3 > this.boundingBox.getXSpan()) {
/*     */           break;
/*     */         }
/* 223 */         MineshaftPieces.MineShaftPiece mineShaftPiece = MineshaftPieces.generateAndAddPiece(param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.minX() + j, this.boundingBox.minY() + param1RandomSource.nextInt(k) + 1, this.boundingBox.minZ() - 1, Direction.NORTH, i);
/* 224 */         if (mineShaftPiece != null) {
/* 225 */           BoundingBox boundingBox = mineShaftPiece.getBoundingBox();
/* 226 */           this.childEntranceBoxes.add(new BoundingBox(boundingBox.minX(), boundingBox.minY(), this.boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), this.boundingBox.minZ() + 1));
/*     */         } 
/* 228 */         j += 4;
/*     */       } 
/*     */       
/* 231 */       j = 0;
/* 232 */       while (j < this.boundingBox.getXSpan()) {
/* 233 */         j += param1RandomSource.nextInt(this.boundingBox.getXSpan());
/* 234 */         if (j + 3 > this.boundingBox.getXSpan()) {
/*     */           break;
/*     */         }
/* 237 */         MineshaftPieces.MineShaftPiece mineShaftPiece = MineshaftPieces.generateAndAddPiece(param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.minX() + j, this.boundingBox.minY() + param1RandomSource.nextInt(k) + 1, this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
/* 238 */         if (mineShaftPiece != null) {
/* 239 */           BoundingBox boundingBox = mineShaftPiece.getBoundingBox();
/* 240 */           this.childEntranceBoxes.add(new BoundingBox(boundingBox.minX(), boundingBox.minY(), this.boundingBox.maxZ() - 1, boundingBox.maxX(), boundingBox.maxY(), this.boundingBox.maxZ()));
/*     */         } 
/* 242 */         j += 4;
/*     */       } 
/*     */       
/* 245 */       j = 0;
/* 246 */       while (j < this.boundingBox.getZSpan()) {
/* 247 */         j += param1RandomSource.nextInt(this.boundingBox.getZSpan());
/* 248 */         if (j + 3 > this.boundingBox.getZSpan()) {
/*     */           break;
/*     */         }
/* 251 */         MineshaftPieces.MineShaftPiece mineShaftPiece = MineshaftPieces.generateAndAddPiece(param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() + param1RandomSource.nextInt(k) + 1, this.boundingBox.minZ() + j, Direction.WEST, i);
/* 252 */         if (mineShaftPiece != null) {
/* 253 */           BoundingBox boundingBox = mineShaftPiece.getBoundingBox();
/* 254 */           this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), this.boundingBox.minX() + 1, boundingBox.maxY(), boundingBox.maxZ()));
/*     */         } 
/* 256 */         j += 4;
/*     */       } 
/*     */       
/* 259 */       j = 0;
/* 260 */       while (j < this.boundingBox.getZSpan()) {
/* 261 */         j += param1RandomSource.nextInt(this.boundingBox.getZSpan());
/* 262 */         if (j + 3 > this.boundingBox.getZSpan()) {
/*     */           break;
/*     */         }
/* 265 */         MineshaftPieces.MineShaftPiece mineShaftPiece = MineshaftPieces.generateAndAddPiece(param1StructurePiece, param1StructurePieceAccessor, param1RandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() + param1RandomSource.nextInt(k) + 1, this.boundingBox.minZ() + j, Direction.EAST, i);
/* 266 */         if (mineShaftPiece != null) {
/* 267 */           BoundingBox boundingBox = mineShaftPiece.getBoundingBox();
/* 268 */           this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.maxX() - 1, boundingBox.minY(), boundingBox.minZ(), this.boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ()));
/*     */         } 
/* 270 */         j += 4;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 276 */       if (isInInvalidLocation((LevelAccessor)param1WorldGenLevel, param1BoundingBox)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 281 */       generateBox(param1WorldGenLevel, param1BoundingBox, this.boundingBox.minX(), this.boundingBox.minY() + 1, this.boundingBox.minZ(), this.boundingBox.maxX(), Math.min(this.boundingBox.minY() + 3, this.boundingBox.maxY()), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 282 */       for (BoundingBox boundingBox : this.childEntranceBoxes) {
/* 283 */         generateBox(param1WorldGenLevel, param1BoundingBox, boundingBox.minX(), boundingBox.maxY() - 2, boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/*     */       }
/* 285 */       generateUpperHalfSphere(param1WorldGenLevel, param1BoundingBox, this.boundingBox.minX(), this.boundingBox.minY() + 4, this.boundingBox.minZ(), this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, false);
/*     */     }
/*     */ 
/*     */     
/*     */     public void move(int param1Int1, int param1Int2, int param1Int3) {
/* 290 */       super.move(param1Int1, param1Int2, param1Int3);
/* 291 */       for (BoundingBox boundingBox : this.childEntranceBoxes) {
/* 292 */         boundingBox.move(param1Int1, param1Int2, param1Int3);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/* 298 */       super.addAdditionalSaveData(param1StructurePieceSerializationContext, param1CompoundTag);
/* 299 */       param1CompoundTag.store("Entrances", BoundingBox.CODEC.listOf(), this.childEntranceBoxes);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MineShaftCorridor extends MineShaftPiece {
/*     */     private final boolean hasRails;
/*     */     private final boolean spiderCorridor;
/*     */     private boolean hasPlacedSpider;
/*     */     private final int numSections;
/*     */     
/*     */     public MineShaftCorridor(CompoundTag param1CompoundTag) {
/* 310 */       super(StructurePieceType.MINE_SHAFT_CORRIDOR, param1CompoundTag);
/*     */       
/* 312 */       this.hasRails = param1CompoundTag.getBooleanOr("hr", false);
/* 313 */       this.spiderCorridor = param1CompoundTag.getBooleanOr("sc", false);
/* 314 */       this.hasPlacedSpider = param1CompoundTag.getBooleanOr("hps", false);
/* 315 */       this.numSections = param1CompoundTag.getIntOr("Num", 0);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/* 320 */       super.addAdditionalSaveData(param1StructurePieceSerializationContext, param1CompoundTag);
/* 321 */       param1CompoundTag.putBoolean("hr", this.hasRails);
/* 322 */       param1CompoundTag.putBoolean("sc", this.spiderCorridor);
/* 323 */       param1CompoundTag.putBoolean("hps", this.hasPlacedSpider);
/* 324 */       param1CompoundTag.putInt("Num", this.numSections);
/*     */     }
/*     */     
/*     */     public MineShaftCorridor(int param1Int, RandomSource param1RandomSource, BoundingBox param1BoundingBox, Direction param1Direction, MineshaftStructure.Type param1Type) {
/* 328 */       super(StructurePieceType.MINE_SHAFT_CORRIDOR, param1Int, param1Type, param1BoundingBox);
/* 329 */       setOrientation(param1Direction);
/* 330 */       this.hasRails = (param1RandomSource.nextInt(3) == 0);
/* 331 */       this.spiderCorridor = (!this.hasRails && param1RandomSource.nextInt(23) == 0);
/*     */       
/* 333 */       if (getOrientation().getAxis() == Direction.Axis.Z) {
/* 334 */         this.numSections = param1BoundingBox.getZSpan() / 5;
/*     */       } else {
/* 336 */         this.numSections = param1BoundingBox.getXSpan() / 5;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static BoundingBox findCorridorSize(StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction) {
/*     */       // Byte code:
/*     */       //   0: aload_1
/*     */       //   1: iconst_3
/*     */       //   2: invokeinterface nextInt : (I)I
/*     */       //   7: iconst_2
/*     */       //   8: iadd
/*     */       //   9: istore #6
/*     */       //   11: iload #6
/*     */       //   13: ifle -> 176
/*     */       //   16: iload #6
/*     */       //   18: iconst_5
/*     */       //   19: imul
/*     */       //   20: istore #8
/*     */       //   22: getstatic net/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$1.$SwitchMap$net$minecraft$core$Direction : [I
/*     */       //   25: aload #5
/*     */       //   27: invokevirtual ordinal : ()I
/*     */       //   30: iaload
/*     */       //   31: tableswitch default -> 60, 1 -> 60, 2 -> 82, 3 -> 103, 4 -> 125
/*     */       //   60: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   63: dup
/*     */       //   64: iconst_0
/*     */       //   65: iconst_0
/*     */       //   66: iload #8
/*     */       //   68: iconst_1
/*     */       //   69: isub
/*     */       //   70: ineg
/*     */       //   71: iconst_2
/*     */       //   72: iconst_2
/*     */       //   73: iconst_0
/*     */       //   74: invokespecial <init> : (IIIIII)V
/*     */       //   77: astore #7
/*     */       //   79: goto -> 143
/*     */       //   82: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   85: dup
/*     */       //   86: iconst_0
/*     */       //   87: iconst_0
/*     */       //   88: iconst_0
/*     */       //   89: iconst_2
/*     */       //   90: iconst_2
/*     */       //   91: iload #8
/*     */       //   93: iconst_1
/*     */       //   94: isub
/*     */       //   95: invokespecial <init> : (IIIIII)V
/*     */       //   98: astore #7
/*     */       //   100: goto -> 143
/*     */       //   103: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   106: dup
/*     */       //   107: iload #8
/*     */       //   109: iconst_1
/*     */       //   110: isub
/*     */       //   111: ineg
/*     */       //   112: iconst_0
/*     */       //   113: iconst_0
/*     */       //   114: iconst_0
/*     */       //   115: iconst_2
/*     */       //   116: iconst_2
/*     */       //   117: invokespecial <init> : (IIIIII)V
/*     */       //   120: astore #7
/*     */       //   122: goto -> 143
/*     */       //   125: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   128: dup
/*     */       //   129: iconst_0
/*     */       //   130: iconst_0
/*     */       //   131: iconst_0
/*     */       //   132: iload #8
/*     */       //   134: iconst_1
/*     */       //   135: isub
/*     */       //   136: iconst_2
/*     */       //   137: iconst_2
/*     */       //   138: invokespecial <init> : (IIIIII)V
/*     */       //   141: astore #7
/*     */       //   143: aload #7
/*     */       //   145: iload_2
/*     */       //   146: iload_3
/*     */       //   147: iload #4
/*     */       //   149: invokevirtual move : (III)Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   152: pop
/*     */       //   153: aload_0
/*     */       //   154: aload #7
/*     */       //   156: invokeinterface findCollisionPiece : (Lnet/minecraft/world/level/levelgen/structure/BoundingBox;)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */       //   161: ifnull -> 170
/*     */       //   164: iinc #6, -1
/*     */       //   167: goto -> 173
/*     */       //   170: aload #7
/*     */       //   172: areturn
/*     */       //   173: goto -> 11
/*     */       //   176: aconst_null
/*     */       //   177: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #341	-> 0
/*     */       //   #342	-> 11
/*     */       //   #344	-> 16
/*     */       //   #346	-> 22
/*     */       //   #349	-> 60
/*     */       //   #350	-> 79
/*     */       //   #352	-> 82
/*     */       //   #353	-> 100
/*     */       //   #355	-> 103
/*     */       //   #356	-> 122
/*     */       //   #358	-> 125
/*     */       //   #362	-> 143
/*     */       //   #364	-> 153
/*     */       //   #365	-> 164
/*     */       //   #367	-> 170
/*     */       //   #369	-> 173
/*     */       //   #372	-> 176
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokevirtual getGenDepth : ()I
/*     */       //   4: istore #4
/*     */       //   6: aload_3
/*     */       //   7: iconst_4
/*     */       //   8: invokeinterface nextInt : (I)I
/*     */       //   13: istore #5
/*     */       //   15: aload_0
/*     */       //   16: invokevirtual getOrientation : ()Lnet/minecraft/core/Direction;
/*     */       //   19: astore #6
/*     */       //   21: aload #6
/*     */       //   23: ifnull -> 689
/*     */       //   26: getstatic net/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$1.$SwitchMap$net$minecraft$core$Direction : [I
/*     */       //   29: aload #6
/*     */       //   31: invokevirtual ordinal : ()I
/*     */       //   34: iaload
/*     */       //   35: tableswitch default -> 64, 1 -> 64, 2 -> 219, 3 -> 378, 4 -> 533
/*     */       //   64: iload #5
/*     */       //   66: iconst_1
/*     */       //   67: if_icmpgt -> 117
/*     */       //   70: aload_1
/*     */       //   71: aload_2
/*     */       //   72: aload_3
/*     */       //   73: aload_0
/*     */       //   74: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   77: invokevirtual minX : ()I
/*     */       //   80: aload_0
/*     */       //   81: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   84: invokevirtual minY : ()I
/*     */       //   87: iconst_1
/*     */       //   88: isub
/*     */       //   89: aload_3
/*     */       //   90: iconst_3
/*     */       //   91: invokeinterface nextInt : (I)I
/*     */       //   96: iadd
/*     */       //   97: aload_0
/*     */       //   98: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   101: invokevirtual minZ : ()I
/*     */       //   104: iconst_1
/*     */       //   105: isub
/*     */       //   106: aload #6
/*     */       //   108: iload #4
/*     */       //   110: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   113: pop
/*     */       //   114: goto -> 689
/*     */       //   117: iload #5
/*     */       //   119: iconst_2
/*     */       //   120: if_icmpne -> 171
/*     */       //   123: aload_1
/*     */       //   124: aload_2
/*     */       //   125: aload_3
/*     */       //   126: aload_0
/*     */       //   127: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   130: invokevirtual minX : ()I
/*     */       //   133: iconst_1
/*     */       //   134: isub
/*     */       //   135: aload_0
/*     */       //   136: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   139: invokevirtual minY : ()I
/*     */       //   142: iconst_1
/*     */       //   143: isub
/*     */       //   144: aload_3
/*     */       //   145: iconst_3
/*     */       //   146: invokeinterface nextInt : (I)I
/*     */       //   151: iadd
/*     */       //   152: aload_0
/*     */       //   153: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   156: invokevirtual minZ : ()I
/*     */       //   159: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   162: iload #4
/*     */       //   164: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   167: pop
/*     */       //   168: goto -> 689
/*     */       //   171: aload_1
/*     */       //   172: aload_2
/*     */       //   173: aload_3
/*     */       //   174: aload_0
/*     */       //   175: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   178: invokevirtual maxX : ()I
/*     */       //   181: iconst_1
/*     */       //   182: iadd
/*     */       //   183: aload_0
/*     */       //   184: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   187: invokevirtual minY : ()I
/*     */       //   190: iconst_1
/*     */       //   191: isub
/*     */       //   192: aload_3
/*     */       //   193: iconst_3
/*     */       //   194: invokeinterface nextInt : (I)I
/*     */       //   199: iadd
/*     */       //   200: aload_0
/*     */       //   201: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   204: invokevirtual minZ : ()I
/*     */       //   207: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   210: iload #4
/*     */       //   212: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   215: pop
/*     */       //   216: goto -> 689
/*     */       //   219: iload #5
/*     */       //   221: iconst_1
/*     */       //   222: if_icmpgt -> 272
/*     */       //   225: aload_1
/*     */       //   226: aload_2
/*     */       //   227: aload_3
/*     */       //   228: aload_0
/*     */       //   229: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   232: invokevirtual minX : ()I
/*     */       //   235: aload_0
/*     */       //   236: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   239: invokevirtual minY : ()I
/*     */       //   242: iconst_1
/*     */       //   243: isub
/*     */       //   244: aload_3
/*     */       //   245: iconst_3
/*     */       //   246: invokeinterface nextInt : (I)I
/*     */       //   251: iadd
/*     */       //   252: aload_0
/*     */       //   253: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   256: invokevirtual maxZ : ()I
/*     */       //   259: iconst_1
/*     */       //   260: iadd
/*     */       //   261: aload #6
/*     */       //   263: iload #4
/*     */       //   265: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   268: pop
/*     */       //   269: goto -> 689
/*     */       //   272: iload #5
/*     */       //   274: iconst_2
/*     */       //   275: if_icmpne -> 328
/*     */       //   278: aload_1
/*     */       //   279: aload_2
/*     */       //   280: aload_3
/*     */       //   281: aload_0
/*     */       //   282: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   285: invokevirtual minX : ()I
/*     */       //   288: iconst_1
/*     */       //   289: isub
/*     */       //   290: aload_0
/*     */       //   291: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   294: invokevirtual minY : ()I
/*     */       //   297: iconst_1
/*     */       //   298: isub
/*     */       //   299: aload_3
/*     */       //   300: iconst_3
/*     */       //   301: invokeinterface nextInt : (I)I
/*     */       //   306: iadd
/*     */       //   307: aload_0
/*     */       //   308: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   311: invokevirtual maxZ : ()I
/*     */       //   314: iconst_3
/*     */       //   315: isub
/*     */       //   316: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   319: iload #4
/*     */       //   321: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   324: pop
/*     */       //   325: goto -> 689
/*     */       //   328: aload_1
/*     */       //   329: aload_2
/*     */       //   330: aload_3
/*     */       //   331: aload_0
/*     */       //   332: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   335: invokevirtual maxX : ()I
/*     */       //   338: iconst_1
/*     */       //   339: iadd
/*     */       //   340: aload_0
/*     */       //   341: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   344: invokevirtual minY : ()I
/*     */       //   347: iconst_1
/*     */       //   348: isub
/*     */       //   349: aload_3
/*     */       //   350: iconst_3
/*     */       //   351: invokeinterface nextInt : (I)I
/*     */       //   356: iadd
/*     */       //   357: aload_0
/*     */       //   358: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   361: invokevirtual maxZ : ()I
/*     */       //   364: iconst_3
/*     */       //   365: isub
/*     */       //   366: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   369: iload #4
/*     */       //   371: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   374: pop
/*     */       //   375: goto -> 689
/*     */       //   378: iload #5
/*     */       //   380: iconst_1
/*     */       //   381: if_icmpgt -> 431
/*     */       //   384: aload_1
/*     */       //   385: aload_2
/*     */       //   386: aload_3
/*     */       //   387: aload_0
/*     */       //   388: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   391: invokevirtual minX : ()I
/*     */       //   394: iconst_1
/*     */       //   395: isub
/*     */       //   396: aload_0
/*     */       //   397: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   400: invokevirtual minY : ()I
/*     */       //   403: iconst_1
/*     */       //   404: isub
/*     */       //   405: aload_3
/*     */       //   406: iconst_3
/*     */       //   407: invokeinterface nextInt : (I)I
/*     */       //   412: iadd
/*     */       //   413: aload_0
/*     */       //   414: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   417: invokevirtual minZ : ()I
/*     */       //   420: aload #6
/*     */       //   422: iload #4
/*     */       //   424: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   427: pop
/*     */       //   428: goto -> 689
/*     */       //   431: iload #5
/*     */       //   433: iconst_2
/*     */       //   434: if_icmpne -> 485
/*     */       //   437: aload_1
/*     */       //   438: aload_2
/*     */       //   439: aload_3
/*     */       //   440: aload_0
/*     */       //   441: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   444: invokevirtual minX : ()I
/*     */       //   447: aload_0
/*     */       //   448: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   451: invokevirtual minY : ()I
/*     */       //   454: iconst_1
/*     */       //   455: isub
/*     */       //   456: aload_3
/*     */       //   457: iconst_3
/*     */       //   458: invokeinterface nextInt : (I)I
/*     */       //   463: iadd
/*     */       //   464: aload_0
/*     */       //   465: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   468: invokevirtual minZ : ()I
/*     */       //   471: iconst_1
/*     */       //   472: isub
/*     */       //   473: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   476: iload #4
/*     */       //   478: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   481: pop
/*     */       //   482: goto -> 689
/*     */       //   485: aload_1
/*     */       //   486: aload_2
/*     */       //   487: aload_3
/*     */       //   488: aload_0
/*     */       //   489: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   492: invokevirtual minX : ()I
/*     */       //   495: aload_0
/*     */       //   496: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   499: invokevirtual minY : ()I
/*     */       //   502: iconst_1
/*     */       //   503: isub
/*     */       //   504: aload_3
/*     */       //   505: iconst_3
/*     */       //   506: invokeinterface nextInt : (I)I
/*     */       //   511: iadd
/*     */       //   512: aload_0
/*     */       //   513: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   516: invokevirtual maxZ : ()I
/*     */       //   519: iconst_1
/*     */       //   520: iadd
/*     */       //   521: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   524: iload #4
/*     */       //   526: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   529: pop
/*     */       //   530: goto -> 689
/*     */       //   533: iload #5
/*     */       //   535: iconst_1
/*     */       //   536: if_icmpgt -> 586
/*     */       //   539: aload_1
/*     */       //   540: aload_2
/*     */       //   541: aload_3
/*     */       //   542: aload_0
/*     */       //   543: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   546: invokevirtual maxX : ()I
/*     */       //   549: iconst_1
/*     */       //   550: iadd
/*     */       //   551: aload_0
/*     */       //   552: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   555: invokevirtual minY : ()I
/*     */       //   558: iconst_1
/*     */       //   559: isub
/*     */       //   560: aload_3
/*     */       //   561: iconst_3
/*     */       //   562: invokeinterface nextInt : (I)I
/*     */       //   567: iadd
/*     */       //   568: aload_0
/*     */       //   569: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   572: invokevirtual minZ : ()I
/*     */       //   575: aload #6
/*     */       //   577: iload #4
/*     */       //   579: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   582: pop
/*     */       //   583: goto -> 689
/*     */       //   586: iload #5
/*     */       //   588: iconst_2
/*     */       //   589: if_icmpne -> 642
/*     */       //   592: aload_1
/*     */       //   593: aload_2
/*     */       //   594: aload_3
/*     */       //   595: aload_0
/*     */       //   596: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   599: invokevirtual maxX : ()I
/*     */       //   602: iconst_3
/*     */       //   603: isub
/*     */       //   604: aload_0
/*     */       //   605: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   608: invokevirtual minY : ()I
/*     */       //   611: iconst_1
/*     */       //   612: isub
/*     */       //   613: aload_3
/*     */       //   614: iconst_3
/*     */       //   615: invokeinterface nextInt : (I)I
/*     */       //   620: iadd
/*     */       //   621: aload_0
/*     */       //   622: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   625: invokevirtual minZ : ()I
/*     */       //   628: iconst_1
/*     */       //   629: isub
/*     */       //   630: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   633: iload #4
/*     */       //   635: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   638: pop
/*     */       //   639: goto -> 689
/*     */       //   642: aload_1
/*     */       //   643: aload_2
/*     */       //   644: aload_3
/*     */       //   645: aload_0
/*     */       //   646: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   649: invokevirtual maxX : ()I
/*     */       //   652: iconst_3
/*     */       //   653: isub
/*     */       //   654: aload_0
/*     */       //   655: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   658: invokevirtual minY : ()I
/*     */       //   661: iconst_1
/*     */       //   662: isub
/*     */       //   663: aload_3
/*     */       //   664: iconst_3
/*     */       //   665: invokeinterface nextInt : (I)I
/*     */       //   670: iadd
/*     */       //   671: aload_0
/*     */       //   672: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   675: invokevirtual maxZ : ()I
/*     */       //   678: iconst_1
/*     */       //   679: iadd
/*     */       //   680: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   683: iload #4
/*     */       //   685: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   688: pop
/*     */       //   689: iload #4
/*     */       //   691: bipush #8
/*     */       //   693: if_icmpge -> 951
/*     */       //   696: aload #6
/*     */       //   698: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   701: if_acmpeq -> 712
/*     */       //   704: aload #6
/*     */       //   706: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   709: if_acmpne -> 833
/*     */       //   712: aload_0
/*     */       //   713: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   716: invokevirtual minZ : ()I
/*     */       //   719: iconst_3
/*     */       //   720: iadd
/*     */       //   721: istore #7
/*     */       //   723: iload #7
/*     */       //   725: iconst_3
/*     */       //   726: iadd
/*     */       //   727: aload_0
/*     */       //   728: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   731: invokevirtual maxZ : ()I
/*     */       //   734: if_icmpgt -> 830
/*     */       //   737: aload_3
/*     */       //   738: iconst_5
/*     */       //   739: invokeinterface nextInt : (I)I
/*     */       //   744: istore #8
/*     */       //   746: iload #8
/*     */       //   748: ifne -> 786
/*     */       //   751: aload_1
/*     */       //   752: aload_2
/*     */       //   753: aload_3
/*     */       //   754: aload_0
/*     */       //   755: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   758: invokevirtual minX : ()I
/*     */       //   761: iconst_1
/*     */       //   762: isub
/*     */       //   763: aload_0
/*     */       //   764: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   767: invokevirtual minY : ()I
/*     */       //   770: iload #7
/*     */       //   772: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   775: iload #4
/*     */       //   777: iconst_1
/*     */       //   778: iadd
/*     */       //   779: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   782: pop
/*     */       //   783: goto -> 824
/*     */       //   786: iload #8
/*     */       //   788: iconst_1
/*     */       //   789: if_icmpne -> 824
/*     */       //   792: aload_1
/*     */       //   793: aload_2
/*     */       //   794: aload_3
/*     */       //   795: aload_0
/*     */       //   796: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   799: invokevirtual maxX : ()I
/*     */       //   802: iconst_1
/*     */       //   803: iadd
/*     */       //   804: aload_0
/*     */       //   805: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   808: invokevirtual minY : ()I
/*     */       //   811: iload #7
/*     */       //   813: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   816: iload #4
/*     */       //   818: iconst_1
/*     */       //   819: iadd
/*     */       //   820: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   823: pop
/*     */       //   824: iinc #7, 5
/*     */       //   827: goto -> 723
/*     */       //   830: goto -> 951
/*     */       //   833: aload_0
/*     */       //   834: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   837: invokevirtual minX : ()I
/*     */       //   840: iconst_3
/*     */       //   841: iadd
/*     */       //   842: istore #7
/*     */       //   844: iload #7
/*     */       //   846: iconst_3
/*     */       //   847: iadd
/*     */       //   848: aload_0
/*     */       //   849: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   852: invokevirtual maxX : ()I
/*     */       //   855: if_icmpgt -> 951
/*     */       //   858: aload_3
/*     */       //   859: iconst_5
/*     */       //   860: invokeinterface nextInt : (I)I
/*     */       //   865: istore #8
/*     */       //   867: iload #8
/*     */       //   869: ifne -> 907
/*     */       //   872: aload_1
/*     */       //   873: aload_2
/*     */       //   874: aload_3
/*     */       //   875: iload #7
/*     */       //   877: aload_0
/*     */       //   878: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   881: invokevirtual minY : ()I
/*     */       //   884: aload_0
/*     */       //   885: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   888: invokevirtual minZ : ()I
/*     */       //   891: iconst_1
/*     */       //   892: isub
/*     */       //   893: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   896: iload #4
/*     */       //   898: iconst_1
/*     */       //   899: iadd
/*     */       //   900: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   903: pop
/*     */       //   904: goto -> 945
/*     */       //   907: iload #8
/*     */       //   909: iconst_1
/*     */       //   910: if_icmpne -> 945
/*     */       //   913: aload_1
/*     */       //   914: aload_2
/*     */       //   915: aload_3
/*     */       //   916: iload #7
/*     */       //   918: aload_0
/*     */       //   919: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   922: invokevirtual minY : ()I
/*     */       //   925: aload_0
/*     */       //   926: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   929: invokevirtual maxZ : ()I
/*     */       //   932: iconst_1
/*     */       //   933: iadd
/*     */       //   934: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   937: iload #4
/*     */       //   939: iconst_1
/*     */       //   940: iadd
/*     */       //   941: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   944: pop
/*     */       //   945: iinc #7, 5
/*     */       //   948: goto -> 844
/*     */       //   951: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #377	-> 0
/*     */       //   #378	-> 6
/*     */       //   #379	-> 15
/*     */       //   #380	-> 21
/*     */       //   #381	-> 26
/*     */       //   #384	-> 64
/*     */       //   #385	-> 70
/*     */       //   #386	-> 117
/*     */       //   #387	-> 123
/*     */       //   #389	-> 171
/*     */       //   #391	-> 216
/*     */       //   #393	-> 219
/*     */       //   #394	-> 225
/*     */       //   #395	-> 272
/*     */       //   #396	-> 278
/*     */       //   #398	-> 328
/*     */       //   #400	-> 375
/*     */       //   #402	-> 378
/*     */       //   #403	-> 384
/*     */       //   #404	-> 431
/*     */       //   #405	-> 437
/*     */       //   #407	-> 485
/*     */       //   #409	-> 530
/*     */       //   #411	-> 533
/*     */       //   #412	-> 539
/*     */       //   #413	-> 586
/*     */       //   #414	-> 592
/*     */       //   #416	-> 642
/*     */       //   #423	-> 689
/*     */       //   #424	-> 696
/*     */       //   #425	-> 712
/*     */       //   #426	-> 737
/*     */       //   #427	-> 746
/*     */       //   #428	-> 751
/*     */       //   #429	-> 786
/*     */       //   #430	-> 792
/*     */       //   #425	-> 824
/*     */       //   #434	-> 833
/*     */       //   #435	-> 858
/*     */       //   #436	-> 867
/*     */       //   #437	-> 872
/*     */       //   #438	-> 907
/*     */       //   #439	-> 913
/*     */       //   #434	-> 945
/*     */       //   #444	-> 951
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected boolean createChest(WorldGenLevel param1WorldGenLevel, BoundingBox param1BoundingBox, RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, ResourceKey<LootTable> param1ResourceKey) {
/* 448 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(param1Int1, param1Int2, param1Int3);
/* 449 */       if (param1BoundingBox.isInside((Vec3i)mutableBlockPos) && 
/* 450 */         param1WorldGenLevel.getBlockState((BlockPos)mutableBlockPos).isAir() && !param1WorldGenLevel.getBlockState(mutableBlockPos.below()).isAir()) {
/* 451 */         BlockState blockState = (BlockState)Blocks.RAIL.defaultBlockState().setValue((Property)RailBlock.SHAPE, param1RandomSource.nextBoolean() ? (Comparable)RailShape.NORTH_SOUTH : (Comparable)RailShape.EAST_WEST);
/* 452 */         placeBlock(param1WorldGenLevel, blockState, param1Int1, param1Int2, param1Int3, param1BoundingBox);
/* 453 */         MinecartChest minecartChest = (MinecartChest)EntityType.CHEST_MINECART.create((Level)param1WorldGenLevel.getLevel(), EntitySpawnReason.CHUNK_GENERATION);
/* 454 */         if (minecartChest != null) {
/* 455 */           minecartChest.setInitialPos(mutableBlockPos.getX() + 0.5D, mutableBlockPos.getY() + 0.5D, mutableBlockPos.getZ() + 0.5D);
/* 456 */           minecartChest.setLootTable(param1ResourceKey, param1RandomSource.nextLong());
/* 457 */           param1WorldGenLevel.addFreshEntity((Entity)minecartChest);
/*     */         } 
/* 459 */         return true;
/*     */       } 
/*     */ 
/*     */       
/* 463 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 468 */       if (isInInvalidLocation((LevelAccessor)param1WorldGenLevel, param1BoundingBox)) {
/*     */         return;
/*     */       }
/*     */       
/* 472 */       boolean bool1 = false;
/* 473 */       byte b1 = 2;
/* 474 */       boolean bool2 = false;
/* 475 */       byte b2 = 2;
/* 476 */       int i = this.numSections * 5 - 1;
/*     */       
/* 478 */       BlockState blockState = this.type.getPlanksState();
/*     */ 
/*     */       
/* 481 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 0, 2, 1, i, CAVE_AIR, CAVE_AIR, false);
/* 482 */       generateMaybeBox(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 0.8F, 0, 2, 0, 2, 2, i, CAVE_AIR, CAVE_AIR, false, false);
/*     */       
/* 484 */       if (this.spiderCorridor) {
/* 485 */         generateMaybeBox(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 0.6F, 0, 0, 0, 2, 1, i, Blocks.COBWEB.defaultBlockState(), CAVE_AIR, false, true);
/*     */       }
/*     */       
/*     */       byte b3;
/* 489 */       for (b3 = 0; b3 < this.numSections; b3++) {
/* 490 */         int j = 2 + b3 * 5;
/*     */         
/* 492 */         placeSupport(param1WorldGenLevel, param1BoundingBox, 0, 0, j, 2, 2, param1RandomSource);
/*     */         
/* 494 */         maybePlaceCobWeb(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 0.1F, 0, 2, j - 1);
/* 495 */         maybePlaceCobWeb(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 0.1F, 2, 2, j - 1);
/* 496 */         maybePlaceCobWeb(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 0.1F, 0, 2, j + 1);
/* 497 */         maybePlaceCobWeb(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 0.1F, 2, 2, j + 1);
/* 498 */         maybePlaceCobWeb(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 0.05F, 0, 2, j - 2);
/* 499 */         maybePlaceCobWeb(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 0.05F, 2, 2, j - 2);
/* 500 */         maybePlaceCobWeb(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 0.05F, 0, 2, j + 2);
/* 501 */         maybePlaceCobWeb(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 0.05F, 2, 2, j + 2);
/*     */         
/* 503 */         if (param1RandomSource.nextInt(100) == 0) {
/* 504 */           createChest(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 2, 0, j - 1, BuiltInLootTables.ABANDONED_MINESHAFT);
/*     */         }
/* 506 */         if (param1RandomSource.nextInt(100) == 0) {
/* 507 */           createChest(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 0, 0, j + 1, BuiltInLootTables.ABANDONED_MINESHAFT);
/*     */         }
/* 509 */         if (this.spiderCorridor && !this.hasPlacedSpider) {
/* 510 */           boolean bool = true;
/* 511 */           int k = j - 1 + param1RandomSource.nextInt(3);
/* 512 */           BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(1, 0, k);
/*     */           
/* 514 */           if (param1BoundingBox.isInside((Vec3i)mutableBlockPos) && isInterior((LevelReader)param1WorldGenLevel, 1, 0, k, param1BoundingBox)) {
/* 515 */             this.hasPlacedSpider = true;
/* 516 */             param1WorldGenLevel.setBlock((BlockPos)mutableBlockPos, Blocks.SPAWNER.defaultBlockState(), 2);
/*     */             
/* 518 */             BlockEntity blockEntity = param1WorldGenLevel.getBlockEntity((BlockPos)mutableBlockPos);
/* 519 */             if (blockEntity instanceof SpawnerBlockEntity) { SpawnerBlockEntity spawnerBlockEntity = (SpawnerBlockEntity)blockEntity;
/* 520 */               spawnerBlockEntity.setEntityId(EntityType.CAVE_SPIDER, param1RandomSource); }
/*     */           
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 527 */       for (b3 = 0; b3 <= 2; b3++) {
/* 528 */         for (byte b = 0; b <= i; b++) {
/* 529 */           setPlanksBlock(param1WorldGenLevel, param1BoundingBox, blockState, b3, -1, b);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 534 */       b3 = 2;
/* 535 */       placeDoubleLowerOrUpperSupport(param1WorldGenLevel, param1BoundingBox, 0, -1, 2);
/* 536 */       if (this.numSections > 1) {
/* 537 */         int j = i - 2;
/* 538 */         placeDoubleLowerOrUpperSupport(param1WorldGenLevel, param1BoundingBox, 0, -1, j);
/*     */       } 
/*     */       
/* 541 */       if (this.hasRails) {
/* 542 */         BlockState blockState1 = (BlockState)Blocks.RAIL.defaultBlockState().setValue((Property)RailBlock.SHAPE, (Comparable)RailShape.NORTH_SOUTH);
/* 543 */         for (byte b = 0; b <= i; b++) {
/* 544 */           BlockState blockState2 = getBlock((BlockGetter)param1WorldGenLevel, 1, -1, b, param1BoundingBox);
/* 545 */           if (!blockState2.isAir() && blockState2.isSolidRender()) {
/* 546 */             float f = isInterior((LevelReader)param1WorldGenLevel, 1, 0, b, param1BoundingBox) ? 0.7F : 0.9F;
/* 547 */             maybeGenerateBlock(param1WorldGenLevel, param1BoundingBox, param1RandomSource, f, 1, 0, b, blockState1);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void placeDoubleLowerOrUpperSupport(WorldGenLevel param1WorldGenLevel, BoundingBox param1BoundingBox, int param1Int1, int param1Int2, int param1Int3) {
/* 554 */       BlockState blockState1 = this.type.getWoodState();
/* 555 */       BlockState blockState2 = this.type.getPlanksState();
/* 556 */       if (getBlock((BlockGetter)param1WorldGenLevel, param1Int1, param1Int2, param1Int3, param1BoundingBox).is(blockState2.getBlock())) {
/* 557 */         fillPillarDownOrChainUp(param1WorldGenLevel, blockState1, param1Int1, param1Int2, param1Int3, param1BoundingBox);
/*     */       }
/* 559 */       if (getBlock((BlockGetter)param1WorldGenLevel, param1Int1 + 2, param1Int2, param1Int3, param1BoundingBox).is(blockState2.getBlock())) {
/* 560 */         fillPillarDownOrChainUp(param1WorldGenLevel, blockState1, param1Int1 + 2, param1Int2, param1Int3, param1BoundingBox);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     protected void fillColumnDown(WorldGenLevel param1WorldGenLevel, BlockState param1BlockState, int param1Int1, int param1Int2, int param1Int3, BoundingBox param1BoundingBox) {
/* 566 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(param1Int1, param1Int2, param1Int3);
/* 567 */       if (!param1BoundingBox.isInside((Vec3i)mutableBlockPos)) {
/*     */         return;
/*     */       }
/*     */       
/* 571 */       int i = mutableBlockPos.getY();
/*     */ 
/*     */       
/* 574 */       while (isReplaceableByStructures(param1WorldGenLevel.getBlockState((BlockPos)mutableBlockPos)) && mutableBlockPos.getY() > param1WorldGenLevel.getMinY() + 1) {
/* 575 */         mutableBlockPos.move(Direction.DOWN);
/*     */       }
/* 577 */       if (!canPlaceColumnOnTopOf((LevelReader)param1WorldGenLevel, (BlockPos)mutableBlockPos, param1WorldGenLevel.getBlockState((BlockPos)mutableBlockPos))) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 582 */       while (mutableBlockPos.getY() < i) {
/* 583 */         mutableBlockPos.move(Direction.UP);
/* 584 */         param1WorldGenLevel.setBlock((BlockPos)mutableBlockPos, param1BlockState, 2);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected void fillPillarDownOrChainUp(WorldGenLevel param1WorldGenLevel, BlockState param1BlockState, int param1Int1, int param1Int2, int param1Int3, BoundingBox param1BoundingBox) {
/* 590 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(param1Int1, param1Int2, param1Int3);
/* 591 */       if (!param1BoundingBox.isInside((Vec3i)mutableBlockPos)) {
/*     */         return;
/*     */       }
/*     */       
/* 595 */       int i = mutableBlockPos.getY();
/*     */ 
/*     */       
/* 598 */       byte b = 1;
/*     */       
/* 600 */       boolean bool1 = true;
/* 601 */       boolean bool2 = true;
/* 602 */       while (bool1 || bool2) {
/* 603 */         if (bool1) {
/* 604 */           mutableBlockPos.setY(i - b);
/* 605 */           BlockState blockState = param1WorldGenLevel.getBlockState((BlockPos)mutableBlockPos);
/* 606 */           boolean bool = (isReplaceableByStructures(blockState) && !blockState.is(Blocks.LAVA)) ? true : false;
/* 607 */           if (!bool && canPlaceColumnOnTopOf((LevelReader)param1WorldGenLevel, (BlockPos)mutableBlockPos, blockState)) {
/* 608 */             fillColumnBetween(param1WorldGenLevel, param1BlockState, mutableBlockPos, i - b + 1, i);
/*     */             return;
/*     */           } 
/* 611 */           bool1 = (b <= 20 && bool && mutableBlockPos.getY() > param1WorldGenLevel.getMinY() + 1) ? true : false;
/*     */         } 
/*     */         
/* 614 */         if (bool2) {
/* 615 */           mutableBlockPos.setY(i + b);
/* 616 */           BlockState blockState = param1WorldGenLevel.getBlockState((BlockPos)mutableBlockPos);
/* 617 */           boolean bool = isReplaceableByStructures(blockState);
/* 618 */           if (!bool && canHangChainBelow((LevelReader)param1WorldGenLevel, (BlockPos)mutableBlockPos, blockState)) {
/*     */             
/* 620 */             param1WorldGenLevel.setBlock((BlockPos)mutableBlockPos.setY(i + 1), this.type.getFenceState(), 2);
/* 621 */             fillColumnBetween(param1WorldGenLevel, Blocks.IRON_CHAIN.defaultBlockState(), mutableBlockPos, i + 2, i + b);
/*     */             return;
/*     */           } 
/* 624 */           bool2 = (b <= 50 && bool && mutableBlockPos.getY() < param1WorldGenLevel.getMaxY()) ? true : false;
/*     */         } 
/*     */         
/* 627 */         b++;
/*     */       } 
/*     */     }
/*     */     
/*     */     private static void fillColumnBetween(WorldGenLevel param1WorldGenLevel, BlockState param1BlockState, BlockPos.MutableBlockPos param1MutableBlockPos, int param1Int1, int param1Int2) {
/* 632 */       for (int i = param1Int1; i < param1Int2; i++) {
/* 633 */         param1WorldGenLevel.setBlock((BlockPos)param1MutableBlockPos.setY(i), param1BlockState, 2);
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean canPlaceColumnOnTopOf(LevelReader param1LevelReader, BlockPos param1BlockPos, BlockState param1BlockState) {
/* 638 */       return param1BlockState.isFaceSturdy((BlockGetter)param1LevelReader, param1BlockPos, Direction.UP);
/*     */     }
/*     */     
/*     */     private boolean canHangChainBelow(LevelReader param1LevelReader, BlockPos param1BlockPos, BlockState param1BlockState) {
/* 642 */       return (Block.canSupportCenter(param1LevelReader, param1BlockPos, Direction.DOWN) && !(param1BlockState.getBlock() instanceof net.minecraft.world.level.block.FallingBlock));
/*     */     }
/*     */ 
/*     */     
/*     */     private void placeSupport(WorldGenLevel param1WorldGenLevel, BoundingBox param1BoundingBox, int param1Int1, int param1Int2, int param1Int3, int param1Int4, int param1Int5, RandomSource param1RandomSource) {
/* 647 */       if (!isSupportingBox((BlockGetter)param1WorldGenLevel, param1BoundingBox, param1Int1, param1Int5, param1Int4, param1Int3)) {
/*     */         return;
/*     */       }
/*     */       
/* 651 */       BlockState blockState1 = this.type.getPlanksState();
/* 652 */       BlockState blockState2 = this.type.getFenceState();
/*     */       
/* 654 */       generateBox(param1WorldGenLevel, param1BoundingBox, param1Int1, param1Int2, param1Int3, param1Int1, param1Int4 - 1, param1Int3, (BlockState)blockState2.setValue((Property)FenceBlock.WEST, Boolean.valueOf(true)), CAVE_AIR, false);
/* 655 */       generateBox(param1WorldGenLevel, param1BoundingBox, param1Int5, param1Int2, param1Int3, param1Int5, param1Int4 - 1, param1Int3, (BlockState)blockState2.setValue((Property)FenceBlock.EAST, Boolean.valueOf(true)), CAVE_AIR, false);
/* 656 */       if (param1RandomSource.nextInt(4) == 0) {
/* 657 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int1, param1Int4, param1Int3, param1Int1, param1Int4, param1Int3, blockState1, CAVE_AIR, false);
/* 658 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int5, param1Int4, param1Int3, param1Int5, param1Int4, param1Int3, blockState1, CAVE_AIR, false);
/*     */       } else {
/* 660 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int1, param1Int4, param1Int3, param1Int5, param1Int4, param1Int3, blockState1, CAVE_AIR, false);
/* 661 */         maybeGenerateBlock(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 0.05F, param1Int1 + 1, param1Int4, param1Int3 - 1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.SOUTH));
/* 662 */         maybeGenerateBlock(param1WorldGenLevel, param1BoundingBox, param1RandomSource, 0.05F, param1Int1 + 1, param1Int4, param1Int3 + 1, (BlockState)Blocks.WALL_TORCH.defaultBlockState().setValue((Property)WallTorchBlock.FACING, (Comparable)Direction.NORTH));
/*     */       } 
/*     */     }
/*     */     
/*     */     private void maybePlaceCobWeb(WorldGenLevel param1WorldGenLevel, BoundingBox param1BoundingBox, RandomSource param1RandomSource, float param1Float, int param1Int1, int param1Int2, int param1Int3) {
/* 667 */       if (isInterior((LevelReader)param1WorldGenLevel, param1Int1, param1Int2, param1Int3, param1BoundingBox) && param1RandomSource.nextFloat() < param1Float && hasSturdyNeighbours(param1WorldGenLevel, param1BoundingBox, param1Int1, param1Int2, param1Int3, 2)) {
/* 668 */         placeBlock(param1WorldGenLevel, Blocks.COBWEB.defaultBlockState(), param1Int1, param1Int2, param1Int3, param1BoundingBox);
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean hasSturdyNeighbours(WorldGenLevel param1WorldGenLevel, BoundingBox param1BoundingBox, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
/* 673 */       BlockPos.MutableBlockPos mutableBlockPos = getWorldPos(param1Int1, param1Int2, param1Int3);
/* 674 */       byte b = 0;
/* 675 */       for (Direction direction : Direction.values()) {
/* 676 */         mutableBlockPos.move(direction);
/*     */         
/* 678 */         b++;
/* 679 */         if (param1BoundingBox.isInside((Vec3i)mutableBlockPos) && param1WorldGenLevel.getBlockState((BlockPos)mutableBlockPos).isFaceSturdy((BlockGetter)param1WorldGenLevel, (BlockPos)mutableBlockPos, direction.getOpposite()) && b >= param1Int4) {
/* 680 */           return true;
/*     */         }
/*     */         
/* 683 */         mutableBlockPos.move(direction.getOpposite());
/*     */       } 
/* 685 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MineShaftCrossing extends MineShaftPiece {
/*     */     private final Direction direction;
/*     */     private final boolean isTwoFloored;
/*     */     
/*     */     public MineShaftCrossing(CompoundTag param1CompoundTag) {
/* 694 */       super(StructurePieceType.MINE_SHAFT_CROSSING, param1CompoundTag);
/* 695 */       this.isTwoFloored = param1CompoundTag.getBooleanOr("tf", false);
/* 696 */       this.direction = param1CompoundTag.read("D", Direction.LEGACY_ID_CODEC_2D).orElse(Direction.SOUTH);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addAdditionalSaveData(StructurePieceSerializationContext param1StructurePieceSerializationContext, CompoundTag param1CompoundTag) {
/* 701 */       super.addAdditionalSaveData(param1StructurePieceSerializationContext, param1CompoundTag);
/* 702 */       param1CompoundTag.putBoolean("tf", this.isTwoFloored);
/* 703 */       param1CompoundTag.store("D", Direction.LEGACY_ID_CODEC_2D, this.direction);
/*     */     }
/*     */     
/*     */     public MineShaftCrossing(int param1Int, BoundingBox param1BoundingBox, Direction param1Direction, MineshaftStructure.Type param1Type) {
/* 707 */       super(StructurePieceType.MINE_SHAFT_CROSSING, param1Int, param1Type, param1BoundingBox);
/*     */       
/* 709 */       this.direction = param1Direction;
/* 710 */       this.isTwoFloored = (param1BoundingBox.getYSpan() > 3);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static BoundingBox findCrossing(StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction) {
/*     */       // Byte code:
/*     */       //   0: aload_1
/*     */       //   1: iconst_4
/*     */       //   2: invokeinterface nextInt : (I)I
/*     */       //   7: ifne -> 17
/*     */       //   10: bipush #6
/*     */       //   12: istore #6
/*     */       //   14: goto -> 20
/*     */       //   17: iconst_2
/*     */       //   18: istore #6
/*     */       //   20: getstatic net/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$1.$SwitchMap$net$minecraft$core$Direction : [I
/*     */       //   23: aload #5
/*     */       //   25: invokevirtual ordinal : ()I
/*     */       //   28: iaload
/*     */       //   29: tableswitch default -> 60, 1 -> 60, 2 -> 80, 3 -> 99, 4 -> 119
/*     */       //   60: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   63: dup
/*     */       //   64: iconst_m1
/*     */       //   65: iconst_0
/*     */       //   66: bipush #-4
/*     */       //   68: iconst_3
/*     */       //   69: iload #6
/*     */       //   71: iconst_0
/*     */       //   72: invokespecial <init> : (IIIIII)V
/*     */       //   75: astore #7
/*     */       //   77: goto -> 135
/*     */       //   80: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   83: dup
/*     */       //   84: iconst_m1
/*     */       //   85: iconst_0
/*     */       //   86: iconst_0
/*     */       //   87: iconst_3
/*     */       //   88: iload #6
/*     */       //   90: iconst_4
/*     */       //   91: invokespecial <init> : (IIIIII)V
/*     */       //   94: astore #7
/*     */       //   96: goto -> 135
/*     */       //   99: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   102: dup
/*     */       //   103: bipush #-4
/*     */       //   105: iconst_0
/*     */       //   106: iconst_m1
/*     */       //   107: iconst_0
/*     */       //   108: iload #6
/*     */       //   110: iconst_3
/*     */       //   111: invokespecial <init> : (IIIIII)V
/*     */       //   114: astore #7
/*     */       //   116: goto -> 135
/*     */       //   119: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   122: dup
/*     */       //   123: iconst_0
/*     */       //   124: iconst_0
/*     */       //   125: iconst_m1
/*     */       //   126: iconst_4
/*     */       //   127: iload #6
/*     */       //   129: iconst_3
/*     */       //   130: invokespecial <init> : (IIIIII)V
/*     */       //   133: astore #7
/*     */       //   135: aload #7
/*     */       //   137: iload_2
/*     */       //   138: iload_3
/*     */       //   139: iload #4
/*     */       //   141: invokevirtual move : (III)Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   144: pop
/*     */       //   145: aload_0
/*     */       //   146: aload #7
/*     */       //   148: invokeinterface findCollisionPiece : (Lnet/minecraft/world/level/levelgen/structure/BoundingBox;)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */       //   153: ifnull -> 158
/*     */       //   156: aconst_null
/*     */       //   157: areturn
/*     */       //   158: aload #7
/*     */       //   160: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #715	-> 0
/*     */       //   #716	-> 10
/*     */       //   #718	-> 17
/*     */       //   #722	-> 20
/*     */       //   #725	-> 60
/*     */       //   #726	-> 77
/*     */       //   #728	-> 80
/*     */       //   #729	-> 96
/*     */       //   #731	-> 99
/*     */       //   #732	-> 116
/*     */       //   #734	-> 119
/*     */       //   #738	-> 135
/*     */       //   #740	-> 145
/*     */       //   #741	-> 156
/*     */       //   #744	-> 158
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokevirtual getGenDepth : ()I
/*     */       //   4: istore #4
/*     */       //   6: getstatic net/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$1.$SwitchMap$net$minecraft$core$Direction : [I
/*     */       //   9: aload_0
/*     */       //   10: getfield direction : Lnet/minecraft/core/Direction;
/*     */       //   13: invokevirtual ordinal : ()I
/*     */       //   16: iaload
/*     */       //   17: tableswitch default -> 48, 1 -> 48, 2 -> 162, 3 -> 276, 4 -> 390
/*     */       //   48: aload_1
/*     */       //   49: aload_2
/*     */       //   50: aload_3
/*     */       //   51: aload_0
/*     */       //   52: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   55: invokevirtual minX : ()I
/*     */       //   58: iconst_1
/*     */       //   59: iadd
/*     */       //   60: aload_0
/*     */       //   61: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   64: invokevirtual minY : ()I
/*     */       //   67: aload_0
/*     */       //   68: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   71: invokevirtual minZ : ()I
/*     */       //   74: iconst_1
/*     */       //   75: isub
/*     */       //   76: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   79: iload #4
/*     */       //   81: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   84: pop
/*     */       //   85: aload_1
/*     */       //   86: aload_2
/*     */       //   87: aload_3
/*     */       //   88: aload_0
/*     */       //   89: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   92: invokevirtual minX : ()I
/*     */       //   95: iconst_1
/*     */       //   96: isub
/*     */       //   97: aload_0
/*     */       //   98: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   101: invokevirtual minY : ()I
/*     */       //   104: aload_0
/*     */       //   105: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   108: invokevirtual minZ : ()I
/*     */       //   111: iconst_1
/*     */       //   112: iadd
/*     */       //   113: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   116: iload #4
/*     */       //   118: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   121: pop
/*     */       //   122: aload_1
/*     */       //   123: aload_2
/*     */       //   124: aload_3
/*     */       //   125: aload_0
/*     */       //   126: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   129: invokevirtual maxX : ()I
/*     */       //   132: iconst_1
/*     */       //   133: iadd
/*     */       //   134: aload_0
/*     */       //   135: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   138: invokevirtual minY : ()I
/*     */       //   141: aload_0
/*     */       //   142: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   145: invokevirtual minZ : ()I
/*     */       //   148: iconst_1
/*     */       //   149: iadd
/*     */       //   150: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   153: iload #4
/*     */       //   155: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   158: pop
/*     */       //   159: goto -> 501
/*     */       //   162: aload_1
/*     */       //   163: aload_2
/*     */       //   164: aload_3
/*     */       //   165: aload_0
/*     */       //   166: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   169: invokevirtual minX : ()I
/*     */       //   172: iconst_1
/*     */       //   173: iadd
/*     */       //   174: aload_0
/*     */       //   175: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   178: invokevirtual minY : ()I
/*     */       //   181: aload_0
/*     */       //   182: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   185: invokevirtual maxZ : ()I
/*     */       //   188: iconst_1
/*     */       //   189: iadd
/*     */       //   190: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   193: iload #4
/*     */       //   195: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   198: pop
/*     */       //   199: aload_1
/*     */       //   200: aload_2
/*     */       //   201: aload_3
/*     */       //   202: aload_0
/*     */       //   203: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   206: invokevirtual minX : ()I
/*     */       //   209: iconst_1
/*     */       //   210: isub
/*     */       //   211: aload_0
/*     */       //   212: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   215: invokevirtual minY : ()I
/*     */       //   218: aload_0
/*     */       //   219: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   222: invokevirtual minZ : ()I
/*     */       //   225: iconst_1
/*     */       //   226: iadd
/*     */       //   227: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   230: iload #4
/*     */       //   232: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   235: pop
/*     */       //   236: aload_1
/*     */       //   237: aload_2
/*     */       //   238: aload_3
/*     */       //   239: aload_0
/*     */       //   240: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   243: invokevirtual maxX : ()I
/*     */       //   246: iconst_1
/*     */       //   247: iadd
/*     */       //   248: aload_0
/*     */       //   249: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   252: invokevirtual minY : ()I
/*     */       //   255: aload_0
/*     */       //   256: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   259: invokevirtual minZ : ()I
/*     */       //   262: iconst_1
/*     */       //   263: iadd
/*     */       //   264: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   267: iload #4
/*     */       //   269: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   272: pop
/*     */       //   273: goto -> 501
/*     */       //   276: aload_1
/*     */       //   277: aload_2
/*     */       //   278: aload_3
/*     */       //   279: aload_0
/*     */       //   280: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   283: invokevirtual minX : ()I
/*     */       //   286: iconst_1
/*     */       //   287: iadd
/*     */       //   288: aload_0
/*     */       //   289: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   292: invokevirtual minY : ()I
/*     */       //   295: aload_0
/*     */       //   296: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   299: invokevirtual minZ : ()I
/*     */       //   302: iconst_1
/*     */       //   303: isub
/*     */       //   304: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   307: iload #4
/*     */       //   309: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   312: pop
/*     */       //   313: aload_1
/*     */       //   314: aload_2
/*     */       //   315: aload_3
/*     */       //   316: aload_0
/*     */       //   317: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   320: invokevirtual minX : ()I
/*     */       //   323: iconst_1
/*     */       //   324: iadd
/*     */       //   325: aload_0
/*     */       //   326: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   329: invokevirtual minY : ()I
/*     */       //   332: aload_0
/*     */       //   333: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   336: invokevirtual maxZ : ()I
/*     */       //   339: iconst_1
/*     */       //   340: iadd
/*     */       //   341: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   344: iload #4
/*     */       //   346: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   349: pop
/*     */       //   350: aload_1
/*     */       //   351: aload_2
/*     */       //   352: aload_3
/*     */       //   353: aload_0
/*     */       //   354: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   357: invokevirtual minX : ()I
/*     */       //   360: iconst_1
/*     */       //   361: isub
/*     */       //   362: aload_0
/*     */       //   363: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   366: invokevirtual minY : ()I
/*     */       //   369: aload_0
/*     */       //   370: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   373: invokevirtual minZ : ()I
/*     */       //   376: iconst_1
/*     */       //   377: iadd
/*     */       //   378: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   381: iload #4
/*     */       //   383: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   386: pop
/*     */       //   387: goto -> 501
/*     */       //   390: aload_1
/*     */       //   391: aload_2
/*     */       //   392: aload_3
/*     */       //   393: aload_0
/*     */       //   394: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   397: invokevirtual minX : ()I
/*     */       //   400: iconst_1
/*     */       //   401: iadd
/*     */       //   402: aload_0
/*     */       //   403: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   406: invokevirtual minY : ()I
/*     */       //   409: aload_0
/*     */       //   410: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   413: invokevirtual minZ : ()I
/*     */       //   416: iconst_1
/*     */       //   417: isub
/*     */       //   418: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   421: iload #4
/*     */       //   423: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   426: pop
/*     */       //   427: aload_1
/*     */       //   428: aload_2
/*     */       //   429: aload_3
/*     */       //   430: aload_0
/*     */       //   431: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   434: invokevirtual minX : ()I
/*     */       //   437: iconst_1
/*     */       //   438: iadd
/*     */       //   439: aload_0
/*     */       //   440: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   443: invokevirtual minY : ()I
/*     */       //   446: aload_0
/*     */       //   447: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   450: invokevirtual maxZ : ()I
/*     */       //   453: iconst_1
/*     */       //   454: iadd
/*     */       //   455: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   458: iload #4
/*     */       //   460: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   463: pop
/*     */       //   464: aload_1
/*     */       //   465: aload_2
/*     */       //   466: aload_3
/*     */       //   467: aload_0
/*     */       //   468: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   471: invokevirtual maxX : ()I
/*     */       //   474: iconst_1
/*     */       //   475: iadd
/*     */       //   476: aload_0
/*     */       //   477: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   480: invokevirtual minY : ()I
/*     */       //   483: aload_0
/*     */       //   484: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   487: invokevirtual minZ : ()I
/*     */       //   490: iconst_1
/*     */       //   491: iadd
/*     */       //   492: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   495: iload #4
/*     */       //   497: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   500: pop
/*     */       //   501: aload_0
/*     */       //   502: getfield isTwoFloored : Z
/*     */       //   505: ifeq -> 708
/*     */       //   508: aload_3
/*     */       //   509: invokeinterface nextBoolean : ()Z
/*     */       //   514: ifeq -> 558
/*     */       //   517: aload_1
/*     */       //   518: aload_2
/*     */       //   519: aload_3
/*     */       //   520: aload_0
/*     */       //   521: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   524: invokevirtual minX : ()I
/*     */       //   527: iconst_1
/*     */       //   528: iadd
/*     */       //   529: aload_0
/*     */       //   530: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   533: invokevirtual minY : ()I
/*     */       //   536: iconst_3
/*     */       //   537: iadd
/*     */       //   538: iconst_1
/*     */       //   539: iadd
/*     */       //   540: aload_0
/*     */       //   541: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   544: invokevirtual minZ : ()I
/*     */       //   547: iconst_1
/*     */       //   548: isub
/*     */       //   549: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   552: iload #4
/*     */       //   554: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   557: pop
/*     */       //   558: aload_3
/*     */       //   559: invokeinterface nextBoolean : ()Z
/*     */       //   564: ifeq -> 608
/*     */       //   567: aload_1
/*     */       //   568: aload_2
/*     */       //   569: aload_3
/*     */       //   570: aload_0
/*     */       //   571: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   574: invokevirtual minX : ()I
/*     */       //   577: iconst_1
/*     */       //   578: isub
/*     */       //   579: aload_0
/*     */       //   580: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   583: invokevirtual minY : ()I
/*     */       //   586: iconst_3
/*     */       //   587: iadd
/*     */       //   588: iconst_1
/*     */       //   589: iadd
/*     */       //   590: aload_0
/*     */       //   591: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   594: invokevirtual minZ : ()I
/*     */       //   597: iconst_1
/*     */       //   598: iadd
/*     */       //   599: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   602: iload #4
/*     */       //   604: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   607: pop
/*     */       //   608: aload_3
/*     */       //   609: invokeinterface nextBoolean : ()Z
/*     */       //   614: ifeq -> 658
/*     */       //   617: aload_1
/*     */       //   618: aload_2
/*     */       //   619: aload_3
/*     */       //   620: aload_0
/*     */       //   621: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   624: invokevirtual maxX : ()I
/*     */       //   627: iconst_1
/*     */       //   628: iadd
/*     */       //   629: aload_0
/*     */       //   630: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   633: invokevirtual minY : ()I
/*     */       //   636: iconst_3
/*     */       //   637: iadd
/*     */       //   638: iconst_1
/*     */       //   639: iadd
/*     */       //   640: aload_0
/*     */       //   641: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   644: invokevirtual minZ : ()I
/*     */       //   647: iconst_1
/*     */       //   648: iadd
/*     */       //   649: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   652: iload #4
/*     */       //   654: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   657: pop
/*     */       //   658: aload_3
/*     */       //   659: invokeinterface nextBoolean : ()Z
/*     */       //   664: ifeq -> 708
/*     */       //   667: aload_1
/*     */       //   668: aload_2
/*     */       //   669: aload_3
/*     */       //   670: aload_0
/*     */       //   671: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   674: invokevirtual minX : ()I
/*     */       //   677: iconst_1
/*     */       //   678: iadd
/*     */       //   679: aload_0
/*     */       //   680: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   683: invokevirtual minY : ()I
/*     */       //   686: iconst_3
/*     */       //   687: iadd
/*     */       //   688: iconst_1
/*     */       //   689: iadd
/*     */       //   690: aload_0
/*     */       //   691: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   694: invokevirtual maxZ : ()I
/*     */       //   697: iconst_1
/*     */       //   698: iadd
/*     */       //   699: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   702: iload #4
/*     */       //   704: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   707: pop
/*     */       //   708: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #749	-> 0
/*     */       //   #752	-> 6
/*     */       //   #755	-> 48
/*     */       //   #756	-> 85
/*     */       //   #757	-> 122
/*     */       //   #758	-> 159
/*     */       //   #760	-> 162
/*     */       //   #761	-> 199
/*     */       //   #762	-> 236
/*     */       //   #763	-> 273
/*     */       //   #765	-> 276
/*     */       //   #766	-> 313
/*     */       //   #767	-> 350
/*     */       //   #768	-> 387
/*     */       //   #770	-> 390
/*     */       //   #771	-> 427
/*     */       //   #772	-> 464
/*     */       //   #776	-> 501
/*     */       //   #777	-> 508
/*     */       //   #778	-> 517
/*     */       //   #780	-> 558
/*     */       //   #781	-> 567
/*     */       //   #783	-> 608
/*     */       //   #784	-> 617
/*     */       //   #786	-> 658
/*     */       //   #787	-> 667
/*     */       //   #790	-> 708
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 794 */       if (isInInvalidLocation((LevelAccessor)param1WorldGenLevel, param1BoundingBox)) {
/*     */         return;
/*     */       }
/*     */       
/* 798 */       BlockState blockState = this.type.getPlanksState();
/*     */ 
/*     */       
/* 801 */       if (this.isTwoFloored) {
/* 802 */         generateBox(param1WorldGenLevel, param1BoundingBox, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3 - 1, this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 803 */         generateBox(param1WorldGenLevel, param1BoundingBox, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.minY() + 3 - 1, this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/* 804 */         generateBox(param1WorldGenLevel, param1BoundingBox, this.boundingBox.minX() + 1, this.boundingBox.maxY() - 2, this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 805 */         generateBox(param1WorldGenLevel, param1BoundingBox, this.boundingBox.minX(), this.boundingBox.maxY() - 2, this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/* 806 */         generateBox(param1WorldGenLevel, param1BoundingBox, this.boundingBox.minX() + 1, this.boundingBox.minY() + 3, this.boundingBox.minZ() + 1, this.boundingBox.maxX() - 1, this.boundingBox.minY() + 3, this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/*     */       } else {
/* 808 */         generateBox(param1WorldGenLevel, param1BoundingBox, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ(), this.boundingBox.maxX() - 1, this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 809 */         generateBox(param1WorldGenLevel, param1BoundingBox, this.boundingBox.minX(), this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ() - 1, CAVE_AIR, CAVE_AIR, false);
/*     */       } 
/*     */ 
/*     */       
/* 813 */       placeSupportPillar(param1WorldGenLevel, param1BoundingBox, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
/* 814 */       placeSupportPillar(param1WorldGenLevel, param1BoundingBox, this.boundingBox.minX() + 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
/* 815 */       placeSupportPillar(param1WorldGenLevel, param1BoundingBox, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.minZ() + 1, this.boundingBox.maxY());
/* 816 */       placeSupportPillar(param1WorldGenLevel, param1BoundingBox, this.boundingBox.maxX() - 1, this.boundingBox.minY(), this.boundingBox.maxZ() - 1, this.boundingBox.maxY());
/*     */ 
/*     */ 
/*     */       
/* 820 */       int i = this.boundingBox.minY() - 1;
/* 821 */       for (int j = this.boundingBox.minX(); j <= this.boundingBox.maxX(); j++) {
/* 822 */         for (int k = this.boundingBox.minZ(); k <= this.boundingBox.maxZ(); k++) {
/* 823 */           setPlanksBlock(param1WorldGenLevel, param1BoundingBox, blockState, j, i, k);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/*     */     private void placeSupportPillar(WorldGenLevel param1WorldGenLevel, BoundingBox param1BoundingBox, int param1Int1, int param1Int2, int param1Int3, int param1Int4) {
/* 829 */       if (!getBlock((BlockGetter)param1WorldGenLevel, param1Int1, param1Int4 + 1, param1Int3, param1BoundingBox).isAir())
/* 830 */         generateBox(param1WorldGenLevel, param1BoundingBox, param1Int1, param1Int2, param1Int3, param1Int1, param1Int4, param1Int3, this.type.getPlanksState(), CAVE_AIR, false); 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MineShaftStairs
/*     */     extends MineShaftPiece {
/*     */     public MineShaftStairs(int param1Int, BoundingBox param1BoundingBox, Direction param1Direction, MineshaftStructure.Type param1Type) {
/* 837 */       super(StructurePieceType.MINE_SHAFT_STAIRS, param1Int, param1Type, param1BoundingBox);
/* 838 */       setOrientation(param1Direction);
/*     */     }
/*     */     
/*     */     public MineShaftStairs(CompoundTag param1CompoundTag) {
/* 842 */       super(StructurePieceType.MINE_SHAFT_STAIRS, param1CompoundTag);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static BoundingBox findStairs(StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource, int param1Int1, int param1Int2, int param1Int3, Direction param1Direction) {
/*     */       // Byte code:
/*     */       //   0: getstatic net/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$1.$SwitchMap$net$minecraft$core$Direction : [I
/*     */       //   3: aload #5
/*     */       //   5: invokevirtual ordinal : ()I
/*     */       //   8: iaload
/*     */       //   9: tableswitch default -> 40, 1 -> 40, 2 -> 60, 3 -> 80, 4 -> 100
/*     */       //   40: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   43: dup
/*     */       //   44: iconst_0
/*     */       //   45: bipush #-5
/*     */       //   47: bipush #-8
/*     */       //   49: iconst_2
/*     */       //   50: iconst_2
/*     */       //   51: iconst_0
/*     */       //   52: invokespecial <init> : (IIIIII)V
/*     */       //   55: astore #6
/*     */       //   57: goto -> 117
/*     */       //   60: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   63: dup
/*     */       //   64: iconst_0
/*     */       //   65: bipush #-5
/*     */       //   67: iconst_0
/*     */       //   68: iconst_2
/*     */       //   69: iconst_2
/*     */       //   70: bipush #8
/*     */       //   72: invokespecial <init> : (IIIIII)V
/*     */       //   75: astore #6
/*     */       //   77: goto -> 117
/*     */       //   80: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   83: dup
/*     */       //   84: bipush #-8
/*     */       //   86: bipush #-5
/*     */       //   88: iconst_0
/*     */       //   89: iconst_0
/*     */       //   90: iconst_2
/*     */       //   91: iconst_2
/*     */       //   92: invokespecial <init> : (IIIIII)V
/*     */       //   95: astore #6
/*     */       //   97: goto -> 117
/*     */       //   100: new net/minecraft/world/level/levelgen/structure/BoundingBox
/*     */       //   103: dup
/*     */       //   104: iconst_0
/*     */       //   105: bipush #-5
/*     */       //   107: iconst_0
/*     */       //   108: bipush #8
/*     */       //   110: iconst_2
/*     */       //   111: iconst_2
/*     */       //   112: invokespecial <init> : (IIIIII)V
/*     */       //   115: astore #6
/*     */       //   117: aload #6
/*     */       //   119: iload_2
/*     */       //   120: iload_3
/*     */       //   121: iload #4
/*     */       //   123: invokevirtual move : (III)Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   126: pop
/*     */       //   127: aload_0
/*     */       //   128: aload #6
/*     */       //   130: invokeinterface findCollisionPiece : (Lnet/minecraft/world/level/levelgen/structure/BoundingBox;)Lnet/minecraft/world/level/levelgen/structure/StructurePiece;
/*     */       //   135: ifnull -> 140
/*     */       //   138: aconst_null
/*     */       //   139: areturn
/*     */       //   140: aload #6
/*     */       //   142: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #848	-> 0
/*     */       //   #851	-> 40
/*     */       //   #852	-> 57
/*     */       //   #854	-> 60
/*     */       //   #855	-> 77
/*     */       //   #857	-> 80
/*     */       //   #858	-> 97
/*     */       //   #860	-> 100
/*     */       //   #864	-> 117
/*     */       //   #866	-> 127
/*     */       //   #867	-> 138
/*     */       //   #870	-> 140
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void addChildren(StructurePiece param1StructurePiece, StructurePieceAccessor param1StructurePieceAccessor, RandomSource param1RandomSource) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokevirtual getGenDepth : ()I
/*     */       //   4: istore #4
/*     */       //   6: aload_0
/*     */       //   7: invokevirtual getOrientation : ()Lnet/minecraft/core/Direction;
/*     */       //   10: astore #5
/*     */       //   12: aload #5
/*     */       //   14: ifnull -> 205
/*     */       //   17: getstatic net/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$1.$SwitchMap$net$minecraft$core$Direction : [I
/*     */       //   20: aload #5
/*     */       //   22: invokevirtual ordinal : ()I
/*     */       //   25: iaload
/*     */       //   26: tableswitch default -> 56, 1 -> 56, 2 -> 94, 3 -> 132, 4 -> 170
/*     */       //   56: aload_1
/*     */       //   57: aload_2
/*     */       //   58: aload_3
/*     */       //   59: aload_0
/*     */       //   60: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   63: invokevirtual minX : ()I
/*     */       //   66: aload_0
/*     */       //   67: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   70: invokevirtual minY : ()I
/*     */       //   73: aload_0
/*     */       //   74: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   77: invokevirtual minZ : ()I
/*     */       //   80: iconst_1
/*     */       //   81: isub
/*     */       //   82: getstatic net/minecraft/core/Direction.NORTH : Lnet/minecraft/core/Direction;
/*     */       //   85: iload #4
/*     */       //   87: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   90: pop
/*     */       //   91: goto -> 205
/*     */       //   94: aload_1
/*     */       //   95: aload_2
/*     */       //   96: aload_3
/*     */       //   97: aload_0
/*     */       //   98: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   101: invokevirtual minX : ()I
/*     */       //   104: aload_0
/*     */       //   105: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   108: invokevirtual minY : ()I
/*     */       //   111: aload_0
/*     */       //   112: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   115: invokevirtual maxZ : ()I
/*     */       //   118: iconst_1
/*     */       //   119: iadd
/*     */       //   120: getstatic net/minecraft/core/Direction.SOUTH : Lnet/minecraft/core/Direction;
/*     */       //   123: iload #4
/*     */       //   125: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   128: pop
/*     */       //   129: goto -> 205
/*     */       //   132: aload_1
/*     */       //   133: aload_2
/*     */       //   134: aload_3
/*     */       //   135: aload_0
/*     */       //   136: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   139: invokevirtual minX : ()I
/*     */       //   142: iconst_1
/*     */       //   143: isub
/*     */       //   144: aload_0
/*     */       //   145: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   148: invokevirtual minY : ()I
/*     */       //   151: aload_0
/*     */       //   152: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   155: invokevirtual minZ : ()I
/*     */       //   158: getstatic net/minecraft/core/Direction.WEST : Lnet/minecraft/core/Direction;
/*     */       //   161: iload #4
/*     */       //   163: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   166: pop
/*     */       //   167: goto -> 205
/*     */       //   170: aload_1
/*     */       //   171: aload_2
/*     */       //   172: aload_3
/*     */       //   173: aload_0
/*     */       //   174: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   177: invokevirtual maxX : ()I
/*     */       //   180: iconst_1
/*     */       //   181: iadd
/*     */       //   182: aload_0
/*     */       //   183: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   186: invokevirtual minY : ()I
/*     */       //   189: aload_0
/*     */       //   190: getfield boundingBox : Lnet/minecraft/world/level/levelgen/structure/BoundingBox;
/*     */       //   193: invokevirtual minZ : ()I
/*     */       //   196: getstatic net/minecraft/core/Direction.EAST : Lnet/minecraft/core/Direction;
/*     */       //   199: iload #4
/*     */       //   201: invokestatic generateAndAddPiece : (Lnet/minecraft/world/level/levelgen/structure/StructurePiece;Lnet/minecraft/world/level/levelgen/structure/StructurePieceAccessor;Lnet/minecraft/util/RandomSource;IIILnet/minecraft/core/Direction;I)Lnet/minecraft/world/level/levelgen/structure/structures/MineshaftPieces$MineShaftPiece;
/*     */       //   204: pop
/*     */       //   205: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #875	-> 0
/*     */       //   #878	-> 6
/*     */       //   #879	-> 12
/*     */       //   #880	-> 17
/*     */       //   #883	-> 56
/*     */       //   #884	-> 91
/*     */       //   #886	-> 94
/*     */       //   #887	-> 129
/*     */       //   #889	-> 132
/*     */       //   #890	-> 167
/*     */       //   #892	-> 170
/*     */       //   #896	-> 205
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void postProcess(WorldGenLevel param1WorldGenLevel, StructureManager param1StructureManager, ChunkGenerator param1ChunkGenerator, RandomSource param1RandomSource, BoundingBox param1BoundingBox, ChunkPos param1ChunkPos, BlockPos param1BlockPos) {
/* 900 */       if (isInInvalidLocation((LevelAccessor)param1WorldGenLevel, param1BoundingBox)) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 905 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 5, 0, 2, 7, 1, CAVE_AIR, CAVE_AIR, false);
/*     */       
/* 907 */       generateBox(param1WorldGenLevel, param1BoundingBox, 0, 0, 7, 2, 2, 8, CAVE_AIR, CAVE_AIR, false);
/*     */       
/* 909 */       for (byte b = 0; b < 5; b++)
/* 910 */         generateBox(param1WorldGenLevel, param1BoundingBox, 0, 5 - b - ((b < 4) ? 1 : 0), 2 + b, 2, 7 - b, 2 + b, CAVE_AIR, CAVE_AIR, false); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\MineshaftPieces.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */