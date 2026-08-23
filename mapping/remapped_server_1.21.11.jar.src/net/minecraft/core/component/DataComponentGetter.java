/*    */ package net.minecraft.core.component;
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
/*    */ public interface DataComponentGetter
/*    */ {
/*    */   <T> T get(DataComponentType<? extends T> paramDataComponentType);
/*    */   
/*    */   default <T> T getOrDefault(DataComponentType<? extends T> paramDataComponentType, T paramT) {
/* 28 */     T t = (T)get((DataComponentType)paramDataComponentType);
/* 29 */     return (t != null) ? t : paramT;
/*    */   }
/*    */   
/*    */   default <T> TypedDataComponent<T> getTyped(DataComponentType<T> paramDataComponentType) {
/* 33 */     T t = (T)get((DataComponentType)paramDataComponentType);
/* 34 */     return (t != null) ? new TypedDataComponent<>(paramDataComponentType, t) : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\component\DataComponentGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */