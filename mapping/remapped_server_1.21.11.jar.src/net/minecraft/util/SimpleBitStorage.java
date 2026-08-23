/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.function.IntConsumer;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ public class SimpleBitStorage
/*     */   implements BitStorage {
/*     */   public static class InitializationException
/*     */     extends RuntimeException {
/*     */     InitializationException(String param1String) {
/*  11 */       super(param1String);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  18 */   private static final int[] MAGIC = new int[] { -1, -1, 0, Integer.MIN_VALUE, 0, 0, 1431655765, 1431655765, 0, Integer.MIN_VALUE, 0, 1, 858993459, 858993459, 0, 715827882, 715827882, 0, 613566756, 613566756, 0, Integer.MIN_VALUE, 0, 2, 477218588, 477218588, 0, 429496729, 429496729, 0, 390451572, 390451572, 0, 357913941, 357913941, 0, 330382099, 330382099, 0, 306783378, 306783378, 0, 286331153, 286331153, 0, Integer.MIN_VALUE, 0, 3, 252645135, 252645135, 0, 238609294, 238609294, 0, 226050910, 226050910, 0, 214748364, 214748364, 0, 204522252, 204522252, 0, 195225786, 195225786, 0, 186737708, 186737708, 0, 178956970, 178956970, 0, 171798691, 171798691, 0, 165191049, 165191049, 0, 159072862, 159072862, 0, 153391689, 153391689, 0, 148102320, 148102320, 0, 143165576, 143165576, 0, 138547332, 138547332, 0, Integer.MIN_VALUE, 0, 4, 130150524, 130150524, 0, 126322567, 126322567, 0, 122713351, 122713351, 0, 119304647, 119304647, 0, 116080197, 116080197, 0, 113025455, 113025455, 0, 110127366, 110127366, 0, 107374182, 107374182, 0, 104755299, 104755299, 0, 102261126, 102261126, 0, 99882960, 99882960, 0, 97612893, 97612893, 0, 95443717, 95443717, 0, 93368854, 93368854, 0, 91382282, 91382282, 0, 89478485, 89478485, 0, 87652393, 87652393, 0, 85899345, 85899345, 0, 84215045, 84215045, 0, 82595524, 82595524, 0, 81037118, 81037118, 0, 79536431, 79536431, 0, 78090314, 78090314, 0, 76695844, 76695844, 0, 75350303, 75350303, 0, 74051160, 74051160, 0, 72796055, 72796055, 0, 71582788, 71582788, 0, 70409299, 70409299, 0, 69273666, 69273666, 0, 68174084, 68174084, 0, Integer.MIN_VALUE, 0, 5 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long[] data;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int bits;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final long mask;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int size;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int valuesPerLong;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int divideMul;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int divideAdd;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int divideShift;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SimpleBitStorage(int paramInt1, int paramInt2, int[] paramArrayOfint) {
/*  97 */     this(paramInt1, paramInt2);
/*     */     
/*  99 */     byte b = 0;
/*     */     int i;
/* 101 */     for (i = 0; i <= paramInt2 - this.valuesPerLong; i += this.valuesPerLong) {
/* 102 */       long l = 0L;
/* 103 */       for (int k = this.valuesPerLong - 1; k >= 0; k--) {
/* 104 */         l <<= paramInt1;
/* 105 */         l |= paramArrayOfint[i + k] & this.mask;
/*     */       } 
/* 107 */       this.data[b++] = l;
/*     */     } 
/*     */     
/* 110 */     int j = paramInt2 - i;
/* 111 */     if (j > 0) {
/* 112 */       long l = 0L;
/* 113 */       for (int k = j - 1; k >= 0; k--) {
/* 114 */         l <<= paramInt1;
/* 115 */         l |= paramArrayOfint[i + k] & this.mask;
/*     */       } 
/* 117 */       this.data[b] = l;
/*     */     } 
/*     */   }
/*     */   
/*     */   public SimpleBitStorage(int paramInt1, int paramInt2) {
/* 122 */     this(paramInt1, paramInt2, (long[])null);
/*     */   }
/*     */   
/*     */   public SimpleBitStorage(int paramInt1, int paramInt2, long[] paramArrayOflong) {
/* 126 */     Validate.inclusiveBetween(1L, 32L, paramInt1);
/*     */     
/* 128 */     this.size = paramInt2;
/* 129 */     this.bits = paramInt1;
/* 130 */     this.mask = (1L << paramInt1) - 1L;
/* 131 */     this.valuesPerLong = (char)(64 / paramInt1);
/*     */     
/* 133 */     int i = 3 * (this.valuesPerLong - 1);
/* 134 */     this.divideMul = MAGIC[i + 0];
/* 135 */     this.divideAdd = MAGIC[i + 1];
/* 136 */     this.divideShift = MAGIC[i + 2];
/*     */     
/* 138 */     int j = (paramInt2 + this.valuesPerLong - 1) / this.valuesPerLong;
/* 139 */     if (paramArrayOflong != null) {
/* 140 */       if (paramArrayOflong.length != j) {
/* 141 */         throw new InitializationException("Invalid length given for storage, got: " + paramArrayOflong.length + " but expected: " + j);
/*     */       }
/* 143 */       this.data = paramArrayOflong;
/*     */     } else {
/* 145 */       this.data = new long[j];
/*     */     } 
/*     */   }
/*     */   
/*     */   private int cellIndex(int paramInt) {
/* 150 */     long l1 = Integer.toUnsignedLong(this.divideMul);
/* 151 */     long l2 = Integer.toUnsignedLong(this.divideAdd);
/* 152 */     return (int)(paramInt * l1 + l2 >> 32L >> this.divideShift);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAndSet(int paramInt1, int paramInt2) {
/* 157 */     Validate.inclusiveBetween(0L, (this.size - 1), paramInt1);
/* 158 */     Validate.inclusiveBetween(0L, this.mask, paramInt2);
/*     */     
/* 160 */     int i = cellIndex(paramInt1);
/* 161 */     long l = this.data[i];
/* 162 */     int j = (paramInt1 - i * this.valuesPerLong) * this.bits;
/*     */     
/* 164 */     int k = (int)(l >> j & this.mask);
/* 165 */     this.data[i] = l & (this.mask << j ^ 0xFFFFFFFFFFFFFFFFL) | (paramInt2 & this.mask) << j;
/*     */     
/* 167 */     return k;
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(int paramInt1, int paramInt2) {
/* 172 */     Validate.inclusiveBetween(0L, (this.size - 1), paramInt1);
/* 173 */     Validate.inclusiveBetween(0L, this.mask, paramInt2);
/*     */     
/* 175 */     int i = cellIndex(paramInt1);
/* 176 */     long l = this.data[i];
/* 177 */     int j = (paramInt1 - i * this.valuesPerLong) * this.bits;
/*     */     
/* 179 */     this.data[i] = l & (this.mask << j ^ 0xFFFFFFFFFFFFFFFFL) | (paramInt2 & this.mask) << j;
/*     */   }
/*     */ 
/*     */   
/*     */   public int get(int paramInt) {
/* 184 */     Validate.inclusiveBetween(0L, (this.size - 1), paramInt);
/*     */     
/* 186 */     int i = cellIndex(paramInt);
/* 187 */     long l = this.data[i];
/* 188 */     int j = (paramInt - i * this.valuesPerLong) * this.bits;
/*     */     
/* 190 */     return (int)(l >> j & this.mask);
/*     */   }
/*     */ 
/*     */   
/*     */   public long[] getRaw() {
/* 195 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 200 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBits() {
/* 205 */     return this.bits;
/*     */   }
/*     */ 
/*     */   
/*     */   public void getAll(IntConsumer paramIntConsumer) {
/* 210 */     byte b = 0;
/* 211 */     for (long l : this.data) {
/* 212 */       for (byte b1 = 0; b1 < this.valuesPerLong; b1++) {
/* 213 */         paramIntConsumer.accept((int)(l & this.mask));
/* 214 */         l >>= this.bits;
/* 215 */         if (++b >= this.size) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void unpack(int[] paramArrayOfint) {
/* 224 */     int i = this.data.length;
/*     */     
/* 226 */     int j = 0; int k;
/* 227 */     for (k = 0; k < i - 1; k++) {
/* 228 */       long l = this.data[k];
/* 229 */       for (byte b = 0; b < this.valuesPerLong; b++) {
/* 230 */         paramArrayOfint[j + b] = (int)(l & this.mask);
/* 231 */         l >>= this.bits;
/*     */       } 
/* 233 */       j += this.valuesPerLong;
/*     */     } 
/*     */     
/* 236 */     k = this.size - j;
/* 237 */     if (k > 0) {
/* 238 */       long l = this.data[i - 1];
/* 239 */       for (byte b = 0; b < k; b++) {
/* 240 */         paramArrayOfint[j + b] = (int)(l & this.mask);
/* 241 */         l >>= this.bits;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BitStorage copy() {
/* 248 */     return new SimpleBitStorage(this.bits, this.size, (long[])this.data.clone());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\SimpleBitStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */