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
/*    */ public class ProfunctorFunctorWrapper<P extends K2, F extends K1, G extends K1, A, B>
/*    */   implements App2<ProfunctorFunctorWrapper.Mu<P, F, G>, A, B> {
/*    */   private final App2<P, App<F, A>, App<G, B>> value;
/*    */   
/*    */   public static final class Mu<P extends K2, F extends K1, G extends K1> implements K2 {}
/*    */   
/*    */   public static <P extends K2, F extends K1, G extends K1, A, B> ProfunctorFunctorWrapper<P, F, G, A, B> unbox(App2<Mu<P, F, G>, A, B> paramApp2) {
/* 18 */     return (ProfunctorFunctorWrapper)paramApp2;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ProfunctorFunctorWrapper(App2<P, App<F, A>, App<G, B>> paramApp2) {
/* 24 */     this.value = paramApp2;
/*    */   }
/*    */   
/*    */   public App2<P, App<F, A>, App<G, B>> value() {
/* 28 */     return this.value;
/*    */   }
/*    */   
/*    */   public static final class Instance<P extends K2, F extends K1, G extends K1> implements Profunctor<Mu<P, F, G>, Instance.Mu>, App<Instance.Mu, Mu<P, F, G>> {
/*    */     private final Profunctor<P, ? extends Profunctor.Mu> profunctor;
/*    */     private final Functor<F, ?> fFunctor;
/*    */     private final Functor<G, ?> gFunctor;
/*    */     
/*    */     public static final class Mu implements Profunctor.Mu {}
/*    */     
/*    */     public Instance(App<? extends Profunctor.Mu, P> param1App, Functor<F, ?> param1Functor, Functor<G, ?> param1Functor1) {
/* 39 */       this.profunctor = Profunctor.unbox(param1App);
/* 40 */       this.fFunctor = param1Functor;
/* 41 */       this.gFunctor = param1Functor1;
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<ProfunctorFunctorWrapper.Mu<P, F, G>, A, B>, App2<ProfunctorFunctorWrapper.Mu<P, F, G>, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 46 */       return param1App2 -> {
/*    */           App2<P, ?, ?> app21 = ProfunctorFunctorWrapper.unbox(param1App2).value();
/*    */           App2<P, ?, ?> app22 = this.profunctor.dimap(app21, (), ());
/*    */           return new ProfunctorFunctorWrapper<>((App2)app22);
/*    */         };
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Mu implements Profunctor.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\profunctors\ProfunctorFunctorWrapper.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */