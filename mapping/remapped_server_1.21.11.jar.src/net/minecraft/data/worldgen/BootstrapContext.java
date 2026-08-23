/*    */ package net.minecraft.data.worldgen;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public interface BootstrapContext<T> {
/*    */   Holder.Reference<T> register(ResourceKey<T> paramResourceKey, T paramT, Lifecycle paramLifecycle);
/*    */   
/*    */   default Holder.Reference<T> register(ResourceKey<T> paramResourceKey, T paramT) {
/* 13 */     return register(paramResourceKey, paramT, Lifecycle.stable());
/*    */   }
/*    */   
/*    */   <S> HolderGetter<S> lookup(ResourceKey<? extends Registry<? extends S>> paramResourceKey);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\data\worldgen\BootstrapContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */