/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util.datafix.schemas;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.TypeTemplate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SequencedMap;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;

public class V3818_3
extends NamespacedSchema {
    public V3818_3(int n, Schema schema) {
        super(n, schema);
    }

    public static SequencedMap<String, Supplier<TypeTemplate>> components(Schema schema) {
        LinkedHashMap<String, Supplier<TypeTemplate>> linkedHashMap = new LinkedHashMap<String, Supplier<TypeTemplate>>();
        linkedHashMap.put("minecraft:bees", () -> DSL.list(DSL.optionalFields("entity_data", References.ENTITY_TREE.in(schema))));
        linkedHashMap.put("minecraft:block_entity_data", () -> References.BLOCK_ENTITY.in(schema));
        linkedHashMap.put("minecraft:bundle_contents", () -> DSL.list(References.ITEM_STACK.in(schema)));
        linkedHashMap.put("minecraft:can_break", () -> DSL.optionalFields("predicates", DSL.list(DSL.optionalFields("blocks", DSL.or(References.BLOCK_NAME.in(schema), DSL.list(References.BLOCK_NAME.in(schema)))))));
        linkedHashMap.put("minecraft:can_place_on", () -> DSL.optionalFields("predicates", DSL.list(DSL.optionalFields("blocks", DSL.or(References.BLOCK_NAME.in(schema), DSL.list(References.BLOCK_NAME.in(schema)))))));
        linkedHashMap.put("minecraft:charged_projectiles", () -> DSL.list(References.ITEM_STACK.in(schema)));
        linkedHashMap.put("minecraft:container", () -> DSL.list(DSL.optionalFields("item", References.ITEM_STACK.in(schema))));
        linkedHashMap.put("minecraft:entity_data", () -> References.ENTITY_TREE.in(schema));
        linkedHashMap.put("minecraft:pot_decorations", () -> DSL.list(References.ITEM_NAME.in(schema)));
        linkedHashMap.put("minecraft:food", () -> DSL.optionalFields("using_converts_to", References.ITEM_STACK.in(schema)));
        linkedHashMap.put("minecraft:custom_name", () -> References.TEXT_COMPONENT.in(schema));
        linkedHashMap.put("minecraft:item_name", () -> References.TEXT_COMPONENT.in(schema));
        linkedHashMap.put("minecraft:lore", () -> DSL.list(References.TEXT_COMPONENT.in(schema)));
        linkedHashMap.put("minecraft:written_book_content", () -> DSL.optionalFields("pages", DSL.list(DSL.or(DSL.optionalFields("raw", References.TEXT_COMPONENT.in(schema), "filtered", References.TEXT_COMPONENT.in(schema)), References.TEXT_COMPONENT.in(schema)))));
        return linkedHashMap;
    }

    @Override
    public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> map, Map<String, Supplier<TypeTemplate>> map2) {
        super.registerTypes(schema, map, map2);
        schema.registerType(true, References.DATA_COMPONENTS, () -> DSL.optionalFieldsLazy(V3818_3.components(schema)));
    }
}

