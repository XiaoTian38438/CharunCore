/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.concurrent.CancellationException;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.CompletionException;
/*    */ import java.util.concurrent.Executor;
/*    */ import java.util.function.Consumer;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class FutureChain
/*    */   implements TaskChainer, AutoCloseable {
/* 13 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 15 */   private CompletableFuture<?> head = CompletableFuture.completedFuture(null);
/*    */   
/*    */   private final Executor executor;
/*    */   private volatile boolean closed;
/*    */   
/*    */   public FutureChain(Executor paramExecutor) {
/* 21 */     this.executor = paramExecutor;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> void append(CompletableFuture<T> paramCompletableFuture, Consumer<T> paramConsumer) {
/* 26 */     this
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 33 */       .head = this.head.thenCombine(paramCompletableFuture, (paramObject1, paramObject2) -> paramObject2).thenAcceptAsync(paramObject -> { if (!this.closed) paramConsumer.accept(paramObject);  }this.executor).exceptionally(paramThrowable -> {
/*    */           if (paramThrowable instanceof CompletionException) {
/*    */             CompletionException completionException = (CompletionException)paramThrowable;
/*    */             paramThrowable = completionException.getCause();
/*    */           } 
/*    */           if (paramThrowable instanceof CancellationException) {
/*    */             CancellationException cancellationException = (CancellationException)paramThrowable;
/*    */             throw cancellationException;
/*    */           } 
/*    */           LOGGER.error("Chain link failed, continuing to next one", paramThrowable);
/*    */           return null;
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 49 */     this.closed = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\FutureChain.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */