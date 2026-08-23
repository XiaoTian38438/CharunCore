/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.Collection;
import java.util.Collections;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.RotationArgument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec2;

public class SetSpawnCommand {
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("spawnpoint").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).executes(commandContext -> SetSpawnCommand.setSpawn((CommandSourceStack)commandContext.getSource(), Collections.singleton(((CommandSourceStack)commandContext.getSource()).getPlayerOrException()), BlockPos.containing(((CommandSourceStack)commandContext.getSource()).getPosition()), WorldCoordinates.ZERO_ROTATION))).then(((RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.players()).executes(commandContext -> SetSpawnCommand.setSpawn((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), BlockPos.containing(((CommandSourceStack)commandContext.getSource()).getPosition()), WorldCoordinates.ZERO_ROTATION))).then(((RequiredArgumentBuilder)Commands.argument("pos", BlockPosArgument.blockPos()).executes(commandContext -> SetSpawnCommand.setSpawn((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), BlockPosArgument.getSpawnablePos(commandContext, "pos"), WorldCoordinates.ZERO_ROTATION))).then(Commands.argument("rotation", RotationArgument.rotation()).executes(commandContext -> SetSpawnCommand.setSpawn((CommandSourceStack)commandContext.getSource(), EntityArgument.getPlayers(commandContext, "targets"), BlockPosArgument.getSpawnablePos(commandContext, "pos"), RotationArgument.getRotation(commandContext, "rotation")))))));
    }

    private static int setSpawn(CommandSourceStack commandSourceStack, Collection<ServerPlayer> collection, BlockPos blockPos, Coordinates coordinates) {
        ResourceKey<Level> resourceKey = commandSourceStack.getLevel().dimension();
        Vec2 vec2 = coordinates.getRotation(commandSourceStack);
        float f = Mth.wrapDegrees(vec2.y);
        float f2 = Mth.clamp(vec2.x, -90.0f, 90.0f);
        for (ServerPlayer serverPlayer : collection) {
            serverPlayer.setRespawnPosition(new ServerPlayer.RespawnConfig(LevelData.RespawnData.of(resourceKey, blockPos, f, f2), true), false);
        }
        String string = resourceKey.identifier().toString();
        if (collection.size() == 1) {
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.spawnpoint.success.single", blockPos.getX(), blockPos.getY(), blockPos.getZ(), Float.valueOf(f), Float.valueOf(f2), string, ((ServerPlayer)collection.iterator().next()).getDisplayName()), true);
        } else {
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.spawnpoint.success.multiple", blockPos.getX(), blockPos.getY(), blockPos.getZ(), Float.valueOf(f), Float.valueOf(f2), string, collection.size()), true);
        }
        return collection.size();
    }
}

