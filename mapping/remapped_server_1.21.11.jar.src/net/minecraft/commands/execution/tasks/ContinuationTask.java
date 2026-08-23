/*    */ package net.minecraft.commands.execution.tasks;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.execution.CommandQueueEntry;
/*    */ import net.minecraft.commands.execution.EntryAction;
/*    */ import net.minecraft.commands.execution.ExecutionContext;
/*    */ import net.minecraft.commands.execution.Frame;
/*    */ 
/*    */ public class ContinuationTask<T, P>
/*    */   implements EntryAction<T> {
/*    */   private final TaskProvider<T, P> taskFactory;
/*    */   private final List<P> arguments;
/*    */   private final CommandQueueEntry<T> selfEntry;
/*    */   private int index;
/*    */   
/*    */   private ContinuationTask(TaskProvider<T, P> paramTaskProvider, List<P> paramList, Frame paramFrame) {
/* 17 */     this.taskFactory = paramTaskProvider;
/* 18 */     this.arguments = paramList;
/* 19 */     this.selfEntry = new CommandQueueEntry(paramFrame, this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(ExecutionContext<T> paramExecutionContext, Frame paramFrame) {
/* 24 */     P p = this.arguments.get(this.index);
/* 25 */     paramExecutionContext.queueNext(this.taskFactory.create(paramFrame, p));
/* 26 */     if (++this.index < this.arguments.size()) {
/* 27 */       paramExecutionContext.queueNext(this.selfEntry);
/*    */     }
/*    */   }
/*    */   
/*    */   public static <T, P> void schedule(ExecutionContext<T> paramExecutionContext, Frame paramFrame, List<P> paramList, TaskProvider<T, P> paramTaskProvider) {
/* 32 */     int i = paramList.size();
/* 33 */     switch (i) { case 0: return;
/*    */       case 1:
/* 35 */         paramExecutionContext.queueNext(paramTaskProvider.create(paramFrame, paramList.get(0)));
/*    */ 
/*    */       
/*    */       case 2:
/* 39 */         paramExecutionContext.queueNext(paramTaskProvider.create(paramFrame, paramList.get(0)));
/* 40 */         paramExecutionContext.queueNext(paramTaskProvider.create(paramFrame, paramList.get(1))); }
/*    */     
/* 42 */     paramExecutionContext.queueNext((new ContinuationTask(paramTaskProvider, paramList, paramFrame)).selfEntry);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface TaskProvider<T, P> {
/*    */     CommandQueueEntry<T> create(Frame param1Frame, P param1P);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\execution\tasks\ContinuationTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */