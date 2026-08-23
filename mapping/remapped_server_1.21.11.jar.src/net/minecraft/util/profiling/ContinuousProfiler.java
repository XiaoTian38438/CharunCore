/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import java.util.function.BooleanSupplier;
/*    */ import java.util.function.IntSupplier;
/*    */ import java.util.function.LongSupplier;
/*    */ 
/*    */ public class ContinuousProfiler {
/*    */   private final LongSupplier realTime;
/*    */   private final IntSupplier tickCount;
/*    */   private final BooleanSupplier suppressWarnings;
/* 11 */   private ProfileCollector profiler = InactiveProfiler.INSTANCE;
/*    */   
/*    */   public ContinuousProfiler(LongSupplier paramLongSupplier, IntSupplier paramIntSupplier, BooleanSupplier paramBooleanSupplier) {
/* 14 */     this.realTime = paramLongSupplier;
/* 15 */     this.tickCount = paramIntSupplier;
/* 16 */     this.suppressWarnings = paramBooleanSupplier;
/*    */   }
/*    */   
/*    */   public boolean isEnabled() {
/* 20 */     return (this.profiler != InactiveProfiler.INSTANCE);
/*    */   }
/*    */   
/*    */   public void disable() {
/* 24 */     this.profiler = InactiveProfiler.INSTANCE;
/*    */   }
/*    */   
/*    */   public void enable() {
/* 28 */     this.profiler = new ActiveProfiler(this.realTime, this.tickCount, this.suppressWarnings);
/*    */   }
/*    */   
/*    */   public ProfilerFiller getFiller() {
/* 32 */     return this.profiler;
/*    */   }
/*    */   
/*    */   public ProfileResults getResults() {
/* 36 */     return this.profiler.getResults();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\ContinuousProfiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */