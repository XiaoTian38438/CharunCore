/*    */ package net.minecraft.util;
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
/*    */ class null
/*    */   implements TaskChainer
/*    */ {
/*    */   public <T> void append(CompletableFuture<T> paramCompletableFuture, Consumer<T> paramConsumer) {
/* 18 */     paramCompletableFuture.thenAcceptAsync(paramConsumer, executor).exceptionally(paramThrowable -> {
/*    */           LOGGER.error("Task failed", paramThrowable);
/*    */           return null;
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\TaskChainer$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */