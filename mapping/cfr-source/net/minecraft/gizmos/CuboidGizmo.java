/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.gizmos;

import net.minecraft.gizmos.Gizmo;
import net.minecraft.gizmos.GizmoPrimitives;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record CuboidGizmo(AABB aabb, GizmoStyle style, boolean coloredCornerStroke) implements Gizmo
{
    @Override
    public void emit(GizmoPrimitives gizmoPrimitives, float f) {
        int n;
        double d = this.aabb.minX;
        double d2 = this.aabb.minY;
        double d3 = this.aabb.minZ;
        double d4 = this.aabb.maxX;
        double d5 = this.aabb.maxY;
        double d6 = this.aabb.maxZ;
        if (this.style.hasFill()) {
            n = this.style.multipliedFill(f);
            gizmoPrimitives.addQuad(new Vec3(d4, d2, d3), new Vec3(d4, d5, d3), new Vec3(d4, d5, d6), new Vec3(d4, d2, d6), n);
            gizmoPrimitives.addQuad(new Vec3(d, d2, d3), new Vec3(d, d2, d6), new Vec3(d, d5, d6), new Vec3(d, d5, d3), n);
            gizmoPrimitives.addQuad(new Vec3(d, d2, d3), new Vec3(d, d5, d3), new Vec3(d4, d5, d3), new Vec3(d4, d2, d3), n);
            gizmoPrimitives.addQuad(new Vec3(d, d2, d6), new Vec3(d4, d2, d6), new Vec3(d4, d5, d6), new Vec3(d, d5, d6), n);
            gizmoPrimitives.addQuad(new Vec3(d, d5, d3), new Vec3(d, d5, d6), new Vec3(d4, d5, d6), new Vec3(d4, d5, d3), n);
            gizmoPrimitives.addQuad(new Vec3(d, d2, d3), new Vec3(d4, d2, d3), new Vec3(d4, d2, d6), new Vec3(d, d2, d6), n);
        }
        if (this.style.hasStroke()) {
            n = this.style.multipliedStroke(f);
            gizmoPrimitives.addLine(new Vec3(d, d2, d3), new Vec3(d4, d2, d3), this.coloredCornerStroke ? ARGB.multiply(n, -34953) : n, this.style.strokeWidth());
            gizmoPrimitives.addLine(new Vec3(d, d2, d3), new Vec3(d, d5, d3), this.coloredCornerStroke ? ARGB.multiply(n, -8913033) : n, this.style.strokeWidth());
            gizmoPrimitives.addLine(new Vec3(d, d2, d3), new Vec3(d, d2, d6), this.coloredCornerStroke ? ARGB.multiply(n, -8947713) : n, this.style.strokeWidth());
            gizmoPrimitives.addLine(new Vec3(d4, d2, d3), new Vec3(d4, d5, d3), n, this.style.strokeWidth());
            gizmoPrimitives.addLine(new Vec3(d4, d5, d3), new Vec3(d, d5, d3), n, this.style.strokeWidth());
            gizmoPrimitives.addLine(new Vec3(d, d5, d3), new Vec3(d, d5, d6), n, this.style.strokeWidth());
            gizmoPrimitives.addLine(new Vec3(d, d5, d6), new Vec3(d, d2, d6), n, this.style.strokeWidth());
            gizmoPrimitives.addLine(new Vec3(d, d2, d6), new Vec3(d4, d2, d6), n, this.style.strokeWidth());
            gizmoPrimitives.addLine(new Vec3(d4, d2, d6), new Vec3(d4, d2, d3), n, this.style.strokeWidth());
            gizmoPrimitives.addLine(new Vec3(d, d5, d6), new Vec3(d4, d5, d6), n, this.style.strokeWidth());
            gizmoPrimitives.addLine(new Vec3(d4, d2, d6), new Vec3(d4, d5, d6), n, this.style.strokeWidth());
            gizmoPrimitives.addLine(new Vec3(d4, d5, d3), new Vec3(d4, d5, d6), n, this.style.strokeWidth());
        }
    }
}

