/*     */ package net.minecraft.util.profiling;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import it.unimi.dsi.fastutil.longs.LongList;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongMaps;
/*     */ import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*     */ import java.time.Duration;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.IntSupplier;
/*     */ import java.util.function.LongSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import org.apache.commons.lang3.tuple.Pair;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ActiveProfiler
/*     */   implements ProfileCollector
/*     */ {
/*  28 */   private static final long WARNING_TIME_NANOS = Duration.ofMillis(100L).toNanos();
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  31 */   private final List<String> paths = Lists.newArrayList();
/*  32 */   private final LongList startTimes = (LongList)new LongArrayList();
/*  33 */   private final Map<String, PathEntry> entries = Maps.newHashMap();
/*     */   private final IntSupplier getTickTime;
/*     */   private final LongSupplier getRealTime;
/*     */   private final long startTimeNano;
/*     */   private final int startTimeTicks;
/*  38 */   private String path = "";
/*     */   
/*     */   private boolean started;
/*     */   
/*     */   private PathEntry currentEntry;
/*     */   private final BooleanSupplier suppressWarnings;
/*  44 */   private final Set<Pair<String, MetricCategory>> chartedPaths = (Set<Pair<String, MetricCategory>>)new ObjectArraySet();
/*     */   
/*     */   public ActiveProfiler(LongSupplier paramLongSupplier, IntSupplier paramIntSupplier, BooleanSupplier paramBooleanSupplier) {
/*  47 */     this.startTimeNano = paramLongSupplier.getAsLong();
/*  48 */     this.getRealTime = paramLongSupplier;
/*  49 */     this.startTimeTicks = paramIntSupplier.getAsInt();
/*  50 */     this.getTickTime = paramIntSupplier;
/*  51 */     this.suppressWarnings = paramBooleanSupplier;
/*     */   }
/*     */ 
/*     */   
/*     */   public void startTick() {
/*  56 */     if (this.started) {
/*  57 */       LOGGER.error("Profiler tick already started - missing endTick()?");
/*     */       
/*     */       return;
/*     */     } 
/*  61 */     this.started = true;
/*  62 */     this.path = "";
/*  63 */     this.paths.clear();
/*  64 */     push("root");
/*     */   }
/*     */ 
/*     */   
/*     */   public void endTick() {
/*  69 */     if (!this.started) {
/*  70 */       LOGGER.error("Profiler tick already ended - missing startTick()?");
/*     */       
/*     */       return;
/*     */     } 
/*  74 */     pop();
/*  75 */     this.started = false;
/*     */     
/*  77 */     if (!this.path.isEmpty()) {
/*  78 */       LOGGER.error("Profiler tick ended before path was fully popped (remainder: '{}'). Mismatched push/pop?", LogUtils.defer(() -> ProfileResults.demanglePath(this.path)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(String paramString) {
/*  84 */     if (!this.started) {
/*  85 */       LOGGER.error("Cannot push '{}' to profiler if profiler tick hasn't started - missing startTick()?", paramString);
/*     */       
/*     */       return;
/*     */     } 
/*  89 */     if (!this.path.isEmpty()) {
/*  90 */       this.path += "\036";
/*     */     }
/*  92 */     this.path += this.path;
/*  93 */     this.paths.add(this.path);
/*  94 */     this.startTimes.add(Util.getNanos());
/*  95 */     this.currentEntry = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void push(Supplier<String> paramSupplier) {
/* 100 */     push(paramSupplier.get());
/*     */   }
/*     */ 
/*     */   
/*     */   public void markForCharting(MetricCategory paramMetricCategory) {
/* 105 */     this.chartedPaths.add(Pair.of(this.path, paramMetricCategory));
/*     */   }
/*     */ 
/*     */   
/*     */   public void pop() {
/* 110 */     if (!this.started) {
/* 111 */       LOGGER.error("Cannot pop from profiler if profiler tick hasn't started - missing startTick()?");
/*     */       return;
/*     */     } 
/* 114 */     if (this.startTimes.isEmpty()) {
/* 115 */       LOGGER.error("Tried to pop one too many times! Mismatched push() and pop()?");
/*     */       return;
/*     */     } 
/* 118 */     long l1 = Util.getNanos();
/* 119 */     long l2 = this.startTimes.removeLong(this.startTimes.size() - 1);
/* 120 */     this.paths.removeLast();
/* 121 */     long l3 = l1 - l2;
/*     */     
/* 123 */     PathEntry pathEntry = getCurrentEntry();
/* 124 */     pathEntry.accumulatedDuration += l3;
/* 125 */     pathEntry.count++;
/* 126 */     pathEntry.maxDuration = Math.max(pathEntry.maxDuration, l3);
/* 127 */     pathEntry.minDuration = Math.min(pathEntry.minDuration, l3);
/*     */     
/* 129 */     if (l3 > WARNING_TIME_NANOS && !this.suppressWarnings.getAsBoolean()) {
/* 130 */       LOGGER.warn("Something's taking too long! '{}' took aprox {} ms", LogUtils.defer(() -> ProfileResults.demanglePath(this.path)), LogUtils.defer(() -> Double.valueOf(paramLong / 1000000.0D)));
/*     */     }
/*     */     
/* 133 */     this.path = this.paths.isEmpty() ? "" : this.paths.getLast();
/* 134 */     this.currentEntry = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void popPush(String paramString) {
/* 139 */     pop();
/* 140 */     push(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public void popPush(Supplier<String> paramSupplier) {
/* 145 */     pop();
/* 146 */     push(paramSupplier);
/*     */   }
/*     */   
/*     */   private PathEntry getCurrentEntry() {
/* 150 */     if (this.currentEntry == null) {
/* 151 */       this.currentEntry = this.entries.computeIfAbsent(this.path, paramString -> new PathEntry());
/*     */     }
/*     */     
/* 154 */     return this.currentEntry;
/*     */   }
/*     */ 
/*     */   
/*     */   public void incrementCounter(String paramString, int paramInt) {
/* 159 */     (getCurrentEntry()).counters.addTo(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void incrementCounter(Supplier<String> paramSupplier, int paramInt) {
/* 164 */     (getCurrentEntry()).counters.addTo(paramSupplier.get(), paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public ProfileResults getResults() {
/* 169 */     return new FilledProfileResults((Map)this.entries, this.startTimeNano, this.startTimeTicks, this.getRealTime.getAsLong(), this.getTickTime.getAsInt());
/*     */   }
/*     */ 
/*     */   
/*     */   public PathEntry getEntry(String paramString) {
/* 174 */     return this.entries.get(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<Pair<String, MetricCategory>> getChartedPaths() {
/* 179 */     return this.chartedPaths;
/*     */   }
/*     */   
/*     */   public static class PathEntry implements ProfilerPathEntry {
/* 183 */     long maxDuration = Long.MIN_VALUE;
/* 184 */     long minDuration = Long.MAX_VALUE;
/*     */     long accumulatedDuration;
/*     */     long count;
/* 187 */     final Object2LongOpenHashMap<String> counters = new Object2LongOpenHashMap();
/*     */ 
/*     */     
/*     */     public long getDuration() {
/* 191 */       return this.accumulatedDuration;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getMaxDuration() {
/* 196 */       return this.maxDuration;
/*     */     }
/*     */ 
/*     */     
/*     */     public long getCount() {
/* 201 */       return this.count;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object2LongMap<String> getCounters() {
/* 206 */       return Object2LongMaps.unmodifiable((Object2LongMap)this.counters);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\ActiveProfiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */