package com.CharunCore.server.worldgen.noisechunk;

/**
 * 原版 net.minecraft.world.level.levelgen.blending.Blender 存根。
 * MVP 阶段 blender 为空，alpha=1.0, offset=0.0。
 */
public final class Blender {
    private static final Blender EMPTY = new Blender(true);

    private final boolean empty;

    private Blender(boolean empty) { this.empty = empty; }

    public static Blender empty() { return EMPTY; }

    public boolean isEmpty() { return empty; }

    public BlendingOutput blendOffsetAndFactor(int x, int z) {
        return new BlendingOutput(1.0, 0.0);
    }

    /** 原版 net.minecraft.world.level.levelgen.blending.Blender$BlendingOutput */
    public record BlendingOutput(double alpha, double blendingOffset) {}
}
