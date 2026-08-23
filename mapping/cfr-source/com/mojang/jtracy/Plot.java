/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.jtracy;

import com.mojang.jtracy.TracyBindings;

public class Plot {
    static final Plot UNAVAILABLE = new Plot(0L);
    private final long handle;

    Plot(long l) {
        this.handle = l;
    }

    public void setValue(double d) {
        if (this != UNAVAILABLE) {
            TracyBindings.plotValue(this.handle, d);
        }
    }
}

