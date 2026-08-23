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
/*    */ public final class Instance<C>
/*    */   implements Applicative<Const.Mu<C>, Const.Instance.Mu<C>>
/*    */ {
/*    */   private final Monoid<C> monoid;
/*    */   
/*    */   public static final class Mu<C>
/*    */     implements Applicative.Mu {}
/*    */   
/*    */   public Instance(Monoid<C> paramMonoid) {
/* 31 */     this.monoid = paramMonoid;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T, R> App<Const.Mu<C>, R> map(Function<? super T, ? extends R> paramFunction, App<Const.Mu<C>, T> paramApp) {
/* 36 */     return Const.create(Const.unbox(paramApp));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A> App<Const.Mu<C>, A> point(A paramA) {
/* 41 */     return Const.create(this.monoid.point());
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, R> Function<App<Const.Mu<C>, A>, App<Const.Mu<C>, R>> lift1(App<Const.Mu<C>, Function<A, R>> paramApp) {
/* 46 */     return paramApp2 -> Const.create(this.monoid.add(Const.unbox(paramApp1), Const.unbox(paramApp2)));
/*    */   }
/*    */ 
/*    */   
/*    */   public <A, B, R> BiFunction<App<Const.Mu<C>, A>, App<Const.Mu<C>, B>, App<Const.Mu<C>, R>> lift2(App<Const.Mu<C>, BiFunction<A, B, R>> paramApp) {
/* 51 */     return (paramApp2, paramApp3) -> Const.create(this.monoid.add(Const.unbox(paramApp1), this.monoid.add(Const.unbox(paramApp2), Const.unbox(paramApp3))));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\Const$Instance.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */