/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.commands.arguments.GameProfileArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.UserBanList;
/*    */ 
/*    */ public class PardonCommand {
/* 21 */   private static final SimpleCommandExceptionType ERROR_NOT_BANNED = new SimpleCommandExceptionType((Message)Component.translatable("commands.pardon.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 24 */     paramCommandDispatcher.register(
/* 25 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("pardon")
/* 26 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 27 */         .then(
/* 28 */           Commands.argument("targets", (ArgumentType)GameProfileArgument.gameProfile())
/* 29 */           .suggests((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggest(((CommandSourceStack)paramCommandContext.getSource()).getServer().getPlayerList().getBans().getUserList(), paramSuggestionsBuilder))
/* 30 */           .executes(paramCommandContext -> pardonPlayers((CommandSourceStack)paramCommandContext.getSource(), GameProfileArgument.getGameProfiles(paramCommandContext, "targets")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int pardonPlayers(CommandSourceStack paramCommandSourceStack, Collection<NameAndId> paramCollection) throws CommandSyntaxException {
/* 36 */     UserBanList userBanList = paramCommandSourceStack.getServer().getPlayerList().getBans();
/* 37 */     byte b = 0;
/*    */     
/* 39 */     for (NameAndId nameAndId : paramCollection) {
/* 40 */       if (userBanList.isBanned(nameAndId)) {
/* 41 */         userBanList.remove(nameAndId);
/* 42 */         b++;
/* 43 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.pardon.success", new Object[] { Component.literal(paramNameAndId.name()) }), true);
/*    */       } 
/*    */     } 
/*    */     
/* 47 */     if (b == 0) {
/* 48 */       throw ERROR_NOT_BANNED.create();
/*    */     }
/*    */     
/* 51 */     return b;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\PardonCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */