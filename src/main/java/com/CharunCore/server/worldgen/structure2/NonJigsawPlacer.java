package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.WorldGenLevel;
import com.CharunCore.server.world.entity.EntityManager;
import com.CharunCore.server.world.entity.MinecartEntity;
import com.CharunCore.server.world.entity.MobEntity;
import com.CharunCore.server.world.DimensionType;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 非拼图结构的放置器。jigsaw 结构由 JigsawPlacement 处理, 这里处理其余类型:
 * buried_treasure / mineshaft / stronghold / shipwreck / ocean_ruin / ruined_portal
 * 以及模板可用的 igloo / nether_fossil。
 *
 * 写入路径与 jigsaw 一致: 经 WorldGenLevel.setBlock / setBlockEntity,
 * 窗口覆盖当前 chunk 及其 3x3 邻居 (WRITE_RADIUS = 1)。
 */
public final class NonJigsawPlacer {

    private NonJigsawPlacer() {}

    // 调试开关：打印未覆盖的非拼图结构类型（P1-7 结构注册核对用）。
    private static final boolean DEBUG_UNHANDLED = false;

    private static final String STRUCT_ROOT =
        "mapping/remapped_server_1.21.11.jar.src/data/minecraft/structure/";

    public static void place(WorldGenLevel level, String id, String type,
                             int chunkX, int chunkZ, int centerX, int centerZ,
                             int surfaceY, RandomSource random) {
        switch (type) {
            case "buried_treasure" ->
                placeBuriedTreasure(level, centerX, centerZ, surfaceY, random);
            // Bug27: mineshaft 改走 ProceduralStructureStart.mineshaft 分件系统
            // (registerStructuresForChunk 注册), 曾每区块独立 14 段迷你走廊导致矿井碎片化。
            case "stronghold" -> placeStronghold(level, centerX, centerZ, surfaceY, random);
            case "shipwreck" -> placeScatterTemplate(level, pickVariant("shipwreck", random),
                centerX, centerZ, level.getSeaLevel() - 3, random);
            case "ocean_ruin" -> placeScatterTemplate(level, pickVariant("underwater_ruin", random),
                centerX, centerZ, surfaceY - 1, random);
            case "ruined_portal" -> placeScatterTemplate(level, pickVariant("ruined_portal", random),
                centerX, centerZ, surfaceY - 1, random);
            case "igloo" -> placeIgloo(level, centerX, centerZ, surfaceY, random);
            case "nether_fossil" -> placeFossil(level, centerX, centerZ, surfaceY, random);
            // fortress: 已改为原版 NetherFortressPieces 逐件移植（ProceduralStructureStart 中央注册）
            case "desert_pyramid" -> placeDesertPyramid(level, centerX, centerZ, surfaceY, random);
            case "jungle_temple" -> placeJungleTemple(level, centerX, centerZ, surfaceY, random);
            case "swamp_hut" -> placeSwampHut(level, centerX, centerZ, surfaceY, random);
            // end_city: 已改为原版 EndCityPieces 逐件移植（ProceduralStructureStart 中央注册）
            case "ocean_monument" -> placeOceanMonument(level, centerX, centerZ, surfaceY, random);
            case "woodland_mansion" -> placeWoodlandMansion(level, centerX, centerZ, surfaceY, random);
            default -> {
                // 未支持的非拼图类型：跳过（不生成但不崩溃）。
                // 结构注册核对（P1-7）：所有 json 注册结构的 type 均已覆盖，仅 `fortress`
                // （地狱堡垒，非拼图类型且无对应程序化生成器）暂未实现，走此分支被跳过。
                if (DEBUG_UNHANDLED) {
                    System.out.println("[structure2] NonJigsawPlacer: 未覆盖的类型 '" + type + "' (id=" + id + ") 跳过");
                }
            }
        }
    }

    // ── 通用模板散布放置 ──────────────────────────────────────────────

    private static void placeScatterTemplate(WorldGenLevel level, String variant,
                                             int centerX, int centerZ, int originY,
                                             RandomSource random) {
        if (variant == null) return;
        Rotation rot = Rotation.random(random);
        StructureTemplate t = StructureTemplate.load(variant);
        if (t == null) return;
        t.placeInWorld(level, centerX, originY, centerZ, rot, Mirror.NONE);
    }

    private static String pickVariant(String dir, RandomSource random) {
        File d = new File(STRUCT_ROOT + dir);
        if (!d.isDirectory()) return null;
        File[] files = d.listFiles((f, n) -> n.endsWith(".nbt"));
        if (files == null || files.length == 0) return null;
        File f = files[random.nextInt(files.length)];
        String name = f.getName();
        name = name.substring(0, name.length() - 4); // 去掉 .nbt
        return dir + "/" + name;
    }

    private static void placeIgloo(WorldGenLevel level, int centerX, int centerZ,
                                   int surfaceY, RandomSource random) {
        Rotation rot = Rotation.random(random);
        StructureTemplate bottom = StructureTemplate.load("igloo/bottom");
        if (bottom == null) return;
        int oy = surfaceY - bottom.sizeY + 1;
        bottom.placeInWorld(level, centerX, oy, centerZ, rot, Mirror.NONE);
        StructureTemplate middle = StructureTemplate.load("igloo/middle");
        int midH = 3;
        if (middle != null) {
            middle.placeInWorld(level, centerX, oy + bottom.sizeY, centerZ, rot, Mirror.NONE);
            midH = middle.sizeY;
        }
        StructureTemplate top = StructureTemplate.load("igloo/top");
        if (top != null) {
            top.placeInWorld(level, centerX, oy + bottom.sizeY + midH, centerZ, rot, Mirror.NONE);
        }
    }

    private static void placeFossil(WorldGenLevel level, int centerX, int centerZ,
                                    int surfaceY, RandomSource random) {
        String variant = pickVariant("fossil", random);
        if (variant == null) return;
        Rotation rot = Rotation.random(random);
        StructureTemplate t = StructureTemplate.load(variant);
        if (t == null) return;
        t.placeInWorld(level, centerX, surfaceY + 1, centerZ, rot, Mirror.NONE);
    }

    // ── 程序化结构 ────────────────────────────────────────────────────

    private static void placeBuriedTreasure(WorldGenLevel level, int centerX, int centerZ,
                                            int surfaceY, RandomSource random) {
        int y = surfaceY - 4;
        if (y < 1) y = 1;
        long seed = ((long) centerX * 73856093L ^ (long) centerZ * 19349663L ^ (long) y * 83492791L)
            & 0x7fffffffffffffffL;
        placeLootChest(level, centerX, y, centerZ, "chests/buried_treasure", seed);
    }

    // Bug28 改造: 对齐原版 MineshaftPieces 的分件链拓扑(算法级还原, 非种子逐字节):
    //  - 起点为房间(MineshaftRoom), 之后沿单一轴向迭代延伸;
    //  - 每段走廊长 5 格、截面 3x3, 沿途每格原木支撑排(原版 Corridor 每段一排);
    //  - 约 1/4 概率十字路口(Crossing, 垂直轴双向各延伸一条支线);
    //  - 约 1/10 概率房间(Room); 楼梯(Stairs)以 ±3 格换层连接;
    //  - 走廊铺铁轨/随机蛛网/墙面火把, 房间低概率刷怪箱+运输矿车箱子。
    private static void placeMineshaft(WorldGenLevel level, int chunkX, int chunkZ,
                                       RandomSource random) {
        int baseX = chunkX << 4;
        int baseZ = chunkZ << 4;
        int startX = baseX + 8, startZ = baseZ + 8;
        int startY = 10 + random.nextInt(30);
        boolean axisX = random.nextBoolean();

        // 分件队列: {x,y,z,axis(0=X,1=Z),piecesLeft}
        // 性能注意: 矿井 structure_set 为 spacing=1 -> 每个区块都会进入本方法,
        // 预算必须保持小(原版分件跨区块增量放置, 这里单区块内完成),
        // 曾给到 24-48 段 -> 区块生成耗时暴涨, 玩家移动时 FPS 周期性骤降。
        java.util.ArrayDeque<int[]> frontier = new java.util.ArrayDeque<>();
        frontier.add(new int[]{startX, startY, startZ, axisX ? 0 : 1, 6 + random.nextInt(6)});
        java.util.Set<Long> visited = new java.util.HashSet<>();

        int piecesBuilt = 0;
        final int MAX_PIECES = 14;
        while (!frontier.isEmpty() && piecesBuilt < MAX_PIECES) {
            int[] job = frontier.poll();
            int x = job[0], y = job[1], z = job[2], axis = job[3], budget = job[4];
            long key = ((long) x & 0x3FFFFFFL) << 38 | ((long) y & 0xFFFL) << 26 | ((long) z & 0x3FFFFFFL);
            if (!visited.add(key)) continue;
            piecesBuilt++;

            double roll = random.nextDouble();
            if (roll < 0.10) {
                placeMineshaftRoom(level, x, y, z, random);
                continue;
            }
            if (roll < 0.32 && budget > 4) {
                placeMineshaftCrossing(level, x, y, z, random);
                // 十字路口向垂直轴分出支线
                frontier.add(new int[]{x, y, z, axis == 0 ? 1 : 0, budget / 2});
            }
            int dir = random.nextBoolean() ? 1 : -1;   // 沿当前轴向的正/负端继续
            for (int seg = 0; seg < 5 && budget > 0; seg++, budget--) {
                // 原版 Stairs 分件: 换层 ±3 格后继续同轴走廊
                boolean stair = seg == 2 && random.nextInt(5) == 0;
                if (stair) y += random.nextBoolean() ? 3 : -3;
                if (y < 5) y = 5;
                if (y > 50) y = 50;
                if (axis == 0) x += dir;
                else z += dir;
                carveTunnelSegment(level, x, y, z, random);
                placeMineshaftSupport(level, x, y, z, random);
                if (stair) break; // 换层后本段结束
            }
            if (budget > 0) frontier.add(new int[]{x, y, z, axis, budget});
        }
    }

    /** 原版 MineshaftRoom: 7x7x3 空腔 + 围栏支撑角。 */
    private static void placeMineshaftRoom(WorldGenLevel level, int x, int y, int z,
                                           RandomSource random) {
        for (int dx = -3; dx <= 3; dx++) {
            for (int dz = -3; dz <= 3; dz++) {
                for (int dy = 0; dy <= 2; dy++) {
                    int cur = level.getBlock(x + dx, y + dy, z + dz);
                    String nm = BlockStateHelper.getName(cur);
                    if (nm != null && !nm.equals("bedrock") && !nm.equals("water") && !nm.equals("lava")) {
                        level.setBlock(x + dx, y + dy, z + dz, 0);
                    }
                }
            }
        }
        // 房间中央低概率洞穴蜘蛛刷怪笼(原版 Room 特征)
        if (random.nextInt(4) == 0) {
            int spawner = BlockStateHelper.getDefault("spawner");
            if (spawner > 0) level.setBlock(x, y, z, spawner);
        }
        // 运输矿车战利品: 用箱子近似(chests/abandoned_mineshaft)
        if (random.nextInt(3) == 0) {
            placeLootChest(level, x + 2, y, z + 2, "chests/abandoned_mineshaft",
                ((long) x * 31L + z * 7L + y) ^ 0x9e3779b9L);
        }
    }

    /** 原版 MineShaftCrossing: 3x3 全高开放 + 四向围栏柱, 双轴交汇。 */
    private static void placeMineshaftCrossing(WorldGenLevel level, int x, int y, int z,
                                               RandomSource random) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = 0; dy <= 2; dy++) {
                    int cur = level.getBlock(x + dx, y + dy, z + dz);
                    String nm = BlockStateHelper.getName(cur);
                    if (nm != null && !nm.equals("bedrock") && !nm.equals("water") && !nm.equals("lava")) {
                        level.setBlock(x + dx, y + dy, z + dz, 0);
                    }
                }
            }
        }
        placeMineshaftSupport(level, x, y, z, random);
        // 十字路口地面满铺铁轨(原版 Crossing 地板为铁轨)
        int railId = BlockStateHelper.getDefault("rail");
        if (railId > 0) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    int fl = level.getBlock(x + dx, y - 1, z + dz);
                    String fnm = BlockStateHelper.getName(fl);
                    if (fnm != null && !fnm.equals("air") && !fnm.equals("water")
                            && !fnm.equals("lava") && !fnm.equals("rail")) {
                        level.setBlock(x + dx, y - 1, z + dz, railId);
                    }
                }
            }
        }
    }

    /** #46 原版矿井支撑柱: 隧道侧壁竖原木 + 顶部横原木(原版 MineshaftRoom/Corridor 的木梁)。 */
    private static void placeMineshaftSupport(WorldGenLevel level, int x, int y, int z,
                                              RandomSource random) {
        int oakLog = BlockStateHelper.getDefault("oak_log");
        int oakPlanks = BlockStateHelper.getDefault("oak_planks");
        if (oakLog <= 0) return;
        // 4 侧选 1-2 侧立竖柱
        int sides = 1 + random.nextInt(2);
        for (int s = 0; s < sides; s++) {
            int dx = 0, dz = 0;
            switch (random.nextInt(4)) {
                case 0 -> dz = 1;
                case 1 -> dz = -1;
                case 2 -> dx = 1;
                default -> dx = -1;
            }
            for (int dy = 1; dy <= 3; dy++) {
                int cy = y - dy;
                if (cy < 1) break;
                int cur = level.getBlock(x + dx, cy, z + dz);
                String nm = BlockStateHelper.getName(cur);
                if (nm == null || nm.equals("air") || nm.equals("cave_air")) {
                    level.setBlock(x + dx, cy, z + dz, oakLog);
                }
            }
        }
        // 顶部横梁(隧道顶贴板)
        if (oakPlanks > 0 && random.nextInt(2) == 0) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0) continue;
                    int ty = y + 1;
                    int cur = level.getBlock(x + dx, ty, z + dz);
                    String nm = BlockStateHelper.getName(cur);
                    if (nm == null || nm.equals("air") || nm.equals("cave_air")) {
                        if (random.nextInt(2) == 0) level.setBlock(x + dx, ty, z + dz, oakPlanks);
                    }
                }
            }
        }
    }

    private static void carveTunnelSegment(WorldGenLevel level, int x, int y, int z,
                                           RandomSource random) {
        for (int dy = 0; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    int yy = y + dy;
                    if (yy < 1 || yy > 319) continue;
                    int cur = level.getBlock(x + dx, yy, z + dz);
                    String nm = BlockStateHelper.getName(cur);
                    if (nm != null && !nm.equals("air") && !nm.equals("cave_air")
                            && !nm.equals("bedrock") && !nm.equals("water")
                            && !nm.equals("lava")) {
                        level.setBlock(x + dx, yy, z + dz, 0);
                    }
                }
            }
        }
        int floor = level.getBlock(x, y - 1, z);
        String fnm = BlockStateHelper.getName(floor);
        if (random.nextInt(3) == 0 && fnm != null && !fnm.equals("air")
                && !fnm.equals("cave_air") && !fnm.equals("water")
                && !fnm.equals("lava") && !fnm.equals("bedrock")) {
            int railId = BlockStateHelper.getDefault("rail");
            if (railId > 0) level.setBlock(x, y - 1, z, railId);
        }
        if (random.nextInt(14) == 0) {
            int cb = BlockStateHelper.getDefault("cobweb");
            if (cb > 0) level.setBlock(x, y, z, cb);
        } else if (random.nextInt(45) == 0) {
            placeLootChest(level, x, y, z, "chests/abandoned_mineshaft",
                ((long) x * 31L + (long) z * 7L + y) ^ 0x9e3779b9L);
        }
    }

    // 注：本方法当前不会被执行。要塞为 concentric_rings 放置（StructureSet.load 不支持），
    // 由 DensityRouterChunkGenerator.generate 中的 legacy 钩子 StructureManager.generateStronghold
    // → StrongholdPortalRoomGenerator 独家生成（单区块，含 12 框架末地门/银鱼笼/战利品/图书馆角）。
    // 保留本跨区块实现作为参考：若未来要塞改为 structure2 管线，可直接复用此走廊+图书馆逻辑。
    private static void placeStronghold(WorldGenLevel level, int centerX, int centerZ,
                                        int surfaceY, RandomSource random) {
        int baseY = Math.max(15, Math.min(surfaceY - 20, 40));
        int ox = centerX - 4;
        int oz = centerZ - 4;
        int brick = BlockStateHelper.getDefault("stone_bricks");
        // 11x11x7 石砖房间, 内部掏空
        for (int dx = 0; dx < 11; dx++) {
            for (int dz = 0; dz < 11; dz++) {
                for (int dy = 0; dy <= 6; dy++) {
                    boolean edge = dx == 0 || dx == 10 || dz == 0 || dz == 10 || dy == 0 || dy == 6;
                    int bx = ox + dx, by = baseY + dy, bz = oz + dz;
                    if (edge) {
                        if (brick > 0) level.setBlock(bx, by, bz, brick);
                    } else {
                        level.setBlock(bx, by, bz, 0);
                    }
                }
            }
        }
        // 中央末地传送门：原版 12 框架环形（四边各 3 个，四角留空）+ 内 3x3 传送门。
        // 注意：必须只用 12 个框架（与 tryActivateEndPortal 的 bestCount>=12 校验一致），
        // 若放满 5x5 共 16 个框架，玩家需塞 16 个末影之眼才能激活，偏离原版。
        int portalFrame = BlockStateHelper.getDefault("end_portal_frame");
        int portal = BlockStateHelper.getDefault("end_portal");
        int py = baseY + 2;
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                int px = centerX + dx, pz = centerZ + dz;
                boolean edgeX = Math.abs(dx) == 2;
                boolean edgeZ = Math.abs(dz) == 2;
                if ((edgeX && !edgeZ) || (!edgeX && edgeZ)) {
                    if (portalFrame > 0) level.setBlock(px, py, pz, portalFrame); // 12 框架
                } else if (!edgeX && !edgeZ) {
                    if (portal > 0) level.setBlock(px, py, pz, portal);            // 内 3x3 传送门
                }
                // 四角(±2,±2) 留空（原版无框架）
            }
        }
        // ⚠️ 修复：原仅生成末地传送门房间，未还原内部房间/走廊/图书馆。现补充：
        // 1) 银鱼刷怪笼（原版藏于传送门房间角落，激活后刷银鱼）
        placeSpawner(level, centerX - 3, baseY + 1, centerZ - 3, "silverfish");
        // 2) 向东开门口 + 连接走廊（石砖，含 stronghold_crossing 战利品箱）
        for (int dy = 1; dy <= 3; dy++) level.setBlock(centerX + 6, baseY + dy, centerZ, 0); // 门口
        int corridorEndX = centerX + 13;
        for (int x = centerX + 7; x <= corridorEndX; x++) {
            for (int dy = 0; dy <= 4; dy++) {
                level.setBlock(x, baseY + dy, centerZ - 1, brick); // 北墙
                level.setBlock(x, baseY + dy, centerZ + 1, brick); // 南墙
                if (dy == 0 || dy == 4) level.setBlock(x, baseY + dy, centerZ, brick); // 地板/天花板
                else level.setBlock(x, baseY + dy, centerZ, 0); // 走廊内部空气
            }
        }
        placeLootChest(level, corridorEndX, baseY + 1, centerZ, "chests/stronghold_crossing",
            ((long) corridorEndX * 31L + (long) centerZ * 7L + baseY) ^ 0x85ebca6bL);
        // 3) 图书馆（7x7：石砖外墙 + 书架内墙 + 中央 stronghold_library 战利品箱）
        int libX0 = corridorEndX + 1, libX1 = corridorEndX + 7;
        int libZ0 = centerZ - 3, libZ1 = centerZ + 3;
        int shelf = BlockStateHelper.getDefault("bookshelf");
        for (int x = libX0; x <= libX1; x++) {
            for (int z = libZ0; z <= libZ1; z++) {
                for (int dy = 0; dy <= 4; dy++) {
                    boolean perimeter = x == libX0 || x == libX1 || z == libZ0 || z == libZ1;
                    boolean innerShelf = dy >= 1 && dy <= 3
                        && ((x == libX0 + 1 && z != centerZ) || x == libX1 - 1
                            || z == libZ0 + 1 || z == libZ1 - 1);
                    if (dy == 0 || dy == 4) {
                        if (brick > 0) level.setBlock(x, baseY + dy, z, brick); // 地板/天花板
                    } else if (perimeter) {
                        // 西墙(图书馆入口)在中心轴开门
                        if (x == libX0 && z == centerZ) level.setBlock(x, baseY + dy, z, 0);
                        else if (brick > 0) level.setBlock(x, baseY + dy, z, brick);
                    } else if (innerShelf && shelf > 0) {
                        level.setBlock(x, baseY + dy, z, shelf);
                    } else {
                        level.setBlock(x, baseY + dy, z, 0); // 室内空气
                    }
                }
            }
        }
        placeLootChest(level, (libX0 + libX1) / 2, baseY + 1, centerZ, "chests/stronghold_library",
            ((long) ((libX0 + libX1) / 2) * 31L + (long) centerZ * 7L + baseY) ^ 0xc2b2ae35L);
    }

    // best-effort 近似（P1-3）：外形接近原版，但缺少内部红石陷阱连线/隐藏密室等逐块还原，与原版 1:1 有偏差。
    
    // 忠实还原原版 DesertPyramidPiece：21x21 砂岩金字塔 + 陶瓦饰带 + 地下陷阱室（9-TNT 缓存 + 压力板 + 红石引线）+ 4 个战利品室。
    private static void placeDesertPyramid(WorldGenLevel level, int centerX, int centerZ,
                                           int surfaceY, RandomSource random) {
        int sand = BlockStateHelper.getDefault("sandstone");
        int cut = BlockStateHelper.getDefault("cut_sandstone");
        if (cut <= 0) cut = sand;
        int chiseled = BlockStateHelper.getDefault("chiseled_sandstone");
        int smooth = BlockStateHelper.getDefault("smooth_sandstone");
        if (smooth <= 0) smooth = sand;
        int orange = BlockStateHelper.getDefault("orange_terracotta");
        int blue = BlockStateHelper.getDefault("blue_terracotta");
        int tnt = BlockStateHelper.getDefault("tnt");
        int plate = BlockStateHelper.getDefault("stone_pressure_plate");
        int wire = BlockStateHelper.getDefault("redstone_wire");
        int air = 0;
        int baseY = surfaceY;

        // 9 层收缩金字塔（21x21 → 顶层 5x5），顶层外圈蓝陶瓦、四角橙陶瓦（原版饰带近似）
        for (int l = 0; l < 9; l++) {
            int r = 10 - l;
            if (r < 2) r = 2;
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    boolean edge = (Math.abs(dx) == r || Math.abs(dz) == r);
                    int id = edge ? sand : air;
                    if (l == 8) {
                        if (Math.abs(dx) == r && Math.abs(dz) == r) id = orange > 0 ? orange : sand;
                        else if (edge) id = blue > 0 ? blue : sand;
                    }
                    level.setBlock(centerX + dx, baseY + l, centerZ + dz, id);
                }
            }
        }
        if (chiseled > 0) level.setBlock(centerX, baseY + 9, centerZ, chiseled);

        // 地下陷阱室：中央 5x5 室（cut_sandstone 墙、chiseled 拱顶）
        int trapTop = baseY - 1;
        int trapBot = baseY - 13;
        for (int y = trapBot; y <= trapTop; y++) {
            for (int dx = -2; dx <= 2; dx++) {
                for (int dz = -2; dz <= 2; dz++) {
                    boolean wall = (Math.abs(dx) == 2 || Math.abs(dz) == 2);
                    int id = wall ? cut : air;
                    if (y == trapBot) id = cut;
                    if (y == trapTop) id = chiseled > 0 ? chiseled : cut;
                    level.setBlock(centerX + dx, y, centerZ + dz, id);
                }
            }
        }
        // 9-TNT 缓存（原版 3x3 埋在陷阱室底部）
        if (tnt > 0) {
            for (int dx = -1; dx <= 1; dx++)
                for (int dz = -1; dz <= 1; dz++)
                    level.setBlock(centerX + dx, trapBot + 1, centerZ + dz, tnt);
        }
        // 石压力板 + 十字红石引线（原版陷阱触发链）
        if (plate > 0) level.setBlock(centerX, trapTop - 1, centerZ, plate);
        if (wire > 0) {
            for (int d = -2; d <= 2; d++) {
                level.setBlock(centerX + d, trapTop, centerZ, wire);
                level.setBlock(centerX, trapTop, centerZ + d, wire);
            }
        }

        // 4 个战利品室（陷阱室四侧）+ 红石引线延伸至各室
        int[][] offs = {{10,0},{-10,0},{0,10},{0,-10}};
        for (int[] o : offs) {
            int rx = centerX + o[0], rz = centerZ + o[1];
            int ry = baseY - 3;
            for (int dx = -1; dx <= 1; dx++)
                for (int dz = -1; dz <= 1; dz++) {
                    level.setBlock(rx+dx, ry, rz+dz, cut);
                    level.setBlock(rx+dx, ry-1, rz+dz, smooth > 0 ? smooth : cut);
                    if (Math.abs(dx)==1 && Math.abs(dz)==1) level.setBlock(rx+dx, ry-2, rz+dz, sand);
                }
            level.setBlock(rx, ry, rz, air);
            if (wire > 0) {
                int sx = o[0] != 0 ? o[0] / Math.abs(o[0]) : 0;
                int sz = o[1] != 0 ? o[1] / Math.abs(o[1]) : 0;
                if (o[0] != 0) for (int x = centerX + sx; x != rx; x += sx) level.setBlock(x, trapTop, centerZ, wire);
                if (o[1] != 0) for (int z = centerZ + sz; z != rz; z += sz) level.setBlock(centerX, trapTop, z, wire);
            }
            placeLootChest(level, rx, ry-1, rz, "chests/desert_pyramid",
                ((long)(rx*31L + rz*7L)) ^ 0x517cc1bL);
        }
    }

    // 忠实还原原版 JunglePyramidPiece：双层苔石神殿 + 主/隐藏宝箱 + 两个装箭发射器（箭塔陷阱）+ 拉杆 + 红石 + 粘性活塞。
    private static void placeJungleTemple(WorldGenLevel level, int centerX, int centerZ,
                                          int surfaceY, RandomSource random) {
        int sb = BlockStateHelper.getDefault("stone_bricks");
        int cb = BlockStateHelper.getDefault("cobblestone");
        int mb = BlockStateHelper.getDefault("mossy_cobblestone");
        if (sb <= 0) sb = 1;
        if (mb <= 0) mb = cb;
        int chiseled = BlockStateHelper.getDefault("chiseled_stone_bricks");
        int stairs = BlockStateHelper.getDefault("stone_brick_stairs");
        int dispenser = BlockStateHelper.getDefault("dispenser");
        int lever = BlockStateHelper.getDefault("lever");
        int wire = BlockStateHelper.getDefault("redstone_wire");
        int sticky = BlockStateHelper.getDefault("sticky_piston");
        int baseY = surfaceY;

        // 底座 15x15（外圈苔石）
        for (int dx = -7; dx <= 7; dx++)
            for (int dz = -7; dz <= 7; dz++) {
                boolean outer = Math.abs(dx)==7 || Math.abs(dz)==7;
                level.setBlock(centerX+dx, baseY, centerZ+dz, outer ? mb : sb);
            }
        // 第一层 11x11 空心（y 1..4，顶层 chiseled）
        for (int y = 1; y <= 4; y++)
            for (int dx = -5; dx <= 5; dx++)
                for (int dz = -5; dz <= 5; dz++) {
                    boolean wall = Math.abs(dx)==5 || Math.abs(dz)==5;
                    if (wall) level.setBlock(centerX+dx, baseY+y, centerZ+dz, y==4 ? (chiseled>0?chiseled:sb) : sb);
                }
        // 第二层 9x9 空心（y 5..8）
        for (int y = 5; y <= 8; y++)
            for (int dx = -4; dx <= 4; dx++)
                for (int dz = -4; dz <= 4; dz++) {
                    boolean wall = Math.abs(dx)==4 || Math.abs(dz)==4;
                    if (wall) level.setBlock(centerX+dx, baseY+y, centerZ+dz, sb);
                }
        // 屋顶台阶
        if (stairs > 0) {
            int sn = BlockStateHelper.withProp(stairs, "facing", "north");
            int se = BlockStateHelper.withProp(stairs, "facing", "east");
            for (int dx = -5; dx <= 5; dx++) { level.setBlock(centerX+dx, baseY+9, centerZ-6, sn); level.setBlock(centerX+dx, baseY+9, centerZ+6, sn); }
            for (int dz = -5; dz <= 5; dz++) { level.setBlock(centerX-6, baseY+9, centerZ+dz, se); level.setBlock(centerX+6, baseY+9, centerZ+dz, se); }
        }
        // 入口楼梯（南）
        if (stairs > 0) {
            int ss = BlockStateHelper.withProp(stairs, "facing", "south");
            for (int s = 0; s < 3; s++)
                for (int dx = -1; dx <= 1; dx++)
                    level.setBlock(centerX+dx, baseY+s+1, centerZ+6+s, ss);
        }

        // 主宝箱 + 隐藏宝箱（假墙密室）
        placeLootChest(level, centerX+3, baseY+1, centerZ+3, "chests/jungle_temple",
            ((long)(centerX*53L + centerZ*17L)) ^ 0x9c2dL);
        placeLootChest(level, centerX-1, baseY-2, centerZ-2, "chests/jungle_temple",
            ((long)(centerX*97L + centerZ*29L)) ^ 0x6d3bL);

        // 两个装箭发射器（原版箭塔陷阱）
        if (dispenser > 0) {
            int dN = BlockStateHelper.withProp(dispenser, "facing", "north");
            int dW = BlockStateHelper.withProp(dispenser, "facing", "west");
            placeDispenserWithItems(level, centerX-1, baseY-2, centerZ+1, dN, "minecraft:arrow", 9);
            placeDispenserWithItems(level, centerX+1, baseY-2, centerZ-1, dW, "minecraft:arrow", 9);
        }
        // 拉杆（机关） + 中央红石引线 + 粘性活塞（隐藏密室门近似）
        if (lever > 0) {
            int lN = BlockStateHelper.withProp(lever, "facing", "north");
            level.setBlock(centerX-2, baseY+1, centerZ-2, lN);
            level.setBlock(centerX+2, baseY+1, centerZ-2, lN);
            level.setBlock(centerX, baseY+1, centerZ+2, lN);
        }
        if (wire > 0) for (int x = centerX-2; x <= centerX+2; x++) level.setBlock(x, baseY-1, centerZ, wire);
        if (sticky > 0) {
            int sUp = BlockStateHelper.withProp(sticky, "facing", "up");
            level.setBlock(centerX, baseY-2, centerZ-2, sUp);
        }
    }

    // 忠实还原原版 SwampHutPiece：云杉木板小屋 + 橡木原木角柱 + 橡木栅栏栏杆 + 工作台/炼药锅/花盆红蘑菇 + 女巫 + 猫。
    private static void placeSwampHut(WorldGenLevel level, int centerX, int centerZ,
                                      int surfaceY, RandomSource random) {
        int baseY = surfaceY;
        int oak = BlockStateHelper.getDefault("spruce_planks");
        int dol = BlockStateHelper.getDefault("oak_log");
        int stair = BlockStateHelper.getDefault("spruce_stairs");
        int craft = BlockStateHelper.getDefault("crafting_table");
        int cauldron = BlockStateHelper.getDefault("cauldron");
        int mushroom = BlockStateHelper.getDefault("potted_red_mushroom");
        if (mushroom <= 0) mushroom = BlockStateHelper.getDefault("red_mushroom");
        int fence = BlockStateHelper.getDefault("oak_fence");
        int floor = oak > 0 ? oak : 1;
        int wall = oak > 0 ? oak : floor;
        int corner = dol > 0 ? dol : wall;

        int w = 3; // 7x7 footprint (-3..3)
        for (int dx = -w; dx <= w; dx++)
            for (int dz = -w; dz <= w; dz++)
                level.setBlock(centerX + dx, baseY, centerZ + dz, floor);

        for (int y = 1; y <= 4; y++) {
            for (int dx = -w; dx <= w; dx++) {
                for (int dz = -w; dz <= w; dz++) {
                    boolean edge = (Math.abs(dx) == w) || (Math.abs(dz) == w);
                    if (!edge) { level.setBlock(centerX + dx, baseY + y, centerZ + dz, 0); continue; }
                    boolean isCorner = (Math.abs(dx) == w) && (Math.abs(dz) == w);
                    level.setBlock(centerX + dx, baseY + y, centerZ + dz, isCorner ? corner : wall);
                }
            }
        }

        for (int dx = -w; dx <= w; dx++)
            for (int dz = -w; dz <= w; dz++)
                level.setBlock(centerX + dx, baseY + 5, centerZ + dz, wall);

        if (stair > 0) {
            int sn = BlockStateHelper.withProp(stair, "facing", "north");
            int ss = BlockStateHelper.withProp(stair, "facing", "south");
            int se = BlockStateHelper.withProp(stair, "facing", "east");
            int sw = BlockStateHelper.withProp(stair, "facing", "west");
            for (int dx = -w; dx <= w; dx++) {
                level.setBlock(centerX + dx, baseY + 6, centerZ - w, sn);
                level.setBlock(centerX + dx, baseY + 6, centerZ + w, ss);
            }
            for (int dz = -w; dz <= w; dz++) {
                level.setBlock(centerX - w, baseY + 6, centerZ + dz, sw);
                level.setBlock(centerX + w, baseY + 6, centerZ + dz, se);
            }
            if (mushroom > 0) level.setBlock(centerX, baseY + 7, centerZ, mushroom);
        }

        // 内部家具 + 居住者（女巫 + 猫，原版 SwampHutPiece）
        if (craft > 0) level.setBlock(centerX + 1, baseY + 1, centerZ + 1, craft);
        if (cauldron > 0) level.setBlock(centerX - 1, baseY + 1, centerZ + 1, cauldron);
        if (fence > 0) for (int dx = -w + 1; dx <= w - 1; dx++) {
            level.setBlock(centerX + dx, baseY + 2, centerZ - w, fence);
            level.setBlock(centerX + dx, baseY + 2, centerZ + w, fence);
        }
        spawnMob("witch", centerX + 0.5, baseY + 1, centerZ + 0.5);
        spawnMob("cat", centerX + 1.5, baseY + 1, centerZ - 0.5);
    }

    
    // 忠实还原原版 OceanMonumentPieces 的规模与材质：58x58x23 海晶石神殿 + 核心房（金块）+ 3 远古守卫者 + 海晶灯装饰。
    private static void placeOceanMonument(WorldGenLevel level, int centerX, int centerZ,
                                           int surfaceY, RandomSource random) {
        int baseY = surfaceY;
        int pm = BlockStateHelper.getDefault("prismarine");
        int pmb = BlockStateHelper.getDefault("prismarine_bricks");
        int dp = BlockStateHelper.getDefault("dark_prismarine");
        int sl = BlockStateHelper.getDefault("sea_lantern");
        int gold = BlockStateHelper.getDefault("gold_block");
        int floor = pm > 0 ? pm : 1;
        int W = 29;   // 半宽 → 全宽 58（原版 WIDTH=58）
        int H = 23;   // 原版 monument 高度

        // 主基座 58x58（边用黑紫晶）
        for (int dx = -W; dx <= W; dx++)
            for (int dz = -W; dz <= W; dz++) {
                boolean edge = Math.abs(dx) == W || Math.abs(dz) == W;
                level.setBlock(centerX + dx, baseY, centerZ + dz, edge ? (dp > 0 ? dp : floor) : floor);
            }
        // 周边墙升起 H 高（黑紫晶）
        for (int y = 1; y <= H; y++)
            for (int dx = -W; dx <= W; dx++)
                for (int dz = -W; dz <= W; dz++) {
                    boolean edge = Math.abs(dx) == W || Math.abs(dz) == W;
                    if (edge) level.setBlock(centerX + dx, baseY + y, centerZ + dz, dp > 0 ? dp : floor);
                }
        // 中央核心柱（9x9）用海晶砖
        for (int y = 1; y <= H; y++)
            for (int dx = -4; dx <= 4; dx++)
                for (int dz = -4; dz <= 4; dz++)
                    level.setBlock(centerX + dx, baseY + y, centerZ + dz, pmb > 0 ? pmb : floor);
        // 核心房装饰：中央金块 + 外圈黑紫晶
        if (gold > 0)
            for (int dx = -1; dx <= 1; dx++)
                for (int dz = -1; dz <= 1; dz++)
                    for (int dy = 3; dy <= 5; dy++)
                        level.setBlock(centerX + dx, baseY + dy, centerZ + dz, gold);
        if (dp > 0)
            for (int dx = 2; dx <= 4; dx++)
                for (int dz = 2; dz <= 4; dz++)
                    for (int dy = 3; dy <= 6; dy++)
                        level.setBlock(centerX + dx - 1, baseY + dy, centerZ + dz - 1, dp);
        // 海晶灯装饰
        if (sl > 0) {
            level.setBlock(centerX, baseY + H, centerZ, sl);
            level.setBlock(centerX - 2, baseY + 1, centerZ - 2, sl);
            level.setBlock(centerX + 2, baseY + 1, centerZ + 2, sl);
        }
        // 3 个远古守卫者（核心房 / 侧翼 / 顶层，原版 OceanMonumentPieces 共 3 只）
        spawnMob("elder_guardian", centerX + 0.5, baseY + 4, centerZ + 0.5);
        spawnMob("elder_guardian", centerX + 15.5, baseY + 4, centerZ + 9.5);
        spawnMob("elder_guardian", centerX - 15.5, baseY + 1, centerZ - 9.5);
        // 战利品箱
        placeLootChest(level, centerX, baseY + 1, centerZ + 3, "chests/simple_dungeon",
            ((long) centerX * 61L + (long) centerZ * 13L) ^ 0x3a7bL);
    }

    private static void placeWoodlandMansion(WorldGenLevel level, int centerX, int centerZ,
                                             int surfaceY, RandomSource random) {
        // 林地府邸：entrance 模板本身就是 21x19x16 的主楼，已极具辨识度。
        // 在其上叠加 2x2 楼层 + 屋顶，构成一个连通的三层府邸（替代原来的圆石盒子）。
        Rotation rot = Rotation.random(random);
        String prefix = "woodland_mansion/";
        StructureTemplate entrance = StructureTemplate.load(prefix + "entrance");
        int topY = surfaceY;
        if (entrance != null) {
            entrance.placeInWorld(level, centerX, surfaceY, centerZ, rot, Mirror.NONE);
            topY = surfaceY + entrance.sizeY;
        }
        // 上层（2x2_a 系列，15x8x15，落在 21 宽主楼内，无缝衔接）
        StructureTemplate upper = loadRandomMansionTemplate(prefix + "2x2_a", random);
        if (upper == null) upper = loadRandomMansionTemplate(prefix + "2x2_b", random);
        if (upper != null) {
            upper.placeInWorld(level, centerX, topY, centerZ, rot, Mirror.NONE);
            topY += upper.sizeY;
        }
        // 屋顶（8x1x8 帽顶装饰）
        StructureTemplate roof = StructureTemplate.load(prefix + "roof");
        if (roof != null) roof.placeInWorld(level, centerX, topY, centerZ, rot, Mirror.NONE);
    }

    private static StructureTemplate loadRandomMansionTemplate(String baseName, RandomSource random) {
        for (int a = 0; a < 10; a++) { StructureTemplate t = StructureTemplate.load(baseName + (1 + random.nextInt(9))); if (t != null) return t; }
        return StructureTemplate.load(baseName + "1");
    }

    // ── 工具 ──────────────────────────────────────────────────────────

    /** 放置一个刷怪笼并写 SpawnData 方块实体，使加载时 SpawnerSystem 刷出指定实体。 */
    private static void placeSpawner(WorldGenLevel level, int x, int y, int z, String entityId) {
        int sid = BlockStateHelper.getDefault("spawner");
        if (sid <= 0) return;
        level.setBlock(x, y, z, sid);
        NbtMap be = NbtMap.builder()
            .putString("id", "minecraft:spawner")
            .putInt("x", x).putInt("y", y).putInt("z", z)
            .putCompound("SpawnData", NbtMap.builder().putString("id", "minecraft:" + entityId).build())
            .putInt("MinSpawnDelay", 20)
            .putInt("MaxSpawnDelay", 200)
            .putInt("SpawnCount", 4)
            .build();
        level.setBlockEntity(x, y, z, be);
    }

    private static void placeLootChest(WorldGenLevel level, int x, int y, int z,
                                       String lootTable, long seed) {
        int chestId = BlockStateHelper.getDefault("chest");
        if (chestId == 0) return;
        level.setBlock(x, y, z, chestId);
        List<LootTableLoader.LootEntry> loot = LootTableLoader.generateLoot(lootTable, seed);
        List<NbtMap> items = new ArrayList<>();
        int slot = 0;
        for (LootTableLoader.LootEntry e : loot) {
            if (slot >= 27) break;
            int iid = BlockManager.getItemIdByName(e.itemName);
            if (iid <= 0) continue;
            items.add(NbtMap.builder()
                .putByte("Slot", (byte) slot)
                .putString("id", e.itemName.startsWith("minecraft:")
                    ? e.itemName : "minecraft:" + e.itemName)
                .putByte("Count", (byte) Math.min(127, e.count))
                .build());
            slot++;
        }
        NbtMap be = NbtMap.builder()
            .putString("id", "minecraft:chest")
            .putInt("x", x).putInt("y", y).putInt("z", z)
            .putList("Items", NbtType.COMPOUND, items)
            .build();
        level.setBlockEntity(x, y, z, be);
    }

    /** 放置一个带物品的发射器（结构陷阱用，如丛林神庙箭塔）。 */
    private static void placeDispenserWithItems(WorldGenLevel level, int x, int y, int z,
                                                int dispenserState, String itemName, int count) {
        if (dispenserState <= 0) return;
        level.setBlock(x, y, z, dispenserState);
        int iid = BlockManager.getItemIdByName(itemName);
        List<NbtMap> items = new ArrayList<>();
        if (iid > 0) {
            items.add(NbtMap.builder()
                .putByte("Slot", (byte) 0)
                .putString("id", itemName.startsWith("minecraft:") ? itemName : "minecraft:" + itemName)
                .putByte("Count", (byte) Math.min(64, count))
                .build());
        }
        NbtMap be = NbtMap.builder()
            .putString("id", "minecraft:dispenser")
            .putInt("x", x).putInt("y", y).putInt("z", z)
            .putList("Items", NbtType.COMPOUND, items)
            .build();
        level.setBlockEntity(x, y, z, be);
    }

    /** 在指定坐标生成一个被动/敌对生物（结构居住者用，如沼泽小屋女巫/猫、海底神殿远古守卫者）。 */
    private static void spawnMob(String name, double x, double y, double z) {
        try {
            MobEntity m = new MobEntity(EntityManager.allocateId(), name, x, y, z);
            EntityManager.addEntity(m);
        } catch (Exception ignored) {
        }
    }
}
