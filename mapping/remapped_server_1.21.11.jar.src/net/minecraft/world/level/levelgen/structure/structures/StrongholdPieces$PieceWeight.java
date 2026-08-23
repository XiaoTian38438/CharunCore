/*    */ package net.minecraft.world.level.levelgen.structure.structures;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PieceWeight
/*    */ {
/*    */   public final Class<? extends StrongholdPieces.StrongholdPiece> pieceClass;
/*    */   public final int weight;
/*    */   public int placeCount;
/*    */   public final int maxPlaceCount;
/*    */   
/*    */   public PieceWeight(Class<? extends StrongholdPieces.StrongholdPiece> paramClass, int paramInt1, int paramInt2) {
/* 62 */     this.pieceClass = paramClass;
/* 63 */     this.weight = paramInt1;
/* 64 */     this.maxPlaceCount = paramInt2;
/*    */   }
/*    */   
/*    */   public boolean doPlace(int paramInt) {
/* 68 */     return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*    */   }
/*    */   
/*    */   public boolean isValid() {
/* 72 */     return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\StrongholdPieces$PieceWeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */