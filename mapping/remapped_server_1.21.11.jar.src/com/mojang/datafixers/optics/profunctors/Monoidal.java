/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Monoidal<P extends com.mojang.datafixers.kinds.K2, Mu extends Monoidal.Mu>
/*    */   extends Profunctor<P, Mu>
/*    */ {
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> Monoidal<P, Proof> unbox(App<Proof, P> paramApp) {
/* 14 */     return (Monoidal<P, Proof>)paramApp;
/*    */   }
/*    */   
/*    */   <A, B, C, D> App2<P, Pair<A, C>, Pair<B, D>> par(App2<P, A, B> paramApp2, Supplier<App2<P, C, D>> paramSupplier);
/*    */   
/*    */   App2<P, Void, Void> empty();
/*    */   
/*    */   public static interface Mu extends Profunctor.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\profunctors\Monoidal.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */