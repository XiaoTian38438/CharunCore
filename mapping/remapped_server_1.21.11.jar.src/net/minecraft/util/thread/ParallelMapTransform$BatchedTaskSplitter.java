/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.util.Mth;
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
/*     */ class BatchedTaskSplitter<K, U, V>
/*     */   extends ParallelMapTransform.SplitterBase<K, U, V>
/*     */ {
/*     */   private final Map<K, V> result;
/*     */   private final int batchSize;
/*     */   private final int firstUndersizedBatchIndex;
/*     */   
/*     */   BatchedTaskSplitter(BiFunction<K, U, V> paramBiFunction, int paramInt1, int paramInt2) {
/* 190 */     super(paramBiFunction, paramInt1, paramInt2);
/* 191 */     this.result = new HashMap<>(paramInt1);
/* 192 */     this.batchSize = Mth.positiveCeilDiv(paramInt1, paramInt2);
/*     */     
/* 194 */     int i = this.batchSize * paramInt2;
/* 195 */     int j = i - paramInt1;
/*     */ 
/*     */     
/* 198 */     this.firstUndersizedBatchIndex = paramInt2 - j;
/* 199 */     assert this.firstUndersizedBatchIndex > 0 && this.firstUndersizedBatchIndex <= paramInt2;
/*     */   }
/*     */ 
/*     */   
/*     */   protected CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> paramContainer, int paramInt1, int paramInt2, Executor paramExecutor) {
/* 204 */     int i = paramInt2 - paramInt1;
/*     */     
/* 206 */     assert i == this.batchSize || i == this.batchSize - 1;
/* 207 */     return CompletableFuture.runAsync(createTask(this.result, paramInt1, paramInt2, paramContainer), paramExecutor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int batchSize(int paramInt) {
/* 212 */     return (paramInt < this.firstUndersizedBatchIndex) ? this.batchSize : (this.batchSize - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <K, U, V> Runnable createTask(Map<K, V> paramMap, int paramInt1, int paramInt2, ParallelMapTransform.Container<K, U, V> paramContainer) {
/* 217 */     return () -> {
/*     */         for (int i = paramInt1; i < paramInt2; i++) {
/*     */           paramContainer.applyOperation(i);
/*     */         }
/*     */         synchronized (paramMap) {
/*     */           for (int j = paramInt1; j < paramInt2; j++) {
/*     */             paramContainer.copyOut(j, paramMap);
/*     */           }
/*     */         } 
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> paramCompletableFuture, ParallelMapTransform.Container<K, U, V> paramContainer) {
/* 234 */     Map<K, V> map = this.result;
/* 235 */     return paramCompletableFuture.thenApply(paramObject -> paramMap);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\thread\ParallelMapTransform$BatchedTaskSplitter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */