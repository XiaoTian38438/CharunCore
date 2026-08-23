/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Profunctor;
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
/*    */ final class ProfunctorInstance<F extends K2, G extends K2>
/*    */   implements Profunctor<Procompose.Mu<F, G>, Profunctor.Mu>
/*    */ {
/*    */   private final Profunctor<F, Profunctor.Mu> p1;
/*    */   private final Profunctor<G, Profunctor.Mu> p2;
/*    */   
/*    */   ProfunctorInstance(Profunctor<F, Profunctor.Mu> paramProfunctor, Profunctor<G, Profunctor.Mu> paramProfunctor1) {
/* 33 */     this.p1 = paramProfunctor;
/* 34 */     this.p2 = paramProfunctor1;
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C, D> FunctionType<App2<Procompose.Mu<F, G>, A, B>, App2<Procompose.Mu<F, G>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 39 */     return paramApp2 -> cap(Procompose.unbox(paramApp2), paramFunction1, paramFunction2);
/*    */   }
/*    */   
/*    */   private <A, B, C, D, E> App2<Procompose.Mu<F, G>, C, D> cap(Procompose<F, G, A, B, E> paramProcompose, Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 43 */     return new Procompose<>(() -> (App2)this.p1.dimap(paramFunction, Function.identity()).apply(paramProcompose.first.get()), (App2<G, ?, D>)this.p2.dimap(Function.identity(), paramFunction1).apply(paramProcompose.second));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Procompose$ProfunctorInstance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */