package com.CharunCore.server.plugin.api;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.network.protocol.PacketBuffer;
import org.cloudburstmc.nbt.NbtMap;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 计分板队伍(对标 Paper Team): 前缀/后缀/颜色/友伤/碰撞规则。
 * 头上名字与 Tab 列表前缀后缀由前缀后缀组件实时下发。
 */
public final class Team {

    public static final String VIS_ALWAYS = "always";
    public static final String VIS_NEVER = "never";
    public static final String VIS_HIDE_OTHER_TEAMS = "hideForOtherTeams";
    public static final String VIS_HIDE_OWN_TEAM = "hideForOwnTeam";

    final String name;
    String displayName;
    String prefix = "";
    String suffix = "";
    /** ChatFormatting ordinal: 0=black .. 15=white, 21=reset。 */
    int color = 21;
    boolean friendlyFire;
    boolean seeFriendlyInvisibles = true;
    String nametagVisibility = VIS_ALWAYS;
    String collisionRule = "always";
    final Set<String> entries = ConcurrentHashMap.newKeySet();
    final CopyOnWriteArraySet<NetworkHandler> viewers = new CopyOnWriteArraySet<>();

    Team(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() { return name; }

    public Team setPrefix(String prefix) { this.prefix = prefix == null ? "" : prefix; update(); return this; }
    public Team setSuffix(String suffix) { this.suffix = suffix == null ? "" : suffix; update(); return this; }
    public String getPrefix() { return prefix; }
    public String getSuffix() { return suffix; }

    /** 用颜色码字符设置队伍颜色: 'a'=绿, 'c'=红 等(§ 可省略)。 */
    public Team setColorCode(char code) {
        java.util.Map<Character, Integer> map = Map.ofEntries(
                Map.entry('0', 0), Map.entry('1', 1), Map.entry('2', 2), Map.entry('3', 3),
                Map.entry('4', 4), Map.entry('5', 5), Map.entry('6', 6), Map.entry('7', 7),
                Map.entry('8', 8), Map.entry('9', 9), Map.entry('a', 10), Map.entry('b', 11),
                Map.entry('c', 12), Map.entry('d', 13), Map.entry('e', 14), Map.entry('f', 15));
        this.color = map.getOrDefault(Character.toLowerCase(code), 21);
        update();
        return this;
    }

    public Team setFriendlyFire(boolean friendlyFire) { this.friendlyFire = friendlyFire; update(); return this; }
    public boolean isFriendlyFire() { return friendlyFire; }

    public Team setNametagVisibility(String v) { this.nametagVisibility = v; update(); return this; }

    public void addEntry(String entry) {
        if (entries.add(entry)) broadcastPlayers(3, entry);
    }

    public void removeEntry(String entry) {
        if (entries.remove(entry)) broadcastPlayers(4, entry);
    }

    public Set<String> getEntries() { return Set.copyOf(entries); }

    // ── 内部 ────────────────────────────────────────────────────

    synchronized void addViewer(NetworkHandler v) {
        if (v.ctx == null || !v.ctx.channel().isActive()) return;
        if (viewers.add(v)) {
            v.sendPacket(v.ctx, 0x6B, pb -> writeTeam(pb, 0));
            for (String e : entries) {
                final String fe = e;
                v.sendPacket(v.ctx, 0x6B, pb -> writePlayers(pb, 3, fe));
            }
        }
    }

    private void update() {
        for (NetworkHandler v : viewers) {
            if (v.ctx == null || !v.ctx.channel().isActive()) continue;
            v.sendPacket(v.ctx, 0x6B, pb -> writeTeam(pb, 2));
        }
    }

    private void broadcastPlayers(int method, String entry) {
        for (NetworkHandler v : viewers) {
            if (v.ctx == null || !v.ctx.channel().isActive()) continue;
            final int m = method;
            final String fe = entry;
            v.sendPacket(v.ctx, 0x6B, pb -> writePlayers(pb, m, fe));
        }
    }

    private static int visId(String v) {
        return switch (v) {
            case VIS_NEVER -> 1;
            case VIS_HIDE_OTHER_TEAMS -> 2;
            case VIS_HIDE_OWN_TEAM -> 3;
            default -> 0;
        };
    }

    private static int collisionId(String v) {
        return switch (v) {
            case "never" -> 1;
            case "pushOtherTeams" -> 2;
            case "pushOwnTeam" -> 3;
            default -> 0;
        };
    }

    private void writeTeam(PacketBuffer pb, int method) {
        pb.writeString(name);
        pb.writeByte(method);
        if (method == 0 || method == 2) {
            pb.writeAnonymousNbt(NbtMap.builder().putString("text", displayName).build());
            pb.writeByte((friendlyFire ? 0x01 : 0) | (seeFriendlyInvisibles ? 0x02 : 0));
            pb.writeVarInt(visId(nametagVisibility));
            pb.writeVarInt(collisionId(collisionRule));
            pb.writeByte(color);
            pb.writeAnonymousNbt(NbtMap.builder().putString("text", prefix).build());
            pb.writeAnonymousNbt(NbtMap.builder().putString("text", suffix).build());
        }
    }

    private void writePlayers(PacketBuffer pb, int method, String entry) {
        pb.writeString(name);
        pb.writeByte(method);
        pb.writeVarInt(1);
        pb.writeString(entry);
    }
}
