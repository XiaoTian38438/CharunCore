/*    */ package net.minecraft.util.profiling.metrics.profiling;
/*    */ 
/*    */ import net.minecraft.util.profiling.InactiveProfiler;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ 
/*    */ public class InactiveMetricsRecorder implements MetricsRecorder {
/*  7 */   public static final MetricsRecorder INSTANCE = new InactiveMetricsRecorder();
/*    */ 
/*    */ 
/*    */   
/*    */   public void end() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void cancel() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void startTick() {}
/*    */ 
/*    */   
/*    */   public boolean isRecording() {
/* 23 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public ProfilerFiller getProfiler() {
/* 28 */     return (ProfilerFiller)InactiveProfiler.INSTANCE;
/*    */   }
/*    */   
/*    */   public void endTick() {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\metrics\profiling\InactiveMetricsRecorder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */