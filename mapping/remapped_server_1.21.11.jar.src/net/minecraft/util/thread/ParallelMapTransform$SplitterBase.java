/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiFunction;
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
/*     */ abstract class SplitterBase<K, U, V>
/*     */ {
/*     */   private int lastScheduledIndex;
/*     */   private int currentIndex;
/*     */   private final CompletableFuture<?>[] tasks;
/*     */   private int batchIndex;
/*     */   private final ParallelMapTransform.Container<K, U, V> container;
/*     */   
/*     */   SplitterBase(BiFunction<K, U, V> paramBiFunction, int paramInt1, int paramInt2) {
/* 117 */     this.container = new ParallelMapTransform.Container<>(paramBiFunction, paramInt1);
/* 118 */     this.tasks = (CompletableFuture<?>[])new CompletableFuture[paramInt2];
/*     */   }
/*     */   
/*     */   private int pendingBatchSize() {
/* 122 */     return this.currentIndex - this.lastScheduledIndex;
/*     */   }
/*     */   
/*     */   public CompletableFuture<Map<K, V>> scheduleTasks(Map<K, U> paramMap, Executor paramExecutor) {
/* 126 */     paramMap.forEach((paramObject1, paramObject2) -> {
/*     */           this.container.put(this.currentIndex++, (K)paramObject1, (U)paramObject2);
/*     */           
/*     */           if (pendingBatchSize() == batchSize(this.batchIndex)) {
/*     */             this.tasks[this.batchIndex++] = scheduleBatch(this.container, this.lastScheduledIndex, this.currentIndex, paramExecutor);
/*     */             this.lastScheduledIndex = this.currentIndex;
/*     */           } 
/*     */         });
/* 134 */     assert this.currentIndex == this.container.size();
/* 135 */     assert this.lastScheduledIndex == this.currentIndex;
/* 136 */     assert this.batchIndex == this.tasks.length;
/*     */     
/* 138 */     return scheduleFinalOperation(CompletableFuture.allOf(this.tasks), this.container);
/*     */   }
/*     */   
/*     */   protected abstract int batchSize(int paramInt);
/*     */   
/*     */   protected abstract CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> paramContainer, int paramInt1, int paramInt2, Executor paramExecutor);
/*     */   
/*     */   protected abstract CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> paramCompletableFuture, ParallelMapTransform.Container<K, U, V> paramContainer);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\thread\ParallelMapTransform$SplitterBase.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */