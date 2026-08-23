/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import java.util.BitSet;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CarvingMask
/*    */ {
/*    */   private final int minY;
/*    */   private final BitSet mask;
/*    */   private Mask additionalMask = (paramInt1, paramInt2, paramInt3) -> false;
/*    */   
/*    */   public CarvingMask(int paramInt1, int paramInt2) {
/* 20 */     this.minY = paramInt2;
/* 21 */     this.mask = new BitSet(256 * paramInt1);
/*    */   }
/*    */   
/*    */   public void setAdditionalMask(Mask paramMask) {
/* 25 */     this.additionalMask = paramMask;
/*    */   }
/*    */   
/*    */   public CarvingMask(long[] paramArrayOflong, int paramInt) {
/* 29 */     this.minY = paramInt;
/* 30 */     this.mask = BitSet.valueOf(paramArrayOflong);
/*    */   }
/*    */   
/*    */   private int getIndex(int paramInt1, int paramInt2, int paramInt3) {
/* 34 */     return paramInt1 & 0xF | (paramInt3 & 0xF) << 4 | paramInt2 - this.minY << 8;
/*    */   }
/*    */   
/*    */   public void set(int paramInt1, int paramInt2, int paramInt3) {
/* 38 */     this.mask.set(getIndex(paramInt1, paramInt2, paramInt3));
/*    */   }
/*    */   
/*    */   public boolean get(int paramInt1, int paramInt2, int paramInt3) {
/* 42 */     return (this.additionalMask.test(paramInt1, paramInt2, paramInt3) || this.mask.get(getIndex(paramInt1, paramInt2, paramInt3)));
/*    */   }
/*    */   
/*    */   public Stream<BlockPos> stream(ChunkPos paramChunkPos) {
/* 46 */     return this.mask.stream().mapToObj(paramInt -> {
/*    */           int i = paramInt & 0xF;
/*    */           int j = paramInt >> 4 & 0xF;
/*    */           int k = paramInt >> 8;
/*    */           return paramChunkPos.getBlockAt(i, k + this.minY, j);
/*    */         });
/*    */   }
/*    */   
/*    */   public long[] toArray() {
/* 55 */     return this.mask.toLongArray();
/*    */   }
/*    */   
/*    */   public static interface Mask {
/*    */     boolean test(int param1Int1, int param1Int2, int param1Int3);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\CarvingMask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */