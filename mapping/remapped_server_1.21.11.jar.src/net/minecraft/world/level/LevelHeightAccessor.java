/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.SectionPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface LevelHeightAccessor
/*    */ {
/*    */   int getHeight();
/*    */   
/*    */   int getMinY();
/*    */   
/*    */   default int getMaxY() {
/* 16 */     return getMinY() + getHeight() - 1;
/*    */   }
/*    */ 
/*    */   
/*    */   default int getSectionsCount() {
/* 21 */     return getMaxSectionY() - getMinSectionY() + 1;
/*    */   }
/*    */ 
/*    */   
/*    */   default int getMinSectionY() {
/* 26 */     return SectionPos.blockToSectionCoord(getMinY());
/*    */   }
/*    */ 
/*    */   
/*    */   default int getMaxSectionY() {
/* 31 */     return SectionPos.blockToSectionCoord(getMaxY());
/*    */   }
/*    */   
/*    */   default boolean isInsideBuildHeight(int paramInt) {
/* 35 */     return (paramInt >= getMinY() && paramInt <= getMaxY());
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean isOutsideBuildHeight(BlockPos paramBlockPos) {
/* 40 */     return isOutsideBuildHeight(paramBlockPos.getY());
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean isOutsideBuildHeight(int paramInt) {
/* 45 */     return (paramInt < getMinY() || paramInt > getMaxY());
/*    */   }
/*    */ 
/*    */   
/*    */   default int getSectionIndex(int paramInt) {
/* 50 */     return getSectionIndexFromSectionY(SectionPos.blockToSectionCoord(paramInt));
/*    */   }
/*    */ 
/*    */   
/*    */   default int getSectionIndexFromSectionY(int paramInt) {
/* 55 */     return paramInt - getMinSectionY();
/*    */   }
/*    */ 
/*    */   
/*    */   default int getSectionYFromSectionIndex(int paramInt) {
/* 60 */     return paramInt + getMinSectionY();
/*    */   }
/*    */   
/*    */   static LevelHeightAccessor create(final int minY, final int height) {
/* 64 */     return new LevelHeightAccessor()
/*    */       {
/*    */         public int getHeight() {
/* 67 */           return height;
/*    */         }
/*    */ 
/*    */         
/*    */         public int getMinY() {
/* 72 */           return minY;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\LevelHeightAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */