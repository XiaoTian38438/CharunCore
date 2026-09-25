package com.CharunCore.server.plugin;

import com.CharunCore.server.Main;
import com.CharunCore.server.ServerConfig;
import com.CharunCore.server.command.BanList;
import com.CharunCore.server.console.ConsoleCommandHandler;
import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.plugin.api.BossBar;
import com.CharunCore.server.plugin.api.Entities;
import com.CharunCore.server.plugin.api.Entity;
import com.CharunCore.server.plugin.api.Inventory;
import com.CharunCore.server.plugin.api.Scoreboard;
import com.CharunCore.server.plugin.api.World;
import com.CharunCore.server.plugin.command.CommandSender;
import com.CharunCore.server.plugin.event.EventManager;
import com.CharunCore.server.plugin.scheduler.ServerScheduler;
import com.CharunCore.server.world.DimensionType;
import com.CharunCore.server.world.WorldManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 服务器门面(对标 Bukkit.getServer): 世界/玩家/实体/命令/调度/配置/封禁/计分板/自定义容器。
 * 插件入口: Server.get()。
 */
public final class Server {

    private static final Server INSTANCE = new Server();

    private final PluginManager pluginManager = new PluginManager();

    private Server() {}

    public static Server get() {
        return INSTANCE;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public EventManager getEventManager() {
        return EventManager.INSTANCE;
    }

    public ServerScheduler getScheduler() {
        return ServerScheduler.INSTANCE;
    }

    // ── 玩家 ────────────────────────────────────────────────────

    public List<NetworkHandler> getOnlinePlayers() {
        return new ArrayList<>(NetworkHandler.players.values());
    }

    public NetworkHandler getPlayer(String name) {
        return NetworkHandler.player(name);
    }

    public NetworkHandler getPlayer(UUID uuid) {
        return NetworkHandler.players.get(uuid);
    }

    public int getOnlineCount() {
        return NetworkHandler.players.size();
    }

    public int getMaxPlayers() {
        return ServerConfig.maxPlayers;
    }

    public void setMaxPlayers(int max) {
        ServerConfig.maxPlayers = Math.max(1, max);
    }

    public void broadcast(String message) {
        NetworkHandler.broadcastSystemMessage(message, "white");
    }

    public void broadcast(String message, String color) {
        NetworkHandler.broadcastSystemMessage(message, color);
    }

    // ── 世界 ────────────────────────────────────────────────────

    public List<World> getWorlds() {
        return List.of(World.overworld(), World.nether(), World.theEnd());
    }

    /** 按主世界键取世界门面: "overworld"/"the_nether"/"the_end"。 */
    public World getWorld(String key) {
        return switch (key.replace("minecraft:", "")) {
            case "the_nether", "nether" -> World.nether();
            case "the_end", "end" -> World.theEnd();
            default -> World.overworld();
        };
    }

    public long getSeed() {
        return WorldManager.getSeed();
    }

    public String getGameRule(String name) {
        return WorldManager.getGameRule(name);
    }

    public void setGameRule(String name, String value) {
        WorldManager.setGameRule(name, value);
    }

    public void setGameRule(String name, boolean value) {
        WorldManager.setGameRule(name, Boolean.toString(value));
    }

    // ── 实体 ────────────────────────────────────────────────────

    public Entity spawnEntity(String typeName, double x, double y, double z) {
        return Entities.spawn(typeName, DimensionType.OVERWORLD, x, y, z);
    }

    public Entity spawnEntity(String typeName, World world, double x, double y, double z) {
        return Entities.spawn(typeName, world.getDimension(), x, y, z);
    }

    // ── 命令 ────────────────────────────────────────────────────

    /** 以控制台身份执行一条命令 (等价 Bukkit.dispatchCommand(console, cmd))。 */
    public boolean dispatchCommand(String commandLine) {
        String cmd = commandLine.startsWith("/") ? commandLine.substring(1) : commandLine;
        if (cmd.isBlank()) return false;
        if (pluginManager.dispatchCommand(CONSOLE, cmd)) return true;
        ConsoleCommandHandler.execute(cmd);
        return true;
    }

    private static final CommandSender CONSOLE =
            new CommandSender() {
                @Override public String getName() { return "Console"; }
                @Override public void sendMessage(String message) {
                    System.out.println("[Console] " + message);
                }
                @Override public boolean hasPermission(String permission) { return true; }
            };

    public CommandSender getConsoleSender() {
        return CONSOLE;
    }

    // ── UI 工厂 ─────────────────────────────────────────────────

    /** 创建 BossBar。 */
    public BossBar createBossBar(String title,
                                 BossBar.Color color,
                                 BossBar.Style style, float progress) {
        return new BossBar(title, color, style, progress);
    }

    /** 创建插件自定义容器界面(9/18/27/36/45/54 格)。 */
    public Inventory createInventory(int size, String title) {
        return new Inventory(size, title);
    }

    /** 创建插件计分板。 */
    public Scoreboard createScoreboard() {
        return new Scoreboard();
    }

    // ── 服务器配置/封禁 ─────────────────────────────────────────

    public String getMotd() { return ServerConfig.motd; }
    public void setMotd(String motd) { ServerConfig.motd = motd; }
    public int getViewDistance() { return ServerConfig.viewDistance; }
    public void setViewDistance(int viewDistance) { ServerConfig.viewDistance = Math.max(2, viewDistance); }
    public int getPort() { return ServerConfig.serverPort; }

    /** 永久封禁(立即踢出在线玩家)。 */
    public void ban(String playerName, String reason) {
        NetworkHandler h = NetworkHandler.player(playerName);
        UUID uuid = h != null ? h.uuid : null;
        BanList.ban(uuid, playerName, reason, -1L);
        if (h != null) {
            h.sendFeedback("§c你已被封禁: " + reason, "red");
            if (h.ctx != null) h.ctx.close();
        }
    }

    /** 限时封禁(毫秒)。 */
    public void banTemp(String playerName, String reason, long durationMs) {
        NetworkHandler h = NetworkHandler.player(playerName);
        UUID uuid = h != null ? h.uuid : null;
        BanList.ban(uuid, playerName, reason, durationMs);
        if (h != null) {
            h.sendFeedback("§c你已被临时封禁: " + reason, "red");
            if (h.ctx != null) h.ctx.close();
        }
    }

    public boolean unban(String playerNameOrUuid) {
        return BanList.unban(playerNameOrUuid);
    }

    public boolean isBanned(String playerName) {
        return BanList.checkBanned(null, playerName) != null;
    }

    public List<BanList.BanEntry> getBanEntries() {
        return BanList.entries();
    }

    // ── 生命周期 ────────────────────────────────────────────────

    public void shutdown() {
        Main.stopServer();
    }

    public void reloadPlugins() {
        pluginManager.reloadPlugins();
    }

    // ── 时间/天气 ───────────────────────────────────────────────

    public long getWorldAge() {
        return Main.worldAge;
    }

    public long getDayTime() {
        return Main.dayTime;
    }

    /** 设置全服时间并广播(0=清晨, 6000=正午, 13000=入夜)。 */
    public void setDayTime(long dayTime) {
        Main.dayTime = dayTime;
        NetworkHandler.broadcastTime();
    }

    public boolean isRaining() {
        return Main.isRaining;
    }

    /** 设置全服天气并广播(客户端立即切换雨层)。 */
    public void setRaining(boolean raining) {
        Main.isRaining = raining;
        Main.isThundering = false;
        Main.rainTarget = raining ? 1.0 : 0.0;
        NetworkHandler.broadcastTime();
        byte event = (byte) (raining ? 2 : 1);
        for (NetworkHandler h : NetworkHandler.players.values()) {
            if (h.ctx == null) continue;
            h.sendPacket(h.ctx, 0x26, pb -> { pb.writeByte(event); pb.writeFloat(raining ? 1f : 0f); });
        }
    }

    public String getVersion() {
        return "1.21.11 (Protocol 774)";
    }

    public String getName() {
        return "CharunCore";
    }
}
