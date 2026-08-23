/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.chunk.BlockColumn;
/*    */ 
/*    */ public final class NoiseColumn implements BlockColumn {
/*    */   private final int minY;
/*    */   private final BlockState[] column;
/*    */   
/*    */   public NoiseColumn(int paramInt, BlockState[] paramArrayOfBlockState) {
/* 12 */     this.minY = paramInt;
/* 13 */     this.column = paramArrayOfBlockState;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getBlock(int paramInt) {
/* 18 */     int i = paramInt - this.minY;
/* 19 */     if (i < 0 || i >= this.column.length) {
/* 20 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 22 */     return this.column[i];
/*    */   }
/*    */ 
/*    */   
/*    */   public void setBlock(int paramInt, BlockState paramBlockState) {
/* 27 */     int i = paramInt - this.minY;
/* 28 */     if (i < 0 || i >= this.column.length) {
/* 29 */       throw new IllegalArgumentException("Outside of column height: " + paramInt);
/*    */     }
/* 31 */     this.column[i] = paramBlockState;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\NoiseColumn.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */