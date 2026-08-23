/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.stream.Stream;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class AttributeCommand {
    private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(object -> Component.translatableEscape("commands.attribute.failed.entity", object));
    private static final Dynamic2CommandExceptionType ERROR_NO_SUCH_ATTRIBUTE = new Dynamic2CommandExceptionType((object, object2) -> Component.translatableEscape("commands.attribute.failed.no_attribute", object, object2));
    private static final Dynamic3CommandExceptionType ERROR_NO_SUCH_MODIFIER = new Dynamic3CommandExceptionType((object, object2, object3) -> Component.translatableEscape("commands.attribute.failed.no_modifier", object2, object, object3));
    private static final Dynamic3CommandExceptionType ERROR_MODIFIER_ALREADY_PRESENT = new Dynamic3CommandExceptionType((object, object2, object3) -> Component.translatableEscape("commands.attribute.failed.modifier_already_present", object3, object2, object));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext) {
        commandDispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("attribute").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))).then(Commands.argument("target", EntityArgument.entity()).then((ArgumentBuilder<CommandSourceStack, ?>)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("attribute", ResourceArgument.resource(commandBuildContext, Registries.ATTRIBUTE)).then((ArgumentBuilder<CommandSourceStack, ?>)((LiteralArgumentBuilder)Commands.literal("get").executes(commandContext -> AttributeCommand.getAttributeValue((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), ResourceArgument.getAttribute(commandContext, "attribute"), 1.0))).then(Commands.argument("scale", DoubleArgumentType.doubleArg()).executes(commandContext -> AttributeCommand.getAttributeValue((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), ResourceArgument.getAttribute(commandContext, "attribute"), DoubleArgumentType.getDouble(commandContext, "scale")))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("base").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("set").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("value", DoubleArgumentType.doubleArg()).executes(commandContext -> AttributeCommand.setAttributeBase((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), ResourceArgument.getAttribute(commandContext, "attribute"), DoubleArgumentType.getDouble(commandContext, "value")))))).then(((LiteralArgumentBuilder)Commands.literal("get").executes(commandContext -> AttributeCommand.getAttributeBase((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), ResourceArgument.getAttribute(commandContext, "attribute"), 1.0))).then(Commands.argument("scale", DoubleArgumentType.doubleArg()).executes(commandContext -> AttributeCommand.getAttributeBase((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), ResourceArgument.getAttribute(commandContext, "attribute"), DoubleArgumentType.getDouble(commandContext, "scale")))))).then(Commands.literal("reset").executes(commandContext -> AttributeCommand.resetAttributeBase((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), ResourceArgument.getAttribute(commandContext, "attribute")))))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.literal("modifier").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("add").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("id", IdentifierArgument.id()).then((ArgumentBuilder<CommandSourceStack, ?>)((RequiredArgumentBuilder)((RequiredArgumentBuilder)Commands.argument("value", DoubleArgumentType.doubleArg()).then((ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("add_value").executes(commandContext -> AttributeCommand.addModifier((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), ResourceArgument.getAttribute(commandContext, "attribute"), IdentifierArgument.getId(commandContext, "id"), DoubleArgumentType.getDouble(commandContext, "value"), AttributeModifier.Operation.ADD_VALUE)))).then(Commands.literal("add_multiplied_base").executes(commandContext -> AttributeCommand.addModifier((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), ResourceArgument.getAttribute(commandContext, "attribute"), IdentifierArgument.getId(commandContext, "id"), DoubleArgumentType.getDouble(commandContext, "value"), AttributeModifier.Operation.ADD_MULTIPLIED_BASE)))).then(Commands.literal("add_multiplied_total").executes(commandContext -> AttributeCommand.addModifier((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), ResourceArgument.getAttribute(commandContext, "attribute"), IdentifierArgument.getId(commandContext, "id"), DoubleArgumentType.getDouble(commandContext, "value"), AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL))))))).then(Commands.literal("remove").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.argument("id", IdentifierArgument.id()).suggests((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggestResource(AttributeCommand.getAttributeModifiers(EntityArgument.getEntity(commandContext, "target"), ResourceArgument.getAttribute(commandContext, "attribute")), suggestionsBuilder)).executes(commandContext -> AttributeCommand.removeModifier((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), ResourceArgument.getAttribute(commandContext, "attribute"), IdentifierArgument.getId(commandContext, "id")))))).then(Commands.literal("value").then((ArgumentBuilder<CommandSourceStack, ?>)Commands.literal("get").then((ArgumentBuilder<CommandSourceStack, ?>)((RequiredArgumentBuilder)Commands.argument("id", IdentifierArgument.id()).suggests((commandContext, suggestionsBuilder) -> SharedSuggestionProvider.suggestResource(AttributeCommand.getAttributeModifiers(EntityArgument.getEntity(commandContext, "target"), ResourceArgument.getAttribute(commandContext, "attribute")), suggestionsBuilder)).executes(commandContext -> AttributeCommand.getAttributeModifier((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), ResourceArgument.getAttribute(commandContext, "attribute"), IdentifierArgument.getId(commandContext, "id"), 1.0))).then(Commands.argument("scale", DoubleArgumentType.doubleArg()).executes(commandContext -> AttributeCommand.getAttributeModifier((CommandSourceStack)commandContext.getSource(), EntityArgument.getEntity(commandContext, "target"), ResourceArgument.getAttribute(commandContext, "attribute"), IdentifierArgument.getId(commandContext, "id"), DoubleArgumentType.getDouble(commandContext, "scale")))))))))));
    }

    private static AttributeInstance getAttributeInstance(Entity entity, Holder<Attribute> holder) throws CommandSyntaxException {
        AttributeInstance attributeInstance = AttributeCommand.getLivingEntity(entity).getAttributes().getInstance(holder);
        if (attributeInstance == null) {
            throw ERROR_NO_SUCH_ATTRIBUTE.create(entity.getName(), AttributeCommand.getAttributeDescription(holder));
        }
        return attributeInstance;
    }

    private static LivingEntity getLivingEntity(Entity entity) throws CommandSyntaxException {
        if (!(entity instanceof LivingEntity)) {
            throw ERROR_NOT_LIVING_ENTITY.create(entity.getName());
        }
        return (LivingEntity)entity;
    }

    private static LivingEntity getEntityWithAttribute(Entity entity, Holder<Attribute> holder) throws CommandSyntaxException {
        LivingEntity livingEntity = AttributeCommand.getLivingEntity(entity);
        if (!livingEntity.getAttributes().hasAttribute(holder)) {
            throw ERROR_NO_SUCH_ATTRIBUTE.create(entity.getName(), AttributeCommand.getAttributeDescription(holder));
        }
        return livingEntity;
    }

    private static int getAttributeValue(CommandSourceStack commandSourceStack, Entity entity, Holder<Attribute> holder, double d) throws CommandSyntaxException {
        LivingEntity livingEntity = AttributeCommand.getEntityWithAttribute(entity, holder);
        double d2 = livingEntity.getAttributeValue(holder);
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.attribute.value.get.success", AttributeCommand.getAttributeDescription(holder), entity.getName(), d2), false);
        return (int)(d2 * d);
    }

    private static int getAttributeBase(CommandSourceStack commandSourceStack, Entity entity, Holder<Attribute> holder, double d) throws CommandSyntaxException {
        LivingEntity livingEntity = AttributeCommand.getEntityWithAttribute(entity, holder);
        double d2 = livingEntity.getAttributeBaseValue(holder);
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.attribute.base_value.get.success", AttributeCommand.getAttributeDescription(holder), entity.getName(), d2), false);
        return (int)(d2 * d);
    }

    private static int getAttributeModifier(CommandSourceStack commandSourceStack, Entity entity, Holder<Attribute> holder, Identifier identifier, double d) throws CommandSyntaxException {
        LivingEntity livingEntity = AttributeCommand.getEntityWithAttribute(entity, holder);
        AttributeMap attributeMap = livingEntity.getAttributes();
        if (!attributeMap.hasModifier(holder, identifier)) {
            throw ERROR_NO_SUCH_MODIFIER.create(entity.getName(), AttributeCommand.getAttributeDescription(holder), identifier);
        }
        double d2 = attributeMap.getModifierValue(holder, identifier);
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.attribute.modifier.value.get.success", Component.translationArg(identifier), AttributeCommand.getAttributeDescription(holder), entity.getName(), d2), false);
        return (int)(d2 * d);
    }

    private static Stream<Identifier> getAttributeModifiers(Entity entity, Holder<Attribute> holder) throws CommandSyntaxException {
        AttributeInstance attributeInstance = AttributeCommand.getAttributeInstance(entity, holder);
        return attributeInstance.getModifiers().stream().map(AttributeModifier::id);
    }

    private static int setAttributeBase(CommandSourceStack commandSourceStack, Entity entity, Holder<Attribute> holder, double d) throws CommandSyntaxException {
        AttributeCommand.getAttributeInstance(entity, holder).setBaseValue(d);
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.attribute.base_value.set.success", AttributeCommand.getAttributeDescription(holder), entity.getName(), d), false);
        return 1;
    }

    private static int resetAttributeBase(CommandSourceStack commandSourceStack, Entity entity, Holder<Attribute> holder) throws CommandSyntaxException {
        LivingEntity livingEntity = AttributeCommand.getLivingEntity(entity);
        if (!livingEntity.getAttributes().resetBaseValue(holder)) {
            throw ERROR_NO_SUCH_ATTRIBUTE.create(entity.getName(), AttributeCommand.getAttributeDescription(holder));
        }
        double d = livingEntity.getAttributeBaseValue(holder);
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.attribute.base_value.reset.success", AttributeCommand.getAttributeDescription(holder), entity.getName(), d), false);
        return 1;
    }

    private static int addModifier(CommandSourceStack commandSourceStack, Entity entity, Holder<Attribute> holder, Identifier identifier, double d, AttributeModifier.Operation operation) throws CommandSyntaxException {
        AttributeInstance attributeInstance = AttributeCommand.getAttributeInstance(entity, holder);
        AttributeModifier attributeModifier = new AttributeModifier(identifier, d, operation);
        if (attributeInstance.hasModifier(identifier)) {
            throw ERROR_MODIFIER_ALREADY_PRESENT.create(entity.getName(), AttributeCommand.getAttributeDescription(holder), identifier);
        }
        attributeInstance.addPermanentModifier(attributeModifier);
        commandSourceStack.sendSuccess(() -> Component.translatable("commands.attribute.modifier.add.success", Component.translationArg(identifier), AttributeCommand.getAttributeDescription(holder), entity.getName()), false);
        return 1;
    }

    private static int removeModifier(CommandSourceStack commandSourceStack, Entity entity, Holder<Attribute> holder, Identifier identifier) throws CommandSyntaxException {
        AttributeInstance attributeInstance = AttributeCommand.getAttributeInstance(entity, holder);
        if (attributeInstance.removeModifier(identifier)) {
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.attribute.modifier.remove.success", Component.translationArg(identifier), AttributeCommand.getAttributeDescription(holder), entity.getName()), false);
            return 1;
        }
        throw ERROR_NO_SUCH_MODIFIER.create(entity.getName(), AttributeCommand.getAttributeDescription(holder), identifier);
    }

    private static Component getAttributeDescription(Holder<Attribute> holder) {
        return Component.translatable(holder.value().getDescriptionId());
    }
}

