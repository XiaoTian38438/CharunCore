/*    */ package net.minecraft.commands.execution;
/*    */ 
/*    */ import com.mojang.brigadier.Command;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface CommandAdapter<T>
/*    */   extends Command<T>, CustomCommandExecutor<T>
/*    */ {
/*    */   default int run(CommandContext<T> paramCommandContext) throws CommandSyntaxException {
/* 16 */     throw new UnsupportedOperationException("This function should not run");
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\commands\execution\CustomCommandExecutor$CommandAdapter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */