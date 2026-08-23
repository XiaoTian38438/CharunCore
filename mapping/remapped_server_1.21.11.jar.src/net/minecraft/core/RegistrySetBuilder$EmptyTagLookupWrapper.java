/*    */ package net.minecraft.core;
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
/*    */ class EmptyTagLookupWrapper<T>
/*    */   extends RegistrySetBuilder.EmptyTagRegistryLookup<T>
/*    */   implements HolderLookup.RegistryLookup.Delegate<T>
/*    */ {
/*    */   private final HolderLookup.RegistryLookup<T> parent;
/*    */   
/*    */   EmptyTagLookupWrapper(HolderOwner<T> paramHolderOwner, HolderLookup.RegistryLookup<T> paramRegistryLookup) {
/* 81 */     super(paramHolderOwner);
/* 82 */     this.parent = paramRegistryLookup;
/*    */   }
/*    */ 
/*    */   
/*    */   public HolderLookup.RegistryLookup<T> parent() {
/* 87 */     return this.parent;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\RegistrySetBuilder$EmptyTagLookupWrapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */