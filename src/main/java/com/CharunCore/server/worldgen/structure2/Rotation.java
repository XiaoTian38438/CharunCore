package com.CharunCore.server.worldgen.structure2;

import com.CharunCore.server.world.gen.RandomSource;

public enum Rotation {
    NONE, CLOCKWISE_90, CLOCKWISE_180, COUNTERCLOCKWISE_90;

    public Rotation getRotated(Rotation other) {
        return values()[(this.ordinal() + other.ordinal()) & 3];
    }

    public Rotation inverse() {
        switch (this) {
            case CLOCKWISE_90: return COUNTERCLOCKWISE_90;
            case COUNTERCLOCKWISE_90: return CLOCKWISE_90;
            default: return this;
        }
    }

    public int rotate(int x, int z, int sizeX, int sizeZ) {
        throw new UnsupportedOperationException();
    }

    public static Rotation random(RandomSource rng) {
        return values()[rng.nextInt(4)];
    }
}
