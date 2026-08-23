/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ public interface ParseState<S>
/*    */ {
/*    */   Scope scope();
/*    */   
/*    */   ErrorCollector<S> errorCollector();
/*    */   
/*    */   default <T> Optional<T> parseTopRule(NamedRule<S, T> paramNamedRule) {
/* 13 */     T t = (T)parse((NamedRule)paramNamedRule);
/* 14 */     if (t != null)
/*    */     {
/* 16 */       errorCollector().finish(mark());
/*    */     }
/*    */     
/* 19 */     if (!scope().hasOnlySingleFrame()) {
/* 20 */       throw new IllegalStateException("Malformed scope: " + String.valueOf(scope()));
/*    */     }
/*    */     
/* 23 */     return Optional.ofNullable(t);
/*    */   }
/*    */   
/*    */   <T> T parse(NamedRule<S, T> paramNamedRule);
/*    */   
/*    */   S input();
/*    */   
/*    */   int mark();
/*    */   
/*    */   void restore(int paramInt);
/*    */   
/*    */   Control acquireControl();
/*    */   
/*    */   void releaseControl();
/*    */   
/*    */   ParseState<S> silent();
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\ParseState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */