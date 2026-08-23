package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.entity.EntityManager;
import com.CharunCore.server.world.entity.ItemFrameEntity;
import com.CharunCore.server.world.entity.MobEntity;
import com.CharunCore.server.worldgen.WorldGenLevel;

import org.cloudburstmc.nbt.NbtMap;

import java.util.List;

/**
 * 结构 data markers（structure_block 方块, 含 metadata）处理器 —— 对齐原版
 * StructurePiece.handleDataMarker 语义：
 *
 *  - "Chest*"  : marker 下方方块设为箱子, 写入 end_city_treasure 战利品表
 *                (打开容器时按 LootTable 延迟填充, NetworkHandler 已支持)。
 *  - "Sentry*" : marker 处生成潜影贝(Shulker)。
 *  - "Elytra*" : marker 处生成物品展示框(ItemFrame)挂鞘翅 —— 原版末地船鞘翅获取方式。
 *
 * 原版 EndCityPieces.handleDataMarker:
 *   Chest  → RandomizableContainer.setBlockEntityLootTable(pos.below(), END_CITY_TREASURE)
 *   Sentry → new Shulker(level, pos, ...) → addFreshEntity
 *   Elytra → new ItemFrame(level, pos, rotation.rotate(Direction.SOUTH)) + setItem(ELYTRA)
 */
public final class StructureMarkerProcessor {

    private StructureMarkerProcessor() {}

    /** 在结构放置完成后调用：处理 level 里收集到的全部 data markers。
     *  @param dim 生成维度（实体/方块写入目标） */
    public static void process(WorldGenLevel level, DimensionType dim) {
        List<StructureTemplate.DataMarker> markers = level.getDataMarkers();
        if (markers.isEmpty()) return;
        for (StructureTemplate.DataMarker m : markers) {
            try {
                String md = m.metadata();
                if (md == null || md.isEmpty()) continue;
                if (md.startsWith("Chest")) {
                    handleChest(level, dim, m.x(), m.y(), m.z());
                } else if (md.startsWith("Sentry")) {
                    handleSentry(dim, m.x(), m.y(), m.z());
                } else if (md.startsWith("Elytra")) {
                    handleElytra(dim, m.x(), m.y(), m.z(), m.rotation());
                }
            } catch (Throwable t) {
                System.err.println("[Marker] 处理 data marker '" + m.metadata()
                    + "' 失败 @(" + m.x() + "," + m.y() + "," + m.z() + "): " + t);
            }
        }
    }

    /** Chest: marker 下方设为箱子 + end_city_treasure 战利品表引用。 */
    private static void handleChest(WorldGenLevel level, DimensionType dim,
                                    int x, int y, int z) {
        int chestY = y - 1;
        if (chestY < level.getMinY() || chestY > 319) return;
        int chestState = BlockStateHelper.getDefault("chest");
        if (chestState <= 0) return;
        level.setBlock(x, chestY, z, chestState);
        // 原版 end_city_treasure 战利品表引用（打开时 NetworkHandler 按 LootTable 生成）
        NbtMap be = NbtMap.builder()
            .putString("id", "minecraft:chest")
            .putInt("x", x).putInt("y", chestY).putInt("z", z)
            .putString("LootTable", "minecraft:chests/end_city_treasure")
            .putLong("LootTableSeed", (long) x * 341873128712L ^ (long) z * 132897987541L ^ chestY)
            .build();
        level.setBlockEntity(x, chestY, z, be);
    }

    /** Sentry: marker 处生成潜影贝（原版 Shulker, 实体 id 112）。 */
    private static void handleSentry(DimensionType dim, int x, int y, int z) {
        // 潜影贝体积: 1x1x1, 生成于 marker 格中心
        MobEntity shulker = new MobEntity(EntityManager.allocateId(), "shulker", x + 0.5, y, z + 0.5);
        shulker.dim = dim;
        shulker.width = 1.0;
        shulker.height = 1.0;
        shulker.maxHealth = 30.0f;
        shulker.health = 30.0f;
        EntityManager.addEntity(shulker);
    }

    /** Elytra: marker 处生成物品展示框挂鞘翅。
     *  原版: new ItemFrame(level, pos, rotation.rotate(Direction.SOUTH)) + setItem(ELYTRA)。
     *  基础朝向 SOUTH(3), 经结构 Rotation 旋转:
     *    CW90: SOUTH→WEST(4)  CW180: SOUTH→NORTH(2)  CCW90: SOUTH→EAST(5) */
    private static void handleElytra(DimensionType dim, int x, int y, int z, Rotation rotation) {
        int facing = rotateSouthFacing(rotation);
        ItemFrameEntity frame = new ItemFrameEntity(EntityManager.allocateId(), x + 0.5, y, z + 0.5, facing);
        frame.dim = dim;
        int elytraId = BlockManager.getItemIdByName("elytra");
        if (elytraId > 0) {
            frame.setItem(elytraId, 1);
        }
        EntityManager.addEntity(frame);
    }

    /** 原版 Direction.rotate 语义: SOUTH 基准经结构旋转后的 facing 码。
     *  facing 码: 2=north 3=south 4=west 5=east（对应 Direction 序号的偏移）。
     *  Direction 顺序: NORTH=0 EAST=1 SOUTH=2 WEST=3; item_frame data: NORTH=2 SOUTH=3 WEST=4 EAST=5
     *  → idx = facing - 2; rotation 步进: NONE=0 CW90=+1 CW180=+2 CCW90=+3 (顺时针) */
    private static int rotateSouthFacing(Rotation rotation) {
        int idx = 1; // SOUTH → idx = 3-2 = 1
        switch (rotation) {
            case CLOCKWISE_90 -> idx = (idx + 1) & 3;          // SOUTH→WEST
            case CLOCKWISE_180 -> idx = (idx + 2) & 3;         // SOUTH→NORTH
            case COUNTERCLOCKWISE_90 -> idx = (idx + 3) & 3;   // SOUTH→EAST
            default -> {}
        }
        return idx + 2;
    }
}
