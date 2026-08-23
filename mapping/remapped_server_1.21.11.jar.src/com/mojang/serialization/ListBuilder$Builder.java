/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.function.UnaryOperator;
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
/*    */ public final class Builder<T>
/*    */   implements ListBuilder<T>
/*    */ {
/*    */   private final DynamicOps<T> ops;
/* 37 */   private DataResult<ImmutableList.Builder<T>> builder = DataResult.success(ImmutableList.builder(), Lifecycle.stable());
/*    */   
/*    */   public Builder(DynamicOps<T> paramDynamicOps) {
/* 40 */     this.ops = paramDynamicOps;
/*    */   }
/*    */ 
/*    */   
/*    */   public DynamicOps<T> ops() {
/* 45 */     return this.ops;
/*    */   }
/*    */ 
/*    */   
/*    */   public ListBuilder<T> add(T paramT) {
/* 50 */     this.builder = this.builder.map(paramBuilder -> paramBuilder.add(paramObject));
/* 51 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public ListBuilder<T> add(DataResult<T> paramDataResult) {
/* 56 */     this.builder = this.builder.apply2stable(ImmutableList.Builder::add, paramDataResult);
/* 57 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public ListBuilder<T> withErrorsFrom(DataResult<?> paramDataResult) {
/* 62 */     this.builder = this.builder.flatMap(paramBuilder -> paramDataResult.map(()));
/* 63 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public ListBuilder<T> mapError(UnaryOperator<String> paramUnaryOperator) {
/* 68 */     this.builder = this.builder.mapError(paramUnaryOperator);
/* 69 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public DataResult<T> build(T paramT) {
/* 74 */     DataResult<?> dataResult = this.builder.flatMap(paramBuilder -> this.ops.mergeToList((T)paramObject, (List<T>)paramBuilder.build()));
/* 75 */     this.builder = DataResult.success(ImmutableList.builder(), Lifecycle.stable());
/* 76 */     return (DataResult)dataResult;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\ListBuilder$Builder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */