/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.optics.profunctors.Closed;
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
/*    */ public final class Instance<A2, B2>
/*    */   implements Closed<Grate.Mu<A2, B2>, Closed.Mu>
/*    */ {
/*    */   public <A, B, C, D> FunctionType<App2<Grate.Mu<A2, B2>, A, B>, App2<Grate.Mu<A2, B2>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 31 */     return paramApp2 -> Optics.grate(());
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, X> App2<Grate.Mu<A2, B2>, FunctionType<X, A>, FunctionType<X, B>> closed(App2<Grate.Mu<A2, B2>, A, B> paramApp2) {
/* 36 */     FunctionType<FunctionType<FunctionType<?, ?>, ?>, ?> functionType = paramFunctionType -> ();
/* 37 */     return (App2<Grate.Mu<A2, B2>, FunctionType<X, A>, FunctionType<X, B>>)Optics.grate(functionType).eval((App)this).apply(Grate.unbox(paramApp2));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Grate$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */