/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.util.Function4;
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
/*     */ public final class P4<F extends K1, T1, T2, T3, T4>
/*     */ {
/*     */   private final App<F, T1> t1;
/*     */   private final App<F, T2> t2;
/*     */   private final App<F, T3> t3;
/*     */   private final App<F, T4> t4;
/*     */   
/*     */   public P4(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3) {
/* 187 */     this.t1 = paramApp;
/* 188 */     this.t2 = paramApp1;
/* 189 */     this.t3 = paramApp2;
/* 190 */     this.t4 = paramApp3;
/*     */   }
/*     */   
/*     */   public App<F, T1> t1() {
/* 194 */     return this.t1;
/*     */   }
/*     */   
/*     */   public App<F, T2> t2() {
/* 198 */     return this.t2;
/*     */   }
/*     */   
/*     */   public App<F, T3> t3() {
/* 202 */     return this.t3;
/*     */   }
/*     */   
/*     */   public App<F, T4> t4() {
/* 206 */     return this.t4;
/*     */   }
/*     */   
/*     */   public <T5> Products.P5<F, T1, T2, T3, T4, T5> and(App<F, T5> paramApp) {
/* 210 */     return new Products.P5<>(this.t1, this.t2, this.t3, this.t4, paramApp);
/*     */   }
/*     */   
/*     */   public <T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(Products.P2<F, T5, T6> paramP2) {
/* 214 */     return new Products.P6<>(this.t1, this.t2, this.t3, this.t4, paramP2.t1, paramP2.t2);
/*     */   }
/*     */   
/*     */   public <T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P3<F, T5, T6, T7> paramP3) {
/* 218 */     return new Products.P7<>(this.t1, this.t2, this.t3, this.t4, paramP3.t1, paramP3.t2, paramP3.t3);
/*     */   }
/*     */   
/*     */   public <T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(P4<F, T5, T6, T7, T8> paramP4) {
/* 222 */     return new Products.P8<>(this.t1, this.t2, this.t3, this.t4, paramP4.t1, paramP4.t2, paramP4.t3, paramP4.t4);
/*     */   }
/*     */   
/*     */   public <R> App<F, R> apply(Applicative<F, ?> paramApplicative, Function4<T1, T2, T3, T4, R> paramFunction4) {
/* 226 */     return apply(paramApplicative, paramApplicative.point(paramFunction4));
/*     */   }
/*     */   
/*     */   public <R> App<F, R> apply(Applicative<F, ?> paramApplicative, App<F, Function4<T1, T2, T3, T4, R>> paramApp) {
/* 230 */     return paramApplicative.ap4(paramApp, this.t1, this.t2, this.t3, this.t4);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\Products$P4.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */