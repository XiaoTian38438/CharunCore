/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.arguments.IntegerArgumentType;
/*    */ import com.mojang.brigadier.arguments.StringArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.common.ClientboundTransferPacket;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class TransferCommand
/*    */ {
/* 26 */   private static final SimpleCommandExceptionType ERROR_NO_PLAYERS = new SimpleCommandExceptionType((Message)Component.translatable("commands.transfer.error.no_players"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 29 */     paramCommandDispatcher.register(
/* 30 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("transfer")
/* 31 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 32 */         .then((
/* 33 */           (RequiredArgumentBuilder)Commands.argument("hostname", (ArgumentType)StringArgumentType.string())
/* 34 */           .executes(paramCommandContext -> transfer((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "hostname"), 25565, List.of(((CommandSourceStack)paramCommandContext.getSource()).getPlayerOrException()))))
/* 35 */           .then((
/* 36 */             (RequiredArgumentBuilder)Commands.argument("port", (ArgumentType)IntegerArgumentType.integer(1, 65535))
/* 37 */             .executes(paramCommandContext -> transfer((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "hostname"), IntegerArgumentType.getInteger(paramCommandContext, "port"), List.of(((CommandSourceStack)paramCommandContext.getSource()).getPlayerOrException()))))
/* 38 */             .then(
/* 39 */               Commands.argument("players", (ArgumentType)EntityArgument.players())
/* 40 */               .executes(paramCommandContext -> transfer((CommandSourceStack)paramCommandContext.getSource(), StringArgumentType.getString(paramCommandContext, "hostname"), IntegerArgumentType.getInteger(paramCommandContext, "port"), EntityArgument.getPlayers(paramCommandContext, "players")))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int transfer(CommandSourceStack paramCommandSourceStack, String paramString, int paramInt, Collection<ServerPlayer> paramCollection) throws CommandSyntaxException {
/* 48 */     if (paramCollection.isEmpty()) {
/* 49 */       throw ERROR_NO_PLAYERS.create();
/*    */     }
/*    */     
/* 52 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 53 */       serverPlayer.connection.send((Packet)new ClientboundTransferPacket(paramString, paramInt));
/*    */     }
/* 55 */     if (paramCollection.size() == 1) {
/* 56 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.transfer.success.single", new Object[] { ((ServerPlayer)paramCollection.iterator().next()).getDisplayName(), paramString, Integer.valueOf(paramInt) }), true);
/*    */     } else {
/* 58 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.transfer.success.multiple", new Object[] { Integer.valueOf(paramCollection.size()), paramString, Integer.valueOf(paramInt) }), true);
/*    */     } 
/* 60 */     return paramCollection.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\TransferCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */