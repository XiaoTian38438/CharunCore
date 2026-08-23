/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class IdF<A>
/*    */   implements App<IdF.Mu, A> {
/*    */   protected final A value;
/*    */   
/*    */   public static final class Mu
/*    */     implements K1 {}
/*    */   
/*    */   IdF(A paramA) {
/* 14 */     this.value = paramA;
/*    */   }
/*    */   
/*    */   public A value() {
/* 18 */     return this.value;
/*    */   }
/*    */   
/*    */   public static <A> A get(App<Mu, A> paramApp) {
/* 22 */     return ((IdF)paramApp).value;
/*    */   }
/*    */   
/*    */   public static <A> IdF<A> create(A paramA) {
/* 26 */     return new IdF<>(paramA);
/*    */   }
/*    */   
/*    */   public enum Instance implements Functor<Mu, Instance.Mu>, Applicative<Mu, Instance.Mu> {
/* 30 */     INSTANCE;
/*    */     
/*    */     public static final class Mu
/*    */       implements Functor.Mu, Applicative.Mu {}
/*    */     
/*    */     public <T, R> App<IdF.Mu, R> map(Function<? super T, ? extends R> param1Function, App<IdF.Mu, T> param1App) {
/* 36 */       IdF idF = (IdF)param1App;
/* 37 */       return new IdF<>(param1Function.apply((T)idF.value));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A> App<IdF.Mu, A> point(A param1A) {
/* 42 */       return IdF.create(param1A);
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, R> Function<App<IdF.Mu, A>, App<IdF.Mu, R>> lift1(App<IdF.Mu, Function<A, R>> param1App) {
/* 47 */       return param1App2 -> IdF.create(((Function)IdF.<Function>get(param1App1)).apply(IdF.get(param1App2)));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, R> BiFunction<App<IdF.Mu, A>, App<IdF.Mu, B>, App<IdF.Mu, R>> lift2(App<IdF.Mu, BiFunction<A, B, R>> param1App) {
/* 52 */       return (param1App2, param1App3) -> IdF.create(((BiFunction)IdF.<BiFunction>get(param1App1)).apply(IdF.get(param1App2), IdF.get(param1App3)));
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Mu implements Functor.Mu, Applicative.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\IdF.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */