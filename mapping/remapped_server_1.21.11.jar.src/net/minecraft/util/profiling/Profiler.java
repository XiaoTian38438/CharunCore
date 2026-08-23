/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import com.mojang.jtracy.TracyClient;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public final class Profiler
/*    */ {
/* 10 */   private static final ThreadLocal<TracyZoneFiller> TRACY_FILLER = ThreadLocal.withInitial(TracyZoneFiller::new);
/*    */   
/* 12 */   private static final ThreadLocal<ProfilerFiller> ACTIVE = new ThreadLocal<>();
/* 13 */   private static final AtomicInteger ACTIVE_COUNT = new AtomicInteger();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Scope use(ProfilerFiller paramProfilerFiller) {
/* 19 */     startUsing(paramProfilerFiller);
/* 20 */     return Profiler::stopUsing;
/*    */   }
/*    */   
/*    */   private static void startUsing(ProfilerFiller paramProfilerFiller) {
/* 24 */     if (ACTIVE.get() != null) {
/* 25 */       throw new IllegalStateException("Profiler is already active");
/*    */     }
/* 27 */     ProfilerFiller profilerFiller = decorateFiller(paramProfilerFiller);
/* 28 */     ACTIVE.set(profilerFiller);
/* 29 */     ACTIVE_COUNT.incrementAndGet();
/* 30 */     profilerFiller.startTick();
/*    */   }
/*    */   
/*    */   private static void stopUsing() {
/* 34 */     ProfilerFiller profilerFiller = ACTIVE.get();
/* 35 */     if (profilerFiller == null) {
/* 36 */       throw new IllegalStateException("Profiler was not active");
/*    */     }
/* 38 */     ACTIVE.remove();
/* 39 */     ACTIVE_COUNT.decrementAndGet();
/* 40 */     profilerFiller.endTick();
/*    */   }
/*    */   
/*    */   private static ProfilerFiller decorateFiller(ProfilerFiller paramProfilerFiller) {
/* 44 */     return ProfilerFiller.combine(getDefaultFiller(), paramProfilerFiller);
/*    */   }
/*    */   
/*    */   public static ProfilerFiller get() {
/* 48 */     if (ACTIVE_COUNT.get() == 0)
/*    */     {
/* 50 */       return getDefaultFiller();
/*    */     }
/* 52 */     return Objects.<ProfilerFiller>requireNonNullElseGet(ACTIVE.get(), Profiler::getDefaultFiller);
/*    */   }
/*    */   
/*    */   private static ProfilerFiller getDefaultFiller() {
/* 56 */     if (TracyClient.isAvailable()) {
/* 57 */       return TRACY_FILLER.get();
/*    */     }
/* 59 */     return InactiveProfiler.INSTANCE;
/*    */   }
/*    */   
/*    */   public static interface Scope extends AutoCloseable {
/*    */     void close();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\profiling\Profiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */