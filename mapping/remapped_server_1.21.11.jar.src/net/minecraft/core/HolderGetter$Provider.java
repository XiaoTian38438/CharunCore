/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.ResourceKey;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Provider
/*    */ {
/*    */   default <T> HolderGetter<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> paramResourceKey) {
/* 33 */     return (HolderGetter<T>)lookup(paramResourceKey).orElseThrow(() -> new IllegalStateException("Registry " + String.valueOf(paramResourceKey.identifier()) + " not found"));
/*    */   }
/*    */   
/*    */   default <T> Optional<Holder.Reference<T>> get(ResourceKey<T> paramResourceKey) {
/* 37 */     return lookup(paramResourceKey.registryKey()).flatMap(paramHolderGetter -> paramHolderGetter.get(paramResourceKey));
/*    */   }
/*    */   
/*    */   default <T> Holder.Reference<T> getOrThrow(ResourceKey<T> paramResourceKey) {
/* 41 */     return (Holder.Reference<T>)lookup(paramResourceKey.registryKey()).flatMap(paramHolderGetter -> paramHolderGetter.get(paramResourceKey)).orElseThrow(() -> new IllegalStateException("Missing element " + String.valueOf(paramResourceKey)));
/*    */   }
/*    */   
/*    */   <T> Optional<? extends HolderGetter<T>> lookup(ResourceKey<? extends Registry<? extends T>> paramResourceKey);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\HolderGetter$Provider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */