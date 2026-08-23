/*    */ package com.mojang.serialization;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractBuilder<T, R>
/*    */   implements RecordBuilder<T>
/*    */ {
/*    */   private final DynamicOps<T> ops;
/* 44 */   protected DataResult<R> builder = DataResult.success(initBuilder(), Lifecycle.stable());
/*    */   
/*    */   protected AbstractBuilder(DynamicOps<T> paramDynamicOps) {
/* 47 */     this.ops = paramDynamicOps;
/*    */   }
/*    */ 
/*    */   
/*    */   public DynamicOps<T> ops() {
/* 52 */     return this.ops;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DataResult<T> build(T paramT) {
/* 61 */     DataResult<?> dataResult = this.builder.flatMap(paramObject2 -> build((R)paramObject2, (T)paramObject1));
/* 62 */     this.builder = DataResult.success(initBuilder(), Lifecycle.stable());
/* 63 */     return (DataResult)dataResult;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecordBuilder<T> withErrorsFrom(DataResult<?> paramDataResult) {
/* 68 */     this.builder = this.builder.flatMap(paramObject -> paramDataResult.map(()));
/* 69 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecordBuilder<T> setLifecycle(Lifecycle paramLifecycle) {
/* 74 */     this.builder = this.builder.setLifecycle(paramLifecycle);
/* 75 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public RecordBuilder<T> mapError(UnaryOperator<String> paramUnaryOperator) {
/* 80 */     this.builder = this.builder.mapError(paramUnaryOperator);
/* 81 */     return this;
/*    */   }
/*    */   
/*    */   protected abstract R initBuilder();
/*    */   
/*    */   protected abstract DataResult<T> build(R paramR, T paramT);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\serialization\RecordBuilder$AbstractBuilder.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */