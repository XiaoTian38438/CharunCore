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
/*    */   implements AffineP<ReForgetEP.Mu<R>, ReForgetEP.Instance.Mu<R>>, App<ReForgetEP.Instance.Mu<R>, ReForgetEP.Mu<R>>
/*    */ {
/*    */   static final class Mu<R>
/*    */     implements AffineP.Mu {}
/*    */   
/*    */   public <A, B, C, D> FunctionType<App2<ReForgetEP.Mu<R>, A, B>, App2<ReForgetEP.Mu<R>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 29 */     return paramApp2 -> Optics.reForgetEP("dimap", ());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ReForgetEP.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ReForgetEP.Mu<R>, A, B> paramApp2) {
/* 39 */     ReForgetEP<R, A, B> reForgetEP = ReForgetEP.unbox(paramApp2);
/* 40 */     return Optics.reForgetEP("left", paramEither -> (Either)paramEither.map((), ()));
/*    */   }
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
/*    */   public <A, B, C> App2<ReForgetEP.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ReForgetEP.Mu<R>, A, B> paramApp2) {
/* 54 */     ReForgetEP<R, A, B> reForgetEP = ReForgetEP.unbox(paramApp2);
/* 55 */     return Optics.reForgetEP("right", paramEither -> (Either)paramEither.map((), ()));
/*    */   }
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
/*    */   public <A, B, C> App2<ReForgetEP.Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<ReForgetEP.Mu<R>, A, B> paramApp2) {
/* 69 */     ReForgetEP<R, A, B> reForgetEP = ReForgetEP.unbox(paramApp2);
/* 70 */     return Optics.reForgetEP("first", paramEither -> (Pair)paramEither.map((), ()));
/*    */   }
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
/*    */   public <A, B, C> App2<ReForgetEP.Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<ReForgetEP.Mu<R>, A, B> paramApp2) {
/* 86 */     ReForgetEP<R, A, B> reForgetEP = ReForgetEP.unbox(paramApp2);
/* 87 */     return Optics.reForgetEP("second", paramEither -> (Pair)paramEither.map((), ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\ReForgetEP$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */