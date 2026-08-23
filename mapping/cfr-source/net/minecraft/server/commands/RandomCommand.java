/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.commands.arguments.RangeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomSequences;
import org.jspecify.annotations.Nullable;

public class RandomCommand {
    private static final SimpleCommandExceptionType ERROR_RANGE_TOO_LARGE = new SimpleCommandExceptionType(Component.translatable("commands.random.error.range_too_large"));
    private static final SimpleCommandExceptionType ERROR_RANGE_TOO_SMALL = new SimpleCommandExceptionType(Component.translatable("commands.random.error.range_too_small"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("random").then(RandomCommand.drawRandomValueTree("value", false))).then(RandomCommand.drawRandomValueTree("roll", true))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("reset").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).then(((LiteralArgumentBuilder)Commands.literal("*").executes(commandContext -> RandomCommand.resetAllSequences((CommandSourceStack)commandContext.getSource()))).then(((RequiredArgumentBuilder)Commands.argument("seed", IntegerArgumentType.integer()).executes(commandContext -> RandomCommand.resetAllSequencesAndSetNewDefaults((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "seed"), true, true))).then(((RequiredArgumentBuilder)Commands.argument("includeWorldSeed", BoolArgumentType.bool()).executes(commandContext -> RandomCommand.resetAllSequencesAndSetNewDefaults((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "seed"), BoolArgumentType.getBool(commandContext, "includeWorldSeed"), true))).then(Commands.argument("includeSequenceId", BoolArgumentType.bool()).executes(commandContext -> RandomCommand.resetAllSequencesAndSetNewDefaults((CommandSourceStack)commandContext.getSource(), IntegerArgumentType.getInteger(commandContext, "seed"), BoolArgumentType.getBool(commandContext, "includeWorldSeed"), BoolArgumentType.getBool(commandContext, "includeSequenceId")))))))).then(((RequiredArgumentBuilder)Commands.argument("sequence", IdentifierArgument.id()).suggests(RandomCommand::suggestRandomSequence).executes(commandContext -> RandomCommand.resetSequence((CommandSourceStack)commandContext.getSource(), IdentifierArgument.getId(commandContext, "sequence")))).then(((RequiredArgumentBuilder)Commands.argument("seed", IntegerArgumentType.integer()).executes(commandContext -> RandomCommand.resetSequence((CommandSourceStack)commandContext.getSource(), IdentifierArgument.getId(commandContext, "sequence"), IntegerArgumentType.getInteger(commandContext, "seed"), true, true))).then(((RequiredArgumentBuilder)Commands.argument("includeWorldSeed", BoolArgumentType.bool()).executes(commandContext -> RandomCommand.resetSequence((CommandSourceStack)commandContext.getSource(), IdentifierArgument.getId(commandContext, "sequence"), IntegerArgumentType.getInteger(commandContext, "seed"), BoolArgumentType.getBool(commandContext, "includeWorldSeed"), true))).then(Commands.argument("includeSequenceId", BoolArgumentType.bool()).executes(commandContext -> RandomCommand.resetSequence((CommandSourceStack)commandContext.getSource(), IdentifierArgument.getId(commandContext, "sequence"), IntegerArgumentType.getInteger(commandContext, "seed"), BoolArgumentType.getBool(commandContext, "includeWorldSeed"), BoolArgumentType.getBool(commandContext, "includeSequenceId")))))))));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> drawRandomValueTree(String string, boolean bl) {
        return (LiteralArgumentBuilder)Commands.literal(string).then((ArgumentBuilder<CommandSourceStack, ?>)((RequiredArgumentBuilder)Commands.argument("range", RangeArgument.intRange()).executes(commandContext -> RandomCommand.randomSample((CommandSourceStack)commandContext.getSource(), RangeArgument.Ints.getRange(commandContext, "range"), null, bl))).then(((RequiredArgumentBuilder)Commands.argument("sequence", IdentifierArgument.id()).suggests(RandomCommand::suggestRandomSequence).requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).executes(commandContext -> RandomCommand.randomSample((CommandSourceStack)commandContext.getSource(), RangeArgument.Ints.getRange(commandContext, "range"), IdentifierArgument.getId(commandContext, "sequence"), bl))));
    }

    private static CompletableFuture<Suggestions> suggestRandomSequence(CommandContext<CommandSourceStack> commandContext, SuggestionsBuilder suggestionsBuilder) {
        ArrayList arrayList = Lists.newArrayList();
        commandContext.getSource().getLevel().getRandomSequences().forAllSequences((identifier, randomSequence) -> arrayList.add(identifier.toString()));
        return SharedSuggestionProvider.suggest(arrayList, suggestionsBuilder);
    }

    private static int randomSample(CommandSourceStack commandSourceStack, MinMaxBounds.Ints ints, @Nullable Identifier identifier, boolean bl) throws CommandSyntaxException {
        RandomSource randomSource = identifier != null ? commandSourceStack.getLevel().getRandomSequence(identifier) : commandSourceStack.getLevel().getRandom();
        int n = ints.min().orElse(Integer.MIN_VALUE);
        int n2 = ints.max().orElse(Integer.MAX_VALUE);
        long l = (long)n2 - (long)n;
        if (l == 0L) {
            throw ERROR_RANGE_TOO_SMALL.create();
        }
        if (l >= Integer.MAX_VALUE) {
            throw ERROR_RANGE_TOO_LARGE.create();
        }
        int n3 = Mth.randomBetweenInclusive(randomSource, n, n2);
        if (bl) {
            commandSourceStack.getServer().getPlayerList().broadcastSystemMessage(Component.translatable("commands.random.roll", commandSourceStack.getDisplayName(), n3, n, n2), false);
        } else {
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.random.sample.success", n3), false);
        }
        return n3;
    }

    private static int resetSequence(CommandSourceStack commandSourceStack, Identifier identifier) throws CommandSyntaxException {
        ServerLevel serverLevel = commandSourceStack.getLevel();
        serverLevel.getRandomSequences().reset(identifier, serverLevel.getSeed());
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.random.reset.success", Component.translationArg(identifier)), false);
        return 1;
    }

    private static int resetSequence(CommandSourceStack commandSourceStack, Identifier identifier, int n, boolean bl, boolean bl2) throws CommandSyntaxException {
        ServerLevel serverLevel = commandSourceStack.getLevel();
        serverLevel.getRandomSequences().reset(identifier, serverLevel.getSeed(), n, bl, bl2);
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.random.reset.success", Component.translationArg(identifier)), false);
        return 1;
    }

    private static int resetAllSequences(CommandSourceStack commandSourceStack) {
        int n = commandSourceStack.getLevel().getRandomSequences().clear();
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.random.reset.all.success", n), false);
        return n;
    }

    private static int resetAllSequencesAndSetNewDefaults(CommandSourceStack commandSourceStack, int n, boolean bl, boolean bl2) {
        RandomSequences randomSequences = commandSourceStack.getLevel().getRandomSequences();
        randomSequences.setSeedDefaults(n, bl, bl2);
        int n2 = randomSequences.clear();
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.random.reset.all.success", n2), false);
        return n2;
    }
}

