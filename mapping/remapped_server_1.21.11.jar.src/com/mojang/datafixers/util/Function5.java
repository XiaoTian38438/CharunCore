/*    */ package com.mojang.datafixers.util;
/*    */ 
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ public interface Function5<T1, T2, T3, T4, T5, R>
/*    */ {
/*    */   default Function<T1, Function4<T2, T3, T4, T5, R>> curry() {
/* 10 */     return paramObject -> ();
/*    */   }
/*    */   
/*    */   default BiFunction<T1, T2, Function3<T3, T4, T5, R>> curry2() {
/* 14 */     return (paramObject1, paramObject2) -> ();
/*    */   }
/*    */   
/*    */   default Function3<T1, T2, T3, BiFunction<T4, T5, R>> curry3() {
/* 18 */     return (paramObject1, paramObject2, paramObject3) -> ();
/*    */   }
/*    */   
/*    */   default Function4<T1, T2, T3, T4, Function<T5, R>> curry4() {
/* 22 */     return (paramObject1, paramObject2, paramObject3, paramObject4) -> ();
/*    */   }
/*    */   
/*    */   R apply(T1 paramT1, T2 paramT2, T3 paramT3, T4 paramT4, T5 paramT5);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\com\mojang\datafixer\\util\Function5.class
 * Java compiler version: 17 (61.0)
 * JD-Core Version:       1.1.3
 */