/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.UserBanList;

public class PardonCommand {
    private static final SimpleCommandExceptionType ERROR_NOT_BANNED = new SimpleCommandExceptionType(Component.translatable("commands.pardon.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("pardon").requires(Commands.hasPermission(Commands.LEVEL_ADMINS))).then(Commands.argument("targets", GameProfileArgument.gameProfile()).suggests((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggest(((CommandSourceStack)commandContext.getSource()).getServer().getPlayerList().getBans().getUserList(), suggestionsBuilder)).executes(commandContext -> PardonCommand.pardonPlayers((CommandSourceStack)commandContext.getSource(), GameProfileArgument.getGameProfiles(commandContext, "targets")))));
    }

    private static int pardonPlayers(CommandSourceStack commandSourceStack, Collection<NameAndId> collection) throws CommandSyntaxException {
        UserBanList userBanList = commandSourceStack.getServer().getPlayerList().getBans();
        int n = 0;
        for (NameAndId nameAndId : collection) {
            if (!userBanList.isBanned(nameAndId)) continue;
            userBanList.remove(nameAndId);
            ++n;
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.pardon.success", Component.literal(nameAndId.name())), true);
        }
        if (n == 0) {
            throw ERROR_NOT_BANNED.create();
        }
        return n;
    }
}

