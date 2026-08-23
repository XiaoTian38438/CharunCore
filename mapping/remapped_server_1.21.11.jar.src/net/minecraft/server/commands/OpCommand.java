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
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.commands.arguments.GameProfileArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ 
/*    */ public class OpCommand {
/* 21 */   private static final SimpleCommandExceptionType ERROR_ALREADY_OP = new SimpleCommandExceptionType((Message)Component.translatable("commands.op.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 24 */     paramCommandDispatcher.register(
/* 25 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("op")
/* 26 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 27 */         .then(
/* 28 */           Commands.argument("targets", (ArgumentType)GameProfileArgument.gameProfile())
/* 29 */           .suggests((paramCommandContext, paramSuggestionsBuilder) -> {
/*    */               PlayerList playerList = ((CommandSourceStack)paramCommandContext.getSource()).getServer().getPlayerList();
/*    */               
/*    */               return SharedSuggestionProvider.suggest(playerList.getPlayers().stream().filter(()).map(()), paramSuggestionsBuilder);
/* 33 */             }).executes(paramCommandContext -> opPlayers((CommandSourceStack)paramCommandContext.getSource(), GameProfileArgument.getGameProfiles(paramCommandContext, "targets")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int opPlayers(CommandSourceStack paramCommandSourceStack, Collection<NameAndId> paramCollection) throws CommandSyntaxException {
/* 39 */     PlayerList playerList = paramCommandSourceStack.getServer().getPlayerList();
/* 40 */     byte b = 0;
/*    */     
/* 42 */     for (NameAndId nameAndId : paramCollection) {
/* 43 */       if (!playerList.isOp(nameAndId)) {
/* 44 */         playerList.op(nameAndId);
/* 45 */         b++;
/* 46 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.op.success", new Object[] { paramNameAndId.name() }), true);
/*    */       } 
/*    */     } 
/*    */     
/* 50 */     if (b == 0) {
/* 51 */       throw ERROR_ALREADY_OP.create();
/*    */     }
/*    */     
/* 54 */     return b;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\OpCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */