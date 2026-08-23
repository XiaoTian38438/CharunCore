/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ import net.minecraft.util.Util;
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
/*     */ class PositionCache
/*     */ {
/*     */   public static final int ENTRY_STRIDE = 2;
/*     */   private static final int NOT_FOUND = -1;
/* 124 */   private Object[] atomCache = new Object[16];
/*     */   private int nextKey;
/*     */   
/*     */   public int findKeyIndex(Atom<?> paramAtom) {
/* 128 */     for (byte b = 0; b < this.nextKey; b += 2) {
/* 129 */       if (this.atomCache[b] == paramAtom) {
/* 130 */         return b;
/*     */       }
/*     */     } 
/*     */     
/* 134 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int allocateNewEntry(Atom<?> paramAtom) {
/* 141 */     int i = this.nextKey;
/* 142 */     this.nextKey += 2;
/*     */     
/* 144 */     int j = i + 1;
/* 145 */     int k = this.atomCache.length;
/* 146 */     if (j >= k) {
/* 147 */       int m = Util.growByHalf(k, j + 1);
/* 148 */       Object[] arrayOfObject = new Object[m];
/* 149 */       System.arraycopy(this.atomCache, 0, arrayOfObject, 0, k);
/* 150 */       this.atomCache = arrayOfObject;
/*     */     } 
/*     */     
/* 153 */     this.atomCache[i] = paramAtom;
/*     */     
/* 155 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> CachedParseState.CacheEntry<T> getValue(int paramInt) {
/* 160 */     return (CachedParseState.CacheEntry<T>)this.atomCache[paramInt + 1];
/*     */   }
/*     */   
/*     */   public void setValue(int paramInt, CachedParseState.CacheEntry<?> paramCacheEntry) {
/* 164 */     this.atomCache[paramInt + 1] = paramCacheEntry;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\CachedParseState$PositionCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */