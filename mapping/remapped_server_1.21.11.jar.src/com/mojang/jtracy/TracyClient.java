/*     */ package com.mojang.jtracy;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ public class TracyClient {
/*  11 */   private static AtomicInteger lastGpuContextId = new AtomicInteger(0);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean loaded = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAvailable() {
/*  23 */     return loaded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized void load() throws UnsatisfiedLinkError {
/*  35 */     if (!loaded) {
/*  36 */       (new Loader()).load();
/*  37 */       loaded = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void markFrame() {
/*  46 */     if (loaded) {
/*  47 */       TracyBindings.markFrame(0L);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void frameImage(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean) {
/*  64 */     if (loaded) {
/*  65 */       TracyBindings.frameImage(paramByteBuffer, paramInt1, paramInt2, paramInt3, paramBoolean);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Zone beginZone(String paramString, boolean paramBoolean) {
/*  78 */     if (loaded) {
/*  79 */       String str1 = "";
/*  80 */       String str2 = "";
/*  81 */       int i = 0;
/*  82 */       if (paramBoolean) {
/*  83 */         StackWalker stackWalker = StackWalker.getInstance(Set.of(StackWalker.Option.RETAIN_CLASS_REFERENCE), 2);
/*  84 */         Optional<StackWalker.StackFrame> optional = stackWalker.<Optional>walk(paramStream -> paramStream.filter(()).findFirst());
/*  85 */         if (optional.isPresent()) {
/*  86 */           StackWalker.StackFrame stackFrame = optional.get();
/*  87 */           str1 = stackFrame.getMethodName();
/*  88 */           str2 = stackFrame.getFileName();
/*  89 */           i = stackFrame.getLineNumber();
/*     */         } 
/*     */       } 
/*  92 */       return new Zone(TracyBindings.beginZone(paramString, str1, str2, i));
/*     */     } 
/*  94 */     return Zone.UNAVAILABLE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Zone beginZone(String paramString1, String paramString2, String paramString3, int paramInt) {
/* 108 */     if (loaded) {
/* 109 */       return new Zone(TracyBindings.beginZone(paramString1, paramString2, paramString3, paramInt));
/*     */     }
/* 111 */     return Zone.UNAVAILABLE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setThreadName(String paramString, int paramInt) {
/* 122 */     if (loaded) {
/* 123 */       TracyBindings.setThreadName(paramString, paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Plot createPlot(String paramString) {
/* 136 */     if (loaded) {
/* 137 */       return new Plot(TracyBindings.leakName(paramString));
/*     */     }
/* 139 */     return Plot.UNAVAILABLE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DiscontinuousFrame createDiscontinuousFrame(String paramString) {
/* 156 */     if (loaded) {
/* 157 */       return new DiscontinuousFrame(TracyBindings.leakName(paramString));
/*     */     }
/* 159 */     return DiscontinuousFrame.UNAVAILABLE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ContinuousFrame createContinuousFrame(String paramString) {
/* 175 */     if (loaded) {
/* 176 */       return new ContinuousFrame(TracyBindings.leakName(paramString));
/*     */     }
/* 178 */     return ContinuousFrame.UNAVAILABLE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MemoryPool createMemoryPool(String paramString) {
/* 192 */     if (loaded) {
/* 193 */       return new MemoryPool(TracyBindings.leakName(paramString));
/*     */     }
/* 195 */     return MemoryPool.UNAVAILABLE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void reportAppInfo(String paramString) {
/* 207 */     if (loaded) {
/* 208 */       TracyBindings.appInfo(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void message(String paramString) {
/* 220 */     if (loaded) {
/* 221 */       TracyBindings.message(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void message(String paramString, int paramInt) {
/* 236 */     if (loaded) {
/* 237 */       TracyBindings.messageColored(paramString, paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void message(Supplier<String> paramSupplier) {
/* 249 */     if (loaded) {
/* 250 */       TracyBindings.message(paramSupplier.get());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void message(Supplier<String> paramSupplier, int paramInt) {
/* 265 */     if (loaded) {
/* 266 */       TracyBindings.messageColored(paramSupplier.get(), paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static GpuContext createGpuContext(GpuApi paramGpuApi, long paramLong, float paramFloat) {
/* 279 */     if (loaded) {
/* 280 */       int i = lastGpuContextId.incrementAndGet();
/* 281 */       if (i == 255) {
/* 282 */         throw new UnsupportedOperationException("Too many GPU contexts were created");
/*     */       }
/* 284 */       TracyBindings.newGpuContext(i, paramLong, paramFloat, 0, paramGpuApi.getId());
/* 285 */       return new GpuContext(i);
/*     */     } 
/* 287 */     return GpuContext.UNAVAILABLE;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\jtracy\TracyClient.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */