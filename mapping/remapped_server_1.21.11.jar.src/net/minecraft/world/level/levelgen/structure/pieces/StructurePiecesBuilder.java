/*    */ package net.minecraft.world.level.levelgen.structure.pieces;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePiece;
/*    */ import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
/*    */ 
/*    */ public class StructurePiecesBuilder
/*    */   implements StructurePieceAccessor
/*    */ {
/* 13 */   private final List<StructurePiece> pieces = Lists.newArrayList();
/*    */ 
/*    */   
/*    */   public void addPiece(StructurePiece paramStructurePiece) {
/* 17 */     this.pieces.add(paramStructurePiece);
/*    */   }
/*    */ 
/*    */   
/*    */   public StructurePiece findCollisionPiece(BoundingBox paramBoundingBox) {
/* 22 */     return StructurePiece.findCollisionPiece(this.pieces, paramBoundingBox);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public void offsetPiecesVertically(int paramInt) {
/* 30 */     for (StructurePiece structurePiece : this.pieces) {
/* 31 */       structurePiece.move(0, paramInt, 0);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public int moveBelowSeaLevel(int paramInt1, int paramInt2, RandomSource paramRandomSource, int paramInt3) {
/* 40 */     int i = paramInt1 - paramInt3;
/*    */ 
/*    */     
/* 43 */     BoundingBox boundingBox = getBoundingBox();
/* 44 */     int j = boundingBox.getYSpan() + paramInt2 + 1;
/*    */     
/* 46 */     if (j < i) {
/* 47 */       j += paramRandomSource.nextInt(i - j);
/*    */     }
/*    */ 
/*    */     
/* 51 */     int k = j - boundingBox.maxY();
/* 52 */     offsetPiecesVertically(k);
/* 53 */     return k;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void moveInsideHeights(RandomSource paramRandomSource, int paramInt1, int paramInt2) {
/*    */     int j;
/* 60 */     BoundingBox boundingBox = getBoundingBox();
/* 61 */     int i = paramInt2 - paramInt1 + 1 - boundingBox.getYSpan();
/*    */ 
/*    */     
/* 64 */     if (i > 1) {
/* 65 */       j = paramInt1 + paramRandomSource.nextInt(i);
/*    */     } else {
/* 67 */       j = paramInt1;
/*    */     } 
/*    */ 
/*    */     
/* 71 */     int k = j - boundingBox.minY();
/* 72 */     offsetPiecesVertically(k);
/*    */   }
/*    */   
/*    */   public PiecesContainer build() {
/* 76 */     return new PiecesContainer(this.pieces);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 81 */     this.pieces.clear();
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 85 */     return this.pieces.isEmpty();
/*    */   }
/*    */   
/*    */   public BoundingBox getBoundingBox() {
/* 89 */     return StructurePiece.createBoundingBox(this.pieces.stream());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\pieces\StructurePiecesBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */