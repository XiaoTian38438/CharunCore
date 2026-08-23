/*    */ package net.minecraft.util.datafix;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import org.apache.commons.lang3.Validate;
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
/*    */ public class PackedBitStorage
/*    */ {
/*    */   private static final int BIT_TO_LONG_SHIFT = 6;
/*    */   private final long[] data;
/*    */   private final int bits;
/*    */   private final long mask;
/*    */   private final int size;
/*    */   
/*    */   public PackedBitStorage(int paramInt1, int paramInt2) {
/* 26 */     this(paramInt1, paramInt2, new long[Mth.roundToward(paramInt2 * paramInt1, 64) / 64]);
/*    */   }
/*    */   
/*    */   public PackedBitStorage(int paramInt1, int paramInt2, long[] paramArrayOflong) {
/* 30 */     Validate.inclusiveBetween(1L, 32L, paramInt1);
/*    */     
/* 32 */     this.size = paramInt2;
/* 33 */     this.bits = paramInt1;
/* 34 */     this.data = paramArrayOflong;
/* 35 */     this.mask = (1L << paramInt1) - 1L;
/*    */     
/* 37 */     int i = Mth.roundToward(paramInt2 * paramInt1, 64) / 64;
/* 38 */     if (paramArrayOflong.length != i) {
/* 39 */       throw new IllegalArgumentException("Invalid length given for storage, got: " + paramArrayOflong.length + " but expected: " + i);
/*    */     }
/*    */   }
/*    */   
/*    */   public void set(int paramInt1, int paramInt2) {
/* 44 */     Validate.inclusiveBetween(0L, (this.size - 1), paramInt1);
/* 45 */     Validate.inclusiveBetween(0L, this.mask, paramInt2);
/*    */     
/* 47 */     int i = paramInt1 * this.bits;
/* 48 */     int j = i >> 6;
/* 49 */     int k = (paramInt1 + 1) * this.bits - 1 >> 6;
/* 50 */     int m = i ^ j << 6;
/*    */     
/* 52 */     this.data[j] = this.data[j] & (this.mask << m ^ 0xFFFFFFFFFFFFFFFFL) | (paramInt2 & this.mask) << m;
/* 53 */     if (j != k) {
/* 54 */       int n = 64 - m;
/* 55 */       int i1 = this.bits - n;
/* 56 */       this.data[k] = this.data[k] >>> i1 << i1 | (paramInt2 & this.mask) >> n;
/*    */     } 
/*    */   }
/*    */   
/*    */   public int get(int paramInt) {
/* 61 */     Validate.inclusiveBetween(0L, (this.size - 1), paramInt);
/*    */     
/* 63 */     int i = paramInt * this.bits;
/* 64 */     int j = i >> 6;
/* 65 */     int k = (paramInt + 1) * this.bits - 1 >> 6;
/* 66 */     int m = i ^ j << 6;
/*    */     
/* 68 */     if (j == k) {
/* 69 */       return (int)(this.data[j] >>> m & this.mask);
/*    */     }
/* 71 */     int n = 64 - m;
/* 72 */     return (int)((this.data[j] >>> m | this.data[k] << n) & this.mask);
/*    */   }
/*    */ 
/*    */   
/*    */   public long[] getRaw() {
/* 77 */     return this.data;
/*    */   }
/*    */   
/*    */   public int getBits() {
/* 81 */     return this.bits;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\PackedBitStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */