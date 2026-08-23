/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface GetterP<P extends com.mojang.datafixers.kinds.K2, Mu extends GetterP.Mu>
/*    */   extends Profunctor<P, Mu>, Bicontravariant<P, Mu>
/*    */ {
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> GetterP<P, Proof> unbox(App<Proof, P> paramApp) {
/* 13 */     return (GetterP<P, Proof>)paramApp;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   default <A, B, C> App2<P, C, A> secondPhantom(App2<P, C, B> paramApp2) {
/* 19 */     return cimap(() -> rmap(paramApp2, ()), Function.identity(), paramObject -> null);
/*    */   }
/*    */   
/*    */   public static interface Mu extends Profunctor.Mu, Bicontravariant.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\profunctors\GetterP.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */