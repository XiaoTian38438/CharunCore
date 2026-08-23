/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap
 */
package com.mojang.serialization;

import com.mojang.serialization.Compressable;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.KeyCompressor;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Map;

public abstract class CompressorHolder
implements Compressable {
    private final Map<DynamicOps<?>, KeyCompressor<?>> compressors = new Object2ObjectArrayMap();

    @Override
    public <T> KeyCompressor<T> compressor(DynamicOps<T> dynamicOps) {
        return this.compressors.computeIfAbsent(dynamicOps, dynamicOps2 -> new KeyCompressor(dynamicOps, this.keys(dynamicOps)));
    }
}

