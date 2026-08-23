/*    */ package net.minecraft;
/*    */ 
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface CharPredicate
/*    */ {
/*    */   default CharPredicate and(CharPredicate paramCharPredicate) {
/* 10 */     Objects.requireNonNull(paramCharPredicate);
/* 11 */     return paramChar -> (test(paramChar) && paramCharPredicate.test(paramChar));
/*    */   }
/*    */   
/*    */   default CharPredicate negate() {
/* 15 */     return paramChar -> !test(paramChar);
/*    */   }
/*    */   
/*    */   default CharPredicate or(CharPredicate paramCharPredicate) {
/* 19 */     Objects.requireNonNull(paramCharPredicate);
/* 20 */     return paramChar -> (test(paramChar) || paramCharPredicate.test(paramChar));
/*    */   }
/*    */   
/*    */   boolean test(char paramChar);
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\CharPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */