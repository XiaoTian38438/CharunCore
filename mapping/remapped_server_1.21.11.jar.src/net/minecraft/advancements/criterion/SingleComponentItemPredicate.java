/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import net.minecraft.core.component.DataComponentGetter;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*    */ 
/*    */ public interface SingleComponentItemPredicate<T>
/*    */   extends DataComponentPredicate {
/*    */   default boolean matches(DataComponentGetter paramDataComponentGetter) {
/* 10 */     Object object = paramDataComponentGetter.get(componentType());
/* 11 */     return (object != null && matches((T)object));
/*    */   }
/*    */   
/*    */   DataComponentType<T> componentType();
/*    */   
/*    */   boolean matches(T paramT);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\SingleComponentItemPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */