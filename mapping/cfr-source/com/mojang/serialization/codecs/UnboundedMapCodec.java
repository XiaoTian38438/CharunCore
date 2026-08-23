/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization.codecs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.codecs.BaseMapCodec;
import java.util.Map;

public record UnboundedMapCodec<K, V>(Codec<K> keyCodec, Codec<V> elementCodec) implements BaseMapCodec<K, V>,
Codec<Map<K, V>>
{
    @Override
    public <T> DataResult<Pair<Map<K, V>, T>> decode(DynamicOps<T> dynamicOps, T t) {
        return dynamicOps.getMap(t).setLifecycle(Lifecycle.stable()).flatMap((? super R mapLike) -> this.decode(dynamicOps, (Object)mapLike)).map((? super R map) -> Pair.of(map, t));
    }

    @Override
    public <T> DataResult<T> encode(Map<K, V> map, DynamicOps<T> dynamicOps, T t) {
        return this.encode(map, dynamicOps, dynamicOps.mapBuilder()).build(t);
    }

    @Override
    public String toString() {
        return "UnboundedMapCodec[" + String.valueOf(this.keyCodec) + " -> " + String.valueOf(this.elementCodec) + "]";
    }
}

