/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;

public class ServerPackCommand {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("serverpack").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).then(Commands.literal("push").then((ArgumentBuilder<CommandSourceStack, ?>)((RequiredArgumentBuilder)Commands.argument("url", StringArgumentType.string()).then((ArgumentBuilder<CommandSourceStack, ?>)((RequiredArgumentBuilder)Commands.argument("uuid", UuidArgument.uuid()).then((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("hash", StringArgumentType.word()).executes(commandContext -> ServerPackCommand.pushPack((CommandSourceStack)commandContext.getSource(), StringArgumentType.getString(commandContext, "url"), Optional.of(UuidArgument.getUuid(commandContext, "uuid")), Optional.of(StringArgumentType.getString(commandContext, "hash")))))).executes(commandContext -> ServerPackCommand.pushPack((CommandSourceStack)commandContext.getSource(), StringArgumentType.getString(commandContext, "url"), Optional.of(UuidArgument.getUuid(commandContext, "uuid")), Optional.empty())))).executes(commandContext -> ServerPackCommand.pushPack((CommandSourceStack)commandContext.getSource(), StringArgumentType.getString(commandContext, "url"), Optional.empty(), Optional.empty()))))).then(Commands.literal("pop").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("uuid", UuidArgument.uuid()).executes(commandContext -> ServerPackCommand.popPack((CommandSourceStack)commandContext.getSource(), UuidArgument.getUuid(commandContext, "uuid"))))));
    }

    private static void sendToAllConnections(CommandSourceStack commandSourceStack, Packet<?> packet) {
        commandSourceStack.getServer().getConnection().getConnections().forEach(connection -> connection.send(packet));
    }

    private static int pushPack(CommandSourceStack commandSourceStack, String string, Optional<UUID> optional, Optional<String> optional2) {
        UUID uUID = optional.orElseGet(() -> UUID.nameUUIDFromBytes(string.getBytes(StandardCharsets.UTF_8)));
        String string2 = optional2.orElse("");
        ClientboundResourcePackPushPacket clientboundResourcePackPushPacket = new ClientboundResourcePackPushPacket(uUID, string, string2, false, null);
        ServerPackCommand.sendToAllConnections(commandSourceStack, clientboundResourcePackPushPacket);
        return 0;
    }

    private static int popPack(CommandSourceStack commandSourceStack, UUID uUID) {
        ClientboundResourcePackPopPacket clientboundResourcePackPopPacket = new ClientboundResourcePackPopPacket(Optional.of(uUID));
        ServerPackCommand.sendToAllConnections(commandSourceStack, clientboundResourcePackPopPacket);
        return 0;
    }
}

