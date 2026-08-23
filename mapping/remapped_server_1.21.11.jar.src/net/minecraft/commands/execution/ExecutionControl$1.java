/*    */ package net.minecraft.commands.execution;
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
/*    */ class null
/*    */   implements ExecutionControl<T>
/*    */ {
/*    */   public void queueNext(EntryAction<T> paramEntryAction) {
/* 19 */     context.queueNext(new CommandQueueEntry<>(frame, paramEntryAction));
/*    */   }
/*    */ 
/*    */   
/*    */   public void tracer(TraceCallbacks paramTraceCallbacks) {
/* 24 */     context.tracer(paramTraceCallbacks);
/*    */   }
/*    */ 
/*    */   
/*    */   public TraceCallbacks tracer() {
/* 29 */     return context.tracer();
/*    */   }
/*    */ 
/*    */   
/*    */   public Frame currentFrame() {
/* 34 */     return frame;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\execution\ExecutionControl$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */