/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.optics.profunctors.TraversalP;
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
/*    */ public final class Instance<A2, B2>
/*    */   implements TraversalP<Traversal.Mu<A2, B2>, TraversalP.Mu>
/*    */ {
/*    */   public <A, B, C, D> FunctionType<App2<Traversal.Mu<A2, B2>, A, B>, App2<Traversal.Mu<A2, B2>, C, D>> dimap(Function<C, A> paramFunction, final Function<B, D> h) {
/* 31 */     return paramApp2 -> new Traversal()
/*    */       {
/*    */         public <F extends com.mojang.datafixers.kinds.K1> FunctionType<C, App<F, D>> wander(Applicative<F, ?> param2Applicative, FunctionType<A2, App<F, B2>> param2FunctionType) {
/* 34 */           return param2Object -> param2Applicative.map(param2Function1, (App)Traversal.unbox(param2App2).wander(param2Applicative, param2FunctionType).apply(param2Function2.apply(param2Object)));
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */   
/*    */   public <S, T, A, B> App2<Traversal.Mu<A2, B2>, S, T> wander(final Wander<S, T, A, B> wander, final App2<Traversal.Mu<A2, B2>, A, B> input) {
/* 41 */     return new Traversal<S, T, A2, B2>()
/*    */       {
/*    */         public <F extends com.mojang.datafixers.kinds.K1> FunctionType<S, App<F, T>> wander(Applicative<F, ?> param2Applicative, FunctionType<A2, App<F, B2>> param2FunctionType) {
/* 44 */           return wander.wander(param2Applicative, Traversal.unbox(input).wander(param2Applicative, param2FunctionType));
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Traversal$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */