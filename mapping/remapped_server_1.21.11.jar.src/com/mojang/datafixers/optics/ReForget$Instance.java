/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.optics.profunctors.Cocartesian;
/*    */ import com.mojang.datafixers.optics.profunctors.ReCartesian;
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
/*    */   implements ReCartesian<ReForget.Mu<R>, ReForget.Instance.Mu<R>>, Cocartesian<ReForget.Mu<R>, ReForget.Instance.Mu<R>>, App<ReForget.Instance.Mu<R>, ReForget.Mu<R>>
/*    */ {
/*    */   static final class Mu<R>
/*    */     implements ReCartesian.Mu, Cocartesian.Mu {}
/*    */   
/*    */   public <A, B, C, D> FunctionType<App2<ReForget.Mu<R>, A, B>, App2<ReForget.Mu<R>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 30 */     return paramApp2 -> Optics.reForget(());
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ReForget.Mu<R>, A, B> unfirst(App2<ReForget.Mu<R>, Pair<A, C>, Pair<B, C>> paramApp2) {
/* 35 */     return Optics.reForget(paramObject -> ((Pair)ReForget.unbox(paramApp2).run(paramObject)).getFirst());
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ReForget.Mu<R>, A, B> unsecond(App2<ReForget.Mu<R>, Pair<C, A>, Pair<C, B>> paramApp2) {
/* 40 */     return Optics.reForget(paramObject -> ((Pair)ReForget.unbox(paramApp2).run(paramObject)).getSecond());
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ReForget.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ReForget.Mu<R>, A, B> paramApp2) {
/* 45 */     return Optics.reForget(paramObject -> Either.left(ReForget.unbox(paramApp2).run(paramObject)));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C> App2<ReForget.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ReForget.Mu<R>, A, B> paramApp2) {
/* 50 */     return Optics.reForget(paramObject -> Either.right(ReForget.unbox(paramApp2).run(paramObject)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\ReForget$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */