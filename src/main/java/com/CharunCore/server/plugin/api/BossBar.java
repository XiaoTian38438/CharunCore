package com.CharunCore.server.plugin.api;

import com.CharunCore.server.network.NetworkHandler;
import org.cloudburstmc.nbt.NbtMap;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** BossBar: 原版 boss_event(0x09) 直发。addPlayer 后进度/标题变更实时同步。 */
public final class BossBar {

    public enum Color { PINK, BLUE, RED, GREEN, YELLOW, PURPLE, WHITE }
    public enum Style { SOLID, NOTCHED_6, NOTCHED_10, NOTCHED_12, NOTCHED_20 }

    private final UUID id = UUID.randomUUID();
    private final Map<NetworkHandler, Boolean> viewers = new ConcurrentHashMap<>();
    private volatile String title;
    private volatile Color color = Color.PURPLE;
    private volatile Style style = Style.SOLID;
    private volatile float progress = 1.0f;

    public BossBar(String title, Color color, Style style, float progress) {
        this.title = title == null ? "" : title;
        if (color != null) this.color = color;
        if (style != null) this.style = style;
        this.progress = clamp(progress);
    }

    private static float clamp(float p) {
        return Math.max(0.0f, Math.min(1.0f, p));
    }

    public void addPlayer(NetworkHandler player) {
        if (player == null || player.ctx == null || viewers.put(player, true) != null) return;
        sendAdd(player);
    }

    public void removePlayer(NetworkHandler player) {
        if (player == null || viewers.remove(player) == null) return;
        if (player.ctx != null) {
            player.sendPacket(player.ctx, 0x09, pb -> {
                pb.writeUUID(id);
                pb.writeVarInt(1); // remove
            });
        }
    }

    public void removeAll() {
        for (NetworkHandler p : Set.copyOf(viewers.keySet())) removePlayer(p);
    }

    public String getTitle() { return title; }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
        for (NetworkHandler p : viewers.keySet()) {
            if (p.ctx != null) {
                p.sendPacket(p.ctx, 0x09, pb -> {
                    pb.writeUUID(id);
                    pb.writeVarInt(3); // update title
                    pb.writeAnonymousNbt(NbtMap.builder().putString("text", this.title).build());
                });
            }
        }
    }

    public float getProgress() { return progress; }

    public void setProgress(float progress) {
        this.progress = clamp(progress);
        for (NetworkHandler p : viewers.keySet()) {
            if (p.ctx != null) {
                p.sendPacket(p.ctx, 0x09, pb -> {
                    pb.writeUUID(id);
                    pb.writeVarInt(2); // update health
                    pb.writeFloat(this.progress);
                });
            }
        }
    }

    public void setColor(Color color) {
        if (color != null) this.color = color;
        sendStyle();
    }

    public void setStyle(Style style) {
        if (style != null) this.style = style;
        sendStyle();
    }

    private void sendStyle() {
        for (NetworkHandler p : viewers.keySet()) {
            if (p.ctx != null) {
                p.sendPacket(p.ctx, 0x09, pb -> {
                    pb.writeUUID(id);
                    pb.writeVarInt(4); // update style
                    pb.writeVarInt(color.ordinal());
                    pb.writeVarInt(style.ordinal());
                });
            }
        }
    }

    private void sendAdd(NetworkHandler player) {
        player.sendPacket(player.ctx, 0x09, pb -> {
            pb.writeUUID(id);
            pb.writeVarInt(0); // add
            pb.writeAnonymousNbt(NbtMap.builder().putString("text", title).build());
            pb.writeFloat(progress);
            pb.writeVarInt(color.ordinal());
            pb.writeVarInt(style.ordinal());
            pb.writeByte(0); // flags: no darken sky / no fog
        });
    }
}
