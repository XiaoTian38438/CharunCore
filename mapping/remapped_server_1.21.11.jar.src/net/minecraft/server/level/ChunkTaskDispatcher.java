/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.IntConsumer;
/*     */ import java.util.function.IntSupplier;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.util.Unit;
/*     */ import net.minecraft.util.thread.PriorityConsecutiveExecutor;
/*     */ import net.minecraft.util.thread.StrictQueue;
/*     */ import net.minecraft.util.thread.TaskScheduler;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChunkTaskDispatcher
/*     */   implements ChunkHolder.LevelChangeListener, AutoCloseable
/*     */ {
/*     */   public static final int DISPATCHER_PRIORITY_COUNT = 4;
/*  20 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private final ChunkTaskPriorityQueue queue;
/*     */   private final TaskScheduler<Runnable> executor;
/*     */   private final PriorityConsecutiveExecutor dispatcher;
/*     */   protected boolean sleeping;
/*     */   
/*     */   public ChunkTaskDispatcher(TaskScheduler<Runnable> paramTaskScheduler, Executor paramExecutor) {
/*  27 */     this.queue = new ChunkTaskPriorityQueue(paramTaskScheduler.name() + "_queue");
/*  28 */     this.executor = paramTaskScheduler;
/*  29 */     this.dispatcher = new PriorityConsecutiveExecutor(4, paramExecutor, "dispatcher");
/*  30 */     this.sleeping = true;
/*     */   }
/*     */   
/*     */   public boolean hasWork() {
/*  34 */     return (this.dispatcher.hasWork() || this.queue.hasWork());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLevelChange(ChunkPos paramChunkPos, IntSupplier paramIntSupplier, int paramInt, IntConsumer paramIntConsumer) {
/*  39 */     this.dispatcher.schedule((Runnable)new StrictQueue.RunnableWithPriority(0, () -> {
/*     */             int i = paramIntSupplier.getAsInt();
/*     */             if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/*     */               LOGGER.debug("RES {} {} -> {}", new Object[] { paramChunkPos, Integer.valueOf(i), Integer.valueOf(paramInt) });
/*     */             }
/*     */             this.queue.resortChunkTasks(i, paramChunkPos, paramInt);
/*     */             paramIntConsumer.accept(paramInt);
/*     */           }));
/*     */   }
/*     */   
/*     */   public void release(long paramLong, Runnable paramRunnable, boolean paramBoolean) {
/*  50 */     this.dispatcher.schedule((Runnable)new StrictQueue.RunnableWithPriority(1, () -> {
/*     */             this.queue.release(paramLong, paramBoolean);
/*     */             onRelease(paramLong);
/*     */             if (this.sleeping) {
/*     */               this.sleeping = false;
/*     */               pollTask();
/*     */             } 
/*     */             paramRunnable.run();
/*     */           }));
/*     */   }
/*     */   
/*     */   public void submit(Runnable paramRunnable, long paramLong, IntSupplier paramIntSupplier) {
/*  62 */     this.dispatcher.schedule((Runnable)new StrictQueue.RunnableWithPriority(2, () -> {
/*     */             int i = paramIntSupplier.getAsInt();
/*     */             if (SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/*     */               LOGGER.debug("SUB {} {} {} {}", new Object[] { new ChunkPos(paramLong), Integer.valueOf(i), this.executor, this.queue });
/*     */             }
/*     */             this.queue.submit(paramRunnable, paramLong, i);
/*     */             if (this.sleeping) {
/*     */               this.sleeping = false;
/*     */               pollTask();
/*     */             } 
/*     */           }));
/*     */   }
/*     */   
/*     */   protected void pollTask() {
/*  76 */     this.dispatcher.schedule((Runnable)new StrictQueue.RunnableWithPriority(3, () -> {
/*     */             ChunkTaskPriorityQueue.TasksForChunk tasksForChunk = popTasks();
/*     */             if (tasksForChunk == null) {
/*     */               this.sleeping = true;
/*     */             } else {
/*     */               scheduleForExecution(tasksForChunk);
/*     */             } 
/*     */           }));
/*     */   }
/*     */   
/*     */   protected void scheduleForExecution(ChunkTaskPriorityQueue.TasksForChunk paramTasksForChunk) {
/*  87 */     CompletableFuture.allOf((CompletableFuture<?>[])paramTasksForChunk.tasks().stream().map(paramRunnable -> this.executor.scheduleWithResult(()))
/*     */ 
/*     */         
/*  90 */         .toArray(paramInt -> new CompletableFuture[paramInt])).thenAccept(paramVoid -> pollTask());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onRelease(long paramLong) {}
/*     */   
/*     */   protected ChunkTaskPriorityQueue.TasksForChunk popTasks() {
/*  97 */     return this.queue.pop();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 102 */     this.executor.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\ChunkTaskDispatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */