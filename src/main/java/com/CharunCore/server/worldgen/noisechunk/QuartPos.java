package com.CharunCore.server.worldgen.noisechunk;

/**
 * 原版 net.minecraft.core.QuartPos — 生物群系/噪声坐标的 1/4 缩放层。
 * block 坐标 -> quart: >> 2 (除 4)
 * quart -> block: << 2 (乘 4)
 */
public final class QuartPos {
    private QuartPos() {}

    public static final int QUART_SIZE = 4;

    public static int fromBlock(int block) {
        return block >> 2;
    }

    public static int toBlock(int quart) {
        return quart << 2;
    }
}
