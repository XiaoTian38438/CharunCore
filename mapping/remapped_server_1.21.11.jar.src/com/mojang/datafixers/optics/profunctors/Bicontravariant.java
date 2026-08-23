/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.Kind2;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ interface Bicontravariant<P extends com.mojang.datafixers.kinds.K2, Mu extends Bicontravariant.Mu>
/*    */   extends Kind2<P, Mu>
/*    */ {
/*    */   static <P extends com.mojang.datafixers.kinds.K2, Proof extends Mu> Bicontravariant<P, Proof> unbox(App<Proof, P> paramApp) {
/* 16 */     return (Bicontravariant<P, Proof>)paramApp;
/*    */   }
/*    */ 
/*    */   
/*    */   <A, B, C, D> FunctionType<Supplier<App2<P, A, B>>, App2<P, C, D>> cimap(Function<C, A> paramFunction, Function<D, B> paramFunction1);
/*    */ 
/*    */   
/*    */   default <A, B, C, D> App2<P, C, D> cimap(Supplier<App2<P, A, B>> paramSupplier, Function<C, A> paramFunction, Function<D, B> paramFunction1) {
/* 24 */     return (App2<P, C, D>)cimap(paramFunction, paramFunction1).apply(paramSupplier);
/*    */   }
/*    */   
/*    */   public static interface Mu extends Kind2.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\profunctors\Bicontravariant.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */