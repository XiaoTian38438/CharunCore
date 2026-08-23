/*    */ package net.minecraft.core.component;
/*    */ 
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface DataComponentHolder
/*    */   extends DataComponentGetter
/*    */ {
/*    */   default <T> T get(DataComponentType<? extends T> paramDataComponentType) {
/* 12 */     return getComponents().get(paramDataComponentType);
/*    */   }
/*    */ 
/*    */   
/*    */   default <T> Stream<T> getAllOfType(Class<? extends T> paramClass) {
/* 17 */     return getComponents().stream().map(TypedDataComponent::value).filter(paramObject -> paramClass.isAssignableFrom(paramObject.getClass())).map(paramObject -> paramObject);
/*    */   }
/*    */ 
/*    */   
/*    */   default <T> T getOrDefault(DataComponentType<? extends T> paramDataComponentType, T paramT) {
/* 22 */     return getComponents().getOrDefault(paramDataComponentType, paramT);
/*    */   }
/*    */   
/*    */   default boolean has(DataComponentType<?> paramDataComponentType) {
/* 26 */     return getComponents().has(paramDataComponentType);
/*    */   }
/*    */   
/*    */   DataComponentMap getComponents();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\component\DataComponentHolder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */