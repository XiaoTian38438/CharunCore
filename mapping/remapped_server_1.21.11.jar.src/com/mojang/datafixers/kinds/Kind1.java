/*    */ package com.mojang.datafixers.kinds;
/*    */ 
/*    */ import com.mojang.datafixers.Products;
/*    */ 
/*    */ public interface Kind1<F extends K1, Mu extends Kind1.Mu>
/*    */   extends App<Mu, F>
/*    */ {
/*    */   static <F extends K1, Proof extends Mu> Kind1<F, Proof> unbox(App<Proof, F> paramApp) {
/*  9 */     return (Kind1<F, Proof>)paramApp;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   default <T1> Products.P1<F, T1> group(App<F, T1> paramApp) {
/* 15 */     return new Products.P1(paramApp);
/*    */   }
/*    */   
/*    */   default <T1, T2> Products.P2<F, T1, T2> group(App<F, T1> paramApp, App<F, T2> paramApp1) {
/* 19 */     return new Products.P2(paramApp, paramApp1);
/*    */   }
/*    */   
/*    */   default <T1, T2, T3> Products.P3<F, T1, T2, T3> group(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2) {
/* 23 */     return new Products.P3(paramApp, paramApp1, paramApp2);
/*    */   }
/*    */   
/*    */   default <T1, T2, T3, T4> Products.P4<F, T1, T2, T3, T4> group(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3) {
/* 27 */     return new Products.P4(paramApp, paramApp1, paramApp2, paramApp3);
/*    */   }
/*    */   
/*    */   default <T1, T2, T3, T4, T5> Products.P5<F, T1, T2, T3, T4, T5> group(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4) {
/* 31 */     return new Products.P5(paramApp, paramApp1, paramApp2, paramApp3, paramApp4);
/*    */   }
/*    */   
/*    */   default <T1, T2, T3, T4, T5, T6> Products.P6<F, T1, T2, T3, T4, T5, T6> group(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5) {
/* 35 */     return new Products.P6(paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5);
/*    */   }
/*    */   
/*    */   default <T1, T2, T3, T4, T5, T6, T7> Products.P7<F, T1, T2, T3, T4, T5, T6, T7> group(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5, App<F, T7> paramApp6) {
/* 39 */     return new Products.P7(paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6);
/*    */   }
/*    */   
/*    */   default <T1, T2, T3, T4, T5, T6, T7, T8> Products.P8<F, T1, T2, T3, T4, T5, T6, T7, T8> group(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5, App<F, T7> paramApp6, App<F, T8> paramApp7) {
/* 43 */     return new Products.P8(paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7);
/*    */   }
/*    */   
/*    */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9> Products.P9<F, T1, T2, T3, T4, T5, T6, T7, T8, T9> group(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5, App<F, T7> paramApp6, App<F, T8> paramApp7, App<F, T9> paramApp8) {
/* 47 */     return new Products.P9(paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8);
/*    */   }
/*    */   
/*    */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Products.P10<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> group(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5, App<F, T7> paramApp6, App<F, T8> paramApp7, App<F, T9> paramApp8, App<F, T10> paramApp9) {
/* 51 */     return new Products.P10(paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8, paramApp9);
/*    */   }
/*    */   
/*    */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> Products.P11<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11> group(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5, App<F, T7> paramApp6, App<F, T8> paramApp7, App<F, T9> paramApp8, App<F, T10> paramApp9, App<F, T11> paramApp10) {
/* 55 */     return new Products.P11(paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8, paramApp9, paramApp10);
/*    */   }
/*    */   
/*    */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> Products.P12<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12> group(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5, App<F, T7> paramApp6, App<F, T8> paramApp7, App<F, T9> paramApp8, App<F, T10> paramApp9, App<F, T11> paramApp10, App<F, T12> paramApp11) {
/* 59 */     return new Products.P12(paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8, paramApp9, paramApp10, paramApp11);
/*    */   }
/*    */   
/*    */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> Products.P13<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13> group(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5, App<F, T7> paramApp6, App<F, T8> paramApp7, App<F, T9> paramApp8, App<F, T10> paramApp9, App<F, T11> paramApp10, App<F, T12> paramApp11, App<F, T13> paramApp12) {
/* 63 */     return new Products.P13(paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8, paramApp9, paramApp10, paramApp11, paramApp12);
/*    */   }
/*    */   
/*    */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> Products.P14<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14> group(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5, App<F, T7> paramApp6, App<F, T8> paramApp7, App<F, T9> paramApp8, App<F, T10> paramApp9, App<F, T11> paramApp10, App<F, T12> paramApp11, App<F, T13> paramApp12, App<F, T14> paramApp13) {
/* 67 */     return new Products.P14(paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8, paramApp9, paramApp10, paramApp11, paramApp12, paramApp13);
/*    */   }
/*    */   
/*    */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> Products.P15<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15> group(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5, App<F, T7> paramApp6, App<F, T8> paramApp7, App<F, T9> paramApp8, App<F, T10> paramApp9, App<F, T11> paramApp10, App<F, T12> paramApp11, App<F, T13> paramApp12, App<F, T14> paramApp13, App<F, T15> paramApp14) {
/* 71 */     return new Products.P15(paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8, paramApp9, paramApp10, paramApp11, paramApp12, paramApp13, paramApp14);
/*    */   }
/*    */   
/*    */   default <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> Products.P16<F, T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16> group(App<F, T1> paramApp, App<F, T2> paramApp1, App<F, T3> paramApp2, App<F, T4> paramApp3, App<F, T5> paramApp4, App<F, T6> paramApp5, App<F, T7> paramApp6, App<F, T8> paramApp7, App<F, T9> paramApp8, App<F, T10> paramApp9, App<F, T11> paramApp10, App<F, T12> paramApp11, App<F, T13> paramApp12, App<F, T14> paramApp13, App<F, T15> paramApp14, App<F, T16> paramApp15) {
/* 75 */     return new Products.P16(paramApp, paramApp1, paramApp2, paramApp3, paramApp4, paramApp5, paramApp6, paramApp7, paramApp8, paramApp9, paramApp10, paramApp11, paramApp12, paramApp13, paramApp14, paramApp15);
/*    */   }
/*    */   
/*    */   public static interface Mu extends K1 {}
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixers\kinds\Kind1.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */