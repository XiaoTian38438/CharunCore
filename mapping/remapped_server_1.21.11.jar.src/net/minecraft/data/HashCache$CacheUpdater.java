/*     */ package net.minecraft.data;
/*     */ 
/*     */ import com.google.common.hash.HashCode;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ class CacheUpdater
/*     */   implements CachedOutput
/*     */ {
/*     */   private final String provider;
/*     */   private final HashCache.ProviderCache oldCache;
/*     */   private final HashCache.ProviderCacheBuilder newCache;
/* 101 */   private final AtomicInteger writes = new AtomicInteger();
/*     */   private volatile boolean closed;
/*     */   
/*     */   CacheUpdater(String paramString1, String paramString2, HashCache.ProviderCache paramProviderCache) {
/* 105 */     this.provider = paramString1;
/* 106 */     this.oldCache = paramProviderCache;
/* 107 */     this.newCache = new HashCache.ProviderCacheBuilder(paramString2);
/*     */   }
/*     */   
/*     */   private boolean shouldWrite(Path paramPath, HashCode paramHashCode) {
/* 111 */     return (!Objects.equals(this.oldCache.get(paramPath), paramHashCode) || !Files.exists(paramPath, new java.nio.file.LinkOption[0]));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeIfNeeded(Path paramPath, byte[] paramArrayOfbyte, HashCode paramHashCode) throws IOException {
/* 116 */     if (this.closed) {
/* 117 */       throw new IllegalStateException("Cannot write to cache as it has already been closed");
/*     */     }
/* 119 */     if (shouldWrite(paramPath, paramHashCode)) {
/* 120 */       this.writes.incrementAndGet();
/* 121 */       Files.createDirectories(paramPath.getParent(), (FileAttribute<?>[])new FileAttribute[0]);
/* 122 */       Files.write(paramPath, paramArrayOfbyte, new java.nio.file.OpenOption[0]);
/*     */     } 
/* 124 */     this.newCache.put(paramPath, paramHashCode);
/*     */   }
/*     */   
/*     */   public HashCache.UpdateResult close() {
/* 128 */     this.closed = true;
/* 129 */     return new HashCache.UpdateResult(this.provider, this.newCache.build(), this.writes.get());
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\HashCache$CacheUpdater.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */