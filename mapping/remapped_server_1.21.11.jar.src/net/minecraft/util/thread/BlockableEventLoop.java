/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.mojang.jtracy.TracyClient;
/*     */ import com.mojang.jtracy.Zone;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.locks.LockSupport;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*     */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*     */ import net.minecraft.util.profiling.metrics.MetricsRegistry;
/*     */ import net.minecraft.util.profiling.metrics.ProfilerMeasured;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class BlockableEventLoop<R extends Runnable>
/*     */   implements ProfilerMeasured, TaskScheduler<R>, Executor
/*     */ {
/*     */   public static final long BLOCK_TIME_NANOS = 100000L;
/*     */   private final String name;
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  31 */   private final Queue<R> pendingRunnables = Queues.newConcurrentLinkedQueue();
/*     */   private int blockingCount;
/*     */   
/*     */   protected BlockableEventLoop(String paramString) {
/*  35 */     this.name = paramString;
/*  36 */     MetricsRegistry.INSTANCE.add(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSameThread() {
/*  42 */     return (Thread.currentThread() == getRunningThread());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean scheduleExecutables() {
/*  48 */     return !isSameThread();
/*     */   }
/*     */   
/*     */   public int getPendingTasksCount() {
/*  52 */     return this.pendingRunnables.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/*  57 */     return this.name;
/*     */   }
/*     */   
/*     */   public <V> CompletableFuture<V> submit(Supplier<V> paramSupplier) {
/*  61 */     if (scheduleExecutables()) {
/*  62 */       return CompletableFuture.supplyAsync(paramSupplier, this);
/*     */     }
/*  64 */     return CompletableFuture.completedFuture(paramSupplier.get());
/*     */   }
/*     */ 
/*     */   
/*     */   private CompletableFuture<Void> submitAsync(Runnable paramRunnable) {
/*  69 */     return CompletableFuture.supplyAsync(() -> { paramRunnable.run(); return null; }this);
/*     */   }
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
/*     */   @CheckReturnValue
/*     */   public CompletableFuture<Void> submit(Runnable paramRunnable) {
/*  83 */     if (scheduleExecutables()) {
/*  84 */       return submitAsync(paramRunnable);
/*     */     }
/*  86 */     paramRunnable.run();
/*  87 */     return CompletableFuture.completedFuture(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void executeBlocking(Runnable paramRunnable) {
/*  92 */     if (!isSameThread()) {
/*  93 */       submitAsync(paramRunnable).join();
/*     */     } else {
/*  95 */       paramRunnable.run();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void schedule(R paramR) {
/* 101 */     this.pendingRunnables.add(paramR);
/* 102 */     LockSupport.unpark(getRunningThread());
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(Runnable paramRunnable) {
/* 107 */     R r = wrapRunnable(paramRunnable);
/* 108 */     if (scheduleExecutables()) {
/* 109 */       schedule(r);
/*     */     } else {
/* 111 */       doRunTask(r);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void executeIfPossible(Runnable paramRunnable) {
/* 116 */     execute(paramRunnable);
/*     */   }
/*     */   
/*     */   protected void dropAllTasks() {
/* 120 */     this.pendingRunnables.clear();
/*     */   }
/*     */   
/*     */   protected void runAllTasks() {
/* 124 */     while (pollTask());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean shouldRunAllTasks() {
/* 130 */     return (this.blockingCount > 0);
/*     */   }
/*     */   
/*     */   public boolean pollTask() {
/* 134 */     Runnable runnable = (Runnable)this.pendingRunnables.peek();
/* 135 */     if (runnable == null) {
/* 136 */       return false;
/*     */     }
/*     */     
/* 139 */     if (!shouldRunAllTasks() && !shouldRun((R)runnable)) {
/* 140 */       return false;
/*     */     }
/*     */     
/* 143 */     doRunTask(this.pendingRunnables.remove());
/*     */     
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   public void managedBlock(BooleanSupplier paramBooleanSupplier) {
/* 149 */     this.blockingCount++;
/*     */     try {
/* 151 */       while (!paramBooleanSupplier.getAsBoolean()) {
/* 152 */         if (!pollTask())
/*     */         {
/* 154 */           waitForTasks();
/*     */         }
/*     */       } 
/*     */     } finally {
/* 158 */       this.blockingCount--;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void waitForTasks() {
/* 163 */     Thread.yield();
/* 164 */     LockSupport.parkNanos("waiting for tasks", 100000L);
/*     */   }
/*     */   protected void doRunTask(R paramR) {
/*     */     
/* 168 */     try { Zone zone = TracyClient.beginZone("Task", SharedConstants.IS_RUNNING_IN_IDE); 
/* 169 */       try { paramR.run();
/* 170 */         if (zone != null) zone.close();  } catch (Throwable throwable) { if (zone != null) try { zone.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception exception)
/* 171 */     { LOGGER.error(LogUtils.FATAL_MARKER, "Error executing task on {}", name(), exception);
/* 172 */       if (isNonRecoverable(exception)) {
/* 173 */         throw exception;
/*     */       } }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MetricSampler> profiledMetrics() {
/* 180 */     return (List<MetricSampler>)ImmutableList.of(
/* 181 */         MetricSampler.create(this.name + "-pending-tasks", MetricCategory.EVENT_LOOPS, this::getPendingTasksCount));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isNonRecoverable(Throwable paramThrowable) {
/* 186 */     if (paramThrowable instanceof ReportedException) { ReportedException reportedException = (ReportedException)paramThrowable;
/* 187 */       return isNonRecoverable(reportedException.getCause()); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 192 */     return (paramThrowable instanceof OutOfMemoryError || paramThrowable instanceof StackOverflowError);
/*     */   }
/*     */   
/*     */   protected abstract boolean shouldRun(R paramR);
/*     */   
/*     */   protected abstract Thread getRunningThread();
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\thread\BlockableEventLoop.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */