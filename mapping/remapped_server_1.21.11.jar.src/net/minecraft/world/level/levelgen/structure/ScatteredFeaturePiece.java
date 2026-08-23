/*    */ package net.minecraft.world.level.levelgen.structure;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.world.level.LevelAccessor;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
/*    */ import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
/*    */ 
/*    */ public abstract class ScatteredFeaturePiece extends StructurePiece {
/*    */   protected final int width;
/*    */   protected final int height;
/*    */   protected final int depth;
/* 16 */   protected int heightPosition = -1;
/*    */   
/*    */   protected ScatteredFeaturePiece(StructurePieceType paramStructurePieceType, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Direction paramDirection) {
/* 19 */     super(paramStructurePieceType, 0, StructurePiece.makeBoundingBox(paramInt1, paramInt2, paramInt3, paramDirection, paramInt4, paramInt5, paramInt6));
/*    */     
/* 21 */     this.width = paramInt4;
/* 22 */     this.height = paramInt5;
/* 23 */     this.depth = paramInt6;
/*    */     
/* 25 */     setOrientation(paramDirection);
/*    */   }
/*    */   
/*    */   protected ScatteredFeaturePiece(StructurePieceType paramStructurePieceType, CompoundTag paramCompoundTag) {
/* 29 */     super(paramStructurePieceType, paramCompoundTag);
/* 30 */     this.width = paramCompoundTag.getIntOr("Width", 0);
/* 31 */     this.height = paramCompoundTag.getIntOr("Height", 0);
/* 32 */     this.depth = paramCompoundTag.getIntOr("Depth", 0);
/* 33 */     this.heightPosition = paramCompoundTag.getIntOr("HPos", 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void addAdditionalSaveData(StructurePieceSerializationContext paramStructurePieceSerializationContext, CompoundTag paramCompoundTag) {
/* 38 */     paramCompoundTag.putInt("Width", this.width);
/* 39 */     paramCompoundTag.putInt("Height", this.height);
/* 40 */     paramCompoundTag.putInt("Depth", this.depth);
/* 41 */     paramCompoundTag.putInt("HPos", this.heightPosition);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean updateAverageGroundHeight(LevelAccessor paramLevelAccessor, BoundingBox paramBoundingBox, int paramInt) {
/* 46 */     if (this.heightPosition >= 0) {
/* 47 */       return true;
/*    */     }
/*    */     
/* 50 */     int i = 0;
/* 51 */     byte b = 0;
/* 52 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 53 */     for (int j = this.boundingBox.minZ(); j <= this.boundingBox.maxZ(); j++) {
/* 54 */       for (int k = this.boundingBox.minX(); k <= this.boundingBox.maxX(); k++) {
/* 55 */         mutableBlockPos.set(k, 64, j);
/* 56 */         if (paramBoundingBox.isInside((Vec3i)mutableBlockPos)) {
/* 57 */           i += paramLevelAccessor.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (BlockPos)mutableBlockPos).getY();
/* 58 */           b++;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 63 */     if (b == 0) {
/* 64 */       return false;
/*    */     }
/* 66 */     this.heightPosition = i / b;
/* 67 */     this.boundingBox.move(0, this.heightPosition - this.boundingBox.minY() + paramInt, 0);
/* 68 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean updateHeightPositionToLowestGroundHeight(LevelAccessor paramLevelAccessor, int paramInt) {
/* 73 */     if (this.heightPosition >= 0) {
/* 74 */       return true;
/*    */     }
/*    */     
/* 77 */     int i = paramLevelAccessor.getMaxY() + 1;
/* 78 */     boolean bool = false;
/* 79 */     BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
/* 80 */     for (int j = this.boundingBox.minZ(); j <= this.boundingBox.maxZ(); j++) {
/* 81 */       for (int k = this.boundingBox.minX(); k <= this.boundingBox.maxX(); k++) {
/*    */         
/* 83 */         mutableBlockPos.set(k, 0, j);
/* 84 */         i = Math.min(i, paramLevelAccessor.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (BlockPos)mutableBlockPos).getY());
/* 85 */         bool = true;
/*    */       } 
/*    */     } 
/*    */     
/* 89 */     if (!bool) {
/* 90 */       return false;
/*    */     }
/* 92 */     this.heightPosition = i;
/* 93 */     this.boundingBox.move(0, this.heightPosition - this.boundingBox.minY() + paramInt, 0);
/* 94 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\levelgen\structure\ScatteredFeaturePiece.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */