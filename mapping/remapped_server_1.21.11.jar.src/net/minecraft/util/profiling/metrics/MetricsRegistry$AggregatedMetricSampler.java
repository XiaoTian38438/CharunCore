/*    */ package net.minecraft.util.profiling.metrics;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class AggregatedMetricSampler
/*    */   extends MetricSampler
/*    */ {
/*    */   private final List<MetricSampler> delegates;
/*    */   
/*    */   AggregatedMetricSampler(String paramString, List<MetricSampler> paramList) {
/* 51 */     super(paramString, ((MetricSampler)paramList.get(0)).getCategory(), () -> averageValueFromDelegates(paramList), () -> beforeTick(paramList), thresholdTest(paramList));
/* 52 */     this.delegates = paramList;
/*    */   }
/*    */   
/*    */   private static MetricSampler.ThresholdTest thresholdTest(List<MetricSampler> paramList) {
/* 56 */     return paramDouble -> paramList.stream().anyMatch(());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void beforeTick(List<MetricSampler> paramList) {
/* 65 */     for (MetricSampler metricSampler : paramList) {
/* 66 */       metricSampler.onStartTick();
/*    */     }
/*    */   }
/*    */   
/*    */   private static double averageValueFromDelegates(List<MetricSampler> paramList) {
/* 71 */     double d = 0.0D;
/*    */     
/* 73 */     for (MetricSampler metricSampler : paramList) {
/* 74 */       d += metricSampler.getSampler().getAsDouble();
/*    */     }
/*    */     
/* 77 */     return d / paramList.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 82 */     if (this == paramObject) {
/* 83 */       return true;
/*    */     }
/* 85 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 86 */       return false;
/*    */     }
/* 88 */     if (!super.equals(paramObject)) {
/* 89 */       return false;
/*    */     }
/* 91 */     AggregatedMetricSampler aggregatedMetricSampler = (AggregatedMetricSampler)paramObject;
/* 92 */     return this.delegates.equals(aggregatedMetricSampler.delegates);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 97 */     return Objects.hash(new Object[] { Integer.valueOf(super.hashCode()), this.delegates });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\metrics\MetricsRegistry$AggregatedMetricSampler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */