/*     */ package net.minecraft.util.profiling.metrics.profiling;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Instant;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.LongSupplier;
/*     */ import net.minecraft.util.profiling.ActiveProfiler;
/*     */ import net.minecraft.util.profiling.ContinuousProfiler;
/*     */ import net.minecraft.util.profiling.EmptyProfileResults;
/*     */ import net.minecraft.util.profiling.InactiveProfiler;
/*     */ import net.minecraft.util.profiling.ProfileCollector;
/*     */ import net.minecraft.util.profiling.ProfileResults;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*     */ import net.minecraft.util.profiling.metrics.MetricsSamplerProvider;
/*     */ import net.minecraft.util.profiling.metrics.storage.MetricsPersister;
/*     */ import net.minecraft.util.profiling.metrics.storage.RecordedDeviation;
/*     */ 
/*     */ public class ActiveMetricsRecorder
/*     */   implements MetricsRecorder
/*     */ {
/*     */   public static final int PROFILING_MAX_DURATION_SECONDS = 10;
/*  33 */   private static Consumer<Path> globalOnReportFinished = null;
/*     */   
/*  35 */   private final Map<MetricSampler, List<RecordedDeviation>> deviationsBySampler = (Map<MetricSampler, List<RecordedDeviation>>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   private final ContinuousProfiler taskProfiler;
/*     */   
/*     */   private final Executor ioExecutor;
/*     */   private final MetricsPersister metricsPersister;
/*     */   private final Consumer<ProfileResults> onProfilingEnd;
/*     */   private final Consumer<Path> onReportFinished;
/*     */   private final MetricsSamplerProvider metricsSamplerProvider;
/*     */   private final LongSupplier wallTimeSource;
/*     */   private final long deadlineNano;
/*     */   private int currentTick;
/*     */   private ProfileCollector singleTickProfiler;
/*     */   private volatile boolean killSwitch;
/*  49 */   private Set<MetricSampler> thisTickSamplers = (Set<MetricSampler>)ImmutableSet.of();
/*     */   
/*     */   private ActiveMetricsRecorder(MetricsSamplerProvider paramMetricsSamplerProvider, LongSupplier paramLongSupplier, Executor paramExecutor, MetricsPersister paramMetricsPersister, Consumer<ProfileResults> paramConsumer, Consumer<Path> paramConsumer1) {
/*  52 */     this.metricsSamplerProvider = paramMetricsSamplerProvider;
/*  53 */     this.wallTimeSource = paramLongSupplier;
/*  54 */     this.taskProfiler = new ContinuousProfiler(paramLongSupplier, () -> this.currentTick, () -> false);
/*  55 */     this.ioExecutor = paramExecutor;
/*  56 */     this.metricsPersister = paramMetricsPersister;
/*  57 */     this.onProfilingEnd = paramConsumer;
/*  58 */     this.onReportFinished = (globalOnReportFinished == null) ? paramConsumer1 : paramConsumer1.andThen(globalOnReportFinished);
/*  59 */     this.deadlineNano = paramLongSupplier.getAsLong() + TimeUnit.NANOSECONDS.convert(10L, TimeUnit.SECONDS);
/*  60 */     this.singleTickProfiler = (ProfileCollector)new ActiveProfiler(this.wallTimeSource, () -> this.currentTick, () -> true);
/*  61 */     this.taskProfiler.enable();
/*     */   }
/*     */   
/*     */   public static ActiveMetricsRecorder createStarted(MetricsSamplerProvider paramMetricsSamplerProvider, LongSupplier paramLongSupplier, Executor paramExecutor, MetricsPersister paramMetricsPersister, Consumer<ProfileResults> paramConsumer, Consumer<Path> paramConsumer1) {
/*  65 */     return new ActiveMetricsRecorder(paramMetricsSamplerProvider, paramLongSupplier, paramExecutor, paramMetricsPersister, paramConsumer, paramConsumer1);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void end() {
/*  70 */     if (!isRecording()) {
/*     */       return;
/*     */     }
/*  73 */     this.killSwitch = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void cancel() {
/*  78 */     if (!isRecording()) {
/*     */       return;
/*     */     }
/*     */     
/*  82 */     this.singleTickProfiler = (ProfileCollector)InactiveProfiler.INSTANCE;
/*  83 */     this.onProfilingEnd.accept(EmptyProfileResults.EMPTY);
/*     */     
/*  85 */     cleanup(this.thisTickSamplers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startTick() {
/*  90 */     verifyStarted();
/*  91 */     this.thisTickSamplers = this.metricsSamplerProvider.samplers(() -> this.singleTickProfiler);
/*  92 */     for (MetricSampler metricSampler : this.thisTickSamplers) {
/*  93 */       metricSampler.onStartTick();
/*     */     }
/*  95 */     this.currentTick++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void endTick() {
/* 100 */     verifyStarted();
/* 101 */     if (this.currentTick == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 106 */     for (MetricSampler metricSampler : this.thisTickSamplers) {
/* 107 */       metricSampler.onEndTick(this.currentTick);
/* 108 */       if (metricSampler.triggersThreshold()) {
/* 109 */         RecordedDeviation recordedDeviation = new RecordedDeviation(Instant.now(), this.currentTick, this.singleTickProfiler.getResults());
/* 110 */         ((List<RecordedDeviation>)this.deviationsBySampler.computeIfAbsent(metricSampler, paramMetricSampler -> Lists.newArrayList())).add(recordedDeviation);
/*     */       } 
/*     */     } 
/*     */     
/* 114 */     if (this.killSwitch || this.wallTimeSource.getAsLong() > this.deadlineNano) {
/* 115 */       this.killSwitch = false;
/* 116 */       ProfileResults profileResults = this.taskProfiler.getResults();
/* 117 */       this.singleTickProfiler = (ProfileCollector)InactiveProfiler.INSTANCE;
/* 118 */       this.onProfilingEnd.accept(profileResults);
/* 119 */       scheduleSaveResults(profileResults);
/*     */       
/*     */       return;
/*     */     } 
/* 123 */     this.singleTickProfiler = (ProfileCollector)new ActiveProfiler(this.wallTimeSource, () -> this.currentTick, () -> true);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRecording() {
/* 128 */     return this.taskProfiler.isEnabled();
/*     */   }
/*     */ 
/*     */   
/*     */   public ProfilerFiller getProfiler() {
/* 133 */     return ProfilerFiller.combine(this.taskProfiler.getFiller(), (ProfilerFiller)this.singleTickProfiler);
/*     */   }
/*     */   
/*     */   private void verifyStarted() {
/* 137 */     if (!isRecording()) {
/* 138 */       throw new IllegalStateException("Not started!");
/*     */     }
/*     */   }
/*     */   
/*     */   private void scheduleSaveResults(ProfileResults paramProfileResults) {
/* 143 */     HashSet<MetricSampler> hashSet = new HashSet<>(this.thisTickSamplers);
/* 144 */     this.ioExecutor.execute(() -> {
/*     */           Path path = this.metricsPersister.saveReports(paramHashSet, this.deviationsBySampler, paramProfileResults);
/*     */           cleanup(paramHashSet);
/*     */           this.onReportFinished.accept(path);
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void cleanup(Collection<MetricSampler> paramCollection) {
/* 153 */     for (MetricSampler metricSampler : paramCollection) {
/* 154 */       metricSampler.onFinished();
/*     */     }
/*     */     
/* 157 */     this.deviationsBySampler.clear();
/* 158 */     this.taskProfiler.disable();
/*     */   }
/*     */   
/*     */   public static void registerGlobalCompletionCallback(Consumer<Path> paramConsumer) {
/* 162 */     globalOnReportFinished = paramConsumer;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\metrics\profiling\ActiveMetricsRecorder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */