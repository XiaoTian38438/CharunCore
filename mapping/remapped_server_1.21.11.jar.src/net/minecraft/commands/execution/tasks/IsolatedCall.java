/*    */ package net.minecraft.commands.execution.tasks;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.commands.CommandResultCallback;
/*    */ import net.minecraft.commands.ExecutionCommandSource;
/*    */ import net.minecraft.commands.execution.EntryAction;
/*    */ import net.minecraft.commands.execution.ExecutionContext;
/*    */ import net.minecraft.commands.execution.ExecutionControl;
/*    */ import net.minecraft.commands.execution.Frame;
/*    */ 
/*    */ public class IsolatedCall<T extends ExecutionCommandSource<T>>
/*    */   implements EntryAction<T> {
/*    */   private final Consumer<ExecutionControl<T>> taskProducer;
/*    */   private final CommandResultCallback output;
/*    */   
/*    */   public IsolatedCall(Consumer<ExecutionControl<T>> paramConsumer, CommandResultCallback paramCommandResultCallback) {
/* 17 */     this.taskProducer = paramConsumer;
/* 18 */     this.output = paramCommandResultCallback;
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(ExecutionContext<T> paramExecutionContext, Frame paramFrame) {
/* 23 */     int i = paramFrame.depth() + 1;
/* 24 */     Frame frame = new Frame(i, this.output, paramExecutionContext.frameControlForDepth(i));
/* 25 */     this.taskProducer.accept(ExecutionControl.create(paramExecutionContext, frame));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\execution\tasks\IsolatedCall.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */