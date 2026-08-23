/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*    */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*    */ import com.mojang.datafixers.optics.profunctors.Cocartesian;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface Affine<S, T, A, B>
/*    */   extends App2<Affine.Mu<A, B>, S, T>, Optic<AffineP.Mu, S, T, A, B>
/*    */ {
/*    */   public static final class Mu<A, B>
/*    */     implements K2 {}
/*    */   
/*    */   static <S, T, A, B> Affine<S, T, A, B> unbox(App2<Mu<A, B>, S, T> paramApp2) {
/* 21 */     return (Affine)paramApp2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends AffineP.Mu, P> paramApp) {
/* 30 */     Cartesian cartesian = Cartesian.unbox(paramApp);
/* 31 */     Cocartesian cocartesian = Cocartesian.unbox(paramApp);
/* 32 */     return paramApp2 -> paramCartesian.dimap(paramCocartesian.left(paramCartesian.rmap(paramCartesian.first(paramApp2), ())), (), Either::unwrap);
/*    */   }
/*    */   
/*    */   Either<T, A> preview(S paramS);
/*    */   
/*    */   T set(B paramB, S paramS);
/*    */   
/*    */   public static final class Instance<A2, B2>
/*    */     implements AffineP<Mu<A2, B2>, AffineP.Mu> {
/*    */     public <A, B, C, D> FunctionType<App2<Affine.Mu<A2, B2>, A, B>, App2<Affine.Mu<A2, B2>, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 42 */       return param1App2 -> Optics.affine((), ());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Affine.Mu<A2, B2>, Pair<A, C>, Pair<B, C>> first(App2<Affine.Mu<A2, B2>, A, B> param1App2) {
/* 50 */       Affine<A, B, A2, B2> affine = Affine.unbox(param1App2);
/* 51 */       return Optics.affine(param1Pair -> param1Affine.preview(param1Pair.getFirst()).mapBoth((), Function.identity()), (param1Object, param1Pair) -> Pair.of(param1Affine.set(param1Object, param1Pair.getFirst()), param1Pair.getSecond()));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Affine.Mu<A2, B2>, Pair<C, A>, Pair<C, B>> second(App2<Affine.Mu<A2, B2>, A, B> param1App2) {
/* 59 */       Affine<A, B, A2, B2> affine = Affine.unbox(param1App2);
/* 60 */       return Optics.affine(param1Pair -> param1Affine.preview(param1Pair.getSecond()).mapBoth((), Function.identity()), (param1Object, param1Pair) -> Pair.of(param1Pair.getFirst(), param1Affine.set(param1Object, param1Pair.getSecond())));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Affine.Mu<A2, B2>, Either<A, C>, Either<B, C>> left(App2<Affine.Mu<A2, B2>, A, B> param1App2) {
/* 68 */       Affine<A, B, A2, B2> affine = Affine.unbox(param1App2);
/* 69 */       return Optics.affine(param1Either -> (Either)param1Either.map((), ()), (param1Object, param1Either) -> (Either)param1Either.map((), Either::right));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Affine.Mu<A2, B2>, Either<C, A>, Either<C, B>> right(App2<Affine.Mu<A2, B2>, A, B> param1App2) {
/* 80 */       Affine<A, B, A2, B2> affine = Affine.unbox(param1App2);
/* 81 */       return Optics.affine(param1Either -> (Either)param1Either.map((), ()), (param1Object, param1Either) -> (Either)param1Either.map(Either::left, ()));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Affine.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */