/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.jtracy;

import com.mojang.jtracy.TracyBindings;

public class MemoryPool {
    static final MemoryPool UNAVAILABLE = new MemoryPool(0L);
    private final long id;

    MemoryPool(long l) {
        this.id = l;
    }

    public void malloc(long l, int n) {
        if (this != UNAVAILABLE) {
            TracyBindings.mallocNamed(this.id, l, n);
        }
    }

    public void free(long l) {
        if (this != UNAVAILABLE) {
            TracyBindings.freeNamed(this.id, l);
        }
    }
}

