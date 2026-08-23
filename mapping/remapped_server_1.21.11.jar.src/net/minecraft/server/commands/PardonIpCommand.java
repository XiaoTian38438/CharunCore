/*    */ package net.minecraft.server.commands;
/*    */ import com.google.common.net.InetAddresses;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.suggestion.SuggestionsBuilder;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.SharedSuggestionProvider;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.players.IpBanList;
/*    */ 
/*    */ public class PardonIpCommand {
/* 19 */   private static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType((Message)Component.translatable("commands.pardonip.invalid"));
/* 20 */   private static final SimpleCommandExceptionType ERROR_NOT_BANNED = new SimpleCommandExceptionType((Message)Component.translatable("commands.pardonip.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 23 */     paramCommandDispatcher.register(
/* 24 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("pardon-ip")
/* 25 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 26 */         .then(
/* 27 */           Commands.argument("target", (ArgumentType)StringArgumentType.word())
/* 28 */           .suggests((paramCommandContext, paramSuggestionsBuilder) -> SharedSuggestionProvider.suggest(((CommandSourceStack)paramCommandContext.getSource()).getServer().getPlayerList().getIpBans().getUserList(), paramSuggestionsBuilder))
/* 29 */           .executes(paramCommandContext -> unban((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "target")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int unban(CommandSourceStack paramCommandSourceStack, String paramString) throws CommandSyntaxException {
/* 35 */     if (!InetAddresses.isInetAddress(paramString)) {
/* 36 */       throw ERROR_INVALID.create();
/*    */     }
/*    */     
/* 39 */     IpBanList ipBanList = paramCommandSourceStack.getServer().getPlayerList().getIpBans();
/* 40 */     if (!ipBanList.isBanned(paramString)) {
/* 41 */       throw ERROR_NOT_BANNED.create();
/*    */     }
/*    */     
/* 44 */     ipBanList.remove(paramString);
/* 45 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.pardonip.success", new Object[] { paramString }), true);
/* 46 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\PardonIpCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */