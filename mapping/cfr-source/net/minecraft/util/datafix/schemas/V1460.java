/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 */
package net.minecraft.util.datafix.schemas;

import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.templates.Hook;
import com.mojang.datafixers.types.templates.TypeTemplate;
import com.mojang.datafixers.util.Pair;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.util.datafix.schemas.NamespacedSchema;
import net.minecraft.util.datafix.schemas.V1451_6;
import net.minecraft.util.datafix.schemas.V1458;
import net.minecraft.util.datafix.schemas.V705;
import net.minecraft.util.datafix.schemas.V99;

public class V1460
extends NamespacedSchema {
    public V1460(int n, Schema schema) {
        super(n, schema);
    }

    protected static void registerMob(Schema schema, Map<String, Supplier<TypeTemplate>> map, String string) {
        schema.registerSimple(map, string);
    }

    protected static void registerInventory(Schema schema, Map<String, Supplier<TypeTemplate>> map, String string) {
        schema.register(map, string, () -> V1458.nameableInventory(schema));
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerEntities(Schema schema) {
        HashMap hashMap = Maps.newHashMap();
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:area_effect_cloud", (String string) -> DSL.optionalFields("Particle", References.PARTICLE.in(schema)));
        V1460.registerMob(schema, hashMap, "minecraft:armor_stand");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:arrow", (String string) -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema)));
        V1460.registerMob(schema, hashMap, "minecraft:bat");
        V1460.registerMob(schema, hashMap, "minecraft:blaze");
        schema.registerSimple(hashMap, "minecraft:boat");
        V1460.registerMob(schema, hashMap, "minecraft:cave_spider");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:chest_minecart", (String string) -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
        V1460.registerMob(schema, hashMap, "minecraft:chicken");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:commandblock_minecart", (String string) -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), "LastOutput", References.TEXT_COMPONENT.in(schema)));
        V1460.registerMob(schema, hashMap, "minecraft:cow");
        V1460.registerMob(schema, hashMap, "minecraft:creeper");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:donkey", (String string) -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema)));
        schema.registerSimple(hashMap, "minecraft:dragon_fireball");
        schema.registerSimple(hashMap, "minecraft:egg");
        V1460.registerMob(schema, hashMap, "minecraft:elder_guardian");
        schema.registerSimple(hashMap, "minecraft:ender_crystal");
        V1460.registerMob(schema, hashMap, "minecraft:ender_dragon");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:enderman", (String string) -> DSL.optionalFields("carriedBlockState", References.BLOCK_STATE.in(schema)));
        V1460.registerMob(schema, hashMap, "minecraft:endermite");
        schema.registerSimple(hashMap, "minecraft:ender_pearl");
        schema.registerSimple(hashMap, "minecraft:evocation_fangs");
        V1460.registerMob(schema, hashMap, "minecraft:evocation_illager");
        schema.registerSimple(hashMap, "minecraft:eye_of_ender_signal");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:falling_block", (String string) -> DSL.optionalFields("BlockState", References.BLOCK_STATE.in(schema), "TileEntityData", References.BLOCK_ENTITY.in(schema)));
        schema.registerSimple(hashMap, "minecraft:fireball");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:fireworks_rocket", (String string) -> DSL.optionalFields("FireworksItem", References.ITEM_STACK.in(schema)));
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:furnace_minecart", (String string) -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));
        V1460.registerMob(schema, hashMap, "minecraft:ghast");
        V1460.registerMob(schema, hashMap, "minecraft:giant");
        V1460.registerMob(schema, hashMap, "minecraft:guardian");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:hopper_minecart", (String string) -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), "Items", DSL.list(References.ITEM_STACK.in(schema))));
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:horse", (String string) -> DSL.optionalFields("ArmorItem", References.ITEM_STACK.in(schema), "SaddleItem", References.ITEM_STACK.in(schema)));
        V1460.registerMob(schema, hashMap, "minecraft:husk");
        V1460.registerMob(schema, hashMap, "minecraft:illusion_illager");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:item", (String string) -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:item_frame", (String string) -> DSL.optionalFields("Item", References.ITEM_STACK.in(schema)));
        schema.registerSimple(hashMap, "minecraft:leash_knot");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:llama", (String string) -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema), "DecorItem", References.ITEM_STACK.in(schema)));
        schema.registerSimple(hashMap, "minecraft:llama_spit");
        V1460.registerMob(schema, hashMap, "minecraft:magma_cube");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:minecart", (String string) -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));
        V1460.registerMob(schema, hashMap, "minecraft:mooshroom");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:mule", (String string) -> DSL.optionalFields("Items", DSL.list(References.ITEM_STACK.in(schema)), "SaddleItem", References.ITEM_STACK.in(schema)));
        V1460.registerMob(schema, hashMap, "minecraft:ocelot");
        schema.registerSimple(hashMap, "minecraft:painting");
        V1460.registerMob(schema, hashMap, "minecraft:parrot");
        V1460.registerMob(schema, hashMap, "minecraft:pig");
        V1460.registerMob(schema, hashMap, "minecraft:polar_bear");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:potion", (String string) -> DSL.optionalFields("Potion", References.ITEM_STACK.in(schema)));
        V1460.registerMob(schema, hashMap, "minecraft:rabbit");
        V1460.registerMob(schema, hashMap, "minecraft:sheep");
        V1460.registerMob(schema, hashMap, "minecraft:shulker");
        schema.registerSimple(hashMap, "minecraft:shulker_bullet");
        V1460.registerMob(schema, hashMap, "minecraft:silverfish");
        V1460.registerMob(schema, hashMap, "minecraft:skeleton");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:skeleton_horse", (String string) -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema)));
        V1460.registerMob(schema, hashMap, "minecraft:slime");
        schema.registerSimple(hashMap, "minecraft:small_fireball");
        schema.registerSimple(hashMap, "minecraft:snowball");
        V1460.registerMob(schema, hashMap, "minecraft:snowman");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:spawner_minecart", (String string) -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema), References.UNTAGGED_SPAWNER.in(schema)));
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:spectral_arrow", (String string) -> DSL.optionalFields("inBlockState", References.BLOCK_STATE.in(schema)));
        V1460.registerMob(schema, hashMap, "minecraft:spider");
        V1460.registerMob(schema, hashMap, "minecraft:squid");
        V1460.registerMob(schema, hashMap, "minecraft:stray");
        schema.registerSimple(hashMap, "minecraft:tnt");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:tnt_minecart", (String string) -> DSL.optionalFields("DisplayState", References.BLOCK_STATE.in(schema)));
        V1460.registerMob(schema, hashMap, "minecraft:vex");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:villager", (String string) -> DSL.optionalFields("Inventory", DSL.list(References.ITEM_STACK.in(schema)), "Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(schema)))));
        V1460.registerMob(schema, hashMap, "minecraft:villager_golem");
        V1460.registerMob(schema, hashMap, "minecraft:vindication_illager");
        V1460.registerMob(schema, hashMap, "minecraft:witch");
        V1460.registerMob(schema, hashMap, "minecraft:wither");
        V1460.registerMob(schema, hashMap, "minecraft:wither_skeleton");
        schema.registerSimple(hashMap, "minecraft:wither_skull");
        V1460.registerMob(schema, hashMap, "minecraft:wolf");
        schema.registerSimple(hashMap, "minecraft:xp_bottle");
        schema.registerSimple(hashMap, "minecraft:xp_orb");
        V1460.registerMob(schema, hashMap, "minecraft:zombie");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:zombie_horse", (String string) -> DSL.optionalFields("SaddleItem", References.ITEM_STACK.in(schema)));
        V1460.registerMob(schema, hashMap, "minecraft:zombie_pigman");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:zombie_villager", (String string) -> DSL.optionalFields("Offers", DSL.optionalFields("Recipes", DSL.list(References.VILLAGER_TRADE.in(schema)))));
        return hashMap;
    }

    @Override
    public Map<String, Supplier<TypeTemplate>> registerBlockEntities(Schema schema) {
        HashMap hashMap = Maps.newHashMap();
        V1460.registerInventory(schema, hashMap, "minecraft:furnace");
        V1460.registerInventory(schema, hashMap, "minecraft:chest");
        V1460.registerInventory(schema, hashMap, "minecraft:trapped_chest");
        schema.registerSimple(hashMap, "minecraft:ender_chest");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:jukebox", (String string) -> DSL.optionalFields("RecordItem", References.ITEM_STACK.in(schema)));
        V1460.registerInventory(schema, hashMap, "minecraft:dispenser");
        V1460.registerInventory(schema, hashMap, "minecraft:dropper");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:sign", () -> V99.sign(schema));
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:mob_spawner", (String string) -> References.UNTAGGED_SPAWNER.in(schema));
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:piston", (String string) -> DSL.optionalFields("blockState", References.BLOCK_STATE.in(schema)));
        V1460.registerInventory(schema, hashMap, "minecraft:brewing_stand");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:enchanting_table", () -> V1458.nameable(schema));
        schema.registerSimple(hashMap, "minecraft:end_portal");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:beacon", () -> V1458.nameable(schema));
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:skull", () -> DSL.optionalFields("custom_name", References.TEXT_COMPONENT.in(schema)));
        schema.registerSimple(hashMap, "minecraft:daylight_detector");
        V1460.registerInventory(schema, hashMap, "minecraft:hopper");
        schema.registerSimple(hashMap, "minecraft:comparator");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:banner", () -> V1458.nameable(schema));
        schema.registerSimple(hashMap, "minecraft:structure_block");
        schema.registerSimple(hashMap, "minecraft:end_gateway");
        schema.register((Map<String, Supplier<TypeTemplate>>)hashMap, "minecraft:command_block", () -> DSL.optionalFields("LastOutput", References.TEXT_COMPONENT.in(schema)));
        V1460.registerInventory(schema, hashMap, "minecraft:shulker_box");
        schema.registerSimple(hashMap, "minecraft:bed");
        return hashMap;
    }

    @Override
    public void registerTypes(Schema schema, Map<String, Supplier<TypeTemplate>> map, Map<String, Supplier<TypeTemplate>> map2) {
        schema.registerType(false, References.LEVEL, () -> DSL.optionalFields("CustomBossEvents", DSL.compoundList(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(schema))), References.LIGHTWEIGHT_LEVEL.in(schema)));
        schema.registerType(false, References.LIGHTWEIGHT_LEVEL, DSL::remainder);
        schema.registerType(false, References.RECIPE, () -> DSL.constType(V1460.namespacedString()));
        schema.registerType(false, References.PLAYER, () -> DSL.optionalFields(Pair.of("RootVehicle", DSL.optionalFields("Entity", References.ENTITY_TREE.in(schema))), Pair.of("ender_pearls", DSL.list(References.ENTITY_TREE.in(schema))), Pair.of("Inventory", DSL.list(References.ITEM_STACK.in(schema))), Pair.of("EnderItems", DSL.list(References.ITEM_STACK.in(schema))), Pair.of("ShoulderEntityLeft", References.ENTITY_TREE.in(schema)), Pair.of("ShoulderEntityRight", References.ENTITY_TREE.in(schema)), Pair.of("recipeBook", DSL.optionalFields("recipes", DSL.list(References.RECIPE.in(schema)), "toBeDisplayed", DSL.list(References.RECIPE.in(schema))))));
        schema.registerType(false, References.CHUNK, () -> DSL.fields("Level", DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema)), "TileEntities", DSL.list(DSL.or(References.BLOCK_ENTITY.in(schema), DSL.remainder())), "TileTicks", DSL.list(DSL.fields("i", References.BLOCK_NAME.in(schema))), "Sections", DSL.list(DSL.optionalFields("Palette", DSL.list(References.BLOCK_STATE.in(schema)))))));
        schema.registerType(true, References.BLOCK_ENTITY, () -> DSL.optionalFields("components", References.DATA_COMPONENTS.in(schema), DSL.taggedChoiceLazy("id", V1460.namespacedString(), map2)));
        schema.registerType(true, References.ENTITY_TREE, () -> DSL.optionalFields("Passengers", DSL.list(References.ENTITY_TREE.in(schema)), References.ENTITY.in(schema)));
        schema.registerType(true, References.ENTITY, () -> DSL.and(References.ENTITY_EQUIPMENT.in(schema), DSL.optionalFields("CustomName", References.TEXT_COMPONENT.in(schema), DSL.taggedChoiceLazy("id", V1460.namespacedString(), map))));
        schema.registerType(true, References.ITEM_STACK, () -> DSL.hook(DSL.optionalFields("id", References.ITEM_NAME.in(schema), "tag", V99.itemStackTag(schema)), V705.ADD_NAMES, Hook.HookFunction.IDENTITY));
        schema.registerType(false, References.HOTBAR, () -> DSL.compoundList(DSL.list(References.ITEM_STACK.in(schema))));
        schema.registerType(false, References.OPTIONS, DSL::remainder);
        schema.registerType(false, References.STRUCTURE, () -> DSL.optionalFields("entities", DSL.list(DSL.optionalFields("nbt", References.ENTITY_TREE.in(schema))), "blocks", DSL.list(DSL.optionalFields("nbt", References.BLOCK_ENTITY.in(schema))), "palette", DSL.list(References.BLOCK_STATE.in(schema))));
        schema.registerType(false, References.BLOCK_NAME, () -> DSL.constType(V1460.namespacedString()));
        schema.registerType(false, References.ITEM_NAME, () -> DSL.constType(V1460.namespacedString()));
        schema.registerType(false, References.BLOCK_STATE, DSL::remainder);
        schema.registerType(false, References.FLAT_BLOCK_STATE, DSL::remainder);
        Supplier<TypeTemplate> supplier = () -> DSL.compoundList(References.ITEM_NAME.in(schema), DSL.constType(DSL.intType()));
        schema.registerType(false, References.STATS, () -> DSL.optionalFields("stats", DSL.optionalFields(Pair.of("minecraft:mined", DSL.compoundList(References.BLOCK_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:crafted", (TypeTemplate)supplier.get()), Pair.of("minecraft:used", (TypeTemplate)supplier.get()), Pair.of("minecraft:broken", (TypeTemplate)supplier.get()), Pair.of("minecraft:picked_up", (TypeTemplate)supplier.get()), Pair.of("minecraft:dropped", (TypeTemplate)supplier.get()), Pair.of("minecraft:killed", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:killed_by", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.intType()))), Pair.of("minecraft:custom", DSL.compoundList(DSL.constType(V1460.namespacedString()), DSL.constType(DSL.intType()))))));
        schema.registerType(false, References.SAVED_DATA_COMMAND_STORAGE, DSL::remainder);
        schema.registerType(false, References.SAVED_DATA_TICKETS, DSL::remainder);
        schema.registerType(false, References.SAVED_DATA_MAP_DATA, () -> DSL.optionalFields("data", DSL.optionalFields("banners", DSL.list(DSL.optionalFields("Name", References.TEXT_COMPONENT.in(schema))))));
        schema.registerType(false, References.SAVED_DATA_MAP_INDEX, DSL::remainder);
        schema.registerType(false, References.SAVED_DATA_RAIDS, DSL::remainder);
        schema.registerType(false, References.SAVED_DATA_RANDOM_SEQUENCES, DSL::remainder);
        schema.registerType(false, References.SAVED_DATA_SCOREBOARD, () -> DSL.optionalFields("data", DSL.optionalFields("Objectives", DSL.list(References.OBJECTIVE.in(schema)), "Teams", DSL.list(References.TEAM.in(schema)), "PlayerScores", DSL.list(DSL.optionalFields("display", References.TEXT_COMPONENT.in(schema))))));
        schema.registerType(false, References.SAVED_DATA_STOPWATCHES, DSL::remainder);
        schema.registerType(false, References.SAVED_DATA_STRUCTURE_FEATURE_INDICES, () -> DSL.optionalFields("data", DSL.optionalFields("Features", DSL.compoundList(References.STRUCTURE_FEATURE.in(schema)))));
        schema.registerType(false, References.SAVED_DATA_WORLD_BORDER, DSL::remainder);
        schema.registerType(false, References.DEBUG_PROFILE, DSL::remainder);
        schema.registerType(false, References.STRUCTURE_FEATURE, DSL::remainder);
        Map<String, Supplier<TypeTemplate>> map3 = V1451_6.createCriterionTypes(schema);
        schema.registerType(false, References.OBJECTIVE, () -> DSL.hook(DSL.optionalFields("CriteriaType", DSL.taggedChoiceLazy("type", DSL.string(), map3), "DisplayName", References.TEXT_COMPONENT.in(schema)), V1451_6.UNPACK_OBJECTIVE_ID, V1451_6.REPACK_OBJECTIVE_ID));
        schema.registerType(false, References.TEAM, () -> DSL.optionalFields("MemberNamePrefix", References.TEXT_COMPONENT.in(schema), "MemberNameSuffix", References.TEXT_COMPONENT.in(schema), "DisplayName", References.TEXT_COMPONENT.in(schema)));
        schema.registerType(true, References.UNTAGGED_SPAWNER, () -> DSL.optionalFields("SpawnPotentials", DSL.list(DSL.fields("Entity", References.ENTITY_TREE.in(schema))), "SpawnData", References.ENTITY_TREE.in(schema)));
        schema.registerType(false, References.ADVANCEMENTS, () -> DSL.optionalFields("minecraft:adventure/adventuring_time", DSL.optionalFields("criteria", DSL.compoundList(References.BIOME.in(schema), DSL.constType(DSL.string()))), "minecraft:adventure/kill_a_mob", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string()))), "minecraft:adventure/kill_all_mobs", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string()))), "minecraft:husbandry/bred_all_animals", DSL.optionalFields("criteria", DSL.compoundList(References.ENTITY_NAME.in(schema), DSL.constType(DSL.string())))));
        schema.registerType(false, References.BIOME, () -> DSL.constType(V1460.namespacedString()));
        schema.registerType(false, References.ENTITY_NAME, () -> DSL.constType(V1460.namespacedString()));
        schema.registerType(false, References.POI_CHUNK, DSL::remainder);
        schema.registerType(false, References.WORLD_GEN_SETTINGS, DSL::remainder);
        schema.registerType(false, References.ENTITY_CHUNK, () -> DSL.optionalFields("Entities", DSL.list(References.ENTITY_TREE.in(schema))));
        schema.registerType(true, References.DATA_COMPONENTS, DSL::remainder);
        schema.registerType(true, References.VILLAGER_TRADE, () -> DSL.optionalFields("buy", References.ITEM_STACK.in(schema), "buyB", References.ITEM_STACK.in(schema), "sell", References.ITEM_STACK.in(schema)));
        schema.registerType(true, References.PARTICLE, () -> DSL.constType(DSL.string()));
        schema.registerType(true, References.TEXT_COMPONENT, () -> DSL.constType(DSL.string()));
        schema.registerType(true, References.ENTITY_EQUIPMENT, () -> DSL.and(DSL.optional(DSL.field("ArmorItems", DSL.list(References.ITEM_STACK.in(schema)))), DSL.optional(DSL.field("HandItems", DSL.list(References.ITEM_STACK.in(schema)))), DSL.optional(DSL.field("body_armor_item", References.ITEM_STACK.in(schema))), DSL.optional(DSL.field("saddle", References.ITEM_STACK.in(schema)))));
    }
}

