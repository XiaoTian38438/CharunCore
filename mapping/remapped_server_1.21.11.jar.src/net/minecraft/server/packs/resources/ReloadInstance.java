/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ public interface ReloadInstance {
/*    */   CompletableFuture<?> done();
/*    */   
/*    */   float getActualProgress();
/*    */   
/*    */   default boolean isDone() {
/* 11 */     return done().isDone();
/*    */   }
/*    */   
/*    */   default void checkExceptions() {
/* 15 */     CompletableFuture<?> completableFuture = done();
/* 16 */     if (completableFuture.isCompletedExceptionally())
/* 17 */       completableFuture.join(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\resources\ReloadInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */