/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.TraversalP;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public interface Traversal<S, T, A, B>
/*    */   extends Wander<S, T, A, B>, App2<Traversal.Mu<A, B>, S, T>, Optic<TraversalP.Mu, S, T, A, B>
/*    */ {
/*    */   public static final class Mu<A, B>
/*    */     implements K2 {}
/*    */   
/*    */   static <S, T, A, B> Traversal<S, T, A, B> unbox(App2<Mu<A, B>, S, T> paramApp2) {
/* 19 */     return (Traversal)paramApp2;
/*    */   }
/*    */ 
/*    */   
/*    */   default <P extends K2> FunctionType<App2<P, A, B>, App2<P, S, T>> eval(App<? extends TraversalP.Mu, P> paramApp) {
/* 24 */     TraversalP traversalP = TraversalP.unbox(paramApp);
/* 25 */     return paramApp2 -> paramTraversalP.wander(this, paramApp2);
/*    */   }
/*    */   
/*    */   public static final class Instance<A2, B2>
/*    */     implements TraversalP<Mu<A2, B2>, TraversalP.Mu> {
/*    */     public <A, B, C, D> FunctionType<App2<Traversal.Mu<A2, B2>, A, B>, App2<Traversal.Mu<A2, B2>, C, D>> dimap(Function<C, A> param1Function, final Function<B, D> h) {
/* 31 */       return param1App2 -> new Traversal()
/*    */         {
/*    */           public <F extends com.mojang.datafixers.kinds.K1> FunctionType<C, App<F, D>> wander(Applicative<F, ?> param2Applicative, FunctionType<A2, App<F, B2>> param2FunctionType) {
/* 34 */             return param2Object -> param2Applicative.map(param2Function1, (App)Traversal.unbox(param2App2).wander(param2Applicative, param2FunctionType).apply(param2Function2.apply(param2Object)));
/*    */           }
/*    */         };
/*    */     }
/*    */ 
/*    */     
/*    */     public <S, T, A, B> App2<Traversal.Mu<A2, B2>, S, T> wander(final Wander<S, T, A, B> wander, final App2<Traversal.Mu<A2, B2>, A, B> input) {
/* 41 */       return new Traversal<S, T, A2, B2>()
/*    */         {
/*    */           public <F extends com.mojang.datafixers.kinds.K1> FunctionType<S, App<F, T>> wander(Applicative<F, ?> param2Applicative, FunctionType<A2, App<F, B2>> param2FunctionType) {
/* 44 */             return wander.wander(param2Applicative, Traversal.unbox(input).wander(param2Applicative, param2FunctionType)); } }; } } class null implements Traversal<C, D, A2, B2> { public <F extends com.mojang.datafixers.kinds.K1> FunctionType<C, App<F, D>> wander(Applicative<F, ?> param1Applicative, FunctionType<A2, App<F, B2>> param1FunctionType) { return param1Object -> param1Applicative.map(param1Function1, (App)Traversal.unbox(param1App2).wander(param1Applicative, param1FunctionType).apply(param1Function2.apply(param1Object))); } } class null implements Traversal<S, T, A2, B2> { public <F extends com.mojang.datafixers.kinds.K1> FunctionType<S, App<F, T>> wander(Applicative<F, ?> param1Applicative, FunctionType<A2, App<F, B2>> param1FunctionType) { return wander.wander(param1Applicative, Traversal.unbox(input).wander(param1Applicative, param1FunctionType)); }
/*    */      }
/*    */ 
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Traversal.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */