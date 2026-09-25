package com.CharunCore.server.world;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.utils.BlockStateHelper;

/** Bug37: 非完整方块支撑系统 —— 放置校验 + 邻居更新(挖掉支撑后依附方块变掉落物)。 */
public final class SupportEngine {
    private SupportEngine() {}

    private static final ThreadLocal<Integer> DEPTH = ThreadLocal.withInitial(() -> 0);

    /** 该非完整方块是否需要实心支撑(原版 canSurvive 的方块集合)。 */
    public static boolean needsSupport(String name) {
        if (name == null) return false;
        if (name.startsWith("minecraft:")) name = name.substring(10);
        if (name.endsWith("_torch") || name.equals("redstone_wire")
                || name.endsWith("_pressure_plate") || name.equals("pressure_plate")
                || name.equals("rail") || name.equals("powered_rail")
                || name.equals("activator_rail") || name.equals("detector_rail")
                || name.endsWith("_button") || name.equals("lever")
                || name.endsWith("_sapling") || name.endsWith("_flower")
                || name.endsWith("_eyeblossom") || name.endsWith("_bush")
                || name.endsWith("_dry_grass") || name.equals("leaf_litter")
                || name.equals("short_grass") || name.equals("tall_grass")
                || name.equals("fern") || name.equals("large_fern")
                || name.equals("dead_bush") || name.equals("vine")
                || name.equals("glow_lichen") || name.equals("sculk_vein")
                || name.equals("fire") || name.equals("soul_fire")
                || name.equals("red_mushroom") || name.equals("brown_mushroom")
                || name.endsWith("_candle")
                || name.equals("nether_wart") || name.equals("wheat") || name.equals("carrots")
                || name.equals("potatoes") || name.equals("beetroot")
                || name.equals("melon_stem") || name.equals("pumpkin_stem")
                || name.equals("torchflower_crop") || name.equals("pitcher_crop")
                || name.equals("sugar_cane") || name.equals("cactus")
                || name.equals("sea_pickle") || name.equals("lily_pad")
                || name.equals("snow") || name.equals("tripwire") || name.equals("tripwire_hook")
                || name.equals("pointed_dripstone") || name.equals("small_dripleaf")
                || name.equals("mangrove_propagule") || name.equals("spore_blossom")
                || name.equals("pink_petals") || name.equals("cocoa")
                || name.endsWith("_carpet") || name.equals("moss_carpet")) {
            return true;
        }
        return false;
    }

    /** 某方块是否可为依附方块提供支撑(近似原版 FaceAttachedHorizontalDirectionalBlock 判定)。 */
    public static boolean isSupportBlock(int state) {
        if (state == 0) return false;
        String n = BlockStateHelper.getName(state);
        if (n == null) return false;
        if (n.startsWith("minecraft:")) n = n.substring(10);
        if (BlockStateHelper.isReplaceable(n)) return false;
        if (n.equals("water") || n.equals("lava") || n.equals("air")
                || n.equals("cave_air") || n.equals("void_air")) return false;
        if (needsSupport(n)) return false;
        if (n.contains("sign") || n.contains("banner") || n.contains("head") || n.contains("skull")
                || n.contains("ladder") || n.contains("kelp") || n.contains("seagrass")
                || n.contains("dripstone") || n.equals("scaffolding")) return false;
        return true;
    }

    /** (x,y,z) 处的依附方块当前是否仍有支撑。 */
    public static boolean hasSupport(DimensionType dim, int x, int y, int z, String name) {
        boolean wallMounted = name.endsWith("_torch") || name.endsWith("_button")
                || name.equals("lever") || name.contains("_wall_")
                || name.equals("ladder") || name.equals("vine")
                || name.equals("glow_lichen") || name.equals("sculk_vein")
                || name.equals("tripwire_hook") || name.equals("piston_head");
        if (name.equals("pointed_dripstone") || name.equals("spore_blossom")) {
            int above = WorldManager.getBlockState(dim, x, y + 1, z);
            return isSupportBlock(above);
        }
        if (wallMounted) {
            return isSupportBlock(WorldManager.getBlockState(dim, x - 1, y, z))
                || isSupportBlock(WorldManager.getBlockState(dim, x + 1, y, z))
                || isSupportBlock(WorldManager.getBlockState(dim, x, y, z - 1))
                || isSupportBlock(WorldManager.getBlockState(dim, x, y, z + 1))
                || isSupportBlock(WorldManager.getBlockState(dim, x, y - 1, z));
        }
        int below = WorldManager.getBlockState(dim, x, y - 1, z);
        return isSupportBlock(below);
    }

    /** 方块变化后检查 6 邻居: 依附方块失去支撑 -> 破碎为掉落物(原版 neighborChanged/canSurvive)。
     *  由 WorldManager.setBlock 在运行时(非世界生成)路径调用。 */
    public static void checkNeighbors(DimensionType dim, int x, int y, int z) {
        if (DEPTH.get() > 0) return;
        DEPTH.set(1);
        try {
            int[][] dirs = {{0, 1, 0}, {0, -1, 0}, {1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
            for (int[] d : dirs) {
                int nx = x + d[0], ny = y + d[1], nz = z + d[2];
                int st = WorldManager.getBlockState(dim, nx, ny, nz);
                if (st == 0) continue;
                String n = BlockStateHelper.getName(st);
                if (!needsSupport(n)) continue;
                if (hasSupport(dim, nx, ny, nz, n)) continue;
                int dropId = BlockManager.getItemIdByName(n);
                WorldManager.setBlock(dim, nx, ny, nz, 0);
                NetworkHandler.broadcastBlockChange(dim, nx, ny, nz, 0);
                if (dropId > 0) {
                    com.CharunCore.server.world.entity.ItemEntity drop =
                        new com.CharunCore.server.world.entity.ItemEntity(
                            com.CharunCore.server.world.entity.EntityManager.allocateId(),
                            nx + 0.5, ny + 0.3, nz + 0.5, dropId, 1);
                    drop.dim = dim;
                    drop.pickupDelay = 10;
                    com.CharunCore.server.world.entity.EntityManager.addEntity(drop);
                }
                NetworkHandler.broadcastSoundAt(dim, nx + 0.5, ny + 0.5, nz + 0.5,
                        "minecraft:block.stone.break", 0.7f, 1.0f);
            }
        } catch (Exception e) {
            System.err.println("[Support] 邻居支撑检查异常: " + e);
        } finally {
            DEPTH.set(0);
        }
    }
}
