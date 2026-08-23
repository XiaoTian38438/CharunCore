/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*    */ import java.util.Collection;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.OutgoingChatMessage;
/*    */ import net.minecraft.network.chat.PlayerChatMessage;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ 
/*    */ public class MsgCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 23 */     LiteralCommandNode literalCommandNode = paramCommandDispatcher.register(
/* 24 */         (LiteralArgumentBuilder)Commands.literal("msg")
/* 25 */         .then(
/* 26 */           Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 27 */           .then(
/* 28 */             Commands.argument("message", (ArgumentType)MessageArgument.message())
/* 29 */             .executes(paramCommandContext -> {
/*    */                 Collection collection = EntityArgument.getPlayers(paramCommandContext, "targets");
/*    */ 
/*    */                 
/*    */                 if (!collection.isEmpty()) {
/*    */                   MessageArgument.resolveChatMessage(paramCommandContext, "message", ());
/*    */                 }
/*    */ 
/*    */                 
/*    */                 return collection.size();
/*    */               }))));
/*    */     
/* 41 */     paramCommandDispatcher.register((LiteralArgumentBuilder)Commands.literal("tell").redirect((CommandNode)literalCommandNode));
/* 42 */     paramCommandDispatcher.register((LiteralArgumentBuilder)Commands.literal("w").redirect((CommandNode)literalCommandNode));
/*    */   }
/*    */   
/*    */   private static void sendMessage(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection, PlayerChatMessage paramPlayerChatMessage) {
/* 46 */     ChatType.Bound bound = ChatType.bind(ChatType.MSG_COMMAND_INCOMING, paramCommandSourceStack);
/* 47 */     OutgoingChatMessage outgoingChatMessage = OutgoingChatMessage.create(paramPlayerChatMessage);
/*    */     
/* 49 */     int i = 0;
/*    */     
/* 51 */     for (ServerPlayer serverPlayer : paramCollection) {
/*    */       
/* 53 */       ChatType.Bound bound1 = ChatType.bind(ChatType.MSG_COMMAND_OUTGOING, paramCommandSourceStack).withTargetName(serverPlayer.getDisplayName());
/* 54 */       paramCommandSourceStack.sendChatMessage(outgoingChatMessage, false, bound1);
/*    */       
/* 56 */       boolean bool = paramCommandSourceStack.shouldFilterMessageTo(serverPlayer);
/* 57 */       serverPlayer.sendChatMessage(outgoingChatMessage, bool, bound);
/*    */       
/* 59 */       i |= (bool && paramPlayerChatMessage.isFullyFiltered()) ? 1 : 0;
/*    */     } 
/*    */     
/* 62 */     if (i != 0)
/* 63 */       paramCommandSourceStack.sendSystemMessage(PlayerList.CHAT_FILTERED_FULL); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\MsgCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */