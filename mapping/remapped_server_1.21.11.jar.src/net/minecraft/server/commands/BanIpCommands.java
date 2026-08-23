/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.google.common.net.InetAddresses;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.commands.arguments.selector.EntitySelector;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.IpBanList;
/*    */ import net.minecraft.server.players.IpBanListEntry;
/*    */ 
/*    */ public class BanIpCommands
/*    */ {
/* 26 */   private static final SimpleCommandExceptionType ERROR_INVALID_IP = new SimpleCommandExceptionType((Message)Component.translatable("commands.banip.invalid"));
/* 27 */   private static final SimpleCommandExceptionType ERROR_ALREADY_BANNED = new SimpleCommandExceptionType((Message)Component.translatable("commands.banip.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 30 */     paramCommandDispatcher.register(
/* 31 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("ban-ip")
/* 32 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 33 */         .then((
/* 34 */           (RequiredArgumentBuilder)Commands.argument("target", (ArgumentType)StringArgumentType.word())
/* 35 */           .executes(paramCommandContext -> banIpOrName((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "target"), null)))
/* 36 */           .then(
/* 37 */             Commands.argument("reason", (ArgumentType)MessageArgument.message())
/* 38 */             .executes(paramCommandContext -> banIpOrName((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "target"), MessageArgument.getMessage(paramCommandContext, "reason"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int banIpOrName(CommandSourceStack paramCommandSourceStack, String paramString, Component paramComponent) throws CommandSyntaxException {
/* 45 */     if (InetAddresses.isInetAddress(paramString)) {
/* 46 */       return banIp(paramCommandSourceStack, paramString, paramComponent);
/*    */     }
/* 48 */     ServerPlayer serverPlayer = paramCommandSourceStack.getServer().getPlayerList().getPlayerByName(paramString);
/* 49 */     if (serverPlayer != null) {
/* 50 */       return banIp(paramCommandSourceStack, serverPlayer.getIpAddress(), paramComponent);
/*    */     }
/*    */     
/* 53 */     throw ERROR_INVALID_IP.create();
/*    */   }
/*    */   
/*    */   private static int banIp(CommandSourceStack paramCommandSourceStack, String paramString, Component paramComponent) throws CommandSyntaxException {
/* 57 */     IpBanList ipBanList = paramCommandSourceStack.getServer().getPlayerList().getIpBans();
/* 58 */     if (ipBanList.isBanned(paramString)) {
/* 59 */       throw ERROR_ALREADY_BANNED.create();
/*    */     }
/* 61 */     List list = paramCommandSourceStack.getServer().getPlayerList().getPlayersWithAddress(paramString);
/* 62 */     IpBanListEntry ipBanListEntry = new IpBanListEntry(paramString, null, paramCommandSourceStack.getTextName(), null, (paramComponent == null) ? null : paramComponent.getString());
/* 63 */     ipBanList.add(ipBanListEntry);
/*    */     
/* 65 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.banip.success", new Object[] { paramString, paramIpBanListEntry.getReasonMessage() }), true);
/* 66 */     if (!list.isEmpty()) {
/* 67 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.banip.info", new Object[] { Integer.valueOf(paramList.size()), EntitySelector.joinNames(paramList) }), true);
/*    */     }
/*    */     
/* 70 */     for (ServerPlayer serverPlayer : list) {
/* 71 */       serverPlayer.connection.disconnect((Component)Component.translatable("multiplayer.disconnect.ip_banned"));
/*    */     }
/*    */     
/* 74 */     return list.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\BanIpCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */