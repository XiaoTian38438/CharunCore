/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import it.unimi.dsi.fastutil.ints.IntCollection;
/*    */ import it.unimi.dsi.fastutil.ints.IntSet;
/*    */ import java.util.BitSet;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class RegionBitmap {
/* 10 */   private final BitSet used = new BitSet();
/*    */   
/*    */   public void force(int paramInt1, int paramInt2) {
/* 13 */     this.used.set(paramInt1, paramInt1 + paramInt2);
/*    */   }
/*    */   
/*    */   public void free(int paramInt1, int paramInt2) {
/* 17 */     this.used.clear(paramInt1, paramInt1 + paramInt2);
/*    */   }
/*    */   
/*    */   public int allocate(int paramInt) {
/* 21 */     int i = 0;
/*    */     while (true) {
/* 23 */       int j = this.used.nextClearBit(i);
/* 24 */       int k = this.used.nextSetBit(j);
/* 25 */       if (k == -1 || k - j >= paramInt) {
/* 26 */         force(j, paramInt);
/* 27 */         return j;
/*    */       } 
/* 29 */       i = k;
/*    */     } 
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public IntSet getUsed() {
/* 35 */     return this.used.stream().<IntSet>collect(it.unimi.dsi.fastutil.ints.IntArraySet::new, IntCollection::add, IntCollection::addAll);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\chunk\storage\RegionBitmap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */