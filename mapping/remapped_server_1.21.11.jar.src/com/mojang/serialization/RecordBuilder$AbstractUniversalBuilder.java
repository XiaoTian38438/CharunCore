/*     */ package com.mojang.serialization;
/*     */ 
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
/*     */ public abstract class AbstractUniversalBuilder<T, R>
/*     */   extends RecordBuilder.AbstractBuilder<T, R>
/*     */ {
/*     */   protected AbstractUniversalBuilder(DynamicOps<T> paramDynamicOps) {
/* 134 */     super(paramDynamicOps);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> add(T paramT1, T paramT2) {
/* 141 */     this.builder = this.builder.map(paramObject3 -> append((T)paramObject1, (T)paramObject2, (R)paramObject3));
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> add(T paramT, DataResult<T> paramDataResult) {
/* 147 */     this.builder = this.builder.apply2stable((paramObject2, paramObject3) -> append((T)paramObject1, (T)paramObject3, (R)paramObject2), paramDataResult);
/* 148 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> add(DataResult<T> paramDataResult1, DataResult<T> paramDataResult2) {
/* 153 */     this.builder = this.builder.ap(paramDataResult1.apply2stable((paramObject1, paramObject2) -> (), paramDataResult2));
/* 154 */     return this;
/*     */   }
/*     */   
/*     */   protected abstract R append(T paramT1, T paramT2, R paramR);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\RecordBuilder$AbstractUniversalBuilder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */