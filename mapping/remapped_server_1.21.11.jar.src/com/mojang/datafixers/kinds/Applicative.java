/*     */ package com.mojang.datafixers.kinds;
/*     */ 
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
/*     */ 
/*     */ public interface Applicative<F extends K1, Mu extends Applicative.Mu>
/*     */   extends Functor<F, Mu>
/*     */ {
/*     */   static <F extends K1, Mu extends Mu> Applicative<F, Mu> unbox(App<Mu, F> paramApp) {
/*  25 */     return (Applicative<F, Mu>)paramApp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default <A, B, R> BiFunction<App<F, A>, App<F, B>, App<F, R>> lift2(App<F, BiFunction<A, B, R>> paramApp) {
/*  35 */     return (paramApp2, paramApp3) -> ap2(paramApp1, paramApp2, paramApp3);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, R> Function3<App<F, T1>, App<F, T2>, App<F, T3>, App<F, R>> lift3(App<F, Function3<T1, T2, T3, R>> paramApp) {
/*  39 */     return (paramApp2, paramApp3, paramApp4) -> ap3(paramApp1, paramApp2, paramApp3, paramApp4);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, R> Function4<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, R>> lift4(App<F, Function4<T1, T2, T3, T4, R>> paramApp) {
/*  43 */     return (paramApp2, paramApp3, paramApp4, paramApp5) -> ap4(paramApp1, paramApp2, paramApp3, paramApp4, paramApp5);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, R> Function5<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, R>> lift5(App<F, Function5<T1, T2, T3, T4, T5, R>> paramApp) {
/*  47 */     return (paramApp2, paramApp3, paramApp4, paramApp5, paramApp6) -> ap5(paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, R> Function6<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, R>> lift6(App<F, Function6<T1, T2, T3, T4, T5, T6, R>> paramApp) {
/*  51 */     return (paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7) -> ap6(paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, R> Function7<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, T7>, App<F, R>> lift7(App<F, Function7<T1, T2, T3, T4, T5, T6, T7, R>> paramApp) {
/*  55 */     return (paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8) -> ap7(paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, R> Function8<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, T7>, App<F, T8>, App<F, R>> lift8(App<F, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>> paramApp) {
/*  59 */     return (paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8, paramApp9) -> ap8(paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8, paramApp9);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> Function9<App<F, T1>, App<F, T2>, App<F, T3>, App<F, T4>, App<F, T5>, App<F, T6>, App<F, T7>, App<F, T8>, App<F, T9>, App<F, R>> lift9(App<F, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>> paramApp) {
/*  63 */     return (paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8, paramApp9, paramApp10) -> ap9(paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8, paramApp9, paramApp10);
/*     */   }
/*     */   
/*     */   default <A, R> App<F, R> ap(App<F, Function<A, R>> paramApp, App<F, A> paramApp1) {
/*  67 */     return lift1(paramApp).apply(paramApp1);
/*     */   }
/*     */   
/*     */   default <A, R> App<F, R> ap(Function<A, R> paramFunction, App<F, A> paramApp) {
/*  71 */     return map(paramFunction, paramApp);
/*     */   }
/*     */   
/*     */   default <A, B, R> App<F, R> ap2(App<F, BiFunction<A, B, R>> paramApp, App<F, A> paramApp1, App<F, B> paramApp2) {
/*  75 */     Function function = paramBiFunction -> ();
/*  76 */     return ap(ap(map(function, paramApp), paramApp1), paramApp2);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, R> App<F, R> ap3(App<F, Function3<T1, T2, T3, R>> paramApp, App<F, T1> paramApp1, App<F, T2> paramApp2, App<F, T3> paramApp3) {
/*  80 */     return ap2(ap(map(Function3::curry, paramApp), paramApp1), paramApp2, paramApp3);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, R> App<F, R> ap4(App<F, Function4<T1, T2, T3, T4, R>> paramApp, App<F, T1> paramApp1, App<F, T2> paramApp2, App<F, T3> paramApp3, App<F, T4> paramApp4) {
/*  84 */     return ap2(ap2(map(Function4::curry2, paramApp), paramApp1, paramApp2), paramApp3, paramApp4);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, R> App<F, R> ap5(App<F, Function5<T1, T2, T3, T4, T5, R>> paramApp, App<F, T1> paramApp1, App<F, T2> paramApp2, App<F, T3> paramApp3, App<F, T4> paramApp4, App<F, T5> paramApp5) {
/*  88 */     return ap3(ap2(map(Function5::curry2, paramApp), paramApp1, paramApp2), paramApp3, paramApp4, paramApp5);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, R> App<F, R> ap6(App<F, Function6<T1, T2, T3, T4, T5, T6, R>> paramApp, App<F, T1> paramApp1, App<F, T2> paramApp2, App<F, T3> paramApp3, App<F, T4> paramApp4, App<F, T5> paramApp5, App<F, T6> paramApp6) {
/*  92 */     return ap3(ap3(map(Function6::curry3, paramApp), paramApp1, paramApp2, paramApp3), paramApp4, paramApp5, paramApp6);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, R> App<F, R> ap7(App<F, Function7<T1, T2, T3, T4, T5, T6, T7, R>> paramApp, App<F, T1> paramApp1, App<F, T2> paramApp2, App<F, T3> paramApp3, App<F, T4> paramApp4, App<F, T5> paramApp5, App<F, T6> paramApp6, App<F, T7> paramApp7) {
/*  96 */     return ap4(ap3(map(Function7::curry3, paramApp), paramApp1, paramApp2, paramApp3), paramApp4, paramApp5, paramApp6, paramApp7);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, R> App<F, R> ap8(App<F, Function8<T1, T2, T3, T4, T5, T6, T7, T8, R>> paramApp, App<F, T1> paramApp1, App<F, T2> paramApp2, App<F, T3> paramApp3, App<F, T4> paramApp4, App<F, T5> paramApp5, App<F, T6> paramApp6, App<F, T7> paramApp7, App<F, T8> paramApp8) {
/* 100 */     return ap4(ap4(map(Function8::curry4, paramApp), paramApp1, paramApp2, paramApp3, paramApp4), paramApp5, paramApp6, paramApp7, paramApp8);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> App<F, R> ap9(App<F, Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>> paramApp, App<F, T1> paramApp1, App<F, T2> paramApp2, App<F, T3> paramApp3, App<F, T4> paramApp4, App<F, T5> paramApp5, App<F, T6> paramApp6, App<F, T7> paramApp7, App<F, T8> paramApp8, App<F, T9> paramApp9) {
/* 104 */     return ap5(ap4(map(Function9::curry4, paramApp), paramApp1, paramApp2, paramApp3, paramApp4), paramApp5, paramApp6, paramApp7, paramApp8, paramApp9);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> App<F, R> ap10(App<F, Function10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R>> paramApp, App<F, T1> paramApp1, App<F, T2> paramApp2, App<F, T3> paramApp3, App<F, T4> paramApp4, App<F, T5> paramApp5, App<F, T6> paramApp6, App<F, T7> paramApp7, App<F, T8> paramApp8, App<F, T9> paramApp9, App<F, T10> paramApp10) {
/* 108 */     return ap5(ap5(map(Function10::curry5, paramApp), paramApp1, paramApp2, paramApp3, paramApp4, paramApp5), paramApp6, paramApp7, paramApp8, paramApp9, paramApp10);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> App<F, R> ap11(App<F, Function11<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R>> paramApp, App<F, T1> paramApp1, App<F, T2> paramApp2, App<F, T3> paramApp3, App<F, T4> paramApp4, App<F, T5> paramApp5, App<F, T6> paramApp6, App<F, T7> paramApp7, App<F, T8> paramApp8, App<F, T9> paramApp9, App<F, T10> paramApp10, App<F, T11> paramApp11) {
/* 112 */     return ap6(ap5(map(Function11::curry5, paramApp), paramApp1, paramApp2, paramApp3, paramApp4, paramApp5), paramApp6, paramApp7, paramApp8, paramApp9, paramApp10, paramApp11);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> App<F, R> ap12(App<F, Function12<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R>> paramApp, App<F, T1> paramApp1, App<F, T2> paramApp2, App<F, T3> paramApp3, App<F, T4> paramApp4, App<F, T5> paramApp5, App<F, T6> paramApp6, App<F, T7> paramApp7, App<F, T8> paramApp8, App<F, T9> paramApp9, App<F, T10> paramApp10, App<F, T11> paramApp11, App<F, T12> paramApp12) {
/* 116 */     return ap6(ap6(map(Function12::curry6, paramApp), paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6), paramApp7, paramApp8, paramApp9, paramApp10, paramApp11, paramApp12);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> App<F, R> ap13(App<F, Function13<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R>> paramApp, App<F, T1> paramApp1, App<F, T2> paramApp2, App<F, T3> paramApp3, App<F, T4> paramApp4, App<F, T5> paramApp5, App<F, T6> paramApp6, App<F, T7> paramApp7, App<F, T8> paramApp8, App<F, T9> paramApp9, App<F, T10> paramApp10, App<F, T11> paramApp11, App<F, T12> paramApp12, App<F, T13> paramApp13) {
/* 120 */     return ap7(ap6(map(Function13::curry6, paramApp), paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6), paramApp7, paramApp8, paramApp9, paramApp10, paramApp11, paramApp12, paramApp13);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> App<F, R> ap14(App<F, Function14<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R>> paramApp, App<F, T1> paramApp1, App<F, T2> paramApp2, App<F, T3> paramApp3, App<F, T4> paramApp4, App<F, T5> paramApp5, App<F, T6> paramApp6, App<F, T7> paramApp7, App<F, T8> paramApp8, App<F, T9> paramApp9, App<F, T10> paramApp10, App<F, T11> paramApp11, App<F, T12> paramApp12, App<F, T13> paramApp13, App<F, T14> paramApp14) {
/* 124 */     return ap7(ap7(map(Function14::curry7, paramApp), paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7), paramApp8, paramApp9, paramApp10, paramApp11, paramApp12, paramApp13, paramApp14);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R> App<F, R> ap15(App<F, Function15<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R>> paramApp, App<F, T1> paramApp1, App<F, T2> paramApp2, App<F, T3> paramApp3, App<F, T4> paramApp4, App<F, T5> paramApp5, App<F, T6> paramApp6, App<F, T7> paramApp7, App<F, T8> paramApp8, App<F, T9> paramApp9, App<F, T10> paramApp10, App<F, T11> paramApp11, App<F, T12> paramApp12, App<F, T13> paramApp13, App<F, T14> paramApp14, App<F, T15> paramApp15) {
/* 128 */     return ap8(ap7(map(Function15::curry7, paramApp), paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7), paramApp8, paramApp9, paramApp10, paramApp11, paramApp12, paramApp13, paramApp14, paramApp15);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> App<F, R> ap16(App<F, Function16<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R>> paramApp, App<F, T1> paramApp1, App<F, T2> paramApp2, App<F, T3> paramApp3, App<F, T4> paramApp4, App<F, T5> paramApp5, App<F, T6> paramApp6, App<F, T7> paramApp7, App<F, T8> paramApp8, App<F, T9> paramApp9, App<F, T10> paramApp10, App<F, T11> paramApp11, App<F, T12> paramApp12, App<F, T13> paramApp13, App<F, T14> paramApp14, App<F, T15> paramApp15, App<F, T16> paramApp16) {
/* 132 */     return ap8(ap8(map(Function16::curry8, paramApp), paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8), paramApp9, paramApp10, paramApp11, paramApp12, paramApp13, paramApp14, paramApp15, paramApp16);
/*     */   }
/*     */   
/*     */   default <A, B, R> App<F, R> apply2(BiFunction<A, B, R> paramBiFunction, App<F, A> paramApp, App<F, B> paramApp1) {
/* 136 */     return ap2(point(paramBiFunction), paramApp, paramApp1);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, R> App<F, R> apply3(Function3<T1, T2, T3, R> paramFunction3, App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2) {
/* 140 */     return ap3(point(paramFunction3), paramApp, paramApp1, paramApp2);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, R> App<F, R> apply4(Function4<T1, T2, T3, T4, R> paramFunction4, App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3) {
/* 144 */     return ap4(point(paramFunction4), paramApp, paramApp1, paramApp2, paramApp3);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, R> App<F, R> apply5(Function5<T1, T2, T3, T4, T5, R> paramFunction5, App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4) {
/* 148 */     return ap5(point(paramFunction5), paramApp, paramApp1, paramApp2, paramApp3, paramApp4);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, R> App<F, R> apply6(Function6<T1, T2, T3, T4, T5, T6, R> paramFunction6, App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5) {
/* 152 */     return ap6(point(paramFunction6), paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, R> App<F, R> apply7(Function7<T1, T2, T3, T4, T5, T6, T7, R> paramFunction7, App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5, App<F, T7> paramApp6) {
/* 156 */     return ap7(point(paramFunction7), paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, R> App<F, R> apply8(Function8<T1, T2, T3, T4, T5, T6, T7, T8, R> paramFunction8, App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5, App<F, T7> paramApp6, App<F, T8> paramApp7) {
/* 160 */     return ap8(point(paramFunction8), paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7);
/*     */   }
/*     */   
/*     */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> App<F, R> apply9(Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R> paramFunction9, App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5, App<F, T7> paramApp6, App<F, T8> paramApp7, App<F, T9> paramApp8) {
/* 164 */     return ap9(point(paramFunction9), paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8);
/*     */   }
/*     */   
/*     */   <A> App<F, A> point(A paramA);
/*     */   
/*     */   <A, R> Function<App<F, A>, App<F, R>> lift1(App<F, Function<A, R>> paramApp);
/*     */   
/*     */   public static interface Mu extends Functor.Mu {}
/*     */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\Applicative.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */