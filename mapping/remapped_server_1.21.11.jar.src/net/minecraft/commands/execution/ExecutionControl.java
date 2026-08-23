/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ExecutionControl<T>
/*    */ {
/*    */   void queueNext(EntryAction<T> paramEntryAction);
/*    */   
/*    */   void tracer(TraceCallbacks paramTraceCallbacks);
/*    */   
/*    */   TraceCallbacks tracer();
/*    */   
/*    */   Frame currentFrame();
/*    */   
/*    */   static <T extends net.minecraft.commands.ExecutionCommandSource<T>> ExecutionControl<T> create(final ExecutionContext<T> context, final Frame frame) {
/* 16 */     return new ExecutionControl<T>()
/*    */       {
/*    */         public void queueNext(EntryAction<T> param1EntryAction) {
/* 19 */           context.queueNext(new CommandQueueEntry<>(frame, param1EntryAction));
/*    */         }
/*    */ 
/*    */         
/*    */         public void tracer(TraceCallbacks param1TraceCallbacks) {
/* 24 */           context.tracer(param1TraceCallbacks);
/*    */         }
/*    */ 
/*    */         
/*    */         public TraceCallbacks tracer() {
/* 29 */           return context.tracer();
/*    */         }
/*    */ 
/*    */         
/*    */         public Frame currentFrame() {
/* 34 */           return frame;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\execution\ExecutionControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */