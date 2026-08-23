/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class P5<F extends K1, T1, T2, T3, T4, T5>
/*     */ {
/*     */   private final App<F, T1> t1;
/*     */   private final App<F, T2> t2;
/*     */   private final App<F, T3> t3;
/*     */   private final App<F, T4> t4;
/*     */   private final App<F, T5> t5;
/*     */   
/*     */   public P5(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4) {
/* 242 */     this.t1 = paramApp;
/* 243 */     this.t2 = paramApp1;
/* 244 */     this.t3 = paramApp2;
/* 245 */     this.t4 = paramApp3;
/* 246 */     this.t5 = paramApp4;
/*     */   }
/*     */   
/*     */   public App<F, T1> t1() {
/* 250 */     return this.t1;
/*     */   }
/*     */   
/*     */   public App<F, T2> t2() {
/* 254 */     return this.t2;
/*     */   }
/*     */   
/*     */   public App<F, T3> t3() {
/* 258 */     return this.t3;
/*     */   }
/*     */   
/*     */   public App<F, T4> t4() {
/* 262 */     return this.t4;
/*     */   }
/*     */   
/*     */   public App<F, T5> t5() {
/* 266 */     return this.t5;
/*     */   }
/*     */   
/*     */   public <T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(App<F, T6> paramApp) {
/* 270 */     return new Products.P6<>(this.t1, this.t2, this.t3, this.t4, this.t5, paramApp);
/*     */   }
/*     */   
/*     */   public <T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P2<F, T6, T7> paramP2) {
/* 274 */     return new Products.P7<>(this.t1, this.t2, this.t3, this.t4, this.t5, paramP2.t1, paramP2.t2);
/*     */   }
/*     */   
/*     */   public <T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P3<F, T6, T7, T8> paramP3) {
/* 278 */     return new Products.P8<>(this.t1, this.t2, this.t3, this.t4, this.t5, paramP3.t1, paramP3.t2, paramP3.t3);
/*     */   }
/*     */   
/*     */   public <R> App<F, R> apply(Applicative<F, ?> paramApplicative, Function5<T1, T2, T3, T4, T5, R> paramFunction5) {
/* 282 */     return apply(paramApplicative, paramApplicative.point(paramFunction5));
/*     */   }
/*     */   
/*     */   public <R> App<F, R> apply(Applicative<F, ?> paramApplicative, App<F, Function5<T1, T2, T3, T4, T5, R>> paramApp) {
/* 286 */     return paramApplicative.ap5(paramApp, this.t1, this.t2, this.t3, this.t4, this.t5);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\Products$P5.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */