/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import java.util.function.BiFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class P2<F extends K1, T1, T2>
/*     */ {
/*     */   private final App<F, T1> t1;
/*     */   private final App<F, T2> t2;
/*     */   
/*     */   public P2(App<F, T1> paramApp, App<F, T2> paramApp1) {
/*  83 */     this.t1 = paramApp;
/*  84 */     this.t2 = paramApp1;
/*     */   }
/*     */   
/*     */   public App<F, T1> t1() {
/*  88 */     return this.t1;
/*     */   }
/*     */   
/*     */   public App<F, T2> t2() {
/*  92 */     return this.t2;
/*     */   }
/*     */   
/*     */   public <T3> Products.P3<F, T1, T2, T3> and(App<F, T3> paramApp) {
/*  96 */     return new Products.P3<>(this.t1, this.t2, paramApp);
/*     */   }
/*     */   
/*     */   public <T3, T4> Products.P4<F, T1, T2, T3, T4> and(P2<F, T3, T4> paramP2) {
/* 100 */     return new Products.P4<>(this.t1, this.t2, paramP2.t1, paramP2.t2);
/*     */   }
/*     */   
/*     */   public <T3, T4, T5> Products.P5<F, T1, T2, T3, T4, T5> and(Products.P3<F, T3, T4, T5> paramP3) {
/* 104 */     return new Products.P5<>(this.t1, this.t2, paramP3.t1, paramP3.t2, paramP3.t3);
/*     */   }
/*     */   
/*     */   public <T3, T4, T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(Products.P4<F, T3, T4, T5, T6> paramP4) {
/* 108 */     return new Products.P6<>(this.t1, this.t2, paramP4.t1, paramP4.t2, paramP4.t3, paramP4.t4);
/*     */   }
/*     */   
/*     */   public <T3, T4, T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P5<F, T3, T4, T5, T6, T7> paramP5) {
/* 112 */     return new Products.P7<>(this.t1, this.t2, paramP5.t1, paramP5.t2, paramP5.t3, paramP5.t4, paramP5.t5);
/*     */   }
/*     */   
/*     */   public <T3, T4, T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P6<F, T3, T4, T5, T6, T7, T8> paramP6) {
/* 116 */     return new Products.P8<>(this.t1, this.t2, paramP6.t1, paramP6.t2, paramP6.t3, paramP6.t4, paramP6.t5, paramP6.t6);
/*     */   }
/*     */   
/*     */   public <R> App<F, R> apply(Applicative<F, ?> paramApplicative, BiFunction<T1, T2, R> paramBiFunction) {
/* 120 */     return apply(paramApplicative, paramApplicative.point(paramBiFunction));
/*     */   }
/*     */   
/*     */   public <R> App<F, R> apply(Applicative<F, ?> paramApplicative, App<F, BiFunction<T1, T2, R>> paramApp) {
/* 124 */     return paramApplicative.ap2(paramApp, this.t1, this.t2);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\Products$P2.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */