package com.CharunCore.server.plugin.api;

import com.CharunCore.server.network.NetworkHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 插件计分板根对象(对标 Paper Scoreboard/ScoreboardManager):
 * 持有 objectives/teams, 注册后向加入的 viewer 下发状态。
 * 典型: 创建侧边栏 objective → display(player, Objective.SLOT_SIDEBAR) → setScore 刷新。
 */
public final class Scoreboard {

    private final Map<String, Objective> objectives = new ConcurrentHashMap<>();
    private final Map<String, Team> teams = new ConcurrentHashMap<>();
    final CopyOnWriteArraySet<NetworkHandler> viewers = new CopyOnWriteArraySet<>();

    /** 创建 objective(自动注册给所有当前 viewer)。 */
    public Objective createObjective(String name, String displayName) {
        return createObjective(name, displayName, Objective.RENDER_INTEGER);
    }

    public Objective createObjective(String name, String displayName, int renderType) {
        Objective obj = new Objective(name, displayName, renderType);
        objectives.put(name, obj);
        for (NetworkHandler v : viewers) obj.addViewer(v);
        return obj;
    }

    public Objective getObjective(String name) { return objectives.get(name); }

    /** 注销 objective(客户端移除, 分数清空)。 */
    public void removeObjective(String name) {
        Objective obj = objectives.remove(name);
        if (obj != null) obj.unregister();
    }

    /** 创建队伍。 */
    public Team createTeam(String name, String displayName) {
        Team team = new Team(name, displayName);
        teams.put(name, team);
        for (NetworkHandler v : viewers) team.addViewer(v);
        return team;
    }

    public Team getTeam(String name) { return teams.get(name); }

    public void removeTeam(String name) {
        Team team = teams.remove(name);
        if (team == null) return;
        for (NetworkHandler v : team.viewers) {
            if (v.ctx == null || !v.ctx.channel().isActive()) continue;
            v.sendPacket(v.ctx, 0x6B, pb -> {
                pb.writeString(team.name);
                pb.writeByte(1); // METHOD_REMOVE
            });
        }
        team.viewers.clear();
    }

    /** 让玩家看到此计分板(补发全部 objectives/teams)。 */
    public void addViewer(NetworkHandler player) {
        if (player.ctx == null || !player.ctx.channel().isActive()) return;
        viewers.add(player);
        for (Objective o : objectives.values()) o.addViewer(player);
        for (Team t : teams.values()) t.addViewer(player);
    }

    public void addViewer(Player player) {
        if (player.getHandle().ctx != null) addViewer(player.getHandle());
    }

    /** 移除 viewer(退出服时)。 */
    public void removeViewer(NetworkHandler player) { viewers.remove(player); }

    public int getViewerCount() { return viewers.size(); }
}
