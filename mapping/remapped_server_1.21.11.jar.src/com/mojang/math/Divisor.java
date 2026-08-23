/*    */ package com.mojang.math;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import it.unimi.dsi.fastutil.ints.IntIterator;
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
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
/*    */ public class Divisor
/*    */   implements IntIterator
/*    */ {
/*    */   private final int denominator;
/*    */   private final int quotient;
/*    */   private final int mod;
/*    */   private int returnedParts;
/*    */   private int remainder;
/*    */   
/*    */   public Divisor(int paramInt1, int paramInt2) {
/* 35 */     this.denominator = paramInt2;
/* 36 */     if (paramInt2 > 0) {
/* 37 */       this.quotient = paramInt1 / paramInt2;
/* 38 */       this.mod = paramInt1 % paramInt2;
/*    */     } else {
/* 40 */       this.quotient = 0;
/* 41 */       this.mod = 0;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 47 */     return (this.returnedParts < this.denominator);
/*    */   }
/*    */ 
/*    */   
/*    */   public int nextInt() {
/* 52 */     if (!hasNext()) {
/* 53 */       throw new NoSuchElementException();
/*    */     }
/* 55 */     int i = this.quotient;
/* 56 */     this.remainder += this.mod;
/* 57 */     if (this.remainder >= this.denominator) {
/* 58 */       this.remainder -= this.denominator;
/* 59 */       i++;
/*    */     } 
/* 61 */     this.returnedParts++;
/* 62 */     return i;
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public static Iterable<Integer> asIterable(int paramInt1, int paramInt2) {
/* 67 */     return () -> new Divisor(paramInt1, paramInt2);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\math\Divisor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */