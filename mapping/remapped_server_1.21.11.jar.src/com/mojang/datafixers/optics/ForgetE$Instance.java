/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
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
/*    */ public final class Instance<R>
/*    */   implements AffineP<ForgetE.Mu<R>, ForgetE.Instance.Mu<R>>, App<ForgetE.Instance.Mu<R>, ForgetE.Mu<R>>
/*    */ {
/*    */   static final class Mu<R>
/*    */     implements AffineP.Mu {}
/*    */   
/*    */   public <A, B, C, D> FunctionType<App2<ForgetE.Mu<R>, A, B>, App2<ForgetE.Mu<R>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 29 */     return paramApp2 -> Optics.forgetE(());
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ForgetE.Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<ForgetE.Mu<R>, A, B> paramApp2) {
/* 34 */     return Optics.forgetE(paramPair -> ForgetE.<R, Object, B>unbox(paramApp2).run(paramPair.getFirst()).mapLeft(()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ForgetE.Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<ForgetE.Mu<R>, A, B> paramApp2) {
/* 39 */     return Optics.forgetE(paramPair -> ForgetE.<R, Object, B>unbox(paramApp2).run(paramPair.getSecond()).mapLeft(()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ForgetE.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ForgetE.Mu<R>, A, B> paramApp2) {
/* 44 */     return Optics.forgetE(paramEither -> (Either)paramEither.map((), ()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ForgetE.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ForgetE.Mu<R>, A, B> paramApp2) {
/* 49 */     return Optics.forgetE(paramEither -> (Either)paramEither.map((), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\ForgetE$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */