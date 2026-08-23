/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.google.common.collect.Maps
 *  org.apache.commons.lang3.math.NumberUtils
 *  org.apache.commons.lang3.mutable.MutableBoolean
 *  org.apache.commons.lang3.mutable.MutableInt
 */
package net.minecraft.util.datafix.fixes;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicLike;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.OptionalDynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.util.datafix.fixes.References;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

public class WorldGenSettingsFix
extends DataFix {
    private static final String VILLAGE = "minecraft:village";
    private static final String DESERT_PYRAMID = "minecraft:desert_pyramid";
    private static final String IGLOO = "minecraft:igloo";
    private static final String JUNGLE_TEMPLE = "minecraft:jungle_pyramid";
    private static final String SWAMP_HUT = "minecraft:swamp_hut";
    private static final String PILLAGER_OUTPOST = "minecraft:pillager_outpost";
    private static final String END_CITY = "minecraft:endcity";
    private static final String WOODLAND_MANSION = "minecraft:mansion";
    private static final String OCEAN_MONUMENT = "minecraft:monument";
    private static final ImmutableMap<String, StructureFeatureConfiguration> DEFAULTS = ImmutableMap.builder().put((Object)"minecraft:village", (Object)new StructureFeatureConfiguration(32, 8, 10387312)).put((Object)"minecraft:desert_pyramid", (Object)new StructureFeatureConfiguration(32, 8, 14357617)).put((Object)"minecraft:igloo", (Object)new StructureFeatureConfiguration(32, 8, 14357618)).put((Object)"minecraft:jungle_pyramid", (Object)new StructureFeatureConfiguration(32, 8, 14357619)).put((Object)"minecraft:swamp_hut", (Object)new StructureFeatureConfiguration(32, 8, 14357620)).put((Object)"minecraft:pillager_outpost", (Object)new StructureFeatureConfiguration(32, 8, 165745296)).put((Object)"minecraft:monument", (Object)new StructureFeatureConfiguration(32, 5, 10387313)).put((Object)"minecraft:endcity", (Object)new StructureFeatureConfiguration(20, 11, 10387313)).put((Object)"minecraft:mansion", (Object)new StructureFeatureConfiguration(80, 20, 10387319)).build();

    public WorldGenSettingsFix(Schema schema) {
        super(schema, true);
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.fixTypeEverywhereTyped("WorldGenSettings building", this.getInputSchema().getType(References.WORLD_GEN_SETTINGS), typed -> typed.update(DSL.remainderFinder(), WorldGenSettingsFix::fix));
    }

    private static <T> Dynamic<T> noise(long l, DynamicLike<T> dynamicLike, Dynamic<T> dynamic, Dynamic<T> dynamic2) {
        return dynamicLike.createMap((Map<Dynamic<?>, Dynamic<?>>)ImmutableMap.of(dynamicLike.createString("type"), dynamicLike.createString("minecraft:noise"), dynamicLike.createString("biome_source"), dynamic2, dynamicLike.createString("seed"), dynamicLike.createLong(l), dynamicLike.createString("settings"), dynamic));
    }

    private static <T> Dynamic<T> vanillaBiomeSource(Dynamic<T> dynamic, long l, boolean bl, boolean bl2) {
        ImmutableMap.Builder builder = ImmutableMap.builder().put(dynamic.createString("type"), dynamic.createString("minecraft:vanilla_layered")).put(dynamic.createString("seed"), dynamic.createLong(l)).put(dynamic.createString("large_biomes"), dynamic.createBoolean(bl2));
        if (bl) {
            builder.put(dynamic.createString("legacy_biome_init_layer"), dynamic.createBoolean(bl));
        }
        return dynamic.createMap((Map<Dynamic<?>, Dynamic<?>>)builder.build());
    }

    private static <T> Dynamic<T> fix(Dynamic<T> dynamic2) {
        ImmutableMap.Builder builder;
        Dynamic<T> dynamic3;
        DynamicOps dynamicOps = dynamic2.getOps();
        long l = dynamic2.get("RandomSeed").asLong(0L);
        Optional<String> optional = dynamic2.get("generatorName").asString().map(string -> string.toLowerCase(Locale.ROOT)).result();
        Optional optional2 = dynamic2.get("legacy_custom_options").asString().result().map(Optional::of).orElseGet(() -> {
            if (optional.equals(Optional.of("customized"))) {
                return dynamic2.get("generatorOptions").asString().result();
            }
            return Optional.empty();
        });
        boolean bl = false;
        if (optional.equals(Optional.of("customized"))) {
            dynamic3 = WorldGenSettingsFix.defaultOverworld(dynamic2, l);
        } else if (optional.isEmpty()) {
            dynamic3 = WorldGenSettingsFix.defaultOverworld(dynamic2, l);
        } else {
            switch (optional.get()) {
                case "flat": {
                    builder = dynamic2.get("generatorOptions");
                    Map map = WorldGenSettingsFix.fixFlatStructures(dynamicOps, builder);
                    dynamic3 = dynamic2.createMap((Map<Dynamic<?>, Dynamic<?>>)ImmutableMap.of(dynamic2.createString("type"), dynamic2.createString("minecraft:flat"), dynamic2.createString("settings"), dynamic2.createMap((Map<Dynamic<?>, Dynamic<?>>)ImmutableMap.of(dynamic2.createString("structures"), dynamic2.createMap(map), dynamic2.createString("layers"), (Object)builder.get("layers").result().orElseGet(() -> dynamic2.createList(Stream.of(dynamic2.createMap((Map<? extends Dynamic<?>, ? extends Dynamic<?>>)ImmutableMap.of(dynamic2.createString("height"), dynamic2.createInt(1), dynamic2.createString("block"), dynamic2.createString("minecraft:bedrock"))), dynamic2.createMap((Map<? extends Dynamic<?>, ? extends Dynamic<?>>)ImmutableMap.of(dynamic2.createString("height"), dynamic2.createInt(2), dynamic2.createString("block"), dynamic2.createString("minecraft:dirt"))), dynamic2.createMap((Map<? extends Dynamic<?>, ? extends Dynamic<?>>)ImmutableMap.of(dynamic2.createString("height"), dynamic2.createInt(1), dynamic2.createString("block"), dynamic2.createString("minecraft:grass_block")))))), dynamic2.createString("biome"), dynamic2.createString(builder.get("biome").asString("minecraft:plains"))))));
                    break;
                }
                case "debug_all_block_states": {
                    dynamic3 = dynamic2.createMap((Map<Dynamic<?>, Dynamic<?>>)ImmutableMap.of(dynamic2.createString("type"), dynamic2.createString("minecraft:debug")));
                    break;
                }
                case "buffet": {
                    Dynamic dynamic4;
                    Dynamic dynamic5;
                    OptionalDynamic<T> optionalDynamic = dynamic2.get("generatorOptions");
                    OptionalDynamic<T> optionalDynamic2 = optionalDynamic.get("chunk_generator");
                    Optional<String> optional3 = optionalDynamic2.get("type").asString().result();
                    if (Objects.equals(optional3, Optional.of("minecraft:caves"))) {
                        dynamic5 = dynamic2.createString("minecraft:caves");
                        bl = true;
                    } else {
                        dynamic5 = Objects.equals(optional3, Optional.of("minecraft:floating_islands")) ? dynamic2.createString("minecraft:floating_islands") : dynamic2.createString("minecraft:overworld");
                    }
                    Dynamic dynamic6 = optionalDynamic.get("biome_source").result().orElseGet(() -> dynamic2.createMap((Map<? extends Dynamic<?>, ? extends Dynamic<?>>)ImmutableMap.of(dynamic2.createString("type"), dynamic2.createString("minecraft:fixed"))));
                    if (dynamic6.get("type").asString().result().equals(Optional.of("minecraft:fixed"))) {
                        String string2 = dynamic6.get("options").get("biomes").asStream().findFirst().flatMap(dynamic -> dynamic.asString().result()).orElse("minecraft:ocean");
                        dynamic4 = dynamic6.remove("options").set("biome", dynamic2.createString(string2));
                    } else {
                        dynamic4 = dynamic6;
                    }
                    dynamic3 = WorldGenSettingsFix.noise(l, dynamic2, dynamic5, dynamic4);
                    break;
                }
                default: {
                    boolean bl2 = optional.get().equals("default");
                    boolean bl3 = optional.get().equals("default_1_1") || bl2 && dynamic2.get("generatorVersion").asInt(0) == 0;
                    boolean bl4 = optional.get().equals("amplified");
                    boolean bl5 = optional.get().equals("largebiomes");
                    dynamic3 = WorldGenSettingsFix.noise(l, dynamic2, dynamic2.createString(bl4 ? "minecraft:amplified" : "minecraft:overworld"), WorldGenSettingsFix.vanillaBiomeSource(dynamic2, l, bl3, bl5));
                }
            }
        }
        boolean bl6 = dynamic2.get("MapFeatures").asBoolean(true);
        boolean bl7 = dynamic2.get("BonusChest").asBoolean(false);
        builder = ImmutableMap.builder();
        builder.put(dynamicOps.createString("seed"), dynamicOps.createLong(l));
        builder.put(dynamicOps.createString("generate_features"), dynamicOps.createBoolean(bl6));
        builder.put(dynamicOps.createString("bonus_chest"), dynamicOps.createBoolean(bl7));
        builder.put(dynamicOps.createString("dimensions"), WorldGenSettingsFix.vanillaLevels(dynamic2, l, dynamic3, bl));
        optional2.ifPresent(string -> builder.put(dynamicOps.createString("legacy_custom_options"), dynamicOps.createString((String)string)));
        return new Dynamic(dynamicOps, dynamicOps.createMap(builder.build()));
    }

    protected static <T> Dynamic<T> defaultOverworld(Dynamic<T> dynamic, long l) {
        return WorldGenSettingsFix.noise(l, dynamic, dynamic.createString("minecraft:overworld"), WorldGenSettingsFix.vanillaBiomeSource(dynamic, l, false, false));
    }

    protected static <T> T vanillaLevels(Dynamic<T> dynamic, long l, Dynamic<T> dynamic2, boolean bl) {
        DynamicOps dynamicOps = dynamic.getOps();
        return dynamicOps.createMap(ImmutableMap.of(dynamicOps.createString("minecraft:overworld"), dynamicOps.createMap(ImmutableMap.of(dynamicOps.createString("type"), dynamicOps.createString("minecraft:overworld" + (bl ? "_caves" : "")), dynamicOps.createString("generator"), dynamic2.getValue())), dynamicOps.createString("minecraft:the_nether"), dynamicOps.createMap(ImmutableMap.of(dynamicOps.createString("type"), dynamicOps.createString("minecraft:the_nether"), dynamicOps.createString("generator"), WorldGenSettingsFix.noise(l, dynamic, dynamic.createString("minecraft:nether"), dynamic.createMap((Map<Dynamic<?>, Dynamic<?>>)ImmutableMap.of(dynamic.createString("type"), dynamic.createString("minecraft:multi_noise"), dynamic.createString("seed"), dynamic.createLong(l), dynamic.createString("preset"), dynamic.createString("minecraft:nether")))).getValue())), dynamicOps.createString("minecraft:the_end"), dynamicOps.createMap(ImmutableMap.of(dynamicOps.createString("type"), dynamicOps.createString("minecraft:the_end"), dynamicOps.createString("generator"), WorldGenSettingsFix.noise(l, dynamic, dynamic.createString("minecraft:end"), dynamic.createMap((Map<Dynamic<?>, Dynamic<?>>)ImmutableMap.of(dynamic.createString("type"), dynamic.createString("minecraft:the_end"), dynamic.createString("seed"), dynamic.createLong(l)))).getValue()))));
    }

    private static <T> Map<Dynamic<T>, Dynamic<T>> fixFlatStructures(DynamicOps<T> dynamicOps, OptionalDynamic<T> optionalDynamic) {
        MutableInt mutableInt = new MutableInt(32);
        MutableInt mutableInt2 = new MutableInt(3);
        MutableInt mutableInt3 = new MutableInt(128);
        MutableBoolean mutableBoolean = new MutableBoolean(false);
        HashMap hashMap = Maps.newHashMap();
        if (optionalDynamic.result().isEmpty()) {
            mutableBoolean.setTrue();
            hashMap.put(VILLAGE, (StructureFeatureConfiguration)DEFAULTS.get((Object)VILLAGE));
        }
        optionalDynamic.get("structures").flatMap(Dynamic::getMapValues).ifSuccess(map2 -> map2.forEach((dynamic, dynamic2) -> dynamic2.getMapValues().result().ifPresent(map2 -> map2.forEach((dynamic2, dynamic3) -> {
            String string = dynamic.asString("");
            String string2 = dynamic2.asString("");
            String string3 = dynamic3.asString("");
            if ("stronghold".equals(string)) {
                mutableBoolean.setTrue();
                switch (string2) {
                    case "distance": {
                        mutableInt.setValue(WorldGenSettingsFix.getInt(string3, mutableInt.intValue(), 1));
                        return;
                    }
                    case "spread": {
                        mutableInt2.setValue(WorldGenSettingsFix.getInt(string3, mutableInt2.intValue(), 1));
                        return;
                    }
                    case "count": {
                        mutableInt3.setValue(WorldGenSettingsFix.getInt(string3, mutableInt3.intValue(), 1));
                        return;
                    }
                }
                return;
            }
            switch (string2) {
                case "distance": {
                    switch (string) {
                        case "village": {
                            WorldGenSettingsFix.setSpacing(hashMap, VILLAGE, string3, 9);
                            return;
                        }
                        case "biome_1": {
                            WorldGenSettingsFix.setSpacing(hashMap, DESERT_PYRAMID, string3, 9);
                            WorldGenSettingsFix.setSpacing(hashMap, IGLOO, string3, 9);
                            WorldGenSettingsFix.setSpacing(hashMap, JUNGLE_TEMPLE, string3, 9);
                            WorldGenSettingsFix.setSpacing(hashMap, SWAMP_HUT, string3, 9);
                            WorldGenSettingsFix.setSpacing(hashMap, PILLAGER_OUTPOST, string3, 9);
                            return;
                        }
                        case "endcity": {
                            WorldGenSettingsFix.setSpacing(hashMap, END_CITY, string3, 1);
                            return;
                        }
                        case "mansion": {
                            WorldGenSettingsFix.setSpacing(hashMap, WOODLAND_MANSION, string3, 1);
                            return;
                        }
                    }
                    return;
                }
                case "separation": {
                    if ("oceanmonument".equals(string)) {
                        StructureFeatureConfiguration structureFeatureConfiguration = hashMap.getOrDefault(OCEAN_MONUMENT, (StructureFeatureConfiguration)DEFAULTS.get((Object)OCEAN_MONUMENT));
                        int n = WorldGenSettingsFix.getInt(string3, structureFeatureConfiguration.separation, 1);
                        hashMap.put(OCEAN_MONUMENT, new StructureFeatureConfiguration(n, structureFeatureConfiguration.separation, structureFeatureConfiguration.salt));
                    }
                    return;
                }
                case "spacing": {
                    if ("oceanmonument".equals(string)) {
                        WorldGenSettingsFix.setSpacing(hashMap, OCEAN_MONUMENT, string3, 1);
                    }
                    return;
                }
            }
        }))));
        ImmutableMap.Builder builder = ImmutableMap.builder();
        builder.put(optionalDynamic.createString("structures"), optionalDynamic.createMap(hashMap.entrySet().stream().collect(Collectors.toMap(entry -> optionalDynamic.createString((String)entry.getKey()), entry -> ((StructureFeatureConfiguration)entry.getValue()).serialize(dynamicOps)))));
        if (mutableBoolean.isTrue()) {
            builder.put(optionalDynamic.createString("stronghold"), optionalDynamic.createMap((Map<Dynamic<?>, Dynamic<?>>)ImmutableMap.of(optionalDynamic.createString("distance"), optionalDynamic.createInt(mutableInt.intValue()), optionalDynamic.createString("spread"), optionalDynamic.createInt(mutableInt2.intValue()), optionalDynamic.createString("count"), optionalDynamic.createInt(mutableInt3.intValue()))));
        }
        return builder.build();
    }

    private static int getInt(String string, int n) {
        return NumberUtils.toInt((String)string, (int)n);
    }

    private static int getInt(String string, int n, int n2) {
        return Math.max(n2, WorldGenSettingsFix.getInt(string, n));
    }

    private static void setSpacing(Map<String, StructureFeatureConfiguration> map, String string, String string2, int n) {
        StructureFeatureConfiguration structureFeatureConfiguration = map.getOrDefault(string, (StructureFeatureConfiguration)DEFAULTS.get((Object)string));
        int n2 = WorldGenSettingsFix.getInt(string2, structureFeatureConfiguration.spacing, n);
        map.put(string, new StructureFeatureConfiguration(n2, structureFeatureConfiguration.separation, structureFeatureConfiguration.salt));
    }

    static final class StructureFeatureConfiguration {
        public static final Codec<StructureFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(((MapCodec)Codec.INT.fieldOf("spacing")).forGetter(structureFeatureConfiguration -> structureFeatureConfiguration.spacing), ((MapCodec)Codec.INT.fieldOf("separation")).forGetter(structureFeatureConfiguration -> structureFeatureConfiguration.separation), ((MapCodec)Codec.INT.fieldOf("salt")).forGetter(structureFeatureConfiguration -> structureFeatureConfiguration.salt)).apply((Applicative<StructureFeatureConfiguration, ?>)instance, StructureFeatureConfiguration::new));
        final int spacing;
        final int separation;
        final int salt;

        public StructureFeatureConfiguration(int n, int n2, int n3) {
            this.spacing = n;
            this.separation = n2;
            this.salt = n3;
        }

        public <T> Dynamic<T> serialize(DynamicOps<T> dynamicOps) {
            return new Dynamic<T>(dynamicOps, CODEC.encodeStart(dynamicOps, this).result().orElse(dynamicOps.emptyMap()));
        }
    }
}

