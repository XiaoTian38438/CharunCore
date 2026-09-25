package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.utils.RegistryHelper;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.world.gen.XoroshiroRandomSource;
import com.CharunCore.server.worldgen.biome.Climate;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 原版 ConcentricRingsStructurePlacement + ChunkGeneratorStructureState.generateRingPositions 移植。
 * 环带位置由世界种子确定；每点在 225×225 方块范围（4 格步长）内按原版 findBiomeHorizontal(bl=false)
 * 的水库抽样寻找偏好群系，找不到则用原始网格点。位置缓存按世界种子全局共享。
 */
public final class ConcentricRingsStructurePlacement implements StructurePlacementLike {

    public interface RingBiomeResolver {
        int biomeAtQuart(int quartX, int quartY, int quartZ);
    }

    private static volatile RingBiomeResolver resolver;

    public static void setBiomeResolver(RingBiomeResolver r) { resolver = r; }

    private final int count;
    private final int distance;
    private final int spread;
    private final int salt;
    private final String preferredBiomesTag;
    private Set<Integer> preferredBiomeIds;

    private static final Map<Long, Set<Long>> CACHE = new ConcurrentHashMap<>();

    public ConcentricRingsStructurePlacement(int count, int distance, int spread, int salt,
                                             String preferredBiomesTag) {
        this.count = count;
        this.distance = distance;
        this.spread = spread;
        this.salt = salt;
        this.preferredBiomesTag = preferredBiomesTag;
    }

    public int getCount() { return count; }

    private static long chunkKey(int x, int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }

    @Override
    public boolean isStructureChunk(long levelSeed, int chunkX, int chunkZ) {
        return positionsFor(levelSeed).contains(chunkKey(chunkX, chunkZ));
    }

    /** 返回该维度全部要塞所在区块坐标（供 /locate 等使用）。 */
    public List<int[]> ringPositions(long levelSeed) {
        Set<Long> keys = positionsFor(levelSeed);
        List<int[]> out = new ArrayList<>(keys.size());
        for (long k : keys) out.add(new int[]{(int) (k >> 32), (int) (long) (k & 0xFFFFFFFFL)});
        return out;
    }

    private synchronized Set<Long> positionsFor(long seed) {
        return CACHE.computeIfAbsent(seed, this::generate);
    }

    private Set<Long> generate(long seed) {
        Set<Long> out = new HashSet<>();
        if (count == 0) return out;
        RandomSource rnd = new XoroshiroRandomSource(seed);
        double angle = rnd.nextDouble() * Math.PI * 2.0;
        int inRing = 0;
        int ringIndex = 0;
        int curSpread = spread;
        for (int i = 0; i < count; i++) {
            double radius = (double) (4 * distance + distance * ringIndex * 6)
                + (rnd.nextDouble() - 0.5) * ((double) distance * 2.5);
            int x = (int) Math.round(Math.cos(angle) * radius);
            int z = (int) Math.round(Math.sin(angle) * radius);
            RandomSource fork = rnd.fork();
            int[] snapped = findBiomeHorizontal(x * 16 + 8, z * 16 + 8, 112, fork);
            if (snapped != null) {
                out.add(chunkKey(snapped[0] >> 4, snapped[1] >> 4));
            } else {
                out.add(chunkKey(x, z));
            }
            angle += Math.PI * 2 / (double) curSpread;
            if (++inRing == curSpread) {
                inRing = 0;
                curSpread += 2 * curSpread / (++ringIndex + 1);
                curSpread = Math.min(curSpread, count - i);
                angle += rnd.nextDouble() * Math.PI * 2.0;
            }
        }
        return out;
    }

    /** 原版 BiomeSource.findBiomeHorizontal（bl=false：全区域 4 格网格 + 水库抽样）。 */
    private int[] findBiomeHorizontal(int blockX, int blockZ, int rangeBlocks, RandomSource rnd) {
        RingBiomeResolver r = resolver;
        if (r == null) return null;
        Set<Integer> biased = preferredBiomes();
        if (biased.isEmpty()) return null;
        int q7 = blockX >> 2;
        int q8 = blockZ >> 2;
        int q9 = rangeBlocks >> 2;
        int[] pair = null;
        int seen = 0;
        for (int i = q9; i <= q9; i += 1) {
            for (int n12 = -i; n12 <= i; n12 += 1) {
                for (int j = -i; j <= i; j += 1) {
                    int qx = q7 + j;
                    int qz = q8 + n12;
                    if (!biased.contains(r.biomeAtQuart(qx, 0, qz))) continue;
                    if (pair == null || rnd.nextInt(seen + 1) == 0) {
                        pair = new int[]{qx << 2, qz << 2};
                    }
                    seen++;
                }
            }
        }
        return pair;
    }

    private synchronized Set<Integer> preferredBiomes() {
        if (preferredBiomeIds != null) return preferredBiomeIds;
        Set<Integer> ids = new HashSet<>();
        try (FileInputStream fis = new FileInputStream(
                "json/minecraft/tags/worldgen/biome/" + strip(preferredBiomesTag) + ".json");
             InputStreamReader reader = new InputStreamReader(fis, StandardCharsets.UTF_8)) {
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonArray values = json.getAsJsonArray("values");
            for (JsonElement e : values) {
                String name = e.getAsString();
                if (name.startsWith("#")) continue;
                name = strip(name);
                int id = RegistryHelper.biomeNameToId(name);
                if (id >= 0) ids.add(id);
            }
        } catch (Exception ignored) {
        }
        preferredBiomeIds = ids;
        return ids;
    }

    private static String strip(String s) {
        return s.startsWith("minecraft:") ? s.substring(10) : s;
    }
}
