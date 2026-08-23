/*    */ package net.minecraft.commands.execution.tasks;
/*    */ 
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ import net.minecraft.commands.execution.EntryAction;
/*    */ import net.minecraft.commands.execution.ExecutionContext;
/*    */ import net.minecraft.commands.execution.Frame;
/*    */ 
/*    */ public class FallthroughTask<T extends ExecutionCommandSource<T>> implements EntryAction<T> {
/*  9 */   private static final FallthroughTask<? extends ExecutionCommandSource<?>> INSTANCE = new FallthroughTask();
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends ExecutionCommandSource<T>> EntryAction<T> instance() {
/* 14 */     return (EntryAction)INSTANCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(ExecutionContext<T> paramExecutionContext, Frame paramFrame) {
/* 19 */     paramFrame.returnFailure();
/*    */ 
/*    */     
/* 22 */     paramFrame.discard();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\execution\tasks\FallthroughTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */