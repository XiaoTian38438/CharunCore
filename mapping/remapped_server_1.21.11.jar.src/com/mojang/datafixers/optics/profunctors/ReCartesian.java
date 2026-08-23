/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ 
/*    */ 
/*    */ public interface ReCartesian<P extends com.mojang.datafixers.kinds.K2, Mu extends ReCartesian.Mu>
/*    */   extends Profunctor<P, Mu>
/*    */ {
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> ReCartesian<P, Proof> unbox(App<Proof, P> paramApp) {
/* 12 */     return (ReCartesian<P, Proof>)paramApp;
/*    */   }
/*    */   
/*    */   <A, B, C> App2<P, A, B> unfirst(App2<P, Pair<A, C>, Pair<B, C>> paramApp2);
/*    */   
/*    */   <A, B, C> App2<P, A, B> unsecond(App2<P, Pair<C, A>, Pair<C, B>> paramApp2);
/*    */   
/*    */   public static interface Mu extends Profunctor.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\profunctors\ReCartesian.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */