/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.jtracy;

import com.mojang.jtracy.TracyBindings;

public class Zone
implements AutoCloseable {
    static final Zone UNAVAILABLE = new Zone(0);
    private final int id;

    Zone(int n) {
        this.id = n;
    }

    public Zone addText(String string) {
        if (this != UNAVAILABLE) {
            TracyBindings.addZoneText(this.id, string);
        }
        return this;
    }

    public Zone setColor(int n) {
        if (this != UNAVAILABLE) {
            TracyBindings.setZoneColor(this.id, n);
        }
        return this;
    }

    public Zone addValue(long l) {
        if (this != UNAVAILABLE) {
            TracyBindings.addZoneValue(this.id, l);
        }
        return this;
    }

    @Override
    public void close() {
        if (this != UNAVAILABLE) {
            TracyBindings.endZone(this.id);
        }
    }
}

