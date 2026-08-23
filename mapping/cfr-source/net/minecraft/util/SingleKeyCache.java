/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.util;

import java.util.Objects;
import java.util.function.Function;
import org.jspecify.annotations.Nullable;

public class SingleKeyCache<K, V> {
    private final Function<K, V> computeValue;
    private @Nullable K cacheKey = null;
    private @Nullable V cachedValue;

    public SingleKeyCache(Function<K, V> function) {
        this.computeValue = function;
    }

    public V getValue(K k) {
        if (this.cachedValue == null || !Objects.equals(this.cacheKey, k)) {
            this.cachedValue = this.computeValue.apply(k);
            this.cacheKey = k;
        }
        return this.cachedValue;
    }
}

