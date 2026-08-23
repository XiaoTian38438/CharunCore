/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix3f
 *  org.joml.Matrix3fc
 *  org.joml.Vector3f
 *  org.joml.Vector3i
 */
package com.mojang.math;

import java.util.Arrays;
import net.minecraft.core.Direction;
import net.minecraft.util.Util;
import org.joml.Matrix3f;
import org.joml.Matrix3fc;
import org.joml.Vector3f;
import org.joml.Vector3i;

public enum SymmetricGroup3 {
    P123(0, 1, 2),
    P213(1, 0, 2),
    P132(0, 2, 1),
    P312(2, 0, 1),
    P231(1, 2, 0),
    P321(2, 1, 0);

    private final int p0;
    private final int p1;
    private final int p2;
    private final Matrix3fc transformation;
    private static final SymmetricGroup3[][] CAYLEY_TABLE;
    private static final SymmetricGroup3[] INVERSE_TABLE;

    private SymmetricGroup3(int n2, int n3, int n4) {
        this.p0 = n2;
        this.p1 = n3;
        this.p2 = n4;
        this.transformation = new Matrix3f().zero().set(this.permute(0), 0, 1.0f).set(this.permute(1), 1, 1.0f).set(this.permute(2), 2, 1.0f);
    }

    public SymmetricGroup3 compose(SymmetricGroup3 symmetricGroup3) {
        return CAYLEY_TABLE[this.ordinal()][symmetricGroup3.ordinal()];
    }

    public SymmetricGroup3 inverse() {
        return INVERSE_TABLE[this.ordinal()];
    }

    public int permute(int n) {
        return switch (n) {
            case 0 -> this.p0;
            case 1 -> this.p1;
            case 2 -> this.p2;
            default -> throw new IllegalArgumentException("Must be 0, 1 or 2, but got " + n);
        };
    }

    public Direction.Axis permuteAxis(Direction.Axis axis) {
        return Direction.Axis.VALUES[this.permute(axis.ordinal())];
    }

    public Vector3f permuteVector(Vector3f vector3f) {
        float f = vector3f.get(this.p0);
        float f2 = vector3f.get(this.p1);
        float f3 = vector3f.get(this.p2);
        return vector3f.set(f, f2, f3);
    }

    public Vector3i permuteVector(Vector3i vector3i) {
        int n = vector3i.get(this.p0);
        int n2 = vector3i.get(this.p1);
        int n3 = vector3i.get(this.p2);
        return vector3i.set(n, n2, n3);
    }

    public Matrix3fc transformation() {
        return this.transformation;
    }

    static {
        CAYLEY_TABLE = Util.make(() -> {
            SymmetricGroup3[] symmetricGroup3Array = SymmetricGroup3.values();
            SymmetricGroup3[][] symmetricGroup3Array2 = new SymmetricGroup3[symmetricGroup3Array.length][symmetricGroup3Array.length];
            for (SymmetricGroup3 symmetricGroup32 : symmetricGroup3Array) {
                for (SymmetricGroup3 symmetricGroup33 : symmetricGroup3Array) {
                    SymmetricGroup3 symmetricGroup34;
                    int n = symmetricGroup32.permute(symmetricGroup33.p0);
                    int n2 = symmetricGroup32.permute(symmetricGroup33.p1);
                    int n3 = symmetricGroup32.permute(symmetricGroup33.p2);
                    symmetricGroup3Array2[symmetricGroup32.ordinal()][symmetricGroup33.ordinal()] = symmetricGroup34 = Arrays.stream(symmetricGroup3Array).filter(symmetricGroup3 -> symmetricGroup3.p0 == n && symmetricGroup3.p1 == n2 && symmetricGroup3.p2 == n3).findFirst().get();
                }
            }
            return symmetricGroup3Array2;
        });
        INVERSE_TABLE = Util.make(() -> {
            SymmetricGroup3[] symmetricGroup3Array = SymmetricGroup3.values();
            return (SymmetricGroup3[])Arrays.stream(symmetricGroup3Array).map(symmetricGroup3 -> Arrays.stream(SymmetricGroup3.values()).filter(symmetricGroup32 -> symmetricGroup3.compose((SymmetricGroup3)((Object)((Object)((Object)symmetricGroup32)))) == P123).findAny().get()).toArray(SymmetricGroup3[]::new);
        });
    }
}

