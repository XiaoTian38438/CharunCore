package com.CharunCore.server.worldgen.structure2;

/** 结构放置的公共视图（random_spread / concentric_rings）。 */
public interface StructurePlacementLike {
    boolean isStructureChunk(long levelSeed, int chunkX, int chunkZ);
}
