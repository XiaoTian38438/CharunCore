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
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ 
/*    */ public class DeOpCommands {
/* 21 */   private static final SimpleCommandExceptionType ERROR_NOT_OP = new SimpleCommandExceptionType((Message)Component.translatable("commands.deop.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 24 */     paramCommandDispatcher.register(
/* 25 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("deop")
/* 26 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 27 */         .then(
/* 28 */           Commands.argument("targets", (ArgumentType)GameProfileArgument.gameProfile())
/* 29 */           .suggests((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggest(((CommandSourceStack)paramCommandContext.getSource()).getServer().getPlayerList().getOpNames(), paramSuggestionsBuilder))
/* 30 */           .executes(paramCommandContext -> deopPlayers((CommandSourceStack)paramCommandContext.getSource(), GameProfileArgument.getGameProfiles(paramCommandContext, "targets")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int deopPlayers(CommandSourceStack paramCommandSourceStack, Collection<NameAndId> paramCollection) throws CommandSyntaxException {
/* 36 */     PlayerList playerList = paramCommandSourceStack.getServer().getPlayerList();
/* 37 */     byte b = 0;
/*    */     
/* 39 */     for (NameAndId nameAndId : paramCollection) {
/* 40 */       if (playerList.isOp(nameAndId)) {
/* 41 */         playerList.deop(nameAndId);
/* 42 */         b++;
/* 43 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.deop.success", new Object[] { ((NameAndId)paramCollection.iterator().next()).name() }), true);
/*    */       } 
/*    */     } 
/*    */     
/* 47 */     if (b == 0) {
/* 48 */       throw ERROR_NOT_OP.create();
/*    */     }
/*    */     
/* 51 */     paramCommandSourceStack.getServer().kickUnlistedPlayers();
/* 52 */     return b;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\DeOpCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */