/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Profunctor;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface Adapter<S, T, A, B>
/*    */   extends App2<Adapter.Mu<A, B>, S, T>, Optic<Profunctor.Mu, S, T, A, B>
/*    */ {
/*    */   public static final class Mu<A, B>
/*    */     implements K2 {}
/*    */   
/*    */   static <S, T, A, B> Adapter<S, T, A, B> unbox(App2<Mu<A, B>, S, T> paramApp2) {
/* 17 */     return (Adapter)paramApp2;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends Profunctor.Mu, P> paramApp) {
/* 26 */     Profunctor profunctor = Profunctor.unbox(paramApp);
/* 27 */     return paramApp2 -> paramProfunctor.dimap(paramApp2, this::from, this::to);
/*    */   }
/*    */   
/*    */   A from(S paramS);
/*    */   
/*    */   T to(B paramB);
/*    */   
/*    */   public static final class Instance<A2, B2>
/*    */     implements Profunctor<Mu<A2, B2>, Profunctor.Mu> {
/*    */     public <A, B, C, D> FunctionType<App2<Adapter.Mu<A2, B2>, A, B>, App2<Adapter.Mu<A2, B2>, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 37 */       return param1App2 -> Optics.adapter((), ());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Adapter.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */