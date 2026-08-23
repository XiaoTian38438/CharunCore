/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.Optional;
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
/*    */ abstract class EmptyTagLookup<T>
/*    */   implements HolderGetter<T>
/*    */ {
/*    */   protected final HolderOwner<T> owner;
/*    */   
/*    */   protected EmptyTagLookup(HolderOwner<T> paramHolderOwner) {
/* 57 */     this.owner = paramHolderOwner;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<HolderSet.Named<T>> get(TagKey<T> paramTagKey) {
/* 62 */     return Optional.of(HolderSet.emptyNamed(this.owner, paramTagKey));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\RegistrySetBuilder$EmptyTagLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */