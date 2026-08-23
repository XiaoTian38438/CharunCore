/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SharedState
/*    */ {
/*    */   private final ResourceManager manager;
/* 30 */   private final Map<PreparableReloadListener.StateKey<?>, Object> state = new IdentityHashMap<>();
/*    */   
/*    */   public SharedState(ResourceManager paramResourceManager) {
/* 33 */     this.manager = paramResourceManager;
/*    */   }
/*    */   
/*    */   public ResourceManager resourceManager() {
/* 37 */     return this.manager;
/*    */   }
/*    */   
/*    */   public <T> void set(PreparableReloadListener.StateKey<T> paramStateKey, T paramT) {
/* 41 */     this.state.put(paramStateKey, paramT);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T get(PreparableReloadListener.StateKey<T> paramStateKey) {
/* 46 */     return Objects.requireNonNull((T)this.state.get(paramStateKey));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\packs\resources\PreparableReloadListener$SharedState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */