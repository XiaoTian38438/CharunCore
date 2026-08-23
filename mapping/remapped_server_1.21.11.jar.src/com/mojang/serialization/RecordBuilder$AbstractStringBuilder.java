/*     */ package com.mojang.serialization;
/*     */ 
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractStringBuilder<T, R>
/*     */   extends RecordBuilder.AbstractBuilder<T, R>
/*     */ {
/*     */   protected AbstractStringBuilder(DynamicOps<T> paramDynamicOps) {
/*  87 */     super(paramDynamicOps);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> add(String paramString, T paramT) {
/*  94 */     this.builder = this.builder.map(paramObject2 -> append(paramString, (T)paramObject1, (R)paramObject2));
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> add(String paramString, DataResult<T> paramDataResult) {
/* 100 */     this.builder = this.builder.apply2stable((paramObject1, paramObject2) -> append(paramString, (T)paramObject2, (R)paramObject1), paramDataResult);
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> add(T paramT1, T paramT2) {
/* 106 */     this.builder = ops().getStringValue(paramT1).flatMap(paramString -> {
/*     */           add(paramString, (T)paramObject);
/*     */           return this.builder;
/*     */         });
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> add(T paramT, DataResult<T> paramDataResult) {
/* 115 */     this.builder = ops().getStringValue(paramT).flatMap(paramString -> {
/*     */           add(paramString, paramDataResult);
/*     */           return this.builder;
/*     */         });
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<T> add(DataResult<T> paramDataResult1, DataResult<T> paramDataResult2) {
/* 124 */     Objects.requireNonNull(ops()); this.builder = paramDataResult1.flatMap(ops()::getStringValue).flatMap(paramString -> {
/*     */           add(paramString, paramDataResult);
/*     */           return this.builder;
/*     */         });
/* 128 */     return this;
/*     */   }
/*     */   
/*     */   protected abstract R append(String paramString, T paramT, R paramR);
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\RecordBuilder$AbstractStringBuilder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */