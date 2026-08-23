/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public interface CartesianLike<T extends K1, C, Mu extends CartesianLike.Mu>
/*    */   extends Functor<T, Mu>, Traversable<T, Mu>
/*    */ {
/*    */   static <F extends K1, C, Mu extends Mu> CartesianLike<F, C, Mu> unbox(App<Mu, F> paramApp) {
/* 11 */     return (CartesianLike)paramApp;
/*    */   }
/*    */ 
/*    */   
/*    */   <A> App<Pair.Mu<C>, A> to(App<T, A> paramApp);
/*    */ 
/*    */   
/*    */   <A> App<T, A> from(App<Pair.Mu<C>, A> paramApp);
/*    */ 
/*    */   
/*    */   default <F extends K1, A, B> App<F, App<T, B>> traverse(Applicative<F, ?> paramApplicative, Function<A, App<F, B>> paramFunction, App<T, A> paramApp) {
/* 22 */     return paramApplicative.map(this::from, (new Pair.Instance()).traverse(paramApplicative, paramFunction, to(paramApp)));
/*    */   }
/*    */   
/*    */   public static interface Mu extends Functor.Mu, Traversable.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\CartesianLike.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */