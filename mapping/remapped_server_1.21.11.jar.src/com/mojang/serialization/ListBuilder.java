/*    */ package com.mojang.serialization;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ 
/*    */ public interface ListBuilder<T>
/*    */ {
/*    */   DynamicOps<T> ops();
/*    */   
/*    */   DataResult<T> build(T paramT);
/*    */   
/*    */   ListBuilder<T> add(T paramT);
/*    */   
/*    */   ListBuilder<T> add(DataResult<T> paramDataResult);
/*    */   
/*    */   ListBuilder<T> withErrorsFrom(DataResult<?> paramDataResult);
/*    */   
/*    */   ListBuilder<T> mapError(UnaryOperator<String> paramUnaryOperator);
/*    */   
/*    */   default DataResult<T> build(DataResult<T> paramDataResult) {
/* 23 */     return paramDataResult.flatMap(this::build);
/*    */   }
/*    */   
/*    */   default <E> ListBuilder<T> add(E paramE, Encoder<E> paramEncoder) {
/* 27 */     return add(paramEncoder.encodeStart(ops(), paramE));
/*    */   }
/*    */   
/*    */   default <E> ListBuilder<T> addAll(Iterable<E> paramIterable, Encoder<E> paramEncoder) {
/* 31 */     paramIterable.forEach(paramObject -> paramEncoder.encode(paramObject, ops(), ops().empty()));
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public static final class Builder<T> implements ListBuilder<T> {
/*    */     private final DynamicOps<T> ops;
/* 37 */     private DataResult<ImmutableList.Builder<T>> builder = DataResult.success(ImmutableList.builder(), Lifecycle.stable());
/*    */     
/*    */     public Builder(DynamicOps<T> param1DynamicOps) {
/* 40 */       this.ops = param1DynamicOps;
/*    */     }
/*    */ 
/*    */     
/*    */     public DynamicOps<T> ops() {
/* 45 */       return this.ops;
/*    */     }
/*    */ 
/*    */     
/*    */     public ListBuilder<T> add(T param1T) {
/* 50 */       this.builder = this.builder.map(param1Builder -> param1Builder.add(param1Object));
/* 51 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public ListBuilder<T> add(DataResult<T> param1DataResult) {
/* 56 */       this.builder = this.builder.apply2stable(ImmutableList.Builder::add, param1DataResult);
/* 57 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public ListBuilder<T> withErrorsFrom(DataResult<?> param1DataResult) {
/* 62 */       this.builder = this.builder.flatMap(param1Builder -> param1DataResult.map(()));
/* 63 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public ListBuilder<T> mapError(UnaryOperator<String> param1UnaryOperator) {
/* 68 */       this.builder = this.builder.mapError(param1UnaryOperator);
/* 69 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public DataResult<T> build(T param1T) {
/* 74 */       DataResult<?> dataResult = this.builder.flatMap(param1Builder -> this.ops.mergeToList((T)param1Object, (List<T>)param1Builder.build()));
/* 75 */       this.builder = DataResult.success(ImmutableList.builder(), Lifecycle.stable());
/* 76 */       return (DataResult)dataResult;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\ListBuilder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */