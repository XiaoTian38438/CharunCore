/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleLists;
/*    */ 
/*    */ public class IndirectMerger implements IndexMerger {
/*  8 */   private static final DoubleList EMPTY = DoubleLists.unmodifiable((DoubleList)DoubleArrayList.wrap(new double[] { 0.0D }));
/*    */   
/*    */   private final double[] result;
/*    */   
/*    */   private final int[] firstIndices;
/*    */   
/*    */   private final int[] secondIndices;
/*    */   
/*    */   private final int resultLength;
/*    */ 
/*    */   
/*    */   public IndirectMerger(DoubleList paramDoubleList1, DoubleList paramDoubleList2, boolean paramBoolean1, boolean paramBoolean2) {
/* 20 */     double d = Double.NaN;
/*    */     
/* 22 */     int i = paramDoubleList1.size();
/* 23 */     int j = paramDoubleList2.size();
/* 24 */     int k = i + j;
/* 25 */     this.result = new double[k];
/* 26 */     this.firstIndices = new int[k];
/* 27 */     this.secondIndices = new int[k];
/*    */     
/* 29 */     boolean bool1 = !paramBoolean1 ? true : false;
/* 30 */     boolean bool2 = !paramBoolean2 ? true : false;
/*    */     
/* 32 */     byte b1 = 0;
/* 33 */     byte b2 = 0;
/* 34 */     byte b3 = 0;
/*    */     
/*    */     while (true) {
/* 37 */       boolean bool3 = (b2 >= i) ? true : false;
/* 38 */       boolean bool4 = (b3 >= j) ? true : false;
/*    */       
/* 40 */       if (bool3 && bool4) {
/*    */         break;
/*    */       }
/* 43 */       boolean bool5 = (!bool3 && (bool4 || paramDoubleList1.getDouble(b2) < paramDoubleList2.getDouble(b3) + 1.0E-7D)) ? true : false;
/*    */       
/* 45 */       if (bool5) {
/* 46 */         b2++;
/* 47 */         if (bool1 && (b3 == 0 || bool4)) {
/*    */           continue;
/*    */         }
/*    */       } else {
/* 51 */         b3++;
/* 52 */         if (bool2 && (b2 == 0 || bool3)) {
/*    */           continue;
/*    */         }
/*    */       } 
/*    */       
/* 57 */       int m = b2 - 1;
/* 58 */       int n = b3 - 1;
/*    */       
/* 60 */       double d1 = bool5 ? paramDoubleList1.getDouble(m) : paramDoubleList2.getDouble(n);
/* 61 */       if (d < d1 - 1.0E-7D) {
/* 62 */         this.firstIndices[b1] = m;
/* 63 */         this.secondIndices[b1] = n;
/* 64 */         this.result[b1] = d1;
/* 65 */         b1++;
/* 66 */         d = d1; continue;
/*    */       } 
/* 68 */       this.firstIndices[b1 - 1] = m;
/* 69 */       this.secondIndices[b1 - 1] = n;
/*    */     } 
/*    */ 
/*    */     
/* 73 */     this.resultLength = Math.max(1, b1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer paramIndexConsumer) {
/* 78 */     int i = this.resultLength - 1;
/* 79 */     for (byte b = 0; b < i; b++) {
/* 80 */       if (!paramIndexConsumer.merge(this.firstIndices[b], this.secondIndices[b], b)) {
/* 81 */         return false;
/*    */       }
/*    */     } 
/* 84 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 89 */     return this.resultLength;
/*    */   }
/*    */ 
/*    */   
/*    */   public DoubleList getList() {
/* 94 */     return (this.resultLength <= 1) ? EMPTY : (DoubleList)DoubleArrayList.wrap(this.result, this.resultLength);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\phys\shapes\IndirectMerger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */