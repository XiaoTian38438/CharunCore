/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import java.util.Optional;
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
/*    */ public enum Instance
/*    */   implements Applicative<OptionalBox.Mu, OptionalBox.Instance.Mu>, Traversable<OptionalBox.Mu, OptionalBox.Instance.Mu>
/*    */ {
/* 27 */   INSTANCE;
/*    */   
/*    */   public static final class Mu
/*    */     implements Applicative.Mu, Traversable.Mu {}
/*    */   
/*    */   public <T, R> App<OptionalBox.Mu, R> map(Function<? super T, ? extends R> paramFunction, App<OptionalBox.Mu, T> paramApp) {
/* 33 */     return OptionalBox.create(OptionalBox.<T>unbox(paramApp).map(paramFunction));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A> App<OptionalBox.Mu, A> point(A paramA) {
/* 38 */     return OptionalBox.create(Optional.of(paramA));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, R> Function<App<OptionalBox.Mu, A>, App<OptionalBox.Mu, R>> lift1(App<OptionalBox.Mu, Function<A, R>> paramApp) {
/* 43 */     return paramApp2 -> OptionalBox.create(OptionalBox.unbox(paramApp1).flatMap(()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, R> BiFunction<App<OptionalBox.Mu, A>, App<OptionalBox.Mu, B>, App<OptionalBox.Mu, R>> lift2(App<OptionalBox.Mu, BiFunction<A, B, R>> paramApp) {
/* 48 */     return (paramApp2, paramApp3) -> OptionalBox.create(OptionalBox.unbox(paramApp1).flatMap(()));
/*    */   }
/*    */ 
/*    */   
/*    */   public <F extends K1, A, B> App<F, App<OptionalBox.Mu, B>> traverse(Applicative<F, ?> paramApplicative, Function<A, App<F, B>> paramFunction, App<OptionalBox.Mu, A> paramApp) {
/* 53 */     Optional<App<F, B>> optional = OptionalBox.<A>unbox(paramApp).map(paramFunction);
/* 54 */     if (optional.isPresent()) {
/* 55 */       return paramApplicative.map(paramObject -> OptionalBox.create(Optional.of(paramObject)), optional.get());
/*    */     }
/* 57 */     return paramApplicative.point(OptionalBox.create(Optional.empty()));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\OptionalBox$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */