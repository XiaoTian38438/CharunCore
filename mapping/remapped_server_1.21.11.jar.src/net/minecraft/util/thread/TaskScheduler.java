/*    */ package net.minecraft.util.thread;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Consumer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface TaskScheduler<R extends Runnable>
/*    */   extends AutoCloseable
/*    */ {
/*    */   default void close() {}
/*    */   
/*    */   default <Source> CompletableFuture<Source> scheduleWithResult(Consumer<CompletableFuture<Source>> paramConsumer) {
/* 20 */     CompletableFuture<Source> completableFuture = new CompletableFuture();
/* 21 */     schedule(wrapRunnable(() -> paramConsumer.accept(paramCompletableFuture)));
/* 22 */     return completableFuture;
/*    */   }
/*    */   
/*    */   static TaskScheduler<Runnable> wrapExecutor(final String name, final Executor executor) {
/* 26 */     return new TaskScheduler<Runnable>()
/*    */       {
/*    */         public String name() {
/* 29 */           return name;
/*    */         }
/*    */ 
/*    */         
/*    */         public void schedule(Runnable param1Runnable) {
/* 34 */           executor.execute(param1Runnable);
/*    */         }
/*    */ 
/*    */         
/*    */         public Runnable wrapRunnable(Runnable param1Runnable) {
/* 39 */           return param1Runnable;
/*    */         }
/*    */ 
/*    */         
/*    */         public String toString() {
/* 44 */           return name;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   String name();
/*    */   
/*    */   void schedule(R paramR);
/*    */   
/*    */   R wrapRunnable(Runnable paramRunnable);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\thread\TaskScheduler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */