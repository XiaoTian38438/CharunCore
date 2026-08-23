/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.Applicative;
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
/*    */   implements Traversal<S, T, A2, B2>
/*    */ {
/*    */   public <F extends com.mojang.datafixers.kinds.K1> FunctionType<S, App<F, T>> wander(Applicative<F, ?> paramApplicative, FunctionType<A2, App<F, B2>> paramFunctionType) {
/* 44 */     return wander.wander(paramApplicative, Traversal.unbox(input).wander(paramApplicative, paramFunctionType));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Traversal$Instance$2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */