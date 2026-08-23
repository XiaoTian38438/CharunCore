/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ interface ReForgetEP<R, A, B>
/*    */   extends App2<ReForgetEP.Mu<R>, A, B>
/*    */ {
/*    */   public static final class Mu<R>
/*    */     implements K2 {}
/*    */   
/*    */   static <R, A, B> ReForgetEP<R, A, B> unbox(App2<Mu<R>, A, B> paramApp2) {
/* 19 */     return (ReForgetEP)paramApp2;
/*    */   }
/*    */   
/*    */   B run(Either<A, Pair<A, R>> paramEither);
/*    */   
/*    */   public static final class Instance<R>
/*    */     implements AffineP<Mu<R>, Instance.Mu<R>>, App<Instance.Mu<R>, Mu<R>> {
/*    */     static final class Mu<R> implements AffineP.Mu {}
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<ReForgetEP.Mu<R>, A, B>, App2<ReForgetEP.Mu<R>, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 29 */       return param1App2 -> Optics.reForgetEP("dimap", ());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForgetEP.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ReForgetEP.Mu<R>, A, B> param1App2) {
/* 39 */       ReForgetEP<R, A, B> reForgetEP = ReForgetEP.unbox(param1App2);
/* 40 */       return Optics.reForgetEP("left", param1Either -> (Either)param1Either.map((), ()));
/*    */     }
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
/*    */     public <A, B, C> App2<ReForgetEP.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ReForgetEP.Mu<R>, A, B> param1App2) {
/* 54 */       ReForgetEP<R, A, B> reForgetEP = ReForgetEP.unbox(param1App2);
/* 55 */       return Optics.reForgetEP("right", param1Either -> (Either)param1Either.map((), ()));
/*    */     }
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
/*    */     public <A, B, C> App2<ReForgetEP.Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<ReForgetEP.Mu<R>, A, B> param1App2) {
/* 69 */       ReForgetEP<R, A, B> reForgetEP = ReForgetEP.unbox(param1App2);
/* 70 */       return Optics.reForgetEP("first", param1Either -> (Pair)param1Either.map((), ()));
/*    */     }
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
/*    */     public <A, B, C> App2<ReForgetEP.Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<ReForgetEP.Mu<R>, A, B> param1App2) {
/* 86 */       ReForgetEP<R, A, B> reForgetEP = ReForgetEP.unbox(param1App2);
/* 87 */       return Optics.reForgetEP("second", param1Either -> (Pair)param1Either.map((), ()));
/*    */     }
/*    */   }
/*    */   
/*    */   static final class Mu<R> implements AffineP.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\ReForgetEP.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */