/*     */ package net.minecraft.util.profiling.metrics.profiling;
/*     */ 
/*     */ import oshi.SystemInfo;
/*     */ import oshi.hardware.CentralProcessor;
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
/*     */ class CpuStats
/*     */ {
/*  96 */   private final SystemInfo systemInfo = new SystemInfo();
/*  97 */   private final CentralProcessor processor = this.systemInfo.getHardware().getProcessor();
/*  98 */   public final int nrOfCpus = this.processor.getLogicalProcessorCount();
/*     */   
/* 100 */   private long[][] previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
/* 101 */   private double[] currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
/*     */   private long lastPollMs;
/*     */   
/*     */   public double loadForCpu(int paramInt) {
/* 105 */     long l = System.currentTimeMillis();
/* 106 */     if (this.lastPollMs == 0L || this.lastPollMs + 501L < l) {
/* 107 */       this.currentLoad = this.processor.getProcessorCpuLoadBetweenTicks(this.previousCpuLoadTick);
/* 108 */       this.previousCpuLoadTick = this.processor.getProcessorCpuLoadTicks();
/* 109 */       this.lastPollMs = l;
/*     */     } 
/*     */     
/* 112 */     return this.currentLoad[paramInt] * 100.0D;
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\metrics\profiling\ServerMetricsSamplersProvider$CpuStats.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */