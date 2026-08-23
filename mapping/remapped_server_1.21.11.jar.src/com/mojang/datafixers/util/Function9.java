/*    */ package com.mojang.datafixers.util;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public interface Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, R>
/*    */ {
/*    */   default Function<T1, Function8<T2, T3, T4, T5, T6, T7, T8, T9, R>> curry() {
/* 10 */     return paramObject -> ();
/*    */   }
/*    */   
/*    */   default BiFunction<T1, T2, Function7<T3, T4, T5, T6, T7, T8, T9, R>> curry2() {
/* 14 */     return (paramObject1, paramObject2) -> ();
/*    */   }
/*    */   
/*    */   default Function3<T1, T2, T3, Function6<T4, T5, T6, T7, T8, T9, R>> curry3() {
/* 18 */     return (paramObject1, paramObject2, paramObject3) -> ();
/*    */   }
/*    */   
/*    */   default Function4<T1, T2, T3, T4, Function5<T5, T6, T7, T8, T9, R>> curry4() {
/* 22 */     return (paramObject1, paramObject2, paramObject3, paramObject4) -> ();
/*    */   }
/*    */   
/*    */   default Function5<T1, T2, T3, T4, T5, Function4<T6, T7, T8, T9, R>> curry5() {
/* 26 */     return (paramObject1, paramObject2, paramObject3, paramObject4, paramObject5) -> ();
/*    */   }
/*    */   
/*    */   default Function6<T1, T2, T3, T4, T5, T6, Function3<T7, T8, T9, R>> curry6() {
/* 30 */     return (paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6) -> ();
/*    */   }
/*    */   
/*    */   default Function7<T1, T2, T3, T4, T5, T6, T7, BiFunction<T8, T9, R>> curry7() {
/* 34 */     return (paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7) -> ();
/*    */   }
/*    */   
/*    */   default Function8<T1, T2, T3, T4, T5, T6, T7, T8, Function<T9, R>> curry8() {
/* 38 */     return (paramObject1, paramObject2, paramObject3, paramObject4, paramObject5, paramObject6, paramObject7, paramObject8) -> ();
/*    */   }
/*    */   
/*    */   R apply(T1 paramT1, T2 paramT2, T3 paramT3, T4 paramT4, T5 paramT5, T6 paramT6, T7 paramT7, T8 paramT8, T9 paramT9);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixer\\util\Function9.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */