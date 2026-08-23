/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentUtils;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class ListPlayersCommand {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 18 */     paramCommandDispatcher.register(
/* 19 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("list")
/* 20 */         .executes(paramCommandContext -> listPlayers((CommandSourceStack)paramCommandContext.getSource())))
/* 21 */         .then(
/* 22 */           Commands.literal("uuids")
/* 23 */           .executes(paramCommandContext -> listPlayersWithUuids((CommandSourceStack)paramCommandContext.getSource()))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int listPlayers(CommandSourceStack paramCommandSourceStack) {
/* 29 */     return format(paramCommandSourceStack, Player::getDisplayName);
/*    */   }
/*    */   
/*    */   private static int listPlayersWithUuids(CommandSourceStack paramCommandSourceStack) {
/* 33 */     return format(paramCommandSourceStack, paramServerPlayer -> Component.translatable("commands.list.nameAndId", new Object[] { paramServerPlayer.getName(), Component.translationArg(paramServerPlayer.getGameProfile().id()) }));
/*    */   }
/*    */   
/*    */   private static int format(CommandSourceStack paramCommandSourceStack, Function<ServerPlayer, Component> paramFunction) {
/* 37 */     PlayerList playerList = paramCommandSourceStack.getServer().getPlayerList();
/* 38 */     List list = playerList.getPlayers();
/* 39 */     Component component = ComponentUtils.formatList(list, paramFunction);
/* 40 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.list.players", new Object[] { Integer.valueOf(paramList.size()), Integer.valueOf(paramPlayerList.getMaxPlayers()), paramComponent }), false);
/* 41 */     return list.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\ListPlayersCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */