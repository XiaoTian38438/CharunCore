/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Consumer;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface TaskChainer
/*    */ {
/* 12 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   static TaskChainer immediate(final Executor executor) {
/* 15 */     return new TaskChainer()
/*    */       {
/*    */         public <T> void append(CompletableFuture<T> param1CompletableFuture, Consumer<T> param1Consumer) {
/* 18 */           param1CompletableFuture.thenAcceptAsync(param1Consumer, executor).exceptionally(param1Throwable -> {
/*    */                 LOGGER.error("Task failed", param1Throwable);
/*    */                 return null;
/*    */               });
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   default void append(Runnable paramRunnable) {
/* 27 */     append(CompletableFuture.completedFuture(null), paramObject -> paramRunnable.run());
/*    */   }
/*    */   
/*    */   <T> void append(CompletableFuture<T> paramCompletableFuture, Consumer<T> paramConsumer);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\TaskChainer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */