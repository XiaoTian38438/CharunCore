/*    */ package net.minecraft.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.ResultConsumer;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandExceptionType;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.execution.TraceCallbacks;
/*    */ import net.minecraft.server.permissions.PermissionSetSupplier;
/*    */ 
/*    */ public interface ExecutionCommandSource<T extends ExecutionCommandSource<T>> extends PermissionSetSupplier {
/*    */   T withCallback(CommandResultCallback paramCommandResultCallback);
/*    */   
/*    */   CommandResultCallback callback();
/*    */   
/*    */   default T clearCallbacks() {
/* 18 */     return withCallback(CommandResultCallback.EMPTY);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   CommandDispatcher<T> dispatcher();
/*    */ 
/*    */   
/*    */   void handleError(CommandExceptionType paramCommandExceptionType, Message paramMessage, boolean paramBoolean, TraceCallbacks paramTraceCallbacks);
/*    */ 
/*    */   
/*    */   boolean isSilent();
/*    */ 
/*    */   
/*    */   default void handleError(CommandSyntaxException paramCommandSyntaxException, boolean paramBoolean, TraceCallbacks paramTraceCallbacks) {
/* 33 */     handleError(paramCommandSyntaxException.getType(), paramCommandSyntaxException.getRawMessage(), paramBoolean, paramTraceCallbacks);
/*    */   }
/*    */ 
/*    */   
/*    */   static <T extends ExecutionCommandSource<T>> ResultConsumer<T> resultConsumer() {
/* 38 */     return (paramCommandContext, paramBoolean, paramInt) -> ((ExecutionCommandSource)paramCommandContext.getSource()).callback().onResult(paramBoolean, paramInt);
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\ExecutionCommandSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */