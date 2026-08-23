/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 */
package com.mojang.serialization.codecs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import com.mojang.serialization.RecordBuilder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public final class CompoundListCodec<K, V>
implements Codec<List<Pair<K, V>>> {
    private final Codec<K> keyCodec;
    private final Codec<V> elementCodec;

    public CompoundListCodec(Codec<K> codec, Codec<V> codec2) {
        this.keyCodec = codec;
        this.elementCodec = codec2;
    }

    @Override
    public <T> DataResult<Pair<List<Pair<K, V>>, T>> decode(DynamicOps<T> dynamicOps, T t) {
        return dynamicOps.getMapEntries(t).flatMap((? super R consumer) -> {
            ImmutableList.Builder builder = ImmutableList.builder();
            ImmutableMap.Builder builder2 = ImmutableMap.builder();
            AtomicReference<DataResult<Unit>> atomicReference = new AtomicReference<DataResult<Unit>>(DataResult.success(Unit.INSTANCE, Lifecycle.experimental()));
            consumer.accept((object, object2) -> {
                DataResult dataResult = this.keyCodec.parse(dynamicOps, object);
                DataResult dataResult2 = this.elementCodec.parse(dynamicOps, object2);
                DataResult<Pair> dataResult3 = dataResult.apply2stable(Pair::new, dataResult2);
                dataResult3.error().ifPresent(error -> builder2.put(object, object2));
                atomicReference.setPlain(((DataResult)atomicReference.getPlain()).apply2stable((unit, pair) -> {
                    builder.add(pair);
                    return unit;
                }, dataResult3));
            });
            ImmutableList immutableList = builder.build();
            Object t = dynamicOps.createMap((Map)builder2.build());
            Pair pair = Pair.of(immutableList, t);
            return atomicReference.getPlain().map((? super R unit) -> pair).setPartial(pair);
        });
    }

    @Override
    public <T> DataResult<T> encode(List<Pair<K, V>> list, DynamicOps<T> dynamicOps, T t) {
        RecordBuilder<T> recordBuilder = dynamicOps.mapBuilder();
        for (Pair<K, V> pair : list) {
            recordBuilder.add(this.keyCodec.encodeStart(dynamicOps, pair.getFirst()), this.elementCodec.encodeStart(dynamicOps, pair.getSecond()));
        }
        return recordBuilder.build(t);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        CompoundListCodec compoundListCodec = (CompoundListCodec)object;
        return Objects.equals(this.keyCodec, compoundListCodec.keyCodec) && Objects.equals(this.elementCodec, compoundListCodec.elementCodec);
    }

    public int hashCode() {
        return Objects.hash(this.keyCodec, this.elementCodec);
    }

    public String toString() {
        return "CompoundListCodec[" + String.valueOf(this.keyCodec) + " -> " + String.valueOf(this.elementCodec) + "]";
    }
}

