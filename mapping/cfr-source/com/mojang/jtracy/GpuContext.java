/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.jtracy;

import com.mojang.jtracy.TracyBindings;

public class GpuContext {
    static final GpuContext UNAVAILABLE = new GpuContext(0);
    private final int id;

    GpuContext(int n) {
        this.id = n;
    }

    public GpuContext setName(String string) {
        if (this != UNAVAILABLE) {
            TracyBindings.setGpuContextName(this.id, string);
        }
        return this;
    }

    public void beginZone(int n, String string, String string2, String string3, int n2) {
        if (this != UNAVAILABLE) {
            TracyBindings.beginGpuZone(this.id, n, string, string2, string3, n2);
        }
    }

    public void endZone(int n) {
        if (this != UNAVAILABLE) {
            TracyBindings.endGpuZone(this.id, n);
        }
    }

    public void submitQueryTimestamp(int n, long l) {
        if (this != UNAVAILABLE) {
            TracyBindings.submitQueryTimestamp(this.id, n, l);
        }
    }
}

