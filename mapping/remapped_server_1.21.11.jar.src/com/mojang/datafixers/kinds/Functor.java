/*   */ package com.mojang.datafixers.kinds;
/*   */ 
/*   */ import java.util.function.Function;
/*   */ 
/*   */ public interface Functor<F extends K1, Mu extends Functor.Mu>
/*   */   extends Kind1<F, Mu>
/*   */ {
/*   */   static <F extends K1, Mu extends Mu> Functor<F, Mu> unbox(App<Mu, F> paramApp) {
/* 9 */     return (Functor<F, Mu>)paramApp;
/*   */   }
/*   */   
/*   */   <T, R> App<F, R> map(Function<? super T, ? extends R> paramFunction, App<F, T> paramApp);
/*   */   
/*   */   public static interface Mu extends Kind1.Mu {}
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\Functor.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */