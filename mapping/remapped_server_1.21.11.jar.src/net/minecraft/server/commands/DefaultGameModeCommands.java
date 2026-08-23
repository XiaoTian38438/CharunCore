/*    */ package net.minecraft.server.commands;
/*    */ import com.mojang.brigadier.CommandDispatcher;
/*    */ import com.mojang.brigadier.arguments.ArgumentType;
/*    */ import com.mojang.brigadier.builder.LiteralArgumentBuilder;
/*    */ import com.mojang.brigadier.context.CommandContext;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.commands.Commands;
/*    */ import net.minecraft.commands.arguments.GameModeArgument;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.world.level.GameType;
/*    */ 
/*    */ public class DefaultGameModeCommands {
/*    */   public static void register(CommandDispatcher<CommandSourceStack> paramCommandDispatcher) {
/* 17 */     paramCommandDispatcher.register(
/* 18 */         (LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("defaultgamemode")
/* 19 */         .requires((Predicate)Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)))
/* 20 */         .then(
/* 21 */           Commands.argument("gamemode", (ArgumentType)GameModeArgument.gameMode())
/* 22 */           .executes(paramCommandContext -> setMode((CommandSourceStack)paramCommandContext.getSource(), GameModeArgument.getGameMode(paramCommandContext, "gamemode")))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static int setMode(CommandSourceStack paramCommandSourceStack, GameType paramGameType) {
/* 28 */     MinecraftServer minecraftServer = paramCommandSourceStack.getServer();
/* 29 */     minecraftServer.setDefaultGameType(paramGameType);
/* 30 */     int i = minecraftServer.enforceGameTypeForPlayers(minecraftServer.getForcedGameType());
/* 31 */     paramCommandSourceStack.sendSuccess(() -> Component.translatable("commands.defaultgamemode.success", new Object[] { paramGameType.getLongDisplayName() }), true);
/* 32 */     return i;
/*    */   }
/*    */ }


/* Location:              C:\Users\tian_\Desktop\mc-server-core1.21.11\mapping\deobf-work\remapped_server_1.21.11.jar!\net\minecraft\server\commands\DefaultGameModeCommands.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */