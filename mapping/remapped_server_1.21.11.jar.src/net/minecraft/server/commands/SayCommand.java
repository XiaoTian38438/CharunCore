/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.PlayerChatMessage;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ 
/*    */ public class SayCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 16 */     paramCommandDispatcher.register(
/* 17 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("say")
/* 18 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 19 */         .then(
/* 20 */           Commands.argument("message", (ArgumentType)MessageArgument.message())
/* 21 */           .executes(paramCommandContext -> {
/*    */               MessageArgument.resolveChatMessage(paramCommandContext, "message", ());
/*    */               return 1;
/*    */             })));
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\SayCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */