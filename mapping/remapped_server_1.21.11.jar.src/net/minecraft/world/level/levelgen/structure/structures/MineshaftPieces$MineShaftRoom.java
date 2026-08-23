/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.StructureManager;
/*     */ import net.minecraft.world.level.WorldGenLevel;
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
/*     */ public class MineShaftRoom
/*     */   extends MineshaftPieces.MineShaftPiece
/*     */ {
/* 193 */   private final List<BoundingBox> childEntranceBoxes = Lists.newLinkedList();
/*     */   
/*     */   public MineShaftRoom(int paramInt1, RandomSource paramRandomSource, int paramInt2, int paramInt3, MineshaftStructure.Type paramType) {
/* 196 */     super(StructurePieceType.MINE_SHAFT_ROOM, paramInt1, paramType, new BoundingBox(paramInt2, 50, paramInt3, paramInt2 + 7 + paramRandomSource.nextInt(6), 54 + paramRandomSource.nextInt(6), paramInt3 + 7 + paramRandomSource.nextInt(6)));
/* 197 */     this.type = paramType;
/*     */   }
/*     */   
/*     */   public MineShaftRoom(CompoundTag paramCompoundTag) {
/* 201 */     super(StructurePieceType.MINE_SHAFT_ROOM, paramCompoundTag);
/* 202 */     this.childEntranceBoxes.addAll(paramCompoundTag.read("Entrances", BoundingBox.CODEC.listOf()).orElse(List.of()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void addChildren(StructurePiece paramStructurePiece, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource) {
/* 207 */     int i = getGenDepth();
/*     */ 
/*     */ 
/*     */     
/* 211 */     int k = this.boundingBox.getYSpan() - 3 - 1;
/* 212 */     if (k <= 0) {
/* 213 */       k = 1;
/*     */     }
/*     */ 
/*     */     
/* 217 */     int j = 0;
/* 218 */     while (j < this.boundingBox.getXSpan()) {
/* 219 */       j += paramRandomSource.nextInt(this.boundingBox.getXSpan());
/* 220 */       if (j + 3 > this.boundingBox.getXSpan()) {
/*     */         break;
/*     */       }
/* 223 */       MineshaftPieces.MineShaftPiece mineShaftPiece = MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + j, this.boundingBox.minY() + paramRandomSource.nextInt(k) + 1, this.boundingBox.minZ() - 1, Direction.NORTH, i);
/* 224 */       if (mineShaftPiece != null) {
/* 225 */         BoundingBox boundingBox = mineShaftPiece.getBoundingBox();
/* 226 */         this.childEntranceBoxes.add(new BoundingBox(boundingBox.minX(), boundingBox.minY(), this.boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), this.boundingBox.minZ() + 1));
/*     */       } 
/* 228 */       j += 4;
/*     */     } 
/*     */     
/* 231 */     j = 0;
/* 232 */     while (j < this.boundingBox.getXSpan()) {
/* 233 */       j += paramRandomSource.nextInt(this.boundingBox.getXSpan());
/* 234 */       if (j + 3 > this.boundingBox.getXSpan()) {
/*     */         break;
/*     */       }
/* 237 */       MineshaftPieces.MineShaftPiece mineShaftPiece = MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + j, this.boundingBox.minY() + paramRandomSource.nextInt(k) + 1, this.boundingBox.maxZ() + 1, Direction.SOUTH, i);
/* 238 */       if (mineShaftPiece != null) {
/* 239 */         BoundingBox boundingBox = mineShaftPiece.getBoundingBox();
/* 240 */         this.childEntranceBoxes.add(new BoundingBox(boundingBox.minX(), boundingBox.minY(), this.boundingBox.maxZ() - 1, boundingBox.maxX(), boundingBox.maxY(), this.boundingBox.maxZ()));
/*     */       } 
/* 242 */       j += 4;
/*     */     } 
/*     */     
/* 245 */     j = 0;
/* 246 */     while (j < this.boundingBox.getZSpan()) {
/* 247 */       j += paramRandomSource.nextInt(this.boundingBox.getZSpan());
/* 248 */       if (j + 3 > this.boundingBox.getZSpan()) {
/*     */         break;
/*     */       }
/* 251 */       MineshaftPieces.MineShaftPiece mineShaftPiece = MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() + paramRandomSource.nextInt(k) + 1, this.boundingBox.minZ() + j, Direction.WEST, i);
/* 252 */       if (mineShaftPiece != null) {
/* 253 */         BoundingBox boundingBox = mineShaftPiece.getBoundingBox();
/* 254 */         this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), this.boundingBox.minX() + 1, boundingBox.maxY(), boundingBox.maxZ()));
/*     */       } 
/* 256 */       j += 4;
/*     */     } 
/*     */     
/* 259 */     j = 0;
/* 260 */     while (j < this.boundingBox.getZSpan()) {
/* 261 */       j += paramRandomSource.nextInt(this.boundingBox.getZSpan());
/* 262 */       if (j + 3 > this.boundingBox.getZSpan()) {
/*     */         break;
/*     */       }
/* 265 */       MineshaftPieces.MineShaftPiece mineShaftPiece = MineshaftPieces.generateAndAddPiece(paramStructurePiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() + paramRandomSource.nextInt(k) + 1, this.boundingBox.minZ() + j, Direction.EAST, i);
/* 266 */       if (mineShaftPiece != null) {
/* 267 */         BoundingBox boundingBox = mineShaftPiece.getBoundingBox();
/* 268 */         this.childEntranceBoxes.add(new BoundingBox(this.boundingBox.maxX() - 1, boundingBox.minY(), boundingBox.minZ(), this.boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ()));
/*     */       } 
/* 270 */       j += 4;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void postProcess(WorldGenLevel paramWorldGenLevel, StructureManager paramStructureManager, ChunkGenerator paramChunkGenerator, RandomSource paramRandomSource, BoundingBox paramBoundingBox, ChunkPos paramChunkPos, BlockPos paramBlockPos) {
/* 276 */     if (isInInvalidLocation((LevelAccessor)paramWorldGenLevel, paramBoundingBox)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 281 */     generateBox(paramWorldGenLevel, paramBoundingBox, this.boundingBox.minX(), this.boundingBox.minY() + 1, this.boundingBox.minZ(), this.boundingBox.maxX(), Math.min(this.boundingBox.minY() + 3, this.boundingBox.maxY()), this.boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/* 282 */     for (BoundingBox boundingBox : this.childEntranceBoxes) {
/* 283 */       generateBox(paramWorldGenLevel, paramBoundingBox, boundingBox.minX(), boundingBox.maxY() - 2, boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ(), CAVE_AIR, CAVE_AIR, false);
/*     */     }
/* 285 */     generateUpperHalfSphere(paramWorldGenLevel, paramBoundingBox, this.boundingBox.minX(), this.boundingBox.minY() + 4, this.boundingBox.minZ(), this.boundingBox.maxX(), this.boundingBox.maxY(), this.boundingBox.maxZ(), CAVE_AIR, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void move(int paramInt1, int paramInt2, int paramInt3) {
/* 290 */     super.move(paramInt1, paramInt2, paramInt3);
/* 291 */     for (BoundingBox boundingBox : this.childEntranceBoxes) {
/* 292 */       boundingBox.move(paramInt1, paramInt2, paramInt3);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/* 298 */     super.addAdditionalSaveData(paramStructurePieceSerializationContext, paramCompoundTag);
/* 299 */     paramCompoundTag.store("Entrances", BoundingBox.CODEC.listOf(), this.childEntranceBoxes);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\MineshaftPieces$MineShaftRoom.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */