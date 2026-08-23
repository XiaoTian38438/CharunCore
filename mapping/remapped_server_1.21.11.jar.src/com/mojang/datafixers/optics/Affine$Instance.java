/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
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
/*    */ public final class Instance<A2, B2>
/*    */   implements AffineP<Affine.Mu<A2, B2>, AffineP.Mu>
/*    */ {
/*    */   public <A, B, C, D> FunctionType<App2<Affine.Mu<A2, B2>, A, B>, App2<Affine.Mu<A2, B2>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 42 */     return paramApp2 -> Optics.affine((), ());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<Affine.Mu<A2, B2>, Pair<A, C>, Pair<B, C>> first(App2<Affine.Mu<A2, B2>, A, B> paramApp2) {
/* 50 */     Affine<A, B, A2, B2> affine = Affine.unbox(paramApp2);
/* 51 */     return Optics.affine(paramPair -> paramAffine.preview(paramPair.getFirst()).mapBoth((), Function.identity()), (paramObject, paramPair) -> Pair.of(paramAffine.set(paramObject, paramPair.getFirst()), paramPair.getSecond()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<Affine.Mu<A2, B2>, Pair<C, A>, Pair<C, B>> second(App2<Affine.Mu<A2, B2>, A, B> paramApp2) {
/* 59 */     Affine<A, B, A2, B2> affine = Affine.unbox(paramApp2);
/* 60 */     return Optics.affine(paramPair -> paramAffine.preview(paramPair.getSecond()).mapBoth((), Function.identity()), (paramObject, paramPair) -> Pair.of(paramPair.getFirst(), paramAffine.set(paramObject, paramPair.getSecond())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<Affine.Mu<A2, B2>, Either<A, C>, Either<B, C>> left(App2<Affine.Mu<A2, B2>, A, B> paramApp2) {
/* 68 */     Affine<A, B, A2, B2> affine = Affine.unbox(paramApp2);
/* 69 */     return Optics.affine(paramEither -> (Either)paramEither.map((), ()), (paramObject, paramEither) -> (Either)paramEither.map((), Either::right));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<Affine.Mu<A2, B2>, Either<C, A>, Either<C, B>> right(App2<Affine.Mu<A2, B2>, A, B> paramApp2) {
/* 80 */     Affine<A, B, A2, B2> affine = Affine.unbox(paramApp2);
/* 81 */     return Optics.affine(paramEither -> (Either)paramEither.map((), ()), (paramObject, paramEither) -> (Either)paramEither.map(Either::left, ()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Affine$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */