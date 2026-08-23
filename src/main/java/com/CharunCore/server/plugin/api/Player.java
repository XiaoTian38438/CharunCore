package com.CharunCore.server.plugin.api;

import com.CharunCore.server.console.ConsoleCommandHandler;
import com.CharunCore.server.plugin.command.CommandSender;
import com.CharunCore.server.command.Permissions;
import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.utils.BlockManager;
import com.CharunCore.server.world.DimensionType;

/** 玩家门面: 插件操作在线玩家的主要入口。底层句柄经 getHandle() 暴露。 */
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

    @Override
    public void sendMessage(String message) {
        h.sendFeedback(message, "white");
    }

    public void sendMessage(String message, String color) {
        h.sendFeedback(message, color);
    }

    @Override
    public boolean hasPermission(String permission) {
        return PermissionManager.hasPermission(h, permission);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    public boolean isOnline() {
        return h.ctx != null && h.ctx.channel().isActive();
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

    public DimensionType getWorld() {
        return h.currentDim;
    }

    public void teleport(double x, double y, double z) {
        NetworkHandler.teleportPlayer(h, x, y, z);
    }

    public void teleport(Player target) {
        NetworkHandler.teleportPlayer(h, target.getX(), target.getY(), target.getZ());
    }

    public void setVelocity(double vx, double vy, double vz) {
        h.knockback(vx, vy, vz);
    }

    // ── 状态 ──────────────────────────────────────────────────

    public float getHealth() {
        return h.health;
    }

    public void setHealth(float health) {
        h.health = Math.max(0, Math.min(health, 20.0f));
        h.sendHealthUpdate();
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

    public int getExpLevel() {
        return h.data != null ? h.data.xpLevel : 0;
    }

    // ── 物品 ──────────────────────────────────────────────────

    public ItemStack getItemInMainHand() {
        int slot = 36 + h.heldItemSlot;
        if (h.data == null) return new ItemStack(0, 0);
        return new ItemStack(h.data.inventoryIds[slot], h.data.inventoryCounts[slot]);
    }

    public void setItemInMainHand(ItemStack item) {
        int slot = 36 + h.heldItemSlot;
        if (h.data == null || item == null) return;
        h.data.inventoryIds[slot] = item.itemId();
        h.data.inventoryCounts[slot] = item.count();
        h.sendSlotUpdate(0, slot);
    }

    public ItemStack getInventorySlot(int slot) {
        if (h.data == null || slot < 0 || slot > 45) return new ItemStack(0, 0);
        return new ItemStack(h.data.inventoryIds[slot], h.data.inventoryCounts[slot]);
    }

    public void setInventorySlot(int slot, ItemStack item) {
        if (h.data == null || slot < 0 || slot > 45 || item == null) return;
        h.data.inventoryIds[slot] = item.itemId();
        h.data.inventoryCounts[slot] = item.count();
        h.sendSlotUpdate(0, slot);
    }

    public boolean giveItem(String itemName, int count) {
        int id = BlockManager.getItemIdByName(itemName);
        if (id <= 0) return false;
        h.giveItem(id, count);
        return true;
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

    public void spawnParticle(String particle, double x, double y, double z, int count,
                              double offsetX, double offsetY, double offsetZ, double speed) {
        h.sendParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, speed);
    }

    public void dispatchCommand(String commandLine) {
        ConsoleCommandHandler.execute(commandLine);
    }

    public static Player wrap(NetworkHandler h) {
        return h == null ? null : new Player(h);
    }

    @SuppressWarnings("unused")
    private static Permissions unusedKeepImport() {
        return null;
    }
}
