/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*    */ import com.mojang.datafixers.optics.profunctors.ReCocartesian;
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
/*    */   implements Cartesian<Forget.Mu<R>, Forget.Instance.Mu<R>>, ReCocartesian<Forget.Mu<R>, Forget.Instance.Mu<R>>, App<Forget.Instance.Mu<R>, Forget.Mu<R>>
/*    */ {
/*    */   public static final class Mu<R>
/*    */     implements Cartesian.Mu, ReCocartesian.Mu {}
/*    */   
/*    */   public <A, B, C, D> FunctionType<App2<Forget.Mu<R>, A, B>, App2<Forget.Mu<R>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 30 */     return paramApp2 -> Optics.forget(());
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<Forget.Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<Forget.Mu<R>, A, B> paramApp2) {
/* 35 */     return Optics.forget(paramPair -> Forget.unbox(paramApp2).run(paramPair.getFirst()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<Forget.Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<Forget.Mu<R>, A, B> paramApp2) {
/* 40 */     return Optics.forget(paramPair -> Forget.unbox(paramApp2).run(paramPair.getSecond()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<Forget.Mu<R>, A, B> unleft(App2<Forget.Mu<R>, Either<A, C>, Either<B, C>> paramApp2) {
/* 45 */     return Optics.forget(paramObject -> Forget.unbox(paramApp2).run(Either.left(paramObject)));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<Forget.Mu<R>, A, B> unright(App2<Forget.Mu<R>, Either<C, A>, Either<C, B>> paramApp2) {
/* 50 */     return Optics.forget(paramObject -> Forget.unbox(paramApp2).run(Either.right(paramObject)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Forget$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */