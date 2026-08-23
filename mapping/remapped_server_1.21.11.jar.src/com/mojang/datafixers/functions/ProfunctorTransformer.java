/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.TypedOptic;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.optics.Optic;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ final class ProfunctorTransformer<S, T, A, B>
/*    */   extends PointFree<Function<Function<A, B>, Function<S, T>>> {
/*    */   protected final TypedOptic<S, T, A, B> optic;
/*    */   
/*    */   public ProfunctorTransformer(TypedOptic<S, T, A, B> paramTypedOptic) {
/* 19 */     this.optic = paramTypedOptic;
/*    */   }
/*    */   
/*    */   public <S2, T2> ProfunctorTransformer<S2, T2, A, B> castOuterUnchecked(Type<S2> paramType, Type<T2> paramType1) {
/* 23 */     return new ProfunctorTransformer(this.optic.castOuterUnchecked(paramType, paramType1));
/*    */   }
/*    */ 
/*    */   
/*    */   public Type<Function<Function<A, B>, Function<S, T>>> type() {
/* 28 */     return DSL.func(DSL.func(this.optic.aType(), this.optic.bType()), DSL.func(this.optic.sType(), this.optic.tType()));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(int paramInt) {
/* 33 */     return "Optic[" + String.valueOf(this.optic) + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public Function<DynamicOps<?>, Function<Function<A, B>, Function<S, T>>> eval() {
/* 38 */     Function function1 = ((Optic)this.optic.upCast(FunctionType.Instance.Mu.TYPE_TOKEN).orElseThrow()).eval((App)FunctionType.Instance.INSTANCE);
/* 39 */     Function function2 = paramFunction2 -> FunctionType.unbox(paramFunction1.apply(FunctionType.create(paramFunction2)));
/* 40 */     return paramDynamicOps -> paramFunction;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object paramObject) {
/* 45 */     if (this == paramObject) {
/* 46 */       return true;
/*    */     }
/* 48 */     if (paramObject == null || getClass() != paramObject.getClass()) {
/* 49 */       return false;
/*    */     }
/* 51 */     ProfunctorTransformer profunctorTransformer = (ProfunctorTransformer)paramObject;
/* 52 */     return Objects.equals(this.optic, profunctorTransformer.optic);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 57 */     return this.optic.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\ProfunctorTransformer.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */