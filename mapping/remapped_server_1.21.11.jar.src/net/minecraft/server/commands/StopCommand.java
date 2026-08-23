/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class StopCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 12 */     paramCommandDispatcher.register(
/* 13 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("stop")
/* 14 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_OWNERS)))
/* 15 */         .executes(paramCommandContext -> {
/*    */             ((CommandSourceStack)paramCommandContext.getSource()).sendSuccess((), true);
/*    */             ((CommandSourceStack)paramCommandContext.getSource()).getServer().halt(false);
/*    */             return 1;
/*    */           }));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\StopCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */