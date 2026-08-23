/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.gizmos;

import net.minecraft.util.ARGB;

public record GizmoStyle(int stroke, float strokeWidth, int fill) {
    private static final float DEFAULT_WIDTH = 2.5f;

    public static GizmoStyle stroke(int n) {
        return new GizmoStyle(n, 2.5f, 0);
    }

    public static GizmoStyle stroke(int n, float f) {
        return new GizmoStyle(n, f, 0);
    }

    public static GizmoStyle fill(int n) {
        return new GizmoStyle(0, 0.0f, n);
    }

    public static GizmoStyle strokeAndFill(int n, float f, int n2) {
        return new GizmoStyle(n, f, n2);
    }

    public boolean hasFill() {
        return this.fill != 0;
    }

    public boolean hasStroke() {
        return this.stroke != 0 && this.strokeWidth > 0.0f;
    }

    public int multipliedStroke(float f) {
        return ARGB.multiplyAlpha(this.stroke, f);
    }

    public int multipliedFill(float f) {
        return ARGB.multiplyAlpha(this.fill, f);
    }
}

