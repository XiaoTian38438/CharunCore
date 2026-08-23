/*     */ package net.minecraft.world.level.levelgen.structure.structures;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.util.RandomSource;
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
/*     */ abstract class NetherBridgePiece
/*     */   extends StructurePiece
/*     */ {
/*     */   protected NetherBridgePiece(StructurePieceType paramStructurePieceType, int paramInt, BoundingBox paramBoundingBox) {
/* 119 */     super(paramStructurePieceType, paramInt, paramBoundingBox);
/*     */   }
/*     */   
/*     */   public NetherBridgePiece(StructurePieceType paramStructurePieceType, CompoundTag paramCompoundTag) {
/* 123 */     super(paramStructurePieceType, paramCompoundTag);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {}
/*     */ 
/*     */   
/*     */   private int updatePieceWeight(List<NetherFortressPieces.PieceWeight> paramList) {
/* 131 */     boolean bool = false;
/* 132 */     int i = 0;
/* 133 */     for (NetherFortressPieces.PieceWeight pieceWeight : paramList) {
/* 134 */       if (pieceWeight.maxPlaceCount > 0 && pieceWeight.placeCount < pieceWeight.maxPlaceCount) {
/* 135 */         bool = true;
/*     */       }
/* 137 */       i += pieceWeight.weight;
/*     */     } 
/* 139 */     return bool ? i : -1;
/*     */   }
/*     */   
/*     */   private NetherBridgePiece generatePiece(NetherFortressPieces.StartPiece paramStartPiece, List<NetherFortressPieces.PieceWeight> paramList, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection, int paramInt4) {
/* 143 */     int i = updatePieceWeight(paramList);
/* 144 */     boolean bool = (i > 0 && paramInt4 <= 30) ? true : false;
/*     */     
/* 146 */     byte b = 0;
/* 147 */     while (b < 5 && bool) {
/* 148 */       b++;
/*     */       
/* 150 */       int j = paramRandomSource.nextInt(i);
/* 151 */       for (NetherFortressPieces.PieceWeight pieceWeight : paramList) {
/* 152 */         j -= pieceWeight.weight;
/* 153 */         if (j < 0) {
/* 154 */           if (!pieceWeight.doPlace(paramInt4) || (pieceWeight == paramStartPiece.previousPiece && !pieceWeight.allowInRow)) {
/*     */             break;
/*     */           }
/*     */           
/* 158 */           NetherBridgePiece netherBridgePiece = NetherFortressPieces.findAndCreateBridgePieceFactory(pieceWeight, paramStructurePieceAccessor, paramRandomSource, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4);
/* 159 */           if (netherBridgePiece != null) {
/* 160 */             pieceWeight.placeCount++;
/* 161 */             paramStartPiece.previousPiece = pieceWeight;
/*     */             
/* 163 */             if (!pieceWeight.isValid()) {
/* 164 */               paramList.remove(pieceWeight);
/*     */             }
/* 166 */             return netherBridgePiece;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 171 */     return NetherFortressPieces.BridgeEndFiller.createPiece(paramStructurePieceAccessor, paramRandomSource, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4);
/*     */   }
/*     */   
/*     */   private StructurePiece generateAndAddPiece(NetherFortressPieces.StartPiece paramStartPiece, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, int paramInt3, Direction paramDirection, int paramInt4, boolean paramBoolean) {
/* 175 */     if (Math.abs(paramInt1 - paramStartPiece.getBoundingBox().minX()) > 112 || Math.abs(paramInt3 - paramStartPiece.getBoundingBox().minZ()) > 112) {
/* 176 */       return NetherFortressPieces.BridgeEndFiller.createPiece(paramStructurePieceAccessor, paramRandomSource, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4);
/*     */     }
/* 178 */     List<NetherFortressPieces.PieceWeight> list = paramStartPiece.availableBridgePieces;
/* 179 */     if (paramBoolean) {
/* 180 */       list = paramStartPiece.availableCastlePieces;
/*     */     }
/* 182 */     NetherBridgePiece netherBridgePiece = generatePiece(paramStartPiece, list, paramStructurePieceAccessor, paramRandomSource, paramInt1, paramInt2, paramInt3, paramDirection, paramInt4 + 1);
/* 183 */     if (netherBridgePiece != null) {
/* 184 */       paramStructurePieceAccessor.addPiece(netherBridgePiece);
/* 185 */       paramStartPiece.pendingChildren.add(netherBridgePiece);
/*     */     } 
/* 187 */     return netherBridgePiece;
/*     */   }
/*     */   
/*     */   protected StructurePiece generateChildForward(NetherFortressPieces.StartPiece paramStartPiece, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 191 */     Direction direction = getOrientation();
/* 192 */     if (direction != null) {
/* 193 */       switch (NetherFortressPieces.null.$SwitchMap$net$minecraft$core$Direction[direction.ordinal()]) {
/*     */         case 1:
/* 195 */           return generateAndAddPiece(paramStartPiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + paramInt1, this.boundingBox.minY() + paramInt2, this.boundingBox.minZ() - 1, direction, getGenDepth(), paramBoolean);
/*     */         case 2:
/* 197 */           return generateAndAddPiece(paramStartPiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + paramInt1, this.boundingBox.minY() + paramInt2, this.boundingBox.maxZ() + 1, direction, getGenDepth(), paramBoolean);
/*     */         case 3:
/* 199 */           return generateAndAddPiece(paramStartPiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() + paramInt2, this.boundingBox.minZ() + paramInt1, direction, getGenDepth(), paramBoolean);
/*     */         case 4:
/* 201 */           return generateAndAddPiece(paramStartPiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() + paramInt2, this.boundingBox.minZ() + paramInt1, direction, getGenDepth(), paramBoolean);
/*     */       } 
/*     */     }
/* 204 */     return null;
/*     */   }
/*     */   
/*     */   protected StructurePiece generateChildLeft(NetherFortressPieces.StartPiece paramStartPiece, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 208 */     Direction direction = getOrientation();
/* 209 */     if (direction != null) {
/* 210 */       switch (NetherFortressPieces.null.$SwitchMap$net$minecraft$core$Direction[direction.ordinal()]) {
/*     */         case 1:
/* 212 */           return generateAndAddPiece(paramStartPiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() + paramInt1, this.boundingBox.minZ() + paramInt2, Direction.WEST, getGenDepth(), paramBoolean);
/*     */         case 2:
/* 214 */           return generateAndAddPiece(paramStartPiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() - 1, this.boundingBox.minY() + paramInt1, this.boundingBox.minZ() + paramInt2, Direction.WEST, getGenDepth(), paramBoolean);
/*     */         case 3:
/* 216 */           return generateAndAddPiece(paramStartPiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + paramInt2, this.boundingBox.minY() + paramInt1, this.boundingBox.minZ() - 1, Direction.NORTH, getGenDepth(), paramBoolean);
/*     */         case 4:
/* 218 */           return generateAndAddPiece(paramStartPiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + paramInt2, this.boundingBox.minY() + paramInt1, this.boundingBox.minZ() - 1, Direction.NORTH, getGenDepth(), paramBoolean);
/*     */       } 
/*     */     }
/* 221 */     return null;
/*     */   }
/*     */   
/*     */   protected StructurePiece generateChildRight(NetherFortressPieces.StartPiece paramStartPiece, StructurePieceAccessor paramStructurePieceAccessor, RandomSource paramRandomSource, int paramInt1, int paramInt2, boolean paramBoolean) {
/* 225 */     Direction direction = getOrientation();
/* 226 */     if (direction != null) {
/* 227 */       switch (NetherFortressPieces.null.$SwitchMap$net$minecraft$core$Direction[direction.ordinal()]) {
/*     */         case 1:
/* 229 */           return generateAndAddPiece(paramStartPiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() + paramInt1, this.boundingBox.minZ() + paramInt2, Direction.EAST, getGenDepth(), paramBoolean);
/*     */         case 2:
/* 231 */           return generateAndAddPiece(paramStartPiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.maxX() + 1, this.boundingBox.minY() + paramInt1, this.boundingBox.minZ() + paramInt2, Direction.EAST, getGenDepth(), paramBoolean);
/*     */         case 3:
/* 233 */           return generateAndAddPiece(paramStartPiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + paramInt2, this.boundingBox.minY() + paramInt1, this.boundingBox.maxZ() + 1, Direction.SOUTH, getGenDepth(), paramBoolean);
/*     */         case 4:
/* 235 */           return generateAndAddPiece(paramStartPiece, paramStructurePieceAccessor, paramRandomSource, this.boundingBox.minX() + paramInt2, this.boundingBox.minY() + paramInt1, this.boundingBox.maxZ() + 1, Direction.SOUTH, getGenDepth(), paramBoolean);
/*     */       } 
/*     */     }
/* 238 */     return null;
/*     */   }
/*     */   
/*     */   protected static boolean isOkBox(BoundingBox paramBoundingBox) {
/* 242 */     return (paramBoundingBox.minY() > 10);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\structures\NetherFortressPieces$NetherBridgePiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */