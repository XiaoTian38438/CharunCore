/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public class NonOverlappingMerger extends AbstractDoubleList implements IndexMerger {
/*    */   private final DoubleList lower;
/*    */   private final DoubleList upper;
/*    */   private final boolean swap;
/*    */   
/*    */   protected NonOverlappingMerger(DoubleList paramDoubleList1, DoubleList paramDoubleList2, boolean paramBoolean) {
/* 12 */     this.lower = paramDoubleList1;
/* 13 */     this.upper = paramDoubleList2;
/* 14 */     this.swap = paramBoolean;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 19 */     return this.lower.size() + this.upper.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer paramIndexConsumer) {
/* 24 */     if (this.swap) {
/* 25 */       return forNonSwappedIndexes((paramInt1, paramInt2, paramInt3) -> paramIndexConsumer.merge(paramInt2, paramInt1, paramInt3));
/*    */     }
/* 27 */     return forNonSwappedIndexes(paramIndexConsumer);
/*    */   }
/*    */   
/*    */   private boolean forNonSwappedIndexes(IndexMerger.IndexConsumer paramIndexConsumer) {
/* 31 */     int i = this.lower.size(); int j;
/* 32 */     for (j = 0; j < i; j++) {
/* 33 */       if (!paramIndexConsumer.merge(j, -1, j)) {
/* 34 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 38 */     j = this.upper.size() - 1;
/* 39 */     for (byte b = 0; b < j; b++) {
/* 40 */       if (!paramIndexConsumer.merge(i - 1, b, i + b)) {
/* 41 */         return false;
/*    */       }
/*    */     } 
/* 44 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public double getDouble(int paramInt) {
/* 49 */     if (paramInt < this.lower.size()) {
/* 50 */       return this.lower.getDouble(paramInt);
/*    */     }
/* 52 */     return this.upper.getDouble(paramInt - this.lower.size());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public DoubleList getList() {
/* 58 */     return (DoubleList)this;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\NonOverlappingMerger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */