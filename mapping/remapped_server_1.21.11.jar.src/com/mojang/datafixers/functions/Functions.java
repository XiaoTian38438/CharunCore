/*    */ package com.mojang.datafixers.functions;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.TypedOptic;
/*    */ import com.mojang.datafixers.types.Type;
/*    */ import com.mojang.datafixers.types.families.Algebra;
/*    */ import com.mojang.datafixers.types.templates.RecursivePoint;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Functions
/*    */ {
/*    */   public static <A, B, C> PointFree<Function<A, C>> comp(PointFree<Function<B, C>> paramPointFree, PointFree<Function<A, B>> paramPointFree1) {
/* 18 */     if (isId(paramPointFree)) {
/* 19 */       return paramPointFree1;
/*    */     }
/* 21 */     if (isId(paramPointFree1)) {
/* 22 */       return paramPointFree;
/*    */     }
/* 24 */     if (paramPointFree instanceof Comp) { Comp comp = (Comp)paramPointFree; if (paramPointFree1 instanceof Comp) { Comp comp1 = (Comp)paramPointFree1;
/* 25 */         PointFree[] arrayOfPointFree = new PointFree[comp.functions.length + comp1.functions.length];
/* 26 */         System.arraycopy(comp.functions, 0, arrayOfPointFree, 0, comp.functions.length);
/* 27 */         System.arraycopy(comp1.functions, 0, arrayOfPointFree, comp.functions.length, comp1.functions.length);
/* 28 */         return new Comp<>((PointFree<? extends Function<?, ?>>[])arrayOfPointFree); }  }
/* 29 */      if (paramPointFree instanceof Comp) { Comp comp = (Comp)paramPointFree;
/* 30 */       PointFree[] arrayOfPointFree = new PointFree[comp.functions.length + 1];
/* 31 */       System.arraycopy(comp.functions, 0, arrayOfPointFree, 0, comp.functions.length);
/* 32 */       arrayOfPointFree[arrayOfPointFree.length - 1] = paramPointFree1;
/* 33 */       return new Comp<>((PointFree<? extends Function<?, ?>>[])arrayOfPointFree); }
/* 34 */      if (paramPointFree1 instanceof Comp) { Comp comp = (Comp)paramPointFree1;
/* 35 */       PointFree[] arrayOfPointFree = new PointFree[1 + comp.functions.length];
/* 36 */       arrayOfPointFree[0] = paramPointFree;
/* 37 */       System.arraycopy(comp.functions, 0, arrayOfPointFree, 1, comp.functions.length);
/* 38 */       return new Comp<>((PointFree<? extends Function<?, ?>>[])arrayOfPointFree); }
/*    */     
/* 40 */     return new Comp<>((PointFree<? extends Function<?, ?>>[])new PointFree[] { paramPointFree, paramPointFree1 });
/*    */   }
/*    */   
/*    */   public static <A, B> PointFree<Function<A, B>> fun(String paramString, Function<DynamicOps<?>, Function<A, B>> paramFunction, Type<A> paramType, Type<B> paramType1) {
/* 44 */     return new FunctionWrapper<>(paramString, paramFunction, paramType, paramType1);
/*    */   }
/*    */   
/*    */   public static <A, B> PointFree<B> app(PointFree<Function<A, B>> paramPointFree, PointFree<A> paramPointFree1) {
/* 48 */     return new Apply<>(paramPointFree, paramPointFree1);
/*    */   }
/*    */   
/*    */   public static <S, T, A, B> PointFree<Function<Function<A, B>, Function<S, T>>> profunctorTransformer(TypedOptic<S, T, A, B> paramTypedOptic) {
/* 52 */     return new ProfunctorTransformer<>(paramTypedOptic);
/*    */   }
/*    */   
/*    */   public static <A> Bang<A> bang(Type<A> paramType) {
/* 56 */     return new Bang<>(paramType);
/*    */   }
/*    */   
/*    */   public static <A> PointFree<Function<A, A>> in(RecursivePoint.RecursivePointType<A> paramRecursivePointType) {
/* 60 */     return new In<>(paramRecursivePointType);
/*    */   }
/*    */   
/*    */   public static <A> PointFree<Function<A, A>> out(RecursivePoint.RecursivePointType<A> paramRecursivePointType) {
/* 64 */     return new Out<>(paramRecursivePointType);
/*    */   }
/*    */   
/*    */   public static <A, B> PointFree<Function<A, B>> fold(RecursivePoint.RecursivePointType<A> paramRecursivePointType, RecursivePoint.RecursivePointType<B> paramRecursivePointType1, Algebra paramAlgebra, int paramInt) {
/* 68 */     return new Fold<>(paramRecursivePointType, paramRecursivePointType1, paramAlgebra, paramInt);
/*    */   }
/*    */   
/*    */   public static <A> PointFree<Function<A, A>> id(Type<A> paramType) {
/* 72 */     return new Id<>(DSL.func(paramType, paramType));
/*    */   }
/*    */   
/*    */   public static boolean isId(PointFree<?> paramPointFree) {
/* 76 */     return paramPointFree instanceof Id;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\functions\Functions.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */