/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Cocartesian;
/*    */ import com.mojang.datafixers.optics.profunctors.ReCartesian;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ interface ReForget<R, A, B>
/*    */   extends App2<ReForget.Mu<R>, A, B>
/*    */ {
/*    */   public static final class Mu<R>
/*    */     implements K2 {}
/*    */   
/*    */   static <R, A, B> ReForget<R, A, B> unbox(App2<Mu<R>, A, B> paramApp2) {
/* 20 */     return (ReForget)paramApp2;
/*    */   }
/*    */   
/*    */   B run(R paramR);
/*    */   
/*    */   public static final class Instance<R>
/*    */     implements ReCartesian<Mu<R>, Instance.Mu<R>>, Cocartesian<Mu<R>, Instance.Mu<R>>, App<Instance.Mu<R>, Mu<R>> {
/*    */     static final class Mu<R> implements ReCartesian.Mu, Cocartesian.Mu {}
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<ReForget.Mu<R>, A, B>, App2<ReForget.Mu<R>, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 30 */       return param1App2 -> Optics.reForget(());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForget.Mu<R>, A, B> unfirst(App2<ReForget.Mu<R>, Pair<A, C>, Pair<B, C>> param1App2) {
/* 35 */       return Optics.reForget(param1Object -> ((Pair)ReForget.unbox(param1App2).run(param1Object)).getFirst());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForget.Mu<R>, A, B> unsecond(App2<ReForget.Mu<R>, Pair<C, A>, Pair<C, B>> param1App2) {
/* 40 */       return Optics.reForget(param1Object -> ((Pair)ReForget.unbox(param1App2).run(param1Object)).getSecond());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForget.Mu<R>, Either<A, C>, Either<B, C>> left(App2<ReForget.Mu<R>, A, B> param1App2) {
/* 45 */       return Optics.reForget(param1Object -> Either.left(ReForget.unbox(param1App2).run(param1Object)));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<ReForget.Mu<R>, Either<C, A>, Either<C, B>> right(App2<ReForget.Mu<R>, A, B> param1App2) {
/* 50 */       return Optics.reForget(param1Object -> Either.right(ReForget.unbox(param1App2).run(param1Object)));
/*    */     }
/*    */   }
/*    */   
/*    */   static final class Mu<R> implements ReCartesian.Mu, Cocartesian.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\ReForget.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */