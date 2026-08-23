/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PoorMansPalettedContainer
/*     */ {
/*     */   private static final long SIZE_BITS = 4L;
/*     */   private final List<? extends Dynamic<?>> palette;
/*     */   private final long[] data;
/*     */   private final int bits;
/*     */   private final long mask;
/*     */   private final int valuesPerLong;
/*     */   
/*     */   public PoorMansPalettedContainer(List<? extends Dynamic<?>> paramList, long[] paramArrayOflong) {
/* 168 */     this.palette = paramList;
/* 169 */     this.data = paramArrayOflong;
/*     */     
/* 171 */     this.bits = Math.max(4, ChunkHeightAndBiomeFix.ceillog2(paramList.size()));
/* 172 */     this.mask = (1L << this.bits) - 1L;
/* 173 */     this.valuesPerLong = (char)(64 / this.bits);
/*     */   }
/*     */   
/*     */   public Dynamic<?> get(int paramInt1, int paramInt2, int paramInt3) {
/* 177 */     int i = this.palette.size();
/* 178 */     if (i < 1) {
/* 179 */       return null;
/*     */     }
/* 181 */     if (i == 1) {
/* 182 */       return this.palette.getFirst();
/*     */     }
/*     */     
/* 185 */     int j = getIndex(paramInt1, paramInt2, paramInt3);
/* 186 */     int k = j / this.valuesPerLong;
/* 187 */     if (k < 0 || k >= this.data.length) {
/* 188 */       return null;
/*     */     }
/* 190 */     long l = this.data[k];
/* 191 */     int m = (j - k * this.valuesPerLong) * this.bits;
/* 192 */     int n = (int)(l >> m & this.mask);
/* 193 */     if (n < 0 || n >= i) {
/* 194 */       return null;
/*     */     }
/* 196 */     return this.palette.get(n);
/*     */   }
/*     */   
/*     */   private int getIndex(int paramInt1, int paramInt2, int paramInt3) {
/* 200 */     return (paramInt2 << 4 | paramInt3) << 4 | paramInt1;
/*     */   }
/*     */   
/*     */   public List<? extends Dynamic<?>> palette() {
/* 204 */     return this.palette;
/*     */   }
/*     */   
/*     */   public long[] data() {
/* 208 */     return this.data;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\datafix\fixes\ChunkProtoTickListFix$PoorMansPalettedContainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */