/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class TellRawCommand {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("tellraw").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).then(Commands.argument("targets", EntityArgument.players()).then((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("message", ComponentArgument.textComponent(commandBuildContext)).executes(commandContext -> {
            int n = 0;
            for (ServerPlayer serverPlayer : EntityArgument.getPlayers(commandContext, "targets")) {
                serverPlayer.sendSystemMessage(ComponentArgument.getResolvedComponent(commandContext, "message", serverPlayer), false);
                ++n;
            }
            return n;
        }))));
    }
}

