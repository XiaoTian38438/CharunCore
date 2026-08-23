/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ 
/*    */ public class MinecraftExecutorServiceImpl
/*    */   implements MinecraftExecutorService
/*    */ {
/*    */   private final DedicatedServer server;
/*    */   
/*    */   public MinecraftExecutorServiceImpl(DedicatedServer paramDedicatedServer) {
/* 13 */     this.server = paramDedicatedServer;
/*    */   }
/*    */ 
/*    */   
/*    */   public <V> CompletableFuture<V> submit(Supplier<V> paramSupplier) {
/* 18 */     return this.server.submit(paramSupplier);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<Void> submit(Runnable paramRunnable) {
/* 23 */     return this.server.submit(paramRunnable);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\jsonrpc\internalapi\MinecraftExecutorServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */