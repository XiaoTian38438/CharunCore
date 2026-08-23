/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.util;

import org.jspecify.annotations.Nullable;

public class ExceptionCollector<T extends Throwable> {
    private @Nullable T result;

    public void add(T t) {
        if (this.result == null) {
            this.result = t;
        } else {
            ((Throwable)this.result).addSuppressed((Throwable)t);
        }
    }

    public void throwIfPresent() throws T {
        if (this.result != null) {
            throw this.result;
        }
    }
}

