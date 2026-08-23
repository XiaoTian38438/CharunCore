/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap
 *  it.unimi.dsi.fastutil.ints.Int2ObjectMap
 *  it.unimi.dsi.fastutil.objects.Object2IntArrayMap
 *  it.unimi.dsi.fastutil.objects.Object2IntMap
 */
package com.mojang.serialization;

import com.mojang.serialization.DynamicOps;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.stream.Stream;

public final class KeyCompressor<T> {
    private final Int2ObjectMap<T> decompress = new Int2ObjectArrayMap();
    private final Object2IntMap<T> compress = new Object2IntArrayMap();
    private final Object2IntMap<String> compressString = new Object2IntArrayMap();
    private final int size;
    private final DynamicOps<T> ops;

    public KeyCompressor(DynamicOps<T> dynamicOps, Stream<T> stream) {
        this.ops = dynamicOps;
        this.compressString.defaultReturnValue(-1);
        stream.forEach(object -> {
            if (this.compress.containsKey(object)) {
                return;
            }
            int n = this.compress.size();
            this.compress.put(object, n);
            dynamicOps.getStringValue(object).result().ifPresent(string -> this.compressString.put(string, n));
            this.decompress.put(n, object);
        });
        this.size = this.compress.size();
    }

    public T decompress(int n) {
        return (T)this.decompress.get(n);
    }

    public int compress(String string) {
        int n = this.compressString.getInt((Object)string);
        return n == -1 ? this.compress(this.ops.createString(string)) : n;
    }

    public int compress(T t) {
        return this.compress.getInt(t);
    }

    public int size() {
        return this.size;
    }
}

