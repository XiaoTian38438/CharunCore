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
/*    */ interface ForgetE<R, A, B>
/*    */   extends App2<ForgetE.Mu<R>, A, B>
/*    */ {
/*    */   public static final class Mu<R>
/*    */     implements K2 {}
/*    */   
/*    */   static <R, A, B> ForgetE<R, A, B> unbox(App2<Mu<R>, A, B> paramApp2) {
/* 19 */     return (ForgetE)paramApp2;
/*    */   }
/*    */   
/*    */   Either<B, R> run(A paramA);
/*    */   
/*    */   public static final class Instance<R>
/*    */     implements AffineP<Mu<R>, Instance.Mu<R>>, App<Instance.Mu<R>, Mu<R>> {
/*    */     static final class Mu<R> implements AffineP.Mu {}
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<ForgetE.Mu<R>, A, B>, App2<ForgetE.Mu<R>, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 29 */       return param1App2 -> Optics.forgetE(());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ForgetE.Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<ForgetE.Mu<R>, A, B> param1App2) {
/* 34 */       return Optics.forgetE(param1Pair -> ForgetE.<R, Object, B>unbox(param1App2).run(param1Pair.getFirst()).mapLeft(()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ForgetE.Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<ForgetE.Mu<R>, A, B> param1App2) {
/* 39 */       return Optics.forgetE(param1Pair -> ForgetE.<R, Object, B>unbox(param1App2).run(param1Pair.getSecond()).mapLeft(()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ForgetE.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ForgetE.Mu<R>, A, B> param1App2) {
/* 44 */       return Optics.forgetE(param1Either -> (Either)param1Either.map((), ()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ForgetE.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ForgetE.Mu<R>, A, B> param1App2) {
/* 49 */       return Optics.forgetE(param1Either -> (Either)param1Either.map((), ()));
/*    */     }
/*    */   }
/*    */   
/*    */   static final class Mu<R> implements AffineP.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\ForgetE.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */