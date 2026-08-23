/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
 *  org.slf4j.Logger
 */
package net.minecraft.commands.synchronization;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.lang.runtime.SwitchBootstraps;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.PermissionProviderCheck;
import org.slf4j.Logger;

public class ArgumentUtils {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final byte NUMBER_FLAG_MIN = 1;
    private static final byte NUMBER_FLAG_MAX = 2;

    public static int createNumberFlags(boolean bl, boolean bl2) {
        int n = 0;
        if (bl) {
            n |= 1;
        }
        if (bl2) {
            n |= 2;
        }
        return n;
    }

    public static boolean numberHasMin(byte by) {
        return (by & 1) != 0;
    }

    public static boolean numberHasMax(byte by) {
        return (by & 2) != 0;
    }

    private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void serializeArgumentCap(JsonObject jsonObject, ArgumentTypeInfo<A, T> argumentTypeInfo, ArgumentTypeInfo.Template<A> template) {
        argumentTypeInfo.serializeToJson(template, jsonObject);
    }

    private static <T extends ArgumentType<?>> void serializeArgumentToJson(JsonObject jsonObject, T t) {
        ArgumentTypeInfo.Template<T> template = ArgumentTypeInfos.unpack(t);
        jsonObject.addProperty("type", "argument");
        jsonObject.addProperty("parser", String.valueOf(BuiltInRegistries.COMMAND_ARGUMENT_TYPE.getKey(template.type())));
        JsonObject jsonObject2 = new JsonObject();
        ArgumentUtils.serializeArgumentCap(jsonObject2, template.type(), template);
        if (!jsonObject2.isEmpty()) {
            jsonObject.add("properties", (JsonElement)jsonObject2);
        }
    }

    public static <S> JsonObject serializeNodeToJson(CommandDispatcher<S> commandDispatcher, CommandNode<S> commandNode) {
        Collection<String> collection;
        Object object;
        JsonElement jsonElement;
        JsonObject jsonObject = new JsonObject();
        CommandNode<S> commandNode2 = commandNode;
        Objects.requireNonNull(commandNode2);
        Object object2 = commandNode2;
        int n = 0;
        switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{RootCommandNode.class, LiteralCommandNode.class, ArgumentCommandNode.class}, object2, n)) {
            case 0: {
                jsonElement = (RootCommandNode)object2;
                jsonObject.addProperty("type", "root");
                break;
            }
            case 1: {
                object = (LiteralCommandNode)object2;
                jsonObject.addProperty("type", "literal");
                break;
            }
            case 2: {
                ArgumentCommandNode object3 = (ArgumentCommandNode)object2;
                ArgumentUtils.serializeArgumentToJson(jsonObject, object3.getType());
                break;
            }
            default: {
                LOGGER.error("Could not serialize node {} ({})!", commandNode, commandNode.getClass());
                jsonObject.addProperty("type", "unknown");
            }
        }
        object2 = commandNode.getChildren();
        if (!object2.isEmpty()) {
            JsonObject jsonObject2 = new JsonObject();
            jsonElement = object2.iterator();
            while (jsonElement.hasNext()) {
                object = (CommandNode)jsonElement.next();
                jsonObject2.add(((CommandNode)object).getName(), (JsonElement)ArgumentUtils.serializeNodeToJson(commandDispatcher, object));
            }
            jsonObject.add("children", (JsonElement)jsonObject2);
        }
        if (commandNode.getCommand() != null) {
            jsonObject.addProperty("executable", Boolean.valueOf(true));
        }
        if ((jsonElement = commandNode.getRequirement()) instanceof PermissionProviderCheck) {
            PermissionProviderCheck permissionProviderCheck = (PermissionProviderCheck)jsonElement;
            jsonElement = PermissionCheck.CODEC.encodeStart(JsonOps.INSTANCE, permissionProviderCheck.test()).getOrThrow(string -> new IllegalStateException("Failed to serialize requirement: " + string));
            jsonObject.add("permissions", jsonElement);
        }
        if (commandNode.getRedirect() != null && !(collection = commandDispatcher.getPath(commandNode.getRedirect())).isEmpty()) {
            jsonElement = new JsonArray();
            for (String string2 : collection) {
                jsonElement.add(string2);
            }
            jsonObject.add("redirect", jsonElement);
        }
        return jsonObject;
    }

    public static <T> Set<ArgumentType<?>> findUsedArgumentTypes(CommandNode<T> commandNode) {
        ReferenceOpenHashSet referenceOpenHashSet = new ReferenceOpenHashSet();
        HashSet hashSet = new HashSet();
        ArgumentUtils.findUsedArgumentTypes(commandNode, hashSet, referenceOpenHashSet);
        return hashSet;
    }

    private static <T> void findUsedArgumentTypes(CommandNode<T> commandNode2, Set<ArgumentType<?>> set, Set<CommandNode<T>> set2) {
        CommandNode commandNode3;
        if (!set2.add(commandNode2)) {
            return;
        }
        if (commandNode2 instanceof ArgumentCommandNode) {
            commandNode3 = (ArgumentCommandNode)commandNode2;
            set.add(commandNode3.getType());
        }
        commandNode2.getChildren().forEach(commandNode -> ArgumentUtils.findUsedArgumentTypes(commandNode, set, set2));
        commandNode3 = commandNode2.getRedirect();
        if (commandNode3 != null) {
            ArgumentUtils.findUsedArgumentTypes(commandNode3, set, set2);
        }
    }
}

