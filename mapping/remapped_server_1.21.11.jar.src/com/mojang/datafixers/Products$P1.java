/*    */ package com.mojang.datafixers;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.kinds.K1;
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
/*    */ public final class P1<F extends K1, T1>
/*    */ {
/*    */   private final App<F, T1> t1;
/*    */   
/*    */   public P1(App<F, T1> paramApp) {
/* 30 */     this.t1 = paramApp;
/*    */   }
/*    */   
/*    */   public App<F, T1> t1() {
/* 34 */     return this.t1;
/*    */   }
/*    */   
/*    */   public <T2> Products.P2<F, T1, T2> and(App<F, T2> paramApp) {
/* 38 */     return new Products.P2<>(this.t1, paramApp);
/*    */   }
/*    */   
/*    */   public <T2, T3> Products.P3<F, T1, T2, T3> and(Products.P2<F, T2, T3> paramP2) {
/* 42 */     return new Products.P3<>(this.t1, paramP2.t1, paramP2.t2);
/*    */   }
/*    */   
/*    */   public <T2, T3, T4> Products.P4<F, T1, T2, T3, T4> and(Products.P3<F, T2, T3, T4> paramP3) {
/* 46 */     return new Products.P4<>(this.t1, paramP3.t1, paramP3.t2, paramP3.t3);
/*    */   }
/*    */   
/*    */   public <T2, T3, T4, T5> Products.P5<F, T1, T2, T3, T4, T5> and(Products.P4<F, T2, T3, T4, T5> paramP4) {
/* 50 */     return new Products.P5<>(this.t1, paramP4.t1, paramP4.t2, paramP4.t3, paramP4.t4);
/*    */   }
/*    */   
/*    */   public <T2, T3, T4, T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(Products.P5<F, T2, T3, T4, T5, T6> paramP5) {
/* 54 */     return new Products.P6<>(this.t1, paramP5.t1, paramP5.t2, paramP5.t3, paramP5.t4, paramP5.t5);
/*    */   }
/*    */   
/*    */   public <T2, T3, T4, T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P6<F, T2, T3, T4, T5, T6, T7> paramP6) {
/* 58 */     return new Products.P7<>(this.t1, paramP6.t1, paramP6.t2, paramP6.t3, paramP6.t4, paramP6.t5, paramP6.t6);
/*    */   }
/*    */   
/*    */   public <T2, T3, T4, T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P7<F, T2, T3, T4, T5, T6, T7, T8> paramP7) {
/* 62 */     return new Products.P8<>(this.t1, paramP7.t1, paramP7.t2, paramP7.t3, paramP7.t4, paramP7.t5, paramP7.t6, paramP7.t7);
/*    */   }
/*    */   
/*    */   public <R> App<F, R> apply(Applicative<F, ?> paramApplicative, Function<T1, R> paramFunction) {
/* 66 */     return apply(paramApplicative, paramApplicative.point(paramFunction));
/*    */   }
/*    */   
/*    */   public <R> App<F, R> apply(Applicative<F, ?> paramApplicative, App<F, Function<T1, R>> paramApp) {
/* 70 */     return paramApplicative.ap(paramApp, this.t1);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\Products$P1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */