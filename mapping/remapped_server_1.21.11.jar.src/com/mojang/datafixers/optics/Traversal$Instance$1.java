/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.Applicative;
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
/*    */ class null
/*    */   implements Traversal<C, D, A2, B2>
/*    */ {
/*    */   public <F extends com.mojang.datafixers.kinds.K1> FunctionType<C, App<F, D>> wander(Applicative<F, ?> paramApplicative, FunctionType<A2, App<F, B2>> paramFunctionType) {
/* 34 */     return paramObject -> paramApplicative.map(paramFunction1, (App)Traversal.unbox(paramApp2).wander(paramApplicative, paramFunctionType).apply(paramFunction2.apply(paramObject)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Traversal$Instance$1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */