/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.builder.RequiredArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.EntityArgument;
/*    */ import net.minecraft.commands.arguments.GameModeArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.server.permissions.PermissionCheck;
/*    */ import net.minecraft.server.permissions.Permissions;
/*    */ import net.minecraft.world.level.GameType;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ public class GameModeCommand {
/* 25 */   public static final PermissionCheck PERMISSION_CHECK = (PermissionCheck)new PermissionCheck.Require(Permissions.COMMANDS_GAMEMASTER);
/*    */   
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 28 */     paramCommandDispatcher.register(
/* 29 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("gamemode")
/* 30 */         .requires((Predicate)Commands.hasPermission(PERMISSION_CHECK)))
/* 31 */         .then((
/* 32 */           (RequiredArgumentBuilder)Commands.argument("gamemode", (ArgumentType)GameModeArgument.gameMode())
/* 33 */           .executes(paramCommandContext -> setMode(paramCommandContext, Collections.singleton(((CommandSourceStack)paramCommandContext.getSource()).getPlayerOrException()), GameModeArgument.getGameMode(paramCommandContext, "gamemode"))))
/* 34 */           .then(
/* 35 */             Commands.argument("target", (ArgumentType)EntityArgument.players())
/* 36 */             .executes(paramCommandContext -> setMode(paramCommandContext, EntityArgument.getPlayers(paramCommandContext, "target"), GameModeArgument.getGameMode(paramCommandContext, "gamemode"))))));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static void logGamemodeChange(CommandSourceStack paramCommandSourceStack, ServerPlayer paramServerPlayer, GameType paramGameType) {
/* 43 */     MutableComponent mutableComponent = Component.translatable("gameMode." + paramGameType.getName());
/* 44 */     if (paramCommandSourceStack.getEntity() == paramServerPlayer) {
/* 45 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.gamemode.success.self", new Object[] { paramComponent }), true);
/*    */     } else {
/* 47 */       if (((Boolean)paramCommandSourceStack.getLevel().getGameRules().get(GameRules.SEND_COMMAND_FEEDBACK)).booleanValue()) {
/* 48 */         paramServerPlayer.sendSystemMessage((Component)Component.translatable("gameMode.changed", new Object[] { mutableComponent }));
/*    */       }
/*    */       
/* 51 */       paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.gamemode.success.other", new Object[] { paramServerPlayer.getDisplayName(), paramComponent }), true);
/*    */     } 
/*    */   }
/*    */   
/*    */   private static int setMode(CommandContext<CommandSourceStack> paramCommandContext, Collection<ServerPlayer> paramCollection, GameType paramGameType) {
/* 56 */     byte b = 0;
/* 57 */     for (ServerPlayer serverPlayer : paramCollection) {
/* 58 */       if (setGameMode((CommandSourceStack)paramCommandContext.getSource(), serverPlayer, paramGameType)) {
/* 59 */         b++;
/*    */       }
/*    */     } 
/* 62 */     return b;
/*    */   }
/*    */   
/*    */   public static void setGameMode(ServerPlayer paramServerPlayer, GameType paramGameType) {
/* 66 */     setGameMode(paramServerPlayer.createCommandSourceStack(), paramServerPlayer, paramGameType);
/*    */   }
/*    */   
/*    */   private static boolean setGameMode(CommandSourceStack paramCommandSourceStack, ServerPlayer paramServerPlayer, GameType paramGameType) {
/* 70 */     if (paramServerPlayer.setGameMode(paramGameType)) {
/* 71 */       logGamemodeChange(paramCommandSourceStack, paramServerPlayer, paramGameType);
/* 72 */       return true;
/*    */     } 
/* 74 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\GameModeCommand.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */