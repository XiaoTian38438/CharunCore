/*     */ package net.minecraft.util.profiling;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CombinedProfileFiller
/*     */   implements ProfilerFiller
/*     */ {
/*     */   private final ProfilerFiller first;
/*     */   private final ProfilerFiller second;
/*     */   
/*     */   public CombinedProfileFiller(ProfilerFiller paramProfilerFiller1, ProfilerFiller paramProfilerFiller2) {
/*  72 */     this.first = paramProfilerFiller1;
/*  73 */     this.second = paramProfilerFiller2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startTick() {
/*  78 */     this.first.startTick();
/*  79 */     this.second.startTick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void endTick() {
/*  84 */     this.first.endTick();
/*  85 */     this.second.endTick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(String paramString) {
/*  90 */     this.first.push(paramString);
/*  91 */     this.second.push(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(Supplier<String> paramSupplier) {
/*  96 */     this.first.push(paramSupplier);
/*  97 */     this.second.push(paramSupplier);
/*     */   }
/*     */ 
/*     */   
/*     */   public void markForCharting(MetricCategory paramMetricCategory) {
/* 102 */     this.first.markForCharting(paramMetricCategory);
/* 103 */     this.second.markForCharting(paramMetricCategory);
/*     */   }
/*     */ 
/*     */   
/*     */   public void pop() {
/* 108 */     this.first.pop();
/* 109 */     this.second.pop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void popPush(String paramString) {
/* 114 */     this.first.popPush(paramString);
/* 115 */     this.second.popPush(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void popPush(Supplier<String> paramSupplier) {
/* 120 */     this.first.popPush(paramSupplier);
/* 121 */     this.second.popPush(paramSupplier);
/*     */   }
/*     */ 
/*     */   
/*     */   public void incrementCounter(String paramString, int paramInt) {
/* 126 */     this.first.incrementCounter(paramString, paramInt);
/* 127 */     this.second.incrementCounter(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void incrementCounter(Supplier<String> paramSupplier, int paramInt) {
/* 132 */     this.first.incrementCounter(paramSupplier, paramInt);
/* 133 */     this.second.incrementCounter(paramSupplier, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addZoneText(String paramString) {
/* 138 */     this.first.addZoneText(paramString);
/* 139 */     this.second.addZoneText(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addZoneValue(long paramLong) {
/* 144 */     this.first.addZoneValue(paramLong);
/* 145 */     this.second.addZoneValue(paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setZoneColor(int paramInt) {
/* 150 */     this.first.setZoneColor(paramInt);
/* 151 */     this.second.setZoneColor(paramInt);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\ProfilerFiller$CombinedProfileFiller.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */