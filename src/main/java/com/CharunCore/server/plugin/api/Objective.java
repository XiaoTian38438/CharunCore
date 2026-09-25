package com.CharunCore.server.plugin.api;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.network.protocol.PacketBuffer;
import org.cloudburstmc.nbt.NbtMap;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 计分板目标(对标 Paper Objective): 侧边栏/Tab 列表/名称下方三种展示位。
 * 典型用法: sidebar 计分板 —— setScore(entry, value) 即时刷新所有 viewer。
 */
public final class Objective {

    /** 展示位: DisplaySlot 枚举序。 */
    public static final int SLOT_LIST = 0;
    public static final int SLOT_SIDEBAR = 1;
    public static final int SLOT_BELOW_NAME = 2;

    /** 渲染类型。 */
    public static final int RENDER_INTEGER = 0;
    public static final int RENDER_HEARTS = 1;

    final String name;
    String displayName;
    int renderType = RENDER_INTEGER;
    final Map<String, Integer> scores = new ConcurrentHashMap<>();
    final CopyOnWriteArraySet<NetworkHandler> viewers = new CopyOnWriteArraySet<>();
    boolean registered;

    Objective(String name, String displayName, int renderType) {
        this.name = name;
        this.displayName = displayName;
        this.renderType = renderType;
    }

    public String getName() { return name; }

    /** 设置某条目的分数并广播(0x6C)。entry 一般为玩家名或任意文本。 */
    public void setScore(String entry, int score) {
        scores.put(entry, score);
        broadcastScore(entry, score);
    }

    public int getScore(String entry) { return scores.getOrDefault(entry, 0); }

    /** 移除某条目分数(0x29 reset_score)。 */
    public void removeScore(String entry) {
        scores.remove(entry);
        for (NetworkHandler v : viewers) {
            if (v.ctx == null || !v.ctx.channel().isActive()) continue;
            final String fEntry = entry;
            v.sendPacket(v.ctx, 0x29, pb -> {
                pb.writeString(fEntry);
                pb.writeBoolean(true);
                pb.writeString(name);
            });
        }
    }

    public Map<String, Integer> getAllScores() { return new java.util.LinkedHashMap<>(scores); }

    /** 设置显示名并广播更新(0x68 method=2)。 */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        if (registered) broadcastState(2);
    }

    public String getDisplayName() { return displayName; }

    // ── 展示 ────────────────────────────────────────────────────

    /** 给玩家展示该 objective(0x60)。随后 setScore 自动可见。 */
    public void display(NetworkHandler player, int slot) {
        addViewer(player);
        player.sendPacket(player.ctx, 0x60, pb -> {
            pb.writeVarInt(slot);
            pb.writeString(name);
        });
        // 首次展示补发全部已有分数
        for (Map.Entry<String, Integer> e : scores.entrySet()) {
            final String entry = e.getKey();
            final int value = e.getValue();
            player.sendPacket(player.ctx, 0x6C, pb -> {
                pb.writeString(entry);
                pb.writeString(name);
                pb.writeVarInt(value);
                pb.writeBoolean(false);
                pb.writeBoolean(false);
            });
        }
    }

    /** 便捷: 给插件门面玩家展示。 */
    public void display(Player player, int slot) {
        if (player.getHandle().ctx != null) display(player.getHandle(), slot);
    }

    /** 隐藏展示位(slot 处显示空)。 */
    public static void clearDisplay(NetworkHandler player, int slot) {
        player.sendPacket(player.ctx, 0x60, pb -> {
            pb.writeVarInt(slot);
            pb.writeString("");
        });
    }

    // ── 内部 ────────────────────────────────────────────────────

    synchronized void addViewer(NetworkHandler v) {
        if (v.ctx == null || !v.ctx.channel().isActive()) return;
        if (!registered) {
            broadcastState(0);
            registered = true;
        }
        if (viewers.add(v)) {
            v.sendPacket(v.ctx, 0x68, pb -> writeObjective(pb, 0));
        }
    }

    synchronized void unregister() {
        broadcastState(1);
        registered = false;
        viewers.clear();
        scores.clear();
    }

    private void broadcastState(int method) {
        for (NetworkHandler v : viewers) {
            if (v.ctx == null || !v.ctx.channel().isActive()) continue;
            v.sendPacket(v.ctx, 0x68, pb -> writeObjective(pb, method));
        }
    }

    private void writeObjective(PacketBuffer pb, int method) {
        pb.writeString(name);
        pb.writeByte(method);
        if (method == 0 || method == 2) {
            pb.writeAnonymousNbt(NbtMap.builder().putString("text", displayName).build());
            pb.writeByte(renderType);
            pb.writeBoolean(false); // numberFormat absent
        }
    }

    private void broadcastScore(String entry, int score) {
        for (NetworkHandler v : viewers) {
            if (v.ctx == null || !v.ctx.channel().isActive()) continue;
            final String fEntry = entry;
            final int fScore = score;
            v.sendPacket(v.ctx, 0x6C, pb -> {
                pb.writeString(fEntry);
                pb.writeString(name);
                pb.writeVarInt(fScore);
                pb.writeBoolean(false);
                pb.writeBoolean(false);
            });
        }
    }
}
