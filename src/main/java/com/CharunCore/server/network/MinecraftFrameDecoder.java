package com.CharunCore.server.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

public class MinecraftFrameDecoder extends ByteToMessageDecoder {
    // 最大包长度限制 (Minecraft 协议限制)
    private static final int MAX_PACKET_LENGTH = 2_097_151;
    private static final int MAX_SIZE_BYTES = 5;

    private volatile int compressionThreshold = -1;

    public void enableCompression(int threshold) {
        this.compressionThreshold = threshold;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        in.markReaderIndex();
        int length = readVarInt(in);
        if (length == -1) {
            in.resetReaderIndex();
            return;
        }
        if (length < 0) {
            ctx.close(); // Negative length, malformed packet
            return;
        }
        if (length > MAX_PACKET_LENGTH) {
            ctx.close(); // Too large packet, potential DoS
            return;
        }
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        ByteBuf frame = in.readRetainedSlice(length);
        if (compressionThreshold < 0) {
            out.add(frame);
            return;
        }
        try {
            int dataLen = readFullVarInt(frame);
            if (dataLen < 0) {
                frame.release();
                ctx.close();
                return;
            }
            if (dataLen > MAX_PACKET_LENGTH) {
                frame.release();
                ctx.close(); // Badly compressed packet, potential zip bomb
                return;
            }
            if (dataLen == 0) {
                out.add(frame);
                return;
            }
            ByteBuf decompressed = Compression.decompress(frame, dataLen);
            frame.release();
            out.add(decompressed);
        } catch (Exception e) {
            frame.release();
            System.err.println("[网络] 解压失败, 关闭连接: " + e.getMessage());
            ctx.close();
        }
    }

    /**
     * Reads a VarInt from the ByteBuf and returns its value, or -1 if not enough bytes.
     * Throws RuntimeException if the VarInt is too big (> 5 bytes).
     */
    private static int readVarInt(ByteBuf buf) {
        int value = 0;
        int size = 0;
        while (buf.isReadable()) {
            byte b = buf.readByte();
            value |= (b & 127) << (size++ * 7);
            if ((b & 128) != 128) return value;
            if (size > MAX_SIZE_BYTES) throw new RuntimeException("Length VarInt too big");
        }
        return -1; // Not enough bytes
    }

    private static int readFullVarInt(ByteBuf buf) {
        int value = 0;
        int size = 0;
        while (buf.isReadable()) {
            byte b = buf.readByte();
            value |= (b & 127) << (size++ * 7);
            if ((b & 128) != 128) return value;
            if (size > MAX_SIZE_BYTES) return -1;
        }
        return -1;
    }
}
