/*     */ package net.minecraft.util.profiling.metrics;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import it.unimi.dsi.fastutil.ints.Int2DoubleMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2DoubleOpenHashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.DoubleSupplier;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetricSampler
/*     */ {
/*     */   private final String name;
/*     */   private final MetricCategory category;
/*     */   private final DoubleSupplier sampler;
/*     */   private final ByteBuf ticks;
/*     */   private final ByteBuf values;
/*     */   private volatile boolean isRunning;
/*     */   private final Runnable beforeTick;
/*     */   final ThresholdTest thresholdTest;
/*     */   private double currentValue;
/*     */   
/*     */   protected MetricSampler(String paramString, MetricCategory paramMetricCategory, DoubleSupplier paramDoubleSupplier, Runnable paramRunnable, ThresholdTest paramThresholdTest) {
/*  29 */     this.name = paramString;
/*  30 */     this.category = paramMetricCategory;
/*  31 */     this.beforeTick = paramRunnable;
/*  32 */     this.sampler = paramDoubleSupplier;
/*  33 */     this.thresholdTest = paramThresholdTest;
/*  34 */     this.values = ByteBufAllocator.DEFAULT.buffer();
/*  35 */     this.ticks = ByteBufAllocator.DEFAULT.buffer();
/*  36 */     this.isRunning = true;
/*     */   }
/*     */   
/*     */   public static MetricSampler create(String paramString, MetricCategory paramMetricCategory, DoubleSupplier paramDoubleSupplier) {
/*  40 */     return new MetricSampler(paramString, paramMetricCategory, paramDoubleSupplier, null, null);
/*     */   }
/*     */   
/*     */   public static <T> MetricSampler create(String paramString, MetricCategory paramMetricCategory, T paramT, ToDoubleFunction<T> paramToDoubleFunction) {
/*  44 */     return builder(paramString, paramMetricCategory, paramToDoubleFunction, paramT).build();
/*     */   }
/*     */   
/*     */   public static <T> MetricSamplerBuilder<T> builder(String paramString, MetricCategory paramMetricCategory, ToDoubleFunction<T> paramToDoubleFunction, T paramT) {
/*  48 */     if (paramToDoubleFunction == null) {
/*  49 */       throw new IllegalStateException();
/*     */     }
/*  51 */     return new MetricSamplerBuilder<>(paramString, paramMetricCategory, paramToDoubleFunction, paramT);
/*     */   }
/*     */   
/*     */   public void onStartTick() {
/*  55 */     if (!this.isRunning) {
/*  56 */       throw new IllegalStateException("Not running");
/*     */     }
/*  58 */     if (this.beforeTick != null) {
/*  59 */       this.beforeTick.run();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onEndTick(int paramInt) {
/*  70 */     verifyRunning();
/*  71 */     this.currentValue = this.sampler.getAsDouble();
/*  72 */     this.values.writeDouble(this.currentValue);
/*  73 */     this.ticks.writeInt(paramInt);
/*     */   }
/*     */   
/*     */   public void onFinished() {
/*  77 */     verifyRunning();
/*  78 */     this.values.release();
/*  79 */     this.ticks.release();
/*  80 */     this.isRunning = false;
/*     */   }
/*     */   
/*     */   private void verifyRunning() {
/*  84 */     if (!this.isRunning) {
/*  85 */       throw new IllegalStateException(String.format(Locale.ROOT, "Sampler for metric %s not started!", new Object[] { this.name }));
/*     */     }
/*     */   }
/*     */   
/*     */   DoubleSupplier getSampler() {
/*  90 */     return this.sampler;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  94 */     return this.name;
/*     */   }
/*     */   
/*     */   public MetricCategory getCategory() {
/*  98 */     return this.category;
/*     */   }
/*     */   
/*     */   public SamplerResult result() {
/* 102 */     Int2DoubleOpenHashMap int2DoubleOpenHashMap = new Int2DoubleOpenHashMap();
/* 103 */     int i = Integer.MIN_VALUE;
/* 104 */     int j = Integer.MIN_VALUE;
/*     */     
/* 106 */     while (this.values.isReadable(8)) {
/* 107 */       int k = this.ticks.readInt();
/* 108 */       if (i == Integer.MIN_VALUE) {
/* 109 */         i = k;
/*     */       }
/* 111 */       int2DoubleOpenHashMap.put(k, this.values.readDouble());
/* 112 */       j = k;
/*     */     } 
/* 114 */     return new SamplerResult(i, j, (Int2DoubleMap)int2DoubleOpenHashMap);
/*     */   }
/*     */   
/*     */   public boolean triggersThreshold() {
/* 118 */     return (this.thresholdTest != null && this.thresholdTest.test(this.currentValue));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 123 */     if (this == paramObject) {
/* 124 */       return true;
/*     */     }
/* 126 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 127 */       return false;
/*     */     }
/* 129 */     MetricSampler metricSampler = (MetricSampler)paramObject;
/* 130 */     return (this.name.equals(metricSampler.name) && this.category.equals(metricSampler.category));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 135 */     return this.name.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class SamplerResult
/*     */   {
/*     */     private final Int2DoubleMap recording;
/*     */     
/*     */     private final int firstTick;
/*     */     
/*     */     private final int lastTick;
/*     */     
/*     */     public SamplerResult(int param1Int1, int param1Int2, Int2DoubleMap param1Int2DoubleMap) {
/* 148 */       this.firstTick = param1Int1;
/* 149 */       this.lastTick = param1Int2;
/* 150 */       this.recording = param1Int2DoubleMap;
/*     */     }
/*     */     
/*     */     public double valueAtTick(int param1Int) {
/* 154 */       return this.recording.get(param1Int);
/*     */     }
/*     */     
/*     */     public int getFirstTick() {
/* 158 */       return this.firstTick;
/*     */     }
/*     */     
/*     */     public int getLastTick() {
/* 162 */       return this.lastTick;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ValueIncreasedByPercentage implements ThresholdTest {
/*     */     private final float percentageIncreaseThreshold;
/* 168 */     private double previousValue = Double.MIN_VALUE;
/*     */     
/*     */     public ValueIncreasedByPercentage(float param1Float) {
/* 171 */       this.percentageIncreaseThreshold = param1Float;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean test(double param1Double) {
/*     */       boolean bool;
/* 178 */       if (this.previousValue == Double.MIN_VALUE || param1Double <= this.previousValue) {
/* 179 */         bool = false;
/*     */       } else {
/* 181 */         bool = ((param1Double - this.previousValue) / this.previousValue >= this.percentageIncreaseThreshold) ? true : false;
/*     */       } 
/*     */       
/* 184 */       this.previousValue = param1Double;
/* 185 */       return bool;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class MetricSamplerBuilder<T>
/*     */   {
/*     */     private final String name;
/*     */     private final MetricCategory category;
/*     */     private final DoubleSupplier sampler;
/*     */     private final T context;
/*     */     private Runnable beforeTick;
/*     */     private MetricSampler.ThresholdTest thresholdTest;
/*     */     
/*     */     public MetricSamplerBuilder(String param1String, MetricCategory param1MetricCategory, ToDoubleFunction<T> param1ToDoubleFunction, T param1T) {
/* 199 */       this.name = param1String;
/* 200 */       this.category = param1MetricCategory;
/* 201 */       this.sampler = (() -> param1ToDoubleFunction.applyAsDouble(param1Object));
/* 202 */       this.context = param1T;
/*     */     }
/*     */     
/*     */     public MetricSamplerBuilder<T> withBeforeTick(Consumer<T> param1Consumer) {
/* 206 */       this.beforeTick = (() -> param1Consumer.accept(this.context));
/* 207 */       return this;
/*     */     }
/*     */     
/*     */     public MetricSamplerBuilder<T> withThresholdAlert(MetricSampler.ThresholdTest param1ThresholdTest) {
/* 211 */       this.thresholdTest = param1ThresholdTest;
/* 212 */       return this;
/*     */     }
/*     */     
/*     */     public MetricSampler build() {
/* 216 */       return new MetricSampler(this.name, this.category, this.sampler, this.beforeTick, this.thresholdTest);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface ThresholdTest {
/*     */     boolean test(double param1Double);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\metrics\MetricSampler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */