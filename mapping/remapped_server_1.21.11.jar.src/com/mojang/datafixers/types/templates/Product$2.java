/*    */ package com.mojang.datafixers.types.templates;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.optics.Traversal;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements Traversal<Pair<LS, RS>, Pair<LT, RT>, A, B>
/*    */ {
/*    */   public <F extends com.mojang.datafixers.kinds.K1> FunctionType<Pair<LS, RS>, App<F, Pair<LT, RT>>> wander(Applicative<F, ?> paramApplicative, FunctionType<A, App<F, B>> paramFunctionType) {
/* 98 */     return paramPair -> paramApplicative.ap2(paramApplicative.point(Pair::of), (App)paramTraversal1.wander(paramApplicative, paramFunctionType).apply(paramPair.getFirst()), (App)paramTraversal2.wander(paramApplicative, paramFunctionType).apply(paramPair.getSecond()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\types\templates\Product$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */