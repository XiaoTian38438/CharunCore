/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagKey;
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
/*    */ public interface Delegate<T>
/*    */   extends HolderLookup.RegistryLookup<T>
/*    */ {
/*    */   HolderLookup.RegistryLookup<T> parent();
/*    */   
/*    */   default ResourceKey<? extends Registry<? extends T>> key() {
/* 74 */     return parent().key();
/*    */   }
/*    */ 
/*    */   
/*    */   default Lifecycle registryLifecycle() {
/* 79 */     return parent().registryLifecycle();
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<Holder.Reference<T>> get(ResourceKey<T> paramResourceKey) {
/* 84 */     return parent().get(paramResourceKey);
/*    */   }
/*    */ 
/*    */   
/*    */   default Stream<Holder.Reference<T>> listElements() {
/* 89 */     return parent().listElements();
/*    */   }
/*    */ 
/*    */   
/*    */   default Optional<HolderSet.Named<T>> get(TagKey<T> paramTagKey) {
/* 94 */     return parent().get(paramTagKey);
/*    */   }
/*    */ 
/*    */   
/*    */   default Stream<HolderSet.Named<T>> listTags() {
/* 99 */     return parent().listTags();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\HolderLookup$RegistryLookup$Delegate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */