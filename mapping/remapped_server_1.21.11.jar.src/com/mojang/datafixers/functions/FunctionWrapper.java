/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ final class FunctionWrapper<A, B>
/*    */   extends PointFree<Function<A, B>>
/*    */ {
/*    */   private final String name;
/*    */   protected final Function<DynamicOps<?>, Function<A, B>> fun;
/*    */   private final Type<Function<A, B>> type;
/*    */   
/*    */   FunctionWrapper(String paramString, Function<DynamicOps<?>, Function<A, B>> paramFunction, Type<A> paramType, Type<B> paramType1) {
/* 18 */     this.name = paramString;
/* 19 */     this.fun = paramFunction;
/* 20 */     this.type = DSL.func(paramType, paramType1);
/*    */   }
/*    */ 
/*    */   
/*    */   public Type<Function<A, B>> type() {
/* 25 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(int paramInt) {
/* 30 */     return "fun[" + this.name + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 35 */     if (this == paramObject) {
/* 36 */       return true;
/*    */     }
/* 38 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 39 */       return false;
/*    */     }
/* 41 */     FunctionWrapper functionWrapper = (FunctionWrapper)paramObject;
/* 42 */     return (Objects.equals(this.fun, functionWrapper.fun) && Objects.equals(this.type, functionWrapper.type));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 47 */     return this.fun.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public Function<DynamicOps<?>, Function<A, B>> eval() {
/* 52 */     return this.fun;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\FunctionWrapper.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */