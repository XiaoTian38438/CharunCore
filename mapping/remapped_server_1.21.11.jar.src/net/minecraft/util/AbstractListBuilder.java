/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.Lifecycle;
/*    */ import com.mojang.serialization.ListBuilder;
/*    */ import java.util.function.UnaryOperator;
/*    */ 
/*    */ abstract class AbstractListBuilder<T, B>
/*    */   implements ListBuilder<T> {
/*    */   private final DynamicOps<T> ops;
/* 12 */   protected DataResult<B> builder = DataResult.success(initBuilder(), Lifecycle.stable());
/*    */   
/*    */   protected AbstractListBuilder(DynamicOps<T> paramDynamicOps) {
/* 15 */     this.ops = paramDynamicOps;
/*    */   }
/*    */ 
/*    */   
/*    */   public DynamicOps<T> ops() {
/* 20 */     return this.ops;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ListBuilder<T> add(T paramT) {
/* 31 */     this.builder = this.builder.map(paramObject2 -> append((B)paramObject2, (T)paramObject1));
/* 32 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public ListBuilder<T> add(DataResult<T> paramDataResult) {
/* 37 */     this.builder = this.builder.apply2stable(this::append, paramDataResult);
/* 38 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public ListBuilder<T> withErrorsFrom(DataResult<?> paramDataResult) {
/* 43 */     this.builder = this.builder.flatMap(paramObject -> paramDataResult.map(()));
/* 44 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public ListBuilder<T> mapError(UnaryOperator<String> paramUnaryOperator) {
/* 49 */     this.builder = this.builder.mapError(paramUnaryOperator);
/* 50 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public DataResult<T> build(T paramT) {
/* 55 */     DataResult<T> dataResult = this.builder.flatMap(paramObject2 -> build((B)paramObject2, (T)paramObject1));
/* 56 */     this.builder = DataResult.success(initBuilder(), Lifecycle.stable());
/* 57 */     return dataResult;
/*    */   }
/*    */   
/*    */   protected abstract B initBuilder();
/*    */   
/*    */   protected abstract B append(B paramB, T paramT);
/*    */   
/*    */   protected abstract DataResult<T> build(B paramB, T paramT);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\AbstractListBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */