/*     */ package net.minecraft.util.thread;
/*     */ 
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SingleTaskSplitter<K, U, V>
/*     */   extends ParallelMapTransform.SplitterBase<K, U, V>
/*     */ {
/*     */   SingleTaskSplitter(BiFunction<K, U, V> paramBiFunction, int paramInt) {
/* 153 */     super(paramBiFunction, paramInt, paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int batchSize(int paramInt) {
/* 158 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   protected CompletableFuture<?> scheduleBatch(ParallelMapTransform.Container<K, U, V> paramContainer, int paramInt1, int paramInt2, Executor paramExecutor) {
/* 163 */     assert paramInt1 + 1 == paramInt2;
/* 164 */     return CompletableFuture.runAsync(() -> paramContainer.applyOperation(paramInt), paramExecutor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected CompletableFuture<Map<K, V>> scheduleFinalOperation(CompletableFuture<?> paramCompletableFuture, ParallelMapTransform.Container<K, U, V> paramContainer) {
/* 169 */     return paramCompletableFuture.thenApply(paramObject -> {
/*     */           HashMap<Object, Object> hashMap = new HashMap<>(paramContainer.size());
/*     */           for (byte b = 0; b < paramContainer.size(); b++)
/*     */             paramContainer.copyOut(b, hashMap); 
/*     */           return hashMap;
/*     */         });
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\thread\ParallelMapTransform$SingleTaskSplitter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */