/*    */ package net.minecraft.util.profiling.metrics;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.WeakHashMap;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class MetricsRegistry
/*    */ {
/* 12 */   public static final MetricsRegistry INSTANCE = new MetricsRegistry();
/*    */ 
/*    */   
/* 15 */   private final WeakHashMap<ProfilerMeasured, Void> measuredInstances = new WeakHashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void add(ProfilerMeasured paramProfilerMeasured) {
/* 21 */     this.measuredInstances.put(paramProfilerMeasured, null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<MetricSampler> getRegisteredSamplers() {
/* 27 */     Map<String, List<MetricSampler>> map = (Map)this.measuredInstances.keySet().stream().flatMap(paramProfilerMeasured -> paramProfilerMeasured.profiledMetrics().stream()).collect(Collectors.groupingBy(MetricSampler::getName));
/*    */     
/* 29 */     return aggregateDuplicates(map);
/*    */   }
/*    */   
/*    */   private static List<MetricSampler> aggregateDuplicates(Map<String, List<MetricSampler>> paramMap) {
/* 33 */     return (List<MetricSampler>)paramMap.entrySet().stream()
/* 34 */       .map(paramEntry -> {
/*    */           String str = (String)paramEntry.getKey();
/*    */           
/*    */           List<MetricSampler> list = (List)paramEntry.getValue();
/*    */           return (list.size() > 1) ? new AggregatedMetricSampler(str, list) : list.get(0);
/* 39 */         }).collect(Collectors.toList());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static class AggregatedMetricSampler
/*    */     extends MetricSampler
/*    */   {
/*    */     private final List<MetricSampler> delegates;
/*    */ 
/*    */     
/*    */     AggregatedMetricSampler(String param1String, List<MetricSampler> param1List) {
/* 51 */       super(param1String, ((MetricSampler)param1List.get(0)).getCategory(), () -> averageValueFromDelegates(param1List), () -> beforeTick(param1List), thresholdTest(param1List));
/* 52 */       this.delegates = param1List;
/*    */     }
/*    */     
/*    */     private static MetricSampler.ThresholdTest thresholdTest(List<MetricSampler> param1List) {
/* 56 */       return param1Double -> param1List.stream().anyMatch(());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     private static void beforeTick(List<MetricSampler> param1List) {
/* 65 */       for (MetricSampler metricSampler : param1List) {
/* 66 */         metricSampler.onStartTick();
/*    */       }
/*    */     }
/*    */     
/*    */     private static double averageValueFromDelegates(List<MetricSampler> param1List) {
/* 71 */       double d = 0.0D;
/*    */       
/* 73 */       for (MetricSampler metricSampler : param1List) {
/* 74 */         d += metricSampler.getSampler().getAsDouble();
/*    */       }
/*    */       
/* 77 */       return d / param1List.size();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean equals(Object param1Object) {
/* 82 */       if (this == param1Object) {
/* 83 */         return true;
/*    */       }
/* 85 */       if (param1Object == null || getClass() != param1Object.getClass()) {
/* 86 */         return false;
/*    */       }
/* 88 */       if (!super.equals(param1Object)) {
/* 89 */         return false;
/*    */       }
/* 91 */       AggregatedMetricSampler aggregatedMetricSampler = (AggregatedMetricSampler)param1Object;
/* 92 */       return this.delegates.equals(aggregatedMetricSampler.delegates);
/*    */     }
/*    */ 
/*    */     
/*    */     public int hashCode() {
/* 97 */       return Objects.hash(new Object[] { Integer.valueOf(super.hashCode()), this.delegates });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\metrics\MetricsRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */