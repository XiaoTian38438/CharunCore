/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Cocartesian;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ interface ReForgetE<R, A, B>
/*    */   extends App2<ReForgetE.Mu<R>, A, B>
/*    */ {
/*    */   public static final class Mu<R>
/*    */     implements K2 {}
/*    */   
/*    */   static <R, A, B> ReForgetE<R, A, B> unbox(App2<Mu<R>, A, B> paramApp2) {
/* 18 */     return (ReForgetE)paramApp2;
/*    */   }
/*    */   
/*    */   B run(Either<A, R> paramEither);
/*    */   
/*    */   public static final class Instance<R>
/*    */     implements Cocartesian<Mu<R>, Instance.Mu<R>>, App<Instance.Mu<R>, Mu<R>> {
/*    */     static final class Mu<R> implements Cocartesian.Mu {}
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<ReForgetE.Mu<R>, A, B>, App2<ReForgetE.Mu<R>, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 28 */       return param1App2 -> Optics.reForgetE("dimap", ());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForgetE.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ReForgetE.Mu<R>, A, B> param1App2) {
/* 38 */       ReForgetE<R, A, B> reForgetE = ReForgetE.unbox(param1App2);
/* 39 */       return Optics.reForgetE("left", param1Either -> (Either)param1Either.map((), ()));
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
/*    */     public <A, B, C> App2<ReForgetE.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ReForgetE.Mu<R>, A, B> param1App2) {
/* 52 */       ReForgetE<R, A, B> reForgetE = ReForgetE.unbox(param1App2);
/* 53 */       return Optics.reForgetE("right", param1Either -> (Either)param1Either.map((), ()));
/*    */     }
/*    */   }
/*    */   
/*    */   static final class Mu<R> implements Cocartesian.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\ReForgetE.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */