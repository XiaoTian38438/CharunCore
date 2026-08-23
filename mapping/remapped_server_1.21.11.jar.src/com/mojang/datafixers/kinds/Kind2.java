/*   */ package com.mojang.datafixers.kinds;
/*   */ 
/*   */ public interface Kind2<F extends K2, Mu extends Kind2.Mu>
/*   */   extends App<Mu, F>
/*   */ {
/*   */   static <F extends K2, Proof extends Mu> Kind2<F, Proof> unbox(App<Proof, F> paramApp) {
/* 7 */     return (Kind2<F, Proof>)paramApp;
/*   */   }
/*   */   
/*   */   public static interface Mu extends K1 {}
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\Kind2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */