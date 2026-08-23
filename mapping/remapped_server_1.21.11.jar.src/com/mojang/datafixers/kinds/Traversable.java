/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public interface Traversable<T extends K1, Mu extends Traversable.Mu>
/*    */   extends Functor<T, Mu>
/*    */ {
/*    */   static <F extends K1, Mu extends Mu> Traversable<F, Mu> unbox(App<Mu, F> paramApp) {
/*  9 */     return (Traversable<F, Mu>)paramApp;
/*    */   }
/*    */ 
/*    */   
/*    */   <F extends K1, A, B> App<F, App<T, B>> traverse(Applicative<F, ?> paramApplicative, Function<A, App<F, B>> paramFunction, App<T, A> paramApp);
/*    */ 
/*    */   
/*    */   default <F extends K1, A> App<F, App<T, A>> flip(Applicative<F, ?> paramApplicative, App<T, App<F, A>> paramApp) {
/* 17 */     return traverse(paramApplicative, Function.identity(), paramApp);
/*    */   }
/*    */   
/*    */   public static interface Mu extends Functor.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\Traversable.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */