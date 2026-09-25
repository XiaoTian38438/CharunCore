package com.CharunCore.server.world;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.CharunCore.server.Main;
import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;

public class FluidEngine {
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    // 入队去重 + 在途标记：位置在处理期间驻留于此，处理完成后移除 (见 B2)。
    // key 形如 "<dimOrdinal>:<x>,<y>,<z>"，使不同维度同名坐标互不干扰。
    private static final java.util.Set<String> activeFluids = java.util.concurrent.ConcurrentHashMap.newKeySet();
    // 本轮待处理队列；每个元素为 {x, y, z, stateId, dimOrdinal}。
    private static final java.util.Set<long[]> pendingFluids = java.util.concurrent.ConcurrentHashMap.newKeySet();
    // 已扫描过流体的区块, 避免重复扫描造成性能浪费
    private static final java.util.Set<Long> scannedChunks = java.util.concurrent.ConcurrentHashMap.newKeySet();
    // 全局调度上限, 防止极端地形象限下流体级联失控
    private static final int MAX_ACTIVE_FLUIDS = 600000;
    // 每 tick 处理预算 (B6)：将大批量待处理流体分摊到多 tick，避免单次卡顿。
    private static final int MAX_PROCESS_PER_TICK = 30000;
    // 原版流体在随机 tick 中流动(平均每 5 游戏刻处理一次流动)。
    private static final int FLOW_DELAY_TICKS = 5;

    // ── 火计划刻 (原版 FireBlock 每 30~40 刻 tick 一次, 非随机刻) ──────────
    // 火此前挂在随机刻引擎, 约每 68 秒才选中一次 → 几乎不蔓延/老化极慢(#43/#47)。
    // 这里用与流体相同的计划刻机制, 每 ~30 刻驱动一次火的 age/蔓延/熄灭。
    private static final java.util.Random FIRE_RND = new java.util.Random();
    private static final int FIRE_TICK_DELAY = 30;            // 原版 getFireTickDelay = 30 + nextInt(10)
    private static final java.util.Set<String> activeFires = java.util.concurrent.ConcurrentHashMap.newKeySet();
    private static final java.util.Set<long[]> pendingFires = java.util.concurrent.ConcurrentHashMap.newKeySet();

    // ── 调度入口 ──────────────────────────────────────────────

    /** 默认维度（主世界）的调度入口，兼容既有调用方（桶放置 / 区块扫描）。 */
    public static void scheduleFluidTick(int x, int y, int z, int stateId) {
        scheduleFluidTick(DimensionType.OVERWORLD, x, y, z, stateId);
    }

    /** 带维度的调度入口。activeFluids 去重，避免同一位置在途期间重复入队。 */
    public static void scheduleFluidTick(DimensionType dim, int x, int y, int z, int stateId) {
        scheduleFluidTick(dim, x, y, z, stateId, 0);
    }

    /** 带延迟的调度入口。delayTicks>0 时延迟若干游戏刻后处理（原版随机 tick 5 刻流动）。 */
    public static void scheduleFluidTick(DimensionType dim, int x, int y, int z, int stateId, int delayTicks) {
        String key = dim.ordinal() + ":" + x + "," + y + "," + z;
        if (!activeFluids.add(key)) return;            // 已在队列/处理中 → 去重
        if (activeFluids.size() > MAX_ACTIVE_FLUIDS) { // 超过上限则不入队
            activeFluids.remove(key);
            return;
        }
        long due = Main.worldAge + Math.max(0, delayTicks);
        pendingFluids.add(new long[]{x, y, z, stateId, dim.ordinal(), due});
    }

    /**
     * 方块邻居发生变化后调用：把受影响格 6 邻居中现存的水/岩浆重新纳入流体引擎，
     * 使玩家挖掘/放置方块后流体能够重新蔓延 (B1)。
     * 由 {@code WorldManager.setBlock} 在方块变更后调用。
     */
    public static void neighborChanged(int x, int y, int z) {
        neighborChanged(DimensionType.OVERWORLD, x, y, z);
    }

    public static void neighborChanged(DimensionType dim, int x, int y, int z) {
        int[][] dirs = {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}, {0, -1, 0}, {0, 1, 0}};
        for (int[] d : dirs) {
            int nx = x + d[0], ny = y + d[1], nz = z + d[2];
            int ns = WorldManager.getBlockState(dim, nx, ny, nz);
            if (isWater(ns) || isLava(ns)) {
                scheduleFluidTick(dim, nx, ny, nz, ns);
            }
        }
    }

    /**
     * 扫描一个区块内的水/岩浆并调度流体 tick。
     * 【重要】只调度"稳定水源"(level=0 且下方有实体支撑): 自然地形水体保持生成时
     * 的稳定状态, 不排洪 —— 原版地形水域不流动, 玩家破坏/放置才通过 neighborChanged
     * 触发流动。此前调度全部水/流动水/悬空水 → 玩家移动加载新区块时大规模"排洪"
     * (实测 1.2 万~1.8 万条 0x08/次), 挤占带宽并引发放置/攻击时序竞态。
     * 同一区块只扫描一次 (scannedChunks 去重)。
     */
    public static void scheduleChunkFluids(DimensionType dim, Chunk chunk) {
        long chunkKey = chunkScanKey(dim, chunk.getX(), chunk.getZ());
        if (!scannedChunks.add(chunkKey)) return;
        Chunk.Section[] sections = chunk.getSections();
        for (int sec = 0; sec < sections.length; sec++) {
            int[] blocks = sections[sec].getBlocks();
            int baseY = (sec - 4) * 16;
            for (int i = 0; i < 4096; i++) {
                int stateId = blocks[i];
                if (stateId == 0) continue;
                String name = BlockStateHelper.getName(stateId);
                if (!"water".equals(name) && !"lava".equals(name)) continue;
                // 只处理水源(level=0); 流动水靠玩家交互(neighborChanged)触发, 不主动排洪
                String lv = BlockStateHelper.getProp(stateId, "level");
                if (lv != null && !"0".equals(lv)) continue;
                int lx = i & 15;
                int ly = (i >> 8) & 15;
                int lz = (i >> 4) & 15;
                int wx = chunk.getX() * 16 + lx;
                int wz = chunk.getZ() * 16 + lz;
                int wy = baseY + ly;
                // 悬空水源(下方可流)不调度: 不主动排洪, 保持生成状态
                if (canDisplace(WorldManager.getBlockState(dim, wx, wy - 1, wz))) continue;
                scheduleFluidTick(dim, wx, wy, wz, stateId);
            }
        }
    }

    /** 每 tick 驱动：限制单 tick 处理预算，处理完成后回收 activeFluids 标记 (B2/B6)。 */
    public static void tick() {
        if (pendingFluids.isEmpty()) return;
        long now = Main.worldAge;
        java.util.List<long[]> batch = new java.util.ArrayList<>(MAX_PROCESS_PER_TICK);
        java.util.Iterator<long[]> it = pendingFluids.iterator();
        while (it.hasNext() && batch.size() < MAX_PROCESS_PER_TICK) {
            long[] p = it.next();
            // 延迟到期检查: 未到期的重新放回(通过 remove+add 原子性近似; 简单起见跳过)
            if (p.length >= 6 && p[5] > now) continue;
            it.remove();
            batch.add(p);
        }
        for (long[] pos : batch) {
            int x = (int) pos[0], y = (int) pos[1], z = (int) pos[2];
            int stateId = (int) pos[3];
            DimensionType dim = DimensionType.values()[(int) pos[4]];
            String key = dim.ordinal() + ":" + x + "," + y + "," + z;
            try {
                processFluid(dim, x, y, z, stateId);
            } catch (Exception e) {
                System.err.println("[流体] 处理异常 @ " + key + ": " + e);
            } finally {
                // B2: 处理完毕即从 activeFluids 移除，允许稳定流体后续被重新调度，
                // 同时防止集合只增不减导致的内存泄漏。
                activeFluids.remove(key);
            }
        }
        tickFires();
    }

    // ── 火的调度 (原版 FireBlock 计划刻, 每 ~30 刻一次) ─────────────────────
    public static void scheduleFireTick(DimensionType dim, int x, int y, int z, int stateId) {
        scheduleFireTick(dim, x, y, z, stateId, 0);
    }

    public static void scheduleFireTick(DimensionType dim, int x, int y, int z, int stateId, int delayTicks) {
        String key = "F:" + dim.ordinal() + ":" + x + "," + y + "," + z;
        if (!activeFires.add(key)) return;                 // 去重, 避免重复入队
        long due = Main.worldAge + Math.max(0, delayTicks);
        pendingFires.add(new long[]{x, y, z, stateId, dim.ordinal(), due});
    }

    private static void tickFires() {
        if (pendingFires.isEmpty()) return;
        long now = Main.worldAge;
        java.util.List<long[]> batch = new java.util.ArrayList<>(MAX_PROCESS_PER_TICK);
        java.util.Iterator<long[]> it = pendingFires.iterator();
        while (it.hasNext() && batch.size() < MAX_PROCESS_PER_TICK) {
            long[] p = it.next();
            if (p.length >= 6 && p[5] > now) continue;
            it.remove();
            batch.add(p);
        }
        for (long[] pos : batch) {
            int x = (int) pos[0], y = (int) pos[1], z = (int) pos[2];
            int stateId = (int) pos[3];
            DimensionType dim = DimensionType.values()[(int) pos[4]];
            String key = "F:" + dim.ordinal() + ":" + x + "," + y + "," + z;
            activeFires.remove(key);                        // 立即释放, 便于 processFire 重新调度自身
            try {
                processFire(dim, x, y, z, stateId);
            } catch (Exception e) {
                System.err.println("[火] 处理异常 @ " + key + ": " + e);
            }
        }
    }

    /**
     * 火的计划刻处理 (对齐原版 FireBlock.tick):
     * - age 每刻推进 min(15, age + nextInt(3)/2);
     * - 雨天露天火按 0.2 + age*0.03 概率熄灭;
     * - 非无限燃烧方块(非地狱岩/岩浆等): 周围无可燃方块且下方不稳固(或 age>3)则熄灭;
     *   age 满 15 且下方不可燃则 1/4 概率熄灭;
     * - 蔓延: 直接点燃/烧毁相邻可燃方块(checkBurnOut) + 在可燃方块相邻的空位生成火(3x3x5 ignite 循环)。
     * 处理末尾若火仍存在则自调度下一次(原版 onPlace/tick 自调度)。
     */
    private static void processFire(DimensionType dim, int x, int y, int z, int state) {
        String name = BlockStateHelper.getName(state);
        if (!"fire".equals(name)) return;

        int below = WorldManager.getBlockState(dim, x, y - 1, z);
        String belowName = BlockStateHelper.getName(below);
        boolean onInfiniburn = belowName.equals("netherrack") || belowName.equals("magma_block")
                || belowName.equals("lava");
        String ageStr = BlockStateHelper.getProp(state, "age");
        int age = ageStr != null ? Integer.parseInt(ageStr) : 0;

        // 雨天露天火熄灭 (原版 0.2 + age*0.03)
        if (Main.isRaining && dim == DimensionType.OVERWORLD && !onInfiniburn) {
            boolean exposed = true;
            for (int sy = y + 1; sy <= y + 4; sy++) {
                int b = WorldManager.getBlockState(dim, x, sy, z);
                if (b != 0 && !"air".equals(BlockStateHelper.getName(b))) { exposed = false; break; }
            }
            if (exposed && FIRE_RND.nextFloat() < 0.2f + age * 0.03f) {
                WorldManager.setBlock(dim, x, y, z, 0);
                NetworkHandler.broadcastBlockChange(dim, x, y, z, 0);
                return;
            }
        }

        // age 推进 (原版: min(15, age + nextInt(3)/2))
        int newAge = Math.min(15, age + FIRE_RND.nextInt(3) / 2);
        if (newAge != age) {
            state = BlockStateHelper.withProp(state, "age", String.valueOf(newAge));
            WorldManager.setBlock(dim, x, y, z, state);
            age = newAge;
        }

        if (!onInfiniburn) {
            // 周围无可燃方块且下方不稳固(或 age>3) → 熄灭 (原版 canSurvive / isValidFireLocation)
            if (!isValidFireLocation(dim, x, y, z)) {
                boolean belowSturdy = BlockStateHelper.isSolidOpaque(below);
                if (!belowSturdy || age > 3) {
                    WorldManager.setBlock(dim, x, y, z, 0);
                    NetworkHandler.broadcastBlockChange(dim, x, y, z, 0);
                    return;
                }
            } else if (age == 15 && FIRE_RND.nextInt(4) == 0 && !isFlammable(belowName)) {
                // age 满 15 且下方不可燃 → 概率熄灭 (原版)
                WorldManager.setBlock(dim, x, y, z, 0);
                NetworkHandler.broadcastBlockChange(dim, x, y, z, 0);
                return;
            }
        }

        // 火焰蔓延
        spreadFire(dim, x, y, z, age);

        // 自调度下一次 (原版 FireBlock.tick 开头 scheduleTick)
        int cur = WorldManager.getBlockState(dim, x, y, z);
        if ("fire".equals(BlockStateHelper.getName(cur))) {
            scheduleFireTick(dim, x, y, z, cur, FIRE_TICK_DELAY + FIRE_RND.nextInt(10));
        }
    }

    /** 火焰蔓延: 1) 直接点燃/烧毁相邻可燃方块; 2) 在可燃方块相邻的空位生成火。对齐原版 FireBlock.tick。 */
    private static void spreadFire(DimensionType dim, int x, int y, int z, int age) {
        // 1) 直接点燃/烧毁相邻可燃方块 (原版 checkBurnOut: burnOdds/n 概率)
        int[][] burnDirs = {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}, {0, 1, 0}, {0, -1, 0}};
        for (int[] d : burnDirs) {
            int nx = x + d[0], ny = y + d[1], nz = z + d[2];
            int nb = WorldManager.getBlockState(dim, nx, ny, nz);
            String nn = BlockStateHelper.getName(nb);
            int odds = flammability(nn);
            if (odds <= 0) continue;
            int n = (d[1] == 0) ? 300 : 250;             // 侧/上 300, 下 250 (原版)
            if (FIRE_RND.nextInt(n) < odds) {
                var burnEvent = com.CharunCore.server.plugin.event.EventManager.INSTANCE.fire(
                        new com.CharunCore.server.plugin.event.events.BlockBurnEvent(nx, ny, nz, nb));
                if (burnEvent.isCancelled()) continue;
                // 5/(age+10) 概率点燃, 否则烧毁 (原版 checkBurnOut)
                if (FIRE_RND.nextInt(age + 10) < 5) {
                    int above = WorldManager.getBlockState(dim, nx, ny + 1, nz);
                    if (above == 0 || "air".equals(BlockStateHelper.getName(above))) {
                        int fs = fireStateAt(dim, nx, ny + 1, nz,
                                Math.min(15, age + FIRE_RND.nextInt(5) / 4));
                        WorldManager.setBlock(dim, nx, ny + 1, nz, fs);
                        NetworkHandler.broadcastBlockChange(dim, nx, ny + 1, nz, fs);
                        scheduleFireTick(dim, nx, ny + 1, nz, fs);
                    }
                } else {
                    WorldManager.setBlock(dim, nx, ny, nz, 0);
                    NetworkHandler.broadcastBlockChange(dim, nx, ny, nz, 0);
                    neighborChanged(dim, nx, ny, nz);     // 烧毁后让相邻流体重算
                }
            }
        }
        // 2) 在空位中蔓延 (原版 3x3x5 ignite 循环: 空格某邻居可燃则概率生成火)
        // #51 修复: 原版 FireBlock 的 3x3x5 循环是 y-1 .. y+4(含下方一格), 曾只 k=0..4(上方)
        // -> 火焰无法向下/侧面下方蔓延, "只能着一面或几面"。
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 4; k++) {
                    if (i == 0 && k == 0 && j == 0) continue;
                    int cx = x + i, cy = y + k, cz = z + j;
                    int cs = WorldManager.getBlockState(dim, cx, cy, cz);
                    if (cs != 0 && !"air".equals(BlockStateHelper.getName(cs))) continue;
                    int ignite = maxIgniteOddsAround(dim, cx, cy, cz);
                    if (ignite <= 0) continue;
                    int n4 = 100 + (k > 1 ? (k - 1) * 100 : 0);
                    int n6 = (ignite + 40) / (age + 30);
                    if (n6 <= 0) continue;
                    if (FIRE_RND.nextInt(n4) <= n6) {
                        int fs = fireStateAt(dim, cx, cy, cz,
                                Math.min(15, age + FIRE_RND.nextInt(5) / 4));
                        WorldManager.setBlock(dim, cx, cy, cz, fs);
                        NetworkHandler.broadcastBlockChange(dim, cx, cy, cz, fs);
                        scheduleFireTick(dim, cx, cy, cz, fs);
                    }
                }
            }
        }
    }

    /** 某格 6 邻居中是否有可燃方块(原版 FireBlock.isValidFireLocation)。 */
    private static boolean isValidFireLocation(DimensionType dim, int x, int y, int z) {
        return maxIgniteOddsAround(dim, x, y, z) > 0;
    }

    /** 返回 pos 周围 6 邻居的最大可燃等级(原版 getIgniteOdds: 空格取其可燃邻居的 ignite 值)。 */
    private static int maxIgniteOddsAround(DimensionType dim, int x, int y, int z) {
        int[][] dirs = {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}, {0, 1, 0}, {0, -1, 0}};
        int max = 0;
        for (int[] d : dirs) {
            int nb = WorldManager.getBlockState(dim, x + d[0], y + d[1], z + d[2]);
            max = Math.max(max, flammability(BlockStateHelper.getName(nb)));
        }
        return max;
    }

    /** 计算给定位置火方块的完整状态: age + 5 个侧面布尔(north/east/south/west/up)。
     *  原版 FireBlock 的侧面布尔由相邻可燃/已燃方块决定, 客户端据此只渲染贴着一面或几面的火
     *  (#30 曾全部用默认态 -> 火焰要么没有侧面要么整块火)。 */
    public static int fireStateAt(DimensionType dim, int x, int y, int z, int age) {
        int base = BlockStateHelper.getDefault("fire");
        int st = age > 0 ? BlockStateHelper.withProp(base, "age", String.valueOf(Math.min(15, age))) : base;
        boolean e = flammableAt(dim, x + 1, y, z);
        boolean w = flammableAt(dim, x - 1, y, z);
        boolean s = flammableAt(dim, x, y, z + 1);
        boolean n = flammableAt(dim, x, y, z - 1);
        boolean u = flammableAt(dim, x, y + 1, z);
        if (e) st = BlockStateHelper.withProp(st, "east", "true");
        if (w) st = BlockStateHelper.withProp(st, "west", "true");
        if (s) st = BlockStateHelper.withProp(st, "south", "true");
        if (n) st = BlockStateHelper.withProp(st, "north", "true");
        if (u) st = BlockStateHelper.withProp(st, "up", "true");
        return st;
    }

    private static boolean flammableAt(DimensionType dim, int x, int y, int z) {
        int st = WorldManager.getBlockState(dim, x, y, z);
        String n = BlockStateHelper.getName(st);
        if (st == 0 || "air".equals(n)) return false;
        if ("fire".equals(n) || "soul_fire".equals(n)) return true;
        return flammability(n) > 0;
    }

    /** 可燃等级: 0 不可燃; >0 为 ignite/burn 概率基准(原版 FireBlock.setFlammable 表简化)。 */
    private static int flammability(String name) {
        if (name == null) return 0;
        // 高可燃 (叶子/羊毛/地毯/植物/花/藤/竹/干草等) ignite~60
        if (name.endsWith("_leaves") || name.endsWith("_wool") || name.endsWith("_carpet")
                || name.equals("vine") || name.equals("bamboo") || name.equals("hay_block")
                || name.equals("dried_kelp_block") || name.endsWith("_banner")
                || name.equals("short_grass") || name.equals("fern") || name.equals("tall_grass")
                || name.equals("dead_bush") || name.equals("dandelion") || name.equals("poppy")
                || name.equals("sunflower") || name.equals("lilac") || name.equals("rose_bush")
                || name.equals("peony") || name.equals("sweet_berry_bush") || name.equals("azalea")
                || name.equals("flowering_azalea") || name.equals("spore_blossom") || name.equals("bush")
                || name.equals("firefly_bush") || name.equals("big_dripleaf") || name.equals("small_dripleaf")
                || name.equals("hanging_roots") || name.equals("cave_vines") || name.equals("cave_vines_plant")
                || name.equals("glow_lichen") || name.equals("pale_moss_block") || name.equals("pale_moss_carpet")
                || name.equals("pale_hanging_moss") || name.equals("azalea_leaves")
                || name.equals("flowering_azalea_leaves") || name.equals("leaf_litter")
                || name.equals("pink_petals") || name.equals("wildflowers"))
            return 60;
        // 木/木板/楼梯/台阶/栅栏/门/书架/工作台/箱/床/煤块/TNT/蜂巢/讲台/堆肥桶等 ignite~5
        if (name.endsWith("_log") || name.endsWith("_wood") || name.endsWith("_planks")
                || name.endsWith("_stairs") || name.endsWith("_slab") || name.endsWith("_fence")
                || name.endsWith("_door") || name.equals("bookshelf") || name.equals("crafting_table")
                || name.equals("chest") || name.endsWith("_bed") || name.equals("target")
                || name.equals("coal_block") || name.equals("tnt") || name.equals("lectern")
                || name.equals("composter") || name.equals("beehive") || name.equals("bee_nest")
                || name.endsWith("_shelf") || name.equals("scaffolding") || name.equals("mangrove_roots"))
            return 5;
        return 0;
    }

    private static boolean isFlammable(String name) {
        return flammability(name) > 0;
    }

    /**
     * #49 钩子: 在某坐标放置方块(覆盖流体)后由 WorldManager.setBlock 调用 —— 清除该格流体
     * 并让周围流动流体重新评估(它们会在 processFluid 中校验水源, 无滋养则干涸)。
     * 注: setBlock 已调用 neighborChanged, 此处额外兜底以防流体残留在非邻居路径上。
     */
    public static void onBlockPlacedIntoFluid(DimensionType dim, int x, int y, int z) {
        int[][] dirs = {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}, {0, -1, 0}, {0, 1, 0}};
        for (int[] d : dirs) {
            int nx = x + d[0], ny = y + d[1], nz = z + d[2];
            int ns = WorldManager.getBlockState(dim, nx, ny, nz);
            if (isWater(ns) || isLava(ns)) scheduleFluidTick(dim, nx, ny, nz, ns);
        }
    }

    /** 当区块从内存卸载时，清理对应的流体扫描记录，防止 scannedChunks 无限增长。 */
    public static void purgeScannedChunks(java.util.Collection<Long> chunkKeys) {
        scannedChunks.removeAll(chunkKeys);
    }

    /** 扫描键带维度序号: 主世界扫描过的 (x,z) 不能屏蔽下界/末地同坐标区块。 */
    private static long chunkScanKey(DimensionType dim, int chunkX, int chunkZ) {
        return ((long) dim.ordinal() << 61) | ((long) (chunkX & 0x3FFFFF) << 30) | (chunkZ & 0x3FFFFFFFL);
    }

    /** 区块卸载时按维度清除扫描记录。 */
    public static void purgeScannedChunk(DimensionType dim, int chunkX, int chunkZ) {
        scannedChunks.remove(chunkScanKey(dim, chunkX, chunkZ));
    }

    private static void processFluid(DimensionType dim, int x, int y, int z, int stateId) {
        String name = BlockStateHelper.getName(stateId);
        if (!"water".equals(name) && !"lava".equals(name)) return;

        // 【模拟距离】只处理玩家附近(128 格)的流体 —— 原版 ticking 区块语义:
        // 远处自然水体不流动, 不会产生大规模"排洪"级联(曾实测 90K 条 0x08/40s)。
        // 玩家放水/挖水时玩家必在附近, 不影响正常交互。
        // 无人时不丢弃: 100 tick 后慢速重试, 玩家回来流体继续流动。
        if (!anyPlayerNearby(dim, x, z, 128)) {
            pendingFluids.add(new long[]{x, y, z, stateId, dim.ordinal(), Main.worldAge + 100});
            return;
        }

        // Re-read current state — it may have changed since scheduling
        int currentState = WorldManager.getBlockState(dim, x, y, z);
        if (currentState == 0) return; // block was removed

        // #49: 流动流体(非水源)若已无更低 level 的同种流体滋养(水源被堵/被方块覆盖),
        // 则干涸清除, 并级联通知外层流动水重新评估 —— 原版流动水每刻由水源重算, 水源没了即停流。
        String lvlProp = BlockStateHelper.getProp(currentState, "level");
        int lvl = lvlProp == null ? 0 : Integer.parseInt(lvlProp);
        if (lvl > 0) {
            if (!hasFluidFeed(dim, x, y, z, name, lvl)) {
                WorldManager.setBlock(dim, x, y, z, 0);
                NetworkHandler.broadcastBlockChange(dim, x, y, z, 0);
                neighborChanged(dim, x, y, z); // 让更外层流动水继续被重算/干涸
                return;
            }
        }

        boolean nether = (dim == DimensionType.THE_NETHER);
        int[][] neighbors = {{x+1,y,z}, {x-1,y,z}, {x,y,z+1}, {x,y,z-1}, {x,y-1,z}, {x,y+1,z}};

        // ── Water + Lava reaction (obsidian / cobblestone) ──────────────────
        // 下界 (nether) 不生成原石/黑曜石 (B5)。
        if (!nether) {
            if ("water".equals(name)) {
                for (int[] nb : neighbors) {
                    int nbState = WorldManager.getBlockState(dim, nb[0], nb[1], nb[2]);
                    if (isLava(nbState)) {
                        boolean lavaSource = "0".equals(BlockStateHelper.getProp(nbState, "level"));
                        int result = BlockStateHelper.getDefault(lavaSource ? "obsidian" : "cobblestone");
                        WorldManager.setBlock(dim, nb[0], nb[1], nb[2], result);
                        NetworkHandler.broadcastBlockChange(dim, nb[0], nb[1], nb[2], result);
                        break;
                    }
                }
                if (WorldManager.getBlockState(dim, x, y, z) != currentState) return; // this water was consumed
            } else if ("lava".equals(name)) {
                for (int[] nb : neighbors) {
                    int nbState = WorldManager.getBlockState(dim, nb[0], nb[1], nb[2]);
                    if (isWater(nbState)) {
                        boolean lavaSource = "0".equals(BlockStateHelper.getProp(currentState, "level"));
                        int result = BlockStateHelper.getDefault(lavaSource ? "obsidian" : "cobblestone");
                        WorldManager.setBlock(dim, x, y, z, result);
                        NetworkHandler.broadcastBlockChange(dim, x, y, z, result);
                        return;
                    }
                }
            }
        }

        String levelProp = BlockStateHelper.getProp(currentState, "level");
        int level = levelProp == null ? 0 : Integer.parseInt(levelProp);
        boolean isSource = level == 0;
        // 原版: 下界岩浆流动 7 格, 主世界岩浆 3 格, 水 7 格。
        int maxLevel = "lava".equals(name) ? (nether ? 7 : 3) : 7;
        // 原版流速: 水 5 tick, 岩浆主世界 30 tick / 下界 10 tick。
        int flowDelay = "water".equals(name) ? FLOW_DELAY_TICKS : (nether ? 10 : 30);

        // ── B3: 无限水 (infinite water source) ──────────────────────────────
        // 流动水(非下落 level=8)若下方有支撑，且水平 4 邻居中 >=2 个为水源，则转化为水源。
        if ("water".equals(name) && level > 0 && level < 8) {
            int below = WorldManager.getBlockState(dim, x, y - 1, z);
            boolean supported = below != 0 && !canDisplace(below); // 下方须为不可被冲走的实体方块/水
            if (supported) {
                int sourceCount = 0;
                int[][] hNeighbor = {{x+1,y,z}, {x-1,y,z}, {x,y,z+1}, {x,y,z-1}};
                for (int[] nb : hNeighbor) {
                    int ns = WorldManager.getBlockState(dim, nb[0], nb[1], nb[2]);
                    if (isWater(ns) && "0".equals(BlockStateHelper.getProp(ns, "level"))) sourceCount++;
                }
                if (sourceCount >= 2) {
                    int srcState = BlockStateHelper.withProp(currentState, "level", "0");
                    WorldManager.setBlock(dim, x, y, z, srcState);
                    NetworkHandler.broadcastBlockChange(dim, x, y, z, srcState);
                    scheduleFluidTick(dim, x, y, z, srcState, flowDelay);
                    return;
                }
            }
        }

        // 1. 向下流 (垂直优先, 原版行为: 下方可流时只向下, 不水平蔓延)。
        //    下落流体为 level=8 (原版 falling): 不是水源, 不可被桶捞起,
        //    不可触发无限水/黑曜石生成。
        boolean flowedDown = false;
        if (isSource || level < maxLevel || level == 8) {
            int below = WorldManager.getBlockState(dim, x, y - 1, z);
            if (canDisplace(below)) { // B4: 允许冲走可替换方块（植物/雪等），不仅是纯空气
                // Bug28: 下方一律生成下落水(level=8)。曾水源向下复制完整水源(level=0)
                // -> 倒一桶水垂直复制出整条源柱(可无限取水, 且源柱彼此互为滋养助长振荡)。
                int downState = BlockStateHelper.withProp(currentState, "level", "8");
                if (below != downState) {
                    WorldManager.setBlock(dim, x, y - 1, z, downState);
                    NetworkHandler.broadcastBlockChange(dim, x, y - 1, z, downState);
                }
                scheduleFluidTick(dim, x, y - 1, z, downState, flowDelay);
                flowedDown = true;
            }
        }
        if (flowedDown) {
            // 原版：向下流后本 tick 仍可水平蔓延（只是优先级低）。
            // 此前 return 导致水源只垂直不水平 → "水柱不扩散"。
            // 折中：水源向下后仍做一次有限水平蔓延；流动水保持原行为。
        }

        // 2. 水平蔓延 —— 流动水 level+1 直到 maxLevel；水源/下落水(8) 落地后以
        //    满强度(level=1) 重新扩散 (原版语义)。
        if (level < maxLevel || level == 8) {
            int nextLevel = (level == 0 || level == 8) ? 1 : level + 1;
            int spreadState = BlockStateHelper.withProp(currentState, "level", String.valueOf(nextLevel));
            int[][] spreadNeighbors = {{x+1,y,z}, {x-1,y,z}, {x,y,z+1}, {x,y,z-1}};
            for (int[] nb : spreadNeighbors) {
                if (canDisplace(WorldManager.getBlockState(dim, nb[0], nb[1], nb[2]))) { // B4
                    // #44: 目标已是该状态则跳过(防振荡期同格反复 set+广播 -> 单块包洪泛)
                    if (WorldManager.getBlockState(dim, nb[0], nb[1], nb[2]) != spreadState) {
                        WorldManager.setBlock(dim, nb[0], nb[1], nb[2], spreadState);
                        NetworkHandler.broadcastBlockChange(dim, nb[0], nb[1], nb[2], spreadState);
                    }
                    scheduleFluidTick(dim, nb[0], nb[1], nb[2], spreadState, flowDelay);
                }
            }
        }
    }

    /**
     * 玩家放置水源后的显式扩散: 向 4 水平方向生成 level=1 流动水并调度。
     * 这样自然水体不排洪(见 processFluid), 而玩家倒水仍能正常扩散成水坑。
     */
    public static void spreadSource(DimensionType dim, int x, int y, int z, int sourceStateId) {
        int level1 = BlockStateHelper.withProp(sourceStateId, "level", "1");
        int[][] nb4 = {{x+1,y,z}, {x-1,y,z}, {x,y,z+1}, {x,y,z-1}};
        for (int[] nb : nb4) {
            int cur = WorldManager.getBlockState(dim, nb[0], nb[1], nb[2]);
            if (canDisplace(cur)) {
                WorldManager.setBlock(dim, nb[0], nb[1], nb[2], level1);
                NetworkHandler.broadcastBlockChange(dim, nb[0], nb[1], nb[2], level1);
                scheduleFluidTick(dim, nb[0], nb[1], nb[2], level1);
            }
        }
    }

    private static boolean anyPlayerNearby(DimensionType dim, int x, int z, int radius) {
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.currentDim != dim) continue;
            int dx = (int) p.x - x;
            int dz = (int) p.z - z;
            if (dx * dx + dz * dz <= radius * radius) return true;
        }
        return false;
    }

    /** 流体是否可流入/冲走该方块：空气，或不可被流体置换的非固体方块（植物/雪等）。流体自身不可被置换 (B4)。 */
    private static boolean canDisplace(int stateId) {
        if (stateId == 0) return true;
        if (isWater(stateId) || isLava(stateId)) return false; // 不覆盖流体
        return !BlockStateHelper.isSolidOpaque(stateId);
    }

    private static boolean isWater(int stateId) {
        return "water".equals(BlockStateHelper.getName(stateId));
    }

    /** #49: 流动流体是否被滋养 —— 6 邻居中存在水源(0)/下落水(8)/更低 level 的同种流体。 */
    private static boolean hasFluidFeed(DimensionType dim, int x, int y, int z, String fluidName, int level) {
        int[][] feed = {{1, 0, 0}, {-1, 0, 0}, {0, 0, 1}, {0, 0, -1}, {0, 1, 0}, {0, -1, 0}};
        for (int[] d : feed) {
            int ns = WorldManager.getBlockState(dim, x + d[0], y + d[1], z + d[2]);
            if (!fluidName.equals(BlockStateHelper.getName(ns))) continue;
            String lp = BlockStateHelper.getProp(ns, "level");
            int nl = lp == null ? 0 : Integer.parseInt(lp);
            // 原版 FlowingFluid: 流动水(level>0)由"任意方向 level 更小"的邻居滋养 ——
            // 水源(0) 自然滋养; 水平相邻 level L-1 滋养 level L(形成 waterfall chain)。
            // 此前水平邻居仅认 nl==0, 导致 level2 认不到 level1 -> 被判定无滋养而删除,
            // 下一刻又被 level1 重新铺出 -> "流动后收回"的抖动(Bug29)。
            // 注意 nl<level 不会产生互保死循环: level 严格递减, 无法成环; 移除水源后
            // 仍会从最外层(level 最大)向内逐刻干涸(见 processFluid 的干涸级联)。
            if (nl == 0) return true;        // 水源直接滋养(任意方向)
            if (nl == 8 && d[1] == 1) return true; // 正上方下落水(瀑布)经竖直列回溯到水源
            if (d[1] == 1 && (nl < level || nl == 8)) return true; // 正上方更低 level 或下落水
            if (d[1] == -1 && nl == 0) return true;  // 正下方水源(静态水柱)
            // Bug28: 水平邻居为下落水(level=8, 瀑布底)也应滋养 level=1 的扩散水。
            // 曾只认 nl<level(8<1 为假) -> 瀑布底铺出的 level1 下一刻被判"无滋养"删除,
            // 下一刻又被母体重铺 -> "流动的水一会流动一会收回"无限横跳。
            if (d[1] == 0 && (nl < level || nl == 8)) return true; // 水平更低 level/下落水滋养
        }
        return false;
    }

    private static boolean isLava(int stateId) {
        return "lava".equals(BlockStateHelper.getName(stateId));
    }
}
