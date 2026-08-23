/*     */ package com.mojang.authlib.yggdrasil;
/*     */ 
/*     */ import com.mojang.authlib.minecraft.client.MinecraftClient;
/*     */ import java.net.URL;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class null
/*     */   implements Runnable
/*     */ {
/*  85 */   private final AtomicInteger failureCount = new AtomicInteger();
/*     */ 
/*     */   
/*     */   public void run() {
/*  89 */     Objects.requireNonNull(keySet); YggdrasilServicesKeyInfo.fetch(url, client).ifPresent(keySet::set);
/*  90 */     ready.complete(null);
/*  91 */     reschedule();
/*     */   }
/*     */   
/*     */   private void reschedule() {
/*  95 */     if (keySet.get() == null) {
/*  96 */       int i = Math.min(this.failureCount.getAndIncrement(), 6);
/*  97 */       int j = 5 * (1 << i);
/*  98 */       YggdrasilServicesKeyInfo.FETCHER_EXECUTOR.schedule(this, j, TimeUnit.MINUTES);
/*     */       return;
/*     */     } 
/* 101 */     YggdrasilServicesKeyInfo.FETCHER_EXECUTOR.schedule(this, 24L, TimeUnit.HOURS);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\authlib\yggdrasil\YggdrasilServicesKeyInfo$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */