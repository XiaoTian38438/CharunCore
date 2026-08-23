/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface HolderGetter<T>
/*    */ {
/*    */   default Holder.Reference<T> getOrThrow(ResourceKey<T> paramResourceKey) {
/* 16 */     return get(paramResourceKey).<Throwable>orElseThrow(() -> new IllegalStateException("Missing element " + String.valueOf(paramResourceKey)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   default HolderSet.Named<T> getOrThrow(TagKey<T> paramTagKey) {
/* 22 */     return get(paramTagKey).<Throwable>orElseThrow(() -> new IllegalStateException("Missing tag " + String.valueOf(paramTagKey)));
/*    */   }
/*    */   Optional<Holder.Reference<T>> get(ResourceKey<T> paramResourceKey);
/*    */   default Optional<Holder<T>> getRandomElementOf(TagKey<T> paramTagKey, RandomSource paramRandomSource) {
/* 26 */     return get(paramTagKey).flatMap(paramNamed -> paramNamed.getRandomElement(paramRandomSource));
/*    */   }
/*    */   
/*    */   Optional<HolderSet.Named<T>> get(TagKey<T> paramTagKey);
/*    */   
/*    */   public static interface Provider {
/*    */     default <T> HolderGetter<T> lookupOrThrow(ResourceKey<? extends Registry<? extends T>> param1ResourceKey) {
/* 33 */       return (HolderGetter<T>)lookup(param1ResourceKey).orElseThrow(() -> new IllegalStateException("Registry " + String.valueOf(param1ResourceKey.identifier()) + " not found"));
/*    */     }
/*    */     
/*    */     default <T> Optional<Holder.Reference<T>> get(ResourceKey<T> param1ResourceKey) {
/* 37 */       return lookup(param1ResourceKey.registryKey()).flatMap(param1HolderGetter -> param1HolderGetter.get(param1ResourceKey));
/*    */     }
/*    */     
/*    */     default <T> Holder.Reference<T> getOrThrow(ResourceKey<T> param1ResourceKey) {
/* 41 */       return (Holder.Reference<T>)lookup(param1ResourceKey.registryKey()).flatMap(param1HolderGetter -> param1HolderGetter.get(param1ResourceKey)).orElseThrow(() -> new IllegalStateException("Missing element " + String.valueOf(param1ResourceKey)));
/*    */     }
/*    */     
/*    */     <T> Optional<? extends HolderGetter<T>> lookup(ResourceKey<? extends Registry<? extends T>> param1ResourceKey);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\HolderGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */