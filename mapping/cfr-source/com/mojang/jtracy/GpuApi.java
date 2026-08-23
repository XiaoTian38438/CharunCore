/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.jtracy;

public enum GpuApi {
    INVALID(0),
    OPENGL(1),
    VULKAN(2),
    OPENCL(3),
    DIRECT3D_12(4),
    DIRECT3D_11(5);

    private final int id;

    private GpuApi(int n2) {
        this.id = n2;
    }

    int getId() {
        return this.id;
    }
}

