/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.kinds.Traversable;
/*    */ import com.mojang.datafixers.optics.Wander;
/*    */ import java.util.function.Function;
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
/*    */   implements Wander<App<T, A>, App<T, B>, A, B>
/*    */ {
/*    */   public <F extends com.mojang.datafixers.kinds.K1> FunctionType<App<T, A>, App<F, App<T, B>>> wander(Applicative<F, ?> paramApplicative, FunctionType<A, App<F, B>> paramFunctionType) {
/* 50 */     return paramApp -> paramTraversable.traverse(paramApplicative, (Function)paramFunctionType, paramApp);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\profunctors\TraversalP$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */