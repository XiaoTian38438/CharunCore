/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.stream.Stream;
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
/*    */ abstract class EmptyTagRegistryLookup<T>
/*    */   extends RegistrySetBuilder.EmptyTagLookup<T>
/*    */   implements HolderLookup.RegistryLookup<T>
/*    */ {
/*    */   protected EmptyTagRegistryLookup(HolderOwner<T> paramHolderOwner) {
/* 68 */     super(paramHolderOwner);
/*    */   }
/*    */ 
/*    */   
/*    */   public Stream<HolderSet.Named<T>> listTags() {
/* 73 */     throw new UnsupportedOperationException("Tags are not available in datagen");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\RegistrySetBuilder$EmptyTagRegistryLookup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */