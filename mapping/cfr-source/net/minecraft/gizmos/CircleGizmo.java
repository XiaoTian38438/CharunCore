/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.gizmos;

import net.minecraft.gizmos.Gizmo;
import net.minecraft.gizmos.GizmoPrimitives;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.world.phys.Vec3;

public record CircleGizmo(Vec3 pos, float radius, GizmoStyle style) implements Gizmo
{
    private static final int CIRCLE_VERTICES = 20;
    private static final float SEGMENT_SIZE_RADIANS = 0.31415927f;

    @Override
    public void emit(GizmoPrimitives gizmoPrimitives, float f) {
        int n;
        if (!this.style.hasStroke() && !this.style.hasFill()) {
            return;
        }
        Vec3[] vec3Array = new Vec3[21];
        for (n = 0; n < 20; ++n) {
            Vec3 vec3;
            float f2 = (float)n * 0.31415927f;
            vec3Array[n] = vec3 = this.pos.add((float)((double)this.radius * Math.cos(f2)), 0.0, (float)((double)this.radius * Math.sin(f2)));
        }
        vec3Array[20] = vec3Array[0];
        if (this.style.hasFill()) {
            n = this.style.multipliedFill(f);
            gizmoPrimitives.addTriangleFan(vec3Array, n);
        }
        if (this.style.hasStroke()) {
            n = this.style.multipliedStroke(f);
            for (int i = 0; i < 20; ++i) {
                gizmoPrimitives.addLine(vec3Array[i], vec3Array[i + 1], n, this.style.strokeWidth());
            }
        }
    }
}

