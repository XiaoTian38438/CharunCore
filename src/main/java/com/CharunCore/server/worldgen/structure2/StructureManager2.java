package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.worldgen.WorldGenLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureManager2 {

    private static final StructureManager2 INSTANCE = new StructureManager2();

    // 按维度分层存储：主世界/下界/末地结构互不干扰。
    // 曾共用一张 (chunkX,chunkZ) 表 → 下界/末地生成时命中主世界 start → 结构泄漏到错误维度。
    private final Map<DimensionType, Map<Long, List<StructureStartLike>>> chunkStarts =
            new java.util.EnumMap<>(DimensionType.class);
    private final StructureTemplateManager templateManager = StructureTemplateManager.getInstance();

    private StructureManager2() {
        for (DimensionType d : DimensionType.values()) {
            chunkStarts.put(d, new HashMap<>());
        }
    }

    public static StructureManager2 getInstance() {
        return INSTANCE;
    }

    public void clear() {
        for (Map<Long, List<StructureStartLike>> byChunk : chunkStarts.values()) byChunk.clear();
    }

    public List<StructureStartLike> getStartsForChunk(DimensionType dim, int chunkX, int chunkZ) {
        return chunkStarts.get(dim).getOrDefault(chunkKey(chunkX, chunkZ), new ArrayList<>());
    }

    /** 注册结构 start；同维度同 id 同中心区块去重（防区块重载后重复放置双份结构） */
    public void addStart(DimensionType dim, int chunkX, int chunkZ, StructureStartLike start) {
        List<StructureStartLike> list = chunkStarts.get(dim)
                .computeIfAbsent(chunkKey(chunkX, chunkZ), k -> new ArrayList<>());
        for (StructureStartLike s : list) {
            if (s.getStructureId().equals(start.getStructureId())
                    && s.getChunkX() == start.getChunkX()
                    && s.getChunkZ() == start.getChunkZ()) {
                return;
            }
        }
        list.add(start);
    }

    public List<StructureStartLike> getStartsIntersecting(DimensionType dim, int chunkX, int chunkZ) {
        List<StructureStartLike> result = new ArrayList<>();
        int chunkMinX = chunkX << 4;
        int chunkMinZ = chunkZ << 4;
        int chunkMaxX = chunkMinX + 15;
        int chunkMaxZ = chunkMinZ + 15;
        for (List<StructureStartLike> starts : chunkStarts.get(dim).values()) {
            for (StructureStartLike start : starts) {
                BoundingBox bb = start.getBoundingBox();
                if (bb == null) continue;
                if (bb.maxX >= chunkMinX && bb.minX <= chunkMaxX
                    && bb.maxZ >= chunkMinZ && bb.minZ <= chunkMaxZ) {
                    result.add(start);
                }
            }
        }
        return result;
    }

    public void placeStructuresForChunk(WorldGenLevel level, DimensionType dim, int chunkX, int chunkZ) {
        List<StructureStartLike> starts = getStartsIntersecting(dim, chunkX, chunkZ);
        for (StructureStartLike start : starts) {
            start.placeInChunk(level, templateManager, chunkX, chunkZ);
        }
    }

    private static long chunkKey(int x, int z) {
        return ((long) x << 32) | (z & 0xFFFFFFFFL);
    }

    public StructureTemplateManager getTemplateManager() {
        return templateManager;
    }
}
