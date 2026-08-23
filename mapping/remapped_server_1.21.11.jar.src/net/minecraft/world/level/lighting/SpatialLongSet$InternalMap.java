/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.HashCommon;
/*     */ import it.unimi.dsi.fastutil.longs.Long2LongLinkedOpenHashMap;
/*     */ import java.util.NoSuchElementException;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InternalMap
/*     */   extends Long2LongLinkedOpenHashMap
/*     */ {
/*  20 */   private static final int X_BITS = Mth.log2(60000000);
/*  21 */   private static final int Z_BITS = Mth.log2(60000000);
/*  22 */   private static final int Y_BITS = 64 - X_BITS - Z_BITS;
/*     */   
/*     */   private static final int Y_OFFSET = 0;
/*  25 */   private static final int Z_OFFSET = Y_BITS;
/*  26 */   private static final int X_OFFSET = Y_BITS + Z_BITS;
/*  27 */   private static final long OUTER_MASK = 3L << X_OFFSET | 0x3L | 3L << Z_OFFSET;
/*     */   
/*  29 */   private int lastPos = -1;
/*     */   private long lastOuterKey;
/*     */   private final int minSize;
/*     */   
/*     */   public InternalMap(int paramInt, float paramFloat) {
/*  34 */     super(paramInt, paramFloat);
/*  35 */     this.minSize = paramInt;
/*     */   }
/*     */   
/*     */   static long getOuterKey(long paramLong) {
/*  39 */     return paramLong & (OUTER_MASK ^ 0xFFFFFFFFFFFFFFFFL);
/*     */   }
/*     */   
/*     */   static int getInnerKey(long paramLong) {
/*  43 */     int i = (int)(paramLong >>> X_OFFSET & 0x3L);
/*  44 */     int j = (int)(paramLong >>> 0L & 0x3L);
/*  45 */     int k = (int)(paramLong >>> Z_OFFSET & 0x3L);
/*  46 */     return i << 4 | k << 2 | j;
/*     */   }
/*     */   
/*     */   static long getFullKey(long paramLong, int paramInt) {
/*  50 */     paramLong |= (paramInt >>> 4 & 0x3) << X_OFFSET;
/*  51 */     paramLong |= (paramInt >>> 2 & 0x3) << Z_OFFSET;
/*  52 */     paramLong |= (paramInt >>> 0 & 0x3) << 0L;
/*  53 */     return paramLong;
/*     */   }
/*     */   public boolean addBit(long paramLong) {
/*     */     int j;
/*  57 */     long l1 = getOuterKey(paramLong);
/*  58 */     int i = getInnerKey(paramLong);
/*  59 */     long l2 = 1L << i;
/*     */     
/*  61 */     if (l1 == 0L) {
/*  62 */       if (this.containsNullKey) {
/*  63 */         return replaceBit(this.n, l2);
/*     */       }
/*  65 */       this.containsNullKey = true;
/*  66 */       j = this.n;
/*     */     } else {
/*  68 */       if (this.lastPos != -1 && l1 == this.lastOuterKey) {
/*  69 */         return replaceBit(this.lastPos, l2);
/*     */       }
/*  71 */       long[] arrayOfLong = this.key;
/*  72 */       j = (int)HashCommon.mix(l1) & this.mask;
/*  73 */       long l = arrayOfLong[j];
/*  74 */       while (l != 0L) {
/*  75 */         if (l == l1) {
/*  76 */           this.lastPos = j;
/*  77 */           this.lastOuterKey = l1;
/*  78 */           return replaceBit(j, l2);
/*     */         } 
/*  80 */         j = j + 1 & this.mask;
/*  81 */         l = arrayOfLong[j];
/*     */       } 
/*     */     } 
/*  84 */     this.key[j] = l1;
/*  85 */     this.value[j] = l2;
/*  86 */     if (this.size == 0) {
/*  87 */       this.first = this.last = j;
/*     */       
/*  89 */       this.link[j] = -1L;
/*     */     } else {
/*  91 */       this.link[this.last] = this.link[this.last] ^ (this.link[this.last] ^ j & 0xFFFFFFFFL) & 0xFFFFFFFFL;
/*  92 */       this.link[j] = (this.last & 0xFFFFFFFFL) << 32L | 0xFFFFFFFFL;
/*  93 */       this.last = j;
/*     */     } 
/*  95 */     if (this.size++ >= this.maxFill) {
/*  96 */       rehash(HashCommon.arraySize(this.size + 1, this.f));
/*     */     }
/*  98 */     return false;
/*     */   }
/*     */   
/*     */   private boolean replaceBit(int paramInt, long paramLong) {
/* 102 */     boolean bool = ((this.value[paramInt] & paramLong) != 0L) ? true : false;
/* 103 */     this.value[paramInt] = this.value[paramInt] | paramLong;
/* 104 */     return bool;
/*     */   }
/*     */   
/*     */   public boolean removeBit(long paramLong) {
/* 108 */     long l1 = getOuterKey(paramLong);
/* 109 */     int i = getInnerKey(paramLong);
/* 110 */     long l2 = 1L << i;
/* 111 */     if (l1 == 0L) {
/* 112 */       if (this.containsNullKey) {
/* 113 */         return removeFromNullEntry(l2);
/*     */       }
/* 115 */       return false;
/*     */     } 
/* 117 */     if (this.lastPos != -1 && l1 == this.lastOuterKey) {
/* 118 */       return removeFromEntry(this.lastPos, l2);
/*     */     }
/* 120 */     long[] arrayOfLong = this.key;
/* 121 */     int j = (int)HashCommon.mix(l1) & this.mask;
/* 122 */     long l3 = arrayOfLong[j];
/*     */     while (true) {
/* 124 */       if (l3 == 0L) {
/* 125 */         return false;
/*     */       }
/* 127 */       if (l1 == l3) {
/* 128 */         this.lastPos = j;
/* 129 */         this.lastOuterKey = l1;
/* 130 */         return removeFromEntry(j, l2);
/*     */       } 
/* 132 */       j = j + 1 & this.mask;
/* 133 */       l3 = arrayOfLong[j];
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean removeFromNullEntry(long paramLong) {
/* 138 */     if ((this.value[this.n] & paramLong) == 0L) {
/* 139 */       return false;
/*     */     }
/* 141 */     this.value[this.n] = this.value[this.n] & (paramLong ^ 0xFFFFFFFFFFFFFFFFL);
/* 142 */     if (this.value[this.n] != 0L) {
/* 143 */       return true;
/*     */     }
/* 145 */     this.containsNullKey = false;
/* 146 */     this.size--;
/* 147 */     fixPointers(this.n);
/* 148 */     if (this.size < this.maxFill / 4 && this.n > 16) {
/* 149 */       rehash(this.n / 2);
/*     */     }
/* 151 */     return true;
/*     */   }
/*     */   
/*     */   private boolean removeFromEntry(int paramInt, long paramLong) {
/* 155 */     if ((this.value[paramInt] & paramLong) == 0L) {
/* 156 */       return false;
/*     */     }
/* 158 */     this.value[paramInt] = this.value[paramInt] & (paramLong ^ 0xFFFFFFFFFFFFFFFFL);
/* 159 */     if (this.value[paramInt] != 0L) {
/* 160 */       return true;
/*     */     }
/* 162 */     this.lastPos = -1;
/* 163 */     this.size--;
/* 164 */     fixPointers(paramInt);
/* 165 */     shiftKeys(paramInt);
/* 166 */     if (this.size < this.maxFill / 4 && this.n > 16) {
/* 167 */       rehash(this.n / 2);
/*     */     }
/* 169 */     return true;
/*     */   }
/*     */   
/*     */   public long removeFirstBit() {
/* 173 */     if (this.size == 0) {
/* 174 */       throw new NoSuchElementException();
/*     */     }
/* 176 */     int i = this.first;
/* 177 */     long l = this.key[i];
/* 178 */     int j = Long.numberOfTrailingZeros(this.value[i]);
/* 179 */     this.value[i] = this.value[i] & (1L << j ^ 0xFFFFFFFFFFFFFFFFL);
/* 180 */     if (this.value[i] == 0L) {
/* 181 */       removeFirstLong();
/* 182 */       this.lastPos = -1;
/*     */     } 
/* 184 */     return getFullKey(l, j);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void rehash(int paramInt) {
/* 189 */     if (paramInt > this.minSize)
/* 190 */       super.rehash(paramInt); 
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\world\level\lighting\SpatialLongSet$InternalMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */