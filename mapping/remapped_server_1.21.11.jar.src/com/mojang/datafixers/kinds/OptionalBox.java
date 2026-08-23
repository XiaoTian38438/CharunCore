/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class OptionalBox<T> implements App<OptionalBox.Mu, T> {
/*    */   private final Optional<T> value;
/*    */   
/*    */   public static final class Mu implements K1 {}
/*    */   
/*    */   public static <T> Optional<T> unbox(App<Mu, T> paramApp) {
/* 13 */     return ((OptionalBox)paramApp).value;
/*    */   }
/*    */   
/*    */   public static <T> OptionalBox<T> create(Optional<T> paramOptional) {
/* 17 */     return new OptionalBox<>(paramOptional);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private OptionalBox(Optional<T> paramOptional) {
/* 23 */     this.value = paramOptional;
/*    */   }
/*    */   
/*    */   public enum Instance implements Applicative<Mu, Instance.Mu>, Traversable<Mu, Instance.Mu> {
/* 27 */     INSTANCE;
/*    */     
/*    */     public static final class Mu
/*    */       implements Applicative.Mu, Traversable.Mu {}
/*    */     
/*    */     public <T, R> App<OptionalBox.Mu, R> map(Function<? super T, ? extends R> param1Function, App<OptionalBox.Mu, T> param1App) {
/* 33 */       return OptionalBox.create(OptionalBox.<T>unbox(param1App).map(param1Function));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A> App<OptionalBox.Mu, A> point(A param1A) {
/* 38 */       return OptionalBox.create(Optional.of(param1A));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, R> Function<App<OptionalBox.Mu, A>, App<OptionalBox.Mu, R>> lift1(App<OptionalBox.Mu, Function<A, R>> param1App) {
/* 43 */       return param1App2 -> OptionalBox.create(OptionalBox.unbox(param1App1).flatMap(()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, R> BiFunction<App<OptionalBox.Mu, A>, App<OptionalBox.Mu, B>, App<OptionalBox.Mu, R>> lift2(App<OptionalBox.Mu, BiFunction<A, B, R>> param1App) {
/* 48 */       return (param1App2, param1App3) -> OptionalBox.create(OptionalBox.unbox(param1App1).flatMap(()));
/*    */     }
/*    */ 
/*    */     
/*    */     public <F extends K1, A, B> App<F, App<OptionalBox.Mu, B>> traverse(Applicative<F, ?> param1Applicative, Function<A, App<F, B>> param1Function, App<OptionalBox.Mu, A> param1App) {
/* 53 */       Optional<App<F, B>> optional = OptionalBox.<A>unbox(param1App).map(param1Function);
/* 54 */       if (optional.isPresent()) {
/* 55 */         return param1Applicative.map(param1Object -> OptionalBox.create(Optional.of(param1Object)), optional.get());
/*    */       }
/* 57 */       return param1Applicative.point(OptionalBox.create(Optional.empty()));
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Mu implements Applicative.Mu, Traversable.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\OptionalBox.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */