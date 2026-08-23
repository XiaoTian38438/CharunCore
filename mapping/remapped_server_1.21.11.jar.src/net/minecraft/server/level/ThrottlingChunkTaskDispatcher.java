/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*    */ import it.unimi.dsi.fastutil.longs.LongSet;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.thread.TaskScheduler;
/*    */ 
/*    */ 
/*    */ public class ThrottlingChunkTaskDispatcher
/*    */   extends ChunkTaskDispatcher
/*    */ {
/* 14 */   private final LongSet chunkPositionsInExecution = (LongSet)new LongOpenHashSet();
/*    */   private final int maxChunksInExecution;
/*    */   private final String executorSchedulerName;
/*    */   
/*    */   public ThrottlingChunkTaskDispatcher(TaskScheduler<Runnable> paramTaskScheduler, Executor paramExecutor, int paramInt) {
/* 19 */     super(paramTaskScheduler, paramExecutor);
/* 20 */     this.maxChunksInExecution = paramInt;
/* 21 */     this.executorSchedulerName = paramTaskScheduler.name();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onRelease(long paramLong) {
/* 26 */     this.chunkPositionsInExecution.remove(paramLong);
/*    */   }
/*    */ 
/*    */   
/*    */   protected ChunkTaskPriorityQueue.TasksForChunk popTasks() {
/* 31 */     return (this.chunkPositionsInExecution.size() < this.maxChunksInExecution) ? super.popTasks() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void scheduleForExecution(ChunkTaskPriorityQueue.TasksForChunk paramTasksForChunk) {
/* 36 */     this.chunkPositionsInExecution.add(paramTasksForChunk.chunkPos());
/* 37 */     super.scheduleForExecution(paramTasksForChunk);
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public String getDebugStatus() {
/* 42 */     return this.executorSchedulerName + "=[" + this.executorSchedulerName + "], s=" + (String)this.chunkPositionsInExecution.longStream().<CharSequence>mapToObj(paramLong -> "" + paramLong + ":" + paramLong).collect(Collectors.joining(","));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\level\ThrottlingChunkTaskDispatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */