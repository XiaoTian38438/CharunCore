/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonParseException
 */
package com.mojang.math;

import com.google.gson.JsonParseException;
import com.mojang.math.OctahedralGroup;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.util.Mth;

public enum Quadrant {
    R0(0, OctahedralGroup.IDENTITY, OctahedralGroup.IDENTITY, OctahedralGroup.IDENTITY),
    R90(1, OctahedralGroup.BLOCK_ROT_X_90, OctahedralGroup.BLOCK_ROT_Y_90, OctahedralGroup.BLOCK_ROT_Z_90),
    R180(2, OctahedralGroup.BLOCK_ROT_X_180, OctahedralGroup.BLOCK_ROT_Y_180, OctahedralGroup.BLOCK_ROT_Z_180),
    R270(3, OctahedralGroup.BLOCK_ROT_X_270, OctahedralGroup.BLOCK_ROT_Y_270, OctahedralGroup.BLOCK_ROT_Z_270);

    public static final Codec<Quadrant> CODEC;
    public final int shift;
    public final OctahedralGroup rotationX;
    public final OctahedralGroup rotationY;
    public final OctahedralGroup rotationZ;

    private Quadrant(int n2, OctahedralGroup octahedralGroup, OctahedralGroup octahedralGroup2, OctahedralGroup octahedralGroup3) {
        this.shift = n2;
        this.rotationX = octahedralGroup;
        this.rotationY = octahedralGroup2;
        this.rotationZ = octahedralGroup3;
    }

    @Deprecated
    public static Quadrant parseJson(int n) {
        return switch (Mth.positiveModulo(n, 360)) {
            case 0 -> R0;
            case 90 -> R90;
            case 180 -> R180;
            case 270 -> R270;
            default -> throw new JsonParseException("Invalid rotation " + n + " found, only 0/90/180/270 allowed");
        };
    }

    public static OctahedralGroup fromXYAngles(Quadrant quadrant, Quadrant quadrant2) {
        return quadrant2.rotationY.compose(quadrant.rotationX);
    }

    public static OctahedralGroup fromXYZAngles(Quadrant quadrant, Quadrant quadrant2, Quadrant quadrant3) {
        return quadrant3.rotationZ.compose(quadrant2.rotationY.compose(quadrant.rotationX));
    }

    public int rotateVertexIndex(int n) {
        return (n + this.shift) % 4;
    }

    static {
        CODEC = Codec.INT.comapFlatMap(n -> switch (Mth.positiveModulo(n, 360)) {
            case 0 -> DataResult.success(R0);
            case 90 -> DataResult.success(R90);
            case 180 -> DataResult.success(R180);
            case 270 -> DataResult.success(R270);
            default -> DataResult.error(() -> "Invalid rotation " + n + " found, only 0/90/180/270 allowed");
        }, quadrant -> switch (quadrant.ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> 0;
            case 1 -> 90;
            case 2 -> 180;
            case 3 -> 270;
        });
    }
}

