package com.CharunCore.server.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MinecraftCipher extends ChannelDuplexHandler {

    private final Cipher decryptCipher;
    private final Cipher encryptCipher;

    public MinecraftCipher(byte[] secret) {
        this.decryptCipher = createCipher(secret);
        this.encryptCipher = createCipher(secret);
    }

    private static Cipher createCipher(byte[] secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(secret, "AES"), new IvParameterSpec(secret));
            return cipher;
        } catch (Exception e) {
            throw new IllegalStateException("无法初始化 AES-CFB8 密码器", e);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ByteBuf buf)) {
            super.channelRead(ctx, msg);
            return;
        }
        try {
            if (!buf.isReadable()) return;
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            ctx.fireChannelRead(io.netty.buffer.Unpooled.wrappedBuffer(decryptCipher.update(data)));
        } finally {
            buf.release();
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ByteBuf buf)) {
            super.write(ctx, msg, promise);
            return;
        }
        try {
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            super.write(ctx, io.netty.buffer.Unpooled.wrappedBuffer(encryptCipher.update(data)), promise);
        } finally {
            buf.release();
        }
    }
}
