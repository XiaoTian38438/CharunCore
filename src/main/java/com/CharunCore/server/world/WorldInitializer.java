package com.CharunCore.server.world;

import org.cloudburstmc.nbt.NBTOutputStream;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.io.*;
import java.util.zip.GZIPOutputStream;

public class WorldInitializer {

    public static void initWorld(String worldDir) {
        File regionDir = new File(worldDir, "region");
        File playerDataDir = new File(worldDir, "playerdata");
        File levelDatFile = new File(worldDir, "level.dat");

        regionDir.mkdirs();
        playerDataDir.mkdirs();

        if (!levelDatFile.exists()) {
            createLevelDat(levelDatFile);
            System.out.println("[世界] 已生成新的 level.dat");
        }

        System.out.println("[世界] 世界文件夹已初始化: " + worldDir);
    }

    private static void createLevelDat(File file) {
        try {
            NbtMap overworldGen = NbtMap.builder()
                .putString("type", "minecraft:noise")
                .putString("settings", "minecraft:overworld")
                .putCompound("biome_source", NbtMap.builder()
                    .putString("type", "minecraft:multi_noise")
                    .putString("preset", "minecraft:overworld")
                    .build())
                .build();

            NbtMap overworldDim = NbtMap.builder()
                .putString("type", "minecraft:overworld")
                .putCompound("generator", overworldGen)
                .build();

            NbtMap theNetherGen = NbtMap.builder()
                .putString("type", "minecraft:noise")
                .putString("settings", "minecraft:nether")
                .putCompound("biome_source", NbtMap.builder()
                    .putString("type", "minecraft:multi_noise")
                    .putString("preset", "minecraft:nether")
                    .build())
                .build();

            NbtMap theNetherDim = NbtMap.builder()
                .putString("type", "minecraft:the_nether")
                .putCompound("generator", theNetherGen)
                .build();

            NbtMap theEndGen = NbtMap.builder()
                .putString("type", "minecraft:noise")
                .putString("settings", "minecraft:end")
                .putCompound("biome_source", NbtMap.builder()
                    .putString("type", "minecraft:the_end")
                    .build())
                .build();

            NbtMap theEndDim = NbtMap.builder()
                .putString("type", "minecraft:the_end")
                .putCompound("generator", theEndGen)
                .build();

            NbtMap dimensions = NbtMap.builder()
                .putCompound("minecraft:overworld", overworldDim)
                .putCompound("minecraft:the_nether", theNetherDim)
                .putCompound("minecraft:the_end", theEndDim)
                .build();

            NbtMap dataTag = NbtMap.builder()
                .putByte("allowCommands", (byte) 1)
                .putByte("hardcore", (byte) 0)
                .putString("LevelName", "world")
                .putLong("LastPlayed", System.currentTimeMillis())
                .putInt("GameType", 0)
                .putInt("SpawnX", 0)
                .putInt("SpawnY", 64)
                .putInt("SpawnZ", 0)
                .putFloat("SpawnAngle", 0.0f)
                .putByte("raining", (byte) 0)
                .putByte("thundering", (byte) 0)
                .putInt("rainTime", 0)
                .putInt("thunderTime", 0)
                .putBoolean("initialized", true)
                .putInt("version", 19133)
                .putInt("DataVersion", 4671)
                .putInt("clearWeatherTime", 10000)
                .putLong("Time", 6000)
                .putLong("DayTime", 6000)
                .putLong("RandomSeed", 1234567L)
                .putByte("MapFeatures", (byte) 1)
                .putString("Difficulty", "2")
                .putByte("DifficultyLocked", (byte) 0)
                .putCompound("GameRules", NbtMap.builder()
                    .putString("doFireTick", "true")
                    .putString("doMobSpawning", "true")
                    .putString("doDaylightCycle", "true")
                    .putString("doWeatherCycle", "true")
                    .putString("keepInventory", "false")
                    .putString("mobGriefing", "true")
                    .putString("naturalRegeneration", "true")
                    .putString("commandBlockOutput", "true")
                    .putString("reducedDebugInfo", "false")
                    .putString("sendCommandFeedback", "true")
                    .putString("showDeathMessages", "true")
                    .putString("spawnRadius", "10")
                    .putString("doInsomnia", "true")
                    .putString("doImmediateRespawn", "false")
                    .putString("announceAdvancements", "true")
                    .build())
                .putDouble("BorderSize", 60000000.0)
                .putDouble("BorderCenterX", 0.0)
                .putDouble("BorderCenterZ", 0.0)
                .putList("ServerBrands", NbtType.STRING, java.util.Collections.emptyList())
                .putByte("WasModded", (byte) 0)
                .putCompound("WorldGenSettings", NbtMap.builder()
                    .putLong("seed", 1234567L)
                    .putBoolean("generate_features", true)
                    .putBoolean("bonus_chest", false)
                    .putCompound("dimensions", dimensions)
                    .build())
                .build();

            NbtMap levelDat = NbtMap.builder()
                .putCompound("Data", dataTag)
                .build();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (NBTOutputStream nbtOut = new NBTOutputStream(new DataOutputStream(baos))) {
                nbtOut.writeTag(levelDat);
            }
            byte[] nbtBytes = baos.toByteArray();

            try (FileOutputStream fos = new FileOutputStream(file);
                 GZIPOutputStream gzos = new GZIPOutputStream(fos)) {
                gzos.write(nbtBytes);
            }

        } catch (Exception e) {
            System.err.println("[世界] 创建 level.dat 失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
