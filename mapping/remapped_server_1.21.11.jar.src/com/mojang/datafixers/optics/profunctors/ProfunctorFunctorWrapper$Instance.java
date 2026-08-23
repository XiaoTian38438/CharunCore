/*    */ package com.mojang.datafixers.optics.profunctors;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.Functor;
/*    */ import com.mojang.datafixers.kinds.K1;
/*    */ import com.mojang.datafixers.kinds.K2;
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
/*    */ public final class Instance<P extends K2, F extends K1, G extends K1>
/*    */   implements Profunctor<ProfunctorFunctorWrapper.Mu<P, F, G>, ProfunctorFunctorWrapper.Instance.Mu>, App<ProfunctorFunctorWrapper.Instance.Mu, ProfunctorFunctorWrapper.Mu<P, F, G>>
/*    */ {
/*    */   private final Profunctor<P, ? extends Profunctor.Mu> profunctor;
/*    */   private final Functor<F, ?> fFunctor;
/*    */   private final Functor<G, ?> gFunctor;
/*    */   
/*    */   public static final class Mu
/*    */     implements Profunctor.Mu {}
/*    */   
/*    */   public Instance(App<? extends Profunctor.Mu, P> paramApp, Functor<F, ?> paramFunctor, Functor<G, ?> paramFunctor1) {
/* 39 */     this.profunctor = Profunctor.unbox(paramApp);
/* 40 */     this.fFunctor = paramFunctor;
/* 41 */     this.gFunctor = paramFunctor1;
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, C, D> FunctionType<App2<ProfunctorFunctorWrapper.Mu<P, F, G>, A, B>, App2<ProfunctorFunctorWrapper.Mu<P, F, G>, C, D>> dimap(Function<C, A> paramFunction, Function<B, D> paramFunction1) {
/* 46 */     return paramApp2 -> {
/*    */         App2<P, ?, ?> app21 = ProfunctorFunctorWrapper.unbox(paramApp2).value();
/*    */         App2<P, ?, ?> app22 = this.profunctor.dimap(app21, (), ());
/*    */         return new ProfunctorFunctorWrapper<>((App2)app22);
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\profunctors\ProfunctorFunctorWrapper$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */