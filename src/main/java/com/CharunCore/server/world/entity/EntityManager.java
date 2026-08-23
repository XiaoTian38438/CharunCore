package com.CharunCore.server.world.entity;

import com.CharunCore.server.Main;
import com.CharunCore.server.world.ExplosionEngine;
import com.CharunCore.server.world.light.LightEngine;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.WorldManager;

import java.io.FileReader;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;

public class EntityManager {
    private static final Map<Integer, Entity> entities = new ConcurrentHashMap<>();
    private static final Map<NetworkHandler, Set<Integer>> trackingFor = new ConcurrentHashMap<>();
    private static int nextId = 1000;
    private static final Random spawnRng = new Random();

    private static final double TRACK_RANGE = 64.0;
    private static final double TRACK_RANGE_SQ = TRACK_RANGE * TRACK_RANGE;

    /** 全服实体上限（防止内存泄漏/OOM）。超过此数不再生成新实体。 */
    private static final int MAX_ENTITIES = 2000;

    public static final int DRAGON_PART_COUNT = 8;
    private static final Map<Integer, Integer> partOwner = new ConcurrentHashMap<>();

    private static final Map<String, Integer> ENTITY_TYPE_IDS = new HashMap<>();
    static {
        try {
            String path = Paths.get("data", "entities.json").toString();
            JsonArray array = JsonParser.parseReader(new FileReader(path)).getAsJsonArray();
            for (JsonElement el : array) {
                JsonObject obj = el.getAsJsonObject();
                int id = obj.get("id").getAsInt();
                String name = obj.get("name").getAsString();
                ENTITY_TYPE_IDS.put(name, id);
            }
            System.out.println("[数据] 已加载 " + ENTITY_TYPE_IDS.size() + " 个实体类型定义");
        } catch (Exception e) {
            System.err.println("[警告] 无法加载 entities.json: " + e.getMessage());
        }
    }

    public static int allocateId() {
        return nextId++;
    }

    public static synchronized int allocateIdBlock(int count) {
        int base = nextId;
        nextId += count;
        return base;
    }

    public static void addEntity(Entity e) {
        // 实体数超限时不再加入（玩家和末影龙除外，防止 OOM）
        if (entities.size() >= MAX_ENTITIES && !(e instanceof EnderDragonEntity)) {
            return;
        }
        entities.put(e.id, e);
        if (e instanceof EnderDragonEntity) {
            for (int i = 1; i <= DRAGON_PART_COUNT; i++) partOwner.put(e.id + i, e.id);
        }
    }

    /**
     * 客户端会为末影龙本地生成 8 个子部件, 其 entity id = 龙ID+1..龙ID+8。
     * 玩家攻击龙的头/身体时发来的是部件 ID, 这里映射回龙本体。
     */
    public static Entity resolveInteractTarget(int id) {
        Entity e = entities.get(id);
        if (e != null) return e;
        Integer owner = partOwner.get(id);
        return owner == null ? null : entities.get(owner);
    }

    public static void removeEntity(int id) {
        Entity e = entities.remove(id);
        if (e == null) return;
        if (e instanceof EnderDragonEntity) {
            for (int i = 1; i <= DRAGON_PART_COUNT; i++) partOwner.remove(e.id + i);
        }
        for (NetworkHandler handler : NetworkHandler.players.values()) {
            Set<Integer> seen = trackingFor.get(handler);
            if (seen == null || !seen.remove(id)) continue;
            if (handler.ctx == null) continue;
            handler.sendPacket(handler.ctx, 0x4B, pb -> {
                pb.writeVarInt(1);
                pb.writeVarInt(id);
            });
        }
    }

    /** 只读访问全部实体(繁殖配对搜索用)。 */
    public static java.util.Map<Integer, Entity> getEntities() {
        return entities;
    }

    /** 两只同种可繁殖生物配对成功 → 在二者中点生成幼体。 */
    public static void spawnBaby(MobEntity a, MobEntity b) {
        double bx = (a.x + b.x) * 0.5 + 0.5;
        double by = Math.max(a.y, b.y);
        double bz = (a.z + b.z) * 0.5 + 0.5;
        MobEntity baby = new MobEntity(allocateId(), a.entityName, bx, by, bz);
        baby.dim = a.dim;
        baby.baby = true;
        baby.width *= 0.5;
        baby.height *= 0.5;
        baby.health = baby.maxHealth;
        addEntity(baby);
        broadcastBaby(baby);
    }

    /** 同步幼体标记(isBaby 元数据索引 17, BOOLEAN 序列化器 id 8, 1.21.11)。 */
    public static void broadcastBaby(MobEntity mob) {
        for (NetworkHandler handler : NetworkHandler.players.values()) {
            if (handler.ctx == null || handler.currentDim != mob.dim) continue;
            Set<Integer> seen = trackingFor.get(handler);
            if (seen == null || !seen.contains(mob.id)) continue;
            handler.sendPacket(handler.ctx, 0x61, pb -> {
                pb.writeVarInt(mob.id);
                pb.writeByte(17);
                pb.writeVarInt(8);
                pb.writeBoolean(mob.baby);
                pb.writeByte(0xFF);
            });
        }
    }

    public static void onPlayerDisconnect(NetworkHandler handler) {
        trackingFor.remove(handler);
    }

    public static void onPlayerChangeDimension(NetworkHandler handler) {
        Set<Integer> seen = trackingFor.remove(handler);
        if (seen != null) seen.clear();
    }

    public static Collection<Entity> getAllEntities() {
        return entities.values();
    }

    public static Entity getEntity(int id) {
        return entities.get(id);
    }

    public static int countInDimension(DimensionType dim, Class<?> type) {
        int n = 0;
        for (Entity e : entities.values()) {
            if (e.dim == dim && type.isInstance(e)) n++;
        }
        return n;
    }

    /**
     * P4-4: 在指定位置生成可拾取的经验球实体(按原版面额拆分算法)。
     * 替代击杀时直接 addExperience 的写法, 使经验可被附近玩家争夺、被经验修补消耗。
     * NetworkHandler 击杀 mob 时应调用本方法代替直接 addExperience。
     *
     * @param dim 维度 id(对应 {@link DimensionType#id})
     */
    public static void spawnExperienceOrbs(double x, double y, double z, int amount, int dim) {
        spawnExperienceOrbs(x, y, z, amount, DimensionType.byId(dim));
    }

    /** 维度重载版本(直接传入 DimensionType)。 */
    public static void spawnExperienceOrbs(double x, double y, double z, int amount, DimensionType dim) {
        if (dim == null || amount <= 0) return;
        Random rng = new Random();
        int guard = 0;
        while (amount > 0 && guard++ < 120) {
            int v = amount >= 2477 ? 2477 : amount >= 1237 ? 1237 : amount >= 617 ? 617
                      : amount >= 307 ? 307 : amount >= 149 ? 149 : amount >= 73 ? 73
                      : amount >= 37 ? 37 : amount >= 17 ? 17 : amount >= 7 ? 7
                      : amount >= 3 ? 3 : 1;
            amount -= v;
            ExperienceOrbEntity orb = new ExperienceOrbEntity(allocateId(),
                x + (rng.nextDouble() - 0.5) * 1.5,
                y + rng.nextDouble() * 0.5,
                z + (rng.nextDouble() - 0.5) * 1.5, v);
            orb.dim = dim;
            orb.vx = (rng.nextDouble() - 0.5) * 0.1;
            orb.vy = rng.nextDouble() * 0.15;
            orb.vz = (rng.nextDouble() - 0.5) * 0.1;
            addEntity(orb);
        }
    }

    /** 在指定位置生成物品掉落实体（携带者与维度由调用方设置）。 */
    public static void spawnItemDrop(double x, double y, double z, int itemId, int count) {
        if (itemId <= 0 || count <= 0) return;
        ItemEntity drop = new ItemEntity(allocateId(), x, y, z, itemId, count);
        addEntity(drop);
    }

    /** 广播实体元数据(0x61)给跟踪它的所有玩家。 */
    public static void broadcastMetadata(Entity e) {
        for (NetworkHandler handler : NetworkHandler.players.values()) {
            if (handler.ctx == null || handler.currentDim != e.dim) continue;
            Set<Integer> seen = trackingFor.get(handler);
            if (seen == null || !seen.contains(e.id)) continue;
            if (e instanceof ItemFrameEntity frame) {
                handler.sendPacket(handler.ctx, 0x61, pb -> {
                    pb.writeVarInt(e.id);
                    pb.writeByte(8);
                    pb.writeVarInt(7);
                    pb.writeSlot(frame.itemId, frame.itemCount);
                    pb.writeByte(0xFF);
                });
            } else if (e instanceof MobEntity mob) {
                handler.sendPacket(handler.ctx, 0x61, pb -> {
                    pb.writeVarInt(e.id);
                    pb.writeByte(9);
                    pb.writeVarInt(3);
                    pb.writeFloat(mob.health);
                    pb.writeByte(0xFF);
                });
            }
        }
    }

    public static void tick() {
        java.util.List<Entity> toExplode = null;
        for (Entity e : entities.values()) {
            try {
                e.tick();
            } catch (Exception ex) {
                System.err.println("[实体] tick 异常 id=" + e.id + ": " + ex);
            }
            if ("tnt".equals(e.typeName) && e.tntFuse >= 0) {
                e.tntFuse--;
                if (e.tntFuse <= 0) {
                    if (toExplode == null) toExplode = new java.util.ArrayList<>();
                    toExplode.add(e);
                }
            }
        }
        if (toExplode != null) {
            for (Entity tnt : toExplode) {
                removeEntity(tnt.id);
                ExplosionEngine.explode(tnt.dim, tnt.x, tnt.y + 0.5, tnt.z, 4.0f, true);
            }
        }
        syncTracking();

        if (spawnRng.nextInt(8) == 0) {
            trySpawnHostileMobs();
        }
        if (spawnRng.nextInt(200) == 0) {
            trySpawnPassiveMobs();
        }
        despawnMobs();
    }

    /** 自然消失: 远离所有玩家的敌对生物按原版节奏消失(>128 格立即消失, 32~128 格随机消失)。 */
    private static void despawnMobs() {
        for (Entity e : new java.util.ArrayList<>(entities.values())) {
            if (!(e instanceof MobEntity m) || !m.isHostile()) continue;
            double nearest = Double.MAX_VALUE;
            for (NetworkHandler p : NetworkHandler.players.values()) {
                if (p.isDead) continue;
                double dx = e.x - p.x, dy = e.y - p.y, dz = e.z - p.z;
                double d = dx * dx + dy * dy + dz * dz;
                if (d < nearest) nearest = d;
            }
            if (nearest > 128 * 128) {
                removeEntity(e.id);
                continue;
            }
            if (nearest > 32 * 32) {
                m.despawnCounter++;
                // 约 30 秒后开始按概率消失(每刻 1/20)
                if (m.despawnCounter > 600 && spawnRng.nextInt(20) == 0) removeEntity(e.id);
            } else {
                m.despawnCounter = 0;
            }
        }
    }

    public static boolean hasSkyAccess(Entity e) {
        int bx = (int) Math.floor(e.x);
        int bz = (int) Math.floor(e.z);
        int top = e.dim.minY + e.dim.height - 1;
        for (int y = (int) Math.ceil(e.y) + 1; y <= top; y++) {
            // BUG8/BUG7: 用缓存只读, 避免生物 tick 时因未加载区块触发同步生成而卡死主线程
            int state = WorldManager.getBlockStateCached(e.dim, bx, y, bz);
            if (state == 0) continue;
            String n = BlockStateHelper.getName(state);
            if ("air".equals(n) || "cave_air".equals(n) || "void_air".equals(n)) continue;
            return false;
        }
        return true;
    }

    private static int surfaceLightLevel() {
        long dayTime = Main.dayTime;
        if (dayTime < 12500 || dayTime > 23000) return 15;
        if (dayTime < 13000 || dayTime > 22500) return 10;
        return 0;
    }

    /** 天空光衰减量(仿原版 skyDarken): 白天 0, 黄昏/黎明 5, 夜晚 11。 */
    private static int skyDarkenNow(DimensionType dim) {
        if (!dim.hasSkylight) return 0;
        long t = Main.dayTime;
        if (t < 12500 || t > 23000) return 0;
        if (t < 13000 || t > 22500) return 5;
        return 11;
    }

    private static final java.util.Set<String> LIGHT_EMITTERS = java.util.Set.of(
        "torch", "wall_torch", "glowstone", "lantern", "campfire", "soul_campfire",
        "redstone_torch", "redstone_wall_torch", "end_rod", "sea_pickle",
        "lava", "lava_cauldron", "shroomlight", "sea_lantern");

    /** 地下刷怪点周围是否有发光方块(近似光照检测, 使火把/荧石等能阻止刷怪)。 */
    private static boolean isLitNearby(DimensionType dim, int x, int y, int z) {
        for (int dy = -1; dy <= 2; dy++)
            for (int dx = -2; dx <= 2; dx++)
                for (int dz = -2; dz <= 2; dz++) {
                    int s = WorldManager.getBlockState(dim, x + dx, y + dy, z + dz);
                    if (s == 0) continue;
                    String n = BlockStateHelper.getName(s);
                    if (n != null && LIGHT_EMITTERS.contains(n)) return true;
                }
        return false;
    }

    /**
     * 在 (wx,wz) 列中找一个合法刷怪 Y 返回(脚底 Y)。
     * 原实现从世界顶向下扫描、只返回"列最高点", 导致主世界只在山顶刷、下界刷在基岩天花板。
     * 现改为: 从玩家参考高度(或随机高度)向下扫描寻找合法地表(包括地下洞穴),
     * 并对 hasCeiling 维度(下界)自地面向上兜底, 避免刷在天花板上。
     */
    private static int findSpawnY(DimensionType dim, int wx, int wz, double refY) {
        int minY = dim.minY;
        int top = dim.minY + dim.height - 1;
        int start = (int) Math.min(top - 2, refY + 8.0);
        // 一半概率从整列随机高度开始, 增加洞穴/山体侧面刷怪多样性
        if (spawnRng.nextBoolean()) {
            start = minY + 2 + spawnRng.nextInt(Math.max(1, dim.height - 4));
        }
        if (start < minY + 2) start = minY + 2;
        for (int y = start; y >= minY; y--) {
            if (validSpawnGround(dim, wx, y, wz)) return y + 1;
        }
        if (dim.hasCeiling) {
            for (int y = minY; y < top; y++) {
                if (validSpawnGround(dim, wx, y, wz)) return y + 1;
            }
        }
        return -1;
    }

    /** 合法刷怪地表: y 处为实心非液体方块, 且 y+1/y+2 为空气(可站立)。 */
    private static boolean validSpawnGround(DimensionType dim, int x, int y, int z) {        int s = WorldManager.getBlockState(dim, x, y, z);
        if (s == 0) return false;
        String n = BlockStateHelper.getName(s);
        if (n == null) return false;
        if (n.equals("air") || n.equals("cave_air") || n.equals("void_air")) return false;
        if (n.equals("water") || n.equals("lava")) return false;
        if (!isAir(WorldManager.getBlockState(dim, x, y + 1, z))) return false;
        if (!isAir(WorldManager.getBlockState(dim, x, y + 2, z))) return false;
        return true;
    }

    /** #39: 末地末影人生成净空 — 脚底(sy-1 已由 validSpawnGround 保证实心)上方 sy..sy+2 共 3 层、每层 3x3 全为空气。 */
    private static boolean endSpawnSpace(DimensionType dim, int sx, int sy, int sz) {
        for (int dy = 0; dy <= 2; dy++)
            for (int dx = -1; dx <= 1; dx++)
                for (int dz = -1; dz <= 1; dz++)
                    if (!isAir(WorldManager.getBlockState(dim, sx + dx, sy + dy, sz + dz))) return false;
        return true;
    }

    private static boolean isAir(int state) {
        if (state == 0) return true;
        String n = BlockStateHelper.getName(state);
        return n != null && (n.equals("air") || n.equals("cave_air") || n.equals("void_air"));
    }

    private static void trySpawnHostileMobs() {
        for (NetworkHandler player : NetworkHandler.players.values()) {
            // #39 修复: 原版创造/旁观模式生物照常生成(只是不主动攻击这些玩家)。
            // 曾跳过 gameMode 1/3 -> 创造模式测试末地时末影人永远不生成。
            if (player.isDead) continue;
            DimensionType dim = player.currentDim;

            int cap = hostileCapFor(dim);
            int mobCount = 0;
            for (Entity e : entities.values()) {
                if (!(e instanceof MobEntity m) || !m.isHostile() || e.dim != dim) continue;
                double dx = e.x - player.x;
                double dz = e.z - player.z;
                if (dx * dx + dz * dz < 128 * 128) mobCount++;
            }
            if (mobCount > cap) continue;

            // P4-5: 每个触发允许刷多只(而非固定 1 只), 但受 per-chunk 上限约束
            int maxSpawns = Math.min(4, Math.max(1, (cap - mobCount) / 8 + 1));
            int done = 0;
            for (int attempt = 0; attempt < maxSpawns * 4 && done < maxSpawns; attempt++) {
                int angle = spawnRng.nextInt(360);
                int dist = 24 + spawnRng.nextInt(40); // 24~64 格
                int sx = (int) (player.x + Math.cos(Math.toRadians(angle)) * dist);
                int sz = (int) (player.z + Math.sin(Math.toRadians(angle)) * dist);
                if (!WorldManager.isChunkCached(dim, ((long) (sx >> 4) << 32) | ((sz >> 4) & 0xFFFFFFFFL))) continue;

                int sy = findSpawnY(dim, sx, sz, player.y);
                if (sy < 0) continue;

                int blockL = LightEngine.blockLight(dim, sx, sy + 1, sz);
                int skyL = LightEngine.skyLight(dim, sx, sy + 1, sz);
                int light = Math.max(blockL, skyL - skyDarkenNow(dim));
                // 末地特例: 末影人无视光照生成(原版 EndSpawner 行为, 末地中央岛天空光=15 恒亮)。
                if (light > 7 && dim != DimensionType.THE_END) continue;

                if (hostileCountInChunk(dim, sx >> 4, sz >> 4) >= 8) continue; // per-chunk 上限

                String[] pool = hostilePoolFor(dim, biomeAt(dim, sx, sz));
                String type = pool[spawnRng.nextInt(pool.length)];
                // #39: 末地末影人需 solid ground + 上方 3x3x3 净空(光照已由末地特例豁免)。
                if (dim == DimensionType.THE_END && "enderman".equals(type)
                        && !endSpawnSpace(dim, sx, sy, sz)) continue;
                MobEntity mob = new MobEntity(allocateId(), type, sx + 0.5, sy, sz + 0.5);
                mob.dim = dim;
                addEntity(mob);
                done++;
            }
        }
    }

    private static void trySpawnPassiveMobs() {
        for (NetworkHandler player : NetworkHandler.players.values()) {
            if (player.isDead) continue;
            DimensionType dim = player.currentDim;
            if (dim != DimensionType.OVERWORLD) continue;

            int count = 0;
            for (Entity e : entities.values()) {
                if (!(e instanceof MobEntity m) || !m.isPassive() || e.dim != dim) continue;
                double dx = e.x - player.x;
                double dz = e.z - player.z;
                if (dx * dx + dz * dz < 96 * 96) count++;
            }
            if (count > 16) continue;

            // P4-8: 水生(鱿鱼)刷怪
            trySpawnAquatic(player);

            String[] pool = {"cow", "sheep", "pig", "chicken"};
            String type = pool[spawnRng.nextInt(pool.length)];
            int herd = type.equals("chicken") ? 4 : 2 + spawnRng.nextInt(3);

            int angle = spawnRng.nextInt(360);
            int dist = 24 + spawnRng.nextInt(40);
            int baseX = (int) (player.x + Math.cos(Math.toRadians(angle)) * dist);
            int baseZ = (int) (player.z + Math.sin(Math.toRadians(angle)) * dist);

            for (int i = 0; i < herd; i++) {
                int sx = baseX + spawnRng.nextInt(7) - 3;
                int sz = baseZ + spawnRng.nextInt(7) - 3;
                if (!WorldManager.isChunkCached(dim, ((long) (sx >> 4) << 32) | ((sz >> 4) & 0xFFFFFFFFL))) continue;
                int sy = findSpawnY(dim, sx, sz, player.y);
                if (sy < 0) continue;
                int ground = WorldManager.getBlockState(dim, sx, sy - 1, sz);
                String gname = BlockStateHelper.getName(ground);
                if (!"grass_block".equals(gname)) continue;

                MobEntity mob = new MobEntity(allocateId(), type, sx + 0.5, sy, sz + 0.5);
                mob.dim = dim;
                addEntity(mob);
            }
        }
    }

    // ── P4-5 / P4-8 辅助方法 ───────────────────────────────────────────────

    private static int hostileCapFor(DimensionType dim) {
        return switch (dim) {
            case THE_NETHER -> 40;
            case THE_END -> 30;
            default -> 50;
        };
    }

    private static int hostileCountInChunk(DimensionType dim, int cx, int cz) {
        int n = 0;
        for (Entity e : entities.values()) {
            if (!(e instanceof MobEntity m) || !m.isHostile() || e.dim != dim) continue;
            if (((int) Math.floor(e.x) >> 4) == cx && ((int) Math.floor(e.z) >> 4) == cz) n++;
        }
        return n;
    }

    private static int biomeAt(DimensionType dim, int wx, int wz) {
        // BUG7: 用缓存只读, 避免刷怪时因未加载区块触发同步生成而卡死主 tick 线程
        var chunk = WorldManager.getChunkCached(dim, wx >> 4, wz >> 4);
        if (chunk == null) return 0;
        return chunk.getBiome(wx & 15, wz & 15);
    }

    /** 按维度 + 生物群系(项目自有 biome id)选择敌对种群权重池。 */
    private static String[] hostilePoolFor(DimensionType dim, int biomeId) {
        if (dim == DimensionType.THE_NETHER)
            return new String[]{"zombified_piglin", "magma_cube", "blaze", "ghast"};
        if (dim == DimensionType.THE_END)
            return new String[]{"enderman", "enderman", "enderman"};
        return switch (biomeId) {
            case 14 -> new String[]{"husk", "husk", "zombie", "spider", "skeleton"};       // 沙漠
            case 46, 48, 26 -> new String[]{"stray", "stray", "zombie", "spider"};          // 雪原/冰刺
            case 54, 31 -> new String[]{"zombie", "skeleton", "slime", "spider", "witch"};  // 沼泽/红树沼泽
            case 28, 50, 1 -> new String[]{"zombie", "spider", "skeleton"};                 // 丛林
            case 35, 29, 58, 22, 6, 9, 11, 12 -> new String[]{"drowned", "drowned", "zombie"}; // 海洋
            default -> new String[]{"zombie", "zombie", "skeleton", "creeper", "spider", "enderman"};
        };
    }

    /** P4-8: 在玩家附近水体中生成鱿鱼群。 */
    private static void trySpawnAquatic(NetworkHandler player) {
        if (spawnRng.nextInt(4) != 0) return; // 降低频率
        DimensionType dim = player.currentDim;
        int aquaticCount = 0;
        for (Entity e : entities.values()) {
            if (e instanceof MobEntity m && "squid".equals(m.entityName) && e.dim == dim) {
                double dx = e.x - player.x, dz = e.z - player.z;
                if (dx * dx + dz * dz < 64 * 64) aquaticCount++;
            }
        }
        if (aquaticCount > 6) return;
        for (int attempt = 0; attempt < 6; attempt++) {
            int angle = spawnRng.nextInt(360);
            int dist = 20 + spawnRng.nextInt(40);
            int sx = (int) (player.x + Math.cos(Math.toRadians(angle)) * dist);
            int sz = (int) (player.z + Math.sin(Math.toRadians(angle)) * dist);
            if (findWaterY(dim, sx, sz, player.y) < 0) continue;
            int group = 2 + spawnRng.nextInt(3);
            for (int i = 0; i < group; i++) {
                int ox = sx + spawnRng.nextInt(5) - 2;
                int oz = sz + spawnRng.nextInt(5) - 2;
                int oy = findWaterY(dim, ox, oz, player.y);
                if (oy < 0) continue;
                MobEntity mob = new MobEntity(allocateId(), "squid", ox + 0.5, oy, oz + 0.5);
                mob.dim = dim;
                addEntity(mob);
            }
            return;
        }
    }

    /** 在水中寻找合法生成 Y: y/y+1 为水, y-1 为固体(非水)。 */
    private static int findWaterY(DimensionType dim, int wx, int wz, double refY) {
        int minY = dim.minY;
        int top = dim.minY + dim.height - 1;
        int start = (int) Math.min(top - 2, refY + 6.0);
        if (start < minY + 2) start = minY + 2;
        for (int y = start; y >= minY; y--) {
            if (!isWater(dim, wx, y, wz)) continue;
            if (!isWater(dim, wx, y + 1, wz)) continue;
            if (isWater(dim, wx, y - 1, wz)) continue; // 下方须为固体
            return y;
        }
        return -1;
    }

    private static boolean isWater(DimensionType dim, int x, int y, int z) {
        return "water".equals(BlockStateHelper.getName(WorldManager.getBlockState(dim, x, y, z)));
    }

    private static void syncTracking() {
        // 先在每实体上统一计算本 tick 的移动增量(再对各玩家广播), 避免 prev 在第一个玩家处已归零。
        for (Entity e : entities.values()) {
            e.dxMove = e.x - e.prevX;
            e.dyMove = e.y - e.prevY;
            e.dzMove = e.z - e.prevZ;
            // 仅当位移过大(>4 格)或首次同步(prevX 全 0)才用 full teleport(0x23);
            // 朝向变化由 entity_move_look(0x34) 携带, 不再触发全量传送。
            boolean tp = Math.abs(e.dxMove) > 4.0 || Math.abs(e.dyMove) > 4.0 || Math.abs(e.dzMove) > 4.0
                || (e.prevX == 0.0 && e.prevY == 0.0 && e.prevZ == 0.0);
            e.needsTeleport = tp;
            e.needHeadRot = (e.yaw != e.prevYaw);
        }

        for (NetworkHandler handler : NetworkHandler.players.values()) {
            if (handler.ctx == null) continue;
            Set<Integer> seen = trackingFor.computeIfAbsent(handler, k -> ConcurrentHashMap.newKeySet());

            for (Entity e : entities.values()) {
                // #37 末影龙: 主岛盘旋半径可达 100+ 格, 远超普通实体 64 格跟踪范围。
                // 曾按统一 TRACK_RANGE=64 -> 玩家进末地后龙不在跟踪内, spawn 包永不发送,
                // 只见 boss bar 血条看不见龙模型。对龙用大范围跟踪(覆盖整个主岛)。
                double rangeSq = (e instanceof EnderDragonEntity) ? 256.0 * 256.0 : TRACK_RANGE_SQ;
                boolean inRange = e.dim == handler.currentDim
                    && sqDist(e, handler) <= rangeSq;

                if (inRange && seen.add(e.id)) {
                    sendSpawn(handler, e);
                } else if (!inRange && seen.remove(e.id)) {
                    final int rid = e.id;
                    handler.sendPacket(handler.ctx, 0x4B, pb -> {
                        pb.writeVarInt(1);
                        pb.writeVarInt(rid);
                    });
                }

                if (!inRange) continue;
                sendMove(handler, e);
            }

            seen.removeIf(id -> !entities.containsKey(id));
        }

        for (Entity e : entities.values()) {
            e.prevX = e.x; e.prevY = e.y; e.prevZ = e.z;
            e.prevYaw = e.yaw; e.prevPitch = e.pitch;
        }
    }

    private static double sqDist(Entity e, NetworkHandler h) {
        double dx = e.x - h.x;
        double dz = e.z - h.z;
        return dx * dx + dz * dz;
    }

    private static void sendMove(NetworkHandler handler, Entity e) {
        // 原版: 正常实体移动用轻量增量包, 远比 sync_entity_position(0x23) 全量双精度传送轻量
        // (曾误用 0x23 -> 每实体每 tick 发 54 字节 X 数百实体 => 客户端网络线程堵塞, FPS 骤降)。
        // dxMove/dyMove/dzMove 由 syncTracking 在外层统一计算, 此处直接广播不修改。
        // 完全静止且朝向无变化且已同步过 -> 不发任何包(进一步减负)。
        if (!e.needsTeleport && !e.needHeadRot && e.yaw == e.prevYaw && e.pitch == e.prevPitch
                && e.dxMove == 0.0 && e.dyMove == 0.0 && e.dzMove == 0.0 && e.syncedOnce) {
            return;
        }
        if (e.needsTeleport) {
            handler.sendPacket(handler.ctx, 0x23, pb -> {
                pb.writeVarInt(e.id);
                pb.writeDouble(e.x); pb.writeDouble(e.y); pb.writeDouble(e.z);
                pb.writeDouble(e.vx); pb.writeDouble(e.vy); pb.writeDouble(e.vz);
                pb.writeFloat(e.yaw); pb.writeFloat(e.pitch);
                pb.writeBoolean(e.onGround);
            });
        } else if (e.yaw != e.prevYaw || e.pitch != e.prevPitch) {
            // 移动 + 朝向都变化: entity_move_look(0x34) 合并增量位置与朝向
            handler.sendPacket(handler.ctx, 0x34, pb -> {
                pb.writeVarInt(e.id);
                pb.writeShort((int) (e.dxMove * 4096.0));
                pb.writeShort((int) (e.dyMove * 4096.0));
                pb.writeShort((int) (e.dzMove * 4096.0));
                pb.writeByte((byte) (int) (e.yaw * 256.0f / 360.0f));
                pb.writeByte((byte) (int) (e.pitch * 256.0f / 360.0f));
                pb.writeBoolean(e.onGround);
            });
        } else {
            // 仅位置变化: rel_entity_move(0x33)
            handler.sendPacket(handler.ctx, 0x33, pb -> {
                pb.writeVarInt(e.id);
                pb.writeShort((int) (e.dxMove * 4096.0));
                pb.writeShort((int) (e.dyMove * 4096.0));
                pb.writeShort((int) (e.dzMove * 4096.0));
                pb.writeBoolean(e.onGround);
            });
        }
        // 头旋转(0x51) 仅在 yaw 变化时发送
        if (e.needHeadRot) {
            handler.sendPacket(handler.ctx, 0x51, pb -> {
                pb.writeVarInt(e.id);
                pb.writeByte((byte) (int) (e.yaw * 256.0f / 360.0f));
            });
        }
        e.syncedOnce = true;
    }

    private static void sendSpawn(NetworkHandler handler, Entity e) {
        int typeId = getEntityTypeId(e);
        int frameFacing = (e instanceof ItemFrameEntity frame) ? frame.facing : 0;
        handler.sendPacket(handler.ctx, 0x01, pb -> {
            pb.writeVarInt(e.id);
            pb.writeUUID(uuidFor(e.id));
            pb.writeVarInt(typeId);
            pb.writeDouble(e.x);
            pb.writeDouble(e.y);
            pb.writeDouble(e.z);
            pb.writeLpVec3(e.vx, e.vy, e.vz);
            pb.writeByte((byte) (int) (e.pitch * 256.0f / 360.0f));
            pb.writeByte((byte) (int) (e.yaw * 256.0f / 360.0f));
            pb.writeByte((byte) (int) (e.yaw * 256.0f / 360.0f));
            int spawnData = frameFacing; // item_frame: data=facing
            if (e instanceof FishingBobberEntity fbe) spawnData = fbe.ownerEid; // 钓竿浮标: data=拥有者 entity id
            pb.writeVarInt(spawnData);
        });

        if (e instanceof ItemFrameEntity frame) {
            // 元数据: Item (index 8, Slot)
            handler.sendPacket(handler.ctx, 0x61, pb -> {
                pb.writeVarInt(e.id);
                pb.writeByte(8);
                pb.writeVarInt(7);
                pb.writeSlot(frame.itemId, frame.itemCount);
                pb.writeByte(0xFF);
            });
        } else if (e instanceof ItemEntity item) {
            handler.sendPacket(handler.ctx, 0x61, pb -> {
                pb.writeVarInt(e.id);
                pb.writeByte(8);
                pb.writeVarInt(7);
                pb.writeSlot(item.itemId, item.count);
                pb.writeByte(0xFF);
            });
        } else if ("tnt".equals(e.typeName)) {
            // 引爆 TNT: FUSE 元数据(index 8, INT), 客户端据此渲染闪烁与缩放
            handler.sendPacket(handler.ctx, 0x61, pb -> {
                pb.writeVarInt(e.id);
                pb.writeByte(8);
                pb.writeVarInt(1);
                pb.writeVarInt(Math.max(0, e.tntFuse));
                pb.writeByte(0xFF);
            });
        } else if (e instanceof MobEntity mob) {
            handler.sendPacket(handler.ctx, 0x61, pb -> {
                pb.writeVarInt(e.id);
                // Entry 1: flags (index 0, byte) — 含 ON_FIRE 位(0x01), 使燃烧中的生物一出生就显示火焰
                pb.writeByte(0);
                pb.writeVarInt(0);
                pb.writeByte(mob.fireTicks > 0 ? (byte) 0x01 : (byte) 0x00);
                // Entry 2: health (index 9, float)
                pb.writeByte(9);
                pb.writeVarInt(3);
                pb.writeFloat(mob.health);
                pb.writeByte(0xFF);
            });
        }
    }

    /** BUG8: 向所有追踪该实体的客户端广播"着火"状态位(index 0 的 0x01 位)。
     *  否则即便服务端已点燃 fireTicks, 客户端也看不到生物在燃烧(只掉血不显示火焰)。 */
    public static void broadcastEntityFire(Entity e, boolean onFire) {
        for (NetworkHandler handler : NetworkHandler.players.values()) {
            if (handler.ctx == null || handler.currentDim != e.dim) continue;
            Set<Integer> seen = trackingFor.get(handler);
            if (seen == null || !seen.contains(e.id)) continue;
            handler.sendPacket(handler.ctx, 0x61, pb -> {
                pb.writeVarInt(e.id);
                pb.writeByte(0);    // metadata index 0
                pb.writeVarInt(0);  // type = byte
                pb.writeByte(onFire ? (byte) 0x01 : (byte) 0x00); // 0x01 = ON_FIRE
                pb.writeByte(0xFF); // end of metadata
            });
        }
    }

    /** 死亡动画 + 死亡音效: 0x22 status=3 (LivingEntity.DEATH), 客户端播放死亡动画。 */
    public static void broadcastEntityDeath(Entity e) {
        String deathSound = null;
        if (e instanceof MobEntity mob) {
            String n = mob.entityName;
            if (n != null) {
                deathSound = switch (n) {
                    case "zombie", "husk", "zombie_villager", "drowned" -> "minecraft:entity.zombie.death";
                    case "skeleton", "stray", "wither_skeleton" -> "minecraft:entity.skeleton.death";
                    case "creeper" -> "minecraft:entity.creeper.death";
                    case "spider", "cave_spider" -> "minecraft:entity.spider.death";
                    case "enderman" -> "minecraft:entity.enderman.death";
                    case "witch" -> "minecraft:entity.witch.death";
                    default -> null;
                };
            }
        }
        final String snd = deathSound;
        for (NetworkHandler handler : NetworkHandler.players.values()) {
            if (handler.ctx == null || handler.currentDim != e.dim) continue;
            Set<Integer> seen = trackingFor.get(handler);
            if (seen == null || !seen.contains(e.id)) continue;
            handler.sendPacket(handler.ctx, 0x22, pb -> {
                pb.writeInt(e.id);
                pb.writeByte(3);
            });
            if (snd != null) {
                handler.sendSoundAt(snd, e.x, e.y + 1.0, e.z, 1.0f, 1.0f);
            }
        }
    }

    /** 广播音效给"看到该实体"的所有玩家(0x73 named sound)。 */
    public static void broadcastSoundAt(Entity e, String sound, float volume, float pitch) {
        for (NetworkHandler handler : NetworkHandler.players.values()) {
            if (handler.ctx == null || handler.currentDim != e.dim) continue;
            Set<Integer> seen = trackingFor.get(handler);
            if (seen == null || !seen.contains(e.id)) continue;
            handler.sendSoundAt(sound, e.x, e.y + 1.0, e.z, volume, pitch);
        }
    }

    /** 生物攻击动画: 0x22 entity_status status=4 (LivingEntity.ATTACK), 客户端播放攻击动作。 */
    public static void broadcastEntityAttack(Entity e) {
        for (NetworkHandler handler : NetworkHandler.players.values()) {
            if (handler.ctx == null || handler.currentDim != e.dim) continue;
            Set<Integer> seen = trackingFor.get(handler);
            if (seen == null || !seen.contains(e.id)) continue;
            handler.sendPacket(handler.ctx, 0x22, pb -> {
                pb.writeInt(e.id);
                pb.writeByte(4);
            });
        }
    }

    public static void broadcastEntityHurt(Entity e) {
        for (NetworkHandler handler : NetworkHandler.players.values()) {
            if (handler.ctx == null || handler.currentDim != e.dim) continue;
            Set<Integer> seen = trackingFor.get(handler);
            if (seen == null || !seen.contains(e.id)) continue;
            handler.sendPacket(handler.ctx, 0x22, pb -> {
                pb.writeInt(e.id);
                pb.writeByte(2);
            });
            // damage_event(0x19): 1.19.4+ 客户端据此播放受击方向倾斜动画
            NetworkHandler attacker = e instanceof LivingEntity le && le.lastAttacker != null
                    ? le.lastAttacker : null;
            final boolean hasAttacker = attacker != null;
            final int attackerEid = hasAttacker ? attacker.eid : 0;
            final double sx = hasAttacker ? attacker.x : e.x;
            final double sz = hasAttacker ? attacker.z : e.z;
            handler.sendPacket(handler.ctx, 0x19, pb -> {
                pb.writeVarInt(e.id);
                pb.writeVarInt(0);
                pb.writeVarInt(hasAttacker ? attackerEid + 1 : 0);
                pb.writeVarInt(hasAttacker ? attackerEid + 1 : 0);
                if (!hasAttacker) {
                    pb.writeBoolean(true);
                    pb.writeDouble(sx); pb.writeDouble(e.y + 1.0); pb.writeDouble(sz);
                } else {
                    pb.writeBoolean(false);
                }
            });
            if (e instanceof MobEntity mob) {
                handler.sendPacket(handler.ctx, 0x61, pb -> {
                    pb.writeVarInt(e.id);
                    pb.writeByte(9);
                    pb.writeVarInt(3);
                    pb.writeFloat(Math.max(0, mob.health));
                    pb.writeByte(0xFF);
                });
                // 受伤音效: 按实体类型播放对应 minecraft:entity.<type>.hurt
                String hurtSound = mobHurtSound(mob.entityName);
                if (hurtSound != null) {
                    handler.sendSoundAt(hurtSound, e.x, e.y + 1.0, e.z, 1.0f, 1.0f);
                }
            }
        }
    }

    /** 苦力怕膨胀状态元数据(index 16 INT): 1=膨胀中, -1=待机。客户端据此播放膨胀动画。 */
    public static void broadcastCreeperSwell(Entity e, int state) {
        for (NetworkHandler handler : NetworkHandler.players.values()) {
            if (handler.ctx == null || handler.currentDim != e.dim) continue;
            Set<Integer> seen = trackingFor.get(handler);
            if (seen == null || !seen.contains(e.id)) continue;
            handler.sendPacket(handler.ctx, 0x61, pb -> {
                pb.writeVarInt(e.id);
                pb.writeByte(16);
                pb.writeVarInt(1);
                pb.writeVarInt(state);
                pb.writeByte(0xFF);
            });
        }
    }

    /** 各类生物的受伤音效(缺失时回退通用 hurt)。 */
    private static String mobHurtSound(String name) {
        if (name == null) return "minecraft:entity.generic.hurt";
        return switch (name) {
            case "zombie", "husk", "zombie_villager", "drowned" -> "minecraft:entity.zombie.hurt";
            case "skeleton", "stray", "wither_skeleton" -> "minecraft:entity.skeleton.hurt";
            case "creeper" -> "minecraft:entity.creeper.hurt";
            case "spider", "cave_spider" -> "minecraft:entity.spider.hurt";
            case "enderman" -> "minecraft:entity.enderman.hurt";
            case "witch" -> "minecraft:entity.witch.hurt";
            case "slime" -> "minecraft:entity.slime.hurt";
            case "pig", "cow", "sheep", "chicken", "horse", "wolf", "cat", "rabbit", "bat", " villager" -> "minecraft:entity.generic.hurt";
            default -> "minecraft:entity.generic.hurt";
        };
    }

    private static UUID uuidFor(int entityId) {
        return new UUID(0x5941_4E52_4F4E_4700L, entityId & 0xFFFFFFFFL);
    }

    private static int getEntityTypeId(Entity e) {
        if (e instanceof ItemEntity) return 71;
        if (e instanceof MobEntity mob) {
            return entityTypeId(mob.entityName);
        }
        if (e.typeName != null) {
            return entityTypeId(e.typeName);
        }
        return 71;
    }

    public static int entityTypeId(String name) {
        Integer v = ENTITY_TYPE_IDS.get(name);
        if (v != null) return v;
        return switch (name) {
            case "allay" -> 2;
            case "armor_stand" -> 5;
            case "arrow" -> 6;
            case "trident" -> 135;
            case "bat" -> 10;
            case "blaze" -> 14;
            case "cave_spider" -> 22;
            case "chicken" -> 26;
            case "cod" -> 27;
            case "cow" -> 30;
            case "creeper" -> 32;
            case "drowned" -> 38;
            case "elder_guardian" -> 40;
            case "enderman" -> 41;
            case "endermite" -> 42;
            case "ender_dragon" -> 43;
            case "ender_pearl" -> 44;
            case "end_crystal" -> 45;
            case "evoker" -> 46;
            case "experience_orb" -> 49;
            case "eye_of_ender" -> 50;
            case "falling_block" -> 51;
            case "fireball" -> 52;
            case "fishing_bobber" -> 156;
            case "fox" -> 54;
            case "chest_minecart" -> 25;
            case "command_block_minecart" -> 29;
            case "ghast" -> 57;
            case "furnace_minecart" -> 56;
            case "hopper_minecart" -> 65;
            case "minecart" -> 85;
            case "tnt_minecart" -> 133;
            case "goat" -> 62;
            case "guardian" -> 63;
            case "hoglin" -> 64;
            case "horse" -> 66;
            case "husk" -> 67;
            case "iron_golem" -> 70;
            case "item" -> 71;
            case "item_frame" -> 73;
            case "lightning_bolt" -> 77;
            case "llama" -> 78;
            case "magma_cube" -> 80;
            case "mooshroom" -> 86;
            case "ocelot" -> 91;
            case "panda" -> 96;
            case "parrot" -> 98;
            case "phantom" -> 99;
            case "pig" -> 100;
            case "piglin" -> 101;
            case "piglin_brute" -> 102;
            case "pillager" -> 103;
            case "polar_bear" -> 104;
            case "rabbit" -> 108;
            case "ravager" -> 109;
            case "salmon" -> 110;
            case "sheep" -> 111;
            case "shulker" -> 112;
            case "silverfish" -> 114;
            case "skeleton" -> 115;
            case "slime" -> 117;
            case "small_fireball" -> 118;
            case "snowball" -> 120;
            case "snow_golem" -> 121;
            case "spider" -> 124;
            case "squid" -> 127;
            case "stray" -> 128;
            case "strider" -> 129;
            case "tnt" -> 132;
            case "turtle" -> 137;
            case "vex" -> 138;
            case "villager" -> 139;
            case "vindicator" -> 140;
            case "warden" -> 142;
            case "witch" -> 144;
            case "wither" -> 145;
            case "wither_skeleton" -> 146;
            case "wither_skull" -> 147;
            case "wolf" -> 148;
            case "zoglin" -> 149;
            case "zombie" -> 150;
            case "zombie_villager" -> 153;
            case "zombified_piglin" -> 154;
            case "player" -> 155;
            default -> 150;
        };
    }
}
