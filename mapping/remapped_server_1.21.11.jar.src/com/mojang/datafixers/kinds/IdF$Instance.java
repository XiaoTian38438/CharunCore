/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import java.util.function.BiFunction;
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
/*    */ public enum Instance
/*    */   implements Functor<IdF.Mu, IdF.Instance.Mu>, Applicative<IdF.Mu, IdF.Instance.Mu>
/*    */ {
/* 30 */   INSTANCE;
/*    */   
/*    */   public static final class Mu
/*    */     implements Functor.Mu, Applicative.Mu {}
/*    */   
/*    */   public <T, R> App<IdF.Mu, R> map(Function<? super T, ? extends R> paramFunction, App<IdF.Mu, T> paramApp) {
/* 36 */     IdF idF = (IdF)paramApp;
/* 37 */     return new IdF<>(paramFunction.apply((T)idF.value));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A> App<IdF.Mu, A> point(A paramA) {
/* 42 */     return IdF.create(paramA);
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, R> Function<App<IdF.Mu, A>, App<IdF.Mu, R>> lift1(App<IdF.Mu, Function<A, R>> paramApp) {
/* 47 */     return paramApp2 -> IdF.create(((Function)IdF.<Function>get(paramApp1)).apply(IdF.get(paramApp2)));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, R> BiFunction<App<IdF.Mu, A>, App<IdF.Mu, B>, App<IdF.Mu, R>> lift2(App<IdF.Mu, BiFunction<A, B, R>> paramApp) {
/* 52 */     return (paramApp2, paramApp3) -> IdF.create(((BiFunction)IdF.<BiFunction>get(paramApp1)).apply(IdF.get(paramApp2), IdF.get(paramApp3)));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\IdF$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */