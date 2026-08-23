package com.CharunCore.server.plugin;

import com.CharunCore.server.Main;
import com.CharunCore.server.console.ConsoleCommandHandler;
import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.plugin.api.BossBar;
import com.CharunCore.server.plugin.command.CommandSender;
import com.CharunCore.server.plugin.event.EventManager;
import com.CharunCore.server.plugin.scheduler.ServerScheduler;

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

    public java.util.List<NetworkHandler> getOnlinePlayers() {
        return new java.util.ArrayList<>(NetworkHandler.players.values());
    }

    public NetworkHandler getPlayer(String name) {
        return NetworkHandler.player(name);
    }

    public int getOnlineCount() {
        return NetworkHandler.players.size();
    }

    public void broadcast(String message) {
        NetworkHandler.broadcastSystemMessage(message, "white");
    }

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

    /** 创建 BossBar。 */
    public BossBar createBossBar(String title,
                                 BossBar.Color color,
                                 BossBar.Style style, float progress) {
        return new BossBar(title, color, style, progress);
    }

    public void shutdown() {
        Main.stopServer();
    }

    public long getWorldAge() {
        return Main.worldAge;
    }

    public long getDayTime() {
        return Main.dayTime;
    }

    public void setDayTime(long dayTime) {
        Main.dayTime = dayTime;
    }

    public boolean isRaining() {
        return Main.isRaining;
    }
}
