/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameModeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameType;

public class DefaultGameModeCommands {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("defaultgamemode").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).then(Commands.argument("gamemode", GameModeArgument.gameMode()).executes(commandContext -> DefaultGameModeCommands.setMode((CommandSourceStack)commandContext.getSource(), GameModeArgument.getGameMode(commandContext, "gamemode")))));
    }

    private static int setMode(CommandSourceStack commandSourceStack, GameType gameType) {
        MinecraftServer minecraftServer = commandSourceStack.getServer();
        minecraftServer.setDefaultGameType(gameType);
        int n = minecraftServer.enforceGameTypeForPlayers(minecraftServer.getForcedGameType());
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.defaultgamemode.success", gameType.getLongDisplayName()), true);
        return n;
    }
}

