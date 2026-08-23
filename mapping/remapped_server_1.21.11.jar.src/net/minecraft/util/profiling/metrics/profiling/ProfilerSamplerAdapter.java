/*    */ package net.minecraft.util.profiling.metrics.profiling;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*    */ import java.util.Set;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.TimeUtil;
/*    */ import net.minecraft.util.profiling.ActiveProfiler;
/*    */ import net.minecraft.util.profiling.ProfileCollector;
/*    */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*    */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*    */ import org.apache.commons.lang3.tuple.Pair;
/*    */ 
/*    */ public class ProfilerSamplerAdapter {
/* 15 */   private final Set<String> previouslyFoundSamplerNames = (Set<String>)new ObjectOpenHashSet();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Set<MetricSampler> newSamplersFoundInProfiler(Supplier<ProfileCollector> paramSupplier) {
/* 21 */     Set<MetricSampler> set = (Set)((ProfileCollector)paramSupplier.get()).getChartedPaths().stream().filter(paramPair -> !this.previouslyFoundSamplerNames.contains(paramPair.getLeft())).map(paramPair -> samplerForProfilingPath(paramSupplier, (String)paramPair.getLeft(), (MetricCategory)paramPair.getRight())).collect(Collectors.toSet());
/*    */     
/* 23 */     for (MetricSampler metricSampler : set) {
/* 24 */       this.previouslyFoundSamplerNames.add(metricSampler.getName());
/*    */     }
/*    */     
/* 27 */     return set;
/*    */   }
/*    */   
/*    */   private static MetricSampler samplerForProfilingPath(Supplier<ProfileCollector> paramSupplier, String paramString, MetricCategory paramMetricCategory) {
/* 31 */     return MetricSampler.create(paramString, paramMetricCategory, () -> {
/*    */           ActiveProfiler.PathEntry pathEntry = ((ProfileCollector)paramSupplier.get()).getEntry(paramString);
/*    */           return (pathEntry == null) ? 0.0D : (pathEntry.getMaxDuration() / TimeUtil.NANOSECONDS_PER_MILLISECOND);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\metrics\profiling\ProfilerSamplerAdapter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */