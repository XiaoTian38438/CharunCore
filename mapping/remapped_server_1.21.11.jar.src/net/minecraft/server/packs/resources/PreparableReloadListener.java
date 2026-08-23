/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface PreparableReloadListener
/*    */ {
/*    */   CompletableFuture<Void> reload(SharedState paramSharedState, Executor paramExecutor1, PreparationBarrier paramPreparationBarrier, Executor paramExecutor2);
/*    */   
/*    */   default void prepareSharedState(SharedState paramSharedState) {}
/*    */   
/*    */   default String getName() {
/* 17 */     return getClass().getSimpleName();
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface PreparationBarrier
/*    */   {
/*    */     <T> CompletableFuture<T> wait(T param1T);
/*    */   }
/*    */   
/*    */   public static final class StateKey<T> {}
/*    */   
/*    */   public static final class SharedState {
/*    */     private final ResourceManager manager;
/* 30 */     private final Map<PreparableReloadListener.StateKey<?>, Object> state = new IdentityHashMap<>();
/*    */     
/*    */     public SharedState(ResourceManager param1ResourceManager) {
/* 33 */       this.manager = param1ResourceManager;
/*    */     }
/*    */     
/*    */     public ResourceManager resourceManager() {
/* 37 */       return this.manager;
/*    */     }
/*    */     
/*    */     public <T> void set(PreparableReloadListener.StateKey<T> param1StateKey, T param1T) {
/* 41 */       this.state.put(param1StateKey, param1T);
/*    */     }
/*    */ 
/*    */     
/*    */     public <T> T get(PreparableReloadListener.StateKey<T> param1StateKey) {
/* 46 */       return Objects.requireNonNull((T)this.state.get(param1StateKey));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\resources\PreparableReloadListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */