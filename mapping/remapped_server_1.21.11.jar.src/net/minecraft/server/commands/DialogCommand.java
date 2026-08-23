/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandBuildContext;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.ResourceOrIdArgument;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.common.ClientboundClearDialogPacket;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class DialogCommand
/*    */ {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher, CommandBuildContext paramCommandBuildContext) {
/* 25 */     paramCommandDispatcher.register(
/* 26 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("dialog")
/* 27 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 28 */         .then(
/* 29 */           Commands.literal("show")
/* 30 */           .then(
/* 31 */             Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 32 */             .then(
/* 33 */               Commands.argument("dialog", (ArgumentType)ResourceOrIdArgument.dialog(paramCommandBuildContext))
/* 34 */               .executes(paramCommandContext -> showDialog((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), ResourceOrIdArgument.getDialog(paramCommandContext, "dialog")))))))
/*    */ 
/*    */ 
/*    */         
/* 38 */         .then(
/* 39 */           Commands.literal("clear")
/* 40 */           .then(
/* 41 */             Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 42 */             .executes(paramCommandContext -> clearDialog((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int showDialog(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection, Holder<Dialog> paramHolder) {
/* 49 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 50 */       serverPlayer.openDialog(paramHolder);
/*    */     }
/*    */     
/* 53 */     if (paramCollection.size() == 1) {
/* 54 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.dialog.show.single", new Object[] { ((ServerPlayer)paramCollection.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 56 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.dialog.show.multiple", new Object[] { Integer.valueOf(paramCollection.size()) }), true);
/*    */     } 
/* 58 */     return paramCollection.size();
/*    */   }
/*    */   
/*    */   private static int clearDialog(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection) {
/* 62 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 63 */       serverPlayer.connection.send((Packet)ClientboundClearDialogPacket.INSTANCE);
/*    */     }
/*    */     
/* 66 */     if (paramCollection.size() == 1) {
/* 67 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.dialog.clear.single", new Object[] { ((ServerPlayer)paramCollection.iterator().next()).getDisplayName() }), true);
/*    */     } else {
/* 69 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.dialog.clear.multiple", new Object[] { Integer.valueOf(paramCollection.size()) }), true);
/*    */     } 
/* 71 */     return paramCollection.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\DialogCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */