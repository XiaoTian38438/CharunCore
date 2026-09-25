package com.CharunCore.server.world;

import com.CharunCore.server.Main;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.world.entity.EntityManager;
import com.CharunCore.server.world.entity.ItemEntity;
import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.worldgen.WorldGenLevel;
import com.CharunCore.server.worldgen.feature.SimpleTreeFeature;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.world.gen.XoroshiroRandomSource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

/**
 * 随机刻引擎：作物生长 / 甘蔗仙人掌长高 / 耕地 moisture / 树叶自然凋零 / 藤蔓蔓延 / 树苗长树。
 * 仅对在线玩家附近已加载区块生效；随机刻范围对齐原版（玩家周围 ±8 区块 / 128 格半径）。
 */
public final class RandomTickEngine {

    private static final Random RND = new Random();

    // 作物 → 最大 age（与原版一致）
    private static final Map<String, Integer> CROP_MAX_AGE = Map.of(
        "wheat", 7, "carrots", 7, "potatoes", 7, "beetroots", 3,
        "nether_wart", 3, "cocoa", 2, "melon_stem", 7, "pumpkin_stem", 7
    );

    private RandomTickEngine() {}

    public static void tick() {
        for (NetworkHandler player : NetworkHandler.players.values()) {
            if (player.ctx == null || !player.ctx.channel().isActive()) continue;
            if (player.isDead) continue;
            int px = (int) Math.floor(player.x);
            int py = (int) Math.floor(player.y);
            int pz = (int) Math.floor(player.z);
            int pcx = px >> 4;
            int pcz = pz >> 4;
            DimensionType dim = player.currentDim;

            // 玩家周围 17x17 区块（±8 ≈ 128 格半径，对齐原版随机刻范围），每区块 3 次随机刻
            // 仅对已加载（已缓存）区块生效，避免随机刻触发大规模地形生成
            for (int dx = -8; dx <= 8; dx++) {
                for (int dz = -8; dz <= 8; dz++) {
                    int cx = pcx + dx;
                    int cz = pcz + dz;
                    long ckey = ((long) cx << 32) | (cz & 0xFFFFFFFFL);
                    if (!WorldManager.isChunkCached(dim, ckey)) continue;
                    for (int n = 0; n < 3; n++) {
                        int x = cx * 16 + RND.nextInt(16);
                        int z = cz * 16 + RND.nextInt(16);
                        int y = py + RND.nextInt(33) - 16;
                        if (y < dim.minY || y > dim.minY + dim.height - 1) continue;
                        randomTick(dim, x, y, z);
                    }
                }
            }
        }
    }

    private static void randomTick(DimensionType dim, int x, int y, int z) {
        int state = WorldManager.getBlockState(dim, x, y, z);
        if (state == 0) return;
        String name = BlockStateHelper.getName(state);

        // 耕地：moisture 灌溉 / 干涸退化
        if (name.equals("farmland")) {
            tickFarmland(dim, x, y, z, state);
            return;
        }
        // 树苗：随机刻长成树
        if (name.endsWith("_sapling")) {
            tickSapling(dim, x, y, z, name);
            return;
        }
        // 藤蔓：随机刻蔓延
        if (name.equals("vine")) {
            tickVine(dim, x, y, z, state);
            return;
        }

        Integer maxAge = CROP_MAX_AGE.get(name);
        if (maxAge != null) {
            if (name.equals("melon_stem") || name.equals("pumpkin_stem")) {
                tickStem(dim, x, y, z, state, name, maxAge);
            } else {
                tickCrop(dim, x, y, z, state, name, maxAge);
            }
            return;
        }
        if (name.equals("cactus") || name.equals("sugar_cane") || name.equals("bamboo")) {
            tickUpwardPlant(dim, x, y, z, name);
            return;
        }
        if (name.endsWith("_leaves")) {
            tickLeavesDecay(dim, x, y, z, name);
        }
        // 沙子 / 砂砾 / 混凝土粉末 重力：下方为空气/可替换方块时坠落
        if (name.equals("sand") || name.equals("gravel") || name.equals("red_sand")
                || name.equals("concrete_powder") || name.equals("suspicious_sand")
                || name.equals("suspicious_gravel")) {
            tickGravityBlock(dim, x, y, z, state, name);
        }
        // 注: 火的 age/蔓延/熄灭已由 FluidEngine 的计划刻驱动(原版每 ~30 刻一次),
        // 不再走随机刻(随机刻约每 68 秒才选中一次 → 老化极慢/几乎不蔓延, 见 #43/#47)。
    }

    // ===== 光照（近似原版 getRawBrightness）=====
    // 无光照引擎：天空暴露则按昼夜时间给出天空亮度（正午≈15，午夜≈0），
    // 上方 64 格内有实心方块遮挡则视为无天空光（洞穴/室内不生长）。
    private static int getRawBrightness(DimensionType dim, int x, int y, int z) {
        int top = dim.minY + dim.height - 1;
        int sy = y + 1;
        int scanned = 0;
        while (sy <= top) {
            int b = WorldManager.getBlockState(dim, x, sy, z);
            if (b != 0 && BlockStateHelper.isSolidOpaque(b)) {
                return 0; // 被遮挡，无天空光
            }
            sy++;
            if (++scanned > 64) break; // 64 格内无遮挡即视为露天（农田不会在 64 格顶盖下）
        }
        long t = Main.dayTime % 24000L;
        double angle = (t / 24000.0) * 2.0 * Math.PI;
        double elevation = Math.sin(angle); // 正午 +1，午夜 ~0，傍晚 -1
        int sky = (int) Math.round((elevation + 1.0) / 2.0 * 15.0);
        if (sky < 0) sky = 0;
        if (sky > 15) sky = 15;
        return sky;
    }

    private static void setBlockAndBroadcast(DimensionType dim, int x, int y, int z, int state) {
        WorldManager.setBlock(dim, x, y, z, state);
        NetworkHandler.broadcastBlockChange(dim, x, y, z, state);
    }

    private static void growAge(DimensionType dim, int x, int y, int z, int state, int newAge) {
        int ns = BlockStateHelper.withProp(state, "age", Integer.toString(newAge));
        if (ns == state) return;
        setBlockAndBroadcast(dim, x, y, z, ns);
    }

    // ===== B1：作物生长（光照 / 耕地 / moisture 概率）=====
    private static void tickCrop(DimensionType dim, int x, int y, int z,
                                 int state, String name, int maxAge) {
        String ageStr = BlockStateHelper.getProp(state, "age");
        if (ageStr == null) return;
        int age = Integer.parseInt(ageStr);
        if (age >= maxAge) return;

        // 地狱疣：长在灵魂沙上，无需光照/耕地。原版每随机刻 1/3 概率生长
        if (name.equals("nether_wart")) {
            if (!BlockStateHelper.getName(WorldManager.getBlockState(dim, x, y - 1, z)).equals("soul_sand")) return;
            if (RND.nextInt(3) != 0) return;
            growAge(dim, x, y, z, state, age + 1);
            return;
        }
        // 可可果：贴在原木侧面（同一高度水平相邻），无需光照/耕地。原版每随机刻 1/5 概率生长
        if (name.equals("cocoa")) {
            boolean supported = false;
            int[][] sdirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] sd : sdirs) {
                if (BlockStateHelper.getName(WorldManager.getBlockState(dim, x + sd[0], y, z + sd[1])).contains("_log")) {
                    supported = true;
                    break;
                }
            }
            if (!supported) return;
            if (RND.nextInt(5) != 0) return;
            growAge(dim, x, y, z, state, age + 1);
            return;
        }

        // 普通作物：必须种在耕地 + 需要光照，按耕地 moisture 调生长概率
        int below = WorldManager.getBlockState(dim, x, y - 1, z);
        String belowName = BlockStateHelper.getName(below);
        if (!belowName.equals("farmland")) {
            // 不在耕地上：原版会弹落作物
            popOffCrop(dim, x, y, z, state, name);
            return;
        }
        if (getRawBrightness(dim, x, y, z) < 9) return; // 夜间/室内不生长

        // 生长速度系数 f（对齐原版 CropBlock.getGrowthSpeed：干地 1.0，湿润 3.0，邻格再加成）
        int moisture = 0;
        String mStr = BlockStateHelper.getProp(below, "moisture");
        if (mStr != null) moisture = Integer.parseInt(mStr);
        float f = (moisture > 0) ? 3.0f : 1.0f;
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] d : dirs) {
            String nn = BlockStateHelper.getName(WorldManager.getBlockState(dim, x + d[0], y, z + d[1]));
            if ("farmland".equals(nn)) f += 0.75f;
            else if (CROP_MAX_AGE.containsKey(nn) || nn.endsWith("_stem")) f += 1.0f;
        }
        int chance = (int) (25.0f / f) + 1; // 干地≈1/26，湿润≈1/9
        if (RND.nextInt(chance) != 0) return;
        growAge(dim, x, y, z, state, age + 1);
    }

    // 作物不在耕地上：弹落（置空气并掉落对应种子/物品，best-effort）
    private static void popOffCrop(DimensionType dim, int x, int y, int z, int state, String name) {
        setBlockAndBroadcast(dim, x, y, z, 0);
        dropItem(dim, x, y, z, cropDropItem(name), 1);
    }

    private static String cropDropItem(String name) {
        return switch (name) {
            case "wheat" -> "wheat_seeds";
            case "beetroots" -> "beetroot_seeds";
            case "carrots" -> "carrot";
            case "potatoes" -> "potato";
            case "nether_wart" -> "nether_wart";
            case "cocoa" -> "cocoa_beans";
            default -> name;
        };
    }

    // ===== B4：瓜类藤（西瓜/南瓜）成熟后生成果实 =====
    private static void tickStem(DimensionType dim, int x, int y, int z,
                                 int state, String name, int maxAge) {
        int below = WorldManager.getBlockState(dim, x, y - 1, z);
        if (!BlockStateHelper.getName(below).equals("farmland")) return; // 藤必须长在耕地
        if (getRawBrightness(dim, x, y, z) < 9) return;

        String ageStr = BlockStateHelper.getProp(state, "age");
        if (ageStr == null) return;
        int age = Integer.parseInt(ageStr);
        if (age < maxAge) {
            int moisture = 0;
            String mStr = BlockStateHelper.getProp(below, "moisture");
            if (mStr != null) moisture = Integer.parseInt(mStr);
            float f = (moisture > 0) ? 3.0f : 1.0f;
            int chance = (int) (25.0f / f) + 1;
            if (RND.nextInt(chance) != 0) return;
            growAge(dim, x, y, z, state, age + 1);
            return;
        }

        // 已成熟：尝试在相邻空格生成果实（对齐原版 StemBlock）
        boolean isMelon = name.startsWith("melon");
        String fruitName = isMelon ? "melon" : "pumpkin";
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        // 随机排列方向，寻找首个合法位置
        int start = RND.nextInt(4);
        if (RND.nextInt(3) != 0) return; // 控制结果频率
        for (int i = 0; i < 4; i++) {
            int[] d = dirs[(start + i) & 3];
            int tx = x + d[0], tz = z + d[1], ty = y;
            if (WorldManager.getBlockState(dim, tx, ty, tz) != 0) continue;
            String ground = BlockStateHelper.getName(WorldManager.getBlockState(dim, tx, ty - 1, tz));
            if (!ground.equals("dirt") && !ground.equals("grass_block") && !ground.equals("farmland")
                    && !ground.equals("mud")) continue;
            // 相邻已有同类型果实则跳过（避免堆叠）
            boolean occupied = false;
            for (int[] d2 : dirs) {
                String an = BlockStateHelper.getName(WorldManager.getBlockState(dim, x + d2[0], y, z + d2[1]));
                if (an.equals(fruitName)) { occupied = true; break; }
            }
            if (occupied) continue;
            int fruit = BlockStateHelper.getDefault(fruitName);
            if (fruit != 0) {
                setBlockAndBroadcast(dim, tx, ty, tz, fruit);
                return;
            }
        }
    }

    // ===== B3：甘蔗 / 仙人掌 / 竹子（邻接约束 + AGE 0..15 累加）=====
    private static void tickUpwardPlant(DimensionType dim, int x, int y, int z, String name) {
        int above = WorldManager.getBlockState(dim, x, y + 1, z);
        if (above != 0) return; // 顶部非空气，停止生长

        // 找到当前植株顶端
        int topY = y;
        while (name.equals(BlockStateHelper.getName(WorldManager.getBlockState(dim, x, topY + 1, z)))) topY++;
        if (topY != y) return; // 只对顶端施加生长

        // 统计高度与基底
        int height = 1;
        int by = y - 1;
        while (name.equals(BlockStateHelper.getName(WorldManager.getBlockState(dim, x, by, z)))) { height++; by--; }
        int baseY = by + 1;

        String baseGround = BlockStateHelper.getName(WorldManager.getBlockState(dim, x, baseY - 1, z));

        if (name.equals("sugar_cane")) {
            // 基底须为泥土/草/沙，且基底水平邻居含水
            if (!(baseGround.equals("dirt") || baseGround.equals("grass_block") || baseGround.equals("sand")
                    || baseGround.equals("red_sand") || baseGround.equals("mud") || baseGround.equals("moss_block"))) return;
            if (!isWaterAdjacent(dim, x, baseY, z)) return;
        } else if (name.equals("cactus")) {
            // 基底为仙人掌或沙；4 个水平邻居均不可实心/不可为岩浆
            if (!(baseGround.equals("cactus") || baseGround.equals("sand") || baseGround.equals("red_sand"))) return;
            int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] d : dirs) {
                int nb = WorldManager.getBlockState(dim, x + d[0], y, z + d[1]);
                String nn = BlockStateHelper.getName(nb);
                if (nn.contains("lava")) return;
                if (BlockStateHelper.isSolidOpaque(nb) && !nn.equals("cactus")) return;
            }
        } else { // bamboo
            if (!(baseGround.equals("bamboo") || baseGround.equals("dirt") || baseGround.equals("grass_block")
                    || baseGround.equals("sand") || baseGround.equals("red_sand") || baseGround.equals("mud")
                    || baseGround.equals("podzol") || baseGround.equals("mycelium") || baseGround.equals("moss_block"))) return;
        }

        // 高度上限: 甘蔗 3 格、仙人掌 3 格、竹子 12-16 格(原版); 竹子取 12
        int maxHeight = name.equals("bamboo") ? 12 : 3;
        if (height >= maxHeight) return;

        // AGE 0..15 累加：满 15 才真正长出一节（对齐原版速率）。age 保存在顶端方块上。
        int topState = WorldManager.getBlockState(dim, x, topY, z);
        String ageStr = BlockStateHelper.getProp(topState, "age");
        if (ageStr == null) {
            // 无 age 属性（如竹子的某些状态）：回退为旧的直接生长行为
            if (RND.nextInt(3) != 0) return;
            setBlockAndBroadcast(dim, x, topY + 1, z, BlockStateHelper.getDefault(name));
            return;
        }
        int age = Integer.parseInt(ageStr);
        if (age >= 15) {
            int ns = BlockStateHelper.getDefault(name); // 新一节，age 归 0
            setBlockAndBroadcast(dim, x, topY + 1, z, ns);
        } else {
            int ns = BlockStateHelper.withProp(topState, "age", Integer.toString(age + 1));
            setBlockAndBroadcast(dim, x, topY, z, ns);
        }
    }

    // ===== B2：耕地 moisture 灌溉 / 干涸退化（best-effort，在耕地方块随机刻实现）=====
    private static void tickFarmland(DimensionType dim, int x, int y, int z, int state) {
        int moisture = 0;
        String mStr = BlockStateHelper.getProp(state, "moisture");
        if (mStr != null) moisture = Integer.parseInt(mStr);

        boolean nearWater = isNearWater(dim, x, y, z);
        boolean raining = isRaining(dim);
        int m = moisture;
        if (nearWater || raining) {
            m = 7;
        } else if (m > 0) {
            m -= 1;
        }

        if (m != moisture) {
            int ns = BlockStateHelper.withProp(state, "moisture", Integer.toString(m));
            setBlockAndBroadcast(dim, x, y, z, ns);
        }

        // moisture==0 且上方无作物维持 → 退化回泥土
        if (m == 0) {
            String an = BlockStateHelper.getName(WorldManager.getBlockState(dim, x, y + 1, z));
            boolean hasCrop = an != null && (CROP_MAX_AGE.containsKey(an) || an.endsWith("_stem")
                    || an.endsWith("_sapling") || an.equals("bamboo") || an.equals("sugar_cane")
                    || an.equals("cactus") || an.equals("nether_wart") || an.equals("cocoa")
                    || an.equals("melon") || an.equals("pumpkin"));
            if (!hasCrop) {
                int dirt = BlockStateHelper.getDefault("dirt");
                if (dirt != 0) setBlockAndBroadcast(dim, x, y, z, dirt);
            }
        }
    }

    // ===== B5：树苗长成树（复用 SimpleTreeFeature，best-effort）=====
    private static void tickSapling(DimensionType dim, int x, int y, int z, String name) {
        if (getRawBrightness(dim, x, y, z) < 9) return; // 需要光照
        String gn = BlockStateHelper.getName(WorldManager.getBlockState(dim, x, y - 1, z));
        if (!(gn.equals("dirt") || gn.equals("grass_block") || gn.equals("farmland") || gn.equals("podzol")
                || gn.equals("mycelium") || gn.equals("mud") || gn.equals("moss_block") || gn.equals("coarse_dirt"))) return;

        // 树干通道需畅通（避免树苗被遮挡时长树失败）
        for (int dy = 1; dy <= 12; dy++) {
            int b = WorldManager.getBlockState(dim, x, y + dy, z);
            if (b != 0 && BlockStateHelper.isSolidOpaque(b)) return;
        }

        if (RND.nextInt(7) != 0) return; // 控制自然生长频率

        SimpleTreeFeature.TreeType type = saplingToTree(name);
        RandomSource rnd = new XoroshiroRandomSource(
                (long) (x * 73856093L) ^ (long) (y * 19349663L) ^ (long) (z * 83492791L) ^ Main.dayTime);

        int oldState = WorldManager.getBlockState(dim, x, y, z);
        setBlockAndBroadcast(dim, x, y, z, 0); // 先移除树苗
        WmLevel lvl = new WmLevel(dim);
        boolean ok = SimpleTreeFeature.placeTree(lvl, x, y, z, type, rnd);
        if (!ok) {
            setBlockAndBroadcast(dim, x, y, z, oldState); // 空间不足则还原
        }
    }

    private static SimpleTreeFeature.TreeType saplingToTree(String name) {
        return switch (name) {
            case "oak_sapling" -> SimpleTreeFeature.TreeType.OAK;
            case "birch_sapling" -> SimpleTreeFeature.TreeType.BIRCH;
            case "spruce_sapling" -> SimpleTreeFeature.TreeType.SPRUCE;
            case "dark_oak_sapling" -> SimpleTreeFeature.TreeType.DARK_OAK;
            case "jungle_sapling" -> SimpleTreeFeature.TreeType.JUNGLE;
            case "acacia_sapling" -> SimpleTreeFeature.TreeType.ACACIA;
            case "cherry_sapling" -> SimpleTreeFeature.TreeType.CHERRY;
            default -> SimpleTreeFeature.TreeType.OAK;
        };
    }

    // 基于 WorldManager 的 WorldGenLevel 适配器，供 SimpleTreeFeature 读写真实世界
    private static final class WmLevel extends WorldGenLevel {
        private final DimensionType dim;
        WmLevel(DimensionType dim) {
            super(0, 0, new java.util.HashMap<Long, Chunk>(), 0, 0, dim.minY, dim.height);
            this.dim = dim;
        }
        @Override
        public int getBlock(int absX, int y, int absZ) {
            return WorldManager.getBlockState(dim, absX, y, absZ);
        }
        @Override
        public boolean setBlock(int absX, int y, int absZ, int stateId) {
            setBlockAndBroadcast(dim, absX, y, absZ, stateId);
            return true;
        }
    }

    // ===== B8：藤蔓随机刻蔓延（best-effort）=====
    private static void tickVine(DimensionType dim, int x, int y, int z, int state) {
        if (RND.nextInt(4) != 0) return;
        int[][] dirs = {{0, -1, 0}, {1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}};
        int[] dir = dirs[RND.nextInt(dirs.length)];
        int nx = x + dir[0], ny = y + dir[1], nz = z + dir[2];
        if (WorldManager.getBlockState(dim, nx, ny, nz) != 0) return;

        Map<String, String> props = new LinkedHashMap<>();
        int support = 0;
        int u = WorldManager.getBlockState(dim, nx, ny + 1, nz);
        if (isVineSupport(u)) { props.put("up", "true"); support++; }
        int n = WorldManager.getBlockState(dim, nx, ny, nz - 1);
        if (isVineSupport(n)) { props.put("north", "true"); support++; }
        int s = WorldManager.getBlockState(dim, nx, ny, nz + 1);
        if (isVineSupport(s)) { props.put("south", "true"); support++; }
        int e = WorldManager.getBlockState(dim, nx + 1, ny, nz);
        if (isVineSupport(e)) { props.put("east", "true"); support++; }
        int w = WorldManager.getBlockState(dim, nx - 1, ny, nz);
        if (isVineSupport(w)) { props.put("west", "true"); support++; }
        if (support == 0) return; // 无处附着

        int ns = BlockStateHelper.getState("vine", props);
        if (ns == 0) ns = BlockStateHelper.getDefault("vine");
        if (ns != 0) setBlockAndBroadcast(dim, nx, ny, nz, ns);
    }

    private static boolean isVineSupport(int state) {
        if (state == 0) return false;
        String n = BlockStateHelper.getName(state);
        return BlockStateHelper.isSolidOpaque(state) || n.equals("vine");
    }

    // ===== B7：树叶凋零（距离判定 BFS + 概率掉落，best-effort）=====
    private static void tickLeavesDecay(DimensionType dim, int x, int y, int z, String name) {
        if (RND.nextInt(8) != 0) return;
        if (leavesConnectedToLog(dim, x, y, z)) return; // 仍连接原木，保留
        setBlockAndBroadcast(dim, x, y, z, 0);
        // 掉落（best-effort）：橡类有概率掉树苗，其余小概率掉木棍
        if (name.endsWith("_leaves") && (name.equals("oak_leaves") || name.equals("dark_oak_leaves")
                || name.equals("birch_leaves") || name.equals("spruce_leaves") || name.equals("jungle_leaves")
                || name.equals("acacia_leaves") || name.equals("cherry_leaves"))) {
            if (RND.nextInt(20) == 0) dropItem(dim, x, y, z, name.replace("_leaves", "_sapling"), 1);
        } else if (RND.nextInt(50) == 0) {
            dropItem(dim, x, y, z, "stick", 1);
        }
    }

    // 从树叶出发，沿树叶 BFS（深度 ≤6）寻找原木；找不到即应凋零
    private static boolean leavesConnectedToLog(DimensionType dim, int x, int y, int z) {
        java.util.ArrayDeque<int[]> q = new java.util.ArrayDeque<>();
        java.util.Set<String> visited = new java.util.HashSet<>();
        q.add(new int[]{x, y, z, 0});
        visited.add(key(x, y, z));
        int[][] steps = {{1, 0, 0}, {-1, 0, 0}, {0, 1, 0}, {0, -1, 0}, {0, 0, 1}, {0, 0, -1}};
        while (!q.isEmpty()) {
            int[] c = q.poll();
            if (c[3] > 6) continue;
            for (int[] st : steps) {
                int nx = c[0] + st[0], ny = c[1] + st[1], nz = c[2] + st[2];
                int nb = WorldManager.getBlockState(dim, nx, ny, nz);
                if (nb == 0) continue;
                String nn = BlockStateHelper.getName(nb);
                if (nn.endsWith("_log")) return true;
                if (nn.endsWith("_leaves")) {
                    String k = key(nx, ny, nz);
                    if (visited.add(k)) q.add(new int[]{nx, ny, nz, c[3] + 1});
                }
            }
        }
        return false;
    }

    private static String key(int x, int y, int z) {
        return x + "," + y + "," + z;
    }

    // ===== 工具 =====
    private static boolean isWaterAdjacent(DimensionType dim, int x, int y, int z) {
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] d : dirs) {
            String nn = BlockStateHelper.getName(WorldManager.getBlockState(dim, x + d[0], y, z + d[1]));
            if (nn != null && nn.contains("water")) return true;
        }
        return false;
    }

    // 半径 4 内、y∈[y-1,y+1] 见水（对齐原版 FarmBlock.isNearWater）
    private static boolean isNearWater(DimensionType dim, int x, int y, int z) {
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                for (int dy = -1; dy <= 1; dy++) {
                    String nn = BlockStateHelper.getName(WorldManager.getBlockState(dim, x + dx, y + dy, z + dz));
                    if (nn != null && nn.contains("water")) return true;
                }
            }
        }
        return false;
    }

    private static boolean isRaining(DimensionType dim) {
        // 本核心暂无天气系统；耕地仅在近水时保持湿润（best-effort）
        return false;
    }

    private static void dropItem(DimensionType dim, int x, int y, int z, String itemName, int count) {
        try {
            int itemId = BlockManager.getItemIdByName(itemName);
            if (itemId <= 0) return;
            ItemEntity ie = new ItemEntity(
                    EntityManager.allocateId(), x + 0.5, y + 0.5, z + 0.5, itemId, count);
            ie.dim = dim;
            EntityManager.addEntity(ie);
        } catch (Throwable t) {
            // best-effort 掉落，失败静默
        }
    }

    // ===== 沙子/砂砾/混凝土粉末 重力 =====
    // 原版：受重力方块在随机刻检测下方支撑，无支撑则逐格下坠直到落地。
    // 为避免单 tick 坠落多格造成性能问题，每随机刻只下坠 1 格并调度下一格。
    private static void tickGravityBlock(DimensionType dim, int x, int y, int z,
                                         int state, String name) {
        if (y <= dim.minY) return;  // 到世界底部不再下
        int below = WorldManager.getBlockState(dim, x, y - 1, z);
        String belowName = BlockStateHelper.getName(below);
        // 下方为空气/液体/可替换非实心 → 下坠
        boolean canFall = (below == 0)
                || belowName.contains("water") || belowName.contains("lava")
                || (below != 0 && !BlockStateHelper.isSolidOpaque(below)
                    && !"sand".equals(belowName) && !"gravel".equals(belowName)
                    && !"red_sand".equals(belowName));
        if (!canFall) return;

        // 特殊：混凝土粉末遇水变为混凝土
        if ("concrete_powder".equals(name) && belowName.contains("water")) {
            String color = name.replace("concrete_powder", "concrete");
            int concreteId = BlockStateHelper.getDefault(color);
            if (concreteId > 0) {
                setBlockAndBroadcast(dim, x, y, z, concreteId);
            }
            return;
        }

        // 清除原位置，在下方放置（如果下方也是重力方块则连锁触发由后续随机刻处理）
        setBlockAndBroadcast(dim, x, y, z, 0);
        setBlockAndBroadcast(dim, x, y - 1, z, state);

        // 如果新位置下方仍可坠落，调度下一格（延迟 2 tick 避免同帧级联）
        if (y - 1 > dim.minY) {
            int nextBelow = WorldManager.getBlockState(dim, x, y - 2, z);
            String nextBelowName = BlockStateHelper.getName(nextBelow);
            boolean nextCanFall = (nextBelow == 0)
                    || (nextBelow != 0 && !BlockStateHelper.isSolidOpaque(nextBelow));
            if (nextCanFall) {
                FluidEngine.scheduleFluidTick(dim, x, y - 1, z, state, 2);
            }
        }
    }
}
