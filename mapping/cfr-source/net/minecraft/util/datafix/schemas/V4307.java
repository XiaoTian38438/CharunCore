/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.Map;
import java.util.SequencedMap;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import net.minecraft.util.datafix.schemas.V4059;

public class V4307
extends NamespacedSchema {
    public V4307(int n, Schema schema) {
        super(n, schema);
    }

    public static SequencedMap<String, Supplier<TypeTemplate>> components(Schema schema) {
        SequencedMap<String, Supplier<TypeTemplate>> sequencedMap = V4059.components(schema);
        sequencedMap.put("minecraft:can_place_on", () -> V4307.adventureModePredicate(schema));
        sequencedMap.put("minecraft:can_break", () -> V4307.adventureModePredicate(schema));
        return sequencedMap;
    }

    private static TypeTemplate adventureModePredicate(Schema schema) {
        TypeTemplate typeTemplate = DSL.optionalFields("blocks", DSL.or(References.BLOCK_NAME.in(schema), DSL.list(References.BLOCK_NAME.in(schema))));
        return DSL.or(typeTemplate, DSL.list(typeTemplate));
    }

    @Override
    public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> map, Map<String, Supplier<TypeTemplate>> map2) {
        super.registerTypes(schema, map, map2);
        schema.registerType(true, References.DATA_COMPONENTS, () -> DSL.optionalFieldsLazy(V4307.components(schema)));
    }
}

