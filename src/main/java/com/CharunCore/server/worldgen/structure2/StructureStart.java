package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.worldgen.WorldGenLevel;

import java.util.List;

public class StructureStart implements StructureStartLike {
    private final String structureId;
    private final int chunkX;
    private final int chunkZ;
    private final List<PoolElementStructurePiece> pieces;
    private int references;

    public StructureStart(String structureId, int chunkX, int chunkZ,
                          List<PoolElementStructurePiece> pieces) {
        this.structureId = structureId;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.pieces = pieces;
        this.references = 0;
    }

    public String getStructureId() { return structureId; }
    public int getChunkX() { return chunkX; }
    public int getChunkZ() { return chunkZ; }
    public List<PoolElementStructurePiece> getPieces() { return pieces; }
    public int getReferences() { return references; }
    public void addReference() { references++; }

    public boolean isValid() { return pieces != null && !pieces.isEmpty(); }

    public BoundingBox getBoundingBox() {
        if (pieces == null || pieces.isEmpty()) return null;
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
        for (PoolElementStructurePiece p : pieces) {
            BoundingBox bb = p.getBoundingBox();
            minX = Math.min(minX, bb.minX); maxX = Math.max(maxX, bb.maxX);
            minY = Math.min(minY, bb.minY); maxY = Math.max(maxY, bb.maxY);
            minZ = Math.min(minZ, bb.minZ); maxZ = Math.max(maxZ, bb.maxZ);
        }
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void placeInChunk(WorldGenLevel level, StructureTemplateManager manager, int chunkX, int chunkZ) {
        int chunkMinX = chunkX << 4;
        int chunkMinZ = chunkZ << 4;
        int chunkMaxX = chunkMinX + 15;
        int chunkMaxZ = chunkMinZ + 15;
        for (PoolElementStructurePiece piece : pieces) {
            BoundingBox bb = piece.getBoundingBox();
            if (bb.maxX < chunkMinX || bb.minX > chunkMaxX) continue;
            if (bb.maxZ < chunkMinZ || bb.minZ > chunkMaxZ) continue;
            piece.place(level, manager);
        }
    }
}
