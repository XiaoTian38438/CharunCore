/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.PlayerChatMessage;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ 
/*    */ public class EmoteCommands {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 15 */     paramCommandDispatcher.register(
/* 16 */         (LiteralArgumentBuilder)Commands.literal("me")
/* 17 */         .then(
/* 18 */           Commands.argument("action", (ArgumentType)MessageArgument.message()).executes(paramCommandContext -> {
/*    */               MessageArgument.resolveChatMessage(paramCommandContext, "action", ());
/*    */               return 1;
/*    */             })));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\EmoteCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */