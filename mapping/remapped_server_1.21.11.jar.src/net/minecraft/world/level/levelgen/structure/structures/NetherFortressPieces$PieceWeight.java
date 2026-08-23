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
/*    */ class PieceWeight
/*    */ {
/*    */   public final Class<? extends NetherFortressPieces.NetherBridgePiece> pieceClass;
/*    */   public final int weight;
/*    */   public int placeCount;
/*    */   public final int maxPlaceCount;
/*    */   public final boolean allowInRow;
/*    */   
/*    */   public PieceWeight(Class<? extends NetherFortressPieces.NetherBridgePiece> paramClass, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 46 */     this.pieceClass = paramClass;
/* 47 */     this.weight = paramInt1;
/* 48 */     this.maxPlaceCount = paramInt2;
/* 49 */     this.allowInRow = paramBoolean;
/*    */   }
/*    */   
/*    */   public PieceWeight(Class<? extends NetherFortressPieces.NetherBridgePiece> paramClass, int paramInt1, int paramInt2) {
/* 53 */     this(paramClass, paramInt1, paramInt2, false);
/*    */   }
/*    */   
/*    */   public boolean doPlace(int paramInt) {
/* 57 */     return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*    */   }
/*    */   
/*    */   public boolean isValid() {
/* 61 */     return (this.maxPlaceCount == 0 || this.placeCount < this.maxPlaceCount);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$PieceWeight.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */