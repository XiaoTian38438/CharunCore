/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Locale;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.commands.arguments.coordinates.Vec2Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.Vec2;

public class WorldBorderCommand {
    private static final SimpleCommandExceptionType ERROR_SAME_CENTER = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.center.failed"));
    private static final SimpleCommandExceptionType ERROR_SAME_SIZE = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.set.failed.nochange"));
    private static final SimpleCommandExceptionType ERROR_TOO_SMALL = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.set.failed.small"));
    private static final SimpleCommandExceptionType ERROR_TOO_BIG = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.set.failed.big", 5.9999968E7));
    private static final SimpleCommandExceptionType ERROR_TOO_FAR_OUT = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.set.failed.far", 2.9999984E7));
    private static final SimpleCommandExceptionType ERROR_SAME_WARNING_TIME = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.warning.time.failed"));
    private static final SimpleCommandExceptionType ERROR_SAME_WARNING_DISTANCE = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.warning.distance.failed"));
    private static final SimpleCommandExceptionType ERROR_SAME_DAMAGE_BUFFER = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.damage.buffer.failed"));
    private static final SimpleCommandExceptionType ERROR_SAME_DAMAGE_AMOUNT = new SimpleCommandExceptionType(Component.translatable("commands.worldborder.damage.amount.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("worldborder").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).then(Commands.literal("add").then((ArgumentBuilder<CommandSourceStack, ?>)((RequiredArgumentBuilder)Commands.argument("distance", DoubleArgumentType.doubleArg(-5.9999968E7, 5.9999968E7)).executes(commandContext -> WorldBorderCommand.setSize((CommandSourceStack)commandContext.getSource(), ((CommandSourceStack)commandContext.getSource()).getLevel().getWorldBorder().getSize() + DoubleArgumentType.getDouble(commandContext, "distance"), 0L))).then(Commands.argument("time", TimeArgument.time(0)).executes(commandContext -> WorldBorderCommand.setSize((CommandSourceStack)commandContext.getSource(), ((CommandSourceStack)commandContext.getSource()).getLevel().getWorldBorder().getSize() + DoubleArgumentType.getDouble(commandContext, "distance"), ((CommandSourceStack)commandContext.getSource()).getLevel().getWorldBorder().getLerpTime() + (long)IntegerArgumentType.getInteger(commandContext, "time"))))))).then(Commands.literal("set").then((ArgumentBuilder<CommandSourceStack, ?>)((RequiredArgumentBuilder)Commands.argument("distance", DoubleArgumentType.doubleArg(-5.9999968E7, 5.9999968E7)).executes(commandContext -> WorldBorderCommand.setSize((CommandSourceStack)commandContext.getSource(), DoubleArgumentType.getDouble(commandContext, "distance"), 0L))).then(Commands.argument("time", TimeArgument.time(0)).executes(commandContext -> WorldBorderCommand.setSize((CommandSourceStack)commandContext.getSource(), DoubleArgumentType.getDouble(commandContext, "distance"), IntegerArgumentType.getInteger(commandContext, "time"))))))).then(Commands.literal("center").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("pos", Vec2Argument.vec2()).executes(commandContext -> WorldBorderCommand.setCenter((CommandSourceStack)commandContext.getSource(), Vec2Argument.getVec2(commandContext, "pos")))))).then(((LiteralArgumentBuilder)Commands.literal("damage").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("amount").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("damagePerBlock", FloatArgumentType.floatArg(0.0f)).executes(commandContext -> WorldBorderCommand.setDamageAmount((CommandSourceStack)commandContext.getSource(), FloatArgumentType.getFloat(commandContext, "damagePerBlock")))))).then(Commands.literal("buffer").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("distance", FloatArgumentType.floatArg(0.0f)).executes(commandContext -> WorldBorderCommand.setDamageBuffer((CommandSourceStack)commandContext.getSource(), FloatArgumentType.getFloat(commandContext, "distance"))))))).then(Commands.literal("get").executes(commandContext -> WorldBorderCommand.getSize((CommandSourceStack)commandContext.getSource())))).then(((LiteralArgumentBuilder)Commands.literal("warning").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("distance").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("distance", IntegerArgumentType.integer(0)).executes(commandContext -> WorldBorderCommand.setWarningDistance((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "distance")))))).then(Commands.literal("time").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("time", TimeArgument.time(0)).executes(commandContext -> WorldBorderCommand.setWarningTime((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "time")))))));
    }

    private static int setDamageBuffer(CommandSourceStack commandSourceStack, float f) throws CommandSyntaxException {
        WorldBorder worldBorder = commandSourceStack.getLevel().getWorldBorder();
        if (worldBorder.getSafeZone() == (double)f) {
            throw ERROR_SAME_DAMAGE_BUFFER.create();
        }
        worldBorder.setSafeZone(f);
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.damage.buffer.success", String.format(Locale.ROOT, "%.2f", Float.valueOf(f))), true);
        return (int)f;
    }

    private static int setDamageAmount(CommandSourceStack commandSourceStack, float f) throws CommandSyntaxException {
        WorldBorder worldBorder = commandSourceStack.getLevel().getWorldBorder();
        if (worldBorder.getDamagePerBlock() == (double)f) {
            throw ERROR_SAME_DAMAGE_AMOUNT.create();
        }
        worldBorder.setDamagePerBlock(f);
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.damage.amount.success", String.format(Locale.ROOT, "%.2f", Float.valueOf(f))), true);
        return (int)f;
    }

    private static int setWarningTime(CommandSourceStack commandSourceStack, int n) throws CommandSyntaxException {
        WorldBorder worldBorder = commandSourceStack.getLevel().getWorldBorder();
        if (worldBorder.getWarningTime() == n) {
            throw ERROR_SAME_WARNING_TIME.create();
        }
        worldBorder.setWarningTime(n);
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.warning.time.success", WorldBorderCommand.formatTicksToSeconds(n)), true);
        return n;
    }

    private static int setWarningDistance(CommandSourceStack commandSourceStack, int n) throws CommandSyntaxException {
        WorldBorder worldBorder = commandSourceStack.getLevel().getWorldBorder();
        if (worldBorder.getWarningBlocks() == n) {
            throw ERROR_SAME_WARNING_DISTANCE.create();
        }
        worldBorder.setWarningBlocks(n);
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.warning.distance.success", n), true);
        return n;
    }

    private static int getSize(CommandSourceStack commandSourceStack) {
        double d = commandSourceStack.getLevel().getWorldBorder().getSize();
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.get", String.format(Locale.ROOT, "%.0f", d)), false);
        return Mth.floor(d + 0.5);
    }

    private static int setCenter(CommandSourceStack commandSourceStack, Vec2 vec2) throws CommandSyntaxException {
        WorldBorder worldBorder = commandSourceStack.getLevel().getWorldBorder();
        if (worldBorder.getCenterX() == (double)vec2.x && worldBorder.getCenterZ() == (double)vec2.y) {
            throw ERROR_SAME_CENTER.create();
        }
        if ((double)Math.abs(vec2.x) > 2.9999984E7 || (double)Math.abs(vec2.y) > 2.9999984E7) {
            throw ERROR_TOO_FAR_OUT.create();
        }
        worldBorder.setCenter(vec2.x, vec2.y);
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.center.success", String.format(Locale.ROOT, "%.2f", Float.valueOf(vec2.x)), String.format(Locale.ROOT, "%.2f", Float.valueOf(vec2.y))), true);
        return 0;
    }

    private static int setSize(CommandSourceStack commandSourceStack, double d, long l) throws CommandSyntaxException {
        ServerLevel serverLevel = commandSourceStack.getLevel();
        WorldBorder worldBorder = serverLevel.getWorldBorder();
        double d2 = worldBorder.getSize();
        if (d2 == d) {
            throw ERROR_SAME_SIZE.create();
        }
        if (d < 1.0) {
            throw ERROR_TOO_SMALL.create();
        }
        if (d > 5.9999968E7) {
            throw ERROR_TOO_BIG.create();
        }
        String string = String.format(Locale.ROOT, "%.1f", d);
        if (l > 0L) {
            worldBorder.lerpSizeBetween(d2, d, l, serverLevel.getGameTime());
            if (d > d2) {
                commandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.set.grow", string, WorldBorderCommand.formatTicksToSeconds(l)), true);
            } else {
                commandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.set.shrink", string, WorldBorderCommand.formatTicksToSeconds(l)), true);
            }
        } else {
            worldBorder.setSize(d);
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.worldborder.set.immediate", string), true);
        }
        return (int)(d - d2);
    }

    private static String formatTicksToSeconds(long l) {
        return String.format(Locale.ROOT, "%.2f", (double)l / 20.0);
    }
}

