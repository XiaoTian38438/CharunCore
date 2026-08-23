package com.CharunCore.server.worldgen.structure;

import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.chunk.Chunk;
import com.CharunCore.server.world.gen.RandomSource;
import com.CharunCore.server.worldgen.structure2.LootTableLoader;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;

import java.util.ArrayList;
import java.util.List;

/**
 * 要塞传送门房间生成器（程序化近似）。
 *
 * <p><b>P1-2（已知近似）：</b>本类仅生成一个 13×13 的“传送门房间”+ 通地表的竖井，
 * 并非原版要塞的完整走廊迷宫（环廊 / 图书馆 / 喷泉房 / 蠹虫刷怪笼等）。
 * 项目 {@code json/minecraft/structure/} 下没有要塞（stronghold）nbt 模板，
 * 因此保留单房间实现并在此明确标注；完整要塞迷宫属于后续 best-effort 项。
 *
 * <p><b>B2 / B13：</b>末地门在生成时处于<b>未激活</b>状态——
 * 12 个框架的 {@code eye} 全部为 {@code false}，中央 3×3 保留实心地板（绝不放 end_portal）。
 * 传送门方块仅由 {@code NetworkHandler.tryActivateEndPortal} 在 12 框架全部填满眼睛后填充，
 * 从而还原原版“抛眼之末影→逐格填眼→激活末地门”的核心机制。
 */
public class StrongholdPortalRoomGenerator {

    private static final int STONE_BRICKS = BlockStateHelper.getDefault("stone_bricks");
    private static final int MOSSY_STONE_BRICKS = BlockStateHelper.getDefault("mossy_stone_bricks");
    private static final int CRACKED_STONE_BRICKS = BlockStateHelper.getDefault("cracked_stone_bricks");
    private static final int STONE_BRICK_STAIRS = BlockStateHelper.getDefault("stone_brick_stairs");
    private static final int END_PORTAL_FRAME = BlockStateHelper.getDefault("end_portal_frame");
    private static final int END_PORTAL = BlockStateHelper.getDefault("end_portal");
    private static final int SPAWNER = BlockStateHelper.getDefault("spawner");
    private static final int CHEST = BlockStateHelper.getDefault("chest");
    private static final int BOOKSHELF = BlockStateHelper.getDefault("bookshelf");
    private static final int IRON_BARS = BlockStateHelper.getDefault("iron_bars");
    private static final int TORCH = BlockStateHelper.getDefault("torch");
    private static final int LADDER = BlockStateHelper.getDefault("ladder");
    private static final int AIR = 0;

    private static final int MIN_Y = -64;
    private static final int MAX_Y = 319;

    // 房间中心（局部坐标），13x13 房间 x/z ∈ [2,14]
    private static final int CX = 8;
    private static final int CZ = 8;

    public static void generate(Chunk chunk, int localX, int localZ, int y, RandomSource random) {
        generate(chunk, y, y + 40, random);
    }

    public static void generate(Chunk chunk, int floorY, int surfaceY, RandomSource random) {
        if (floorY < MIN_Y + 4) floorY = MIN_Y + 4;

        buildShell(chunk, floorY, random);
        placeFloorAndPortal(chunk, floorY, random);
        placeApproachStairs(chunk, floorY);
        placeSpawner(chunk, floorY);
        placeDecor(chunk, floorY);
        placeLibrary(chunk, floorY);
        buildShaft(chunk, floorY, surfaceY);
    }

    private static void set(Chunk chunk, int x, int y, int z, int state) {
        if (x < 0 || x > 15 || z < 0 || z > 15) return;
        if (y < MIN_Y || y > MAX_Y) return;
        chunk.setBlock(x, y, z, state);
    }

    private static int brick(int r) {
        if (r == 0) return MOSSY_STONE_BRICKS;
        if (r == 1) return CRACKED_STONE_BRICKS;
        return STONE_BRICKS;
    }

    // 13x13 房间外壳：地板、四壁、天花板。地板为实心石砖（中央 3x3 亦实心，
    // 待激活逻辑填充末地门；B2 移除原悬浮传送门下方的岩浆坑）。
    private static void buildShell(Chunk chunk, int floorY, RandomSource random) {
        for (int x = 2; x <= 14; x++) {
            for (int z = 2; z <= 14; z++) {
                // 地板（整体实心；中央 3x3 留作末地门激活区）
                set(chunk, x, floorY, z, STONE_BRICKS);
                // 墙体 + 天花板
                for (int dy = 1; dy <= 10; dy++) {
                    boolean shell = dy == 10 || x == 2 || x == 14 || z == 2 || z == 14;
                    set(chunk, x, floorY + dy, z, shell ? brick(random.nextInt(10)) : AIR);
                }
            }
        }
    }

    // 终末之门框架：5x5 边框(石砖角 + 12 末地传送门框架) 位于地板层。
    // 中央 3x3 刻意留空（实心地板），不放置 end_portal——B2 修复末地门预激活：
    // 传送门仅在 12 框架全部 eye=true 时由 NetworkHandler.tryActivateEndPortal 填充。
    private static void placeFloorAndPortal(Chunk chunk, int floorY, RandomSource random) {
        int frameY = floorY; // 框架与房间地板同层
        for (int d = -2; d <= 2; d++) {
            placeFrame(chunk, CX + d, frameY, CZ - 2, "south", random);
            placeFrame(chunk, CX + d, frameY, CZ + 2, "north", random);
            placeFrame(chunk, CX - 2, frameY, CZ + d, "east", random);
            placeFrame(chunk, CX + 2, frameY, CZ + d, "west", random);
        }
        // 四角实心石砖
        set(chunk, CX - 2, frameY, CZ - 2, STONE_BRICKS);
        set(chunk, CX + 2, frameY, CZ - 2, STONE_BRICKS);
        set(chunk, CX - 2, frameY, CZ + 2, STONE_BRICKS);
        set(chunk, CX + 2, frameY, CZ + 2, STONE_BRICKS);
        // 中央 3x3：保持实心地板（无岩浆、无 end_portal），等待激活逻辑填充。
    }

    // B13：框架眼睛默认 false，由玩家逐一填充（抛眼之末影），不再随机预置装饰眼。
    private static void placeFrame(Chunk chunk, int x, int y, int z, String facing, RandomSource random) {
        int state = BlockStateHelper.withProp(END_PORTAL_FRAME, "facing", facing);
        state = BlockStateHelper.withProp(state, "eye", "false");
        set(chunk, x, y, z, state);
    }

    // 入口阶梯：从 +z 墙向室内下行 3 级，落到传送门地板层
    private static void placeApproachStairs(Chunk chunk, int floorY) {
        int stairNorth = BlockStateHelper.withProp(STONE_BRICK_STAIRS, "facing", "north");
        for (int step = 0; step < 3; step++) {
            int y = floorY + step;
            int z = CZ + 5 - step;
            for (int x = CX - 2; x <= CX + 2; x++) {
                set(chunk, x, y, z, stairNorth);
                for (int fill = floorY; fill < y; fill++) {
                    set(chunk, x, fill, z, STONE_BRICKS);
                }
            }
        }
    }

    private static void placeSpawner(Chunk chunk, int floorY) {
        set(chunk, 4, floorY + 1, 12, SPAWNER);
        // 写 SpawnData 方块实体：原版要塞传送门房间藏银鱼刷怪笼；加载时 SpawnerSystem 据
        // SpawnData.id 刷银鱼（修复前为无 NBT 的通用刷怪笼，刷出僵尸/骷髅等错误种群）。
        NbtMap be = NbtMap.builder()
            .putString("id", "minecraft:spawner")
            .putInt("x", 4).putInt("y", floorY + 1).putInt("z", 12)
            .putCompound("SpawnData", NbtMap.builder().putString("id", "minecraft:silverfish").build())
            .putInt("MinSpawnDelay", 20).putInt("MaxSpawnDelay", 200).putInt("SpawnCount", 4)
            .build();
        chunk.setBlockEntity(4, floorY + 1, 12, be);
    }

    private static void placeDecor(Chunk chunk, int floorY) {
        int barY = floorY + 5;
        for (int x = 4; x <= 12; x += 2) {
            set(chunk, x, barY, 3, IRON_BARS);
            set(chunk, x, barY + 1, 3, IRON_BARS);
        }
        // 两个角落战利品箱（原版 stronghold_crossing 表），写 loot BE 使运行时开箱有战利品
        placeLootChest(chunk, 4, floorY + 1, 4, "chests/stronghold_crossing",
            ((long) 4 * 31L + 4 * 7L + floorY) ^ 0x9e3779b9L);
        placeLootChest(chunk, 12, floorY + 1, 4, "chests/stronghold_crossing",
            ((long) 12 * 31L + 4 * 7L + floorY) ^ 0x85ebca6bL);
        set(chunk, 3, floorY + 5, 3, TORCH);
        set(chunk, 13, floorY + 5, 3, TORCH);
        set(chunk, 3, floorY + 5, 13, TORCH);
        set(chunk, 13, floorY + 5, 13, TORCH);
    }

    /** 图书馆角：北墙内侧书架墙 + 中央图书馆战利品箱（单区块生成器能力所限，仅还原内部一隅）。 */
    private static void placeLibrary(Chunk chunk, int floorY) {
        if (BOOKSHELF <= 0) return;
        for (int x = 5; x <= 11; x += 2) {
            for (int dy = 1; dy <= 3; dy++) {
                set(chunk, x, floorY + dy, 3, BOOKSHELF);
            }
        }
        placeLootChest(chunk, 8, floorY + 1, 4, "chests/stronghold_library",
            ((long) 8 * 31L + 4 * 7L + floorY) ^ 0xc2b2ae35L);
    }

    /** 放置带战利品的箱子：复用 LootTableLoader 生成物品写入方块实体（与 NonJigsawPlacer 格式一致）。 */
    private static void placeLootChest(Chunk chunk, int x, int y, int z, String lootTable, long seed) {
        if (CHEST <= 0) return;
        set(chunk, x, y, z, CHEST);
        List<NbtMap> items = new ArrayList<>();
        int slot = 0;
        for (LootTableLoader.LootEntry e : LootTableLoader.generateLoot(lootTable, seed)) {
            if (slot >= 27) break;
            int iid = BlockManager.getItemIdByName(e.itemName);
            if (iid <= 0) continue;
            items.add(NbtMap.builder()
                .putByte("Slot", (byte) slot)
                .putString("id", e.itemName.startsWith("minecraft:") ? e.itemName : "minecraft:" + e.itemName)
                .putByte("Count", (byte) Math.min(127, e.count))
                .build());
            slot++;
        }
        NbtMap be = NbtMap.builder()
            .putString("id", "minecraft:chest")
            .putInt("x", x).putInt("y", y).putInt("z", z)
            .putList("Items", NbtType.COMPOUND, items)
            .build();
        chunk.setBlockEntity(x, y, z, be);
    }

    // 竖直竖井：从房间天花板向上贯通到地表，附带梯子
    private static void buildShaft(Chunk chunk, int floorY, int surfaceY) {
        int ceiling = floorY + 10;
        int top = Math.min(surfaceY + 1, MAX_Y - 4);
        if (top <= ceiling + 2) return;
        int sx = 12;
        int sz = 12;
        int ladderState = BlockStateHelper.withProp(LADDER, "facing", "south");
        for (int y = floorY + 1; y <= top; y++) {
            if (y >= ceiling) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        if (dx == 0 && dz == 0) continue;
                        set(chunk, sx + dx, y, sz + dz, STONE_BRICKS);
                    }
                }
            } else {
                set(chunk, sx, y, sz - 1, STONE_BRICKS);
            }
            set(chunk, sx, y, sz, y >= top - 1 ? AIR : ladderState);
        }
    }
}
