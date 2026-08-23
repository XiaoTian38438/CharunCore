/*     */ package net.minecraft.core.component;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Builder
/*     */ {
/*  99 */   private final List<TypedDataComponent<?>> expectedComponents = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> Builder expect(TypedDataComponent<T> paramTypedDataComponent) {
/* 105 */     return expect(paramTypedDataComponent.type(), paramTypedDataComponent.value());
/*     */   }
/*     */   
/*     */   public <T> Builder expect(DataComponentType<? super T> paramDataComponentType, T paramT) {
/* 109 */     for (TypedDataComponent<? super T> typedDataComponent : this.expectedComponents) {
/* 110 */       if (typedDataComponent.type() == paramDataComponentType) {
/* 111 */         throw new IllegalArgumentException("Predicate already has component of type: '" + String.valueOf(paramDataComponentType) + "'");
/*     */       }
/*     */     } 
/* 114 */     this.expectedComponents.add(new TypedDataComponent(paramDataComponentType, paramT));
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public DataComponentExactPredicate build() {
/* 119 */     return new DataComponentExactPredicate(List.copyOf(this.expectedComponents));
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\core\component\DataComponentExactPredicate$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */