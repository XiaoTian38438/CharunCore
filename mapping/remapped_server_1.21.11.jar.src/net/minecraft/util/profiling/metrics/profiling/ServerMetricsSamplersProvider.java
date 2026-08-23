/*     */ package net.minecraft.util.profiling.metrics.profiling;
/*     */ 
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.base.Ticker;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.LongSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.SystemReport;
/*     */ import net.minecraft.util.profiling.ProfileCollector;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*     */ import net.minecraft.util.profiling.metrics.MetricsRegistry;
/*     */ import net.minecraft.util.profiling.metrics.MetricsSamplerProvider;
/*     */ import org.slf4j.Logger;
/*     */ import oshi.SystemInfo;
/*     */ import oshi.hardware.CentralProcessor;
/*     */ 
/*     */ public class ServerMetricsSamplersProvider implements MetricsSamplerProvider {
/*  26 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  28 */   private final Set<MetricSampler> samplers = (Set<MetricSampler>)new ObjectOpenHashSet();
/*  29 */   private final ProfilerSamplerAdapter samplerFactory = new ProfilerSamplerAdapter();
/*     */   
/*     */   public ServerMetricsSamplersProvider(LongSupplier paramLongSupplier, boolean paramBoolean) {
/*  32 */     this.samplers.add(tickTimeSampler(paramLongSupplier));
/*     */     
/*  34 */     if (paramBoolean) {
/*  35 */       this.samplers.addAll(runtimeIndependentSamplers());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<MetricSampler> runtimeIndependentSamplers() {
/*  44 */     ImmutableSet.Builder builder = ImmutableSet.builder();
/*     */     
/*     */     try {
/*  47 */       CpuStats cpuStats = new CpuStats();
/*     */ 
/*     */       
/*  50 */       Objects.requireNonNull(builder); IntStream.range(0, cpuStats.nrOfCpus).mapToObj(paramInt -> MetricSampler.create("cpu#" + paramInt, MetricCategory.CPU, ())).forEach(builder::add);
/*  51 */     } catch (Throwable throwable) {
/*  52 */       LOGGER.warn("Failed to query cpu, no cpu stats will be recorded", throwable);
/*     */     } 
/*     */     
/*  55 */     builder.add(MetricSampler.create("heap MiB", MetricCategory.JVM, () -> SystemReport.sizeInMiB(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory())));
/*  56 */     builder.addAll(MetricsRegistry.INSTANCE.getRegisteredSamplers());
/*  57 */     return (Set<MetricSampler>)builder.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<MetricSampler> samplers(Supplier<ProfileCollector> paramSupplier) {
/*  62 */     this.samplers.addAll(this.samplerFactory.newSamplersFoundInProfiler(paramSupplier));
/*  63 */     return this.samplers;
/*     */   }
/*     */   
/*     */   public static MetricSampler tickTimeSampler(final LongSupplier timeSource) {
/*  67 */     Stopwatch stopwatch = Stopwatch.createUnstarted(new Ticker()
/*     */         {
/*     */           public long read() {
/*  70 */             return timeSource.getAsLong();
/*     */           }
/*     */         });
/*     */     
/*  74 */     ToDoubleFunction toDoubleFunction = paramStopwatch -> {
/*     */         if (paramStopwatch.isRunning()) {
/*     */           paramStopwatch.stop();
/*     */         }
/*     */         
/*     */         long l = paramStopwatch.elapsed(TimeUnit.NANOSECONDS);
/*     */         paramStopwatch.reset();
/*     */         return l;
/*     */       };
/*  83 */     MetricSampler.ValueIncreasedByPercentage valueIncreasedByPercentage = new MetricSampler.ValueIncreasedByPercentage(2.0F);
/*     */     
/*  85 */     return MetricSampler.builder("ticktime", MetricCategory.TICK_LOOP, toDoubleFunction, stopwatch)
/*  86 */       .withBeforeTick(Stopwatch::start)
/*  87 */       .withThresholdAlert((MetricSampler.ThresholdTest)valueIncreasedByPercentage)
/*  88 */       .build();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class CpuStats
/*     */   {
/*  96 */     private final SystemInfo systemInfo = new SystemInfo();
/*  97 */     private final CentralProcessor processor = this.systemInfo.getHardware().getProcessor();
/*  98 */     public final int nrOfCpus = this.processor.getLogicalProcessorCount();
/*     */     
/* 100 */     private long[][] previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
/* 101 */     private double[] currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
/*     */     private long lastPollMs;
/*     */     
/*     */     public double loadForCpu(int param1Int) {
/* 105 */       long l = System.currentTimeMillis();
/* 106 */       if (this.lastPollMs == 0L || this.lastPollMs + 501L < l) {
/* 107 */         this.currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
/* 108 */         this.previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
/* 109 */         this.lastPollMs = l;
/*     */       } 
/*     */       
/* 112 */       return this.currentLoad[param1Int] * 100.0D;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\metrics\profiling\ServerMetricsSamplersProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */