/*    */ package net.minecraft.server.commands;
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.players.BanListEntry;
/*    */ import net.minecraft.server.players.PlayerList;
/*    */ 
/*    */ public class BanListCommands {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 18 */     paramCommandDispatcher.register(
/* 19 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("banlist")
/* 20 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 21 */         .executes(paramCommandContext -> {
/*    */             PlayerList playerList = ((CommandSourceStack)paramCommandContext.getSource()).getServer().getPlayerList();
/*    */             
/*    */             return showList((CommandSourceStack)paramCommandContext.getSource(), Lists.newArrayList(Iterables.concat(playerList.getBans().getEntries(), playerList.getIpBans().getEntries())));
/* 25 */           })).then(
/* 26 */           Commands.literal("ips")
/* 27 */           .executes(paramCommandContext -> showList((CommandSourceStack)paramCommandContext.getSource(), ((CommandSourceStack)paramCommandContext.getSource()).getServer().getPlayerList().getIpBans().getEntries()))))
/*    */         
/* 29 */         .then(
/* 30 */           Commands.literal("players")
/* 31 */           .executes(paramCommandContext -> showList((CommandSourceStack)paramCommandContext.getSource(), ((CommandSourceStack)paramCommandContext.getSource()).getServer().getPlayerList().getBans().getEntries()))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int showList(CommandSourceStack paramCommandSourceStack, Collection<? extends BanListEntry<?>> paramCollection) {
/* 37 */     if (paramCollection.isEmpty()) {
/* 38 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.banlist.none"), false);
/*    */     } else {
/* 40 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.banlist.list", new Object[] { Integer.valueOf(paramCollection.size()) }), false);
/* 41 */       for (BanListEntry<?> banListEntry : paramCollection) {
/* 42 */         paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.banlist.entry", new Object[] { paramBanListEntry.getDisplayName(), paramBanListEntry.getSource(), paramBanListEntry.getReasonMessage() }), false);
/*    */       } 
/*    */     } 
/* 45 */     return paramCollection.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\BanListCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */