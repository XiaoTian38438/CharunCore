/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class P7<F extends K1, T1, T2, T3, T4, T5, T6, T7>
/*     */ {
/*     */   private final App<F, T1> t1;
/*     */   private final App<F, T2> t2;
/*     */   private final App<F, T3> t3;
/*     */   private final App<F, T4> t4;
/*     */   private final App<F, T5> t5;
/*     */   private final App<F, T6> t6;
/*     */   private final App<F, T7> t7;
/*     */   
/*     */   public P7(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5, App<F, T7> paramApp6) {
/* 358 */     this.t1 = paramApp;
/* 359 */     this.t2 = paramApp1;
/* 360 */     this.t3 = paramApp2;
/* 361 */     this.t4 = paramApp3;
/* 362 */     this.t5 = paramApp4;
/* 363 */     this.t6 = paramApp5;
/* 364 */     this.t7 = paramApp6;
/*     */   }
/*     */   
/*     */   public App<F, T1> t1() {
/* 368 */     return this.t1;
/*     */   }
/*     */   
/*     */   public App<F, T2> t2() {
/* 372 */     return this.t2;
/*     */   }
/*     */   
/*     */   public App<F, T3> t3() {
/* 376 */     return this.t3;
/*     */   }
/*     */   
/*     */   public App<F, T4> t4() {
/* 380 */     return this.t4;
/*     */   }
/*     */   
/*     */   public App<F, T5> t5() {
/* 384 */     return this.t5;
/*     */   }
/*     */   
/*     */   public App<F, T6> t6() {
/* 388 */     return this.t6;
/*     */   }
/*     */   
/*     */   public App<F, T7> t7() {
/* 392 */     return this.t7;
/*     */   }
/*     */   
/*     */   public <T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(App<F, T8> paramApp) {
/* 396 */     return new Products.P8<>(this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, paramApp);
/*     */   }
/*     */   
/*     */   public <R> App<F, R> apply(Applicative<F, ?> paramApplicative, Function7<T1, T2, T3, T4, T5, T6, T7, R> paramFunction7) {
/* 400 */     return apply(paramApplicative, paramApplicative.point(paramFunction7));
/*     */   }
/*     */   
/*     */   public <R> App<F, R> apply(Applicative<F, ?> paramApplicative, App<F, Function7<T1, T2, T3, T4, T5, T6, T7, R>> paramApp) {
/* 404 */     return paramApplicative.ap7(paramApp, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7);
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\Products$P7.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */