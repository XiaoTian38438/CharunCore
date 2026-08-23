/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.util.profiling.Profiler;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ 
/*    */ public interface ResourceManagerReloadListener
/*    */   extends PreparableReloadListener
/*    */ {
/*    */   default CompletableFuture<Void> reload(PreparableReloadListener.SharedState paramSharedState, Executor paramExecutor1, PreparableReloadListener.PreparationBarrier paramPreparationBarrier, Executor paramExecutor2) {
/* 13 */     ResourceManager resourceManager = paramSharedState.resourceManager();
/* 14 */     return paramPreparationBarrier.<Unit>wait(Unit.INSTANCE).thenRunAsync(() -> { ProfilerFiller profilerFiller = Profiler.get(); profilerFiller.push("listener"); onResourceManagerReload(paramResourceManager); profilerFiller.pop(); }paramExecutor2);
/*    */   }
/*    */   
/*    */   void onResourceManagerReload(ResourceManager paramResourceManager);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\resources\ResourceManagerReloadListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */