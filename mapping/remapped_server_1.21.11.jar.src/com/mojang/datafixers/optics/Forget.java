/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Cartesian;
/*    */ import com.mojang.datafixers.optics.profunctors.ReCocartesian;
/*    */ import com.mojang.datafixers.util.Either;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface Forget<R, A, B>
/*    */   extends App2<Forget.Mu<R>, A, B>
/*    */ {
/*    */   public static final class Mu<R>
/*    */     implements K2 {}
/*    */   
/*    */   static <R, A, B> Forget<R, A, B> unbox(App2<Mu<R>, A, B> paramApp2) {
/* 20 */     return (Forget)paramApp2;
/*    */   }
/*    */   
/*    */   R run(A paramA);
/*    */   
/*    */   public static final class Instance<R>
/*    */     implements Cartesian<Mu<R>, Instance.Mu<R>>, ReCocartesian<Mu<R>, Instance.Mu<R>>, App<Instance.Mu<R>, Mu<R>> {
/*    */     public static final class Mu<R> implements Cartesian.Mu, ReCocartesian.Mu {}
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<Forget.Mu<R>, A, B>, App2<Forget.Mu<R>, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 30 */       return param1App2 -> Optics.forget(());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Forget.Mu<R>, Pair<A, C>, Pair<B, C>> first(App2<Forget.Mu<R>, A, B> param1App2) {
/* 35 */       return Optics.forget(param1Pair -> Forget.unbox(param1App2).run(param1Pair.getFirst()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Forget.Mu<R>, Pair<C, A>, Pair<C, B>> second(App2<Forget.Mu<R>, A, B> param1App2) {
/* 40 */       return Optics.forget(param1Pair -> Forget.unbox(param1App2).run(param1Pair.getSecond()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Forget.Mu<R>, A, B> unleft(App2<Forget.Mu<R>, Either<A, C>, Either<B, C>> param1App2) {
/* 45 */       return Optics.forget(param1Object -> Forget.unbox(param1App2).run(Either.left(param1Object)));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C> App2<Forget.Mu<R>, A, B> unright(App2<Forget.Mu<R>, Either<C, A>, Either<C, B>> param1App2) {
/* 50 */       return Optics.forget(param1Object -> Forget.unbox(param1App2).run(Either.right(param1Object)));
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Mu<R> implements Cartesian.Mu, ReCocartesian.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Forget.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */