/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.optics.profunctors.AffineP;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.BiFunction;
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
/*    */ public final class Instance<R>
/*    */   implements AffineP<ReForgetC.Mu<R>, ReForgetC.Instance.Mu<R>>, App<ReForgetC.Instance.Mu<R>, ReForgetC.Mu<R>>
/*    */ {
/*    */   public static final class Mu<R>
/*    */     implements AffineP.Mu {}
/*    */   
/*    */   public <A, B, C, D> FunctionType<App2<ReForgetC.Mu<R>, A, B>, App2<ReForgetC.Mu<R>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 34 */     return paramApp2 -> Optics.reForgetC("dimap", (Either<Function<?, ?>, BiFunction<?, ?, ?>>)ReForgetC.<R, A, B>unbox(paramApp2).impl().map((), ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ReForgetC.Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<ReForgetC.Mu<R>, A, B> paramApp2) {
/* 42 */     return Optics.reForgetC("first", (Either<Function<R, Pair<B, C>>, BiFunction<Pair<A, C>, R, Pair<B, C>>>)ReForgetC.<R, A, B>unbox(paramApp2).impl().map(paramFunction -> Either.right(()), paramBiFunction -> Either.right(())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ReForgetC.Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<ReForgetC.Mu<R>, A, B> paramApp2) {
/* 51 */     return Optics.reForgetC("second", (Either<Function<R, Pair<C, B>>, BiFunction<Pair<C, A>, R, Pair<C, B>>>)ReForgetC.<R, A, B>unbox(paramApp2).impl().map(paramFunction -> Either.right(()), paramBiFunction -> Either.right(())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ReForgetC.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ReForgetC.Mu<R>, A, B> paramApp2) {
/* 60 */     return Optics.reForgetC("left", (Either<Function<R, Either<B, C>>, BiFunction<Either<A, C>, R, Either<B, C>>>)ReForgetC.<R, A, B>unbox(paramApp2).impl().map(paramFunction -> Either.left(()), paramBiFunction -> Either.right(())));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ReForgetC.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ReForgetC.Mu<R>, A, B> paramApp2) {
/* 70 */     return Optics.reForgetC("right", (Either<Function<R, Either<C, B>>, BiFunction<Either<C, A>, R, Either<C, B>>>)ReForgetC.<R, A, B>unbox(paramApp2).impl().map(paramFunction -> Either.left(()), paramBiFunction -> Either.right(())));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\ReForgetC$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */