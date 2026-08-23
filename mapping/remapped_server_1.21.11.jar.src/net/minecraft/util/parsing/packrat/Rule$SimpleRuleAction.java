/*    */ package net.minecraft.util.parsing.packrat;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface SimpleRuleAction<S, T>
/*    */   extends Rule.RuleAction<S, T>
/*    */ {
/*    */   T run(Scope paramScope);
/*    */   
/*    */   default T run(ParseState<S> paramParseState) {
/* 25 */     return run(paramParseState.scope());
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraf\\util\parsing\packrat\Rule$SimpleRuleAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */