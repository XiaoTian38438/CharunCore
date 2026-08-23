package com.CharunCore.server.world.entity;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.WorldManager;
import com.CharunCore.server.world.gen.RandomSource;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class EndDragonFight {

    private static final DimensionType END = DimensionType.THE_END;
    private static final UUID BOSS_BAR_ID = new UUID(0x59414E524F4E4744L, 0x5241474F4E000001L);

    private static final int PKT_BOSS_EVENT = 0x09;
    private static final int PKT_BLOCK_UPDATE = 0x08;
    private static final int PKT_SYSTEM_CHAT = 0x77;
    private static final int PKT_SET_TITLE = 0x70;
    private static final int PKT_SET_SUBTITLE = 0x6E;
    private static final int PKT_TITLE_ANIM = 0x71;

    private static final Object LOCK = new Object();

    private static volatile boolean islandBuilt = false;
    /** 服务器重启后待恢复的水晶实体标志 (仅重启场景, 玩家战斗中破坏的水晶不重建)。 */
    private static volatile boolean pendingCrystalRestore = false;
    private static volatile boolean dragonKilled = false;
    private static volatile boolean portalOpen = false;
    private static volatile int podiumY = 64;
    private static volatile EnderDragonEntity dragon = null;

    private static final Set<Integer> crystalIds = ConcurrentHashMap.newKeySet();
    private static final Set<NetworkHandler> barViewers = ConcurrentHashMap.newKeySet();
    private static volatile float lastProgress = -1.0f;

    /** 四水晶重生：记录玩家放在祭坛四边立柱顶端的末地水晶坐标。 */
    private static final Set<Long> placedRespawnCrystals = ConcurrentHashMap.newKeySet();
    /** 四水晶重生的四根立柱顶端位置（相对 podiumY 偏移）。 */
    private static final int[][] RESPAWN_PILLARS = {{3, 0}, {-3, 0}, {0, 3}, {0, -3}};
    private static int respawnCheckTimer = 0;

    /** 主岛返程 gateway 的固定位置（被击败后生成于此，出口指向外岛）。 */
    private static final int RETURN_GATEWAY_X = 96;
    private static final int RETURN_GATEWAY_Y = 75;
    private static final int RETURN_GATEWAY_Z = 0;

    /** 外岛散布 gateway 数量（原版 20 个）。 */
    private static final int SCATTER_GATEWAY_COUNT = 20;

    private static final File STATE_FILE = new File("world/end_dragon_state.dat");

    private EndDragonFight() {
    }

    static {
        loadState();
    }

    public static int getPodiumY() {
        return podiumY;
    }

    public static boolean isDragonKilled() {
        return dragonKilled;
    }

    public static EnderDragonEntity getDragon() {
        return dragon;
    }

    public static boolean hasLivingCrystals() {
        for (Integer id : crystalIds) {
            Entity e = EntityManager.getEntity(id);
            if (e != null && e.deathTime == 0) return true;
        }
        return false;
    }


    // =====================================================================
    // PERSISTENCE (B5): 持久化 dragonKilled / islandBuilt, 重启不再重置
    // =====================================================================

    private static void saveState() {
        try {
            File parent = STATE_FILE.getParentFile();
            if (parent != null) parent.mkdirs();
            try (PrintWriter w = new PrintWriter(STATE_FILE)) {
                w.println("islandBuilt=" + islandBuilt);
                w.println("dragonKilled=" + dragonKilled);
            }
        } catch (Exception e) {
            System.err.println("[末地] 状态保存失败: " + e);
        }
    }

    private static void loadState() {
        try {
            if (!STATE_FILE.exists()) return;
            for (String line : Files.readAllLines(STATE_FILE.toPath())) {
                if (line.startsWith("islandBuilt=")) {
                    islandBuilt = Boolean.parseBoolean(line.substring("islandBuilt=".length()));
                } else if (line.startsWith("dragonKilled=")) {
                    dragonKilled = Boolean.parseBoolean(line.substring("dragonKilled=".length()));
                }
            }
            // 服务器重启后: 若岛已建且龙未击败, 水晶实体已随重启丢失 → 标记待恢复
            // (由首次进入末地的玩家触发重建; 玩家战斗中主动破坏的水晶不在此列)。
            if (islandBuilt && !dragonKilled) {
                pendingCrystalRestore = true;
            }
            System.out.println("[末地] 已加载状态 islandBuilt=" + islandBuilt + " dragonKilled=" + dragonKilled
                + " pendingRestore=" + pendingCrystalRestore);
        } catch (Exception e) {
            System.err.println("[末地] 状态加载失败: " + e);
        }
    }

    // =====================================================================
    // ENTRY POINT
    // =====================================================================

    public static void onPlayerEnterEnd(NetworkHandler player) {
        synchronized (LOCK) {
            if (!islandBuilt) {
                try {
                    podiumY = computePodiumY();
                    buildPodium(false);
                    buildSpikes();
                    islandBuilt = true;
                    saveState();
                    System.out.println("[末地] 主岛已构建, 平台 Y=" + podiumY);
                } catch (Exception e) {
                    System.err.println("[末地] 主岛构建失败: " + e);
                }
            } else if (pendingCrystalRestore && !hasLivingCrystals()) {
                // 【修复】只恢复"服务器重启导致的水晶实体丢失"(待恢复标志):
                // 玩家在战斗中主动破坏的水晶原版不会重生 —— 若无条件重建,
                // 玩家破坏全部水晶后重新进末地会刷出 10 颗新水晶, 龙永远被保护。
                try {
                    buildSpikes();
                    System.out.println("[末地] 水晶实体缺失(重启恢复), 已重建");
                } catch (Exception e) {
                    System.err.println("[末地] 水晶重建失败: " + e);
                }
                pendingCrystalRestore = false;
            }
            if (!dragonKilled && (dragon == null || dragon.deathTime > 0)) {
                spawnDragon();
            }
        }
    }

    /**
     * 原版 ServerLevel.makeObsidianPlatform: 玩家从主世界进入末地时的落点平台。
     * 原版是 5x5 黑曜石平台(中心 (100, 50, 0)), 上方 3 格清空。
     * #38 修复: 曾误生成 33x33 巨大平台 -> 改为原版 5x5。
     */
    public static int createObsidianPlatform() {
        int obsidian = BlockStateHelper.getDefault("obsidian");
        int cx = 100, cy = 49, cz = 0;
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                WorldManager.setBlock(END, cx + dx, cy, cz + dz, obsidian);
                for (int dy = 1; dy <= 3; dy++) {
                    if (WorldManager.getBlockState(END, cx + dx, cy + dy, cz + dz) != 0) {
                        WorldManager.setBlock(END, cx + dx, cy + dy, cz + dz, 0);
                    }
                }
            }
        }
        return cy + 1;
    }

    private static void spawnDragon() {
        int base = EntityManager.allocateIdBlock(1 + EntityManager.DRAGON_PART_COUNT);
        EnderDragonEntity d = new EnderDragonEntity(base, podiumY);
        d.dim = END;
        EntityManager.addEntity(d);
        dragon = d;
        lastProgress = -1.0f;
        System.out.println("[末地] 末影龙已生成 id=" + d.id);
    }

    // =====================================================================
    // TERRAIN
    // =====================================================================

    private static int computePodiumY() {
        for (int y = 200; y > 1; y--) {
            int state = WorldManager.getBlockState(END, 0, y, 0);
            if (state == 0) continue;
            String n = BlockStateHelper.getName(state);
            if ("air".equals(n) || "cave_air".equals(n) || "void_air".equals(n)) continue;
            return Math.max(50, Math.min(120, y + 1));
        }
        return 64;
    }

    /**
     * #38 重做: 完全对齐原版 EndPodiumFeature。
     * 结构(origin = 祭坛中心 blockPos):
     *  - y-1 层: 中心 3x3(≤2.5) bedrock, 四角(≤3.5) end_stone;
     *  - y   层: 中心 3x3 end_portal(active) 或 air, 四角 bedrock;
     *  - 中心柱 y..y+3 共 4 块 bedrock(爬上祭坛);
     *  - y+2 处四方向墙火把。
     */
    private static void buildPodium(boolean active) {
        int bedrock = BlockStateHelper.getDefault("bedrock");
        int endStone = BlockStateHelper.getDefault("end_stone");
        int portal = BlockStateHelper.getDefault("end_portal");

        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist > 3.5) continue; // 原版 closerThan(3.5) 才处理
                boolean inner = dist <= 2.5; // 原版 closerThan(2.5)

                // y-1 层: 中心 bedrock, 四角 end_stone
                WorldManager.setBlock(END, dx, podiumY - 1, dz, inner ? bedrock : endStone);
                // y 层: 中心 end_portal/air, 四角 bedrock
                WorldManager.setBlock(END, dx, podiumY, dz, inner ? (active ? portal : 0) : bedrock);
                // y+1.. 清空
                for (int dy = 1; dy <= 8; dy++) {
                    WorldManager.setBlock(END, dx, podiumY + dy, dz, 0);
                }
            }
        }
        // 中心柱 4 块 bedrock (y..y+3)
        for (int i = 0; i < 4; i++) {
            WorldManager.setBlock(END, 0, podiumY + i, 0, bedrock);
        }
        // 四方向墙火把 (y+2)
        for (int i = 0; i < 4; i++) {
            int tx = i == 0 ? 1 : i == 1 ? 0 : i == 2 ? -1 : 0;
            int tz = i == 0 ? 0 : i == 1 ? 1 : i == 2 ? 0 : -1;
            // 原版 WallTorchBlock.FACING = 火把贴墙方向 = 从中心指向火把位置的反向
            String f2 = i == 0 ? "west" : i == 1 ? "north" : i == 2 ? "east" : "south";
            int wt = BlockStateHelper.withProp(BlockStateHelper.getDefault("wall_torch"), "facing", f2);
            WorldManager.setBlock(END, tx, podiumY + 2, tz, wt);
        }
    }

    private static void buildSpikes() {
        // 【修复】重建前清空旧水晶追踪: 多次重建时 crystalIds 累积旧 id,
        // 导致 hasLivingCrystals() 恒 true → 末影龙永远被"幽灵水晶"保护无法击杀。
        crystalIds.clear();
        int obsidian = BlockStateHelper.getDefault("obsidian");
        int bedrock = BlockStateHelper.getDefault("bedrock");
        int ironBars = BlockStateHelper.getDefault("iron_bars");

        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < 10; i++) order.add(i);
        Collections.shuffle(order, new Random(WorldManager.getSeed()));

        int baseY = Math.max(1, podiumY - 24);

        for (int i = 0; i < 10; i++) {
            double a = i * Math.PI * 2.0 / 10.0;
            int sx = (int) Math.round(42.0 * Math.cos(a));
            int sz = (int) Math.round(42.0 * Math.sin(a));
            int j = order.get(i);
            int radius = 2 + j / 3;
            int height = 76 + j * 3;
            boolean guarded = (j == 1 || j == 2);

            for (int dx = -radius; dx <= radius; dx++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    if (dx * dx + dz * dz > radius * radius + 1) continue;
                    for (int y = baseY; y < height; y++) {
                        WorldManager.setBlock(END, sx + dx, y, sz + dz, obsidian);
                    }
                    for (int y = height; y <= height + 6; y++) {
                        WorldManager.setBlock(END, sx + dx, y, sz + dz, 0);
                    }
                }
            }
            WorldManager.setBlock(END, sx, height, sz, bedrock);

            EndCrystalEntity crystal = new EndCrystalEntity(EntityManager.allocateId(),
                    sx + 0.5, height + 1, sz + 0.5);
            crystal.dim = END;
            EntityManager.addEntity(crystal);
            crystalIds.add(crystal.id);

            if (guarded) {
                for (int dx = -2; dx <= 2; dx++) {
                    for (int dz = -2; dz <= 2; dz++) {
                        for (int dy = 0; dy <= 3; dy++) {
                            boolean wall = Math.abs(dx) == 2 || Math.abs(dz) == 2 || dy == 3;
                            if (!wall) continue;
                            WorldManager.setBlock(END, sx + dx, height + 1 + dy, sz + dz, ironBars);
                        }
                    }
                }
            }
        }
    }

    // =====================================================================
    // CALLBACKS
    // =====================================================================

    public static void onCrystalDestroyed(EndCrystalEntity crystal) {
        crystalIds.remove(crystal.id);
        EnderDragonEntity d = dragon;
        if (d != null && d.deathTime == 0) {
            d.damage(10.0f, "crystal");
        }
    }

    public static void onDragonDeath(EnderDragonEntity d) {
        synchronized (LOCK) {
            if (dragonKilled) return;
            dragonKilled = true;
            dragon = d;
            saveState();
        }

        removeBossBarForAll();

        if (!portalOpen) {
            portalOpen = true;
            openExitPortal();
        }

        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.ctx == null || p.currentDim != END) continue;
            // 原版: 末影龙死亡掉落 12000 经验(经验球), 非直接发放
            EntityManager.spawnExperienceOrbs(
                d.x, d.y + 0.5, d.z, 12000, END.id);
            p.sendSoundAt("minecraft:entity.ender_dragon.death", 0.5, podiumY + 10, 0.5, 8.0f, 1.0f);
            sendEndPoem(p);
        }
        broadcastSystem("末影龙已被击败! 末地传送门已开启。", "light_purple");
        System.out.println("[末地] 末影龙已被击败, 出口传送门开启");
    }

    private static void openExitPortal() {
        int portal = BlockStateHelper.getDefault("end_portal");
        int egg = BlockStateHelper.getDefault("dragon_egg");

        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist > 2.6) continue;
                setAndSync(dx, podiumY, dz, portal);
            }
        }
        setAndSync(0, podiumY + 1, 0, egg);
        buildGateway();
    }

    // =====================================================================
    // B7: GATEWAY 生成 / 散布 + 随机出口坐标
    // =====================================================================

    private static void buildGateway() {
        int bedrock = BlockStateHelper.getDefault("bedrock");
        int gateway = BlockStateHelper.getDefault("end_gateway");
        if (gateway <= 0) return;

        // 主岛返程 gateway：出口指向随机外岛（由 NH 通过 randomOuterExit 取得）
        placeGateway(RETURN_GATEWAY_X, RETURN_GATEWAY_Y, RETURN_GATEWAY_Z, bedrock, gateway);

        // 散布 20 个外岛 gateway（原版 EndGatewayFeature），每个出口指向主岛随机点
        for (int i = 0; i < SCATTER_GATEWAY_COUNT; i++) {
            int[] p = gatewayScatterPosition(i);
            placeGateway(p[0], p[1], p[2], bedrock, gateway);
        }
    }

    private static void placeGateway(int x, int y, int z, int bedrock, int gateway) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dz == 0 && dy == 0) continue;
                    setAndSync(x + dx, y + dy, z + dz, bedrock);
                }
            }
        }
        setAndSync(x, y, z, gateway);
    }

    /**
     * 第 i 个散布 gateway 的世界坐标（环绕主岛的均匀环，半径 96）。
     * 供 EndDragonFight 生成与 NetworkHandler 定位使用。
     */
    public static int[] gatewayScatterPosition(int index) {
        double ang = index * (Math.PI * 2.0 / SCATTER_GATEWAY_COUNT) + 0.35;
        int r = 96;
        return new int[]{
                (int) Math.round(Math.cos(ang) * r),
                RETURN_GATEWAY_Y,
                (int) Math.round(Math.sin(ang) * r)
        };
    }

    /** 主岛返程 gateway 的坐标。 */
    public static int[] gatewayReturnPosition() {
        return new int[]{RETURN_GATEWAY_X, RETURN_GATEWAY_Y, RETURN_GATEWAY_Z};
    }

    // -- 随机出口坐标（供 NetworkHandler.teleportViaEndGateway 调用，取代硬编码） --

    private static int[] pickMainExit(Random r) {
        double ang = r.nextDouble() * Math.PI * 2.0;
        double rad = 24.0 + r.nextDouble() * 40.0; // 主岛内、避开祭坛中心的随机环
        int x = (int) Math.round(Math.cos(ang) * rad);
        int z = (int) Math.round(Math.sin(ang) * rad);
        return new int[]{x, getPodiumY() + 1, z};
    }

    private static int[] pickOuterExit(Random r) {
        double ang = r.nextDouble() * Math.PI * 2.0;
        double rad = 96.0 + r.nextDouble() * 904.0; // 96..1000 的外岛范围
        int x = (int) Math.round(Math.cos(ang) * rad);
        int z = (int) Math.round(Math.sin(ang) * rad);
        return new int[]{x, 75, z};
    }

    /** 外岛 gateway → 主岛的随机出口坐标 {x,y,z}。 */
    public static int[] randomGatewayExit(RandomSource random) {
        return pickMainExit(new Random(random.nextLong()));
    }

    /** 外岛 gateway → 主岛的随机出口坐标 {x,y,z}（java.util.Random 重载）。 */
    public static int[] randomGatewayExit(Random random) {
        return pickMainExit(random);
    }

    /** 主岛返程 gateway → 外岛的随机出口坐标 {x,y,z}。 */
    public static int[] randomOuterExit(RandomSource random) {
        return pickOuterExit(new Random(random.nextLong()));
    }

    /** 主岛返程 gateway → 外岛的随机出口坐标 {x,y,z}（java.util.Random 重载）。 */
    public static int[] randomOuterExit(Random random) {
        return pickOuterExit(random);
    }

    private static void setAndSync(int x, int y, int z, int state) {
        WorldManager.setBlock(END, x, y, z, state);
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.ctx == null || p.currentDim != END) continue;
            p.sendPacket(p.ctx, PKT_BLOCK_UPDATE, pb -> {
                pb.writePosition(x, y, z);
                pb.writeVarInt(state);
            });
        }
    }

    // =====================================================================
    // B5: 四水晶重生
    // =====================================================================

    /**
     * 玩家在祭坛四边立柱顶端放置末地水晶时由 NetworkHandler 调用。
     * 当 dragon 已被击败且四根立柱顶端都放有水晶时触发重生。
     */
    public static void onEndCrystalPlaced(int blockX, int blockY, int blockZ) {
        if (!dragonKilled) return;
        int py = getPodiumY() + 4;
        boolean isPillar = (blockY == py) && (
                (blockX == 3 && blockZ == 0) || (blockX == -3 && blockZ == 0) ||
                (blockX == 0 && blockZ == 3) || (blockX == 0 && blockZ == -3));
        if (!isPillar) return;
        placedRespawnCrystals.add(packXZ(blockX, blockZ));
        if (placedRespawnCrystals.size() >= 4) {
            respawnDragon();
        }
    }

    /** 水晶被移除时清理追踪（防止误判）。 */
    public static void onEndCrystalRemoved(int blockX, int blockZ) {
        placedRespawnCrystals.remove(packXZ(blockX, blockZ));
    }

    /**
     * 不依赖 NetworkHandler 的自检：扫描四根立柱顶端附近是否存在 EndCrystalEntity，
     * 集齐四个即重生（可被 NH 的 onEndCrystalPlaced 加速触发）。
     */
    private static void checkRespawn() {
        if (!dragonKilled) return;
        if (++respawnCheckTimer < 20) return;
        respawnCheckTimer = 0;

        int py = getPodiumY() + 4;
        int found = 0;
        Collection<Entity> all = EntityManager.getAllEntities();
        for (int[] p : RESPAWN_PILLARS) {
            double cx = p[0] + 0.5, cz = p[1] + 0.5;
            for (Entity e : all) {
                if (!(e instanceof EndCrystalEntity) || e.deathTime > 0) continue;
                if (Math.abs(e.x - cx) < 1.5 && Math.abs(e.z - cz) < 1.5 && Math.abs(e.y - py) < 2.0) {
                    found++;
                    break;
                }
            }
        }
        if (found >= 4) respawnDragon();
    }

    private static void respawnDragon() {
        synchronized (LOCK) {
            if (!dragonKilled) return;
            removeExitPortal();
            placedRespawnCrystals.clear();
            crystalIds.clear();
            dragonKilled = false;
            portalOpen = false;
            buildSpikes();   // 重新生成尖刺水晶，使战斗可重复
            spawnDragon();
            saveState();
            System.out.println("[末地] 四水晶重生末影龙!");
        }
        broadcastSystem("末影龙被四水晶重新召唤!", "light_purple");
    }

    private static void removeExitPortal() {
        int egg = BlockStateHelper.getDefault("dragon_egg");
        for (int dx = -4; dx <= 4; dx++) {
            for (int dz = -4; dz <= 4; dz++) {
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist <= 2.6) setAndSync(dx, podiumY, dz, 0);
            }
        }
        setAndSync(0, podiumY + 1, 0, 0); // 龙蛋
        // 移除返程 gateway 及其基岩外壳
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = -1; dy <= 1; dy++) {
                    setAndSync(RETURN_GATEWAY_X + dx, RETURN_GATEWAY_Y + dy, RETURN_GATEWAY_Z + dz, 0);
                }
            }
        }
    }

    private static long packXZ(int x, int z) {
        return ((long) x & 0xFFFFFFFFL) | (((long) z & 0xFFFFFFFFL) << 32);
    }

    // =====================================================================
    // BOSS BAR
    // =====================================================================

    public static void tick() {
        EnderDragonEntity d = dragon;
        if (d != null && (d.deathTime > 0 || EntityManager.getEntity(d.id) == null)) {
            if (!dragonKilled) {
                dragon = null;
            }
        }

        checkRespawn();

        d = dragon;
        boolean alive = d != null && !dragonKilled && d.deathTime == 0;

        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.ctx == null) continue;
            boolean shouldSee = alive && p.currentDim == END;
            boolean seeing = barViewers.contains(p);
            if (shouldSee && !seeing) {
                barViewers.add(p);
                sendBossAdd(p, d);
            } else if (!shouldSee && seeing) {
                barViewers.remove(p);
                sendBossRemove(p);
            }
        }

        if (!alive) return;

        float progress = Math.max(0.0f, Math.min(1.0f, d.health / d.maxHealth));
        if (Math.abs(progress - lastProgress) < 0.001f) return;
        lastProgress = progress;
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.ctx == null || !barViewers.contains(p)) continue;
            p.sendPacket(p.ctx, PKT_BOSS_EVENT, pb -> {
                pb.writeUUID(BOSS_BAR_ID);
                pb.writeVarInt(2);
                pb.writeFloat(progress);
            });
        }
    }

    private static void sendBossAdd(NetworkHandler p, EnderDragonEntity d) {
        float progress = Math.max(0.0f, Math.min(1.0f, d.health / d.maxHealth));
        org.cloudburstmc.nbt.NbtMap name = org.cloudburstmc.nbt.NbtMap.builder()
                .putString("text", "末影龙")
                .putString("color", "light_purple")
                .build();
        p.sendPacket(p.ctx, PKT_BOSS_EVENT, pb -> {
            pb.writeUUID(BOSS_BAR_ID);
            pb.writeVarInt(0);
            pb.writeAnonymousNbt(name);
            pb.writeFloat(progress);
            pb.writeVarInt(0);
            pb.writeVarInt(0);
            pb.writeByte(0x04);
        });
    }

    private static void sendBossRemove(NetworkHandler p) {
        p.sendPacket(p.ctx, PKT_BOSS_EVENT, pb -> {
            pb.writeUUID(BOSS_BAR_ID);
            pb.writeVarInt(1);
        });
    }

    private static void removeBossBarForAll() {
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.ctx == null) continue;
            if (!barViewers.remove(p)) continue;
            sendBossRemove(p);
        }
        barViewers.clear();
    }

    public static void onPlayerDisconnect(NetworkHandler p) {
        barViewers.remove(p);
    }

    // =====================================================================
    // END POEM / CREDITS (B12, best-effort)
    // =====================================================================

    private static void sendEndPoem(NetworkHandler p) {
        org.cloudburstmc.nbt.NbtMap title = org.cloudburstmc.nbt.NbtMap.builder()
                .putString("text", "自由的末路")
                .putString("color", "light_purple")
                .build();
        org.cloudburstmc.nbt.NbtMap sub = org.cloudburstmc.nbt.NbtMap.builder()
                .putString("text", "末影龙已被击败")
                .putString("color", "gray")
                .build();

        p.sendPacket(p.ctx, PKT_TITLE_ANIM, pb -> {
            pb.writeInt(10);
            pb.writeInt(80);
            pb.writeInt(20);
        });
        p.sendPacket(p.ctx, PKT_SET_TITLE, pb -> pb.writeAnonymousNbt(title));
        p.sendPacket(p.ctx, PKT_SET_SUBTITLE, pb -> pb.writeAnonymousNbt(sub));

        String[] poem = {
                "我看到了那个玩家。",
                "它在读我们的思想, 就像在读文字一样。",
                "那正是我们所构筑的, 一个可以被阅读的世界。",
                "它已经赢了。踏入传送门, 回到属于它的世界吧。"
        };
        for (String line : poem) {
            sendSystem(p, line, "gray");
        }
    }

    /**
     * B12 (best-effort): 触发末地结局。原版在玩家走入出口传送门后展示 credits 画面并授予
     * minecraft:the_end。本服无 advancement 下发系统，故以"传送回主世界"近似 credits 结果，
     * 并提示 the_end 成就。由 NetworkHandler 在玩家踏入出口传送门且 dragonKilled 时调用。
     */
    public static void triggerEndCredits(NetworkHandler p) {
        if (p == null || p.ctx == null) return;
        grantTheEndAdvancement(p);
        p.teleportToDimension(p.ctx, DimensionType.OVERWORLD);
    }

    private static void grantTheEndAdvancement(NetworkHandler p) {
        // best-effort：本服暂无 advancement 下发机制，仅作提示（原版应授予 minecraft:the_end）
        sendSystem(p, "§6[成就] the_end — 自由之末路（本服暂无 advancement 系统，已近似处理）", "gold");
    }

    private static void sendSystem(NetworkHandler p, String text, String color) {
        org.cloudburstmc.nbt.NbtMap comp = org.cloudburstmc.nbt.NbtMap.builder()
                .putString("text", text)
                .putString("color", color)
                .build();
        p.sendPacket(p.ctx, PKT_SYSTEM_CHAT, pb -> {
            pb.writeAnonymousNbt(comp);
            pb.writeBoolean(false);
        });
    }

    private static void broadcastSystem(String text, String color) {
        for (NetworkHandler p : NetworkHandler.players.values()) {
            if (p.ctx == null) continue;
            sendSystem(p, text, color);
        }
    }
}
