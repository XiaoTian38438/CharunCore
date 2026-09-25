package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.worldgen.WorldGenLevel;

/** 结构 start 的公共视图：模板池结构（StructureStart）与过程化结构共用。 */
public interface StructureStartLike {
    String getStructureId();

    int getChunkX();

    int getChunkZ();

    boolean isValid();

    BoundingBox getBoundingBox();

    void placeInChunk(WorldGenLevel level, StructureTemplateManager manager, int chunkX, int chunkZ);
}
