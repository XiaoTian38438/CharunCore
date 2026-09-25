package com.CharunCore.server.worldgen.feature;

import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.entity.EndCrystalEntity;
import com.CharunCore.server.world.entity.EntityManager;
import com.CharunCore.server.worldgen.WorldGenLevel;

import java.util.ArrayList;
import java.util.List;

/** Bug21: 末地主岛黑曜石柱(原版 SpikeFeature/EndSpikeFeature 忠实移植)。
 *  10 根柱按种子环绕原点 42 格, 半径 2+n/3, 高 76+3n, 部分带铁栏杆笼+顶置末影水晶。 */
public final class EndSpikeFeature {

    private EndSpikeFeature() {}

    public static final class Spike {
        public final int centerX, centerZ, radius, height;
        public final boolean guarded;
        Spike(int cx, int cz, int r, int h, boolean g) {
            this.centerX = cx; this.centerZ = cz; this.radius = r; this.height = h; this.guarded = g;
        }
    }

    /** 与原版一致: seed -> nextLong()&0xFFFF 打乱 0..9, 环绕 42 格余弦分布。 */
    public static List<Spike> createSpikes(long worldSeed) {
        com.CharunCore.server.world.gen.RandomSource seedRng =
            new com.CharunCore.server.world.gen.XoroshiroRandomSource(worldSeed);
        long l = seedRng.nextLong() & 0xFFFFL;
        // Util.toShuffledList(IntStream 0..9, RandomSource.create(l))
        List<Integer> idx = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) idx.add(i);
        com.CharunCore.server.world.gen.RandomSource shuffler =
            new com.CharunCore.server.world.gen.XoroshiroRandomSource(l);
        for (int i = idx.size() - 1; i > 0; i--) {
            int j = shuffler.nextInt(i + 1);
            Integer t = idx.set(i, idx.get(j));
            idx.set(j, t);
        }
        List<Spike> out = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            int cx = (int) Math.floor(42.0 * Math.cos(2.0 * (-Math.PI + Math.PI / 10.0 * i)));
            int cz = (int) Math.floor(42.0 * Math.sin(2.0 * (-Math.PI + Math.PI / 10.0 * i)));
            int n3 = idx.get(i);
            int radius = 2 + n3 / 3;
            int height = 76 + n3 * 3;
            boolean guarded = n3 == 1 || n3 == 2;
            out.add(new Spike(cx, cz, radius, height, guarded));
        }
        return out;
    }

    /** 在区块生成期调用: 只放置包围盒与该 chunk 相交的柱(跨区块确定性一致, 重放幂等)。 */
    public static void generate(WorldGenLevel level, int chunkX, int chunkZ, long worldSeed) {
        List<Spike> spikes = createSpikes(worldSeed);
        int obsidian = BlockStateHelper.getDefault("obsidian");
        if (obsidian <= 0) return;
        int air = 0;
        int minY = level.getMinY();
        for (Spike s : spikes) {
            int minX = s.centerX - s.radius, maxX = s.centerX + s.radius;
            int minZ = s.centerZ - s.radius, maxZ = s.centerZ + s.radius;
            if (maxX < chunkX * 16 || minX > chunkX * 16 + 15
                    || maxZ < chunkZ * 16 || minZ > chunkZ * 16 + 15) continue;
            for (int x = minX; x <= maxX; x++) {
                for (int z = minZ; z <= maxZ; z++) {
                    double dx = x - s.centerX, dz = z - s.centerZ;
                    boolean inside = dx * dx + dz * dz <= (double) (s.radius * s.radius + 1);
                    for (int y = minY; y <= s.height + 10; y++) {
                        if (inside && y < s.height) {
                            level.setBlock(x, y, z, obsidian);
                        } else if (y > 65) {
                            if (level.getBlock(x, y, z) != air) level.setBlock(x, y, z, air);
                        }
                    }
                }
            }
            if (s.guarded) {
                int bars = BlockStateHelper.getDefault("iron_bars");
                if (bars > 0) {
                    for (int i = -2; i <= 2; i++) {
                        for (int j = -2; j <= 2; j++) {
                            for (int k = 0; k <= 3; k++) {
                                boolean edgeX = Math.abs(i) == 2;
                                boolean edgeZ = Math.abs(j) == 2;
                                boolean top = k == 3;
                                if (!edgeX && !edgeZ && !top) continue;
                                boolean ns = i == -2 || i == 2 || top;
                                boolean ew = j == -2 || j == 2 || top;
                                int st = bars;
                                if (BlockStateHelper.getProp(st, "north") != null) {
                                    st = BlockStateHelper.withProp(st, "north", (ns && j != -2) ? "true" : "false");
                                    st = BlockStateHelper.withProp(st, "south", (ns && j != 2) ? "true" : "false");
                                    st = BlockStateHelper.withProp(st, "west", (ew && i != -2) ? "true" : "false");
                                    st = BlockStateHelper.withProp(st, "east", (ew && i != 2) ? "true" : "false");
                                }
                                level.setBlock(s.centerX + i, s.height + k, s.centerZ + j, st);
                            }
                        }
                    }
                }
            }
            // 水晶/基岩座/永久火只在中心列所在 chunk 放一次(原版 isCenterWithinChunk)
            if ((s.centerX >> 4) == chunkX && (s.centerZ >> 4) == chunkZ) {
                int bedrock = BlockStateHelper.getDefault("bedrock");
                int fire = BlockStateHelper.getDefault("fire");
                if (bedrock > 0) level.setBlock(s.centerX, s.height, s.centerZ, bedrock);
                if (fire > 0) level.setBlock(s.centerX, s.height + 1, s.centerZ, fire);
                // Bug57: 放置前按 ±2/±3 去重(与 EndDragonFight.buildSpikes 同款判定),
                // 防止 feature 重放/与恢复逻辑叠加导致"一柱两颗水晶"。
                double cx2 = s.centerX + 0.5, cy2 = s.height + 1.0, cz2 = s.centerZ + 0.5;
                boolean exists = false;
                for (com.CharunCore.server.world.entity.Entity e : EntityManager.getEntities().values()) {
                    if (e instanceof EndCrystalEntity c && c.deathTime == 0
                            && Math.abs(c.x - cx2) < 2 && Math.abs(c.y - cy2) < 3 && Math.abs(c.z - cz2) < 2) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    EndCrystalEntity crystal = new EndCrystalEntity(
                        EntityManager.allocateId(), cx2, cy2, cz2);
                    crystal.dim = level.getWindow().values().iterator().next().dim;
                    EntityManager.addEntity(crystal);
                }
            }
        }
    }
}
