/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Function;
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
/*     */ final class NamedFunctionWrapper<A, B>
/*     */   implements Function<DynamicOps<?>, Function<A, B>>
/*     */ {
/*     */   private final String name;
/*     */   private final Function<DynamicOps<?>, Function<A, B>> delegate;
/*     */   
/*     */   public NamedFunctionWrapper(String paramString, Function<DynamicOps<?>, Function<A, B>> paramFunction) {
/* 143 */     this.name = paramString;
/* 144 */     this.delegate = paramFunction;
/*     */   }
/*     */ 
/*     */   
/*     */   public Function<A, B> apply(DynamicOps<?> paramDynamicOps) {
/* 149 */     return this.delegate.apply(paramDynamicOps);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 154 */     if (this == paramObject) {
/* 155 */       return true;
/*     */     }
/* 157 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 158 */       return false;
/*     */     }
/* 160 */     NamedFunctionWrapper namedFunctionWrapper = (NamedFunctionWrapper)paramObject;
/* 161 */     return Objects.equals(this.name, namedFunctionWrapper.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 166 */     return this.name.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\DataFix$NamedFunctionWrapper.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */