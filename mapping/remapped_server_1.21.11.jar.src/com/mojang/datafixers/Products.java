/*     */ package com.mojang.datafixers;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.kinds.IdF;
/*     */ import com.mojang.datafixers.kinds.K1;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.datafixers.util.Function11;
/*     */ import com.mojang.datafixers.util.Function12;
/*     */ import com.mojang.datafixers.util.Function13;
/*     */ import com.mojang.datafixers.util.Function14;
/*     */ import com.mojang.datafixers.util.Function15;
/*     */ import com.mojang.datafixers.util.Function16;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.datafixers.util.Function5;
/*     */ import com.mojang.datafixers.util.Function6;
/*     */ import com.mojang.datafixers.util.Function7;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.datafixers.util.Function9;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ 
/*     */ public interface Products
/*     */ {
/*     */   public static final class P1<F extends K1, T1> {
/*     */     private final App<F, T1> t1;
/*     */     
/*     */     public P1(App<F, T1> param1App) {
/*  30 */       this.t1 = param1App;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/*  34 */       return this.t1;
/*     */     }
/*     */     
/*     */     public <T2> Products.P2<F, T1, T2> and(App<F, T2> param1App) {
/*  38 */       return new Products.P2<>(this.t1, param1App);
/*     */     }
/*     */     
/*     */     public <T2, T3> Products.P3<F, T1, T2, T3> and(Products.P2<F, T2, T3> param1P2) {
/*  42 */       return new Products.P3<>(this.t1, param1P2.t1, param1P2.t2);
/*     */     }
/*     */     
/*     */     public <T2, T3, T4> Products.P4<F, T1, T2, T3, T4> and(Products.P3<F, T2, T3, T4> param1P3) {
/*  46 */       return new Products.P4<>(this.t1, param1P3.t1, param1P3.t2, param1P3.t3);
/*     */     }
/*     */     
/*     */     public <T2, T3, T4, T5> Products.P5<F, T1, T2, T3, T4, T5> and(Products.P4<F, T2, T3, T4, T5> param1P4) {
/*  50 */       return new Products.P5<>(this.t1, param1P4.t1, param1P4.t2, param1P4.t3, param1P4.t4);
/*     */     }
/*     */     
/*     */     public <T2, T3, T4, T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(Products.P5<F, T2, T3, T4, T5, T6> param1P5) {
/*  54 */       return new Products.P6<>(this.t1, param1P5.t1, param1P5.t2, param1P5.t3, param1P5.t4, param1P5.t5);
/*     */     }
/*     */     
/*     */     public <T2, T3, T4, T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P6<F, T2, T3, T4, T5, T6, T7> param1P6) {
/*  58 */       return new Products.P7<>(this.t1, param1P6.t1, param1P6.t2, param1P6.t3, param1P6.t4, param1P6.t5, param1P6.t6);
/*     */     }
/*     */     
/*     */     public <T2, T3, T4, T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P7<F, T2, T3, T4, T5, T6, T7, T8> param1P7) {
/*  62 */       return new Products.P8<>(this.t1, param1P7.t1, param1P7.t2, param1P7.t3, param1P7.t4, param1P7.t5, param1P7.t6, param1P7.t7);
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function<T1, R> param1Function) {
/*  66 */       return apply(param1Applicative, param1Applicative.point(param1Function));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function<T1, R>> param1App) {
/*  70 */       return param1Applicative.ap(param1App, this.t1);
/*     */     }
/*     */   }
/*     */   
/*     */   static <T1, T2> P2<IdF.Mu, T1, T2> of(T1 paramT1, T2 paramT2) {
/*  75 */     return new P2<>((App<IdF.Mu, T1>)IdF.create(paramT1), (App<IdF.Mu, T2>)IdF.create(paramT2));
/*     */   }
/*     */   
/*     */   public static final class P2<F extends K1, T1, T2> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     
/*     */     public P2(App<F, T1> param1App, App<F, T2> param1App1) {
/*  83 */       this.t1 = param1App;
/*  84 */       this.t2 = param1App1;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/*  88 */       return this.t1;
/*     */     }
/*     */     
/*     */     public App<F, T2> t2() {
/*  92 */       return this.t2;
/*     */     }
/*     */     
/*     */     public <T3> Products.P3<F, T1, T2, T3> and(App<F, T3> param1App) {
/*  96 */       return new Products.P3<>(this.t1, this.t2, param1App);
/*     */     }
/*     */     
/*     */     public <T3, T4> Products.P4<F, T1, T2, T3, T4> and(P2<F, T3, T4> param1P2) {
/* 100 */       return new Products.P4<>(this.t1, this.t2, param1P2.t1, param1P2.t2);
/*     */     }
/*     */     
/*     */     public <T3, T4, T5> Products.P5<F, T1, T2, T3, T4, T5> and(Products.P3<F, T3, T4, T5> param1P3) {
/* 104 */       return new Products.P5<>(this.t1, this.t2, param1P3.t1, param1P3.t2, param1P3.t3);
/*     */     }
/*     */     
/*     */     public <T3, T4, T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(Products.P4<F, T3, T4, T5, T6> param1P4) {
/* 108 */       return new Products.P6<>(this.t1, this.t2, param1P4.t1, param1P4.t2, param1P4.t3, param1P4.t4);
/*     */     }
/*     */     
/*     */     public <T3, T4, T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P5<F, T3, T4, T5, T6, T7> param1P5) {
/* 112 */       return new Products.P7<>(this.t1, this.t2, param1P5.t1, param1P5.t2, param1P5.t3, param1P5.t4, param1P5.t5);
/*     */     }
/*     */     
/*     */     public <T3, T4, T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P6<F, T3, T4, T5, T6, T7, T8> param1P6) {
/* 116 */       return new Products.P8<>(this.t1, this.t2, param1P6.t1, param1P6.t2, param1P6.t3, param1P6.t4, param1P6.t5, param1P6.t6);
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, BiFunction<T1, T2, R> param1BiFunction) {
/* 120 */       return apply(param1Applicative, param1Applicative.point(param1BiFunction));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, BiFunction<T1, T2, R>> param1App) {
/* 124 */       return param1Applicative.ap2(param1App, this.t1, this.t2);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P3<F extends K1, T1, T2, T3> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     
/*     */     public P3(App<F, T1> param1App, App<F, T2> param1App1, App<F, T3> param1App2) {
/* 134 */       this.t1 = param1App;
/* 135 */       this.t2 = param1App1;
/* 136 */       this.t3 = param1App2;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/* 140 */       return this.t1;
/*     */     }
/*     */     
/*     */     public App<F, T2> t2() {
/* 144 */       return this.t2;
/*     */     }
/*     */     
/*     */     public App<F, T3> t3() {
/* 148 */       return this.t3;
/*     */     }
/*     */     
/*     */     public <T4> Products.P4<F, T1, T2, T3, T4> and(App<F, T4> param1App) {
/* 152 */       return new Products.P4<>(this.t1, this.t2, this.t3, param1App);
/*     */     }
/*     */     
/*     */     public <T4, T5> Products.P5<F, T1, T2, T3, T4, T5> and(Products.P2<F, T4, T5> param1P2) {
/* 156 */       return new Products.P5<>(this.t1, this.t2, this.t3, param1P2.t1, param1P2.t2);
/*     */     }
/*     */     
/*     */     public <T4, T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(P3<F, T4, T5, T6> param1P3) {
/* 160 */       return new Products.P6<>(this.t1, this.t2, this.t3, param1P3.t1, param1P3.t2, param1P3.t3);
/*     */     }
/*     */     
/*     */     public <T4, T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P4<F, T4, T5, T6, T7> param1P4) {
/* 164 */       return new Products.P7<>(this.t1, this.t2, this.t3, param1P4.t1, param1P4.t2, param1P4.t3, param1P4.t4);
/*     */     }
/*     */     
/*     */     public <T4, T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P5<F, T4, T5, T6, T7, T8> param1P5) {
/* 168 */       return new Products.P8<>(this.t1, this.t2, this.t3, param1P5.t1, param1P5.t2, param1P5.t3, param1P5.t4, param1P5.t5);
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function3<T1, T2, T3, R> param1Function3) {
/* 172 */       return apply(param1Applicative, param1Applicative.point(param1Function3));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function3<T1, T2, T3, R>> param1App) {
/* 176 */       return param1Applicative.ap3(param1App, this.t1, this.t2, this.t3);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P4<F extends K1, T1, T2, T3, T4> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     
/*     */     public P4(App<F, T1> param1App, App<F, T2> param1App1, App<F, T3> param1App2, App<F, T4> param1App3) {
/* 187 */       this.t1 = param1App;
/* 188 */       this.t2 = param1App1;
/* 189 */       this.t3 = param1App2;
/* 190 */       this.t4 = param1App3;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/* 194 */       return this.t1;
/*     */     }
/*     */     
/*     */     public App<F, T2> t2() {
/* 198 */       return this.t2;
/*     */     }
/*     */     
/*     */     public App<F, T3> t3() {
/* 202 */       return this.t3;
/*     */     }
/*     */     
/*     */     public App<F, T4> t4() {
/* 206 */       return this.t4;
/*     */     }
/*     */     
/*     */     public <T5> Products.P5<F, T1, T2, T3, T4, T5> and(App<F, T5> param1App) {
/* 210 */       return new Products.P5<>(this.t1, this.t2, this.t3, this.t4, param1App);
/*     */     }
/*     */     
/*     */     public <T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(Products.P2<F, T5, T6> param1P2) {
/* 214 */       return new Products.P6<>(this.t1, this.t2, this.t3, this.t4, param1P2.t1, param1P2.t2);
/*     */     }
/*     */     
/*     */     public <T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P3<F, T5, T6, T7> param1P3) {
/* 218 */       return new Products.P7<>(this.t1, this.t2, this.t3, this.t4, param1P3.t1, param1P3.t2, param1P3.t3);
/*     */     }
/*     */     
/*     */     public <T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(P4<F, T5, T6, T7, T8> param1P4) {
/* 222 */       return new Products.P8<>(this.t1, this.t2, this.t3, this.t4, param1P4.t1, param1P4.t2, param1P4.t3, param1P4.t4);
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function4<T1, T2, T3, T4, R> param1Function4) {
/* 226 */       return apply(param1Applicative, param1Applicative.point(param1Function4));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function4<T1, T2, T3, T4, R>> param1App) {
/* 230 */       return param1Applicative.ap4(param1App, this.t1, this.t2, this.t3, this.t4);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P5<F extends K1, T1, T2, T3, T4, T5> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     
/*     */     public P5(App<F, T1> param1App, App<F, T2> param1App1, App<F, T3> param1App2, App<F, T4> param1App3, App<F, T5> param1App4) {
/* 242 */       this.t1 = param1App;
/* 243 */       this.t2 = param1App1;
/* 244 */       this.t3 = param1App2;
/* 245 */       this.t4 = param1App3;
/* 246 */       this.t5 = param1App4;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/* 250 */       return this.t1;
/*     */     }
/*     */     
/*     */     public App<F, T2> t2() {
/* 254 */       return this.t2;
/*     */     }
/*     */     
/*     */     public App<F, T3> t3() {
/* 258 */       return this.t3;
/*     */     }
/*     */     
/*     */     public App<F, T4> t4() {
/* 262 */       return this.t4;
/*     */     }
/*     */     
/*     */     public App<F, T5> t5() {
/* 266 */       return this.t5;
/*     */     }
/*     */     
/*     */     public <T6> Products.P6<F, T1, T2, T3, T4, T5, T6> and(App<F, T6> param1App) {
/* 270 */       return new Products.P6<>(this.t1, this.t2, this.t3, this.t4, this.t5, param1App);
/*     */     }
/*     */     
/*     */     public <T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(Products.P2<F, T6, T7> param1P2) {
/* 274 */       return new Products.P7<>(this.t1, this.t2, this.t3, this.t4, this.t5, param1P2.t1, param1P2.t2);
/*     */     }
/*     */     
/*     */     public <T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P3<F, T6, T7, T8> param1P3) {
/* 278 */       return new Products.P8<>(this.t1, this.t2, this.t3, this.t4, this.t5, param1P3.t1, param1P3.t2, param1P3.t3);
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function5<T1, T2, T3, T4, T5, R> param1Function5) {
/* 282 */       return apply(param1Applicative, param1Applicative.point(param1Function5));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function5<T1, T2, T3, T4, T5, R>> param1App) {
/* 286 */       return param1Applicative.ap5(param1App, this.t1, this.t2, this.t3, this.t4, this.t5);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P6<F extends K1, T1, T2, T3, T4, T5, T6> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     
/*     */     public P6(App<F, T1> param1App, App<F, T2> param1App1, App<F, T3> param1App2, App<F, T4> param1App3, App<F, T5> param1App4, App<F, T6> param1App5) {
/* 299 */       this.t1 = param1App;
/* 300 */       this.t2 = param1App1;
/* 301 */       this.t3 = param1App2;
/* 302 */       this.t4 = param1App3;
/* 303 */       this.t5 = param1App4;
/* 304 */       this.t6 = param1App5;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/* 308 */       return this.t1;
/*     */     }
/*     */     
/*     */     public App<F, T2> t2() {
/* 312 */       return this.t2;
/*     */     }
/*     */     
/*     */     public App<F, T3> t3() {
/* 316 */       return this.t3;
/*     */     }
/*     */     
/*     */     public App<F, T4> t4() {
/* 320 */       return this.t4;
/*     */     }
/*     */     
/*     */     public App<F, T5> t5() {
/* 324 */       return this.t5;
/*     */     }
/*     */     
/*     */     public App<F, T6> t6() {
/* 328 */       return this.t6;
/*     */     }
/*     */     
/*     */     public <T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> and(App<F, T7> param1App) {
/* 332 */       return new Products.P7<>(this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, param1App);
/*     */     }
/*     */     
/*     */     public <T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(Products.P2<F, T7, T8> param1P2) {
/* 336 */       return new Products.P8<>(this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, param1P2.t1, param1P2.t2);
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function6<T1, T2, T3, T4, T5, T6, R> param1Function6) {
/* 340 */       return apply(param1Applicative, param1Applicative.point(param1Function6));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function6<T1, T2, T3, T4, T5, T6, R>> param1App) {
/* 344 */       return param1Applicative.ap6(param1App, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P7<F extends K1, T1, T2, T3, T4, T5, T6, T7> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     
/*     */     public P7(App<F, T1> param1App, App<F, T2> param1App1, App<F, T3> param1App2, App<F, T4> param1App3, App<F, T5> param1App4, App<F, T6> param1App5, App<F, T7> param1App6) {
/* 358 */       this.t1 = param1App;
/* 359 */       this.t2 = param1App1;
/* 360 */       this.t3 = param1App2;
/* 361 */       this.t4 = param1App3;
/* 362 */       this.t5 = param1App4;
/* 363 */       this.t6 = param1App5;
/* 364 */       this.t7 = param1App6;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/* 368 */       return this.t1;
/*     */     }
/*     */     
/*     */     public App<F, T2> t2() {
/* 372 */       return this.t2;
/*     */     }
/*     */     
/*     */     public App<F, T3> t3() {
/* 376 */       return this.t3;
/*     */     }
/*     */     
/*     */     public App<F, T4> t4() {
/* 380 */       return this.t4;
/*     */     }
/*     */     
/*     */     public App<F, T5> t5() {
/* 384 */       return this.t5;
/*     */     }
/*     */     
/*     */     public App<F, T6> t6() {
/* 388 */       return this.t6;
/*     */     }
/*     */     
/*     */     public App<F, T7> t7() {
/* 392 */       return this.t7;
/*     */     }
/*     */     
/*     */     public <T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> and(App<F, T8> param1App) {
/* 396 */       return new Products.P8<>(this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, param1App);
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function7<T1, T2, T3, T4, T5, T6, T7, R> param1Function7) {
/* 400 */       return apply(param1Applicative, param1Applicative.point(param1Function7));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function7<T1, T2, T3, T4, T5, T6, T7, R>> param1App) {
/* 404 */       return param1Applicative.ap7(param1App, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P8<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     
/*     */     public P8(App<F, T1> param1App, App<F, T2> param1App1, App<F, T3> param1App2, App<F, T4> param1App3, App<F, T5> param1App4, App<F, T6> param1App5, App<F, T7> param1App6, App<F, T8> param1App7) {
/* 419 */       this.t1 = param1App;
/* 420 */       this.t2 = param1App1;
/* 421 */       this.t3 = param1App2;
/* 422 */       this.t4 = param1App3;
/* 423 */       this.t5 = param1App4;
/* 424 */       this.t6 = param1App5;
/* 425 */       this.t7 = param1App6;
/* 426 */       this.t8 = param1App7;
/*     */     }
/*     */     
/*     */     public App<F, T1> t1() {
/* 430 */       return this.t1;
/*     */     }
/*     */     
/*     */     public App<F, T2> t2() {
/* 434 */       return this.t2;
/*     */     }
/*     */     
/*     */     public App<F, T3> t3() {
/* 438 */       return this.t3;
/*     */     }
/*     */     
/*     */     public App<F, T4> t4() {
/* 442 */       return this.t4;
/*     */     }
/*     */     
/*     */     public App<F, T5> t5() {
/* 446 */       return this.t5;
/*     */     }
/*     */     
/*     */     public App<F, T6> t6() {
/* 450 */       return this.t6;
/*     */     }
/*     */     
/*     */     public App<F, T7> t7() {
/* 454 */       return this.t7;
/*     */     }
/*     */     
/*     */     public App<F, T8> t8() {
/* 458 */       return this.t8;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> param1Function8) {
/* 462 */       return apply(param1Applicative, param1Applicative.point(param1Function8));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>> param1App) {
/* 466 */       return param1Applicative.ap8(param1App, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P9<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     
/*     */     public P9(App<F, T1> param1App, App<F, T2> param1App1, App<F, T3> param1App2, App<F, T4> param1App3, App<F, T5> param1App4, App<F, T6> param1App5, App<F, T7> param1App6, App<F, T8> param1App7, App<F, T9> param1App8) {
/* 482 */       this.t1 = param1App;
/* 483 */       this.t2 = param1App1;
/* 484 */       this.t3 = param1App2;
/* 485 */       this.t4 = param1App3;
/* 486 */       this.t5 = param1App4;
/* 487 */       this.t6 = param1App5;
/* 488 */       this.t7 = param1App6;
/* 489 */       this.t8 = param1App7;
/* 490 */       this.t9 = param1App8;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> param1Function9) {
/* 494 */       return apply(param1Applicative, param1Applicative.point(param1Function9));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>> param1App) {
/* 498 */       return param1Applicative.ap9(param1App, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P10<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     private final App<F, T10> t10;
/*     */     
/*     */     public P10(App<F, T1> param1App, App<F, T2> param1App1, App<F, T3> param1App2, App<F, T4> param1App3, App<F, T5> param1App4, App<F, T6> param1App5, App<F, T7> param1App6, App<F, T8> param1App7, App<F, T9> param1App8, App<F, T10> param1App9) {
/* 515 */       this.t1 = param1App;
/* 516 */       this.t2 = param1App1;
/* 517 */       this.t3 = param1App2;
/* 518 */       this.t4 = param1App3;
/* 519 */       this.t5 = param1App4;
/* 520 */       this.t6 = param1App5;
/* 521 */       this.t7 = param1App6;
/* 522 */       this.t8 = param1App7;
/* 523 */       this.t9 = param1App8;
/* 524 */       this.t10 = param1App9;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> param1Function10) {
/* 528 */       return apply(param1Applicative, param1Applicative.point(param1Function10));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>> param1App) {
/* 532 */       return param1Applicative.ap10(param1App, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P11<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     private final App<F, T10> t10;
/*     */     private final App<F, T11> t11;
/*     */     
/*     */     public P11(App<F, T1> param1App, App<F, T2> param1App1, App<F, T3> param1App2, App<F, T4> param1App3, App<F, T5> param1App4, App<F, T6> param1App5, App<F, T7> param1App6, App<F, T8> param1App7, App<F, T9> param1App8, App<F, T10> param1App9, App<F, T11> param1App10) {
/* 550 */       this.t1 = param1App;
/* 551 */       this.t2 = param1App1;
/* 552 */       this.t3 = param1App2;
/* 553 */       this.t4 = param1App3;
/* 554 */       this.t5 = param1App4;
/* 555 */       this.t6 = param1App5;
/* 556 */       this.t7 = param1App6;
/* 557 */       this.t8 = param1App7;
/* 558 */       this.t9 = param1App8;
/* 559 */       this.t10 = param1App9;
/* 560 */       this.t11 = param1App10;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> param1Function11) {
/* 564 */       return apply(param1Applicative, param1Applicative.point(param1Function11));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R>> param1App) {
/* 568 */       return param1Applicative.ap11(param1App, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P12<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     private final App<F, T10> t10;
/*     */     private final App<F, T11> t11;
/*     */     private final App<F, T12> t12;
/*     */     
/*     */     public P12(App<F, T1> param1App, App<F, T2> param1App1, App<F, T3> param1App2, App<F, T4> param1App3, App<F, T5> param1App4, App<F, T6> param1App5, App<F, T7> param1App6, App<F, T8> param1App7, App<F, T9> param1App8, App<F, T10> param1App9, App<F, T11> param1App10, App<F, T12> param1App11) {
/* 587 */       this.t1 = param1App;
/* 588 */       this.t2 = param1App1;
/* 589 */       this.t3 = param1App2;
/* 590 */       this.t4 = param1App3;
/* 591 */       this.t5 = param1App4;
/* 592 */       this.t6 = param1App5;
/* 593 */       this.t7 = param1App6;
/* 594 */       this.t8 = param1App7;
/* 595 */       this.t9 = param1App8;
/* 596 */       this.t10 = param1App9;
/* 597 */       this.t11 = param1App10;
/* 598 */       this.t12 = param1App11;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> param1Function12) {
/* 602 */       return apply(param1Applicative, param1Applicative.point(param1Function12));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R>> param1App) {
/* 606 */       return param1Applicative.ap12(param1App, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P13<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     private final App<F, T10> t10;
/*     */     private final App<F, T11> t11;
/*     */     private final App<F, T12> t12;
/*     */     private final App<F, T13> t13;
/*     */     
/*     */     public P13(App<F, T1> param1App, App<F, T2> param1App1, App<F, T3> param1App2, App<F, T4> param1App3, App<F, T5> param1App4, App<F, T6> param1App5, App<F, T7> param1App6, App<F, T8> param1App7, App<F, T9> param1App8, App<F, T10> param1App9, App<F, T11> param1App10, App<F, T12> param1App11, App<F, T13> param1App12) {
/* 626 */       this.t1 = param1App;
/* 627 */       this.t2 = param1App1;
/* 628 */       this.t3 = param1App2;
/* 629 */       this.t4 = param1App3;
/* 630 */       this.t5 = param1App4;
/* 631 */       this.t6 = param1App5;
/* 632 */       this.t7 = param1App6;
/* 633 */       this.t8 = param1App7;
/* 634 */       this.t9 = param1App8;
/* 635 */       this.t10 = param1App9;
/* 636 */       this.t11 = param1App10;
/* 637 */       this.t12 = param1App11;
/* 638 */       this.t13 = param1App12;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> param1Function13) {
/* 642 */       return apply(param1Applicative, param1Applicative.point(param1Function13));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R>> param1App) {
/* 646 */       return param1Applicative.ap13(param1App, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P14<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     private final App<F, T10> t10;
/*     */     private final App<F, T11> t11;
/*     */     private final App<F, T12> t12;
/*     */     private final App<F, T13> t13;
/*     */     private final App<F, T14> t14;
/*     */     
/*     */     public P14(App<F, T1> param1App, App<F, T2> param1App1, App<F, T3> param1App2, App<F, T4> param1App3, App<F, T5> param1App4, App<F, T6> param1App5, App<F, T7> param1App6, App<F, T8> param1App7, App<F, T9> param1App8, App<F, T10> param1App9, App<F, T11> param1App10, App<F, T12> param1App11, App<F, T13> param1App12, App<F, T14> param1App13) {
/* 667 */       this.t1 = param1App;
/* 668 */       this.t2 = param1App1;
/* 669 */       this.t3 = param1App2;
/* 670 */       this.t4 = param1App3;
/* 671 */       this.t5 = param1App4;
/* 672 */       this.t6 = param1App5;
/* 673 */       this.t7 = param1App6;
/* 674 */       this.t8 = param1App7;
/* 675 */       this.t9 = param1App8;
/* 676 */       this.t10 = param1App9;
/* 677 */       this.t11 = param1App10;
/* 678 */       this.t12 = param1App11;
/* 679 */       this.t13 = param1App12;
/* 680 */       this.t14 = param1App13;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> param1Function14) {
/* 684 */       return apply(param1Applicative, param1Applicative.point(param1Function14));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R>> param1App) {
/* 688 */       return param1Applicative.ap14(param1App, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13, this.t14);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P15<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     private final App<F, T10> t10;
/*     */     private final App<F, T11> t11;
/*     */     private final App<F, T12> t12;
/*     */     private final App<F, T13> t13;
/*     */     private final App<F, T14> t14;
/*     */     private final App<F, T15> t15;
/*     */     
/*     */     public P15(App<F, T1> param1App, App<F, T2> param1App1, App<F, T3> param1App2, App<F, T4> param1App3, App<F, T5> param1App4, App<F, T6> param1App5, App<F, T7> param1App6, App<F, T8> param1App7, App<F, T9> param1App8, App<F, T10> param1App9, App<F, T11> param1App10, App<F, T12> param1App11, App<F, T13> param1App12, App<F, T14> param1App13, App<F, T15> param1App14) {
/* 710 */       this.t1 = param1App;
/* 711 */       this.t2 = param1App1;
/* 712 */       this.t3 = param1App2;
/* 713 */       this.t4 = param1App3;
/* 714 */       this.t5 = param1App4;
/* 715 */       this.t6 = param1App5;
/* 716 */       this.t7 = param1App6;
/* 717 */       this.t8 = param1App7;
/* 718 */       this.t9 = param1App8;
/* 719 */       this.t10 = param1App9;
/* 720 */       this.t11 = param1App10;
/* 721 */       this.t12 = param1App11;
/* 722 */       this.t13 = param1App12;
/* 723 */       this.t14 = param1App13;
/* 724 */       this.t15 = param1App14;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R> param1Function15) {
/* 728 */       return apply(param1Applicative, param1Applicative.point(param1Function15));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R>> param1App) {
/* 732 */       return param1Applicative.ap15(param1App, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13, this.t14, this.t15);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class P16<F extends K1, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> {
/*     */     private final App<F, T1> t1;
/*     */     private final App<F, T2> t2;
/*     */     private final App<F, T3> t3;
/*     */     private final App<F, T4> t4;
/*     */     private final App<F, T5> t5;
/*     */     private final App<F, T6> t6;
/*     */     private final App<F, T7> t7;
/*     */     private final App<F, T8> t8;
/*     */     private final App<F, T9> t9;
/*     */     private final App<F, T10> t10;
/*     */     private final App<F, T11> t11;
/*     */     private final App<F, T12> t12;
/*     */     private final App<F, T13> t13;
/*     */     private final App<F, T14> t14;
/*     */     private final App<F, T15> t15;
/*     */     private final App<F, T16> t16;
/*     */     
/*     */     public P16(App<F, T1> param1App, App<F, T2> param1App1, App<F, T3> param1App2, App<F, T4> param1App3, App<F, T5> param1App4, App<F, T6> param1App5, App<F, T7> param1App6, App<F, T8> param1App7, App<F, T9> param1App8, App<F, T10> param1App9, App<F, T11> param1App10, App<F, T12> param1App11, App<F, T13> param1App12, App<F, T14> param1App13, App<F, T15> param1App14, App<F, T16> param1App15) {
/* 755 */       this.t1 = param1App;
/* 756 */       this.t2 = param1App1;
/* 757 */       this.t3 = param1App2;
/* 758 */       this.t4 = param1App3;
/* 759 */       this.t5 = param1App4;
/* 760 */       this.t6 = param1App5;
/* 761 */       this.t7 = param1App6;
/* 762 */       this.t8 = param1App7;
/* 763 */       this.t9 = param1App8;
/* 764 */       this.t10 = param1App9;
/* 765 */       this.t11 = param1App10;
/* 766 */       this.t12 = param1App11;
/* 767 */       this.t13 = param1App12;
/* 768 */       this.t14 = param1App13;
/* 769 */       this.t15 = param1App14;
/* 770 */       this.t16 = param1App15;
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> param1Function16) {
/* 774 */       return apply(param1Applicative, param1Applicative.point(param1Function16));
/*     */     }
/*     */     
/*     */     public <R> App<F, R> apply(Applicative<F, ?> param1Applicative, App<F, Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> param1App) {
/* 778 */       return param1Applicative.ap16(param1App, this.t1, this.t2, this.t3, this.t4, this.t5, this.t6, this.t7, this.t8, this.t9, this.t10, this.t11, this.t12, this.t13, this.t14, this.t15, this.t16);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\Products.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */