package com.CharunCore.server.worldgen.structure2;

import java.util.List;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.WorldGenLevel;

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
        // 原版 getBeardContribution: d3 = -(dy+0.5) / (l * sqrt2), 无额外衰减系数
        double d3 = -(dy + 0.5) / (l * 1.4142135623730951);
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

        // B4: 桩基 —— 核函数(exp 衰减 12 格)拉不平大落差(山坡村庄 piece 高于地形 15~25 格),
        // 这是"村庄悬空/地形被切断"的残留根因。对 beard_thin/beard_box 片段底部每格一个
        // 桩位向下填石柱直到触地(<=28 格), 观感为自然地基, 彻底消除悬空。
        for (Rigid r : rigids) {
            if (!r.adjustment.equals("beard_thin") && !r.adjustment.equals("beard_box")) continue;
            int bottom = r.box.minY + r.groundLevelDelta - 1;
            int px0 = Math.max(r.box.minX, baseX), px1 = Math.min(r.box.maxX, baseX + 15);
            int pz0 = Math.max(r.box.minZ, baseZ), pz1 = Math.min(r.box.maxZ, baseZ + 15);
            for (int px = px0; px <= px1; px++) {
                for (int pz = pz0; pz <= pz1; pz++) {
                    for (int y = bottom; y > Math.max(minY, bottom - 28); y--) {
                        int cur = level.getBlock(px, y, pz);
                        if (cur == 0) {
                            level.setBlock(px, y, pz, y < 0 ? deepslate : stone);
                            continue;
                        }
                        String cn = BlockStateHelper.getName(cur);
                        if (cn != null && (cn.equals("water") || cn.equals("lava"))) {
                            level.setBlock(px, y, pz, y < 0 ? deepslate : stone);
                            continue;
                        }
                        break; // 触地(任意实体方块) —— 桩到此为止
                    }
                }
            }
        }

        // Bug#2: 回填的地基要"地表化" —— 原版 Beardifier 在密度层工作, 表面规则随后
        // 正常长草; 我们是方块后处理, 直接填 stone 会留下一圈灰色石壁截断观感。
        // 记录回填格, 收尾时给顶面 1 层草方块、下面 2 层泥土。
        java.util.HashSet<Long> filled = new java.util.HashSet<>();
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                for (int y = yMin; y <= yMax; y++) {
                    double d = compute(x, y, z, rigids, junctions);
                    if (d > 0.08) {
                        int cur = level.getBlock(x, y, z);
                        if (cur == 0) {
                            level.setBlock(x, y, z, y < 0 ? deepslate : stone);
                            filled.add(((long) (x & 0x3FFFFFF) << 38) | ((long) (z & 0x3FFFFFF) << 12) | (y & 0xFFFL));
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
        if (!filled.isEmpty()) {
            int grass = BlockStateHelper.getDefault("grass_block");
            int dirt = BlockStateHelper.getDefault("dirt");
            for (long pk : filled) {
                int x = (int) (pk >> 38) & 0x3FFFFFF;
                int z = (int) (pk >> 12) & 0x3FFFFFF;
                int y = (int) pk & 0xFFF;
                boolean aboveFilled = filled.contains((((long) (x & 0x3FFFFFF)) << 38)
                    | (((long) (z & 0x3FFFFFF)) << 12) | ((y + 1) & 0xFFF));
                if (aboveFilled) continue;
                int above = level.getBlock(x, y + 1, z);
                String an = BlockStateHelper.getName(above);
                boolean airAbove = above == 0 || (an != null && (an.endsWith("air") || an.endsWith("leaves")
                    || an.endsWith("log") || an.endsWith("water")));
                if (airAbove) {
                    level.setBlock(x, y, z, grass);
                    // 顶面下面两层回填改泥土, 过渡自然
                    for (int dy = 1; dy <= 2; dy++) {
                        long belowKey = (((long) (x & 0x3FFFFFF)) << 38)
                            | (((long) (z & 0x3FFFFFF)) << 12) | ((y - dy) & 0xFFF);
                        if (filled.contains(belowKey)) {
                            level.setBlock(x, y - dy, z, dirt);
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
