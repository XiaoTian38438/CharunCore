/*     */ package net.minecraft.util.profiling;
/*     */ 
/*     */ import com.mojang.jtracy.Plot;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import com.mojang.jtracy.Zone;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TracyZoneFiller implements ProfilerFiller {
/*  20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  22 */   private static final StackWalker STACK_WALKER = StackWalker.getInstance(Set.of(StackWalker.Option.RETAIN_CLASS_REFERENCE), 5);
/*     */   
/*  24 */   private final List<Zone> activeZones = new ArrayList<>();
/*  25 */   private final Map<String, PlotAndValue> plots = new HashMap<>();
/*     */   private final String name;
/*     */   
/*     */   public TracyZoneFiller() {
/*  29 */     this.name = Thread.currentThread().getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void startTick() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void endTick() {
/*  39 */     for (PlotAndValue plotAndValue : this.plots.values()) {
/*  40 */       plotAndValue.set(0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(String paramString) {
/*  46 */     String str1 = "";
/*  47 */     String str2 = "";
/*  48 */     int i = 0;
/*  49 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/*  50 */       Optional<StackWalker.StackFrame> optional = STACK_WALKER.<Optional>walk(paramStream -> paramStream.filter(()).findFirst());
/*  51 */       if (optional.isPresent()) {
/*  52 */         StackWalker.StackFrame stackFrame = optional.get();
/*  53 */         str1 = stackFrame.getMethodName();
/*  54 */         str2 = stackFrame.getFileName();
/*  55 */         i = stackFrame.getLineNumber();
/*     */       } 
/*     */     } 
/*  58 */     Zone zone = TracyClient.beginZone(paramString, str1, str2, i);
/*  59 */     this.activeZones.add(zone);
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(Supplier<String> paramSupplier) {
/*  64 */     push(paramSupplier.get());
/*     */   }
/*     */ 
/*     */   
/*     */   public void pop() {
/*  69 */     if (this.activeZones.isEmpty()) {
/*  70 */       LOGGER.error("Tried to pop one too many times! Mismatched push() and pop()?");
/*     */       return;
/*     */     } 
/*  73 */     Zone zone = this.activeZones.removeLast();
/*  74 */     zone.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void popPush(String paramString) {
/*  79 */     pop();
/*  80 */     push(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void popPush(Supplier<String> paramSupplier) {
/*  85 */     pop();
/*  86 */     push(paramSupplier.get());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void markForCharting(MetricCategory paramMetricCategory) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void incrementCounter(String paramString, int paramInt) {
/*  96 */     ((PlotAndValue)this.plots.computeIfAbsent(paramString, paramString2 -> new PlotAndValue(this.name + " " + this.name))).add(paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void incrementCounter(Supplier<String> paramSupplier, int paramInt) {
/* 101 */     incrementCounter(paramSupplier.get(), paramInt);
/*     */   }
/*     */   
/*     */   private Zone activeZone() {
/* 105 */     return this.activeZones.getLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addZoneText(String paramString) {
/* 110 */     activeZone().addText(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addZoneValue(long paramLong) {
/* 115 */     activeZone().addValue(paramLong);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setZoneColor(int paramInt) {
/* 120 */     activeZone().setColor(paramInt);
/*     */   }
/*     */   
/*     */   private static final class PlotAndValue {
/*     */     private final Plot plot;
/*     */     private int value;
/*     */     
/*     */     PlotAndValue(String param1String) {
/* 128 */       this.plot = TracyClient.createPlot(param1String);
/* 129 */       this.value = 0;
/*     */     }
/*     */     
/*     */     void set(int param1Int) {
/* 133 */       this.value = param1Int;
/* 134 */       this.plot.setValue(param1Int);
/*     */     }
/*     */     
/*     */     void add(int param1Int) {
/* 138 */       set(this.value + param1Int);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\TracyZoneFiller.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */