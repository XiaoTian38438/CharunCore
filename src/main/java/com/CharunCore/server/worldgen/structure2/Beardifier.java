package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.WorldGenLevel;

import java.util.List;

/** Bug51: 原版 Beardifier —— 结构(村庄/掠夺者前哨/远古城市/试炼密室等)对地形的增密修正。
 *  曾无任何 terrain adaptation -> 结构下方没有"地基", 村庄房屋悬空/嵌地、同一高度。
 *  本实现按原版 Beardifier 的核函数(exp(-len^2/16) 衰减)以方块后处理方式应用:
 *  d>0 回填石/deepslate(地形向结构底部隆起), d<0 挖开方块(解除山体掩埋)。
 *  在 carveOverworld 之后、placeStructures2 之前调用(结构随后覆盖自己的内部空间)。 */
public final class Beardifier {

    private Beardifier() {}

    public static final class Rigid {
        final BoundingBox box;
        final String adjustment;   // none/beard_thin/beard_box/encapsulate/bury
        final int groundLevelDelta;
        public Rigid(BoundingBox box, String adjustment, int groundLevelDelta) {
            this.box = box; this.adjustment = adjustment; this.groundLevelDelta = groundLevelDelta;
        }
    }

    public record Junction(int x, int y, int z) {}

    /** 与原版一致: 中心距平方 -> exp(-d2/16) 的 12 格衰减核。 */
    private static double kernel(int dx, int dy, int dz) {
        double d2 = dx * (double) dx + (dy + 0.5) * (dy + 0.5) + dz * (double) dz;
        if (d2 > 12.0 * 12.0) return 0.0;
        return Math.exp(-d2 / 16.0);
    }

    private static double beardContribution(int dx, int dy, int dz, int n8) {
        double l2 = dx * (double) dx + (dy + 0.5) * (dy + 0.5) + dz * (double) dz;
        if (l2 > 12.0 * 12.0) return 0.0;
        double l = Math.sqrt(l2);
        double d3 = -(dy + 0.5) / (l * 1.4142135623730951) * 0.5;
        return d3 * kernel(dx, dy, dz);
    }

    private static double buryContribution(double dx, double dy, double dz) {
        double d = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (d <= 0.0) return 1.0;
        if (d >= 6.0) return 0.0;
        return 1.0 - d / 6.0;
    }

    /** 计算世界坐标 (x,y,z) 的总修正值(原版 Beardifier.compute)。 */
    public static double compute(int x, int y, int z, List<Rigid> rigids, List<Junction> junctions) {
        double d = 0.0;
        for (Rigid r : rigids) {
            BoundingBox bb = r.box;
            int dx = Math.max(0, Math.max(bb.minX - x, x - bb.maxX));
            int dz = Math.max(0, Math.max(bb.minZ - z, z - bb.maxZ));
            int gy = bb.minY + r.groundLevelDelta;
            int dy = y - gy;
            int n9;
            switch (r.adjustment) {
                case "bury", "beard_thin" -> n9 = dy;
                case "beard_box" -> n9 = Math.max(0, Math.max(gy - y, y - bb.maxY));
                case "encapsulate" -> n9 = Math.max(0, Math.max(bb.minY - y, y - bb.maxY));
                default -> n9 = 0;
            }
            double c = switch (r.adjustment) {
                case "bury" -> buryContribution(dx, n9 / 2.0, dz);
                case "beard_thin", "beard_box" -> beardContribution(dx, dy, dz, n9) * 0.8;
                case "encapsulate" -> buryContribution(dx / 2.0, n9 / 2.0, dz / 2.0) * 0.8;
                default -> 0.0;
            };
            d += c;
        }
        for (Junction j : junctions) {
            int dx = x - j.x, dy = y - j.y, dz = z - j.z;
            d += beardContribution(dx, dy, dz, dy) * 0.4;
        }
        return d;
    }

    /** 对整个区块应用 Beardifier 后处理。rigids/junctions 来自周围 3x3 已注册 start。 */
    public static void applyToChunk(WorldGenLevel level, Chunk chunk, int chunkX, int chunkZ,
                                    List<Rigid> rigids, List<Junction> junctions) {
        if (rigids.isEmpty() && junctions.isEmpty()) return;
        int baseX = chunkX << 4, baseZ = chunkZ << 4;
        int stone = BlockStateHelper.getDefault("stone");
        int deepslate = BlockStateHelper.getDefault("deepslate");
        int minY = level.getMinY(), maxY = level.getMinY() + level.getHeight() - 1;

        // 受影响包围盒 = 全部 rigid 核范围并集, 裁剪到本区块
        int minX = baseX, maxX = baseX + 15, minZ = baseZ, maxZ = baseZ + 15;
        int yMin = maxY, yMax = minY;
        for (Rigid r : rigids) {
            minX = Math.min(minX, r.box.minX - 12); maxX = Math.max(maxX, r.box.maxX + 12);
            minZ = Math.min(minZ, r.box.minZ - 12); maxZ = Math.max(maxZ, r.box.maxZ + 12);
            yMin = Math.min(yMin, Math.max(minY, r.box.minY + r.groundLevelDelta - 12));
            yMax = Math.max(yMax, Math.min(maxY, r.box.maxY + 12));
        }
        for (Junction j : junctions) {
            yMin = Math.min(yMin, Math.max(minY, j.y - 12));
            yMax = Math.max(yMax, Math.min(maxY, j.y + 12));
        }
        minX = Math.max(minX, baseX); maxX = Math.min(maxX, baseX + 15);
        minZ = Math.max(minZ, baseZ); maxZ = Math.min(maxZ, baseZ + 15);

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = yMin; y <= yMax; y++) {
                    double d = compute(x, y, z, rigids, junctions);
                    if (d > 0.08) {
                        int cur = level.getBlock(x, y, z);
                        if (cur == 0) {
                            level.setBlock(x, y, z, y < 0 ? deepslate : stone);
                        }
                    } else if (d < -0.08) {
                        int cur = level.getBlock(x, y, z);
                        if (cur != 0 && isCarvable(cur)) {
                            level.setBlock(x, y, z, 0);
                        }
                    }
                }
            }
        }
    }

    private static boolean isCarvable(int state) {
        String n = BlockStateHelper.getName(state);
        if (n == null) return false;
        if (n.startsWith("minecraft:")) n = n.substring(10);
        return n.equals("stone") || n.equals("deepslate") || n.equals("dirt")
            || n.equals("grass_block") || n.equals("gravel") || n.equals("sand")
            || n.equals("sandstone") || n.equals("tuff") || n.equals("granite")
            || n.equals("diorite") || n.equals("andesite") || n.equals("netherrack")
            || n.equals("end_stone") || n.equals("water") || n.equals("lava");
    }
}
