package com.example.infoboard;

import com.CharunCore.server.plugin.Plugin;
import com.CharunCore.server.plugin.Server;
import com.CharunCore.server.plugin.api.Objective;
import com.CharunCore.server.plugin.api.Player;
import com.CharunCore.server.plugin.api.Scoreboard;
import com.CharunCore.server.plugin.api.Team;
import com.CharunCore.server.plugin.event.EventListener;
import com.CharunCore.server.plugin.event.EventHandler;
import com.CharunCore.server.plugin.event.EventPriority;
import com.CharunCore.server.plugin.event.events.PlayerJoinEvent;
import com.CharunCore.server.plugin.event.events.PlayerQuitEvent;
import com.CharunCore.server.plugin.event.events.ServerTickEvent;
import com.CharunCore.server.plugin.scheduler.ServerScheduler;

import java.util.List;

/**
 * 示例: 侧边栏信息板 + 队伍前缀。
 * 演示 Scoreboard/Objective/Team + ServerTickEvent + 调度器。
 */
public class InfoBoardPlugin extends Plugin implements EventListener {

    private Scoreboard board;
    private Objective side;
    private Team adminTeam;
    private long lastUpdate = 0;

    @Override
    public void onEnable() {
        board = Server.get().createScoreboard();
        side = board.createObjective("info", "§b§lCharunCore");
        adminTeam = board.createTeam("admin", "管理组");
        adminTeam.setColorCode('c');
        adminTeam.setPrefix("§c[管理] ");

        // 每 20 tick (1 秒) 刷新一次侧边栏
        ServerScheduler.ref(this);
        getScheduler().runTaskTimer(ServerScheduler.ref(this), () -> {
            int online = Server.get().getOnlineCount();
            long time = Server.get().getDayTime();
            String clock = String.format("%02d:%02d", (time / 1000 + 6) % 24, (time % 1000) * 60 / 1000);
            side.setDisplayName("§b§lCharunCore");
            side.setScore("§e在线: §f" + online, 100);
            side.setScore("§e时间: §f" + clock, 90);
            side.setScore("§e天气: §f" + (Server.get().isRaining() ? "雨天" : "晴天"), 80);
        }, 20L, 20L);

        registerEvents(this);
        getLogger().info("InfoBoard 已启用");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = Player.wrap(e.getPlayer());
        side.display(p, Objective.SLOT_SIDEBAR);
        board.addViewer(p);
        // OP 玩家自动加入管理组(头上带 [管理] 前缀)
        if (p.isOp()) adminTeam.addEntry(p.getName());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        side.removeScore(e.getPlayer().username);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTick(ServerTickEvent e) {
        // ServerTickEvent 每 50ms 触发一次, 这里演示 100 tick 一次的轻量任务
        if (e.getTickCount() - lastUpdate < 100) return;
        lastUpdate = e.getTickCount();
    }
}
