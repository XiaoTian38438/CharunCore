/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class Const<C, T> implements App<Const.Mu<C>, T> {
/*    */   private final C value;
/*    */   
/*    */   public static final class Mu<C> implements K1 {}
/*    */   
/*    */   public static <C, T> C unbox(App<Mu<C>, T> paramApp) {
/* 12 */     return ((Const)paramApp).value;
/*    */   }
/*    */   
/*    */   public static <C, T> Const<C, T> create(C paramC) {
/* 16 */     return new Const<>(paramC);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   Const(C paramC) {
/* 22 */     this.value = paramC;
/*    */   }
/*    */   
/*    */   public static final class Instance<C> implements Applicative<Mu<C>, Instance.Mu<C>> {
/*    */     private final Monoid<C> monoid;
/*    */     
/*    */     public static final class Mu<C> implements Applicative.Mu {}
/*    */     
/*    */     public Instance(Monoid<C> param1Monoid) {
/* 31 */       this.monoid = param1Monoid;
/*    */     }
/*    */ 
/*    */     
/*    */     public <T, R> App<Const.Mu<C>, R> map(Function<? super T, ? extends R> param1Function, App<Const.Mu<C>, T> param1App) {
/* 36 */       return Const.create(Const.unbox(param1App));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A> App<Const.Mu<C>, A> point(A param1A) {
/* 41 */       return Const.create(this.monoid.point());
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, R> Function<App<Const.Mu<C>, A>, App<Const.Mu<C>, R>> lift1(App<Const.Mu<C>, Function<A, R>> param1App) {
/* 46 */       return param1App2 -> Const.create(this.monoid.add(Const.unbox(param1App1), Const.unbox(param1App2)));
/*    */     }
/*    */ 
/*    */     
/*    */     public <A, B, R> BiFunction<App<Const.Mu<C>, A>, App<Const.Mu<C>, B>, App<Const.Mu<C>, R>> lift2(App<Const.Mu<C>, BiFunction<A, B, R>> param1App) {
/* 51 */       return (param1App2, param1App3) -> Const.create(this.monoid.add(Const.unbox(param1App1), this.monoid.add(Const.unbox(param1App2), Const.unbox(param1App3))));
/*    */     }
/*    */   }
/*    */   
/*    */   public static final class Mu<C> implements Applicative.Mu {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\Const.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */