package com.CharunCore.server.worldgen;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.structure2.EndCityPieces;
import com.CharunCore.server.worldgen.structure2.NetherFortressPieces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** #52 冒烟：要塞/末地城件图生成统计 + 完整构建无异常。 */
public final class StructureSmokeTest {
    public static void main(String[] args) {
        BlockStateHelper.init();
        long seed = 1234567L;

        System.out.println("=== NETHER FORTRESS ===");
        for (int i = 0; i < 5; i++) {
            long sSeed = seed + i * 7919L;
            List<NetherFortressPieces.Piece> pieces =
                NetherFortressPieces.generate(sSeed, 8, 8);
            Map<String, Integer> kinds = new HashMap<>();
            for (NetherFortressPieces.Piece p : pieces) {
                kinds.merge(p.getClass().getSimpleName(), 1, Integer::sum);
            }
            var box = NetherFortressPieces.totalBox(pieces);
            System.out.printf("seed+%d: pieces=%d y=[%d..%d] xSpan=%d zSpan=%d %s%n",
                i, pieces.size(), box.minY, box.maxY,
                box.maxX - box.minX + 1, box.maxZ - box.minZ + 1, kinds);
        }

        System.out.println();
        System.out.println("=== END CITY ===");
        for (int i = 0; i < 5; i++) {
            long sSeed = seed + i * 104729L;
            List<EndCityPieces.CityPiece> pieces =
                EndCityPieces.generate(sSeed, 0, 64, 0);
            int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
            int minZ = Integer.MAX_VALUE, maxZ = Integer.MIN_VALUE;
            int maxY = Integer.MIN_VALUE;
            Map<String, Integer> kinds = new HashMap<>();
            for (EndCityPieces.CityPiece p : pieces) {
                var b = p.box();
                minX = Math.min(minX, b.minX); maxX = Math.max(maxX, b.maxX);
                minZ = Math.min(minZ, b.minZ); maxZ = Math.max(maxZ, b.maxZ);
                maxY = Math.max(maxY, b.maxY);
                kinds.merge(String.valueOf(p.hashCode() & 0), 1, Integer::sum);
            }
            System.out.printf("seed+%d: pieces=%d span=%dx%d top=%d %s%n",
                i, pieces.size(), maxX - minX + 1, maxZ - minZ + 1, maxY - 64, kinds);
        }

        // 完整构建（3x3 窗口内）不抛异常
        System.out.println();
        System.out.println("=== BUILD SMOKE ===");
        DensityRouterChunkGenerator gen = new DensityRouterChunkGenerator(seed, DimensionType.THE_END);
        Chunk c = gen.generateBaseOnly(0, 0);
        java.util.Map<Long, Chunk> win = new HashMap<>();
        for (int dx = -1; dx <= 1; dx++)
            for (int dz = -1; dz <= 1; dz++)
                win.put(WorldGenLevel.key(dx, dz), gen.generateBaseOnly(dx, dz));
        WorldGenLevel level = new WorldGenLevel(0, 0, win, seed, 63, gen.MIN_Y, 128);
        List<EndCityPieces.CityPiece> city = EndCityPieces.generate(seed, 0, 70, 0);
        EndCityPieces.placeInChunk(level, city, 0, 0);
        System.out.println("end city built OK (" + city.size() + " pieces)");

        List<NetherFortressPieces.Piece> fort = NetherFortressPieces.generate(seed, 0, 0);
        NetherFortressPieces.placeInChunk(level, fort,
            level.getCenterCX() * 16 - 16, level.getCenterCZ() * 16 - 16, 1L);
        System.out.println("fortress built OK (" + fort.size() + " pieces)");
    }
}
