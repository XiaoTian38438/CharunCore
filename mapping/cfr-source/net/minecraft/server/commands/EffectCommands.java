/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.server.commands;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.Collection;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

public class EffectCommands {
    private static final SimpleCommandExceptionType ERROR_GIVE_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.effect.give.failed"));
    private static final SimpleCommandExceptionType ERROR_CLEAR_EVERYTHING_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.effect.clear.everything.failed"));
    private static final SimpleCommandExceptionType ERROR_CLEAR_SPECIFIC_FAILED = new SimpleCommandExceptionType(Component.translatable("commands.effect.clear.specific.failed"));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("effect").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).then(((LiteralArgumentBuilder)Commands.literal("clear").executes(commandContext -> EffectCommands.clearEffects((CommandSourceStack)commandContext.getSource(), (Collection<? extends Entity>)ImmutableList.of((Object)((CommandSourceStack)commandContext.getSource()).getEntityOrException())))).then(((RequiredArgumentBuilder)Commands.argument("targets", EntityArgument.entities()).executes(commandContext -> EffectCommands.clearEffects((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets")))).then(Commands.argument("effect", ResourceArgument.resource(commandBuildContext, Registries.MOB_EFFECT)).executes(commandContext -> EffectCommands.clearEffect((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), ResourceArgument.getMobEffect(commandContext, "effect"))))))).then(Commands.literal("give").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("targets", EntityArgument.entities()).then((ArgumentBuilder<CommandSourceStack, ?>)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("effect", ResourceArgument.resource(commandBuildContext, Registries.MOB_EFFECT)).executes(commandContext -> EffectCommands.giveEffect((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), ResourceArgument.getMobEffect(commandContext, "effect"), null, 0, true))).then(((RequiredArgumentBuilder)Commands.argument("seconds", IntegerArgumentType.integer(1, 1000000)).executes(commandContext -> EffectCommands.giveEffect((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), ResourceArgument.getMobEffect(commandContext, "effect"), IntegerArgumentType.getInteger(commandContext, "seconds"), 0, true))).then(((RequiredArgumentBuilder)Commands.argument("amplifier", IntegerArgumentType.integer(0, 255)).executes(commandContext -> EffectCommands.giveEffect((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), ResourceArgument.getMobEffect(commandContext, "effect"), IntegerArgumentType.getInteger(commandContext, "seconds"), IntegerArgumentType.getInteger(commandContext, "amplifier"), true))).then(Commands.argument("hideParticles", BoolArgumentType.bool()).executes(commandContext -> EffectCommands.giveEffect((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), ResourceArgument.getMobEffect(commandContext, "effect"), IntegerArgumentType.getInteger(commandContext, "seconds"), IntegerArgumentType.getInteger(commandContext, "amplifier"), !BoolArgumentType.getBool(commandContext, "hideParticles"))))))).then(((LiteralArgumentBuilder)Commands.literal("infinite").executes(commandContext -> EffectCommands.giveEffect((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), ResourceArgument.getMobEffect(commandContext, "effect"), -1, 0, true))).then(((RequiredArgumentBuilder)Commands.argument("amplifier", IntegerArgumentType.integer(0, 255)).executes(commandContext -> EffectCommands.giveEffect((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), ResourceArgument.getMobEffect(commandContext, "effect"), -1, IntegerArgumentType.getInteger(commandContext, "amplifier"), true))).then(Commands.argument("hideParticles", BoolArgumentType.bool()).executes(commandContext -> EffectCommands.giveEffect((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), ResourceArgument.getMobEffect(commandContext, "effect"), -1, IntegerArgumentType.getInteger(commandContext, "amplifier"), !BoolArgumentType.getBool(commandContext, "hideParticles"))))))))));
    }

    private static int giveEffect(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection, Holder<MobEffect> holder, @Nullable Integer n, int n2, boolean bl) throws CommandSyntaxException {
        MobEffect mobEffect = holder.value();
        int n3 = 0;
        int n4 = n != null ? (mobEffect.isInstantenous() ? n : (n == -1 ? -1 : n * 20)) : (mobEffect.isInstantenous() ? 1 : 600);
        for (Entity entity : collection) {
            MobEffectInstance mobEffectInstance;
            if (!(entity instanceof LivingEntity) || !((LivingEntity)entity).addEffect(mobEffectInstance = new MobEffectInstance(holder, n4, n2, false, bl), commandSourceStack.getEntity())) continue;
            ++n3;
        }
        if (n3 == 0) {
            throw ERROR_GIVE_FAILED.create();
        }
        if (collection.size() == 1) {
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.effect.give.success.single", mobEffect.getDisplayName(), ((Entity)collection.iterator().next()).getDisplayName(), n4 / 20), true);
        } else {
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.effect.give.success.multiple", mobEffect.getDisplayName(), collection.size(), n4 / 20), true);
        }
        return n3;
    }

    private static int clearEffects(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection) throws CommandSyntaxException {
        int n = 0;
        for (Entity entity : collection) {
            if (!(entity instanceof LivingEntity) || !((LivingEntity)entity).removeAllEffects()) continue;
            ++n;
        }
        if (n == 0) {
            throw ERROR_CLEAR_EVERYTHING_FAILED.create();
        }
        if (collection.size() == 1) {
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.effect.clear.everything.success.single", ((Entity)collection.iterator().next()).getDisplayName()), true);
        } else {
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.effect.clear.everything.success.multiple", collection.size()), true);
        }
        return n;
    }

    private static int clearEffect(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection, Holder<MobEffect> holder) throws CommandSyntaxException {
        MobEffect mobEffect = holder.value();
        int n = 0;
        for (Entity entity : collection) {
            if (!(entity instanceof LivingEntity) || !((LivingEntity)entity).removeEffect(holder)) continue;
            ++n;
        }
        if (n == 0) {
            throw ERROR_CLEAR_SPECIFIC_FAILED.create();
        }
        if (collection.size() == 1) {
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.effect.clear.specific.success.single", mobEffect.getDisplayName(), ((Entity)collection.iterator().next()).getDisplayName()), true);
        } else {
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.effect.clear.specific.success.multiple", mobEffect.getDisplayName(), collection.size()), true);
        }
        return n;
    }
}

