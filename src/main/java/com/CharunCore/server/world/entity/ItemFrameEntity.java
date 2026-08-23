package com.CharunCore.server.world.entity;

import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.utils.BlockStateHelper;
import com.CharunCore.server.world.WorldManager;

/**
 * 物品展示框（原版 item_frame, 实体 id 73）。装饰实体：
 * - 挂在方块侧面/顶面/底面, 不参与物理/碰撞；
 * - 内含一个物品（原版末地船鞘翅即展示框挂鞘翅）；
 * - 左键打掉掉落内含物品, 右键放入/交换物品。
 *
 * 朝向数据（data 字段, 对应 0x01 spawn entity 的 data）：
 * 0=down 1=up 2=north 3=south 4=west 5=east
 */
public class ItemFrameEntity extends Entity {

    public int itemId = 0;
    public int itemCount = 0;
    /** 展示框朝向: 2=north 3=south 4=west 5=east 0=down 1=up */
    public int facing = 3;
    /** 旋转 (0-7, 原版 item frame rotation) */
    public int frameRotation = 0;
    public boolean fixed = false;

    public ItemFrameEntity(int id, double x, double y, double z, int facing) {
        super(id, 73, x, y, z);
        this.facing = facing;
        this.typeName = "item_frame";
        this.noPhysics = true;
        this.width = 0.25;
        this.height = 0.25;
        // 朝向 -> yaw/pitch (客户端朝向显示)
        switch (facing) {
            case 2 -> { this.yaw = 180.0f; }            // north
            case 3 -> { this.yaw = 0.0f; }              // south
            case 4 -> { this.yaw = 90.0f; }             // west
            case 5 -> { this.yaw = -90.0f; }            // east
            case 0 -> { this.yaw = 0.0f; this.pitch = 90.0f; }  // down (挂在天花板, 框朝下)
            case 1 -> { this.yaw = 0.0f; this.pitch = -90.0f; } // up (放在地面, 框朝上)
            default -> { this.yaw = 0.0f; }
        }
    }

    public void setItem(int itemId, int count) {
        this.itemId = itemId;
        this.itemCount = count;
        syncItemMetadata();
    }

    /** 向跟踪玩家广播 Item 元数据 (0x61, index 8, Slot) */
    public void syncItemMetadata() {
        EntityManager.broadcastMetadata(this);
    }

    @Override
    public void tick() {
        // 展示框: 仅当挂载面被破坏时掉落 (由 block 破坏逻辑处理), 自身不移动
        if (itemId > 0) return;
    }

    /** 打掉展示框: 掉落内含物品 + 框自身 */
    public void dropWithFrame() {
        if (itemId > 0) {
            EntityManager.spawnItemDrop(x, y, z, itemId, itemCount);
        }
        EntityManager.spawnItemDrop(x, y, z, BlockManager.getItemIdByName("item_frame"), 1);
        remove();
    }

    public void remove() {
        EntityManager.removeEntity(id);
    }

    /** 挂载面方块是否仍然存在 */
    public boolean attachedBlockExists() {
        int bx = (int) Math.floor(x), by = (int) Math.floor(y), bz = (int) Math.floor(z);
        switch (facing) {
            case 2 -> bz += 1;  // north: 挂在北侧
            case 3 -> bz -= 1;  // south: 挂在南侧
            case 4 -> bx += 1;  // west
            case 5 -> bx -= 1;  // east
            case 0 -> by += 1;  // down: 挂在上方
            case 1 -> by -= 1;  // up: 挂在下方
            default -> {}
        }
        int state = WorldManager.getBlockState(dim, bx, by, bz);
        String n = BlockStateHelper.getName(state);
        return state != 0 && n != null && !n.equals("air") && !n.equals("cave_air");
    }
}
