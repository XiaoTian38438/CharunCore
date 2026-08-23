/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.component.DataComponentExactPredicate;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.predicates.DataComponentPredicate;
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
/*    */ public class Builder
/*    */ {
/* 53 */   private DataComponentExactPredicate exact = DataComponentExactPredicate.EMPTY;
/* 54 */   private final ImmutableMap.Builder<DataComponentPredicate.Type<?>, DataComponentPredicate> partial = ImmutableMap.builder();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Builder components() {
/* 60 */     return new Builder();
/*    */   }
/*    */   
/*    */   public <T extends DataComponentType<?>> Builder any(DataComponentType<?> paramDataComponentType) {
/* 64 */     DataComponentPredicate.AnyValueType anyValueType = DataComponentPredicate.AnyValueType.create(paramDataComponentType);
/* 65 */     this.partial.put(anyValueType, anyValueType.predicate());
/* 66 */     return this;
/*    */   }
/*    */   
/*    */   public <T extends DataComponentPredicate> Builder partial(DataComponentPredicate.Type<T> paramType, T paramT) {
/* 70 */     this.partial.put(paramType, paramT);
/* 71 */     return this;
/*    */   }
/*    */   
/*    */   public Builder exact(DataComponentExactPredicate paramDataComponentExactPredicate) {
/* 75 */     this.exact = paramDataComponentExactPredicate;
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public DataComponentMatchers build() {
/* 80 */     return new DataComponentMatchers(this.exact, (Map<DataComponentPredicate.Type<?>, DataComponentPredicate>)this.partial.buildOrThrow());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\advancements\criterion\DataComponentMatchers$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */