package com.CharunCore.server.plugin.api;

import com.CharunCore.server.network.NetworkHandler;
import com.CharunCore.server.network.protocol.PacketBuffer;
import com.CharunCore.server.world.DimensionType;

import java.util.function.Consumer;

/**
 * 底层协议包直发通道: 插件可向单个玩家/指定维度/全服发送任意 clientbound 包(774)。
 * 包 id 与字段布局参考 docs/PLUGIN_API.md 附录与 json/1.21.11/protocol.json。
 * 危险操作(格式错误会导致客户端断线), 请仅发送已知格式的包。
 */
public final class Packets {

    private Packets() {}

    /** 向单个玩家发送任意 clientbound 包。 */
    public static void send(Player player, int packetId, Consumer<PacketBuffer> writer) {
        NetworkHandler h = player.getHandle();
        if (h.ctx != null && h.ctx.channel().isActive()) {
            h.sendPacket(h.ctx, packetId, writer);
        }
    }

    /** 向指定维度的所有在线玩家广播。 */
    public static void broadcast(DimensionType dim, int packetId, Consumer<PacketBuffer> writer) {
        for (NetworkHandler h : NetworkHandler.players.values()) {
            if (h.ctx == null || !h.ctx.channel().isActive() || h.currentDim != dim) continue;
            h.sendPacket(h.ctx, packetId, writer);
        }
    }

    /** 向全服广播。 */
    public static void broadcastAll(int packetId, Consumer<PacketBuffer> writer) {
        for (NetworkHandler h : NetworkHandler.players.values()) {
            if (h.ctx == null || !h.ctx.channel().isActive()) continue;
            h.sendPacket(h.ctx, packetId, writer);
        }
    }

    // ── 常用底层操作快捷方法 ────────────────────────────────────

    /** 服务端强制刷新一个方块显示 (0x09 block_update)。 */
    public static void sendBlockChange(Player player, int x, int y, int z, int blockStateId) {
        send(player, 0x09, pb -> {
            pb.writeLong(((long) (x & 0x3FFFFFF) << 38) | ((long) (z & 0x3FFFFFF) << 12) | (y & 0xFFF));
            pb.writeVarInt(blockStateId);
        });
    }

    /** 服务端强制刷新一段方块(区块内, 0x0B section_blocks_update)。 */
    public static void sendMultiBlockChange(Player player, int chunkX, int chunkY, int chunkZ,
                                            long[] packedCoords, int[] states) {
        send(player, 0x0B, pb -> {
            pb.writeLong(((long) (chunkX & 0x3FFFFF) << 42) | ((long) (chunkZ & 0x3FFFFF) << 20) | (chunkY & 0xFFFFF));
            pb.writeVarInt(packedCoords.length);
            pb.writeVarInt(0); // array size占位(无 palette 直接值数组)
            for (int i = 0; i < packedCoords.length; i++) {
                pb.writeVarInt((int) (packedCoords[i] >> 12));
                pb.writeVarInt(states[i]);
            }
        });
    }

    /** 更新单个实体元数据(0x61)。index/type/value 见 EntityDataSerializers 注册表。 */
    public static void sendEntityMetadata(Player player, int entityId,
                                          int index, int serializerId, Consumer<PacketBuffer> valueWriter) {
        send(player, 0x61, pb -> {
            pb.writeVarInt(entityId);
            pb.writeByte(index);
            pb.writeVarInt(serializerId);
            valueWriter.accept(pb);
            pb.writeByte(0xFF);
        });
    }

    /** 全服音效 (0x73 sound_effect, 按坐标)。 */
    public static void broadcastSound(DimensionType dim, double x, double y, double z,
                                      String soundName, float volume, float pitch) {
        NetworkHandler.broadcastSoundAt(dim, x, y, z, soundName, volume, pitch);
    }
}
