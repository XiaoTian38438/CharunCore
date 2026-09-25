package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.world.gen.LegacyRandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * 过程化结构（NetherFortressPieces / EndCityPieces）的 StructureStartLike 适配：
 * 中央注册一次，跨区块按包围盒重复构建（WorldGenLevel 窗口外写入自动跳过）。
 */
public final class ProceduralStructureStart implements StructureStartLike {

    private final String structureId;
    private final int chunkX;
    private final int chunkZ;
    private final List<BuildablePiece> pieces;
    private BoundingBox totalBox;

    private ProceduralStructureStart(String structureId, int chunkX, int chunkZ,
                                     List<BuildablePiece> pieces) {
        this.structureId = structureId;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.pieces = pieces;
    }

    public static ProceduralStructureStart fortress(long structureSeed, int startBlockX, int startBlockZ,
                                                    String id, int chunkX, int chunkZ) {
        List<NetherFortressPieces.Piece> fp = NetherFortressPieces.generate(structureSeed,
            startBlockX + 2, startBlockZ + 2);
        List<BuildablePiece> refs = new ArrayList<>();
        for (NetherFortressPieces.Piece p : fp) {
            refs.add(new BuildablePiece() {
                @Override public BoundingBox box() { return p.box; }
                @Override public void build(WorldGenLevel level) {
                    p.build(level, new LegacyRandomSource(
                        ((long) level.getCenterCX() * 341873128712L)
                            ^ ((long) level.getCenterCZ() * 132897987541L)));
                }
            });
        }
        return new ProceduralStructureStart(id, chunkX, chunkZ, refs);
    }

    public static ProceduralStructureStart endCity(long structureSeed, int startBlockX, int startY,
                                                   int startBlockZ, String id, int chunkX, int chunkZ) {
        List<EndCityPieces.CityPiece> cp = EndCityPieces.generate(structureSeed,
            startBlockX, startY, startBlockZ);
        List<BuildablePiece> refs = new ArrayList<>(cp);
        return new ProceduralStructureStart(id, chunkX, chunkZ, refs);
    }

    /** Bug27: 主世界要塞（原版 StrongholdPieces 分件系统）。 */
    public static ProceduralStructureStart stronghold(long structureSeed, int startX, int startY,
                                                      int startZ, String id, int chunkX, int chunkZ) {
        List<StrongholdPieces.Piece> sp = StrongholdPieces.generate(structureSeed,
            startX, startY, startZ);
        List<BuildablePiece> refs = new ArrayList<>();
        for (StrongholdPieces.Piece p : sp) {
            refs.add(new BuildablePiece() {
                @Override public BoundingBox box() { return p.box; }
                @Override public void build(WorldGenLevel level) {
                    p.build(level, new LegacyRandomSource(
                        level.getSeed() ^ ((long) level.getCenterCX() * 341873128712L)
                            ^ ((long) level.getCenterCZ() * 132897987541L)));
                }
            });
        }
        return new ProceduralStructureStart(id, chunkX, chunkZ, refs);
    }

    /** Bug27: 废弃矿井分件系统(原版 MineshaftPieces)。 */
    public static ProceduralStructureStart mineshaft(long structureSeed, int startBlockX, int startBlockZ,
                                                     boolean mesa, String id, int chunkX, int chunkZ) {
        List<MineshaftPieces.Piece> mp = MineshaftPieces.generate(structureSeed,
            startBlockX, startBlockZ, mesa);
        List<BuildablePiece> refs = new ArrayList<>(mp);
        return new ProceduralStructureStart(id, chunkX, chunkZ, refs);
    }

    @Override public String getStructureId() { return structureId; }
    @Override public int getChunkX() { return chunkX; }
    @Override public int getChunkZ() { return chunkZ; }
    @Override public boolean isValid() { return !pieces.isEmpty(); }

    @Override public BoundingBox getBoundingBox() {
        if (totalBox == null) {
            int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, minZ = Integer.MAX_VALUE;
            int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE, maxZ = Integer.MIN_VALUE;
            for (BuildablePiece p : pieces) {
                BoundingBox bb = p.box();
                minX = Math.min(minX, bb.minX); maxX = Math.max(maxX, bb.maxX);
                minY = Math.min(minY, bb.minY); maxY = Math.max(maxY, bb.maxY);
                minZ = Math.min(minZ, bb.minZ); maxZ = Math.max(maxZ, bb.maxZ);
            }
            totalBox = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
        }
        return totalBox;
    }

    @Override
    public void placeInChunk(WorldGenLevel level, StructureTemplateManager manager,
                             int chunkX, int chunkZ) {
        int chunkMinX = chunkX << 4, chunkMaxX = chunkMinX + 15;
        int chunkMinZ = chunkZ << 4, chunkMaxZ = chunkMinZ + 15;
        for (BuildablePiece p : pieces) {
            BoundingBox bb = p.box();
            if (bb.maxX < chunkMinX || bb.minX > chunkMaxX) continue;
            if (bb.maxZ < chunkMinZ || bb.minZ > chunkMaxZ) continue;
            p.build(level);
        }
    }
}
