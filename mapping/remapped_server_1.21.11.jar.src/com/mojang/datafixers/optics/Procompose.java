/*    */ package com.mojang.datafixers.optics;
/*    */ 
/*    */ import com.mojang.datafixers.FunctionType;
/*    */ import com.mojang.datafixers.kinds.App2;
/*    */ import com.mojang.datafixers.kinds.K2;
/*    */ import com.mojang.datafixers.optics.profunctors.Profunctor;
/*    */ import java.util.function.Function;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public final class Procompose<F extends K2, G extends K2, A, B, C> implements App2<Procompose.Mu<F, G>, A, B> {
/*    */   private final Supplier<App2<F, A, C>> first;
/*    */   private final App2<G, C, B> second;
/*    */   
/*    */   public Procompose(Supplier<App2<F, A, C>> paramSupplier, App2<G, C, B> paramApp2) {
/* 15 */     this.first = paramSupplier;
/* 16 */     this.second = paramApp2;
/*    */   }
/*    */   
/*    */   public static final class Mu<F extends K2, G extends K2> implements K2 {}
/*    */   
/*    */   public static <F extends K2, G extends K2, A, B> Procompose<F, G, A, B, ?> unbox(App2<Mu<F, G>, A, B> paramApp2) {
/* 22 */     return (Procompose)paramApp2;
/*    */   }
/*    */ 
/*    */   
/*    */   static final class ProfunctorInstance<F extends K2, G extends K2>
/*    */     implements Profunctor<Mu<F, G>, Profunctor.Mu>
/*    */   {
/*    */     private final Profunctor<F, Profunctor.Mu> p1;
/*    */     private final Profunctor<G, Profunctor.Mu> p2;
/*    */     
/*    */     ProfunctorInstance(Profunctor<F, Profunctor.Mu> param1Profunctor, Profunctor<G, Profunctor.Mu> param1Profunctor1) {
/* 33 */       this.p1 = param1Profunctor;
/* 34 */       this.p2 = param1Profunctor1;
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, C, D> FunctionType<App2<Procompose.Mu<F, G>, A, B>, App2<Procompose.Mu<F, G>, C, D>> dimap(Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 39 */       return param1App2 -> cap(Procompose.unbox(param1App2), param1Function1, param1Function2);
/*    */     }
/*    */     
/*    */     private <A, B, C, D, E> App2<Procompose.Mu<F, G>, C, D> cap(Procompose<F, G, A, B, E> param1Procompose, Function<C, A> param1Function, Function<B, D> param1Function1) {
/* 43 */       return new Procompose<>(() -> (App2)this.p1.dimap(param1Function, Function.identity()).apply(param1Procompose.first.get()), (App2<G, ?, D>)this.p2.dimap(Function.identity(), param1Function1).apply(param1Procompose.second));
/*    */     }
/*    */   }
/*    */   
/*    */   public Supplier<App2<F, A, C>> first() {
/* 48 */     return this.first;
/*    */   }
/*    */   
/*    */   public App2<G, C, B> second() {
/* 52 */     return this.second;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\optics\Procompose.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */