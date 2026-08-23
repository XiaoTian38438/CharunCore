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
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.MessageArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ 
/*    */ public class KickCommand {
/* 21 */   private static final SimpleCommandExceptionType ERROR_KICKING_OWNER = new SimpleCommandExceptionType((Message)Component.translatable("commands.kick.owner.failed"));
/* 22 */   private static final SimpleCommandExceptionType ERROR_SINGLEPLAYER = new SimpleCommandExceptionType((Message)Component.translatable("commands.kick.singleplayer.failed"));
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 25 */     paramCommandDispatcher.register(
/* 26 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("kick")
/* 27 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_ADMINS)))
/* 28 */         .then((
/* 29 */           (RequiredArgumentBuilder)Commands.argument("targets", (ArgumentType)EntityArgument.players())
/* 30 */           .executes(paramCommandContext -> kickPlayers((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), (Component)Component.translatable("multiplayer.disconnect.kicked"))))
/* 31 */           .then(
/* 32 */             Commands.argument("reason", (ArgumentType)MessageArgument.message())
/* 33 */             .executes(paramCommandContext -> kickPlayers((CommandSourceStack)paramCommandContext.getSource(), EntityArgument.getPlayers(paramCommandContext, "targets"), MessageArgument.getMessage(paramCommandContext, "reason"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int kickPlayers(CommandSourceStack paramCommandSourceStack, Collection<ServerPlayer> paramCollection, Component paramComponent) throws CommandSyntaxException {
/* 40 */     if (!paramCommandSourceStack.getServer().isPublished()) {
/* 41 */       throw ERROR_SINGLEPLAYER.create();
/*    */     }
/*    */     
/* 44 */     byte b = 0;
/* 45 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 46 */       if (paramCommandSourceStack.getServer().isSingleplayerOwner(serverPlayer.nameAndId())) {
/*    */         continue;
/*    */       }
/* 49 */       serverPlayer.connection.disconnect(paramComponent);
/* 50 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.kick.success", new Object[] { paramServerPlayer.getDisplayName(), paramComponent }), true);
/* 51 */       b++;
/*    */     } 
/*    */     
/* 54 */     if (b == 0) {
/* 55 */       throw ERROR_KICKING_OWNER.create();
/*    */     }
/*    */     
/* 58 */     return b;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\KickCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */