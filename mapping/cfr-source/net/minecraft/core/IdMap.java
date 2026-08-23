/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.core;

import org.jspecify.annotations.Nullable;

public interface IdMap<T>
extends Iterable<T> {
    public static final int DEFAULT = -1;

    public int getId(T var1);

    public @Nullable T byId(int var1);

    default public T byIdOrThrow(int n) {
        T t = this.byId(n);
        if (t == null) {
            throw new IllegalArgumentException("No value with id " + n);
        }
        return t;
    }

    default public int getIdOrThrow(T t) {
        int n = this.getId(t);
        if (n == -1) {
            throw new IllegalArgumentException("Can't find id for '" + String.valueOf(t) + "' in map " + String.valueOf(this));
        }
        return n;
    }

    public int size();
}

