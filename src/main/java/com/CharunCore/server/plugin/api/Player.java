package com.CharunCore.server.plugin.api;

import com.CharunCore.server.console.ConsoleCommandHandler;
import com.CharunCore.server.plugin.command.CommandSender;
import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.PlayerData;
import com.CharunCore.server.world.WorldManager;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * 玩家门面(对标 Paper Player): 消息/标题/动作栏/音效/粒子/传送(含跨维度)/状态/背包/
 * 自定义容器/持久数据。底层句柄经 getHandle() 暴露。
 */
public final class Player implements CommandSender {

    private final NetworkHandler h;

    public Player(NetworkHandler handle) {
        this.h = handle;
    }

    public NetworkHandler getHandle() {
        return h;
    }

    @Override
    public String getName() {
        return h.username;
    }

    public UUID getUniqueId() {
        return h.uuid;
    }

    /** 客户端地址(未连接时 null)。 */
    public InetSocketAddress getAddress() {
        return h.ctx != null && h.ctx.channel().remoteAddress() instanceof InetSocketAddress addr
                ? addr : null;
    }

    @Override
    public void sendMessage(String message) {
        h.sendFeedback(message, "white");
    }

    public void sendMessage(String message, String color) {
        h.sendFeedback(message, color);
    }

    @Override
    public boolean hasPermission(String permission) {
        return com.CharunCore.server.plugin.api.PermissionManager.hasPermission(h, permission);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    public boolean isOnline() {
        return h.ctx != null && h.ctx.channel().isActive();
    }

    public boolean isOp() {
        return h.opLevel() > 0;
    }

    public int getOpLevel() {
        return h.opLevel();
    }

    public void kick(String reason) {
        h.sendFeedback("§c你已被踢出: " + reason, "red");
        if (h.ctx != null) h.ctx.close();
    }

    // ── 位置/移动 ─────────────────────────────────────────────

    public double getX() { return h.x; }
    public double getY() { return h.y; }
    public double getZ() { return h.z; }
    public float getYaw() { return h.yaw; }
    public float getPitch() { return h.pitch; }
    public boolean isOnGround() { return h.onGround; }

    public DimensionType getWorld() {
        return h.currentDim;
    }

    public void teleport(double x, double y, double z) {
        NetworkHandler.teleportPlayer(h, x, y, z);
    }

    public void teleport(Player target) {
        NetworkHandler.teleportPlayer(h, target.getX(), target.getY(), target.getZ());
    }

    /** 跨维度传送(先切维度再落坐标, 比例换算被目标坐标覆盖)。 */
    public void teleport(DimensionType dim, double x, double y, double z) {
        if (h.currentDim != dim) {
            h.teleportToDimension(h.ctx, dim, false);
        }
        NetworkHandler.teleportPlayer(h, x, y, z);
    }

    /** 跨维度传送(World 门面重载)。 */
    public void teleport(World world, double x, double y, double z) {
        teleport(world.getDimension(), x, y, z);
    }

    public void setVelocity(double vx, double vy, double vz) {
        h.knockback(vx, vz, vy);
    }

    // ── 飞行/潜行/疾跑 ─────────────────────────────────────────

    public boolean getAllowFlight() { return h.allowFlight; }
    public void setAllowFlight(boolean allow) {
        h.allowFlight = allow;
        h.sendAbilitiesUpdate();
    }

    public boolean isFlying() { return h.flying; }
    /** 服务端强制飞行开关(需先 setAllowFlight(true))。 */
    public void setFlying(boolean flying) {
        if (h.allowFlight) {
            h.flying = flying;
            h.sendAbilitiesUpdate();
        }
    }

    public boolean isSneaking() { return h.isSneaking; }
    public boolean isSprinting() { return h.sprinting; }

    // ── 状态 ──────────────────────────────────────────────────

    public float getHealth() {
        return h.health;
    }

    public void setHealth(float health) {
        h.health = Math.max(0, Math.min(health, 20.0f));
        h.sendHealthUpdate();
    }

    /** 回满血并清空身上的火。 */
    public void heal() {
        h.health = 20.0f;
        h.fireTicks = 0;
        h.sendHealthUpdate();
    }

    /** 饱食度回满。 */
    public void feed() {
        if (h.data != null) {
            h.data.food = 20;
            h.data.saturation = 5.0f;
            h.sendHealthUpdate();
        }
    }

    /** 直接造成伤害(走底层伤害管线, 触发事件/死亡/护甲计算)。 */
    public void damage(float amount) {
        h.damagePlayer(amount, "plugin", h.x, h.z);
    }

    public void damage(float amount, String source) {
        h.damagePlayer(amount, source, h.x, h.z);
    }

    public int getGameMode() {
        return h.gameMode;
    }

    public void setGameMode(int mode) {
        h.setGameModeInternal(mode);
    }

    public int getFoodLevel() {
        return h.data != null ? h.data.food : 20;
    }

    public void setFoodLevel(int food) {
        if (h.data != null) {
            h.data.food = Math.max(0, Math.min(food, 20));
            h.sendHealthUpdate();
        }
    }

    public float getSaturation() {
        return h.data != null ? h.data.saturation : 5.0f;
    }

    public void setSaturation(float saturation) {
        if (h.data != null) {
            h.data.saturation = Math.max(0, saturation);
            h.sendHealthUpdate();
        }
    }

    public int getExpLevel() {
        return h.data != null ? h.data.xpLevel : 0;
    }

    /** 设置经验等级(客户端即时刷新)。 */
    public void setExpLevel(int level) {
        if (h.data != null) {
            h.data.xpLevel = Math.max(0, level);
            h.sendExperienceUpdate();
        }
    }

    /** 增减经验点数(走底层结算)。 */
    public void giveExp(int amount) {
        h.addExperience(amount);
    }

    public int getFireTicks() { return h.fireTicks; }
    public void setFireTicks(int ticks) {
        h.fireTicks = Math.max(0, ticks);
        h.syncOnFire();
    }

    // ── 物品/背包 ──────────────────────────────────────────────

    /** 当前手持快捷栏槽位 (0-8)。 */
    public int getHeldItemSlot() { return h.heldItemSlot; }

    public void setHeldItemSlot(int slot) {
        if (slot >= 0 && slot <= 8 && h.ctx != null) {
            h.heldItemSlot = (short) slot;
            h.sendPacket(h.ctx, 0x67, pb -> pb.writeByte((byte) slot));
        }
    }

    public ItemStack getItemInMainHand() {
        int slot = 36 + h.heldItemSlot;
        if (h.data == null) return new ItemStack(0, 0);
        int id = h.data.inventoryIds[slot];
        if (id <= 0 || h.data.inventoryCounts[slot] <= 0) return new ItemStack(0, 0);
        return ItemStackBuilder.fromPlayerSlot(h.data, slot);
    }

    public void setItemInMainHand(ItemStack item) {
        int slot = 36 + h.heldItemSlot;
        if (h.data == null || item == null) return;
        ItemStackBuilder.writeToPlayerSlot(h.data, slot, item);
        h.sendSlotUpdate(0, slot);
    }

    /** 玩家背包槽(0-8 快捷栏, 9-35 主背包, 36-39 靴/腿/胸/头, 40 副手)。 */
    public ItemStack getInventorySlot(int slot) {
        if (h.data == null || slot < 0 || slot > 45) return new ItemStack(0, 0);
        if (h.data.inventoryIds[slot] <= 0 || h.data.inventoryCounts[slot] <= 0) return new ItemStack(0, 0);
        return ItemStackBuilder.fromPlayerSlot(h.data, slot);
    }

    public void setInventorySlot(int slot, ItemStack item) {
        if (h.data == null || slot < 0 || slot > 45 || item == null) return;
        ItemStackBuilder.writeToPlayerSlot(h.data, slot, item);
        h.sendSlotUpdate(0, slot);
    }

    /** 给物品(自动入包/掉落溢出)。 */
    public boolean giveItem(String itemName, int count) {
        int id = com.CharunCore.server.utils.BlockManager.getItemIdByName(itemName);
        if (id <= 0) return false;
        h.giveItem(id, count);
        return true;
    }

    /** 给带元数据的物品。 */
    public boolean giveItem(ItemStack item) {
        if (item == null || item.isEmpty()) return false;
        if (!item.hasItemMeta()) return giveItem(item.name(), item.count());
        int id = item.itemId();
        if (id <= 0) return false;
        int slot = h.firstEmptyInventorySlot();
        if (slot < 0) {
            // 背包满: 掉落
            com.CharunCore.server.world.entity.EntityManager.spawnItemDrop(h.x, h.y, h.z, id, item.count());
            return true;
        }
        ItemStackBuilder.writeToPlayerSlot(h.data, slot, item);
        h.sendSlotUpdate(0, slot);
        return true;
    }

    public void clearInventory() {
        if (h.data == null) return;
        for (int i = 0; i < 46; i++) {
            h.data.inventoryIds[i] = 0;
            h.data.inventoryCounts[i] = 0;
            h.data.inventoryDamage[i] = 0;
            h.data.inventoryEnchants[i].clear();
            h.data.inventoryPotion[i] = null;
            h.data.inventoryCustomName[i] = null;
            h.data.inventoryTrimMaterial[i] = -1;
            h.data.inventoryTrimPattern[i] = -1;
        }
        for (int s = 0; s < 46; s++) h.sendSlotUpdate(0, s);
    }

    // ── 自定义容器 ──────────────────────────────────────────────

    /** 打开插件自定义界面(9/18/27/36/45/54 格)。 */
    public void openInventory(Inventory inventory) {
        h.openPluginInventory(inventory);
    }

    /** 关闭当前打开的任何窗口(含插件界面)。 */
    public void closeInventory() {
        if (h.ctx != null) h.sendPacket(h.ctx, 0x13, pb -> pb.writeByte(0));
    }

    // ── 持久数据 ────────────────────────────────────────────────

    /** 插件持久数据容器(随玩家数据落盘)。 */
    public PersistentDataContainer getPersistentDataContainer() {
        PlayerData d = h.data;
        if (d.pluginData == null) d.pluginData = new java.util.HashMap<>();
        return new PersistentDataContainer(d.pluginData);
    }

    // ── 表现 ──────────────────────────────────────────────────

    public void sendTitle(String title, String subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        h.sendTitle(title, subtitle, fadeInTicks, stayTicks, fadeOutTicks);
    }

    public void sendActionBar(String message) {
        h.sendActionBar(message);
    }

    public void playSound(String sound, float volume, float pitch) {
        if (h.ctx != null) {
            h.sendSoundAt(sound, h.x, h.y, h.z, volume, pitch);
        }
    }

    public void playSound(String sound, double x, double y, double z, float volume, float pitch) {
        if (h.ctx != null) {
            h.sendSoundAt(sound, x, y, z, volume, pitch);
        }
    }

    public void spawnParticle(String particle, double x, double y, double z, int count,
                              double offsetX, double offsetY, double offsetZ, double speed) {
        h.sendParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, speed);
    }

    /** 强制刷新单个方块显示。 */
    public void sendBlockChange(int x, int y, int z, int blockStateId) {
        Packets.sendBlockChange(this, x, y, z, blockStateId);
    }

    public void sendBlockChange(int x, int y, int z, String blockName, String... props) {
        int state = com.CharunCore.server.utils.BlockStateHelper.getDefault(blockName);
        for (int i = 0; i + 1 < props.length; i += 2) {
            state = com.CharunCore.server.utils.BlockStateHelper.withProp(state, props[i], props[i + 1]);
        }
        Packets.sendBlockChange(this, x, y, z, state);
    }

    /** 以该玩家身份执行命令(无前导 /, 走插件命令路由+内置命令)。 */
    public boolean performCommand(String commandLine) {
        String cmd = commandLine.startsWith("/") ? commandLine.substring(1) : commandLine;
        if (Server0.pluginManager().dispatchCommand(this, cmd)) return true;
        ConsoleCommandHandler.execute(cmd);
        return true;
    }

    public static Player wrap(NetworkHandler h) {
        return h == null ? null : new Player(h);
    }

    /** 延迟引用, 避免 Server↔Player 循环依赖。 */
    private static final class Server0 {
        static com.CharunCore.server.plugin.PluginManager pluginManager() {
            return com.CharunCore.server.plugin.Server.get().getPluginManager();
        }
    }
}
