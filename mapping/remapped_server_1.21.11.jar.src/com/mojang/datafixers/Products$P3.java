/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.util.Function3;
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
/*     */ public final class P3<F extends K1, T1, T2, T3>
/*     */ {
/*     */   private final App<F, T1> t1;
/*     */   private final App<F, T2> t2;
/*     */   private final App<F, T3> t3;
/*     */   
/*     */   public P3(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2) {
/* 134 */     this.t1 = paramApp;
/* 135 */     this.t2 = paramApp1;
/* 136 */     this.t3 = paramApp2;
/*     */   }
/*     */   
/*     */   public App<F, T1> t1() {
/* 140 */     return this.t1;
/*     */   }
/*     */   
/*     */   public App<F, T2> t2() {
/* 144 */     return this.t2;
/*     */   }
/*     */   
/*     */   public App<F, T3> t3() {
/* 148 */     return this.t3;
/*     */   }
/*     */   
/*     */   public <T4> Products.P4<F, T1, T2, T3, T4> and(App<F, T4> paramApp) {
/* 152 */     return new Products.P4<>(this.t1, this.t2, this.t3, paramApp);
/*     */   }
/*     */   
/*     */   public <T4, T5> Products.P5<F, T1, T2, T3, T4, T5> and(Products.P2<F, T4, T5> paramP2) {
/* 156 */     return new Products.P5<>(this.t1, this.t2, this.t3, paramP2.t1, paramP2.t2);
/*     */   }
/*     */   
/*     */   public <T4, T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(P3<F, T4, T5, T6> paramP3) {
/* 160 */     return new Products.P6<>(this.t1, this.t2, this.t3, paramP3.t1, paramP3.t2, paramP3.t3);
/*     */   }
/*     */   
/*     */   public <T4, T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P4<F, T4, T5, T6, T7> paramP4) {
/* 164 */     return new Products.P7<>(this.t1, this.t2, this.t3, paramP4.t1, paramP4.t2, paramP4.t3, paramP4.t4);
/*     */   }
/*     */   
/*     */   public <T4, T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P5<F, T4, T5, T6, T7, T8> paramP5) {
/* 168 */     return new Products.P8<>(this.t1, this.t2, this.t3, paramP5.t1, paramP5.t2, paramP5.t3, paramP5.t4, paramP5.t5);
/*     */   }
/*     */   
/*     */   public <R> App<F, R> apply(Applicative<F, ?> paramApplicative, Function3<T1, T2, T3, R> paramFunction3) {
/* 172 */     return apply(paramApplicative, paramApplicative.point(paramFunction3));
/*     */   }
/*     */   
/*     */   public <R> App<F, R> apply(Applicative<F, ?> paramApplicative, App<F, Function3<T1, T2, T3, R>> paramApp) {
/* 176 */     return paramApplicative.ap3(paramApp, this.t1, this.t2, this.t3);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\Products$P3.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */