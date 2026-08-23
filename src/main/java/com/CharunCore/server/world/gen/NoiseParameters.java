package com.CharunCore.server.world.gen;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/**
 * 原版 Noises 注册表的本地版。
 * 项目无 Mojang 注册表 / Codec，所以用 json 数据 + 简单 Gson 流加。
 * json 位置：json\minecraft\worldgen\noise\*.json（60 个原版 noise 注册项）
 *
 * 调用 NoiseParameters.get("minecraft:temperature_large") 直接拿到 NoiseParameters 实例。
 * 加载顺序：先硬编码（保证有 fallback），再 json 加载（覆盖硬编码、补齐大群系变种）。
 */
public final class NoiseParameters {
    private static final Map<String, NormalNoise.NoiseParameters> PARAMETERS = new HashMap<>();

    static {
        registerHardcodedFallback();
        try {
            loadFromDirectory(new java.io.File("json/minecraft/worldgen/noise"));
        } catch (Throwable ignored) { /* json 不可用时使用硬编码值即可 */ }
    }

    private NoiseParameters() {}

    public static NormalNoise.NoiseParameters get(String name) {
        return PARAMETERS.get(name);
    }

    private static void register(String name, int firstOctave, double... amplitudes) {
        PARAMETERS.putIfAbsent(name, new NormalNoise.NoiseParameters(firstOctave, amplitudes));
    }

    /** json 加载会覆盖同名硬编码项 */
    private static void loadFromDirectory(java.io.File dir) {
        if (!dir.isDirectory()) return;
        Gson gson = new Gson();
        java.io.File[] files = dir.listFiles((d, name) -> name.endsWith(".json"));
        if (files == null) return;
        for (java.io.File f : files) {
            try {
                JsonObject obj = gson.fromJson(new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8), JsonObject.class);
                int firstOctave = obj.get("firstOctave").getAsInt();
                var amps = obj.getAsJsonArray("amplitudes");
                double[] amplitudes = new double[amps.size()];
                for (int i = 0; i < amps.size(); i++) amplitudes[i] = amps.get(i).getAsDouble();
                PARAMETERS.put("minecraft:" + f.getName().replace(".json", ""),
                        new NormalNoise.NoiseParameters(firstOctave, amplitudes));
            } catch (Throwable ignored) { /* skip bad file */ }
        }
    }

    private static void registerHardcodedFallback() {
        register("minecraft:temperature", -10, 1.5, 0.0, 1.0, 0.0, 0.0, 0.0);
        register("minecraft:vegetation", -8, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0);
        register("minecraft:continentalness", -9, 1.0, 1.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0, 1.0);
        register("minecraft:erosion", -9, 1.0, 1.0, 0.0, 1.0, 1.0);
        register("minecraft:ridge", -7, 1.0, 2.0, 1.0, 0.0, 0.0, 0.0);
        register("minecraft:offset", -3, 1.0, 1.0, 1.0, 0.0);
        register("minecraft:jagged", -16, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0);

        register("minecraft:aquifer_barrier", -3, 1.0);
        register("minecraft:aquifer_fluid_level_floodedness", -7, 1.0);
        register("minecraft:aquifer_fluid_level_spread", -5, 1.0);
        register("minecraft:aquifer_lava", -1, 1.0);

        register("minecraft:cave_entrance", -7, 0.4, 0.5, 1.0);
        register("minecraft:cave_cheese", -8, 0.5, 1.0, 2.0, 1.0, 2.0, 1.0, 0.0, 2.0, 0.0);
        register("minecraft:cave_layer", -8, 1.0);

        register("minecraft:spaghetti_2d", -7, 1.0);
        register("minecraft:spaghetti_2d_elevation", -8, 1.0);
        register("minecraft:spaghetti_2d_modulator", -11, 1.0);
        register("minecraft:spaghetti_2d_thickness", -11, 1.0);
        register("minecraft:spaghetti_3d_1", -7, 1.0);
        register("minecraft:spaghetti_3d_2", -7, 1.0);
        register("minecraft:spaghetti_3d_rarity", -11, 1.0);
        register("minecraft:spaghetti_3d_thickness", -8, 1.0);
        register("minecraft:spaghetti_roughness", -5, 1.0);
        register("minecraft:spaghetti_roughness_modulator", -8, 1.0);

        register("minecraft:noodle", -8, 1.0);
        register("minecraft:noodle_thickness", -8, 1.0);
        register("minecraft:noodle_ridge_a", -7, 1.0);
        register("minecraft:noodle_ridge_b", -7, 1.0);

        register("minecraft:pillar", -7, 1.0, 1.0);
        register("minecraft:pillar_rareness", -8, 1.0);
        register("minecraft:pillar_thickness", -8, 1.0);

        register("minecraft:ore_veininess", -8, 1.0);
        register("minecraft:ore_vein_a", -8, 1.0);
        register("minecraft:ore_vein_b", -8, 1.0);
        register("minecraft:ore_gap", -8, 1.0);

        // large biomes + 地表 noise 等 (默认占位避免 NPE，若有 json 会被覆盖)
        register("minecraft:temperature_large", -10, 1.5, 1.5, 0.0, 1.0, 0.0, 0.0, 0.0);
        register("minecraft:vegetation_large", -8, 1.0, 1.0, 1.0, 0.0, 0.0, 0.0, 0.0);
        register("minecraft:continentalness_large", -9, 1.0, 1.0, 2.0, 2.0, 2.0, 2.0, 1.0, 1.0, 1.0, 1.0);
        register("minecraft:erosion_large", -9, 1.0, 1.0, 1.0, 1.0, 1.0);

        register("minecraft:surface", 0, 1.0);
        register("minecraft:surface_secondary", 0, 1.0);
        register("minecraft:clay_bands_offset", 0, 1.0);
        register("minecraft:badlands_pillar", 0, 1.0);
        register("minecraft:badlands_pillar_roof", 0, 1.0);
        register("minecraft:badlands_surface", 0, 1.0);
        register("minecraft:iceberg_pillar", 0, 1.0);
        register("minecraft:iceberg_pillar_roof", 0, 1.0);
        register("minecraft:iceberg_surface", 0, 1.0);
        register("minecraft:surface_swamp", -2, 1.0);

        register("minecraft:calcite", -9, 1.0, 1.0, 1.0, 1.0);
        register("minecraft:gravel", -8, 1.0, 1.0, 1.0, 1.0);
        register("minecraft:powder_snow", -6, 1.0, 1.0, 1.0, 1.0);
        register("minecraft:packed_ice", -7, 1.0, 1.0, 1.0, 1.0);
        register("minecraft:ice", -4, 1.0, 1.0, 1.0, 1.0);
    }
}
