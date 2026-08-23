/*     */ package net.minecraft.util.profiling;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ProfilerFiller
/*     */ {
/*     */   public static final String ROOT = "root";
/*     */   
/*     */   void startTick();
/*     */   
/*     */   void endTick();
/*     */   
/*     */   void push(String paramString);
/*     */   
/*     */   void push(Supplier<String> paramSupplier);
/*     */   
/*     */   void pop();
/*     */   
/*     */   void popPush(String paramString);
/*     */   
/*     */   void popPush(Supplier<String> paramSupplier);
/*     */   
/*     */   default void addZoneText(String paramString) {}
/*     */   
/*     */   default void addZoneValue(long paramLong) {}
/*     */   
/*     */   default void setZoneColor(int paramInt) {}
/*     */   
/*     */   default Zone zone(String paramString) {
/*  34 */     push(paramString);
/*  35 */     return new Zone(this);
/*     */   }
/*     */   
/*     */   default Zone zone(Supplier<String> paramSupplier) {
/*  39 */     push(paramSupplier);
/*  40 */     return new Zone(this);
/*     */   }
/*     */   
/*     */   void markForCharting(MetricCategory paramMetricCategory);
/*     */   
/*     */   default void incrementCounter(String paramString) {
/*  46 */     incrementCounter(paramString, 1);
/*     */   }
/*     */   
/*     */   void incrementCounter(String paramString, int paramInt);
/*     */   
/*     */   default void incrementCounter(Supplier<String> paramSupplier) {
/*  52 */     incrementCounter(paramSupplier, 1);
/*     */   }
/*     */   
/*     */   void incrementCounter(Supplier<String> paramSupplier, int paramInt);
/*     */   
/*     */   static ProfilerFiller combine(ProfilerFiller paramProfilerFiller1, ProfilerFiller paramProfilerFiller2) {
/*  58 */     if (paramProfilerFiller1 == InactiveProfiler.INSTANCE) {
/*  59 */       return paramProfilerFiller2;
/*     */     }
/*  61 */     if (paramProfilerFiller2 == InactiveProfiler.INSTANCE) {
/*  62 */       return paramProfilerFiller1;
/*     */     }
/*  64 */     return new CombinedProfileFiller(paramProfilerFiller1, paramProfilerFiller2);
/*     */   }
/*     */   
/*     */   public static class CombinedProfileFiller implements ProfilerFiller {
/*     */     private final ProfilerFiller first;
/*     */     private final ProfilerFiller second;
/*     */     
/*     */     public CombinedProfileFiller(ProfilerFiller param1ProfilerFiller1, ProfilerFiller param1ProfilerFiller2) {
/*  72 */       this.first = param1ProfilerFiller1;
/*  73 */       this.second = param1ProfilerFiller2;
/*     */     }
/*     */ 
/*     */     
/*     */     public void startTick() {
/*  78 */       this.first.startTick();
/*  79 */       this.second.startTick();
/*     */     }
/*     */ 
/*     */     
/*     */     public void endTick() {
/*  84 */       this.first.endTick();
/*  85 */       this.second.endTick();
/*     */     }
/*     */ 
/*     */     
/*     */     public void push(String param1String) {
/*  90 */       this.first.push(param1String);
/*  91 */       this.second.push(param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public void push(Supplier<String> param1Supplier) {
/*  96 */       this.first.push(param1Supplier);
/*  97 */       this.second.push(param1Supplier);
/*     */     }
/*     */ 
/*     */     
/*     */     public void markForCharting(MetricCategory param1MetricCategory) {
/* 102 */       this.first.markForCharting(param1MetricCategory);
/* 103 */       this.second.markForCharting(param1MetricCategory);
/*     */     }
/*     */ 
/*     */     
/*     */     public void pop() {
/* 108 */       this.first.pop();
/* 109 */       this.second.pop();
/*     */     }
/*     */ 
/*     */     
/*     */     public void popPush(String param1String) {
/* 114 */       this.first.popPush(param1String);
/* 115 */       this.second.popPush(param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public void popPush(Supplier<String> param1Supplier) {
/* 120 */       this.first.popPush(param1Supplier);
/* 121 */       this.second.popPush(param1Supplier);
/*     */     }
/*     */ 
/*     */     
/*     */     public void incrementCounter(String param1String, int param1Int) {
/* 126 */       this.first.incrementCounter(param1String, param1Int);
/* 127 */       this.second.incrementCounter(param1String, param1Int);
/*     */     }
/*     */ 
/*     */     
/*     */     public void incrementCounter(Supplier<String> param1Supplier, int param1Int) {
/* 132 */       this.first.incrementCounter(param1Supplier, param1Int);
/* 133 */       this.second.incrementCounter(param1Supplier, param1Int);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addZoneText(String param1String) {
/* 138 */       this.first.addZoneText(param1String);
/* 139 */       this.second.addZoneText(param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addZoneValue(long param1Long) {
/* 144 */       this.first.addZoneValue(param1Long);
/* 145 */       this.second.addZoneValue(param1Long);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setZoneColor(int param1Int) {
/* 150 */       this.first.setZoneColor(param1Int);
/* 151 */       this.second.setZoneColor(param1Int);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\ProfilerFiller.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */