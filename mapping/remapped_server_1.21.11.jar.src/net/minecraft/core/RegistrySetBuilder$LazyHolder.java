/*    */ package net.minecraft.core;
/*    */ 
/*    */ import java.util.function.Supplier;
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
/*    */ class LazyHolder<T>
/*    */   extends Holder.Reference<T>
/*    */ {
/*    */   Supplier<T> supplier;
/*    */   
/*    */   protected LazyHolder(HolderOwner<T> paramHolderOwner, ResourceKey<T> paramResourceKey) {
/* 35 */     super(Holder.Reference.Type.STAND_ALONE, paramHolderOwner, paramResourceKey, null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void bindValue(T paramT) {
/* 40 */     super.bindValue(paramT);
/* 41 */     this.supplier = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public T value() {
/* 46 */     if (this.supplier != null) {
/* 47 */       bindValue(this.supplier.get());
/*    */     }
/* 49 */     return super.value();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\RegistrySetBuilder$LazyHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */