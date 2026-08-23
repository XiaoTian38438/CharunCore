/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*    */ import com.mojang.brigadier.tree.CommandNode;
/*    */ import com.mojang.brigadier.tree.LiteralCommandNode;
/*    */ import java.util.List;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.HoverEvent;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.chat.OutgoingChatMessage;
/*    */ import net.minecraft.network.chat.PlayerChatMessage;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.scores.PlayerTeam;
/*    */ 
/*    */ public class TeamMsgCommand {
/* 27 */   private static final Style SUGGEST_STYLE = Style.EMPTY
/* 28 */     .withHoverEvent((HoverEvent)new HoverEvent.ShowText((Component)Component.translatable("chat.type.team.hover")))
/* 29 */     .withClickEvent((ClickEvent)new ClickEvent.SuggestCommand("/teammsg "));
/*    */   
/* 31 */   private static final SimpleCommandExceptionType ERROR_NOT_ON_TEAM = new SimpleCommandExceptionType((Message)Component.translatable("commands.teammsg.failed.noteam"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 34 */     LiteralCommandNode literalCommandNode = paramCommandDispatcher.register(
/* 35 */         (LiteralArgumentBuilder)Commands.literal("teammsg")
/* 36 */         .then(
/* 37 */           Commands.argument("message", (ArgumentType)MessageArgument.message())
/* 38 */           .executes(paramCommandContext -> {
/*    */               CommandSourceStack commandSourceStack = (CommandSourceStack)paramCommandContext.getSource();
/*    */ 
/*    */               
/*    */               Entity entity = commandSourceStack.getEntityOrException();
/*    */ 
/*    */               
/*    */               PlayerTeam playerTeam = entity.getTeam();
/*    */               
/*    */               if (playerTeam == null) {
/*    */                 throw ERROR_NOT_ON_TEAM.create();
/*    */               }
/*    */               
/*    */               List list = commandSourceStack.getServer().getPlayerList().getPlayers().stream().filter(()).toList();
/*    */               
/*    */               if (!list.isEmpty()) {
/*    */                 MessageArgument.resolveChatMessage(paramCommandContext, "message", ());
/*    */               }
/*    */               
/*    */               return list.size();
/*    */             })));
/*    */     
/* 60 */     paramCommandDispatcher.register((LiteralArgumentBuilder)Commands.literal("tm").redirect((CommandNode)literalCommandNode));
/*    */   }
/*    */   
/*    */   private static void sendMessage(CommandSourceStack paramCommandSourceStack, Entity paramEntity, PlayerTeam paramPlayerTeam, List<ServerPlayer> paramList, PlayerChatMessage paramPlayerChatMessage) {
/* 64 */     MutableComponent mutableComponent = paramPlayerTeam.getFormattedDisplayName().withStyle(SUGGEST_STYLE);
/* 65 */     ChatType.Bound bound1 = ChatType.bind(ChatType.TEAM_MSG_COMMAND_INCOMING, paramCommandSourceStack).withTargetName((Component)mutableComponent);
/* 66 */     ChatType.Bound bound2 = ChatType.bind(ChatType.TEAM_MSG_COMMAND_OUTGOING, paramCommandSourceStack).withTargetName((Component)mutableComponent);
/* 67 */     OutgoingChatMessage outgoingChatMessage = OutgoingChatMessage.create(paramPlayerChatMessage);
/*    */     
/* 69 */     int i = 0;
/*    */     
/* 71 */     for (ServerPlayer serverPlayer : paramList) {
/* 72 */       ChatType.Bound bound = (serverPlayer == paramEntity) ? bound2 : bound1;
/*    */       
/* 74 */       boolean bool = paramCommandSourceStack.shouldFilterMessageTo(serverPlayer);
/* 75 */       serverPlayer.sendChatMessage(outgoingChatMessage, bool, bound);
/*    */       
/* 77 */       i |= (bool && paramPlayerChatMessage.isFullyFiltered()) ? 1 : 0;
/*    */     } 
/*    */     
/* 80 */     if (i != 0)
/* 81 */       paramCommandSourceStack.sendSystemMessage(PlayerList.CHAT_FILTERED_FULL); 
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\TeamMsgCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */