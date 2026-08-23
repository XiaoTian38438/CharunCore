/*     */ package net.minecraft.util.profiling.metrics;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.DoubleSupplier;
/*     */ import java.util.function.ToDoubleFunction;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetricSamplerBuilder<T>
/*     */ {
/*     */   private final String name;
/*     */   private final MetricCategory category;
/*     */   private final DoubleSupplier sampler;
/*     */   private final T context;
/*     */   private Runnable beforeTick;
/*     */   private MetricSampler.ThresholdTest thresholdTest;
/*     */   
/*     */   public MetricSamplerBuilder(String paramString, MetricCategory paramMetricCategory, ToDoubleFunction<T> paramToDoubleFunction, T paramT) {
/* 199 */     this.name = paramString;
/* 200 */     this.category = paramMetricCategory;
/* 201 */     this.sampler = (() -> paramToDoubleFunction.applyAsDouble(paramObject));
/* 202 */     this.context = paramT;
/*     */   }
/*     */   
/*     */   public MetricSamplerBuilder<T> withBeforeTick(Consumer<T> paramConsumer) {
/* 206 */     this.beforeTick = (() -> paramConsumer.accept(this.context));
/* 207 */     return this;
/*     */   }
/*     */   
/*     */   public MetricSamplerBuilder<T> withThresholdAlert(MetricSampler.ThresholdTest paramThresholdTest) {
/* 211 */     this.thresholdTest = paramThresholdTest;
/* 212 */     return this;
/*     */   }
/*     */   
/*     */   public MetricSampler build() {
/* 216 */     return new MetricSampler(this.name, this.category, this.sampler, this.beforeTick, this.thresholdTest);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\metrics\MetricSampler$MetricSamplerBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */