/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.GameProfileArgument;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ import net.minecraft.server.players.UserBanList;
/*    */ import net.minecraft.server.players.UserBanListEntry;
/*    */ 
/*    */ public class BanPlayerCommands
/*    */ {
/* 25 */   private static final SimpleCommandExceptionType ERROR_ALREADY_BANNED = new SimpleCommandExceptionType((Message)Component.translatable("commands.ban.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 28 */     paramCommandDispatcher.register(
/* 29 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("ban")
/* 30 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 31 */         .then((
/* 32 */           (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)GameProfileArgument.gameProfile())
/* 33 */           .executes(paramCommandContext -> banPlayers((CommandSourceStack)paramCommandContext.getSource(), GameProfileArgument.getGameProfiles(paramCommandContext, "targets"), null)))
/* 34 */           .then(
/* 35 */             Commands.argument("reason", (ArgumentType)MessageArgument.message())
/* 36 */             .executes(paramCommandContext -> banPlayers((CommandSourceStack)paramCommandContext.getSource(), GameProfileArgument.getGameProfiles(paramCommandContext, "targets"), MessageArgument.getMessage(paramCommandContext, "reason"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int banPlayers(CommandSourceStack paramCommandSourceStack, Collection<NameAndId> paramCollection, Component paramComponent) throws CommandSyntaxException {
/* 43 */     UserBanList userBanList = paramCommandSourceStack.getServer().getPlayerList().getBans();
/* 44 */     byte b = 0;
/*    */     
/* 46 */     for (NameAndId nameAndId : paramCollection) {
/* 47 */       if (!userBanList.isBanned(nameAndId)) {
/* 48 */         UserBanListEntry userBanListEntry = new UserBanListEntry(nameAndId, null, paramCommandSourceStack.getTextName(), null, (paramComponent == null) ? null : paramComponent.getString());
/* 49 */         userBanList.add(userBanListEntry);
/* 50 */         b++;
/* 51 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.ban.success", new Object[] { Component.literal(paramNameAndId.name()), paramUserBanListEntry.getReasonMessage() }), true);
/*    */         
/* 53 */         ServerPlayer serverPlayer = paramCommandSourceStack.getServer().getPlayerList().getPlayer(nameAndId.id());
/* 54 */         if (serverPlayer != null) {
/* 55 */           serverPlayer.connection.disconnect((Component)Component.translatable("multiplayer.disconnect.banned"));
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 60 */     if (b == 0) {
/* 61 */       throw ERROR_ALREADY_BANNED.create();
/*    */     }
/*    */     
/* 64 */     return b;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\BanPlayerCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */