/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.io.File;
/*    */ import java.util.function.LongSupplier;
/*    */ import net.minecraft.SharedConstants;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public class SingleTickProfiler
/*    */ {
/* 13 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   private final LongSupplier realTime;
/*    */   private final long saveThreshold;
/*    */   private int tick;
/*    */   private final File location;
/* 18 */   private ProfileCollector profiler = InactiveProfiler.INSTANCE;
/*    */   
/*    */   public SingleTickProfiler(LongSupplier paramLongSupplier, String paramString, long paramLong) {
/* 21 */     this.realTime = paramLongSupplier;
/* 22 */     this.location = new File("debug", paramString);
/* 23 */     this.saveThreshold = paramLong;
/*    */   }
/*    */   
/*    */   public ProfilerFiller startTick() {
/* 27 */     this.profiler = new ActiveProfiler(this.realTime, () -> this.tick, () -> true);
/* 28 */     this.tick++;
/* 29 */     return this.profiler;
/*    */   }
/*    */   
/*    */   public void endTick() {
/* 33 */     if (this.profiler == InactiveProfiler.INSTANCE) {
/*    */       return;
/*    */     }
/*    */     
/* 37 */     ProfileResults profileResults = this.profiler.getResults();
/* 38 */     this.profiler = InactiveProfiler.INSTANCE;
/*    */     
/* 40 */     if (profileResults.getNanoDuration() >= this.saveThreshold) {
/* 41 */       File file = new File(this.location, "tick-results-" + Util.getFilenameFormattedDateTime() + ".txt");
/* 42 */       profileResults.saveResults(file.toPath());
/* 43 */       LOGGER.info("Recorded long tick -- wrote info to: {}", file.getAbsolutePath());
/*    */     } 
/*    */   }
/*    */   
/*    */   public static SingleTickProfiler createTickProfiler(String paramString) {
/* 48 */     if (SharedConstants.DEBUG_MONITOR_TICK_TIMES) {
/* 49 */       return new SingleTickProfiler((LongSupplier)Util.timeSource, paramString, SharedConstants.MAXIMUM_TICK_TIME_NANOS);
/*    */     }
/* 51 */     return null;
/*    */   }
/*    */   
/*    */   public static ProfilerFiller decorateFiller(ProfilerFiller paramProfilerFiller, SingleTickProfiler paramSingleTickProfiler) {
/* 55 */     if (paramSingleTickProfiler != null) {
/* 56 */       return ProfilerFiller.combine(paramSingleTickProfiler.startTick(), paramProfilerFiller);
/*    */     }
/* 58 */     return paramProfilerFiller;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\SingleTickProfiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */