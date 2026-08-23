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
/*    */   implements AffineP<ReForgetP.Mu<R>, ReForgetP.Instance.Mu<R>>, App<ReForgetP.Instance.Mu<R>, ReForgetP.Mu<R>>
/*    */ {
/*    */   static final class Mu<R>
/*    */     implements AffineP.Mu {}
/*    */   
/*    */   public <A, B, C, D> FunctionType<App2<ReForgetP.Mu<R>, A, B>, App2<ReForgetP.Mu<R>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 29 */     return paramApp2 -> Optics.reForgetP("dimap", ());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ReForgetP.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ReForgetP.Mu<R>, A, B> paramApp2) {
/* 39 */     return Optics.reForgetP("left", (paramEither, paramObject) -> paramEither.mapLeft(()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ReForgetP.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ReForgetP.Mu<R>, A, B> paramApp2) {
/* 44 */     return Optics.reForgetP("right", (paramEither, paramObject) -> paramEither.mapRight(()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ReForgetP.Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<ReForgetP.Mu<R>, A, B> paramApp2) {
/* 49 */     return Optics.reForgetP("first", (paramPair, paramObject) -> Pair.of(ReForgetP.unbox(paramApp2).run(paramPair.getFirst(), paramObject), paramPair.getSecond()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ReForgetP.Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<ReForgetP.Mu<R>, A, B> paramApp2) {
/* 54 */     return Optics.reForgetP("second", (paramPair, paramObject) -> Pair.of(paramPair.getFirst(), ReForgetP.unbox(paramApp2).run(paramPair.getSecond(), paramObject)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\ReForgetP$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */