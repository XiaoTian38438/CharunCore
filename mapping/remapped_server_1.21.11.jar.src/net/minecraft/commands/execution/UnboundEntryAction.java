/*   */ package net.minecraft.commands.execution;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface UnboundEntryAction<T> {
/*   */   void execute(T paramT, ExecutionContext<T> paramExecutionContext, Frame paramFrame);
/*   */   
/*   */   default EntryAction<T> bind(T paramT) {
/* 8 */     return (paramExecutionContext, paramFrame) -> execute((T)paramObject, paramExecutionContext, paramFrame);
/*   */   }
/*   */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\execution\UnboundEntryAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */