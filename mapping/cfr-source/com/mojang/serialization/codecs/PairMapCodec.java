/*
 * Decompiled with CFR 0.152.
 */
package com.mojang.serialization.codecs;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.Objects;
import java.util.stream.Stream;

public final class PairMapCodec<F, S>
extends MapCodec<Pair<F, S>> {
    private final MapCodec<F> first;
    private final MapCodec<S> second;

    public PairMapCodec(MapCodec<F> mapCodec, MapCodec<S> mapCodec2) {
        this.first = mapCodec;
        this.second = mapCodec2;
    }

    @Override
    public <T> DataResult<Pair<F, S>> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
        return this.first.decode(dynamicOps, mapLike).flatMap((? super R object) -> this.second.decode(dynamicOps, mapLike).map((? super R object2) -> Pair.of(object, object2)));
    }

    @Override
    public <T> RecordBuilder<T> encode(Pair<F, S> pair, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
        return this.first.encode(pair.getFirst(), dynamicOps, this.second.encode(pair.getSecond(), dynamicOps, recordBuilder));
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        PairMapCodec pairMapCodec = (PairMapCodec)object;
        return Objects.equals(this.first, pairMapCodec.first) && Objects.equals(this.second, pairMapCodec.second);
    }

    public int hashCode() {
        return Objects.hash(this.first, this.second);
    }

    public String toString() {
        return "PairMapCodec[" + String.valueOf(this.first) + ", " + String.valueOf(this.second) + "]";
    }

    @Override
    public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
        return Stream.concat(this.first.keys(dynamicOps), this.second.keys(dynamicOps));
    }
}

