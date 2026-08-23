/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.Maps
 */
package net.minecraft.util.datafix.schemas;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.Hook;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.resources.Identifier;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class V1451_6
extends NamespacedSchema {
    public static final String SPECIAL_OBJECTIVE_MARKER = "_special";
    protected static final Hook.HookFunction UNPACK_OBJECTIVE_ID = new Hook.HookFunction(){

        @Override
        public <T> T apply(DynamicOps<T> dynamicOps, T t) {
            Dynamic dynamic = new Dynamic(dynamicOps, t);
            return DataFixUtils.orElse(dynamic.get("CriteriaName").asString().result().map(string -> {
                int n = string.indexOf(58);
                if (n < 0) {
                    return Pair.of(V1451_6.SPECIAL_OBJECTIVE_MARKER, string);
                }
                try {
                    Identifier identifier = Identifier.bySeparator(string.substring(0, n), '.');
                    Identifier identifier2 = Identifier.bySeparator(string.substring(n + 1), '.');
                    return Pair.of(identifier.toString(), identifier2.toString());
                }
                catch (Exception exception) {
                    return Pair.of(V1451_6.SPECIAL_OBJECTIVE_MARKER, string);
                }
            }).map(pair -> dynamic.set("CriteriaType", dynamic.createMap((Map<? extends Dynamic<?>, ? extends Dynamic<?>>)ImmutableMap.of(dynamic.createString("type"), dynamic.createString((String)pair.getFirst()), dynamic.createString("id"), dynamic.createString((String)pair.getSecond()))))), dynamic).getValue();
        }
    };
    protected static final Hook.HookFunction REPACK_OBJECTIVE_ID = new Hook.HookFunction(){

        @Override
        public <T> T apply(DynamicOps<T> dynamicOps, T t) {
            Dynamic dynamic = new Dynamic(dynamicOps, t);
            Optional<Dynamic> optional = dynamic.get("CriteriaType").get().result().flatMap(dynamic2 -> {
                Optional<String> optional = dynamic2.get("type").asString().result();
                Optional<String> optional2 = dynamic2.get("id").asString().result();
                if (optional.isPresent() && optional2.isPresent()) {
                    String string = optional.get();
                    if (string.equals(V1451_6.SPECIAL_OBJECTIVE_MARKER)) {
                        return Optional.of(dynamic.createString(optional2.get()));
                    }
                    return Optional.of(dynamic2.createString(V1451_6.packNamespacedWithDot(string) + ":" + V1451_6.packNamespacedWithDot(optional2.get())));
                }
                return Optional.empty();
            });
            return DataFixUtils.orElse(optional.map(dynamic2 -> dynamic.set("CriteriaName", (Dynamic<?>)dynamic2).remove("CriteriaType")), dynamic).getValue();
        }
    };

    public V1451_6(int n, Schema schema) {
        super(n, schema);
    }

    @Override
    public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> map, Map<String, Supplier<TypeTemplate>> map2) {
        super.registerTypes(schema, map, map2);
        Supplier<TypeTemplate> supplier = () -> DSL.compoundList(References.ITEM_NAME.in(schema), DSL.constType(DSL.intType()));
        schema.registerType(false, References.STATS, () -> DSL.optionalFields("stats", DSL.optionalFields(Pair.of("minecraft:mined", DSL.compoundList(References.BLOCK_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:crafted", (TypeTemplate)supplier.get()), Pair.of("minecraft:used", (TypeTemplate)supplier.get()), Pair.of("minecraft:broken", (TypeTemplate)supplier.get()), Pair.of("minecraft:picked_up", (TypeTemplate)supplier.get()), Pair.of("minecraft:dropped", (TypeTemplate)supplier.get()), Pair.of("minecraft:killed", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:killed_by", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:custom", DSL.compoundList(DSL.constType(V1451_6.namespacedString()), DSL.constType(DSL.intType()))))));
        Map<String, Supplier<TypeTemplate>> map3 = V1451_6.createCriterionTypes(schema);
        schema.registerType(false, References.OBJECTIVE, () -> DSL.hook(DSL.optionalFields("CriteriaType", DSL.taggedChoiceLazy("type", DSL.string(), map3), "DisplayName", References.TEXT_COMPONENT.in(schema)), UNPACK_OBJECTIVE_ID, REPACK_OBJECTIVE_ID));
    }

    protected static Map<String, Supplier<TypeTemplate>> createCriterionTypes(Schema schema) {
        Supplier<TypeTemplate> supplier = () -> DSL.optionalFields("id", References.ITEM_NAME.in(schema));
        Supplier<TypeTemplate> supplier2 = () -> DSL.optionalFields("id", References.BLOCK_NAME.in(schema));
        Supplier<TypeTemplate> supplier3 = () -> DSL.optionalFields("id", References.ENTITY_NAME.in(schema));
        HashMap hashMap = Maps.newHashMap();
        hashMap.put("minecraft:mined", supplier2);
        hashMap.put("minecraft:crafted", supplier);
        hashMap.put("minecraft:used", supplier);
        hashMap.put("minecraft:broken", supplier);
        hashMap.put("minecraft:picked_up", supplier);
        hashMap.put("minecraft:dropped", supplier);
        hashMap.put("minecraft:killed", supplier3);
        hashMap.put("minecraft:killed_by", supplier3);
        hashMap.put("minecraft:custom", () -> DSL.optionalFields("id", DSL.constType(V1451_6.namespacedString())));
        hashMap.put(SPECIAL_OBJECTIVE_MARKER, () -> DSL.optionalFields("id", DSL.constType(DSL.string())));
        return hashMap;
    }

    public static String packNamespacedWithDot(String string) {
        Identifier identifier = Identifier.tryParse(string);
        return identifier != null ? identifier.getNamespace() + "." + identifier.getPath() : string;
    }
}

