/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.util.profiling.Profiler;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ 
/*    */ public abstract class SimplePreparableReloadListener<T>
/*    */   implements PreparableReloadListener {
/*    */   public final CompletableFuture<Void> reload(PreparableReloadListener.SharedState paramSharedState, Executor paramExecutor1, PreparableReloadListener.PreparationBarrier paramPreparationBarrier, Executor paramExecutor2) {
/* 12 */     ResourceManager resourceManager = paramSharedState.resourceManager();
/*    */     
/* 14 */     Objects.requireNonNull(paramPreparationBarrier); return CompletableFuture.supplyAsync(() -> prepare(paramResourceManager, Profiler.get()), paramExecutor1).thenCompose(paramPreparationBarrier::wait)
/* 15 */       .thenAcceptAsync(paramObject -> apply((T)paramObject, paramResourceManager, Profiler.get()), paramExecutor2);
/*    */   }
/*    */   
/*    */   protected abstract T prepare(ResourceManager paramResourceManager, ProfilerFiller paramProfilerFiller);
/*    */   
/*    */   protected abstract void apply(T paramT, ResourceManager paramResourceManager, ProfilerFiller paramProfilerFiller);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\resources\SimplePreparableReloadListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */